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

import com.frameworkset.orm.platform.PlatformInterbaseImpl;

/**
 * This code should be used for an Interbase database pool.
 *
 * @author <a href="mailto:frank@opticode.co.za">Frank Conradie</a>
 * @version $Id: DBInterbase.java,v 1.9 2005/01/31 19:43:55 tfischer Exp $
 */
public class DBInterbase extends DB
{
	public DBInterbase()
	{
		this.platform = new PlatformInterbaseImpl();
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
     * This method is used to ignore case in an ORDER BY clause.
     * Usually it is the same as ignoreCase, but some databases
     * (Interbase for example) does not use the same SQL in ORDER BY
     * and other clauses.
     *
     * @param in The string whose case to ignore.
     * @return The string in a case that can be ignored.
     */
    public String ignoreCaseInOrderBy(String in)
    {
        return in;
    }

    /**
     * Gets the string delimiter (usually '\'').
     *
     * @return The delimiter.
     */
    public char getStringDelimiter()
    {
        return '\'';
    }

    /**
     * @see com.frameworkset.orm.adapter.DB#getIDMethodType()
     */
    public String getIDMethodType()
    {
        return NO_ID_METHOD;
    }

    /**
     * @see com.frameworkset.orm.adapter.DB#getIDMethodSQL(Object obj)
     */
    public String getIDMethodSQL(Object obj)
    {
        return null;
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
        Statement statement = con.createStatement();

        StringBuffer stmt = new StringBuffer();
        stmt.append("SET TRANSACTION ")
                .append("ISOLATION LEVEL READ COMMITTED ")
                .append("NO RECORD_VERSION WAIT ")
                .append("RESERVING ")
                .append(table)
                .append(" FOR PROTECTED WRITE");

        statement.executeQuery(stmt.toString());
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
        // Tables in Interbase are unlocked when a commit is issued.
        // The user may have issued a commit but do it here to be
        // sure.
        con.commit();
    }
}
