package org.frameworkset.security.session;

public interface SessionListener {
	public void createSession(SessionEvent event);
	public void destroySession(SessionEvent event);
	public void addAttribute(SessionEvent event);
	public void removeAttribute(SessionEvent event);

}
