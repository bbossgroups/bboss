/*
 *  Copyright 2008 biaoping.yin
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.frameworkset.common.rowhandler;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.frameworkset.common.TestNewface;
import com.frameworkset.common.poolman.PreparedDBUtil;
import com.frameworkset.common.poolman.Record;
import com.frameworkset.common.poolman.handle.NullRowHandler;

/**
 * 
 * <p>Title: PreparedDBUtilRowhandler.java</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2007</p>
 * @Date 2010-2-7 下午03:50:42
 * @author biaoping.yin
 * @version 1.0
 */
public class PreparedDBUtilRowhandler
{
	@Test
	public void testNullRowhandler()
	{
		PreparedDBUtil dbUtil = new PreparedDBUtil();
		try {
			dbUtil.preparedSelect("select * from testnewface where object_id < ?");
			dbUtil.setInt(1, 100);
			final List<TestNewface> datas = new ArrayList<TestNewface>();
			dbUtil.executePreparedWithRowHandler(new NullRowHandler()
			{

				public void handleRow( Record record) {
					TestNewface t = new TestNewface();
					try {
						t.setCREATED(record.getDate("created"));
						t.setDATA_OBJECT_ID(record.getInt("DATA_OBJECT_ID"));
						datas.add(t);
						//........设置其他的属性
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					System.out.println("row handler:"+t);
					
				}
				
			});
			//记录条数
			System.out.print("dbUtil.size():" + dbUtil.size());
			
			//遍历记录集
			for(int i = 0; i < datas.size(); i ++)
			{
				TestNewface testNewface = datas.get(i);
				System.out.println(testNewface);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 分页nullhandler演示
	 */
	@Test
	public void testPageinNullRowhandler()
	{
		PreparedDBUtil dbUtil = new PreparedDBUtil();
		try {
			dbUtil.preparedSelect("select * from testnewface where object_id < ?",0,10);
			dbUtil.setInt(1, 100);
			final List<TestNewface> datas = new ArrayList<TestNewface>();
			dbUtil.executePreparedWithRowHandler(new NullRowHandler()
			{

				public void handleRow( Record record) {
					TestNewface t = new TestNewface();
					try {
						t.setCREATED(record.getDate("created"));
						t.setDATA_OBJECT_ID(record.getInt("DATA_OBJECT_ID"));
						datas.add(t);
						//........设置其他的属性
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					System.out.println("row handler:"+t);
					
				}
				
			});
			//当前页记录条数
			System.out.print("dbUtil.size():" + dbUtil.size());
			
			//总记录条数
			System.out.print("dbUtil.getTotalSize():" + dbUtil.getLongTotalSize());
			
			/**
			 * 遍历当前记录
			 */
			for(int i = 0; i < datas.size(); i ++)
			{
				TestNewface testNewface = datas.get(i);
				System.out.println(testNewface);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
