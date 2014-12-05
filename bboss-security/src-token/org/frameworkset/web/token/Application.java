package org.frameworkset.web.token;

/**
 * 
 * @author yinbp
 *
 */
public class Application {
	private String appid;
	private String secret;
	private long ticketlivetime;
	private long dualtokenlivetime;
	private long temptokenlivetime;
	public String getAppid() {
		return appid;
	}
	public void setAppid(String appid) {
		this.appid = appid;
	}
	public String getSecret() {
		return secret;
	}
	public void setSecret(String secret) {
		this.secret = secret;
	}
	public long getTicketlivetime() {
		return ticketlivetime;
	}
	public void setTicketlivetime(long ticketlivetime) {
		this.ticketlivetime = ticketlivetime;
	}
	public long getDualtokenlivetime() {
		return dualtokenlivetime;
	}
	public void setDualtokenlivetime(long dualtokenlivetime) {
		this.dualtokenlivetime = dualtokenlivetime;
	}
	public long getTemptokenlivetime() {
		return temptokenlivetime;
	}
	public void setTemptokenlivetime(long temptokenlivetime) {
		this.temptokenlivetime = temptokenlivetime;
	}

}
