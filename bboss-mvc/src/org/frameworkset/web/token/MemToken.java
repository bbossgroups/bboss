package org.frameworkset.web.token;

import com.frameworkset.orm.annotation.Column;

/**
 * @author biaoping.yin
 * 
 *
 */
public class MemToken implements java.io.Serializable{
	/**
	 * 令牌信息
	 */
	private String id;
	private String token;
	private long createTime;
	@Column(name="validate_")
	private boolean validate = true;
	private long lastVistTime;
	private long livetime;
	private String appid;
	private String secret;
	
	private String signtoken;
	public String getToken() {
		return token;
	}

	public long getCreateTime() {
		return createTime;
	}
	
	
	
	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return token.hashCode();
	}
	public MemToken()
	{
		
	}
	public MemToken(String token, long createTime) {
		super();
		this.token = token;
		this.createTime = createTime;
	}
	public MemToken(String token, long createTime, boolean validate,
			long lastVistTime, long livetime) {
		super();
		this.token = token;
		this.createTime = createTime;
		this.validate = validate;
		this.lastVistTime = lastVistTime;
		this.livetime = livetime;
	}

	@Override
	public boolean equals(Object obj) {
		if(obj != null && obj instanceof MemToken)
		{
			return token.equals(((MemToken)obj).getToken());
		}
		return false;
		// TODO Auto-generated method stub
		
	}

	public boolean isValidate() {
		return validate;
	}

	public void setValidate(boolean validate) {
		this.validate = validate;
	}

	public long getLastVistTime() {
		return lastVistTime;
	}

	public void setLastVistTime(long lastVistTime) {
		this.lastVistTime = lastVistTime;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	public long getLivetime() {
		return livetime;
	}

	public void setLivetime(long livetime) {
		this.livetime = livetime;
	}

	public String getAppid() {
		return appid;
	}

	public void setAppid(String appid) {
		this.appid = appid;
	}

	public String getSecret() {
		return secret;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}

	public String getSigntoken() {
		return signtoken;
	}

	public void setSigntoken(String signtoken) {
		this.signtoken = signtoken;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}



}
