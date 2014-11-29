package org.frameworkset.nosql.mongodb;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.frameworkset.util.StringUtil;
import com.mongodb.Bytes;
import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoClientOptions.Builder;
import com.mongodb.MongoCredential;
import com.mongodb.ReadPreference;
import com.mongodb.ServerAddress;
import com.mongodb.WriteConcern;


public class MongoDB {
	private static Logger log = Logger.getLogger(MongoDB.class);
	private String serverAddresses;
	private String option;
	private String writeConcern;
	private String readPreference;
	private Mongo mongoclient;
	private String mode = null;
	private boolean autoConnectRetry = true;
	private int connectionsPerHost = 500;
	private int maxWaitTime = 120000;
	private int socketTimeout = 0;
	private int connectTimeout = 15000;
	private int threadsAllowedToBlockForConnectionMultiplier = 50;
	private boolean socketKeepAlive = true;
	private List<ClientMongoCredential> credentials;
	private List<MongoCredential> mongoCredentials;
	public Mongo getMongoClient()
	{
	
//		try {
//			Mongo mongoClient = new Mongo(Arrays.asList(new ServerAddress("10.0.15.134", 27017),
//					new ServerAddress("10.0.15.134", 27018),
//			                                      new ServerAddress("10.0.15.38", 27017),new ServerAddress("10.0.15.39", 27017)
//			                                      ));
//			mongoClient.addOption( Bytes.QUERYOPTION_SLAVEOK );
//			mongoClient.setWriteConcern(WriteConcern.JOURNAL_SAFE);
////	ReadPreference.secondaryPreferred();
//			mongoClient.setReadPreference(ReadPreference.nearest());
////			mongoClient.setReadPreference(ReadPreference.primaryPreferred());
//			return mongoClient;
//		} catch (Exception e) {
//			throw new java.lang.RuntimeException(e);
//		} 
		return mongoclient;
	}
	
	private List<ServerAddress> parserAddress() throws NumberFormatException, UnknownHostException
	{
		if(StringUtil.isEmpty(serverAddresses))
			return null;
		
		serverAddresses = serverAddresses.trim();
		List<ServerAddress> trueaddresses = new ArrayList<ServerAddress>();
		if(mode != null && mode.equals("simple"))
		{
			String info[] = serverAddresses.split(":");
			ServerAddress ad = new ServerAddress(info[0].trim(),Integer.parseInt(info[1].trim()));
			trueaddresses.add(ad);
			return trueaddresses;
		}
		
		String[] addresses = this.serverAddresses.split("\n");
		for(String address:addresses)
		{
			address = address.trim();
			String info[] = address.split(":");
			ServerAddress ad = new ServerAddress(info[0].trim(),Integer.parseInt(info[1].trim()));
			trueaddresses.add(ad);
		}
		return trueaddresses;
	}
	
	private int[] parserOption() throws NumberFormatException, UnknownHostException
	{
		if(StringUtil.isEmpty(this.option))
			return null;
		option = option.trim();
		String[] options = this.option.split("\r\n");
		int[] ret = new int[options.length];
		int i = 0;
		for(String op:options)
		{
			op = op.trim();
			ret[i] = _getOption( op);
			i ++;
		}
		return ret;
	}
	
	private int _getOption(String op)
	{
		if(op.equals("QUERYOPTION_TAILABLE"))
			return Bytes.QUERYOPTION_TAILABLE;
		else if(op.equals("QUERYOPTION_SLAVEOK"))
			return Bytes.QUERYOPTION_SLAVEOK;
		else if(op.equals("QUERYOPTION_OPLOGREPLAY"))
			return Bytes.QUERYOPTION_OPLOGREPLAY;
		else if(op.equals("QUERYOPTION_NOTIMEOUT"))
			return Bytes.QUERYOPTION_NOTIMEOUT;
		
		else if(op.equals("QUERYOPTION_AWAITDATA"))
			return Bytes.QUERYOPTION_AWAITDATA;
		
		else if(op.equals("QUERYOPTION_EXHAUST"))
			return Bytes.QUERYOPTION_EXHAUST;
		
		else if(op.equals("QUERYOPTION_PARTIAL"))
			return Bytes.QUERYOPTION_PARTIAL;
		
		else if(op.equals("RESULTFLAG_CURSORNOTFOUND"))
			return Bytes.RESULTFLAG_CURSORNOTFOUND;
		else if(op.equals("RESULTFLAG_ERRSET"))
			return Bytes.RESULTFLAG_ERRSET;
		
		else if(op.equals("RESULTFLAG_SHARDCONFIGSTALE"))
			return Bytes.RESULTFLAG_SHARDCONFIGSTALE;
		else if(op.equals("RESULTFLAG_AWAITCAPABLE"))
			return Bytes.RESULTFLAG_AWAITCAPABLE;
		throw new RuntimeException("未知的option:"+op);

	 
	}
	
	private WriteConcern  _getWriteConcern()
	{
		if(StringUtil.isEmpty(this.writeConcern))
			return null;
		writeConcern=writeConcern.trim();
		if(this.writeConcern.equals("NONE"))
			return WriteConcern.NONE;
		else if(this.writeConcern.equals("NORMAL"))
			return WriteConcern.NORMAL;
		else if(this.writeConcern.equals("SAFE"))
			return WriteConcern.SAFE;
		else if(this.writeConcern.equals("MAJORITY"))
			return WriteConcern.MAJORITY;
		else if(this.writeConcern.equals("FSYNC_SAFE"))
			return WriteConcern.FSYNC_SAFE;
		else if(this.writeConcern.equals("JOURNAL_SAFE"))
			return WriteConcern.JOURNAL_SAFE;
		else if(this.writeConcern.equals("REPLICAS_SAFE"))
			return WriteConcern.REPLICAS_SAFE;
		throw new RuntimeException("未知的WriteConcern:"+writeConcern);
	}
	
	private ReadPreference _getReadPreference()
	{
		if(StringUtil.isEmpty(this.readPreference))
			return null;
		if(readPreference.equals("PRIMARY"))
			return ReadPreference.primary();
		else if(readPreference.equals("SECONDARY"))
			return ReadPreference.secondary();
		else if(readPreference.equals("SECONDARY_PREFERRED"))
			return ReadPreference.secondaryPreferred();
		else if(readPreference.equals("PRIMARY_PREFERRED"))
			return ReadPreference.primaryPreferred();
		else if(readPreference.equals("NEAREST"))
			return ReadPreference.nearest();
		throw new RuntimeException("未知的ReadPreference:"+readPreference);
	}
	private void buildCredentials()
	{
		if(this.credentials != null && this.credentials.size() > 0)
		{
			this.mongoCredentials = new ArrayList<MongoCredential>();
			for(ClientMongoCredential clientMongoCredential:this.credentials)
			{
				if(StringUtil.isEmpty(clientMongoCredential.getMechanism()) 
						||clientMongoCredential.getMechanism().equals(MongoCredential.MONGODB_CR_MECHANISM))
				{
					mongoCredentials.add(MongoCredential.createMongoCRCredential(clientMongoCredential.getUserName(), clientMongoCredential.getDatabase(),clientMongoCredential.getPassword().toCharArray()));
				}
				else if(clientMongoCredential.getMechanism().equals(MongoCredential.PLAIN_MECHANISM))
				{
					mongoCredentials.add(MongoCredential.createPlainCredential(clientMongoCredential.getUserName(), clientMongoCredential.getDatabase(),clientMongoCredential.getPassword().toCharArray()));
				}
				else if(clientMongoCredential.getMechanism().equals(MongoCredential.MONGODB_X509_MECHANISM))
				{
					mongoCredentials.add(MongoCredential.createMongoX509Credential(clientMongoCredential.getUserName()));
				}
				else if(clientMongoCredential.getMechanism().equals(MongoCredential.GSSAPI_MECHANISM))
				{
					mongoCredentials.add(MongoCredential.createGSSAPICredential(clientMongoCredential.getUserName()));
				}
			}
		}
	}
	public void init()
	{
		try {
			buildCredentials();
			if(mode != null && mode.equals("simple"))
			{
				this.initsimple();
			}
			else
			{
				
				
//				options.autoConnectRetry = autoConnectRetry;
//				options.connectionsPerHost = connectionsPerHost;
//				options.maxWaitTime = maxWaitTime;
//	            options.socketTimeout = socketTimeout;
//	            options.connectTimeout = connectTimeout;
//	            options.threadsAllowedToBlockForConnectionMultiplier = threadsAllowedToBlockForConnectionMultiplier;
//	            options.socketKeepAlive=socketKeepAlive;
				Builder builder = MongoClientOptions.builder();
				builder.autoConnectRetry( autoConnectRetry);
				builder.connectionsPerHost( connectionsPerHost);
				builder.maxWaitTime( maxWaitTime);
				builder.socketTimeout( socketTimeout);
				builder.connectTimeout( connectTimeout);
				builder.threadsAllowedToBlockForConnectionMultiplier( threadsAllowedToBlockForConnectionMultiplier);
				builder.socketKeepAlive(socketKeepAlive);
	            MongoClientOptions options = builder.build();//new MongoClientOptions();
	            MongoClient mongoClient = null;
	            if(mongoCredentials == null || mongoCredentials.size() == 0)
	            {
	            	mongoClient = new MongoClient(parserAddress(),options);
	            }
	            else
	            {
	            	mongoClient = new MongoClient(parserAddress(),mongoCredentials,options);
	            }
				int[] ops = parserOption();
				for(int i = 0; ops != null && i < ops.length; i ++)
					mongoClient.addOption( ops[i] );
				WriteConcern wc = this._getWriteConcern();
				if(wc != null)
					mongoClient.setWriteConcern(wc);
		//ReadPreference.secondaryPreferred();
				ReadPreference rf = _getReadPreference();
				if(rf != null)
					mongoClient.setReadPreference(ReadPreference.nearest());
		//		mongoClient.setReadPreference(ReadPreference.primaryPreferred());
				this.mongoclient = mongoClient;
			}
		} catch (RuntimeException e) {
			log.error("初始化mongodb client failed.", e);
			throw e;
			
		} 
		catch (Exception e) {
			log.error("初始化mongodb client failed.", e);
			throw new RuntimeException(e);
			
		} 
	}
	
	public void initsimple() throws Exception
	{
		try {
//			MongoOptions options = new MongoOptions();
//            options.autoConnectRetry = autoConnectRetry;
//            options.connectionsPerHost = connectionsPerHost;
//            options.maxWaitTime = maxWaitTime;
//            options.socketTimeout = socketTimeout;
//            options.connectTimeout = connectTimeout;
//            options.threadsAllowedToBlockForConnectionMultiplier = threadsAllowedToBlockForConnectionMultiplier;
//            options.socketKeepAlive=socketKeepAlive;
//			Mongo mongoClient = new Mongo(parserAddress().get(0),options);
			Builder builder = MongoClientOptions.builder();
			builder.autoConnectRetry( autoConnectRetry);
			builder.connectionsPerHost( connectionsPerHost);
			builder.maxWaitTime( maxWaitTime);
			builder.socketTimeout( socketTimeout);
			builder.connectTimeout( connectTimeout);
			builder.threadsAllowedToBlockForConnectionMultiplier( threadsAllowedToBlockForConnectionMultiplier);
			builder.socketKeepAlive(socketKeepAlive);
            MongoClientOptions options = builder.build();//new MongoClientOptions();
            MongoClient mongoClient = null;
            if(mongoCredentials == null || mongoCredentials.size() == 0)
            {
            	mongoClient = new MongoClient(parserAddress().get(0),options);
            }
            else
            {
            	mongoClient = new MongoClient(parserAddress().get(0),mongoCredentials,options);
            }
			
			int[] ops = parserOption();
			for(int i = 0; ops != null && i < ops.length; i ++)
				mongoClient.addOption( ops[i] );
			WriteConcern wc = this._getWriteConcern();
			if(wc != null)
				mongoClient.setWriteConcern(wc);
	//ReadPreference.secondaryPreferred();
			
	//		mongoClient.setReadPreference(ReadPreference.primaryPreferred());
			this.mongoclient = mongoClient;
		} catch (RuntimeException e) {
			throw e;
			
		} 
		catch (Exception e) {
			throw e;
			
		} 
	}
	
	public void close()
	{
		if(this.mongoclient != null)
			this.mongoclient.close();
	}

}
