package cats.twitter.webapp.service;

import java.io.IOException;
import java.io.Writer;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cats.twitter.repository.TweetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import cats.twitter.exceptions.NotFoundException;
import cats.twitter.model.Corpus;
import cats.twitter.model.SubCorpus;
import cats.twitter.model.Tweet;
import cats.twitter.model.User;
import cats.twitter.repository.CorpusRepository;
import cats.twitter.repository.SubCorpusRepository;

/**
 * Created by Hicham.
 */
@Service
public class SubCorpusServiceImpl implements SubCorpusService
{
	@Autowired
	CorpusRepository corpusRepository;
	@Autowired
	SubCorpusRepository subRepository;
	@Autowired
	TweetRepository tweetRepository;

	@Transactional
	public SubCorpus getSubCorpus(Long subId)
	{
		SubCorpus sub = subRepository.findOne(subId);
		sub.lazyLoad(tweetRepository, true);
		return sub;
	}

	@Override
	@Transactional
	public void exportCSV(User user, Long id, Writer writer) throws IOException
	{
		// uses the Super CSV API to generate CSV data from the model data
		ICsvBeanWriter csvWriter = new CsvBeanWriter(writer,
			CsvPreference.STANDARD_PREFERENCE);

		String[] headers = getHeaders();
		csvWriter.writeHeader(headers);

		SubCorpus sub = subRepository.findOne(id);
		Corpus corpus = sub.getCorpus();
		Pattern pRegex = Pattern.compile(sub.getRegex());
		Pattern pTags;
		Matcher m;
		Boolean isFiltered = false;
		if (corpus != null)
		{
			for (Tweet atweet : corpus.getTweets())
			{
				m = pRegex.matcher(atweet.getText());
				if (m.find())
				{
					for (String tag : sub.getHashtags())
					{
						pTags = Pattern.compile("#" + tag);
						m = pTags.matcher(atweet.getText());
						if (!m.find())
						{
							isFiltered = true;
						}
					}
					for (String mention : sub.getMentions())
					{
						pTags = Pattern.compile("@" + mention);
						m = pTags.matcher(atweet.getText());
						if (!m.find())
						{
							isFiltered = true;
						}
					}
					if (!isFiltered)
					{
						csvWriter.write(atweet, headers);
					}
				}
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

	@Transactional
	public SubCorpus createSubCorpus(Long corpusId, String name, Optional<String> regexp, Optional<String[]> hashtags,
		Optional<String[]> mentions, Optional<Date> start, Optional<Date> end)
	{
		Corpus corpus = corpusRepository.findOne(corpusId);
		List<Tweet> tweetList;
		if(start.isPresent() && end.isPresent())
			tweetList = tweetRepository.findByCorpusIdAndDateBetween(corpusId, start.get(), end.get());
		else if(start.isPresent() && !end.isPresent())
			tweetList = tweetRepository.findByCorpusIdAndDateAfter(corpusId, start.get());
		else if(!start.isPresent() && end.isPresent())
			tweetList = tweetRepository.findByCorpusIdAndDateBefore(corpusId, end.get());
		else
		{
			tweetList = corpus.getTweets();
		}
		SubCorpus sub = new SubCorpus();
		sub.setCreationDate(new Date());
		sub.setName(name);
		sub.setCorpus(corpus);
		if (regexp.isPresent())
		{
			sub.setRegex(regexp.get());
		}
		if (hashtags.isPresent())
		{
			sub.setHashtags(hashtags.get());
		}
		if (mentions.isPresent())
		{
			sub.setMentions(mentions.get());
		}
		Pattern pRegex = Pattern.compile(sub.getRegex());
		Pattern pTags;
		Matcher m;
		Boolean isFiltered = false;
		if (corpus != null)
		{
			for (Tweet atweet : tweetList)
			{
				m = pRegex.matcher(atweet.getText());
				if (m.find())
				{
					if(hashtags.isPresent()){
						for (String tag : sub.getHashtags())
						{
							pTags = Pattern.compile("#" + tag);
							m = pTags.matcher(atweet.getText());
							if (!m.find())
							{
								isFiltered = true;
							}
						}
					}
					if (mentions.isPresent())
					{
						for (String mention : sub.getMentions())
						{
							pTags = Pattern.compile("@" + mention);
							m = pTags.matcher(atweet.getText());
							if (!m.find())
							{
								isFiltered = true;
							}
						}
					}

					if (!isFiltered)
					{
						Tweet t = new Tweet();
						t.setText(atweet.getText());
						t.setAuthor(atweet.getAuthor());
						t.setDate(atweet.getDate());
						t.setLocation(atweet.getLocation());
						t.setName(atweet.getName());
						t.setDescriptionAuthor(atweet.getDescriptionAuthor());
						t.setSubCorpus(sub);
						tweetRepository.save(t);
					}
				}
				isFiltered = false;
			}
		}
		else
			throw new NotFoundException();


		return subRepository.save(sub);
	}
}
