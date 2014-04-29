package org.frameworkset.security.session.impl;

import java.util.Enumeration;
import java.util.Iterator;
import java.util.Set;

import org.apache.log4j.Logger;
import org.frameworkset.nosql.mongodb.MongoDBHelper;
import org.frameworkset.security.session.Session;
import org.frameworkset.security.session.SessionEvent;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.Mongo;

public class MongDBSessionStore extends BaseSessionStore{
	private Mongo mongoClient;
	private DB db = null;
	private static Logger log = Logger.getLogger(MongDBSessionStore.class);
	public MongDBSessionStore()
	{
		mongoClient = MongoDBHelper.getMongoClient(MongoDBHelper.defaultMongoDB);
		db = mongoClient.getDB( "sessiondb" );
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
	@Override
	public void livecheck() {
		Set<String> apps = db.getCollectionNames();
		if(apps == null || apps.size() == 0)
			return;
		long curtime = System.currentTimeMillis();
		StringBuffer wherefun = new StringBuffer();
		wherefun.append("function() ")
				.append("{")			
			    .append(" if(this.lastAccessedTime + this.maxInactiveInterval < ").append(curtime).append(")")
			    .append("{")
				.append("return true;")				
				.append("}")
				.append("else")
				.append(" {")
				.append(" return false;")		
				.append("}")
				.append("}");
		String temp = wherefun.toString();
		Iterator<String> itr = apps.iterator();
		while(itr.hasNext())
		{
			String app = itr.next();
			DBCollection appsessions = db.getCollection(app);
			appsessions.remove(new BasicDBObject("$where",temp));
		}
		
	}

	private String getAppSessionTableName(String appKey)
	{
		return appKey+"_sessions";
	}
	private DBCollection getAppSessionDBCollection(String appKey)
	{
		 DBCollection sessions = db.getCollection(getAppSessionTableName( appKey));
		 sessions.ensureIndex("sessionid");
		 return sessions;
	}
	
	@Override
	public Session createSession(String appKey) {
		String sessionid = this.randomToken();
		long creationTime = System.currentTimeMillis();
		long maxInactiveInterval = this.getSessionTimeout();
		long lastAccessedTime = creationTime;
		DBCollection sessions =getAppSessionDBCollection( appKey);
		sessions.insert(new BasicDBObject("sessionid",sessionid)
		.append("creationTime", creationTime)
		.append("maxInactiveInterval",maxInactiveInterval)
		.append("lastAccessedTime", lastAccessedTime)
		.append("_validate", true)
		.append("appKey", appKey));
		SimpleSessionImpl session = new SimpleSessionImpl();
		session.setMaxInactiveInterval(maxInactiveInterval);
		session.setAppKey(appKey);
		session.setCreationTime(creationTime);
		session.setLastAccessedTime(lastAccessedTime);
		session.setId(sessionid);
		session._setSessionStore(this);
		this.sessionManager.dispatchEvent(new SessionEventImpl(session,SessionEvent.EventType_create));
		return session;
	}

	@Override
	public Object getAttribute(String appKey,String sessionID, String attribute) {
		DBCollection sessions =getAppSessionDBCollection( appKey);
		BasicDBObject keys = new BasicDBObject();
		keys.put(attribute, 1);
		
		DBObject obj = sessions.findOne(new BasicDBObject("sessionid",sessionID),keys);
		return obj.get(attribute);
//		return null;
	}

	@Override
	public Enumeration getAttributeNames(String appKey,String sessionID) {
//		DBCollection sessions =getAppSessionDBCollection( appKey);
//		
//		DBObject obj = sessions.findOne(new BasicDBObject("sessionid",sessionID));
//		
//		if(obj == null)
//			throw new SessionException("SessionID["+sessionID+"],appKey["+appKey+"] do not exist or is invalidated!" );
//		String[] valueNames = null;
//		if(obj.keySet() != null)
//		{
//			return obj.keySet().iterator();
//		}
		throw new java.lang.UnsupportedOperationException();
	}

	@Override
	public void updateLastAccessedTime(String appKey,String sessionID, long lastAccessedTime) {
		DBCollection sessions =getAppSessionDBCollection( appKey);
		
		sessions.update(new BasicDBObject("sessionid",sessionID), new BasicDBObject("$set",new BasicDBObject("lastAccessedTime", lastAccessedTime)));
		
	}

	@Override
	public long getLastAccessedTime(String appKey,String sessionID) {
		DBCollection sessions =getAppSessionDBCollection( appKey);
		BasicDBObject keys = new BasicDBObject();
		keys.put("lastVistTime", 1);
		
		DBObject obj = sessions.findOne(new BasicDBObject("sessionid",sessionID),keys);
		if(obj == null)
			throw new SessionException("SessionID["+sessionID+"],appKey["+appKey+"] do not exist or is invalidated!" );
		return (Long)obj.get("lastAccessedTime");
	}

	@Override
	public String[] getValueNames(String appKey,String sessionID) {
		DBCollection sessions =getAppSessionDBCollection( appKey);
		
		DBObject obj = sessions.findOne(new BasicDBObject("sessionid",sessionID));
		
		if(obj == null)
			throw new SessionException("SessionID["+sessionID+"],appKey["+appKey+"] do not exist or is invalidated!" );
		String[] valueNames = null;
		if(obj.keySet() != null)
		{
			valueNames = new String[obj.keySet().size()];
			Iterator<String> keys = obj.keySet().iterator();
			int i = 0; 
			while(keys.hasNext())
			{
				valueNames[i] = keys.next();
				i ++;
			}
			
		}
		return valueNames ;
	}

	@Override
	public void invalidate(String appKey,String sessionID) {
		DBCollection sessions = getAppSessionDBCollection( appKey);		
		sessions.update(new BasicDBObject("sessionid",sessionID), new BasicDBObject("$set",new BasicDBObject("_validate", false)));
		
	}

	@Override
	public boolean isNew(String appKey,String sessionID) {
		DBCollection sessions =getAppSessionDBCollection( appKey);
		BasicDBObject keys = new BasicDBObject();
		keys.put("lastAccessedTime", 1);
		keys.put("creationTime", 1);
		DBObject obj = sessions.findOne(new BasicDBObject("sessionid",sessionID),keys);
		
		if(obj == null)
			throw new SessionException("SessionID["+sessionID+"],appKey["+appKey+"] do not exist or is invalidated!" );
		 long lastAccessedTime =(Long)obj.get("lastAccessedTime");
		 long creationTime =(Long)obj.get("creationTime");
		 return creationTime == lastAccessedTime;
	}

	@Override
	public void removeAttribute(String appKey,String sessionID, String attribute) {
		DBCollection sessions = getAppSessionDBCollection( appKey);		
		sessions.update(new BasicDBObject("sessionid",sessionID), new BasicDBObject("$set",new BasicDBObject(attribute, null)));
		
	}

	@Override
	public void addAttribute(String appKey,String sessionID, String attribute, Object value) {
		DBCollection sessions = getAppSessionDBCollection( appKey);		
		sessions.update(new BasicDBObject("sessionid",sessionID), new BasicDBObject("$set",new BasicDBObject(attribute, value)));
		
	}


}


