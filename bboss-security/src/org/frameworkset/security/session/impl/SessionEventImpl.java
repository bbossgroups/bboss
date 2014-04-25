package org.frameworkset.security.session.impl;

import org.frameworkset.security.session.Session;
import org.frameworkset.security.session.SessionEvent;

public class SessionEventImpl implements SessionEvent {
	private Session session;
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

}
