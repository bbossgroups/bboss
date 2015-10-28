package com.frameworkset.common;

import java.sql.SQLException;

import javax.transaction.RollbackException;

import org.junit.Test;

import com.frameworkset.common.poolman.DBUtil;
import com.frameworkset.common.poolman.PreparedDBUtil;
import com.frameworkset.orm.transaction.TransactionException;
import com.frameworkset.orm.transaction.TransactionManager;

/**
 * 
 * 
 * <p>Title: TestPreparedBatch.java</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2007</p>
 *

 * @Date Oct 15, 2008 11:43:03 PM
 * @author biaoping.yin
 * @version 1.0
 */
public class TestPreparedBatch {
	/**
	 * 测试单条语句
	 */
	@Test
	public  void testSingleSTMTBatch()
	{
		PreparedDBUtil pre = new PreparedDBUtil();
		try {
			
			pre.preparedInsert("insert into test(id,name) values(?,?)");
			for(int i = 0; i < 100; i ++)
			{
				pre.setString(1, "" +i);
				pre.setString(2, "name" + i);
				pre.addPreparedBatch();
			}
			pre.executePreparedBatch();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 测试多条语句
	 */
	@Test
	public void testMutiSTMTBatch()
	{
		PreparedDBUtil pre = new PreparedDBUtil();
		TransactionManager tm = new TransactionManager();
		try {
		    tm.begin();
			pre.preparedDelete("delete from test");
			pre.addPreparedBatch();
			pre.preparedInsert("insert into test(id,name) values(?,?)");
			for(int i = 0; i < 10; i ++)
			{
				pre.setString(1, "" +i);
				pre.setString(2, "testMutiSTMTBatch()" + i);
				pre.addPreparedBatch();
			}
			
			pre.preparedInsert("insert into test1(a) values(?)");
			for(int i = 0; i < 10; i ++)
			{
				pre.setString(1, "" + i);
				
				pre.addPreparedBatch();
			}
			pre.preparedInsert("insert into test(id,name) values(?,?)");
			for(int i = 10; i < 20; i ++)
			{
				pre.setString(1, "" +i);
				pre.setString(2, "gggtestMutiSTMTBatch()" + i);
				pre.addPreparedBatch();
			}
			
			pre.executePreparedBatch();
			tm.commit();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally
		{
			tm.release();
		}
		
	}
	
	/**
	 * 测试多条语句,并且通过pre.setBatchOptimize(true);方法进行优化执行预编译操作
	 */
	public @Test void testOptimizeMutiSTMTBatch()
	{
		PreparedDBUtil pre = new PreparedDBUtil();
		
		try {
			pre.setBatchOptimize(true);
			pre.preparedDelete("delete from test");
			pre.addPreparedBatch();
			pre.preparedInsert("insert into test(id,name) values(?,?)");
			for(int i = 0; i < 10; i ++)
			{
				pre.setString(1, "" +i);
				pre.setString(2, "testMutiSTMTBatch()" + i);
				pre.addPreparedBatch();
			}
			
			pre.preparedInsert("insert into test1(a) values(?)");
			for(int i = 0; i < 10; i ++)
			{
				pre.setString(1, "" + i);				
				pre.addPreparedBatch();
			}
			pre.preparedInsert("insert into test(id,name) values(?,?)");
			for(int i = 10; i < 20; i ++)
			{
				pre.setString(1, "" +i);
				pre.setString(2, "testMutiSTMTBatch()" + i);
				pre.addPreparedBatch();
			}
			pre.executePreparedBatch();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 在事务环境中测试多条语句的批处理操作
	 * 参数t的值来测试事务回滚和事务提交操作
	 */
	public static void testTXOptimizeMutiSTMTBatch(int t)
	{
		TransactionManager tm = new TransactionManager();		
		PreparedDBUtil pre = new PreparedDBUtil();
		try {
			tm.begin();
						
			pre.setBatchOptimize(true);
			pre.preparedDelete("delete from test");
			pre.addPreparedBatch();
			pre.preparedDelete("delete from test1");
			pre.addPreparedBatch();
			pre.preparedInsert("insert into test(id,name) values(?,?)");
			for(int i = 0; i < 10; i ++)
			{
				pre.setString(1, "" +i);
				pre.setString(2, "testMutiSTMTBatch()" + i);
				pre.addPreparedBatch();
			}
			
			pre.preparedInsert("insert into test1(a) values(?)");
			for(int i = 0; i < 10; i ++)
			{
				pre.setString(1, "" + i);
				
				pre.addPreparedBatch();
			}
			pre.preparedInsert("insert into test(id,name) values(?,?)");
			for(int i = 10; i < 20; i ++)
			{
				pre.setString(1, "" +i);
				pre.setString(2, "testMutiSTMTBatch()" + i);
				pre.addPreparedBatch();
			}
			
			pre.executePreparedBatch();
			if(t == 0)
			{
				tm.commit();
			}
			else
			{
				tm.rollback();
			}
		} catch (SQLException e) {
			try {
				tm.rollback();
			} catch (RollbackException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			e.printStackTrace();
		} catch (TransactionException e) {
			try {
				tm.rollback();
			} catch (RollbackException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			e.printStackTrace();
		} catch (RollbackException e) {
			try {
				tm.rollback();
			} catch (RollbackException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
		catch (Exception e) {
			try {
				tm.rollback();
			} catch (RollbackException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
		
		
		 
		
	}
	
	public static void main(String[] args)
	{
		
//		TestPreparedBatch.testSingleSTMTBatch();
		testTXOptimizeMutiSTMTBatch(0);
//		testMutiSTMTBatch();		
		DBUtil.debugStatus();
		DBUtil.getNumActive();
		DBUtil.getNumIdle();
		DBUtil.getMaxNumActive();
		System.out.println("freeMemory:" + Runtime.getRuntime().freeMemory() / 1024/1024);
		System.out.println("maxMemory:" + Runtime.getRuntime().maxMemory() / 1024/1024);
		
		System.out.println("totalMemory:" + Runtime.getRuntime().totalMemory()/ 1024/1024);
		System.gc();
	}
	
	
	

}
