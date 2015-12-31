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

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.frameworkset.common.poolman.PreparedDBUtil;
import com.frameworkset.common.poolman.SQLExecutor;
import com.frameworkset.common.poolman.handle.NullRowHandler;
import com.frameworkset.orm.platform.PlatformMysqlImpl;
import com.frameworkset.util.SimpleStringUtil;

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
public class DBMM extends DB
{

//    /** A specialized date format for MySQL. */
//    public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public String sysdate()
    {
    	return "sysdate()";
    }
    /**
     * Empty protected constructor.
     */
    protected DBMM()
    {
    	this.platform = new PlatformMysqlImpl();
    	FORMART_YEAR = "%Y";
    	FORMART_MONTH = "%c";
    	FORMART_DAY = "%d";
    	FORMART_HOUR = "%h";
    	FORMART_MINUTE = "%i";
    	FORMART_SECOND = "%s";
    	FORMART_ALL = "%Y-%c-%d %h:%i:%s";
    	FORMART_YEAR_MM_DD = "%Y-%c-%d";
    	FORMART_HH_MM_SS = "%h:%i:%s";
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
     * Returns the SQL to get the database key of the last row
     * inserted, which in this case is <code>SELECT
     * LAST_INSERT_ID()</code>.
     *
     * @see com.frameworkset.orm.adapter.DB#getIDMethodSQL(Object obj)
     */
    public String getIDMethodSQL(Object obj)
    {
    	
        return "SELECT LAST_INSERT_ID()";
    }
    
    /**
     * 采用mysql官方生成主键方法，但是有并发干扰问题
     * 例如，一个con来生成不同表的主键时，由于时间顺序问题可能出现以下情况：
     * 第一个表执行了"UPDATE "+sequence +" SET id=LAST_INSERT_ID(id+1)"，正要执行"SELECT LAST_INSERT_ID()"时
     * 第二个表先于第一个表执行完"UPDATE "+sequence +" SET id=LAST_INSERT_ID(id+1)"，"SELECT LAST_INSERT_ID()"两步，第二个得到正确的主键值
     * 最终第一个表在执行"SELECT LAST_INSERT_ID()"时，结果得到的是第二个表的主键值，导致不正确的结果出现。
     */
//    public long getNextValue(String sequence,Connection con,String dbname) throws SQLException
//    {
//    	long curValue = 0;
//    	PreparedDBUtil dbutil = new PreparedDBUtil();
//    	boolean flag = false;
//		try {
////			if()
//			if(con == null)
//			{
//				con = PreparedDBUtil.getConection(dbname);
////				System.out.println("getNextValue:"+con);
//				flag = true;
//			}
//			dbutil.executeUpdate(dbname, "UPDATE "+sequence +" SET id=LAST_INSERT_ID(id+1)",con);
//			dbutil.executeSelect(dbname,"SELECT LAST_INSERT_ID()",con);
//			if(dbutil.size() <= 0)
//			{
////				System.out.println("select " + this.generator + ".nextval from dual");
//				throw new SQLException("[SELECT nextval('"+sequence+"')] from [" + dbname + "] failed:retrun records is 0.");
//			}
//			curValue = dbutil.getLong(0,0);
//			
//			return curValue;
//				
//		} catch (SQLException e) {
//			throw e;
//		}
//		finally{
//			if(flag )
//			{
//				if(con != null)con.close();
//			}
//		}
//		
//    }
    private static final String sql_sequence = "select nextval(?) as pk";
    public long getNextValue(String sequence,Connection con,String dbname) throws SQLException
    {
//    	long curValue = 0;
    	
    	PreparedDBUtil dbutil = new PreparedDBUtil();
		
		dbutil.preparedSelect(dbname, sql_sequence);
		dbutil.setString(1, sequence);
		dbutil.executePrepared();
		return dbutil.getLong(0, 0);
		
    }
    
    public long getNextValue(String seqfunctionname,String sequence,Connection con,String dbname) throws SQLException
    {
//    	long curValue = 0;
    	
    	PreparedDBUtil dbutil = new PreparedDBUtil();
		if(seqfunctionname == null || seqfunctionname.equals(""))
			dbutil.preparedSelect(dbname, sql_sequence);
		else
			dbutil.preparedSelect(dbname, "select " + seqfunctionname + "(?)  as pk");
		dbutil.setString(1, sequence);
		dbutil.executePrepared();
		return dbutil.getLong(0, 0);
		
    }

    /**
     * Locks the specified table.
     *
     * @param con The JDBC connection to use.
     * @param table The name of the table to lock.
     * @exception SQLException No Statement could be created or
     * executed.
     */
    public void lockTable(Connection con, String table) throws SQLException
    {
        Statement statement = con.createStatement();
        StringBuilder stmt = new StringBuilder();
        stmt.append("LOCK TABLE ").append(table).append(" WRITE");
        statement.executeUpdate(stmt.toString());
    }

    /**
     * Unlocks the specified table.
     *
     * @param con The JDBC connection to use.
     * @param table The name of the table to unlock.
     * @exception SQLException No Statement could be created or
     * executed.
     */
    public void unlockTable(Connection con, String table) throws SQLException
    {
        Statement statement = con.createStatement();
        statement.executeUpdate("UNLOCK TABLES");
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
     * @return LIMIT_STYLE_MYSQL.
     */
    public int getLimitStyle()
    {
        return DB.LIMIT_STYLE_MYSQL;
    }

    
    
    public String to_char(String date,String format)
    {
    	StringBuilder ret = new StringBuilder();
   	 ret.append("date_format(")
   	 	.append(date)
   	 	.append(",'")
   	 	.append(format != null?format:FORMART_ALL)
   	 	.append("')"); 
   	 
   	 return ret.toString();
	         
    }
    	
    public String to_char(String date)
    {
    	return to_char(date,FORMART_ALL);
    }
    
    
    /**
     * This method overrides the JDBC escapes used to format dates
     * using a <code>DateFormat</code>.  As of version 2.0.11, the MM
     * JDBC driver does not implement JDBC 3.0 escapes.
     *
     * @param date the date to format
     * @return The properly formatted date String.
     * @deprecated use to_date function.
     */
    public String getDateString(Date date)
    {
    	return to_date(date,this.date_format);
    }
    
    /**
     * This method overrides the JDBC escapes used to format dates
     * using a <code>DateFormat</code>.  As of version 2.0.11, the MM
     * JDBC driver does not implement JDBC 3.0 escapes.
     *
     * @param date the date to format
     * @return The properly formatted date String.
     * @deprecated use to_date function.
     */
    public String getDateString(Date date,String format)
    {
    	return to_date(date,format);
    }
    
    /**
     * This method overrides the JDBC escapes used to format dates
     * using a <code>DateFormat</code>.  As of version 2.0.11, the MM
     * JDBC driver does not implement JDBC 3.0 escapes.
     *
     * @param date the date to format
     * @return The properly formatted date String.
     * @deprecated use to_date function.
     */
    public String getDateString(String date)
    {
    	return to_date(date,this.date_format);
    	
    }
    /**
     * This method overrides the JDBC escapes used to format dates
     * using a <code>DateFormat</code>.  As of version 2.0.11, the MM
     * JDBC driver does not implement JDBC 3.0 escapes.
     *
     * @param date the date to format
     * @return The properly formatted date String.
     * @deprecated use to_date function.
     */
    public String getDateString(String date,String format)
    {
    	return to_date(date,format);
    }
    
    
    /**
     * This method overrides the JDBC escapes used to format dates
     * using a <code>DateFormat</code>.  As of version 2.0.11, the MM
     * JDBC driver does not implement JDBC 3.0 escapes.
     *
     * @param date the date to format
     * @return The properly formatted date String.
     */
    public String to_date(Date date)
    {
    	return to_date(date,this.date_format);
    }
    
    /**
     * This method overrides the JDBC escapes used to format dates
     * using a <code>DateFormat</code>.  As of version 2.0.11, the MM
     * JDBC driver does not implement JDBC 3.0 escapes.
     *
     * @param date the date to format
     * @return The properly formatted date String.
     */
    public String to_date(Date date,String format)
    {
    	if(date == null)
    		return null;
        char delim = getStringDelimiter();
        return (delim + new SimpleDateFormat(format == null?this.date_format:format).format(date) + delim);
    }
    
    /**
     * This method overrides the JDBC escapes used to format dates
     * using a <code>DateFormat</code>.  As of version 2.0.11, the MM
     * JDBC driver does not implement JDBC 3.0 escapes.
     *
     * @param date the date to format
     * @return The properly formatted date String.
     */
    public String to_date(String date)
    {
    	return to_date(date,this.date_format);
    }
    /**
     * This method overrides the JDBC escapes used to format dates
     * using a <code>DateFormat</code>.  As of version 2.0.11, the MM
     * JDBC driver does not implement JDBC 3.0 escapes.
     *
     * @param date the date to format
     * @return The properly formatted date String.
     */
    public String to_date(String date,String format)
    {
    	if(date == null)
    		return null;
        char delim = getStringDelimiter();
        return (delim + new SimpleDateFormat(format == null?this.date_format:format).format(SimpleStringUtil.stringToDate(date,format)) + delim);
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
    			maxSql = "select max(CAST(SUBSTRING(" + table_id_name + ",len(" + table_id_prefix + ") + 1) as DECIMAL))) from " + table_name;
    		}
    		else
    		{
    			maxSql = "select max(CAST(" + table_id_name + " as DECIMAL)) from " + table_name;
    		}
    	}
		
		return maxSql;
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
	  public void queryByNullRowHandler(NullRowHandler handler,String dbname,String pageinestatement,long offset,int pagesize) throws SQLException
	    {
	    	SQLExecutor.queryWithDBNameByNullRowHandler(handler, dbname, pageinestatement,offset,pagesize);
	    }
	
	public void resetPostion( PreparedStatement statement,int startidx,int endidx,long offset,int maxsize) throws SQLException
    {
    	statement.setLong(startidx, offset);
		statement.setLong(endidx, maxsize);
    }
	public Object getLONGVARBINARY(CallableStatement cstmt,int parameterIndex) throws SQLException
    {
        return    cstmt.getBlob(parameterIndex);
    }
    
    public Object getLONGVARCHAR(CallableStatement cstmt,int parameterIndex) throws SQLException
    {
        return    cstmt.getClob(parameterIndex);
    }
    public Object getLONGVARCHAR(ResultSet res,int parameterIndex) throws SQLException
    {
        return    res.getClob(parameterIndex);
    }
    
    public Object getLONGVARBINARY(ResultSet res,int parameterIndex) throws SQLException
    {
        return    res.getBlob(parameterIndex);
    }
    
    
    
    public Object getLONGVARBINARY(CallableStatement cstmt,String parameterIndex) throws SQLException
    {
        return    cstmt.getBlob(parameterIndex);
    }
    
    public Object getLONGVARCHAR(CallableStatement cstmt,String parameterIndex) throws SQLException
    {
        return    cstmt.getClob(parameterIndex);
    }
    public Object getLONGVARCHAR(ResultSet res,String parameterIndex) throws SQLException
    {
        return    res.getClob(parameterIndex);
    }
    
    public Object getLONGVARBINARY(ResultSet res,String parameterIndex) throws SQLException
    {
        return    res.getBlob(parameterIndex);
    }
    

	public String concat(String ... concatString)
	{
		if(concatString == null || concatString.length == 0)
			return "";
		StringBuilder ret = new StringBuilder();
		boolean i = false;
		for(String token : concatString)
		{
			if(!i)
			{
				ret.append("concat(").append(token);
				i = true;
			}				
			else
			{
				
				ret.append( " , ").append(token);
			}			
			
		}
		ret.append(")");
		return ret.toString();
	}
	
	public String disableFK(String table,String FKName)
	{
		StringBuilder ret = new StringBuilder();
		ret.append("alter table ").append(table).append(" drop FOREIGN KEY ").append(FKName);
		
		return ret.toString();
		
		
		
	}
	
	public String enableFK(String table,String FKName,String column,String FKTable,String FKColumn)
	{
		StringBuilder ret = new StringBuilder();
		ret.append("alter table ").append(table).append(" add constraint ").append(FKName)
		.append(" foreign key (").append(column).append(") references ").append(FKTable).append(" (").append(FKColumn).append(")");
		   
		return ret.toString();
	}
	public boolean neadGetGenerateKeys()
	{
		return true;
	}
    
}
