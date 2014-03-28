package org.frameworkset.web.token;


public interface TokenStore {


	public abstract void destory();

	public abstract void livecheck();

	public abstract Integer existToken(String token);
	public abstract Integer existToken(String appid,String statictoken,String dynamictoken);

	public abstract long getTempTokendualtime();

	public abstract void setTempTokendualtime(long tokendualtime);

//	public abstract Session getSession();
//
//	public abstract void setSession(Session session);
	
	public MemToken genToken();
	public MemToken genToken(String appid,String statictoken,long livetime);
	

}