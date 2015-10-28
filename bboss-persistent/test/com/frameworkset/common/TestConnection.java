package com.frameworkset.common;

import java.sql.Connection;
import java.sql.SQLException;

import javax.transaction.RollbackException;

import com.frameworkset.common.poolman.DBUtil;
import com.frameworkset.orm.transaction.TransactionException;
import com.frameworkset.orm.transaction.TransactionManager;

public class TestConnection {
	static long max = 0;
	public static void getConntectionTest()
	{
		try {
			long s = System.currentTimeMillis();
			Connection con = DBUtil.getConection();
			long end = System.currentTimeMillis();
			max = max < (end - s)?(end -s):max;
			System.out.println("获取connection耗时：" + (end - s)  + "毫秒.");
			con.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	public static void main(String[] args)
	{
		try {
			long s = System.currentTimeMillis();
			System.out.println("获取connection前空闲：" +DBUtil.getNumIdle()  + "个.");
			TransactionManager tm = new TransactionManager();
			tm.begin();
			Connection con = DBUtil.getConection();
			Connection connn = DBUtil.getDataSource(null).getConnection();
			System.out.println("获取connection耗时空闲：" +DBUtil.getNumIdle()  + "个.");
			Connection con1 = DBUtil.getConection();
			long end = System.currentTimeMillis();
			System.out.println("获取connection1耗时空闲：" +DBUtil.getNumIdle()  + "个.");
			con.close();
			con1.close();
			tm.commit();
			System.out.println("获取connection1后空闲：" +DBUtil.getNumIdle()  + "个.");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RollbackException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransactionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
//		try {
//			Class r = Runtime.getRuntime().getClass();
//			java.lang.reflect.Method m = r.getDeclaredMethod(
//					"addShutdownHook", new Class[] { Thread.class });
//			m.invoke(Runtime.getRuntime(), new Object[] { new Thread(
//					new ShutDown()) });
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		for(int i = 0; i < 10; i ++)
//		{
//			T t = new T();
//			t.start();
//		}
	}
	
	
	
	static class T extends Thread
	{
		public void run()
		{
			int i = 0;
			while(i < 100)
			{
				i ++ ;
				getConntectionTest();
				try {
					sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	static class ShutDown implements Runnable
	{

		public void run() {
			System.out.println("获取链接的最长时间：" + max + "毫秒.");
			
		}
		
	}

}
