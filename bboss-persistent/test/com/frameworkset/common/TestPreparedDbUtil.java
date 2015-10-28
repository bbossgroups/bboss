package com.frameworkset.common;

import java.sql.Connection;
import java.sql.SQLException;

import com.frameworkset.common.poolman.PreparedDBUtil;

public class TestPreparedDbUtil {
	public static void testWithNotConnection()
	{
		PreparedDBUtil db = new PreparedDBUtil();
		Connection con = null;
		try {
//			con = PreparedDBUtil.getConection();
			db.preparedDelete("bspf","delete from test");
			db.executePrepared(con);
			
			db.preparedInsert("bspf","insert into test(id,name) values(?,?)");
			db.setString(1, "1");
			db.setString(2, "name");
			db.executePrepared(con);
			
			db.preparedInsert("bspf","insert into test(name) values(?)");

			db.setString(1, "name");
			db.executePrepared(con);
			
			db.preparedInsert("bspf","insert into test(name,clobname,blobname) values(?,?,?)");

			db.setString(1, "name");
			db.setClob(2, "content");
			db.setBlob(3, "content".getBytes());
			db.executePrepared(con);
			
			db.preparedSelect("bspf","select * from test where name=?");
			//db.setString(1, "1");
			db.setString(1, "name");
			db.executePrepared(con);
			
			
			for(int i = 0; i < db.size(); i ++)
			{
				System.out.println(i + " id=" + db.getString(i, "id"));
				System.out.println(i + " name=" + db.getString(i, "name"));
				
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally
		{
			if(con != null)
				try {
					con.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
	}
	
	public static void testWithNotConnectionNotDBName()
	{
		PreparedDBUtil db = new PreparedDBUtil();
		Connection con = null;
		try {
//			con = PreparedDBUtil.getConection();
			db.preparedDelete("delete from test");
			db.executePrepared(con);
			
			db.preparedInsert("insert into test(id,name) values(?,?)");
			db.setString(1, "test1");
			db.setString(2, "name");
			db.executePrepared(con);
			
			db.preparedInsert("insert into test(name) values(?)");

			db.setString(1, "name auto id");
			db.executePrepared(con);
			
			db.preparedInsert("insert into test(name,clobname,blobname) values(?,?,?)");

			db.setString(1, "name with clob and blob");
			db.setClob(2, "content", "clobname");
			db.setBlob(3, "content".getBytes(), "blobname");
			db.executePrepared(con);
			
			db.preparedSelect("select * from test where name=?",0,100);
			//db.setString(1, "1");
			db.setString(1, "name");
			db.executePrepared(con);
			
			
			for(int i = 0; i < db.size(); i ++)
			{
				System.out.println(i + " id=" + db.getString(i, "id"));
				System.out.println(i + " name=" + db.getString(i, "name"));
				
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally
		{
			if(con != null)
				try {
					con.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
	}
	public static void testWithConnection()
	{
		PreparedDBUtil db = new PreparedDBUtil();
		Connection con = null;
		try {
			con = PreparedDBUtil.getConection();
			db.preparedDelete("bspf","delete from test");
			db.executePrepared(con);
			
			db.preparedInsert("bspf","insert into test(id,name) values(?,?)");
			db.setString(1, "1");
			db.setString(2, "name");
			db.executePrepared(con);
			
			db.preparedInsert("bspf","insert into test(name) values(?)");

			db.setString(1, "name");
			db.executePrepared(con);
			
			db.preparedInsert("bspf","insert into test(name,clobname,blobname) values(?,?,?)");

			db.setString(1, "name");
			db.setClob(2, "content");
			db.setBlob(3, "content".getBytes());
			db.executePrepared(con);
			
			db.preparedSelect("bspf","select * from test where name=?");
			//db.setString(1, "1");
			db.setString(1, "name");
			db.executePrepared(con);
			
			
			for(int i = 0; i < db.size(); i ++)
			{
				System.out.println(i + " id=" + db.getString(i, "id"));
				System.out.println(i + " name=" + db.getString(i, "name"));
				
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally
		{
			try {
				con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	public static void main(String[] args)
	{
		
//		TestPreparedDbUtil.testWithNotConnection();
		TestPreparedDbUtil.testWithConnection();//testWithNotConnection();//testWithNotConnectionNotDBName();
		PreparedDBUtil.debugStatus();
	}
	
	

}
