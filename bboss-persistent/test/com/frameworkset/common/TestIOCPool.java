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

import org.junit.Test;

import com.frameworkset.common.poolman.DBUtil;
import com.frameworkset.common.poolman.SQLExecutor;
import com.frameworkset.common.poolman.util.JDBCPool;

public class TestIOCPool {
	@Test
	public void testc3p0()
	{
		try {
			System.out.println(SQLExecutor.queryObjectWithDBName(int.class, "c3p0", "select 1 from dual"));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void testdbcp14()
	{
		try {
			System.out.println(SQLExecutor.queryObjectWithDBName(int.class, "bspf", "select 1 from dual"));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void testmulti()
	{
		try {
			JDBCPool pool = DBUtil.getPool("bspf");
			JDBCPool pool_1 = DBUtil.getPool("proxool");
			System.out.println(SQLExecutor.queryObjectWithDBName(int.class, "bspf", "select count(1) from TD_APP_BOM"));
			System.out.println(SQLExecutor.queryObjectWithDBName(int.class, "proxool", "select count(1) from TD_APP_BOM"));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
