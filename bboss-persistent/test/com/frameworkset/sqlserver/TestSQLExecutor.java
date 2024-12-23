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
package com.frameworkset.sqlserver;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import org.junit.Test;

import com.frameworkset.common.poolman.SQLExecutor;
import com.frameworkset.util.ListInfo;

public class TestSQLExecutor {
	@Test
	public void testpagine() throws SQLException
	{
		String sql = "select * from sit_user_cp";
		ListInfo listinfo = SQLExecutor.queryListInfoBean(HashMap.class, sql, 0, 10, null);
		List<HashMap> datas = SQLExecutor.queryListBean(HashMap.class, sql, null);
	}

}
