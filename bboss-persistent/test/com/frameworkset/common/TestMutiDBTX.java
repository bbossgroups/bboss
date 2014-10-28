package com.frameworkset.common;

import org.junit.Test;

import com.frameworkset.common.poolman.DBUtil;
import com.frameworkset.common.poolman.SQLExecutor;
import com.frameworkset.orm.transaction.TransactionManager;

public class TestMutiDBTX {
	@Test
	public void testMutiDBTX()
	{
		TransactionManager tm =  new TransactionManager();
		try
		{
			tm.begin();
			SQLExecutor.deleteWithDBName("bspf","delete from table1 where id=1");
			SQLExecutor.updateWithDBName("query","update table1 set value='test' where id=1");
			tm.commit();
			DBUtil.debugStatus();//debug连接池实时状态
		}
		catch(Exception e)
		{
			
		}
		finally
		{
			tm.release();
		}

	}
	
	@Test
	public void testNestedMutiDBTX()
	{
		TransactionManager tm =  new TransactionManager();
		try
		{
			tm.begin();
			SQLExecutor.deleteWithDBName("bspf","delete from table1 where id=1");
			SQLExecutor.updateWithDBName("query","update table1 set value='test' where id=1");
			testMutiDBButSampleDatabaseTX();
			tm.commit();
			DBUtil.debugStatus();//debug连接池实时状态
		}
		catch(Exception e)
		{
			
		}
		finally
		{
			tm.release();
		}

	}
	@Test
	public void testMutiDBButSampleDatabaseTX()
	{
		TransactionManager tm =  new TransactionManager();
		try
		{
			tm.begin();
			SQLExecutor.deleteWithDBName("bspf","delete from table1 where id=1");
			SQLExecutor.updateWithDBName("mq","update table1 set value='test' where id=1");
			tm.commit();
			DBUtil.debugStatus();//debug连接池实时状态
		}
		catch(Exception e)
		{
			
		}
		finally
		{
			tm.release();
		}
		DBUtil.debugStatus();;//debug连接池实时状态

	}

}
