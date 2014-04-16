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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updateLastAccessedTime(String appKey,String sessionID, long lastAccessedTime) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public long getLastAccessedTime(String appKey,String sessionID) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String[] getValueNames(String appKey,String sessionID) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void invalidate(String appKey,String sessionID) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isNew(String appKey,String sessionID) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void removeAttribute(String appKey,String sessionID, String attribute) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addAttribute(String appKey,String sessionID, String attribute, Object value) {
		
		
	}


}

