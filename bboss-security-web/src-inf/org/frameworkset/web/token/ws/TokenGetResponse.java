package org.frameworkset.web.token.ws;

public class TokenGetResponse implements java.io.Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2533519006415287132L;
	private int resultcode;
	private String token;
	private String ticket;	
	public TokenGetResponse() {
		// TODO Auto-generated constructor stub
	}
	public int getResultcode() {
		return resultcode;
	}
	public void setResultcode(int resultcode) {
		this.resultcode = resultcode;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getTicket() {
		return ticket;
	}
	public void setTicket(String ticket) {
		this.ticket = ticket;
	}

}
