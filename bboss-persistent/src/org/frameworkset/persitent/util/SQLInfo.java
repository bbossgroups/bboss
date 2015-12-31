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

/**
 * <p>Title: SQLInfo.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2007</p>
 * @Date 2012-12-5 上午9:46:28
 * @author biaoping.yin
 * @version 1.0
 */
public class SQLInfo {
	private String sqlname;
	private String sql;
	private SQLTemplate sqltpl;
	private boolean istpl;
	private boolean multiparser;
	/**
	 * @param multiparser the multiparser to set
	 */
	public void setMultiparser(boolean multiparser) {
		this.multiparser = multiparser;
	}

	private SQLUtil sqlutil;
	public SQLInfo(String sqlname, String sql,boolean istpl,boolean multiparser) {
		super();		
		this.sqlname = sqlname;
		this.sql = sql;
//		this.sqltpl = sqltpl;
		this.istpl = istpl;
		this.multiparser = multiparser;
	}
	
	
	public SQLInfo(String sql,boolean istpl,boolean multiparser) {
		super();		
		this.sql = sql;
		this.istpl = istpl;
		this.multiparser = multiparser;
//		
	}
//	public SQLInfo(String sql) {
//				
//		this(sql,false,false);
////		
//	}
	public boolean istpl()
	{
		return this.istpl;
	}
	public void setIstpl(boolean istpl)
	{
		this.istpl = istpl;
	}
	public boolean multiparser()
	{
		return multiparser;
	}
	
	public String getSqlname() {
		return sqlname;
	}
	public void setSqlname(String sqlname) {
		this.sqlname = sqlname;
	}
	public String getSql() {
		return sql;
	}
	public void setSql(String sql) {
		this.sql = sql;
	}
	public SQLTemplate getSqltpl() {
		return sqltpl;
	}
	public void setSqltpl(SQLTemplate sqltpl) {
		this.sqltpl = sqltpl;
	}


	public SQLUtil getSqlutil() {
		return sqlutil;
	}


	public void setSqlutil(SQLUtil sqlutil) {
		this.sqlutil = sqlutil;
	}
	
	public boolean equals(Object obj)
	{
		if(obj == null)
			return false;
		if(obj instanceof SQLInfo)
		{
			SQLInfo o = (SQLInfo)obj;
			return this.getSql().equals(o.getSql());
		}
		else
		{
			return false;
		}
	}
	public boolean fromConfig()
	{
		return this.sqlutil != null && this.sqlutil.fromConfig();
	}
	public int compareTo(SQLInfo sql)
	{
		return this.sql.compareTo(sql.getSql());
	}
	
	public SQLInfo getSQLInfo(String dbname,String sqlname)
	{
		return this.sqlutil.getSQLInfo(dbname, sqlname);
	}
	
	public String getPlainSQL(String dbname,String sqlname)
	{
		return this.sqlutil.getPlainSQL(dbname, sqlname);
	}


}
