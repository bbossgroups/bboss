package org.frameworkset.security.session.impl;

import org.frameworkset.security.session.Session;
import org.frameworkset.security.session.SessionEvent;

public class SessionEventImpl implements SessionEvent {
	private Session session;
	private String attributeName;
	private Object attributeValue; 
	private int eventType;
	public SessionEventImpl(Session session,int eventType) {
		this.session = session;
		this.eventType = eventType;
	}

	@Override
	public Session getSource() {
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
		return attributeValue;
	}

	public SessionEventImpl setAttributeValue(Object attributeValue) {
		this.attributeValue = attributeValue;
		return this;
	}

}
