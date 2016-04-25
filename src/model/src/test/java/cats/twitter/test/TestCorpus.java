package cats.twitter.test;

import cats.twitter.model.Corpus;
import cats.twitter.model.Tweet;
import cats.twitter.model.User;
import cats.twitter.repository.CorpusRepository;
import cats.twitter.repository.TweetRepository;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;


import cats.twitter.repository.UserRepository;
import java.util.ArrayList;
import org.springframework.transaction.annotation.Transactional;

/**
 * Classe de test du repository.
 * @author Netapsys
 * @version $Revision$ $Date$
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath*:config/applicationContext.xml" })
@Transactional
public class TestCorpus
{
    @Autowired UserRepository userRepo;
    @Autowired TweetRepository tweetRepo;
    @Autowired CorpusRepository corpusRepo;
	@Test
        public void tstFindCorpusById(){
		final User client1=new User();
		
		client1.setLastName("TOTO");
		client1.setFirstName("titi");
                client1.setLogin("k");
                client1.setActivated(true);
                client1.setPassword("password");
		User user=userRepo.save(client1);
                
                final Tweet tw=new Tweet();
		
		tw.setText("dgfolij ojlk kjj kj kleroâ pzkr, kefjkf. Jndh jizpjp jeoltrùm jkero");
		tw.setLocation("Lyon");
                tw.setAuthor(Long.MIN_VALUE);
		Tweet tweet=tweetRepo.save(tw);
		
		final Corpus corpus1 = new Corpus();
                List<Tweet> lstT = new ArrayList<>();
                lstT.add(tw);
                corpus1.setTweets(lstT);
                corpus1.setUser(user);
                corpusRepo.save(corpus1);
		// List<Corpus> lst = corpusRepo.findByUserIdLike(client1.getId());
		//                System.out.println(lst.get(0).getTweets());

        //user.getCorpus().add(corpus1);
        //userRepo.save(user);

	}
        
        
        
	
	
}
