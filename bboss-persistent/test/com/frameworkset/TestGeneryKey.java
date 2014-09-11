package com.frameworkset;

import java.sql.SQLException;
import java.sql.Timestamp;

import org.junit.Test;

import com.frameworkset.common.poolman.DBUtil;
import com.frameworkset.common.poolman.SQLExecutor;
import com.frameworkset.orm.annotation.PrimaryKey;

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
	/**
	 * create table TESTPK
		(
		  ID VARCHAR2(100),
		  CT TIMESTAMP(6)
		)
	 */
	@Test
	public void testpk()
	{
		TestPK test = new TestPK();
		test.setCt(new Timestamp(System.currentTimeMillis()));
		try {
			SQLExecutor.insertBean("insert into testpk(id,ct) values(#[id],#[ct])", test);
			System.out.println("id:"+test.getId());
			TestPK test1 =  SQLExecutor.queryObjectBean(TestPK.class, "select * from testpk where id=#[id]", test);
			System.out.println("id:"+test1.getId());
			System.out.println("ct:"+test1.getCt());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static class TestPK
	{
		@PrimaryKey
//		@PrimaryKey(pkname="td_sm_user")
		private String id;
		private Timestamp ct;
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		public Timestamp getCt() {
			return ct;
		}
		public void setCt(Timestamp ct) {
			this.ct = ct;
		}
	}

}
