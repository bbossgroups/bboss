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
import java.util.StringTokenizer;

import com.frameworkset.orm.engine.model.Domain;
import com.frameworkset.orm.engine.model.SchemaType;
import com.frameworkset.orm.platform.PlatformCloudscapeImpl;

/**
 * This is used to connect to Cloudscape SQL databases.
 *
 * @author <a href="mailto:taylor@apache.org">David Sean Taylor</a>
 * @version $Id: DBCloudscape.java,v 1.7 2005/01/31 19:43:55 tfischer Exp $
 */
public class DBCloudscape extends DB
{
    /** qualifier */
    private static final String QUALIFIER = ".";

    /**
     * Constructor.
     */
    protected DBCloudscape()
    {
    	this.platform = new PlatformCloudscapeImpl();
    }
    /**
     * This method is used to ignore case.
     *
     * @param in The string to transform to upper case.
     * @return The upper case string.
     */
    public String toUpperCase(String in)
    {
        return in;
    }

    /**
     * This method is used to ignore case.
     *
     * @param in The string whose case to ignore.
     * @return The string in a case that can be ignored.
     */
    public String ignoreCase(String in)
    {
        return in;
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
        StringBuffer sql = new StringBuffer(132);
        sql.append("select distinct ConnectionInfo.lastAutoincrementValue(");

        String qualifiedIdentifier = (String) obj;

        StringTokenizer tokenizer = new StringTokenizer(qualifiedIdentifier,
                QUALIFIER);
        int count = tokenizer.countTokens();

        String schema, table, column;

        System.out.println("qi = " + qualifiedIdentifier);
        // no qualifiers, its simply a column name
        switch (count)
        {
        case 0:
            return ""; // not valid -- we need the column name and table name
        case 1:
            return ""; // not valid -- we need the table name to select from

        case 2:
            table = tokenizer.nextToken();
            column = tokenizer.nextToken();
            sql.append("'APP', '");
            sql.append(table);
            break;

        case 3:
            schema = tokenizer.nextToken();
            table = tokenizer.nextToken();
            column = tokenizer.nextToken();
            sql.append("'");
            sql.append(schema);
            sql.append("', '");
            sql.append(table);
            break;

        default:
            return ""; // not valid
        }

        sql.append("', '");
        sql.append(column);
        sql.append("') FROM ");
        sql.append(table);

        System.out.println(sql.toString());
        return sql.toString();
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
    public String getNativeIdMethod()
	{
		// TODO Auto-generated method stub
		return this.platform.getNativeIdMethod();
	}

	public int getMaxColumnNameLength()
	{
		// TODO Auto-generated method stub
		return this.platform.getMaxColumnNameLength();
	}

	public Domain getDomainForSchemaType(SchemaType jdbcType)
	{
		// TODO Auto-generated method stub
		return this.getDomainForSchemaType(jdbcType);
	}

	public String getNullString(boolean notNull)
	{
		// TODO Auto-generated method stub
		return this.platform.getNullString(notNull);
	}

	public String getAutoIncrement()
	{
		// TODO Auto-generated method stub
		return this.platform.getAutoIncrement();
	}

	public boolean hasSize(String sqlType)
	{
		// TODO Auto-generated method stub
		return this.platform.hasSize(sqlType);
	}

	public boolean hasScale(String sqlType)
	{
		// TODO Auto-generated method stub
		return this.platform.hasScale(sqlType);
	}
}
