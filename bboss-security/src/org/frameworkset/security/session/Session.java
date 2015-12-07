package org.frameworkset.security.session;

import java.util.Enumeration;

import javax.servlet.http.HttpSession;

public interface Session {

	
	public Object getAttribute(HttpSession session,String attribute,String contextpath) ;

	public Object getCacheAttribute(String attribute);
	public Enumeration getAttributeNames(HttpSession session,String contextpath) ;

	
	public long getCreationTime() ;
	
	public String getId() ;
	/**
	 * 更新最后访问时间
	 */
	public void touch(HttpSession session,String lastAccessedUrl,String contextpath);
	public long getLastAccessedTime() ;
	public void setLastAccessedTime(long lastAccessedTime) ;
	public long getMaxInactiveInterval();

//	@Override
//	public ServletContext getServletContext() {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public HttpSessionContext getSessionContext() {
//		// TODO Auto-generated method stub
//		return null;
//	}

	
	public Object getValue(HttpSession session,String attribute,String contextpath) ;
	public String[] getValueNames(HttpSession session,String contextpath) ;
	public void invalidate(HttpSession session,String contextpath) ;
	public boolean isNew() ;
	public void putValue(HttpSession session,String attribute, Object value,String contextpath) ;

	
	public void removeAttribute(HttpSession session,String attribute,String contextpath) ;
	public void removeValue(HttpSession session,String attribute,String contextpath) ;
	public void setAttribute(HttpSession session,String attribute, Object value,String contextpath) ;
	public void setMaxInactiveInterval(HttpSession session,long maxInactiveInterval,String contextpath) ;
	public void setMaxInactiveInterval(HttpSession session,long maxInactiveInterval,boolean refreshstore,String contextpath) ;
	public String getReferip();
	public boolean isValidate();
	public void _setSessionStore(SessionStore sessionStore);
//	public String getSessionID();
//	public Object getAttribute(String attribute);
//	public void setAttribute(String attribute,Object value);


	public void putNewStatus();
	
	public String getRequesturi() ;
	public void setRequesturi(String requesturi);
}
