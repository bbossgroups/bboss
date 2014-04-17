package org.frameworkset.web.token;

import java.sql.SQLException;

import javax.transaction.RollbackException;

import org.apache.log4j.Logger;
import org.frameworkset.security.ecc.ECCCoder;
import org.frameworkset.security.ecc.ECCCoder.ECKeyPair;

import com.frameworkset.common.poolman.ConfigSQLExecutor;
import com.frameworkset.common.poolman.Record;
import com.frameworkset.common.poolman.handle.RowHandler;
import com.frameworkset.orm.transaction.TransactionException;
import com.frameworkset.orm.transaction.TransactionManager;


public class DBTokenStore extends BaseTokenStore {
	private ConfigSQLExecutor executor;

	private static Logger log = Logger.getLogger(DBTokenStore.class);

	
	
	
	public DBTokenStore()
	{
//		mongoClient = MongoDBHelper.getMongoClient(MongoDBHelper.defaultMongoDB);
//		db = mongoClient.getDB( "tokendb" );
//		authtemptokens = db.getCollection("authtemptokens");
//		authtemptokens.createIndex(new BasicDBObject("appid", 1).append("secret", 1).append("token", 1));
//		temptokens = db.getCollection("temptokens");
//		temptokens.createIndex(new BasicDBObject("token", 1));
//		dualtokens = db.getCollection("dualtokens");
//		dualtokens.createIndex(new BasicDBObject("appid", 1).append("secret", 1));
//		eckeypairs = db.getCollection("eckeypair");
//		eckeypairs.createIndex(new BasicDBObject("appid", 1));
		executor = new ConfigSQLExecutor("org/frameworkset/web/token/apptoken.xml");
	}
	public void destory()
	{
		executor = null;
		
	}
	
	public void livecheck()
	{
		
		long curtime = System.currentTimeMillis();
//		synchronized(this.checkLock)
		
		try
		{
			
			this.executor.delete("deleteExpiredTempToken", curtime);
			
		} catch (Exception e) {
			log.debug("deleteExpiredTempToken",e);
		}
		finally
		{
			
		}
		
		
		
		try
		{
			this.executor.delete("deleteExpiredAuthTempToken", curtime);
		} catch (Exception e) {
			log.debug("deleteExpiredAuthTempToken",e);
		}
		finally
		{
			
		}
		
		
		try
		{
			this.executor.delete("deleteExpiredAuthdualToken", curtime);
		} catch (Exception e) {
			log.debug("deleteExpiredAuthdualToken",e);
		}
		finally
		{
			
		}
		
		
	}
	
//	private MemToken todualToken(DBObject object)
//	{
//		if(object == null)
//			return null;
//		MemToken token = new MemToken((String)object.get("token"),(Long)object.get("createTime"), (Boolean)object.get("validate"),
//					(Long)object.get("lastVistTime"), (Long)object.get("livetime"));
//		token.setAppid((String)object.get("appid"));
//		token.setSecret((String)object.get("secret"));
//		return token;
//	}
	
//	private MemToken totempToken(DBObject object)
//	{
//		MemToken token = new MemToken((String)object.get("token"),(Long)object.get("createTime"));
//		token.setLivetime((Long)object.get("livetime"));
//		return token;
//	}
	
	
	public Integer checkAuthTempToken(TokenResult tokeninfo)
	{
		
		if(tokeninfo != null)
		{
//			MemToken token = new MemToken((String)object.get("token"),(Long)object.get("createTime"));
//			token.setLivetime((Long)object.get("livetime"));
			
//			synchronized(checkLock)
//			DBCursor cursor = null;
//			BasicDBObject dbobject = new BasicDBObject("token", tokeninfo.getToken()).append("appid", tokeninfo.getAppid()).append("secret", tokeninfo.getSecret());
			TransactionManager tm = new TransactionManager();
			try {
				tm.begin();
				MemToken token_m = this.executor.queryObjectByRowHandler(new RowHandler<MemToken>(){

					@Override
					public void handleRow(MemToken rowValue, Record record)
							throws Exception {
						rowValue.setToken(record.getString("token"));
						rowValue.setCreateTime(record.getLong("createTime"));
						rowValue.setLivetime(record.getLong("livetime"));
					}
					
				}, MemToken.class, "getAuthTempToken", tokeninfo.getToken(),tokeninfo.getAppid());
				executor.delete("deleteAuthTempToken", tokeninfo.getToken(),tokeninfo.getAppid());
//				DBObject tt = this.authtemptokens.findAndRemove(dbobject);
				tm.commit();
				if(token_m != null)
				{
//					DBObject tt = cursor.next();
	//				authtemptokens.remove(dbobject);
//					MemToken token_m = totempToken(tt);					
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
			} catch (Exception e) {
				throw new TokenException(e);
			}
			finally
			{
				tm.release();
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
			TransactionManager tm = new TransactionManager();
			try {
				tm.begin();
				MemToken token_m = this.executor.queryObjectByRowHandler(new RowHandler<MemToken>(){

					@Override
					public void handleRow(MemToken rowValue, Record record)
							throws Exception {
						rowValue.setToken(record.getString("token"));
						rowValue.setCreateTime(record.getLong("createTime"));
						rowValue.setLivetime(record.getLong("livetime"));
					}
					
				}, MemToken.class, "getTempToken", tokeninfo.getToken());
				executor.delete("deleteTempToken", tokeninfo.getToken());
				tm.commit();
				if(token_m != null)
				{
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
					
				
			} catch (Exception e) {
				throw new TokenException(e);
			}
			finally
			{
				tm.release();
			}
////			synchronized(checkLock)
//			BasicDBObject dbobject = new BasicDBObject("token", tokeninfo.getToken());
//			DBObject tt = temptokens.findAndRemove(dbobject);
//			if(tt != null)
//			{
////				DBObject tt = cursor.next();
//				MemToken token_m = totempToken(tt);					
//				if(!this.isold(token_m))
//				{
//					return TokenStore.temptoken_request_validateresult_ok;
//				}
//				else
//				{
//					return TokenStore.temptoken_request_validateresult_expired;
//				}
//				
//			}
//			else
//			{
//				return TokenStore.temptoken_request_validateresult_fail;
//			}			
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
		
		MemToken tt = null;
		
		if(lastVistTime > 0)
		{
			TransactionManager tm = new TransactionManager();
			try {
				tm.begin();
				this.executor.update("updateDualTokenLastVistime", lastVistTime,appid);
				tt = executor.queryObject(MemToken.class, "queryDualToken",appid);
				tm.commit();
			} catch (Exception e) {
				throw new TokenException(e);
			}
			finally
			{
				tm.release();
			}
//			dt = dualtokens.findAndModify(dbobject, new BasicDBObject("$set",new BasicDBObject("lastVistTime", lastVistTime)));
//			tt = todualToken(dt); 
		}
		else
		{
//			try
//			{
//				cursor = dualtokens.find(dbobject);
//				if(cursor.hasNext())
//				{
//					dt = cursor.next();
//					tt = todualToken(dt); 
//				}
//			}
//			finally
//			{
//				if(cursor != null)
//				{
//					cursor.close();
//				}
//			}
			try {
				tt = executor.queryObject(MemToken.class, "queryDualToken",appid);
			} catch (SQLException e) {
				throw new TokenException(e);
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

	private void insertDualToken(String sqlname,MemToken dualToken)
	{
//		dualtokens.insert(new BasicDBObject("token",dualToken.getToken())
//		.append("createTime", dualToken.getCreateTime())
//		.append("lastVistTime", dualToken.getLastVistTime())
//		.append("livetime", dualToken.getLivetime())
//		.append("appid", dualToken.getAppid())
//		.append("secret", dualToken.getSecret())
//		.append("validate", dualToken.isValidate()));
		dualToken.setId(getID());
		try {
			this.executor.insertBean(sqlname, dualToken);
		} catch (SQLException e) {
			throw new TokenException(e);
		}
	}
	
	private void updateDualToken(MemToken dualToken)
	{
		try {
			this.executor.updateBean("updateDualToken", dualToken);
		} catch (SQLException e) {
			throw new TokenException(e);
		}
//		this.dualtokens.update(new BasicDBObject(
//		"appid", dualToken.getAppid())
//		.append("secret", dualToken.getSecret()),
//		new BasicDBObject("token",dualToken.getToken())
//		.append("createTime", dualToken.getCreateTime())
//		.append("lastVistTime", dualToken.getLastVistTime())
//		.append("livetime", dualToken.getLivetime())
//		.append("appid", dualToken.getAppid())
//		.append("secret", dualToken.getSecret())
//		.append("validate", dualToken.isValidate()));
	}
	
	private String getID()
	{
		return java.util.UUID.randomUUID().toString();
	}

	@Override
	public MemToken genTempToken() {
		String token = this.randomToken();
		MemToken token_m = new MemToken(token,System.currentTimeMillis());
		try {
			this.executor.insert("genTempToken", getID(),token_m.getToken(),token_m.getCreateTime(),this.tempTokendualtime,"1");
		} catch (SQLException e) {
			throw new TokenException(e);
		}
//		temptokens.insert(new BasicDBObject("token",token_m.getToken()).append("createTime", token_m.getCreateTime()).append("livetime", this.tempTokendualtime).append("validate", true));
		this.signToken(token_m, type_temptoken, null,null);
		return token_m;
		
	}

	@Override
	public MemToken genDualToken(String appid,String ticket, String secret, long livetime) {
		
		String[] accountinfo = decodeTicket( ticket,
				 appid,  secret);
		MemToken token_m = null;
		TransactionManager tm = new TransactionManager();
		try
		{
			tm.begin();
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
				this.insertDualToken("insertDualToken",token_m);
			}
			tm.commit();
		} catch (RollbackException e) {
			throw new TokenException(e);
		} catch (TransactionException e) {
			throw new TokenException(e);
		}
		finally
		{
			tm.release();
		}
		this.signToken(token_m,TokenStore.type_dualtoken,accountinfo,ticket);
		return token_m ;
		
	}
	/**
	 * 创建带认证的临时令牌
	 * @param string
	 * @param string2
	 * @return
	 */
	public MemToken genAuthTempToken(String appid,String ticket, String secret) {
		String[] accountinfo = decodeTicket( ticket,
				 appid,  secret);
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
				this.insertDualToken("genAuthTempToken",token_m);
			}
		}
		this.signToken(token_m,TokenStore.type_authtemptoken,accountinfo,ticket);
		return token_m ;
	}
	
	public ECKeyPair getKeyPair(String appid,String secret) throws TokenException
	{
		
		
		try {
			ECKeyPair ECKeyPair =this.executor.queryObjectByRowHandler(new RowHandler<ECKeyPair>(){

				@Override
				public void handleRow(ECKeyPair rowValue, Record record)
						throws Exception {
					rowValue.setPrivateKey(record.getString("privateKey"));
					rowValue.setPublicKey(record.getString("publicKey"));
//					ECKeyPair ECKeyPair = new ECKeyPair((String)value.get("privateKey"),(String)value.get("publicKey"),null,null);
					
				}
				
			},ECKeyPair.class, "getKeyPairs", appid);
//			cursor = eckeypairs.find(new BasicDBObject("appid", appid));
			if(ECKeyPair != null)
			{
//				DBObject value = cursor.next();
//				return toECKeyPair(value);
				return ECKeyPair;
				
			}
			else
			{
				ECKeyPair keypair = ECCCoder.genECKeyPair();
				insertECKeyPair( appid, secret, keypair);
				return keypair;
			}
		} catch (SQLException e) {
			throw new TokenException(e);
		} catch (TokenException e) {
			throw new TokenException(e);
		}
		catch (Exception e) {
			throw new TokenException(e);
		}
		
	}
	private void insertECKeyPair(String appid,String secret,ECKeyPair keypair)
	{
//		this.eckeypairs.insert(new BasicDBObject("appid",appid)		
//		.append("privateKey", keypair.getPrivateKey())
//		.append("createTime", System.currentTimeMillis())
//		.append("publicKey", keypair.getPublicKey()) );
		try {
			this.executor.insert("insertECKeyPair", appid, keypair.getPrivateKey(),System.currentTimeMillis(),keypair.getPublicKey());
		} catch (SQLException e) {
			throw new TokenException(e);
		}
	}

}
