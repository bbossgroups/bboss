package org.frameworkset.security.session.impl;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.frameworkset.nosql.mongodb.MongoDBHelper;
import org.frameworkset.security.session.Session;

import com.frameworkset.util.SimpleStringUtil;
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
				 .append(" if(!this._validate) return true;")
				 .append(" if(this.maxInactiveInterval <= 0) return false;")
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
			if(app.endsWith("_sessions"))
			{
				DBCollection appsessions = db.getCollection(app);
				appsessions.remove(new BasicDBObject("$where",temp));
			}
		}
		
	}

	
	private DBCollection getAppSessionDBCollection(String appKey)
	{
		 DBCollection sessions = db.getCollection(MongoDBHelper.getAppSessionTableName( appKey));
		 sessions.ensureIndex("sessionid");
		 return sessions;
	}
	
	@Override
	public Session createSession(String appKey,String referip) {
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
		.append("appKey", appKey).append("referip", referip).append("host", SimpleStringUtil.getHostIP()));
		SimpleSessionImpl session = new SimpleSessionImpl();
		
		session.setMaxInactiveInterval(maxInactiveInterval);
		session.setAppKey(appKey);
		session.setCreationTime(creationTime);
		session.setLastAccessedTime(lastAccessedTime);
		session.setId(sessionid);
		session.setHost(SimpleStringUtil.getHostIP());
		session.setValidate(true);
//		session._setSessionStore(this);
		
		return session;
	}
	
	
	

	@Override
	public Object getAttribute(String appKey,String sessionID, String attribute) {
		DBCollection sessions =getAppSessionDBCollection( appKey);
		BasicDBObject keys = new BasicDBObject();
		attribute = MongoDBHelper.converterSpecialChar( attribute);
		keys.put(attribute, 1);
		
		DBObject obj = sessions.findOne(new BasicDBObject("sessionid",sessionID).append("_validate", true),keys);
		if(obj == null)
			return null;
		return SessionHelper.unserial((String)obj.get(attribute));
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
		
		sessions.update(new BasicDBObject("sessionid",sessionID).append("_validate", true), new BasicDBObject("$set",new BasicDBObject("lastAccessedTime", lastAccessedTime)));
		
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
	public Session invalidate(String appKey,String sessionID) {
//		DBCollection sessions = getAppSessionDBCollection( appKey);		
//		sessions.update(new BasicDBObject("sessionid",sessionID), new BasicDBObject("$set",new BasicDBObject("_validate", false)));
		Session session = _getSession(appKey, sessionID);
		return session;
		
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
	public Object removeAttribute(String appKey,String sessionID, String attribute) {
		DBCollection sessions = getAppSessionDBCollection( appKey);
		List<String> list = new ArrayList<String>();
//		attribute = converterSpecialChar( attribute);
		list.add(attribute);
		Session value = getSession(appKey, sessionID,list);
		sessions.update(new BasicDBObject("sessionid",sessionID), new BasicDBObject("$set",new BasicDBObject(list.get(0), null)));
		return value;
		
	}

	@Override
	public Object addAttribute(String appKey,String sessionID, String attribute, Object value) {
		attribute = MongoDBHelper.converterSpecialChar( attribute);
		DBCollection sessions = getAppSessionDBCollection( appKey);	
		Session session = getSession(appKey, sessionID);
		sessions.update(new BasicDBObject("sessionid",sessionID), new BasicDBObject("$set",new BasicDBObject(attribute, value)));
		
		return session;
		
	}
	public Session getSession(String appKey, String sessionid,List<String> attributeNames) {
		DBCollection sessions =getAppSessionDBCollection( appKey);
		BasicDBObject keys = new BasicDBObject();
		keys.put("creationTime", 1);
		keys.put("maxInactiveInterval", 1);
		keys.put("lastAccessedTime", 1);
		keys.put("_validate", 1);
		keys.put("appKey", 1);
		keys.put("referip", 1);
		keys.put("host", 1);
		List<String> copy = new ArrayList<String>(attributeNames);
		for(int i = 0; attributeNames != null && i < attributeNames.size(); i ++)
		{
			String r = MongoDBHelper.converterSpecialChar(attributeNames.get(i));
			attributeNames.set(i, r);
			keys.put(r, 1);
		}
		
		
		
		DBObject object = sessions.findOne(new BasicDBObject("sessionid",sessionid).append("_validate", true),keys);
		if(object != null)
		{
			SimpleSessionImpl session = new SimpleSessionImpl();
			session.setMaxInactiveInterval((Long)object.get("maxInactiveInterval"));
			session.setAppKey(appKey);
			session.setCreationTime((Long)object.get("creationTime"));
			session.setLastAccessedTime((Long)object.get("lastAccessedTime"));
			session.setId(sessionid);
			session.setReferip((String)object.get("referip"));
			session.setValidate((Boolean)object.get("_validate"));
			session.setHost((String)object.get("host"));
//			session._setSessionStore(this);
			Map<String,Object> attributes = new HashMap<String,Object>();
			for(int i = 0; attributeNames != null && i < attributeNames.size(); i ++)
			{
				String name = attributeNames.get(i);
				Object value = object.get(name);
				try {
					attributes.put(copy.get(i), SessionHelper.unserial((String)value));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			session.setAttributes(attributes);
			return session;
		}
		else
		{
			return null;
		}
	}
	@Override
	public Session getSession(String appKey, String sessionid) {
		DBCollection sessions =getAppSessionDBCollection( appKey);
		BasicDBObject keys = new BasicDBObject();
		keys.put("creationTime", 1);
		keys.put("maxInactiveInterval", 1);
		keys.put("lastAccessedTime", 1);
		keys.put("_validate", 1);
		keys.put("appKey", 1);
		keys.put("referip", 1);
		keys.put("host", 1);
		
		DBObject object = sessions.findOne(new BasicDBObject("sessionid",sessionid).append("_validate", true),keys);
		if(object != null)
		{
			SimpleSessionImpl session = new SimpleSessionImpl();
			session.setMaxInactiveInterval((Long)object.get("maxInactiveInterval"));
			session.setAppKey(appKey);
			session.setCreationTime((Long)object.get("creationTime"));
			session.setLastAccessedTime((Long)object.get("lastAccessedTime"));
			session.setId(sessionid);
			session.setReferip((String)object.get("referip"));
			session.setValidate((Boolean)object.get("_validate"));
			session.setHost((String)object.get("host"));
//			session._setSessionStore(this);
			return session;
		}
		else
		{
			return null;
		}
	}
	
	private Session _getSession(String appKey, String sessionid) {
		DBCollection sessions =getAppSessionDBCollection( appKey);
	
		
		
		DBObject object = sessions.findAndRemove(new BasicDBObject("sessionid",sessionid));
		if(object != null)
		{
			SimpleSessionImpl session = new SimpleSessionImpl();
			session.setMaxInactiveInterval((Long)object.get("maxInactiveInterval"));
			session.setAppKey(appKey);
			session.setCreationTime((Long)object.get("creationTime"));
			session.setLastAccessedTime((Long)object.get("lastAccessedTime"));
			session.setId(sessionid);
			session.setReferip((String)object.get("referip"));
			session.setValidate((Boolean)object.get("_validate"));
			session.setHost((String)object.get("host"));
//			session._setSessionStore(this);
			Map<String,Object> attributes = MongoDBHelper.toMap(object,true);
			session.setAttributes(attributes);
			return session;
		}
		else
		{
			return null;
		}
	}


}


