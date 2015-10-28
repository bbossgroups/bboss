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
package com.frameworkset.sqlexecutor;

import java.sql.SQLException;
import java.util.List;

import org.junit.Test;

import com.frameworkset.common.poolman.SQLExecutor;

public class SimpleApiTest1 {
	@Test
	public void dblist() throws SQLException
	{
		String sql ="insert into xxxx(f1,f2,f3) values(#[f1],#[f2],#[f2])"; 
	
		List beans = null;
		SQLExecutor.insert("bspf",sql,beans);
		
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
	
	

}
