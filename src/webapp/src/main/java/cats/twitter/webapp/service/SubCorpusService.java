package cats.twitter.webapp.service;

import java.io.IOException;
import java.io.Writer;
import java.util.Date;
import java.util.Optional;

import cats.twitter.model.SubCorpus;
import cats.twitter.model.User;
import org.apache.zookeeper.Op;

/**
 * Created by Hicham.
 */
public interface SubCorpusService
{
	SubCorpus createSubCorpus(Long corpusId, String name, Optional<String> regexp, Optional<String[]> hashtags,
		Optional<String[]> mentions, Optional<Date> start, Optional<Date> end);

	public SubCorpus getSubCorpus(Long subId);

	void exportCSV(User user, Long id, Writer writer) throws IOException;
}
