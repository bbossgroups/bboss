package org.frameworkset.nosql.mongodb;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.frameworkset.security.session.impl.SessionHelper;
import org.frameworkset.spi.BaseApplicationContext;
import org.frameworkset.spi.DefaultApplicationContext;

import com.frameworkset.util.StringUtil;
import com.mongodb.DBObject;
import com.mongodb.Mongo;

public class MongoDBHelper {
	public static final String defaultMongoDB = "default";
	private static BaseApplicationContext context = DefaultApplicationContext.getApplicationContext("mongodb.xml");
	public static Mongo getMongoClient(String name)
	{
		if(StringUtil.isEmpty(name))
		{
			return context.getTBeanObject(defaultMongoDB, Mongo.class);
		}
		else
			return context.getTBeanObject(name, Mongo.class);
	}
	
	public static Mongo getMongoClient()
	{
		return getMongoClient(null);
	}
	private static final String dianhaochar = "____";
	private static final String moneychar = "_____";
	private static final int msize = moneychar.length();

	public static String recoverSpecialChar(String attribute) {
		if (attribute.startsWith(moneychar)) {
			attribute = "$" + attribute.substring(msize);
		}

		attribute = attribute.replace(dianhaochar, ".");
		return attribute;
	}
	public static String converterSpecialChar(String attribute)
	{
		attribute = attribute.replace(".", dianhaochar);
		if(attribute.startsWith("$"))
		{
			if(attribute.length() == 1)
			{
				attribute = moneychar;
			}
			else
			{
				attribute = moneychar + attribute.substring(1);
			}
		}
		return attribute;
	}
	public static boolean filter(String key) {
		return key.equals("maxInactiveInterval") || key.equals("creationTime")
				|| key.equals("lastAccessedTime") || key.equals("referip")
				|| key.equals("_validate") || key.equals("sessionid")
				|| key.equals("_id") || key.equals("appKey")
				|| key.equals("host")
				|| key.equals("secure")
				|| key.equals("httpOnly")
				|| key.equals("requesturi")
				|| key.equals("lastAccessedUrl")
				|| key.equals("lastAccessedHostIP");
			
	}
	
	public static String getAppSessionTableName(String appKey)
	{
		return appKey+"_sessions";
	}
	
	public static Map<String,Object> toMap(DBObject object,boolean deserial) {

		Set set = object.keySet();
		if (set != null && set.size() > 0) {
			Map<String,Object> attrs = new HashMap<String,Object>();
			Iterator it = set.iterator();
			while (it.hasNext()) {
				String key = (String) it.next();
				if (!MongoDBHelper.filter(key)) {
					Object value = object.get(key);
					try {
						attrs.put(MongoDBHelper.recoverSpecialChar(key),
								deserial?SessionHelper.unserial((String) value):value);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			return attrs;
		}
		return null;
	}
	
	public static Map<String,Object> toMap(String appkey,String contextpath,DBObject object,boolean deserial) {

		Set set = object.keySet();
		if (set != null && set.size() > 0) {
			Map<String,Object> attrs = new HashMap<String,Object>();
			Iterator it = set.iterator();
			while (it.hasNext()) {
				String key = (String) it.next();
				if (!MongoDBHelper.filter(key)) {
					Object value = object.get(key);
					try {
						String temp = MongoDBHelper.recoverSpecialChar(key);
						temp = SessionHelper.dewraperAttributeName(appkey, contextpath, temp);
						if(temp != null)
							attrs.put(temp,
									deserial?SessionHelper.unserial((String) value):value);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			return attrs;
		}
		return null;
	}
}
