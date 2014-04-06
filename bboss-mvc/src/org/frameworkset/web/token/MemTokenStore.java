package org.frameworkset.web.token;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class MemTokenStore extends BaseTokenStore{
	private  Map<String,MemToken> temptokens = new HashMap<String,MemToken>();
	private  Map<String,MemToken> authtemptokens = new HashMap<String,MemToken>();
	private  Map<String,MemToken> dualtokens = new HashMap<String,MemToken>();
	private final Object checkLock = new Object();
	private final Object dualcheckLock = new Object();
	private final Object authtempcheckLock = new Object();
	
	public Integer checkAuthTempToken(TokenInfo token)
	{
		if(token != null)
		{
//			String[] tokeninfos = decodeToken(token);
			String appid = token.getAppid(); String secret = token.getSecret();
			String dynamictoken = token.getToken();
			String key = appid + ":" + secret + ":"+dynamictoken;
			
			synchronized(dualcheckLock)
			{
				MemToken tt = authtemptokens.remove(key);
				
				if(tt != null )//is first request,and clear temp token to against Cross Site Request Forgery
				{
					if(tt.getToken().equals(dynamictoken))
					{
						long lastVistTime = System.currentTimeMillis();
						if(!this.isold(tt,tt.getLivetime(),lastVistTime))
						{
							tt.setLastVistTime(lastVistTime);
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
	
	public void destory()
	{
		temptokens.clear();
		temptokens = null;
		
	}
	
	public void livecheck()
	{
		List<String> olds = new ArrayList<String>();
		synchronized(this.checkLock)
		{
			Set<Entry<String, MemToken>> keySet = this.temptokens.entrySet();
			Iterator<Entry<String, MemToken>> itr = keySet.iterator();			
			while(itr.hasNext())
			{	
				Entry<String, MemToken> token = itr.next();
				if(isold(token.getValue()))
				{
					olds.add(token.getKey());
				}
			}
		}
		
		for(int i = 0; i < olds.size(); i ++)
		{
//			if(tokenMonitor.isKilldown())
//				break;
			temptokens.remove(olds.get(i));
		}
		
		 olds = new ArrayList<String>();
		synchronized(this.dualcheckLock)
		{
			Set<Entry<String, MemToken>> keySet = this.dualtokens.entrySet();
			Iterator<Entry<String, MemToken>> itr = keySet.iterator();			
			while(itr.hasNext())
			{	
				Entry<String, MemToken> token = itr.next();
				
				if(isold(token.getValue(),token.getValue().getLivetime(),System.currentTimeMillis()))
				{
					olds.add(token.getKey());
				}
			}
		}
		
		for(int i = 0; i < olds.size(); i ++)
		{
//				if(tokenMonitor.isKilldown())
//					break;
			dualtokens.remove(olds.get(i));
		}
		
		 olds = new ArrayList<String>();
			synchronized(this.authtempcheckLock)
			{
				Set<Entry<String, MemToken>> keySet = this.authtemptokens.entrySet();
				Iterator<Entry<String, MemToken>> itr = keySet.iterator();			
				while(itr.hasNext())
				{	
					Entry<String, MemToken> token = itr.next();
					
					if(isold(token.getValue(),token.getValue().getLivetime(),System.currentTimeMillis()))
					{
						olds.add(token.getKey());
					}
				}
			}
			
			for(int i = 0; i < olds.size(); i ++)
			{
//					if(tokenMonitor.isKilldown())
//						break;
				authtemptokens.remove(olds.get(i));
			}
		olds = null;
		
	}
	
	
	
	public Integer checkTempToken(TokenInfo token)
	{
		
		if(token != null)
		{
			synchronized(checkLock)
			{
				MemToken tt =temptokens.remove(token.getToken());
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
			String appid = token.getAppid(); String secret = token.getSecret();
			String dynamictoken = token.getToken();
			String key = appid + ":" + secret;
			
			synchronized(dualcheckLock)
			{
				MemToken tt = dualtokens.get(key);
				
				if(tt != null )//is first request,and clear temp token to against Cross Site Request Forgery
				{
					if(tt.getToken().equals(dynamictoken))
					{
						long lastVistTime = System.currentTimeMillis();
						if(!this.isold(tt,tt.getLivetime(),lastVistTime))
						{
							tt.setLastVistTime(lastVistTime);
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
			
			temptokens.put(token, token_m);
			
		}
		this.signToken(token_m, type_temptoken, null);
		return token_m;
		
	}

	@Override
	public MemToken genDualToken(String appid,String account, String secret, long livetime) {
		String token = this.randomToken();
		String key = appid + ":"+secret;
		MemToken token_m = null;
		synchronized(this.dualcheckLock)
		{
			token_m = this.dualtokens.get(key);
			if(token_m != null)
			{
				long lastVistTime = System.currentTimeMillis();
				if(isold(token_m, livetime,lastVistTime))//如果令牌已经过期，重新申请新的令牌
				{
					token_m.setLastVistTime(lastVistTime);
//					this.dualtokens.remove(key);
					long createTime = System.currentTimeMillis();
					token_m = new MemToken(token, createTime, true,
							createTime, livetime);
					dualtokens.put(key, token_m);
				}
			}
			else
			{
				long createTime = System.currentTimeMillis();
				token_m = new MemToken(token, createTime, true,
						createTime, livetime);
				dualtokens.put(key, token_m);
			}
		}
		this.signToken(token_m, type_dualtoken, account);
		return token_m ;
		
	}
	
	@Override
	public MemToken genAuthTempToken(String appid,String account, String secret) {
		String token = this.randomToken();
		String key = appid + ":"+secret +":"+token;
		MemToken token_m = null;
		synchronized(this.authtempcheckLock)
		{
			
			{
				long createTime = System.currentTimeMillis();
				token_m = new MemToken(token, createTime, true,
						createTime, this.tempTokendualtime);
				this.authtemptokens.put(key, token_m);
			}
		}
		this.signToken(token_m, TokenStore.type_authtemptoken, account);
		return token_m ;
		
	}
	
	
	
}
