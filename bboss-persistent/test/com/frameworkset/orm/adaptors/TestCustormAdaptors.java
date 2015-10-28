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
package com.frameworkset.orm.adaptors;

import java.sql.SQLException;

import org.junit.Test;

import com.frameworkset.common.poolman.DBUtil;

public class TestCustormAdaptors {
	@Test
	public void testAdaptors()
	{
		System.out.println("bspf:"+DBUtil.getDBAdapter("bspf"));
		System.out.println("other_oracle:"+DBUtil.getDBAdapter("other_oracle"));
		System.out.println("db2:"+DBUtil.getDBAdapter("db2"));
		System.out.println("mysql:"+DBUtil.getDBAdapter("mysql"));
		
	}
	
	@Test
	public void testInterceptors()
	{
		System.out.println("bspf:"+DBUtil.getInterceptorInf("bspf"));
		System.out.println("other_oracle:"+DBUtil.getInterceptorInf("other_oracle"));
		System.out.println("db2:"+DBUtil.getInterceptorInf("db2"));
		System.out.println("mysql:"+DBUtil.getInterceptorInf("mysql"));
		
	}
	
	@Test
	public void testDB()
	{
		DBUtil db = new DBUtil();
		try {
			db.executeSelect("select 1 from dual");
			System.out.println("db.size:"+db.size());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
}
