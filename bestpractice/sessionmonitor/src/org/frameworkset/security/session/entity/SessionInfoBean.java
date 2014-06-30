package org.frameworkset.security.session.entity;

import java.util.Date;
import java.util.Map;

/**
 * @todo SessionBean实体
 * @author tanx
 * @date 2014年6月4日
 * 
 */
public class SessionInfoBean {

	private String sessionid;
	private String appKey;// 应用名称
	private Long sessionNum;// 连接应用个数
	private Date creationTime;// 创建时间
	private Date lastAccessedTime;// 最后访问时间
	private Date loseTime;//失效时间
	private String maxInactiveInterval;// 有效期
	private String referip;// 客户端
	private boolean validate;// 状态
	private Map<String, Object> attributes;// 属性
	private String host;// 服务端
	private String requesturi;
	private String lastAccessedUrl;
	private boolean httpOnly;
	private boolean secure;
	private String lastAccessedHostIP;
	public Date getLoseTime() {
		return loseTime;
	}

	public void setLoseTime(Date loseTime) {
		this.loseTime = loseTime;
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

	public String getMaxInactiveInterval() {
		return maxInactiveInterval;
	}

	public void setMaxInactiveInterval(String maxInactiveInterval) {
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

	public Long getSessionNum() {
		return sessionNum;
	}

	public void setSessionNum(Long sessionNum) {
		this.sessionNum = sessionNum;
	}

	public String getSessionid() {
		return sessionid;
	}

	public void setSessionid(String sessionid) {
		this.sessionid = sessionid;
	}

	public String getAppKey() {
		return appKey;
	}

	public void setAppKey(String appKey) {
		this.appKey = appKey;
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
