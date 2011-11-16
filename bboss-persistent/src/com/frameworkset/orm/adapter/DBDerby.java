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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * This is used to connect to an embedded Apache Derby Database using
 * the supplied JDBC driver.
 *
 * @author <a href="mailto:hps@intermeta.de">Henning P. Schmiedehausen</a>
 * @version $Id: DBDerby.java,v 1.2 2005/01/31 19:43:55 tfischer Exp $
 */
public class DBDerby
        extends DB
{
    /**
     * Empty constructor.
     */
    protected DBDerby()
    {
    	
    }

    /**
     * This method is used to ignore case.
     *
     * @param in The string to transform to upper case.
     * @return The upper case string.
     */
    public String toUpperCase(String str)
    {
        return new StringBuffer("UPPER(")
                .append(str)
                .append(")")
                .toString();
    }

    /**
     * This method is used to ignore case.
     *
     * @param in The string whose case to ignore.
     * @return The string in a case that can be ignored.
     */
    public String ignoreCase(String str)
    {
        return toUpperCase(str);
    }

    /**
     * @see com.frameworkset.orm.adapter.DB#getIDMethodType()
     */
    public String getIDMethodType()
    {
        return NO_ID_METHOD; // @todo IDENTITY;
    }

    /**
     * @see com.frameworkset.orm.adapter.DB#getIDMethodSQL(Object obj)
     */
    public String getIDMethodSQL(Object obj)
    {
        return ""; // @todo VALUES IDENTITY_VAL_LOCAL()";
    }

    /**
     * Locks the specified table.
     *
     * @param con The JDBC connection to use.
     * @param table The name of the table to lock.
     * @exception SQLException No Statement could be created or executed.
     */
    public void lockTable(Connection con, String table)
            throws SQLException
    {
        Statement statement = con.createStatement();
        StringBuffer stmt = new StringBuffer();
        stmt.append("LOCK TABLE ")
                .append(table).append(" IN EXCLUSIVE MODE");
        statement.executeUpdate(stmt.toString());
    }

    /**
     * Unlocks the specified table.
     *
     * @param con The JDBC connection to use.
     * @param table The name of the table to unlock.
     * @exception SQLException No Statement could be created or executed.
     */
    public void unlockTable(Connection con, String table)
            throws SQLException
    {
    }

    /**
     * This method is used to check whether the database supports
     * limiting the size of the resultset.
     *
     * @return LIMIT_STYLE_DB2.
     */
    public int getLimitStyle()
    {
        return DB.LIMIT_STYLE_DB2;
    }
    
  public int getSCROLLType(String dbdriver)
  {
      return ResultSet.TYPE_SCROLL_INSENSITIVE;
  }
  public int getCusorType(String dbdriver)
    {
        
        return ResultSet.CONCUR_READ_ONLY;
    }
}
