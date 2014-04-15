package org.frameworkset.security.session.impl;

import java.util.Enumeration;

import org.frameworkset.security.session.Session;

public class MongDBSessionStore extends BaseSessionStore{

	@Override
	public void livecheck() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Session createSession(Object sessionSource) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getAttribute(String sessionID, String attribute) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Enumeration getAttributeNames(String sessionID) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updateLastAccessedTime(String sessionID, long lastAccessedTime) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public long getLastAccessedTime(String sessionID) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String[] getValueNames(String sessionID) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void invalidate(String sessionID) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isNew(String sessionID) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void removeAttribute(String sessionID, String attribute) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addAttribute(String sessionID, String attribute, Object value) {
		
		
	}


}

