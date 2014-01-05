/*****************************************************************************
 *                                                                           *
 *  This file is part of the tna framework distribution.                     *
 *  Documentation and updates may be get from  biaoping.yin the author of    *
 *  this framework							     							 *
 *                                                                           *
 *  Sun Public License Notice:                                               *
 *                                                                           *
 *  The contents of this file are subject to the Sun Public License Version  *
 *  1.0 (the "License"); you may not use this file except in compliance with *
 *  the License. A copy of the License is available at http://www.sun.com    *
 *                                                                           *
 *  The Original Code is tag. The Initial Developer of the Original          *
 *  Code is biaoping yin. Portions created by biaoping yin are Copyright     *
 *  (C) 2000.  All Rights Reserved.                                          *
 *                                                                           *
 *  GNU Public License Notice:                                               *
 *                                                                           *
 *  Alternatively, the contents of this file may be used under the terms of  *
 *  the GNU Lesser General Public License (the "LGPL"), in which case the    *
 *  provisions of LGPL are applicable instead of those above. If you wish to *
 *  allow use of your version of this file only under the  terms of the LGPL *
 *  and not to allow others to use your version of this file under the SPL,  *
 *  indicate your decision by deleting the provisions above and replace      *
 *  them with the notice and other provisions required by the LGPL.  If you  *
 *  do not delete the provisions above, a recipient may use your version of  *
 *  this file under either the SPL or the LGPL.                              *
 *                                                                           *
 *  biaoping.yin (yin-bp@163.com)                                            *
 *                                                                           *
 *****************************************************************************/
package com.frameworkset.common.poolman.sql;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.frameworkset.common.poolman.management.BaseTableManager;

/**
 * 缓冲数据库的主键信息
 * 
 * @author biaoping.yin created on 2005-3-29 version 1.0
 */
public class PrimaryKeyCache {
	private static Logger log = Logger.getLogger(PrimaryKeyCache.class);

	// 数据库链接池名称
	private String dbname;

	// private static PrimaryKeyCache primaryKeyCache;
	private Map id_tables;
	
	/**
	 * 没有在tableinfo中存放主键的信息的表的主键信息用NULL_来替换
	 */
	private static final PrimaryKey NULL_ = new PrimaryKey();

	public PrimaryKeyCache(String dbname) {
		this.dbname = dbname;
		id_tables = new java.util.concurrent.ConcurrentHashMap(new HashMap());
	}

	// public static PrimaryKeyCache getInstance()
	// {
	// if(primaryKeyCache == null)
	// primaryKeyCache = new PrimaryKeyCache();
	// return primaryKeyCache;
	// }

	public void addIDTable(PrimaryKey primaryKey) {
		if (!id_tables.containsKey(primaryKey.getTableName()))
			id_tables.put(primaryKey.getTableName(), primaryKey);
	}
	
	public PrimaryKey getIDTable(String tableName) {
		return getIDTable(null,tableName);
		
		
		
	}
	
	public PrimaryKey getIDTable(Connection con,String tableName) {
		PrimaryKey key = (PrimaryKey) id_tables.get(tableName.toLowerCase());
		if (key != null)
		{
			if(key == NULL_)
				return null;
			return key;
		}
		else
		{
			key = loaderPrimaryKey(con,tableName);
			return key;
		}
		
		
		
	}

	/**
	 * @return Returns the dbname.
	 */
	public String getDbname() {
		return dbname;
	}
	
	/**
	 * 动态增加表的主键信息到系统缓冲中
	 * @param tableName
	 * @return
	 */
	public PrimaryKey loaderPrimaryKey(String tableName) {
		return loaderPrimaryKey(null,tableName);
	}
	
	/**
	 * 动态增加表的主键信息到系统缓冲中
	 * @param tableName
	 * @return
	 */
	public PrimaryKey loaderPrimaryKey(Connection con,String tableName) {
		try {
			
			log.debug("开始装载表【" + tableName +"】的主键信息到缓冲。");
//			PrimaryKey key = this.getIDTable(tableName);
//			if(key != null)
//			{
//				System.out.println("表【" + tableName +"】的主键信息已经存在，无需装载！");
//				return key;
//			}
			PrimaryKey key = BaseTableManager.getPoolTableInfo(dbname,con,
					tableName);
			if (key != null)
			{
				id_tables.put(key.getTableName().trim().toLowerCase(), key);
				log.debug("完成装载表【" + tableName +"】的主键信息。");
			}
			else
			{
				id_tables.put(tableName.trim().toLowerCase(),NULL_);
				log.debug("完成装载表【" + tableName +"】的主键信息,NULL_,");
			}
			
			return key;
		} catch (Exception ex) {
//			ex.printStackTrace();
			log.error(ex.getMessage(),ex);
		}
		return null;
	}

	public void destroy() {
		if(id_tables != null)
		{
			id_tables.clear();
			id_tables = null;
		}
		
	}

}
