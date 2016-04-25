package cats.twitter.repository;

import cats.twitter.model.User;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;


/**
 * Spring Data JPA repository for the User entity.
 * 
 * @author Hicham BOUBOUH hicham.boubouh1@gmail.com
 * @version $$Id$$
 */
public interface UserRepository extends JpaRepository<User, Long>
{
	Optional<User> findOneByEmail(String email);

	Optional<User> findOneByLogin(String login);

	@Override
	void delete(User t);

}
