package cats.twitter.security;

import cats.twitter.model.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class SuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {
        User user = (User) authentication.getPrincipal();
        if (!user.hasAllTheirTokens())
            response.sendRedirect("associate");
        else {
            request.getSession().setAttribute("twitter",createTwitter(user));
            super.onAuthenticationSuccess(request, response, authentication);
        }
    }

    private Twitter createTwitter(User user){
        ConfigurationBuilder builder = new ConfigurationBuilder();
        builder.setOAuthConsumerKey(user.getConsumerKey());
        builder.setOAuthConsumerSecret(user.getConsumerSecret());
        Configuration configuration = builder.build();
        TwitterFactory factory = new TwitterFactory(configuration);
        Twitter twitter = factory.getInstance();
        twitter.setOAuthAccessToken(new AccessToken(user.getToken(),user.getTokenSecret()));
        return twitter;
    }
}
