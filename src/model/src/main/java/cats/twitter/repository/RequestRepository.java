package cats.twitter.repository;

import java.util.List;
import java.util.Optional;

import cats.twitter.model.Module;
import org.springframework.data.jpa.repository.JpaRepository;

import cats.twitter.model.Request;
/**
 *
 * @author Anthony Deseille
 */

public interface RequestRepository extends JpaRepository<Request, Long>
{
	Optional<Request> findOneByToken(String token);
	List<Request> findByModule(Module module);
}
