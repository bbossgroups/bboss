package org.frameworkset.security.session.entity;

/**
 * @todo SessionBean查询条件实体
 * @author tanx
 * @date 2014年6月4日
 * 
 */
public class SessionCondition {

	private String sessionid;
	private String appkey;// 应用名称
	private String createtime_start;
	private String createtime_end;
	private String referip;// 客户端
	private String validate;// 状态
	private String host;// 服务端

	public String getCreatetime_start() {
		return createtime_start;
	}

	public void setCreatetime_start(String createtime_start) {
		this.createtime_start = createtime_start;
	}

	public String getCreatetime_end() {
		return createtime_end;
	}

	public void setCreatetime_end(String createtime_end) {
		this.createtime_end = createtime_end;
	}

	public String getReferip() {
		return referip;
	}

	public void setReferip(String referip) {
		this.referip = referip;
	}

	public String getValidate() {
		return validate;
	}

	public void setValidate(String validate) {
		this.validate = validate;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getSessionid() {
		return sessionid;
	}

	public void setSessionid(String sessionid) {
		this.sessionid = sessionid;
	}

	public String getAppkey() {
		return appkey;
	}

	public void setAppkey(String appkey) {
		this.appkey = appkey;
	}

}
