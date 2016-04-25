package cats.twitter.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import cats.twitter.model.SubCorpus;
/**
 *
 * @author Anthony Deseille
 */

public interface SubCorpusRepository extends JpaRepository<SubCorpus, Long>
{
    SubCorpus findById(Long id);
}
