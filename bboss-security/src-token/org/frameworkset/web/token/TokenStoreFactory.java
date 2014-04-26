package org.frameworkset.web.token;

public class TokenStoreFactory {
	
	public static TokenStore getTokenStore(String tokenstore)
	{
		if(tokenstore.equals("mem"))
		{
			tokenstore = "org.frameworkset.web.token.MemTokenStore";
		}
		else if(tokenstore.equals("mongodb"))
		{
			tokenstore = "org.frameworkset.web.token.MongodbTokenStore";
			
		}
		else if(tokenstore.equals("db"))
		{
			tokenstore = "org.frameworkset.web.token.DBTokenStore";
		}
		
		
			
		try {
			TokenStore tokenStore = (TokenStore)Class.forName(tokenstore).newInstance();
			return tokenStore;
		} catch (Exception e) {
			throw new TokenException("不支持的tokenstore类型："+tokenstore,e);
		}
		
			
	}

}
