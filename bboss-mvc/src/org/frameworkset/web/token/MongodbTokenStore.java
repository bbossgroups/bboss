package org.frameworkset.web.token;

import org.apache.log4j.Logger;
import org.frameworkset.nosql.mongodb.MongoDBHelper;
import org.frameworkset.security.ecc.SimpleKeyPair;

import com.mongodb.BasicDBObject;
import com.mongodb.CommandResult;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;


public class MongodbTokenStore extends BaseTokenStore{
	private static Logger log = Logger.getLogger(MongodbTokenStore.class);
//	private  Map<String,MemToken> temptokens = new HashMap<String,MemToken>();
//	private  Map<String,MemToken> dualtokens = new HashMap<String,MemToken>();
//	private final Object checkLock = new Object();
//	private final Object dualcheckLock = new Object();
	private Mongo mongoClient;
	
	private DB db = null;
	private DBCollection temptokens = null;
	private DBCollection authtemptokens = null;
	private DBCollection dualtokens = null;
	private DBCollection eckeypairs = null;
	public void requestStart()
	{
		if(db != null)
		{
			db.requestStart();
		}
	}
	public void requestDone()
	{
		if(db != null)
		{
			db.requestDone();
		}
	}
	
	public CommandResult getLastError()
	{
		if(db != null)
		{
			return db.getLastError();
		}
		return null;
	}
	
	public MongodbTokenStore()
	{
		mongoClient = MongoDBHelper.getMongoClient(MongoDBHelper.defaultMongoDB);
		db = mongoClient.getDB( "tokendb" );
		authtemptokens = db.getCollection("authtemptokens");
		authtemptokens.createIndex(new BasicDBObject("appid", 1).append("secret", 1).append("token", 1));
		temptokens = db.getCollection("temptokens");
		temptokens.createIndex(new BasicDBObject("token", 1));
		dualtokens = db.getCollection("dualtokens");
		dualtokens.createIndex(new BasicDBObject("appid", 1).append("secret", 1));
		eckeypairs = db.getCollection("eckeypair");
		eckeypairs.createIndex(new BasicDBObject("appid", 1));
	}
	public void destory()
	{
		
		if(mongoClient != null)
		{
			try {
				mongoClient.close();
			} catch (Exception e) {
				log.error("", e);
			}
		}
		
	}
	public void livecheck()
	{
		
		long curtime = System.currentTimeMillis();
//		synchronized(this.checkLock)
		
		StringBuffer wherefun = new StringBuffer();
		wherefun.append("function() ")
				.append("{")			
			    .append(" if(this.createTime + this.livetime < ").append(curtime).append(")")
			    .append("{")
				.append("return true;")				
				.append("}")
				.append("else")
				.append(" {")
				.append(" return false;")		
				.append("}")
				.append("}");
		String temp = wherefun.toString();

		try
		{
			temptokens.remove(new BasicDBObject("$where",temp));
			
		}
		finally
		{
			
		}		
//		

		try
		{
			dualtokens.remove(new BasicDBObject("$where",temp));
			
		}
		finally
		{
			
		}		

		try
		{
			authtemptokens.remove(new BasicDBObject("$where",temp));
		}
		finally
		{
			
		}
		
		
	}
//	public void livecheck()
//	{
//		List<BasicDBObject> dolds = new ArrayList<BasicDBObject>();
//		DBCursor cursor = null;
////		synchronized(this.checkLock)
//		{
//			try
//			{
//				cursor = this.temptokens.find();				
//				while(cursor.hasNext())
//				{	
//					DBObject tt = cursor.next();
//					MemToken token_m = totempToken(tt);				
//					if(isold(token_m))
//					{
//						dolds.add(new BasicDBObject("token", token_m.getToken()));
//					}
//				}
//			}
//			finally
//			{
//				if(cursor != null)
//				{
//					cursor.close();
//					cursor = null;
//				}
//			}
//		}
//		
//		for(int i = 0; i < dolds.size(); i ++)
//		{
////			if(tokenMonitor.isKilldown())
////				break;
//			temptokens.remove(dolds.get(i));
//		}
//		
//		dolds = new ArrayList<BasicDBObject>();
//		try
//		{
//			cursor = this.dualtokens.find();
//			while(cursor.hasNext())
//			{	
//				DBObject tt = cursor.next();
////				MemToken token_m = new MemToken((String)tt.get("token"),(Long)tt.get("createTime"));		
//				MemToken token_m = todualToken(tt);
//				if(isold(token_m,token_m.getLivetime(),System.currentTimeMillis()))
//				{
//					dolds.add(new BasicDBObject("appid", (String)tt.get("appid")).append("secret", (String)tt.get("secret")));
//				}
//			}
//		}
//		finally
//		{
//			if(cursor != null)
//			{
//				cursor.close();
//				cursor = null;
//			}
//		}
//		
//		for(int i = 0; i < dolds.size(); i ++)
//		{
//			dualtokens.remove(dolds.get(i));
//		}
//		
//		dolds = new ArrayList<BasicDBObject>();
//		try
//		{
//			cursor = this.authtemptokens.find();
//			while(cursor.hasNext())
//			{	
//				DBObject tt = cursor.next();
////				MemToken token_m = new MemToken((String)tt.get("token"),(Long)tt.get("createTime"));		
//				MemToken token_m = todualToken(tt);
//				if(isold(token_m,token_m.getLivetime(),System.currentTimeMillis()))
//				{
//					dolds.add(new BasicDBObject("appid", (String)tt.get("appid")).append("secret", (String)tt.get("secret")).append("token", (String)tt.get("token")));
//				}
//			}
//		}
//		finally
//		{
//			if(cursor != null)
//			{
//				cursor.close();
//				cursor = null;
//			}
//		}
//		for(int i = 0; i < dolds.size(); i ++)
//		{
//			authtemptokens.remove(dolds.get(i));
//		}
//		
//		dolds = null;
//		
//	}
	
	private MemToken todualToken(DBObject object)
	{
		if(object == null)
			return null;
		MemToken token = new MemToken((String)object.get("token"),(Long)object.get("createTime"), (Boolean)object.get("validate"),
					(Long)object.get("lastVistTime"), (Long)object.get("livetime"));
		token.setAppid((String)object.get("appid"));
		token.setSecret((String)object.get("secret"));
		return token;
	}
	
	private MemToken totempToken(DBObject object)
	{
		MemToken token = new MemToken((String)object.get("token"),(Long)object.get("createTime"));
		token.setLivetime((Long)object.get("livetime"));
		return token;
	}
	
	
	public Integer checkAuthTempToken(TokenResult tokeninfo)
	{
		
		if(tokeninfo != null)
		{
			
//			synchronized(checkLock)
//			DBCursor cursor = null;
			BasicDBObject dbobject = new BasicDBObject("token", tokeninfo.getToken()).append("appid", tokeninfo.getAppid());
			try
			{
				DBObject tt = this.authtemptokens.findAndRemove(dbobject);
				if(tt != null)
				{
//					DBObject tt = cursor.next();
	//				authtemptokens.remove(dbobject);
					MemToken token_m = totempToken(tt);					
					if(!this.isold(token_m))
					{
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
			finally
			{
//				if(cursor != null)
//				{
//					cursor.close();
//					cursor = null;
//				}
			}
		}
		else 
		{
			return TokenStore.temptoken_request_validateresult_nodtoken;
		}
	}
	
	public Integer checkTempToken(TokenResult tokeninfo)
	{
		
		if(tokeninfo != null)
		{
//			synchronized(checkLock)
			BasicDBObject dbobject = new BasicDBObject("token", tokeninfo.getToken());
			DBObject tt = temptokens.findAndRemove(dbobject);
			if(tt != null)
			{
//				DBObject tt = cursor.next();
				MemToken token_m = totempToken(tt);					
				if(!this.isold(token_m))
				{
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
			return TokenStore.temptoken_request_validateresult_nodtoken;
		}
	}

	private MemToken queryDualToken(String appid, String secret)
	{
		return queryDualToken(appid, secret,-1);
	}
	private MemToken queryDualToken(String appid, String secret,long lastVistTime)
	{
		BasicDBObject dbobject = new BasicDBObject("appid", appid);
		MemToken tt = null;
		DBCursor cursor = null;
		DBObject dt = null;
		if(lastVistTime > 0)
		{
			
			dt = dualtokens.findAndModify(dbobject, new BasicDBObject("$set",new BasicDBObject("lastVistTime", lastVistTime)));
			tt = todualToken(dt); 
		}
		else
		{
			try
			{
				cursor = dualtokens.find(dbobject);
				if(cursor.hasNext())
				{
					dt = cursor.next();
					tt = todualToken(dt); 
				}
			}
			finally
			{
				if(cursor != null)
				{
					cursor.close();
				}
			}
		}
		
		
		return tt;
	}
	@Override
	public Integer checkDualToken(TokenResult tokeninfo) {
		
		
		if(tokeninfo != null)
		{	
			
			String appid=tokeninfo.getAppid();String secret=tokeninfo.getSecret();
			String dynamictoken=tokeninfo.getToken();
			long lastVistTime = System.currentTimeMillis();
			MemToken tt = queryDualToken( appid, secret,lastVistTime);
			if(tt != null )//is first request,and clear temp token to against Cross Site Request Forgery
			{
				if(tt.getToken().equals(dynamictoken))
				{
					if(!this.isold(tt,tt.getLivetime(),lastVistTime))
					{
						tt.setLastVistTime(lastVistTime);
						if(tt.isValidate())
							return TokenStore.temptoken_request_validateresult_ok;
						else
							return TokenStore.temptoken_request_validateresult_invalidated;
					}
					else
					{
						return TokenStore.temptoken_request_validateresult_expired;
					}
					
				}
				else
					return TokenStore.temptoken_request_validateresult_fail;
				
			}
			else
			{
				return TokenStore.temptoken_request_validateresult_fail;
			}
		}
		else 
		{
			return TokenStore.temptoken_request_validateresult_nodtoken;
		}
		
		
	}

	private void insertDualToken(DBCollection dualtokens,MemToken dualToken)
	{
		dualtokens.insert(new BasicDBObject("token",dualToken.getToken())
		.append("createTime", dualToken.getCreateTime())
		.append("lastVistTime", dualToken.getLastVistTime())
		.append("livetime", dualToken.getLivetime())
		.append("appid", dualToken.getAppid())
		.append("secret", dualToken.getSecret())
		.append("validate", dualToken.isValidate()));
	}
	
	private void updateDualToken(MemToken dualToken)
	{
		this.dualtokens.update(new BasicDBObject(
		"appid", dualToken.getAppid()),
		new BasicDBObject("token",dualToken.getToken())
		.append("createTime", dualToken.getCreateTime())
		.append("lastVistTime", dualToken.getLastVistTime())
		.append("livetime", dualToken.getLivetime())
		.append("appid", dualToken.getAppid())
		.append("secret", dualToken.getSecret())
		.append("validate", dualToken.isValidate()));
	}

	@Override
	public MemToken genTempToken() throws TokenException {
		String token = this.randomToken();
		MemToken token_m = new MemToken(token,System.currentTimeMillis());
		temptokens.insert(new BasicDBObject("token",token_m.getToken()).append("createTime", token_m.getCreateTime()).append("livetime", this.tempTokendualtime).append("validate", true));
		this.signToken(token_m, type_temptoken, null,null);
		return token_m;
		
	}
	
	@Override
	public MemToken genDualToken(String appid,String ticket, String secret, long livetime) throws TokenException {
		
		String accountinfo[] = this.decodeTicket(ticket, appid, secret);
		MemToken token_m = null;
//		synchronized(this.dualcheckLock)
		
		token_m = queryDualToken( appid, secret);
		if(token_m != null)
		{
			long lastVistTime = System.currentTimeMillis();
			if(isold(token_m, livetime,lastVistTime))//如果令牌已经过期，重新申请新的令牌
			{
				//刷新过期的有效期令牌
				String token = this.randomToken();
				token_m.setLastVistTime(lastVistTime);
//					this.dualtokens.remove(key);
				long createTime = System.currentTimeMillis();
				token_m = new MemToken(token, createTime, true,
						createTime, livetime);
				token_m.setAppid(appid);
				token_m.setSecret(secret);
				updateDualToken(token_m);
				
			}
		}
		else
		{
			String token = this.randomToken();
			long createTime = System.currentTimeMillis();
			token_m = new MemToken(token, createTime, true,
					createTime, livetime);
			token_m.setAppid(appid);
			token_m.setSecret(secret);
			this.insertDualToken(this.dualtokens,token_m);
		}
		
		this.signToken(token_m,TokenStore.type_dualtoken,accountinfo,ticket);
		return token_m ;
		
	}
	/**
	 * 创建带认证的临时令牌
	 * @param string
	 * @param string2
	 * @return
	 * @throws TokenException 
	 */
	public MemToken genAuthTempToken(String appid,String ticket, String secret) throws TokenException {
		String accountinfo[] = this.decodeTicket(ticket, appid, secret);
		String token = this.randomToken();//需要将appid,secret,token进行混合加密，生成最终的token进行存储，校验时，只对令牌进行拆分校验
		
		MemToken token_m = null;
//		synchronized(this.dualcheckLock)
		{
		
			{
				long createTime = System.currentTimeMillis();
				token_m = new MemToken(token, createTime, true,
						createTime, this.tempTokendualtime);
				token_m.setAppid(appid);
				token_m.setSecret(secret);
				this.insertDualToken(this.authtemptokens,token_m);
			}
		}
		this.signToken(token_m,TokenStore.type_authtemptoken,accountinfo,ticket);
		return token_m ;
	}
	
	public SimpleKeyPair getKeyPair(String appid,String secret) throws TokenException
	{
		DBCursor cursor = null;
		try
		{
			cursor = eckeypairs.find(new BasicDBObject("appid", appid));
			if(cursor.hasNext())
			{
				DBObject value = cursor.next();
				return toECKeyPair(value);
				
			}
			else
			{
				try {
					SimpleKeyPair keypair = ECCCoder.genECKeyPair();
					insertECKeyPair( appid, secret, keypair);
					return keypair;
				} catch (Exception e) {
					throw new TokenException(TokenStore.ERROR_CODE_GETKEYPAIRFAILED,e);
				}
			}
		}
		finally
		{
			if(cursor != null)
			{
				cursor.close();
			}
		}
	}
	private void insertECKeyPair(String appid,String secret,SimpleKeyPair keypair)
	{
		this.eckeypairs.insert(new BasicDBObject("appid",appid)		
		.append("privateKey", keypair.getPrivateKey())
		.append("createTime", System.currentTimeMillis())
		.append("publicKey", keypair.getPublicKey()) );
	}
	
	protected SimpleKeyPair toECKeyPair(DBObject value)
	{
		SimpleKeyPair ECKeyPair = new SimpleKeyPair((String)value.get("privateKey"),(String)value.get("publicKey"),null,null);
		return ECKeyPair;
	}	

}
