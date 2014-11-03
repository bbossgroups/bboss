package org.frameworkset.web.token;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.frameworkset.util.StringUtil;


/**
 * 防止跨站请求过滤器
 * bboss防止跨站请求过滤器的机制如下：
 * 采用动态令牌和session相结合的方式产生客户端令牌，一次请求产生一个唯一令牌
 * 令牌识别采用客户端令牌和服务端session标识混合的方式进行判别，如果客户端令牌和服务端令牌正确匹配，则允许访问，否则认为用户为非法用户并阻止用户访问并跳转到
 * redirectpath参数对应的地址，默认为/login.jsp。
 * 
 * 令牌存储机制通过参数tokenstore指定，包括两种，内存存储和session存储，默认为session存储，当令牌失效（匹配后立即失效，或者超时失效）后，系统自动清除失效的令牌；采用session方式
 * 存储令牌时，如果客户端页面没有启用session，那么令牌还是会存储在内存中。
 * 
 * 令牌生命周期：客户端的令牌在服务器端留有存根，当令牌失效（匹配后立即失效，或者超时失效）后，系统自动清除失效的令牌；
 * 当客户端并没有正确提交请求，会导致服务端令牌存根变为垃圾令牌，需要定时清除这些
 * 垃圾令牌；如果令牌存储在session中，那么令牌的生命周期和session的生命周期保持一致，无需额外清除机制；
 * 如果令牌存储在内存中，那么令牌的清除由令牌管理组件自己定时扫描清除，定时扫描时间间隔为由tokenscaninterval参数指定，单位为毫秒，默认为30分钟，存根保存时间由tokendualtime参数指定，默认为1个小时
 * 
 * 可以通过enableToken参数配置指定是否启用令牌检测机制，true检测，false不检测，默认为false不检测
 * 
 * @author biaoping.yin
 *
 */
public class TokenFilter implements Filter{
	private static Logger log = Logger.getLogger(TokenFilter.class);
	
	protected String redirectpath = "/login.jsp";
	private TokenService tokenService = null;
	
	/**
	 * tokenstore
	 * 指定令牌存储机制，目前提供两种机制：
	 * mem：将令牌直接存储在内存空间中
	 * session：将令牌存储在session中
	 * 默认存储在session中
	 */
//	protected String tokenstore = "mem";
	
	public void init(FilterConfig arg0) throws ServletException
	{
		try {
			tokenService = TokenHelper.getTokenService();
		} catch (Throwable e) {
			log.warn("",e);
		}
		
		String redirectpath_ =  arg0.getInitParameter("redirecturl");
		
//		String tokenstore_ = arg0.getInitParameter("tokenstore");
//		if(!StringUtil.isEmpty(tokenstore_))
//		{
//			if(tokenstore_.toLowerCase().equals("mem") || tokenstore_.toLowerCase().equals("session"))
//			{
//				tokenstore = tokenstore_.toLowerCase();
//				log.debug("Set tokenstore["+tokenstore_+"] success,tokens will be stored in memorey.");
//			}
//			else
//			{
//				tokenstore = tokenstore_.trim();
//				log.debug("Set tokenstore["+tokenstore_+"] success,tokens will be stored in "+tokenstore_+".");
//			}
//		}
		
		
		if(!StringUtil.isEmpty(redirectpath_))
		{
			redirectpath = redirectpath_; 
		}
		
		
//		else
//		{
//			tokenfailpath = redirectpath; 
//		}
		
		
//		String tmp = arg0.getServletContext().getServletContextName();
//		this.redirectpath = StringUtil.getRealPath(tmp, redirectpath);
		TokenHelper.setTokenFilter(this);
		
		
//MemTokenManagerFactory.getMemTokenManager(ticketdualtime_,temptokenlivetime_,dualtokenlivetime_,tokenscaninterval_,enableToken,this.tokenstore,this);
	}
	
	@Override
	public void destroy() {
		TokenHelper.destroy();
		tokenService = null;
		
	}
	@Override
	public void doFilter(ServletRequest arg0, ServletResponse arg1,
			FilterChain arg2) throws IOException, ServletException {
		try {
			if(!checkTokenExist((HttpServletRequest )arg0,(HttpServletResponse )arg1))//令牌检查，如果当前令牌已经失效则直接跳转到登录页，否则继续进行后去安全认证检查
			{
				return ;
			}
			else
			{				
				arg2.doFilter(arg0, arg1);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	protected boolean checkTokenExist(HttpServletRequest request,HttpServletResponse response) throws Exception
	{
		if(!this.tokenService.isEnableToken())//如果没有启用令牌机制，则直接声明令牌存在
			return true;
		if(!firstRequest(request))
		{
//			if(!response.isCommitted())
//			{
//				StringBuffer targetUrl = new StringBuffer();
//				if ( this.tokenfailpath.startsWith("/")) {
//					targetUrl.append(request.getContextPath());
//				}
//				targetUrl.append(this.tokenfailpath);
//				sendRedirect(request, response,targetUrl.toString(),  true,false,false);
//			}
			sendRedirect(request,
					response);
			return false;
		}
		else
		{
			return true;
		}
	}
	
	public void sendRedirect(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		if(!response.isCommitted())
		{
			if(this.tokenService.getTokenfailpath() != null)
			{
				StringBuffer targetUrl = new StringBuffer();
				if ( this.tokenService.getTokenfailpath().startsWith("/")) {
					targetUrl.append(request.getContextPath());
				}
				targetUrl.append(this.tokenService.getTokenfailpath());
				
				sendRedirect(request, response,targetUrl.toString(),  true,false,false);
			}
			else
			{
				sendRedirect403(request,response);
			}
		}
	}
	
	public void sendRedirect403(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		if(!response.isCommitted())
		{
			response.sendError(HttpServletResponse.SC_FORBIDDEN);
		}
	}
	protected String appendDTokenToTargetURL(HttpServletRequest request, String targetUrl) throws TokenException
	{
		if(this.tokenService.isEnableToken())
		{
			return tokenService.appendDTokenToURL(request, targetUrl);
		}
		else
		{
			return targetUrl;
		}
	}
	
	protected boolean isloopredirect(HttpServletRequest request,
			String targetUrl)
	{
		String fromurl = request.getRequestURI();
		int idx = targetUrl.indexOf("?");
		String comp = targetUrl;
		if(idx > 0)
		{
			comp = targetUrl.substring(0,idx);
		}
		if(fromurl.equals(comp))
			return true;
		return false;
	}
	protected void sendRedirect(HttpServletRequest request,
			HttpServletResponse response, String targetUrl,
			boolean http10Compatible,boolean isforward,boolean isinclude) throws IOException  {

		if(isloopredirect(request,
				targetUrl))
		{
			response.sendError(HttpServletResponse.SC_FORBIDDEN);
			return;
		}
		if(!isforward)
		{
			/**失效页面无需设置token*/
			//targetUrl = appendDTokenToTargetURL(request, targetUrl);
			if(!isinclude)
			{
				if (http10Compatible) {
					// Always send status code 302.
					response.sendRedirect(response.encodeRedirectURL(targetUrl));
				} else {
					// Correct HTTP status code is 303, in particular for POST requests.
					response.setStatus(303);
					response.setHeader("Location", response
							.encodeRedirectURL(targetUrl));
				}
			}
			else
			{
				 try
					{
						request.getRequestDispatcher(targetUrl).include(request, response);
					}
					catch (ServletException e)
					{
						log.error("", e);
					}
			}
		}
		else
		{
			 try
			{
				request.getRequestDispatcher(targetUrl).forward(request, response);
			}
			catch (ServletException e)
			{
				log.error("", e);
			}
		}
	}
	
	
	
	
	
	
	public String getRedirecturl() {

		return this.redirectpath;
	}

	public void setRedirecturl(String redirecturl) {

		this.redirectpath = redirecturl;
	}
	
	/**
	 * 判断令牌或者ticket是否有效，一次请求只判断一次，避免多次判断
	 * 同时记录判断结果，以便后续处理操作获取这个结果进行相应的处理
	 * @param request
	 * @return
	 */
	protected boolean firstRequest(ServletRequest request) 
	{
		Integer result = null;
		if(!this.tokenService.isEnableToken())
		{
			result = TokenStore.token_request_validateresult_notenabletoken;
			request.setAttribute(TokenStore.temptoken_request_validateresult_key,result);
			return true;
		}
		result = (Integer)request.getAttribute(TokenStore.temptoken_request_validateresult_key);//
		if(result != null)
		{
			return tokenService.assertDToken(result);
		}
		
		String token = request.getParameter(TokenStore.temptoken_param_name);
		String ticket = request.getParameter(TokenStore.ticket_param_name);
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
			
			
			
			try {
				if(token == null || token.equals(""))
				{
					if(ticket == null || ticket.equals(""))
						result = TokenStore.token_request_validateresult_nodtoken;
					else//校验ticket
					{
//						String appid= request.getParameter(TokenStore.app_param_name);
//						String secret= request.getParameter(TokenStore.app_secret_param_name);
						
						String appid= tokenService.getAppid();
						String secret= tokenService.getSecret();
						TokenResult tokenResult = this.tokenService.checkTicket(appid, secret, ticket);
						request.setAttribute(TokenStore.token_request_validatetoken_key, tokenResult);
						if( tokenResult != null )
						{
							if(tokenResult.getAccount() != null)
								request.setAttribute(TokenStore.token_request_account_key, tokenResult.getAccount());
							if(tokenResult.getWorknumber() != null)
								request.setAttribute(TokenStore.token_request_worknumber_key, tokenResult.getWorknumber());
						}
						result = tokenResult.getResult();
					}
				}
				else
				{
//					String appid= request.getParameter(TokenStore.app_param_name);
//					String secret= request.getParameter(TokenStore.app_secret_param_name);
					String appid= tokenService.getAppid();
					String secret= tokenService.getSecret();
					TokenResult tokenResult = this.tokenService.checkToken(appid,secret,token);
					request.setAttribute(TokenStore.token_request_validatetoken_key, tokenResult);
					if( tokenResult != null )
					{
						if(tokenResult.getAccount() != null)
							request.setAttribute(TokenStore.token_request_account_key, tokenResult.getAccount());
						if(tokenResult.getWorknumber() != null)
							request.setAttribute(TokenStore.token_request_worknumber_key, tokenResult.getWorknumber());
					}
					result = tokenResult.getResult();
				}
			} catch (Exception e) {
				log.error("令牌校验失败:",e);
				result = TokenStore.token_request_validateresult_fail;
			}
		}
		request.setAttribute(TokenStore.temptoken_request_validateresult_key,result);
		return 	tokenService.assertDToken(result);
	}
	
	
	public void doDTokencheck(ServletRequest request,ServletResponse response) throws IOException, DTokenValidateFailedException
	{
		if(!tokenService.assertDTokenSetted(request))
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
	
	public void doTicketcheck(ServletRequest request,ServletResponse response) throws IOException, DTokenValidateFailedException
	{
		if(!tokenService.assertDTokenSetted(request))
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
