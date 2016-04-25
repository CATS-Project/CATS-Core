package cats.twitter.webapp.service;

import java.io.*;
import java.util.*;

import javax.transaction.Transactional;

import cats.twitter.collect.Collect;
import cats.twitter.model.*;
import cats.twitter.repository.CorpusRepository;
import cats.twitter.repository.TweetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import cats.twitter.repository.ModuleRepository;
import cats.twitter.repository.RequestRepository;
import cats.twitter.webapp.dto.Query;
import cats.twitter.webapp.dto.Result;
import cats.twitter.model.Result.TypeRes;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ModuleServiceImpl implements ModuleService
{
	@Autowired
	ModuleRepository repository;
	@Autowired
	RequestRepository reqRepository;

	@Autowired
	TweetRepository tweetRepo;
	@Autowired
	CorpusRepository corpusRepo;

	public ModuleServiceImpl() {
	}

	@Override
	@Transactional
	public Module register(Module module)
	{
		Module mod = repository.findByEndpoint(module.getEndpoint());
		if (mod == null) {
			mod = repository.save(module);
		}
		else {
			mod.set(module);
			mod = repository.save(mod);
		}
		return mod;
	}

	/**
	 * A user asked a new treatment. We need to contact the module.
	 * on URL : <endpoint>/init
	 * Body : {
	 *     "token":"O29090R9309", <- It's the key to access to the corpus and the key to return the response
	 *     "params":{
	 *         "param1":"toto", <- Params specified when a module register
	 *         "param2":true
	 *     }
	 * }
	 * @param module On what module the treatment will begin
	 * @param params Params to send to the module
	 * @param corpus On what corpus the treatment will begin
	 * @return the Query object sent to the module.
     */
	@Override
	public Query send(Module module, Map<String, String> params, Corpus corpus)
	{
		Query query = new Query();
		Request request = makeQuery(module, params, corpus, query);

		RestTemplate restTemplate = new RestTemplate();
		try {
			return restTemplate.postForObject(module.getEndpoint() + "/init", query, Query.class);
		}
		catch (RestClientException exception){
			cats.twitter.model.Result res = new cats.twitter.model.Result();
			res.setType(TypeRes.ERROR);
			res.setDate(new Date());
			res.setResult(exception.getMessage());
			request.addResult(res);
			reqRepository.save(request);
			return null;
		}
	}

    /**
     * A user asked a new treatment. We need to contact the module.
     * on URL : <endpoint>/init
     * Body : {
     *     "token":"O29090R9309", <- It's the key to access to the corpus and the key to return the response
     *     "params":{
     *         "param1":"toto", <- Params specified when a module register
     *         "param2":true
     *     }
     * }
     * @param module On what module the treatment will begin
     * @param params Params to send to the module
     * @param subcorpus On what subcorpus the treatment will begin
     * @return the Query object sent to the module.
     */
    @Override
    public Query send(Module module, Map<String, String> params, SubCorpus subcorpus)
    {
        Query query = new Query();
        Request request = makeQuery(module, params, subcorpus, query);

        RestTemplate restTemplate = new RestTemplate();
        try {
            return restTemplate.postForObject(module.getEndpoint() + "/init", query, Query.class);
        }
        catch (RestClientException exception){
            cats.twitter.model.Result res = new cats.twitter.model.Result();
            res.setType(TypeRes.ERROR);
            res.setDate(new Date());
            res.setResult(exception.getMessage());
            request.addResult(res);
            reqRepository.save(request);
            return null;
        }
    }


	private Map<String, String> flattenChain(Map<String, String[]> map, int moduleId)
	{
		Map<String, String> params = new HashMap<>();
		String prefix = "mod" + moduleId + ".";
		map.entrySet().stream().filter(entry -> !entry.getKey().equals("corpusId")
			&& !entry.getKey().equals("moduleId") && !entry.getKey().equals("subcorpus") && entry.getKey().startsWith(prefix))
			.forEach(entry -> params.put(entry.getKey().replace(prefix,""),entry.getValue()[0]));
		return params;
	}

    @org.springframework.transaction.annotation.Transactional(propagation = Propagation.REQUIRES_NEW)
    public Request recordRequest(String moduleId, Corpus corpus, long chainID,int i,Query firstQuery,Map<String,String[]> map,Query query) {
        Module module = repository.findOne(Integer.parseInt(moduleId));
        // TODO: add sendChain that sets Request attribute isChained to true
        // Also on moduleResult,check if request is chained and send the result to the next module
        // PS:for Request add nextRequests
        // think about when to call init in sendChai or here after i create all requests in DB
        // ps to verify if its first request , compare id to chaindId , first element has them equal
        Map<String, String> flatten = flattenChain(map, module.getId());
        Map<String, Object> params = changeType(module, flatten);

        Request request = new Request();
        request.setModule(module);
        request.setToken(query.getToken());
        request.setParams(flatten);
        request.setCorpus(corpus);
        request.setInitDate(new Date());
        request.setChained(true);
        if (chainID != -1) {
            request.setIdChain(chainID);
        }
        request = reqRepository.save(request);
        if (i == 0) {
            query.setParams(params);
            request.setIdChain(request.getId());
            request = reqRepository.save(request);
        }
        return request;
    }
        
        public void sendChain(String[] modules, Map<String, String[]> map, Corpus corpus)
            throws RestClientException {
        List<Request> requests = new LinkedList<Request>();
        Query firstQuery = null;
        long chainID = -1;
        for (int i = 0; i < modules.length; i++) {
            Query query = new Query();
            Request req = recordRequest(modules[i], corpus, chainID, i, firstQuery, map,query);
            if (i == 0) {
                chainID = req.getId();
                firstQuery = query;
            }
            requests.add(req);
        }
        for (int i = 0; i < requests.size(); i++) {
            if (i != 0) {
                requests.get(i).setPreviousRequest(requests.get(i - 1));
            }
            if (i != requests.size() - 1) {
                requests.get(i).setNextRequest(requests.get(i + 1));
            }
        }
        
            for (Request request : requests) {
                saveRequest(request);
            }

        RestTemplate restTemplate = new RestTemplate();
        if (requests.size() > 0) {
            Request firstRequest = requests.get(0);
            try {
                restTemplate.postForObject(firstRequest.getModule().getEndpoint() + "/init", firstQuery,
                        Query.class);
            } catch (RestClientException exception) {
                   recordError(exception, firstRequest);
            }
        }
    }
        
        @Transactional
        public void recordError(RestClientException exception,Request firstRequest){
            cats.twitter.model.Result res = new cats.twitter.model.Result();
				res.setType(TypeRes.ERROR);
				res.setDate(new Date());
				res.setResult(exception.getMessage());
				firstRequest.addResult(res);
				reqRepository.save(firstRequest);
        }

    /**
     * Wrapped function (Transactional(propagation = Propagation.REQUIRES_NEW)),
     * to make sure the newly created Request is fully committed and present in
     * the database.
     *
     * @param module On what module the treatment will begin
     * @param parames Params that will be sent to the module
     * @param corpus On what module the treatment will begin
     * @param query The object that will be sent to the module
     * @return the newly created request with the token.
     */
    @org.springframework.transaction.annotation.Transactional(propagation = Propagation.REQUIRES_NEW)
    private Request makeQuery(Module module, Map<String, String> parames, Corpus corpus, Query query) {
        Map<String, Object> params = changeType(module, parames);

        Request request = new Request();
        request.setModule(module);
        request.setToken(query.getToken());
        request.setParams(parames);
        request.setCorpus(corpus);
        request.setInitDate(new Date());
        request = reqRepository.save(request);
        reqRepository.flush();

        query.setParams(params);
        return request;
    }

    /**
     * Wrapped function (Transactional(propagation = Propagation.REQUIRES_NEW)),
     * to make sure the newly created Request is fully committed and present in
     * the database.
     *
     * @param module On what module the treatment will begin
     * @param parames Params that will be sent to the module
     * @param subcorpus On what module the treatment will begin
     * @param query The object that will be sent to the module
     * @return the newly created request with the token.
     */
    @org.springframework.transaction.annotation.Transactional(propagation = Propagation.REQUIRES_NEW)
    private Request makeQuery(Module module, Map<String, String> parames, SubCorpus subcorpus, Query query) {
        Map<String, Object> params = changeType(module, parames);

        Request request = new Request();
        request.setModule(module);
        request.setToken(query.getToken());
        request.setParams(parames);
        request.setSubCorpus(subcorpus);
        request.setInitDate(new Date());
        request = reqRepository.save(request);
        reqRepository.flush();

        query.setParams(params);
        return request;
    }

    /**
     * Create a new result and a CORPUS from a token and a tweet list.
     *
     * @param token the token sent by CATS in the /init call
     * @param tweets list of tweets to insert
     */
    @Override
    @Transactional
    public void postResultCorpus(String token, List<Tweet> tweets) {
        Optional<Request> req = reqRepository.findOneByToken(token);
        if (req.isPresent()) {
            Request request = req.get();
            Corpus corpus = new Corpus();
            corpus.setName(request.getCorpus().getName() + " " + request.getModule().getName());
            corpus.setUser(request.getCorpus().getUser());
            corpus.setCalculated(true);
            corpus.setFather(request.getCorpus());
            corpus.setState(Collect.State.SHUTDOWN);
            corpus = corpusRepo.save(corpus);

            for (Tweet tweet : tweets) {
                tweet.setCorpus(corpus);
                tweet.setId(null);
                tweetRepo.save(tweet);
            }

            cats.twitter.model.Result res = new cats.twitter.model.Result();
            res.setDate(new Date());
            res.setType(TypeRes.NONE);
            res.setResult(Long.toString(corpus.getId()));
            request.addResult(res);
            reqRepository.save(request);
        }
    }

    /**
     * Create a new result, it will store it like file. The user will be able to
     * download it.
     *
     * @param result the result to store.
     * @return The request associated to the result.
     */
    @Override
    @Transactional
    public Request postResultAsFile(Result result) {
        Optional<Request> req = reqRepository.findOneByToken(result.getToken());
        Request request = req.isPresent() ? req.get() : null;
        if (request != null) {
            try {
                String filename = System.getProperty("user.home") + File.separator + "results" + File.separator + "tto" + req.get().getId() + ".res";
                File file = new File(filename);
                if (!file.getParentFile().exists()) {
                    file.getParentFile().mkdirs();
                }
                if (!file.exists()) {
                    file.createNewFile();
                }
                OutputStream stream = new FileOutputStream(file);
                stream.write(result.getResult().getBytes());

                cats.twitter.model.Result res = new cats.twitter.model.Result();
                res.setDate(new Date());
                res.setType(TypeRes.FILE);
                res.setResult(filename);
                request.addResult(res);
                return reqRepository.save(request);

            } catch (IOException e) {
                cats.twitter.model.Result res = new cats.twitter.model.Result();
                res.setDate(new Date());
                res.setType(TypeRes.ERROR);
                res.setResult(e.getMessage());
                request.addResult(res);
                return reqRepository.save(request);
            }
        }
        return null;
    }

    private Map<String, Object> changeType(Module module, Map<String, String> flatten) {
        Map<String, Object> map = new HashMap<>();
        for (Params param : module.getParams()) {
            String value = flatten.get(param.getName());
            if (value != null) {
                switch (param.getType()) {
                    case "bool":
                        map.put(param.getName(), value != null && value.length() > 0);
                        break;
                    case "number":
                        map.put(param.getName(), Double.parseDouble(value));
                        break;
                    default:
                        map.put(param.getName(), value);
                        break;
                }
            } else if (param.getType().equals("bool")) {
                map.put(param.getName(), false);
            }
        }
        return map;
    }

    @Transactional
    public void initNextModule(Result result) {
        Optional<Request> req = reqRepository.findOneByToken(result.getToken());
        Request request = req.isPresent() ? req.get() : null;
        if (request != null && request.isChained() && request.getNextRequest() != null) {
            Request nextRequest = request.getNextRequest();
            Query query = new Query();
            Map<String, Object> params = changeType(nextRequest.getModule(), nextRequest.getParams());
            query.setParams(params);
            query.setToken(nextRequest.getToken());

            RestTemplate restTemplate = new RestTemplate();
            try {
                restTemplate.postForObject(nextRequest.getModule().getEndpoint() + "/init", query,
                        Query.class);
            } catch (RestClientException exception) {
                cats.twitter.model.Result res = new cats.twitter.model.Result();
                res.setType(TypeRes.ERROR);
                res.setDate(new Date());
                res.setResult(exception.getMessage());
                request.addResult(res);
                reqRepository.save(request);
            }
        }
    }

    /**
     * Store a result as HTML, it will be displayed as if to the user.
     *
     * @param result Object result
     * @return The request associated to the result.
     */
    @Override
    @Transactional
    public Request postResult(Result result) {
        Optional<Request> req = reqRepository.findOneByToken(result.getToken());
        Request request = req.isPresent() ? req.get() : null;
        if (request != null) {
            cats.twitter.model.Result res = new cats.twitter.model.Result();
            res.setDate(new Date());
            res.setType(TypeRes.HTML);
            res.setResult(result.getResult());
            request.addResult(res);
            return reqRepository.save(request);
        }
        return null;
    }

    @org.springframework.transaction.annotation.Transactional()
    private void saveRequest(Request request) {
        reqRepository.save(request);
    }
}
