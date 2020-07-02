package org.frameworkset.nosql.redis;
/**
 * Copyright 2020 bboss
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import redis.clients.jedis.*;
import redis.clients.jedis.params.sortedset.ZAddParams;
import redis.clients.jedis.params.sortedset.ZIncrByParams;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.frameworkset.nosql.redis.RedisFactory.DEFAULT_REDIS_POOL;

/**
 * <p>Description: </p>
 * <p></p>
 * <p>Copyright (c) 2020</p>
 * @Date 2020/7/1 20:47
 * @author biaoping.yin
 * @version 1.0
 */
public class RedisTool {
	private String redisPoolName;
	private static RedisTool defaultRedisTool;
	private static Map<String,RedisTool> redisToolMap = new HashMap<String, RedisTool>();
	private RedisTool(){
		redisPoolName = DEFAULT_REDIS_POOL;
	}
	private RedisTool(String redisPoolName){
		this.redisPoolName = redisPoolName;
	}
	public static RedisTool getInstance(){
		if(defaultRedisTool != null)
			return defaultRedisTool;
		synchronized (RedisTool.class) {
			if(defaultRedisTool != null)
				return defaultRedisTool;
			return defaultRedisTool = new RedisTool();
		}
	}
	public static RedisTool getInstance(String redisPoolName){
		RedisTool redisTool = redisToolMap.get(redisPoolName);
		if(redisTool != null)
			return redisTool;
		synchronized (RedisTool.class) {
			redisTool = new RedisTool(redisPoolName);
			redisToolMap.put(redisPoolName,redisTool);
		}
		return redisTool;
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
		RedisHelper redisHelper = null;
		try
		{
			redisHelper = RedisFactory.getRedisHelper(redisPoolName);
			return redisHelper.set(  key,   value);
		}
		finally
		{
			if(redisHelper != null)
				redisHelper.release();
		}
	}



	/**
	 * Get the value of the specified key. If the key does not exist null is returned. If the value
	 * stored at key is not a string an error is returned because GET can only handle string values.
	 * <p>
	 * Time complexity: O(1)
	 * @param key
	 * @return Bulk reply
	 */

	public String get(final String key) {
		RedisHelper redisHelper = null;
		try
		{
			redisHelper = RedisFactory.getRedisHelper(redisPoolName);
			return redisHelper.get(    key);
		}
		finally
		{
			if(redisHelper != null)
				redisHelper.release();
		}
	}

	/**
	 * Test if the specified key exists. The command returns the number of keys existed Time
	 * complexity: O(N)
	 * @param keys
	 * @return Integer reply, specifically: an integer greater than 0 if one or more keys were removed
	 *         0 if none of the specified key existed
	 */
	public Long exists(final String... keys) {
		RedisHelper redisHelper = null;
		try
		{
			redisHelper = RedisFactory.getRedisHelper(redisPoolName);
			return redisHelper.exists( keys);
		}
		finally
		{
			if(redisHelper != null)
				redisHelper.release();
		}
	}

	/**
	 * Test if the specified key exists. The command returns "1" if the key exists, otherwise "0" is
	 * returned. Note that even keys set with an empty string as value will return "1". Time
	 * complexity: O(1)
	 * @param key
	 * @return Boolean reply, true if the key exists, otherwise false
	 */

	public Boolean exists(final String key) {
		RedisHelper redisHelper = null;
		try
		{
			redisHelper = RedisFactory.getRedisHelper(redisPoolName);
			return redisHelper.exists(  key) ;
		}
		finally
		{
			if(redisHelper != null)
				redisHelper.release();
		}
	}

	/**
	 * Remove the specified keys. If a given key does not exist no operation is performed for this
	 * key. The command returns the number of keys removed. Time complexity: O(1)
	 * @param keys
	 * @return Integer reply, specifically: an integer greater than 0 if one or more keys were removed
	 *         0 if none of the specified key existed
	 */

	public Long del(final String... keys) {
		RedisHelper redisHelper = null;
		try
		{
			redisHelper = RedisFactory.getRedisHelper(redisPoolName);
			return redisHelper.del(  keys) ;
		}
		finally
		{
			if(redisHelper != null)
				redisHelper.release();
		}
	}


	public Long del(String key) {
		RedisHelper redisHelper = null;
		try
		{
			redisHelper = RedisFactory.getRedisHelper(redisPoolName);
			return redisHelper.del(  key) ;
		}
		finally
		{
			if(redisHelper != null)
				redisHelper.release();
		}
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
		RedisHelper redisHelper = null;
		try
		{
			redisHelper = RedisFactory.getRedisHelper(redisPoolName);
			return redisHelper.type( key);
		}
		finally
		{
			if(redisHelper != null)
				redisHelper.release();
		}
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
		RedisHelper redisHelper = null;
		try
		{
			redisHelper = RedisFactory.getRedisHelper(redisPoolName);
			return redisHelper.keys(  pattern);
		}
		finally
		{
			if(redisHelper != null)
				redisHelper.release();
		}
	}

	/**
	 * Return a randomly selected key from the currently selected DB.
	 * <p>
	 * Time complexity: O(1)
	 * @return Singe line reply, specifically the randomly selected key or an empty string is the
	 *         database is empty
	 */

	public String randomKey() {
		RedisHelper redisHelper = null;
		try
		{
			redisHelper = RedisFactory.getRedisHelper(redisPoolName);
			return redisHelper.randomKey(   );
		}
		finally
		{
			if(redisHelper != null)
				redisHelper.release();
		}
	}


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
		RedisHelper redisHelper = null;
		try
		{
			redisHelper = RedisFactory.getRedisHelper(redisPoolName);
			return redisHelper.expire(  key, seconds) ;
		}
		finally
		{
			if(redisHelper != null)
				redisHelper.release();
		}
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
		RedisHelper redisHelper = null;
		try
		{
			redisHelper = RedisFactory.getRedisHelper(redisPoolName);
			return redisHelper.expireAt(  key,   unixTime);
		}
		finally
		{
			if(redisHelper != null)
				redisHelper.release();
		}
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
		RedisHelper redisHelper = null;
		try
		{
			redisHelper = RedisFactory.getRedisHelper(redisPoolName);
			return redisHelper.ttl(  key);
		}
		finally
		{
			if(redisHelper != null)
				redisHelper.release();
		}
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
		RedisHelper redisHelper = null;
		try
		{
			redisHelper = RedisFactory.getRedisHelper(redisPoolName);
			return redisHelper.getSet(  key,   value);
		}
		finally
		{
			if(redisHelper != null)
				redisHelper.release();
		}
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
		RedisHelper redisHelper = null;
		try
		{
			redisHelper = RedisFactory.getRedisHelper(redisPoolName);
			return redisHelper.mget( keys);
		}
		finally
		{
			if(redisHelper != null)
				redisHelper.release();
		}
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
		RedisHelper redisHelper = null;
		try
		{
			redisHelper = RedisFactory.getRedisHelper(redisPoolName);
			return redisHelper.setnx(  key,  value) ;
		}
		finally
		{
			if(redisHelper != null)
				redisHelper.release();
		}
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
		RedisHelper redisHelper = null;
		try
		{
			redisHelper = RedisFactory.getRedisHelper(redisPoolName);
			return redisHelper.setex(  key,   seconds,   value);
		}
		finally
		{
			if(redisHelper != null)
				redisHelper.release();
		}
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
		RedisHelper redisHelper = null;
		try
		{
			redisHelper = RedisFactory.getRedisHelper(redisPoolName);
			return redisHelper.mset(  keysvalues);
		}
		finally
		{
			if(redisHelper != null)
				redisHelper.release();
		}
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
		RedisHelper redisHelper = null;
		try
		{
			redisHelper = RedisFactory.getRedisHelper(redisPoolName);
			return redisHelper.msetnx(  keysvalues);
		}
		finally
		{
			if(redisHelper != null)
				redisHelper.release();
		}
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
		RedisHelper redisHelper = null;
		try
		{
			redisHelper = RedisFactory.getRedisHelper(redisPoolName);
			return redisHelper.decrBy(  key,  integer);
		}
		finally
		{
			if(redisHelper != null)
				redisHelper.release();
		}
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
		RedisHelper redisHelper = null;
		try
		{
			redisHelper = RedisFactory.getRedisHelper(redisPoolName);
			return redisHelper.decr(  key) ;
		}
		finally
		{
			if(redisHelper != null)
				redisHelper.release();
		}
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
		RedisHelper redisHelper = null;
		try
		{
			redisHelper = RedisFactory.getRedisHelper(redisPoolName);
			return redisHelper.incrBy( key,  integer);
		}
		finally
		{
			if(redisHelper != null)
				redisHelper.release();
		}
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
		RedisHelper redisHelper = null;
		try
		{
			redisHelper = RedisFactory.getRedisHelper(redisPoolName);
			return redisHelper.incrByFloat(  key,   value);
		}
		finally
		{
			if(redisHelper != null)
				redisHelper.release();
		}
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
		RedisHelper redisHelper = null;
		try
		{
			redisHelper = RedisFactory.getRedisHelper(redisPoolName);
			return redisHelper.incr( key);
		}
		finally
		{
			if(redisHelper != null)
				redisHelper.release();
		}
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
		RedisHelper redisHelper = null;
		try
		{
			redisHelper = RedisFactory.getRedisHelper(redisPoolName);
			return redisHelper.append(  key,   value);
		}
		finally
		{
			if(redisHelper != null)
				redisHelper.release();
		}
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
		RedisHelper redisHelper = null;
		try
		{
			redisHelper = RedisFactory.getRedisHelper(redisPoolName);
			return redisHelper.substr( key,  start,   end) ;
		}
		finally
		{
			if(redisHelper != null)
				redisHelper.release();
		}
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
		RedisHelper redisHelper = null;
		try
		{
			redisHelper = RedisFactory.getRedisHelper(redisPoolName);
			return redisHelper.hset(  key,   field,   value) ;
		}
		finally
		{
			if(redisHelper != null)
				redisHelper.release();
		}
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
		RedisHelper redisHelper = null;
		try
		{
			redisHelper = RedisFactory.getRedisHelper(redisPoolName);
			return redisHelper.hget(  key,   field);
		}
		finally
		{
			if(redisHelper != null)
				redisHelper.release();
		}
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
		RedisHelper redisHelper = null;
		try
		{
			redisHelper = RedisFactory.getRedisHelper(redisPoolName);
			return redisHelper.hsetnx(  key,   field,  value);
		}
		finally
		{
			if(redisHelper != null)
				redisHelper.release();
		}
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
		RedisHelper redisHelper = null;
		try
		{
			redisHelper = RedisFactory.getRedisHelper(redisPoolName);
			return redisHelper.hmset( key,   hash);
		}
		finally
		{
			if(redisHelper != null)
				redisHelper.release();
		}
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
		RedisHelper redisHelper = null;
		try
		{
			redisHelper = RedisFactory.getRedisHelper(redisPoolName);
			return redisHelper.hmget(  key,   fields);
		}
		finally
		{
			if(redisHelper != null)
				redisHelper.release();
		}
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
		RedisHelper redisHelper = null;
		try
		{
			redisHelper = RedisFactory.getRedisHelper(redisPoolName);
			return redisHelper.hincrBy(  key,   field,  value) ;
		}
		finally
		{
			if(redisHelper != null)
				redisHelper.release();
		}
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
		RedisHelper redisHelper = null;
		try
		{
			redisHelper = RedisFactory.getRedisHelper(redisPoolName);
			return redisHelper.hincrByFloat(  key,   field,   value) ;
		}
		finally
		{
			if(redisHelper != null)
				redisHelper.release();
		}
	}

	/**
	 * Test for existence of a specified field in a hash. <b>Time complexity:</b> O(1)
	 * @param key
	 * @param field
	 * @return Return 1 if the hash stored at key contains the specified field. Return 0 if the key is
	 *         not found or the field is not present.
	 */
	public Boolean hexists(final String key, final String field) {
		RedisHelper redisHelper = null;
		try
		{
			redisHelper = RedisFactory.getRedisHelper(redisPoolName);
			return redisHelper.hexists(  key,   field);
		}
		finally
		{
			if(redisHelper != null)
				redisHelper.release();
		}
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
		RedisHelper redisHelper = null;
		try
		{
			redisHelper = RedisFactory.getRedisHelper(redisPoolName);
			return redisHelper.hdel(  key,   fields);
		}
		finally
		{
			if(redisHelper != null)
				redisHelper.release();
		}
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
		RedisHelper redisHelper = null;
		try
		{
			redisHelper = RedisFactory.getRedisHelper(redisPoolName);
			return redisHelper.hlen(  key);
		}
		finally
		{
			if(redisHelper != null)
				redisHelper.release();
		}
	}

	/**
	 * Return all the fields in a hash.
	 * <p>
	 * <b>Time complexity:</b> O(N), where N is the total number of entries
	 * @param key
	 * @return All the fields names contained into a hash.
	 */
	public Set<String> hkeys(final String key) {
		RedisHelper redisHelper = null;
		try
		{
			redisHelper = RedisFactory.getRedisHelper(redisPoolName);
			return redisHelper.hkeys( key);
		}
		finally
		{
			if(redisHelper != null)
				redisHelper.release();
		}
	}

	/**
	 * Return all the values in a hash.
	 * <p>
	 * <b>Time complexity:</b> O(N), where N is the total number of entries
	 * @param key
	 * @return All the fields values contained into a hash.
	 */
	public List<String> hvals(final String key) {
		RedisHelper redisHelper = null;
		try
		{
			redisHelper = RedisFactory.getRedisHelper(redisPoolName);
			return redisHelper.hvals(  key);
		}
		finally
		{
			if(redisHelper != null)
				redisHelper.release();
		}
	}

	/**
	 * Return all the fields and associated values in a hash.
	 * <p>
	 * <b>Time complexity:</b> O(N), where N is the total number of entries
	 * @param key
	 * @return All the fields and values contained into a hash.
	 */
	public Map<String, String> hgetAll(final String key) {
		RedisHelper redisHelper = null;
		try
		{
			redisHelper = RedisFactory.getRedisHelper(redisPoolName);
			return redisHelper.hgetAll(  key);
		}
		finally
		{
			if(redisHelper != null)
				redisHelper.release();
		}
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
		RedisHelper redisHelper = null;
		try
		{
			redisHelper = RedisFactory.getRedisHelper(redisPoolName);
			return redisHelper.rpush(  key,   strings);
		}
		finally
		{
			if(redisHelper != null)
				redisHelper.release();
		}
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
		RedisHelper redisHelper = null;
		try
		{
			redisHelper = RedisFactory.getRedisHelper(redisPoolName);
			return redisHelper.lpush( key,  strings);
		}
		finally
		{
			if(redisHelper != null)
				redisHelper.release();
		}
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
		RedisHelper redisHelper = null;
		try
		{
			redisHelper = RedisFactory.getRedisHelper(redisPoolName);
			return redisHelper.llen(  key);
		}
		finally
		{
			if(redisHelper != null)
				redisHelper.release();
		}
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
		RedisHelper redisHelper = null;
		try
		{
			redisHelper = RedisFactory.getRedisHelper(redisPoolName);
			return redisHelper.lrange( key,   start,   end);
		}
		finally
		{
			if(redisHelper != null)
				redisHelper.release();
		}
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
		RedisHelper redisHelper = null;
		try
		{
			redisHelper = RedisFactory.getRedisHelper(redisPoolName);
			return redisHelper.ltrim(  key,  start,  end);
		}
		finally
		{
			if(redisHelper != null)
				redisHelper.release();
		}
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
		RedisHelper redisHelper = null;
		try
		{
			redisHelper = RedisFactory.getRedisHelper(redisPoolName);
			return redisHelper.lindex(  key,   index);
		}
		finally
		{
			if(redisHelper != null)
				redisHelper.release();
		}
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
		RedisHelper redisHelper = null;
		try
		{
			redisHelper = RedisFactory.getRedisHelper(redisPoolName);
			return redisHelper.lset(  key,   index,  value);
		}
		finally
		{
			if(redisHelper != null)
				redisHelper.release();
		}
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
		RedisHelper redisHelper = null;
		try
		{
			redisHelper = RedisFactory.getRedisHelper(redisPoolName);
			return redisHelper.lrem(  key,   count,   value);
		}
		finally
		{
			if(redisHelper != null)
				redisHelper.release();
		}
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
		RedisHelper redisHelper = null;
		try
		{
			redisHelper = RedisFactory.getRedisHelper(redisPoolName);
			return redisHelper.lpop(  key);
		}
		finally
		{
			if(redisHelper != null)
				redisHelper.release();
		}
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
		RedisHelper redisHelper = null;
		try
		{
			redisHelper = RedisFactory.getRedisHelper(redisPoolName);
			return redisHelper.rpop(  key);
		}
		finally
		{
			if(redisHelper != null)
				redisHelper.release();
		}
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
		RedisHelper redisHelper = null;
		try
		{
			redisHelper = RedisFactory.getRedisHelper(redisPoolName);
			return redisHelper.rpoplpush( srckey,   dstkey);
		}
		finally
		{
			if(redisHelper != null)
				redisHelper.release();
		}
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
		RedisHelper redisHelper = null;
		try
		{
			redisHelper = RedisFactory.getRedisHelper(redisPoolName);
			return redisHelper.sadd(  key,  members);
		}
		finally
		{
			if(redisHelper != null)
				redisHelper.release();
		}
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
		RedisHelper redisHelper = null;
		try
		{
			redisHelper = RedisFactory.getRedisHelper(redisPoolName);
			return redisHelper.smembers(    key);
		}
		finally
		{
			if(redisHelper != null)
				redisHelper.release();
		}
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
		RedisHelper redisHelper = null;
		try
		{
			redisHelper = RedisFactory.getRedisHelper(redisPoolName);
			return redisHelper.srem(  key,     members);
		}
		finally
		{
			if(redisHelper != null)
				redisHelper.release();
		}
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
		RedisHelper redisHelper = null;
		try
		{
			redisHelper = RedisFactory.getRedisHelper(redisPoolName);
			return redisHelper.spop(  key);
		}
		finally
		{
			if(redisHelper != null)
				redisHelper.release();
		}
	}

	public Set<String> spop(final String key, final long count) {
		RedisHelper redisHelper = null;
		try
		{
			redisHelper = RedisFactory.getRedisHelper(redisPoolName);
			return redisHelper.spop(  key,   count) ;
		}
		finally
		{
			if(redisHelper != null)
				redisHelper.release();
		}
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
		RedisHelper redisHelper = null;
		try
		{
			redisHelper = RedisFactory.getRedisHelper(redisPoolName);
			return redisHelper.smove(  srckey,   dstkey,  member);
		}
		finally
		{
			if(redisHelper != null)
				redisHelper.release();
		}
	}

	/**
	 * Return the set cardinality (number of elements). If the key does not exist 0 is returned, like
	 * for empty sets.
	 * @param key
	 * @return Integer reply, specifically: the cardinality (number of elements) of the set as an
	 *         integer.
	 */
	public Long scard(final String key) {
		RedisHelper redisHelper = null;
		try
		{
			redisHelper = RedisFactory.getRedisHelper(redisPoolName);
			return redisHelper.scard(  key) ;
		}
		finally
		{
			if(redisHelper != null)
				redisHelper.release();
		}
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
		RedisHelper redisHelper = null;
		try
		{
			redisHelper = RedisFactory.getRedisHelper(redisPoolName);
			return redisHelper.sismember( key,   member);
		}
		finally
		{
			if(redisHelper != null)
				redisHelper.release();
		}
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
		RedisHelper redisHelper = null;
		try
		{
			redisHelper = RedisFactory.getRedisHelper(redisPoolName);
			return redisHelper.sinter( keys) ;
		}
		finally
		{
			if(redisHelper != null)
				redisHelper.release();
		}
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
		RedisHelper redisHelper = null;
		try
		{
			redisHelper = RedisFactory.getRedisHelper(redisPoolName);
			return redisHelper.sinterstore(  dstkey,   keys);
		}
		finally
		{
			if(redisHelper != null)
				redisHelper.release();
		}
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
		RedisHelper redisHelper = null;
		try
		{
			redisHelper = RedisFactory.getRedisHelper(redisPoolName);
			return redisHelper.sunion(  keys) ;
		}
		finally
		{
			if(redisHelper != null)
				redisHelper.release();
		}
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
		RedisHelper redisHelper = null;
		try
		{
			redisHelper = RedisFactory.getRedisHelper(redisPoolName);
			return redisHelper.sunionstore(    dstkey,    keys);
		}
		finally
		{
			if(redisHelper != null)
				redisHelper.release();
		}
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
		RedisHelper redisHelper = null;
		try
		{
			redisHelper = RedisFactory.getRedisHelper(redisPoolName);
			return redisHelper.sdiff(   keys);
		}
		finally
		{
			if(redisHelper != null)
				redisHelper.release();
		}
	}

	/**
	 * This command works exactly like {@link #sdiff(String...) SDIFF} but instead of being returned
	 * the resulting set is stored in dstkey.
	 * @param dstkey
	 * @param keys
	 * @return Status code reply
	 */

	public Long sdiffstore(final String dstkey, final String... keys) {
		RedisHelper redisHelper = null;
		try
		{
			redisHelper = RedisFactory.getRedisHelper(redisPoolName);
			return redisHelper.sdiffstore(    dstkey,  keys) ;
		}
		finally
		{
			if(redisHelper != null)
				redisHelper.release();
		}
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
		RedisHelper redisHelper = null;
		try
		{
			redisHelper = RedisFactory.getRedisHelper(redisPoolName);
			return redisHelper.srandmember(    key);
		}
		finally
		{
			if(redisHelper != null)
				redisHelper.release();
		}
	}

	public List<String> srandmember(final String key, final int count) {
		RedisHelper redisHelper = null;
		try
		{
			redisHelper = RedisFactory.getRedisHelper(redisPoolName);
			return redisHelper.srandmember(    key,     count) ;
		}
		finally
		{
			if(redisHelper != null)
				redisHelper.release();
		}
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
		RedisHelper redisHelper = null;
		try
		{
			redisHelper = RedisFactory.getRedisHelper(redisPoolName);
			return redisHelper.zadd(  key,   score,  member);
		}
		finally
		{
			if(redisHelper != null)
				redisHelper.release();
		}
	}


	public Long zadd(final String key, final double score, final String member,
					 final ZAddParams params) {
		RedisHelper redisHelper = null;
		try
		{
			redisHelper = RedisFactory.getRedisHelper(redisPoolName);
			return redisHelper.zadd(  key,   score,   member,
			 params);
		}
		finally
		{
			if(redisHelper != null)
				redisHelper.release();
		}
	}

	public Long zadd(final String key, final Map<String, Double> scoreMembers) {
		RedisHelper redisHelper = null;
		try
		{
			redisHelper = RedisFactory.getRedisHelper(redisPoolName);
			return redisHelper.zadd(  key,  scoreMembers);
		}
		finally
		{
			if(redisHelper != null)
				redisHelper.release();
		}
	}

	public Long zadd(String key, Map<String, Double> scoreMembers, ZAddParams params) {
		RedisHelper redisHelper = null;
		try
		{
			redisHelper = RedisFactory.getRedisHelper(redisPoolName);
			return redisHelper.zadd(  key,   scoreMembers,   params);
		}
		finally
		{
			if(redisHelper != null)
				redisHelper.release();
		}
	}

	public Set<String> zrange(final String key, final long start, final long end) {
		RedisHelper redisHelper = null;
		try
		{
			redisHelper = RedisFactory.getRedisHelper(redisPoolName);
			return redisHelper.zrange(  key,  start,   end);
		}
		finally
		{
			if(redisHelper != null)
				redisHelper.release();
		}
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
		RedisHelper redisHelper = null;
		try
		{
			redisHelper = RedisFactory.getRedisHelper(redisPoolName);
			return redisHelper.zrem(  key,   members);
		}
		finally
		{
			if(redisHelper != null)
				redisHelper.release();
		}
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
		RedisHelper redisHelper = null;
		try
		{
			redisHelper = RedisFactory.getRedisHelper(redisPoolName);
			return redisHelper.zincrby(  key,  score,  member);
		}
		finally
		{
			if(redisHelper != null)
				redisHelper.release();
		}
	}

	public Double zincrby(String key, double score, String member, ZIncrByParams params) {
		RedisHelper redisHelper = null;
		try
		{
			redisHelper = RedisFactory.getRedisHelper(redisPoolName);
			return redisHelper.zincrby(  key,   score,   member,   params);
		}
		finally
		{
			if(redisHelper != null)
				redisHelper.release();
		}
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
		RedisHelper redisHelper = null;
		try
		{
			redisHelper = RedisFactory.getRedisHelper(redisPoolName);
			return redisHelper.zrank(    key,  member);
		}
		finally
		{
			if(redisHelper != null)
				redisHelper.release();
		}
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
		RedisHelper redisHelper = null;
		try
		{
			redisHelper = RedisFactory.getRedisHelper(redisPoolName);
			return redisHelper.zcard(    key);
		}
		finally
		{
			if(redisHelper != null)
				redisHelper.release();
		}
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
		RedisHelper redisHelper = null;
		try
		{
			redisHelper = RedisFactory.getRedisHelper(redisPoolName);
			return redisHelper.zscore(    key,     member)  ;
		}
		finally
		{
			if(redisHelper != null)
				redisHelper.release();
		}
	}
	//
//	  @Override
	public String watch(final String... keys) {
		RedisHelper redisHelper = null;
		try
		{
			redisHelper = RedisFactory.getRedisHelper(redisPoolName);
			return redisHelper.watch( keys);
		}
		finally
		{
			if(redisHelper != null)
				redisHelper.release();
		}
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
		RedisHelper redisHelper = null;
		try
		{
			redisHelper = RedisFactory.getRedisHelper(redisPoolName);
			return redisHelper.sort(  key);
		}
		finally
		{
			if(redisHelper != null)
				redisHelper.release();
		}
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
		RedisHelper redisHelper = null;
		try
		{
			redisHelper = RedisFactory.getRedisHelper(redisPoolName);
			return redisHelper.sort(  key,   sortingParameters);
		}
		finally
		{
			if(redisHelper != null)
				redisHelper.release();
		}
	}


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
		RedisHelper redisHelper = null;
		try
		{
			redisHelper = RedisFactory.getRedisHelper(redisPoolName);
			return redisHelper.sort(  key,  sortingParameters,   dstkey);
		}
		finally
		{
			if(redisHelper != null)
				redisHelper.release();
		}
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
		RedisHelper redisHelper = null;
		try
		{
			redisHelper = RedisFactory.getRedisHelper(redisPoolName);
			return redisHelper.sort(  key,   dstkey );
		}
		finally
		{
			if(redisHelper != null)
				redisHelper.release();
		}
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
		RedisHelper redisHelper = null;
		try
		{
			redisHelper = RedisFactory.getRedisHelper(redisPoolName);
			return redisHelper.brpop(  timeout,   keys);
		}
		finally
		{
			if(redisHelper != null)
				redisHelper.release();
		}
	}

	public Long zcount(final String key, final double min, final double max) {
		RedisHelper redisHelper = null;
		try
		{
			redisHelper = RedisFactory.getRedisHelper(redisPoolName);
			return redisHelper.zcount(    key,     min,     max);
		}
		finally
		{
			if(redisHelper != null)
				redisHelper.release();
		}
	}

	public Long zcount(final String key, final String min, final String max) {
		RedisHelper redisHelper = null;
		try
		{
			redisHelper = RedisFactory.getRedisHelper(redisPoolName);
			return redisHelper.zcount(    key,     min,   max);
		}
		finally
		{
			if(redisHelper != null)
				redisHelper.release();
		}
	}


	public Long strlen(final String key) {
		RedisHelper redisHelper = null;
		try
		{
			redisHelper = RedisFactory.getRedisHelper(redisPoolName);
			return redisHelper.strlen(  key);
		}
		finally
		{
			if(redisHelper != null)
				redisHelper.release();
		}
	}

	public Long lpushx(final String key, final String... string) {
		RedisHelper redisHelper = null;
		try
		{
			redisHelper = RedisFactory.getRedisHelper(redisPoolName);
			return redisHelper.lpushx(  key,   string);
		}
		finally
		{
			if(redisHelper != null)
				redisHelper.release();
		}
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
		RedisHelper redisHelper = null;
		try
		{
			redisHelper = RedisFactory.getRedisHelper(redisPoolName);
			return redisHelper.persist(  key);
		}
		finally
		{
			if(redisHelper != null)
				redisHelper.release();
		}
	}

	public Long rpushx(final String key, final String... string) {
		RedisHelper redisHelper = null;
		try
		{
			redisHelper = RedisFactory.getRedisHelper(redisPoolName);
			return redisHelper.rpushx(  key,   string);
		}
		finally
		{
			if(redisHelper != null)
				redisHelper.release();
		}
	}


	public String echo(final String string) {
		RedisHelper redisHelper = null;
		try
		{
			redisHelper = RedisFactory.getRedisHelper(redisPoolName);
			return redisHelper.echo(  string);
		}
		finally
		{
			if(redisHelper != null)
				redisHelper.release();
		}
	}


	public Long linsert(final String key, final BinaryClient.LIST_POSITION where, final String pivot,
						final String value) {
		RedisHelper redisHelper = null;
		try
		{
			redisHelper = RedisFactory.getRedisHelper(redisPoolName);
			return redisHelper.linsert( key,  where,   pivot,
			  value);
		}
		finally
		{
			if(redisHelper != null)
				redisHelper.release();
		}
	}


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
		RedisHelper redisHelper = null;
		try
		{
			redisHelper = RedisFactory.getRedisHelper(redisPoolName);
			return redisHelper.configGet(  pattern);
		}
		finally
		{
			if(redisHelper != null)
				redisHelper.release();
		}
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
		RedisHelper redisHelper = null;
		try
		{
			redisHelper = RedisFactory.getRedisHelper(redisPoolName);
			return redisHelper.eval(  script,   keyCount,   params);
		}
		finally
		{
			if(redisHelper != null)
				redisHelper.release();
		}
	}

	public void subscribe(final JedisPubSub jedisPubSub, final String... channels) {
		RedisHelper redisHelper = null;
		try
		{
			redisHelper = RedisFactory.getRedisHelper(redisPoolName);
			redisHelper.subscribe(  jedisPubSub,  channels) ;
		}
		finally
		{
			if(redisHelper != null)
				redisHelper.release();
		}
	}


	public Long publish(final String channel, final String message) {
		RedisHelper redisHelper = null;
		try
		{
			redisHelper = RedisFactory.getRedisHelper(redisPoolName);
			return redisHelper.publish(   channel,  message);
		}
		finally
		{
			if(redisHelper != null)
				redisHelper.release();
		}
	}

	public void psubscribe(final JedisPubSub jedisPubSub, final String... patterns) {
		RedisHelper redisHelper = null;
		try
		{
			redisHelper = RedisFactory.getRedisHelper(redisPoolName);
			redisHelper.psubscribe(  jedisPubSub,   patterns);
		}
		finally
		{
			if(redisHelper != null)
				redisHelper.release();
		}
	}




	public Long pexpire(final String key, final long milliseconds) {
		RedisHelper redisHelper = null;
		try
		{
			redisHelper = RedisFactory.getRedisHelper(redisPoolName);
			return redisHelper.pexpire(  key,milliseconds);
		}
		finally
		{
			if(redisHelper != null)
				redisHelper.release();
		}
	}

	public Long pexpireAt(final String key, final long millisecondsTimestamp) {
		RedisHelper redisHelper = null;
		try
		{
			redisHelper = RedisFactory.getRedisHelper(redisPoolName);
			return redisHelper.pexpireAt(  key,millisecondsTimestamp);
		}
		finally
		{
			if(redisHelper != null)
				redisHelper.release();
		}
	}

	public Long pttl(final String key) {
		RedisHelper redisHelper = null;
		try
		{
			redisHelper = RedisFactory.getRedisHelper(redisPoolName);
			return redisHelper.pttl(  key);
		}
		finally
		{
			if(redisHelper != null)
				redisHelper.release();
		}
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
		RedisHelper redisHelper = null;
		try
		{
			redisHelper = RedisFactory.getRedisHelper(redisPoolName);
			return redisHelper.psetex(  key,  milliseconds,  value);
		}
		finally
		{
			if(redisHelper != null)
				redisHelper.release();
		}
	}

	public Pipeline pipelined() {
		RedisHelper redisHelper = null;
		try
		{
			redisHelper = RedisFactory.getRedisHelper(redisPoolName);
			return redisHelper.pipelined();
		}
		finally
		{
			if(redisHelper != null)
				redisHelper.release();
		}
	}
}
