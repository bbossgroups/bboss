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
import org.frameworkset.security.session.SessionBasicInfo;

import com.frameworkset.util.SimpleStringUtil;
import com.frameworkset.util.StringUtil;
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
//		 sessions.ensureIndex("sessionid");
		 sessions.createIndex(new BasicDBObject( "sessionid" , 1 ));
		 return sessions;
	}
	
	@Override
	public Session createSession(SessionBasicInfo sessionBasicInfo) {
		String sessionid = this.randomToken();
		long creationTime = System.currentTimeMillis();
		long maxInactiveInterval = this.getSessionTimeout();
		long lastAccessedTime = creationTime;
	
		boolean isHttpOnly = StringUtil.hasHttpOnlyMethod()?SessionHelper.getSessionManager().isHttpOnly():false;
		boolean secure = SessionHelper.getSessionManager().isSecure();
		DBCollection sessions =getAppSessionDBCollection( sessionBasicInfo.getAppKey());
		sessions.insert(new BasicDBObject("sessionid",sessionid)
		.append("creationTime", creationTime)
		.append("maxInactiveInterval",maxInactiveInterval)
		.append("lastAccessedTime", lastAccessedTime)
		.append("_validate", true)
		.append("appKey", sessionBasicInfo.getAppKey()).append("referip", sessionBasicInfo.getReferip())
		.append("host", SimpleStringUtil.getHostIP())
		.append("requesturi", sessionBasicInfo.getRequesturi())
		.append("lastAccessedUrl", sessionBasicInfo.getRequesturi())
		.append("httpOnly",isHttpOnly)
		.append("secure", secure)
		.append("lastAccessedHostIP", SimpleStringUtil.getHostIP()));
		SimpleSessionImpl session = new SimpleSessionImpl();
		session.setMaxInactiveInterval(maxInactiveInterval);
		session.setAppKey(sessionBasicInfo.getAppKey());
		session.setCreationTime(creationTime);
		session.setLastAccessedTime(lastAccessedTime);
		session.setId(sessionid);
		session.setHost(SimpleStringUtil.getHostIP());
		session.setValidate(true);
		session.setRequesturi(sessionBasicInfo.getRequesturi());
		session.setLastAccessedUrl(sessionBasicInfo.getRequesturi());
		session.setSecure(secure);
		session.setHttpOnly(isHttpOnly);
		session.setLastAccessedHostIP(SimpleStringUtil.getHostIP());
		return session;
	}
	
	
	

	@Override
	public Object getAttribute(String appKey,String contextpath,String sessionID, String attribute) {
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
	public Enumeration getAttributeNames(String appKey,String contextpath,String sessionID) {
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
//		throw new java.lang.UnsupportedOperationException();
		String[] names = getValueNames(appKey,contextpath,sessionID);
		
		return SimpleStringUtil.arryToenum(names);
		
	}

	@Override
	public void updateLastAccessedTime(String appKey,String sessionID, long lastAccessedTime,String lastAccessedUrl) {
		DBCollection sessions =getAppSessionDBCollection( appKey);
		
		sessions.update(new BasicDBObject("sessionid",sessionID).append("_validate", true), new BasicDBObject("$set",new BasicDBObject("lastAccessedTime", lastAccessedTime).append("lastAccessedUrl", lastAccessedUrl).append("lastAccessedHostIP", SimpleStringUtil.getHostIP())));
		
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
	public String[] getValueNames(String appKey,String contextpath,String sessionID) {
		
		DBCollection sessions =getAppSessionDBCollection( appKey);
		
		DBObject obj = sessions.findOne(new BasicDBObject("sessionid",sessionID));
		
		if(obj == null)
			throw new SessionException("SessionID["+sessionID+"],appKey["+appKey+"] do not exist or is invalidated!" );
		String[] valueNames = null;
		if(obj.keySet() != null)
		{
//			valueNames = new String[obj.keySet().size()];
			List<String> temp = new ArrayList<String>();
			Iterator<String> keys = obj.keySet().iterator();
			while(keys.hasNext())
			{
				String tempstr = keys.next();
				if(!MongoDBHelper.filter(tempstr))
				{
					tempstr = SessionHelper.dewraperAttributeName(appKey, contextpath, tempstr);
					if(tempstr != null)
					{
						temp.add(tempstr);
					}
				}
			}
			valueNames = new String[temp.size()];
			valueNames = temp.toArray(valueNames);
			
		}
		return valueNames ;
	}

	@Override
	public Session invalidate(String appKey,String contextpath,String sessionID) {
//		DBCollection sessions = getAppSessionDBCollection( appKey);		
//		sessions.update(new BasicDBObject("sessionid",sessionID), new BasicDBObject("$set",new BasicDBObject("_validate", false)));
		Session session = getSessionAndRemove(appKey, contextpath, sessionID);
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
	public Object removeAttribute(String appKey,String contextpath,String sessionID, String attribute) {
		DBCollection sessions = getAppSessionDBCollection( appKey);
		if(SessionHelper.haveSessionListener())
		{
			List<String> list = new ArrayList<String>();
	//		attribute = converterSpecialChar( attribute);
			list.add(attribute);
			Session value = getSession(appKey, contextpath, sessionID,list);
			sessions.update(new BasicDBObject("sessionid",sessionID), new BasicDBObject("$unset",new BasicDBObject(list.get(0), 1)));
			return value;
		}
		else
		{
			attribute = MongoDBHelper.converterSpecialChar(attribute);
			sessions.update(new BasicDBObject("sessionid",sessionID), new BasicDBObject("$unset",new BasicDBObject(attribute, 1)));
			//sessions.update(new BasicDBObject("sessionid",sessionID), new BasicDBObject("$set",new BasicDBObject(attribute, null)));
			return null;
		}
		
	}

	@Override
	public Object addAttribute(String appKey,String contextpath,String sessionID, String attribute, Object value) {
		attribute = MongoDBHelper.converterSpecialChar( attribute);
		DBCollection sessions = getAppSessionDBCollection( appKey);	
		Session session = getSession(appKey,contextpath, sessionID);
		sessions.update(new BasicDBObject("sessionid",sessionID), new BasicDBObject("$set",new BasicDBObject(attribute, value)));
		
		return session;
		
	}
	private Session getSession(String appKey,String contextpath, String sessionid,List<String> attributeNames) {
		DBCollection sessions =getAppSessionDBCollection( appKey);
		BasicDBObject keys = new BasicDBObject();
		keys.put("creationTime", 1);
		keys.put("maxInactiveInterval", 1);
		keys.put("lastAccessedTime", 1);
		keys.put("_validate", 1);
		keys.put("appKey", 1);
		keys.put("referip", 1);
		keys.put("host", 1);
		keys.put("requesturi",1);
		keys.put("lastAccessedUrl", 1);
		keys.put("secure",1);
		keys.put("httpOnly", 1);
		keys.put("lastAccessedHostIP", 1);
//		.append("lastAccessedHostIP", SimpleStringUtil.getHostIP())
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
			session.setRequesturi((String)object.get("requesturi"));
			session.setLastAccessedUrl((String)object.get("lastAccessedUrl"));
			session.setLastAccessedHostIP((String)object.get("lastAccessedHostIP"));
			Object secure_ = object.get("secure");
			if(secure_ != null)
			{
				session.setSecure((Boolean)secure_);
			}
			Object httpOnly_ = object.get("httpOnly");
			if(httpOnly_ != null)
			{
				session.setHttpOnly((Boolean)httpOnly_);
			}
			else
			{
				session.setHttpOnly(StringUtil.hasHttpOnlyMethod()?SessionHelper.getSessionManager().isHttpOnly():false);
			}
			Map<String,Object> attributes = new HashMap<String,Object>();
			for(int i = 0; attributeNames != null && i < attributeNames.size(); i ++)
			{
				String name = attributeNames.get(i);
				Object value = object.get(name);
				try {
					String temp = SessionHelper.dewraperAttributeName(appKey, contextpath, copy.get(i));		
					if(temp != null)
						attributes.put(temp, SessionHelper.unserial((String)value));
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
	public Session getSession(String appKey,String contextpath, String sessionid) {
		DBCollection sessions =getAppSessionDBCollection( appKey);
		BasicDBObject keys = new BasicDBObject();
		keys.put("creationTime", 1);
		keys.put("maxInactiveInterval", 1);
		keys.put("lastAccessedTime", 1);
		keys.put("_validate", 1);
		keys.put("appKey", 1);
		keys.put("referip", 1);
		keys.put("host", 1);
		keys.put("requesturi", 1);
		keys.put("lastAccessedUrl", 1);
		keys.put("secure",1);
		keys.put("httpOnly", 1);
		keys.put("lastAccessedHostIP", 1);
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
			session.setRequesturi((String)object.get("requesturi"));
			session.setLastAccessedUrl((String)object.get("lastAccessedUrl"));
			Object secure_ = object.get("secure");
			if(secure_ != null)
			{
				session.setSecure((Boolean)secure_);
			}
			Object httpOnly_ = object.get("httpOnly");
			if(httpOnly_ != null)
			{
				session.setHttpOnly((Boolean)httpOnly_);
			}
			else
			{
				session.setHttpOnly(StringUtil.hasHttpOnlyMethod()?SessionHelper.getSessionManager().isHttpOnly():false);
			}
			session.setLastAccessedHostIP((String)object.get("lastAccessedHostIP"));
			return session;
		}
		else
		{
			return null;
		}
	}
	
	private Session getSessionAndRemove(String appKey,String contextpath, String sessionid) {
		DBCollection sessions =getAppSessionDBCollection( appKey);
	
		
		if(SessionHelper.haveSessionListener())
		{
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
				session.setRequesturi((String)object.get("requesturi"));
				session.setLastAccessedUrl((String)object.get("lastAccessedUrl"));
				session.setLastAccessedHostIP((String)object.get("lastAccessedHostIP"));
				Object secure_ = object.get("secure");
				if(secure_ != null)
				{
					session.setSecure((Boolean)secure_);
				}
				Object httpOnly_ = object.get("httpOnly");
				if(httpOnly_ != null)
				{
					session.setHttpOnly((Boolean)httpOnly_);
				}	
				else
				{
					session.setHttpOnly(StringUtil.hasHttpOnlyMethod()?SessionHelper.getSessionManager().isHttpOnly():false);
				}
	//			session._setSessionStore(this);
				Map<String,Object> attributes = MongoDBHelper.toMap(appKey, contextpath,object,true);
				session.setAttributes(attributes);
				return session;
			}
			else
			{
//				sessions.remove(new BasicDBObject("sessionid",sessionid));
				return null;
			}
		}
		else
		{
			sessions.remove(new BasicDBObject("sessionid",sessionid));
			return null;
		}
	}


}


