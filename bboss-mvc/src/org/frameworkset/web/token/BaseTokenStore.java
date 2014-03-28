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

}
