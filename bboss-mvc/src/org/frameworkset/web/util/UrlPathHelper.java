package org.frameworkset.web.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.frameworkset.util.StringUtil;

public class UrlPathHelper
{

  /** @deprecated */
  public static final String INCLUDE_URI_REQUEST_ATTRIBUTE = "javax.servlet.include.request_uri";

  /** @deprecated */
  public static final String INCLUDE_CONTEXT_PATH_REQUEST_ATTRIBUTE = "javax.servlet.include.context_path";

  /** @deprecated */
  public static final String INCLUDE_SERVLET_PATH_REQUEST_ATTRIBUTE = "javax.servlet.include.servlet_path";
  private final Logger logger = Logger.getLogger(getClass());

  private boolean alwaysUseFullPath = true;

  private boolean urlDecode = true;

  private String defaultEncoding = "ISO-8859-1";

  public void setAlwaysUseFullPath(boolean alwaysUseFullPath)
  {
    this.alwaysUseFullPath = alwaysUseFullPath;
  }

  public void setUrlDecode(boolean urlDecode)
  {
    this.urlDecode = urlDecode;
  }

  public void setDefaultEncoding(String defaultEncoding)
  {
    this.defaultEncoding = defaultEncoding;
  }

  protected String getDefaultEncoding()
  {
    return this.defaultEncoding;
  }

  public String getLookupPathForRequest(HttpServletRequest request)
  {
    if (this.alwaysUseFullPath) {
      return getPathWithinApplication(request);
    }

    String rest = getPathWithinServletMapping(request);
    if (!"".equals(rest)) {
      return rest;
    }

    return getPathWithinApplication(request);
  }

  public String getPathWithinServletMapping(HttpServletRequest request)
  {
    String pathWithinApp = getPathWithinApplication(request);
    String servletPath = getServletPath(request);
    if (pathWithinApp.startsWith(servletPath))
    {
      return pathWithinApp.substring(servletPath.length());
    }

    return servletPath;
  }

  private static final String requestPathWithinApplicationcachkey = "request.pathWithinApplication.cachkey";
  public String getPathWithinApplication(HttpServletRequest request)
  {
	  //解决上下文为/时，mvc映射请求无效问题注释开始
//    String contextPath = getContextPath(request);
//    String requestUri = getRequestUri(request);
//    
//    if (StringUtil.startsWithIgnoreCase(requestUri, contextPath))
//    {
//      String path = requestUri.substring(contextPath.length());
//      return !StringUtil.isEmpty((String)path) ? path : "/";
//    }
//
//    return requestUri;
	//解决上下文为/时，mvc映射请求无效问题注释完毕 20150122
	  String ret = (String)request.getAttribute(requestPathWithinApplicationcachkey);
	  String servletpath = request.getServletPath();
	  /**
	   * ret在以下情况与servletpath不相等
	   * 场景1 index.jsp forword to /index.htm时，在处理/index.htm时 servletpath为/index.htm而ret为index.jsp，所以需要重新计算地址
	   * 场景2 mvc dispatcher拦截了地址模式类似于 /rest/*的请求时，servletpath为/rest,而ret为具体的地址/rest/people/1 这样每次都不会相等，每次都需要重新计算地址
	   * 
	   */
	  if(ret != null && ret.equals(servletpath)) 
	  
		  return ret;
	  String contextPath = getContextPath(request);
	  String requestUri = getRequestUri(request);
	    
	    if (StringUtil.startsWithIgnoreCase(requestUri, contextPath))
	    {
	      String path = requestUri.substring(contextPath.length());
	      requestUri = path ;
	    }
	    else
	    {
	    	
	    }
	    if(requestUri.equals("/"))
	    {
//	    	String servletpath = request.getServletPath();
	    	if(servletpath != null && !servletpath.equals(""))
	    		requestUri = servletpath;
	    }
	    request.setAttribute(requestPathWithinApplicationcachkey, requestUri);
	    return requestUri;
  }

  public String getRequestUri(HttpServletRequest request)
  {
//    String uri = (String)request.getAttribute("javax.servlet.include.request_uri");
//    if (uri == null) {
//      uri = request.getRequestURI();
//    }
//    
//    String path = request.getServletPath();
//    return decodeAndCleanUriString(request, uri);
	  return request.getRequestURI();
  }

  public String getContextPath(HttpServletRequest request)
  {
//    String contextPath = (String)request.getAttribute("javax.servlet.include.context_path");
//    if (contextPath == null) {
//      contextPath = request.getContextPath();
//    }
//    if ("/".equals(contextPath))
//    {
//      contextPath = "";
//    }
//    return decodeRequestString(request, contextPath);
	String  contextPath = request.getContextPath();
	  if ("/".equals(contextPath))
	  {
	    contextPath = "";
	  }
    return contextPath;

  }

  public String getServletPath(HttpServletRequest request)
  {
//    String servletPath = (String)request.getAttribute("javax.servlet.include.servlet_path");
//    if (servletPath == null) {
//      servletPath = request.getServletPath();
//    }
//    return servletPath;
	  return request.getServletPath();
  }

  public String getOriginatingRequestUri(HttpServletRequest request)
  {
//    String uri = (String)request.getAttribute("javax.servlet.forward.request_uri");
//    if (uri == null) {
//      uri = request.getRequestURI();
//    }    
//    return decodeAndCleanUriString(request, uri);
  	String uri = request.getRequestURI();
    return uri;
  }

  public String getOriginatingContextPath(HttpServletRequest request)
  {
//    String contextPath = (String)request.getAttribute("javax.servlet.forward.context_path");
//    if (contextPath == null) {
//      contextPath = request.getContextPath();
//    }
//    return decodeRequestString(request, contextPath);
	  return request.getContextPath();
  }

  public String getOriginatingQueryString(HttpServletRequest request)
  {
    String queryString = (String)request.getAttribute("javax.servlet.forward.query_string");
    if (queryString == null) {
      queryString = request.getQueryString();
    }
    return queryString;
  }

  private String decodeAndCleanUriString(HttpServletRequest request, String uri)
  {
    uri = decodeRequestString(request, uri);
    int semicolonIndex = uri.indexOf(';');
    return semicolonIndex != -1 ? uri.substring(0, semicolonIndex) : uri;
  }

  public String decodeRequestString(HttpServletRequest request, String source)
  {
    if (this.urlDecode) {
      String enc = determineEncoding(request);
      try {
        return URLDecoder.decode(source, enc);
      }
      catch (UnsupportedEncodingException ex)
      {
        this.logger.warn("Could not decode request string [" + source + "] with encoding '" + enc + "': falling back to platform default encoding; exception message: " + ex.getMessage());

        return URLDecoder.decode(source);
      }
    }
    return source;
  }

  protected String determineEncoding(HttpServletRequest request)
  {
    String enc = request.getCharacterEncoding();
    if (enc == null) {
      enc = getDefaultEncoding();
    }
    return enc;
  }
}