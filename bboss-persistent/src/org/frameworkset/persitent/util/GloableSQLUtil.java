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
package org.frameworkset.persitent.util;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.frameworkset.spi.BaseApplicationContext;

import com.frameworkset.common.poolman.sql.PoolManResultSetMetaData;
import com.frameworkset.util.VariableHandler.SQLStruction;
import com.frameworkset.velocity.BBossVelocityUtil;

public class GloableSQLUtil extends SQLUtil {

	
	public GloableSQLUtil()
	{
		sqls = new HashMap<String,SQLInfo>();
	}
	@Override
	public SQLStruction getSQLStruction(SQLInfo sqlinfo, String newsql) {
		// TODO Auto-generated method stub
		return super.getSQLStruction(sqlinfo, newsql);
	}

	@Override
	public SQLStruction getTotalsizeSQLStruction(SQLInfo totalsizesqlinfo,
			String totalsizesql) {
		// TODO Auto-generated method stub
		return super.getTotalsizeSQLStruction(totalsizesqlinfo, totalsizesql);
	}

	@Override
	public SQLInfo getSQLInfo(String dbname, String sql) {
		throw new java.lang.UnsupportedOperationException();
	}

	@Override
	public String getSQL(String dbname, String sql) {
		return sql;
	}

	
	public String getSQL(String dbname, String sql, Map variablevalues) {
		throw new java.lang.UnsupportedOperationException();
	}


	public String getSQL(String sql,boolean istpl,boolean multiparser, Map variablevalues) {
		SQLInfo sqlinfo = getSQLInfo(sql,istpl,multiparser);
		String newsql = null;
		newsql = _getSQL(sqlinfo,variablevalues);
		return newsql;
	}

	@Override
	public String[] getPropertyKeys() {
		throw new java.lang.UnsupportedOperationException();
	}
	public SQLInfo getSQLInfo(String sql)
	{
		return getSQLInfo(sql,true,true);
	}
	
	public SQLInfo getSQLInfo(String sql,boolean istpl,boolean multiparser) {
		
				
				SQLInfo sqlinfo = null;
		
//					sql = sqlcontext.getProperty(sqlname + "-" + dbtype.toLowerCase());		
				sqlinfo = sqls.get(sql);
				if (sqlinfo == null) {
//					sql = sqlcontext.getProperty(sqlname);
					
					synchronized(GloableSQLUtil.class)
					{
						sqlinfo = sqls.get(sql);
						if (sqlinfo != null)
							return sqlinfo;
						SQLTemplate sqltpl = null;
						sqlinfo = new SQLInfo(sql,sql, istpl,multiparser);
						sqlinfo.setSqlutil(this);
						if(istpl)
						{
							sqltpl = new SQLTemplate(sqlinfo);
							sqlinfo.setSqltpl(sqltpl);
							BBossVelocityUtil.initTemplate(sqltpl);
							sqltpl.process();
						}
						sqls.put(sql, sqlinfo);
					}
					
					
				}
				return sqlinfo;
	}

	@Override
	public String getSQL(String sql) {
		return sql;
	}

	@Override
	public PoolManResultSetMetaData getPoolManResultSetMetaData(String dbname,
			String sqlkey, ResultSetMetaData rsmetadata) throws SQLException {
		// TODO Auto-generated method stub
		return super.getPoolManResultSetMetaData(dbname, sqlkey, rsmetadata);
	}

	

	@Override
	public String getSQLFile() {
		throw new java.lang.UnsupportedOperationException();
	}

	@Override
	public String evaluateSQL(String name, String sql, Map variablevalues) {
		throw new java.lang.UnsupportedOperationException();
	}

	@Override
	public String getDBName(String sqlname) {
		throw new java.lang.UnsupportedOperationException();
	}

	@Override
	public Map getMapSQLs(String sqlname) {
		throw new java.lang.UnsupportedOperationException();
	}

	@Override
	public Map getMapSQLs(String dbname, String sqlname) {
		throw new java.lang.UnsupportedOperationException();
	}

	@Override
	public List getListSQLs(String sqlname) {
		throw new java.lang.UnsupportedOperationException();
	}

	@Override
	public List getListSQLs(String dbname, String sqlname) {
		throw new java.lang.UnsupportedOperationException();
	}

	@Override
	public Set getSetSQLs(String sqlname) {
		throw new java.lang.UnsupportedOperationException();
	}

	@Override
	public Set getSetSQLs(String dbname, String sqlname) {
		throw new java.lang.UnsupportedOperationException();
	}

	@Override
	public BaseApplicationContext getSqlcontext() {
		throw new java.lang.UnsupportedOperationException();
	}

	@Override
	public long getRefresh_interval() {
		throw new java.lang.UnsupportedOperationException();
	}

}
