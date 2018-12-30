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
import com.frameworkset.util.VariableHandler;
import com.frameworkset.util.VariableHandler.SQLStruction;
import org.frameworkset.cache.EdenConcurrentCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.ref.SoftReference;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * <p>Title: SQLCache.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2007</p>
 * @Date 2012-12-5 下午6:15:07
 * @author biaoping.yin
 * @version 1.0
 */
public class SQLCache {
	private static final Logger logger = LoggerFactory.getLogger(SQLCache.class);
	private Lock lock = new ReentrantLock();
	private Lock vtplLock = new ReentrantLock();
	private Map<String,SQLStruction> parserSQLStructions = new java.util.HashMap<String,SQLStruction>();
	private Map<String,SQLStruction> parsertotalsizeSQLStructions = new java.util.HashMap<String,SQLStruction>();
	private Map<String,EdenConcurrentCache<String,VariableHandler.SQLStruction>> parserTPLSQLStructions = new java.util.HashMap<String,EdenConcurrentCache<String,VariableHandler.SQLStruction>>();
	private Map<String,EdenConcurrentCache<String,VariableHandler.SQLStruction>> parserTPLTotalsizeSQLStructions = new java.util.HashMap<String,EdenConcurrentCache<String,VariableHandler.SQLStruction>>();
	private int resultMetaCacheSize;
	private int perKeySqlStructionCacheSize;
	private String sqlfile;
	protected final Map<String, EdenConcurrentCache<String, SoftReference<PoolManResultSetMetaData>>> metas ;
	public SQLCache(String sqlfile,int resultMetaCacheSize,int perKeySqlStructionCacheSize) {
		this.resultMetaCacheSize = resultMetaCacheSize;
		this.perKeySqlStructionCacheSize = perKeySqlStructionCacheSize;
		this.sqlfile = sqlfile;
		metas = new HashMap<String,EdenConcurrentCache<String, SoftReference<PoolManResultSetMetaData>>>( );
	}
	
	
	public void clear()
	{
		metas.clear();
		parserSQLStructions.clear();
		parsertotalsizeSQLStructions.clear();
		parserTPLSQLStructions.clear();
		parserTPLTotalsizeSQLStructions.clear();

	}
	
	private boolean needRefreshMeta(PoolManResultSetMetaData meta,ResultSetMetaData rsmetadata) throws SQLException
	{
		if(meta.getColumnCount() != rsmetadata.getColumnCount())//列数发生变化
			return true;
		String[] labels = meta.get_columnLabel();//判断列名称是否变化
		int coltypes[] = meta.get_columnType();
		for(int i = 0; i < labels.length;i ++)
		{
			if(!labels[i].equals(rsmetadata.getColumnLabel(i + 1)))//列名变化
			{
				return true;
			}
			if(coltypes[i] != rsmetadata.getColumnType(i + 1))//类型变化
				return true;
		}
		return false;
	}
	public PoolManResultSetMetaData getPoolManResultSetMetaData(com.frameworkset.orm.adapter.DB db,String dbname,String sqlkey,ResultSetMetaData rsmetadata) throws SQLException
	{
		PoolManResultSetMetaData meta = null;
		EdenConcurrentCache<String, SoftReference<PoolManResultSetMetaData>> dbmetas = metas.get(dbname);
		if(dbmetas == null)
		{
			synchronized(metas)
			{
				dbmetas = metas.get(dbname);
				if(dbmetas == null)
				{
					dbmetas = new EdenConcurrentCache<String, SoftReference<PoolManResultSetMetaData>>(resultMetaCacheSize);
					metas.put(dbname, dbmetas);
				}
			}
		}

		SoftReference<PoolManResultSetMetaData> wr =  dbmetas.get(sqlkey);
		boolean newMeta = false;
		boolean outOfSize = false;
		if (wr != null) {
			meta = (PoolManResultSetMetaData) wr.get();
			if(meta == null){
				synchronized (dbmetas) {
					wr =  dbmetas.get(sqlkey);
					if(wr != null){
						meta = wr.get();
					}
					if(meta == null) {
						newMeta = true;
						meta = PoolManResultSetMetaData.getCopy(db, rsmetadata);
						SoftReference<PoolManResultSetMetaData> wr1 = new SoftReference<PoolManResultSetMetaData>(meta);
						outOfSize = dbmetas.put(sqlkey, wr1);

					}
				}
			}

		}
		else{
			synchronized (dbmetas) {
				wr =  dbmetas.get(sqlkey);
				if(wr != null){
					meta = wr.get();
				}
				if(meta == null) {
					newMeta = true;
					meta = PoolManResultSetMetaData.getCopy(db, rsmetadata);
					SoftReference<PoolManResultSetMetaData> wr1 = new SoftReference<PoolManResultSetMetaData>(meta);
					outOfSize = dbmetas.put(sqlkey, wr1);
				}
			}
		}
		//检测从缓冲中获取的数据是否发生变化
		if(!newMeta && needRefreshMeta(meta,rsmetadata))
		{
			meta = PoolManResultSetMetaData.getCopy(db, rsmetadata);
			wr = new SoftReference<PoolManResultSetMetaData>(meta);
			outOfSize = dbmetas.put(sqlkey, wr);

		}
		if(outOfSize && logger.isWarnEnabled()) {
			logMetaWarn(sqlkey, dbmetas);
		}
		return meta;
	}

	private void logMetaWarn(String sql,EdenConcurrentCache dbmetas ){

		StringBuilder info = new StringBuilder();
		if(sqlfile != null) {
			info.append("\n\r**********************************************************************\r\n")
					.append("*********************************警告:sql file[").append(this.sqlfile).append("]*********************************\r\n")
					.append("**********************************************************************\r\n")
					.append("调用方法getPoolManResultSetMetaData从sqlmetacache 中获取sql[")
					.append(sql).append("]查询元数据信息时，检测到缓冲区信息记录数超出meta cache区允许的最大cache size:")
					.append(dbmetas.getMaxSize())
					.append(",\r\n导致告警原因分析:")
					.append("\r\n本条sql或者本配置文件中其他sql语句可能存在不断变化的值参数;")
					.append("\r\n本条sql或者本配置文件中其他sql语句可能存在的$var模式的变量并且$var的值不断变化;")
					.append("\r\n优化建议：\r\n将sql中可能存在不断变化的值参数转化为绑定变量或者#[variable]变量，或将sql中可能存在的$var模式的变量转换为#[varibale]模式的变量，以提升系统性能!")
					.append("\n\r**********************************************************************")
					.append("\n\r**********************************************************************");
		}
		else{
			info.append("\n\r**********************************************************************\r\n")
					.append("*********************************警告:*********************************\r\n")
					.append("**********************************************************************\r\n")
					.append("调用方法getPoolManResultSetMetaData从sqlmetacache 中获取代码中硬编码sql[")
					.append(sql).append("]查询元数据信息时，检测到缓冲区信息记录数超出meta cache区允许的最大cache size:")
					.append(dbmetas.getMaxSize())
					.append("\r\n导致告警原因分析:\r\n本条sql或者其他sql语句直接硬编码在代码中;")
					.append("\r\n本条sql或者本文件中其他sql语句可能存在不断变化的值参数;")
					.append("\r\n本条sql或者本文件中其他sql语句可能存在的$var模式的变量并且$var的值不断变化;")
					.append("\r\n优化建议：\r\n将sql中可能存在不断变化的值参数转化为绑定变量或者#[variable]变量，或将sql中可能存在的$var模式的变量转换为#[varibale]模式的变量，并采用配置文件来管理sql语句，以提升系统性能!")
					.append("\n\r**********************************************************************")
					.append("\n\r**********************************************************************");
		}
		logger.warn(info.toString());

	}

	private void logSqlStructionWarn(String sql,EdenConcurrentCache dbmetas ,String okey){
		StringBuilder info = new StringBuilder();
		if(sqlfile != null) {
			info.append("\n\r**********************************************************************\r\n")
					.append("*********************************警告:sql ").append(okey).append("@file[").append(this.sqlfile).append("]*********************************\r\n")
					.append("**********************************************************************\r\n")
					.append("调用方法_getVTPLSQLStruction从sql struction cache获取[")
					.append(sql).append("]sql struction 信息时,检测到缓冲区信息记录数超出SqlStructionCache允许的最大cache size:")
					.append(dbmetas.getMaxSize())
					.append(",\r\n导致告警原因分析:")
					.append("\r\n本条sql可能存在不断变化的值参数;")
					.append("\r\n本条sql可能存在的$var模式的变量并且$var的值不断变化;")
					.append("\r\n优化建议：\r\n将sql中可能存在不断变化的值参数转化为绑定变量或者#[variable]变量，或将sql中可能存在的$var模式的变量转换为#[varibale]模式的变量，以提升系统性能!")
					.append("\n\r**********************************************************************")
					.append("\n\r**********************************************************************");
		}
		else{
			info.append("\n\r**********************************************************************\r\n")
					.append("*********************************警告*********************************\r\n")
					.append("**********************************************************************\r\n")
					.append("调用方法_getVTPLSQLStruction从sql struction cache获取代码中硬编码[")
					.append(sql).append("]sql struction 信息时,检测到缓冲区信息记录数超出SqlStructionCache允许的最大cache size:")
					.append(dbmetas.getMaxSize())
					.append(",\r\n导致告警原因分析:\r\n本条sql或者其他sql语句直接硬编码在代码中;")
					.append("\r\n本条sql或者其他sql语句可能存在不断变化的值参数;")
					.append("\r\n本条sql或者其他sql语句可能存在的$var模式的变量并且$var的值不断变化;")
					.append("\r\n优化建议：\r\n将sql中可能存在不断变化的值参数转化为绑定变量或者#[variable]变量，或将sql中可能存在的$var模式的变量转换为#[varibale]模式的变量，并采用配置文件来管理sql语句，以提升系统性能!")
					.append("\n\r**********************************************************************")
					.append("\n\r**********************************************************************");
		}
		logger.warn(info.toString());

	}
	
	public SQLStruction getSQLStruction(SQLInfo sqlinfo,String newsql)
	{

		if(sqlinfo.getSqlutil() == null 
				|| sqlinfo.getSqlutil() == SQLUtil.getGlobalSQLUtil()) {
			return this._getVTPLSQLStruction(parserTPLSQLStructions,sqlinfo,newsql,"___GlobalSQLUtil_",this.resultMetaCacheSize);
		}
		else
		{
			if(sqlinfo.istpl() )
			{
				return this._getVTPLSQLStruction(parserTPLSQLStructions,sqlinfo,newsql,sqlinfo.getSqlname(),perKeySqlStructionCacheSize);
			}
			else
			{
				return _getSQLStruction(parserSQLStructions,sqlinfo, newsql);
			}
		}

	}

	private VariableHandler.SQLStruction _getSQLStruction(Map<String,SQLStruction> parserSQLStructions ,SQLInfo sqlinfo, String newsql)
	{

		String key = sqlinfo.getSqlname();
		VariableHandler.SQLStruction sqlstruction =  parserSQLStructions.get(key);
		if(sqlstruction == null)
		{
			try
			{
				lock.lock();
				sqlstruction =  parserSQLStructions.get(key);
				if(sqlstruction == null)
				{
					sqlstruction = VariableHandler.parserSQLStruction(newsql);
					parserSQLStructions.put(key,sqlstruction);
				}
			}
			finally {
				lock.unlock();
			}
		}
		return sqlstruction;
	}

	/**
	 * vtpl需要进行分级缓存
	 * @param sqlinfo
	 * @param newsql
	 * @return
	 */
	private VariableHandler.SQLStruction _getVTPLSQLStruction(Map<String, EdenConcurrentCache<String, SQLStruction>> parserTPLSQLStructions,
															  SQLInfo sqlinfo, String newsql, String okey,int cacheSize)
	{

		String ikey = newsql;
		EdenConcurrentCache<String,VariableHandler.SQLStruction> sqlstructionMap =  parserTPLSQLStructions.get(okey);
		if(sqlstructionMap == null)
		{
			try
			{
				this.vtplLock.lock();
				sqlstructionMap =  parserTPLSQLStructions.get(okey);
				if(sqlstructionMap == null)
				{
					sqlstructionMap = new   EdenConcurrentCache<String,VariableHandler.SQLStruction>(cacheSize);
					parserTPLSQLStructions.put(okey,sqlstructionMap);
				}
			}
			finally {
				vtplLock.unlock();
			}
		}
//		if(sqlstructionMap.stopCache()){
//			return VariableHandler.parserSQLStruction(newsql);
//		}
		VariableHandler.SQLStruction urlStruction = sqlstructionMap.get(ikey);
		boolean outOfSize = false;
		if(urlStruction == null){
			try
			{
				this.vtplLock.lock();
				urlStruction = sqlstructionMap.get(ikey);
				if(urlStruction == null){
//					sqlstructionMap.increamentMissing();
					urlStruction = VariableHandler.parserSQLStruction(newsql);
//					if(!sqlstructionMap.stopCache()){
					outOfSize = sqlstructionMap.put(ikey,urlStruction);
//					}
				}
			}
			finally {
				this.vtplLock.unlock();
			}
			if(outOfSize && logger.isWarnEnabled()) {
				this.logSqlStructionWarn( ikey, sqlstructionMap,okey);
			}
		}
		return urlStruction;
	}
	
	public SQLStruction getTotalsizeSQLStruction(SQLInfo totalsizesqlinfo,String newtotalsizesql)
	{

		if(totalsizesqlinfo.getSqlutil() == null 
				|| totalsizesqlinfo.getSqlutil() == SQLUtil.getGlobalSQLUtil()) {
			return this._getVTPLSQLStruction(this.parserTPLTotalsizeSQLStructions, totalsizesqlinfo, newtotalsizesql, "___GlobalSQLUtil_",this.resultMetaCacheSize);
		}
		else
		{
			if(totalsizesqlinfo.istpl() )
			{
				return this._getVTPLSQLStruction(this.parserTPLTotalsizeSQLStructions,totalsizesqlinfo,newtotalsizesql,totalsizesqlinfo.getSqlname(),perKeySqlStructionCacheSize);
			}
			else
			{
				return _getSQLStruction(parsertotalsizeSQLStructions,totalsizesqlinfo, newtotalsizesql);
			}
		}

	}

}
