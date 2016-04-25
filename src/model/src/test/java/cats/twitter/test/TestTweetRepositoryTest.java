/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cats.twitter.test;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

import cats.twitter.model.Tweet;
import cats.twitter.repository.TweetRepository;
/**
 *
 * @author p0807460
 * 
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath*:config/applicationContext.xml" })
@TransactionConfiguration(transactionManager="transactionManager",defaultRollback=true)
public class TestTweetRepositoryTest {
    @Autowired TweetRepository tweetRepo;
	
	@Test public void tstFindTweetByNom(){
		/*final Tweet tw=new Tweet();
		
		tw.setText("dgfolij ojlk kjj kj kleroâ pzkr, kefjkf. Jndh jizpjp jeoltrùm jkero");
		tw.setLocation("Lyon");
                tw.setAuthor(Long.MIN_VALUE);
		Tweet tweet=tweetRepo.save(tw);
		
		Assert.assertTrue(tweet.getId()>0);
		//List<Customer> founds=customerRepo.findAll();
		List<Tweet> founds=tweetRepo.findAll();
		
		Assert.assertTrue(founds.size()>0);
		
		Tweet found=tweetRepo.findOne(tweet.getId());*/
		
		//Assert.assertNotNull(found);
	}
}
