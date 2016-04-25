package cats.twitter.test;

import cats.twitter.collect.ManageCollect;
import cats.twitter.model.Corpus;
import cats.twitter.model.Tweet;
import cats.twitter.model.User;
import cats.twitter.repository.CorpusRepository;
import cats.twitter.repository.TweetRepository;

import java.util.Date;
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
import java.util.Optional;

import org.springframework.transaction.annotation.Transactional;

/**
 * Classe de test du repository.
 * @author Netapsys
 * @version $Revision$ $Date$
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath*:config/applicationContext.xml"})
@Transactional
@TransactionConfiguration(transactionManager="transactionManager",defaultRollback=true)
public class TestCollect
{

    @Autowired
    private CorpusRepository corpusRepository;
    @Autowired
    private UserRepository userRepository;
    @Test
    public void tstAddCollect(){
        ManageCollect manageCollect = new ManageCollect();
        manageCollect.setCorpusRepository(corpusRepository);
        User user = new User();
        user.setLastName("aaaaaaa");
        user.setFirstName("titi");
        user.setLogin("sddsfdsfdsfsd");
        user.setActivated(true);
        user.setPassword("password");
        User userSave = userRepository.save(user);
        /*double lat = 53.186288;
        double longitude = -8.043709;
        double lat1 = lat - 4;
        double longitude1 = longitude - 8;
        double lat2 = lat + 4;
        double longitude2 = longitude + 8;
        double longitude1 = 41.38156626488825;
        double lat1 = -7.548828125;
        double longitude2 = 52.1767885655741;
        double lat2 = 10.451171875;
        double[][] tmp = {{longitude1, lat1}, {longitude2, lat2}};
        Optional<double[][]> bb = Optional.ofNullable(tmp);
        Date d = new Date(115, 9, 21, 11, 48);
        String[] tmpString = null;
        Optional<String[]> keywords =  Optional.ofNullable(tmpString);
        Long[] tmpLong = null;
        Optional<Long[]> follow = Optional.ofNullable(tmpLong);*/
//        Corpus corpus = manageCollect.addCollect(userSave, 10, keywords, follow, bb);

        //System.out.println(corpus.getId());
    }
}
