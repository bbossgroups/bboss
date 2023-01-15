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

import com.frameworkset.common.poolman.util.SQLManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.util.*;
import java.util.Map.Entry;

/**
 * 管理全部数据库链接池的所有主键信息
 * @author biaoping.yin
 * created on 2005-3-30
 * version 1.0
 */
public class PrimaryKeyCacheManager {
    private static Logger log = LoggerFactory.getLogger(PrimaryKeyCacheManager.class);
    private Map<String,PrimaryKeyCache> primaryKeyCaches;
    public static void  destroy( Map<String,Integer> destroies )
    {
    	if(self != null)
    	{
    		self._destroy( destroies );
    		self = null;
    	}
    }
    void _destroy(Map<String,Integer> destroies )
    {
    	if(primaryKeyCaches != null)
    	{
    		Iterator<Entry<String, PrimaryKeyCache>> it = primaryKeyCaches.entrySet().iterator();
			List<String> closed = new ArrayList<>();
    		while(it.hasNext())
    		{
    			Entry<String, PrimaryKeyCache> entry = it.next();
    			if(destroies.containsKey(entry.getKey())) {
					entry.getValue().destroy();
					closed.add(entry.getKey());
				}
    		}
    		for(String name: closed) {
				primaryKeyCaches.remove(name);
			}
//    		primaryKeyCaches = null;
    	}
    }
    
    public static void  reset()
    {
    	if(self != null)
    	{
    		self._reset();
    		 
    	}
    }
    void _reset()
    {
    	if(primaryKeyCaches != null)
    	{
    		Iterator<Entry<String, PrimaryKeyCache>> it = primaryKeyCaches.entrySet().iterator();
    		while(it.hasNext())
    		{
    			Entry<String, PrimaryKeyCache> entry = it.next();
    			entry.getValue().reset();
    		}
//    		primaryKeyCaches.clear();
//    		primaryKeyCaches = null;
    	}
    }
    private static PrimaryKeyCacheManager self;

    private PrimaryKeyCacheManager()
    {
        primaryKeyCaches = Collections.synchronizedMap(new HashMap());
    }
    
    public static PrimaryKeyCacheManager getInstance()
    {
        if(self == null)
            self = new PrimaryKeyCacheManager();
        return self;
    }

    public void addPrimaryKeyCache(PrimaryKeyCache primaryKeyCache)
    {
        primaryKeyCaches.put(primaryKeyCache.getDbname(),primaryKeyCache);
    }
    
    public boolean removePrimaryKeyCache(String dbname)
    {
        if(primaryKeyCaches.remove(dbname)!= null)
        	return true;
        return false;
    }


    public PrimaryKeyCache getPrimaryKeyCache(String dbname)
    {
    	if(SQLManager.getInstance().getPool(dbname) == null)
    		return null;
    	String _dbname = SQLManager.getRealDBNameFromExternalDBNameIfExist(dbname);
       PrimaryKeyCache keyCache =  (PrimaryKeyCache)primaryKeyCaches.get(_dbname);

       if(keyCache != null)
           return keyCache;
      /** if(keyCache == null)
        try {
            keyCache = BaseTableManager.getPoolTableInfos(dbname);
            if(keyCache != null)
            {
                addPrimaryKeyCache(keyCache);
            }
        } catch (Exception ex) {
            log.error("",ex);
        }
       return  keyCache;*/
      return null;
    }
    
    
    public PrimaryKey loaderPrimaryKey(String dbname,String tableName)
    {
    	return loaderPrimaryKey(null, dbname, tableName);
    }
    
    /**
     * 从数据库中加载表的主键信息
     * @param con
     * @param dbname
     * @param tableName
     * @return
     */
    public PrimaryKey loaderPrimaryKey(Connection con,String dbname,String tableName)
    {
    	return getPrimaryKeyCache(dbname).loaderPrimaryKey(con,tableName);
    }


}
