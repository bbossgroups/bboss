package org.frameworkset.nosql.redis;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.frameworkset.spi.BeanInfoAware;
import org.frameworkset.spi.InitializingBean;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.Protocol;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

public class RedisDB extends BeanInfoAware implements InitializingBean,org.frameworkset.spi.DisposableBean{
	private ShardedJedisPool shardedJedispool;
	private JedisPool jedisPool;
	private Map<String,String> properties;
	private List<NodeInfo> nodes;
	private int poolMaxTotal;
	private long poolMaxWaitMillis;
	private int maxIdle = -1;
	private int timeout = Protocol.DEFAULT_TIMEOUT;
	private int soTimeout =  Protocol.DEFAULT_TIMEOUT;
	private int maxRedirections = 5;
	private String auth;
	public static final String mode_single = "single";
	public static final String mode_cluster = "cluster";
	public static final String mode_shared = "shared";
	/**
	 * single|cluster|shared
	 */
	private String mode = mode_single;
	
	private JedisCluster jc; 

	 
	public RedisDB() {
		// TODO Auto-generated constructor stub
	}
	
	public void startSharedPool() {
		  GenericObjectPoolConfig config = new GenericObjectPoolConfig();
		    config.setMaxTotal(poolMaxTotal);
		    config.setMaxWaitMillis(poolMaxWaitMillis);
		    List<JedisShardInfo> jedisClusterNode = new ArrayList<JedisShardInfo>();
		    for(int i = 0; i < nodes.size(); i ++)
		    {
		    	NodeInfo node = nodes.get(i);
		    	JedisShardInfo jedisShardInfo = new JedisShardInfo(node.getHost(), node.getPort());
		    	if(this.auth != null)
		    		jedisShardInfo.setPassword(auth);
		    	jedisClusterNode.add(jedisShardInfo);
		    }
		    
		    shardedJedispool = new ShardedJedisPool(config, jedisClusterNode);
	  
	   
	  }
	
	public void startSingleNode()
	{
		JedisPoolConfig config = new JedisPoolConfig();
		config.setMaxTotal(poolMaxTotal);
		config.setMaxWaitMillis(poolMaxWaitMillis);
		if(maxIdle > 0)
			config.setMaxIdle(maxIdle);
		
		NodeInfo node = nodes.get(0);
		jedisPool = new JedisPool(config,node.getHost(), node.getPort(), timeout,this.auth);
		 
//		    Jedis jedis = pool.getResource();
//		    jedis.auth(this.auth);
//		   
//		    jedis.close();
//		    pool.destroy();
	}
	 public void startPoolClusterPools() {
		    GenericObjectPoolConfig config = new GenericObjectPoolConfig();
		    config.setMaxTotal(poolMaxTotal);
			config.setMaxWaitMillis(poolMaxWaitMillis);
			if(maxIdle > 0)
				config.setMaxIdle(maxIdle);
			
			
		    Set<HostAndPort> jedisClusterNode = new HashSet<HostAndPort>();
		    for(int i = 0; i < nodes.size(); i ++)
		    {
		    	NodeInfo node = nodes.get(i);
		    	HostAndPort hostAndPort = new HostAndPort(node.getHost(), node.getPort());
		    	 
		    	jedisClusterNode.add(hostAndPort);
		    }
		  
		    
//		    jc = new JedisCluster(jedisClusterNode,this.timeout,this.maxRedirections, config);
		    jc = new JedisCluster(jedisClusterNode, timeout, soTimeout,
		    		this.maxRedirections, auth,config);
//		    jc.set("52", "poolTestValue2");
//		    jc.set("53", "poolTestValue2");
//		    System.out.println(jc.get("52")); 
//		    System.out.println(jc.get("53")); 
//		    jc.close();
		 
		  }
		  
	
	public Jedis getRedis()
	{
		 Jedis jedis = jedisPool.getResource();
		 if(auth != null)
			 jedis.auth(this.auth);
		 return jedis;
	}
	public ShardedJedis getSharedRedis()
	{
		 ShardedJedis jedis = shardedJedispool.getResource();
		 
			 
		 return jedis;
	}
	public JedisCluster geJedisCluster()
	{
		return jc;
	}
	public void releaseSharedRedis(ShardedJedis redis) throws IOException
	{
		redis.close();
	}
	
	public void releaseRedis(Jedis redis) throws IOException
	{
		redis.close();
	}
	
	public void close()
	{
		if(shardedJedispool != null)
			this.shardedJedispool.destroy();
		if(jc != null)
			try {
				jc.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		if(jedisPool != null)
			jedisPool.destroy();
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		if(getMode().equals(mode_shared))
		{
			this.startSharedPool();
		}
		else if(getMode().equals(mode_cluster))
		{
			this.startPoolClusterPools();
		}
		else if(getMode().equals(mode_single))
		{
			this.startSingleNode();
		}
		
	}

	public int getPoolMaxTotal() {
		return poolMaxTotal;
	}

	public void setPoolMaxTotal(int poolMaxTotal) {
		this.poolMaxTotal = poolMaxTotal;
	}

	public long getPoolMaxWaitMillis() {
		return poolMaxWaitMillis;
	}

	public void setPoolMaxWaitMillis(long poolMaxWaitMillis) {
		this.poolMaxWaitMillis = poolMaxWaitMillis;
	}

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	public String getAuth() {
		return auth;
	}

	public void setAuth(String auth) {
		this.auth = auth;
	}

	@Override
	public void destroy() throws Exception {
		// TODO Auto-generated method stub
		close();
	}
	
	
	
	

}
