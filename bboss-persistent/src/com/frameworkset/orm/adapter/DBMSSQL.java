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
package com.frameworkset.orm.adapter;

import java.sql.ResultSet;

import com.frameworkset.common.poolman.util.JDBCPoolMetaData;
import com.frameworkset.orm.platform.PlatformMssqlImpl;


/**
 * This is used to connect to a MSSQL database.  For now, this class
 * simply extends the adaptor for Sybase.  You can use one of several
 * commercial JDBC drivers; the one I use is:
 *
 *   i-net SPRINTA(tm) 2000 Driver Version 3.03 for MS SQL Server
 *   http://www.inetsoftware.de/
 *
 * @author <a href="mailto:gonzalo.diethelm@sonda.com">Gonzalo Diethelm</a>
 * @version $Id: DBMSSQL.java,v 1.7 2005/01/31 19:43:55 tfischer Exp $
 */
public class DBMSSQL extends DBSybase
{
    /**
     * Empty constructor.
     */
    protected DBMSSQL()
    {
    	this.platform = new PlatformMssqlImpl();
    }

    /**
     * This method is used to chek whether the database natively
     * supports limiting the size of the resultset.
     *
     * @return True.
     */
    public boolean supportsNativeLimit()
    {
        return false;
    }
    
    
 
    /**
     * 数据库主键最大值的获取方法
     */
    public String getIDMAXSql(String table_name,String table_id_name,String table_id_prefix,String type)
	{
    	//SUBSTR(table_id_name,LENGTH(table_id_prefix))
    	String maxSql = "select max("+ table_id_name + ") from " + table_name;
    	if(type.equalsIgnoreCase("string") || type.equalsIgnoreCase("java.lang.string"))
    	{
    		if(table_id_prefix != null && !table_id_prefix.trim().equals(""))
    		{
    			maxSql = "select max(CAST(SUBSTRING(" + table_id_name + ",len(" + table_id_prefix + ") + 1) as bigint))) from " + table_name;
    		}
    		else
    		{
    			maxSql = "select max(CAST(" + table_id_name + " as bigint)) from " + table_name;
    		}
    	}
		
		return maxSql;
	}
    
    public int getCusorType(String dbdriver)
    {
        if(dbdriver == null || dbdriver.equals("com.microsoft.jdbc.sqlserver.SQLServerDriver"))
            return super.getCusorType(dbdriver);
        else
            return ResultSet.CONCUR_UPDATABLE;
    }
    
//    public int getSCROLLType(String dbdriver)
//    {
//    	
//        if(dbdriver == null || dbdriver.equals("com.microsoft.jdbc.sqlserver.SQLServerDriver"))
//            return super.getSCROLLType(dbdriver);
//        else
//            return ResultSet.;
//    }
    
    public String getSchema(JDBCPoolMetaData info)
	{
		return DB.NULL_SCHEMA;
	}
    
    

}
