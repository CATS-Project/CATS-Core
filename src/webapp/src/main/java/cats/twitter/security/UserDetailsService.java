package cats.twitter.security;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cats.twitter.exceptions.UserNotActivatedException;
import cats.twitter.model.User;
import cats.twitter.repository.UserRepository;

/**
 * Authenticate a user from the database.
 */
@Service("userDetailsService")
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService
{

	private final Logger log = LoggerFactory.getLogger(UserDetailsService.class);

	@Autowired
	private UserRepository userRepository;

	@Override
	@Transactional
	public UserDetails loadUserByUsername(final String login)
	{
		log.debug("Authenticating {}",login);
		String lowercaseLogin = login.toLowerCase();
		Optional<User> userFromDatabase = userRepository.findOneByLogin(lowercaseLogin);
		return userFromDatabase.map(user -> {
			if (!user.getActivated())
			{
				throw new UserNotActivatedException("User " + lowercaseLogin + " was not activated");
			}
			List<GrantedAuthority> grantedAuthorities = user.getAuthorities().stream()
				.map(authority -> new SimpleGrantedAuthority(authority.getName()))
				.collect(Collectors.toList());
			return user;
		}).orElseThrow(
			() -> new UsernameNotFoundException("User " + lowercaseLogin + " was not found in the database"));
	}
}
