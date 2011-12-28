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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.frameworkset.spi.BaseApplicationContext;
import org.frameworkset.spi.SOAFileApplicationContext;
import org.frameworkset.spi.assemble.Pro;

import com.frameworkset.common.poolman.util.SQLManager;
import com.frameworkset.util.DaemonThread;
import com.frameworkset.util.ResourceInitial;
import com.frameworkset.util.VelocityUtil;

/**
 * <p>
 * Title: SQLUtil.java
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * bboss workgroup
 * </p>
 * <p>
 * Copyright (c) 2007
 * </p>
 * 
 * @Date 2010-7-12 ÏÂÎç07:38:55
 * @author biaoping.yin
 * @version 1.0
 */
public class SQLUtil {
	private BaseApplicationContext sqlcontext;
	private static Map<String,SQLUtil> sqlutils = new HashMap<String,SQLUtil>(); 
	
	private static long refresh_interval = 5000;
	private String defaultDBName = null;
	private static DaemonThread damon = null; 
	
	
	
	void reinit()
	{
		String file = sqlcontext.getConfigfile();
		sqlcontext.destroy(true);
		sqlcontext = new SOAFileApplicationContext(file);		
		defaultDBName = sqlcontext.getProperty("default.dbname");
//		if(refresh_interval > 0 )
//		{
//			if(damon == null)
//			{
//				damon = new DaemonThread(file,refresh_interval,new ResourceSQLRefresh(this));
//				 damon.start();
//			}
//		}
//		else
//		{
//			if(damon != null)
//			{
//				
//				 try
//				 {
//					 damon.interrupt();
//				 }
//				 catch(Exception e)
//				 {
//					 try {
//						damon.stop();
//					} catch (Exception e2) {
//						damon.stopped();
//					}
//				 }
//				 damon = null;
//			}
//		}
		
		
	}
	static class ResourceSQLRefresh implements ResourceInitial
	{
		private SQLUtil sqlutil ;
		public ResourceSQLRefresh(SQLUtil sqlutil)
		{
			this.sqlutil = sqlutil;
		}
		public void reinit() {
			sqlutil.reinit();
		}
		
	}
	
	public String getSQLFile()
	{
		return this.sqlcontext.getConfigfile();
		
	}
	private static Object lock = new Object();
	private static void checkSQLUtil(String sqlfile,SQLUtil sqlutil){
		
		refresh_interval = BaseApplicationContext.getSQLFileRefreshInterval();
		if(refresh_interval > 0)
		{
			if(damon == null)
			{
				synchronized(lock)
				{
					if(damon == null)
					{
						damon = new DaemonThread(refresh_interval,"SQLFILES REFresh Worker"); 
						damon.start();
					}
				}
			}
			damon.addFile(sqlfile, new ResourceSQLRefresh(sqlutil));
		}
		
	}
	private SQLUtil(String sqlfile) {
		sqlcontext = new SOAFileApplicationContext(sqlfile);		
		defaultDBName = sqlcontext.getProperty("default.dbname");
//		refresh_interval = ApplicationContext.getApplicationContext().getLongProperty("sqlfile.refresh_interval", -1);
		
		checkSQLUtil(sqlfile,this);
		
//		if(refresh_interval > 0)
//		{
//			 damon = new DaemonThread(sqlfile,refresh_interval,new ResourceSQLRefresh(this));
//			 damon.start();
//		}
	}
	

	public static SQLUtil getInstance(String sqlfile) {
		
		SQLUtil sqlUtil = sqlutils.get(sqlfile);
		if(sqlUtil != null)
			return sqlUtil;
		synchronized(sqlutils)
		{
			sqlUtil = sqlutils.get(sqlfile);
			if(sqlUtil != null)
				return sqlUtil;
			sqlUtil = new SQLUtil(sqlfile);
			
			sqlutils.put(sqlfile, sqlUtil);
			
		}
		
		return sqlUtil;
	}

	public String getSQL(String dbname, String sqlname) {
		String dbtype = SQLManager.getInstance().getDBAdapter(dbname)
		.getDBTYPE();
		
		String sql = null;
		if(dbtype != null)
			sql = sqlcontext.getProperty(sqlname + "-" + dbtype.toLowerCase());		
		if (sql == null) {
			sql = sqlcontext.getProperty(sqlname);
		}
		if (sql == null) {
			sql = sqlcontext.getProperty(sqlname + "-default");
		}		
		return sql;

	}
	
	
	public String getSQL(String dbname, String sqlname,Map variablevalues) {
		String dbtype = SQLManager.getInstance().getDBAdapter(dbname)
		.getDBTYPE();
		String sql = null;
		if(dbtype != null)
			sql = sqlcontext.getProperty(sqlname + "-" + dbtype.toLowerCase());
		
		if (sql == null) {
			sql = sqlcontext.getProperty(sqlname);
		}
		if (sql == null) {
			sql = sqlcontext.getProperty(sqlname + "-default");
		}
		if(sql != null &&  variablevalues != null && variablevalues.size() > 0)
		{
			sql = VelocityUtil.evaluate(variablevalues, sqlcontext.getConfigfile()+"|"+sqlname, sql);
		}
		return sql;

	}
	
	public String evaluateSQL(String name,String sql,Map variablevalues) {
		
		if(sql != null &&  variablevalues != null && variablevalues.size() > 0)
		{
			sql = VelocityUtil.evaluate(variablevalues, sqlcontext.getConfigfile()+"|"+name, sql);
		}
		return sql;

	}
	
	public String getSQL(String sqlname,Map variablevalues) {
		return getSQL(null, sqlname,variablevalues) ;
	}

	public String getDBName(String sqlname) {
		Pro pro = sqlcontext.getProBean(sqlname);

		if (pro == null) {
			return defaultDBName;

		}
		return pro.getStringExtendAttribute("dbname");

	}
	
	public String[] getPropertyKeys()
	{
		Set<String> keys = this.sqlcontext.getPropertyKeys();
		if(keys == null )
			return new String[]{};
		String[] rets = new String[keys.size()];
		Iterator<String> its = keys.iterator();
		int i = 0;
		while(its.hasNext())
		{
			rets[i] = its.next();
			i ++;
		}
		
		return rets;
	}

	public String getSQL(String sqlname) {
		return getSQL(null, sqlname);

	}

	public Map getMapSQLs(String sqlname) {
		return getMapSQLs(getDBName(sqlname), sqlname);

	}

	public Map getMapSQLs(String dbname, String sqlname) {
		Map sqls = sqlcontext.getMapProperty(sqlname);
		if (sqls == null) {
			String dbtype = SQLManager.getInstance().getDBAdapter(dbname)
					.getDBTYPE();
			sqls = sqlcontext.getMapProperty(sqlname + "-"
					+ dbtype.toLowerCase());

		}
		if (sqls == null) {
			sqls = sqlcontext.getMapProperty(sqlname + "-default");
		}
		return sqls;

	}

	public List getListSQLs(String sqlname) {
		return getListSQLs(getDBName(sqlname), sqlname);

	}

	public List getListSQLs(String dbname, String sqlname) {
		List sqls = sqlcontext.getListProperty(sqlname);
		if (sqls == null) {
			String dbtype = SQLManager.getInstance().getDBAdapter(dbname)
					.getDBTYPE();
			sqls = sqlcontext.getListProperty(sqlname + "-"
					+ dbtype.toLowerCase());
		}
		if (sqls == null) {
			sqls = sqlcontext.getListProperty(sqlname + "-default");
		}
		return sqls;

	}

	public Set getSetSQLs(String sqlname) {
		return getSetSQLs(getDBName(sqlname), sqlname);

	}

	public Set getSetSQLs(String dbname, String sqlname) {
		Set sqls = sqlcontext.getSetProperty(sqlname);
		if (sqls == null) {
			String dbtype = SQLManager.getInstance().getDBAdapter(dbname)
					.getDBTYPE();
			sqls = sqlcontext.getSetProperty(sqlname + "-"
					+ dbtype.toLowerCase());
		}
		if (sqls == null) {
			sqls = sqlcontext.getSetProperty(sqlname + "-default");
		}
		return sqls;

	}


	/**
	 * @return the sqlcontext
	 */
	public BaseApplicationContext getSqlcontext() {
		return sqlcontext;
	}
	
	/**
	 * @return the refresh_interval
	 */
	public long getRefresh_interval() {
		return refresh_interval;
	}
	
	public static List<String> getSQLFiles()
	{
		Iterator<String> it = sqlutils.keySet().iterator();
		List<String> files = new ArrayList<String>();
		while(it.hasNext())
			files.add(it.next());
		return files;
	}

}
