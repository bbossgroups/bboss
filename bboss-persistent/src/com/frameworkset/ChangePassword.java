package com.frameworkset;

import java.sql.SQLException;

import com.frameworkset.common.poolman.DBUtil;
import com.frameworkset.common.poolman.PreparedDBUtil;

public class ChangePassword {
	
	public static void main(String[] args)
	{
		String select = "select user_id,USER_PASSWORD from td_sm_user";
		String update = "update td_sm_user set USER_PASSWORD=? where user_id=?";
		DBUtil dbUtil = new DBUtil();
//		TransactionManager tm = new TransactionManager ();
		
		try {
//			tm
			dbUtil.executeSelect(select);
			for(int i = 0; i < dbUtil.size(); i ++)
			{
				String password = dbUtil.getString(i,"USER_PASSWORD");
				String userid = dbUtil.getString(i,"user_id");
				PreparedDBUtil updatedbUtil = new PreparedDBUtil();
//				String newPassword = 
				updatedbUtil.preparedUpdate(update);
//				updatedbUtil.setString(1,)
				
				
				
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
