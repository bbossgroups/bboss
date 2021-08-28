package org.frameworkset.nosql.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.BinaryClient.LIST_POSITION;
import redis.clients.jedis.*;
import redis.clients.jedis.params.sortedset.ZAddParams;
import redis.clients.jedis.params.sortedset.ZIncrByParams;

import java.util.List;
import java.util.Map;
import java.util.Set;

//import redis.clients.jedis.params.set.SetParams;

public class RedisHelper {
	private static Logger logger = LoggerFactory.getLogger(RedisHelper.class);

	public Jedis getJedis() {
		init();
		return jedis;
	}



	private Jedis jedis;
	private ShardedJedis shardedJedis; 
	private JedisCluster jc; 
	private RedisDB db;
	private boolean inited;
	
	public RedisHelper(RedisDB db ) {
		 this.db = db;
		
	}
	public void init()
	{
		if(inited)
			return;
		if(db.getMode().equals(db.mode_shared))
		{
			this.shardedJedis = db.getSharedRedis();
		}
		else if(db.getMode().equals(db.mode_cluster))
		{
			this.jc = db.geJedisCluster();
		}
		else if(db.getMode().equals(db.mode_single))
		{
			this.jedis = db.getRedis();
		}
		inited = true;	
	}
	
	 
	public void release()
	{
		if(shardedJedis != null)
			try {
				db.releaseSharedRedis(shardedJedis);
			} catch (Exception e) {
				logger.warn("",e);
			}
		if(this.jedis != null)
			try {
				db.releaseRedis(jedis);
			} catch (Exception e) {
				logger.warn("",e);
			}
	}
	/**
	   * Set the string value as value of the key. The string can't be longer than 1073741824 bytes (1
	   * GB).
	   * <p>
	   * Time complexity: O(1)
	   * @param key
	   * @param value
	   * @return Status code reply
	   */
	   
	  public String set(final String key, String value) {
		  init();
		  if(shardedJedis != null)
				return shardedJedis.set(key, value);
		  else if(this.jedis != null)
				return jedis.set(key, value);
		  else
			return jc.set(key, value);
	  }

//	  /**
//	   * Set the string value as value of the key. The string can't be longer than 1073741824 bytes (1
//	   * GB).
//	   * @param key
//	   * @param value
//	   * @param params NX|XX, NX -- Only set the key if it does not already exist. XX -- Only set the
//	   *          key if it already exist. EX|PX, expire time units: EX = seconds; PX = milliseconds
//	   * @return Status code reply
//	   */
//	  public String set(final String key, final String value, final SetParams params) {
//		  init();
//		  if(shardedJedis != null)
//				return shardedJedis.set(key, value,params);
//		  else if(this.jedis != null)
//				return jedis.set(key, value,params);
//		  else
//			  	return jc.set(key, value,params);
//	  }

	  /**
	   * Get the value of the specified key. If the key does not exist null is returned. If the value
	   * stored at key is not a string an error is returned because GET can only handle string values.
	   * <p>
	   * Time complexity: O(1)
	   * @param key
	   * @return Bulk reply
	   */
	 
	  public String get(final String key) {
		  init();
		  if(shardedJedis != null)
				return shardedJedis.get(  key);
		  else if(this.jedis != null)
				return jedis.get(  key);
		  else
			  	return jc.get(  key);
	  }

	  /**
	   * Test if the specified key exists. The command returns the number of keys existed Time
	   * complexity: O(N)
	   * @param keys
	   * @return Integer reply, specifically: an integer greater than 0 if one or more keys were removed
	   *         0 if none of the specified key existed
	   */
	  public Long exists(final String... keys) {
		  init();
		  if(shardedJedis != null)
				throw new java.lang.UnsupportedOperationException("sharded Jedis Unsupport exists multi keys mehtod.");
		  else if(this.jedis != null)
				return jedis.exists(  keys);
		  else
			  	return jc.exists(  keys);
	  }

	  /**
	   * Test if the specified key exists. The command returns "1" if the key exists, otherwise "0" is
	   * returned. Note that even keys set with an empty string as value will return "1". Time
	   * complexity: O(1)
	   * @param key
	   * @return Boolean reply, true if the key exists, otherwise false
	   */
	  
	  public Boolean exists(final String key) {
		  init();
		  if(shardedJedis != null)
				return shardedJedis.exists(  key);
		  else if(this.jedis != null)
				return jedis.exists(  key);
		  else
			  	return jc.exists(  key);
	  }

	  /**
	   * Remove the specified keys. If a given key does not exist no operation is performed for this
	   * key. The command returns the number of keys removed. Time complexity: O(1)
	   * @param keys
	   * @return Integer reply, specifically: an integer greater than 0 if one or more keys were removed
	   *         0 if none of the specified key existed
	   */
	 
	  public Long del(final String... keys) {
		  init();
		  if(shardedJedis != null)
			  throw new java.lang.UnsupportedOperationException("sharded Jedis Unsupport del multi keys mehtod.");
		  else if(this.jedis != null)
				return jedis.del(  keys);
		  else
			  	return jc.del(  keys);
	  }

	 
	  public Long del(String key) {
		  init();
		  if(shardedJedis != null)
				return shardedJedis.del(  key);
		  else if(this.jedis != null)
				return jedis.del(  key);
		  else
			  	return jc.del(  key);
	  }

	  /**
	   * Return the type of the value stored at key in form of a string. The type can be one of "none",
	   * "string", "list", "set". "none" is returned if the key does not exist. Time complexity: O(1)
	   * @param key
	   * @return Status code reply, specifically: "none" if the key does not exist "string" if the key
	   *         contains a String value "list" if the key contains a List value "set" if the key
	   *         contains a Set value "zset" if the key contains a Sorted Set value "hash" if the key
	   *         contains a Hash value
	   */
	 
	  public String type(final String key) {
		  init();
		  if(shardedJedis != null)
				return shardedJedis.type(  key) ;
		  else if(this.jedis != null)
				return jedis.type(  key) ;
		  else
			  	return jc.type(  key) ;
	  }

	  /**
	   * Returns all the keys matching the glob-style pattern as space separated strings. For example if
	   * you have in the database the keys "foo" and "foobar" the command "KEYS foo*" will return
	   * "foo foobar".
	   * <p>
	   * Note that while the time complexity for this operation is O(n) the constant times are pretty
	   * low. For example Redis running on an entry level laptop can scan a 1 million keys database in
	   * 40 milliseconds. <b>Still it's better to consider this one of the slow commands that may ruin
	   * the DB performance if not used with care.</b>
	   * <p>
	   * In other words this command is intended only for debugging and special operations like creating
	   * a script to change the DB schema. Don't use it in your normal code. Use Redis Sets in order to
	   * group together a subset of objects.
	   * <p>
	   * Glob style patterns examples:
	   * <ul>
	   * <li>h?llo will match hello hallo hhllo
	   * <li>h*llo will match hllo heeeello
	   * <li>h[ae]llo will match hello and hallo, but not hillo
	   * </ul>
	   * <p>
	   * Use \ to escape special chars if you want to match them verbatim.
	   * <p>
	   * Time complexity: O(n) (with n being the number of keys in the DB, and assuming keys and pattern
	   * of limited length)
	   * @param pattern
	   * @return Multi bulk reply
	   */
	  
	  public Set<String> keys(final String pattern) {
		  init();
		  if(shardedJedis != null)
			  throw new java.lang.UnsupportedOperationException("sharded Jedis Unsupport   keys pattern mehtod.");
		  else if(this.jedis != null)
				return jedis.keys(  pattern) ;
		  else
			  throw new java.lang.UnsupportedOperationException("Jedis cluster Unsupport   keys pattern mehtod.");
	  }

	  /**
	   * Return a randomly selected key from the currently selected DB.
	   * <p>
	   * Time complexity: O(1)
	   * @return Singe line reply, specifically the randomly selected key or an empty string is the
	   *         database is empty
	   */
	  
	  public String randomKey() {
		  init();
		  if(shardedJedis != null)
			  throw new java.lang.UnsupportedOperationException("sharded Jedis Unsupport   randomKey mehtod.");
		  else if(this.jedis != null)
				return jedis.randomKey();
		  else
			  throw new java.lang.UnsupportedOperationException("Jedis cluster Unsupport   randomKey mehtod.");
	  }

//	  /**
//	   * Atomically renames the key oldkey to newkey. If the source and destination name are the same an
//	   * error is returned. If newkey already exists it is overwritten.
//	   * <p>
//	   * Time complexity: O(1)
//	   * @param oldkey
//	   * @param newkey
//	   * @return Status code repy
//	   */
//	  @Override
//	  public String rename(final String oldkey, final String newkey) {
//		  if(shardedJedis != null)
//				return shardedJedis.set(key, value,params);
//		  else if(this.jedis != null)
//				return jedis.set(key, value,params);
//		  else
//			  	return jc.set(key, value,params);
//	  }

//	  /**
//	   * Rename oldkey into newkey but fails if the destination key newkey already exists.
//	   * <p>
//	   * Time complexity: O(1)
//	   * @param oldkey
//	   * @param newkey
//	   * @return Integer reply, specifically: 1 if the key was renamed 0 if the target key already exist
//	   */
//	  @Override
//	  public Long renamenx(final String oldkey, final String newkey) {
//		  if(shardedJedis != null)
//				return shardedJedis.set(key, value,params);
//		  else if(this.jedis != null)
//				return jedis.set(key, value,params);
//		  else
//			  	return jc.set(key, value,params);
//	  }

	  /**
	   * Set a timeout on the specified key. After the timeout the key will be automatically deleted by
	   * the server. A key with an associated timeout is said to be volatile in Redis terminology.
	   * <p>
	   * Voltile keys are stored on disk like the other keys, the timeout is persistent too like all the
	   * other aspects of the dataset. Saving a dataset containing expires and stopping the server does
	   * not stop the flow of time as Redis stores on disk the time when the key will no longer be
	   * available as Unix time, and not the remaining seconds.
	   * <p>
	   * Since Redis 2.1.3 you can update the value of the timeout of a key already having an expire
	   * set. It is also possible to undo the expire at all turning the key into a normal key using the
	   * {@link #persist(String) PERSIST} command.
	   * <p>
	   * Time complexity: O(1)
	   * @see <a href="http://redis.io/commands/expire">Expire Command</a>
	   * @param key
	   * @param seconds
	   * @return Integer reply, specifically: 1: the timeout was set. 0: the timeout was not set since
	   *         the key already has an associated timeout (this may happen only in Redis versions &lt;
	   *         2.1.3, Redis &gt;= 2.1.3 will happily update the timeout), or the key does not exist.
	   */
	 
	  public Long expire(final String key, final int seconds) {
		  init();
		  if(shardedJedis != null)
				return shardedJedis.expire(  key,   seconds);
		  else if(this.jedis != null)
				return jedis.expire(  key,   seconds);
		  else
			  	return jc.expire(  key,   seconds);
	  }

	  /**
	   * EXPIREAT works exctly like {@link #expire(String, int) EXPIRE} but instead to get the number of
	   * seconds representing the Time To Live of the key as a second argument (that is a relative way
	   * of specifing the TTL), it takes an absolute one in the form of a UNIX timestamp (Number of
	   * seconds elapsed since 1 Gen 1970).
	   * <p>
	   * EXPIREAT was introduced in order to implement the Append Only File persistence mode so that
	   * EXPIRE commands are automatically translated into EXPIREAT commands for the append only file.
	   * Of course EXPIREAT can also used by programmers that need a way to simply specify that a given
	   * key should expire at a given time in the future.
	   * <p>
	   * Since Redis 2.1.3 you can update the value of the timeout of a key already having an expire
	   * set. It is also possible to undo the expire at all turning the key into a normal key using the
	   * {@link #persist(String) PERSIST} command.
	   * <p>
	   * Time complexity: O(1)
	   * @see <a href="http://redis.io/commands/expire">Expire Command</a>
	   * @param key
	   * @param unixTime
	   * @return Integer reply, specifically: 1: the timeout was set. 0: the timeout was not set since
	   *         the key already has an associated timeout (this may happen only in Redis versions &lt;
	   *         2.1.3, Redis &gt;= 2.1.3 will happily update the timeout), or the key does not exist.
	   */
	 
	  public Long expireAt(final String key, final long unixTime) {
		  init();
		  if(shardedJedis != null)
				return shardedJedis.expireAt(  key,  unixTime);
		  else if(this.jedis != null)
				return jedis.expireAt(  key,  unixTime);
		  else
			  	return jc.expireAt(  key,  unixTime);
	  }

	  /**
	   * The TTL command returns the remaining time to live in seconds of a key that has an
	   * {@link #expire(String, int) EXPIRE} set. This introspection capability allows a Redis client to
	   * check how many seconds a given key will continue to be part of the dataset.
	   * @param key
	   * @return Integer reply, returns the remaining time to live in seconds of a key that has an
	   *         EXPIRE. In Redis 2.6 or older, if the Key does not exists or does not have an
	   *         associated expire, -1 is returned. In Redis 2.8 or newer, if the Key does not have an
	   *         associated expire, -1 is returned or if the Key does not exists, -2 is returned.
	   */
	 
	  public Long ttl(final String key) {
		  init();
		  if(shardedJedis != null)
				return shardedJedis.ttl(  key) ;
		  else if(this.jedis != null)
				return jedis.ttl(  key) ;
		  else
			  	return jc.ttl(  key) ;
	  }

	 

	  /**
	   * GETSET is an atomic set this value and return the old value command. Set key to the string
	   * value and return the old value stored at key. The string can't be longer than 1073741824 bytes
	   * (1 GB).
	   * <p>
	   * Time complexity: O(1)
	   * @param key
	   * @param value
	   * @return Bulk reply
	   */
	 
	  public String getSet(final String key, final String value) {
		  init();
		  if(shardedJedis != null)
				return shardedJedis.getSet(  key,  value);
		  else if(this.jedis != null)
				return jedis.getSet(  key,  value);
		  else
			  	return jc.getSet(  key,  value);
	  }

	  /**
	   * Get the values of all the specified keys. If one or more keys dont exist or is not of type
	   * String, a 'nil' value is returned instead of the value of the specified key, but the operation
	   * never fails.
	   * <p>
	   * Time complexity: O(1) for every key
	   * @param keys
	   * @return Multi bulk reply
	   */
	  
	  public List<String> mget(final String... keys) {
		  init();
		  if(shardedJedis != null)
			  throw new java.lang.UnsupportedOperationException("sharded Jedis Unsupport   mget mehtod.");
		  else if(this.jedis != null)
				return jedis.mget( keys) ;
		  else
			  	return jc.mget( keys) ;
	  }

	  /**
	   * SETNX works exactly like {@link #set(String, String) SET} with the only difference that if the
	   * key already exists no operation is performed. SETNX actually means "SET if Not eXists".
	   * <p>
	   * Time complexity: O(1)
	   * @param key
	   * @param value
	   * @return Integer reply, specifically: 1 if the key was set 0 if the key was not set
	   */
	  
	  public Long setnx(final String key, final String value) {
		  init();
		  if(shardedJedis != null)
				return shardedJedis.setnx( key,   value);
		  else if(this.jedis != null)
				return jedis.setnx( key,   value);
		  else
			  	return jc.setnx( key,   value);
	  }

	  /**
	   * The command is exactly equivalent to the following group of commands:
	   * {@link #set(String, String) SET} + {@link #expire(String, int) EXPIRE}. The operation is
	   * atomic.
	   * <p>
	   * Time complexity: O(1)
	   * @param key
	   * @param seconds
	   * @param value
	   * @return Status code reply
	   */
	 
	  public String setex(final String key, final int seconds, final String value) {
		  init();
		  if(shardedJedis != null)
				return shardedJedis.setex( key,  seconds,   value);
		  else if(this.jedis != null)
				return jedis.setex( key,  seconds,   value);
		  else
			  	return jc.setex( key,  seconds,   value);
	  }

	  /**
	   * Set the the respective keys to the respective values. MSET will replace old values with new
	   * values, while {@link #msetnx(String...) MSETNX} will not perform any operation at all even if
	   * just a single key already exists.
	   * <p>
	   * Because of this semantic MSETNX can be used in order to set different keys representing
	   * different fields of an unique logic object in a way that ensures that either all the fields or
	   * none at all are set.
	   * <p>
	   * Both MSET and MSETNX are atomic operations. This means that for instance if the keys A and B
	   * are modified, another client talking to Redis can either see the changes to both A and B at
	   * once, or no modification at all.
	   * @see #msetnx(String...)
	   * @param keysvalues
	   * @return Status code reply Basically +OK as MSET can't fail
	   */
	 
	  public String mset(final String... keysvalues) {
		  init();
		  if(shardedJedis != null)
			  throw new java.lang.UnsupportedOperationException("sharded Jedis Unsupport   mset mehtod.");
		  else if(this.jedis != null)
				return jedis.mset(  keysvalues);
		  else
			  	return jc.mset(  keysvalues);
	  }

	  /**
	   * Set the the respective keys to the respective values. {@link #mset(String...) MSET} will
	   * replace old values with new values, while MSETNX will not perform any operation at all even if
	   * just a single key already exists.
	   * <p>
	   * Because of this semantic MSETNX can be used in order to set different keys representing
	   * different fields of an unique logic object in a way that ensures that either all the fields or
	   * none at all are set.
	   * <p>
	   * Both MSET and MSETNX are atomic operations. This means that for instance if the keys A and B
	   * are modified, another client talking to Redis can either see the changes to both A and B at
	   * once, or no modification at all.
	   * @see #mset(String...)
	   * @param keysvalues
	   * @return Integer reply, specifically: 1 if the all the keys were set 0 if no key was set (at
	   *         least one key already existed)
	   */
	  
	  public Long msetnx(final String... keysvalues) {
		  init();
		  if(shardedJedis != null)
			  throw new java.lang.UnsupportedOperationException("sharded Jedis Unsupport   msetnx mehtod.");
		  else if(this.jedis != null)
				return jedis.msetnx( keysvalues) ;
		  else
			  	return jc.msetnx(  keysvalues);
	  }

	  /**
	   * IDECRBY work just like {@link #decr(String) INCR} but instead to decrement by 1 the decrement
	   * is integer.
	   * <p>
	   * INCR commands are limited to 64 bit signed integers.
	   * <p>
	   * Note: this is actually a string operation, that is, in Redis there are not "integer" types.
	   * Simply the string stored at the key is parsed as a base 10 64 bit signed integer, incremented,
	   * and then converted back as a string.
	   * <p>
	   * Time complexity: O(1)
	   * @see #incr(String)
	   * @see #decr(String)
	   * @see #incrBy(String, long)
	   * @param key
	   * @param integer
	   * @return Integer reply, this commands will reply with the new value of key after the increment.
	   */
	 
	  public Long decrBy(final String key, final long integer) {
		  init();
		  if(shardedJedis != null)
			  return shardedJedis.decrBy(  key,   integer) ;
		  else if(this.jedis != null)
				return jedis.decrBy(  key,   integer) ;
		  else
			  	return jc.decrBy(  key,   integer) ;
	  }

	  /**
	   * Decrement the number stored at key by one. If the key does not exist or contains a value of a
	   * wrong type, set the key to the value of "0" before to perform the decrement operation.
	   * <p>
	   * INCR commands are limited to 64 bit signed integers.
	   * <p>
	   * Note: this is actually a string operation, that is, in Redis there are not "integer" types.
	   * Simply the string stored at the key is parsed as a base 10 64 bit signed integer, incremented,
	   * and then converted back as a string.
	   * <p>
	   * Time complexity: O(1)
	   * @see #incr(String)
	   * @see #incrBy(String, long)
	   * @see #decrBy(String, long)
	   * @param key
	   * @return Integer reply, this commands will reply with the new value of key after the increment.
	   */
	 
	  public Long decr(final String key) {
		  init();
		  if(shardedJedis != null)
			  return shardedJedis.decr(  key);
		  else if(this.jedis != null)
				return jedis.decr(  key);
		  else
			  	return jc.decr(  key);
	  }

	  /**
	   * INCRBY work just like {@link #incr(String) INCR} but instead to increment by 1 the increment is
	   * integer.
	   * <p>
	   * INCR commands are limited to 64 bit signed integers.
	   * <p>
	   * Note: this is actually a string operation, that is, in Redis there are not "integer" types.
	   * Simply the string stored at the key is parsed as a base 10 64 bit signed integer, incremented,
	   * and then converted back as a string.
	   * <p>
	   * Time complexity: O(1)
	   * @see #incr(String)
	   * @see #decr(String)
	   * @see #decrBy(String, long)
	   * @param key
	   * @param integer
	   * @return Integer reply, this commands will reply with the new value of key after the increment.
	   */
	  public Long incrBy(final String key, final long integer) {
		  init();
		  if(shardedJedis != null)
				return shardedJedis.incrBy(  key,  integer);
		  else if(this.jedis != null)
				return jedis.incrBy(  key,  integer);
		  else
			  	return jc.incrBy(  key,  integer);
	  }

	  /**
	   * INCRBYFLOAT
	   * <p>
	   * INCRBYFLOAT commands are limited to double precision floating point values.
	   * <p>
	   * Note: this is actually a string operation, that is, in Redis there are not "double" types.
	   * Simply the string stored at the key is parsed as a base double precision floating point value,
	   * incremented, and then converted back as a string. There is no DECRYBYFLOAT but providing a
	   * negative value will work as expected.
	   * <p>
	   * Time complexity: O(1)
	   * @param key
	   * @param value
	   * @return Double reply, this commands will reply with the new value of key after the increment.
	   */
	  
	  public Double incrByFloat(final String key, final double value) {
		  init();
		  if(shardedJedis != null)
				return shardedJedis.incrByFloat(  key,   value);
		  else if(this.jedis != null)
				return jedis.incrByFloat(  key,   value);
		  else
			  	return jc.incrByFloat(  key,   value);
	  }

	  /**
	   * Increment the number stored at key by one. If the key does not exist or contains a value of a
	   * wrong type, set the key to the value of "0" before to perform the increment operation.
	   * <p>
	   * INCR commands are limited to 64 bit signed integers.
	   * <p>
	   * Note: this is actually a string operation, that is, in Redis there are not "integer" types.
	   * Simply the string stored at the key is parsed as a base 10 64 bit signed integer, incremented,
	   * and then converted back as a string.
	   * <p>
	   * Time complexity: O(1)
	   * @see #incrBy(String, long)
	   * @see #decr(String)
	   * @see #decrBy(String, long)
	   * @param key
	   * @return Integer reply, this commands will reply with the new value of key after the increment.
	   */
	 
	  public Long incr(final String key) {
		  init();
		  if(shardedJedis != null)
				return shardedJedis.incr(  key);
		  else if(this.jedis != null)
				return jedis.incr(  key);
		  else
			  	return jc.incr(  key);
	  }

	  /**
	   * If the key already exists and is a string, this command appends the provided value at the end
	   * of the string. If the key does not exist it is created and set as an empty string, so APPEND
	   * will be very similar to SET in this special case.
	   * <p>
	   * Time complexity: O(1). The amortized time complexity is O(1) assuming the appended value is
	   * small and the already present value is of any size, since the dynamic string library used by
	   * Redis will double the free space available on every reallocation.
	   * @param key
	   * @param value
	   * @return Integer reply, specifically the total length of the string after the append operation.
	   */
	  public Long append(final String key, final String value) {
		  init();
		  if(shardedJedis != null)
				return shardedJedis.append( key,   value);
		  else if(this.jedis != null)
				return jedis.append( key,   value);
		  else
			  	return jc.append( key,   value);
	  }

	  /**
	   * Return a subset of the string from offset start to offset end (both offsets are inclusive).
	   * Negative offsets can be used in order to provide an offset starting from the end of the string.
	   * So -1 means the last char, -2 the penultimate and so forth.
	   * <p>
	   * The function handles out of range requests without raising an error, but just limiting the
	   * resulting range to the actual length of the string.
	   * <p>
	   * Time complexity: O(start+n) (with start being the start index and n the total length of the
	   * requested range). Note that the lookup part of this command is O(1) so for small strings this
	   * is actually an O(1) command.
	   * @param key
	   * @param start
	   * @param end
	   * @return Bulk reply
	   */
	  public String substr(final String key, final int start, final int end) {
		  init();
		  if(shardedJedis != null)
				return shardedJedis.substr(  key,  start,   end);
		  else if(this.jedis != null)
				return jedis.substr(  key,  start,   end);
		  else
			  	return jc.substr(  key,  start,   end);
	  }

	  /**
	   * Set the specified hash field to the specified value.
	   * <p>
	   * If key does not exist, a new key holding a hash is created.
	   * <p>
	   * <b>Time complexity:</b> O(1)
	   * @param key
	   * @param field
	   * @param value
	   * @return If the field already exists, and the HSET just produced an update of the value, 0 is
	   *         returned, otherwise if a new field is created 1 is returned.
	   */
	  public Long hset(final String key, final String field, final String value) {
		  init();
		  if(shardedJedis != null)
				return shardedJedis.hset(  key,   field,   value) ;
		  else if(this.jedis != null)
				return jedis.hset(  key,   field,   value) ;
		  else
			  	return jc.hset(  key,   field,   value) ;
	  }

	  /**
	   * If key holds a hash, retrieve the value associated to the specified field.
	   * <p>
	   * If the field is not found or the key does not exist, a special 'nil' value is returned.
	   * <p>
	   * <b>Time complexity:</b> O(1)
	   * @param key
	   * @param field
	   * @return Bulk reply
	   */
	  public String hget(final String key, final String field) {
		  init();
		  if(shardedJedis != null)
				return shardedJedis.hget(  key,   field);
		  else if(this.jedis != null)
				return jedis.hget(  key,   field);
		  else
			  	return jc.hget(  key,   field);
	  }

	  /**
	   * Set the specified hash field to the specified value if the field not exists. <b>Time
	   * complexity:</b> O(1)
	   * @param key
	   * @param field
	   * @param value
	   * @return If the field already exists, 0 is returned, otherwise if a new field is created 1 is
	   *         returned.
	   */
	  public Long hsetnx(final String key, final String field, final String value) {
		  init();
		  if(shardedJedis != null)
				return shardedJedis.hsetnx(  key,   field,   value) ;
		  else if(this.jedis != null)
				return jedis.hsetnx(  key,   field,   value) ;
		  else
			  	return jc.hsetnx(  key,   field,   value) ;
	  }

	  /**
	   * Set the respective fields to the respective values. HMSET replaces old values with new values.
	   * <p>
	   * If key does not exist, a new key holding a hash is created.
	   * <p>
	   * <b>Time complexity:</b> O(N) (with N being the number of fields)
	   * @param key
	   * @param hash
	   * @return Return OK or Exception if hash is empty
	   */
	 
	  public String hmset(final String key, final Map<String, String> hash) {
		  init();
		  if(shardedJedis != null)
				return shardedJedis. hmset( key,  hash)  ;
		  else if(this.jedis != null)
				return jedis. hmset( key,  hash)  ;
		  else
			  	return jc. hmset( key,  hash)  ;
	  }

	  /**
	   * Retrieve the values associated to the specified fields.
	   * <p>
	   * If some of the specified fields do not exist, nil values are returned. Non existing keys are
	   * considered like empty hashes.
	   * <p>
	   * <b>Time complexity:</b> O(N) (with N being the number of fields)
	   * @param key
	   * @param fields
	   * @return Multi Bulk Reply specifically a list of all the values associated with the specified
	   *         fields, in the same order of the request.
	   */
	  public List<String> hmget(final String key, final String... fields) {
		  init();
		  if(shardedJedis != null)
				return shardedJedis.hmget(  key,  fields) ;
		  else if(this.jedis != null)
				return jedis.hmget(  key,  fields) ;
		  else
			  	return jc.hmget(  key,  fields) ;
	  }

	  /**
	   * Increment the number stored at field in the hash at key by value. If key does not exist, a new
	   * key holding a hash is created. If field does not exist or holds a string, the value is set to 0
	   * before applying the operation. Since the value argument is signed you can use this command to
	   * perform both increments and decrements.
	   * <p>
	   * The range of values supported by HINCRBY is limited to 64 bit signed integers.
	   * <p>
	   * <b>Time complexity:</b> O(1)
	   * @param key
	   * @param field
	   * @param value
	   * @return Integer reply The new value at field after the increment operation.
	   */
	  public Long hincrBy(final String key, final String field, final long value) {
		  init();
		  if(shardedJedis != null)
				return shardedJedis.hincrBy(  key,   field,   value);
		  else if(this.jedis != null)
				return jedis.hincrBy(  key,   field,   value);
		  else
			  	return jc.hincrBy(  key,   field,   value);
	  }

	  /**
	   * Increment the number stored at field in the hash at key by a double precision floating point
	   * value. If key does not exist, a new key holding a hash is created. If field does not exist or
	   * holds a string, the value is set to 0 before applying the operation. Since the value argument
	   * is signed you can use this command to perform both increments and decrements.
	   * <p>
	   * The range of values supported by HINCRBYFLOAT is limited to double precision floating point
	   * values.
	   * <p>
	   * <b>Time complexity:</b> O(1)
	   * @param key
	   * @param field
	   * @param value
	   * @return Double precision floating point reply The new value at field after the increment
	   *         operation.
	   */
	  public Double hincrByFloat(final String key, final String field, final double value) {
		  init();
		  if(shardedJedis != null)
				return shardedJedis. hincrByFloat(  key,   field,   value);
		  else if(this.jedis != null)
				return jedis. hincrByFloat(  key,   field,   value);
		  else
			  	return jc. hincrByFloat(  key.getBytes(),   field.getBytes(),   value);
	  }

	  /**
	   * Test for existence of a specified field in a hash. <b>Time complexity:</b> O(1)
	   * @param key
	   * @param field
	   * @return Return 1 if the hash stored at key contains the specified field. Return 0 if the key is
	   *         not found or the field is not present.
	   */
	  public Boolean hexists(final String key, final String field) {
		  init();
		  if(shardedJedis != null)
				return shardedJedis. hexists( key,  field) ;
		  else if(this.jedis != null)
				return jedis. hexists( key,  field) ;
		  else
			  	return jc. hexists( key,  field) ;
	  }

	  /**
	   * Remove the specified field from an hash stored at key.
	   * <p>
	   * <b>Time complexity:</b> O(1)
	   * @param key
	   * @param fields
	   * @return If the field was present in the hash it is deleted and 1 is returned, otherwise 0 is
	   *         returned and no operation is performed.
	   */
	  public Long hdel(final String key, final String... fields) {
		  init();
		  if(shardedJedis != null)
				return shardedJedis.hdel(  key,  fields);
		  else if(this.jedis != null)
				return jedis.hdel(  key,  fields);
		  else
			  	return jc.hdel(  key,  fields);
	  }

	  /**
	   * Return the number of items in a hash.
	   * <p>
	   * <b>Time complexity:</b> O(1)
	   * @param key
	   * @return The number of entries (fields) contained in the hash stored at key. If the specified
	   *         key does not exist, 0 is returned assuming an empty hash.
	   */
	  public Long hlen(final String key) {
		  init();
		  if(shardedJedis != null)
				return shardedJedis.hlen( key);
		  else if(this.jedis != null)
				return jedis.hlen( key);
		  else
			  	return jc.hlen( key);
	  }

	  /**
	   * Return all the fields in a hash.
	   * <p>
	   * <b>Time complexity:</b> O(N), where N is the total number of entries
	   * @param key
	   * @return All the fields names contained into a hash.
	   */
	  public Set<String> hkeys(final String key) {
		  init();
		  if(shardedJedis != null)
				return shardedJedis.hkeys(   key);
		  else if(this.jedis != null)
				return jedis.hkeys(   key);
		  else
			  	return jc.hkeys(   key);
	  }

	  /**
	   * Return all the values in a hash.
	   * <p>
	   * <b>Time complexity:</b> O(N), where N is the total number of entries
	   * @param key
	   * @return All the fields values contained into a hash.
	   */
	  public List<String> hvals(final String key) {
		  init();
		  if(shardedJedis != null)
				return shardedJedis.hvals(  key) ;
		  else if(this.jedis != null)
				return jedis.hvals(  key) ;
		  else
			  	return jc.hvals(  key) ;
	  }

	  /**
	   * Return all the fields and associated values in a hash.
	   * <p>
	   * <b>Time complexity:</b> O(N), where N is the total number of entries
	   * @param key
	   * @return All the fields and values contained into a hash.
	   */
	  public Map<String, String> hgetAll(final String key) {
		  init();
		  if(shardedJedis != null)
				return shardedJedis. hgetAll(  key);
		  else if(this.jedis != null)
				return jedis.hgetAll(  key);
		  else
			  	return jc.hgetAll(  key);
	  }

	  /**
	   * Add the string value to the head (LPUSH) or tail (RPUSH) of the list stored at key. If the key
	   * does not exist an empty list is created just before the append operation. If the key exists but
	   * is not a List an error is returned.
	   * <p>
	   * Time complexity: O(1)
	   * @param key
	   * @param strings
	   * @return Integer reply, specifically, the number of elements inside the list after the push
	   *         operation.
	   */
	 
	  public Long rpush(final String key, final String... strings) {
		  init();
		  if(shardedJedis != null)
				return shardedJedis.rpush(  key,  strings) ;
		  else if(this.jedis != null)
				return jedis.rpush(  key,  strings) ;
		  else
			  	return jc.rpush(  key,  strings) ;
	  }

	  /**
	   * Add the string value to the head (LPUSH) or tail (RPUSH) of the list stored at key. If the key
	   * does not exist an empty list is created just before the append operation. If the key exists but
	   * is not a List an error is returned.
	   * <p>
	   * Time complexity: O(1)
	   * @param key
	   * @param strings
	   * @return Integer reply, specifically, the number of elements inside the list after the push
	   *         operation.
	   */
	  public Long lpush(final String key, final String... strings) {
		  init();
		  if(shardedJedis != null)
				return shardedJedis.lpush(  key,   strings) ;
		  else if(this.jedis != null)
				return jedis.lpush(  key,   strings) ;
		  else
			  	return jc.lpush(  key,   strings) ;
	  }

	  /**
	   * Return the length of the list stored at the specified key. If the key does not exist zero is
	   * returned (the same behaviour as for empty lists). If the value stored at key is not a list an
	   * error is returned.
	   * <p>
	   * Time complexity: O(1)
	   * @param key
	   * @return The length of the list.
	   */
	  public Long llen(final String key) {
		  init();
		  if(shardedJedis != null)
				return shardedJedis.llen(  key);
		  else if(this.jedis != null)
				return jedis.llen(  key);
		  else
			  	return jc.llen(  key);
	  }

	  /**
	   * Return the specified elements of the list stored at the specified key. Start and end are
	   * zero-based indexes. 0 is the first element of the list (the list head), 1 the next element and
	   * so on.
	   * <p>
	   * For example LRANGE foobar 0 2 will return the first three elements of the list.
	   * <p>
	   * start and end can also be negative numbers indicating offsets from the end of the list. For
	   * example -1 is the last element of the list, -2 the penultimate element and so on.
	   * <p>
	   * <b>Consistency with range functions in various programming languages</b>
	   * <p>
	   * Note that if you have a list of numbers from 0 to 100, LRANGE 0 10 will return 11 elements,
	   * that is, rightmost item is included. This may or may not be consistent with behavior of
	   * range-related functions in your programming language of choice (think Ruby's Range.new,
	   * Array#slice or Python's range() function).
	   * <p>
	   * LRANGE behavior is consistent with one of Tcl.
	   * <p>
	   * <b>Out-of-range indexes</b>
	   * <p>
	   * Indexes out of range will not produce an error: if start is over the end of the list, or start
	   * &gt; end, an empty list is returned. If end is over the end of the list Redis will threat it
	   * just like the last element of the list.
	   * <p>
	   * Time complexity: O(start+n) (with n being the length of the range and start being the start
	   * offset)
	   * @param key
	   * @param start
	   * @param end
	   * @return Multi bulk reply, specifically a list of elements in the specified range.
	   */
	  public List<String> lrange(final String key, final long start, final long end) {
		  init();
		  if(shardedJedis != null)
				return shardedJedis.lrange(  key,  start,   end);
		  else if(this.jedis != null)
				return jedis.lrange(  key,  start,   end);
		  else
			  	return jc.lrange(  key,  start,   end);
	  }

	  /**
	   * Trim an existing list so that it will contain only the specified range of elements specified.
	   * Start and end are zero-based indexes. 0 is the first element of the list (the list head), 1 the
	   * next element and so on.
	   * <p>
	   * For example LTRIM foobar 0 2 will modify the list stored at foobar key so that only the first
	   * three elements of the list will remain.
	   * <p>
	   * start and end can also be negative numbers indicating offsets from the end of the list. For
	   * example -1 is the last element of the list, -2 the penultimate element and so on.
	   * <p>
	   * Indexes out of range will not produce an error: if start is over the end of the list, or start
	   * &gt; end, an empty list is left as value. If end over the end of the list Redis will threat it
	   * just like the last element of the list.
	   * <p>
	   * Hint: the obvious use of LTRIM is together with LPUSH/RPUSH. For example:
	   * <p>
	   * {@code lpush("mylist", "someelement"); ltrim("mylist", 0, 99); * }
	   * <p>
	   * The above two commands will push elements in the list taking care that the list will not grow
	   * without limits. This is very useful when using Redis to store logs for example. It is important
	   * to note that when used in this way LTRIM is an O(1) operation because in the average case just
	   * one element is removed from the tail of the list.
	   * <p>
	   * Time complexity: O(n) (with n being len of list - len of range)
	   * @param key
	   * @param start
	   * @param end
	   * @return Status code reply
	   */
	  public String ltrim(final String key, final long start, final long end) {
		  init();
		  if(shardedJedis != null)
				return shardedJedis.ltrim( key,  start,   end);
		  else if(this.jedis != null)
				return jedis.ltrim( key,  start,   end);
		  else
			  	return jc.ltrim( key,  start,   end);
	  }

	  /**
	   * Return the specified element of the list stored at the specified key. 0 is the first element, 1
	   * the second and so on. Negative indexes are supported, for example -1 is the last element, -2
	   * the penultimate and so on.
	   * <p>
	   * If the value stored at key is not of list type an error is returned. If the index is out of
	   * range a 'nil' reply is returned.
	   * <p>
	   * Note that even if the average time complexity is O(n) asking for the first or the last element
	   * of the list is O(1).
	   * <p>
	   * Time complexity: O(n) (with n being the length of the list)
	   * @param key
	   * @param index
	   * @return Bulk reply, specifically the requested element
	   */
	  public String lindex(final String key, final long index) {
		  init();
		  if(shardedJedis != null)
				return shardedJedis.lindex(  key,   index);
		  else if(this.jedis != null)
				return jedis.lindex(  key,   index);
		  else
			  	return jc.lindex(  key,   index);
	  }

	  /**
	   * Set a new value as the element at index position of the List at key.
	   * <p>
	   * Out of range indexes will generate an error.
	   * <p>
	   * Similarly to other list commands accepting indexes, the index can be negative to access
	   * elements starting from the end of the list. So -1 is the last element, -2 is the penultimate,
	   * and so forth.
	   * <p>
	   * <b>Time complexity:</b>
	   * <p>
	   * O(N) (with N being the length of the list), setting the first or last elements of the list is
	   * O(1).
	   * @see #lindex(String, long)
	   * @param key
	   * @param index
	   * @param value
	   * @return Status code reply
	   */
	  public String lset(final String key, final long index, final String value) {
		  init();
		  if(shardedJedis != null)
				return shardedJedis.lset(  key,   index,   value);
		  else if(this.jedis != null)
				return jedis.lset(  key,   index,   value);
		  else
			  	return jc.lset(  key,   index,   value);
	  }

	  /**
	   * Remove the first count occurrences of the value element from the list. If count is zero all the
	   * elements are removed. If count is negative elements are removed from tail to head, instead to
	   * go from head to tail that is the normal behaviour. So for example LREM with count -2 and hello
	   * as value to remove against the list (a,b,c,hello,x,hello,hello) will lave the list
	   * (a,b,c,hello,x). The number of removed elements is returned as an integer, see below for more
	   * information about the returned value. Note that non existing keys are considered like empty
	   * lists by LREM, so LREM against non existing keys will always return 0.
	   * <p>
	   * Time complexity: O(N) (with N being the length of the list)
	   * @param key
	   * @param count
	   * @param value
	   * @return Integer Reply, specifically: The number of removed elements if the operation succeeded
	   */
	  public Long lrem(final String key, final long count, final String value) {
		  init();
		  if(shardedJedis != null)
				return shardedJedis.lrem( key,   count,   value);
		  else if(this.jedis != null)
				return jedis.lrem( key,   count,   value);
		  else
			  	return jc.lrem( key,   count,   value);
	  }

	  /**
	   * Atomically return and remove the first (LPOP) or last (RPOP) element of the list. For example
	   * if the list contains the elements "a","b","c" LPOP will return "a" and the list will become
	   * "b","c".
	   * <p>
	   * If the key does not exist or the list is already empty the special value 'nil' is returned.
	   * @see #rpop(String)
	   * @param key
	   * @return Bulk reply
	   */
	  public String lpop(final String key) {
		  init();
		  if(shardedJedis != null)
				return shardedJedis.lpop(  key);
		  else if(this.jedis != null)
				return jedis.lpop(  key);
		  else
			  	return jc.lpop(  key);
	  }

	  /**
	   * Atomically return and remove the first (LPOP) or last (RPOP) element of the list. For example
	   * if the list contains the elements "a","b","c" RPOP will return "c" and the list will become
	   * "a","b".
	   * <p>
	   * If the key does not exist or the list is already empty the special value 'nil' is returned.
	   * @see #lpop(String)
	   * @param key
	   * @return Bulk reply
	   */
	  public String rpop(final String key) {
		  init();
		  if(shardedJedis != null)
				return shardedJedis.rpop(  key);
		  else if(this.jedis != null)
				return jedis.rpop(  key);
		  else
			  	return jc.rpop(  key);
	  }

	  /**
	   * Atomically return and remove the last (tail) element of the srckey list, and push the element
	   * as the first (head) element of the dstkey list. For example if the source list contains the
	   * elements "a","b","c" and the destination list contains the elements "foo","bar" after an
	   * RPOPLPUSH command the content of the two lists will be "a","b" and "c","foo","bar".
	   * <p>
	   * If the key does not exist or the list is already empty the special value 'nil' is returned. If
	   * the srckey and dstkey are the same the operation is equivalent to removing the last element
	   * from the list and pusing it as first element of the list, so it's a "list rotation" command.
	   * <p>
	   * Time complexity: O(1)
	   * @param srckey
	   * @param dstkey
	   * @return Bulk reply
	   */
	  public String rpoplpush(final String srckey, final String dstkey) {
		  init();
		  if(shardedJedis != null)
			  throw new java.lang.UnsupportedOperationException("sharded Jedis Unsupport   rpoplpush mehtod.");
		  else if(this.jedis != null)
				return jedis.rpoplpush(  srckey,   dstkey);
		  else
			  	return jc.rpoplpush(  srckey,   dstkey);
	  }

	  /**
	   * Add the specified member to the set value stored at key. If member is already a member of the
	   * set no operation is performed. If key does not exist a new set with the specified member as
	   * sole member is created. If the key exists but does not hold a set value an error is returned.
	   * <p>
	   * Time complexity O(1)
	   * @param key
	   * @param members
	   * @return Integer reply, specifically: 1 if the new element was added 0 if the element was
	   *         already a member of the set
	   */
	  public Long sadd(final String key, final String... members) {
		  init();
		  if(shardedJedis != null)
				return shardedJedis.sadd(  key,   members);
		  else if(this.jedis != null)
				return jedis.sadd(  key,   members);
		  else
			  	return jc.sadd(  key,   members);
	  }

	  /**
	   * Return all the members (elements) of the set value stored at key. This is just syntax glue for
	   * {@link #sinter(String...) SINTER}.
	   * <p>
	   * Time complexity O(N)
	   * @param key
	   * @return Multi bulk reply
	   */
	  public Set<String> smembers(final String key) {
		  init();
		  if(shardedJedis != null)
				return shardedJedis.smembers(  key);
		  else if(this.jedis != null)
				return jedis.smembers(  key);
		  else
			  	return jc.smembers(  key);
	  }

	  /**
	   * Remove the specified member from the set value stored at key. If member was not a member of the
	   * set no operation is performed. If key does not hold a set value an error is returned.
	   * <p>
	   * Time complexity O(1)
	   * @param key
	   * @param members
	   * @return Integer reply, specifically: 1 if the new element was removed 0 if the new element was
	   *         not a member of the set
	   */
	  public Long srem(final String key, final String... members) {
		  init();
		  if(shardedJedis != null)
				return shardedJedis.srem(  key,   members) ;
		  else if(this.jedis != null)
				return jedis.srem(  key,   members) ;
		  else
			  	return jc.srem(  key,   members) ;
	  }

	  /**
	   * Remove a random element from a Set returning it as return value. If the Set is empty or the key
	   * does not exist, a nil object is returned.
	   * <p>
	   * The {@link #srandmember(String)} command does a similar work but the returned element is not
	   * removed from the Set.
	   * <p>
	   * Time complexity O(1)
	   * @param key
	   * @return Bulk reply
	   */
	  public String spop(final String key) {
		  init();
		  if(shardedJedis != null)
				return shardedJedis.spop(  key);
		  else if(this.jedis != null)
				return jedis.spop(  key);
		  else
			  	return jc.spop(  key);
	  }

	  public Set<String> spop(final String key, final long count) {
		  init();
		  if(shardedJedis != null)
				return shardedJedis.spop(  key,   count);
		  else if(this.jedis != null)
				return jedis.spop(  key,   count);
		  else
			  	return jc.spop(  key,   count);
	  }

	  /**
	   * Move the specifided member from the set at srckey to the set at dstkey. This operation is
	   * atomic, in every given moment the element will appear to be in the source or destination set
	   * for accessing clients.
	   * <p>
	   * If the source set does not exist or does not contain the specified element no operation is
	   * performed and zero is returned, otherwise the element is removed from the source set and added
	   * to the destination set. On success one is returned, even if the element was already present in
	   * the destination set.
	   * <p>
	   * An error is raised if the source or destination keys contain a non Set value.
	   * <p>
	   * Time complexity O(1)
	   * @param srckey
	   * @param dstkey
	   * @param member
	   * @return Integer reply, specifically: 1 if the element was moved 0 if the element was not found
	   *         on the first set and no operation was performed
	   */
	  public Long smove(final String srckey, final String dstkey, final String member) {
		  init();
		  if(shardedJedis != null)
			  throw new java.lang.UnsupportedOperationException("sharded Jedis Unsupport   smove mehtod.");
		  else if(this.jedis != null)
				return jedis.smove(  srckey,   dstkey,   member);
		  else
			  	return jc.smove(  srckey,   dstkey,   member);
	  }

	  /**
	   * Return the set cardinality (number of elements). If the key does not exist 0 is returned, like
	   * for empty sets.
	   * @param key
	   * @return Integer reply, specifically: the cardinality (number of elements) of the set as an
	   *         integer.
	   */
	  public Long scard(final String key) {
		  init();
		  if(shardedJedis != null)
				return shardedJedis.scard(  key) ;
		  else if(this.jedis != null)
				return jedis.scard(  key) ;
		  else
			  	return jc.scard(  key) ;
	  }

	  /**
	   * Return 1 if member is a member of the set stored at key, otherwise 0 is returned.
	   * <p>
	   * Time complexity O(1)
	   * @param key
	   * @param member
	   * @return Integer reply, specifically: 1 if the element is a member of the set 0 if the element
	   *         is not a member of the set OR if the key does not exist
	   */
	  public Boolean sismember(final String key, final String member) {
		  init();
		  if(shardedJedis != null)
				return shardedJedis.sismember(  key,   member);
		  else if(this.jedis != null)
				return jedis.sismember(  key,   member);
		  else
			  	return jc.sismember(  key,   member);
	  }

	  /**
	   * Return the members of a set resulting from the intersection of all the sets hold at the
	   * specified keys. Like in {@link #lrange(String, long, long) LRANGE} the result is sent to the
	   * client as a multi-bulk reply (see the protocol specification for more information). If just a
	   * single key is specified, then this command produces the same result as
	   * {@link #smembers(String) SMEMBERS}. Actually SMEMBERS is just syntax sugar for SINTER.
	   * <p>
	   * Non existing keys are considered like empty sets, so if one of the keys is missing an empty set
	   * is returned (since the intersection with an empty set always is an empty set).
	   * <p>
	   * Time complexity O(N*M) worst case where N is the cardinality of the smallest set and M the
	   * number of sets
	   * @param keys
	   * @return Multi bulk reply, specifically the list of common elements.
	   */
	  public Set<String> sinter(final String... keys) {
		  init();
		  if(shardedJedis != null)
			  throw new java.lang.UnsupportedOperationException("sharded Jedis Unsupport sinter mehtod.");
		  else if(this.jedis != null)
				return jedis.sinter(  keys);
		  else
			  	return jc.sinter(  keys);
	  }

	  /**
	   * This commnad works exactly like {@link #sinter(String...) SINTER} but instead of being returned
	   * the resulting set is sotred as dstkey.
	   * <p>
	   * Time complexity O(N*M) worst case where N is the cardinality of the smallest set and M the
	   * number of sets
	   * @param dstkey
	   * @param keys
	   * @return Status code reply
	   */
	  public Long sinterstore(final String dstkey, final String... keys) {
		  init();
		  if(shardedJedis != null)
			  throw new java.lang.UnsupportedOperationException("sharded Jedis Unsupport sinterstore mehtod.");
		  else if(this.jedis != null)
				return jedis.sinterstore( dstkey,   keys);
		  else
			  	return jc.sinterstore( dstkey,   keys);
	  }

	  /**
	   * Return the members of a set resulting from the union of all the sets hold at the specified
	   * keys. Like in {@link #lrange(String, long, long) LRANGE} the result is sent to the client as a
	   * multi-bulk reply (see the protocol specification for more information). If just a single key is
	   * specified, then this command produces the same result as {@link #smembers(String) SMEMBERS}.
	   * <p>
	   * Non existing keys are considered like empty sets.
	   * <p>
	   * Time complexity O(N) where N is the total number of elements in all the provided sets
	   * @param keys
	   * @return Multi bulk reply, specifically the list of common elements.
	   */
	 
	  public Set<String> sunion(final String... keys) {
		  init();
		  if(shardedJedis != null)
			  throw new java.lang.UnsupportedOperationException("sharded Jedis Unsupport sunion mehtod.");
		  else if(this.jedis != null)
				return jedis.sunion(  keys);
		  else
			  	return jc.sunion(  keys);
	  }

	  /**
	   * This command works exactly like {@link #sunion(String...) SUNION} but instead of being returned
	   * the resulting set is stored as dstkey. Any existing value in dstkey will be over-written.
	   * <p>
	   * Time complexity O(N) where N is the total number of elements in all the provided sets
	   * @param dstkey
	   * @param keys
	   * @return Status code reply
	   */
	  
	  public Long sunionstore(final String dstkey, final String... keys) {
		  init();
		  if(shardedJedis != null)
			  throw new java.lang.UnsupportedOperationException("sharded Jedis Unsupport sunionstore mehtod.");
		  else if(this.jedis != null)
				return jedis.sunionstore( dstkey,   keys);
		  else
			  	return jc.sunionstore(  dstkey,   keys);
	  }

	  /**
	   * Return the difference between the Set stored at key1 and all the Sets key2, ..., keyN
	   * <p>
	   * <b>Example:</b>
	   * 
	   * <pre>
	   * key1 = [x, a, b, c]
	   * key2 = [c]
	   * key3 = [a, d]
	   * SDIFF key1,key2,key3 =&gt; [x, b]
	   * </pre>
	   * 
	   * Non existing keys are considered like empty sets.
	   * <p>
	   * <b>Time complexity:</b>
	   * <p>
	   * O(N) with N being the total number of elements of all the sets
	   * @param keys
	   * @return Return the members of a set resulting from the difference between the first set
	   *         provided and all the successive sets.
	   */
	  
	  public Set<String> sdiff(final String... keys) {
		  init();
		  if(shardedJedis != null)
			  throw new java.lang.UnsupportedOperationException("sharded Jedis Unsupport sdiff mehtod.");
		  else if(this.jedis != null)
				return jedis.sdiff(  keys) ;
		  else
			  	return jc.sdiff(  keys) ;
	  }

	  /**
	   * This command works exactly like {@link #sdiff(String...) SDIFF} but instead of being returned
	   * the resulting set is stored in dstkey.
	   * @param dstkey
	   * @param keys
	   * @return Status code reply
	   */
	  
	  public Long sdiffstore(final String dstkey, final String... keys) {
		  init();
		  if(shardedJedis != null)
			  throw new java.lang.UnsupportedOperationException("sharded Jedis Unsupport sdiffstore mehtod.");
		  else if(this.jedis != null)
				return jedis.sdiffstore( dstkey,   keys);
		  else
			  	return jc.sdiffstore( dstkey,  keys);
	  }

	  /**
	   * Return a random element from a Set, without removing the element. If the Set is empty or the
	   * key does not exist, a nil object is returned.
	   * <p>
	   * The SPOP command does a similar work but the returned element is popped (removed) from the Set.
	   * <p>
	   * Time complexity O(1)
	   * @param key
	   * @return Bulk reply
	   */
	  public String srandmember(final String key) {
		  init();
		  if(shardedJedis != null)
				return shardedJedis.srandmember(  key) ;
		  else if(this.jedis != null)
				return jedis.srandmember(  key) ;
		  else
			  	return jc.srandmember(  key) ;
	  }

	  public List<String> srandmember(final String key, final int count) {
		  init();
		  if(shardedJedis != null)
				return shardedJedis.srandmember(  key,   count);
		  else if(this.jedis != null)
				return jedis.srandmember(  key,   count);
		  else
			  	return jc.srandmember(  key,   count);
	  }

	  /**
	   * Add the specified member having the specifeid score to the sorted set stored at key. If member
	   * is already a member of the sorted set the score is updated, and the element reinserted in the
	   * right position to ensure sorting. If key does not exist a new sorted set with the specified
	   * member as sole member is crated. If the key exists but does not hold a sorted set value an
	   * error is returned.
	   * <p>
	   * The score value can be the string representation of a double precision floating point number.
	   * <p>
	   * Time complexity O(log(N)) with N being the number of elements in the sorted set
	   * @param key
	   * @param score
	   * @param member
	   * @return Integer reply, specifically: 1 if the new element was added 0 if the element was
	   *         already a member of the sorted set and the score was updated
	   */
	  public Long zadd(final String key, final double score, final String member) {
		  init();
		  if(shardedJedis != null)
				return shardedJedis.zadd(  key,   score,  member);
		  else if(this.jedis != null)
				return jedis.zadd(  key,   score,  member);
		  else
			  	return jc.zadd(  key,   score,  member);
	  }

	 
	  public Long zadd(final String key, final double score, final String member,
	      final ZAddParams params) {
		  init();
		  if(shardedJedis != null)
				return shardedJedis.zadd(  key,  score,  member,
					        params);
		  else if(this.jedis != null)
				return jedis.zadd(  key,  score,  member,
				        params);
		  else
			  	return jc.zadd(  key,  score,  member,
				        params);
	  }

	  public Long zadd(final String key, final Map<String, Double> scoreMembers) {
		  init();
		  if(shardedJedis != null)
				return shardedJedis.zadd( key,  scoreMembers) ;
		  else if(this.jedis != null)
				return jedis.zadd( key,  scoreMembers) ;
		  else
			  	return jc.zadd( key,  scoreMembers) ;
	  }

	  public Long zadd(String key, Map<String, Double> scoreMembers, ZAddParams params) {
		  init();
		  if(shardedJedis != null)
				return shardedJedis.zadd(  key,   scoreMembers,   params);
		  else if(this.jedis != null)
				return jedis.zadd(  key,   scoreMembers,   params);
		  else
			  	return jc.zadd(  key,   scoreMembers,   params);
	  }

	  public Set<String> zrange(final String key, final long start, final long end) {
		  init();
		  if(shardedJedis != null)
				return shardedJedis.zrange(  key,   start,   end);
		  else if(this.jedis != null)
				return jedis.zrange(  key,   start,   end);
		  else
			  	return jc.zrange(  key,   start,   end);
	  }

	  /**
	   * Remove the specified member from the sorted set value stored at key. If member was not a member
	   * of the set no operation is performed. If key does not not hold a set value an error is
	   * returned.
	   * <p>
	   * Time complexity O(log(N)) with N being the number of elements in the sorted set
	   * @param key
	   * @param members
	   * @return Integer reply, specifically: 1 if the new element was removed 0 if the new element was
	   *         not a member of the set
	   */
	  public Long zrem(final String key, final String... members) {
		  init();
		  if(shardedJedis != null)
				return shardedJedis.zrem(  key,   members);
		  else if(this.jedis != null)
				return jedis.zrem(  key,   members);
		  else
			  	return jc.zrem(  key,   members);
	  }

	  /**
	   * If member already exists in the sorted set adds the increment to its score and updates the
	   * position of the element in the sorted set accordingly. If member does not already exist in the
	   * sorted set it is added with increment as score (that is, like if the previous score was
	   * virtually zero). If key does not exist a new sorted set with the specified member as sole
	   * member is crated. If the key exists but does not hold a sorted set value an error is returned.
	   * <p>
	   * The score value can be the string representation of a double precision floating point number.
	   * It's possible to provide a negative value to perform a decrement.
	   * <p>
	   * For an introduction to sorted sets check the Introduction to Redis data types page.
	   * <p>
	   * Time complexity O(log(N)) with N being the number of elements in the sorted set
	   * @param key
	   * @param score
	   * @param member
	   * @return The new score
	   */
	  public Double zincrby(final String key, final double score, final String member) {
		  init();
		  if(shardedJedis != null)
				return shardedJedis.zincrby(  key,   score,  member);
		  else if(this.jedis != null)
				return jedis.zincrby(  key,   score,  member);
		  else
			  	return jc.zincrby(  key,   score,  member);
	  }

	  public Double zincrby(String key, double score, String member, ZIncrByParams params) {
		  init();
		  if(shardedJedis != null)
				return shardedJedis.zincrby(  key,   score,   member,   params);
		  else if(this.jedis != null)
				return jedis.zincrby(  key,   score,   member,   params);
		  else
			  	return jc.zincrby(  key,   score,   member,   params);
	  }

	  /**
	   * Return the rank (or index) or member in the sorted set at key, with scores being ordered from
	   * low to high.
	   * <p>
	   * When the given member does not exist in the sorted set, the special value 'nil' is returned.
	   * The returned rank (or index) of the member is 0-based for both commands.
	   * <p>
	   * <b>Time complexity:</b>
	   * <p>
	   * O(log(N))
	   * @param key
	   * @param member
	   * @return Integer reply or a nil bulk reply, specifically: the rank of the element as an integer
	   *         reply if the element exists. A nil bulk reply if there is no such element.
	   */
	  public Long zrank(final String key, final String member) {
		  init();
		  if(shardedJedis != null)
				return shardedJedis.zrank(  key,   member);
		  else if(this.jedis != null)
				return jedis.zrank(  key,   member);
		  else
			  	return jc.zrank(  key,   member);
	  }

//	  /**
//	   * Return the rank (or index) or member in the sorted set at key, with scores being ordered from
//	   * high to low.
//	   * <p>
//	   * When the given member does not exist in the sorted set, the special value 'nil' is returned.
//	   * The returned rank (or index) of the member is 0-based for both commands.
//	   * <p>
//	   * <b>Time complexity:</b>
//	   * <p>
//	   * O(log(N))
//	   * @see #zrank(String, String)
//	   * @param key
//	   * @param member
//	   * @return Integer reply or a nil bulk reply, specifically: the rank of the element as an integer
//	   *         reply if the element exists. A nil bulk reply if there is no such element.
//	   */
//	  @Override
//	  public Long zrevrank(final String key, final String member) {
//		  if(shardedJedis != null)
//				return shardedJedis.set(key, value,params);
//		  else if(this.jedis != null)
//				return jedis.set(key, value,params);
//		  else
//			  	return jc.set(key, value,params);
//	  }
//
//	  @Override
//	  public Set<String> zrevrange(final String key, final long start, final long end) {
//		  if(shardedJedis != null)
//				return shardedJedis.set(key, value,params);
//		  else if(this.jedis != null)
//				return jedis.set(key, value,params);
//		  else
//			  	return jc.set(key, value,params);
//	  }
//
//	  @Override
//	  public Set<Tuple> zrangeWithScores(final String key, final long start, final long end) {
//		  if(shardedJedis != null)
//				return shardedJedis.set(key, value,params);
//		  else if(this.jedis != null)
//				return jedis.set(key, value,params);
//		  else
//			  	return jc.set(key, value,params);
//	  }
//
//	  @Override
//	  public Set<Tuple> zrevrangeWithScores(final String key, final long start, final long end) {
//		  if(shardedJedis != null)
//				return shardedJedis.set(key, value,params);
//		  else if(this.jedis != null)
//				return jedis.set(key, value,params);
//		  else
//			  	return jc.set(key, value,params);
//	  }
//
	  /**
	   * Return the sorted set cardinality (number of elements). If the key does not exist 0 is
	   * returned, like for empty sorted sets.
	   * <p>
	   * Time complexity O(1)
	   * @param key
	   * @return the cardinality (number of elements) of the set as an integer.
	   */
	  
	  public Long zcard(final String key) {
		  init();
		  if(shardedJedis != null)
				return shardedJedis.zcard(  key);
		  else if(this.jedis != null)
				return jedis.zcard(  key);
		  else
			  	return jc.zcard(  key);
	  }

	  /**
	   * Return the score of the specified element of the sorted set at key. If the specified element
	   * does not exist in the sorted set, or the key does not exist at all, a special 'nil' value is
	   * returned.
	   * <p>
	   * <b>Time complexity:</b> O(1)
	   * @param key
	   * @param member
	   * @return the score
	   */
	  public Double zscore(final String key, final String member) {
		  init();
		  if(shardedJedis != null)
				return shardedJedis.zscore(  key,   member);
		  else if(this.jedis != null)
				return jedis.zscore(  key,   member);
		  else
			  	return jc.zscore(  key,   member);
	  }
//
//	  @Override
	  public String watch(final String... keys) {
		  init();
		  if(shardedJedis != null)
		  {
			  throw new java.lang.UnsupportedOperationException("sharded Jedis Unsupport watch mehtod.");
		  }
		  else if(this.jedis != null)
				return jedis.watch(  keys) ;
		  else
			  throw new java.lang.UnsupportedOperationException("Cluster Jedis Unsupport watch mehtod.");
	  }

	  /**
	   * Sort a Set or a List.
	   * <p>
	   * Sort the elements contained in the List, Set, or Sorted Set value at key. By default sorting is
	   * numeric with elements being compared as double precision floating point numbers. This is the
	   * simplest form of SORT.
	   * @see #sort(String, String)
	   * @see #sort(String, SortingParams)
	   * @see #sort(String, SortingParams, String)
	   * @param key
	   * @return Assuming the Set/List at key contains a list of numbers, the return value will be the
	   *         list of numbers ordered from the smallest to the biggest number.
	   */
	   
	  public List<String> sort(final String key) {
		  init();
		  if(shardedJedis != null)
				return shardedJedis. sort(  key) ;
		  else if(this.jedis != null)
				return jedis.sort(  key) ;
		  else
			  	return jc.sort(  key) ;
	  }

	  /**
	   * Sort a Set or a List accordingly to the specified parameters.
	   * <p>
	   * <b>examples:</b>
	   * <p>
	   * Given are the following sets and key/values:
	   * 
	   * <pre>
	   * x = [1, 2, 3]
	   * y = [a, b, c]
	   * 
	   * k1 = z
	   * k2 = y
	   * k3 = x
	   * 
	   * w1 = 9
	   * w2 = 8
	   * w3 = 7
	   * </pre>
	   * 
	   * Sort Order:
	   * 
	   * <pre>
	   * sort(x) or sort(x, sp.asc())
	   * -&gt; [1, 2, 3]
	   * 
	   * sort(x, sp.desc())
	   * -&gt; [3, 2, 1]
	   * 
	   * sort(y)
	   * -&gt; [c, a, b]
	   * 
	   * sort(y, sp.alpha())
	   * -&gt; [a, b, c]
	   * 
	   * sort(y, sp.alpha().desc())
	   * -&gt; [c, a, b]
	   * </pre>
	   * 
	   * Limit (e.g. for Pagination):
	   * 
	   * <pre>
	   * sort(x, sp.limit(0, 2))
	   * -&gt; [1, 2]
	   * 
	   * sort(y, sp.alpha().desc().limit(1, 2))
	   * -&gt; [b, a]
	   * </pre>
	   * 
	   * Sorting by external keys:
	   * 
	   * <pre>
	   * sort(x, sb.by(w*))
	   * -&gt; [3, 2, 1]
	   * 
	   * sort(x, sb.by(w*).desc())
	   * -&gt; [1, 2, 3]
	   * </pre>
	   * 
	   * Getting external keys:
	   * 
	   * <pre>
	   * sort(x, sp.by(w*).get(k*))
	   * -&gt; [x, y, z]
	   * 
	   * sort(x, sp.by(w*).get(#).get(k*))
	   * -&gt; [3, x, 2, y, 1, z]
	   * </pre>
	   * @see #sort(String)
	   * @see #sort(String, SortingParams, String)
	   * @param key
	   * @param sortingParameters
	   * @return a list of sorted elements.
	   */
	 
	  public List<String> sort(final String key, final SortingParams sortingParameters) {
		  init();
		  if(shardedJedis != null)
				return shardedJedis.sort(  key,   sortingParameters);
		  else if(this.jedis != null)
				return jedis.sort(  key,   sortingParameters);
		  else
			  	return jc.sort(  key,   sortingParameters);
	  }

//	  /**
//	   * BLPOP (and BRPOP) is a blocking list pop primitive. You can see this commands as blocking
//	   * versions of LPOP and RPOP able to block if the specified keys don't exist or contain empty
//	   * lists.
//	   * <p>
//	   * The following is a description of the exact semantic. We describe BLPOP but the two commands
//	   * are identical, the only difference is that BLPOP pops the element from the left (head) of the
//	   * list, and BRPOP pops from the right (tail).
//	   * <p>
//	   * <b>Non blocking behavior</b>
//	   * <p>
//	   * When BLPOP is called, if at least one of the specified keys contain a non empty list, an
//	   * element is popped from the head of the list and returned to the caller together with the name
//	   * of the key (BLPOP returns a two elements array, the first element is the key, the second the
//	   * popped value).
//	   * <p>
//	   * Keys are scanned from left to right, so for instance if you issue BLPOP list1 list2 list3 0
//	   * against a dataset where list1 does not exist but list2 and list3 contain non empty lists, BLPOP
//	   * guarantees to return an element from the list stored at list2 (since it is the first non empty
//	   * list starting from the left).
//	   * <p>
//	   * <b>Blocking behavior</b>
//	   * <p>
//	   * If none of the specified keys exist or contain non empty lists, BLPOP blocks until some other
//	   * client performs a LPUSH or an RPUSH operation against one of the lists.
//	   * <p>
//	   * Once new data is present on one of the lists, the client finally returns with the name of the
//	   * key unblocking it and the popped value.
//	   * <p>
//	   * When blocking, if a non-zero timeout is specified, the client will unblock returning a nil
//	   * special value if the specified amount of seconds passed without a push operation against at
//	   * least one of the specified keys.
//	   * <p>
//	   * The timeout argument is interpreted as an integer value. A timeout of zero means instead to
//	   * block forever.
//	   * <p>
//	   * <b>Multiple clients blocking for the same keys</b>
//	   * <p>
//	   * Multiple clients can block for the same key. They are put into a queue, so the first to be
//	   * served will be the one that started to wait earlier, in a first-blpopping first-served fashion.
//	   * <p>
//	   * <b>blocking POP inside a MULTI/EXEC transaction</b>
//	   * <p>
//	   * BLPOP and BRPOP can be used with pipelining (sending multiple commands and reading the replies
//	   * in batch), but it does not make sense to use BLPOP or BRPOP inside a MULTI/EXEC block (a Redis
//	   * transaction).
//	   * <p>
//	   * The behavior of BLPOP inside MULTI/EXEC when the list is empty is to return a multi-bulk nil
//	   * reply, exactly what happens when the timeout is reached. If you like science fiction, think at
//	   * it like if inside MULTI/EXEC the time will flow at infinite speed :)
//	   * <p>
//	   * Time complexity: O(1)
//	   * @see #brpop(int, String...)
//	   * @param timeout
//	   * @param keys
//	   * @return BLPOP returns a two-elements array via a multi bulk reply in order to return both the
//	   *         unblocking key and the popped value.
//	   *         <p>
//	   *         When a non-zero timeout is specified, and the BLPOP operation timed out, the return
//	   *         value is a nil multi bulk reply. Most client values will return false or nil
//	   *         accordingly to the programming language used.
//	   */
//	 
//	  public List<String> blpop(final int timeout, final String... keys) {
//	    return blpop(getArgsAddTimeout(timeout, keys));
//	  }
//
//	  
//
//	 
//	  public List<String> blpop(String... args) {
//		  if(shardedJedis != null)
//				return shardedJedis.set(key, value,params);
//		  else if(this.jedis != null)
//				return jedis.set(key, value,params);
//		  else
//			  	return jc.set(key, value,params);
//	  }
//
//	  @Override
//	  public List<String> brpop(String... args) {
//		  if(shardedJedis != null)
//				return shardedJedis.set(key, value,params);
//		  else if(this.jedis != null)
//				return jedis.set(key, value,params);
//		  else
//			  	return jc.set(key, value,params);
//	  }

	  /**
	   * Sort a Set or a List accordingly to the specified parameters and store the result at dstkey.
	   * @see #sort(String, SortingParams)
	   * @see #sort(String)
	   * @see #sort(String, String)
	   * @param key
	   * @param sortingParameters
	   * @param dstkey
	   * @return The number of elements of the list at dstkey.
	   */
	  
	  public Long sort(final String key, final SortingParams sortingParameters, final String dstkey) {
		  init();
		  if(shardedJedis != null)
			  throw new java.lang.UnsupportedOperationException("sharded Jedis Unsupport sort(final String key, final SortingParams sortingParameters, final String dstkey) mehtod.");
		  else if(this.jedis != null)
				return jedis.sort(  key,   sortingParameters,  dstkey);
		  else
			  	return jc.sort(  key,   sortingParameters,  dstkey);
	  }

	  /**
	   * Sort a Set or a List and Store the Result at dstkey.
	   * <p>
	   * Sort the elements contained in the List, Set, or Sorted Set value at key and store the result
	   * at dstkey. By default sorting is numeric with elements being compared as double precision
	   * floating point numbers. This is the simplest form of SORT.
	   * @see #sort(String)
	   * @see #sort(String, SortingParams)
	   * @see #sort(String, SortingParams, String)
	   * @param key
	   * @param dstkey
	   * @return The number of elements of the list at dstkey.
	   */
	 
	  public Long sort(final String key, final String dstkey) {
		  init();
		  if(shardedJedis != null)
			  throw new java.lang.UnsupportedOperationException("sharded Jedis Unsupport sort(final String key, final String dstkey)  mehtod.");
		  else if(this.jedis != null)
				return jedis.sort(  key,   dstkey) ;
		  else
			  	return jc.sort(  key,   dstkey) ;
	  }

	  /**
	   * BLPOP (and BRPOP) is a blocking list pop primitive. You can see this commands as blocking
	   * versions of LPOP and RPOP able to block if the specified keys don't exist or contain empty
	   * lists.
	   * <p>
	   * The following is a description of the exact semantic. We describe BLPOP but the two commands
	   * are identical, the only difference is that BLPOP pops the element from the left (head) of the
	   * list, and BRPOP pops from the right (tail).
	   * <p>
	   * <b>Non blocking behavior</b>
	   * <p>
	   * When BLPOP is called, if at least one of the specified keys contain a non empty list, an
	   * element is popped from the head of the list and returned to the caller together with the name
	   * of the key (BLPOP returns a two elements array, the first element is the key, the second the
	   * popped value).
	   * <p>
	   * Keys are scanned from left to right, so for instance if you issue BLPOP list1 list2 list3 0
	   * against a dataset where list1 does not exist but list2 and list3 contain non empty lists, BLPOP
	   * guarantees to return an element from the list stored at list2 (since it is the first non empty
	   * list starting from the left).
	   * <p>
	   * <b>Blocking behavior</b>
	   * <p>
	   * If none of the specified keys exist or contain non empty lists, BLPOP blocks until some other
	   * client performs a LPUSH or an RPUSH operation against one of the lists.
	   * <p>
	   * Once new data is present on one of the lists, the client finally returns with the name of the
	   * key unblocking it and the popped value.
	   * <p>
	   * When blocking, if a non-zero timeout is specified, the client will unblock returning a nil
	   * special value if the specified amount of seconds passed without a push operation against at
	   * least one of the specified keys.
	   * <p>
	   * The timeout argument is interpreted as an integer value. A timeout of zero means instead to
	   * block forever.
	   * <p>
	   * <b>Multiple clients blocking for the same keys</b>
	   * <p>
	   * Multiple clients can block for the same key. They are put into a queue, so the first to be
	   * served will be the one that started to wait earlier, in a first-blpopping first-served fashion.
	   * <p>
	   * <b>blocking POP inside a MULTI/EXEC transaction</b>
	   * <p>
	   * BLPOP and BRPOP can be used with pipelining (sending multiple commands and reading the replies
	   * in batch), but it does not make sense to use BLPOP or BRPOP inside a MULTI/EXEC block (a Redis
	   * transaction).
	   * <p>
	   * The behavior of BLPOP inside MULTI/EXEC when the list is empty is to return a multi-bulk nil
	   * reply, exactly what happens when the timeout is reached. If you like science fiction, think at
	   * it like if inside MULTI/EXEC the time will flow at infinite speed :)
	   * <p>
	   * Time complexity: O(1)
	   * @param timeout
	   * @param keys
	   * @return BLPOP returns a two-elements array via a multi bulk reply in order to return both the
	   *         unblocking key and the popped value.
	   *         <p>
	   *         When a non-zero timeout is specified, and the BLPOP operation timed out, the return
	   *         value is a nil multi bulk reply. Most client values will return false or nil
	   *         accordingly to the programming language used.
	   */
	 
	  public List<String> brpop(final int timeout, final String... keys) {
		  init();
		  if(shardedJedis != null)
			  throw new java.lang.UnsupportedOperationException("sharded Jedis Unsupport brpop(final int timeout, final String... keys)  mehtod.");
		  else if(this.jedis != null)
				return jedis.brpop( timeout,   keys) ;
		  else
			  	return jc.brpop( timeout,   keys) ;
	  }

	  public Long zcount(final String key, final double min, final double max) {
		  init();
		  if(shardedJedis != null)
				return shardedJedis.zcount( key,   min,   max);
		  else if(this.jedis != null)
				return jedis.zcount( key,   min,   max);
		  else
			  	return jc.zcount( key,   min,   max);
	  }

	  public Long zcount(final String key, final String min, final String max) {
		  init();
		  if(shardedJedis != null)
				return shardedJedis.zcount(  key,   min,   max);
		  else if(this.jedis != null)
				return jedis.zcount(  key,   min,   max);
		  else
			  	return jc.zcount(  key,   min,   max);
	  }

//	  /**
//	   * Return the all the elements in the sorted set at key with a score between min and max
//	   * (including elements with score equal to min or max).
//	   * <p>
//	   * The elements having the same score are returned sorted lexicographically as ASCII strings (this
//	   * follows from a property of Redis sorted sets and does not involve further computation).
//	   * <p>
//	   * Using the optional {@link #zrangeByScore(String, double, double, int, int) LIMIT} it's possible
//	   * to get only a range of the matching elements in an SQL-alike way. Note that if offset is large
//	   * the commands needs to traverse the list for offset elements and this adds up to the O(M)
//	   * figure.
//	   * <p>
//	   * The {@link #zcount(String, double, double) ZCOUNT} command is similar to
//	   * {@link #zrangeByScore(String, double, double) ZRANGEBYSCORE} but instead of returning the
//	   * actual elements in the specified interval, it just returns the number of matching elements.
//	   * <p>
//	   * <b>Exclusive intervals and infinity</b>
//	   * <p>
//	   * min and max can be -inf and +inf, so that you are not required to know what's the greatest or
//	   * smallest element in order to take, for instance, elements "up to a given value".
//	   * <p>
//	   * Also while the interval is for default closed (inclusive) it's possible to specify open
//	   * intervals prefixing the score with a "(" character, so for instance:
//	   * <p>
//	   * {@code ZRANGEBYSCORE zset (1.3 5}
//	   * <p>
//	   * Will return all the values with score &gt; 1.3 and &lt;= 5, while for instance:
//	   * <p>
//	   * {@code ZRANGEBYSCORE zset (5 (10}
//	   * <p>
//	   * Will return all the values with score &gt; 5 and &lt; 10 (5 and 10 excluded).
//	   * <p>
//	   * <b>Time complexity:</b>
//	   * <p>
//	   * O(log(N))+O(M) with N being the number of elements in the sorted set and M the number of
//	   * elements returned by the command, so if M is constant (for instance you always ask for the
//	   * first ten elements with LIMIT) you can consider it O(log(N))
//	   * @see #zrangeByScore(String, double, double)
//	   * @see #zrangeByScore(String, double, double, int, int)
//	   * @see #zrangeByScoreWithScores(String, double, double)
//	   * @see #zrangeByScoreWithScores(String, String, String)
//	   * @see #zrangeByScoreWithScores(String, double, double, int, int)
//	   * @see #zcount(String, double, double)
//	   * @param key
//	   * @param min a double or Double.MIN_VALUE for "-inf"
//	   * @param max a double or Double.MAX_VALUE for "+inf"
//	   * @return Multi bulk reply specifically a list of elements in the specified score range.
//	   */
//	 
//	  public Set<String> zrangeByScore(final String key, final double min, final double max) {
//		  if(shardedJedis != null)
//				return shardedJedis.set(key, value,params);
//		  else if(this.jedis != null)
//				return jedis.set(key, value,params);
//		  else
//			  	return jc.set(key, value,params);
//	  }
//
//	  @Override
//	  public Set<String> zrangeByScore(final String key, final String min, final String max) {
//		  if(shardedJedis != null)
//				return shardedJedis.set(key, value,params);
//		  else if(this.jedis != null)
//				return jedis.set(key, value,params);
//		  else
//			  	return jc.set(key, value,params);
//	  }
//
//	  /**
//	   * Return the all the elements in the sorted set at key with a score between min and max
//	   * (including elements with score equal to min or max).
//	   * <p>
//	   * The elements having the same score are returned sorted lexicographically as ASCII strings (this
//	   * follows from a property of Redis sorted sets and does not involve further computation).
//	   * <p>
//	   * Using the optional {@link #zrangeByScore(String, double, double, int, int) LIMIT} it's possible
//	   * to get only a range of the matching elements in an SQL-alike way. Note that if offset is large
//	   * the commands needs to traverse the list for offset elements and this adds up to the O(M)
//	   * figure.
//	   * <p>
//	   * The {@link #zcount(String, double, double) ZCOUNT} command is similar to
//	   * {@link #zrangeByScore(String, double, double) ZRANGEBYSCORE} but instead of returning the
//	   * actual elements in the specified interval, it just returns the number of matching elements.
//	   * <p>
//	   * <b>Exclusive intervals and infinity</b>
//	   * <p>
//	   * min and max can be -inf and +inf, so that you are not required to know what's the greatest or
//	   * smallest element in order to take, for instance, elements "up to a given value".
//	   * <p>
//	   * Also while the interval is for default closed (inclusive) it's possible to specify open
//	   * intervals prefixing the score with a "(" character, so for instance:
//	   * <p>
//	   * {@code ZRANGEBYSCORE zset (1.3 5}
//	   * <p>
//	   * Will return all the values with score &gt; 1.3 and &lt;= 5, while for instance:
//	   * <p>
//	   * {@code ZRANGEBYSCORE zset (5 (10}
//	   * <p>
//	   * Will return all the values with score &gt; 5 and &lt; 10 (5 and 10 excluded).
//	   * <p>
//	   * <b>Time complexity:</b>
//	   * <p>
//	   * O(log(N))+O(M) with N being the number of elements in the sorted set and M the number of
//	   * elements returned by the command, so if M is constant (for instance you always ask for the
//	   * first ten elements with LIMIT) you can consider it O(log(N))
//	   * @see #zrangeByScore(String, double, double)
//	   * @see #zrangeByScore(String, double, double, int, int)
//	   * @see #zrangeByScoreWithScores(String, double, double)
//	   * @see #zrangeByScoreWithScores(String, double, double, int, int)
//	   * @see #zcount(String, double, double)
//	   * @param key
//	   * @param min
//	   * @param max
//	   * @return Multi bulk reply specifically a list of elements in the specified score range.
//	   */
//	  @Override
//	  public Set<String> zrangeByScore(final String key, final double min, final double max,
//	      final int offset, final int count) {
//		  if(shardedJedis != null)
//				return shardedJedis.set(key, value,params);
//		  else if(this.jedis != null)
//				return jedis.set(key, value,params);
//		  else
//			  	return jc.set(key, value,params);
//	  }
//
//	  @Override
//	  public Set<String> zrangeByScore(final String key, final String min, final String max,
//	      final int offset, final int count) {
//		  if(shardedJedis != null)
//				return shardedJedis.set(key, value,params);
//		  else if(this.jedis != null)
//				return jedis.set(key, value,params);
//		  else
//			  	return jc.set(key, value,params);
//	  }
//
//	  /**
//	   * Return the all the elements in the sorted set at key with a score between min and max
//	   * (including elements with score equal to min or max).
//	   * <p>
//	   * The elements having the same score are returned sorted lexicographically as ASCII strings (this
//	   * follows from a property of Redis sorted sets and does not involve further computation).
//	   * <p>
//	   * Using the optional {@link #zrangeByScore(String, double, double, int, int) LIMIT} it's possible
//	   * to get only a range of the matching elements in an SQL-alike way. Note that if offset is large
//	   * the commands needs to traverse the list for offset elements and this adds up to the O(M)
//	   * figure.
//	   * <p>
//	   * The {@link #zcount(String, double, double) ZCOUNT} command is similar to
//	   * {@link #zrangeByScore(String, double, double) ZRANGEBYSCORE} but instead of returning the
//	   * actual elements in the specified interval, it just returns the number of matching elements.
//	   * <p>
//	   * <b>Exclusive intervals and infinity</b>
//	   * <p>
//	   * min and max can be -inf and +inf, so that you are not required to know what's the greatest or
//	   * smallest element in order to take, for instance, elements "up to a given value".
//	   * <p>
//	   * Also while the interval is for default closed (inclusive) it's possible to specify open
//	   * intervals prefixing the score with a "(" character, so for instance:
//	   * <p>
//	   * {@code ZRANGEBYSCORE zset (1.3 5}
//	   * <p>
//	   * Will return all the values with score &gt; 1.3 and &lt;= 5, while for instance:
//	   * <p>
//	   * {@code ZRANGEBYSCORE zset (5 (10}
//	   * <p>
//	   * Will return all the values with score &gt; 5 and &lt; 10 (5 and 10 excluded).
//	   * <p>
//	   * <b>Time complexity:</b>
//	   * <p>
//	   * O(log(N))+O(M) with N being the number of elements in the sorted set and M the number of
//	   * elements returned by the command, so if M is constant (for instance you always ask for the
//	   * first ten elements with LIMIT) you can consider it O(log(N))
//	   * @see #zrangeByScore(String, double, double)
//	   * @see #zrangeByScore(String, double, double, int, int)
//	   * @see #zrangeByScoreWithScores(String, double, double)
//	   * @see #zrangeByScoreWithScores(String, double, double, int, int)
//	   * @see #zcount(String, double, double)
//	   * @param key
//	   * @param min
//	   * @param max
//	   * @return Multi bulk reply specifically a list of elements in the specified score range.
//	   */
//	  @Override
//	  public Set<Tuple> zrangeByScoreWithScores(final String key, final double min, final double max) {
//		  if(shardedJedis != null)
//				return shardedJedis.set(key, value,params);
//		  else if(this.jedis != null)
//				return jedis.set(key, value,params);
//		  else
//			  	return jc.set(key, value,params);
//	  }
//
//	  @Override
//	  public Set<Tuple> zrangeByScoreWithScores(final String key, final String min, final String max) {
//		  if(shardedJedis != null)
//				return shardedJedis.set(key, value,params);
//		  else if(this.jedis != null)
//				return jedis.set(key, value,params);
//		  else
//			  	return jc.set(key, value,params);
//	  }
//
//	  /**
//	   * Return the all the elements in the sorted set at key with a score between min and max
//	   * (including elements with score equal to min or max).
//	   * <p>
//	   * The elements having the same score are returned sorted lexicographically as ASCII strings (this
//	   * follows from a property of Redis sorted sets and does not involve further computation).
//	   * <p>
//	   * Using the optional {@link #zrangeByScore(String, double, double, int, int) LIMIT} it's possible
//	   * to get only a range of the matching elements in an SQL-alike way. Note that if offset is large
//	   * the commands needs to traverse the list for offset elements and this adds up to the O(M)
//	   * figure.
//	   * <p>
//	   * The {@link #zcount(String, double, double) ZCOUNT} command is similar to
//	   * {@link #zrangeByScore(String, double, double) ZRANGEBYSCORE} but instead of returning the
//	   * actual elements in the specified interval, it just returns the number of matching elements.
//	   * <p>
//	   * <b>Exclusive intervals and infinity</b>
//	   * <p>
//	   * min and max can be -inf and +inf, so that you are not required to know what's the greatest or
//	   * smallest element in order to take, for instance, elements "up to a given value".
//	   * <p>
//	   * Also while the interval is for default closed (inclusive) it's possible to specify open
//	   * intervals prefixing the score with a "(" character, so for instance:
//	   * <p>
//	   * {@code ZRANGEBYSCORE zset (1.3 5}
//	   * <p>
//	   * Will return all the values with score &gt; 1.3 and &lt;= 5, while for instance:
//	   * <p>
//	   * {@code ZRANGEBYSCORE zset (5 (10}
//	   * <p>
//	   * Will return all the values with score &gt; 5 and &lt; 10 (5 and 10 excluded).
//	   * <p>
//	   * <b>Time complexity:</b>
//	   * <p>
//	   * O(log(N))+O(M) with N being the number of elements in the sorted set and M the number of
//	   * elements returned by the command, so if M is constant (for instance you always ask for the
//	   * first ten elements with LIMIT) you can consider it O(log(N))
//	   * @see #zrangeByScore(String, double, double)
//	   * @see #zrangeByScore(String, double, double, int, int)
//	   * @see #zrangeByScoreWithScores(String, double, double)
//	   * @see #zrangeByScoreWithScores(String, double, double, int, int)
//	   * @see #zcount(String, double, double)
//	   * @param key
//	   * @param min
//	   * @param max
//	   * @return Multi bulk reply specifically a list of elements in the specified score range.
//	   */
//	  @Override
//	  public Set<Tuple> zrangeByScoreWithScores(final String key, final double min, final double max,
//	      final int offset, final int count) {
//		  if(shardedJedis != null)
//				return shardedJedis.set(key, value,params);
//		  else if(this.jedis != null)
//				return jedis.set(key, value,params);
//		  else
//			  	return jc.set(key, value,params);
//	  }
//
//	  @Override
//	  public Set<Tuple> zrangeByScoreWithScores(final String key, final String min, final String max,
//	      final int offset, final int count) {
//	    checkIsInMultiOrPipeline();
//	    if(shardedJedis != null)
//			return shardedJedis.set(key, value,params);
//	  else if(this.jedis != null)
//			return jedis.set(key, value,params);
//	  else
//		  	return jc.set(key, value,params);
//	  }
//
//	  
//	  @Override
//	  public Set<String> zrevrangeByScore(final String key, final double max, final double min) {
//	    checkIsInMultiOrPipeline();
//	    client.zrevrangeByScore(key, max, min);
//	    final List<String> members = client.getMultiBulkReply();
//	    if (members == null) {
//	      return null;
//	    }
//	    return SetFromList.of(members);
//	  }
//
//	  @Override
//	  public Set<String> zrevrangeByScore(final String key, final String max, final String min) {
//	    checkIsInMultiOrPipeline();
//	    client.zrevrangeByScore(key, max, min);
//	    final List<String> members = client.getMultiBulkReply();
//	    if (members == null) {
//	      return null;
//	    }
//	    return SetFromList.of(members);
//	  }
//
//	  @Override
//	  public Set<String> zrevrangeByScore(final String key, final double max, final double min,
//	      final int offset, final int count) {
//	    checkIsInMultiOrPipeline();
//	    client.zrevrangeByScore(key, max, min, offset, count);
//	    final List<String> members = client.getMultiBulkReply();
//	    if (members == null) {
//	      return null;
//	    }
//	    return SetFromList.of(members);
//	  }
//
//	  @Override
//	  public Set<Tuple> zrevrangeByScoreWithScores(final String key, final double max, final double min) {
//	    checkIsInMultiOrPipeline();
//	    client.zrevrangeByScoreWithScores(key, max, min);
//	    return getTupledSet();
//	  }
//
//	  @Override
//	  public Set<Tuple> zrevrangeByScoreWithScores(final String key, final double max,
//	      final double min, final int offset, final int count) {
//	    checkIsInMultiOrPipeline();
//	    client.zrevrangeByScoreWithScores(key, max, min, offset, count);
//	    return getTupledSet();
//	  }
//
//	  @Override
//	  public Set<Tuple> zrevrangeByScoreWithScores(final String key, final String max,
//	      final String min, final int offset, final int count) {
//	    checkIsInMultiOrPipeline();
//	    client.zrevrangeByScoreWithScores(key, max, min, offset, count);
//	    return getTupledSet();
//	  }
//
//	  @Override
//	  public Set<String> zrevrangeByScore(final String key, final String max, final String min,
//	      final int offset, final int count) {
//	    checkIsInMultiOrPipeline();
//	    client.zrevrangeByScore(key, max, min, offset, count);
//	    final List<String> members = client.getMultiBulkReply();
//	    if (members == null) {
//	      return null;
//	    }
//	    return SetFromList.of(members);
//	  }
//
//	  @Override
//	  public Set<Tuple> zrevrangeByScoreWithScores(final String key, final String max, final String min) {
//	    checkIsInMultiOrPipeline();
//	    client.zrevrangeByScoreWithScores(key, max, min);
//	    return getTupledSet();
//	  }
//
//	  /**
//	   * Remove all elements in the sorted set at key with rank between start and end. Start and end are
//	   * 0-based with rank 0 being the element with the lowest score. Both start and end can be negative
//	   * numbers, where they indicate offsets starting at the element with the highest rank. For
//	   * example: -1 is the element with the highest score, -2 the element with the second highest score
//	   * and so forth.
//	   * <p>
//	   * <b>Time complexity:</b> O(log(N))+O(M) with N being the number of elements in the sorted set
//	   * and M the number of elements removed by the operation
//	   */
//	  @Override
//	  public Long zremrangeByRank(final String key, final long start, final long end) {
//	    checkIsInMultiOrPipeline();
//	    client.zremrangeByRank(key, start, end);
//	    return client.getIntegerReply();
//	  }
//
//	  /**
//	   * Remove all the elements in the sorted set at key with a score between min and max (including
//	   * elements with score equal to min or max).
//	   * <p>
//	   * <b>Time complexity:</b>
//	   * <p>
//	   * O(log(N))+O(M) with N being the number of elements in the sorted set and M the number of
//	   * elements removed by the operation
//	   * @param key
//	   * @param start
//	   * @param end
//	   * @return Integer reply, specifically the number of elements removed.
//	   */
//	  @Override
//	  public Long zremrangeByScore(final String key, final double start, final double end) {
//	    checkIsInMultiOrPipeline();
//	    client.zremrangeByScore(key, start, end);
//	    return client.getIntegerReply();
//	  }
//
//	  @Override
//	  public Long zremrangeByScore(final String key, final String start, final String end) {
//	    checkIsInMultiOrPipeline();
//	    client.zremrangeByScore(key, start, end);
//	    return client.getIntegerReply();
//	  }
//
//	  /**
//	   * Creates a union or intersection of N sorted sets given by keys k1 through kN, and stores it at
//	   * dstkey. It is mandatory to provide the number of input keys N, before passing the input keys
//	   * and the other (optional) arguments.
//	   * <p>
//	   * As the terms imply, the {@link #zinterstore(String, String...) ZINTERSTORE} command requires an
//	   * element to be present in each of the given inputs to be inserted in the result. The
//	   * {@link #zunionstore(String, String...) ZUNIONSTORE} command inserts all elements across all
//	   * inputs.
//	   * <p>
//	   * Using the WEIGHTS option, it is possible to add weight to each input sorted set. This means
//	   * that the score of each element in the sorted set is first multiplied by this weight before
//	   * being passed to the aggregation. When this option is not given, all weights default to 1.
//	   * <p>
//	   * With the AGGREGATE option, it's possible to specify how the results of the union or
//	   * intersection are aggregated. This option defaults to SUM, where the score of an element is
//	   * summed across the inputs where it exists. When this option is set to be either MIN or MAX, the
//	   * resulting set will contain the minimum or maximum score of an element across the inputs where
//	   * it exists.
//	   * <p>
//	   * <b>Time complexity:</b> O(N) + O(M log(M)) with N being the sum of the sizes of the input
//	   * sorted sets, and M being the number of elements in the resulting sorted set
//	   * @see #zunionstore(String, String...)
//	   * @see #zunionstore(String, ZParams, String...)
//	   * @see #zinterstore(String, String...)
//	   * @see #zinterstore(String, ZParams, String...)
//	   * @param dstkey
//	   * @param sets
//	   * @return Integer reply, specifically the number of elements in the sorted set at dstkey
//	   */
//	  @Override
//	  public Long zunionstore(final String dstkey, final String... sets) {
//	    checkIsInMultiOrPipeline();
//	    client.zunionstore(dstkey, sets);
//	    return client.getIntegerReply();
//	  }
//
//	  /**
//	   * Creates a union or intersection of N sorted sets given by keys k1 through kN, and stores it at
//	   * dstkey. It is mandatory to provide the number of input keys N, before passing the input keys
//	   * and the other (optional) arguments.
//	   * <p>
//	   * As the terms imply, the {@link #zinterstore(String, String...) ZINTERSTORE} command requires an
//	   * element to be present in each of the given inputs to be inserted in the result. The
//	   * {@link #zunionstore(String, String...) ZUNIONSTORE} command inserts all elements across all
//	   * inputs.
//	   * <p>
//	   * Using the WEIGHTS option, it is possible to add weight to each input sorted set. This means
//	   * that the score of each element in the sorted set is first multiplied by this weight before
//	   * being passed to the aggregation. When this option is not given, all weights default to 1.
//	   * <p>
//	   * With the AGGREGATE option, it's possible to specify how the results of the union or
//	   * intersection are aggregated. This option defaults to SUM, where the score of an element is
//	   * summed across the inputs where it exists. When this option is set to be either MIN or MAX, the
//	   * resulting set will contain the minimum or maximum score of an element across the inputs where
//	   * it exists.
//	   * <p>
//	   * <b>Time complexity:</b> O(N) + O(M log(M)) with N being the sum of the sizes of the input
//	   * sorted sets, and M being the number of elements in the resulting sorted set
//	   * @see #zunionstore(String, String...)
//	   * @see #zunionstore(String, ZParams, String...)
//	   * @see #zinterstore(String, String...)
//	   * @see #zinterstore(String, ZParams, String...)
//	   * @param dstkey
//	   * @param sets
//	   * @param params
//	   * @return Integer reply, specifically the number of elements in the sorted set at dstkey
//	   */
//	  @Override
//	  public Long zunionstore(final String dstkey, final ZParams params, final String... sets) {
//	    checkIsInMultiOrPipeline();
//	    client.zunionstore(dstkey, params, sets);
//	    return client.getIntegerReply();
//	  }
//
//	  /**
//	   * Creates a union or intersection of N sorted sets given by keys k1 through kN, and stores it at
//	   * dstkey. It is mandatory to provide the number of input keys N, before passing the input keys
//	   * and the other (optional) arguments.
//	   * <p>
//	   * As the terms imply, the {@link #zinterstore(String, String...) ZINTERSTORE} command requires an
//	   * element to be present in each of the given inputs to be inserted in the result. The
//	   * {@link #zunionstore(String, String...) ZUNIONSTORE} command inserts all elements across all
//	   * inputs.
//	   * <p>
//	   * Using the WEIGHTS option, it is possible to add weight to each input sorted set. This means
//	   * that the score of each element in the sorted set is first multiplied by this weight before
//	   * being passed to the aggregation. When this option is not given, all weights default to 1.
//	   * <p>
//	   * With the AGGREGATE option, it's possible to specify how the results of the union or
//	   * intersection are aggregated. This option defaults to SUM, where the score of an element is
//	   * summed across the inputs where it exists. When this option is set to be either MIN or MAX, the
//	   * resulting set will contain the minimum or maximum score of an element across the inputs where
//	   * it exists.
//	   * <p>
//	   * <b>Time complexity:</b> O(N) + O(M log(M)) with N being the sum of the sizes of the input
//	   * sorted sets, and M being the number of elements in the resulting sorted set
//	   * @see #zunionstore(String, String...)
//	   * @see #zunionstore(String, ZParams, String...)
//	   * @see #zinterstore(String, String...)
//	   * @see #zinterstore(String, ZParams, String...)
//	   * @param dstkey
//	   * @param sets
//	   * @return Integer reply, specifically the number of elements in the sorted set at dstkey
//	   */
//	  @Override
//	  public Long zinterstore(final String dstkey, final String... sets) {
//	    checkIsInMultiOrPipeline();
//	    client.zinterstore(dstkey, sets);
//	    return client.getIntegerReply();
//	  }
//
//	  /**
//	   * Creates a union or intersection of N sorted sets given by keys k1 through kN, and stores it at
//	   * dstkey. It is mandatory to provide the number of input keys N, before passing the input keys
//	   * and the other (optional) arguments.
//	   * <p>
//	   * As the terms imply, the {@link #zinterstore(String, String...) ZINTERSTORE} command requires an
//	   * element to be present in each of the given inputs to be inserted in the result. The
//	   * {@link #zunionstore(String, String...) ZUNIONSTORE} command inserts all elements across all
//	   * inputs.
//	   * <p>
//	   * Using the WEIGHTS option, it is possible to add weight to each input sorted set. This means
//	   * that the score of each element in the sorted set is first multiplied by this weight before
//	   * being passed to the aggregation. When this option is not given, all weights default to 1.
//	   * <p>
//	   * With the AGGREGATE option, it's possible to specify how the results of the union or
//	   * intersection are aggregated. This option defaults to SUM, where the score of an element is
//	   * summed across the inputs where it exists. When this option is set to be either MIN or MAX, the
//	   * resulting set will contain the minimum or maximum score of an element across the inputs where
//	   * it exists.
//	   * <p>
//	   * <b>Time complexity:</b> O(N) + O(M log(M)) with N being the sum of the sizes of the input
//	   * sorted sets, and M being the number of elements in the resulting sorted set
//	   * @see #zunionstore(String, String...)
//	   * @see #zunionstore(String, ZParams, String...)
//	   * @see #zinterstore(String, String...)
//	   * @see #zinterstore(String, ZParams, String...)
//	   * @param dstkey
//	   * @param sets
//	   * @param params
//	   * @return Integer reply, specifically the number of elements in the sorted set at dstkey
//	   */
//	  @Override
//	  public Long zinterstore(final String dstkey, final ZParams params, final String... sets) {
//	    checkIsInMultiOrPipeline();
//	    client.zinterstore(dstkey, params, sets);
//	    return client.getIntegerReply();
//	  }
//
//	  @Override
//	  public Long zlexcount(final String key, final String min, final String max) {
//	    checkIsInMultiOrPipeline();
//	    client.zlexcount(key, min, max);
//	    return client.getIntegerReply();
//	  }
//
//	  @Override
//	  public Set<String> zrangeByLex(final String key, final String min, final String max) {
//	    checkIsInMultiOrPipeline();
//	    client.zrangeByLex(key, min, max);
//	    final List<String> members = client.getMultiBulkReply();
//	    if (members == null) {
//	      return null;
//	    }
//	    return SetFromList.of(members);
//	  }
//
//	  @Override
//	  public Set<String> zrangeByLex(final String key, final String min, final String max,
//	      final int offset, final int count) {
//	    checkIsInMultiOrPipeline();
//	    client.zrangeByLex(key, min, max, offset, count);
//	    final List<String> members = client.getMultiBulkReply();
//	    if (members == null) {
//	      return null;
//	    }
//	    return SetFromList.of(members);
//	  }
//
//	  @Override
//	  public Set<String> zrevrangeByLex(String key, String max, String min) {
//	    checkIsInMultiOrPipeline();
//	    client.zrevrangeByLex(key, max, min);
//	    final List<String> members = client.getMultiBulkReply();
//	    if (members == null) {
//	      return null;
//	    }
//	    return SetFromList.of(members);
//	  }
//
//	  @Override
//	  public Set<String> zrevrangeByLex(String key, String max, String min, int offset, int count) {
//	    checkIsInMultiOrPipeline();
//	    client.zrevrangeByLex(key, max, min, offset, count);
//	    final List<String> members = client.getMultiBulkReply();
//	    if (members == null) {
//	      return null;
//	    }
//	    return SetFromList.of(members);
//	  }
//
//	  @Override
//	  public Long zremrangeByLex(final String key, final String min, final String max) {
//	    checkIsInMultiOrPipeline();
//	    client.zremrangeByLex(key, min, max);
//	    return client.getIntegerReply();
//	  }
//

	  public Long strlen(final String key) {
		  init();
		  if(shardedJedis != null)
				return shardedJedis.strlen(  key) ;
		  else if(this.jedis != null)
				return jedis.strlen(  key)  ;
		  else
			  	return jc.strlen(  key)  ;
	  }

	  public Long lpushx(final String key, final String... string) {
		  init();
		  if(shardedJedis != null)
				return shardedJedis.lpushx(  key,   string)  ;
		  else if(this.jedis != null)
				return jedis.lpushx(  key,   string)  ;
		  else
			  	return jc.lpushx(  key,   string)  ;
	  }

	  /**
	   * Undo a {@link #expire(String, int) expire} at turning the expire key into a normal key.
	   * <p>
	   * Time complexity: O(1)
	   * @param key
	   * @return Integer reply, specifically: 1: the key is now persist. 0: the key is not persist (only
	   *         happens when key not set).
	   */
	  public Long persist(final String key) {
		  init();
		  if(shardedJedis != null)
				return shardedJedis.persist(  key) ;
		  else if(this.jedis != null)
				return jedis.persist(  key) ;
		  else
			  	return jc.persist(  key) ;
	  }

	  public Long rpushx(final String key, final String... string) {
		  init();
		  if(shardedJedis != null)
				return shardedJedis.rpushx( key,   string)  ;
		  else if(this.jedis != null)
				return jedis.rpushx( key,   string)  ;
		  else
			  	return jc.rpushx( key,   string)  ;
	  }

	   
	  public String echo(final String string) {
		  init();
		  if(shardedJedis != null)
				return shardedJedis.echo( string)  ;
		  else if(this.jedis != null)
				return jedis.echo( string)  ;
		  else
			  	return jc.echo( string)  ;
	  }

	   
	  public Long linsert(final String key, final LIST_POSITION where, final String pivot,
	      final String value) {
		  init();
		  if(shardedJedis != null)
				return shardedJedis.linsert(  key,   where,  pivot,
					       value)   ;
		  else if(this.jedis != null)
				return jedis.linsert(  key,   where,  pivot,
					       value)   ;
		  else
			  	return jc.linsert(  key,   where,  pivot,
					       value)   ;
	  }

//	  /**
//	   * Pop a value from a list, push it to another list and return it; or block until one is available
//	   * @param source
//	   * @param destination
//	   * @param timeout
//	   * @return the element
//	   */
//	  @Override
//	  public String brpoplpush(String source, String destination, int timeout) {
//	    client.brpoplpush(source, destination, timeout);
//	    client.setTimeoutInfinite();
//	    try {
//	      return client.getBulkReply();
//	    } finally {
//	      client.rollbackTimeout();
//	    }
//	  }
//
//	  /**
//	   * Sets or clears the bit at offset in the string value stored at key
//	   * @param key
//	   * @param offset
//	   * @param value
//	   * @return
//	   */
//	  @Override
//	  public Boolean setbit(String key, long offset, boolean value) {
//	    client.setbit(key, offset, value);
//	    return client.getIntegerReply() == 1;
//	  }
//
//	  @Override
//	  public Boolean setbit(String key, long offset, String value) {
//	    client.setbit(key, offset, value);
//	    return client.getIntegerReply() == 1;
//	  }
//
//	  /**
//	   * Returns the bit value at offset in the string value stored at key
//	   * @param key
//	   * @param offset
//	   * @return
//	   */
//	  @Override
//	  public Boolean getbit(String key, long offset) {
//	    client.getbit(key, offset);
//	    return client.getIntegerReply() == 1;
//	  }
//
//	  @Override
//	  public Long setrange(String key, long offset, String value) {
//	    client.setrange(key, offset, value);
//	    return client.getIntegerReply();
//	  }
//
//	  @Override
//	  public String getrange(String key, long startOffset, long endOffset) {
//	    client.getrange(key, startOffset, endOffset);
//	    return client.getBulkReply();
//	  }
//
//	  @Override
//	  public Long bitpos(final String key, final boolean value) {
//	    return bitpos(key, value, new BitPosParams());
//	  }
//
//	  @Override
//	  public Long bitpos(final String key, final boolean value, final BitPosParams params) {
//	    client.bitpos(key, value, params);
//	    return client.getIntegerReply();
//	  }

	  /**
	   * Retrieve the configuration of a running Redis server. Not all the configuration parameters are
	   * supported.
	   * <p>
	   * CONFIG GET returns the current configuration parameters. This sub command only accepts a single
	   * argument, that is glob style pattern. All the configuration parameters matching this parameter
	   * are reported as a list of key-value pairs.
	   * <p>
	   * <b>Example:</b>
	   * 
	   * <pre>
	   * $ redis-cli config get '*'
	   * 1. "dbfilename"
	   * 2. "dump.rdb"
	   * 3. "requirepass"
	   * 4. (nil)
	   * 5. "masterauth"
	   * 6. (nil)
	   * 7. "maxmemory"
	   * 8. "0\n"
	   * 9. "appendfsync"
	   * 10. "everysec"
	   * 11. "save"
	   * 12. "3600 1 300 100 60 10000"
	   * 
	   * $ redis-cli config get 'm*'
	   * 1. "masterauth"
	   * 2. (nil)
	   * 3. "maxmemory"
	   * 4. "0\n"
	   * </pre>
	   * @param pattern
	   * @return Bulk reply.
	   */
	  
	  public List<String> configGet(final String pattern) {
		  init();
		  if(shardedJedis != null)
		  {
			  Jedis d = this.shardedJedis.getAllShards().iterator().next();
			  
			  return d.configGet(pattern);
		  }
		  else if(this.jedis != null)
				return jedis.configGet( pattern) ;
		  else
			  throw new java.lang.UnsupportedOperationException("  Jedis Cluster Unsupport   configGet mehtod.");
	  }

//	  /**
//	   * Alter the configuration of a running Redis server. Not all the configuration parameters are
//	   * supported.
//	   * <p>
//	   * The list of configuration parameters supported by CONFIG SET can be obtained issuing a
//	   * {@link #configGet(String) CONFIG GET *} command.
//	   * <p>
//	   * The configuration set using CONFIG SET is immediately loaded by the Redis server that will
//	   * start acting as specified starting from the next command.
//	   * <p>
//	   * <b>Parameters value format</b>
//	   * <p>
//	   * The value of the configuration parameter is the same as the one of the same parameter in the
//	   * Redis configuration file, with the following exceptions:
//	   * <p>
//	   * <ul>
//	   * <li>The save paramter is a list of space-separated integers. Every pair of integers specify the
//	   * time and number of changes limit to trigger a save. For instance the command CONFIG SET save
//	   * "3600 10 60 10000" will configure the server to issue a background saving of the RDB file every
//	   * 3600 seconds if there are at least 10 changes in the dataset, and every 60 seconds if there are
//	   * at least 10000 changes. To completely disable automatic snapshots just set the parameter as an
//	   * empty string.
//	   * <li>All the integer parameters representing memory are returned and accepted only using bytes
//	   * as unit.
//	   * </ul>
//	   * @param parameter
//	   * @param value
//	   * @return Status code reply
//	   */
 

	 
	  public Object eval(String script, int keyCount, String... params) {
		  init();
		  if(shardedJedis != null)
		  {
			  throw new java.lang.UnsupportedOperationException("  shared Jedis  Unsupport   eval(String script, int keyCount, String... params)  mehtod.");
			  
		  }
		  else if(this.jedis != null)
				return jedis.eval(  script,   keyCount,  params) ;
		  else
			  return jc.eval(  script,   keyCount,  params) ;
	  }

	  public void subscribe(final JedisPubSub jedisPubSub, final String... channels) {
		  init();
		  if(shardedJedis != null)
		  {
			  throw new java.lang.UnsupportedOperationException("  shared Jedis  Unsupport   subscribe  mehtod.");
			  
		  }
		  else if(this.jedis != null)
				  jedis.subscribe(  jedisPubSub,   channels) ;
		  else
			    jc.subscribe(  jedisPubSub,   channels) ;
	  }

	   
	  public Long publish(final String channel, final String message) {
		  init();
		  if(shardedJedis != null)
		  {
			  
			  throw new java.lang.UnsupportedOperationException("  shared Jedis  Unsupport   publish  mehtod.");
			  
		  }
		  else if(this.jedis != null)
				return   jedis.publish(  channel,   message) ;
		  else
			  return   jc.publish(  channel,   message) ;
	  }

	  public void psubscribe(final JedisPubSub jedisPubSub, final String... patterns) {
		  init();
		  if(shardedJedis != null)
		  {
			   
			  throw new java.lang.UnsupportedOperationException("  shared Jedis  Unsupport   psubscribe  mehtod.");
			  
		  }
		  else if(this.jedis != null)
				    jedis.psubscribe( jedisPubSub, patterns) ;
		  else
			      jc.psubscribe( jedisPubSub, patterns) ;
	  }

	 

	  
	   

	  

	 

//	  @Override
//	  public Object evalsha(String sha1, List<String> keys, List<String> args) {
//	    return evalsha(sha1, keys.size(), getParams(keys, args));
//	  }
//
//	  @Override
//	  public Object evalsha(String sha1, int keyCount, String... params) {
//	    checkIsInMultiOrPipeline();
//	    client.evalsha(sha1, keyCount, params);
//	    return getEvalResult();
//	  }
//
//	  @Override
//	  public Boolean scriptExists(String sha1) {
//	    String[] a = new String[1];
//	    a[0] = sha1;
//	    return scriptExists(a).get(0);
//	  }
//
//	  @Override
//	  public List<Boolean> scriptExists(String... sha1) {
//	    client.scriptExists(sha1);
//	    List<Long> result = client.getIntegerMultiBulkReply();
//	    List<Boolean> exists = new ArrayList<Boolean>();
//
//	    for (Long value : result)
//	      exists.add(value == 1);
//
//	    return exists;
//	  }
//
//	  @Override
//	  public String scriptLoad(String script) {
//	    client.scriptLoad(script);
//	    return client.getBulkReply();
//	  }

//	  public List<Slowlog> slowlogGet() {
//		  if(shardedJedis != null)
//		  {
//			  
//			  throw new java.lang.UnsupportedOperationException("  shared Jedis  Unsupport   publish  mehtod.");
//			  
//		  }
//		  else if(this.jedis != null)
//				return   jedis.slowlogGet() ;
//		  else
//			  return   jc.slowlogGet() ;
//	  }
//
//	  @Override
//	  public List<Slowlog> slowlogGet(long entries) {
//	    client.slowlogGet(entries);
//	    return Slowlog.from(client.getObjectMultiBulkReply());
//	  }

//	  public Long objectRefcount(String string) {
//		  if(shardedJedis != null)
//		  {
//			  
//			  throw new java.lang.UnsupportedOperationException("  shared Jedis  Unsupport   publish  mehtod.");
//			  
//		  }
//		  else if(this.jedis != null)
//				return   jedis.objectRefcount(  string) ;
//		  else
//			  return   jc.objectRefcount(  string) ;
//	  }
//
//	  @Override
//	  public String objectEncoding(String string) {
//	    client.objectEncoding(string);
//	    return client.getBulkReply();
//	  }
//
//	  @Override
//	  public Long objectIdletime(String string) {
//	    client.objectIdletime(string);
//	    return client.getIntegerReply();
//	  }
//
//	  @Override
//	  public Long bitcount(final String key) {
//	    client.bitcount(key);
//	    return client.getIntegerReply();
//	  }
//
//	  @Override
//	  public Long bitcount(final String key, long start, long end) {
//	    client.bitcount(key, start, end);
//	    return client.getIntegerReply();
//	  }
//
//	  @Override
//	  public Long bitop(BitOP op, final String destKey, String... srcKeys) {
//	    client.bitop(op, destKey, srcKeys);
//	    return client.getIntegerReply();
//	  }

	  /**
	   * <pre>
	   * redis 127.0.0.1:26381&gt; sentinel masters
	   * 1)  1) "name"
	   *     2) "mymaster"
	   *     3) "ip"
	   *     4) "127.0.0.1"
	   *     5) "port"
	   *     6) "6379"
	   *     7) "runid"
	   *     8) "93d4d4e6e9c06d0eea36e27f31924ac26576081d"
	   *     9) "flags"
	   *    10) "master"
	   *    11) "pending-commands"
	   *    12) "0"
	   *    13) "last-ok-ping-reply"
	   *    14) "423"
	   *    15) "last-ping-reply"
	   *    16) "423"
	   *    17) "info-refresh"
	   *    18) "6107"
	   *    19) "num-slaves"
	   *    20) "1"
	   *    21) "num-other-sentinels"
	   *    22) "2"
	   *    23) "quorum"
	   *    24) "2"
	   * 
	   * </pre>
	   * @return
	   */
//	  @Override
//	  @SuppressWarnings("rawtypes")
//	  public List<Map<String, String>> sentinelMasters() {
//	    client.sentinel(Protocol.SENTINEL_MASTERS);
//	    final List<Object> reply = client.getObjectMultiBulkReply();
//
//	    final List<Map<String, String>> masters = new ArrayList<Map<String, String>>();
//	    for (Object obj : reply) {
//	      masters.add(BuilderFactory.STRING_MAP.build((List) obj));
//	    }
//	    return masters;
//	  }
//
//	  /**
//	   * <pre>
//	   * redis 127.0.0.1:26381&gt; sentinel get-master-addr-by-name mymaster
//	   * 1) "127.0.0.1"
//	   * 2) "6379"
//	   * </pre>
//	   * @param masterName
//	   * @return two elements list of strings : host and port.
//	   */
//	  @Override
//	  public List<String> sentinelGetMasterAddrByName(String masterName) {
//	    client.sentinel(Protocol.SENTINEL_GET_MASTER_ADDR_BY_NAME, masterName);
//	    final List<Object> reply = client.getObjectMultiBulkReply();
//	    return BuilderFactory.STRING_LIST.build(reply);
//	  }
//
//	  /**
//	   * <pre>
//	   * redis 127.0.0.1:26381&gt; sentinel reset mymaster
//	   * (integer) 1
//	   * </pre>
//	   * @param pattern
//	   * @return
//	   */
//	  @Override
//	  public Long sentinelReset(String pattern) {
//	    client.sentinel(Protocol.SENTINEL_RESET, pattern);
//	    return client.getIntegerReply();
//	  }
//
//	  /**
//	   * <pre>
//	   * redis 127.0.0.1:26381&gt; sentinel slaves mymaster
//	   * 1)  1) "name"
//	   *     2) "127.0.0.1:6380"
//	   *     3) "ip"
//	   *     4) "127.0.0.1"
//	   *     5) "port"
//	   *     6) "6380"
//	   *     7) "runid"
//	   *     8) "d7f6c0ca7572df9d2f33713df0dbf8c72da7c039"
//	   *     9) "flags"
//	   *    10) "slave"
//	   *    11) "pending-commands"
//	   *    12) "0"
//	   *    13) "last-ok-ping-reply"
//	   *    14) "47"
//	   *    15) "last-ping-reply"
//	   *    16) "47"
//	   *    17) "info-refresh"
//	   *    18) "657"
//	   *    19) "master-link-down-time"
//	   *    20) "0"
//	   *    21) "master-link-status"
//	   *    22) "ok"
//	   *    23) "master-host"
//	   *    24) "localhost"
//	   *    25) "master-port"
//	   *    26) "6379"
//	   *    27) "slave-priority"
//	   *    28) "100"
//	   * </pre>
//	   * @param masterName
//	   * @return
//	   */
//	  @Override
//	  @SuppressWarnings("rawtypes")
//	  public List<Map<String, String>> sentinelSlaves(String masterName) {
//	    client.sentinel(Protocol.SENTINEL_SLAVES, masterName);
//	    final List<Object> reply = client.getObjectMultiBulkReply();
//
//	    final List<Map<String, String>> slaves = new ArrayList<Map<String, String>>();
//	    for (Object obj : reply) {
//	      slaves.add(BuilderFactory.STRING_MAP.build((List) obj));
//	    }
//	    return slaves;
//	  }
//
//	  @Override
//	  public String sentinelFailover(String masterName) {
//	    client.sentinel(Protocol.SENTINEL_FAILOVER, masterName);
//	    return client.getStatusCodeReply();
//	  }
//
//	  @Override
//	  public String sentinelMonitor(String masterName, String ip, int port, int quorum) {
//	    client.sentinel(Protocol.SENTINEL_MONITOR, masterName, ip, String.valueOf(port),
//	      String.valueOf(quorum));
//	    return client.getStatusCodeReply();
//	  }
//
//	  @Override
//	  public String sentinelRemove(String masterName) {
//	    client.sentinel(Protocol.SENTINEL_REMOVE, masterName);
//	    return client.getStatusCodeReply();
//	  }
//
//	  @Override
//	  public String sentinelSet(String masterName, Map<String, String> parameterMap) {
//	    int index = 0;
//	    int paramsLength = parameterMap.size() * 2 + 2;
//	    String[] params = new String[paramsLength];
//
//	    params[index++] = Protocol.SENTINEL_SET;
//	    params[index++] = masterName;
//	    for (Entry<String, String> entry : parameterMap.entrySet()) {
//	      params[index++] = entry.getKey();
//	      params[index++] = entry.getValue();
//	    }
//
//	    client.sentinel(params);
//	    return client.getStatusCodeReply();
//	  }
//
//	  public byte[] dump(final String key) {
//	    checkIsInMultiOrPipeline();
//	    client.dump(key);
//	    return client.getBinaryBulkReply();
//	  }

//	  public String restore(final String key, final int ttl, final byte[] serializedValue) {
//		  if(shardedJedis != null)
//				return shardedJedis.restore(  key,   ttl,   serializedValue)   ;
//		  else if(this.jedis != null)
//				return jedis.restore(  key,   ttl,   serializedValue)   ;
//		  else
//			  	return jc.restore(  key,   ttl,   serializedValue)   ;
//	  }

	  public Long pexpire(final String key, final long milliseconds) {
		  init();
		  if(shardedJedis != null)
				return shardedJedis.pexpire( key,   milliseconds)   ;
		  else if(this.jedis != null)
				return jedis.pexpire( key,   milliseconds)   ;
		  else
			  	return jc.pexpire( key,   milliseconds)   ;
	  }

	  public Long pexpireAt(final String key, final long millisecondsTimestamp) {
		  init();
		  if(shardedJedis != null)
				return shardedJedis.pexpireAt(  key,   millisecondsTimestamp);
		  else if(this.jedis != null)
				return jedis.pexpireAt(  key,   millisecondsTimestamp);
		  else
			  	return jc.pexpireAt(  key,   millisecondsTimestamp);
	  }

	  public Long pttl(final String key) {
		  init();
		  if(shardedJedis != null)
				return shardedJedis.pttl(  key);
		  else if(this.jedis != null)
				return jedis.pttl(  key);
		  else
			  throw new java.lang.UnsupportedOperationException("  Cluster Jedis  Unsupport   pttl  mehtod.");
	  }

	  /**
	   * PSETEX works exactly like {@link #setex(String, int, String)} with the sole difference that the
	   * expire time is specified in milliseconds instead of seconds. Time complexity: O(1)
	   * @param key
	   * @param milliseconds
	   * @param value
	   * @return Status code reply
	   */

	  public String psetex(final String key, final long milliseconds, final String value) {
		  init();
		  if(shardedJedis != null)
				return shardedJedis.psetex(  key,   milliseconds,   value) ;
		  else if(this.jedis != null)
				return jedis.psetex(  key,   milliseconds,   value) ;
		  else
			  throw new java.lang.UnsupportedOperationException("  Cluster Jedis  Unsupport   psetex  mehtod.");
	  }

//	  public String clientKill(final String client) {
//	    checkIsInMultiOrPipeline();
//	    this.client.clientKill(client);
//	    return this.client.getStatusCodeReply();
//	  }
//
//	  public String clientSetname(final String name) {
//	    checkIsInMultiOrPipeline();
//	    client.clientSetname(name);
//	    return client.getStatusCodeReply();
//	  }
//
//	  public String migrate(final String host, final int port, final String key,
//	      final int destinationDb, final int timeout) {
//	    checkIsInMultiOrPipeline();
//	    client.migrate(host, port, key, destinationDb, timeout);
//	    return client.getStatusCodeReply();
//	  }
//
//	  @Override
//	  public ScanResult<String> scan(final String cursor) {
//	    return scan(cursor, new ScanParams());
//	  }
//
//	  @Override
//	  public ScanResult<String> scan(final String cursor, final ScanParams params) {
//	    checkIsInMultiOrPipeline();
//	    client.scan(cursor, params);
//	    List<Object> result = client.getObjectMultiBulkReply();
//	    String newcursor = new String((byte[]) result.get(0));
//	    List<String> results = new ArrayList<String>();
//	    List<byte[]> rawResults = (List<byte[]>) result.get(1);
//	    for (byte[] bs : rawResults) {
//	      results.add(SafeEncoder.encode(bs));
//	    }
//	    return new ScanResult<String>(newcursor, results);
//	  }
//
//	  @Override
//	  public ScanResult<Map.Entry<String, String>> hscan(final String key, final String cursor) {
//	    return hscan(key, cursor, new ScanParams());
//	  }
//
//	  @Override
//	  public ScanResult<Map.Entry<String, String>> hscan(final String key, final String cursor,
//	      final ScanParams params) {
//	    checkIsInMultiOrPipeline();
//	    client.hscan(key, cursor, params);
//	    List<Object> result = client.getObjectMultiBulkReply();
//	    String newcursor = new String((byte[]) result.get(0));
//	    List<Map.Entry<String, String>> results = new ArrayList<Map.Entry<String, String>>();
//	    List<byte[]> rawResults = (List<byte[]>) result.get(1);
//	    Iterator<byte[]> iterator = rawResults.iterator();
//	    while (iterator.hasNext()) {
//	      results.add(new AbstractMap.SimpleEntry<String, String>(SafeEncoder.encode(iterator.next()),
//	          SafeEncoder.encode(iterator.next())));
//	    }
//	    return new ScanResult<Map.Entry<String, String>>(newcursor, results);
//	  }
//
//	  @Override
//	  public ScanResult<String> sscan(final String key, final String cursor) {
//	    return sscan(key, cursor, new ScanParams());
//	  }
//
//	  @Override
//	  public ScanResult<String> sscan(final String key, final String cursor, final ScanParams params) {
//	    checkIsInMultiOrPipeline();
//	    client.sscan(key, cursor, params);
//	    List<Object> result = client.getObjectMultiBulkReply();
//	    String newcursor = new String((byte[]) result.get(0));
//	    List<String> results = new ArrayList<String>();
//	    List<byte[]> rawResults = (List<byte[]>) result.get(1);
//	    for (byte[] bs : rawResults) {
//	      results.add(SafeEncoder.encode(bs));
//	    }
//	    return new ScanResult<String>(newcursor, results);
//	  }
//
//	  @Override
//	  public ScanResult<Tuple> zscan(final String key, final String cursor) {
//	    return zscan(key, cursor, new ScanParams());
//	  }
//
//	  @Override
//	  public ScanResult<Tuple> zscan(final String key, final String cursor, final ScanParams params) {
//	    checkIsInMultiOrPipeline();
//	    client.zscan(key, cursor, params);
//	    List<Object> result = client.getObjectMultiBulkReply();
//	    String newcursor = new String((byte[]) result.get(0));
//	    List<Tuple> results = new ArrayList<Tuple>();
//	    List<byte[]> rawResults = (List<byte[]>) result.get(1);
//	    Iterator<byte[]> iterator = rawResults.iterator();
//	    while (iterator.hasNext()) {
//	      results.add(new Tuple(SafeEncoder.encode(iterator.next()), Double.valueOf(SafeEncoder
//	          .encode(iterator.next()))));
//	    }
//	    return new ScanResult<Tuple>(newcursor, results);
//	  }
//
//	  @Override
//	  public String clusterNodes() {
//	    checkIsInMultiOrPipeline();
//	    client.clusterNodes();
//	    return client.getBulkReply();
//	  }

//	  public String readonly() {
//		  if(shardedJedis != null)
//				return shardedJedis.readonly();
//		  else if(this.jedis != null)
//				return jedis.readonly();
//		  else
//			  	return jc.readonly();
//	  }

//	  @Override
//	  public String clusterMeet(final String ip, final int port) {
//	    checkIsInMultiOrPipeline();
//	    client.clusterMeet(ip, port);
//	    return client.getStatusCodeReply();
//	  }
//
//	  @Override
//	  public String clusterReset(final Reset resetType) {
//	    checkIsInMultiOrPipeline();
//	    client.clusterReset(resetType);
//	    return client.getStatusCodeReply();
//	  }
//
//	  @Override
//	  public String clusterAddSlots(final int... slots) {
//	    checkIsInMultiOrPipeline();
//	    client.clusterAddSlots(slots);
//	    return client.getStatusCodeReply();
//	  }
//
//	  @Override
//	  public String clusterDelSlots(final int... slots) {
//	    checkIsInMultiOrPipeline();
//	    client.clusterDelSlots(slots);
//	    return client.getStatusCodeReply();
//	  }
//
//	  @Override
//	  public String clusterInfo() {
//	    checkIsInMultiOrPipeline();
//	    client.clusterInfo();
//	    return client.getStatusCodeReply();
//	  }
//
//	  @Override
//	  public List<String> clusterGetKeysInSlot(final int slot, final int count) {
//	    checkIsInMultiOrPipeline();
//	    client.clusterGetKeysInSlot(slot, count);
//	    return client.getMultiBulkReply();
//	  }
//
//	  @Override
//	  public String clusterSetSlotNode(final int slot, final String nodeId) {
//	    checkIsInMultiOrPipeline();
//	    client.clusterSetSlotNode(slot, nodeId);
//	    return client.getStatusCodeReply();
//	  }
//
//	  @Override
//	  public String clusterSetSlotMigrating(final int slot, final String nodeId) {
//	    checkIsInMultiOrPipeline();
//	    client.clusterSetSlotMigrating(slot, nodeId);
//	    return client.getStatusCodeReply();
//	  }
//
//	  @Override
//	  public String clusterSetSlotImporting(final int slot, final String nodeId) {
//	    checkIsInMultiOrPipeline();
//	    client.clusterSetSlotImporting(slot, nodeId);
//	    return client.getStatusCodeReply();
//	  }
//
//	  @Override
//	  public String clusterSetSlotStable(final int slot) {
//	    checkIsInMultiOrPipeline();
//	    client.clusterSetSlotStable(slot);
//	    return client.getStatusCodeReply();
//	  }
//
//	  @Override
//	  public String clusterForget(final String nodeId) {
//	    checkIsInMultiOrPipeline();
//	    client.clusterForget(nodeId);
//	    return client.getStatusCodeReply();
//	  }
//
//	  @Override
//	  public String clusterFlushSlots() {
//	    checkIsInMultiOrPipeline();
//	    client.clusterFlushSlots();
//	    return client.getStatusCodeReply();
//	  }
//
//	  @Override
//	  public Long clusterKeySlot(final String key) {
//	    checkIsInMultiOrPipeline();
//	    client.clusterKeySlot(key);
//	    return client.getIntegerReply();
//	  }
//
//	  @Override
//	  public Long clusterCountKeysInSlot(final int slot) {
//	    checkIsInMultiOrPipeline();
//	    client.clusterCountKeysInSlot(slot);
//	    return client.getIntegerReply();
//	  }
//
//	  @Override
//	  public String clusterSaveConfig() {
//	    checkIsInMultiOrPipeline();
//	    client.clusterSaveConfig();
//	    return client.getStatusCodeReply();
//	  }
//
//	  @Override
//	  public String clusterReplicate(final String nodeId) {
//	    checkIsInMultiOrPipeline();
//	    client.clusterReplicate(nodeId);
//	    return client.getStatusCodeReply();
//	  }
//
//	  @Override
//	  public List<String> clusterSlaves(final String nodeId) {
//	    checkIsInMultiOrPipeline();
//	    client.clusterSlaves(nodeId);
//	    return client.getMultiBulkReply();
//	  }
//
//	  @Override
//	  public String clusterFailover() {
//	    checkIsInMultiOrPipeline();
//	    client.clusterFailover();
//	    return client.getStatusCodeReply();
//	  }
//
//	  @Override
//	  public List<Object> clusterSlots() {
//	    checkIsInMultiOrPipeline();
//	    client.clusterSlots();
//	    return client.getObjectMultiBulkReply();
//	  }
//
//	  public String asking() {
//	    checkIsInMultiOrPipeline();
//	    client.asking();
//	    return client.getStatusCodeReply();
//	  }
//
//	  public List<String> pubsubChannels(String pattern) {
//	    checkIsInMultiOrPipeline();
//	    client.pubsubChannels(pattern);
//	    return client.getMultiBulkReply();
//	  }
//
//	  public Long pubsubNumPat() {
//	    checkIsInMultiOrPipeline();
//	    client.pubsubNumPat();
//	    return client.getIntegerReply();
//	  }
//
//	  public Map<String, String> pubsubNumSub(String... channels) {
//	    checkIsInMultiOrPipeline();
//	    client.pubsubNumSub(channels);
//	    return BuilderFactory.PUBSUB_NUMSUB_MAP.build(client.getBinaryMultiBulkReply());
//	  }
//
//	  @Override
//	  public void close() {
//	    if (dataSource != null) {
//	      if (client.isBroken()) {
//	        this.dataSource.returnBrokenResource(this);
//	      } else {
//	        this.dataSource.returnResource(this);
//	      }
//	    } else {
//	      client.close();
//	    }
//	  }
//
//	  public void setDataSource(JedisPoolAbstract jedisPool) {
//	    this.dataSource = jedisPool;
//	  }
//
//	  @Override
//	  public Long pfadd(final String key, final String... elements) {
//	    checkIsInMultiOrPipeline();
//	    client.pfadd(key, elements);
//	    return client.getIntegerReply();
//	  }
//
//	  @Override
//	  public long pfcount(final String key) {
//	    checkIsInMultiOrPipeline();
//	    client.pfcount(key);
//	    return client.getIntegerReply();
//	  }
//
//	  @Override
//	  public long pfcount(String... keys) {
//	    checkIsInMultiOrPipeline();
//	    client.pfcount(keys);
//	    return client.getIntegerReply();
//	  }
//
//	  @Override
//	  public String pfmerge(final String destkey, final String... sourcekeys) {
//	    checkIsInMultiOrPipeline();
//	    client.pfmerge(destkey, sourcekeys);
//	    return client.getStatusCodeReply();
//	  }
//
//	  @Override
//	  public List<String> blpop(int timeout, String key) {
//	    return blpop(key, String.valueOf(timeout));
//	  }
//
//	  @Override
//	  public List<String> brpop(int timeout, String key) {
//	    return brpop(key, String.valueOf(timeout));
//	  }
//
//	  @Override
//	  public Long geoadd(String key, double longitude, double latitude, String member) {
//	    checkIsInMultiOrPipeline();
//	    client.geoadd(key, longitude, latitude, member);
//	    return client.getIntegerReply();
//	  }
//
//	  @Override
//	  public Long geoadd(String key, Map<String, GeoCoordinate> memberCoordinateMap) {
//	    checkIsInMultiOrPipeline();
//	    client.geoadd(key, memberCoordinateMap);
//	    return client.getIntegerReply();
//	  }
//
//	  @Override
//	  public Double geodist(String key, String member1, String member2) {
//	    checkIsInMultiOrPipeline();
//	    client.geodist(key, member1, member2);
//	    String dval = client.getBulkReply();
//	    return (dval != null ? new Double(dval) : null);
//	  }
//
//	  @Override
//	  public Double geodist(String key, String member1, String member2, GeoUnit unit) {
//	    checkIsInMultiOrPipeline();
//	    client.geodist(key, member1, member2, unit);
//	    String dval = client.getBulkReply();
//	    return (dval != null ? new Double(dval) : null);
//	  }
//
//	  @Override
//	  public List<String> geohash(String key, String... members) {
//	    checkIsInMultiOrPipeline();
//	    client.geohash(key, members);
//	    return client.getMultiBulkReply();
//	  }
//
//	  @Override
//	  public List<GeoCoordinate> geopos(String key, String... members) {
//	    checkIsInMultiOrPipeline();
//	    client.geopos(key, members);
//	    return BuilderFactory.GEO_COORDINATE_LIST.build(client.getObjectMultiBulkReply());
//	  }
//
//	  @Override
//	  public List<GeoRadiusResponse> georadius(String key, double longitude, double latitude,
//	      double radius, GeoUnit unit) {
//	    checkIsInMultiOrPipeline();
//	    client.georadius(key, longitude, latitude, radius, unit);
//	    return BuilderFactory.GEORADIUS_WITH_PARAMS_RESULT.build(client.getObjectMultiBulkReply());
//	  }
//
//	  @Override
//	  public List<GeoRadiusResponse> georadius(String key, double longitude, double latitude,
//	      double radius, GeoUnit unit, GeoRadiusParam param) {
//	    checkIsInMultiOrPipeline();
//	    client.georadius(key, longitude, latitude, radius, unit, param);
//	    return BuilderFactory.GEORADIUS_WITH_PARAMS_RESULT.build(client.getObjectMultiBulkReply());
//	  }
//
//	  @Override
//	  public List<GeoRadiusResponse> georadiusByMember(String key, String member, double radius,
//	      GeoUnit unit) {
//	    checkIsInMultiOrPipeline();
//	    client.georadiusByMember(key, member, radius, unit);
//	    return BuilderFactory.GEORADIUS_WITH_PARAMS_RESULT.build(client.getObjectMultiBulkReply());
//	  }
//
//	  @Override
//	  public List<GeoRadiusResponse> georadiusByMember(String key, String member, double radius,
//	      GeoUnit unit, GeoRadiusParam param) {
//	    checkIsInMultiOrPipeline();
//	    client.georadiusByMember(key, member, radius, unit, param);
//	    return BuilderFactory.GEORADIUS_WITH_PARAMS_RESULT.build(client.getObjectMultiBulkReply());
//	  }
	  public Pipeline pipelined() {
		  init();
		  if(shardedJedis != null)
			  throw new java.lang.UnsupportedOperationException("  sharded Jedis  Unsupport   pipelined  mehtod.");
		  else if(this.jedis != null)
				return jedis.pipelined();
		  else
			  throw new java.lang.UnsupportedOperationException("  Cluster Jedis  Unsupport   pipelined  mehtod.");
	  }
}
