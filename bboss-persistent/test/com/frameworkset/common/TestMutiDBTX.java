package com.frameworkset.common;

import javax.transaction.RollbackException;

import org.junit.Test;

import com.frameworkset.common.poolman.DBUtil;
import com.frameworkset.orm.transaction.TransactionManager;

public class TestMutiDBTX {
	@Test
	public void testMutiDBTX()
	{
		TransactionManager tm =  new TransactionManager();
		try
		{
			tm.begin();
			DBUtil db = new DBUtil();
			db.executeDelete("bspf","delete from table1 where id=1");
			db.executeUpdate("query","update table1 set value='test' where id=1");
			tm.commit();
			DBUtil.debugStatus();
		}
		catch(Exception e)
		{
			try {
				tm.rollback();
			} catch (RollbackException e1) {
				
				e1.printStackTrace();
			}
		}

	}
	
	@Test
	public void testNestedMutiDBTX()
	{
		TransactionManager tm =  new TransactionManager();
		try
		{
			tm.begin();
			DBUtil db = new DBUtil();
			db.executeDelete("bspf","delete from table1 where id=1");
			db.executeUpdate("query","update table1 set value='test' where id=1");
			testMutiDBButSampleDatabaseTX();
			tm.commit();
			DBUtil.debugStatus();
		}
		catch(Exception e)
		{
			try {
				tm.rollback();
			} catch (RollbackException e1) {
				
				e1.printStackTrace();
			}
		}

	}
	@Test
	public void testMutiDBButSampleDatabaseTX()
	{
		TransactionManager tm =  new TransactionManager();
		try
		{
			tm.begin();
			DBUtil db = new DBUtil();
			db.executeDelete("bspf","delete from table1 where id=1");
			db.executeUpdate("mq","update table1 set value='test' where id=1");
			tm.commit();
			DBUtil.debugStatus();
		}
		catch(Exception e)
		{
			try {
				tm.rollback();
			} catch (RollbackException e1) {
				
				e1.printStackTrace();
			}
		}
		DBUtil.debugStatus();

	}

}
