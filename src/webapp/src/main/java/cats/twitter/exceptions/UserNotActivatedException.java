package cats.twitter.exceptions;

import org.springframework.security.core.AuthenticationException;

/**
 * This exception is throw in case of a not activated user trying to authenticate.
 * 
 * @author Hicham BOUBOUH hicham.boubouh1@gmail.com
 * @version $$Id$$
 */
public class UserNotActivatedException extends AuthenticationException
{

	public UserNotActivatedException(String message)
	{
		super(message);
	}

	public UserNotActivatedException(String message, Throwable t)
	{
		super(message,t);
	}
}
