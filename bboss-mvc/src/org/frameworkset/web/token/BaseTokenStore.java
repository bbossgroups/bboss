package org.frameworkset.web.token;

import java.util.UUID;

import org.frameworkset.security.ecc.ECCCoder;
import org.frameworkset.security.ecc.ECCCoder.ECKeyPair;
import org.frameworkset.util.Base64;

//import com.mongodb.DBObject;


public abstract class BaseTokenStore implements TokenStore {
	protected long tempTokendualtime;
	protected long ticketdualtime;
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
//	protected String encodeToken(MemToken token,String tokentype,String ticket) throws TokenException
//	{
//		signToken( token, tokentype, ticket);
//		return token.getSigntoken();
//			
//	}
	
	public String genTicket(String account, String worknumber,
			String appid, String secret) throws TokenException
	{
		long createTime = System.currentTimeMillis();
		if(worknumber == null)
		{
			worknumber = "";
		}
		try {
			String ticket = account+"|"+worknumber +"|"+createTime;
			ECKeyPair keyPairs = getKeyPair(appid,secret);
			byte[] data =  null;
			if(keyPairs.getPubKey() != null)
			{
				
				data = ECCCoder.encrypt(ticket.getBytes(), keyPairs.getPubKey());
				ticket= Base64.encode(data);
				
			}
			else
			{
				data = ECCCoder.encrypt(ticket.getBytes(), keyPairs.getPublicKey());
				ticket= Base64.encode(data);
			}
			return ticket;
		} catch (TokenException e) {
			throw e;
		} catch (Exception e) {
			throw new TokenException(e);
		}
		
		
	}
	/**
	 * 0 account 1 worknumber
	 */
	public String[] decodeTicket(String ticket,
			String appid, String secret) throws TokenException
	{
		
		try {
			String accountinfo = null;
			ECKeyPair keyPairs = getKeyPair(appid,secret);
			byte[] data =  null;
			if(keyPairs.getPriKey() != null)
			{
				
				data = ECCCoder.decrypt(ticket, keyPairs.getPriKey());
				accountinfo = new String(data);
				
//			return signtoken;
			}
			else
			{
				data = ECCCoder.encrypt(ticket.getBytes(), keyPairs.getPublicKey());
				accountinfo = new String(data);
//			return signtoken;
			}
			String infs[] = accountinfo.split("\\|");
			long createTime = Long.parseLong(infs[2]);
			if(createTime + this.ticketdualtime < System.currentTimeMillis())
			{
				throw new TokenException("ticketexpired");
			}
			return infs;
		} catch (TokenException e) {
			throw e;
		} catch (Exception e) {
			throw new TokenException(e);
		}
		
		
	}
	
	protected void signToken(MemToken token,String tokentype,String accountinfo[],String ticket) throws TokenException
	{
		try {
			if(tokentype == null || tokentype.equals(TokenStore.type_temptoken))
			{
				token.setSigntoken(TokenStore.type_temptoken+"_" + token.getToken());
			}
			else if(tokentype.equals(TokenStore.type_authtemptoken))
			{			
				ECKeyPair keyPairs = getKeyPair(token.getAppid(),token.getSecret());
				String input = accountinfo[0] + "|" + accountinfo[1] + "|" + token.getToken();
				byte[] data =  null;
				if(keyPairs.getPubKey() != null)
				{
					
					data = ECCCoder.encrypt(input.getBytes(), keyPairs.getPubKey());
					String signtoken =TokenStore.type_authtemptoken+"_"+ Base64.encode(data);
					token.setSigntoken(signtoken);
//					return signtoken;
				}
				else
				{
					data = ECCCoder.encrypt(input.getBytes(), keyPairs.getPublicKey());
					String signtoken = TokenStore.type_authtemptoken+"_"+Base64.encode(data);
					token.setSigntoken(signtoken);
//					return signtoken;
				}
				
				
			}
			else if(tokentype.equals(TokenStore.type_dualtoken))
			{			
				ECKeyPair keyPairs = getKeyPair(token.getAppid(),token.getSecret());
				String input = accountinfo[0] + "|" + accountinfo[1] + "|" + "|" + token.getToken();
				byte[] data =  null;
				if(keyPairs.getPubKey() != null)
				{				
					data = ECCCoder.encrypt(input.getBytes(), keyPairs.getPubKey());
					String signtoken = TokenStore.type_dualtoken+"_"+Base64.encode(data);
					token.setSigntoken(signtoken);
//					return signtoken;
				}
				else
				{
					data = ECCCoder.encrypt(input.getBytes(), keyPairs.getPublicKey());
					String signtoken = TokenStore.type_dualtoken+"_"+Base64.encode(data);
					token.setSigntoken(signtoken);
//					return signtoken;
				}
				
				
			}
			else
			{
				throw new TokenException("unknowntokentype："+tokentype+",token="+token.getToken()+",appid="+token.getAppid()+",ticket="+ticket);
			}
		} catch (TokenException e) {
			throw e;
		} catch (Exception e) {
			throw  new TokenException(e);
		}
		
			
	}
	protected TokenResult decodeToken(String appid,String secret,String token) throws TokenException
	{
		TokenResult tokenInfo = new TokenResult();
		char line = token.charAt(2);
		String signtoken = null;
		if(line =='_')
		{
			String tokentype = token.substring(0,2);
			 
			if(tokentype.equals(TokenStore.type_temptoken))//无需认证的临时令牌
			{
				
				tokenInfo.setTokentype(tokentype);
				
				tokenInfo.setToken(token.substring(3));
			}
			else if(tokentype.equals(TokenStore.type_authtemptoken))//需要认证的临时令牌
			{
				try {
					tokenInfo.setTokentype(tokentype);
					signtoken = token.substring(3);
					ECKeyPair keyPairs = getKeyPair(appid,secret);
					
					tokenInfo.setAppid(appid);
					String mw = new String(ECCCoder.decrypt(Base64.decode(signtoken), keyPairs.getPrivateKey()));
					String[] t = mw.split("\\|");
					tokenInfo.setToken(t[1]);
					tokenInfo.setAccount(t[0]);
					tokenInfo.setSecret(secret);
				} catch (Exception e) {
					throw new TokenException(e);
				}
			}
			else if(tokentype.equals(TokenStore.type_dualtoken))//有效期令牌校验
			{
				try {
					tokenInfo.setTokentype(tokentype);
					signtoken = token.substring(3);
					ECKeyPair keyPairs = getKeyPair(appid,secret);
					
					tokenInfo.setAppid(appid);
					String mw = new String(ECCCoder.decrypt(Base64.decode(signtoken), keyPairs.getPrivateKey()));
					String[] t = mw.split("\\|");
					tokenInfo.setToken(t[1]);
					tokenInfo.setAccount(t[0]);
					tokenInfo.setSecret(secret);
				} catch (Exception e) {
					throw new TokenException(e);
				}
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
	
	
	
	public TokenResult checkToken(String appid,String secret,String token) throws TokenException
	{
		
		if(token == null)
		{
			TokenResult result = new TokenResult();
			result.setResult(TokenStore.temptoken_request_validateresult_nodtoken);
			return result; 
		}
		
		TokenResult tokeninfo = this.decodeToken(appid,secret,token);
		Integer result = null;
		
		if(tokeninfo.getTokentype().equals(TokenStore.type_temptoken))//无需认证的临时令牌
		{
			result = this.checkTempToken(tokeninfo);			
		}
		else if(tokeninfo.getTokentype().equals(TokenStore.type_authtemptoken))//需要认证的临时令牌
		{
			result = this.checkAuthTempToken(tokeninfo);
		}
		else if(tokeninfo.getTokentype().equals(TokenStore.type_dualtoken))//有效期令牌校验
		{
			result = this.checkDualToken(tokeninfo);
		}
		if(result != null)
		{
			tokeninfo.setResult(result);
			return tokeninfo;
		}
		
		throw new TokenException("unknowntoken:appid="+appid+",secret="+ secret+",token="+token);
			
		
		
			
			
	}
	
	public Integer checkAuthTempToken(TokenResult token)
	{
		return TokenStore.temptoken_request_validateresult_fail;
	}
	
	public ECKeyPair getKeyPair(String appid,String secret) throws TokenException
	{
		return null;
	}
	
	

	public void setTicketdualtime(long ticketdualtime) {
		this.ticketdualtime = ticketdualtime;
	}

	public long getTicketdualtime() {
		return ticketdualtime;
	}
	
	

}
