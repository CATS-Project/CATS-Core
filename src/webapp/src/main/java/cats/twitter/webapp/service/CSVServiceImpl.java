package cats.twitter.webapp.service;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Writer;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.supercsv.cellprocessor.ParseDate;
import org.supercsv.cellprocessor.ParseLong;
import org.supercsv.cellprocessor.constraint.NotNull;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvBeanReader;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanReader;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import cats.twitter.collect.Collect;
import cats.twitter.collect.IManageCollect;
import cats.twitter.exceptions.NotFoundException;
import cats.twitter.model.Corpus;
import cats.twitter.model.Tweet;
import cats.twitter.model.User;
import cats.twitter.repository.CorpusRepository;
import cats.twitter.repository.TweetRepository;

@Service
public class CSVServiceImpl implements CSVService
{

	@Autowired
	IManageCollect collect;

	@Autowired
	CorpusRepository repository;
	@Autowired
	TweetRepository tweetRepository;

	private static int LIMIT = 2000000;

	/**
	 * Create a corpus from a csv file.
	 * 
	 * @param stream
	 *            Input stream of a csv file
	 * @param user
	 *            user who will belong the new corpus
	 * @param name
	 *            name of the newly created corpus
	 * @return the corpus newly created
	 * @throws IOException
	 */
	@Override
	@Transactional
	public Corpus importCSV(InputStream stream, User user, String name) throws IOException
	{
		Corpus corpus = new Corpus();
		corpus.setUser(user);
		corpus.setName(name);
		corpus.setState(Collect.State.SHUTDOWN);
		corpus = repository.save(corpus);

		ICsvBeanReader reader = new CsvBeanReader(new InputStreamReader(stream),CsvPreference.STANDARD_PREFERENCE);
		reader.getHeader(true);
		final String[] header = getHeaders();

		Tweet tweet;
		int i =0;
		while ((tweet = reader.read(Tweet.class,header,getProcessors())) != null && i < LIMIT)
		{

			tweet.setId(null);
			tweet.setCorpus(corpus);
			tweetRepository.save(tweet);
			i++;
		}

		return repository.findById(corpus.getId());
	}

	/**
	 * Export a corpus into csv format, in a writer.
	 * 
	 * @param user
	 *            user who own the corpus.
	 * @param id
	 *            the id of the corpus to export.
	 * @param writer
	 *            the stream to write the file.
	 * @throws IOException
	 */
	@Override
	@Transactional
	public void exportCSV(User user, long id, Writer writer) throws IOException
	{
		// uses the Super CSV API to generate CSV data from the model data
		ICsvBeanWriter csvWriter = new CsvBeanWriter(writer,
			CsvPreference.STANDARD_PREFERENCE);

		String[] headers = getHeaders();
		csvWriter.writeHeader(headers);

		Optional<Corpus> corpusFromUser = collect.getCorpusFromUser(user,id);
		if (corpusFromUser.isPresent())
		{
			for (Tweet aBook : corpusFromUser.get().getTweets())
			{
				csvWriter.write(aBook,headers);
			}
		}
		else
			throw new NotFoundException();

		csvWriter.close();
	}

	public String[] getHeaders()
	{
		return new String[] { "Id", "Author", "Date", "Text", "Location", "DescriptionAuthor", "Name" };
	}

	private static CellProcessor[] getProcessors()
	{
		return new CellProcessor[] {
			new NotNull(new ParseLong()), // id
			new NotNull(new ParseLong()), // Author
			new NotNull(new ParseDate("yyyy-MM-dd HH:mm:ss")), // Date
			new NotNull(), // Text
			new org.supercsv.cellprocessor.Optional(), // Location
			new org.supercsv.cellprocessor.Optional(), // DescriptionAuthor
			new org.supercsv.cellprocessor.Optional(), // Name
		};
	}
}
