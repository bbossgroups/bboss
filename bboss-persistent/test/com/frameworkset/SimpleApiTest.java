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
package com.frameworkset;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.junit.Test;

import com.frameworkset.common.poolman.SQLExecutor;

public class SimpleApiTest {
	@Test
	public void testsqlserver() throws Exception
	{
		int re = SQLExecutor.queryObjectWithDBName(int.class, "sqlserverdb","select count(*) from ZZ_COMPLAIN where 1<>1" );
		System.out.println(re);
	}
	@Test
	public void dblist() throws SQLException
	{
		String sql ="insert into xxxx(f1,f2,f3) values(#[f1],#[f2],#[f2])"; 
	
		List beans = null;
		SQLExecutor.insert(sql,beans);
		
		sql ="insert into xxxx(f1,f2,f3) values(#[f1],#[f2],#[f2])"; 
		
		SQLExecutor.update("bspf",sql,beans);
		sql ="insert into xxxx(f1,f2,f3) values(#[f1],#[f2],#[f2])"; 
		SQLExecutor.delete("bspf",sql,beans);
	}
	
	@Test
	public void dbobject() throws SQLException
	{
		String sql ="insert into xxxx(f1,f2,f3) values(#[f1],#[f2],#[f2])"; 
//		
		Object bean = null;
		SQLExecutor.insert("bspf",sql,bean);
//		
		sql ="insert into xxxx(f1,f2,f3) values(#[f1],#[f2],#[f2])"; 
//		
		SQLExecutor.update("bspf",sql,bean);
		sql ="insert into xxxx(f1,f2,f3) values(#[f1],#[f2],#[f2])"; 
		
		SQLExecutor.delete("bspf",sql,bean);
	}
	
	@Test
	public void dbqueryobject()
	{
		String sql ="insert into xxxx(f1,f2,f3) values(#[f1],#[f2],#[f2])"; 
//		
		Object bean = null;
//		SQLExecutor.query("bspf",sql,bean);
//		
		
	}
	
	
	@Test
	  public void testinsert(){
		  MetadataSet m = new MetadataSet();
		
		  Date date = new Date();
		  Timestamp timestamp = new Timestamp(date.getTime());
		  m.setCreate_time(timestamp);
		  m.setCreator("zhangsan");
		  m.setMetadata_set_id("1");
		  m.setMetadata_set_code("aaaaaaaa");
		  m.setMetadata_set_name("bbbbbbbb");
		  m.setModifier("lisi");
		  
		  m.setModify_time(timestamp);
		  m.setData_type_code("java");
		  m.setRemark("remark");
		 
		  String sql ="insert into xxxx(f1,f2,f3) values(#[create_time],#[creator],#[data_type_code])"; 
//			
			Object bean = null;
			try {
				SQLExecutor.insertBean(sql,m);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		  
	  }
	
	

}
