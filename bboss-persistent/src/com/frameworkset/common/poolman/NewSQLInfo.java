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

import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.frameworkset.persitent.util.SQLInfo;
import org.frameworkset.persitent.util.SQLUtil;

import com.frameworkset.common.poolman.sql.PoolManResultSetMetaData;
import com.frameworkset.util.VariableHandler.SQLStruction;

public class NewSQLInfo
{
	private SQLStruction sqlstruction;
	public NewSQLInfo(String newsql)
	{
		this.newsql = newsql;
	}
	public NewSQLInfo(String newsql,String newtotalsizesql)
	{
		this.newsql = newsql;
		this.newtotalsizesql = newtotalsizesql;
	}
	public NewSQLInfo(SQLInfo sqlinfo,SQLInfo totalsizesqlinfo)
	{
		this.oldsql = sqlinfo;
		this.oldtotalsizesql = totalsizesqlinfo;
		this.newsql = this.oldsql.getSql();
		if(totalsizesqlinfo != null)
			this.newtotalsizesql = totalsizesqlinfo.getSql(); 
	}
	
	public NewSQLInfo(SQLInfo sqlinfo)
	{
		this.oldsql = sqlinfo;
		this.newsql = this.oldsql.getSql();
		 
	}
	
	public boolean equals(Object other)
	{
		if(other == null)
			return false;
		if(other instanceof NewSQLInfo)
		{
			return ((NewSQLInfo)other).getNewsql().equals(this.newsql);
		}
		else
			return false;
	}
	private String newsql = null;
    private String newtotalsizesql ;
    private SQLInfo oldsql;
    private SQLInfo oldtotalsizesql;
	public String getNewsql() {
		return newsql;
	}
	public void setNewsql(String newsql) {
		this.newsql = newsql;
	}
	public String getNewtotalsizesql() {
		return newtotalsizesql;
	}
	public void setNewtotalsizesql(String newtotalsizesql) {
		this.newtotalsizesql = newtotalsizesql;
	}
	public SQLInfo getOldsql() {
		return oldsql;
	}
	public void setOldsql(SQLInfo oldsql) {
		this.oldsql = oldsql;
	}
	public SQLInfo getOldtotalsizesql() {
		return oldtotalsizesql;
	}
	public void setOldtotalsizesql(SQLInfo oldtotalsizesql) {
		this.oldtotalsizesql = oldtotalsizesql;
	}
	public SQLStruction getSqlstruction() {
		return sqlstruction;
	}
	public void setSqlstruction(SQLStruction sqlstruction) {
		this.sqlstruction = sqlstruction;
	}
	
	public PoolManResultSetMetaData getPoolManResultSetMetaData(String dbname,String sqlkey,ResultSetMetaData rsmetadata) throws SQLException
	{
		if(this.oldsql != null && this.oldsql.getSqlutil() != null)
		{
			if(oldsql.istpl())//如果sql语句是一个
			{
				return this.oldsql.getSqlutil().getPoolManResultSetMetaData(dbname, sqlkey, rsmetadata);
			}
			else
			{
				return this.oldsql.getSqlutil().getPoolManResultSetMetaData(dbname, oldsql.getSqlname(), rsmetadata);
			}
		}
		else
		{
			return SQLUtil.getGlobalSQLUtil().getPoolManResultSetMetaData(dbname, sqlkey, rsmetadata);
		}
	}
	
}
