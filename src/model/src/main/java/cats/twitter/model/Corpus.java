/*
 * To change this license header, choose License Headers in Project Properties. To change this template file, choose
 * Tools | Templates and open the template in the editor.
 */
package cats.twitter.model;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.persistence.*;

import cats.twitter.collect.Collect;
import cats.twitter.repository.TweetRepository;

/**
 * @author Anthony Deseille
 */
@Entity
public class Corpus implements Serializable
{

	static private final SimpleDateFormat format = new SimpleDateFormat("EEE, d MMMM yyyy HH:mm", Locale.ENGLISH);


	@Id
	@SequenceGenerator(name="corpus_seq",
			sequenceName="corpus_seq",
			allocationSize=1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE,
			generator="corpus_seq")
	private Long id;

	@OneToMany(mappedBy = "corpus", cascade = { CascadeType.PERSIST, CascadeType.REMOVE })
	private List<Tweet> tweets = new ArrayList<Tweet>();

	@Column(name = "name")
	private String name;

	@ManyToOne(fetch = FetchType.LAZY)
	private User user;

	@OneToMany(mappedBy = "corpus", fetch = FetchType.LAZY, cascade = { CascadeType.MERGE, CascadeType.REMOVE})
	private Set<Request> requests;

	@Column(length = 1024)
	private String errorMessage;

	@ManyToOne()
	private Corpus father;

	@OneToMany(mappedBy = "father")
	private List<Corpus> fils;

	@OneToMany(mappedBy = "corpus", fetch = FetchType.LAZY, cascade = { CascadeType.ALL})
	private List<SubCorpus> subCorpuses;

	private boolean calculated = false;

	private int duree;

	@ElementCollection
	private List<String> keyWords;

	@ElementCollection
	private List<String> follows;

	@OneToOne(cascade = CascadeType.ALL)
	private Location location;

	private Date lauchDate;

	public String getLang() {
		return lang;
	}

	public void setLang(String lang) {
		this.lang = lang;
	}

	private Date stopDate;
	private String lang;

	@Enumerated(EnumType.STRING)
	private Collect.State state;

	@Transient
	private long count;

	public User getUser()
	{
		return user;
	}

	public void setUser(User user)
	{
		this.user = user;
	}

	public Corpus()
	{
		lauchDate = new Date();
	}

	public List<Tweet> getTweets()
	{
		return tweets;
	}

	public void setTweets(List<Tweet> t)
	{
		tweets = t;
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

	public List<Corpus> getFils() {
		return fils;
	}

	@Override
	public boolean equals(Object o)
	{
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		Corpus corpus = (Corpus) o;

		return !(id != null ? !id.equals(corpus.id) : corpus.id != null);

	}

	@Override
	public int hashCode()
	{
		return id != null ? id.hashCode() : 0;
	}

	public Set<Request> getRequests()
	{
		return requests;
	}

	public void setErrorMessage(String errorMessage)
	{
		this.errorMessage = errorMessage;
	}

	public String getErrorMessage()
	{
		return errorMessage;
	}

	public void setDuree(int duree)
	{
		this.duree = duree;
	}

	public int getDuree()
	{
		return duree;
	}

	public String getLaunchDateString() {
		return format.format(lauchDate);
	}

	public String getStopDateString() {
		return format.format(stopDate);
	}

	public void setSubCorpuses(List<SubCorpus> subs)
	{
		if (subs != null)
		{
			this.subCorpuses = subs;
		}
	}

	public List<SubCorpus> getSubCorpuses()
	{
		return subCorpuses;
	}

	public void setKeyWords(String[] keyWords)
	{
		if (keyWords != null)
		{
			this.keyWords = Arrays.asList(keyWords);
		}
	}

	public void setFollows(String[] follows)
	{
		if (follows != null)
		{
			this.follows = Arrays.asList(follows);
		}
	}

	public void setLocation(double[][] location)
	{
		if (location != null)
		{
			this.location = new Location(location[0][1],location[0][0],location[1][1],location[1][0]);
		}
	}

	public Date getLauchDate()
	{
		return lauchDate;
	}

	public void setState(Collect.State state)
	{
		this.state = state;
		if (state.equals(Collect.State.ERROR) || state.equals(Collect.State.SHUTDOWN))
			stopDate = new Date();
		if (state.equals(Collect.State.INPROGRESS))
			stopDate = null;
	}

	public Collect.State getState()
	{
		return state;
	}

	public List<String> getKeyWords()
	{
		return keyWords;
	}

	public List<String> getFollows()
	{
		return follows;
	}

	public Location getLocation()
	{
		return location;
	}

	/**
	 * To avoid the org.hibernate.LazyInitializationException in jsp,
	 * call this function to preload the object.
	 * @param repository TweetRepository
	 * @param deep Loading the requests or not.
     */
	public void lazyLoad(TweetRepository repository, boolean deep)
	{
		if (follows == null || follows.size() == 0)
			follows = null;
		else
			follows = new ArrayList<>(follows);

		if (keyWords == null || keyWords.size() == 0)
			keyWords = null;
		else
			keyWords = new ArrayList<>(keyWords);
		if (subCorpuses == null || subCorpuses.size() == 0)
			subCorpuses = null;
		else
		{
			subCorpuses = new ArrayList<>(subCorpuses);
			for(SubCorpus sc :subCorpuses)
			{
				sc.lazyLoad(repository, true);
			}
		}

		if (deep)
		{
			requests = new HashSet<>(requests);
			for (Request r : requests)
			{
				r.lazyLoad();
			}
		}

		count = repository.countByCorpusId(id);
	}

	public Date getStopDate()
	{
		return stopDate;
	}

	public long getCount()
	{
		return count;
	}

	public Corpus getFather()
	{
		return father;
	}

	public void setFather(Corpus pere)
	{
		this.father = pere;
	}

	public boolean isCalculated() {
		return calculated;
	}

	public void setCalculated(boolean calculated) {
		this.calculated = calculated;
	}
}
