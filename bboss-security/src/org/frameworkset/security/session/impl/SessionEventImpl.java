package org.frameworkset.security.session.impl;

import javax.servlet.http.HttpSession;

import org.frameworkset.security.session.SessionEvent;

public class SessionEventImpl implements SessionEvent {
	private HttpSession session;
	private String attributeName;
	
	private int eventType;
	public SessionEventImpl(HttpSession session,int eventType) {
		this.session = session;
		this.eventType = eventType;
	}

	@Override
	public HttpSession getSource() {
		// TODO Auto-generated method stub
		return session;
	}

	@Override
	public int getEventType() {
		// TODO Auto-generated method stub
		return eventType;
	}

	public String getAttributeName() {
		return attributeName;
	}

	public SessionEventImpl setAttributeName(String attributeName) {
		this.attributeName = attributeName;
		return this;
	}

	public Object getAttributeValue() {
		return session.getAttribute(attributeName);
	}

	

}
