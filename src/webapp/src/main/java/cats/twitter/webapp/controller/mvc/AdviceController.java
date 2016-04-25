package cats.twitter.webapp.controller.mvc;

import cats.twitter.model.User;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class AdviceController {

    /**
     * @param authentication The autowired spring security authentication
     * @return The user, added to session
     */
    @ModelAttribute(value = "user")
    public User getUserFromAuth(Authentication authentication){
        if (authentication != null)
            return (User) authentication.getPrincipal();
        return null;
    }

}
