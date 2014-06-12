package org.frameworkset.security.session.impl;

import org.apache.log4j.Logger;
import org.frameworkset.security.session.SessionEvent;
import org.frameworkset.security.session.SessionListener;

public class NullSessionListener implements SessionListener {
	private static Logger log = Logger.getLogger(NullSessionListener.class);
	@Override
	public void createSession(SessionEvent event) {
		log.debug("createSession session id:"+event.getSource().getId());
	}

	@Override
	public void destroySession(SessionEvent event) {
		log.debug("destroySession session id:"+event.getSource().getId());

	}

	@Override
	public void addAttribute(SessionEvent event) {
		log.debug("addAttribute session id:"+event.getSource().getId() + ",attirbute name is "+event.getAttributeName());


	}

	@Override
	public void removeAttribute(SessionEvent event) {
		log.debug("removeAttribute session id:"+event.getSource().getId() + ",attirbute name is "+event.getAttributeName());
	}

}
