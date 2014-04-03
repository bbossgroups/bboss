package org.frameworkset.web.token;

import java.security.interfaces.ECPublicKey;
import java.util.UUID;

import org.frameworkset.security.ecc.ECCCoder;
import org.frameworkset.security.ecc.ECCCoder.ECKeyPair;

import com.sun.org.apache.xml.internal.security.utils.Base64;

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
	protected String encodeToken(MemToken token,String tokentype,String account) throws Exception
	{
		if(tokentype == null || tokentype.equals(TokenStore.type_temptoken))
		{
			return TokenStore.type_temptoken+"_" + token.getToken();
		}
		else if(tokentype.equals(TokenStore.type_authtemptoken))
		{			
			ECKeyPair keyPairs = getKeyPairs(token.getAppid(),account,token.getSecret());
			String input = account + "|" + token.getToken();
			byte[] data =  null;
			if(keyPairs.getPubKey() != null)
			{
				
				data = ECCCoder.encrypt(input.getBytes(), keyPairs.getPubKey());
				String signtoken =TokenStore.type_authtemptoken+"_"+ Base64.encode(data);
				token.setSigntoken(signtoken);
				return signtoken;
			}
			else
			{
				data = ECCCoder.encrypt(input.getBytes(), keyPairs.getPublicKey());
				String signtoken = TokenStore.type_authtemptoken+"_"+Base64.encode(data);
				token.setSigntoken(signtoken);
				return signtoken;
			}
			
			
		}
		else if(tokentype.equals(TokenStore.type_dualtoken))
		{			
			ECKeyPair keyPairs = getKeyPairs(token.getAppid(),account,token.getSecret());
			String input = account + "|" + token.getToken();
			byte[] data =  null;
			if(keyPairs.getPubKey() != null)
			{				
				data = ECCCoder.encrypt(input.getBytes(), keyPairs.getPubKey());
				String signtoken = TokenStore.type_dualtoken+"_"+Base64.encode(data);
				token.setSigntoken(signtoken);
				return signtoken;
			}
			else
			{
				data = ECCCoder.encrypt(input.getBytes(), keyPairs.getPublicKey());
				String signtoken = Base64.encode(data)+"_"+TokenStore.type_dualtoken;
				token.setSigntoken(signtoken);
				return signtoken;
			}
			
			
		}
		else
		{
			throw new TokenException("无法识别的令牌类型："+tokentype+",token="+token.getToken()+",appid="+token.getAppid()+",account="+account);
		}
			
	}
	protected TokenInfo decodeToken(String appid,String secret,String token) throws Exception
	{
		TokenInfo tokenInfo = new TokenInfo();
		char line = token.charAt(2);
		if(line =='_')
		{
			String tokentype = token.substring(0,1);
			 
			if(tokentype.equals(TokenStore.type_temptoken))//无需认证的临时令牌
			{
				
				tokenInfo.setTokentype(tokentype);
				tokenInfo.setToken(token.substring(3));
			}
			else if(tokentype.equals(TokenStore.type_temptoken))//需要认证的临时令牌
			{
				tokenInfo.setTokentype(tokentype);
				tokenInfo.setToken(token.substring(3));
			}
			else if(tokentype.equals("dt"))//有效期令牌校验
			{
				tokenInfo.setTokentype(tokentype);
				tokenInfo.setToken(token.substring(3));
			}
			else
			{
				throw new TokenException("不正确的令牌类型："+token);
			}
			
		}
		else
		{
			tokenInfo.setTokentype(TokenStore.type_temptoken);
			tokenInfo.setToken(token);
		}
		ECKeyPair keyPairs = getKeyPairs(appid,null,secret);
		
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
	
	public MemToken genAuthTempToken(String appid, String account,String secret)
	{
		return null;
	}
	
	public static class TokenInfo
	{
		private String appid;
		private String secret;
		private String token;
		private String tokentype;
		private String account;
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
		public String getAccount() {
			return account;
		}
		public void setAccount(String account) {
			this.account = account;
		}
	}
	
	public Integer checkToken(String appid,String secret,String token) throws Exception
	{
		
		if(token == null)
		{
			return TokenStore.temptoken_request_validateresult_nodtoken; 
		}
		
		TokenInfo tokeninfo = this.decodeToken(appid,secret,token);
		if(line =='_')
		{
			
			
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
			
		}
		else
		{
			TokenInfo tokeninfo = new TokenInfo();
			tokeninfo.setToken(token);
			return this.checkTempToken(tokeninfo);
		}
			
			
	}
	
	public Integer checkAuthTempToken(TokenInfo token)
	{
		return TokenStore.temptoken_request_validateresult_fail;
	}
	
	public ECKeyPair getKeyPairs(String appid,String account,String secret) throws Exception
	{
		return null;
	}
	
	

}
