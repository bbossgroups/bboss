package org.frameworkset.web.token.ws;

public class TokenCheckResponse  implements java.io.Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4298093947423068003L;
	private int resultcode;
	private String userAccount;
	private String worknumber;
	public TokenCheckResponse () {
		// TODO Auto-generated constructor stub
	}
	public int getResultcode() {
		return resultcode;
	}
	public void setResultcode(int resultcode) {
		this.resultcode = resultcode;
	}
	public String getUserAccount() {
		return userAccount;
	}
	public void setUserAccount(String userAccount) {
		this.userAccount = userAccount;
	}
	public String getWorknumber() {
		return worknumber;
	}
	public void setWorknumber(String worknumber) {
		this.worknumber = worknumber;
	}

}
