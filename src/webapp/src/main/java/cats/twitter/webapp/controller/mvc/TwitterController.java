package cats.twitter.webapp.controller.mvc;

import cats.twitter.model.User;
import cats.twitter.repository.UserRepository;
import cats.twitter.webapp.dto.MyAccessToken;
import cats.twitter.webapp.dto.OAuthToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

/**
 * Deal with association between CATS user and Twitter user to obtain tokens and execute collects
 */
@Controller
@SessionAttributes({"user","twitter"})
public class TwitterController {

    private MyAccessToken accessToken;

    private OAuthToken oauthToken;

    Twitter twitter;

    @Autowired
    UserRepository userRepository;

    @Autowired
    public TwitterController(OAuthToken oauthToken,MyAccessToken accessToken) {
        this.oauthToken = oauthToken;
        this.accessToken = accessToken;
    }

    /**
     * COntroller used to associate and collect tokens, it send to twitter login page, with a callback given to the JSP
     * @return the view to draw
     */
    @RequestMapping(value = "/associate",method = RequestMethod.GET)
    public ModelAndView associateTwitterAccount(HttpServletRequest request) {

        twitter = new TwitterFactory().getInstance();
        twitter.setOAuthConsumer(oauthToken.getConsumerKey(),
                oauthToken.getConsumerSecret());

        RequestToken requestToken;
        ModelAndView modelAndView = new ModelAndView("oauth");
        try {
            String callbackURL = urlExtractor(request.getRequestURL().toString(),"/associate")+"/associate-callback";
            requestToken = twitter.getOAuthRequestToken(callbackURL);
            String token = requestToken.getToken();
            String tokenSecret = requestToken.getTokenSecret();
            accessToken.setTokensecret(tokenSecret);
            accessToken.setToken(token);
            modelAndView.addObject("authUrl", requestToken.getAuthorizationURL());
        } catch (TwitterException e) {
            e.printStackTrace();
        }
        return modelAndView;
    }


    private String urlExtractor(String url,String mapping){
        return url.substring(0,url.indexOf(mapping));
    }

    /**
     * The request is fired from twitter success login page with the tokens in parameters
     * @param verifier Http get parameter, given from twitter login page
     * @param userModel Session variable of the user authenticated
     * @param model The session
     * @return The view to draw
     * @throws Exception Twitter connectivity problems
     */
    @Transactional
    @RequestMapping(value = "/associate-callback",method={RequestMethod.GET,RequestMethod.POST})
    protected String handleRequestInternal(
            @RequestParam(value = "oauth_verifier",required = false) String verifier,
            @ModelAttribute User userModel,
            Model model) throws Exception {
        if (verifier != null) {

            RequestToken requestToken = new RequestToken(accessToken.getToken(), accessToken.getTokensecret());
            AccessToken accessToken = twitter.getOAuthAccessToken(requestToken, verifier);
            twitter.setOAuthAccessToken(accessToken);
            twitter4j.User user = twitter.verifyCredentials();

            //Retrieving data from twitter4j model inside our model
            System.out.println("TOKEN");
            System.out.println(accessToken.getToken());
            System.out.println("TOKEN SECRET");
            System.out.println(accessToken.getTokenSecret());
            System.out.println("CONSUMER KEY");
            System.out.println(oauthToken.getConsumerKey());
            System.out.println("CONSUMER SECRET");
            System.out.println(oauthToken.getConsumerSecret());

            userModel.setToken(accessToken.getToken());
            userModel.setTokenSecret(accessToken.getTokenSecret());
            userModel.setConsumerKey(oauthToken.getConsumerKey());
            userModel.setConsumerSecret(oauthToken.getConsumerSecret());
            userModel.setTwitterName(user.getName());
            userModel.setImageUrl(user.getBiggerProfileImageURL());

            model.addAttribute("twitter", twitter);

            //Saving our model into DB
            userRepository.save(userModel);
        }
        return "redirect:/";
    }
}

