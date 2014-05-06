package org.frameworkset.security.session;

import java.util.Enumeration;

public interface Session {

	
	public Object getAttribute(String attribute) ;

	
	public Enumeration getAttributeNames() ;

	
	public long getCreationTime() ;
	
	public String getId() ;
	/**
	 * 更新最后访问时间
	 */
	public void touch();
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

	
	public Object getValue(String attribute) ;
	public String[] getValueNames() ;
	public void invalidate() ;
	public boolean isNew() ;
	public void putValue(String attribute, Object value) ;

	
	public void removeAttribute(String attribute) ;
	public void removeValue(String attribute) ;
	public void setAttribute(String attribute, Object value) ;
	public void setMaxInactiveInterval(long maxInactiveInterval) ;
	public String getReferip();
//	public String getSessionID();
//	public Object getAttribute(String attribute);
//	public void setAttribute(String attribute,Object value);
	

}
