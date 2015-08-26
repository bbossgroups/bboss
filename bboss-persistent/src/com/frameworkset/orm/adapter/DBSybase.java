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
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.frameworkset.common.poolman.PreparedDBUtil;
import com.frameworkset.orm.platform.PlatformSybaseImpl;

/**
 * This is used to connect to a Sybase database using Sybase's
 * JConnect JDBC driver.
 *
 * <B>NOTE:</B><I>Currently JConnect does not implement the required
 * methods for ResultSetMetaData, and therefore the village API's may
 * not function.  For connection pooling, everything works.</I>
 *
 * @author <a href="mailto:ekkerbj@netscape.net">Jeff Brekke</a>
 * @version $Id: DBSybase.java,v 1.10 2005/01/31 19:43:55 tfischer Exp $
 */
public class DBSybase extends DB
{
    /** date format */
    public static final String DATE_FORMAT = "yyyyMMdd HH:mm:ss";

    /**
     * Empty constructor.
     */
    protected DBSybase()
    {
    	this.platform = new PlatformSybaseImpl();
    }

    /**
     * This method is used to ignore case.
     *
     * @param in The string to transform to upper case.
     * @return The upper case string.
     */
    public String toUpperCase(String in)
    {
        return new StringBuffer("UPPER(").append(in).append(")").toString();
    }

    /**
     * This method is used to ignore case.
     *
     * @param in The string whose case to ignore.
     * @return The string in a case that can be ignored.
     */
    public String ignoreCase(String in)
    {
        return new StringBuffer("UPPER(").append(in).append(")").toString();
    }

    /**
     * @see com.frameworkset.orm.adapter.DB#getIDMethodType()
     */
    public String getIDMethodType()
    {
        return AUTO_INCREMENT;
    }

    /**
     * Returns the last value from an identity column (available on a
     * per-session basis from the global variable
     * <code>@@identity</code>).
     *
     * @see com.frameworkset.orm.adapter.DB#getIDMethodSQL(Object obj)
     */
    public String getIDMethodSQL(Object unused)
    {
        return "select @@identity";
    }

    /**
     * Locks the specified table.
     *
     * @param con The JDBC connection to use.
     * @param table The name of the table to lock.
     * @throws SQLException No Statement could be created or executed.
     */
    public void lockTable(Connection con, String table) throws SQLException
    {
        Statement statement = con.createStatement();

        StringBuffer stmt = new StringBuffer();
        stmt.append("SELECT next_id FROM ")
        .append(table)
        .append(" FOR UPDATE");

        statement.executeQuery(stmt.toString());
    }

    /**
     * Unlocks the specified table.
     *
     * @param con The JDBC connection to use.
     * @param table The name of the table to unlock.
     * @throws SQLException No Statement could be created or executed.
     */
    public void unlockTable(Connection con, String table) throws SQLException
    {
        // Tables in Sybase are unlocked when a commit is issued.  The
        // user may have issued a commit but do it here to be sure.
        con.commit();
    }

    /**
     * This method is used to chek whether the database natively
     * supports limiting the size of the resultset.
     *
     * @return True.
     */
    public boolean supportsNativeLimit()
    {
        return true;
    }

    /**
     * This method is used to chek whether the database supports
     * limiting the size of the resultset.
     *
     * @return LIMIT_STYLE_SYBASE.
     */
    public int getLimitStyle()
    {
        return DB.LIMIT_STYLE_SYBASE;
    }

    /**
     * This method overrides the JDBC escapes used to format dates
     * using a <code>DateFormat</code>.  As of version 11, the Sybase
     * JDBC driver does not implement JDBC 3.0 escapes.
     *
     * @param date the date to format
     * @return The properly formatted date String.
     */
    public String getDateString(Date date)
    {
        char delim = getStringDelimiter();
        return (delim + new SimpleDateFormat(DATE_FORMAT).format(date) + delim);
    }
    
    public void setObject(PreparedDBUtil dbutil,int i, Object o) throws SQLException
	  {
    	if(o == null )
    	{
    		 dbutil.setObject(i, o, 12);//fixed sysbase null exception.
    	}
    	else
    	{
    		super.setObject(dbutil, i, o);
    	}
		  
	  }
}
