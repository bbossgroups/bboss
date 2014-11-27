package org.frameworkset.nosql;

import java.net.UnknownHostException;
import java.util.Arrays;

import com.mongodb.BasicDBObject;
import com.mongodb.Bytes;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.Mongo;
import com.mongodb.ReadPreference;
import com.mongodb.ServerAddress;

public class MongoDBTest {
	public static void main(String[] args) throws UnknownHostException
	{
		// To directly connect to a single MongoDB server (note that this will not auto-discover the primary even
		// if it's a member of a replica set:
//		MongoClient mongoClient = new MongoClient();
		// or
//		MongoClient mongoClient = new MongoClient( "localhost" );
		// or
//		Mongo mongoClient = new Mongo( "10.0.15.134" , 27018 );
		// or, to connect to a replica set, with auto-discovery of the primary, supply a seed list of members
		Mongo mongoClient = new Mongo(Arrays.asList(new ServerAddress("10.0.15.134", 27017),
				new ServerAddress("10.0.15.134", 27018),
		                                      new ServerAddress("10.0.15.38", 27017),new ServerAddress("10.0.15.39", 27017)
		                                      ));
		mongoClient.addOption( Bytes.QUERYOPTION_SLAVEOK );
		
//		ReadPreference.secondaryPreferred();
		mongoClient.setReadPreference(ReadPreference.nearest());
		DB db = mongoClient.getDB( "mydb" );
		DBCollection coll = db.getCollection("testData");
		DBCursor cursor = coll.find();
		try {
		   while(cursor.hasNext()) {
//		       System.out.println(cursor.next());
		       coll.remove(cursor.next());
		   }
		} finally {
		   cursor.close();
		}
		BasicDBObject doc = new BasicDBObject("name", "MongoDB").
                append("type", "database").
                append("count", 1).
                append("info", new BasicDBObject("x", 203).append("y", 102).append("z", 3000));

		coll.insert(doc);		
		
		BasicDBObject  doc1 = new BasicDBObject("name", "MongoDB Hosts").
	                append("type", "members").
	                append("count", 5).
	                append("Replica Set", new BasicDBObject("member1", "10.0.15.38:27017")
	                		.append("member2", "10.0.15.134:27017")
	                		.append("member3", "10.0.15.134:27018")
	                		.append("member4", "10.0.15.39:27017")
	                		.append("abr", "10.0.15.39:30000"));

			coll.insert(doc1);		
		for (int i=0; i < 100; i++) {
		    coll.insert(new BasicDBObject("i", i));
		}

		cursor = coll.find();
		try {
		   while(cursor.hasNext()) {
		       System.out.println(cursor.next());
		      
		   }
		} finally {
		   cursor.close();
		}

		System.out.println(cursor.count());

	}
	
	
}
