package cats.twitter.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import cats.twitter.model.Corpus;
import cats.twitter.model.User;

/**
 *
 * @author Anthony Deseille
 */

public interface CorpusRepository extends JpaRepository<Corpus, Long>
{

	Corpus findById(Long id);

	List<Corpus> findByUser(User user);

	List<Corpus> findByUserIdLike(Long id);

	// Page<Customer> findByNomLikeOrderByNomAsc(String nom, Pageable pageable);

}
