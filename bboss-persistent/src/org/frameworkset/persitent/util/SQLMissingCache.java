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
import org.frameworkset.cache.MissingStaticCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>Title: SQLCache.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2007</p>
 * @Date 2012-12-5 下午6:15:07
 * @author biaoping.yin
 * @version 1.0
 */
public class SQLMissingCache extends SQLBaseCache{
	private static final Logger logger = LoggerFactory.getLogger(SQLMissingCache.class);
	private Map<String, MissingStaticCache<String, SQLStruction>> parserTPLSQLStructions ;
	private Map<String,MissingStaticCache<String, SQLStruction>> parserTPLTotalsizeSQLStructions ;

	protected  Map<String, MissingStaticCache<String, PoolManResultSetMetaData>> missingCacheMetas ;
	public SQLMissingCache(String sqlfile, int resultMetaCacheSize, int perKeySqlStructionCacheSize) {
		super(sqlfile,resultMetaCacheSize,perKeySqlStructionCacheSize);
		parserTPLSQLStructions = new HashMap<String, MissingStaticCache<String, SQLStruction>>();
		parserTPLTotalsizeSQLStructions = new HashMap<String, MissingStaticCache<String, SQLStruction>>();
		missingCacheMetas = new HashMap<String,MissingStaticCache<String, PoolManResultSetMetaData>>( );

	}


	public void clear()
	{
		missingCacheMetas.clear();
		parserTPLSQLStructions.clear();
		parserTPLTotalsizeSQLStructions.clear();
		super.clear();

	}


	public PoolManResultSetMetaData getPoolManResultSetMetaData(boolean cacheSql,com.frameworkset.orm.adapter.DB db,String dbname,String sqlkey,ResultSetMetaData rsmetadata) throws SQLException
	{
		PoolManResultSetMetaData meta = null;
		if(cacheSql) {
			MissingStaticCache<String, PoolManResultSetMetaData> dbmetas = missingCacheMetas.get(dbname);
			if (dbmetas == null) {
				synchronized (missingCacheMetas) {
					dbmetas = missingCacheMetas.get(dbname);
					if (dbmetas == null) {
						dbmetas = new MissingStaticCache<String, PoolManResultSetMetaData>(resultMetaCacheSize);
						missingCacheMetas.put(dbname, dbmetas);
					}
				}
			}
			long missing = 0l;
			if (dbmetas.stopCache()) {
				missing = dbmetas.increamentMissing();
				meta = PoolManResultSetMetaData.getCopy(db, rsmetadata);
				if (logger.isWarnEnabled() && dbmetas.needLogWarn(missing,warnInterval)) {
					logMetaWarn(logger, sqlkey, dbmetas.getMissesMax());
				}
			} else {
				boolean newMeta = false;
				meta = dbmetas.get(sqlkey);
				if (meta == null) {
					try {
						metaLock.lock();
						meta = dbmetas.get(sqlkey);
						if (meta == null) {
							missing = dbmetas.increamentMissing();
							newMeta = true;
							meta = PoolManResultSetMetaData.getCopy(db, rsmetadata);
							if (!dbmetas.stopCache()) {
								dbmetas.put(sqlkey, meta);
							}
						}
					} finally {
						metaLock.unlock();
					}
				}
				//检测从缓冲中获取的数据是否发生变化
				if (!newMeta && needRefreshMeta(meta, rsmetadata)) {
					meta = PoolManResultSetMetaData.getCopy(db, rsmetadata);
					dbmetas.put(sqlkey, meta);

				}
				if (dbmetas.stopCache() && logger.isWarnEnabled() && dbmetas.needLogWarn(missing,warnInterval)) {
					logMetaWarn(logger, sqlkey, dbmetas.getMissesMax());
				}
			}
		}
		else
		{
			meta = PoolManResultSetMetaData.getCopy(db, rsmetadata);
		}
		return meta;

	}


	/**
	 * vtpl需要进行分级缓存
	 * @param sqlinfo
	 * @param newsql
	 * @return
	 */
	protected SQLStruction _getVTPLSQLStruction(boolean isTotalSize,
															  SQLInfo sqlinfo, String newsql, String okey,int cacheSize)
	{
		if(sqlinfo.isCacheSql()) {
			Map<String, MissingStaticCache<String, SQLStruction>> _parserTPLSQLStructions = !isTotalSize ? parserTPLSQLStructions : parserTPLTotalsizeSQLStructions;
			String ikey = newsql;
			MissingStaticCache<String, SQLStruction> sqlstructionMap = _parserTPLSQLStructions.get(okey);
			if (sqlstructionMap == null) {
				try {
					this.vtplLock.lock();
					sqlstructionMap = _parserTPLSQLStructions.get(okey);
					if (sqlstructionMap == null) {
						sqlstructionMap = new MissingStaticCache<String, SQLStruction>(cacheSize);
						_parserTPLSQLStructions.put(okey, sqlstructionMap);
					}
				} finally {
					vtplLock.unlock();
				}
			}
			long missing = 0l;
			if (sqlstructionMap.stopCache()) {
				missing = sqlstructionMap.increamentMissing();
				if (logger.isWarnEnabled() && sqlstructionMap.needLogWarn(missing,warnInterval)) {
					this.logSqlStructionWarn(logger, ikey, sqlstructionMap.getMissesMax(), okey);
				}
				return VariableHandler.parserSQLStruction(newsql);
			} else {
				SQLStruction urlStruction = sqlstructionMap.get(ikey);
				if (urlStruction == null) {
					try {
						this.vtplLock.lock();
						urlStruction = sqlstructionMap.get(ikey);
						if (urlStruction == null) {
							missing = sqlstructionMap.increamentMissing();
							urlStruction = VariableHandler.parserSQLStruction(newsql);
							if (!sqlstructionMap.stopCache()) {
								sqlstructionMap.put(ikey, urlStruction);
							}
						}
					} finally {
						this.vtplLock.unlock();
					}
					if (sqlstructionMap.stopCache() && logger.isWarnEnabled() && sqlstructionMap.needLogWarn(missing,warnInterval)) {
						this.logSqlStructionWarn(logger, ikey, sqlstructionMap.getMissesMax(), okey);
					}
				}
				return urlStruction;
			}
		}
		else{
			return  VariableHandler.parserSQLStruction(newsql);
		}

	}

	


}
