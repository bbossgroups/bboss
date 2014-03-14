/*****************************************************************************
 *                                                                           *
 *  This file is part of the tna framework distribution.                     *
 *  Documentation and updates may be get from  biaoping.yin the author of    *
 *  this framework							     *
 *                                                                           *
 *  Sun Public License Notice:                                               *
 *                                                                           *
 *  The contents of this file are subject to the Sun Public License Version  *
 *  1.0 (the "License"); you may not use this file except in compliance with *
 *  the License. A copy of the License is available at http://www.sun.com    *
 *                                                                           *
 *  The Original Code is tag. The Initial Developer of the Original          *
 *  Code is biaoping yin. Portions created by biaoping yin are Copyright     *
 *  (C) 2000.  All Rights Reserved.                                          *
 *                                                                           *
 *  GNU Public License Notice:                                               *
 *                                                                           *
 *  Alternatively, the contents of this file may be used under the terms of  *
 *  the GNU Lesser General Public License (the "LGPL"), in which case the    *
 *  provisions of LGPL are applicable instead of those above. If you wish to *
 *  allow use of your version of this file only under the  terms of the LGPL *
 *  and not to allow others to use your version of this file under the SPL,  *
 *  indicate your decision by deleting the provisions above and replace      *
 *  them with the notice and other provisions required by the LGPL.  If you  *
 *  do not delete the provisions above, a recipient may use your version of  *
 *  this file under either the SPL or the LGPL.                              *
 *                                                                           *
 *  biaoping.yin (yin-bp@163.com)                                            *
 *  Author of Learning Java 						     *
 *                                                                           *
 *****************************************************************************/
package com.frameworkset.common.tag;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletInputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.turbine.services.jsp.JspService;
import org.apache.turbine.util.RunData;

/**
 * To change for your class or interface
 *
 * @author biaoping.yin
 * @version 1.0
 */
public class CommonRequest implements HttpServletRequest {
    public CommonRequest() {
      
    }

    private static Logger log = Logger.getLogger(CommonRequest.class);
	protected HttpServletRequest request = null;
	protected RunData rundata = null;
	protected String encode = null;
    protected String mobileEncode = null;

    public void setMobileEncode(String mobileEncode)
    {
        this.mobileEncode = mobileEncode;
    }
    
    public HttpServletRequest getInternalrequest()
    {
    	return this.request;
    }

	public CommonRequest(RunData rundata,HttpServletRequest request)
	{
		this.rundata = rundata;
		this.request = request;
	}

	public CommonRequest(HttpServletRequest request)
	{
		this.rundata = (RunData)request.getAttribute(JspService.RUNDATA);
		this.request = request;

	}
	/**
	 *  Description:
	 * @return String
	 * @see javax.servlet.http.HttpServletRequest#getAuthType()
	 */
	public String getAuthType() {
		// TODO Auto-generated method stub
		return request.getAuthType();
	}

	/**
	 *  Description:
	 * @return Cookie[]
	 * @see javax.servlet.http.HttpServletRequest#getCookies()
	 */
	public Cookie[] getCookies() {
		// TODO Auto-generated method stub
		return request.getCookies();
	}

	/**
	 *  Description:
	 * @param arg0
	 * @return DateHeader
	 * @see javax.servlet.http.HttpServletRequest#getDateHeader(java.lang.String)
	 */
	public long getDateHeader(String arg0) {
		// TODO Auto-generated method stub
		return request.getDateHeader(arg0);
	}

	/**
	 *  Description:
	 * @param arg0
	 * @return String
	 * @see javax.servlet.http.HttpServletRequest#getHeader(java.lang.String)
	 */
	public String getHeader(String arg0) {
		// TODO Auto-generated method stub
		return request.getHeader(arg0);
	}

	/**
	 *  Description:
	 * @param arg0
	 * @return Enumeration
	 * @see javax.servlet.http.HttpServletRequest#getHeaders(java.lang.String)
	 */
	public Enumeration getHeaders(String arg0) {
		// TODO Auto-generated method stub
		return request.getHeaders(arg0);
	}

	/**
	 *  Description:
	 * @return Enumeration
	 * @see javax.servlet.http.HttpServletRequest#getHeaderNames()
	 */
	public Enumeration getHeaderNames() {
		// TODO Auto-generated method stub
		return request.getHeaderNames();
	}

	/**
	 *  Description:
	 * @param arg0
	 * @return int
	 * @see javax.servlet.http.HttpServletRequest#getIntHeader(java.lang.String)
	 */
	public int getIntHeader(String arg0) {
		// TODO Auto-generated method stub
		return request.getIntHeader(arg0);
	}

	/**
	 *  Description:
	 * @return String
	 * @see javax.servlet.http.HttpServletRequest#getMethod()
	 */
	public String getMethod() {
		// TODO Auto-generated method stub
		return request.getMethod();
	}

	/**
	 *  Description:
	 * @return String
	 * @see javax.servlet.http.HttpServletRequest#getPathInfo()
	 */
	public String getPathInfo() {
		// TODO Auto-generated method stub
		return request.getPathInfo();
	}

	/**
	 *  Description:
	 * @return String
	 * @see javax.servlet.http.HttpServletRequest#getPathTranslated()
	 */
	public String getPathTranslated() {
		// TODO Auto-generated method stub
		return request.getPathTranslated();
	}

	/**
	 *  Description:
	 * @return String
	 * @see javax.servlet.http.HttpServletRequest#getContextPath()
	 */
	public String getContextPath() {
		// TODO Auto-generated method stub
		return request.getContextPath();
	}

	/**
	 *  Description:
	 * @return String
	 * @see javax.servlet.http.HttpServletRequest#getQueryString()
	 */
	public String getQueryString() {
		// TODO Auto-generated method stub
		return request.getQueryString();
	}

	/**
	 *  Description:
	 * @return String
	 * @see javax.servlet.http.HttpServletRequest#getRemoteUser()
	 */
	public String getRemoteUser() {
		// TODO Auto-generated method stub
		return request.getRemoteUser();
	}

	/**
	 *  Description:
	 * @param arg0
	 * @return boolean
	 * @see javax.servlet.http.HttpServletRequest#isUserInRole(java.lang.String)
	 */
	public boolean isUserInRole(String arg0) {
		// TODO Auto-generated method stub
		return request.isUserInRole(arg0);
	}

	/**
	 *  Description:
	 * @return Principal
	 * @see javax.servlet.http.HttpServletRequest#getUserPrincipal()
	 */
	public Principal getUserPrincipal() {
		// TODO Auto-generated method stub
		return request.getUserPrincipal();
	}

	/**
	 *  Description:
	 * @return String
	 * @see javax.servlet.http.HttpServletRequest#getRequestedSessionId()
	 */
	public String getRequestedSessionId() {
		// TODO Auto-generated method stub
		return request.getRequestedSessionId();
	}

	/**
	 *  Description:
	 * @return String
	 * @see javax.servlet.http.HttpServletRequest#getRequestURI()
	 */
	public String getRequestURI() {
		// TODO Auto-generated method stub
		return request.getRequestURI();
	}

	/**
	 *  Description:
	 * @return StringBuffer
	 * @see javax.servlet.http.HttpServletRequest#getRequestURL()
	 */
	public StringBuffer getRequestURL() {
		// TODO Auto-generated method stub
		return request.getRequestURL();
	}

	/**
	 *  Description:
	 * @return String
	 * @see javax.servlet.http.HttpServletRequest#getServletPath()
	 */
	public String getServletPath() {
		// TODO Auto-generated method stub
		return request.getServletPath();
	}

	/**
	 *  Description:
	 * @param arg0
	 * @return HttpSession
	 * @see javax.servlet.http.HttpServletRequest#getSession(boolean)
	 */
	public HttpSession getSession(boolean arg0) {
		// TODO Auto-generated method stub
		return request.getSession(arg0);
	}

	/**
	 *  Description:
	 * @return HttpSession
	 * @see javax.servlet.http.HttpServletRequest#getSession()
	 */
	public HttpSession getSession() {
		// TODO Auto-generated method stub
		return request.getSession();
	}

	/**
	 *  Description:
	 * @return boolean
	 * @see javax.servlet.http.HttpServletRequest#isRequestedSessionIdValid()
	 */
	public boolean isRequestedSessionIdValid() {
		// TODO Auto-generated method stub
		return request.isRequestedSessionIdValid();
	}

	/**
	 *  Description:
	 * @return boolean
	 * @see javax.servlet.http.HttpServletRequest#isRequestedSessionIdFromCookie()
	 */
	public boolean isRequestedSessionIdFromCookie() {
		// TODO Auto-generated method stub
		return request.isRequestedSessionIdFromCookie();
	}

	/**
	 *  Description:
	 * @return boolean
	 * @see javax.servlet.http.HttpServletRequest#isRequestedSessionIdFromURL()
	 */
	public boolean isRequestedSessionIdFromURL() {
		// TODO Auto-generated method stub
		return request.isRequestedSessionIdFromURL();
	}

	/**
	 *  Description:
	 * @return boolean
	 * @deprecated
	 * @see javax.servlet.http.HttpServletRequest#isRequestedSessionIdFromUrl()
	 */
	public boolean isRequestedSessionIdFromUrl() {
		// TODO Auto-generated method stub
		return request.isRequestedSessionIdFromUrl();
	}

	/**
	 *  Description:
	 * @param arg0
	 * @return Object
	 * @see javax.servlet.ServletRequest#getAttribute(java.lang.String)
	 */
	public Object getAttribute(String arg0) {
		// TODO Auto-generated method stub
		return request.getAttribute(arg0);
	}

	/**
	 *  Description:
	 * @return Enumeration
	 * @see javax.servlet.ServletRequest#getAttributeNames()
	 */
	public Enumeration getAttributeNames() {
		// TODO Auto-generated method stub
		return request.getAttributeNames();
	}

	/**
	 *  Description:
	 * @return String
	 * @see javax.servlet.ServletRequest#getCharacterEncoding()
	 */
	public String getCharacterEncoding() {
		// TODO Auto-generated method stub
		return request.getCharacterEncoding();
	}

	/**
	 *  Description:
	 * @param arg0
	 * @throws UnsupportedEncodingException
	 * @see javax.servlet.ServletRequest#setCharacterEncoding(java.lang.String)
	 */
	public void setCharacterEncoding(String arg0)
		throws UnsupportedEncodingException {
		// TODO Auto-generated method stub
		//request.setCharacterEncoding(arg0);

        request.setCharacterEncoding(arg0);
        this.encode = arg0;
	}

	/**
	 *  Description:
	 * @return 内容长度
	 * @see javax.servlet.ServletRequest#getContentLength()
	 */
	public int getContentLength() {
		// TODO Auto-generated method stub
		return request.getContentLength();
	}

	/**
	 *  Description:
	 * @return 内容类型
	 * @see javax.servlet.ServletRequest#getContentType()
	 */
	public String getContentType() {
		// TODO Auto-generated method stub
		return request.getContentType();
	}

	/**
	 *  Description:
	 * @return ServletInputStream
	 * @throws IOException
	 * @see javax.servlet.ServletRequest#getInputStream()
	 */
	public ServletInputStream getInputStream() throws IOException {
		// TODO Auto-generated method stub
		return request.getInputStream();
	}

    private String getParameterOfMobile(String value)
    {
//        if(mobileEncode == null)
//            return value;
           return value;
//        } catch (UnsupportedEncodingException ex) {
//            ex.printStackTrace();
//            log.error(ex);
//            return value;
//        }
    }
	/**
	 *  Description:
	 * @param arg0
	 * @return String
	 * @see javax.servlet.ServletRequest#getParameter(java.lang.String)
	 */
	public String getParameter(String arg0) {
		// TODO Auto-generated method stub
        String value = "";
        //处理tubine中文问题
		if(rundata == null)
			value = request.getParameter(arg0);
		else
			value = rundata.getParameters().getString(arg0);

        //处理手机浏览器和oprea中文问题
        value = this.getParameterOfMobile(value);
        return value;
	}

	/**
	 *  Description:
	 * @return Enumeration
	 * @see javax.servlet.ServletRequest#getParameterNames()
	 */
	public Enumeration getParameterNames() {
		// TODO Auto-generated method stub
		return request.getParameterNames();
	}

	/**
	 *  Description:
	 * @param arg0
	 * @return String[]
	 * @see javax.servlet.ServletRequest#getParameterValues(java.lang.String)
	 */
	public String[] getParameterValues(String arg0) {
		// TODO Auto-generated method stub
        String[] values = null;
		if(rundata == null)
			values = request.getParameterValues(arg0);
		else
			values = rundata.getParameters().getStrings(arg0);
        for(int i = 0; values != null && i < values.length; i ++)
        {
            values[i] = getParameterOfMobile(values[i]);
        }
        return values;
	}

	/**
	 *  Description:
	 * @return Map
	 * @see javax.servlet.ServletRequest#getParameterMap()
	 */
	public Map getParameterMap() {
		// TODO Auto-generated method stub
		return request.getParameterMap();
	}

	/**
	 *  Description:
	 * @return String
	 * @see javax.servlet.ServletRequest#getProtocol()
	 */
	public String getProtocol() {
		// TODO Auto-generated method stub
		return request.getProtocol();
	}

	/**
	 *  Description:
	 * @return String
	 * @see javax.servlet.ServletRequest#getScheme()
	 */
	public String getScheme() {
		// TODO Auto-generated method stub
		return request.getScheme();
	}

	/**
	 *  Description:
	 * @return String
	 * @see javax.servlet.ServletRequest#getServerName()
	 */
	public String getServerName() {
		// TODO Auto-generated method stub
		return request.getServerName();
	}

	/**
	 *  Description:
	 * @return int
	 * @see javax.servlet.ServletRequest#getServerPort()
	 */
	public int getServerPort() {
		// TODO Auto-generated method stub
		return request.getServerPort();
	}

	/**
	 *  Description:
	 * @return String
	 * @throws IOException
	 * @see javax.servlet.ServletRequest#getReader()
	 */
	public BufferedReader getReader() throws IOException {
		// TODO Auto-generated method stub
		return request.getReader();
	}

	/**
	 *  Description:
	 * @return String
	 * @see javax.servlet.ServletRequest#getRemoteAddr()
	 */
	public String getRemoteAddr() {
		// TODO Auto-generated method stub
		return request.getRemoteAddr();
	}

	/**
	 *  Description:
	 * @return String
	 * @see javax.servlet.ServletRequest#getRemoteHost()
	 */
	public String getRemoteHost() {
		// TODO Auto-generated method stub
		return request.getRemoteHost();
	}

	/**
	 *  Description:
	 * @param arg0
	 * @param arg1
	 * @see javax.servlet.ServletRequest#setAttribute(java.lang.String, java.lang.Object)
	 */
	public void setAttribute(String arg0, Object arg1) {
		// TODO Auto-generated method stub
		request.setAttribute(arg0,arg1);
	}

	/**
	 *  Description:
	 * @param arg0
	 * @see javax.servlet.ServletRequest#removeAttribute(java.lang.String)
	 */
	public void removeAttribute(String arg0) {
		// TODO Auto-generated method stub
		request.removeAttribute(arg0);
	}

	/**
	 *  Description:
	 * @return Locale
	 * @see javax.servlet.ServletRequest#getLocale()
	 */
	public Locale getLocale() {
		// TODO Auto-generated method stub
		return request.getLocale();
	}

	/**
	 *  Description:
	 * @return Enumeration
	 * @see javax.servlet.ServletRequest#getLocales()
	 */
	public Enumeration getLocales() {
		// TODO Auto-generated method stub
		return request.getLocales();
	}

	/**
	 *  Description:
	 * @return boolean
	 * @see javax.servlet.ServletRequest#isSecure()
	 */
	public boolean isSecure() {
		// TODO Auto-generated method stub
		return request.isSecure();
	}

	/**
	 *  Description:
	 * @param arg0
	 * @return RequestDispatcher
	 * @see javax.servlet.ServletRequest#getRequestDispatcher(java.lang.String)
	 */
	public RequestDispatcher getRequestDispatcher(String arg0) {
		// TODO Auto-generated method stub
		return request.getRequestDispatcher(arg0);
	}

	/**
	 *  Description:
	 * @param arg0
	 * @return String
	 * @deprecated
	 * @see javax.servlet.ServletRequest#getRealPath(java.lang.String)
	 */
	public String getRealPath(String arg0) {
		// TODO Auto-generated method stub
		return request.getRealPath(arg0);
	}

	/**
	 *  Description:
	 * @return int

	 */
	public int getLocalPort() {
		// TODO Auto-generated method stub
		return request.getServerPort();
	}

	/**
	 *  Description:
	 * @return int

	 */
	public int getRemotePort() {
		// TODO Auto-generated method stub
		return request.getServerPort();
	}

	/**
	 *  Description:
	 * @return String

	 */
	public String getLocalAddr() {
		// TODO Auto-generated method stub
		//return request.getLocalAddr();
		return request.getContextPath();
	}

	/**
	 *  Description:
	 * @return String

	 */
	public String getLocalName() {
		// TODO Auto-generated method stub
		//return request.getLocalName();
		return null;
	}

    public String getMobileEncode() {
        return mobileEncode;
    }

    private void jbInit() throws Exception {
    }

}
