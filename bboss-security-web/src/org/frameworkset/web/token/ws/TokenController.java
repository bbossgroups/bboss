package org.frameworkset.web.token.ws;

import javax.jws.WebService;
import javax.servlet.http.HttpServletRequest;

import org.frameworkset.util.annotations.ResponseBody;
import org.frameworkset.web.token.TokenException;
import org.frameworkset.web.token.TokenHelper;

/**
 * <p>
 * Title: TokenController.java
 * </p>
 * 
 * <p>
 * Description:
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2006
 * </p>
 * 
 * <p>
 * Company: 三一集团
 * </p>
 * 
 * @Date 2012-8-27 上午11:43:42
 * @author biaoping.yin
 * @version 1.0.0
 */
@WebService(name="TokenService",targetNamespace="org.frameworkset.web.token.ws.TokenService")
public class TokenController implements TokenService {
	/**
	 * 获取令牌请求
	 * @param request
	 * @return
	 * @throws TokenException 
	 */
	public @ResponseBody String getToken(HttpServletRequest request) throws TokenException
	{
		if(TokenHelper.isEnableToken())//如果开启令牌机制就会存在memTokenManager对象，否则不存在
		{
			return  TokenHelper.getTokenService().buildDToken(request);
		}
		else
		{
			return null;
		}
	}
	
	public @ResponseBody String genTempToken() throws Exception
	{
		if(TokenHelper.isEnableToken())//如果开启令牌机制就会存在memTokenManager对象，否则不存在
		{
			return  TokenHelper.getTokenService().genTempToken();
		}
		else
		{
			return null;
		}
	}
	
	/**
	 * 获取令牌请求
	 * @param request
	 * @return
	 * @throws Exception 
	 */
	public @ResponseBody String genAuthTempToken(String appid,String secret,String ticket) throws Exception
	{
		if(TokenHelper.isEnableToken())//如果开启令牌机制就会存在memTokenManager对象，否则不存在
		{
			return  TokenHelper.getTokenService().genAuthTempToken(appid, secret, ticket);
		}
		else
		{
			return null;
		}
	}
	
	/**
	 * 获取令牌请求
	 * @param request
	 * @return
	 * @throws Exception 
	 */
	public @ResponseBody String genDualToken(String appid,String secret,String ticket) throws Exception
	{
		if(TokenHelper.isEnableToken())//如果开启令牌机制就会存在memTokenManager对象，否则不存在
		{
			long dualtime = 30l*24l*60l*60l*1000l;
			return  TokenHelper.getTokenService().genDualToken(appid, secret, ticket,dualtime);
		}
		else
		{
			return null;
		}
	}
	
	/**
	 * 获取令牌请求
	 * @param request
	 * @return
	 * @throws Exception 
	 */
	public @ResponseBody String genDualTokenWithDefaultLiveTime(String appid,String secret,String ticket) throws Exception
	{
		if(TokenHelper.isEnableToken())//如果开启令牌机制就会存在memTokenManager对象，否则不存在
		{

			return  TokenHelper.getTokenService().genDualTokenWithDefaultLiveTime(appid, secret, ticket);
		}
		else
		{
			return null;
		}
	}
//	/**
//	 * 获取应用公钥
//	 * @param appid
//	 * @param secret
//	 * @return
//	 * @throws Exception 
//	 */
//	public @ResponseBody String getPublicKey(String appid,String secret) throws Exception
//	{
//		if(TokenHelper.isEnableToken())//如果开启令牌机制就会存在memTokenManager对象，否则不存在
//		{
//			return  TokenHelper.getTokenService().getPublicKey(appid, secret);
//		}
//		else
//		{
//			return null;
//		}
//	}
	
	/**
	 * 获取令牌请求
	 * http://localhost:8081/SanyPDP/token/getParameterToken.freepage
	 * @param request
	 * @return
	 * @throws TokenException 
	 */
	public @ResponseBody String getParameterToken(HttpServletRequest request) throws TokenException
	{
		if(TokenHelper.isEnableToken())//如果开启令牌机制就会存在memTokenManager对象，否则不存在
		{
			return  TokenHelper.getTokenService().buildParameterDToken(request);
		}
		else
		{
			return null;
		}
	}
	public @ResponseBody TokenGetResponse genTicket(String account,String worknumber,String appid,String secret) throws TokenException
	{
		if(TokenHelper.isEnableToken())//如果开启令牌机制就会存在memTokenManager对象，否则不存在
		{
			TokenGetResponse tokenGetResponse = new TokenGetResponse();
			String ticket =  TokenHelper.getTokenService().genTicket( account, worknumber, appid, secret);
			tokenGetResponse.setTicket(ticket);
			return tokenGetResponse;
		}
		else
		{
			return null;
		}
	}
}
