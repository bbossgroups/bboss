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
	private  Map<String,MemToken> dualtokens = new HashMap<String,MemToken>();
	private final Object checkLock = new Object();
	private final Object dualcheckLock = new Object();
	
	
	
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
		olds = null;
		
	}
	
	
	
	public Integer existToken(String token)
	{
		
		if(token != null)
		{
			synchronized(checkLock)
			{
				MemToken tt =temptokens.remove(token);
				if(tt != null  )//is first request,and clear temp token to against Cross Site Request Forgery
				{
					if(!this.isold(tt))
					{
//						temptokens.remove(token);				
						return MemTokenManager.temptoken_request_validateresult_ok;
					}
					else
					{
						return MemTokenManager.temptoken_request_validateresult_expired;
					}
				}
				else
				{
					return MemTokenManager.temptoken_request_validateresult_fail;
				}
			}
		}
		else 
		{
			return MemTokenManager.temptoken_request_validateresult_nodtoken;
		}
	}

	

	@Override
	public Integer existToken(String appid, String statictoken,
			String dynamictoken) {
		String key = appid + ":" + statictoken;
		if(dynamictoken != null)
		{
			synchronized(dualcheckLock)
			{
				MemToken tt = dualtokens.get(key);
				if(tt != null )//is first request,and clear temp token to against Cross Site Request Forgery
				{
					long lastVistTime = System.currentTimeMillis();
					if(!this.isold(tt,tt.getLivetime(),lastVistTime))
					{
						tt.setLastVistTime(lastVistTime);
//					temptokens.remove(token);				
						return MemTokenManager.temptoken_request_validateresult_ok;
					}
					else
					{
						return MemTokenManager.temptoken_request_validateresult_expired;
					}
				}
				else
				{
					return MemTokenManager.temptoken_request_validateresult_fail;
				}
			}
		}
		else 
		{
			return MemTokenManager.temptoken_request_validateresult_nodtoken;
		}
		
		
	}

	

	@Override
	public MemToken genToken() {
		String token = this.randomToken();
		MemToken token_m = new MemToken(token,System.currentTimeMillis());
		synchronized(checkLock)
		{
			
			temptokens.put(token, token_m);
			
		}
		return token_m;
		
	}

	@Override
	public MemToken genToken(String appid, String statictoken, long livetime) {
		String token = this.randomToken();
		String key = appid + ":"+statictoken;
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
		return token_m ;
		
	}

	
	
}
