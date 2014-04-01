package org.frameworkset.web.token;


public interface TokenStore {


	public abstract void destory();

	public abstract void livecheck();

	public abstract Integer checkTempToken(String token);
	public abstract Integer checkAuthTempToken(String token);
	public abstract Integer checkDualToken(String token);

	public abstract long getTempTokendualtime();

	public abstract void setTempTokendualtime(long tokendualtime);

//	public abstract Session getSession();
//
//	public abstract void setSession(Session session);
	
	public MemToken getTempToken();
	public MemToken getDualToken(String appid,String secret,long livetime);
	public MemToken genAuthTempToken(String appid, String secret);
	

}