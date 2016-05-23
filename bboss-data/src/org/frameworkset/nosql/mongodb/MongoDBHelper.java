package org.frameworkset.nosql.mongodb;

import org.frameworkset.spi.BaseApplicationContext;
import org.frameworkset.spi.DefaultApplicationContext;

import com.frameworkset.util.StringUtil;
import com.mongodb.DB;

public class MongoDBHelper {
	public static final String defaultMongoDB = "default";

	private static BaseApplicationContext context = DefaultApplicationContext.getApplicationContext("mongodb.xml");


	public static MongoDB getMongoClient(String name)
	{
		if(StringUtil.isEmpty(name))
		{
			return context.getTBeanObject(defaultMongoDB, MongoDB.class);
		}
		else
			return context.getTBeanObject(name, MongoDB.class);
	}
	
	public static MongoDB getMongoDB(String name)
	{
		if(StringUtil.isEmpty(name))
		{
			return context.getTBeanObject(defaultMongoDB, MongoDB.class);
		}
		else
			return context.getTBeanObject(name, MongoDB.class);
	}
	
	public static MongoDB getMongoDB()
	{
		 
			return context.getTBeanObject(defaultMongoDB, MongoDB.class);
		 
	}
	
	public static MongoDB getMongoClient()
	{
		return getMongoDB();
	}
	
//	public static void destory()
//	{
//		if(closeDB)
//			return;
//		try
//		{
//			if(mongoClient != null)
//			{
//				try {
//					mongoClient.close();
//				} catch (Exception e) {
//					log.error("", e);
//				}
//			}
//		}
//		finally
//		{
//			closeDB = true;
//		}
//		
//	}
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
	
	
	
 
	
	
}
