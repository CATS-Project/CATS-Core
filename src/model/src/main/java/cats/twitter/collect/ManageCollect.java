package cats.twitter.collect;

import cats.twitter.model.Corpus;
import cats.twitter.model.User;
import cats.twitter.repository.CorpusRepository;
import cats.twitter.repository.TweetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
/**
 *
 * @author Anthony Deseille
 */

@Service
public class ManageCollect  implements IManageCollect, Observer {

    private Map<Corpus,Collect> collects;

    public ManageCollect(){
        this.collects = new HashMap<>();
    }

    public void setCorpusRepository(CorpusRepository corpusRepository) {
        this.corpusRepository = corpusRepository;
    }

    @Autowired
    private CorpusRepository corpusRepository;

    @Override
    public Corpus addCollect(User u, String name, int duree, Optional<String[]> keywords, Optional<String[]> followsString, Optional<Long[]> follows, Optional<double[][]> location, Optional<String> lang, TweetRepository tweetRepository) {
        System.out.println("Debug");
        Collect c = new Collect(u, name, duree, keywords, followsString, follows, location,lang, this.corpusRepository, tweetRepository);

        c.addObserver(this);
        collects.put(c.getCorpus(),c);

        c.run();
        return c.getCorpus();
    }

    @Override
    @org.springframework.transaction.annotation.Transactional
    public void update(Observable o, Object arg) {
        System.out.println("debug");
        corpusRepository.save(((Collect) o).getCorpus());
        collects.remove(o);
        //if(collects.size() >0)
        //    collects.get(0).run();
    }

    @Override
    @org.springframework.transaction.annotation.Transactional
    public Optional<Corpus> getCorpusFromUser(User user, long id) {
        Corpus one = corpusRepository.findById(id);
        if (one == null) return Optional.empty();
        if (one.getUser().getId().equals(user.getId()))
            return Optional.of(one);
        throw new IllegalAccessError();
    }

    @Override
    public void shutdownCollect(Corpus c){
        Collect collect = collects.get(c);
        if (collect != null) {
            collect.shutdown();
            collects.remove(c);
        }
        else {
            c.setState(Collect.State.SHUTDOWN);
            corpusRepository.save(c);
        }
    }

    @Override
    public Collect.State collectIsRunning(Corpus c){
        Collect collect = collects.get(c);
        return collect == null ? Collect.State.SHUTDOWN :  collect.isInProgress();
    }
}
