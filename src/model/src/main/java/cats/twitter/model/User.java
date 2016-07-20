package cats.twitter.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * A user.
 * 
 * @author Hicham BOUBOUH hicham.boubouh1@gmail.com
 * @version $$Id$$
 */
@Entity
@Table(name = "USER")
public class User implements Serializable, UserDetails
{

	@Id
	@SequenceGenerator(name="user_seq",
			sequenceName="user_seq",
			allocationSize=1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE,
			generator="user_seq")
	private Long id;

	@NotNull
	@Size(min = 1, max = 50)
	@Column(length = 50, unique = true, nullable = false)
	private String login;

	@NotNull
	@Size(min = 4, max = 60)
	@Column(length = 60)
	private String password;

	@Size(max = 50)
	@Column(name = "first_name", length = 50)
	private String firstName;

	@Size(max = 50)
	@Column(name = "last_name", length = 50)
	private String lastName;

	@Email
	@Size(max = 100)
	@Column(length = 100, unique = true)
	private String email;

	@Column
	private boolean activated = false;

	@OneToMany(mappedBy = "user", fetch = FetchType.EAGER, cascade = { CascadeType.REMOVE }, orphanRemoval = true)
	private List<Corpus> corpus = new ArrayList<Corpus>();

	private String token;

	private String tokenSecret;

	private String consumerKey;

	private String consumerSecret;

	private String name;

	private String imageUrl;

	private String interest;

	private String affiliation;

	@ManyToMany
	@JoinTable(
		name = "USER_AUTHORITY",
		joinColumns = { @JoinColumn(name = "user_id", referencedColumnName = "id") },
		inverseJoinColumns = { @JoinColumn(name = "authority_name", referencedColumnName = "name") })
	private Set<Authority> authorities = new HashSet<>();

	public Long getId()
	{
		return id;
	}

	public String getLogin()
	{
		return login;
	}

	public void setLogin(String login)
	{
		this.login = login;
	}

	public String getPassword()
	{
		return password;
	}

	public void setPassword(String password)
	{
		this.password = password;
	}

	public String getFirstName()
	{
		return firstName;
	}

	public void setFirstName(String firstName)
	{
		this.firstName = firstName;
	}

	public String getLastName()
	{
		return lastName;
	}

	public void setLastName(String lastName)
	{
		this.lastName = lastName;
	}

	public String getEmail()
	{
		return email;
	}

	public void setEmail(String email)
	{
		this.email = email;
	}

	public boolean getActivated()
	{
		return activated;
	}

	public void setActivated(boolean activated)
	{
		this.activated = activated;
	}

	@Override
	public Set<Authority> getAuthorities()
	{
		return authorities;
	}

	public void setAuthorities(Set<Authority> authorities)
	{
		this.authorities = authorities;
	}

	public boolean isActivated()
	{
		return activated;
	}

	public String getToken()
	{
		return token;
	}

	public void setToken(String token)
	{
		this.token = token;
	}

	public String getTokenSecret()
	{
		return tokenSecret;
	}

	public void setTokenSecret(String tokenSecret)
	{
		this.tokenSecret = tokenSecret;
	}

	public String getConsumerKey()
	{
		return consumerKey;
	}

	public void setConsumerKey(String consumerKey)
	{
		this.consumerKey = consumerKey;
	}

	public String getConsumerSecret()
	{
		return consumerSecret;
	}

	public void setConsumerSecret(String consumerSecret)
	{
		this.consumerSecret = consumerSecret;
	}

	public String getTwitterName()
	{
		return name;
	}

	public void setTwitterName(String name)
	{
		this.name = name;
	}

	public String getImageUrl()
	{
		return imageUrl;
	}

	public void setImageUrl(String imageUrl)
	{
		this.imageUrl = imageUrl;
	}

	public List<Corpus> getCorpus()
	{
		return this.corpus;
	}

	public void setCorpus(List<Corpus> c)
	{
		this.corpus = c;
	}

	public String getInterest() {
		return interest;
	}

	public void setInterest(String interest) {
		this.interest = interest;
	}

	public String getAffiliation() {
		return affiliation;
	}

	public void setAffiliation(String affiliation) {
		this.affiliation = affiliation;
	}

	public boolean hasAllTheirTokens()
	{
		return token != null && token.length() != 0 &&
			tokenSecret != null && tokenSecret.length() != 0 &&
			consumerKey != null && consumerKey.length() != 0 &&
			consumerSecret != null && consumerSecret.length() != 0;
	}

	// For jsp...
	public boolean isTokenOk()
	{
		return hasAllTheirTokens();
	}

	@Override
	public String toString()
	{
		return "User{" +
			"login='" + login + '\'' +
			", password='" + password + '\'' +
			", firstName='" + firstName + '\'' +
			", lastName='" + lastName + '\'' +
			", email='" + email + '\'' +
			", activated='" + activated + '\'' +
			"}";
	}

	@Override
	public String getUsername()
	{
		return login;
	}

	@Override
	public boolean isAccountNonExpired()
	{
		return true;
	}

	@Override
	public boolean isAccountNonLocked()
	{
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired()
	{
		return true;
	}

	@Override
	public boolean isEnabled()
	{
		return activated;
	}

	@Override
	public boolean equals(Object o)
	{
		if (this == o)
			return true;
		if (o == null || !(o instanceof User))
			return false;

		User user = (User) o;

		return !(id != null ? !id.equals(user.id) : user.id != null);

	}

	@Override
	public int hashCode()
	{
		return id != null ? id.hashCode() : 0;
	}
}
