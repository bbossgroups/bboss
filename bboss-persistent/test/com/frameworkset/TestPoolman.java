package com.frameworkset;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import javax.transaction.RollbackException;

import com.frameworkset.common.poolman.DBUtil;
import com.frameworkset.common.poolman.PreparedDBUtil;
import com.frameworkset.common.poolman.monitor.AbandonedTraceExt;
import com.frameworkset.orm.transaction.TransactionException;
import com.frameworkset.orm.transaction.TransactionManager;

public class TestPoolman
{
	public static void main(String[] args) {
//      Framework frameworkinit = new Framework();
//      frameworkinit.init("templates/module.xml");
//		String path = "d:/tets.x";
//		int idx = path.indexOf('/');
//		System.out.println(path.substring(0,idx));
		
//			Framework.getInstance();
		
		
		
//		for(int i = 0; i < 1; i ++)
//		{
//			new TestDB(i).start();
//			
//		}
		
		try {
			 Connection con = DBUtil.getConection();
			 Statement smt = con.createStatement();
			 smt.executeQuery("select 1 from dual");
			 DBUtil.debugStatus();
			 List<AbandonedTraceExt> traces = DBUtil.getGoodTraceObjects();
			 con.close();
			 smt.close();
			 DBUtil.debugStatus();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		TransactionManager tm = new TransactionManager();
		try {
			tm.begin();
			tm.getTransaction().getConnection();
			tm.destroyTransaction();
			
			
		} catch (TransactionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
		finally
		{
			tm.release();
		}
		
		
		
//		//new TestDB(0).delete();
//		new TestDB(0).prepareInsert();
//		
  }
	
	static class TestDB extends Thread
	{
		static final int count = 10;
		int j = 0;
		public TestDB(int i)
		{
			this.j = i;
//			System.out.println(j + "% 2=" +(j % 2));
		}
		public void run()
		{
//			int i = j % 6;
//			switch(i)
//			{
//			case 0:
//				insert();//break;
//			prepareInsertBlob();
//			prepareInsertBlob();
//			selectBlob();
//			this.prepareInsert();
			
//			this.testPreparedInsert();
////			this.testPreparedSelect();
//			
//			testPreparedPagineSelectFororacle();
//			testPreparedPagineSelect();
//			testPreparedInsert();
//			testPreparedPagineSelectFororacle();
//			try
//			{
//			prepareInsertBlob();
//			}
//			catch(Exception e)
//			{
//				e.printStackTrace();
//			}
			
//			prepareDefaultInsertBlob();
			selectBlob();
//			testTableDataCopy();
				
//				while(true)
//				{
//					try {
//						sleep(10);
//					} catch (InterruptedException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//					System.out.println("locked:" + SQLManager.getInstance().getPool("bspf").getLocked());
//					System.out.println("unlocked:" + SQLManager.getInstance().getPool("bspf").getUnlocked());
//				}
				
//			case 1:
//				selectpagining();break;
//			case 2:
//				select();break;
//			case 3:
//				update();break;
//			case 4:
//				delete();break;
//			case 5:
//				prepareInsert();break;
//			}
		}
		
		void insert()
		{
			String sql = "insert into test(name) values('biaoping.yin')";
			for(int i = 0; i < 1;i ++)
			{
				DBUtil dbUtil = new DBUtil();
				try {
					dbUtil.executeInsert(sql);
					System.out.println("Thread[" + j + "] " + i + " inserted ");
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			//SQLManager.getInstance().destroyPools();
		}
		
		void select()
		{
			String sql = "select * from test";
			for(int i = 0; i < 1000;i ++)
			{
				DBUtil dbUtil = new DBUtil();
				try {
					dbUtil.executeSelect("oadx",sql);
					System.out.println("Thread[" + j + "] " + i + " dbUtil.size:" + dbUtil.size());
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		void update()
		{
			String sql = "update test set name=''";
			for(int i = 0; i < 1000;i ++)
			{
				DBUtil dbUtil = new DBUtil();
				try {
					dbUtil.executeUpdate("oadx",sql);
					System.out.println("Thread[" + j + "] " + i + " dbUtil.size:" + dbUtil.size());
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		void delete()
		{
			String sql = "delete from test";
			for(int i = 0; i < 1000;i ++)
			{
				DBUtil dbUtil = new DBUtil();
				try {
					dbUtil.executeUpdate("oadx",sql);
					System.out.println("Thread[" + j + "] " + i + " dbUtil.size:" + dbUtil.size());
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		
		  
		void prepareInsert()
		{
			for(int i = 0; i < 100; i ++)
			{
				PreparedDBUtil p = new PreparedDBUtil();
				
				try {
					p.setBatchDBName("bspf");
					for(int j = 0; j < 10; j ++)
					{
						p.addBatch("insert into test(name) values('biaoping.yin')");
						
					}
					p.executeBatch();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			
		}
		
		void prepareInsertBlob()
		{
			for(int i = 0; i < 1; i ++)
			{
				PreparedDBUtil p = new PreparedDBUtil();
				
				
				try {
					
				
					p.preparedInsert("query","insert into test(name,content) values(?,?)");
//					p.setString(1,"biaoping.yin");
					p.setPrimaryKey(1,"biaoping.yin1","name");
					
					p.setBlob(2,new File("D:/workspace/shark-1.1-2.src.zip"),"content");
//					p.setBlob(2,"asdfasdf".getBytes(),"content");
//					p.setClob(2,"","content");
					
//					p.getString(1,"content");
//					p.getFile(2,"content",new File(""));
//					p.getBlob(2,"content");
					
					p.executePrepared();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			
		}
		
		void prepareDefaultInsertBlob()
		{
			for(int i = 0; i < 1; i ++)
			{
				PreparedDBUtil p = new PreparedDBUtil();
				
				
				try {
					
				
					p.preparedInsert("insert into test(name,content) values(?,?)");
//					p.setString(1,"biaoping.yin");
					p.setPrimaryKey(1,"biaoping.yin1","name");
					
					p.setBlob(2,new File("D:/workspace/shark-1.1-2.src.zip"),"content");
//					p.setBlob(2,"asdfasdf".getBytes(),"content");
//					p.setClob(2,"","content");
					
//					p.getString(1,"content");
//					p.getFile(2,"content",new File(""));
//					p.getBlob(2,"content");
					
					p.executePrepared();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			
		}
		
		void testTableDataCopy()
		{
			DBUtil dbUtil = new DBUtil();
			String insert = "insert into test1(name) select * from test";
			
			dbUtil.setBatchDBName("bspf");
			
			String insert1 = "insert into test1 select *  from test";
			
			String insert2 = "INSERT INTO " +
			   "(SELECT name FROM test1) " +
			   "select name from test where name like 'biaoping%'";
			
			String insert3 = "INSERT ALL " +
			   "WHEN name = 'biaoping.yin1' THEN " +
			   "INTO test2 " +
			   "WHEN name = 'biaoping.yin2' THEN " +
			   "INTO test3 " +
			   "ELSE " +
			   "INTO test4 " +
			   "SELECT name " +
			   "FROM test";
			


			try {
				dbUtil.executeInsert(insert3);
//				dbUtil.executeInsert(insert1);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		void testPreparedInsert()
		{
			TransactionManager tm = new TransactionManager();
			try
			{
				tm.begin();
				for(int i = 0; i < 10; i ++)
				{
					PreparedDBUtil p = new PreparedDBUtil();
					
					
					
						
					
					p.preparedInsert("insert into test(name) values(?)");
//					p.setString(1,"biaoping.yin");
					p.setString(1,"biaoping.yin" + i);
					
					
//					p.setBlob(2,"asdfasdf".getBytes(),"content");
//					p.setClob(2,"","content");
					
//					p.getString(1,"content");
//					p.getFile(2,"content",new File(""));
//					p.getBlob(2,"content");
					
					p.executePrepared();
					
				}
				tm.commit();
			} catch (SQLException e) {
				try {
					tm.rollback();
				} catch (RollbackException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				try {
					tm.rollback();
				} catch (RollbackException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		void testPreparedSelect()
		{
			String sql = "select name from test where name=?";
			PreparedDBUtil pdb = new PreparedDBUtil();
			try {
				pdb.preparedSelect(sql);
				pdb.setString(1,"biaoping.yin1");
				pdb.executePrepared();
				
				System.out.println(pdb.size());
				System.out.println(pdb.getString(0,"name"));
				
				
			} catch (SQLException e) {
			
				e.printStackTrace();
			}
			
		}
		
		void testPreparedPagineSelect()
		{
			String sql = "select name from test where name=? order by name";
			PreparedDBUtil pdb = new PreparedDBUtil();
			try {
				long time = System.currentTimeMillis();
				pdb.preparedSelect(sql,1,2);
				pdb.setString(1,"biaoping.yin1");
				pdb.executePrepared();
				long end = System.currentTimeMillis();
				System.out.println("common total times:" + (end - time));
				System.out.println(pdb.size());
				System.out.println(pdb.getLongTotalSize());
				System.out.println(pdb.getString(0,"name"));
				
				
			} catch (SQLException e) {
			
				e.printStackTrace();
			}
			
		}
		
		void testPreparedPagineSelectFororacle()
		{
			
			String sql = "select name ,row_number() over (order by name) rownum_ from test where name=?";
			PreparedDBUtil pdb = new PreparedDBUtil();
			try {
				long time = System.currentTimeMillis();
				
				pdb.preparedSelect(sql,1,2,"rownum_");
				pdb.setString(1,"biaoping.yin1");
				pdb.executePrepared();
				long end = System.currentTimeMillis();
				System.out.println("oracle total times:" + (end - time));
				System.out.println(pdb.size());
				System.out.println(pdb.getLongTotalSize());
				System.out.println(pdb.getString(0,"name"));
				
				
			} catch (SQLException e) {
			
				e.printStackTrace();
			}
			
		}
		
		void selectBlob()
		{
			for(int i = 0; i < 1; i ++)
			{
				PreparedDBUtil p = new PreparedDBUtil();
				
				
				try {
					
					p.executeSelect("select content,name from test where name='biaoping.yin1'");
					String[] fields = p.getFields();
					System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>." + fields[0] + ":" + fields[1]);
					File shark = new File("d:/workspace/shark-222.zip");
					p.getFile(0,"content", shark);
//					p.preparedInsert("insert into test(name,content) values(?,?)");
//					p.setPrimaryKey(1,"biaoping.yin1","name");
//					p.setBlob(2,new File("D:/workspace/shark-1.1-2.src.zip"),"content");
//					
//					p.executePrepared();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		void selectpagining()
		{
			String sql = "select * from test";
			for(int i = 0; i < 1000;i ++)
			{
				DBUtil dbUtil = new DBUtil();
				try {
					dbUtil.executeSelect("oadx",sql,0,10);
					System.out.println("Thread[" + j + "] " + i + " dbUtil.size:" + dbUtil.size());
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

}
