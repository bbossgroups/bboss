package org.frameworkset.web.token;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.frameworkset.spi.BaseApplicationContext;
import org.frameworkset.util.HashUtil;

import com.frameworkset.util.StringUtil;

/**
 * @author biaoping.yin
 * 
 *
 */
public class MemTokenManager {
	private final Map<MemToken,Object> temptokens = new HashMap<MemToken,Object>();
	private TokenFilter tokenFilter;
	/**
	 * bboss跨站攻击token的参数名称，每个客户端页面通过这个名称将token传回服务端进行
	 * 校验
	 */
	public static final String temptoken_param_name = "_dt_token_";
	private static final String temptoken_request_attribute = "org.frameworkset.web.token.bboss_csrf_Token"; 
	public static final String temptoken_request_validateresult_key = "temptoken_request_validateresult_key";
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
	private final Object c = new Object();
	private final Object checkLock = new Object();
	private boolean enableToken = false;
	private TokenMonitor tokenMonitor;
	public static final int tokenstore_in_session = 1;
	public static final int tokenstore_in_mem = 0;
	
	
	/**
	 * tokenstore
	 * 指定令牌存储机制，目前提供两种机制：
	 * mem：将令牌直接存储在内存空间中
	 * session：将令牌存储在session中
	 * 默认存储在session中
	 */
	protected String tokenstore = "session";
	protected int tokenstore_i = tokenstore_in_session;
	/**
	 * 令牌持续时间,默认为1个小时
	 */
	private long tokendualtime = 3600000;
	/**
	 * 令牌超时检测时间间隔，默认为-1，不检测
	 * 如果需要检测，那么只要令牌持续时间超过tokendualtime
	 * 对应的时间将会被清除
	 */
	private long tokenscaninterval = 1800000;
	
	MemTokenManager(long tokendualtime,long tokenscaninterval,boolean enableToken,String tokenstore,TokenFilter tokenFilter)
	{
		this.tokendualtime = tokendualtime;
		this.tokenscaninterval = tokenscaninterval;
		this.enableToken = enableToken;
		this.tokenstore = tokenstore; 
		this.tokenFilter = tokenFilter;
		if(tokenstore.equals("mem"))
			tokenstore_i = tokenstore_in_mem;
		else
			tokenstore_i = tokenstore_in_session;
		if(enableToken && tokenscaninterval > 0 && tokendualtime > 0)
		{
			tokenMonitor = new TokenMonitor();
			tokenMonitor.start();
			BaseApplicationContext.addShutdownHook(new Runnable(){

				@Override
				public void run() {
					tokenMonitor.killdown();
					
				}
				
			});
		}
	}
	
	public void put(String token)
	{
		if(this.enableToken)
		{
			synchronized(checkLock)
			{
				temptokens.put(new MemToken(token,System.currentTimeMillis()), c);
			}
		}
	}
	
	public Integer sessionmemhash(String token,HttpSession session)
	{
//		String sessionid = session.getId();
//		String token = request.getParameter(temptoken_param_name);
		if(token == null)
			return MemTokenManager.temptoken_request_validateresult_nodtoken;
		
//		String hash = String.valueOf(HashUtil.mixHash(new StringBuffer().append(sessionid).append("_").append(token).toString()));
//		if(session.getAttribute(hash) != null)
//		{
//			session.removeAttribute(hash);
//			return true;
//		}
//		else
//			return false;
		if(this.tokenstore_i == tokenstore_in_session)
		{
			
			if(session.getAttribute(token) != null)
			{
				session.removeAttribute(token);
				return MemTokenManager.temptoken_request_validateresult_ok;
			}
			else
				return MemTokenManager.temptoken_request_validateresult_fail;
		}
		else//in memory
		{
			String sessionid = session.getId();
			token = token + "_" + sessionid;
			return _mem(token);
		}
		
		
	}
	
	/**
	 * 如果动态令牌校验成功或者令牌没有设置返回true
	 * @param result
	 * @return
	 */
	private boolean assertDToken(Integer result)
	{
		return result == temptoken_request_validateresult_ok || result == temptoken_request_validateresult_nodtoken;
	}
	/**
	 * 判断令牌是否有效，一次请求只判断一次，避免多次判断
	 * 同时记录判断结果，以便后续处理操作获取这个结果进行相应的处理
	 * @param request
	 * @return
	 */
	protected boolean firstRequest(ServletRequest request) 
	{
		Integer result = (Integer)request.getAttribute(MemTokenManager.temptoken_request_validateresult_key);//
		if(result != null)
		{
			return assertDToken(result);
		}
		
		String token = request.getParameter(MemTokenManager.temptoken_param_name);
		if(request instanceof HttpServletRequest)
		{
			
			HttpSession session = ((HttpServletRequest)request).getSession(false);
			if(session == null)
			{
				result = mem(token);
			}
			else
			{
				result = sessionmemhash(token,session);
			}
		}
		else
		{
			result = mem(token);
		}
		request.setAttribute(MemTokenManager.temptoken_request_validateresult_key,result);
		return 	assertDToken(result);
	}
	public static final String temptoken_param_name_word = temptoken_param_name + "=";
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
			ret.append(url).append("&").append(temptoken_param_name).append("=").append(token);
		}
		else
		{
			ret.append(url).append("?").append(temptoken_param_name).append("=").append(token);
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
		return result == MemTokenManager.temptoken_request_validateresult_ok;
	}
	
	/**
	 * 判断令牌是否设置并且校验成功
	 * @param result
	 * @return
	 */
	public boolean assertDTokenSetted(ServletRequest request)
	{

		Integer result = (Integer)request.getAttribute(temptoken_request_validateresult_key);
		return assertDTokenSetted(result);
		
	}
	
	public Integer mem(String token)
	{
//		String token = request.getParameter(temptoken_param_name);
		return _mem(token);
	}
	
	private Integer _mem(String token)
	{
		
		if(token != null)
		{
			synchronized(checkLock)
			{
				Object tt =temptokens.remove(new MemToken(token,-1));
				if(tt != null)//is first request,and clear temp token to against Cross Site Request Forgery
				{
//					temptokens.remove(token);				
					return MemTokenManager.temptoken_request_validateresult_ok;
				}
				else
				{
					return MemTokenManager.temptoken_request_validateresult_fail;
				}
			}
		}
		else 
		{
			return MemTokenManager.temptoken_request_validateresult_nodtoken;
		}
	}
	
	public String genToken(ServletRequest request,String fid,boolean cache)
	{
		String tmp = null;
		String k = null;
		if(fid != null)
		{
			k = temptoken_request_attribute+ "_" + fid;
			tmp = (String)request.getAttribute(k);
			if(tmp != null)//如果已经生产token，则直接返回生产的toke，无需重复生产token
				return tmp;
		}
		
		if(request instanceof HttpServletRequest)
		{
			HttpSession session = ((HttpServletRequest)request).getSession(false);
			if(session == null)
			{
				tmp = memToken( cache);
			}
			else
			{
				tmp = sessionmemhashToken(request,session,cache);
			}
		}
		else
		{
			tmp = memToken( cache);
		}
		if(fid != null)
		{
			request.setAttribute(k, tmp);//将产生的token存入request，避免一个窗口生成两个不同的token
		}
		return tmp;
	}
	
	private String sessionmemhashToken(ServletRequest request,HttpSession session,boolean cache)
	{
		String sessionid = session.getId();
		String token = UUID.randomUUID().toString();
		String hash = String.valueOf(HashUtil.mixHash(new StringBuffer().append(sessionid).append("_").append(token).toString()));
		if(this.enableToken)
		{
			if(cache)
			{
				if(this.tokenstore_i == tokenstore_in_session)
				{
					session.setAttribute(hash, c);
				}
				else
				{
					put(hash + "_" + sessionid);
				}
			}
		}
		return hash;
	}
	private String memToken(boolean cache)
	{
		String token = UUID.randomUUID().toString();
		if(cache)
			put(token);
		return token;
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
				check();				
			}
		}
		public boolean isKilldown() {
			return killdown;
		}
		
	}
	
	private void check()
	{
		List<MemToken> olds = new ArrayList<MemToken>();
		synchronized(this.checkLock)
		{
			Set<MemToken> keySet = this.temptokens.keySet();
			Iterator<MemToken> itr = keySet.iterator();
			
			while(itr.hasNext())
			{
				
				MemToken token = itr.next();
				if(isold(token))
				{
					olds.add(token);
//					temptokens.remove(token);
				}
			}
		}
		MemToken token = null;
		for(int i = 0; i < olds.size(); i ++)
		{
			if(tokenMonitor.isKilldown())
				break;
			token = olds.get(i);
			temptokens.remove(token);
		}
		olds = null;
		
	}
	
	private boolean isold(MemToken token)
	{
		long currentTime = System.currentTimeMillis();
		long age = currentTime - token.getCreateTime();		
		return age > this.tokendualtime;
		
	}
	
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
			buffer.append("<input type=\"hidden\" name=\"").append(temptoken_param_name).append("\" value=\"").append(this.genToken(request,fid, cache)).append("\">");
		}
		else if(elementType.equals("json"))//json
		{
			buffer.append(temptoken_param_name).append(":").append(jsonsplit).append(this.genToken(request,fid,cache)).append(jsonsplit);
		}
		else if(elementType.equals("param"))//参数
		{
			buffer.append(temptoken_param_name).append("=").append(this.genToken(request,fid,cache));
		}
		else if(elementType.equals("token"))//只输出token
		{
			buffer.append(this.genToken(request,fid,cache));
		}
		else
		{
			buffer.append("<input type=\"hidden\" name=\"").append(temptoken_param_name).append("\" value=\"").append(this.genToken(request,fid, cache)).append("\">");
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
	

}
