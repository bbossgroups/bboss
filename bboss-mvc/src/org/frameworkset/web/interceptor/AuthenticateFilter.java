package org.frameworkset.web.interceptor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.frameworkset.util.AntPathMatcher;
import org.frameworkset.util.PathMatcher;
import org.frameworkset.web.servlet.handler.HandlerMeta;
import org.frameworkset.web.util.UrlPathHelper;
import org.frameworkset.web.util.WebUtils;

import com.frameworkset.util.StringUtil;

/**
 * <p>
 * AuthenticateFilter.java
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * bboss workgroup
 * </p>
 * <p>
 * Copyright (c) 2009
 * </p>
 * 
 * @Date 2011-5-31
 * @author biaoping.yin
 * @version 1.0
 */
public abstract class AuthenticateFilter  implements Filter {
	public static final String accesscontrol_check_result = "com.frameworkset.platform.security.accesscontrol_check_result";
	public static final String accesscontrol_check_result_ok = "ok";
	public static final String accesscontrol_check_result_fail = "fail";
	protected boolean preventDispatchLoop = false;
	protected boolean http10Compatible = true;
	protected String redirecturl = "/login.jsp";
	public static String REDIRECT = "redirect";
	public static String FORWARD = "forward";
	public static String INCLUDE = "include";
	
	protected boolean isforward = false;
	protected boolean isinclude = false;
	
	/**
	 * redirect
	 * forward
	 */
	protected String directtype = "redirect";
	protected boolean contextRelative = true;
	protected boolean exposeModelAttributes = false;
	protected List<String> patternsInclude;
	protected List<String> patternsExclude;
	protected UrlPathHelper urlPathHelper = new UrlPathHelper();
	

	protected PathMatcher pathMatcher = new AntPathMatcher();

	protected String encodingScheme;
	private static Logger logger = Logger.getLogger(AuthenticateFilter.class);
	
	protected boolean needCheck(String path)
	{
		boolean needcheck = false;
		if(this.patternsExclude == null && this.patternsExclude == null)
			return true;
		if(this.patternsInclude != null)
		{
			for(String pattern:this.patternsInclude)
			{
				if(this.pathMatcher.match(pattern, path))
				{
					needcheck = true;
					break;
				}
			}
			if(needcheck && patternsExclude != null)
			{
				for(String pattern:this.patternsExclude)
				{
					if(this.pathMatcher.match(pattern, path))
					{
						needcheck = false;
						break;
					}
				}
			}
			
		}
		else if(patternsExclude != null)
		{
			needcheck = true;
			for(String pattern:this.patternsExclude)
			{
				if(this.pathMatcher.match(pattern, path))
				{
					needcheck = false;
					break;
				}
			}
		}
		else
			needcheck = true;
		
		return needcheck;
	}
	protected String prepareForRendering(HttpServletRequest request,
			HttpServletResponse response,String uripathwithnocontextpath) throws Exception {

		String path = this.getRedirecturl();
		if (this.preventDispatchLoop) {
			String uri = uripathwithnocontextpath;
			if (path.startsWith("/") ? uri.equals(path) : uri.equals(StringUtil
					.applyRelativePath(uri, path))) {
				throw new ServletException(
						"Circular view path ["
								+ path
								+ "]: would dispatch back "
								+ "to the current handler URL ["
								+ uri
								+ "] again. Check your ViewResolver setup! "
								+ "(Hint: This may be the result of an unspecified view, due to default view name generation.)");
			}
		}
		return path;
	}
	protected abstract boolean check(HttpServletRequest request,
			HttpServletResponse response, HandlerMeta handlerMeta); 
	protected String getPathUrl(HttpServletRequest request)
	{
		String requesturipath = WebUtils.getHANDLER_Mappingpath(request);
		if(requesturipath != null)
			return requesturipath;
		String contextpath = request.getContextPath();
    	requesturipath = request.getRequestURI();
    	requesturipath = requesturipath.substring(contextpath.length());
    	return requesturipath;
	}
    protected boolean _preHandle(HttpServletRequest request,
			HttpServletResponse response, HandlerMeta handlerMeta)
	throws Exception
	{
//    	String requesturipath = WebUtils.getHANDLER_Mappingpath(request);
    	String requesturipath = getPathUrl(request);
		//做控制逻辑检测，如果检测失败，则执行下述逻辑，否则执行正常的控制器方法		
		if(needCheck(requesturipath) )
		{			
			boolean checkresult = check(request,
					response, handlerMeta);
			if(!checkresult)
			{
				request.setAttribute(accesscontrol_check_result, accesscontrol_check_result_fail);
				if(!response.isCommitted())
				{
					String dispatcherPath = prepareForRendering(request, response,requesturipath);
					StringBuffer targetUrl = new StringBuffer();
					if (!this.isforward() && !this.isinclude && this.contextRelative && dispatcherPath.startsWith("/")) {
						targetUrl.append(request.getContextPath());
					}
					targetUrl.append(dispatcherPath);
					
					sendRedirect(request, response, targetUrl.toString(), http10Compatible);
				}
				return false;
			}
			else
			{
				request.setAttribute(accesscontrol_check_result, accesscontrol_check_result_ok);
				return true;
			}
		}
		else
		{
			request.setAttribute(accesscontrol_check_result, accesscontrol_check_result_ok);
			return true;
		}
		
	}
	
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, HandlerMeta handlerMeta)
			throws Exception {
		String result = (String)request.getAttribute(accesscontrol_check_result);
		if(result == null)
		{
			return _preHandle(request,
					response, handlerMeta);
		}
		else if(result.equals(accesscontrol_check_result_ok))
		{
			return true;
		}
		else 
//			if(result.equals(accesscontrol_check_result_ok))
		{
			return false;
		}
		
		
	}
	
	protected boolean isforward()
	{
		return isforward;
	}

	

	

	

	/**
	 * Send a redirect back to the HTTP client
	 * 
	 * @param request
	 *            current HTTP request (allows for reacting to request method)
	 * @param response
	 *            current HTTP response (for sending response headers)
	 * @param targetUrl
	 *            the target URL to redirect to
	 * @param http10Compatible
	 *            whether to stay compatible with HTTP 1.0 clients
	 * @throws IOException
	 *             if thrown by response methods
	 */
	protected void sendRedirect(HttpServletRequest request,
			HttpServletResponse response, String targetUrl,
			boolean http10Compatible) throws IOException {

		if(!this.isforward())
		{
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
						// TODO Auto-generated catch block
						e.printStackTrace();
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
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		// TODO Auto-generated method stub

	}

	public boolean isPreventDispatchLoop() {

		return preventDispatchLoop;
	}

	public void setPreventDispatchLoop(boolean preventDispatchLoop) {

		this.preventDispatchLoop = preventDispatchLoop;
	}

	public String getRedirecturl() {

		return redirecturl;
	}

	public void setRedirecturl(String redirecturl) {

		this.redirecturl = redirecturl;
	}

	public boolean isHttp10Compatible() {

		return http10Compatible;
	}

	public void setHttp10Compatible(boolean http10Compatible) {

		this.http10Compatible = http10Compatible;
	}

	/**
	 * @return the patternsInclude
	 */
	public List<String> getPatternsInclude() {
		return patternsInclude;
	}

	/**
	 * @param patternsInclude
	 *            the patternsInclude to set
	 */
	public void setPatternsInclude(List<String> patternsInclude) {
		this.patternsInclude = patternsInclude;
	}

	/**
	 * @return the patternsExclude
	 */
	public List<String> getPatternsExclude() {
		return patternsExclude;
	}

	/**
	 * @param patternsExclude
	 *            the patternsExclude to set
	 */
	public void setPatternsExclude(List<String> patternsExclude) {
		this.patternsExclude = patternsExclude;
	}

	
	public String getDirecttype()
	{
	
		return directtype;
	}

	
	public void setDirecttype(String directtype)
	{
		
		this.directtype = directtype;
		if(this.directtype != null && this.directtype.equals(FORWARD))
		{
			this.isforward = true;
		}
		else if(this.directtype != null && this.directtype.equals(INCLUDE))
		{
			this.isinclude = true;
		}
		
	}
	
	/*************Filter接口实现开始********************/
	public void destroy() {
		
		
	}

	public void doFilter(ServletRequest arg0, ServletResponse arg1,
			FilterChain arg2) throws IOException, ServletException {
		try {
			if(arg0 instanceof HttpServletRequest)
			{
				boolean result = preHandle((HttpServletRequest)arg0, (HttpServletResponse)arg1, null);
				if(result)
				{
					arg2.doFilter(arg0, arg1);
				}
			}
			else
			{
				arg2.doFilter(arg0, arg1);
			}
		}
		 catch (IOException e) {
				// TODO Auto-generated catch block
				throw e;
			}
		catch (ServletException e) {
			// TODO Auto-generated catch block
			throw e;
		}
		catch (Throwable e) {
			// TODO Auto-generated catch block
			throw new ServletException(e);
		}
		
	}

	public void init(FilterConfig arg0) throws ServletException {
		String preventDispatchLoop = arg0.getInitParameter("preventDispatchLoop");
		if(preventDispatchLoop != null && preventDispatchLoop.equals("true"))
		{
			setPreventDispatchLoop(true);
		}
		String redirecturl = arg0.getInitParameter("redirecturl");
		
		if(redirecturl != null && !redirecturl.equals(""))
		{
			setRedirecturl(redirecturl);
		}
		String http10Compatible = arg0.getInitParameter("http10Compatible");
		if(http10Compatible != null && http10Compatible.equals("false"))
		{
			setHttp10Compatible(false);
		}
		String patternsInclude = arg0.getInitParameter("patternsInclude");
		if(patternsInclude != null && !patternsInclude.trim().equals(""))
		{
			String[] ips = patternsInclude.split(",");
			setPatternsInclude(convertArrayToList(ips));
		}
		String patternsExclude = arg0.getInitParameter("patternsExclude");
		if(patternsExclude != null && !patternsExclude.trim().equals(""))
		{
			String[] ips = patternsExclude.split(",");
			setPatternsExclude(convertArrayToList(ips));
		}
		String directtype = arg0.getInitParameter("directtype");
		if(directtype != null && !directtype.equals(""))
		{
			setDirecttype(directtype);
		}
		
	}
	
	private List<String> convertArrayToList(String[] arrays)
	{
		List<String> rets = new ArrayList<String>(arrays.length);
		for(String value:arrays)
		{
			if(!value.trim().equals(""))
			{
				rets.add(value.trim());
			}
		}
		return rets;
	} 
	
	/*************Filter接口实现结束********************/
}
