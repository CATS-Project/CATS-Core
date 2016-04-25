package cats.twitter.repository;

import cats.twitter.model.Authority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


/**
 * Spring Data JPA repository for the Authority entity.
 * 
 * @author Hicham BOUBOUH hicham.boubouh1@gmail.com
 * @version $$Id$$
 */
@Repository
public interface AuthorityRepository extends JpaRepository<Authority, String>
{}
