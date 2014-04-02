package org.frameworkset.web.token;

import java.util.UUID;

public abstract class BaseTokenStore implements TokenStore {
	protected long tempTokendualtime;
	protected String randomToken()
	{
		String token = UUID.randomUUID().toString();
		return token;
	}
	
	protected boolean isold(MemToken token)
	{
		long currentTime = System.currentTimeMillis();
		long age = currentTime - token.getCreateTime();		
		return age > this.tempTokendualtime;
		
	}
	protected String encodeToken(MemToken token,String tokentype)
	{
		
	}
	protected TokenInfo decodeToken(String token)
	{
		TokenInfo tokenInfo = new TokenInfo();
		tokenInfo.setAppid("appid");
		tokenInfo.setToken(token);
		tokenInfo.setSecret("secret");
		tokenInfo.setTokentype("tt");
		return tokenInfo;
	}
	protected boolean isold(MemToken token,long livetime,long currentTime)
	{
		long age = currentTime - token.getCreateTime();		
		return age > livetime;
		
	}
	
	@Override
	public long getTempTokendualtime() {
		// TODO Auto-generated method stub
		return tempTokendualtime;
	}

	@Override
	public void setTempTokendualtime(long tempTokendualtime) {
		this.tempTokendualtime = tempTokendualtime;
		
	}
	
	public MemToken genAuthTempToken(String appid, String secret)
	{
		return null;
	}
	
	public static class TokenInfo
	{
		private String appid;
		private String secret;
		private String token;
		private String tokentype;
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
		public String getToken() {
			return token;
		}
		public void setToken(String token) {
			this.token = token;
		}
		public String getTokentype() {
			return tokentype;
		}
		public void setTokentype(String tokentype) {
			this.tokentype = tokentype;
		}
	}
	
	public Integer checkToken(String token)
	{
		
		if(token == null)
		{
			return TokenStore.temptoken_request_validateresult_nodtoken; 
		}
		TokenInfo tokeninfo = this.decodeToken(token);
		if(tokeninfo.getTokentype().equals("tt"))//无需认证的临时令牌
		{
			return this.checkTempToken(tokeninfo);
		}
		else if(tokeninfo.getTokentype().equals("at"))//需要认证的临时令牌
		{
			return this.checkAuthTempToken(tokeninfo);
		}
		else if(tokeninfo.getTokentype().equals("dt"))//有效期令牌校验
		{
			return this.checkAuthTempToken(tokeninfo);
		}
		else
		{
			return this.checkTempToken(tokeninfo);
		}
			
			
	}
	
	public Integer checkAuthTempToken(TokenInfo token)
	{
		return TokenStore.temptoken_request_validateresult_fail;
	}
	
	

}
