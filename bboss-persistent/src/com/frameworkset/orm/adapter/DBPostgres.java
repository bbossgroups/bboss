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
import java.text.SimpleDateFormat;
import java.util.Date;

import com.frameworkset.orm.adapter.DB.PagineSql;
import com.frameworkset.orm.platform.PlatformPostgresqlImpl;

/**
 * This is used to connect to PostgresQL databases.
 *
 * <a href="http://www.postgresql.org/">http://www.postgresql.org/</a>
 *
 * @author <a href="mailto:hakan42@gmx.de">Hakan Tandogan</a>
 * @author <a href="mailto:hps@intermeta.de">Henning P. Schmiedehausen</a>
 * @version $Id: DBPostgres.java,v 1.19 2005/05/08 15:56:49 tfischer Exp $
 */
public class DBPostgres extends DB
{

    /** A specialized date format for PostgreSQL. */
    public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    private SimpleDateFormat sdf = null;

//    /**
//     * Empty constructor.
//     */
//    protected DBPostgres()
//    {
//        sdf = new SimpleDateFormat(DATE_FORMAT);
//    }

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
        String s = new StringBuffer("UPPER(").append(in).append(")").toString();
        return s;
    }

    /**
     * @see com.frameworkset.orm.adapter.DB#getIDMethodType()
     */
    public String getIDMethodType()
    {
        return SEQUENCE;
    }

    /**
     * @param name The name of the field (should be of type
     *      <code>String</code>).
     * @return SQL to retreive the next database key.
     * @see com.frameworkset.orm.adapter.DB#getIDMethodSQL(Object)
     */
    public String getIDMethodSQL(Object name)
    {
        return ("select nextval('" + name + "')");
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
     * This method is used to chek whether the database natively
     * supports returning results starting at an offset position other
     * than 0.
     *
     * @return True.
     */
    public boolean supportsNativeOffset()
    {
        return true;
    }

    /**
     * This method is used to chek whether the database supports
     * limiting the size of the resultset.
     *
     * @return LIMIT_STYLE_POSTGRES.
     */
    public int getLimitStyle()
    {
        return DB.LIMIT_STYLE_POSTGRES;
    }

    /**
     * Override the default behavior to associate b with null?
     *
     * @see DB#getBooleanString(Boolean)
     */
    public String getBooleanString(Boolean b)
    {
        return (b == null) ? "FALSE" : (Boolean.TRUE.equals(b) ? "TRUE" : "FALSE");
    }

    /**
     * This method overrides the JDBC escapes used to format dates
     * using a <code>DateFormat</code>.
     *
     * This generates the timedate format defined in
     * http://www.postgresql.org/docs/7.3/static/datatype-datetime.html
     * which defined PostgreSQL dates as YYYY-MM-DD hh:mm:ss
     * @param date the date to format
     * @return The properly formatted date String.
     */
    public String getDateString(Date date)
    {
        StringBuffer dateBuf = new StringBuffer();
        char delim = getStringDelimiter();
        dateBuf.append(delim);
        dateBuf.append(sdf.format(date));
        dateBuf.append(delim);
        return dateBuf.toString();
    }
    
    public DBPostgres()
    {
    	 sdf = new SimpleDateFormat(DATE_FORMAT);
    	this.platform = new PlatformPostgresqlImpl();
    }
    
	/**
	 * 获取指定数据的分页数据sql语句
	 * @param sql
	 * @return
	 */
	public PagineSql getDBPagineSql(String sql, long offset, int maxsize,boolean prepared) {
		
//		return new StringBuilder(sql).append(" limit ").append(offset).append(",").append(maxsize).toString();
		StringBuilder newsql = null;
		if(prepared)
			newsql = new StringBuilder().append(sql).append(" limit ?,?");
		else
			newsql = new StringBuilder().append(sql).append(" limit ").append(offset).append(",").append(maxsize);
		return new PagineSql(newsql.toString(),offset,(long)maxsize,offset, maxsize, prepared).setRebuilded(true);
	}
	
	  public String getStringPagineSql(String sql)
	  {
		  StringBuilder newsql = new StringBuilder().append(sql).append(" limit ?,?");
			return newsql.toString();
	  }
	  public String getStringPagineSql(String schema,String tablename,String pkname ,String columns)
	    {
	    	StringBuilder sqlbuilder = new StringBuilder();
			 	sqlbuilder.append("SELECT ");
			 	if(columns != null && ! columns.equals(""))
			 	{
			 		sqlbuilder.append( columns);
			 	}
			 	else
			 		sqlbuilder.append("* ");
			 	sqlbuilder.append(" from   ");
			 	if(schema != null && !schema.equals(""))
			 		sqlbuilder.append(schema).append(".");
			 	sqlbuilder.append( tablename);
			 sqlbuilder.append( " limit ?,?");
	        return sqlbuilder.toString();
	    }
	  /**
		 * 获取指定数据的分页数据sql语句
		 * @param sql
		 * @return
		 */
		public PagineSql getDBPagineSql(String sql, long offset, int maxsize,boolean prepared,String orderBy) {
			
//			return new StringBuilder(sql).append(" limit ").append(offset).append(",").append(maxsize).toString();
			StringBuilder newsql = null;
			if(prepared)
				newsql = new StringBuilder().append(sql).append(" ").append(orderBy).append(" limit ?,?");
			else
				newsql = new StringBuilder().append(sql).append(" ").append(orderBy).append(" limit ").append(offset).append(",").append(maxsize);
			return new PagineSql(newsql.toString(),offset,(long)maxsize,offset, maxsize, prepared).setRebuilded(true);
		}
		
		  public String getStringPagineSql(String sql,String orderBy)
		  {
			  StringBuilder newsql = new StringBuilder().append(sql).append(" ").append(orderBy).append(" limit ?,?");
				return newsql.toString();
		  }
		  public String getStringPagineSql(String schema,String tablename,String pkname ,String columns,String orderBy)
		    {
		    	StringBuilder sqlbuilder = new StringBuilder();
				 	sqlbuilder.append("SELECT ");
				 	if(columns != null && ! columns.equals(""))
				 	{
				 		sqlbuilder.append( columns);
				 	}
				 	else
				 		sqlbuilder.append("* ");
				 	sqlbuilder.append(" from   ");
				 	if(schema != null && !schema.equals(""))
				 		sqlbuilder.append(schema).append(".");
				 	sqlbuilder.append( tablename);
				 sqlbuilder.append(" ").append(orderBy).append( " limit ?,?");
		        return sqlbuilder.toString();
		    }
}
