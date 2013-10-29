package org.frameworkset.web.token;

public class MemTokenManagerFactory {
	private static MemTokenManager memTokenManager;
	/**
	 * used by TokenFilter
	 * @param dualtime
	 * @return
	 */
	public static MemTokenManager getMemTokenManager(long dualtime,long tokenscaninterval,boolean enableToken,String tokenstore,TokenFilter tokenFilter)
	{
		if(memTokenManager == null)
		{
			synchronized(MemTokenManager.class)
			{
				if(memTokenManager == null)
					memTokenManager = new MemTokenManager(dualtime,tokenscaninterval,enableToken,tokenstore, tokenFilter);
			}
		}
		return memTokenManager;
	}
	public static MemTokenManager getMemTokenManager()
	{
		if(memTokenManager == null)
		{
			throw new RuntimeException("MemTokenManager not build corrected using getMemTokenManager(long dualtime)");
		}
		return memTokenManager;
	}
	/**
	 * 如果没有开启动态令牌检测机制，返回null，开启了就返回令牌管理对象（如果系统配置开启令牌检测机制，则该对象在TokenFilter中进行初始化，否则不会初始化）
	 * @return
	 */
	public static MemTokenManager getMemTokenManagerNoexception()
	{
		return memTokenManager;
	}

}
