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
package com.frameworkset.unpool;

import java.sql.SQLException;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.frameworkset.common.poolman.DBUtil;
import com.frameworkset.common.poolman.SQLExecutor;
import com.frameworkset.common.poolman.monitor.AbandonedTraceExt;
import com.frameworkset.common.poolman.util.JDBCPoolMetaData;
import com.frameworkset.orm.transaction.TransactionManager;

public class UnpoolTest {
	@Before
	public void init()
	{
		DBUtil.startPoolFromConf("com/frameworkset/unpool/commondb.xml");
	}
	
	@Test
	public void selectSql()
	{
		try {
			
			String file = SQLExecutor.queryField("select 1 as LOG_FIELD from dual");
			System.out.println(file);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void selectTXSql()
	{
		TransactionManager tm = new TransactionManager(); 
		try {
			tm.begin();
			SQLExecutor.insert("insert into dual(col1) values(1)");
			String file = SQLExecutor.queryField("select col1 from dual");
			tm.getTransaction().printStackTrace();
			System.out.println("活动连接数:"+DBUtil.getNumActive());
			System.out.println("活动高峰连接数:"+DBUtil.getMaxNumActive());
			List<AbandonedTraceExt> t = DBUtil.getGoodTraceObjects();
			 t.get(0).printStackTrace();
			tm.commit();
			System.out.println("col1："+file);
		} catch (Exception e) {
			try {
				tm.rollback();
			} catch (Exception e2) {
				// TODO: handle exception
			}
			e.printStackTrace();
		}
	}
	
	
	@Test
	public void unpooldbmeta()
	{
		
		JDBCPoolMetaData meta = DBUtil.getJDBCPoolMetaData();
		System.out.println(meta.getDriverVersion());
		
		
	}
	
	@Test
	public void destroy()
	{
		
	}

}
