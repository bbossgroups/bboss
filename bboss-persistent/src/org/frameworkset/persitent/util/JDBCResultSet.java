package org.frameworkset.persitent.util;/*
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

import com.frameworkset.common.poolman.sql.PoolManResultSetMetaData;

import java.sql.ResultSet;

public class JDBCResultSet {
	protected ResultSet resultSet;
	protected PoolManResultSetMetaData metaData;

	public ResultSet getResultSet() {
		return resultSet;
	}

	public void setResultSet(ResultSet resultSet) {
		this.resultSet = resultSet;
	}

	public PoolManResultSetMetaData getMetaData() {
		return metaData;
	}

	public void setMetaData(PoolManResultSetMetaData metaData) {
		this.metaData = metaData;
	}
}
