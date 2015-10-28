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
package com.frameworkset.common;

import java.sql.SQLException;
import java.util.List;

import com.frameworkset.common.poolman.DBUtil;
import com.frameworkset.common.poolman.PreparedDBUtil;
import com.frameworkset.common.poolman.Record;
import com.frameworkset.common.poolman.handle.RowHandler;

/**
 * create table testnewface as select * from all_objects

 * <p>Title: TestDBUtilNewInterface.java</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2007</p>
 * @Date Nov 4, 2008 2:58:51 PM
 * @author biaoping.yin
 * @version 1.0
 */
public class TestDBUtilNewInterface {
	
	public static void testObjectResult()
	{
		DBUtil dbUtil = new DBUtil();
		try {
			List list = dbUtil.executeSelectForList("select * from testnewface where object_id < 100", TestNewface.class);
			for(int i = 0; i < list.size(); i ++)
			{
				System.out.println(list.get(i));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	/**
	 * 普通分页查询
	 */
	public static void testObjectPagineResult()
	{
		DBUtil dbUtil = new DBUtil();
		try {
			List list = dbUtil.executeSelectForList("select * from testnewface where object_id",
															0,//offset分页数据的起点
															100, //每页最多取100条
															TestNewface.class);
			//list中存放当前页面的TestNewface对象列表，从0开始，每页取100条，第二页的数据从将从100开始。
			for(int i = 0;
					i < list.size();//当前页面的数据条数 
						i ++)
			{
				System.out.println(list.get(i));
			}
			
			long totalsize = dbUtil.getLongTotalSize();//总记录数
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	/**
	 * 预编译分页查询
	 */
	public static void testObjectPreparedPagineResult()
	{
		PreparedDBUtil dbUtil = new PreparedDBUtil();
		try {
			dbUtil.preparedSelect("select * from testnewface where object_id < ?",
															0,//offset分页数据的起点
															100 //每页最多取100条
															);
			dbUtil.setInt(1, 100000);
			List list = dbUtil.executePreparedForList(TestNewface.class);
			//list中存放当前页面的TestNewface对象列表，从0开始，每页取100条，第二页的数据从将从100开始。
			for(int i = 0;
					i < list.size();//当前页面的数据条数 
						i ++)
			{
				System.out.println(list.get(i));
			}
			
			long totalsize = dbUtil.getLongTotalSize();//总记录数
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public static void testObjectResultWithRowhandler()
	{
		DBUtil dbUtil = new DBUtil();
		try {
			List list = dbUtil.executeSelectForList("select * from testnewface where object_id < 100",
					TestNewface.class,new RowHandler()
			{

				public void handleRow(Object rowValue, Record record) {
//					String value = record.getString("object_id");
					TestNewface t = (TestNewface)rowValue;
					System.out.println(t);
					
				}
				
			});
			for(int i = 0; i < list.size(); i ++)
			{
				System.out.println(list.get(i));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public static void testXMLResult()
	{
		DBUtil dbUtil = new DBUtil();
		try {
			String list = dbUtil.executeSelectForXML("select * from testnewface where object_id < 100");
//			for(int i = 0; i < list.size(); i ++)
//			{
//				System.out.println(list.get(i));
//			}
			
			System.out.println(list);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	public static void testXMLResultWithRowHandler()
	{
		DBUtil dbUtil = new DBUtil();
		try {
			String list = dbUtil.executeSelectForXML("select * from testnewface where object_id < 100",new RowHandler()
			{

				public void handleRow(Object rowValue, Record record) {
					StringBuffer object = (StringBuffer)rowValue;
//					<record>
//			        <column name="OWNER" type="VARCHAR" javatype="String">
//			            <![CDATA[SYS]]>
//			        </column>
//			        <column name="OBJECT_NAME" type="VARCHAR" javatype="String">
//			            <![CDATA[I_CDEF1]]>
//			        </column>
//			        <column name="SUBOBJECT_NAME" type="VARCHAR" javatype="String">
//			            <![CDATA[null]]>
//			        </column>
//			        <column name="OBJECT_ID" type="NUMERIC" javatype="BigDecimal">
//			            <![CDATA[50]]>
//			        </column>
//			        <column name="DATA_OBJECT_ID" type="NUMERIC" javatype="BigDecimal">
//			            <![CDATA[50]]>
//			        </column>
//			        <column name="OBJECT_TYPE" type="VARCHAR" javatype="String">
//			            <![CDATA[INDEX]]>
//			        </column>
//			        <column name="CREATED" type="DATE" javatype="Date">
//			            <![CDATA[2005-08-30 13:50:25]]>
//			        </column>
//			        <column name="LAST_DDL_TIME" type="DATE" javatype="Date">
//			            <![CDATA[2005-08-30 13:50:25]]>
//			        </column>
//			        <column name="TIMESTAMP" type="VARCHAR" javatype="String">
//			            <![CDATA[2005-08-30:13:50:25]]>
//			        </column>
//			        <column name="STATUS" type="VARCHAR" javatype="String">
//			            <![CDATA[VALID]]>
//			        </column>
//			        <column name="TEMPORARY" type="VARCHAR" javatype="String">
//			            <![CDATA[N]]>
//			        </column>
//			        <column name="GENERATED" type="VARCHAR" javatype="String">
//			            <![CDATA[N]]>
//			        </column>
//			        <column name="SECONDARY" type="VARCHAR" javatype="String">
//			            <![CDATA[N]]>
//			        </column>
//			    </record> 
					object.append("<record>\r\n");
					object.append("		<column name=\"OWNER\">\r\n");
					try {
						object.append(record.getString("OWNER"));
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					object.append("		</column>\r\n");
					object.append("</record>");
					
				}
				
			});
//			for(int i = 0; i < list.size(); i ++)
//			{
//				System.out.println(list.get(i));
//			}
			
			System.out.println(list);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public static void testObject()
	{
		DBUtil dbUtil = new DBUtil();
		try {
			TestNewface testNew = (TestNewface)dbUtil.executeSelectForObject("select * from testnewface where object_id = 100",TestNewface.class);
//			for(int i = 0; i < list.size(); i ++)
//			{
//				System.out.println(list.get(i));
//			}
			
			System.out.println(testNew);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public static void testRecordArray()
	{
		String sql="select filelist,sel from td_test  where sel='0732' ";
		DBUtil db=new DBUtil();
		try
		{
		   db.executeSelect(sql);
		   Record[] records = db.getAllResults();
		   for(int i = 0; i < records.length; i ++)
		   {
			   /**
			    * 提供各种类型数据的获取方法
			    */
			   records[i].getString(0);//与records[i].getString("filelist")方法一致
		   }
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		   

	}
	
	
	
	
	public static void main(String[] args)
	{
//		testObjectResultWithRowhandler();
//		testObjectResult();
////		 testXMLResult();
//		 testObject();
//		 testObjectResult();
//		testXMLResult();
		testXMLResultWithRowHandler();
	}

}
