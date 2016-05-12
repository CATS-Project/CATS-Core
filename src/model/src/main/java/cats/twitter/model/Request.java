package cats.twitter.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Represent a treatment between a Module and a corpus.
 * When a module has finished its treatment, it can send a result from 3 different ways:
 * - CORPUS : The result is a brand new corpus who will be added to the other corpus.
 * - DATA : The result is data in every type of data possible.
 * - HTML : The result is visualisable in a browser.
 */

/**
 *
 * @author Anthony Deseille
 */

@Entity
public class Request
{
    static private final SimpleDateFormat format = new SimpleDateFormat("EEE, d MMMM yyyy HH:mm", Locale.ENGLISH);

	@Id
	@SequenceGenerator(name="request_seq",
			sequenceName="request_seq",
			allocationSize=1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE,
			generator="request_seq")
	private Long id;

	@OneToMany(cascade = CascadeType.ALL)
	private List<Result> results = new ArrayList<>();

	private String token;

	private Boolean isChained = false;

	private Boolean isSubCorpus = false;

	private Long idChain;

	@ManyToOne(cascade = {CascadeType.MERGE, CascadeType.REMOVE })
	private Request nextRequest;

	@ManyToOne(cascade =  {CascadeType.MERGE, CascadeType.REMOVE })
	private Request previousRequest;

	@ManyToOne(cascade = CascadeType.MERGE)
	private Module module;

	@ManyToOne()
	private Corpus corpus;

	@ManyToOne()
	private SubCorpus subCorpus;

	public SubCorpus getSubCorpus() {
		return subCorpus;
	}

	public void setSubCorpus(SubCorpus subCorpus) {
		this.subCorpus = subCorpus;
	}

	@Temporal(TemporalType.TIMESTAMP)
	private java.util.Date initDate;

    @Transient
    private Boolean finished;

	@ElementCollection
	private Map<String,String> params;

	@OneToMany(mappedBy = "corpus", fetch = FetchType.LAZY, cascade = { CascadeType.MERGE, CascadeType.REMOVE })
	private Set<Request> requests;

	public Set<Request> getRequests() {
		return requests;
	}

	public void setRequests(Set<Request> requests) {
		this.requests = requests;
	}

	public Map<String, String> getParams() {
		return params;
	}

	public void setParams(Map<String, String> params) {
		this.params = params;
	}

	public List<Result> getResults() {
		return results;
	}

	public void setResults(List<Result> results) {
		this.results = results;
	}

	public String getToken()
	{
		return token;
	}

	public void setToken(String token)
	{
		this.token = token;
	}

	public Module getModule()
	{
		return module;
	}

	public void setModule(Module module)
	{
		this.module = module;
	}

	public Corpus getCorpus()
	{
		return corpus;
	}

	public java.util.Date getInitDate()
	{
		return initDate;
	}

	@Transient
	@JsonIgnore
	public String getInitDateFormat()
	{
		return format.format(initDate);
	}

	public void setInitDate(java.util.Date initDate)
	{
		this.initDate = initDate;
	}

	public void setCorpus(Corpus corpus)
	{
		this.corpus = corpus;
	}

	public Long getId()
	{
		return id;
	}

	@JsonIgnore
	public boolean isFinished() {
        if (finished==null)
		    return !results.isEmpty();
        return finished;
	}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Request request = (Request) o;

        return !(id != null ? !id.equals(request.id) : request.id != null);

    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

	public void addResult(Result res) {
		results.add(res);
        //if (isFinished())
        //    token = null;
	}

    public Result getResult(Result.TypeRes type){
        for (Result res : results) {
            if (res.getType().equals(type))
                return res;
        }
        return null;
    }

	/**
	 * To avoid the org.hibernate.LazyInitializationException in jsp,
	 * call this function to preload the object.
	 */
	public void lazyLoad() {
        finished = isFinished();
        results = new ArrayList<>(results);
		params = new HashMap<>(params);
    }
	public Boolean isChained()
	{
		return isChained;
	}

	public void setChained(Boolean isChained)
	{
		this.isChained = isChained;
	}

	public Long getIdChain()
	{
		return idChain;
	}

	public void setIdChain(Long idChain)
	{
		this.idChain = idChain;
	}

	public Request getNextRequest()
	{
		return nextRequest;
	}

	public void setNextRequest(Request nextRequest)
	{
		this.nextRequest = nextRequest;
	}

	public Request getPreviousRequest()
	{
		return previousRequest;
	}

	public void setPreviousRequest(Request previousRequest)
	{
		this.previousRequest = previousRequest;
	}
}
