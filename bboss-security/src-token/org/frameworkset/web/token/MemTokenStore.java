package org.frameworkset.web.token;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.frameworkset.security.ecc.SimpleKeyPair;

import com.mongodb.BasicDBObject;

public class MemTokenStore extends BaseTokenStore{
	private  Map<String,MemToken> temptokens = new HashMap<String,MemToken>();
	private  Map<String,MemToken> authtemptokens = new HashMap<String,MemToken>();
	private  Map<String,MemToken> dualtokens = new HashMap<String,MemToken>();
	private  Map<String,Ticket> tickets = new HashMap<String,Ticket>();
	private  Map<String,SimpleKeyPair> keypairs = new HashMap<String,SimpleKeyPair>();
	private final Object checkLock = new Object();
	private final Object dualcheckLock = new Object();
	private final Object authtempcheckLock = new Object();
	
	
	
	public void destory()
	{
		temptokens.clear();
		temptokens = null;
		
	}
	
	public void livecheck()
	{
		long curtime = System.currentTimeMillis();
		List<String> olds = new ArrayList<String>();
		synchronized(this.checkLock)
		{
			Set<Entry<String, MemToken>> keySet = this.temptokens.entrySet();
			Iterator<Entry<String, MemToken>> itr = keySet.iterator();			
			while(itr.hasNext())
			{	
				Entry<String, MemToken> token = itr.next();
				if(isold(token.getValue(),curtime))
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
				
				if(isold(token.getValue(),token.getValue().getLivetime(),curtime))
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
					
					if(isold(token.getValue(),token.getValue().getLivetime(),curtime))
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
		
		//清理过期ticket
		 olds = new ArrayList<String>();
			synchronized(this.tickets)
			{
				Set<Entry<String, Ticket>> keySet = this.tickets.entrySet();
				Iterator<Entry<String, Ticket>> itr = keySet.iterator();			
				while(itr.hasNext())
				{	
					Entry<String, Ticket> ticket = itr.next();
					
					if(isoldticket(ticket.getValue(),curtime))
					{
						olds.add(ticket.getKey());
					}
				}
			}
			
			for(int i = 0; i < olds.size(); i ++)
			{
				tickets.remove(olds.get(i));
			}
		
	}
	
	
	
	
	
	@Override
	protected MemToken getDualMemToken(String token, String appid,
			long lastVistTime) {
		String key = appid;
		MemToken tt = dualtokens.get(key);
		return tt;
	}

	@Override
	protected MemToken getAuthTempMemToken(String token, String appid) {
		String key = appid + ":"+token;
		
		MemToken tt = authtemptokens.remove(key);
		return tt;
	}

	@Override
	protected MemToken getTempMemToken(String token, String appid) {
		MemToken tt =temptokens.remove(token);
		return tt;
	}
	

	

	@Override
	public MemToken _genTempToken() throws TokenException {
		String token = this.randomToken();
		MemToken token_m = new MemToken(token,System.currentTimeMillis());
		synchronized(checkLock)
		{
			
			temptokens.put(token, token_m);
			
		}
		this.signToken(token_m, type_temptoken, null,null);
		return token_m;
		
	}

	@Override
	protected MemToken _genDualToken(String appid,String ticket, String secret, long livetime) throws TokenException {
		String[] accountinfo = decodeTicket( ticket,
				 appid,  secret);
		String token = this.randomToken();
		String key = appid ;
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
					token_m.setAppid(appid);
					token_m.setSecret(secret);
					this.signToken(token_m, TokenStore.type_authtemptoken, accountinfo,ticket);
					dualtokens.put(key, token_m);
				}
			}
			else
			{
				long createTime = System.currentTimeMillis();
				token_m = new MemToken(token, createTime, true,
						createTime, livetime);
				token_m.setAppid(appid);
				token_m.setSecret(secret);
				this.signToken(token_m, TokenStore.type_authtemptoken, accountinfo,ticket);
				dualtokens.put(key, token_m);
			}
		}
		
		return token_m ;
		
	}
	
	@Override
	protected MemToken _genAuthTempToken(String appid,String ticket, String secret) throws TokenException {
		String[] accountinfo = decodeTicket( ticket,
				 appid,  secret);
		String token = this.randomToken();
		String key = appid + ":"+token;
		MemToken token_m = null;
		synchronized(this.authtempcheckLock)
		{
			
			
			long createTime = System.currentTimeMillis();
			token_m = new MemToken(token, createTime, true,
					createTime, this.tempTokendualtime);
			token_m.setAppid(appid);
			token_m.setSecret(secret);
			this.signToken(token_m, TokenStore.type_authtemptoken, accountinfo,ticket);
			this.authtemptokens.put(key, token_m);
			
		}
		
		return token_m ;
		
	}

	@Override
	protected SimpleKeyPair _getKeyPair(String appid, String secret)
			throws TokenException {
		try {
			SimpleKeyPair ECKeyPair = keypairs.get(appid);//			cursor = eckeypairs.find(new BasicDBObject("appid", appid));
			if(ECKeyPair != null)
			{
//				DBObject value = cursor.next();
//				return toECKeyPair(value);
				return ECKeyPair;
				
			}
			else
			{
				ECKeyPair = ECCCoder.genECKeyPair();
				this.keypairs.put(appid, ECKeyPair);
				return ECKeyPair;
			}
		}catch (TokenException e) {
			throw (e);
		} 
		catch (Exception e) {
			throw new TokenException(TokenStore.ERROR_CODE_GETKEYPAIRFAILED,e);
		}
	}

	@Override
	protected void persisteTicket(Ticket ticket) {
		synchronized(tickets)
		{
			tickets.put(ticket.getToken(), ticket);
		}
		
	}

	@Override
	protected Ticket getTicket(String token, String appid) {
		
		Ticket ticket = this.tickets.get(token);
		if(ticket != null)
		{
			long lastVistTime =  System.currentTimeMillis();
			assertExpiredTicket(ticket,appid,lastVistTime);
			ticket.setLastVistTime(lastVistTime);
		}
		
		return ticket;
	}

	protected boolean refreshTicket(String token,String appid) 
	{
		if(getTicket( token,  appid) != null)
			return true;
		else
			return false;
	}
	protected boolean destroyTicket(String token,String appid)
	{
		try {
			if(this.tickets.remove( token) != null)
				return true;
			return false;
		} catch (Exception e) {
			throw new TokenException("destroy ticket["+token+"] of app["+appid+"] failed:",e);
		}
	}
	
	
	
}
