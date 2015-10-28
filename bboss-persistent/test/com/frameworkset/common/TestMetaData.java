package com.frameworkset.common;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.junit.Test;

import com.frameworkset.common.poolman.DBUtil;
import com.frameworkset.common.poolman.PreparedDBUtil;
import com.frameworkset.common.poolman.util.JDBCPool;

public class TestMetaData {
	public static void  testVMetaData()
	{
		System.out.println(DBUtil.getTableMetaData("V_VCH_FIXED_TAX_CF"));
	}
	@Test
	public void testStatementMeta()
	{
		DBUtil dbutil =new DBUtil();
		try {
			dbutil.executeSelect("select * from tableinfo");
			dbutil.executeSelect("select * from tableinfo");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	@Test
	public void testTablesMeta()
	{
		System.out.println(DBUtil.getTableMetaDatasFromDataBase());
		
	}
	@Test
	public void testTablesMetaByTypes()
	{
		System.out.println(DBUtil.getTableMetaDatasFromDataBase(new String[]{JDBCPool.TABLE_TYPE_TABLE}).size());
		System.out.println(DBUtil.getTableMetaDatasFromDataBase(new String[]{JDBCPool.TABLE_TYPE_ALL}).size());
		System.out.println(DBUtil.getTableMetaDatasFromDataBase(new String[]{JDBCPool.TABLE_TYPE_VIEW}).size());
		
	}
	
	@Test
	public void testTablesMetaPattern()
	{
		System.out.println(DBUtil.getTableMetaDatasFromDataBaseByPattern("td_sm_dicattachfield").size());
		System.out.println(DBUtil.getTableMetaDatasFromDataBaseByPattern("%_sm_%").size());
		

		
	}
	
	@Test
	public void getTableMetaDatasFromDataBase()
	{
		
		System.out.println(DBUtil.getTableMetaDatasFromDataBase("pw").size());

		
	}
	
	public static void main(String[] args)
	{
		testVMetaData();
	}
	@Test
	public void testPagineMeta()
	{
		PreparedDBUtil s = new PreparedDBUtil();
		try {
			s.preparedSelect("select * from tableinfo where 1 != 1",0,10);
			s.executePrepared();
			ResultSetMetaData meta = s.getMeta();
			meta.getColumnCount();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
