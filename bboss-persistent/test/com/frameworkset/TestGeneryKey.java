package com.frameworkset;

import java.sql.SQLException;

import com.frameworkset.common.poolman.DBUtil;

public class TestGeneryKey {
	
	public static void main(String args[])
	{
		DBUtil dbUtil = new DBUtil();
//		TransactionManager tm = new TransactionManager();
		try {
			
//			tm.begin();
//			dbUtil.getConection();
			dbUtil.executeInsert("insert into test_t(name) values('测试')");
			dbUtil.executeInsert("insert into test_t(name) values('测试')");
			dbUtil.executeInsert("insert into test_t(name) values('测试')");
			dbUtil.executeInsert("insert into test_t(name) values('测试')");
			dbUtil.executeInsert("insert into test_t(name) values('测试')");
			dbUtil.executeInsert("insert into test_t(name) values('测试')");
			dbUtil.executeInsert("insert into test_t(name) values('测试')");
			System.out.println(dbUtil.getNextPrimaryKey("test_t"));
//			tm.commit();
		} catch (SQLException e) {
			
	 		e.printStackTrace();
		}
		finally
		{
			
		}
		
	}

}
