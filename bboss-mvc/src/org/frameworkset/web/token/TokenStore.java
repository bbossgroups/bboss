package org.frameworkset.web.token;

import org.frameworkset.security.ecc.ECCCoder.ECKeyPair;
import org.frameworkset.web.token.BaseTokenStore.TokenInfo;


public interface TokenStore {
	public static final String type_temptoken = "tt";
	public static final String type_authtemptoken = "at";
	public static final String type_dualtoken = "dt";
	
	/**
	 * 令牌校验成功
	 */
	public static final Integer temptoken_request_validateresult_ok = new Integer(1);
	/**
	 * 令牌校验失败
	 */
	public static final Integer temptoken_request_validateresult_fail = new Integer(0);
	/**
	 * 无令牌状态，这个状态配合控制器方法的AssertDToken注解和jsp页面上的AssertDTokenTag一起使用，如果控制器方法AssertDToken注解或者jsp页面设置了AssertDTokenTag标签，则要求必须使用令牌
	 * 如果客户端没有传输令牌，则拒绝请求。
	 * AssertDToken和AssertDTokenTag主要用来防止客户端把令牌去掉后欺骗服务器进行访问
	 */
	public static final Integer temptoken_request_validateresult_nodtoken = new Integer(2);
	
	public static final Integer temptoken_request_validateresult_notenabletoken = new Integer(3);
	public static final Integer temptoken_request_validateresult_expired = new Integer(4);
	public static final Integer temptoken_request_validateresult_invalidated = new Integer(5);
	public static final String temptoken_param_name = "_dt_token_";
	public static final String temptoken_request_attribute = "org.frameworkset.web.token.bboss_csrf_Token"; 
	public static final String temptoken_request_validateresult_key = "temptoken_request_validateresult_key";
	public abstract void destory();

	public abstract void livecheck();
	public abstract Integer checkToken(String appid,String secret,String tokeninfo)  throws Exception;

	public abstract Integer checkTempToken(TokenInfo tokeninfo);
	public abstract Integer checkAuthTempToken(TokenInfo tokeninfo);
	public abstract Integer checkDualToken(TokenInfo tokeninfo);

	public abstract long getTempTokendualtime();

	public abstract void setTempTokendualtime(long tokendualtime);

//	public abstract Session getSession();
//
//	public abstract void setSession(Session session);
	
	public MemToken genTempToken();
	public MemToken genDualToken(String appid,String account,String secret,long livetime);
	public MemToken genAuthTempToken(String appid, String account,String secret);
	public ECKeyPair getKeyPairs(String appid,String account,String secret) throws Exception;
	
	
	

}