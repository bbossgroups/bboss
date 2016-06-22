package org.frameworkset.nosql;

import org.frameworkset.nosql.redis.RedisFactory;
import org.frameworkset.nosql.redis.RedisHelper;
import org.junit.Test;

public class RedisTest {

	public RedisTest() {
		// TODO Auto-generated constructor stub
	}
	@Test
	public void get()
	{
		RedisHelper redisHelper = null;
		try
		{
			redisHelper = RedisFactory.getRedisHelper();
			redisHelper.set("test", "value1");
			String value = redisHelper.get("test");
			System.out.println("test="+value);
			redisHelper.set("foo", "fasdfasf");
			
			value = redisHelper.get("foo");
			System.out.println("foo="+value);
			value = redisHelper.get("fowwero");
			System.out.println("fowwero="+value);
		}
		finally
		{
			if(redisHelper != null)
				redisHelper.release();
		}
	}
	 

}
