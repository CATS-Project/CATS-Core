package cats.twitter.security;

/**
 * Constants for Spring Security authorities.
 * 
 * @author Hicham BOUBOUH hicham.boubouh1@gmail.com
 * @version $$Id$$
 */
public final class AuthoritiesConstants
{

	private AuthoritiesConstants()
	{}

	public static final String ADMIN = "ROLE_ADMIN";

	public static final String USER = "ROLE_USER";

	public static final String ANONYMOUS = "ROLE_ANONYMOUS";
}
