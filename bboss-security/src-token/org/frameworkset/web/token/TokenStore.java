package org.frameworkset.web.token;

import org.frameworkset.security.ecc.ECCCoderInf;
import org.frameworkset.security.ecc.SimpleKeyPair;
import org.frameworkset.web.token.BaseTokenStore.TokenInfo;


public interface TokenStore {
	public static final String type_temptoken = "tt";
	public static final String type_authtemptoken = "at";
	public static final String type_dualtoken = "dt";
	
	/**
	 * 令牌校验成功
	 */
	public static final Integer token_request_validateresult_ok = new Integer(1);
	/**
	 * 令牌校验失败
	 */
	public static final Integer token_request_validateresult_fail = new Integer(0);
	/**
	 * 无令牌状态，这个状态配合控制器方法的AssertDToken注解和jsp页面上的AssertDTokenTag一起使用，如果控制器方法AssertDToken注解或者jsp页面设置了AssertDTokenTag标签，则要求必须使用令牌
	 * 如果客户端没有传输令牌，则拒绝请求。
	 * AssertDToken和AssertDTokenTag主要用来防止客户端把令牌去掉后欺骗服务器进行访问
	 */
	public static final Integer token_request_validateresult_nodtoken = new Integer(2);
	
	public static final Integer token_request_validateresult_notenabletoken = new Integer(3);
	public static final Integer token_request_validateresult_expired = new Integer(4);
	public static final Integer token_request_validateresult_invalidated = new Integer(5);
	public static final Integer token_request_validateresult_notexist = new Integer(6);
	
	public static final String temptoken_param_name = "_dt_token_";
	public static final String ticket_param_name = "_dt_ticket_";
	public static final String app_param_name = "_dt_appid_";
	public static final String app_secret_param_name = "_dt_appid_secret";
	public static final String temptoken_request_attribute = "org.frameworkset.web.token.bboss_csrf_Token"; 
	public static final String temptoken_request_validateresult_key = "temptoken_request_validateresult_key";
	public static final String token_request_validatetoken_key = "token_request_validatetoken_key";
	public static final String token_request_account_key = "token_request_account_key";
	public static final String token_request_worknumber_key = "token_request_worknumber_key";
	public static final String RESULT_OK = "ok";
	public static final String RESULT_FAIL = "fail";
	public static final String ERROR_CODE_NOENABLETICKET = "NOENABLETICKET";
	public static final String ERROR_CODE_NOENABLETOKEN = "NOENABLETOKEN";
	public static final String ERROR_CODE_GETKEYPAIRFAILED = "GETKEYPAIRFAILED";
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
	public static final long DEFAULT_DUALTOKENLIVETIME = 30*24*60*60*1000L;
	public static final long DEFAULT_TEMPTOKENLIVETIME = 1*60*60*1000L;
	public static final long DEFAULT_TICKETTOKENLIVETIME = 1*60*60*1000L;
	public static final long DEFAULT_TOKENSCANINTERVAL = 60*1000L;
	public static final String temptoken_param_name_word = TokenStore.temptoken_param_name + "=";
	
	
	public abstract void destory();

	public abstract void livecheck();
	public abstract TokenResult checkTicket(String appid,String secret,String ticket)  throws TokenException;
	public abstract TokenResult checkToken(String appid,String secret,String tokeninfo)  throws TokenException;

//	public abstract Integer checkTempToken(TokenInfo tokeninfo)throws TokenException;
//	public abstract Integer checkAuthTempToken(TokenInfo tokeninfo)throws TokenException;
	public abstract Integer checkDualToken(TokenInfo tokeninfo)throws TokenException;

	public abstract long getTempTokendualtime()throws TokenException;

	public abstract void setTempTokendualtime(long tokendualtime);

//	public abstract Session getSession();
//
//	public abstract void setSession(Session session);
	
	public MemToken genTempToken()  throws TokenException ;
	public MemToken genDualToken(String appid,String ticket,String secret,long livetime)throws TokenException;
	public MemToken genDualTokenWithDefaultLiveTime(String appid,String ticket,String secret)throws TokenException;
	public MemToken genAuthTempToken(String appid, String ticket,String secret)throws TokenException;

	public SimpleKeyPair getKeyPair(String appid,String secret) throws TokenException;

	public abstract String genTicket(String account, String worknumber,
			String appid, String secret)throws TokenException;
//	public String[] decodeTicket(String ticket,
//			String appid, String secret) throws TokenException;
	public void setTicketdualtime(long ticketdualtime);

	public long getTicketdualtime() ;
	public long getDualtokenlivetime() ;

	public void setDualtokenlivetime(long dualtokenlivetime);

	public abstract void setECCCoder(ECCCoderInf eCCCoder);
	public abstract ECCCoderInf getECCCoder();
	public ValidateApplication getValidateApplication() ;
	public void setValidateApplication(ValidateApplication validateApplication) ;
	/**
	 * 销毁令牌票据ticket
	 * @param token
	 * @param appid
	 * @param secret
	 */
	public boolean destroyTicket(String ticket,String appid,String secret) throws TokenException;
	/**
	 * 刷新令牌票据ticket有效时间，如果ticket已经失效则抛出异常
	 * @param token
	 * @param appid
	 * @param secret
	 */
	public boolean refreshTicket(String ticket,String appid,String secret) throws TokenException;
}