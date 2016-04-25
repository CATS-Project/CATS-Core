package cats.twitter.webapp.dto;

/**
 * Created by Nathanael on 19/10/2015.
 */
public class OAuthToken {
    private String consumerKey;
    private String consumerSecret;

    public String getConsumerKey() {
        return consumerKey;
    }

    public String getConsumerSecret() {
        return consumerSecret;
    }

    public void setConsumerKey(String consumerKey) {
        this.consumerKey = consumerKey;
    }

    public void setConsumerSecret(String consumerSecret) {
        this.consumerSecret = consumerSecret;
    }
}
