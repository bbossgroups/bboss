package org.frameworkset.web.token;

import org.frameworkset.security.session.Session;

public class SessionTokenStore extends BaseTokenStore {
	
	private final Object checkLock = new Object();
	private final Object dualcheckLock = new Object();
	private Session session;
	
	
	public void destory()
	{
				
	}
	
	public void livecheck()
	{
		
		
	}
	
	
	
	public Integer checkTempToken(TokenInfo token)
	{
		
		if(token != null)
		{
			synchronized(checkLock)
			{
				MemToken tt =(MemToken)session.getAttribute(token.getToken());
				if(tt != null  )//is first request,and clear temp token to against Cross Site Request Forgery
				{
					if(!this.isold(tt))
					{
//						temptokens.remove(token);				
						return TokenStore.temptoken_request_validateresult_ok;
					}
					else
					{
						return TokenStore.temptoken_request_validateresult_expired;
					}
				}
				else
				{
					return TokenStore.temptoken_request_validateresult_fail;
				}
			}
		}
		else 
		{
			return TokenStore.temptoken_request_validateresult_nodtoken;
		}
	}

	

	@Override
	public Integer checkDualToken(TokenInfo token) {
		
		if(token != null)
		{
//			String[] tokeninfos = decodeToken(token);
			String appid = token.getAppid(); String statictoken = token.getSecret();
			String dynamictoken = token.getToken();
			String key = appid + ":" + statictoken;
			synchronized(dualcheckLock)
			{
				MemToken tt = (MemToken)session.getAttribute(key);
				if(tt != null )//is first request,and clear temp token to against Cross Site Request Forgery
				{
					long vistTime = System.currentTimeMillis();
					if(dynamictoken.equals(tt.getToken()))
					{
						if(!this.isold(tt,tt.getLivetime(),vistTime))
						{
							tt.setLastVistTime(vistTime);
	//					temptokens.remove(token);				
							return TokenStore.temptoken_request_validateresult_ok;
						}
						else
						{
							return TokenStore.temptoken_request_validateresult_expired;
						}
					}
					else
					{
						return TokenStore.temptoken_request_validateresult_fail;
					}
				}
				else
				{
					return TokenStore.temptoken_request_validateresult_fail;
				}
			}
		}
		else 
		{
			return TokenStore.temptoken_request_validateresult_nodtoken;
		}
		
		
	}

	

	@Override
	public MemToken genTempToken() {
		String token = this.randomToken();
		MemToken token_m = new MemToken(token,System.currentTimeMillis());
		synchronized(checkLock)
		{
			
			session.setAttribute(token, token_m);
			
		}
		return token_m;
		
	}

	@Override
	public MemToken genDualToken(String appid,String account, String statictoken, long livetime) {
		String token = this.randomToken();
		String key = appid + ":"+statictoken;
		MemToken token_m = null;
		synchronized(this.dualcheckLock)
		{
			token_m = (MemToken)this.session.getAttribute(key);
			if(token_m != null)
			{
				long vistTime = System.currentTimeMillis();
				if(isold(token_m, livetime,vistTime))//如果令牌已经过期，重新申请新的令牌
				{
//					this.dualtokens.remove(key);
					token_m.setLastVistTime(vistTime);
					long createTime = System.currentTimeMillis();
					token_m = new MemToken(token, createTime, true,
							createTime, livetime);
					session.setAttribute(key, token_m);
				}
			}
			else
			{
				long createTime = System.currentTimeMillis();
				token_m = new MemToken(token, createTime, true,
						createTime, livetime);
				session.setAttribute(key, token_m);
			}
		}
		return token_m ;
		
	}

	

}
