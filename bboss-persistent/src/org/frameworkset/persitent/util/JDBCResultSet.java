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
import com.frameworkset.orm.adapter.DB;

import java.sql.ResultSet;
import java.sql.SQLException;

public class JDBCResultSet {
	protected ResultSet resultSet;
	protected PoolManResultSetMetaData metaData;
	protected DB dbadapter;
	public ResultSet getResultSet() {
		return resultSet;
	}
	public DB getDbadapter(){
		return dbadapter;
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
	public boolean isOracleTimestamp(int sqlType){
		return dbadapter.isOracleTimestamp( sqlType);
	}

	public void setDbadapter(DB dbadapter) {
		this.dbadapter = dbadapter;
	}

	public Object getValue(  int i, String colName,int sqlType) throws Exception
	{
		if(!this.isOracleTimestamp(sqlType)) {
			return this.resultSet.getObject(i + 1);
		}
		else{
			return this.resultSet.getTimestamp(i + 1);
		}
	}

	public Object getValue( String colName) throws Exception
	{
		if(colName == null)
			return null;
		Object value = this.resultSet.getObject(colName);
		return value;
	}


	public Object getValue( String colName,int sqlType) throws Exception
	{
		if(colName == null)
			return null;
		if(!this.isOracleTimestamp(sqlType)) {
			return this.resultSet.getObject(colName);
		}
		else{
			return this.resultSet.getTimestamp(colName);
		}

	}

	public Object getDateTimeValue( String colName) throws Exception
	{
		if(colName == null)
			return null;
		try {
			Object value = this.resultSet.getTimestamp(colName);
			return value;
		}
		catch (Exception e){
			Object value = this.resultSet.getDate(colName);
			return value;
		}
	}


	public boolean next() throws SQLException {
		return resultSet.next();
	}
}
