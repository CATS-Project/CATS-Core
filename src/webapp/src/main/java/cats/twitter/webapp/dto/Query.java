package cats.twitter.webapp.dto;

import com.fasterxml.jackson.annotation.JsonUnwrapped;

import java.math.BigInteger;
import java.util.Map;
import java.util.Random;

public class Query {
    /**
     * {
         "token":"YFODIHF54sdf",
         "params":{
             "param1":"ttatata",
             "param2":"true"
     }

     }
     *
     */
    private static Random random = new Random();
    private String token;

    private Map<String,Object> params;

    public Query() {
        token = new BigInteger(130,random).toString(32);
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Map<String, Object> getParams() {
        return params;
    }

    public void setParams(Map<String, Object> params) {
        this.params = params;
    }

    @Override
    public String toString() {
        return "Query{" +
                "token='" + token + '\'' +
                ", params=" + params +
                '}';
    }
}
