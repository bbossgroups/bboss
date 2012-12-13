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

import java.io.StringWriter;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.frameworkset.spi.BaseApplicationContext;
import org.frameworkset.spi.SOAFileApplicationContext;
import org.frameworkset.spi.assemble.Pro;

import com.frameworkset.common.poolman.sql.PoolManResultSetMetaData;
import com.frameworkset.common.poolman.util.SQLManager;
import com.frameworkset.util.DaemonThread;
import com.frameworkset.util.ResourceInitial;
import com.frameworkset.util.VariableHandler.SQLStruction;
import com.frameworkset.velocity.BBossVelocityUtil;

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
 * @Date 2010-7-12 下午07:38:55
 * @author biaoping.yin
 * @version 1.0
 */
public class SQLUtil {
	protected BaseApplicationContext sqlcontext;
	protected static Map<String,SQLUtil> sqlutils = new HashMap<String,SQLUtil>(); 
	protected SQLCache cache = new SQLCache(); 
	protected static long refresh_interval = 5000;
	protected String defaultDBName = null;
	protected Map<String,SQLInfo> sqls;
	
	
	
	
	 /**
     * 默认的sql结构缓存器
     */
    private static final GloableSQLUtil globalSQLUtil = new GloableSQLUtil();
//	/**
//	 * sql语句velocity模板索引表，以sql语句的名称为索引
//	 * 当sql文件重新加载时，这些模板也会被重置
//	 */
//	private Map<String,SQLTemplate> sqlVelocityTemplates;
//	
	
	private static DaemonThread damon = null; 
	/**
	 * 
	 */
	private void trimValues()
	{
		if(sqlcontext == null)
			return;
		sqls = null;
		sqls = new HashMap<String,SQLInfo>();
		Set keys = this.sqlcontext.getPropertyKeys();
		if(keys != null && keys.size() > 0)
		{
			Iterator<String> keys_it = keys.iterator();
			while(keys_it.hasNext())
			{
				String key = keys_it.next();
				Pro pro = this.sqlcontext.getProBean(key);
				Object o = pro.getObject();
				if(o instanceof String)
				{
					
					String value = (String)o;
					
					if(value != null)
					{
						boolean istpl = pro.getBooleanExtendAttribute("istpl",true);//标识sql语句是否为velocity模板
						boolean multiparser = pro.getBooleanExtendAttribute("multiparser",false);//如果sql语句为velocity模板，则在批处理时是否需要每条记录都需要分析sql语句
						SQLTemplate sqltpl = null;
						value = value.trim();
						SQLInfo sqlinfo = new SQLInfo(key, value, istpl,multiparser);
						sqlinfo.setSqlutil(this);
						if(istpl)
						{
							sqltpl = new SQLTemplate(sqlinfo);
							sqlinfo.setSqltpl(sqltpl);
							BBossVelocityUtil.initTemplate(sqltpl);
						}
						
						sqls.put(key, sqlinfo);
					}
				}
			}
		}
	}
	
	
	           
	void reinit()
	{
		if(sqls != null)
		{
			this.sqls.clear();
			sqls = null;
		}
		this.cache.clear();
		String file = sqlcontext.getConfigfile();
		sqlcontext.destroy(true);
		sqlcontext = new SOAFileApplicationContext(file);		
		defaultDBName = sqlcontext.getProperty("default.dbname");
		trimValues();
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
	public SQLStruction getSQLStruction(SQLInfo sqlinfo,String newsql)
	{
		return this.cache.getSQLStruction(sqlinfo,newsql);
	}
	public SQLStruction getTotalsizeSQLStruction(SQLInfo totalsizesqlinfo,String totalsizesql)
	{
		return this.cache.getTotalsizeSQLStruction(totalsizesqlinfo,totalsizesql);
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
	
	public static void stopmonitor()
	{
		try {
			if(SQLUtil.damon != null)
				SQLUtil.damon.stopped();
		} catch (Throwable e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
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
						damon = new DaemonThread(refresh_interval,"SQL files Refresh Worker"); 
						damon.start();
						BaseApplicationContext.addShutdownHook(new Runnable(){

							public void run() {
								SQLUtil.stopmonitor();
								
							}});
					}
				}
			}
			damon.addFile(sqlfile, new ResourceSQLRefresh(sqlutil));
		}
		
	}
	private SQLUtil(String sqlfile) {
		sqlcontext = new SOAFileApplicationContext(sqlfile);		
		this.trimValues();
		defaultDBName = sqlcontext.getProperty("default.dbname");
//		refresh_interval = ApplicationContext.getApplicationContext().getLongProperty("sqlfile.refresh_interval", -1);
		
		checkSQLUtil(sqlfile,this);
		
//		if(refresh_interval > 0)
//		{
//			 damon = new DaemonThread(sqlfile,refresh_interval,new ResourceSQLRefresh(this));
//			 damon.start();
//		}
	}
	

	public SQLUtil() {
		// TODO Auto-generated constructor stub
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

	public SQLInfo getSQLInfo(String dbname, String sqlname) {
		String dbtype = SQLManager.getInstance().getDBAdapter(dbname)
		.getDBTYPE();
		
		SQLInfo sql = null;
		if(dbtype != null)
//			sql = sqlcontext.getProperty(sqlname + "-" + dbtype.toLowerCase());		
			sql = sqls.get(sqlname + "-" + dbtype.toLowerCase());
		if (sql == null) {
//			sql = sqlcontext.getProperty(sqlname);
			sql = sqls.get(sqlname);
		}
		if (sql == null) {
//			sql = sqlcontext.getProperty(sqlname + "-default");
			sql = sqls.get(sqlname + "-default");
		}		
		return sql;

	}
	
	public String getSQL(String dbname, String sqlname) {
		String dbtype = SQLManager.getInstance().getDBAdapter(dbname)
		.getDBTYPE();
		
		SQLInfo sql = null;
		if(dbtype != null)
//			sql = sqlcontext.getProperty(sqlname + "-" + dbtype.toLowerCase());		
			sql = sqls.get(sqlname + "-" + dbtype.toLowerCase());
		if (sql == null) {
//			sql = sqlcontext.getProperty(sqlname);
			sql = sqls.get(sqlname);
		}
		if (sql == null) {
//			sql = sqlcontext.getProperty(sqlname + "-default");
			sql = sqls.get(sqlname + "-default");
		}		
		return sql != null?sql.getSql():null;

	}
	
	
	public String getSQL(String dbname, String sqlname,Map variablevalues) {
		String dbtype = SQLManager.getInstance().getDBAdapter(dbname)
		.getDBTYPE();
		String newsql = null;
		SQLInfo sql = null;
		if(dbtype != null)
//			sql = sqlcontext.getProperty(sqlname + "-" + dbtype.toLowerCase());
			sql = sqls.get(sqlname + "-" + dbtype.toLowerCase());
		
		if (sql == null) {
//			sql = sqlcontext.getProperty(sqlname);
			sql = sqls.get(sqlname);
		}
		if (sql == null) {
//			sql = sqlcontext.getProperty(sqlname + "-default");
			sql = sqls.get(sqlname + "-default");
		}
		if(sql != null )
		{
			newsql = _getSQL(sql,variablevalues);
			
		}
		return newsql;

	}
	
	protected String _getSQL(SQLInfo sqlinfo,Map variablevalues)
	{
		String newsql = null;
		if(sqlinfo.istpl() )
		{
			StringWriter sw = new StringWriter();
			sqlinfo.getSqltpl().merge(BBossVelocityUtil.buildVelocityContext(variablevalues),sw);
			newsql = sw.toString();
		}
		else
			newsql = sqlinfo.getSql();
		return newsql;
	}
	
	public String evaluateSQL(String name,String sql,Map variablevalues) {
		
		if(sql != null &&  variablevalues != null && variablevalues.size() > 0)
		{
			sql = BBossVelocityUtil.evaluate(variablevalues, sqlcontext.getConfigfile()+"|"+name, sql);
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

	public SQLInfo getSQLInfo(String sqlname) {
		return getSQLInfo(null, sqlname);

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
	
	public PoolManResultSetMetaData getPoolManResultSetMetaData(String dbname,String sqlkey,ResultSetMetaData rsmetadata) throws SQLException
	{
		return this.cache.getPoolManResultSetMetaData(dbname, sqlkey, rsmetadata);
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



	public static GloableSQLUtil getGlobalSQLUtil() {
		return globalSQLUtil;
	}

}
