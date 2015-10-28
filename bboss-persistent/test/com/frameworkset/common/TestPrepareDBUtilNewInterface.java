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

import com.frameworkset.common.poolman.PreparedDBUtil;
import com.frameworkset.common.poolman.Record;
import com.frameworkset.common.poolman.handle.RowHandler;

public class TestPrepareDBUtilNewInterface {
	public static void all_objects()
	{
		PreparedDBUtil dbUtil = new PreparedDBUtil();
		try {
			dbUtil.preparedSelect("select owner from testnewface where object_id < ?");
			dbUtil.setInt(1, 100);
			List list = dbUtil.executePreparedForList(TestNewface.class);
			for(int i = 0; i < list.size(); i ++)
			{
				TestNewface testNewface = (TestNewface)list.get(i);
				System.out.println(testNewface);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public static void testObjectResultWithRowhandler()
	{
		PreparedDBUtil dbUtil = new PreparedDBUtil();
		try {
			dbUtil.preparedSelect("select * from testnewface where object_id < ?",0,10);//分页查询方法
			dbUtil.setInt(1, 100);
			List list = dbUtil.executePreparedForList(TestNewface.class,new RowHandler<TestNewface>()
			{

				public void handleRow(TestNewface t, Record record) {
					
					try {
						t.setCREATED(record.getDate("created"));
						t.setDATA_OBJECT_ID(record.getInt("DATA_OBJECT_ID"));
						//........设置其他的属性
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					System.out.println("row handler:"+t);
					
				}
				
			});
			long totalsize = dbUtil.getLongTotalSize();//获得总记录数
			for(int i = 0; i < list.size()/*list.size()当页记录数*/; i ++)//遍历当页记录
			{
				TestNewface testNewface = (TestNewface)list.get(i);
				System.out.println(testNewface);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	public static void testXMLResult()
	{
		PreparedDBUtil dbUtil = new PreparedDBUtil();
		try {
			dbUtil.preparedSelect("select * from testnewface where object_id < ?");
			dbUtil.setInt(1, 100);
			String xml = dbUtil.executePreparedForXML();
			System.out.println(xml);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	public static void testXMLResultWithRowHandler()
	{
		PreparedDBUtil dbUtil = new PreparedDBUtil();
		try {
			dbUtil.preparedSelect("select * from testnewface where object_id < ?");
			dbUtil.setInt(1, 100);
			//通过行处理器构造自己的xml串
			String xml = dbUtil.executePreparedForXML(new RowHandler()
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
			System.out.println(xml);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	/**
	 * ibatis
	 */
	public static void testObject()
	{
		PreparedDBUtil dbUtil = new PreparedDBUtil();
		try {
			dbUtil.preparedSelect("select * from testnewface where object_id = ?");
			dbUtil.setInt(1, 100);
			TestNewface testNew = (TestNewface)dbUtil.executePreparedForObject(TestNewface.class);
			System.out.println(testNew);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	public static void testRecordArray()
	{
		String sql="select filelist,sel from td_test  where sel=? ";
		PreparedDBUtil db=new PreparedDBUtil();
		try
		{
		   db.preparedSelect(sql);
		   db.setString(1, "0732");
		   db.executePrepared();
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
		all_objects();
	}
}
