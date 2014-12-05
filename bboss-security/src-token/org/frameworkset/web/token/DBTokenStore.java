package org.frameworkset.web.token;

import java.sql.SQLException;

import org.apache.log4j.Logger;
import org.frameworkset.security.ecc.SimpleKeyPair;

import com.frameworkset.common.poolman.ConfigSQLExecutor;
import com.frameworkset.common.poolman.Record;
import com.frameworkset.common.poolman.handle.RowHandler;
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
			log.debug(TokenStore.ERROR_CODE_DELETEEXPIREDTEMPTOKENFAILED,e);
		}
		finally
		{
			
		}
		
		
		
		try
		{
			this.executor.delete("deleteExpiredAuthTempToken", curtime);
		} catch (Exception e) {
			log.debug(TokenStore.ERROR_CODE_DELETEEXPIREDAUTHTEMPTOKENFAILED,e);
		}
		finally
		{
			
		}
		
		
		try
		{
			this.executor.delete("deleteExpiredAuthdualToken", curtime);
		} catch (Exception e) {
			log.debug(TokenStore.ERROR_CODE_DELETEEXPIREDAUTHDUALTOKENFAILED,e);
		}
		finally
		{
			
		}
		
		try
		{
			this.executor.delete("deleteExpiredAuthdualToken", curtime);
		} catch (Exception e) {
			log.debug(TokenStore.ERROR_CODE_DELETEEXPIREDAUTHDUALTOKENFAILED,e);
		}
		finally
		{
			
		}
		
		try
		{
			this.executor.delete("deleteExpiredTicket", curtime);
		} catch (Exception e) {
			log.debug(TokenStore.ERROR_CODE_DELETEEXPIREDAUTHDUALTOKENFAILED,e);
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
	protected MemToken getAuthTempMemToken(String token,String appid)
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
					rowValue.setSigntoken(record.getString("signtoken"));
				}
				
			}, MemToken.class, "getAuthTempToken", token,appid);
			if(token_m != null)
				executor.delete("deleteAuthTempToken", token,appid);
//			DBObject tt = this.authtemptokens.findAndRemove(dbobject);
			tm.commit();
			return token_m;
		}
		catch(Exception e)
		{
			throw new TokenException(TokenStore.ERROR_CODE_CHECKAUTHTEMPTOKENFAILED,e);
		}
	}
	
	
	protected MemToken getTempMemToken(String token,String appid)
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
				
			}, MemToken.class, "getTempToken", token);
			if(token_m != null)
				executor.delete("deleteTempToken", token);
			tm.commit();
			return token_m;
		}catch (Exception e) {
			throw new TokenException(TokenStore.ERROR_CODE_CHECKTEMPTOKENFAILED,e);
		}
		finally
		{
			tm.release();
		}
		
	}


	private MemToken queryDualToken(String appid, String secret) throws TokenException
	{
		return queryDualToken(appid, secret,-1);
	}
	private MemToken queryDualToken(String appid, String secret,long lastVistTime) throws TokenException
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
				throw new TokenException(TokenStore.ERROR_CODE_QUERYDUALTOKENFAILED,e);
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
				throw new TokenException(TokenStore.ERROR_CODE_QUERYDUALTOKENFAILED,e);
			}
		}
		
		
		return tt;
	}
	
	protected MemToken getDualMemToken(String token,String appid,long lastVistTime )
	{
		
		MemToken tt = queryDualToken( appid, null,lastVistTime);
		return tt;
	}
	
	

	private void insertDualToken(String sqlname,MemToken dualToken) throws TokenException
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
			throw new TokenException(TokenStore.ERROR_CODE_STOREDUALTOKENFAILED,e);
		}
	}
	
	private void updateDualToken(MemToken dualToken) throws TokenException
	{
		try {
			this.executor.updateBean("updateDualToken", dualToken);
		} catch (SQLException e) {
			throw new TokenException(TokenStore.ERROR_CODE_UPDATEDUALTOKENFAILED,e);
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
	public MemToken _genTempToken() throws TokenException {
		String token = this.randomToken();
		MemToken token_m = new MemToken(token,System.currentTimeMillis());
		try {
			this.signToken(token_m, type_temptoken, null,null);
			this.executor.insert("genTempToken", getID(),token_m.getToken(),token_m.getCreateTime(),this.tempTokendualtime,"1");
		} catch (Exception e) {
			throw new TokenException(TokenStore.ERROR_CODE_GENTEMPTOKENFAILED,e);
		}
//		temptokens.insert(new BasicDBObject("token",token_m.getToken()).append("createTime", token_m.getCreateTime()).append("livetime", this.tempTokendualtime).append("validate", true));
		
		return token_m;
		
	}

	@Override
	protected MemToken _genDualToken(String appid,String ticket, String secret, long livetime) throws TokenException {
		
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
					this.signToken(token_m,TokenStore.type_dualtoken,accountinfo,ticket);
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
				this.signToken(token_m,TokenStore.type_dualtoken,accountinfo,ticket);
				this.insertDualToken("insertDualToken",token_m);
			}
			tm.commit();
		}catch (TokenException e) {
			throw (e);
		} catch (Exception e) {
			throw new TokenException(TokenStore.ERROR_CODE_GENDUALTOKENFAILED,e);
		} 
		finally
		{
			tm.release();
		}
		
		return token_m ;
		
	}
	/**
	 * 创建带认证的临时令牌
	 * @param string
	 * @param string2
	 * @return
	 * @throws TokenException 
	 */
	protected MemToken _genAuthTempToken(String appid,String ticket, String secret) throws TokenException {
		String[] accountinfo = decodeTicket( ticket,
				 appid,  secret);
		String token = this.randomToken();//需要将appid,secret,token进行混合加密，生成最终的token进行存储，校验时，只对令牌进行拆分校验
		
		MemToken token_m = null;
//		synchronized(this.dualcheckLock)
		
		long createTime = System.currentTimeMillis();
		token_m = new MemToken(token, createTime, true,
				createTime, this.tempTokendualtime);
		token_m.setAppid(appid);
		token_m.setSecret(secret);
		this.signToken(token_m,TokenStore.type_authtemptoken,accountinfo,ticket);
		this.insertDualToken("genAuthTempToken",token_m);
		
		
		return token_m ;
	}
	
	protected SimpleKeyPair _getKeyPair(String appid,String secret) throws TokenException
	{
		
		
		try {
			SimpleKeyPair ECKeyPair =this.executor.queryObjectByRowHandler(new RowHandler<SimpleKeyPair>(){

				@Override
				public void handleRow(SimpleKeyPair rowValue, Record record)
						throws Exception {
					rowValue.setPrivateKey(record.getString("privateKey"));
					rowValue.setPublicKey(record.getString("publicKey"));
//					ECKeyPair ECKeyPair = new ECKeyPair((String)value.get("privateKey"),(String)value.get("publicKey"),null,null);
					
				}
				
			},SimpleKeyPair.class, "getKeyPairs", appid);
//			cursor = eckeypairs.find(new BasicDBObject("appid", appid));
			if(ECKeyPair != null)
			{
//				DBObject value = cursor.next();
//				return toECKeyPair(value);
				return ECKeyPair;
				
			}
			else
			{
				SimpleKeyPair keypair = ECCCoder.genECKeyPair();
				insertECKeyPair( appid, secret, keypair);
				return keypair;
			}
		}catch (TokenException e) {
			throw (e);
		} 
		catch (Exception e) {
			throw new TokenException(TokenStore.ERROR_CODE_GETKEYPAIRFAILED,e);
		}
		
	}
	private void insertECKeyPair(String appid,String secret,SimpleKeyPair keypair) throws TokenException
	{
//		this.eckeypairs.insert(new BasicDBObject("appid",appid)		
//		.append("privateKey", keypair.getPrivateKey())
//		.append("createTime", System.currentTimeMillis())
//		.append("publicKey", keypair.getPublicKey()) );
		try {
			this.executor.insert("insertECKeyPair", appid, keypair.getPrivateKey(),System.currentTimeMillis(),keypair.getPublicKey());
		} catch (SQLException e) {
			throw new TokenException(TokenStore.ERROR_CODE_STOREKEYPAIRFAILED,e);
		}
	}
	@Override
	protected void persisteTicket(Ticket ticket) throws TokenException{
		try {
			this.executor.insertBean("persisteTicket",ticket);
		} catch (SQLException e) {
			throw new TokenException(TokenStore.ERROR_CODE_PERSISTENTTICKETFAILED,e);
		}
		
	}
	protected boolean refreshTicket(String token,String appid) 
	{
		TransactionManager tm = new TransactionManager();
		try {
			tm.begin();
			
			Ticket ticket = this.executor.queryObjectByRowHandler(new RowHandler<Ticket>(){

				@Override
				public void handleRow(Ticket arg0, Record arg1)
						throws Exception {
					arg0.setLivetime(arg1.getLong("livetime"));
					arg0.setLastVistTime(arg1.getLong("lastVistTime"));
					
				}
				
			},Ticket.class,"getTicketLivetimeandLastVisttime",token);
			if(ticket != null)
			{
				long lastVistTime =  System.currentTimeMillis();
				assertExpiredTicket(ticket,appid,lastVistTime);
				this.executor.update("updateTicketlastAccessedtime", lastVistTime,token);
				return true;
			}
			tm.commit();
			return false;
		}
		catch (TokenException e) {
			throw e;
		}
		catch (Exception e) {
			throw new TokenException("refresh ticket["+token+"] of app["+appid+"] failed:",e);
		}
		finally
		{
			tm.release();
		}
	}
	protected boolean destroyTicket(String token,String appid)
	{
		try {
			this.executor.delete("destroyTicket", token);
			return true;
		} catch (Exception e) {
			throw new TokenException("destroy ticket["+token+"] of app["+appid+"] failed:",e);
		}
	}
	@Override
	protected Ticket getTicket(String token, String appid) {
		TransactionManager tm = new TransactionManager();
		try {
			tm.begin();
			
			Ticket ticket = this.executor.queryObject(Ticket.class,"getTicket",token);
			if(ticket != null)
			{
				long lastVistTime =  System.currentTimeMillis();
				assertExpiredTicket(ticket,appid,lastVistTime);
				this.executor.update("updateTicketlastAccessedtime", lastVistTime,token);
			}
			tm.commit();
			return ticket;
		}
		catch (TokenException e) {
			throw e;
		}
		catch (Exception e) {
			throw new TokenException(TokenStore.ERROR_CODE_GETTICKETFAILED,e);
		}
		finally
		{
			tm.release();
		}
	}

}
