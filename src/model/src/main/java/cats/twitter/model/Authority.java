package cats.twitter.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.security.core.GrantedAuthority;

/**
 * An authority (a security role) used by Spring Security.
 * 
 * @author Hicham BOUBOUH hicham.boubouh1@gmail.com
 * @version $$Id$$
 */
@Entity
@Table(name = "AUTHORITY")
public class Authority implements Serializable, GrantedAuthority
{

	@NotNull
	@Size(min = 0, max = 50)
	@Id
	@Column(length = 50)
	private String name;

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	@Override
	public boolean equals(Object o)
	{
		if (this == o)
		{
			return true;
		}
		if (o == null || getClass() != o.getClass())
		{
			return false;
		}

		Authority authority = (Authority) o;

		if (name != null ? !name.equals(authority.name) : authority.name != null)
		{
			return false;
		}

		return true;
	}

	@Override
	public String toString()
	{
		return "Authority{" +
			"name='" + name + '\'' +
			"}";
	}

	@Override
	public String getAuthority()
	{
		return name;
	}
}
