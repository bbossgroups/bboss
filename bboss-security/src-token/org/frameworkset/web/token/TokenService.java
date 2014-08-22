/*
 *  Copyright 2008 bbossgroups
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.frameworkset.web.token;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.frameworkset.security.ecc.ECCCoderInf;
import org.frameworkset.security.ecc.ECCHelper;
import org.frameworkset.security.ecc.SimpleKeyPair;

import com.frameworkset.util.StringUtil;

/**
 * <p>Title: TokenService.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2008</p>
 * @Date 2014年4月18日
 * @author biaoping.yin
 * @version 3.8.0
 */
public class TokenService implements TokenServiceInf {
	private static Logger log = Logger.getLogger(TokenService.class);
	private TokenStore tokenStore;
	private boolean enableToken = false;
	
	public void destroy()
	{
//		temptokens.clear();
//		temptokens = null;
		this.tokenStore.destory();
		if(this.tokenMonitor != null)
		{
			this.tokenMonitor.killdown();
		}
	}
	

	
	
	
	
	private TokenMonitor tokenMonitor;
	
	
	
	/**
	 * tokenstore
	 * 指定令牌存储机制，目前提供两种机制：
	 * mem：将令牌直接存储在内存空间中
	 * session：将令牌存储在session中
	 * 默认存储在session中
	 */
//	protected String tokenstore = "session";
//	protected int tokenstore_i = tokenstore_in_session;
	/**
	 * 令牌持续时间,默认为1个小时
	 */
//	private long tokendualtime = 3600000;
	/**
	 * 令牌超时检测时间间隔，默认为-1，不检测
	 * 如果需要检测，那么只要令牌持续时间超过tokendualtime
	 * 对应的时间将会被清除
	 */
	private long tokenscaninterval = 1800000;
	private String tokenfailpath;
	public TokenService(TokenStore tokenStore,boolean enableToken,String tokenfailpath)
	{
		this.tokenStore = tokenStore;
		this.enableToken = enableToken;
		this.tokenfailpath = tokenfailpath;
	}
	public TokenService(long ticketdualtime,long temptokenlivetime,long dualtokenlivetime,long tokenscaninterval,Object tokenstore,boolean enableToken,String tokenfailpath)
	{
		this(ticketdualtime,temptokenlivetime,dualtokenlivetime,tokenscaninterval,tokenstore,enableToken,ECCHelper.RSA, tokenfailpath);
	}
	public TokenService(long ticketdualtime,long temptokenlivetime,long dualtokenlivetime,long tokenscaninterval,Object tokenstore,boolean enableToken,String ecctype,String tokenfailpath)
	{
		this(ticketdualtime,temptokenlivetime,dualtokenlivetime,tokenscaninterval,tokenstore,enableToken,ecctype,new NullValidateApplication(), tokenfailpath);
	}
	public TokenService(long ticketdualtime,long temptokenlivetime,long dualtokenlivetime,long tokenscaninterval,Object tokenstore,boolean enableToken,String ecctype,ValidateApplication validateApplication,String tokenfailpath)
	{
//		this.tokendualtime = tokendualtime;
		this.tokenscaninterval = tokenscaninterval;
		this.tokenfailpath = tokenfailpath;
//		this.tokenstore = tokenstore; 
		if(tokenstore instanceof String)
		{
			this.tokenStore = TokenStoreFactory.getTokenStore((String)tokenstore);
			ECCCoderInf ECCCoder= ECCHelper.getECCCoder(ecctype);
			tokenStore.setECCCoder(ECCCoder);
			tokenStore.setValidateApplication(validateApplication);
		}
		else
		{
			this.tokenStore = (TokenStore)tokenstore;
			if(this.tokenStore.getECCCoder() == null)
			{
				ECCCoderInf ECCCoder= ECCHelper.getECCCoder(ecctype);
				tokenStore.setECCCoder(ECCCoder);
			}
			if(tokenStore.getValidateApplication() == null)
			{
				tokenStore.setValidateApplication(validateApplication);
			}
		}
		
		
		
		this.tokenStore.setTempTokendualtime(temptokenlivetime);
		this.tokenStore.setTicketdualtime(ticketdualtime);
		tokenStore.setDualtokenlivetime(dualtokenlivetime);
//		if(tokenstore.equals("mem"))
//			tokenstore_i = tokenstore_in_mem;
//		else
//			tokenstore_i = tokenstore_in_session;
		this.enableToken = enableToken;
		if(enableToken && tokenscaninterval > 0 && (temptokenlivetime > 0 || dualtokenlivetime > 0))
		{
			tokenMonitor = new TokenMonitor();
			tokenMonitor.start();
			
		}
	}
	

	
//	public Integer sessionmemhash(String token,HttpSession session)
//	{
////		String sessionid = session.getId();
////		String token = request.getParameter(temptoken_param_name);
//		if(token == null)
//			return MemTokenManager.temptoken_request_validateresult_nodtoken;
//		
////		String hash = String.valueOf(HashUtil.mixHash(new StringBuffer().append(sessionid).append("_").append(token).toString()));
////		if(session.getAttribute(hash) != null)
////		{
////			session.removeAttribute(hash);
////			return true;
////		}
////		else
////			return false;
//		if(this.tokenstore_i == tokenstore_in_session)
//		{
//			
//			if(session.getAttribute(token) != null)
//			{
//				session.removeAttribute(token);
//				return MemTokenManager.temptoken_request_validateresult_ok;
//			}
//			else
//				return MemTokenManager.temptoken_request_validateresult_fail;
//		}
//		else//in memory
//		{
//			String sessionid = session.getId();
//			token = token + "_" + sessionid;
//			return _mem(token);
//		}
//		
//		
//	}
	
	/**
	 * 如果动态令牌校验成功或者令牌没有设置返回true
	 * @param result
	 * @return
	 */
	public static boolean assertDToken(Integer result)
	{
		return result.intValue() == TokenStore.token_request_validateresult_ok.intValue() || result.intValue() == TokenStore.token_request_validateresult_nodtoken.intValue() || result.intValue() == TokenStore.token_request_validateresult_notenabletoken.intValue();
	}
	
	
	/**
	 * 为url追加动态令牌参数
	 * @param url
	 * @return
	 * @throws TokenException 
	 */
	public String appendDTokenToURL(HttpServletRequest request,String url) throws TokenException
	{
		if(url == null)
			return url;
		if(url.indexOf(TokenStore.temptoken_param_name_word) > 0)
			return url;
		StringBuffer ret = new StringBuffer();
		String token = this.buildDToken(request);
		int idx = url.indexOf("?");
		if(idx > 0)
		{
			ret.append(url).append("&").append(TokenStore.temptoken_param_name).append("=").append(token);
		}
		else
		{
			ret.append(url).append("?").append(TokenStore.temptoken_param_name).append("=").append(token);
		}
		return ret.toString();
			
		
	}
	
	/**
	 * 判断令牌是否设置并且校验成功
	 * @param result
	 * @return
	 */
	public static boolean assertDTokenSetted(Integer result)
	{
//		return !(result == MemTokenManager.temptoken_request_validateresult_nodtoken 
//				|| result == MemTokenManager.temptoken_request_validateresult_fail);		
		return result.intValue() == TokenStore.token_request_validateresult_ok.intValue() || result.intValue() == TokenStore.token_request_validateresult_notenabletoken.intValue();
	}
	
	/**
	 * 判断令牌是否设置并且校验成功
	 * @param result
	 * @return
	 */
	public static boolean assertDTokenSetted(ServletRequest request)
	{

		Integer result = (Integer)request.getAttribute(TokenStore.temptoken_request_validateresult_key);
		return assertDTokenSetted(result);
		
	}
	

	
	/* (non-Javadoc)
	 * @see org.frameworkset.web.token.TokenServiceInf#genToken(javax.servlet.ServletRequest, java.lang.String, boolean)
	 */
	@Override
	public String genToken(ServletRequest request,String fid,boolean cache) throws TokenException
	{
		String tmp = null;
		String k = null;
		if(fid != null)
		{
			k = TokenStore.temptoken_request_attribute+ "_" + fid;
			tmp = (String)request.getAttribute(k);
			if(tmp != null)//如果已经生产token，则直接返回生产的toke，无需重复生产token
				return tmp;
		}
		
		
		{
			tmp = genMemToken( cache);
		}
		if(fid != null)
		{
			request.setAttribute(k, tmp);//将产生的token存入request，避免一个窗口生成两个不同的token
		}
		return tmp;
	}
	
	
	private String genMemToken(boolean cache) throws TokenException
	{
		
		
		
			
			
		if(cache)
		{

			return this.tokenStore.genTempToken().getToken();
		}
		else
		{
			String token = UUID.randomUUID().toString();
			return token;
		}
		
		
	}
	
	class TokenMonitor extends Thread
	{
		public TokenMonitor()
		{
			super("DTokens Scan Thread.");
		}
		private boolean killdown = false;
		public void start()
		{
			super.start();
		}
		public void killdown() {
			killdown = true;
			synchronized(this)
			{
				this.notifyAll();
			}
			
		}
		
		public void run() {
			while(!killdown)
			{
				
//				check();
				log.debug("过期令牌清理开始....");
				try {
					tokenStore.livecheck();
				} catch (Exception e1) {
					log.debug("过期令牌扫描异常：",e1);
				}
				log.debug("过期令牌清理结束.");
				synchronized(this)
				{
					try {
						
						this.wait(tokenscaninterval);
					} catch (InterruptedException e) {
						break;
					}
				}
				if(killdown)
					break;
			}
		}
		public boolean isKilldown() {
			return killdown;
		}
		
	}
	

	
	/* (non-Javadoc)
	 * @see org.frameworkset.web.token.TokenServiceInf#buildDToken(java.lang.String, javax.servlet.http.HttpServletRequest)
	 */
	@Override
	public String buildDToken(String elementType,HttpServletRequest request) throws TokenException
	{
		return buildDToken(elementType,"'",request,null);
	}
	/* (non-Javadoc)
	 * @see org.frameworkset.web.token.TokenServiceInf#buildDToken(java.lang.String, java.lang.String, javax.servlet.http.HttpServletRequest, java.lang.String)
	 */
	@Override
	public String buildDToken(String elementType,String jsonsplit,HttpServletRequest request,String fid) throws TokenException
	{
		return buildDToken(elementType,jsonsplit,request,fid,true);
	}
	/* (non-Javadoc)
	 * @see org.frameworkset.web.token.TokenServiceInf#buildHiddenDToken(javax.servlet.http.HttpServletRequest)
	 */
	@Override
	public String buildHiddenDToken(HttpServletRequest request) throws TokenException
	{
		return buildDToken("input",null,request,null,true);
	}
	/* (non-Javadoc)
	 * @see org.frameworkset.web.token.TokenServiceInf#buildJsonDToken(java.lang.String, javax.servlet.http.HttpServletRequest)
	 */
	@Override
	public String buildJsonDToken(String jsonsplit,HttpServletRequest request) throws TokenException
	{
		return buildDToken("json","'",request,null,true);
	}
	/* (non-Javadoc)
	 * @see org.frameworkset.web.token.TokenServiceInf#buildParameterDToken(javax.servlet.http.HttpServletRequest)
	 */
	@Override
	public String buildParameterDToken(HttpServletRequest request) throws TokenException
	{
		return buildDToken("param",null,request,null,true);
	}
	/* (non-Javadoc)
	 * @see org.frameworkset.web.token.TokenServiceInf#buildDToken(javax.servlet.http.HttpServletRequest)
	 */
	@Override
	public String buildDToken(HttpServletRequest request) throws TokenException
	{
		return buildDToken("token",null,request,null,true);
	}
	
	/* (non-Javadoc)
	 * @see org.frameworkset.web.token.TokenServiceInf#buildDToken(java.lang.String, java.lang.String, javax.servlet.http.HttpServletRequest, java.lang.String, boolean)
	 */
	@Override
	public String buildDToken(String elementType,String jsonsplit,HttpServletRequest request,String fid,boolean cache) throws TokenException
	{
//		if(!this.enableToken)
//			return "";
		StringBuffer buffer = new StringBuffer();
		if(StringUtil.isEmpty(elementType) || elementType.equals("input"))
		{
			buffer.append("<input type=\"hidden\" name=\"").append(TokenStore.temptoken_param_name).append("\" value=\"").append(this.genToken(request,fid, cache)).append("\">");
		}
		else if(elementType.equals("json"))//json
		{
			buffer.append(TokenStore.temptoken_param_name).append(":").append(jsonsplit).append(this.genToken(request,fid,cache)).append(jsonsplit);
		}
		else if(elementType.equals("param"))//参数
		{
			buffer.append(TokenStore.temptoken_param_name).append("=").append(this.genToken(request,fid,cache));
		}
		else if(elementType.equals("token"))//只输出token
		{
			buffer.append(this.genToken(request,fid,cache));
		}
		else
		{
			buffer.append("<input type=\"hidden\" name=\"").append(TokenStore.temptoken_param_name).append("\" value=\"").append(this.genToken(request,fid, cache)).append("\">");
		}
		return buffer.toString();
	}

	
	
	public static void main(String[] args)
	{
		Map h = new HashMap();
		h.put("1", "1");
		h.put("2", "2");
		h.put("3", "3");
		Iterator it = h.keySet().iterator();
		List olds = new ArrayList();
		while(it.hasNext())
		{
			olds.add(it.next());			
		}
		
		for(int i = 0; i < olds.size(); i ++)
		{
			Object token = olds.get(i);
			h.remove(token);
		}
	}
	
	
	
	/* (non-Javadoc)
	 * @see org.frameworkset.web.token.TokenServiceInf#genTempToken()
	 */
	
	/* (non-Javadoc)
	 * @see org.frameworkset.web.token.TokenServiceInf#genTempToken()
	 */
	@Override
	public String genTempToken() throws Exception
	{
		return tokenStore.genTempToken().getToken();
	}
	
	/* (non-Javadoc)
	 * @see org.frameworkset.web.token.TokenServiceInf#genDualToken(java.lang.String, java.lang.String, java.lang.String, long)
	 */
	
	/* (non-Javadoc)
	 * @see org.frameworkset.web.token.TokenServiceInf#genDualToken(java.lang.String, java.lang.String, java.lang.String, long)
	 */
	@Override
	public String genDualToken(String appid,String secret,String ticket,long dualtime) throws Exception
	{
		//long start = System.currentTimeMillis();
//		long dualtime = 30l*24l*60l*60l*1000l;
		MemToken token = this.tokenStore.genDualToken(appid,ticket,secret,dualtime);
		return token.getToken();
	}

	/* (non-Javadoc)
	 * @see org.frameworkset.web.token.TokenServiceInf#genDualTokenWithDefaultLiveTime(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public String genDualTokenWithDefaultLiveTime(String appid,String secret,String ticket) throws Exception
	{
		//long start = System.currentTimeMillis();
//		long dualtime = 30l*24l*60l*60l*1000l;
		MemToken token = this.tokenStore.genDualTokenWithDefaultLiveTime(appid,ticket,secret);
		return token.getToken();
	}
	
	
	
	/* (non-Javadoc)
	 * @see org.frameworkset.web.token.TokenServiceInf#genAuthTempToken(java.lang.String, java.lang.String, java.lang.String)
	 */
	
	/* (non-Javadoc)
	 * @see org.frameworkset.web.token.TokenServiceInf#genAuthTempToken(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public String genAuthTempToken(String appid,String secret,String ticket) throws Exception
	{
		MemToken token = tokenStore.genAuthTempToken(appid,ticket,secret);
		return token.getToken();
//		Assert.assertTrue(TokenStore.temptoken_request_validateresult_ok == mongodbTokenStore.checkToken("sim","xxxxxxxxxxxxxxxxxxxxxx",token.getSigntoken()).getResult());
	}
	
	/* (non-Javadoc)
	 * @see org.frameworkset.web.token.TokenServiceInf#getPublicKey(java.lang.String, java.lang.String)
	 */
	
	public String getPublicKey(String appid,String secret) throws Exception
	{
		SimpleKeyPair pairs = tokenStore.getKeyPair(appid,secret);
		return pairs.getPublicKey();
	}
	
	public String getPrivateKey(String appid,String secret) throws Exception
	{
		SimpleKeyPair pairs = tokenStore.getKeyPair(appid,secret);
		return pairs.getPrivateKey();
	}
	
	public SimpleKeyPair getKeyPair(String appid,String secret) throws Exception
	{
		SimpleKeyPair pairs = tokenStore.getKeyPair(appid,secret);
		return pairs;
	}
	/* (non-Javadoc)
	 * @see org.frameworkset.web.token.TokenServiceInf#checkToken(java.lang.String, java.lang.String, java.lang.String)
	 */
	
	public TokenResult checkToken(String appid,String secret,String token) throws TokenException
	{
		if(token == null)
		{
			TokenResult result = new TokenResult();
			result.setResult(TokenStore.token_request_validateresult_nodtoken);
			
			return result;
		}
		return this.tokenStore.checkToken(appid, secret, token);
	}
	
	/* (non-Javadoc)
	 * @see org.frameworkset.web.token.TokenServiceInf#checkTempToken(java.lang.String)
	 */
	
	public int checkTempToken(String token) throws TokenException
	{
		if(token == null)
		{
			
			return TokenStore.token_request_validateresult_nodtoken;
		}
		return this.tokenStore.checkToken(null,null,token).getResult();
	}
	/* (non-Javadoc)
	 * @see org.frameworkset.web.token.TokenServiceInf#genTicket(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	
	/* (non-Javadoc)
	 * @see org.frameworkset.web.token.TokenServiceInf#genTicket(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public String genTicket(String account,String worknumber,String appid,String secret) throws TokenException
	{
		return this.tokenStore.genTicket( account, worknumber, appid, secret);
	}



	/* (non-Javadoc)
	 * @see org.frameworkset.web.token.TokenServiceInf#isEnableToken()
	 */
	@Override
	public boolean isEnableToken() {
		return enableToken;
	}
	public String getTokenfailpath() {
		return tokenfailpath;
	}



}
