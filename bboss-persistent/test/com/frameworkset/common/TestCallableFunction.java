package com.frameworkset.common;

import javax.transaction.RollbackException;

import org.junit.Test;

import com.frameworkset.common.poolman.CallableDBUtil;
import com.frameworkset.common.poolman.Record;
import com.frameworkset.orm.transaction.TransactionManager;

public class TestCallableFunction {
	public @Test void testTest_fWithPositionIndex()
	{
		CallableDBUtil callableDBUtil = new CallableDBUtil();  
		try
		{
			callableDBUtil.prepareCallable("{? = call Test_f(?,?,?)}");
			callableDBUtil.registerOutParameter(1, java.sql.Types.INTEGER);
			callableDBUtil.setInt(2, 10);
			callableDBUtil.registerOutParameter(3, java.sql.Types.VARCHAR);
			callableDBUtil.registerOutParameter(4, java.sql.Types.VARCHAR);
			callableDBUtil.executeCallable();
			System.out.println("ret:" + callableDBUtil.getInt(1));
			System.out.println("name:" + callableDBUtil.getString(3));
			System.out.println("name1:" + callableDBUtil.getString(4));
						
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		
	}
	
	/**
	 * 执行函数时不能通过命名方式绑定变量
	 */
	public @Test void testTest_fWithNameIndex()
	{
		CallableDBUtil callableDBUtil = new CallableDBUtil();
		try
		{
			callableDBUtil.prepareCallable("{? = call Test_f(?,?,?)}");
			callableDBUtil.registerOutParameter(1, java.sql.Types.INTEGER);
			callableDBUtil.setInt(2, 10);
			callableDBUtil.registerOutParameter(3, java.sql.Types.VARCHAR);
			callableDBUtil.registerOutParameter(4, java.sql.Types.VARCHAR);
			callableDBUtil.executeCallable();
			System.out.println("ret:" + callableDBUtil.getInt(1));
			System.out.println("name:" + callableDBUtil.getString(3));
			System.out.println("name1:" + callableDBUtil.getString(4));
					
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		
	}
	
	public @Test void testTest_fWithNameIndexForXMLString()
	{
		CallableDBUtil callableDBUtil = new CallableDBUtil();
		try
		{
			callableDBUtil.prepareCallable("{? = call Test_f(?,?,?)}");
			callableDBUtil.registerOutParameter(1, java.sql.Types.INTEGER);
			//不允许的操作: Ordinal binding and Named binding cannot be combined!
			callableDBUtil.setInt(2, 10);
			callableDBUtil.registerOutParameter(3, java.sql.Types.VARCHAR);
			callableDBUtil.registerOutParameter(4, java.sql.Types.VARCHAR);
			String xmlString = callableDBUtil.executeCallableForXML();
//			System.out.println("name1:" + callableDBUtil.getString("name"));
//			System.out.println("name2:" + callableDBUtil.getString("name1"));
			System.out.println("xmlString:" + xmlString);
					
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		
	}
	
	/**
	 * 对于函数的调用使能使用顺序位置标识来绑定变量
	 */
	public @Test void testTest_fWithNameIndexForXMLStringRowHandler()
	{
		CallableDBUtil callableDBUtil = new CallableDBUtil();
		try
		{
			callableDBUtil.prepareCallable("{? = call Test_f(?,?,?)}");
			//不允许的操作: Ordinal binding and Named binding cannot be combined!
			callableDBUtil.registerOutParameter(1, java.sql.Types.INTEGER);
			callableDBUtil.setInt(2, 10);
			callableDBUtil.registerOutParameter(3, java.sql.Types.VARCHAR);
			callableDBUtil.registerOutParameter(4, java.sql.Types.VARCHAR);
			String xmlString = callableDBUtil.executeCallableForXML(new com.frameworkset.common.poolman.handle.RowHandler()
			{
				/**
				 * 对已经处理好的行记录进行处理的逻辑
				 * @param rowValue
				 */
				public void handleRow(Object rowValue,Record record)
				{
					StringBuffer objects = (StringBuffer)rowValue;
					objects.append(record);
					System.out.println("objects:" + objects);
					
				}
			});
//			System.out.println("name1:" + callableDBUtil.getString("name"));
//			System.out.println("name2:" + callableDBUtil.getString("name1"));
			System.out.println("xmlString:" + xmlString);
					
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		
	}
	
	public @Test void testTest_fWithNameIndexForObject()
	{
		CallableDBUtil callableDBUtil = new CallableDBUtil();
		try
		{
			
			callableDBUtil.prepareCallable("{? = call Test_f(?,?,?)}");
			callableDBUtil.registerOutParameter(1, java.sql.Types.INTEGER);
			callableDBUtil.setInt(2, 10);
			callableDBUtil.registerOutParameter(3, java.sql.Types.VARCHAR);
			callableDBUtil.registerOutParameter(4, java.sql.Types.VARCHAR);
			Test_f tets = (Test_f)callableDBUtil.executeCallableForObject(Test_f.class,new com.frameworkset.common.poolman.handle.RowHandler()
			{

				public void handleRow(Object rowValue,Record record) {
					Test_f objects = (Test_f)rowValue;
					try
					{
						objects.setRet(record.getString(1));
						objects.setName(record.getString(3));
						objects.setName1(record.getString(4));
					}
					catch(Exception e)
					{
						
					}
//					origine.put(new Integer(4), "55");
					System.out.println("rowValue:" + rowValue);
					
				}
				
			}
			);
			
			
//			System.out.println("name1:" + callableDBUtil.getString("name"));
//			System.out.println("name2:" + callableDBUtil.getString("name1"));
			System.out.println("Test_f is " + tets);
					
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
	public @Test void testTest_fWithNameIndexForObjectTx()
	{
		int i = 1;
		TransactionManager tm = new TransactionManager();
		try
		{
			tm.begin();
			CallableDBUtil callableDBUtil = new CallableDBUtil();
			callableDBUtil.prepareCallable("{? = call Test_f(?,?,?)}");
			callableDBUtil.registerOutParameter(1, java.sql.Types.INTEGER);
			//不允许的操作: Ordinal binding and Named binding cannot be combined!
			
			callableDBUtil.setInt(2, 10);
			callableDBUtil.registerOutParameter(3, java.sql.Types.VARCHAR);
			callableDBUtil.registerOutParameter(4, java.sql.Types.VARCHAR);
			Test_f tets = (Test_f)callableDBUtil.executeCallableForObject(Test_f.class);
			
			
//			System.out.println("name1:" + callableDBUtil.getString("name"));
//			System.out.println("name2:" + callableDBUtil.getString("name1"));
			System.out.println("Test_f is " + tets);
			callableDBUtil.executeInsert("insert into test(id,name) values('11','name11')");
			if(i == 0)
				tm.rollback();
			else
				tm.commit();
		}
		catch(Exception e)
		{
			
			e.printStackTrace();
		}	
		finally
		{
			tm.release();
		}
	}
	
	public static void main(String[] args)
	{
//		System.out.println("1------------------------------------------testTest_fWithNameIndexForObject()");
//		testTest_fWithNameIndexForObject();
//		System.out.println("2------------------------------------------testTest_fWithNameIndexForObjectTx(1)");
//		testTest_fWithNameIndexForObjectTx(1);
//		System.out.println("3------------------------------------------testTest_fWithPositionIndex()");
//		testTest_fWithPositionIndex();
//		System.out.println("4------------------------------------------testTest_fWithNameIndexForXMLString()");
//		testTest_fWithNameIndexForXMLString();
//		System.out.println("5------------------------------------------testTest_fWithNameIndexForXMLStringRowHandler()");
//		testTest_fWithNameIndexForXMLStringRowHandler();
		
		CallableDBUtil.debugStatus();
	}
}
