package com.frameworkset.common;

import java.sql.SQLException;

import com.frameworkset.common.poolman.DBUtil;

public class TestGeneratorKey {
	public static void main(String[] args)
	{
		try {
			System.out.println("aaa:"+DBUtil.getNextStringPrimaryKey("mq","MQ_BROKER"));
			System.out.println("meta:" + DBUtil.getTableMetaData("mq","MQ_BROKER"));
//			System.out.println("aaa1:"+DBUtil.getNextPrimaryKey("td_comm_leavewords"));
//			DBUtil
			
			
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(Integer.MAX_VALUE);
//		DBUtil.debugStatus("bspf");
	}
	

}
