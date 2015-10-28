package com.frameworkset.common;

import java.sql.SQLException;
import java.util.Hashtable;

import org.junit.Test;

import com.frameworkset.common.poolman.DBUtil;
import com.frameworkset.common.poolman.Record;

public class TestDBUtil {

	public static void test1()
	{
		com.frameworkset.common.poolman.PreparedDBUtil d = new com.frameworkset.common.poolman.PreparedDBUtil();
        System.out.println(d.getTableMetaData("td_sm_log"));
//		String sql1 = "select '0_10000104_l中文中文中文中文中文中文中文中文  '  as id,'0_1000010' || '4' as parentid from dual";
//		//String sql1 = "select '4' as id from td_sm_user";
//
//
//		try {
//			d.executeSelect(sql1);
//			for (int i = 0; d.size() > 0 && i < d.size(); i++) {
//				   System.out.println("'"+d.getString(i,"id")+"'");
//				}
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}


	}
	public static void testInsert()
	{
		DBUtil db = new DBUtil();
		try {
			db.executeInsert("insert into td_reg_bank_acc_bak(endtime) values(sysdate)");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void testObjectArray()
	{
		DBUtil db = new DBUtil();
		try {
			Hashtable[] datas = (Hashtable[])db.executeSelectForObjectArray("mysql","select * from tableinfo", Record.class);
			System.out.println(datas.length);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void testOraclePagine()
	{
//		String sql = "select document_id,title,subtitle,author,channel_id,status," +
//				"doctype,createtime,createuser,flow_id,docabstract," +
//				"ROW_NUMBER() OVER ( order by CREATETIME desc,document_id desc ) aa from  TD_CMS_DOCUMENT   ";
		String sql = "select id," +
		"ROW_NUMBER() OVER ( order by id desc) aa from  td_reg_bank_acc_bak   ";
		DBUtil dbUtil = new DBUtil();
		try {
			dbUtil.executeSelectForOracle(sql,0,10,"aa");
			for(int i = 0; i < dbUtil.size(); i ++)
			{
				System.out.println("id :" + dbUtil.getInt(i, 0));
			}
			System.out.println("page size:" + dbUtil.size());
			System.out.println("total page size:" + dbUtil.getLongTotalSize());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public static void testCaseWhen()
	{
		String sql = "select case when a.PRINT_FLAG=0 then '一打印' when a.PRINT_FLAG=1 then '未打印' end PRINT_FLAG_TO from TD_WR_USER_WRIT a inner join TD_WR_WRIT_COMMON_DATA b on a.ID = b.id  inner join td_sm_organization c on b.ORG_ID = c.ORG_ID where  a.USER_ID=1";
		DBUtil dbUtil = new DBUtil();
		try {
			dbUtil.executeSelect(sql);
			String PRINT_FLAG_TO = dbUtil.getString(0,"PRINT_FLAG_TO");
			System.out.println(PRINT_FLAG_TO);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public static void testSelectMutitable()
	{
		StringBuffer sql = new StringBuffer();
		sql.append("select a.id,e.name tax_person,a.acc_identity_id,c.name reg_identity_type,")
		  .append("a.send_write_id,a.send_write_name,a.accept_send_user,a.send_user_code,a.send_user_name")
		  .append(" from td_reg_tax_send_returnreceipt a,TD_REG_ACC_IDENTITY b,TB_REG_ACC_IDENTITY_TYPE c,")
		  .append("TD_REG_ACCOUNT d,TD_REG_MGR_OBJ e where a.STATUS='0' and a.acc_identity_id = b.id and")
		  .append(" b.identity_type = c.code and b.acc_id = d.id and ")
		  .append(" d.mgr_obj_id = e.id and d.mgr_code ='")
		  .append("112wt").append("'"); 
		DBUtil db = new DBUtil();
		try {
			db.executeSelect(sql.toString());
		
			System.out.println(db.size());
			for(int i = 0;  i < db.size(); i ++)
			{
				System.out.println("id=" + db.getString(i,"id") + ",tax_person=" + db.getString(i,"tax_person"));
			}
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
	}
	
	
	public static void testBatchWithDefaultTrasaction() throws Exception
	{
		DBUtil dbUtil = new DBUtil();
		dbUtil.addBatch("insert into test (name) values('biaoping.yin123')");
		dbUtil.addBatch("insert into test (name) values(biaoping.yin123)");
		dbUtil.executeBatch();
		
		
	}
	
	public static void testBatchWithOutTrasaction() throws Exception
	{
		DBUtil dbUtil = new DBUtil();
		dbUtil.setAutoCommit(true);
		dbUtil.addBatch("insert into test (name) values('biaoping.yin123')");
		dbUtil.addBatch("insert into test (name) values(biaoping.yin123)");
		dbUtil.executeBatch();		
	}
	
	public static void testBatchWithTrasaction() throws Exception
	{
		DBUtil dbUtil = new DBUtil();
		dbUtil.setAutoCommit(false);
		dbUtil.addBatch("insert into test (name) values('biaoping.yin123')");
		dbUtil.addBatch("insert into test (name) values(biaoping.yin123)");
		dbUtil.executeBatch();		
	}

	public static void main(String args[]) throws Exception
	{
//		test1();
//		testOraclePagine();
//		testCaseWhen();
//		testSelectMutitable();
		
//		testBatchWithDefaultTrasaction() ;
//		testBatchWithOutTrasaction();
//		testInsert();
		testOraclePagine();
	}

}
