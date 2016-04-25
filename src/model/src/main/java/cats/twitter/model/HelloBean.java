package cats.twitter.model;

import org.springframework.stereotype.Service;


/**
 * Created by Nathanael on 17/10/2015.
 */
@Service
public class HelloBean {

    public String sayHello(){
        return "hello";
    }
}
