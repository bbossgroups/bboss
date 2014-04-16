package org.frameworkset.security.session.impl;

import java.util.Enumeration;

import org.frameworkset.security.session.Session;
import org.frameworkset.security.session.SessionStore;

public class SimpleSessionImpl implements Session{
	private String appKey;
	private String sessionID;
	private long creationTime;
	private long lastAccessedTime;
	private long maxInactiveInterval;
	private transient SessionStore sessionStore;
	@Override
	public Object getAttribute(String attribute) {
		
		return sessionStore.getAttribute(appKey,sessionID,attribute);
	}

	@Override
	public Enumeration getAttributeNames() {
		return sessionStore.getAttributeNames(appKey,sessionID);
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
		sessionStore.updateLastAccessedTime(appKey,sessionID,lastAccessedTime);
		
	}

	@Override
	public long getLastAccessedTime() {
		// TODO Auto-generated method stub
		return lastAccessedTime = sessionStore.getLastAccessedTime(appKey,sessionID);
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
		return sessionStore.getValueNames(appKey,sessionID);
	}

	@Override
	public void invalidate() {
		sessionStore.invalidate(appKey,sessionID);
		
	}

	@Override
	public boolean isNew() {
		// TODO Auto-generated method stub
		return sessionStore.isNew(appKey,sessionID);
	}

	@Override
	public void putValue(String attribute, Object value) {
		setAttribute( attribute,  value) ;
		
	}

	@Override
	public void removeAttribute(String attribute) {
		sessionStore.removeAttribute(appKey,sessionID,attribute);
		
	}

	@Override
	public void removeValue(String attribute) {
		removeAttribute( attribute);
		
	}

	@Override
	public void setAttribute(String attribute, Object value) {
		sessionStore.addAttribute(appKey,sessionID,attribute,value);
		
	}

	@Override
	public void setMaxInactiveInterval(long maxInactiveInterval) {
		this.maxInactiveInterval = maxInactiveInterval;
		
	}

}
