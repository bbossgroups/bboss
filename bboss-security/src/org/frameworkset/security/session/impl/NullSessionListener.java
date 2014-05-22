package org.frameworkset.security.session.impl;

import org.frameworkset.security.session.SessionEvent;
import org.frameworkset.security.session.SessionListener;

public class NullSessionListener implements SessionListener {

	@Override
	public void createSession(SessionEvent event) {
		System.out.println("createSession session id:"+event.getSource().getId());

	}

	@Override
	public void destroySession(SessionEvent event) {
		System.out.println("destroySession session id:"+event.getSource().getId());

	}

	@Override
	public void addAttribute(SessionEvent event) {
		System.out.println("addAttribute session id:"+event.getSource().getId() + ",attirbute name is "+event.getAttributeName());


	}

	@Override
	public void removeAttribute(SessionEvent event) {
		System.out.println("removeAttribute session id:"+event.getSource().getId() + ",attirbute name is "+event.getAttributeName());
	}

}
