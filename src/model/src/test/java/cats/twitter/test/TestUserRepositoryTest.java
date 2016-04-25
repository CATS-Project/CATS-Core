package cats.twitter.test;

import cats.twitter.model.User;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;


import cats.twitter.repository.UserRepository;
import java.util.Optional;

/**
 * Classe de test du repository.
 * @author Netapsys
 * @version $Revision$ $Date$
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath*:config/applicationContext.xml" })
@TransactionConfiguration(transactionManager="transactionManager",defaultRollback=true)
public class TestUserRepositoryTest
{
    @Autowired UserRepository clientRepo;
	
	@Test public void tstFindClientByNom(){
		/*final User client1=new User();
		
		client1.setLastName("TOTO");
		client1.setFirstName("titi");
                client1.setLogin("eeeeffffefefef");
                client1.setActivated(true);
                client1.setPassword("password");
		User client=clientRepo.save(client1);
		
		//Assert.assertTrue(client.getId()>0);
		//List<Customer> founds=customerRepo.findAll();
		Optional<User> founds=clientRepo.findOneByLogin("TOTO2");
		
		//Assert.assertTrue(founds.isPresent());
		
		User found=clientRepo.findOne(client.getId());
		//Assert.assertNotNull(found);*/
	}
	
	
}
