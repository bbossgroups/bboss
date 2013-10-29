package test;

import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;

import com.frameworkset.common.poolman.DBUtil;
import com.frameworkset.common.poolman.JDBCValueTemplate;
import com.frameworkset.common.poolman.PreparedDBUtil;
import com.frameworkset.common.poolman.TemplateDBUtil;


public class TestValueTemplateDBUtil {
	public static void testrollbackWithConnection() throws Throwable
	{
		Object ret = TemplateDBUtil.executeTemplate(
				new JDBCValueTemplate(){
					public Object execute() throws Exception {
						DBUtil dbUtil = new DBUtil();
						Connection con = PreparedDBUtil.getConection();
						PreparedDBUtil db = null;
						try {
							for (int i = 0; i < 1; i++) {
								db = new PreparedDBUtil();
								
									db
											.preparedInsert(
													"insert into td_reg_bank_acc_bak (create_acc_time,starttime,endtime) values(?,?,?)");
									Date today = new Date(new java.util.Date().getTime());
//									db.setInt(1, 1000 + i);

									db.setDate(1, new java.util.Date());
									db.setDate(2, new java.util.Date());
									db.setDate(3, new java.util.Date());

									// if(true && i < 50)
									// {
									// throw new Exception("e");
									// }

									db.executePrepared(con);

								
							}
							
							
						
						}
						catch(SQLException e)
						{
							con.rollback();
							throw e;
						}
						catch(Exception e)
						{
							con.rollback();
							throw e;
						}
						finally {
							db.resetPrepare();
							con.close();
						}
						
						dbUtil.executeInsert("insert into table values()");
						return "go";
					}
				
				}
			);
		System.out.println("testrollbackWithConnection return value:" + ret);
	}
	
	public static void testrollback() throws Throwable
	{
		Object ret = TemplateDBUtil.executeTemplate(
				new JDBCValueTemplate(){
					public Object execute() throws Exception {
						DBUtil dbUtil = new DBUtil();
						
						PreparedDBUtil db = null;
						try {
							for (int i = 0; i < 1; i++) {
								db = new PreparedDBUtil();
								
									db.preparedInsert(
													"insert into td_reg_bank_acc_bak (create_acc_time,starttime,endtime) values(?,?,?)");
									Date today = new Date(new java.util.Date().getTime());
//									db.setInt(1, 1000 + i);

									db.setDate(1, new java.util.Date());
									db.setDate(2, new java.util.Date());
									db.setDate(3, new java.util.Date());

									// if(true && i < 50)
									// {
									// throw new Exception("e");
									// }

									db.executePrepared();

								
							}
						} finally {
							
						}
						
						dbUtil.executeInsert("insert into table values()");
						return "go";
					}
				
				}
			);
		System.out.println("testrollback return value:" + ret);
	}
	
	public static void testcommit() throws Throwable
	{
		
		Object ret = TemplateDBUtil.executeTemplate(
				new JDBCValueTemplate(){
					/**
					 * 整个execute()方法的执行都会被包含在一个数据库事务中
					 * 当有异常抛出时TemplateDBUtil.executeTemplate()方法就会自动回滚整个数据库事务
					 * 当整个方法正常结束后，事务就会自动被提交，并将返回值返回给调用程序。
					 * 通过模板工具提供的便利，开发人员不需要编写自己的事务代码就可以顺利地实现数据库的事务性操作
					 */
					public Object execute() throws Exception {
						DBUtil dbUtil = new DBUtil();						
						PreparedDBUtil db = null;
						try {
							for (int i = 0; i < 1; i++) {
								db = new PreparedDBUtil();								
								db.preparedInsert("insert into " +
										"td_reg_bank_acc_bak " +
										"(create_acc_time,starttime,endtime) " +
										"values(?,?,?)");
								Date today = new Date(
										new java.util.Date().getTime());
								db.setDate(1, new java.util.Date());
								db.setDate(2, new java.util.Date());
								db.setDate(3, new java.util.Date());
								db.executePrepared();
							}
						} 
						catch(Exception e)
						{
							throw e;//抛出异常，将导致整个数据库事务回滚
						}
						try
						{
							dbUtil.executeInsert("insert into " +
									"td_reg_bank_acc_bak " +
									"(clob1,clob2) values('aa','bb')");
						}
						catch(Exception e)
						{
							throw e;//抛出异常，将导致整个数据库事务回滚
						}						
						//方法顺利执行完成，事务自动提交，并返回方法的返回值
						return "go";
					}				
				}
			);
		System.out.println("testcommit return value:" + ret);
	}
	
	
	public static void testcommitWithConnection() throws Throwable
	{
		Object ret = TemplateDBUtil.executeTemplate(
				new JDBCValueTemplate(){
					public Object execute() throws Exception {
						DBUtil dbUtil = new DBUtil();
						PreparedDBUtil db = null;
						try {
							for (int i = 0; i < 1; i++) {
								db = new PreparedDBUtil();
								
									db.preparedInsert(
													"insert into td_reg_bank_acc_bak (create_acc_time,starttime,endtime) values(?,?,?)");
									Date today = new Date(new java.util.Date().getTime());
//									db.setInt(1, 1000 + i);

									db.setDate(1, new java.util.Date());
									db.setDate(2, new java.util.Date());
									db.setDate(3, new java.util.Date());

									// if(true && i < 50)
									// {
									// throw new Exception("e");
									// }

									db.executePrepared(dbUtil.getConection());

								
							}
						} finally {
							
						}
						
						dbUtil.executeInsert("insert into td_reg_bank_acc_bak (clob1,clob2) values('conaa','conbb')");
						return "go";
					}
				
				}
			);
		System.out.println("testcommitWithConnection return value:" + ret);
	}
	
	public static void main(String[] args) 
	{
		try
		{
			TestValueTemplateDBUtil.testrollback();
		}
		catch(Throwable e)
		{
			e.printStackTrace();
		}
		try {
			TestValueTemplateDBUtil.testrollbackWithConnection();
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			TestValueTemplateDBUtil.testcommit();
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			TestValueTemplateDBUtil.testcommitWithConnection();
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(DBUtil.getNumActive());
		System.out.println(DBUtil.getNumIdle());
		System.out.println(DBUtil.getMaxNumActive());
		
	}

}
