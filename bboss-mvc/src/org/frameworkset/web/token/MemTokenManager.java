package org.frameworkset.web.token;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.frameworkset.security.ecc.ECCCoder.ECKeyPair;
import org.frameworkset.security.session.Session;
import org.frameworkset.web.token.BaseTokenStore.TokenResult;

import com.frameworkset.util.StringUtil;
import common.Logger;

/**
 * @author biaoping.yin
 * 
 *
 */
public class MemTokenManager {
	private static Logger log = Logger.getLogger(MemTokenManager.class);
	private TokenStore tokenStore;
	private static ThreadLocal<Session> localSession = new ThreadLocal<Session>();  
	public void destory()
	{
//		temptokens.clear();
//		temptokens = null;
		this.tokenStore.destory();
		if(this.tokenMonitor != null)
		{
			this.tokenMonitor.killdown();
		}
	}
	
//	private  Map<MemToken,Object> temptokens = new HashMap<MemToken,Object>();
	private TokenFilter tokenFilter;
	/**
	 * bboss跨站攻击token的参数名称，每个客户端页面通过这个名称将token传回服务端进行
	 * 校验
	 */
	
	
	
	private boolean enableToken = false;
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
	
	MemTokenManager(long tokendualtime,long tokenscaninterval,boolean enableToken,String tokenstore,TokenFilter tokenFilter)
	{
//		this.tokendualtime = tokendualtime;
		this.tokenscaninterval = tokenscaninterval;
		this.enableToken = enableToken;
//		this.tokenstore = tokenstore; 
		this.tokenStore = TokenStoreFactory.getTokenStore(tokenstore);
		this.tokenStore.setTempTokendualtime(tokendualtime);
		this.tokenFilter = tokenFilter;
//		if(tokenstore.equals("mem"))
//			tokenstore_i = tokenstore_in_mem;
//		else
//			tokenstore_i = tokenstore_in_session;
		if(enableToken && tokenscaninterval > 0 && tokendualtime > 0)
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
	private boolean assertDToken(Integer result)
	{
		return result == TokenStore.temptoken_request_validateresult_ok || result == TokenStore.temptoken_request_validateresult_nodtoken || result == TokenStore.temptoken_request_validateresult_notenabletoken;
	}
	/**
	 * 判断令牌是否有效，一次请求只判断一次，避免多次判断
	 * 同时记录判断结果，以便后续处理操作获取这个结果进行相应的处理
	 * @param request
	 * @return
	 */
	protected boolean firstRequest(ServletRequest request) 
	{
		Integer result = null;
		if(!this.enableToken)
		{
			result = TokenStore.temptoken_request_validateresult_notenabletoken;
			request.setAttribute(TokenStore.temptoken_request_validateresult_key,result);
			return true;
		}
		result = (Integer)request.getAttribute(TokenStore.temptoken_request_validateresult_key);//
		if(result != null)
		{
			return assertDToken(result);
		}
		
		String token = request.getParameter(TokenStore.temptoken_param_name);
//		if(request instanceof HttpServletRequest)
//		{
//			
//			HttpSession session = ((HttpServletRequest)request).getSession(false);
//			if(session == null)
//			{
//				result = mem(token);
//			}
//			else
//			{
//				result = sessionmemhash(token,session);
//			}
//		}
//		else
		{
			String appid= request.getParameter("appid");
			String secret= request.getParameter("secret");
			
			
			try {
				TokenResult tokenResult = this.tokenStore.checkToken(appid,secret,token);
				request.setAttribute(TokenStore.token_request_validatetoken_key, tokenResult);
				if( tokenResult.getTokenInfo() != null &&  tokenResult.getTokenInfo().getAccount() != null)
					request.setAttribute(TokenStore.token_request_account_key, tokenResult.getTokenInfo().getAccount());
				result = tokenResult.getResult();
			} catch (Exception e) {
				log.error("令牌校验失败:",e);
				result = TokenStore.temptoken_request_validateresult_fail;
			}
		}
		request.setAttribute(TokenStore.temptoken_request_validateresult_key,result);
		return 	assertDToken(result);
	}
	public static final String temptoken_param_name_word = TokenStore.temptoken_param_name + "=";
	/**
	 * 为url追加动态令牌参数
	 * @param url
	 * @return
	 */
	public String appendDTokenToURL(HttpServletRequest request,String url)
	{
		if(url == null)
			return url;
		if(url.indexOf(temptoken_param_name_word) > 0)
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
	private boolean assertDTokenSetted(Integer result)
	{
//		return !(result == MemTokenManager.temptoken_request_validateresult_nodtoken 
//				|| result == MemTokenManager.temptoken_request_validateresult_fail);		
		return result == TokenStore.temptoken_request_validateresult_ok || result == TokenStore.temptoken_request_validateresult_notenabletoken;
	}
	
	/**
	 * 判断令牌是否设置并且校验成功
	 * @param result
	 * @return
	 */
	public boolean assertDTokenSetted(ServletRequest request)
	{

		Integer result = (Integer)request.getAttribute(TokenStore.temptoken_request_validateresult_key);
		return assertDTokenSetted(result);
		
	}
	

	
	public String genToken(ServletRequest request,String fid,boolean cache)
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
	
	
	private String genMemToken(boolean cache)
	{
		
		if(this.enableToken)
		{
			
			
			if(cache)
			{
//				if(this.tokenstore_i == tokenstore_in_session)
//				{
//					session.setAttribute(hash, c);
//				}
//				else
				{
					return this.tokenStore.genTempToken().getToken();
				}
			}
			else
			{
				String token = UUID.randomUUID().toString();
				return token;
			}
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
		@Override
		public void run() {
			while(!killdown)
			{
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
//				check();
				tokenStore.livecheck();
			}
		}
		public boolean isKilldown() {
			return killdown;
		}
		
	}
	
//	private void check()
//	{
//		List<MemToken> olds = new ArrayList<MemToken>();
//		synchronized(this.checkLock)
//		{
//			Set<MemToken> keySet = this.temptokens.keySet();
//			Iterator<MemToken> itr = keySet.iterator();
//			
//			while(itr.hasNext())
//			{
//				
//				MemToken token = itr.next();
//				if(isold(token))
//				{
//					olds.add(token);
////					temptokens.remove(token);
//				}
//			}
//		}
//		MemToken token = null;
//		for(int i = 0; i < olds.size(); i ++)
//		{
//			if(tokenMonitor.isKilldown())
//				break;
//			token = olds.get(i);
//			temptokens.remove(token);
//		}
//		olds = null;
//		
//	}
	
//	private boolean isold(MemToken token)
//	{
//		long currentTime = System.currentTimeMillis();
//		long age = currentTime - token.getCreateTime();		
//		return age > this.tokendualtime;
//		
//	}
	
	public String buildDToken(String elementType,HttpServletRequest request)
	{
		return buildDToken(elementType,"'",request,null);
	}
	public String buildDToken(String elementType,String jsonsplit,HttpServletRequest request,String fid)
	{
		return buildDToken(elementType,jsonsplit,request,fid,true);
	}
	/**
	 * 生成隐藏域令牌,输出值为：
	 * <input type="hidden" name="_dt_token_" value="-1518435257">
	 * @param request
	 * @return
	 */
	public String buildHiddenDToken(HttpServletRequest request)
	{
		return buildDToken("input",null,request,null,true);
	}
	/**
	 * 生成json串令牌
	 * 如果jsonsplit为'，则输出值为：
	 * _dt_token_:'1518435257'
	 * 如果如果jsonsplit为",则输出值为：
	 * _dt_token_:"1518435257"
	 * @param jsonsplit
	 * @param request
	 * @return
	 */
	public String buildJsonDToken(String jsonsplit,HttpServletRequest request)
	{
		return buildDToken("json","'",request,null,true);
	}
	/**
	 * 生成url参数串令牌
	 * 输出值为：
	 * _dt_token_=1518435257
	 * @param request
	 * @return
	 */
	public String buildParameterDToken(HttpServletRequest request)
	{
		return buildDToken("param",null,request,null,true);
	}
	/**
	 * 只生成令牌，对于这种方式，客户端必须将该token以参数名_dt_token_传回服务端，否则不起作用
	 * 输出值为：
	 * 1518435257
	 * @param request
	 * @return
	 */
	public String buildDToken(HttpServletRequest request)
	{
		return buildDToken("token",null,request,null,true);
	}
	
	public String buildDToken(String elementType,String jsonsplit,HttpServletRequest request,String fid,boolean cache)
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

	public boolean isEnableToken() {
		return enableToken;
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
	public void sendRedirect(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		this.tokenFilter.sendRedirect(request, response);
	}
	
	public void doDTokencheck(ServletRequest request,ServletResponse response) throws IOException, DTokenValidateFailedException
	{
		if(!assertDTokenSetted(request))
		{
			if(request instanceof HttpServletRequest)
			{
				sendRedirect((HttpServletRequest) request,(HttpServletResponse) response);
			}
			else
			{
				throw new DTokenValidateFailedException();
			}
		}
	}
	public String genTempToken() throws Exception
	{
		return tokenStore.genTempToken().getSigntoken();
	}
	
	public String genDualToken(String appid,String secret,String account,long dualtime) throws Exception
	{
		//long start = System.currentTimeMillis();
//		long dualtime = 30l*24l*60l*60l*1000l;
		MemToken token = this.tokenStore.genDualToken(appid,account,secret,dualtime);
		return token.getSigntoken();
	}
	
	
	public String genAuthTempToken(String appid,String secret,String account) throws Exception
	{
		MemToken token = tokenStore.genAuthTempToken(appid,account,secret);
		return token.getSigntoken();
//		Assert.assertTrue(TokenStore.temptoken_request_validateresult_ok == mongodbTokenStore.checkToken("sim","xxxxxxxxxxxxxxxxxxxxxx",token.getSigntoken()).getResult());
	}
	
	public String getPublicKey(String appid,String secret) throws Exception
	{
		ECKeyPair pairs = tokenStore.getKeyPair(appid,secret);
		return pairs.getPublicKey();
	}
	
	public String getPrivateKey(String appid,String secret) throws Exception
	{
		ECKeyPair pairs = tokenStore.getKeyPair(appid,secret);
		return pairs.getPrivateKey();
	}
	
	public ECKeyPair getKeyPair(String appid,String secret) throws Exception
	{
		ECKeyPair pairs = tokenStore.getKeyPair(appid,secret);
		return pairs;
	}
	

}
