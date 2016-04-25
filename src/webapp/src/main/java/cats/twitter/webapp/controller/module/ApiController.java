/*
 * To change this license header, choose License Headers in Project Properties. To change this template file, choose
 * Tools | Templates and open the template in the editor.
 */
package cats.twitter.webapp.controller.module;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import cats.twitter.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import cats.twitter.repository.RequestRepository;
import cats.twitter.repository.TweetRepository;

import javax.servlet.http.HttpServletResponse;

/**
 * @author selimdogan
 */
@CrossOrigin(maxAge = 3600)
@RestController
public class ApiController {
    private static final int BUFFER_SIZE = 5176;
    @Autowired
    TweetRepository tweetRepository;
    @Autowired
    RequestRepository reqRepository;

    /**
     * Modules have to query here when they want to retrieve a corpus.
     * @param token The token sent in the initialisation request (/init)
     * @param from (not required) Pagination
     * @param to (not required) Pagination
     * @return The list of tweets of the corpus in JSON
     */
    @RequestMapping(value = "/api", method = RequestMethod.GET)
    public List<Tweet> api(@RequestHeader("token") String token,
                           @RequestParam(value = "from", required = false) Integer from,
                           @RequestParam(value = "to", required = false) Integer to) {
        Optional<Request> req = reqRepository.findOneByToken(token);
        if (!req.isPresent()) {
            throw new IllegalAccessError("Please verify your token!");
        }
        Corpus corpus = req.get().getCorpus();
        if(corpus != null){
            if (from == null) from = 0;
            if (to == null) to = Math.toIntExact(tweetRepository.countByCorpusId(corpus.getId()));

            return tweetRepository.findByCorpusId(corpus.getId(), new ChunkRequest(from, to-from)).getContent();
        }
        else{
            SubCorpus subCorpus = req.get().getSubCorpus();
            if (from == null) from = 0;
            if (to == null) to = Math.toIntExact(tweetRepository.countBySubCorpusId(subCorpus.getId()));

            return tweetRepository.findBySubCorpusId(subCorpus.getId(), new ChunkRequest(from, to-from)).getContent();

        }



    }

    @Transactional
    @RequestMapping(value = "/api-chain", method = RequestMethod.GET)
    public void apiChain(@RequestHeader("token") String token, HttpServletResponse response)
    {
        Optional<Request> req = reqRepository.findOneByToken(token);
        if (!req.isPresent())
        {
            throw new IllegalAccessError("Please verify your token!");
        }
        String result = null;
        if (req.get().isChained() && req.get().getPreviousRequest() != null)
        {
            result = req.get().getPreviousRequest().getResult(Result.TypeRes.FILE).getResult();
            try
            {

                File downloadFile = new File(result);
                FileInputStream inputStream = new FileInputStream(downloadFile);

                // set content attributes for the response
                response.setContentType("text/plain");
                response.setContentLength((int) downloadFile.length());

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
            catch (Exception e)
            {
                Result res = new Result();
                res.setDate(new Date());
                res.setResult(e.getMessage());
                res.setType(Result.TypeRes.ERROR);
                req.get().addResult(res);
                e.printStackTrace();
            }
        }
    }
}
