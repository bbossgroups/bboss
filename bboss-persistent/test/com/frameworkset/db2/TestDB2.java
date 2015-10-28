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
package com.frameworkset.db2;

import java.sql.SQLException;

import org.junit.Test;

import com.frameworkset.common.poolman.DBUtil;
import com.frameworkset.common.poolman.PreparedDBUtil;
import com.frameworkset.common.poolman.sql.TableMetaData;

public class TestDB2 {
	@Test
	public void testPagine()
	{
		PreparedDBUtil dbutil = new PreparedDBUtil();
		try {
			dbutil.preparedSelect("select * from td_sm_log",11,20);
			dbutil.executePrepared();
			System.out.println(dbutil.size());
			System.out.println(dbutil.getString(1, "log_id"));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	@Test
	public void testSelect()
	{
		PreparedDBUtil dbutil = new PreparedDBUtil();
		try {
			dbutil.executeSelect("select * from td_sm_log",11,20);
			
			System.out.println(dbutil.size());
			System.out.println(dbutil.getString(1, "log_id"));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	@Test
	public void testMeta()
	{
		TableMetaData meta = DBUtil.getTableMetaData("bspf","tableinfo");
		
		System.out.println(meta);
	}

}
