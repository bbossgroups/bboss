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

import com.frameworkset.common.poolman.sql.PoolManResultSetMetaData;
import com.frameworkset.util.VariableHandler.SQLStruction;
import com.frameworkset.velocity.BBossVelocityUtil;
import org.frameworkset.cache.EdenConcurrentCache;
import org.frameworkset.spi.BaseApplicationContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class GloableSQLUtil extends SQLUtil {
	private static final Logger logger = LoggerFactory.getLogger(GloableSQLUtil.class);
	protected EdenConcurrentCache<String,SQLInfo> tplEdenConcurrentCache;

	private Lock tplCacheLock = new ReentrantLock();
	GloableSQLUtil(int gloableResultMetaCacheSize,int maxGloableTemplateCacheSize,int globalKeySqlStructionCacheSize)
	{
		super(gloableResultMetaCacheSize,globalKeySqlStructionCacheSize);
		tplEdenConcurrentCache = new EdenConcurrentCache<String,SQLInfo>(maxGloableTemplateCacheSize);
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
		return getSQLInfo( sql, false, false);
	}
	
	public SQLInfo getSQLInfo(String sql,boolean istpl,boolean multiparser) {
		
				
				SQLInfo sqlinfo = null;
				if(!istpl){
					sqlinfo = new SQLInfo(sql, sql, istpl, multiparser);
					sqlinfo.setSqlutil(this);
					return sqlinfo;
				}
				boolean outOfSize = false;
				sqlinfo = tplEdenConcurrentCache.get(sql);
				if (sqlinfo == null) {
					try {
						tplCacheLock.lock();
						sqlinfo = tplEdenConcurrentCache.get(sql);
						if (sqlinfo != null)
							return sqlinfo;
						SQLTemplate sqltpl = null;
						sqlinfo = new SQLInfo(sql, sql, istpl, multiparser);
						sqlinfo.setSqlutil(this);

						sqltpl = new SQLTemplate(sqlinfo);
						sqlinfo.setSqltpl(sqltpl);
						BBossVelocityUtil.initDBTemplate(sqltpl);
						sqltpl.process();

						outOfSize = tplEdenConcurrentCache.put(sql, sqlinfo);

					}
					finally {
						tplCacheLock.unlock();
					}
					if(outOfSize && logger.isWarnEnabled()) {
						StringBuilder info = new StringBuilder();
						info.append("\r\n**********************************************************************\r\n")
								.append("*********************************警告*********************************\r\n")
								.append("**********************************************************************\r\n")
								.append("调用GloableSQLUtil getSQLInfo 方法从tplEdenConcurrentCache获取[")
								.append(sql).append("] 对应的信息时，检测到缓冲区记录数超出cache允许的最大cache size:")
								.append(tplEdenConcurrentCache.getMaxSize())
								.append(",\r\n导致告警原因分析:\r\n本条sql或者其他sql语句直接硬编码在代码中;")
								.append("\r\n本条sql或者其他sql语句可能存在不断变化的值参数;")
								.append("\r\n本条sql或者其他sql语句可能存在的$var模式的变量并且$var的值不断变化;")
								.append("\r\n优化建议：\r\n将sql中可能存在不断变化的值参数转化为绑定变量或者#[variable]变量，或将sql中可能存在的$var模式的变量转换为#[varibale]模式的变量，并采用配置文件来管理sql语句，以提升系统性能!")
								.append("\n\r**********************************************************************")
								.append("\n\r**********************************************************************");
						logger.warn(info.toString());
					}
					
				}
				return sqlinfo;
	}

	@Override
	public String getSQL(String sql) {
		return sql;
	}

	@Override
	public PoolManResultSetMetaData getPoolManResultSetMetaData(com.frameworkset.orm.adapter.DB db,String dbname,
			String sqlkey, ResultSetMetaData rsmetadata) throws SQLException {
		// TODO Auto-generated method stub
		return super.getPoolManResultSetMetaData(db,dbname, sqlkey, rsmetadata);
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
	protected void _destroy(){
		this.tplEdenConcurrentCache.clear();
//		this.edenConcurrentCache.clear();
	}
	protected void reinit(){
		this.tplEdenConcurrentCache.clear();
//		this.edenConcurrentCache.clear();
	}

	@Override
	public Map<String, SQLRef> getSQLRefers() {
		return null;
	}

	@Override
	public boolean hasrefs() {
		return false;
	}

	@Override
	public String getPlainSQL(String dbname, String sqlname) {
		return sqlname;
	}

	@Override
	public String getSQL(String sqlname, Map variablevalues) {
		return _getSQL(this.getSQLInfo(sqlname,true,false),variablevalues);
	}

	@Override
	public boolean fromConfig() {
		return false;
	}
}
