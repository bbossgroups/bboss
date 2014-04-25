package org.frameworkset.nosql.mongodb;

import org.frameworkset.spi.BaseApplicationContext;
import org.frameworkset.spi.DefaultApplicationContext;

import com.frameworkset.util.StringUtil;
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

}
