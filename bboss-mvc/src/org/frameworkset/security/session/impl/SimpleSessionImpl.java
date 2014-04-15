package org.frameworkset.security.session.impl;

import java.util.Enumeration;

import org.frameworkset.security.session.Session;
import org.frameworkset.security.session.SessionStore;

public class SimpleSessionImpl implements Session{
	private String sessionID;
	private long creationTime;
	private long lastAccessedTime;
	private long maxInactiveInterval;
	private SessionStore sessionStore;
	@Override
	public Object getAttribute(String attribute) {
		
		return sessionStore.getAttribute(sessionID,attribute);
	}

	@Override
	public Enumeration getAttributeNames() {
		return sessionStore.getAttributeNames(sessionID);
	}

	@Override
	public long getCreationTime() {
		// TODO Auto-generated method stub
		return creationTime;
	}

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return sessionID;
	}

	@Override
	public void touch() {
		lastAccessedTime = System.currentTimeMillis();
		sessionStore.updateLastAccessedTime(sessionID,lastAccessedTime);
		
	}

	@Override
	public long getLastAccessedTime() {
		// TODO Auto-generated method stub
		return lastAccessedTime = sessionStore.getLastAccessedTime(sessionID);
	}

	@Override
	public long getMaxInactiveInterval() {
		// TODO Auto-generated method stub
		return maxInactiveInterval;
	}

	@Override
	public Object getValue(String attribute) {
		// TODO Auto-generated method stub
		return getAttribute( attribute);
	}

	@Override
	public String[] getValueNames() {
		// TODO Auto-generated method stub
		return sessionStore.getValueNames(sessionID);
	}

	@Override
	public void invalidate() {
		sessionStore.invalidate(sessionID);
		
	}

	@Override
	public boolean isNew() {
		// TODO Auto-generated method stub
		return sessionStore.isNew(sessionID);
	}

	@Override
	public void putValue(String attribute, Object value) {
		setAttribute( attribute,  value) ;
		
	}

	@Override
	public void removeAttribute(String attribute) {
		sessionStore.removeAttribute(sessionID,attribute);
		
	}

	@Override
	public void removeValue(String attribute) {
		removeAttribute( attribute);
		
	}

	@Override
	public void setAttribute(String attribute, Object value) {
		sessionStore.addAttribute(sessionID,attribute,value);
		
	}

	@Override
	public void setMaxInactiveInterval(long maxInactiveInterval) {
		this.maxInactiveInterval = maxInactiveInterval;
		
	}

}
