package org.frameworkset.security.session.statics;

public class SessionAPP {

	public SessionAPP() {
		// TODO Auto-generated constructor stub
	}
	
	private String appkey;
	private long sessions;
	private boolean hasDeletePermission;
	public String getAppkey() {
		return appkey;
	}
	public void setAppkey(String appkey) {
		this.appkey = appkey;
	}
	public long getSessions() {
		return sessions;
	}
	public void setSessions(long sessions) {
		this.sessions = sessions;
	}
	public boolean isHasDeletePermission() {
		return hasDeletePermission;
	}
	public void setHasDeletePermission(boolean hasDeletePermission) {
		this.hasDeletePermission = hasDeletePermission;
	}

}
