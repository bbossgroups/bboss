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
package com.frameworkset.common.poolman;

import java.util.List;

import org.frameworkset.persitent.util.SQLInfo;

public class ListSQLParams {
	private List<SQLParams> sqlparams;
	
	private SQLInfo sql;
	public ListSQLParams(List<SQLParams> sqlparams,SQLInfo sql) {
		this.sqlparams = sqlparams;		
		this.sql = sql;
		
	}
	public List<SQLParams> getSqlparams() {
		return sqlparams;
	}
	
	public boolean multiparser()
	{
		if(sql == null)
			return true;
		return sql.multiparser();
	}

}
