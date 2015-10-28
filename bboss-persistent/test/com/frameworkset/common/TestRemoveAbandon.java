package com.frameworkset.common;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.frameworkset.common.poolman.DBUtil;

/**
 * 
 * 
 * <p>Title: TestRemoveAbandon.java</p>
 *
 * <p>Description: 对数据库链接池的泄漏事务进行强制回收功能进行测试</p>
 *
 * <p>
 * bboss workgroup
 * </p>
 * <p>
 * Copyright (c) 2007
 * </p>
 * 
 * @Date 2009-6-1 下午08:58:51
 * @author biaoping.yin
 * @version 1.0
 */
public class TestRemoveAbandon {
	
	public static void testAbandonEvict()
	{
		
		T t = new T();
		t.start();
	}
	
	static class T extends Thread
	{
		public void run()
		{
			try {
				List list = new ArrayList();
				for(int i =0; i < 10; i ++)
				{
					Connection con = DBUtil.getConection();
					list.add(con);	
				}
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			while(true)
			{
				System.out.println("空闲链接数：" + DBUtil.getNumIdle());
				System.out.println("正在使用链接数：" + DBUtil.getNumActive());
				System.out.println("使用链接数最大值：" + DBUtil.getMaxNumActive());
				try {
					sleep(10000);
					DBUtil.getConection();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	public static void main(String[] args)
	{
		testAbandonEvict();
	}
	

}
