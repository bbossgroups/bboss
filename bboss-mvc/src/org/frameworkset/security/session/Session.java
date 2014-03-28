package org.frameworkset.security.session;

import java.util.Enumeration;

public interface Session {

	
	public Object getAttribute(String attribute) ;

	
	public Enumeration getAttributeNames() ;

	
	public long getCreationTime() ;
	
	public String getId() ;
	public long getLastAccessedTime() ;
	public int getMaxInactiveInterval();

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
	public void setMaxInactiveInterval(int maxInactiveInterval) ;
//	public String getSessionID();
//	public Object getAttribute(String attribute);
//	public void setAttribute(String attribute,Object value);
	

}
