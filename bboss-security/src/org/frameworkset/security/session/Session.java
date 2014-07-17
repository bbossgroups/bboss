package org.frameworkset.security.session;

import java.util.Enumeration;

public interface Session {

	
	public Object getAttribute(String attribute,String contextpath) ;

	public Object getCacheAttribute(String attribute);
	public Enumeration getAttributeNames(String contextpath) ;

	
	public long getCreationTime() ;
	
	public String getId() ;
	/**
	 * 更新最后访问时间
	 */
	public void touch(String lastAccessedUrl,String contextpath);
	public long getLastAccessedTime() ;
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

	
	public Object getValue(String attribute,String contextpath) ;
	public String[] getValueNames(String contextpath) ;
	public void invalidate(String contextpath) ;
	public boolean isNew() ;
	public void putValue(String attribute, Object value,String contextpath) ;

	
	public void removeAttribute(String attribute,String contextpath) ;
	public void removeValue(String attribute,String contextpath) ;
	public void setAttribute(String attribute, Object value,String contextpath) ;
	public void setMaxInactiveInterval(long maxInactiveInterval) ;
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
