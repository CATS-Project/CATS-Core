package cats.twitter.collect;

import cats.twitter.model.Corpus;
import cats.twitter.model.User;
import cats.twitter.repository.TweetRepository;

/**
 *
 * @author Anthony Deseille
 */

import java.util.Optional;
public interface IManageCollect {
    Corpus addCollect(User u, String name, int duree, Optional<String[]> keywords, Optional<String[]> followsString, Optional<Long[]> follows, Optional<double[][]> location,Optional<String> lang, TweetRepository corpusRepository);

    Optional<Corpus> getCorpusFromUser(User user, long id);

    void shutdownCollect(Corpus c);

    Collect.State collectIsRunning(Corpus c);
}