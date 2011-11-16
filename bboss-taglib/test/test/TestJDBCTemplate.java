package test;

import java.sql.SQLException;

import com.frameworkset.common.poolman.DBUtil;
import com.frameworkset.common.poolman.JDBCTemplate;
import com.frameworkset.common.poolman.TemplateDBUtil;


public class TestJDBCTemplate {
	public static void main(String[] args) throws Throwable
	{
//		final String s = "";
		TemplateDBUtil.executeTemplate(new JDBCTemplate()
		{

			public void execute() throws SQLException {
				DBUtil db = new DBUtil();
				String sql = "delete td_sm_user where user_name='2430' or user_name='2429' or user_name='2428' or user_name='2427'";
				db.executeDelete(sql);
			}
			
		});
	}

}
