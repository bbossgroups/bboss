package org.frameworkset.web.token.ws;

public class TokenCheckResponse  implements java.io.Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4298093947423068003L;
	private String resultcode;
	private String userAccount;
	private String worknumber;
	private boolean validateResult;
	public TokenCheckResponse () {
		// TODO Auto-generated constructor stub
	}
	public String getResultcode() {
		return resultcode;
	}
	public void setResultcode(String resultcode) {
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
	public boolean isValidateResult() {
		return validateResult;
	}
	public void setValidateResult(boolean validateResult) {
		this.validateResult = validateResult;
	}

}
