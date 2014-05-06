package org.frameworkset.security.session.impl;

import java.util.Enumeration;

import org.frameworkset.security.session.Session;
import org.frameworkset.security.session.SessionStore;

public class SimpleSessionImpl implements Session{
	private String appKey;
	private String id;
	private long creationTime;
	private long lastAccessedTime;
	private long maxInactiveInterval;
	private String referip;
	private boolean validate;
	private transient SessionStore sessionStore;
	@Override
	public Object getAttribute(String attribute) {
		
		return sessionStore.getAttribute(appKey,id,attribute);
	}

	@Override
	public Enumeration getAttributeNames() {
		return sessionStore.getAttributeNames(appKey,id);
	}

	@Override
	public long getCreationTime() {
		// TODO Auto-generated method stub
		return creationTime;
	}

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return id;
	}

	@Override
	public void touch() {
		lastAccessedTime = System.currentTimeMillis();
		sessionStore.updateLastAccessedTime(appKey,id,lastAccessedTime);
		
	}

	@Override
	public long getLastAccessedTime() {
		// TODO Auto-generated method stub
		return lastAccessedTime = sessionStore.getLastAccessedTime(appKey,id);
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
		return sessionStore.getValueNames(appKey,id);
	}

	@Override
	public void invalidate() {
		sessionStore.invalidate(appKey,id);
		
	}

	@Override
	public boolean isNew() {
		// TODO Auto-generated method stub
		//return sessionStore.isNew(appKey,id);
		return this.creationTime == this.lastAccessedTime;
	}

	@Override
	public void putValue(String attribute, Object value) {
		setAttribute( attribute,  value) ;
		
	}

	@Override
	public void removeAttribute(String attribute) {
		sessionStore.removeAttribute(appKey,id,attribute);
		
	}

	@Override
	public void removeValue(String attribute) {
		removeAttribute( attribute);
		
	}

	@Override
	public void setAttribute(String attribute, Object value) {
		sessionStore.addAttribute(appKey,id,attribute,value);
		
	}

	@Override
	public void setMaxInactiveInterval(long maxInactiveInterval) {
		this.maxInactiveInterval = maxInactiveInterval;
		
	}

	public String getAppKey() {
		return appKey;
	}

	public void setAppKey(String appKey) {
		this.appKey = appKey;
	}

	

	public SessionStore _getSessionStore() {
		return sessionStore;
	}

	public void _setSessionStore(SessionStore sessionStore) {
		this.sessionStore = sessionStore;
	}

	public void setCreationTime(long creationTime) {
		this.creationTime = creationTime;
	}

	public void setLastAccessedTime(long lastAccessedTime) {
		this.lastAccessedTime = lastAccessedTime;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getReferip() {
		return referip;
	}

	public void setReferip(String referip) {
		this.referip = referip;
	}
	public boolean isValidate()
	{
		return validate;
	}
	
	public void setValidate(boolean validate)
	{
		this.validate = validate;
	}

}
