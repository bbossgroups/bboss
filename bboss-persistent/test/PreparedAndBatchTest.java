

import java.io.File;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;

import javax.transaction.RollbackException;

import com.frameworkset.common.poolman.DBUtil;
import com.frameworkset.common.poolman.PreparedDBUtil;
import com.frameworkset.orm.transaction.TransactionManager;

public class PreparedAndBatchTest {
	
	/**
	 * 执行预编译更新操作,释放数据库资源实例
	 * @throws SQLException
	 */
	public static void updateTest() throws SQLException
	{
		for(int i = 0 ; i < 60 ; i ++)
		{
			PreparedDBUtil db = new PreparedDBUtil();
			try
			{
				
				db.preparedUpdate("update TD_REG_BANK_ACC_bak set create_acc_time=?,starttime=?,endtime=? where id=356");
				Date today = new Date(new java.util.Date().getTime());
				db.setTimestamp(1, new Timestamp(new java.util.Date().getTime()));
				db.setTimestamp(2, new Timestamp(new java.util.Date().getTime()));
				if(true && i < 50)
				{
					//此处抛出业务异常，必须在相应的异常处理块或者finally块中调用db.resetPrepare()方法来释放数据库资源
					//如果没有释放则会导致系统运行一段时候后抛出以下异常：
					//Cannot get a connection, pool error Timeout waiting for idle object
					throw new Exception("e");
				}
				
				db.setTimestamp(3, new Timestamp(new java.util.Date().getTime()));
				db.executePrepared();
//				db.addBatch(sql);
			}
			catch(Exception e)
			{
				/**
				 * PreparedDBUtil执行预编译操作过程中抛出异常后，调用resetPrepare()方法来释放数据库资源
				 * 该方法也可以放到finally块中执行
				 * finally
				 *	{
				 *		
				 *		db.resetPrepare();
				 *	}
				 */
				db.resetPrepare();
				System.out.println(e.getMessage());
			}
			finally
			{
				/**
				 * PreparedDBUtil执行预编译操作结束后调用resetPrepare()方法来释放数据库资源
				 */
				db.resetPrepare();
			}
	
		
		}
	}
	
	/**
	 * 包含在事务环境中的预编译操作，执行抛出异常后
	 * @throws SQLException
	 */
	public static void updateWithTXtest() throws SQLException
	{
		
		for(int i = 0 ; i < 60 ; i ++)
		{
			TransactionManager tm = new TransactionManager();
			
			PreparedDBUtil db = new PreparedDBUtil();
			try
			{
				tm.begin();
				db.preparedUpdate("update TD_REG_BANK_ACC_bak set create_acc_time=?,starttime=?,endtime=? where id=951");
				Date today = new Date(new java.util.Date().getTime());
				db.setTimestamp(1, new Timestamp(new java.util.Date().getTime()));
				db.setTimestamp(2, new Timestamp(new java.util.Date().getTime()));
				if(true && i < 50)
				{
					throw new Exception("e");
				}				
				db.setTimestamp(3, new Timestamp(new java.util.Date().getTime()));
				db.executePrepared();
				tm.commit();//事务提交后自动释放系统资源

			}
			catch(Exception e)
			{
				try {
					tm.rollback();//事务回滚后也会自动释放系统资源
				} catch (RollbackException e1) {
					
					e1.printStackTrace();
				}
				
			}
		}
	}
	
	public static void inserttest() throws SQLException
	{
		for(int i = 0 ; i < 60 ; i ++)
		{
			PreparedDBUtil db = new PreparedDBUtil();
			try
			{
				db.preparedInsert("insert into td_reg_bank_acc_bak (id,create_acc_time,starttime,endtime) values(?,?,?,?)");
				Date today = new Date(new java.util.Date().getTime());
				db.setInt(1, 1000 + i);
				
				db.setDate(2, new java.util.Date());
				db.setDate(3, new java.util.Date());
				db.setDate(4, new java.util.Date());
				
				
				if(true && i < 50)
				{
					throw new Exception("e");
				}
				

				db.executePrepared();

			}
			catch(Exception e)
			{
				System.out.println(e.getMessage());
			}
			finally
			{
				db.resetPrepare();
			}
		}
	}
	
	public static void insertWithTXtest() throws SQLException
	{
		for(int i = 0 ; i < 60 ; i ++)
		{
			
			PreparedDBUtil db = new PreparedDBUtil();
			TransactionManager tm = new TransactionManager();
			try
			{
				tm.begin();
				db.preparedInsert("insert into td_reg_bank_acc_bak (id,create_acc_time,starttime,endtime) values(?,?,?,?)");
				Date today = new Date(new java.util.Date().getTime());
				db.setInt(1, 1000 + i);
				
				db.setDate(2, new java.util.Date());
				db.setDate(3, new java.util.Date());
				db.setDate(4, new java.util.Date());
				
				
				if(true && i < 50)
				{
					throw new Exception("e");
				}
				

				db.executePrepared();
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
				System.out.println(e.getMessage());
			}
			
		}
	}
	
	public static void deleteWithTXtest() throws SQLException
	{
		for(int i = 0 ; i < 60 ; i ++)
		{
			PreparedDBUtil db = new PreparedDBUtil();
			TransactionManager tm = new TransactionManager();
			try
			{
				
				tm.begin();
				db.preparedDelete("delete from TD_REG_BANK_ACC_bak where id=?");
				db.setInt(1, 1000+i);
				if(true && i < 50)
				{
					throw new Exception("e");
				}
				

				db.executePrepared();
				tm.commit();

			}
			catch(Exception e)
			{
				System.out.println(e.getMessage());
				try {
					
					tm.rollback();
				} catch (RollbackException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}

			
		}
	}
	
	public static void deletetest() throws SQLException
	{
		for(int i = 0 ; i < 60 ; i ++)
		{
			PreparedDBUtil db = new PreparedDBUtil();
			try
			{
				
			
				db.preparedDelete("delete from TD_REG_BANK_ACC_bak where id=?");
				db.setInt(1, 1000+i);
				if(true && i < 50)
				{
					throw new Exception("e");
				}
				

				db.executePrepared();

			}
			catch(Exception e)
			{
				System.out.println(e.getMessage());
			}
			finally
			{

				db.resetPrepare();
			}
			
		}
	}
	
	public static void selecttest() throws SQLException
	{
		for(int i = 0 ; i < 1 ; i ++)
		{
			PreparedDBUtil db = new PreparedDBUtil();
			try
			{
	
		
				db.preparedSelect("select create_acc_time,starttime,endtime from TD_REG_BANK_ACC_bak where id=?");
				db.setInt(1, 376);
//				if(true && i < 50)
//				{
//					throw new Exception("e");
//				}
				db.executePrepared();
//				db.executeSelect("select create_acc_time,starttime,endtime from TD_REG_BANK_ACC_bak where id=951");
				System.out.println("create_acc_time="+db.getDate(0, 0));
				System.out.println("starttime="+db.getDate(0, 1));
				System.out.println("endtime="+db.getDate(0, 2));
				
				System.out.println("create_acc_time Time="+db.getTime(0, 0));
				System.out.println("starttime Time="+db.getTime(0, 1));
				System.out.println("endtime Time="+db.getTime(0, 2));
			}
			catch(Exception e)
			{
				e.printStackTrace();
				System.out.println(e.getMessage());
			}
			finally
			{
//				db.resetPreparedResource();
				db.resetPrepare();
			}
	
			
		}
	}
	
	public static void selectpaginetest() throws SQLException
	{
		for(int i = 0 ; i < 60 ; i ++)
		{
			PreparedDBUtil db = new PreparedDBUtil();
			try
			{
	
		
				db.preparedSelect("select create_acc_time,starttime,endtime from TD_REG_BANK_ACC_bak where id between ? and ?",0,100);
				db.setInt(1, 1);
				db.setInt(2, 10000);
				if(true && i < 50)
				{
					throw new Exception("e");
				}
				db.executePrepared();
			}
			catch(Exception e)
			{
				System.out.println(e.getMessage());
			}
			finally
			{
//				db.resetPreparedResource();
				db.resetPrepare();
			}
			
//			db.executePrepared();
			System.out.println("total size:" + db.getLongTotalSize());
			System.out.println("page size:" + db.size());
	
			
		}
	}
	
    public static void batchWithTXTest() throws Exception
    {
    	
    	for(int i = 0; i < 60; i ++)
    	{
    		TransactionManager tm = new TransactionManager();
	    	PreparedDBUtil db = new PreparedDBUtil();
	    	try
	    	{
	    		tm.begin();
		    	db.addBatch("delete from TD_REG_BANK_ACC_bak where id=951");
		    	db.addBatch("delete from TD_REG_BANK_ACC_bak where id=951");
		    	if(i < 30)
		    		throw new Exception("batch");
		    	db.addBatch("delete from TD_REG_BANK_ACC_bak where id=951");
		    	db.addBatch("delete from TD_REG_BANK_ACC_bak where id=951");
		    	db.executeBatch();
		    	tm.commit();
	    	}
	    	catch(Exception e)
	    	{
	    		e.printStackTrace();
	    		tm.rollback();

	    	}
	    	
    	}
    }
    
    public static void batchTest() throws Exception
    {
    	
    	for(int i = 0; i < 60; i ++)
    	{
	    	PreparedDBUtil db = new PreparedDBUtil();
	    	try
	    	{
		    	db.addBatch("delete from TD_REG_BANK_ACC_bak where id=951");
		    	db.addBatch("delete from TD_REG_BANK_ACC_bak where id=951");
		    	if(i < 55)
		    		//此处抛出业务异常，需要在相应的异常处理块或finally块中调用db.resetBatch()方法释放数据库资源
		    		throw new Exception("batch"); 
		    	db.addBatch("delete from TD_REG_BANK_ACC_bak where id=951");
		    	db.addBatch("delete from TD_REG_BANK_ACC_bak where id=951");
		    	db.executeBatch();
	    	}
	    	catch(Exception e)
	    	{
	    		e.printStackTrace();
	    		db.resetBatch();//在相应的异常处理块或finally块中调用db.resetBatch()方法释放数据库资源
	    	}
	    	finally
	    	{
	    		//finally块中调用db.resetBatch()方法释放数据库资源，确保资源能够正常回收
	    		db.resetBatch();
	    	}
    	}
    }
	
	public static void test() throws SQLException
	{
		System.out.println("PreparedAndBatchTest.deletetest() before;");
		PreparedAndBatchTest.deletetest();
		System.out.println("PreparedAndBatchTest.deletetest() end;");
		System.out.println("PreparedAndBatchTest.inserttest() before;");
		PreparedAndBatchTest.inserttest();
		System.out.println("PreparedAndBatchTest.inserttest() end;");
		System.out.println("PreparedAndBatchTest.selecttest() before;");
		PreparedAndBatchTest.selecttest();
		System.out.println("PreparedAndBatchTest.selecttest() end;");
		System.out.println("PreparedAndBatchTest.updateTest() before;");
		PreparedAndBatchTest.updateTest();
		System.out.println("PreparedAndBatchTest.updateTest() end;");
		System.out.println("after update PreparedAndBatchTest.selecttest() before;");
		PreparedAndBatchTest.selecttest();
		System.out.println("after update PreparedAndBatchTest.selecttest() end;");
		System.out.println("2 PreparedAndBatchTest.deletetest() before;");
		PreparedAndBatchTest.deletetest();
		System.out.println("2 PreparedAndBatchTest.deletetest() end;");	
	}
	
	public static void testTX() throws SQLException
	{
		System.out.println("PreparedAndBatchTest.deletetest() before;");
		PreparedAndBatchTest.deleteWithTXtest();
		System.out.println("PreparedAndBatchTest.deletetest() end;");
		System.out.println("PreparedAndBatchTest.inserttest() before;");
		PreparedAndBatchTest.insertWithTXtest();
		System.out.println("PreparedAndBatchTest.inserttest() end;");
		System.out.println("PreparedAndBatchTest.selecttest() before;");
		PreparedAndBatchTest.selecttest();
		System.out.println("PreparedAndBatchTest.selecttest() end;");
		System.out.println("PreparedAndBatchTest.updateTest() before;");
		PreparedAndBatchTest.updateWithTXtest();
		System.out.println("PreparedAndBatchTest.updateTest() end;");
		System.out.println("after update PreparedAndBatchTest.selecttest() before;");
		PreparedAndBatchTest.selecttest();
		System.out.println("after update PreparedAndBatchTest.selecttest() end;");
		System.out.println("2 PreparedAndBatchTest.deletetest() before;");
		PreparedAndBatchTest.deleteWithTXtest();
		System.out.println("2 PreparedAndBatchTest.deletetest() end;");	
	}
	
	public static void insertBoubleClob()
	{
		
    		TransactionManager tm = new TransactionManager();
	    	PreparedDBUtil db = new PreparedDBUtil();
	    	try
	    	{
	    		tm.begin();
		    	db.preparedInsert("insert into TD_REG_BANK_ACC_bak (clob1,clob2, id) values(?,?,?)");
		    	db.setClob(1, "aa", "clob1");
		    	db.setClob(2, "bb", "clob2");
		    	db.setPrimaryKey(3, "10000", "id");
		    	
		    	db.executePrepared();
		    	tm.commit();
	    	}
	    	catch(Exception e)
	    	{
	    		e.printStackTrace();
	    		try {
					tm.rollback();
				} catch (RollbackException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
//	    		db.resetBatch();
	    	}
	    	finally
	    	{
//	    		db.resetBatch();
//	    		tm.begin();
	    	}
	}
	
	public static void updateBoubleClob()
	{
		
			DBUtil dbUtil = new DBUtil();
			TransactionManager tm = new TransactionManager();
	    	PreparedDBUtil db = new PreparedDBUtil();
			try {
				dbUtil.executeSelect("select id from TD_REG_BANK_ACC_bak");
			
				
    		
	    		tm.begin();
	    		for(int i = 0; i < dbUtil.size();i ++)
				{
			    	db.preparedUpdate("update TD_REG_BANK_ACC_bak set clob1 = ?,clob2 = ? where id=?");
			    	db.setClob(1, "aaa1", "clob1");
			    	db.setClob(2, "bbb2", "clob2");
//			    	db.setNull(1, java.sql.Types.CLOB);
//			    	db.setNull(2, java.sql.Types.CLOB);
			    	
			    	db.setPrimaryKey(3, dbUtil.getInt(i, "id"), "id");
			    	
			    	db.executePrepared();
				}
		    	tm.commit();
	    	}
	    	catch(Exception e)
	    	{
	    		e.printStackTrace();
	    		try {
					tm.rollback();
				} catch (RollbackException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
//	    		db.resetBatch();
	    	}
	    	finally
	    	{
//	    		db.resetBatch();
//	    		tm.begin();
	    	}
	}
	
	public static void updateBoubleClobWithOuterConnection()
	{
		
			DBUtil dbUtil = new DBUtil();
			Connection con= null;
			try {
				con = dbUtil.getConection();
			} catch (SQLException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
			TransactionManager tm = new TransactionManager();
	    	PreparedDBUtil db = new PreparedDBUtil();
			try {
				dbUtil.executeSelect("select id from TD_REG_BANK_ACC_bak",con);
			
				
    		
	    		tm.begin();
	    		for(int i = 0; i < dbUtil.size();i ++)
				{
			    	db.preparedUpdate("update TD_REG_BANK_ACC_bak set clob1 = ?,clob2 = ? where id=?");
			    	db.setClob(1, new File("c:/存储过程_更新sequence.txt"));
			    	db.setClob(2, "bbb2");
//			    	db.setNull(1, java.sql.Types.CLOB);
//			    	db.setNull(2, java.sql.Types.CLOB);
			    	
			    	db.setInt(3,dbUtil.getInt(i, "id"));
			    	
			    	db.executePrepared(con);
				}
		    	tm.commit();
	    	}
	    	catch(Exception e)
	    	{
	    		e.printStackTrace();
	    		try {
					tm.rollback();
				} catch (RollbackException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
//	    		db.resetBatch();
	    	}
	    	finally
	    	{
	    		try {
					con.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
//	    		db.resetBatch();
//	    		tm.begin();
	    	}
	}
	
	public static void updateBoubleBlob()
	{
		
			DBUtil dbUtil = new DBUtil();
			TransactionManager tm = new TransactionManager();
	    	PreparedDBUtil db = new PreparedDBUtil();
			try {
				dbUtil.executeSelect("select id from TD_REG_BANK_ACC_bak");
			
				
    		
	    		tm.begin();
	    		
	    		for(int i = 0; i < dbUtil.size();i ++)
				{
			    	db.preparedUpdate("update TD_REG_BANK_ACC_bak set blob1 = ? where id=?");
			    	db.setBlob(1, "addddvvvbbb1".getBytes());
//			    	db.setClob(2, "bbb2", "clob2");
//			    	db.setNull(1, java.sql.Types.CLOB);
//			    	db.setNull(2, java.sql.Types.CLOB);
			    	
//			    	db.setPrimaryKey(2, dbUtil.getInt(i, "id"), "id");
			    	db.setInt(2,  dbUtil.getInt(i, "id"));
			    	
			    	db.executePrepared();
				}
//	    		dbUtil.execute("commit");
//	    		tm.rollback();
		    	tm.commit();		    	
		    	
	    	}
	    	catch(Exception e)
	    	{
	    		e.printStackTrace();
	    		try {
					tm.rollback();
				} catch (RollbackException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
//	    		db.resetBatch();
	    	}
	    	finally
	    	{
//	    		db.resetBatch();
//	    		tm.begin();
	    	}
	}
	
	public static void updateBoubleBlobFile()
	{
		
			DBUtil dbUtil = new DBUtil();
			TransactionManager tm = new TransactionManager();
	    	PreparedDBUtil db = new PreparedDBUtil();
			try {
				tm.begin();
				dbUtil.executeSelect("select id from TD_REG_BANK_ACC_bak");
			
				
    		
	    		
	    		for(int i = 0; i < dbUtil.size();i ++)
				{
			    	db.preparedUpdate("update TD_REG_BANK_ACC_bak set blob1 = ? where id=?");
			    	db.setBlob(1, new File("D:\\workspace\\cms20080416\\creatorcms\\WEB-INF\\lib\\frameworkset-pool.jar"));
//			    	db.setClob(2, "bbb2", "clob2");
//			    	db.setNull(1, java.sql.Types.CLOB);
//			    	db.setNull(2, java.sql.Types.CLOB);
			    	
			    	db.setInt(2, dbUtil.getInt(i, "id"));
			    	
			    	db.executePrepared();
				}
//	    		dbUtil.execute("commit");
//	    		tm.rollback();
		    	tm.commit();
	    	}
	    	catch(Exception e)
	    	{
	    		
	    		e.printStackTrace();
	    		try {
					tm.rollback();
				} catch (RollbackException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
//	    		db.resetBatch();
	    	}
	    	finally
	    	{
	    		dbUtil.clear();
	    		db.resetPrepare();
	    		db = null;
	    		dbUtil = null;
	    		tm = null;
//	    		db.resetBatch();
//	    		tm.begin();
	    	}
	}
	
	public static void insertBlobFile()
	{
		
//			DBUtil dbUtil = new DBUtil();
			TransactionManager tm = new TransactionManager();
	    	PreparedDBUtil db = new PreparedDBUtil();
			try {
//				dbUtil.executeSelect("select id from TD_REG_BANK_ACC_bak");
			
				
    		
	    		tm.begin();
//	    		for(int i = 0; i < dbUtil.size();i ++)
//				{
		    	db.preparedInsert("insert into TD_REG_BANK_ACC_bak(blob1) values(?)");
		    	File f = new File("D:\\workspace\\cms20080416\\creatorcms\\WEB-INF\\lib\\frameworkset-pool.jar");
		    	System.out.println("f.exist():" + f.exists());
		    	db.setBlob(1, new File("D:\\workspace\\cms20080416\\creatorcms\\WEB-INF\\lib\\frameworkset-pool.jar"));
		    	
//			    	db.setClob(2, "bbb2", "clob2");
//			    	db.setNull(1, java.sql.Types.CLOB);
//			    	db.setNull(2, java.sql.Types.CLOB);
			    	
//			    	db.setPrimaryKey(2, dbUtil.getInt(i, "id"), "id");
			    	
		    	db.executePrepared();
//				}
//	    		dbUtil.execute("commit");
//	    		tm.rollback();
		    	tm.commit();
	    	}
	    	catch(Exception e)
	    	{
	    		e.printStackTrace();
	    		try {
					tm.rollback();
				} catch (RollbackException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
//	    		db.resetBatch();
	    	}
	    	finally
	    	{
//	    		db.resetBatch();
//	    		tm.begin();
	    	}
	}
	
	public static void updateBoubleBlobWithCommon()
	{
		
			DBUtil dbUtil = new DBUtil();
			TransactionManager tm = new TransactionManager();
	    	PreparedDBUtil db = new PreparedDBUtil();
			try {
				dbUtil.executeSelect("select id from TD_REG_BANK_ACC_bak");
			
				
    		
	    		tm.begin();
	    		for(int i = 0; i < dbUtil.size();i ++)
				{
			    	db.preparedUpdate("update TD_REG_BANK_ACC_bak set blob1 = ?,BANK_ACC=? where id=?");
			    	db.setBlob(1, "addddvvvbbbwwww1".getBytes(), "blob1");
			    	db.setString(2, "etst");
//			    	db.setClob(2, "bbb2", "clob2");
//			    	db.setNull(1, java.sql.Types.CLOB);
//			    	db.setNull(2, java.sql.Types.CLOB);
			    	
			    	db.setPrimaryKey(3, dbUtil.getInt(i, "id"), "id");
			    	
			    	db.executePrepared();
				}
//	    		dbUtil.execute("commit");
//	    		tm.rollback();
//		    	tm.commit();
	    	}
	    	catch(Exception e)
	    	{
	    		e.printStackTrace();
	    		try {
					tm.rollback();
				} catch (RollbackException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
//	    		db.resetBatch();
	    	}
	    	finally
	    	{
	    		try {
					tm.commit();
				} catch (RollbackException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
//	    		db.resetBatch();
//	    		tm.begin();
	    	}
	}
	
	public static void updateWithCommon()
	{
		
			DBUtil dbUtil = new DBUtil();
			TransactionManager tm = new TransactionManager();
	    	PreparedDBUtil db = new PreparedDBUtil();
			try {
				dbUtil.executeSelect("select id from TD_REG_BANK_ACC_bak");
			
				
    		
	    		tm.begin();
	    		for(int i = 0; i < dbUtil.size();i ++)
				{
			    	db.preparedUpdate("update TD_REG_BANK_ACC_bak set BANK_ACC=? where id=?");
//			    	db.setBlob(1, "addddvvvbbbwwww1".getBytes(), "blob1");
			    	db.setString(1, "qqqqqq");
//			    	db.setClob(2, "bbb2", "clob2");
//			    	db.setNull(1, java.sql.Types.CLOB);
//			    	db.setNull(2, java.sql.Types.CLOB);
			    	
			    	db.setPrimaryKey(2, dbUtil.getInt(i, "id"), "id");
			    	
			    	db.executePrepared();
				}
//	    		dbUtil.execute("commit");
//	    		tm.rollback();
		    	tm.commit();
	    	}
	    	catch(Exception e)
	    	{
	    		e.printStackTrace();
	    		try {
					tm.rollback();
				} catch (RollbackException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
//	    		db.resetBatch();
	    	}
	    	finally
	    	{
//	    		try {
////					tm.commit();
//				} catch (RollbackException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//	    		db.resetBatch();
//	    		tm.begin();
	    	}
	}
	
	public static void getTableMetas()
	{
//		DBUtil.getTableMetaDatas();
		DBUtil.getTableMetaDatas();
	}
	
	public static void insertAutoID()
	{
		
    		TransactionManager tm = new TransactionManager();
	    	PreparedDBUtil db = new PreparedDBUtil();
	    	try
	    	{
	    		tm.begin();
		    	db.preparedInsert("insert into TD_REG_BANK_ACC_bak (clob1,clob2) values(?,?)");
		    	db.setClob(1, "aa", "clob1");
		    	db.setClob(2, "bb", "clob2");		    	
		    	System.out.println(db.executePrepared());
		    	tm.commit();
	    	}
	    	catch(Exception e)
	    	{
	    		e.printStackTrace();
	    		try {
					tm.rollback();
				} catch (RollbackException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
//	    		db.resetBatch();
	    	}
	    	finally
	    	{
//	    		db.resetBatch();
//	    		tm.begin();
	    	}
	}
	
	public static void insertAutoIDRollback()
	{
		
    		TransactionManager tm = new TransactionManager();
	    	PreparedDBUtil db = new PreparedDBUtil();
	    	try
	    	{
	    		tm.begin();
		    	db.preparedInsert("insert into TD_REG_BANK_ACC_bak (BANK_ACC) values(?)");
//		    	db.setClob(1, "aa", "clob1");
//		    	db.setClob(2, "bb", "clob2");	
		    	db.setString(1, "aa");
		    	System.out.println(db.executePrepared());
//		    	tm.rollback();
		    	tm.commit();
	    	}
	    	catch(Exception e)
	    	{
	    		e.printStackTrace();
	    		try {
					tm.rollback();
				} catch (RollbackException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
//	    		db.resetBatch();
	    	}
	    	finally
	    	{
//	    		db.resetBatch();
//	    		tm.begin();
	    	}
	}
	
	public static void columnMetaDataTest()
	{
//		java.sql.Types.FLOAT;
		System.out.println(DBUtil.getColumnMetaData("TB_PUB_LEVY_ITEM", "LEVY_RATE"));
		System.out.println(DBUtil.getColumnMetaData("TB_PUB_LEVY_ITEM", "PARENT_ID"));
		
	}
	
	public static void main(String[] args) throws Exception
	{
		DBUtil.debugMemory();
////		selectpaginetest();
////		testTX();
////		test();
////		PreparedAndBatchTest.batchTest();
////		batchWithTXTest();
//		insertBoubleClob();
////		LoadThread t = new LoadThread();
////		t.start();
////		LoadThread t1 = new LoadThread();
////		t1.start();
//		updateBoubleClob();
//		updateBoubleBlob();
////		updateWithCommon();
////		insertAutoID();
////		insertAutoIDRollback();
//////		updateBoubleBlobFile();
//		insertBlobFile();
//////		String test  = ":s:";
//////		String test1  = "s::";
//////		String test2  = "::s";
//////		String a[] = test.split(":");
//////		System.out.println("a.length:" +a.length);
//////		System.out.println("a[0]:"+a[0]);
//////		System.out.println("a[1]:"+a[1]);
//////		
//////		String b[] = test1.split(":");
//////		System.out.println("b.length:" +b.length);
//////		System.out.println("b[0]:"+b[0]);
//////		
//////		String c[] = test2.split(":");
//////		System.out.println("c.length:" +c.length);
//////		System.out.println("c[0]:"+c[0]);
////		columnMetaDataTest();
//////		System.out.println("b[1]:"+b[1]);
//////		System.out.println(a.length);
////		testIdleNumber();
////		testActiveNumber();
////		
////		updateBoubleClobWithOuterConnection();
//		
////		updateTest();
//		selecttest();
//		updateBoubleClobWithOuterConnection();
//		updateBoubleBlob();
//		updateBoubleBlobFile();
//		Connection con = DBUtil.getConection();
//		con.close();
//		updateBoubleBlob();
		for(int i = 0;i < 10; i ++)
		{
			updateBoubleBlobFile();
		}
		DBUtil.debugStatus();
		System.out.println(Integer.MAX_VALUE);
//		DBUtil.debugMemory();
//		System.gc();
//		DBUtil.debugMemory();
//		for(int i = 0; i < 1000;i ++)
//		{
//			System.out.println("qqq");
//		}
//		updateBoubleBlob();
		
		DBUtil.debugMemory();
		
		
		
	}
	
	public static void testIdleNumber()
	{
		System.out.println("DBUtil.getNumIdle():"+DBUtil.getNumIdle());
		
		
		
		
	}
	
	public static void testActiveNumber()
	{
		System.out.println("DBUtil.getNumActive():"+DBUtil.getNumActive());
	}
//	static class LoadThread extends Thread
//	{
//		public void run()
//		{
//			getTableMetas();
//		}
//	}

}

