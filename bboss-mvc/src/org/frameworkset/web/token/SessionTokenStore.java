package org.frameworkset.web.token;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

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
	
	
	
	public Integer existToken(String token)
	{
		
		if(token != null)
		{
			synchronized(checkLock)
			{
				MemToken tt =(MemToken)session.getAttribute(token);
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
				MemToken tt = (MemToken)session.getAttribute(key);
				if(tt != null )//is first request,and clear temp token to against Cross Site Request Forgery
				{
					long vistTime = System.currentTimeMillis();
					if(!this.isold(tt,tt.getLivetime(),vistTime))
					{
						tt.setLastVistTime(vistTime);
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
			
			session.setAttribute(token, token_m);
			
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
