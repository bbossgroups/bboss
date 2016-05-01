package com.frameworkset.common;

import java.sql.SQLException;

import javax.transaction.RollbackException;

import org.junit.Test;

import com.frameworkset.common.poolman.CallableDBUtil;
import com.frameworkset.common.poolman.PreparedDBUtil;
import com.frameworkset.common.poolman.Record;
import com.frameworkset.common.poolman.handle.RowHandler;
import com.frameworkset.orm.transaction.TransactionManager;

public class TestCallableDBUtil {
	public @Test void testTest_pWithPositionIndex()
	{
		CallableDBUtil callableDBUtil = new CallableDBUtil();
		try
		{
//			callableDBUtil.execute("{call test_p(1,'ss',3,4)}");
			callableDBUtil.prepareCallable("{call test_p(?,?,?,?,?)}");
			callableDBUtil.setInt(1, 10);
			callableDBUtil.registerOutParameter(2, java.sql.Types.VARCHAR);
			callableDBUtil.registerOutParameter(3, java.sql.Types.VARCHAR);
			callableDBUtil.registerOutParameter(4, java.sql.Types.INTEGER);
			callableDBUtil.registerOutParameter(5, java.sql.Types.INTEGER);
			callableDBUtil.executeCallable();
			System.out.println("name1:" + callableDBUtil.getString(2));
			System.out.println("name2:" + callableDBUtil.getString(3));
			System.out.println("test:" + callableDBUtil.getInt(4));
			System.out.println("nomath:" + callableDBUtil.getInt(5));
			
						
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		
	}
	
	public @Test void testTest_pWithNameIndex()
	{
		CallableDBUtil callableDBUtil = new CallableDBUtil();
		try
		{
			callableDBUtil.prepareCallable("{call test_p(?,?,?,?,?)}");
			//不允许的操作: Ordinal binding and Named binding cannot be combined!
			callableDBUtil.setInt("id", 10);
			callableDBUtil.registerOutParameter("name", java.sql.Types.VARCHAR);
			callableDBUtil.registerOutParameter("name1", java.sql.Types.VARCHAR);
			callableDBUtil.registerOutParameter("test", java.sql.Types.INTEGER);
			callableDBUtil.registerOutParameter("nomatch", java.sql.Types.INTEGER);
			callableDBUtil.executeCallable();
			System.out.println("name1:" + callableDBUtil.getString("name"));
			System.out.println("name2:" + callableDBUtil.getString("name1"));
			System.out.println("test:" + callableDBUtil.getInt("test"));
			System.out.println("nomatch:" + callableDBUtil.getInt("nomatch"));
					
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		
	}
	
	public @Test void testTest_pWithNameIndexForXMLString()
	{
		CallableDBUtil callableDBUtil = new CallableDBUtil();
		try
		{
			callableDBUtil.prepareCallable("{call test_p(?,?,?,?,?)}");
			//不允许的操作: Ordinal binding and Named binding cannot be combined!
			callableDBUtil.setInt("id", 10);
			callableDBUtil.registerOutParameter("name", java.sql.Types.VARCHAR);
			callableDBUtil.registerOutParameter("name1", java.sql.Types.VARCHAR);
			callableDBUtil.registerOutParameter("test", java.sql.Types.INTEGER);
			callableDBUtil.registerOutParameter("nomatch", java.sql.Types.INTEGER);
			
			String xmlString = callableDBUtil.executeCallableForXML();
			System.out.println("xmlString:" + xmlString);
//			System.out.println("name1:" + callableDBUtil.getString("name"));
//			System.out.println("name2:" + callableDBUtil.getString("name1"));
//			System.out.println("test:" + callableDBUtil.getInt("test"));
			
					
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		
	}
	
	public @Test void testTest_pWithNameIndexForXMLStringRowHandler()
	{
		CallableDBUtil callableDBUtil = new CallableDBUtil();
		try
		{
			callableDBUtil.prepareCallable("{call test_p(?,?,?,?,?)}");
			//不允许的操作: Ordinal binding and Named binding cannot be combined!
			callableDBUtil.setInt("id", 10);
			callableDBUtil.registerOutParameter("name", java.sql.Types.VARCHAR);
			callableDBUtil.registerOutParameter("name1", java.sql.Types.VARCHAR);
			callableDBUtil.registerOutParameter("test", java.sql.Types.INTEGER);
			callableDBUtil.registerOutParameter("nomatch", java.sql.Types.INTEGER);
			String xmlString = callableDBUtil.executeCallableForXML(new com.frameworkset.common.poolman.handle.RowHandler()
			{
				/**
				 * 对已经处理好的行记录进行处理的逻辑
				 * @param rowValue
				 */
				public void handleRow(Object rowValue,Record origine)
				{
					Object objects = (Object)rowValue;
					
					System.out.println("objects rowhandler:" + objects);
//					System.out.println("objects【0】:" + objects[0]);
//					System.out.println(objects[1]);
				}
			});
			System.out.println("name1:" + callableDBUtil.getString("name"));
			System.out.println("name2:" + callableDBUtil.getString("name1"));
			System.out.println("xmlString:" + xmlString);
					
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		
	}
	
	public @Test void testTest_pWithNameIndexForObject()
	{
		CallableDBUtil callableDBUtil = new CallableDBUtil();
		try
		{
			callableDBUtil.prepareCallable("{call test_p(?,?,?,?,?)}");
			//不允许的操作: Ordinal binding and Named binding cannot be combined!
			callableDBUtil.setInt("id", 10);
			callableDBUtil.registerOutParameter("name", java.sql.Types.VARCHAR);
			callableDBUtil.registerOutParameter("name1", java.sql.Types.VARCHAR);
			callableDBUtil.registerOutParameter("test", java.sql.Types.INTEGER);
			callableDBUtil.registerOutParameter("nomatch", java.sql.Types.INTEGER);
			Test_p tets = (Test_p)callableDBUtil.executeCallableForObject(Test_p.class);
			
			System.out.println("Test_p is " + tets);
					
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}		
	}
	
	public @Test void testoldMethod()
	{
		PreparedDBUtil callableDBUtil = new PreparedDBUtil();
//		try {
//			
//			SQLResult result = callableDBUtil.execute("select * from tableinfo");
//			System.out.println(result.size());
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}
	
	public @Test void testTest_pWithNameIndexForObjectWithRowHandler()
	{
		CallableDBUtil callableDBUtil = new CallableDBUtil();
		try
		{
			callableDBUtil.prepareCallable("{call test_p(?,?,?,?)}");
			//不允许的操作: Ordinal binding and Named binding cannot be combined!
			callableDBUtil.setInt("id", 10);
			callableDBUtil.registerOutParameter("name", java.sql.Types.VARCHAR);
			callableDBUtil.registerOutParameter("name1", java.sql.Types.VARCHAR);
			callableDBUtil.registerOutParameter("test", java.sql.Types.INTEGER);
			callableDBUtil.registerOutParameter("nomatch", java.sql.Types.INTEGER);
			Test_p tets = (Test_p)callableDBUtil.executeCallableForObject(Test_p.class,new RowHandler(){

				public void handleRow(Object rowValue, Record record) {
					Test_p test_p = (Test_p)rowValue;
					try {
						test_p.setTest(record.getString("test")+"天马");
						test_p.setName1(record.getString("name1"));
						test_p.setName(record.getString("name"));
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
				
			});
			
//			System.out.println("name1:" + callableDBUtil.getString("name"));
//			System.out.println("name2:" + callableDBUtil.getString("name1"));
			
			System.out.println("Test_p is " + tets);
					
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}		
	}
	
	/**
	 * 经过测试证明存储过程中如果没有对插入/删除/修改操作做事务提交、回滚时，可以通过poolman
	 * 的事务框架来管理事务;如果存储过程中对插入/删除/修改操作已经做了事务提交或者回滚时，那么应用的事务和存储过程中
	 * 中的事务就是分离的两个事务。 
	 * @param i 为0时回滚事务，1时提交事务
	 */
	public @Test void testTest_pWithNameIndexForObjectTx()
	{
		int i = 1;
		TransactionManager tm = new TransactionManager();
		try
		{
			tm.begin();
			CallableDBUtil callableDBUtil = new CallableDBUtil();
			callableDBUtil.prepareCallable("{call test_p(?,?,?,?)}");
			//不允许的操作: Ordinal binding and Named binding cannot be combined!
			callableDBUtil.setInt("id", 10);
			callableDBUtil.registerOutParameter("name", java.sql.Types.VARCHAR);
			callableDBUtil.registerOutParameter("name1", java.sql.Types.VARCHAR);
			callableDBUtil.registerOutParameter("test", java.sql.Types.INTEGER);
			callableDBUtil.registerOutParameter("nomatch", java.sql.Types.INTEGER);
			Test_p tets = (Test_p)callableDBUtil.executeCallableForObject(Test_p.class);
			
			
			System.out.println("name1:" + callableDBUtil.getString("name"));
			System.out.println("name2:" + callableDBUtil.getString("name1"));
			System.out.println("Test_p is " + tets);
			callableDBUtil.executeInsert("insert into test(id,name) values('11','name11')");
			if(i == 0)
				tm.rollback();
			else
				tm.commit();
		}
		catch(Exception e)
		{
			try {
				tm.rollback();
			} catch (RollbackException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			e.printStackTrace();
		}		
	}
	
	
	
//	@SuppressWarnings("finally")
	static boolean test()
	{
		try
		{
			return true;
		}
		finally
		{
			return false;
		}
	}
	
	
	public static void main(String[] args)
	{
		
		System.out.println(test());
//		System.out.println("1---------------------------------------------------testTest_pWithPositionIndex()");
//		testTest_pWithPositionIndex();
//		System.out.println("2---------------------------------------------------testTest_pWithNameIndex()");
//		testTest_pWithNameIndex();
//		System.out.println("3---------------------------------------------------testTest_pWithNameIndexForObject()");
//		testTest_pWithNameIndexForObject();
//		System.out.println("4---------------------------------------------------testTest_pWithNameIndexForObjectTx()");
//		testTest_pWithNameIndexForObjectTx(0);
//		System.out.println("5---------------------------------------------------testTest_pWithNameIndexForXMLString()");
//		testTest_pWithNameIndexForXMLString();
//		System.out.println("6---------------------------------------------------testTest_pWithNameIndexForXMLStringRowHandler()");
//		testTest_pWithNameIndexForXMLStringRowHandler();
//		System.out.println("7---------------------------------------------------testTest_pWithNameIndexForObjectWithRowHandler()");
//		testTest_pWithNameIndexForObjectWithRowHandler();
//		CallableDBUtil.debugStatus();	
//		testoldMethod();
		
//	    testTest_pWithNameIndexForXMLString();
	}

}
