package cats.twitter.model;

import org.hibernate.annotations.Type;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.*;

/**
 *
 * @author Anthony Deseille
 */

@Entity
public class Module
{
	@Id()
	@SequenceGenerator(name="module_seq",
			sequenceName="module_seq",
			allocationSize=1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE,
			generator="module_seq")
	int id;

	String endpoint;

	private String name;
	@Type(type="text")
	@Column(name = "description")
	private String description;

	@OneToMany(fetch = FetchType.EAGER, cascade = { CascadeType.ALL })
	List<Params> params;

	@ElementCollection
	List<String> returns;

	public String getEndpoint()
	{
		return endpoint;
	}

	public List<Params> getParams()
	{
		return params;
	}

	public void setEndpoint(String endpoint)
	{
		this.endpoint = endpoint;
	}

    public void setParams(List<Params> params) {
        this.params = params;
    }

    @Override
    public String toString() {
        return "Module{" +
                "id=" + id +
                ", endpoint='" + endpoint + '\'' +
                ", params=" + params +
                '}';
    }

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public int getId()
	{
		return id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<String> getReturns() {
		return returns;
	}

	public void setReturns(List<String> returns) {
		this.returns = returns;
	}

	public void set(Module module) {
		name = module.name;
		description = module.description;
		params = module.params;
		returns = module.returns;
	}

	/**
	 * To avoid the org.hibernate.LazyInitializationException in jsp,
 	 * call this function to preload the object.
  	 */
	public void lazyLoad() {
		params = new ArrayList<>(params);
		returns = new ArrayList<>(returns);
	}
}
