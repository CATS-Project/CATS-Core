package cats.twitter.model;

import java.util.*;

import javax.persistence.*;

import cats.twitter.repository.TweetRepository;
import org.springframework.stereotype.Service;

/**
 * @author Hicham
 */
@Entity
public class SubCorpus
{
	@Id
	@SequenceGenerator(name="subcorpus_seq",
			sequenceName="subcorpus_seq",
			allocationSize=1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE,
			generator="subcorpus_seq")
	private Long id;

	@Column(name = "name")
	private String name;

	@Transient
	private long count;

	@ManyToOne(cascade = CascadeType.MERGE)
	private Corpus corpus;

	@OneToMany(mappedBy = "subCorpus", cascade = { CascadeType.MERGE, CascadeType.REMOVE })
	private List<Tweet> tweets = new ArrayList<>();

	@OneToMany(mappedBy = "subCorpus", fetch = FetchType.LAZY, cascade = { CascadeType.MERGE, CascadeType.REMOVE })
	private Set<Request> requests;

	@Column(name = "regex")
	private String regex;

	@ElementCollection
	private List<String> hashtags;
	@ElementCollection
	private List<String> mentions;

	@Temporal(TemporalType.TIMESTAMP)
	private Date creationDate;


	public Set<Request> getRequests() {
		return requests;
	}

	public void setRequests(Set<Request> requests) {
		this.requests = requests;
	}


	public void setMentions(List<String> mentions) {
		this.mentions = mentions;
	}




	public Long getId()
	{
		return id;
	}

	public void setId(Long id)
	{
		this.id = id;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public Corpus getCorpus()
	{
		return corpus;
	}

	public void setCorpus(Corpus corpus)
	{
		this.corpus = corpus;
	}

	public String getRegex()
	{
		return regex;
	}

	public void setRegex(String regex)
	{
		this.regex = regex;
	}

	public void setHashtags(String[] hashtags)
	{
		if (hashtags != null)
		{
			this.hashtags = Arrays.asList(hashtags);
		}
	}

	public List<String> getHashtags()
	{
		return hashtags;
	}

	public void setMentions(String[] mentions)
	{
		if (mentions != null)
		{
			this.mentions = Arrays.asList(mentions);
		}
	}

	public long getCount() {
		return count;
	}

	public void setCount(long count) {
		this.count = count;
	}

	/**
	 * To avoid the org.hibernate.LazyInitializationException in jsp,
	 * call this function to preload the object.
	 */
	public void lazyLoad(TweetRepository repository, boolean deep)
	{
		if (mentions == null || mentions.size() == 0)
			mentions = null;
		else
			mentions = new ArrayList<>(mentions);

		if (hashtags == null || hashtags.size() == 0)
			hashtags = null;
		else
			hashtags = new ArrayList<>(hashtags);

		if (deep)
		{

			requests = new HashSet<>(requests);
			for (Request r : requests)
			{
				r.lazyLoad();
			}
		}

		count = repository.countBySubCorpusId(id);
	}

	public List<String> getMentions()
	{
		return this.mentions;
	}

	public Date getCreationDate()
	{
		return creationDate;
	}

	public void setCreationDate(Date creationDate)
	{
		this.creationDate = creationDate;
	}

	public List<Tweet> getTweets() {
		return tweets;
	}

	public void setTweets(List<Tweet> tweets) {
		this.tweets = tweets;
	}
}
