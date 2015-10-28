import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;

import javax.transaction.RollbackException;

import com.frameworkset.common.poolman.DBUtil;
import com.frameworkset.orm.transaction.TransactionManager;

public class TestDBUtil1 {
	
	public static void test1()
	{
		com.frameworkset.common.poolman.PreparedDBUtil d = new com.frameworkset.common.poolman.PreparedDBUtil();
		String sql1 = "select '0_10000104_'||'l' as id,'0_1000010'||'4' as parentid from dual";
		

		try {
			d.executeSelect(sql1);
			for (int i = 0; d.size() > 0 && i < d.size(); i++) {
				   System.out.println("'"+d.getString(i,"id")+"'");
				}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	
	public static void testBatch()
	{
		DBUtil dbUtil = new DBUtil();
		try {
			
			dbUtil.addBatch("truncate table td_reg_bank_acc_bak");
			dbUtil.addBatch("insert into td_reg_bank_acc_bak (create_acc_time,starttime,endtime) values(" + dbUtil.getDBDate(new Date()) 
																										 
																										  + "," + dbUtil.getDBDate(new Date()) 
																										  + "," + dbUtil.getDBDate(new Date()) + ")");
			
			dbUtil.addBatch("insert into td_reg_bank_acc_bak (create_acc_time,starttime,endtime) values(" + dbUtil.getDBDate(new Date()) 
					  + "," + dbUtil.getDBDate(new Date()) 
					
					  + "," + dbUtil.getDBDate(new Date()) + ")");
			
			dbUtil.addBatch("insert into td_reg_bank_acc_bak (create_acc_time,starttime,endtime) values(" + dbUtil.getDBDate(new Date()) 
					  + "," + dbUtil.getDBDate(new Date()) 
			
					  + "," + dbUtil.getDBDate(new Date()) + ")");
			
			dbUtil.addBatch("insert into td_reg_bank_acc_bak (create_acc_time,starttime,endtime) values(" + dbUtil.getDBDate(new Date()) 
					  + "," + dbUtil.getDBDate(new Date()) 
				
					  + "," + dbUtil.getDBDate(new Date()) + ")");
			
			dbUtil.executeBatch();
			System.out.println("dbUtil.getNumActive():" + dbUtil.getNumActive());
			System.out.println("dbUtil.getNumIdle():" + dbUtil.getNumIdle());
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			dbUtil.resetBatch();
		}
	}
	
	public static void testBatchTransactionRollback()
	{
		DBUtil dbUtil = new DBUtil();
		try {
			
			dbUtil.addBatch("truncate table td_reg_bank_acc_bak");
			dbUtil.addBatch("insert into td_reg_bank_acc_bak (create_acc_time,starttime,endtime) values(" + dbUtil.getDBDate(new Date()) 
																										 
																										  + "," + dbUtil.getDBDate(new Date()) 
																										  + "," + dbUtil.getDBDate(new Date()) + ")");
			
			dbUtil.addBatch("insert into td_reg_bank_acc_bak (create_acc_time,starttime,endtime) values(" + dbUtil.getDBDate(new Date()) 
					  + "," + dbUtil.getDBDate(new Date()) 
					
					  + "," + dbUtil.getDBDate(new Date()) + ")");
			
			dbUtil.addBatch("insert into td_reg_bank_acc_bak (create_acc_time,starttime,endtime) values(" + dbUtil.getDBDate(new Date()) 
					  + "," + dbUtil.getDBDate(new Date()) 
			
					  + "," + dbUtil.getDBDate(new Date()) + ")");
			//user error tablename will make tx rollbak 
			dbUtil.addBatch("insert into td_reg_bank_acc_bak1111 (create_acc_time,starttime,endtime) values(" + dbUtil.getDBDate(new Date()) 
					  + "," + dbUtil.getDBDate(new Date()) 
				
					  + "," + dbUtil.getDBDate(new Date()) + ")");
			
			dbUtil.executeBatch();
			System.out.println("dbUtil.getNumActive():" + dbUtil.getNumActive());
			System.out.println("dbUtil.getNumIdle():" + dbUtil.getNumIdle());
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			dbUtil.resetBatch();
		}
	}
	
	public static void testBatchNoexecute()
	{
		DBUtil dbUtil = new DBUtil();
		try {
			
			dbUtil.addBatch("truncate table td_reg_bank_acc_bak");
			dbUtil.addBatch("insert into td_reg_bank_acc_bak (create_acc_time,starttime,endtime) values(" + dbUtil.getDBDate(new Date()) 
																										 
																										  + "," + dbUtil.getDBDate(new Date()) 
																										  + "," + dbUtil.getDBDate(new Date()) + ")");
			
			dbUtil.addBatch("insert into td_reg_bank_acc_bak (create_acc_time,starttime,endtime) values(" + dbUtil.getDBDate(new Date()) 
					  + "," + dbUtil.getDBDate(new Date()) 
					
					  + "," + dbUtil.getDBDate(new Date()) + ")");
			
			dbUtil.addBatch("insert into td_reg_bank_acc_bak (create_acc_time,starttime,endtime) values(" + dbUtil.getDBDate(new Date()) 
					  + "," + dbUtil.getDBDate(new Date()) 
			
					  + "," + dbUtil.getDBDate(new Date()) + ")");
			
			dbUtil.addBatch("insert into td_reg_bank_acc_bak (create_acc_time,starttime,endtime) values(" + dbUtil.getDBDate(new Date()) 
					  + "," + dbUtil.getDBDate(new Date()) 
				
					  + "," + dbUtil.getDBDate(new Date()) + ")");
			
			//dbUtil.executeBatch();
			System.out.println("dbUtil.getNumActive():" + dbUtil.getNumActive());
			System.out.println("dbUtil.getNumIdle():" + dbUtil.getNumIdle());
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			dbUtil.resetBatch();
		}
	}
	
	
	public static void testBatchWithConnection()
	{
		DBUtil dbUtil = new DBUtil();
		Connection con = null;
		try {
			
			dbUtil.addBatch("truncate table td_reg_bank_acc_bak");
			dbUtil.addBatch("insert into td_reg_bank_acc_bak (create_acc_time,starttime,endtime) values(" + dbUtil.getDBDate(new Date()) 
																										 
																										  + "," + dbUtil.getDBDate(new Date()) 
																										  + "," + dbUtil.getDBDate(new Date()) + ")");
			
			dbUtil.addBatch("insert into td_reg_bank_acc_bak (create_acc_time,starttime,endtime) values(" + dbUtil.getDBDate(new Date()) 
					  + "," + dbUtil.getDBDate(new Date()) 
					
					  + "," + dbUtil.getDBDate(new Date()) + ")");
			
			dbUtil.addBatch("insert into td_reg_bank_acc_bak (create_acc_time,starttime,endtime) values(" + dbUtil.getDBDate(new Date()) 
					  + "," + dbUtil.getDBDate(new Date()) 
			
					  + "," + dbUtil.getDBDate(new Date()) + ")");
			
			dbUtil.addBatch("insert into td_reg_bank_acc_bak (create_acc_time,starttime,endtime) values(" + dbUtil.getDBDate(new Date()) 
					  + "," + dbUtil.getDBDate(new Date()) 
				
					  + "," + dbUtil.getDBDate(new Date()) + ")");
			con = dbUtil.getConection();
			dbUtil.executeBatch(con);
			System.out.println("dbUtil.getNumActive():" + dbUtil.getNumActive());
			System.out.println("dbUtil.getNumIdle():" + dbUtil.getNumIdle());
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			dbUtil.resetBatch();
		}
		finally
		{
			if(con != null)
			{
				try {
					con.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				con = null;
			}
			System.out.println("finally dbUtil.getNumActive():" + dbUtil.getNumActive());
			System.out.println("finally dbUtil.getNumIdle():" + dbUtil.getNumIdle());
		}
	}
	
	
	public static void testBatchWithConnectionNoexecute()
	{
		DBUtil dbUtil = new DBUtil();
		Connection con = null;
		try {
			
			dbUtil.addBatch("truncate table td_reg_bank_acc_bak");
			dbUtil.addBatch("insert into td_reg_bank_acc_bak (create_acc_time,starttime,endtime) values(" + dbUtil.getDBDate(new Date()) 
																										  + "," + dbUtil.getDBDate(new Date()) 
																										  + "," + dbUtil.getDBDate(new Date()) + ")");			
			dbUtil.addBatch("insert into td_reg_bank_acc_bak (create_acc_time,starttime,endtime) values(" + dbUtil.getDBDate(new Date()) 
					  + "," + dbUtil.getDBDate(new Date()) 
					
					  + "," + dbUtil.getDBDate(new Date()) + ")");
			
			dbUtil.addBatch("insert into td_reg_bank_acc_bak (create_acc_time,starttime,endtime) values(" + dbUtil.getDBDate(new Date()) 
					  + "," + dbUtil.getDBDate(new Date()) 
			
					  + "," + dbUtil.getDBDate(new Date()) + ")");
			
			dbUtil.addBatch("insert into td_reg_bank_acc_bak (create_acc_time,starttime,endtime) values(" + dbUtil.getDBDate(new Date()) 
					  + "," + dbUtil.getDBDate(new Date()) 
				
					  + "," + dbUtil.getDBDate(new Date()) + ")");
			con = dbUtil.getConection();
//			dbUtil.executeBatch(con);
			System.out.println("dbUtil.getNumActive():" + dbUtil.getNumActive());
			System.out.println("dbUtil.getNumIdle():" + dbUtil.getNumIdle());
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			dbUtil.resetBatch();
		}
		finally
		{
			if(con != null)
			{
				try {
					con.close();
					
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				con = null;
			}
			System.out.println("finally dbUtil.getNumActive():" + dbUtil.getNumActive());
			System.out.println("finally dbUtil.getNumIdle():" + dbUtil.getNumIdle());
		}
	}
	
	public static void testTXConnectionRollback()
	{
		
		
		DBUtil dbUtil = new DBUtil();
		Connection con = null;
		try {
			
			dbUtil.addBatch("truncate table td_reg_bank_acc_bak");
			dbUtil.addBatch("insert into td_reg_bank_acc_bak (create_acc_time,starttime,endtime) values(" + dbUtil.getDBDate(new Date()) 
																										 
																										  + "," + dbUtil.getDBDate(new Date()) 
																										  + "," + dbUtil.getDBDate(new Date()) + ")");
			
			dbUtil.addBatch("insert into td_reg_bank_acc_bak (create_acc_time,starttime,endtime) values(" + dbUtil.getDBDate(new Date()) 
					  + "," + dbUtil.getDBDate(new Date()) 
					
					  + "," + dbUtil.getDBDate(new Date()) + ")");
			
			dbUtil.addBatch("insert into td_reg_bank_acc_bak (create_acc_time,starttime,endtime) values(" + dbUtil.getDBDate(new Date()) 
					  + "," + dbUtil.getDBDate(new Date()) 
			
					  + "," + dbUtil.getDBDate(new Date()) + ")");
			//error sql
			dbUtil.addBatch("qqinsert into td_reg_bank_acc_bak (create_acc_time,starttime,endtime) values(" + dbUtil.getDBDate(new Date()) 
					  + "," + dbUtil.getDBDate(new Date()) 
				
					  + "," + dbUtil.getDBDate(new Date()) + ")");
			con = dbUtil.getConection();
			dbUtil.executeBatch(con);
			System.out.println("dbUtil.getNumActive():" + dbUtil.getNumActive());
			System.out.println("dbUtil.getNumIdle():" + dbUtil.getNumIdle());
			
		} catch (SQLException e) {
			try {
				con.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			// TODO Auto-generated catch block
			e.printStackTrace();
			dbUtil.resetBatch();
		}
		finally
		{
			if(con != null)
			{
				try {
					con.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				con = null;
			}
			System.out.println("finally dbUtil.getNumActive():" + dbUtil.getNumActive());
			System.out.println("finally dbUtil.getNumIdle():" + dbUtil.getNumIdle());
		}
		
	}
	public static void testConnection()
	{
		
		
		DBUtil dbUtil = new DBUtil();
		Connection con = null;
		try {
			
			dbUtil.addBatch("truncate table td_reg_bank_acc_bak");
			dbUtil.addBatch("insert into td_reg_bank_acc_bak (create_acc_time,starttime,endtime) values(" + dbUtil.getDBDate(new Date()) 
																										 
																										  + "," + dbUtil.getDBDate(new Date()) 
																										  + "," + dbUtil.getDBDate(new Date()) + ")");
			
			dbUtil.addBatch("insert into td_reg_bank_acc_bak (create_acc_time,starttime,endtime) values(" + dbUtil.getDBDate(new Date()) 
					  + "," + dbUtil.getDBDate(new Date()) 
					
					  + "," + dbUtil.getDBDate(new Date()) + ")");
			
			dbUtil.addBatch("insert into td_reg_bank_acc_bak (create_acc_time,starttime,endtime) values(" + dbUtil.getDBDate(new Date()) 
					  + "," + dbUtil.getDBDate(new Date()) 
			
					  + "," + dbUtil.getDBDate(new Date()) + ")");
			
			dbUtil.addBatch("insert into td_reg_bank_acc_bak (create_acc_time,starttime,endtime) values(" + dbUtil.getDBDate(new Date()) 
					  + "," + dbUtil.getDBDate(new Date()) 
				
					  + "," + dbUtil.getDBDate(new Date()) + ")");
			con = dbUtil.getConection();
			dbUtil.executeBatch(con);
			System.out.println("dbUtil.getNumActive():" + dbUtil.getNumActive());
			System.out.println("dbUtil.getNumIdle():" + dbUtil.getNumIdle());
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			dbUtil.resetBatch();
		}
		finally
		{
			if(con != null)
			{
				try {
					con.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				con = null;
			}
			System.out.println("finally dbUtil.getNumActive():" + dbUtil.getNumActive());
			System.out.println("finally dbUtil.getNumIdle():" + dbUtil.getNumIdle());
		}
		
	}
	
	public static void testTXConnection()
	{
		TransactionManager tm = new TransactionManager();
		try
		{
			tm.begin();
			testConnection();
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
		}
		System.out.println("testTXConnection dbUtil.getNumActive():" + DBUtil.getNumActive());
		System.out.println("testTXConnection dbUtil.getNumIdle():" + DBUtil.getNumIdle());
		System.out.println("testTXConnection dbUtil.getMaxNumActive():" + DBUtil.getMaxNumActive());
		
		
	}
	
	
	public static void testTXConnectionWithRollBack()
	{
		TransactionManager tm = new TransactionManager();
		try
		{
			tm.begin();
			testTXConnectionRollback();
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
		}
		System.out.println("testTXConnection dbUtil.getNumActive():" + DBUtil.getNumActive());
		System.out.println("testTXConnection dbUtil.getNumIdle():" + DBUtil.getNumIdle());
		System.out.println("testTXConnection dbUtil.getMaxNumActive():" + DBUtil.getMaxNumActive());
		
		
	}
	
	
	
	public static void test()
	{
		new Thread(){
			public void run(){

				TransactionManager tm = new TransactionManager();

				DBUtil dbUtil = new DBUtil();
				try
				{
					tm.begin();
					
					long key = dbUtil.getNextPrimaryKey("kettle","mq_properties");
					
					System.out.println("1 Thread :"+key);
					
					Thread.sleep(5000);
					
				}
				catch (Exception e)
				{
				}
			}
		}.start();
		new Thread(){
			public void run(){

				TransactionManager tm = new TransactionManager();

				DBUtil dbUtil = new DBUtil();
				try
				{
					tm.begin();
					
					long key = dbUtil.getNextPrimaryKey("kettle","mq_properties");
					
					System.out.println("2 Thread :"+key);
					
					Thread.sleep(5000);
					
				}
				catch (Exception e)
				{
				}
			}
		}.start();
	}
	
	
	
	
	public static void main(String args[])
	{
//		testBatch();
//		testBatchNoexecute();
//		TestDBUtil1.testBatchWithConnectionNoexecute();
//		TestDBUtil1.testTXConnectionWithRollBack();
//		testBatch();
//		testBatchTransactionRollback();
		test();
		
	}

}
