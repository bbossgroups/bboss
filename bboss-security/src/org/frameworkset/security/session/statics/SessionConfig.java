package org.frameworkset.security.session.statics;

import java.io.Serializable;
import java.util.Date;

public class SessionConfig implements Serializable {
	private long sessionTimeout;
	private String cookiename;
	private String crossDomain;
	private boolean startLifeScan = false;
	private Date scanStartTime;
	private Date createTime;
	private Date updateTime;
 
	private boolean httpOnly;
	private boolean secure;
	private String domain;
	private String monitorAttributes;
	AttributeInfo[] extendAttributeInfos;
	/**
	 * 应用编码，如果没有指定appcode值默认为应用上下文
	 * appcode的作用：当所有的应用上下文为/时，用来区分后台统计的会话信息
	 */
	private String appcode;
	private long cookieLiveTime;
	private String sessionStore;
	 
	private String sessionListeners;
	
	/**
	 * session超时检测时间间隔，默认为-1，不检测
	 * 如果需要检测，那么只要令牌持续时间超过sessionscaninterval
	 * 对应的时间将会被清除
	 */
	private long sessionscaninterval = 60*60000;
	private boolean usewebsession = false; 
	public SessionConfig() {
		// TODO Auto-generated constructor stub
	}
	public long getSessionTimeout() {
		return sessionTimeout;
	}
	public void setSessionTimeout(long sessionTimeout) {
		this.sessionTimeout = sessionTimeout;
	}
	public String getCookiename() {
		return cookiename;
	}
	public void setCookiename(String cookiename) {
		this.cookiename = cookiename;
	}
 
	 
	public boolean isStartLifeScan() {
		return startLifeScan;
	}
	public void setStartLifeScan(boolean startLifeScan) {
		this.startLifeScan = startLifeScan;
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
	public String getDomain() {
		return domain;
	}
	public void setDomain(String domain) {
		this.domain = domain;
	}
	public String getAppcode() {
		return appcode;
	}
	public void setAppcode(String appcode) {
		this.appcode = appcode;
	}
	public long getCookieLiveTime() {
		return cookieLiveTime;
	}
	public void setCookieLiveTime(long cookieLiveTime) {
		this.cookieLiveTime = cookieLiveTime;
	}
	public String getSessionStore() {
		return sessionStore;
	}
	public void setSessionStore(String sessionStore) {
		this.sessionStore = sessionStore;
	}
	public String getSessionListeners() {
		return sessionListeners;
	}
	public void setSessionListeners(String sessionListeners) {
		this.sessionListeners = sessionListeners;
	}
	public long getSessionscaninterval() {
		return sessionscaninterval;
	}
	public void setSessionscaninterval(long sessionscaninterval) {
		this.sessionscaninterval = sessionscaninterval;
	}
	public boolean isUsewebsession() {
		return usewebsession;
	}
	public void setUsewebsession(boolean usewebsession) {
		this.usewebsession = usewebsession;
	}
	 
	public String getCrossDomain() {
		return crossDomain;
	}
	public void setCrossDomain(String crossDomain) {
		this.crossDomain = crossDomain;
	}
	public Date getScanStartTime() {
		return scanStartTime;
	}
	public void setScanStartTime(Date scanStartTime) {
		this.scanStartTime = scanStartTime;
	}
	public String getMonitorAttributes() {
		return monitorAttributes;
	}
	public void setMonitorAttributes(String monitorAttributes) {
		this.monitorAttributes = monitorAttributes;
	}
	
	public AttributeInfo[] getExtendAttributeInfos() {
		return extendAttributeInfos;
	}
	public void setExtendAttributeInfos(AttributeInfo[] extendAttributeInfos) {
		this.extendAttributeInfos = extendAttributeInfos;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

}
