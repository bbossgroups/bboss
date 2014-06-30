package org.frameworkset.security.session.statics;

import java.util.Date;
import java.util.Map;

public class SessionInfo {
	private String appKey;
	private String sessionid;
	private Date creationTime;
	private Date lastAccessedTime;
	private long maxInactiveInterval;
	private String referip;
	private boolean validate;
	private Map<String, Object> attributes;
	private String host;
	private String requesturi;
	private String lastAccessedUrl;
	private boolean httpOnly;
	private boolean secure;
	private String lastAccessedHostIP;
	public SessionInfo() {
		// TODO Auto-generated constructor stub
	}

	public String getAppKey() {
		return appKey;
	}

	public void setAppKey(String appKey) {
		this.appKey = appKey;
	}

	public String getSessionid() {
		return sessionid;
	}

	public void setSessionid(String sessionid) {
		this.sessionid = sessionid;
	}

	public Date getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}

	public Date getLastAccessedTime() {
		return lastAccessedTime;
	}

	public void setLastAccessedTime(Date lastAccessedTime) {
		this.lastAccessedTime = lastAccessedTime;
	}

	public long getMaxInactiveInterval() {
		return maxInactiveInterval;
	}

	public void setMaxInactiveInterval(long maxInactiveInterval) {
		this.maxInactiveInterval = maxInactiveInterval;
	}

	public String getReferip() {
		return referip;
	}

	public void setReferip(String referip) {
		this.referip = referip;
	}

	public boolean isValidate() {
		return validate;
	}

	public void setValidate(boolean validate) {
		this.validate = validate;
	}

	public Map<String, Object> getAttributes() {
		return attributes;
	}

	public void setAttributes(Map<String, Object> attributes) {
		this.attributes = attributes;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	
	public String getRequesturi() {
		return requesturi;
	}

	public void setRequesturi(String requesturi) {
		this.requesturi = requesturi;
	}

	public String getLastAccessedUrl() {
		return lastAccessedUrl;
	}

	public void setLastAccessedUrl(String lastAccessedUrl) {
		this.lastAccessedUrl = lastAccessedUrl;
	}

	public boolean isHttpOnly() {
		return httpOnly;
	}

	public void setHttpOnly(boolean httpOnly) {
		this.httpOnly = httpOnly;
	}

	public boolean isSecure() {
		return secure;
	}

	public void setSecure(boolean secure) {
		this.secure = secure;
	}

	public String getLastAccessedHostIP() {
		return lastAccessedHostIP;
	}

	public void setLastAccessedHostIP(String lastAccessedHostIP) {
		this.lastAccessedHostIP = lastAccessedHostIP;
	}

}
