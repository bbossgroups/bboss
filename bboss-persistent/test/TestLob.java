import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

import com.frameworkset.common.poolman.DBUtil;
import com.frameworkset.common.poolman.PreparedDBUtil;


public class TestLob {
	public static void testReadClobToString()
	{
		PreparedDBUtil db = new PreparedDBUtil();
		try {
			db.preparedSelect("select * from TD_REG_BANK_ACC_bak where id=?");
			db.setString(1, "3545");
			db.executePrepared();
			for(int i = 0 ; i < db.size(); i ++)
			{
				System.out.println(i + " id=" +db.getString(i, "id"));
				System.out.println(i + " clob1=" +db.getString(i, "clob1"));
				
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public static void testReadLobToFile()
	{
		PreparedDBUtil db = new PreparedDBUtil();
		try {
			db.preparedSelect("select * from TD_REG_BANK_ACC_bak ");
//			db.setString(1, "3538");
			db.executePrepared();
			for(int i = 0 ; i < db.size(); i ++)
			{
				String id = db.getString(i, "id");
				System.out.println(i + " id=" +db.getString(i, "id"));
				db.getFile(i, "clob1",new File("d:/test/clob1" + id + ".txt"));
				db.getFile(i, "blob1",new File("d:/test/blob1" + id + ".jar"));
				
			}
		} catch (SQLException e) {
			
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public static void testReadClobToFile()
	{
		PreparedDBUtil db = new PreparedDBUtil();
		try {
			db.preparedSelect("select * from TD_REG_BANK_ACC_bak where id=?");
			db.setString(1, "3545");
			db.executePrepared();
			for(int i = 0 ; i < db.size(); i ++)
			{
				String id = db.getString(i, "id");
				System.out.println(i + " id=" +db.getString(i, "id"));
				db.getFile(i, "clob1",new File("d:/test/clob1" + id + ".txt"));
				
				
			}
		} catch (SQLException e) {
			
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public static void testReadClob()
	{
		PreparedDBUtil db = new PreparedDBUtil();
		try {
			db.preparedSelect("select * from TD_REG_BANK_ACC_bak where id=?");
			db.setString(1, "3550");
			db.executePrepared();
			for(int i = 0 ; i < db.size(); i ++)
			{
				String id = db.getString(i, "id");
				System.out.println(i + " clob1_s=" +db.getString(i, "clob1"));
				System.out.println(i + " clob1=" +db.getClob(i, "clob1"));
				
				
			}
		} catch (SQLException e) {
			
			e.printStackTrace();
		} 
		
	}
	
	public static void testInsertClob()
	{
		PreparedDBUtil db = new PreparedDBUtil();
		try {
			db.preparedInsert("insert into TD_REG_BANK_ACC_bak(clob1) values(?)");
			db.setClob(1, "asdfasdfasdfmmmmm33333");
			db.executePrepared();
			
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
	}
	
	public static void testInsertClobWithField()
	{
		PreparedDBUtil db = new PreparedDBUtil();
		try {
			db.preparedInsert("insert into TD_REG_BANK_ACC_bak(clob1) values(?)");
			db.setClob(1,"asdfasdfasdfmmmmm11", "clob1");
			db.executePrepared();
			
		} catch (SQLException e) {			
			e.printStackTrace();
		}
	}
	
	public static void testInsertClobFromFile()
	{
		PreparedDBUtil db = new PreparedDBUtil();
		try {
			db.preparedInsert("insert into TD_REG_BANK_ACC_bak(clob1,clob2) values(?,?)");
			db.setClob(1,new File("D:/test/alert_taxp2.log"), "clob1");
			db.setClob(2,"3333");
			db.executePrepared();
			
		} catch (SQLException e) {			
			e.printStackTrace();
		}
		
	}
	
	public static void testInsertBlobFromFile()
	{
		PreparedDBUtil db = new PreparedDBUtil();
		try {
			db.preparedInsert("insert into TD_REG_BANK_ACC_bak(blob1) values(?)");
			db.setBlob(1,new File("D:\\workspace\\cms20080416\\creatorcms\\WEB-INF\\lib\\frameworkset.jar"));
			
			db.executePrepared();
			
		} catch (SQLException e) {			
			e.printStackTrace();
		}
	}
	
	public static void testInsertBlobFromFileWithField()
	{
		PreparedDBUtil db = new PreparedDBUtil();
		try {
			db.preparedInsert("insert into TD_REG_BANK_ACC_bak(blob1) values(?)");
			db.setBlob(1,new File("D:\\workspace\\cms20080416\\creatorcms\\WEB-INF\\lib\\frameworkset.jar"), "blob1");
			
			db.executePrepared();
			
		} catch (SQLException e) {			
			e.printStackTrace();
		}
	}
	public static void main(String[] args)
	{
//		testInsertClobWithField();
//		testInsertClobFromFile();
//		testInsertBlobFromFileWithField();
//		testInsertBlobFromFile();
		System.out.println(DBUtil.getNumActive());
		System.out.println(DBUtil.getNumIdle());
		
		testReadClob();
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		System.out.println(DBUtil.getNumActive());
		System.out.println(DBUtil.getNumIdle());
	}

}
