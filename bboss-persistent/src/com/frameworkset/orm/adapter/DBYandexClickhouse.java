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

/*
 
 *
 * Licensed under the Apache License, Version 2.0 (the "License")
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import com.frameworkset.common.poolman.PreparedDBUtil;
import org.frameworkset.persitent.datasource.BalanceDatasourceConfig;
import org.frameworkset.persitent.datasource.BalanceDatasourceUtil;

import java.sql.*;

/**
 * This is used in order to connect to a MySQL database using the MM
 * drivers.  Simply comment the above and uncomment this code below and
 * fill in the appropriate values for DB_NAME, DB_HOST, DB_USER,
 * DB_PASS.
 *
 * <P><A HREF="http://www.worldserver.com/mm.mysql/">
 * http://www.worldserver.com/mm.mysql/</A>
 * <p>"jdbc:mysql://" + DB_HOST + "/" + DB_NAME + "?user=" +
 * DB_USER + "&password=" + DB_PASS;
 *
 * @author <a href="mailto:jon@clearink.com">Jon S. Stevens</a>
 * @author <a href="mailto:bmclaugh@algx.net">Brett McLaughlin</a>
 * @author <a href="mailto:dlr@finemaltcoding.com">Daniel Rall</a>
 * @version $Id: DBMM.java,v 1.13 2005/01/31 19:43:55 tfischer Exp $
 */
public class DBYandexClickhouse extends DBMM
{

//    /** A specialized date format for MySQL. */
//    public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
	 
    /**
     * Empty protected constructor.
     */
    protected DBYandexClickhouse()
    {
    	super();
    }

    public static final String[] JDBC_CLICKHOUSE_PREFIXS = {"jdbc:clickhouse://","jdbc:clickhouse:http://","jdbc:clickhouse:https://","jdbc:ch:http://","jdbc:ch:https://"};


    /**
     * jdbc:clickhouse://101.13.6.4:29000,101.13.6.7:29000,101.13.6.6:29000/visualops
     * jdbc:clickhouse:http://192.168.137.1:28123,192.168.137.1:28125,192.168.137.1:28126/visualops?ssl=false&connect_timeout=60000&socket_timeout=60000&query_timeout=60000&keep_alive_timeout=60000&clickhouse.jdbc.v1=false
     * @param url
     * @return
     * @throws SQLException
     */
    @Override
    public BalanceDatasourceConfig getBalanceDatasourceConfig(String url) throws SQLException{
        return BalanceDatasourceUtil.getBalanceDatasourceConfig(JDBC_CLICKHOUSE_PREFIXS,url,false);
    }
    @Override
    public ResultSet getImportedKeys(DatabaseMetaData metaData, String catalog, String schemaName, String tableName) throws SQLException {
        return null;
    }
    
    @Override
    public void setAutoCommit(Connection con, boolean autoCommit) throws SQLException
    { 
        
    }
    @Override
    public void commit(Connection con) throws SQLException {
    }

    @Override
    public void rollback(Connection con) throws SQLException {
    }
    @Override
    public void rollback(Connection con,Savepoint savepoint) throws SQLException {
    }

}
