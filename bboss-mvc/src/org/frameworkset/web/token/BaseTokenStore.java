package org.frameworkset.web.token;

import java.util.UUID;

import org.frameworkset.security.ecc.ECCCoderInf;
import org.frameworkset.security.ecc.SimpleKeyPair;
import org.frameworkset.util.Base64;

//import com.mongodb.DBObject;


public abstract class BaseTokenStore implements TokenStore {
	protected long tempTokendualtime;
	protected long ticketdualtime;
	protected long dualtokenlivetime;
	protected ECCCoderInf ECCCoder = null;
	protected ValidateApplication validateApplication;
	protected String randomToken()
	{
		String token = UUID.randomUUID().toString();
		return token;
	}
	public void setECCCoder(ECCCoderInf eCCCoder)
	{
		this.ECCCoder =  eCCCoder;
	}
	
	public ECCCoderInf getECCCoder()
	{
		return this.ECCCoder;
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
	public MemToken genDualTokenWithDefaultLiveTime(String appid,String ticket,String secret)throws TokenException
	{
		return genDualToken(appid,ticket, secret, TokenStore.DEFAULT_DUALTOKENLIVETIME) ;
	}
	
	protected void assertApplication(String appid,String secret) throws TokenException
	{
		try {
			boolean result = validateApplication.checkApp(appid, secret);
			if(!result)
			{
				throw new TokenException(TokenStore.ERROR_CODE_APPVALIDATEFAILED);
			}
		} catch (TokenException e) {
			throw e;
		} catch (Exception e) {
			throw new TokenException(TokenStore.ERROR_CODE_APPVALIDATERROR,e);
		}
			
	}
	public String genTicket(String account, String worknumber,
			String appid, String secret) throws TokenException
	{
		
		this.assertApplication(appid, secret);
		long createTime = System.currentTimeMillis();
		if(worknumber == null)
		{
			worknumber = "";
		}
		if(account == null)
		{
			account = "";
		}
		try {
			String ticket = account+"|"+worknumber +"|"+createTime;
			SimpleKeyPair keyPairs = getKeyPair(appid,secret);
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
			throw new TokenException(TokenStore.ERROR_CODE_GENTICKETFAILED,e);
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
			SimpleKeyPair keyPairs = getKeyPair(appid,secret);
			byte[] data =  null;
			if(keyPairs.getPriKey() != null)
			{
				
				data = ECCCoder.decrypt(Base64.decode(ticket), keyPairs.getPriKey());
				accountinfo = new String(data);
				
//			return signtoken;
			}
			else
			{
				data = ECCCoder.decrypt(Base64.decode(ticket), keyPairs.getPrivateKey());
				accountinfo = new String(data);
//			return signtoken;
			}
			String infs[] = accountinfo.split("\\|");
			long createTime = Long.parseLong(infs[2]);
			if(createTime + this.ticketdualtime < System.currentTimeMillis())
			{
				throw new TokenException(TokenStore.ERROR_CODE_TICKETEXPIRED);
			}
			return infs;
		} catch (TokenException e) {
			throw e;
		} catch (Exception e) {
			throw new TokenException(TokenStore.ERROR_CODE_DECODETICKETFAILED,e);
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
				SimpleKeyPair keyPairs = getKeyPair(token.getAppid(),token.getSecret());
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
				SimpleKeyPair keyPairs = getKeyPair(token.getAppid(),token.getSecret());
				String input = accountinfo[0] + "|" + accountinfo[1] +  "|" + token.getToken();
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
				throw new TokenException(TokenStore.ERROR_CODE_UNKNOWNTOKENTYPE,new Exception("unknowntokentype："+tokentype+",token="+token.getToken()+",appid="+token.getAppid()+",ticket="+ticket));
			}
		} catch (TokenException e) {
			throw e;
		} catch (Exception e) {
			throw  new TokenException(TokenStore.ERROR_CODE_SIGNTOKENFAILED,e);
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
					assertApplication( appid, secret);
					tokenInfo.setTokentype(tokentype);
					signtoken = token.substring(3);
					SimpleKeyPair keyPairs = getKeyPair(appid,secret);
					
					tokenInfo.setAppid(appid);
					String mw = new String(ECCCoder.decrypt(Base64.decode(signtoken), keyPairs.getPrivateKey()));
					String[] t = mw.split("\\|");
					tokenInfo.setToken(t[2]);
					tokenInfo.setWorknumber(t[1]);
					tokenInfo.setAccount(t[0]);
					tokenInfo.setSecret(secret);
				}catch (TokenException e) {
					throw  (e);
				} 
				catch (Exception e) {
					throw new TokenException(TokenStore.ERROR_CODE_DECODETOKENFAILED,e);
				}
			}
			else if(tokentype.equals(TokenStore.type_dualtoken))//有效期令牌校验
			{
				try {
					assertApplication( appid, secret);
					tokenInfo.setTokentype(tokentype);
					signtoken = token.substring(3);
					SimpleKeyPair keyPairs = getKeyPair(appid,secret);
					
					tokenInfo.setAppid(appid);
					String mw = new String(ECCCoder.decrypt(Base64.decode(signtoken), keyPairs.getPrivateKey()));
					String[] t = mw.split("\\|");
					tokenInfo.setToken(t[2]);
					tokenInfo.setWorknumber(t[1]);
					tokenInfo.setAccount(t[0]);
					tokenInfo.setSecret(secret);
				}catch (TokenException e) {
					throw  (e);
				} 
				catch (Exception e) {
					throw new TokenException(TokenStore.ERROR_CODE_DECODETOKENFAILED,e);
				}
			}
			else
			{
				throw new TokenException(TokenStore.ERROR_CODE_UNKNOWNTOKENTYPE,new Exception("不正确的令牌类型："+token));
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
	
	public MemToken genAuthTempToken(String appid, String ticket,String secret)  throws TokenException 
	{
		this.assertApplication(appid, secret);
		return _genAuthTempToken(appid, ticket,secret);
	}
	
	protected abstract MemToken _genAuthTempToken(String appid, String ticket,String secret)  throws TokenException;
	
	
	
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
		
		throw new TokenException(TokenStore.ERROR_CODE_UNKNOWNTOKEN,new Exception("unknowntoken:appid="+appid+",secret="+ secret+",token="+token));
			
		
		
			
			
	}
	
	public Integer checkAuthTempToken(TokenResult token)  throws TokenException 
	{
		return TokenStore.temptoken_request_validateresult_fail;
	}
	
	public SimpleKeyPair getKeyPair(String appid,String secret) throws TokenException
	{
		this.assertApplication(appid, secret);		
		return _getKeyPair( appid, secret);
	}
	
	protected abstract SimpleKeyPair _getKeyPair(String appid,String secret) throws TokenException;
	
	

	public void setTicketdualtime(long ticketdualtime) {
		this.ticketdualtime = ticketdualtime;
	}

	public long getTicketdualtime() {
		return ticketdualtime;
	}

	public long getDualtokenlivetime() {
		return dualtokenlivetime;
	}

	public void setDualtokenlivetime(long dualtokenlivetime) {
		this.dualtokenlivetime = dualtokenlivetime;
	}
	
	
	@Override
	public MemToken genDualToken(String appid, String ticket, String secret,
			long livetime) throws TokenException {
		assertApplication( appid, secret);
		return  _genDualToken( appid,  ticket,  secret,
				 livetime);
	}
	
	protected abstract MemToken _genDualToken(String appid, String ticket, String secret,
			long livetime) throws TokenException ;
	public ValidateApplication getValidateApplication() {
		return validateApplication;
	}
	public void setValidateApplication(ValidateApplication validateApplication) {
		this.validateApplication = validateApplication;
	}
	
	

}
