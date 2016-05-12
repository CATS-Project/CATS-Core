/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cats.twitter.repository;
import java.util.Date;
import java.util.List;
import cats.twitter.model.Tweet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Temporal;

import javax.persistence.TemporalType;

/**
 *
 * @author p0807460
**/ 
public interface TweetRepository extends JpaRepository<Tweet, Long>{
    List<Tweet> findAll();
    
    Page<Tweet> findByCorpusId(Long corpusId,Pageable pageable);
    Page<Tweet> findBySubCorpusId(Long subCorpusId, Pageable pageable);

    Long countByCorpusId(Long corpusId);
    Long countBySubCorpusId(Long corpusId);

    List<Tweet> findByCorpusId(Long corpusId);
    List<Tweet> findBySubCorpusId(Long subCorpusId);

    List<Tweet> findByCorpusIdAndDateBetween(Long corpusId, Date from, Date to);
    List<Tweet> findByCorpusIdAndDateBefore(Long corpusId, Date from);
    List<Tweet> findByCorpusIdAndDateAfter(Long corpusId, Date from);

}
