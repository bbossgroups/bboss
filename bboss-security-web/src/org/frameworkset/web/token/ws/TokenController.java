package org.frameworkset.web.token.ws;

import javax.jws.WebService;
import javax.servlet.http.HttpServletRequest;

import org.frameworkset.util.annotations.ResponseBody;
import org.frameworkset.web.token.TokenException;
import org.frameworkset.web.token.TokenHelper;
import org.frameworkset.web.token.TokenStore;

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
			return  TokenHelper.getTokenService().genTempToken();
	}
	
	@Override
	public @ResponseBody(datatype="json") TokenGetResponse getTempToken() throws Exception {
		TokenGetResponse tokenGetResponse = new TokenGetResponse();
			try {
				tokenGetResponse.setToken(TokenHelper.getTokenService().genTempToken());
				tokenGetResponse.setResultcode(TokenStore.RESULT_OK);
			
			}catch (TokenException e) {
				tokenGetResponse.setResultcode(e.getMessage());
			
			} catch (Exception e) {
				tokenGetResponse.setResultcode(TokenStore.ERROR_CODE_BACKENDERROR);
			}
			return  tokenGetResponse;
	}
	
	/**
	 * 获取令牌请求
	 * @param request
	 * @return
	 * @throws Exception 
	 */
	public @ResponseBody String genAuthTempToken(String appid,String secret,String ticket) throws Exception
	{
			return  TokenHelper.getTokenService().genAuthTempToken(appid, secret, ticket);
	}
	@Override
	public @ResponseBody(datatype="json") TokenGetResponse getAuthTempToken(String appid, String secret,
			String ticket) throws Exception {
		TokenGetResponse tokenGetResponse = new TokenGetResponse();
			try {
				tokenGetResponse.setToken( TokenHelper.getTokenService().genAuthTempToken(appid, secret, ticket));
				tokenGetResponse.setResultcode(TokenStore.RESULT_OK);
			
			}catch (TokenException e) {
				tokenGetResponse.setResultcode(e.getMessage());
			
			} catch (Exception e) {
				tokenGetResponse.setResultcode(TokenStore.ERROR_CODE_BACKENDERROR);
			}
			return  tokenGetResponse;
	}
	/**
	 * 获取令牌请求
	 * @param request
	 * @return
	 * @throws Exception 
	 */
	public @ResponseBody String genDualToken(String appid,String secret,String ticket) throws Exception
	{
			long dualtime = 30l*24l*60l*60l*1000l;
			return  TokenHelper.getTokenService().genDualToken(appid, secret, ticket,dualtime);
	}
	
	@Override
	public @ResponseBody(datatype="json") TokenGetResponse getDualToken(String appid, String secret,
			String ticket) throws Exception {
		TokenGetResponse tokenGetResponse = new TokenGetResponse();
			try {
				long dualtime = 30l*24l*60l*60l*1000l;
				tokenGetResponse.setToken( TokenHelper.getTokenService().genDualToken(appid, secret, ticket,dualtime));
				tokenGetResponse.setResultcode(TokenStore.RESULT_OK);
			
			}catch (TokenException e) {
				tokenGetResponse.setResultcode(e.getMessage());
			
			} catch (Exception e) {
				tokenGetResponse.setResultcode(TokenStore.ERROR_CODE_BACKENDERROR);
			}
			return  tokenGetResponse;
	}
	/**
	 * 获取令牌请求
	 * @param request
	 * @return
	 * @throws Exception 
	 */
	public @ResponseBody String genDualTokenWithDefaultLiveTime(String appid,String secret,String ticket) throws Exception
	{

			return  TokenHelper.getTokenService().genDualTokenWithDefaultLiveTime(appid, secret, ticket);
	}
	
	/**
	 * 获取令牌请求
	 * @param request
	 * @return
	 * @throws Exception 
	 */
	public @ResponseBody(datatype="json") TokenGetResponse getDualTokenWithDefaultLiveTime(String appid,String secret,String ticket) throws Exception
	{
		TokenGetResponse tokenGetResponse = new TokenGetResponse();
//		if(TokenHelper.isEnableToken())//如果开启令牌机制就会存在memTokenManager对象，否则不存在
		{
			try {
				tokenGetResponse.setToken( TokenHelper.getTokenService().genDualTokenWithDefaultLiveTime(appid, secret, ticket));
				tokenGetResponse.setResultcode(TokenStore.RESULT_OK);
			
			}catch (TokenException e) {
				tokenGetResponse.setResultcode(e.getMessage());
			
			} catch (Exception e) {
				tokenGetResponse.setResultcode(TokenStore.ERROR_CODE_BACKENDERROR);
			}
			return  tokenGetResponse;
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
	public @ResponseBody(datatype="json") TicketGetResponse getTicket(String account,String worknumber,String appid,String secret) throws TokenException
	{

		TicketGetResponse tokenGetResponse = new TicketGetResponse();
		try {
			String ticket =  TokenHelper.getTokenService().genTicket( account, worknumber, appid, secret);
			tokenGetResponse.setTicket(ticket);
			tokenGetResponse.setResultcode(TokenStore.RESULT_OK);
			
		} catch (TokenException e) {
			tokenGetResponse.setResultcode(e.getMessage());
		
		} catch (Exception e) {
			tokenGetResponse.setResultcode(TokenStore.ERROR_CODE_BACKENDERROR);
		}
		return tokenGetResponse;
		
	}

	@Override
	public @ResponseBody String genTicket(String account, String worknumber, String appid,
			String secret) throws Exception {
		String ticket =  TokenHelper.getTokenService().genTicket( account, worknumber, appid, secret);
		return  ticket;
	}

	



	
}
