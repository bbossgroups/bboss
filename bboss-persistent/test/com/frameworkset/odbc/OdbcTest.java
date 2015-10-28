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
package com.frameworkset.odbc;

import java.sql.SQLException;
import java.util.List;

import org.junit.Test;

import com.frameworkset.common.poolman.DBUtil;
import com.frameworkset.common.poolman.SQLExecutor;

public class OdbcTest {
	@Test
	public void selectSql()
	{
		try {
			DBUtil.startPoolFromConf("com/frameworkset/odbc/odbc.xml");
			String file = SQLExecutor.queryField("select LOG_FIELD from testlog2_2 where LOG_FIELD is not null");
			System.out.println(file);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void selectSqlList()
	{
		try {
			DBUtil.startPoolFromConf("com/frameworkset/odbc/odbc.xml");
			List<TransLogTableModel> files = SQLExecutor.queryList(TransLogTableModel.class,"select LOG_FIELD from testlog2_2 where LOG_FIELD is not null");
			for(TransLogTableModel f:files)
			{
				System.out.println(f.getLOG_FIELD());
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
