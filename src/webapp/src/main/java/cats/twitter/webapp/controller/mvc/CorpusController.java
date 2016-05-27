package cats.twitter.webapp.controller.mvc;


import cats.twitter.collect.IManageCollect;
import cats.twitter.exceptions.NotFoundException;
import cats.twitter.model.Corpus;
import cats.twitter.model.SubCorpus;
import cats.twitter.model.User;
import cats.twitter.repository.*;

import cats.twitter.webapp.form.CorpusFormService;
import cats.twitter.webapp.service.CSVService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import twitter4j.Twitter;
import twitter4j.TwitterException;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.*;

import org.springframework.data.domain.PageRequest;

@Controller
@RequestMapping("/corpus")
@SessionAttributes({"twitter","user"})
public class CorpusController {
    private final Logger log = LoggerFactory.getLogger(CorpusController.class);
    @Autowired
    IManageCollect collect;
    @Autowired
    UserRepository repository;
    /**
     * @param principal Current authenticated user
     * @return the form of creation of a corpus
     */
    @Autowired
    CorpusRepository corpusRepository;
    @Autowired
    SubCorpusRepository subCorpusRepository;

    @Autowired
    TweetRepository tweetRepository;
    @Autowired
    CSVService service;
    @Autowired
    CorpusFormService formService;
    @Autowired
    ModuleRepository moduleRepository;


    /**
     * Display the dashboard
     * @param principal current user
     * @return view of the dashboard
     */
    @RequestMapping(method = RequestMethod.GET)
    @Transactional
    public ModelAndView pageAll(@ModelAttribute("user") User principal) {

        ModelAndView mv = new ModelAndView("corpus");
        List<Corpus> corpus = corpusRepository.findByUser(principal);
        for (Corpus c : corpus) c.lazyLoad(tweetRepository,false);
        mv.addObject("corpus",corpus);
        mv.addObject("modules",moduleRepository.findAll());

        return mv;
    }

    /**
     * Display the creation of corpus page.
     * @param principal current user
     * @return the view of creation of copus
     */
    @RequestMapping(method = RequestMethod.GET,value = "/create")
    public ModelAndView create(@ModelAttribute("user") User principal) {
        ModelAndView mv = new ModelAndView("createCorpus");
        mv.addObject("forms",formService.getFormList());
        return mv;
    }

    /**
     * @param authentication
     * @return
     */
    @ModelAttribute("user")
    public User getUserFromAuth(Authentication authentication){
        return (User) authentication.getPrincipal();
    }

    /**
     * @param id the id of the corpus asked
     * @return The view of a corpus
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @Transactional
    public ModelAndView pageAll(@PathVariable("id") Long id, @ModelAttribute User user) {
        ModelAndView model = new ModelAndView("detailCorpus");
        Corpus c = corpusRepository.findById(id);
        c.lazyLoad(tweetRepository,true);
        model.addObject("corpus",c);
        return model;
    }


    /**
     * Delete a corpus with all their treatments (but not their newly created corpus)
     * @param id id of the corpus to remove.
     * @param user current user.
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    @Transactional
    public void delete(@PathVariable("id") Long id, @ModelAttribute User user){
        Corpus corpus = corpusRepository.findById(id);
        if (corpus.getUser().equals(user) &&
                (corpus.getState().isShutdown() || corpus.getState().isError())){
            collect.shutdownCollect(corpus);
            if (corpus.getFils()!=null)
                corpus.getFils().forEach(corpus1 -> {
                    corpus1.setFather(null); corpusRepository.save(corpus1);
                });

            corpusRepository.delete(corpus);
            user.getCorpus().remove(corpus);
        }
    }

    /**
     * Stop a running collect.
     * @param id id of corpus
     * @param user current user.
     */
    @RequestMapping(value = "/{id}/collect", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    @Transactional
    public void stopCollect(@PathVariable("id") Long id, @ModelAttribute User user){
        Corpus c = corpusRepository.findById(id);
        if (c!=null && c.getUser().equals(user)){
            collect.shutdownCollect(c);
        }
    }

    /**
     * @param id the id of the corpus asked
     * @param page the number of the current page
     * @return The view of a corpus
     */
    @RequestMapping(value = "/{id}/{page}", method = RequestMethod.GET)
    public ModelAndView pageOne(@PathVariable("id") Long id,@PathVariable("page") int page, @ModelAttribute User user) {
        ModelAndView model = new ModelAndView("viewCorpus");
        Corpus corpus = corpusRepository.findById(id);
        int decal=100;
        int nbrPage = (int) (tweetRepository.countByCorpusId(id)/decal) + 1;
        List l = tweetRepository.findByCorpusId(id, new PageRequest(page-1, decal)).getContent();
        if (l == null || page <1){
            throw new NotFoundException();
        }
        model.addObject("currentPage",page);
        model.addObject("id",id);
        model.addObject("list",l);
        model.addObject("nbrPage", nbrPage);
        model.addObject("errorMsg",corpus.getErrorMessage());
        model.addObject("state",collect.collectIsRunning(corpus));
        return model;
    }

    /**
     * CSV exportation.
     * @param id id of corpus to export.
     * @param response HTTP response to get Output stream.
     * @param user Current user
     * @throws IOException
     */
    @Transactional
    @RequestMapping(value = "/{idRoute}/machin.csv")
    public void toCSV(@PathVariable("idRoute") long id,
                      HttpServletResponse response,
                      @ModelAttribute User user) throws IOException {
        String csvFileName = "exportCorpus"+id+".csv";

        response.setContentType("text/csv");

        // creates mock data
        String headerKey = "Content-Disposition";
        String headerValue = String.format("attachment; filename=\"%s\"",
                csvFileName);
        response.setHeader(headerKey, headerValue);
        headerKey = "Content-Size";
        headerValue = "" + (tweetRepository.countByCorpusId(id)*10);
        response.setHeader(headerKey,headerValue);

        OutputStream resOs= response.getOutputStream();
        OutputStream buffOs= new BufferedOutputStream(resOs);
        OutputStreamWriter outputwriter = new OutputStreamWriter(buffOs);

        service.exportCSV(user,id,outputwriter);

    }

    /**
     * Main controller, it launch a collect with some parameters, it's all injected by Spring
     * @param duration The duration
     * @param keywords Some keywords
     * @param location WARNING!!! the params are in a list in this order : originLat originLng destinationLat destinationLng
     * @param users List of name of users
     * @param twitter Session variable twitter
     * @param user Current user
     * @return Redirect to the corpus detail
     */
    @RequestMapping(method = RequestMethod.POST)
    public String createCorpus(
            @RequestParam("duration") int duration,
            @RequestParam("name") String nameC,
            @RequestParam(value = "keyword", required = false) List<String> keywords,
            @RequestParam(value = "location", required = false) List<Double> location,
            @RequestParam(value = "user", required = false) List<String> users,
            @RequestParam(value= "lang", required= false) String lang,
            @ModelAttribute("twitter") Twitter twitter,
            @ModelAttribute("user") User user,
            Model model
    ) {

        //Some dirty things here...
        Long[] ids = null;
        if (users != null) {
            ids = users.stream().map(name -> {
                try {
                    return twitter.showUser(name).getId();
                } catch (TwitterException e) {
                    return 0;
                }
            }).toArray(Long[]::new);
        }
        double[][] doubles = null;
        if (location != null && location.get(0) != null) {
            doubles = new double[][]{{location.get(1), location.get(0)}, {location.get(3), location.get(2)}};
        }
        Optional<String[]> okeywords = keywords == null ? Optional.empty() : Optional.of(keywords.toArray(new String[keywords.size()]));
        Optional<double[][]> oLocation = doubles == null ? Optional.empty() : Optional.of(doubles);
        Optional<Long[]> oUser = ids == null ? Optional.empty() : Optional.of(ids);
        Optional<String[]> oUserString = ids == null ? Optional.empty() : Optional.of(users.toArray(new String[users.size()]));
        Optional<String> oLangString = lang == null ? Optional.empty() : Optional.of(lang);

        Corpus corpus = collect.addCollect(user, nameC, duration, okeywords, oUserString, oUser, oLocation,oLangString, tweetRepository);

        model.addAttribute("user", repository.findOne(user.getId()));

        return "redirect:/corpus/" + corpus.getId() + "/1";
    }

    /**
     * Import a CSV file and create a new corpus with it. When this is done, it display the detailed view of the newly created corpus.
     * @param file Multipart file sent by user
     * @param user current user
     * @param model Session
     * @return the view of the newlyCreatedCorpus
     * @throws IOException When the file is not fully formed
     */
    @RequestMapping(value = "/import",method = RequestMethod.POST,headers=("content-type=multipart/*"))
    public String importCorpus(@RequestParam("file") MultipartFile file,
                               @ModelAttribute User user,
                               @RequestParam("name") String name    ,
                               Model model) throws IOException {
        if (file.isEmpty())
            throw new UnsupportedOperationException();

        Corpus corpus = service.importCSV(file.getInputStream(), user, name);

        model.addAttribute("user",repository.findOne(user.getId()));

        return "redirect:/corpus/" + corpus.getId();
    }

    @ExceptionHandler(NotFoundException.class)
    public ModelAndView handleCorpusNotFound(NotFoundException ex){
        return new ModelAndView("error");
    }
}
