package org.frameworkset.nosql.mongodb;

import com.frameworkset.util.StringUtil;
import com.mongodb.*;
import com.mongodb.MongoClientOptions.Builder;
import org.frameworkset.spi.BeanNameAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public class MongoDB implements BeanNameAware {
	private static Method autoConnectRetryMethod;
	static {
		try {
			autoConnectRetryMethod = Builder.class.getMethod("autoConnectRetry", boolean.class);
		} catch (NoSuchMethodException e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
		} catch (SecurityException e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
		}
	}
	private MongoDBConfig config;
	private static Logger log = LoggerFactory.getLogger(MongoDB.class);
//	private String serverAddresses;
//	private String option;
//	private String writeConcern;
//	private String readPreference;
	private Mongo mongoclient;
//	private String mode = null;
//	private boolean autoConnectRetry = true;
//	private int connectionsPerHost = 500;
//	private int maxWaitTime = 120000;
//	private int socketTimeout = 0;
//	private int connectTimeout = 15000;
//	private int threadsAllowedToBlockForConnectionMultiplier = 50;
//	private boolean socketKeepAlive = true;

	private List<MongoCredential> mongoCredentials;

	public MongoDB getMongoClient() {

		// try {
		// Mongo mongoClient = new Mongo(Arrays.asList(new
		// ServerAddress("10.0.15.134", 27017),
		// new ServerAddress("10.0.15.134", 27018),
		// new ServerAddress("10.0.15.38", 27017),new
		// ServerAddress("10.0.15.39", 27017)
		// ));
		// mongoClient.addOption( Bytes.QUERYOPTION_SLAVEOK );
		// mongoClient.setWriteConcern(WriteConcern.JOURNAL_SAFE);
		//// ReadPreference.secondaryPreferred();
		// mongoClient.setReadPreference(ReadPreference.nearest());
		//// mongoClient.setReadPreference(ReadPreference.primaryPreferred());
		// return mongoClient;
		// } catch (Exception e) {
		// throw new java.lang.RuntimeException(e);
		// }
		return this;
	}
	
	public Mongo getMongo() {

		// try {
		// Mongo mongoClient = new Mongo(Arrays.asList(new
		// ServerAddress("10.0.15.134", 27017),
		// new ServerAddress("10.0.15.134", 27018),
		// new ServerAddress("10.0.15.38", 27017),new
		// ServerAddress("10.0.15.39", 27017)
		// ));
		// mongoClient.addOption( Bytes.QUERYOPTION_SLAVEOK );
		// mongoClient.setWriteConcern(WriteConcern.JOURNAL_SAFE);
		//// ReadPreference.secondaryPreferred();
		// mongoClient.setReadPreference(ReadPreference.nearest());
		//// mongoClient.setReadPreference(ReadPreference.primaryPreferred());
		// return mongoClient;
		// } catch (Exception e) {
		// throw new java.lang.RuntimeException(e);
		// }
		return this.mongoclient;
	}

	private List<ServerAddress> parserAddress() throws NumberFormatException, UnknownHostException {
		String serverAddresses = this.getServerAddresses();
		if (StringUtil.isEmpty(serverAddresses))
			return null;

		serverAddresses = serverAddresses.trim();
		List<ServerAddress> trueaddresses = new ArrayList<ServerAddress>();
		String mode = this.getMode();
		if (mode != null && mode.equals("simple")) {
			String info[] = serverAddresses.split(":");
			ServerAddress ad = new ServerAddress(info[0].trim(), Integer.parseInt(info[1].trim()));
			trueaddresses.add(ad);
			return trueaddresses;
		}

		String[] addresses = serverAddresses.split("\n");
		for (String address : addresses) {
			address = address.trim();
			String info[] = address.split(":");
			ServerAddress ad = new ServerAddress(info[0].trim(), Integer.parseInt(info[1].trim()));
			trueaddresses.add(ad);
		}
		return trueaddresses;
	}

	private int[] parserOption() throws NumberFormatException, UnknownHostException {
		String option = this.getOption();
		if (StringUtil.isEmpty(option))
			return null;
		option = option.trim();
		String[] options = option.split("\r\n");
		int[] ret = new int[options.length];
		int i = 0;
		for (String op : options) {
			op = op.trim();
			ret[i] = _getOption(op);
			i++;
		}
		return ret;
	}

	private int _getOption(String op) {
		if (op.equals("QUERYOPTION_TAILABLE"))
			return Bytes.QUERYOPTION_TAILABLE;
		else if (op.equals("QUERYOPTION_SLAVEOK"))
			return Bytes.QUERYOPTION_SLAVEOK;
		else if (op.equals("QUERYOPTION_OPLOGREPLAY"))
			return Bytes.QUERYOPTION_OPLOGREPLAY;
		else if (op.equals("QUERYOPTION_NOTIMEOUT"))
			return Bytes.QUERYOPTION_NOTIMEOUT;

		else if (op.equals("QUERYOPTION_AWAITDATA"))
			return Bytes.QUERYOPTION_AWAITDATA;

		else if (op.equals("QUERYOPTION_EXHAUST"))
			return Bytes.QUERYOPTION_EXHAUST;

		else if (op.equals("QUERYOPTION_PARTIAL"))
			return Bytes.QUERYOPTION_PARTIAL;

		else if (op.equals("RESULTFLAG_CURSORNOTFOUND"))
			return Bytes.RESULTFLAG_CURSORNOTFOUND;
		else if (op.equals("RESULTFLAG_ERRSET"))
			return Bytes.RESULTFLAG_ERRSET;

		else if (op.equals("RESULTFLAG_SHARDCONFIGSTALE"))
			return Bytes.RESULTFLAG_SHARDCONFIGSTALE;
		else if (op.equals("RESULTFLAG_AWAITCAPABLE"))
			return Bytes.RESULTFLAG_AWAITCAPABLE;
		throw new RuntimeException("未知的option:" + op);

	}

	public static void main(String[] args) {
		String aa = "REPLICA_ACKNOWLEDGED(10)";
		int idx = aa.indexOf("(");
		String n = aa.substring(idx + 1, aa.length() - 1);
		System.out.println(n);
	}

	private WriteConcern _getWriteConcern() {
		String writeConcern = this.getWriteConcern();
		if (StringUtil.isEmpty(writeConcern))
			return null;
		writeConcern = writeConcern.trim();
		if (writeConcern.equals("NONE"))
			return WriteConcern.UNACKNOWLEDGED;
		else if (writeConcern.equals("NORMAL"))
			return WriteConcern.NORMAL;
		else if (writeConcern.equals("SAFE"))
			return WriteConcern.SAFE;
		else if (writeConcern.equals("MAJORITY"))
			return WriteConcern.MAJORITY;
		else if (writeConcern.equals("W1"))
			return WriteConcern.W1;
		else if (writeConcern.equals("W2"))
			return WriteConcern.W2;
		else if (writeConcern.equals("W3"))
			return WriteConcern.W3;
		else if (writeConcern.equals("FSYNC_SAFE"))
			return WriteConcern.FSYNC_SAFE;
		else if (writeConcern.equals("JOURNAL_SAFE"))
			return WriteConcern.JOURNAL_SAFE;
		else if (writeConcern.equals("JOURNALED"))
			return WriteConcern.JOURNALED;
		else if (writeConcern.equals("REPLICAS_SAFE"))
			return WriteConcern.REPLICAS_SAFE;
		else if (writeConcern.startsWith("REPLICA_ACKNOWLEDGED")) {
			int idx = writeConcern.indexOf("(");
			if (idx < 0) {
				return WriteConcern.REPLICA_ACKNOWLEDGED;
			} else {
				String n = writeConcern.substring(idx + 1, writeConcern.length() - 1);
				try {
					if (n.indexOf(",") < 0) {
						int N = Integer.parseInt(n);
						return new WriteConcern(N);
					} else {
						String[] p = n.split(",");
						n = p[0];
						String _wtimeout = p[1];
						int N = Integer.parseInt(n);
						int wtimeout = Integer.parseInt(_wtimeout);
						return new WriteConcern(N, wtimeout, false);
					}
				} catch (NumberFormatException e) {
					return WriteConcern.REPLICA_ACKNOWLEDGED;
				}
			}
		} else if (writeConcern.equals("ACKNOWLEDGED"))
			return WriteConcern.ACKNOWLEDGED;
		else if (writeConcern.equals("UNACKNOWLEDGED"))
			return WriteConcern.UNACKNOWLEDGED;
		else if (writeConcern.equals("FSYNCED"))
			return WriteConcern.FSYNCED;
		else if (writeConcern.equals("JOURNALED"))
			return WriteConcern.JOURNALED;
		else if (writeConcern.equals("ERRORS_IGNORED"))
			return WriteConcern.UNACKNOWLEDGED;

		throw new RuntimeException("未知的WriteConcern:" + writeConcern);
	}

	private ReadPreference _getReadPreference() {
		String readPreference = this.getReadPreference();
		if (StringUtil.isEmpty(readPreference))
			return null;
		if (readPreference.equals("PRIMARY"))
			return ReadPreference.primary();
		else if (readPreference.equals("SECONDARY"))
			return ReadPreference.secondary();
		else if (readPreference.equals("SECONDARY_PREFERRED"))
			return ReadPreference.secondaryPreferred();
		else if (readPreference.equals("PRIMARY_PREFERRED"))
			return ReadPreference.primaryPreferred();
		else if (readPreference.equals("NEAREST"))
			return ReadPreference.nearest();
		throw new RuntimeException("未知的ReadPreference:" + readPreference);
	}

	private void buildCredentials() {

		if (config.getCredentials() != null && config.getCredentials().size() > 0) {
			this.mongoCredentials = new ArrayList<MongoCredential>();
			for (ClientMongoCredential clientMongoCredential : config.getCredentials()) {
				if (StringUtil.isEmpty(clientMongoCredential.getMechanism())) {
					mongoCredentials.add(MongoCredential.createCredential(clientMongoCredential.getUserName(),
							clientMongoCredential.getDatabase(),
							clientMongoCredential.getPassword().toCharArray()));
				}
				else  if (clientMongoCredential.getMechanism().equals(MongoCredential.SCRAM_SHA_1_MECHANISM)) {
					mongoCredentials.add(MongoCredential.createScramSha1Credential(clientMongoCredential.getUserName(),
							clientMongoCredential.getDatabase(),
							clientMongoCredential.getPassword().toCharArray()));
				}
				else if (clientMongoCredential.getMechanism().equals(MongoCredential.MONGODB_CR_MECHANISM)) {
					mongoCredentials.add(MongoCredential.createMongoCRCredential(clientMongoCredential.getUserName(),
							clientMongoCredential.getDatabase(), clientMongoCredential.getPassword().toCharArray()));
				} else if (clientMongoCredential.getMechanism().equals(MongoCredential.PLAIN_MECHANISM)) {
					mongoCredentials.add(MongoCredential.createPlainCredential(clientMongoCredential.getUserName(),
							clientMongoCredential.getDatabase(), clientMongoCredential.getPassword().toCharArray()));
				} else if (clientMongoCredential.getMechanism().equals(MongoCredential.MONGODB_X509_MECHANISM)) {
					mongoCredentials
							.add(MongoCredential.createMongoX509Credential(clientMongoCredential.getUserName()));
				} else if (clientMongoCredential.getMechanism().equals(MongoCredential.GSSAPI_MECHANISM)) {
					mongoCredentials.add(MongoCredential.createGSSAPICredential(clientMongoCredential.getUserName()));
				}
				
				
			}
		}
	}

	public void initWithConfig(MongoDBConfig config){
		this.config = config;
		this.init();
	}


	public String getName() {
		check();
		return config.getName();
	}

	public void setName(String name) {
		check();
		this.config.setName(name);
	}

	public String getServerAddresses() {
		return this.config.getServerAddresses();
	}

	private void check(){
		if(config == null){
			config = new MongoDBConfig();
		}
	}
	public void setServerAddresses(String serverAddresses) {
		check();
		this.config.setServerAddresses(serverAddresses);
	}
	public String getMode() {
		check();
		return this.config.getMode();
	}

	public void setMode(String mode) {
		check();
		this.config.setMode(mode);
	}
	public String getOption() {
		check();
		return this.config.getOption();
	}

	public void setOption(String option) {
		check();
		this.config.setOption(option);
	}

	public String getWriteConcern() {
		check();
		return config.getWriteConcern();
	}

	public void setWriteConcern(String writeConcern) {
		check();
		this.config.setWriteConcern(writeConcern);
	}

	public String getReadPreference() {
		check();
		return config.getReadPreference();
	}

	public void setReadPreference(String readPreference) {
		check();
		this.config.setReadPreference(readPreference);
	}

	public Boolean getAutoConnectRetry() {
		check();
		return config.getAutoConnectRetry();
	}

	public void setAutoConnectRetry(Boolean autoConnectRetry) {
		check();
		this.config.setAutoConnectRetry(autoConnectRetry);
	}

	public int getConnectionsPerHost() {
		check();
		return config.getConnectionsPerHost();
	}

	public void setConnectionsPerHost(int connectionsPerHost) {
		check();
		this.config.setConnectionsPerHost(connectionsPerHost);
	}

	public int getMaxWaitTime() {
		check();
		return config.getMaxWaitTime();
	}

	public void setMaxWaitTime(int maxWaitTime) {
		check();
		this.config.setMaxWaitTime(maxWaitTime);
	}

	public int getSocketTimeout() {
		check();
		return config.getSocketTimeout();
	}

	public void setSocketTimeout(int socketTimeout) {
		check();
		this.config.setSocketTimeout(socketTimeout);
	}

	public int getConnectTimeout() {
		check();
		return config.getConnectTimeout();
	}

	public void setConnectTimeout(int connectTimeout) {
		check();
		this.config.setConnectTimeout(connectTimeout);
	}

	public int getThreadsAllowedToBlockForConnectionMultiplier() {
		check();
		return config.getThreadsAllowedToBlockForConnectionMultiplier();
	}

	public void setThreadsAllowedToBlockForConnectionMultiplier(int threadsAllowedToBlockForConnectionMultiplier) {
		check();
		this.config.setThreadsAllowedToBlockForConnectionMultiplier(threadsAllowedToBlockForConnectionMultiplier);
	}

	public Boolean getSocketKeepAlive() {
		check();
		return config.getSocketKeepAlive();
	}

	public void setSocketKeepAlive(Boolean socketKeepAlive) {
		check();
		this.config.setSocketKeepAlive(socketKeepAlive);
	}

	public void init() {
		try {
			buildCredentials();
			String mode = this.getMode();
			if (mode != null && mode.equals("simple")) {
				this.initsimple();
			} else {

				// options.autoConnectRetry = autoConnectRetry;
				// options.connectionsPerHost = connectionsPerHost;
				// options.maxWaitTime = maxWaitTime;
				// options.socketTimeout = socketTimeout;
				// options.connectTimeout = connectTimeout;
				// options.threadsAllowedToBlockForConnectionMultiplier =
				// threadsAllowedToBlockForConnectionMultiplier;
				// options.socketKeepAlive=socketKeepAlive;
				Builder builder = MongoClientOptions.builder();
				// builder.autoConnectRetry( autoConnectRetry);
				_autoConnectRetry(builder, this.getAutoConnectRetry());
				builder.connectionsPerHost(this.getConnectionsPerHost());
				builder.maxWaitTime(this.getMaxWaitTime());
				builder.socketTimeout(this.getSocketTimeout());
				builder.connectTimeout(this.getConnectTimeout());
				builder.threadsAllowedToBlockForConnectionMultiplier(this.getThreadsAllowedToBlockForConnectionMultiplier());
				builder.socketKeepAlive(this.getSocketKeepAlive());
				MongoClientOptions options = builder.build();// new
																// MongoClientOptions();
				MongoClient mongoClient = null;
				List<ServerAddress> servers = parserAddress();
				if (mongoCredentials == null || mongoCredentials.size() == 0) {
					if (servers.size() > 1)
						mongoClient = new MongoClient(servers, options);
					else
						mongoClient = new MongoClient(servers.get(0), options);
				} else {
					if (servers.size() > 1)
						mongoClient = new MongoClient(servers, mongoCredentials, options);
					else
						mongoClient = new MongoClient(servers.get(0), mongoCredentials, options);
				}
				int[] ops = parserOption();
				for (int i = 0; ops != null && i < ops.length; i++)
					mongoClient.addOption(ops[i]);
				WriteConcern wc = this._getWriteConcern();
				if (wc != null)
					mongoClient.setWriteConcern(wc);
				// ReadPreference.secondaryPreferred();
				// if(servers.size() > 1)
				{
					ReadPreference rf = _getReadPreference();
					if (rf != null)
						mongoClient.setReadPreference(ReadPreference.nearest());
				}
				// mongoClient.setReadPreference(ReadPreference.primaryPreferred());
				this.mongoclient = mongoClient;
			}
		} catch (RuntimeException e) {
			log.error("初始化mongodb client failed.", e);
			throw e;

		} catch (Exception e) {
			log.error("初始化mongodb client failed.", e);
			throw new RuntimeException(e);

		}
	}

	private void _autoConnectRetry(Builder builder, boolean autoConnectRetry) {
		if (autoConnectRetryMethod != null)
			try {
				autoConnectRetryMethod.invoke(builder, autoConnectRetry);
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}

	public void initsimple() throws Exception {
		try {
			// MongoOptions options = new MongoOptions();
			// options.autoConnectRetry = autoConnectRetry;
			// options.connectionsPerHost = connectionsPerHost;
			// options.maxWaitTime = maxWaitTime;
			// options.socketTimeout = socketTimeout;
			// options.connectTimeout = connectTimeout;
			// options.threadsAllowedToBlockForConnectionMultiplier =
			// threadsAllowedToBlockForConnectionMultiplier;
			// options.socketKeepAlive=socketKeepAlive;
			// Mongo mongoClient = new Mongo(parserAddress().get(0),options);
			Builder builder = MongoClientOptions.builder();
			// builder.autoConnectRetry( autoConnectRetry);
			_autoConnectRetry(builder, this.getAutoConnectRetry());
			builder.connectionsPerHost(this.getConnectionsPerHost());
			builder.maxWaitTime(this.getMaxWaitTime());
			builder.socketTimeout(this.getSocketTimeout());
			builder.connectTimeout(this.getConnectTimeout());
			builder.threadsAllowedToBlockForConnectionMultiplier(this.getThreadsAllowedToBlockForConnectionMultiplier());
			builder.socketKeepAlive(this.getSocketKeepAlive());
			MongoClientOptions options = builder.build();// new
															// MongoClientOptions();
			MongoClient mongoClient = null;
			if (mongoCredentials == null || mongoCredentials.size() == 0) {
				mongoClient = new MongoClient(parserAddress().get(0), options);
			} else {
				mongoClient = new MongoClient(parserAddress().get(0), mongoCredentials, options);
			}

			int[] ops = parserOption();
			for (int i = 0; ops != null && i < ops.length; i++)
				mongoClient.addOption(ops[i]);
			WriteConcern wc = this._getWriteConcern();
			if (wc != null)
				mongoClient.setWriteConcern(wc);
			// ReadPreference.secondaryPreferred();

			// mongoClient.setReadPreference(ReadPreference.primaryPreferred());
			this.mongoclient = mongoClient;
		} catch (RuntimeException e) {
			throw e;

		} catch (Exception e) {
			throw e;

		}
	}

	public void close() {
		if (this.mongoclient != null)
			this.mongoclient.close();
	}

	public static WriteResult update(DBCollection collection, DBObject q, DBObject o) {
		try {
			WriteResult wr = collection.update(q, o);

			return wr;
		} catch (WriteConcernException e) {
			log.debug("update:", e);
			return null;
		}
	}

	public static WriteResult update(DBCollection collection, DBObject q, DBObject o, WriteConcern concern) {

		WriteResult wr = collection.update(q, o, false, false, concern);

		return wr;

	}

	public static DBObject findAndModify(DBCollection collection, DBObject query, DBObject update) {
		try {
			DBObject object = collection.findAndModify(query, update);
			return object;
		} catch (WriteConcernException e) {
			log.debug("findAndModify:", e);
			return null;
		}
	}

	public static DBObject findAndRemove(DBCollection collection, DBObject query) {
		try {
			DBObject object = collection.findAndRemove(query);
			return object;
		} catch (WriteConcernException e) {
			log.debug("findAndRemove:", e);
			return null;
		}
	}

	public static WriteResult insert(DBCollection collection, DBObject... arr) {

		try {
			return collection.insert(arr);
		} catch (WriteConcernException e) {
			log.debug("insert:", e);
			return null;
		}
	}
	
	public static WriteResult insert(DBCollection collection, List<? extends DBObject> arr) {

		try {
			return collection.insert(arr);
		} catch (WriteConcernException e) {
			log.debug("insert:", e);
			return null;
		}
	}

	public static WriteResult insert(WriteConcern concern, DBCollection collection, DBObject... arr) {

		return collection.insert(arr, concern);

	}
	
	public static WriteResult insert(WriteConcern concern, DBCollection collection, List<? extends DBObject> arr) {

		return collection.insert(arr, concern);

	}

	public static WriteResult remove(DBCollection collection, DBObject o) {
		try {
			return collection.remove(o);
		} catch (WriteConcernException e) {
			log.debug("remove:", e);
			return null;
		}
	}

	public static WriteResult remove(DBCollection collection, DBObject o, WriteConcern concern) {
		return collection.remove(o, concern);
	}

	public DB getDB(String dbname) {
		return this.mongoclient.getDB(dbname);
	}
	
	public  DBCollection getDBCollection(String dbname,String table)
	{
		DB db = this.getDB(dbname);
		return db.getCollection(table);
		
	}
	public void setCredentials(List<ClientMongoCredential> credentials) {
		this.check();
		this.config.setCredentials( credentials);
	}
	@Override
	public void setBeanName(String name) {
		this.check();
		this.config.setName(name);
	}
}
