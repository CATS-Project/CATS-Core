/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cats.twitter.repository;
import java.util.List;
import cats.twitter.model.Tweet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.solr.repository.SolrRepository;
/**
 *
 * @author p0807460
**/ 
public interface TweetRepository extends  SolrRepository<Tweet, Long>, JpaRepository<Tweet, Long>{
    List<Tweet> findAll();
    
    Page<Tweet> findByCorpusId(Long corpusId,Pageable pageable);
    Page<Tweet> findBySubCorpusId(Long subCorpusId, Pageable pageable);

    Long countByCorpusId(Long corpusId);
    Long countBySubCorpusId(Long corpusId);

    List<Tweet> findByCorpusId(Long corpusId);
    List<Tweet> findBySubCorpusId(Long subCorpusId);

}
