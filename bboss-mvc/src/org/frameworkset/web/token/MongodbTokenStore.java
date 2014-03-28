package org.frameworkset.web.token;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.frameworkset.nosql.mongodb.MongodbHelper;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;


public class MongodbTokenStore extends BaseTokenStore{
	private static Logger log = Logger.getLogger(MongodbTokenStore.class);
//	private  Map<String,MemToken> temptokens = new HashMap<String,MemToken>();
//	private  Map<String,MemToken> dualtokens = new HashMap<String,MemToken>();
	private final Object checkLock = new Object();
	private final Object dualcheckLock = new Object();
	private Mongo mongoClient;
	
	private DB db = null;
	private DBCollection temptokens = null;
	private DBCollection dualtokens = null;
	
	public MongodbTokenStore()
	{
		mongoClient = MongodbHelper.getMongoClient("");
		db = mongoClient.getDB( "tokendb" );		
		temptokens = db.getCollection("temptokens");
		temptokens.createIndex(new BasicDBObject("token", 1));
		dualtokens = db.getCollection("dualtokens");
		dualtokens.createIndex(new BasicDBObject("appid", 1).append("statictoken", 1));
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
		List<BasicDBObject> dolds = new ArrayList<BasicDBObject>();
		synchronized(this.checkLock)
		{
			DBCursor cursor = this.temptokens.find();
			
			while(cursor.hasNext())
			{	
				DBObject tt = cursor.next();
				MemToken token_m = totempToken(tt);				
				if(isold(token_m))
				{
					dolds.add(new BasicDBObject("token", token_m.getToken()));
				}
			}
		}
		
		for(int i = 0; i < dolds.size(); i ++)
		{
//			if(tokenMonitor.isKilldown())
//				break;
			temptokens.remove(dolds.get(i));
		}
		
		dolds = new ArrayList<BasicDBObject>();
		synchronized(this.dualcheckLock)
		{
			DBCursor cursor = this.dualtokens.find();
			while(cursor.hasNext())
			{	
				DBObject tt = cursor.next();
//				MemToken token_m = new MemToken((String)tt.get("token"),(Long)tt.get("createTime"));		
				MemToken token_m = todualToken(tt);
				if(isold(token_m,token_m.getLivetime(),System.currentTimeMillis()))
				{
					dolds.add(new BasicDBObject("appid", (String)tt.get("appid")).append("statictoken", (String)tt.get("statictoken")));
				}
			}
		}
		
		for(int i = 0; i < dolds.size(); i ++)
		{
			dualtokens.remove(dolds.get(i));
		}
		
		dolds = null;
		
	}
	
	private MemToken todualToken(DBObject object)
	{
		MemToken token = new MemToken((String)object.get("token"),(Long)object.get("createTime"), (Boolean)object.get("validate"),
					(Long)object.get("lastVistTime"), (Long)object.get("livetime"));
		token.setAppid((String)object.get("appid"));
		token.setStatictoken((String)object.get("statictoken"));
		return token;
	}
	
	private MemToken totempToken(DBObject object)
	{
		MemToken token = new MemToken((String)object.get("token"),(Long)object.get("createTime"));
		return token;
	}
	
	
	
	public Integer existToken(String token)
	{
		
		if(token != null)
		{
//			synchronized(checkLock)
			BasicDBObject dbobject = new BasicDBObject("token", token);
			DBCursor cursor = temptokens.find(dbobject);
			if(cursor.hasNext())
			{
				DBObject tt = cursor.next();
				temptokens.remove(dbobject);
				MemToken token_m = totempToken(tt);					
				if(!this.isold(token_m))
				{
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
		else 
		{
			return MemTokenManager.temptoken_request_validateresult_nodtoken;
		}
	}

	private MemToken getDualToken(String appid, String statictoken)
	{
		return getDualToken(appid, statictoken,-1);
	}
	private MemToken getDualToken(String appid, String statictoken,long lastVistTime)
	{
		BasicDBObject dbobject = new BasicDBObject("appid", appid).append("statictoken", statictoken);
		MemToken tt = null;
		DBCursor cursor = null;
		DBObject dt = null;
		if(lastVistTime > 0)
		{
			
			dt = dualtokens.findAndModify(dbobject, new BasicDBObject("lastVistTime", lastVistTime));
			tt = todualToken(dt); 
		}
		else
		{
			cursor = dualtokens.find(dbobject);
			if(cursor.hasNext())
			{
				dt = cursor.next();
				tt = todualToken(dt); 
			}
		}
		
		
		return tt;
	}
	@Override
	public Integer existToken(String appid, String statictoken,
			String dynamictoken) {
		
		if(dynamictoken != null)
		{
			
			{
				long lastVistTime = System.currentTimeMillis();
				MemToken tt = getDualToken( appid, statictoken,lastVistTime);
				if(tt != null )//is first request,and clear temp token to against Cross Site Request Forgery
				{
					
					if(!this.isold(tt,tt.getLivetime(),lastVistTime))
					{
						tt.setLastVistTime(lastVistTime);
//					temptokens.remove(token);				
						if(tt.getToken().equals(dynamictoken))
							return MemTokenManager.temptoken_request_validateresult_ok;
						else
							return MemTokenManager.temptoken_request_validateresult_fail;
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

	private void insertDualToken(MemToken dualToken)
	{
		this.dualtokens.insert(new BasicDBObject("token",dualToken.getToken())
		.append("createTime", dualToken.getCreateTime())
		.append("lastVistTime", dualToken.getLastVistTime())
		.append("livetime", dualToken.getLivetime())
		.append("appid", dualToken.getAppid())
		.append("statictoken", dualToken.getStatictoken())
		.append("validate", dualToken.isValidate()));
	}
	
	private void updateDualToken(MemToken dualToken)
	{
		this.dualtokens.update(new BasicDBObject(
		"appid", dualToken.getAppid())
		.append("statictoken", dualToken.getStatictoken()),
		new BasicDBObject("validate", dualToken.isValidate()).append("lastVistTime", dualToken.getLastVistTime()).append("token", dualToken.getToken()));
	}

	@Override
	public MemToken genToken() {
		String token = this.randomToken();
		MemToken token_m = new MemToken(token,System.currentTimeMillis());
		temptokens.insert(new BasicDBObject("token",token_m.getToken()).append("createTime", token_m.getCreateTime()));
		return token_m;
		
	}

	@Override
	public MemToken genToken(String appid, String statictoken, long livetime) {
		String token = this.randomToken();
		
		MemToken token_m = null;
//		synchronized(this.dualcheckLock)
		{
			token_m = getDualToken( appid, statictoken);
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
					token_m.setStatictoken(statictoken);
					updateDualToken(token_m);
				}
			}
			else
			{
				long createTime = System.currentTimeMillis();
				token_m = new MemToken(token, createTime, true,
						createTime, livetime);
				token_m.setAppid(appid);
				token_m.setStatictoken(statictoken);
				this.insertDualToken(token_m);
			}
		}
		return token_m ;
		
	}

}
