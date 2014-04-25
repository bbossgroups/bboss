package org.frameworkset.nosql.mongodb;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import com.frameworkset.util.StringUtil;
import com.mongodb.Bytes;
import com.mongodb.Mongo;
import com.mongodb.ReadPreference;
import com.mongodb.ServerAddress;
import com.mongodb.WriteConcern;

public class MongoDB {
	
	private String serverAddresses;
	private String option;
	private String writeConcern;
	private String readPreference;
	private Mongo mongoclient;
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
	
	public void init()
	{
		try {
			Mongo mongoClient = new Mongo(parserAddress());
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
		} catch (RuntimeException e) {
			throw e;
			
		} 
		catch (Exception e) {
			throw new RuntimeException(e);
			
		} 
	}
	
	public void close()
	{
		if(this.mongoclient != null)
			this.mongoclient.close();
	}

}
