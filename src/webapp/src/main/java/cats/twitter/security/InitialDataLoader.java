package cats.twitter.security;

import java.io.FileInputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.util.Optional;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import cats.twitter.model.Authority;
import cats.twitter.model.User;
import cats.twitter.repository.AuthorityRepository;
import cats.twitter.repository.CorpusRepository;
import cats.twitter.repository.TweetRepository;
import cats.twitter.repository.UserRepository;

/**
 * Initialize authorities in dataBase on application Startup
 * 
 * @author Hicham BOUBOUH hicham.boubouh1@gmail.com
 * @version $Id$
 */
@Component
public class InitialDataLoader implements ApplicationListener<ContextRefreshedEvent>
{
	boolean alreadySetup = false;

	@Autowired
	private AuthorityRepository roleRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private CorpusRepository corpusRepository;
	@Autowired
	private TweetRepository tweetRepository;

	@Override
	@Transactional
	public void onApplicationEvent(ContextRefreshedEvent event)
	{
		Properties properties = new Properties();
		try {
			properties.load(new FileInputStream("/Users/Anthony/Documents/Master/Stage/CATS/src/webapp/src/main/java/cats/twitter/security/AdminLogin.properties"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		PasswordEncoder encoder = new BCryptPasswordEncoder();
		if (alreadySetup)
			return;

		Authority adminAuth = createRoleIfNotFound(AuthoritiesConstants.ADMIN);
		Authority userAuth = createRoleIfNotFound(AuthoritiesConstants.USER);

		Optional<User> ouser = userRepository.findOneByLogin(properties.getProperty("login"));
		if (!ouser.isPresent())
		{
			User user = new User();
			user.setLogin(properties.getProperty("login"));
			user.setPassword(encoder.encode(properties.getProperty("password")));
			user.setActivated(true);
			user.getAuthorities().add(userAuth);
			user.getAuthorities().add(adminAuth);
			userRepository.save(user);

		}
		alreadySetup = true;
	}

	@Transactional
	private Authority createRoleIfNotFound(String name)
	{
		Authority role = roleRepository.findOne(name);
		if (role == null)
		{
			role = new Authority();
			role.setName(name);
			roleRepository.save(role);
		}
		return role;
	}
}
