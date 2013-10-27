/**
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

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.frameworkset.spi.BaseSPIManager;

import com.frameworkset.common.poolman.util.SQLManager;

/**
 * <p>SQL.java</p>
 * <p> Description: 获取sql语句，对应不同的数据库库类型的sql语句，尤其是insert语句</p>
 * <p> bboss workgroup </p>
 * <p> Copyright (c) 2009 </p>
 * 
 * @Date Jun 28, 2009
 * @author biaoping.yin
 * @version 1.0
 */
public class SQL {
	public static String getSQL(String dbname,String sqlname)
	{
		String sql = BaseSPIManager.getProperty(sqlname);
		if(sql == null)
		{
			String dbtype = SQLManager.getInstance().getDBAdapter(dbname).getDBTYPE();
			sql = BaseSPIManager.getProperty(sqlname + "-" + dbtype.toLowerCase());
		}
		return sql;
		
	}
	
	public static String getSQL(String sqlname)
	{
		return getSQL(null,sqlname);
		
	}
	
	public static Map getMapSQLs(String sqlname)
        {
                return getMapSQLs(null,sqlname);
                
        }
	public static Map getMapSQLs(String dbname,String sqlname)
        {
                Map sqls = BaseSPIManager.getMapProperty(sqlname);
                if(sqls == null)
                {
                        String dbtype = SQLManager.getInstance().getDBAdapter(dbname).getDBTYPE();
                        sqls = BaseSPIManager.getMapProperty(sqlname + "-" + dbtype.toLowerCase());
                }
                return sqls;
                
        }
	
	public static List getListSQLs(String sqlname)
        {
                return getListSQLs(null,sqlname);
                
        }
        public static List getListSQLs(String dbname,String sqlname)
        {
            List sqls = BaseSPIManager.getListProperty(sqlname);
                if(sqls == null)
                {
                        String dbtype = SQLManager.getInstance().getDBAdapter(dbname).getDBTYPE();
                        sqls = BaseSPIManager.getListProperty(sqlname + "-" + dbtype.toLowerCase());
                }
                return sqls;
                
        }
        
        public static Set getSetSQLs(String sqlname)
        {
                return getSetSQLs(null,sqlname);
                
        }
        public static Set getSetSQLs(String dbname,String sqlname)
        {
            Set sqls = BaseSPIManager.getSetProperty(sqlname);
                if(sqls == null)
                {
                        String dbtype = SQLManager.getInstance().getDBAdapter(dbname).getDBTYPE();
                        sqls = BaseSPIManager.getSetProperty(sqlname + "-" + dbtype.toLowerCase());
                }
                return sqls;
                
        }

}
