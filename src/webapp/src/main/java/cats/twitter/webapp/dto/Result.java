package cats.twitter.webapp.dto;

public class Result
{
	public Result() {
	}

	public Result(String token, String result) {
		this.token = token;
		this.result = result;
	}

	private String token;
	private String result;

	public String getToken()
	{
		return token;
	}

	public void setToken(String token)
	{
		this.token = token;
	}

	public String getResult()
	{
		return result;
	}

	public void setResult(String result)
	{
		this.result = result;
	}
}
