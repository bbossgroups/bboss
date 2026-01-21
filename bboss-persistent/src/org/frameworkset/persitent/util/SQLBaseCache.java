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
import com.frameworkset.common.poolman.util.JDBCPool;
import com.frameworkset.util.VariableHandler;
import com.frameworkset.util.VariableHandler.SQLStruction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
public abstract class SQLBaseCache {
	private static final Logger logger = LoggerFactory.getLogger(SQLBaseCache.class);
	protected Lock lock = new ReentrantLock();
	protected Lock metaLock = new ReentrantLock();
	protected Lock vtplLock = new ReentrantLock();
	protected Map<String,SQLStruction> parserSQLStructions = new HashMap<String,SQLStruction>();
	protected Map<String,SQLStruction> parsertotalsizeSQLStructions = new HashMap<String,SQLStruction>();
	protected long warnInterval = 500;
	protected int resultMetaCacheSize;
	protected int perKeySqlStructionCacheSize;
	protected String sqlfile;

	public SQLBaseCache(String sqlfile, int resultMetaCacheSize, int perKeySqlStructionCacheSize) {
		this.resultMetaCacheSize = resultMetaCacheSize;
		this.perKeySqlStructionCacheSize = perKeySqlStructionCacheSize;
		this.sqlfile = sqlfile;
	}


	public void clear()
	{
		parserSQLStructions.clear();
		parsertotalsizeSQLStructions.clear();
//		resultMetaCacheSize = 0;
//		perKeySqlStructionCacheSize = 0;

	}

	protected boolean needRefreshMeta(PoolManResultSetMetaData meta,ResultSetMetaData rsmetadata) throws SQLException
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
	public abstract PoolManResultSetMetaData getPoolManResultSetMetaData(boolean cacheSql, JDBCPool db, String dbname, String sqlkey, ResultSetMetaData rsmetadata) throws SQLException;

	protected void logMetaWarn(Logger logger,String sql,int maxSize,long missing ){

		StringBuilder info = new StringBuilder();
		if(sqlfile != null) {
			info.append("\n\r**********************************************************************\r\n")
					.append("*********************************警告:Missing cache ").append(missing).append(" times of sql file[").append(this.sqlfile).append("]*********************************\r\n")
					.append("**********************************************************************\r\n")
					.append("调用方法getPoolManResultSetMetaData从sqlmetacache 中获取sql[")
					.append(sql).append("]查询元数据信息时，检测到缓冲区信息记录数超出meta cache区允许的最大cache size:")
					.append(maxSize)
					.append(",\r\n导致告警原因分析:")
					.append("\r\n本条sql或者本配置文件中其他sql语句可能存在不断变化的值参数;")
					.append("\r\n本条sql或者本配置文件中其他sql语句可能存在的$var模式的变量并且$var的值不断变化;")
					.append("\r\n优化建议：\r\n将sql中可能存在不断变化的值参数转化为绑定变量或者#[variable]变量，或将sql中可能存在的$var模式的变量转换为#[varibale]模式的变量，以提升系统性能!")
					.append("\r\n或者通过在").append(this.sqlfile).append("中对应sql配置的property元素增加cacheSql=\"false\"属性来关闭sql语法缓存机制.")
					.append("\n\r**********************************************************************")
					.append("\n\r**********************************************************************");
		}
		else{
			info.append("\n\r**********************************************************************\r\n")
					.append("*********************************警告:Missing cache ").append(missing).append(" times *********************************\r\n")
					.append("**********************************************************************\r\n")
					.append("调用方法getPoolManResultSetMetaData从sqlmetacache 中获取代码中硬编码sql[")
					.append(sql).append("]查询元数据信息时，检测到缓冲区信息记录数超出meta cache区允许的最大cache size:")
					.append(maxSize)
					.append("\r\n导致告警原因分析:\r\n本条sql或者其他sql语句直接硬编码在代码中;")
					.append("\r\n本条sql或者本文件中其他sql语句可能存在不断变化的值参数;")
					.append("\r\n本条sql或者本文件中其他sql语句可能存在的$var模式的变量并且$var的值不断变化;")
					.append("\r\n优化建议：\r\n将sql中可能存在不断变化的值参数转化为绑定变量或者#[variable]变量，或将sql中可能存在的$var模式的变量转换为#[varibale]模式的变量，并采用配置文件来管理sql语句，以提升系统性能!")
					.append("\r\n优化建议：\r\n将sql中可能存在不断变化的值参数转化为绑定变量或者#[variable]变量，或将sql中可能存在的$var模式的变量转换为#[varibale]模式的变量，并采用配置文件来管理sql语句，以提升系统性能!")
					.append("\n\r**********************************************************************")
					.append("\n\r**********************************************************************");
		}
		logger.warn(info.toString());

	}

	protected void logSqlStructionWarn(Logger logger,String sql,int maxSize ,String okey,long missing){
		StringBuilder info = new StringBuilder();
		if(sqlfile != null) {
			info.append("\n\r**********************************************************************\r\n")
					.append("*********************************警告:Missing cache ").append(missing).append(" times of sql ").append(okey).append("@file[").append(this.sqlfile).append("]*********************************\r\n")
					.append("**********************************************************************\r\n")
					.append("调用方法_getVTPLSQLStruction从sql struction cache获取[")
					.append(sql).append("]sql struction 信息时,检测到缓冲区信息记录数超出SqlStructionCache允许的最大cache size:")
					.append(maxSize)
					.append(",\r\n导致告警原因分析:")
					.append("\r\n本条sql可能存在不断变化的值参数;")
					.append("\r\n本条sql可能存在的$var模式的变量并且$var的值不断变化;")
					.append("\r\n优化建议：\r\n将sql中可能存在不断变化的值参数转化为绑定变量或者#[variable]变量，或将sql中可能存在的$var模式的变量转换为#[varibale]模式的变量，以提升系统性能!")
					.append("\r\n或者通过在").append(okey).append("@file[").append(this.sqlfile).append("]的property元素增加cacheSql=\"false\"属性来关闭sql语法缓存机制.")
					.append("\n\r**********************************************************************")
					.append("\n\r**********************************************************************");
		}
		else{
			info.append("\n\r**********************************************************************\r\n")
					.append("*********************************警告 Missing cache ").append(missing).append(" times *********************************\r\n")
					.append("**********************************************************************\r\n")
					.append("调用方法_getVTPLSQLStruction从sql struction cache获取代码中硬编码[")
					.append(sql).append("]sql struction 信息时,检测到缓冲区信息记录数超出SqlStructionCache允许的最大cache size:")
					.append(maxSize)
					.append(",\r\n导致告警原因分析:\r\n本条sql或者其他sql语句直接硬编码在代码中;")
					.append("\r\n本条sql或者其他sql语句可能存在不断变化的值参数;")
					.append("\r\n本条sql或者其他sql语句可能存在的$var模式的变量并且$var的值不断变化;")
					.append("\r\n优化建议：\r\n将sql中可能存在不断变化的值参数转化为绑定变量或者#[variable]变量，或将sql中可能存在的$var模式的变量转换为#[varibale]模式的变量，并采用配置文件来管理sql语句，以提升系统性能!")
					.append("\n\r**********************************************************************")
					.append("\n\r**********************************************************************");
		}
		logger.warn(info.toString());

	}
	public final static PersistentSQLStructionBuilder persistentSQLStructionBuilder = new PersistentSQLStructionBuilder();

    public final static APISQLStructionBuilder apiSQLStructionBuilder = new APISQLStructionBuilder();
	public SQLStruction getSQLStruction(SQLInfo sqlinfo,String newsql)
	{

		if(sqlinfo.getSqlutil() == null
				|| sqlinfo.getSqlutil() == SQLUtil.getGlobalSQLUtil()) {
			return this._getVTPLSQLStruction(false,sqlinfo,newsql,"___GlobalSQLUtil_",this.resultMetaCacheSize);
		}
		else
		{
			if(sqlinfo.istpl() )
			{
				return this._getVTPLSQLStruction(false,sqlinfo,newsql,sqlinfo.getSqlname(),perKeySqlStructionCacheSize);
			}
			else
			{
				return _getSQLStruction(parserSQLStructions,sqlinfo, newsql);
			}
		}

	}

    /**
     * 模版sql解析语句
     * @param newsql
     * @return
     */
    
	public static SQLStruction evalSQLStruction(String newsql){
		return (SQLStruction) VariableHandler._parserStruction(newsql,persistentSQLStructionBuilder);
	}

    /**
     * 模版sql解析语句
     * @param newsql
     * @return
     */

    public static APISQLStruction evalAPISQLStruction(String newsql){
        return (APISQLStruction) VariableHandler._parserStruction(newsql,apiSQLStructionBuilder);
    }
	protected SQLStruction _getSQLStruction(Map<String,SQLStruction> parserSQLStructions ,SQLInfo sqlinfo, String newsql)
	{

		String key = sqlinfo.getSqlname();
		SQLStruction sqlstruction =  parserSQLStructions.get(key);
		if(sqlstruction == null)
		{
			lock.lock();
			try
			{

				sqlstruction =  parserSQLStructions.get(key);
				if(sqlstruction == null)
				{
					sqlstruction = evalSQLStruction(newsql);//(SQLStruction) VariableHandler._parserStruction(newsql,persistentSQLStructionBuilder);
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
	protected abstract SQLStruction _getVTPLSQLStruction(boolean isTotalSize,
															  SQLInfo sqlinfo, String newsql, String okey,int cacheSize);


	
	public SQLStruction getTotalsizeSQLStruction(SQLInfo totalsizesqlinfo,String newtotalsizesql)
	{

		if(totalsizesqlinfo.getSqlutil() == null 
				|| totalsizesqlinfo.getSqlutil() == SQLUtil.getGlobalSQLUtil()) {
			return this._getVTPLSQLStruction(true, totalsizesqlinfo, newtotalsizesql, "___GlobalSQLUtil_",this.resultMetaCacheSize);
		}
		else
		{
			if(totalsizesqlinfo.istpl() )
			{
				return this._getVTPLSQLStruction(true,totalsizesqlinfo,newtotalsizesql,totalsizesqlinfo.getSqlname(),perKeySqlStructionCacheSize);
			}
			else
			{
				return _getSQLStruction(parsertotalsizeSQLStructions,totalsizesqlinfo, newtotalsizesql);
			}
		}

	}

}
