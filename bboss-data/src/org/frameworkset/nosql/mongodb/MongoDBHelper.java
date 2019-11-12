package org.frameworkset.nosql.mongodb;

import com.frameworkset.util.StringUtil;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import org.frameworkset.spi.BaseApplicationContext;
import org.frameworkset.spi.DefaultApplicationContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class MongoDBHelper {
	public static final String defaultMongoDB = "default";
	private static Logger logger = LoggerFactory.getLogger(MongoDBHelper.class);
	private static BaseApplicationContext context = null;
	private static Map<String,MongoDB> mongoDBContainer = new HashMap<String,MongoDB>();

	private static void check(){
		if(context == null){
			try {
				context = DefaultApplicationContext.getApplicationContext("mongodb.xml");
			}
			catch (Exception e){
				logger.warn("Init context with mongodb.xml failed",e);
			}
		}
	}
	public static void init(MongoDBConfig mongoDBConfig){
		MongoDB mongoDB = new MongoDB();
		mongoDB.initWithConfig(mongoDBConfig);
		mongoDB = mongoDB.getMongoClient();
		mongoDBContainer.put(mongoDB.getName(),mongoDB);
	}
	public static MongoDB getMongoClient(String name)
	{
		if(StringUtil.isEmpty(name))
		{
			name = defaultMongoDB;
		}
		MongoDB mongoDB = mongoDBContainer.get(defaultMongoDB);
		if(mongoDB != null)
			return mongoDB;
		synchronized (MongoDBHelper.class) {
			mongoDB = mongoDBContainer.get(name);
			if(mongoDB != null)
				return mongoDB;
			check();
			mongoDB = context.getTBeanObject(name, MongoDB.class);
			if (mongoDB != null) {
				mongoDBContainer.put(name, mongoDB);
			}
		}
		return mongoDB;
	}
	
	public static MongoDB getMongoDB(String name)
	{
		return getMongoClient(name);
	}
	
	public static MongoDB getMongoDB()
	{
		 return getMongoDB(defaultMongoDB);
	}
	
	public static MongoDB getMongoClient()
	{
		return getMongoDB();
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
	

	
	public static DB getDB(String poolname,String dbname)
	{
		return getMongoDB(poolname).getDB( dbname );
	}
	public static DB getDB(String dbname)
	{
		return getDB(defaultMongoDB,dbname);
		
	}
	
	
	public static DBCollection getDBCollection(String poolname,String dbname,String table)
	{
		return getMongoDB(poolname).getDB( dbname ).getCollection(table);
	}
	public static DBCollection getDBCollection(String dbname,String table)
	{
		return getDBCollection(defaultMongoDB,dbname,table);
		
	}
	
	
	
 
	
	
}
