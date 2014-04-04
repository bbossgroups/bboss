package org.frameworkset.nosql.mongodb;

import java.util.Arrays;

import com.mongodb.Bytes;
import com.mongodb.Mongo;
import com.mongodb.ReadPreference;
import com.mongodb.ServerAddress;
import com.mongodb.WriteConcern;

public class MongodbHelper {
	
	public static Mongo getMongoClient(String mongoName)
	{
	
		try {
			Mongo mongoClient = new Mongo(Arrays.asList(new ServerAddress("10.0.15.134", 27017),
					new ServerAddress("10.0.15.134", 27018),
			                                      new ServerAddress("10.0.15.38", 27017),new ServerAddress("10.0.15.39", 27017)
			                                      ));
			mongoClient.addOption( Bytes.QUERYOPTION_SLAVEOK );
			mongoClient.setWriteConcern(WriteConcern.JOURNAL_SAFE);
//	ReadPreference.secondaryPreferred();
			mongoClient.setReadPreference(ReadPreference.nearest());
//			mongoClient.setReadPreference(ReadPreference.primaryPreferred());
			return mongoClient;
		} catch (Exception e) {
			throw new java.lang.RuntimeException(e);
		} 
	}

}
