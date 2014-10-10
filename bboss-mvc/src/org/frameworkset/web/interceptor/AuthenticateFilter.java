package org.frameworkset.web.interceptor;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

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
import org.frameworkset.web.token.TokenFilter;
import org.frameworkset.web.token.TokenStore;
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
public abstract class AuthenticateFilter extends TokenFilter{
	public static final String accesscontrol_check_result = "com.frameworkset.platform.security.accesscontrol_check_result";
	public static final String accesscontrol_check_result_ok = "ok";
	public static final String accesscontrol_check_result_free = "free";
	public static final String accesscontrol_check_result_fail = "fail";
	public static final String referpath_parametername = "accesscontrol_check_referpath";
	
	
	public static final String accesscontrol_permissioncheck_result = "com.frameworkset.platform.security.accesscontrol_permissioncheck_result";
	public static final String accesscontrol_permissioncheck_result_ok = "ok";
	public static final String accesscontrol_permissioncheck_result_fail = "fail";
	protected boolean preventDispatchLoop = false;
	protected boolean http10Compatible = true;
	
	
	public static String REDIRECT = "redirect";
	public static String FORWARD = "forward";
	public static String INCLUDE = "include";
	
	protected boolean isforward = false;
	protected boolean isinclude = false;
	
	/**
	 * redirect
	 * forward
	 * include
	 */
	protected String directtype = "redirect";
	protected boolean contextRelative = true;
	protected boolean exposeModelAttributes = false;
	protected List<String> patternsInclude;
	protected List<String> patternsExclude;
	protected UrlPathHelper urlPathHelper = new UrlPathHelper();
	
	protected boolean enablePermissionCheck;
	protected List<String> permissionExclude;
	protected List<String> permissionInclude;
	protected String authorfailedurl = "/authorfailedurl.jsp";
	/**
	 * redirect
	 * forward
	 * include
	 * 权限检测定向方法暂时不用，和认证检测公用directtype属性
	 */
	protected String permissiondirecttype = "redirect";
	/**
	 * 是否启用认证失败重新登录后仍然回到请求页面
	 */
	protected boolean failedback = false;
	/**
	 * 指定需要failedback的url地址模式，只有符合相关的模式，这个地址才能被failedback
	 */
	protected String[] failedbackurlpattern = null;
	public String getPermissiondirecttype() {
		return permissiondirecttype;
	}


	public void setPermissiondirecttype(String permissiondirecttype) {
		this.permissiondirecttype = permissiondirecttype;
	}
	protected PathMatcher pathMatcher = new AntPathMatcher();

	protected String encodingScheme;
	private static Logger logger = Logger.getLogger(AuthenticateFilter.class);
	
	protected boolean needCheck(String path)
	{
//		boolean needcheck = false;
//		if(this.patternsExclude == null && this.patternsExclude == null)
//			return true;
//		if(this.patternsInclude != null)
//		{
//			for(String pattern:this.patternsInclude)
//			{
//				if(this.pathMatcher.match(pattern, path))
//				{
//					needcheck = true;
//					break;
//				}
//			}
//			if(needcheck && patternsExclude != null)
//			{
//				for(String pattern:this.patternsExclude)
//				{
//					if(this.pathMatcher.match(pattern, path))
//					{
//						needcheck = false;
//						break;
//					}
//				}
//			}
//			
//		}
//		else if(patternsExclude != null)
//		{
//			needcheck = true;
//			for(String pattern:this.patternsExclude)
//			{
//				if(this.pathMatcher.match(pattern, path))
//				{
//					needcheck = false;
//					break;
//				}
//			}
//		}
//		else
//			needcheck = true;
//		
//		return needcheck;
		return needCheck(path,patternsExclude,patternsInclude);
	}
	
	
	protected boolean needCheck(String path,List<String> patternsExclude,List<String> patternsInclude)
	{
		boolean needcheck = false;
		if(patternsExclude == null && patternsExclude == null)
			return true;
		if(patternsInclude != null)
		{
			for(String pattern:patternsInclude)
			{
				if(this.pathMatcher.match(pattern, path))
				{
					needcheck = true;
					break;
				}
			}
			if(needcheck && patternsExclude != null)
			{
				for(String pattern:patternsExclude)
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
			for(String pattern:patternsExclude)
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

//		String path = this.getRedirecturl();
//		if (this.preventDispatchLoop) {
//			String uri = uripathwithnocontextpath;
//			if (path.startsWith("/") ? uri.equals(path) : uri.equals(StringUtil
//					.applyRelativePath(uri, path))) {
//				throw new ServletException(
//						"Circular view path ["
//								+ path
//								+ "]: would dispatch back "
//								+ "to the current handler URL ["
//								+ uri
//								+ "] again. Check your ViewResolver setup! "
//								+ "(Hint: This may be the result of an unspecified view, due to default view name generation.)");
//			}
//		}
//		return path;
		return prepareForRendering(request,response,uripathwithnocontextpath,this.getRedirecturl());
	}
	
	protected String preparePermissionForRendering(HttpServletRequest request,
			HttpServletResponse response,String uripathwithnocontextpath) throws Exception {

//		String path = this.getRedirecturl();
//		if (this.preventDispatchLoop) {
//			String uri = uripathwithnocontextpath;
//			if (path.startsWith("/") ? uri.equals(path) : uri.equals(StringUtil
//					.applyRelativePath(uri, path))) {
//				throw new ServletException(
//						"Circular view path ["
//								+ path
//								+ "]: would dispatch back "
//								+ "to the current handler URL ["
//								+ uri
//								+ "] again. Check your ViewResolver setup! "
//								+ "(Hint: This may be the result of an unspecified view, due to default view name generation.)");
//			}
//		}
//		return path;
		return prepareForRendering(request,response,uripathwithnocontextpath,this.getAuthorfailedurl());
	}
	
	protected String prepareForRendering(HttpServletRequest request,
			HttpServletResponse response,String uripathwithnocontextpath,String renderpath) throws Exception {

		String path = renderpath;
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
	protected abstract boolean checkPermission(HttpServletRequest request,
			HttpServletResponse response, HandlerMeta handlerMeta,String uri); 
	protected String getPathUrl(HttpServletRequest request)
	{
//		String requesturipath = WebUtils.getHANDLER_Mappingpath(request);
//		if(requesturipath != null)
//			return requesturipath;
//		String contextpath = request.getContextPath();
//    	requesturipath = request.getRequestURI();
//    	requesturipath = requesturipath.substring(contextpath.length());
//    	return requesturipath;
		return UrlPathHelper.getPathWithinApplication(request);
	}
	private boolean needfailedback(String uri)
	{
		if(this.failedbackurlpattern == null || this.failedbackurlpattern.length == 0)
		{
			return true;
		}
		for(String pattern:this.failedbackurlpattern)
		{
			if(this.pathMatcher.match(pattern, uri))
				return true;
		}
		return false;
	}
	private void appendReferBackPath(HttpServletRequest request,StringBuffer path,boolean hasParams) throws UnsupportedEncodingException
	{
//		String referer = request.getHeader("Referer");
		String uri = UrlPathHelper.getPathWithinApplication(request);
		if(!needfailedback(uri)) 
			return;
		
		
		StringBuffer referer = new StringBuffer();
		referer.append(request.getRequestURI());
		Enumeration<String> names = request.getParameterNames();
		boolean first = true;
		while(names.hasMoreElements())
		{
			String name = names.nextElement();
			String[] values = request.getParameterValues(name);
			for(int i = 0; values != null && i < values.length; i ++)
			{
				if(name.equals(TokenStore.temptoken_param_name))//忽略令牌参数
					continue;
				if(first)
				{
					referer.append("?").append(name).append("=").append(values[i]);
					first = false;
				}
				else
				{
					referer.append("&").append(name).append("=").append(values[i]);						
				}
			}
			
		}
		if(hasParams)
			path.append("&").append(referpath_parametername).append("=").append(java.net.URLEncoder.encode(referer.toString(), "UTF-8"));
		else
			path.append("?").append(referpath_parametername).append("=").append(java.net.URLEncoder.encode(referer.toString(), "UTF-8"));
		
	}
    protected boolean _preHandle(HttpServletRequest request,
			HttpServletResponse response, HandlerMeta handlerMeta)
	throws Exception
	{
//    	String requesturipath = WebUtils.getHANDLER_Mappingpath(request);
    	String requesturipath = getPathUrl(request);
		//做控制逻辑检测，如果检测失败，则执行下述逻辑，否则执行正常的控制器方法	
    	boolean checkresult = check(request,
				response, handlerMeta);//在有些情况下对匿名用户允许访问的开放地址也需要设置已经登录过的会话信息，所以不管页面是否需要做认证保护，都需要执行会话对象设置动作
    	boolean neadcheck = needCheck(requesturipath);
//    	if(checkresult)
//    	{
//    		
//    		request.setAttribute(accesscontrol_check_result, accesscontrol_check_result_ok);
//			return true;
//    	}
		if(neadcheck )
		{			
			
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
					if(failedback)
						this.appendReferBackPath(request, targetUrl, dispatcherPath != null?dispatcherPath.indexOf("?")>=0:false);
					sendRedirect(request, response, targetUrl.toString(), http10Compatible,this.isforward(),this.isinclude);
				}
				return false;
			}
			else
			{
				request.setAttribute(accesscontrol_check_result, accesscontrol_check_result_ok);
				return true;
			}
		}
		else //无需认证的地址，直接设置允许访问标识以及授权成功标识
		{
			request.setAttribute(accesscontrol_check_result, accesscontrol_check_result_ok);
			request.setAttribute(accesscontrol_permissioncheck_result, accesscontrol_permissioncheck_result_ok);
			return true;
		}
		
	}
    
    
    protected boolean _permissionHandle(HttpServletRequest request,
			HttpServletResponse response, HandlerMeta handlerMeta)
	throws Exception
	{
//    	String requesturipath = WebUtils.getHANDLER_Mappingpath(request);
    	String requesturipath = getPathUrl(request);
		//做控制逻辑检测，如果检测失败，则执行下述逻辑，否则执行正常的控制器方法		
		if(needCheck(requesturipath,this.permissionExclude,this.permissionInclude) )
		{			
			boolean checkresult = checkPermission(request,
					response, handlerMeta,requesturipath);
			if(!checkresult)
			{
				request.setAttribute(accesscontrol_permissioncheck_result, accesscontrol_permissioncheck_result_fail);
				if(!response.isCommitted())
				{
					String dispatcherPath = this.preparePermissionForRendering(request, response,requesturipath);
					StringBuffer targetUrl = new StringBuffer();
					if (!this.isforward() && !this.isinclude && this.contextRelative && dispatcherPath.startsWith("/")) {
						targetUrl.append(request.getContextPath());
					}
					targetUrl.append(dispatcherPath);
					
					sendRedirect(request, response, targetUrl.toString(), http10Compatible,this.isforward(),this.isinclude);
				}
				return false;
			}
			else
			{
				request.setAttribute(accesscontrol_permissioncheck_result, accesscontrol_permissioncheck_result_ok);
				return true;
			}
		}
		else
		{
			request.setAttribute(accesscontrol_permissioncheck_result, accesscontrol_permissioncheck_result_ok);
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
	public boolean prepermissionHandle(HttpServletRequest request,
			HttpServletResponse response, HandlerMeta handlerMeta)
			throws Exception {
		String result = (String)request.getAttribute(accesscontrol_permissioncheck_result);
		if(result == null)
		{
			return _permissionHandle(request,
					response, handlerMeta);
		}
		else if(result.equals(accesscontrol_permissioncheck_result_ok))
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
				if(!checkTokenExist((HttpServletRequest )arg0,(HttpServletResponse )arg1))//令牌检查，如果当前令牌已经失效则直接跳转到登录页，否则继续进行后去安全认证检查
				{
					return ;
				}
				boolean result = preHandle((HttpServletRequest)arg0, (HttpServletResponse)arg1, null);//认证检测
				if(result)
				{
					if(!this.isEnablePermissionCheck())//如果没有启用权限检测则忽略页面权限检测，继续后续处理流程
						arg2.doFilter(arg0, arg1);
					else
					{
						result = prepermissionHandle((HttpServletRequest)arg0, (HttpServletResponse)arg1, null);//权限检测
						if(result)
							arg2.doFilter(arg0, arg1);
					}
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
		super.init(arg0);
		
		String preventDispatchLoop = arg0.getInitParameter("preventDispatchLoop");
		if(preventDispatchLoop != null && preventDispatchLoop.equals("true"))
		{
			setPreventDispatchLoop(true);
		}
		;
		String failedback = arg0.getInitParameter("failedback");
		
		if(failedback != null && failedback.equals("true"))
		{
			this.setFailedback(true);
		}
		
		String failedbackurlpattern = arg0.getInitParameter("failedbackurlpattern");
		
		if(!StringUtil.isEmpty(failedbackurlpattern))
		{
			this.setFailedbackurlpattern(failedbackurlpattern);
		}
		String authorfailedurl = arg0.getInitParameter("authorfailedurl");
		
		if(authorfailedurl != null && !authorfailedurl.equals(""))
		{
			setAuthorfailedurl(authorfailedurl);
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
		
//		protected boolean enablePermissionCheck;
		String enablePermissionCheck = arg0.getInitParameter("enablePermissionCheck");
		if(enablePermissionCheck != null && enablePermissionCheck.equals("true"))
		{
			setEnablePermissionCheck(true);
		}
//		protected List<String> permissionExclude;
//		protected List<String> permissionInclude;
		
		String permissionInclude = arg0.getInitParameter("permissionInclude");
		if(permissionInclude != null && !permissionInclude.trim().equals(""))
		{
			String[] ips = patternsInclude.split(",");
			setPermissionInclude(convertArrayToList(ips));
		}
		String permissionExclude = arg0.getInitParameter("permissionExclude");
		if(permissionExclude != null && !permissionExclude.trim().equals(""))
		{
			String[] ips = patternsExclude.split(",");
			setPermissionExclude(convertArrayToList(ips));
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
	public boolean isEnablePermissionCheck() {
		return enablePermissionCheck;
	}
	public void setEnablePermissionCheck(boolean enablePermissionCheck) {
		this.enablePermissionCheck = enablePermissionCheck;
	}
	public List<String> getPermissionExclude() {
		return permissionExclude;
	}
	public void setPermissionExclude(List<String> permissionExclude) {
		this.permissionExclude = permissionExclude;
	}
	public List<String> getPermissionInclude() {
		return permissionInclude;
	}
	public void setPermissionInclude(List<String> permissionInclude) {
		this.permissionInclude = permissionInclude;
	}
	public String getAuthorfailedurl() {
		return authorfailedurl;
	}
	public void setAuthorfailedurl(String authorfailedurl) {
		this.authorfailedurl = authorfailedurl;
	}


	public boolean isFailedback() {
		return failedback;
	}


	public void setFailedback(boolean failedback) {
		this.failedback = failedback;
	}


	public String[] getFailedbackurlpattern() {
		return failedbackurlpattern;
	}


	public void setFailedbackurlpattern(String failedbackurlpattern_) {
		String[] failedbackurlpatterns = failedbackurlpattern_.split("\\,");
		List temp = new ArrayList();
		for(int i = 0; i < failedbackurlpatterns.length; i ++)
		{
			String t = failedbackurlpatterns[i].trim();
			if(!t.equals("")){
				temp.add(t);
			}
		}
		String[] as = new String[temp.size()];
		this.failedbackurlpattern = (String[]) temp.toArray(as);
	} 
	
	/*************Filter接口实现结束********************/
}
