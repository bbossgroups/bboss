package com.frameworkset.common;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.frameworkset.common.poolman.DBUtil;

/**
 * 
 * 
 * <p>Title: TestIdleEvict.java</p>
 *
 * <p>Description:测试poolman的链接池空闲链接回收功能 </p>
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
public class TestIdleEvict {
	
	public static void testIdleEvict()
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
				for(int i = 0; i < list.size(); i ++)
				{
					Connection con = (Connection)list.get(i);
					con.close();
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
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	public static void main(String[] args)
	{
		testIdleEvict();
	}

}
