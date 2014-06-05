package org.frameworkset.security.session.statics;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.frameworkset.nosql.mongodb.MongoDBHelper;
import org.frameworkset.security.session.impl.MongDBSessionStore;

import com.mongodb.DB;
import com.mongodb.Mongo;

public class MongoSessionStaticManagerImpl implements SessionStaticManager {
	private Mongo mongoClient;
	private DB db = null;
	private static Logger log = Logger.getLogger(MongDBSessionStore.class);
	public MongoSessionStaticManagerImpl() {
		mongoClient = MongoDBHelper.getMongoClient(MongoDBHelper.defaultMongoDB);
		db = mongoClient.getDB( "sessiondb" );
	}

	@Override
	public List<SessionAPP> getSessionAPP() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SessionInfo> getSessionInfos(String appkey, long start, long end) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SessionInfo> getAllSessionInfos(Map params, long start, long end) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SessionInfo getSessionInfo(String sessionid) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void removeSessionInfo(String sessionid) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeSessionInfos(String[] sessionid) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeAppSessionInfo(String appkey) {
		// TODO Auto-generated method stub
		
	}

}
