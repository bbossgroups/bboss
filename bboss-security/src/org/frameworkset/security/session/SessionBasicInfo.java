package org.frameworkset.security.session;

public class SessionBasicInfo {
    private String appKey;
    private String referip;
    private String requesturi;
    private String lastAccessedHostIP;
	public SessionBasicInfo() {
		// TODO Auto-generated constructor stub
	}
	public String getAppKey() {
		return appKey;
	}
	public void setAppKey(String appKey) {
		this.appKey = appKey;
	}
	public String getReferip() {
		return referip;
	}
	public void setReferip(String referip) {
		this.referip = referip;
	}
	public String getRequesturi() {
		return requesturi;
	}
	public void setRequesturi(String requesturi) {
		this.requesturi = requesturi;
	}
	public String getLastAccessedHostIP() {
		return lastAccessedHostIP;
	}
	public void setLastAccessedHostIP(String lastAccessedHostIP) {
		this.lastAccessedHostIP = lastAccessedHostIP;
	}

}
