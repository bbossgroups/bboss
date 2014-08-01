package org.frameworkset.web.token.ws;

public class TokenGetResponse implements java.io.Serializable{
	public static final String result_ok = "ok";
	/**
	 * 
	 */
	private static final long serialVersionUID = -2533519006415287132L;
	/**
	 * @see org.frameworkset.web.token.TokenStore
	 * public static final String ERROR_CODE_GETKEYPAIRFAILED = "GETKEYPAIRFAILED";
	public static final String ERROR_CODE_STOREKEYPAIRFAILED = "STOREKEYPAIRFAILED";
	
	public static final String ERROR_CODE_GETTICKETFAILED = "GETTICKETFAILED";
	public static final String ERROR_CODE_TICKETEXPIRED = "TICKETEXPIRED";
	public static final String ERROR_CODE_GENTICKETFAILED = "GENTICKETFAILED";
	public static final String ERROR_CODE_TICKETNOTEXIST = "TICKETNOTEXIST";
	public static final String ERROR_CODE_PERSISTENTTICKETFAILED = "PERSISTENTTICKETFAILED";
	
	public static final String ERROR_CODE_DECODETOKENFAILED = "DECODETOKENFAILED";
	
	public static final String ERROR_CODE_GENDUALTOKENFAILED = "GENDUALTOKENFAILED";
	public static final String ERROR_CODE_APPVALIDATEFAILED = "APPVALIDATEFAILED";
	public static final String ERROR_CODE_APPVALIDATERROR = "APPVALIDATERROR";
	
	public static final String ERROR_CODE_BACKENDERROR = "BACKENDERROR";
	public static final String ERROR_CODE_DECODETICKETFAILED = "DECODETICKETFAILED";
	public static final String ERROR_CODE_SIGNTOKENFAILED = "SIGNTOKENFAILED";
	public static final String ERROR_CODE_UNKNOWNTOKENTYPE = "UNKNOWNTOKENTYPE";
	public static final String ERROR_CODE_UNKNOWNTOKEN = "UNKNOWNTOKEN";
	public static final String ERROR_CODE_AUTHTEMPTOKENNOTEXIST = "AUTHTEMP TOKEN NOT EXIST";
	public static final String ERROR_CODE_DUALTOKENNOTEXIST = "DUAL TOKEN NOT EXIST";
	
	public static final String ERROR_CODE_GENTEMPTOKENFAILED = "GENTEMPTOKENFAILED";
	public static final String ERROR_CODE_DELETEEXPIREDTEMPTOKENFAILED = "DELETEEXPIREDTEMPTOKENFAILED";
	public static final String ERROR_CODE_DELETEEXPIREDAUTHTEMPTOKENFAILED = "DELETEEXPIREDAUTHTEMPTOKENFAILED";
	public static final String ERROR_CODE_DELETEEXPIREDAUTHDUALTOKENFAILED = "DELETEEXPIREDAUTHDUALTOKENFAILED";
	public static final String ERROR_CODE_CHECKAUTHTEMPTOKENFAILED = "CHECKAUTHTEMPTOKENFAILED";
	public static final String ERROR_CODE_CHECKTEMPTOKENFAILED = "CHECKTEMPTOKENFAILED";
	public static final String ERROR_CODE_QUERYDUALTOKENFAILED = "QUERYDUALTOKENFAILED";
	public static final String ERROR_CODE_STOREDUALTOKENFAILED = "STOREDUALTOKENFAILED";
	public static final String ERROR_CODE_UPDATEDUALTOKENFAILED = "UPDATEDUALTOKENFAILED";
	 */
	private String resultcode;
	private String token;
	private String ticket;	
	public TokenGetResponse() {
		// TODO Auto-generated constructor stub
	}
	public String getResultcode() {
		return resultcode;
	}
	public void setResultcode(String resultcode) {
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
