package test;

import java.sql.SQLException;

import com.frameworkset.common.poolman.DBUtil;


public class TestSEQ {
	public static void main(String args[])
	{
		try {
			System.out.println(DBUtil.getNextStringPrimaryKey("td_sm_log"));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
