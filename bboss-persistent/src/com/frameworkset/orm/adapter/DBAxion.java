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
 * Copyright 2001-2004 The Apache Software Foundation.
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

import com.frameworkset.orm.engine.model.Domain;
import com.frameworkset.orm.engine.model.SchemaType;
import com.frameworkset.orm.platform.PlatformAxionImpl;

/**
 * This is used to connect to Hypersonic SQL databases.
 *
 * <a href="http://axion.tigris.org">http://axion.tigris.org</a>
 *
 * @author <a href="mailto:mpoeschl@marmot.at">Martin Poeschl</a>
 * @version $Id: DBAxion.java,v 1.3 2005/01/31 19:43:55 tfischer Exp $
 */
public class DBAxion extends DB
{
	
	/**
     * Constructor.
     */
    protected DBAxion()
    {
    	this.platform = new PlatformAxionImpl();
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
