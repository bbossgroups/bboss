package org.frameworkset.web.token;

public class TokenStoreFactory {
	
	public static TokenStore getTokenStore(String tokenstore)
	{
		if(tokenstore.equals("mem"))
		{
			return new MemTokenStore();
		}
		else if(tokenstore.equals("session"))
		{
			return new SessionTokenStore();
		}
		else
		{
			
			try {
				TokenStore tokenStore = (TokenStore)Class.forName(tokenstore).newInstance();
				return tokenStore;
			} catch (Exception e) {
				return new MemTokenStore();
			}
		}
			
	}

}
