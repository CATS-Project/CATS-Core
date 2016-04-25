package cats.twitter.webapp.dto;

/**
 * Created by Nathanael on 19/10/2015.
 */
public class MyAccessToken {
    private String tokensecret;
    private String token;

    public void setTokensecret(String tokensecret) {
        this.tokensecret = tokensecret;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public String getTokensecret() {
        return tokensecret;
    }
}
