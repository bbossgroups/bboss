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

import java.sql.Connection;
import java.sql.SQLException;

import com.frameworkset.orm.platform.PlatformHypersonicImpl;

/**
 * This is used to connect to Hypersonic SQL databases.
 *
 * <a href="http://hsql.oron.ch/">http://hsql.oron.ch</a>
 *
 * @author <a href="mailto:celkins@scardini.com">Christopher Elkins</a>
 * @version $Id: DBHypersonicSQL.java,v 1.11 2005/01/31 19:43:55 tfischer Exp $
 */
public class DBHypersonicSQL
    extends DB
{
    /**
     * Constructor.
     */
    protected DBHypersonicSQL()
    {
    	this.platform = new PlatformHypersonicImpl();
    }

    /**
     * This method is used to ignore case.
     *
     * @param in The string to transform to upper case.
     * @return The upper case string.
     */
    public String toUpperCase(String in)
    {
        String s = new StringBuffer("UPPER(").append(in).append(")").toString();
        return s;
    }

    /**
     * This method is used to ignore case.
     *
     * @param in The string whose case to ignore.
     * @return The string in a case that can be ignored.
     */
    public String ignoreCase(String in)
    {
        return toUpperCase(in);
    }

    /**
     * @see com.frameworkset.orm.adapter.DB#getIDMethodType()
     */
    public String getIDMethodType()
    {
        return AUTO_INCREMENT;
    }

    /**
     * @see com.frameworkset.orm.adapter.DB#getIDMethodSQL(Object obj)
     */
    public String getIDMethodSQL(Object obj)
    {
        StringBuffer command = new StringBuffer("select IDENTITY() from ");
        String qualifiedIdentifier = (String) obj;
        command.append(qualifiedIdentifier);
        return command.toString();
    }

    /**
     * Locks the specified table.
     *
     * @param con The JDBC connection to use.
     * @param table The name of the table to lock.
     * @exception SQLException No Statement could be created or executed.
     */
    public void lockTable(Connection con, String table) throws SQLException
    {
    }

    /**
     * Unlocks the specified table.
     *
     * @param con The JDBC connection to use.
     * @param table The name of the table to unlock.
     * @exception SQLException No Statement could be created or executed.
     */
    public void unlockTable(Connection con, String table) throws SQLException
    {
    }
}
