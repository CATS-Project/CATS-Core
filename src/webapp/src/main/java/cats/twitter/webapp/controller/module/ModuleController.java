package cats.twitter.webapp.controller.module;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import cats.twitter.model.*;
import cats.twitter.repository.SubCorpusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

import cats.twitter.repository.CorpusRepository;
import cats.twitter.repository.ModuleRepository;
import cats.twitter.repository.RequestRepository;
import cats.twitter.webapp.service.ModuleService;

/**
 * Controller of the request of treatments user-side.
 */
@Controller
@RequestMapping("/module")
@SessionAttributes("user")
public class ModuleController
{

	@Autowired
	ModuleRepository moduleRepository;
	@Autowired
	CorpusRepository corpusRepository;
	@Autowired
	SubCorpusRepository subCorpusRepository;
	@Autowired
	ModuleService service;
	@Autowired
	RequestRepository requestRepository;

	@RequestMapping(value = "/request-chained", method = RequestMethod.POST)
	public String createRequestChained(WebRequest webRequest, @RequestParam("corpusId") long id,
		@RequestParam("moduleId") String modulesIDS, @ModelAttribute User user)
	{
		Map<String, String[]> map = webRequest.getParameterMap();
		String[] modules = modulesIDS.split(",");
		Corpus corpus = corpusRepository.findOne(id);
		service.sendChain(modules,map,corpus);
		return "redirect:/corpus";
	}

	/**
	 * Controller who create the request
	 * 
	 * @param webRequest
	 *            request http to retrieve the parameter map
	 * @param idModule
	 *            id of the module
	 * @param id
	 *            id of the corpus which recieved the treatment
	 * @param user
	 *            The user currently connected
	 * @return redirection to the main corpus page
	 */
	@RequestMapping(value = "/request/{idModule}", method = RequestMethod.POST)
	public String createRequest(WebRequest webRequest, @PathVariable("idModule") int idModule,
		@RequestParam("corpusId") long id,@RequestParam("subcorpus") boolean subcorpus, @ModelAttribute User user)
	{
		Module module = moduleRepository.findOne(idModule);
		Map<String, String[]> map = webRequest.getParameterMap();
		if(subcorpus){
			SubCorpus subCorpus = subCorpusRepository.findById(id);
			service.send(module,flatten(map),subCorpus);
		}
		else{
			Corpus corpus = corpusRepository.findOne(id);
			service.send(module,flatten(map),corpus);
		}

		/*
		 * The webRequest returns a map of arrays, I know that modules parameter are singleton. So we call flatten() to
		 * transform Map<String,String[]> to Map<String,String>
		 */
		return "redirect:/corpus";
	}

	private Map<String, String> flatten(Map<String, String[]> map)
	{
		Map<String, String> params = new HashMap<>();
		map.entrySet().stream().filter(entry -> !entry.getKey().equals("corpusId") && !entry.getKey().equals("subcorpus")
			&& !entry.getKey().equals("moduleId")).forEach(entry -> params.put(entry.getKey(),entry.getValue()[0]));
		return params;
	}

	/**
	 * Delete a request.
	 * 
	 * @param idRequest
	 *            the request of the id concerned
	 * @param user
	 *            current user
	 * @throws IllegalAccessException
	 *             if a user try to delete a request which is not his own
	 */
	@RequestMapping(value = "/request/{idRequest}", method = RequestMethod.DELETE)
	@Transactional
	@ResponseStatus(HttpStatus.OK)
	public void removeResponse(@PathVariable("idRequest") long idRequest, @ModelAttribute User user)
		throws IllegalAccessException
	{
		Request request = requestRepository.findOne(idRequest);
		if (!request.getCorpus().getUser().equals(user))
			throw new IllegalAccessException();

		requestRepository.delete(idRequest);

	}

	@Transactional
	@RequestMapping(value = "/request/{idRequest}", method = RequestMethod.GET)
	public ModelAndView seeResponse(@PathVariable("idRequest") long idRequest, @ModelAttribute User user)
		throws IllegalAccessException
	{
		Request request = requestRepository.findOne(idRequest);
		if (!request.getCorpus().getUser().equals(user))
			throw new IllegalAccessException();
		ModelAndView modelAndView = new ModelAndView("response");
		modelAndView.addObject("response",request.getResult(Result.TypeRes.HTML));
		return modelAndView;
	}

	private static final int BUFFER_SIZE = 4096;

	/**
	 * When the response of a module is "data", the user is able to download a file
	 * 
	 * @param idRequest
	 *            the id of request concerned
	 * @param user
	 *            current user
	 * @param response
	 *            the HTTPResponse with the Output stream
	 * @throws IllegalAccessException
	 *             if a user try to download a response of a request which is not his own
	 */
	@Transactional
	@RequestMapping(value = "/requestFile/{idRequest}", method = RequestMethod.GET)
	public void dowloadResponse(@PathVariable("idRequest") long idRequest,
		@ModelAttribute User user,
		HttpServletResponse response) throws IllegalAccessException
	{
		Request request = requestRepository.findOne(idRequest);
		if (!request.getCorpus().getUser().equals(user))
			throw new IllegalAccessException();
		Result result = request.getResult(Result.TypeRes.FILE);
		try
		{

			File downloadFile = new File(result.getResult());
			FileInputStream inputStream = new FileInputStream(downloadFile);

			// set content attributes for the response
			response.setContentType("text/plain");
			response.setContentLength((int) downloadFile.length());

			// set headers for the response
			String headerKey = "Content-Disposition";
			String headerValue = String.format("attachment; filename=\"%s\"",
				downloadFile.getName());
			response.setHeader(headerKey,headerValue);

			// get output stream of the response
			OutputStream outStream = response.getOutputStream();

			byte[] buffer = new byte[BUFFER_SIZE];
			int bytesRead = -1;

			// write bytes read from the input stream into the output stream
			while ((bytesRead = inputStream.read(buffer)) != -1)
			{
				outStream.write(buffer,0,bytesRead);
			}

			inputStream.close();
			outStream.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

	}

	/**
	 * When an IllegalAccessException occurred it returned here, and draw an error page.
	 * 
	 * @return
	 */
	@ExceptionHandler(IllegalAccessError.class)
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	public String handleError()
	{
		return "error";
	}

}
