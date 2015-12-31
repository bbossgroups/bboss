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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import org.frameworkset.spi.BaseApplicationContext;

import com.frameworkset.common.poolman.PreparedDBUtil;
import com.frameworkset.common.poolman.SQLExecutor;
import com.frameworkset.common.poolman.handle.NullRowHandler;
import com.frameworkset.common.poolman.handle.ValueExchange;
import com.frameworkset.common.poolman.security.DBInfoEncrypt;
import com.frameworkset.common.poolman.security.DESDBPasswordEncrypt;
import com.frameworkset.common.poolman.util.JDBCPoolMetaData;
import com.frameworkset.orm.adapter.DB.PagineSql;
import com.frameworkset.orm.engine.model.Domain;
import com.frameworkset.orm.engine.model.SchemaType;
import com.frameworkset.orm.platform.Platform;
import com.frameworkset.orm.platform.PlatformDefaultImpl;
import com.frameworkset.util.SimpleStringUtil;

/**
 * <code>DB</code> defines the interface for a Torque database
 * adapter.  Support for new databases is added by subclassing
 * <code>DB</code> and implementing its abstract interface, and by
 * registering the new database adapter and its corresponding
 * JDBC driver in the service configuration file.
 *
 * <p>The Torque database adapters exist to present a uniform
 * interface to database access across all available databases.  Once
 * the necessary adapters have been written and configured,
 * transparent swapping of databases is theoretically supported with
 * <i>zero code changes</i> and minimal configuration file
 * modifications.
 *
 * <p>Torque uses the driver class name to find the right adapter.
 * A JDBC driver corresponding to your adapter must be added to the properties
 * file, using the fully-qualified class name of the driver. If no driver is
 * specified for your database, <code>driver.default</code> is used.
 *
 * <pre>
 * #### MySQL MM Driver
 * database.default.driver=org.gjt.mm.mysql.Driver
 * database.default.url=jdbc:mysql://localhost/DATABASENAME
 * </pre>
 *
 * @author <a href="mailto:jon@latchkey.com">Jon S. Stevens</a>
 * @author <a href="mailto:bmclaugh@algx.net">Brett McLaughlin</a>
 * @author <a href="mailto:dlr@finemaltcoding.com">Daniel Rall</a>
 * @author <a href="mailto:vido@ldh.org">Augustin Vidovic</a>
 * @version $Id: DB.java,v 1.34 2005/01/31 19:43:55 tfischer Exp $
 */
public abstract class DB implements Serializable, IDMethod,Platform
{
	public static final String NULL_SCHEMA = "NULL_SCHEMA";
    /** date format used in getDateString() */
    protected String date_format = "yyyy-MM-dd HH:mm:ss";
    private static String java_date_format = "yyyy-MM-dd HH:mm:ss";
    protected String FORMART_YEAR = "yyyy";
    protected String FORMART_MONTH = "MM";
    protected String FORMART_DAY = "dd";
    protected String FORMART_HOUR = "HH";
    protected String FORMART_MINUTE = "mm";
    protected String FORMART_SECOND = "ss";
    protected String FORMART_ALL = "yyyy-MM-dd HH:mm:ss";
    protected String FORMART_YEAR_MM_DD = "yyyy-MM-dd";
    protected String FORMART_HH_MM_SS = "HH:mm:ss";
    /** Database does not support limiting result sets. */
    public static final int LIMIT_STYLE_NONE = 0;

    /** <code>SELECT ... LIMIT <limit>, [&lt;offset&gt;]</code> */
    public static final int LIMIT_STYLE_POSTGRES = 1;

    /** <code>SELECT ... LIMIT [<offset>, ] &lt;offset&gt;</code> */
    public static final int LIMIT_STYLE_MYSQL = 2;

    /** <code>SET ROWCOUNT &lt;offset&gt; SELECT ... SET ROWCOUNT 0</code> */
    public static final int LIMIT_STYLE_SYBASE = 3;

    /** <code><pre>SELECT ... WHERE ... AND ROWNUM < <limit></pre></code> */
    public static final int LIMIT_STYLE_ORACLE = 4;

    /** <code><pre>SELECT ... WHERE ... AND ROW_NUMBER() OVER() < <limit></pre></code> */
    public static final int LIMIT_STYLE_DB2 = 5;
    
    protected Platform platform = null;
    protected String dbtype = null;

    /**
     * Empty constructor.
     */
    protected DB()
    {
    	this.platform = new PlatformDefaultImpl();
    }

    /**
     * This method is used to ignore case.
     *
     * @param in The string to transform to upper case.
     * @return The upper case string.
     */
    public abstract String toUpperCase(String in);

    /**
     * Returns the character used to indicate the beginning and end of
     * a piece of text used in a SQL statement (generally a single
     * quote).
     *
     * @return The text delimeter.
     */
    public char getStringDelimiter()
    {
        return '\'';
    }
    
    public String sysdate()
    {
    	return "sysdate";
    }
    
    public long getNextValue(String sequence,Connection con,String dbname) throws SQLException
    {
    	long curValue = 0;
    	PreparedDBUtil dbutil = new PreparedDBUtil();
		try {
//			if()
			String sql = "select " + sequence + ".nextval from dual";
			dbutil.preparedSelect(dbname, sql);
			dbutil.executePrepared(con);
			if(dbutil.size() <= 0)
			{
//				System.out.println("select " + this.generator + ".nextval from dual");
				throw new SQLException("[select " + sequence
						+ ".nextval from dual] from [" + dbname + "] failed:retrun records is 0.");
			}
			curValue = dbutil.getInt(0,0);
			
			return curValue;
				
		} catch (SQLException e) {
			throw e;
		}
		
    }
    
    public long getNextValue(String seqfunctionname,String sequence,Connection con,String dbname) throws SQLException
    {
    	long curValue = 0;
    	PreparedDBUtil dbutil = new PreparedDBUtil();
		try {
//			if()
			String sql = "select " + sequence + ".nextval from dual";
			dbutil.preparedSelect(dbname, sql);
			dbutil.executePrepared(con);
			if(dbutil.size() <= 0)
			{
//				System.out.println("select " + this.generator + ".nextval from dual");
				throw new SQLException("[select " + sequence
						+ ".nextval from dual] from [" + dbname + "] failed:retrun records is 0.");
			}
			curValue = dbutil.getInt(0,0);
			
			return curValue;
				
		} catch (SQLException e) {
			throw e;
		}
		
    }

    /**
     * Returns the constant from the {@link
     * com.frameworkset.orm.adapter.IDMethod} interface denoting which
     * type of primary key generation method this type of RDBMS uses.
     *
     * @return IDMethod constant
     */
    public abstract String getIDMethodType();

    /**
     * Returns SQL used to get the most recently inserted primary key.
     * Databases which have no support for this return
     * <code>null</code>.
     *
     * @param obj Information used for key generation.
     * @return The most recently inserted database key.
     */
    public abstract String getIDMethodSQL(Object obj);

    /**
     * Locks the specified table.
     *
     * @param con The JDBC connection to use.
     * @param table The name of the table to lock.
     * @throws SQLException No Statement could be created or executed.
     */
    public abstract void lockTable(Connection con, String table)
            throws SQLException;

    /**
     * Unlocks the specified table.
     *
     * @param con The JDBC connection to use.
     * @param table The name of the table to unlock.
     * @throws SQLException No Statement could be created or executed.
     */
    public abstract void unlockTable(Connection con, String table)
            throws SQLException;

    /**
     * This method is used to ignore case.
     *
     * @param in The string whose case to ignore.
     * @return The string in a case that can be ignored.
     */
    public abstract String ignoreCase(String in);

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
        return ignoreCase(in);
    }

    /**
     * This method is used to check whether the database natively
     * supports limiting the size of the resultset.
     *
     * @return True if the database natively supports limiting the
     * size of the resultset.
     */
    public boolean supportsNativeLimit()
    {
        return false;
    }

    /**
     * This method is used to check whether the database natively
     * supports returning results starting at an offset position other
     * than 0.
     *
     * @return True if the database natively supports returning
     * results starting at an offset position other than 0.
     */
    public boolean supportsNativeOffset()
    {
        return false;
    }

   /**
    * This method is for the SqlExpression.quoteAndEscape rules.  The rule is,
    * any string in a SqlExpression with a BACKSLASH will either be changed to
    * "\\" or left as "\".  SapDB does not need the escape character.
    *
    * @return true if the database needs to escape text in SqlExpressions.
    */

    public boolean escapeText()
    {
        return true;
    }

    /**
     * This method is used to check whether the database supports
     * limiting the size of the resultset.
     *
     * @return The limit style for the database.
     */
    public int getLimitStyle()
    {
        return LIMIT_STYLE_NONE;
    }
    
    /**
     * This method is used to format any date string.
     * Database can use different default date formats.
     *
     * @param date the Date to format
     * @return The proper date formatted String.
     */
    public String to_date(Date date)
    {
        Timestamp ts = null;
        if (date instanceof Timestamp)
        {
            ts = (Timestamp) date;
        }
        else
        {
            ts = new Timestamp(date.getTime());
        }

        return ("{ts '" + ts + "'}");
    }
    /**
     * This method is used to format any date string.
     *
     * @param date the Date to format
     * @return The date formatted String for Oracle.
     */
    public String to_date(String date)
    {
        return ("{ts '" + date + "'}");
    }
    
    /**
     * This method is used to format any date string.
     * Database can use different default date formats.
     *
     * @param date the Date to format
     * @return The proper date formatted String.
     */
    public String to_date(Date date,String format)
    {
        Timestamp ts = null;
        if (date instanceof Timestamp)
        {
            ts = (Timestamp) date;
        }
        else
        {
            ts = new Timestamp(date.getTime());
        }

        return ("{ts '" + ts + "'}");
    }
    /**
     * This method is used to format any date string.
     *
     * @param date the Date to format
     * @return The date formatted String for Oracle.
     */
    public String to_date(String date,String format)
    {
        return ("{ts '" + date + "'}");
    }

    /**
     * This method is used to format any date string.
     * Database can use different default date formats.
     *
     * @param date the Date to format
     * @return The proper date formatted String.
     * @deprecated use to_date function.
     */
    public String getDateString(Date date)
    {
        return to_date(date);
    }
    /**
     * This method is used to format any date string.
     *
     * @param date the Date to format
     * @return The date formatted String for Oracle.
     * @deprecated use to_date function.
     */
    public String getDateString(String date)
    {
    	return to_date(date);
    }
    
    /**
     * This method is used to format any date string.
     * Database can use different default date formats.
     *
     * @param date the Date to format
     * @return The proper date formatted String.
     * @deprecated use to_date function.
     */
    public String getDateString(Date date,String format)
    {
    	return to_date(date,format);
    }
    /**
     * This method is used to format any date string.
     *
     * @param date the Date to format
     * @return The date formatted String for Oracle.
     * @deprecated use to_date function.
     */
    public String getDateString(String date,String format)
    {
    	return to_date(date,format);
    }


    /**
     * This method is used to format a boolean string.
     *
     * @param b the Boolean to format
     * @return The proper date formatted String.
     */
    public String getBooleanString(Boolean b)
    {
        return (Boolean.TRUE.equals(b) ? "1" : "0");
    }

    

    /**
     * 获取受限制结果条数的sql语句，默认为mysql语法规则
     * 不同的数据库需要重载本方法
     * @param selectSql String
     * @param limit int
     * @return String
     */
    public String getLimitSelect(String selectSql , int limit)
    {
        selectSql += " LIMIT " + limit;
        return selectSql;
    }

	public Object getCharValue(ResultSet res, int i, String value) {
		// TODO Auto-generated method stub
		return value;
	}
	
	public Object getCharValue(CallableStatement cstmt, int i, String value) {
		// TODO Auto-generated method stub
		return value;
	}
	
	public Object getCharValue(CallableStatement cstmt, String paramName, String value) {
		// TODO Auto-generated method stub
		return value;
	}
	public  static int countZHWord(byte[] bytes)
	  {
	  	if(bytes == null)
	  		return 0;
	  	int count = 0;
	  	for(int i = 0; i < bytes.length; i ++)
	  	{
	  		if(bytes[i] < 0)
	  		{
	  			count ++;
	  		}
	  	}
	  	return count / 3;
	  }
	
	public String getOROPR()
	{
		return "|";
	}
	
	public String getSchema(JDBCPoolMetaData info)
	{
		if(info.getUserName() == null || info.getUserName().equals(""))
    	{
    		return null;
    	}
		if(!info.isEncryptdbinfo())
			return info.getUserName().toUpperCase();
		else
		{
			DBInfoEncrypt dbInfoEncrypt = getDBInfoEncrypt();
			return dbInfoEncrypt.decryptDBUser(info.getUserName()).toUpperCase();
		}
	}
	
	public static DBInfoEncrypt getDBInfoEncrypt()
	{
		try {
			Properties  p = BaseApplicationContext.fillProperties();
			if(p != null)
			{
				String DBInfoEncryptclass = p.getProperty("DBInfoEncryptclass");
				if(DBInfoEncryptclass != null && !DBInfoEncryptclass.trim().equals(""))
				{
					DBInfoEncryptclass = DBInfoEncryptclass.trim();
					return (DBInfoEncrypt)Class.forName(DBInfoEncryptclass).newInstance();
				}
				
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (Throwable e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
		}
		return new DESDBPasswordEncrypt();
	}
	
	
	public String getSchemaTableTableName(JDBCPoolMetaData info,String tablename)
	{
		if(tablename == null)
			return null;
		return tablename.toUpperCase();
	}
	
	public String getIDMAXSql(String tableName,String table_id_name,String table_id_prefix,String type)
	{		
		String maxSql = "select max("+ table_id_name + ") from " + tableName;
		return maxSql;
	}
	
	/*********************************************
	 *
	 *   	platform接口方法定义开始
	 * 
	 *********************************************/
	public String getNativeIdMethod()
	{
		
		return this.platform.getNativeIdMethod();
	}

	public int getMaxColumnNameLength()
	{
		
		return this.platform.getMaxColumnNameLength();
	}

	public Domain getDomainForSchemaType(SchemaType jdbcType)
	{
		// TODO Auto-generated method stub
		return this.platform.getDomainForSchemaType(jdbcType);
	}
	
	public Domain getDomainForSchemaType(int jdbcType,String datatypeName)
	{
		// TODO Auto-generated method stub
		return this.platform.getDomainForSchemaType(jdbcType, datatypeName);
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
		
		return this.platform.hasScale(sqlType);
	}
	
	public boolean hasSize(int sqlType)
	{
		
		return this.platform.hasSize(sqlType);
	}

	public boolean hasScale(int sqlType)
	{
		
		
		return this.platform.hasScale(sqlType);
	}
	
	public String getDBTYPE()
	{
		return this.dbtype;
//		return platform.getDBTYPE();
	}

	/**
	 * 获取指定数据的分页数据sql语句
	 * @param sql
	 * @return
	 */
	public PagineSql getDBPagineSql(String sql, long offset, int maxsize,boolean prepared) {
		
		return new PagineSql(sql,-1L,-1L,offset,maxsize,prepared);
	}
	
	public void resetPostion( PreparedStatement statement,int startidx,int endidx,long offset,int maxsize) throws SQLException
    {
//    	statement.setLong(startidx, offset);
//		statement.setLong(endidx, maxsize);
    }
	
//	public String getDBPagineSql(String sql, long offset, int maxsize) {
//		
//		return sql;
//	}
	
	public static class PagineSql
	{
		private long offset;
		private int maxsize;
		private String sql;
		private long start = -1L;
		private long end= -1L;
		private boolean prepared = true;
		private boolean rebuilded = false;
		public boolean isRebuilded() {
			return rebuilded;
		}
		public PagineSql setRebuilded(boolean rebuilded) {
			this.rebuilded = rebuilded;
			return this;
		}
		public PagineSql(String sql, long start, long end,long offset,int maxsize,boolean prepared) {
			super();
			this.sql = sql;
			this.start = start;
			this.end = end;
			this.offset = offset;
			this.maxsize = maxsize;
			this.prepared = prepared;
		}
		public PagineSql(String sql,boolean prepared) {
			super();
			this.sql = sql;
			this.prepared = prepared;
		}
		
		public String getSql() {
			return sql;
		}
		
		public long getStart() {
			return start;
		}
		
		public long getEnd() {
			return end;
		}
		public long getOffset() {
			return offset;
		}
		
		public int getMaxsize() {
			return maxsize;
		}
		
		public boolean isPrepared() {
			return prepared;
		}
	
		
	}
	
	 /**
     * 获取受限制结果条数的sql语句，要求selectSql的语法，按oracle自定义受限语句语法，例如
     * SELECT   a.cpmc,a.ggxh,a.cjdd,a.ph,a.jgxs,a.dj,a.jldw,a.cd,a.cddm,a.flag,cjrq,a.pricepsj_id,b.user_name, 
     * ROW_NUMBER() OVER ( ORDER   BY   cjrq ) aa 
     * from td_price_pricepsj_jmrygypjg a,td_sm_user b
     * @param selectSql String
     * @param limit int
     * @param rownum 行号的别名
     * @return String
     */
    public String getOracleLimitSelect(String selectSql , int limit,String rownum)
    {
//        selectSql += " LIMIT " + limit;
        StringBuilder ret = new StringBuilder();
        ret.append("select * from (")
        	.append(selectSql)
        	.append(") where ")
        	.append(rownum)
        	.append(" <=").append(limit);        
        return ret.toString();
    }
    
    
	
//   /**
//     * 获取高效的oracle分页语句，sql中已经写好ROW_NUMBER() OVER ( ORDER   BY   cjrq ) rownum
//     * 否则不能调用本方法生成oralce的分页语句
//     */
//	 
//	public String getOracleDBPagineSql(String sql, long offset, int maxsize,String rownum) {
//		StringBuffer ret = new StringBuffer("select * from (")
//									.append(sql)
//									.append(") where ").append(rownum).append(" between ")
//									.append((offset + 1) + "")
//									.append(" and ")
//									.append((offset  + maxsize) + "");
//		return ret.toString();
//	}
    
    /**
     * 获取高效的oracle分页语句，sql中已经写好ROW_NUMBER() OVER ( ORDER   BY   cjrq ) rownum
     * 否则不能调用本方法生成oralce的分页语句
     */
	 
	public PagineSql getOracleDBPagineSql(String sql, long offset, int maxsize,String rownum,boolean prepared) {
		StringBuilder ret = null;
		if(prepared)
			ret = new StringBuilder().append("select * from (")
									.append(sql)
									.append(") where ").append(rownum).append(" between ? and ?");
		else
			ret = new StringBuilder("select * from (")
			.append(sql)
			.append(") where ").append(rownum).append(" between ")
			.append((offset + 1) + "")
			.append(" and ")
			.append((offset  + maxsize) + "");
		return new PagineSql(ret.toString(),offset + 1,offset  + maxsize,offset, maxsize,prepared);
		
	}

	public String getTableRemarks(Connection con,String tableName, String tableRemark) {
		// TODO Auto-generated method stub
		return tableRemark;
	}

	public String getColumnRemarks(Connection con,String tableName, String columnName, String remarks_c) {
		// TODO Auto-generated method stub
		return remarks_c;
	}
	
	public SchemaType getSchemaTypeFromSqlType(int sqltype,String typeName)
	{
		return this.platform.getSchemaTypeFromSqlType(sqltype,  typeName);
	}
//	public int getSCROLLType(String dbdriver)
//	{
//	    return ResultSet.TYPE_SCROLL_INSENSITIVE;
//	}
//	public int getCusorType(String dbdriver)
//    {
//        
//        return ResultSet.CONCUR_READ_ONLY;
//    }
	
	  public int getSCROLLType(String dbdriver)
	    {
	        return ResultSet.TYPE_FORWARD_ONLY;
	    }

	    public int getCusorType(String dbdriver)
	    {

	        return ResultSet.CONCUR_READ_ONLY;
	    }
	    
	    public String getDateFormat()
	    {
	        return this.date_format;
	    }
	    public String getJavaDateFormat()
	    {
	        return java_date_format;
	    }
	    
	    public String to_char(String date,String format)
	    {
	    	 SimpleDateFormat f = new SimpleDateFormat(format);
	    	 return f.format(SimpleStringUtil.stringToDate(date,format));
		         
	    }
	    
	    public String to_char(String date)
	    {
	    	return to_char(date,this.FORMART_ALL);
		    
	    }
	    
	    public java.sql.Date getDate(String date,String format) throws ParseException
	    {
	        if(format == null || format.equals(""))
	        {
	            format = this.getJavaDateFormat();
	        }
	        SimpleDateFormat f = new SimpleDateFormat(format);
	        Date _date = f.parse(date);
	        java.sql.Date ret = new java.sql.Date(_date.getTime());
	        return ret;
	    }
	    
	    public Timestamp getTimestamp(String timestamp,String format) throws ParseException
        {
            if(format == null || format.equals(""))
            {
                format = this.getJavaDateFormat();
            }
            SimpleDateFormat f = new SimpleDateFormat(format);
            Date date = f.parse(timestamp);
            Timestamp timestamp_ = new Timestamp(date.getTime());
            return timestamp_;
        }
	    
	    public Object getLONGVARBINARY(CallableStatement cstmt,int parameterIndex) throws SQLException
	    {
	        return    cstmt.getObject(parameterIndex);
	    }
	    
	    public Object getLONGVARCHAR(CallableStatement cstmt,int parameterIndex) throws SQLException
        {
            return    cstmt.getObject(parameterIndex);
        }
	    public Object getLONGVARCHAR(ResultSet res,int parameterIndex) throws SQLException
        {
            return    res.getObject(parameterIndex);
        }
	    
	    public Object getLONGVARCHAR(ResultSet res,String colName) throws SQLException
        {
            return    res.getObject(colName);
        }
	    
	    public Object getLONGVARBINARY(ResultSet res,int parameterIndex) throws SQLException
        {
            return    res.getObject(parameterIndex);
        }
	    
	    public Object getLONGVARBINARY(ResultSet res,String colName) throws SQLException
        {
            return    res.getObject(colName);
        }

		

		/**
		 * @param dbtype the dbtype to set
		 */
		public void setDbtype(String dbtype) {
			this.dbtype = dbtype;
		}

		/**
		 * @return the fORMART_DAY
		 */
		public String getFORMART_DAY() {
			return FORMART_DAY;
		}

		/**
		 * @param fORMARTDAY the fORMART_DAY to set
		 */
		public void setFORMART_DAY(String fORMARTDAY) {
			FORMART_DAY = fORMARTDAY;
		}

		/**
		 * @return the fORMART_YEAR
		 */
		public String getFORMART_YEAR() {
			return FORMART_YEAR;
		}

		/**
		 * @return the fORMART_MONTH
		 */
		public String getFORMART_MONTH() {
			return FORMART_MONTH;
		}

		/**
		 * @return the fORMART_HOUR
		 */
		public String getFORMART_HOUR() {
			return FORMART_HOUR;
		}

		/**
		 * @return the fORMART_MINUTE
		 */
		public String getFORMART_MINUTE() {
			return FORMART_MINUTE;
		}

		/**
		 * @return the fORMART_SECOND
		 */
		public String getFORMART_SECOND() {
			return FORMART_SECOND;
		}

		/**
		 * @return the fORMART_ALL
		 */
		public String getFORMART_ALL() {
			return FORMART_ALL;
		}

		/**
		 * @return the fORMART_HH_MM_SS
		 */
		public String getFORMART_HH_MM_SS() {
			return FORMART_HH_MM_SS;
		}

		/**
		 * @return the fORMART_YEAR_MM_DD
		 */
		public String getFORMART_YEAR_MM_DD() {
			return FORMART_YEAR_MM_DD;
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
					ret.append(token);
					i = true;
				}				
				else
				{
					
					ret.append( " || ").append(token);
				}			
				
			}
			return ret.toString();
		}
		
		public String disableFK(String table,String FKName)
		{
			StringBuilder ret = new StringBuilder();
			ret.append("alter table ").append(table).append(" disable constraint ").append(FKName);
			
			return ret.toString();
			
			
			
		}
		
		public String enableFK(String table,String FKName,String column,String FKTable,String FKColumn)
		{
			StringBuilder ret = new StringBuilder();
			ret.append("alter table ").append(table).append(" enable constraint ").append(FKName);
			return ret.toString();
		}
		
		public  void updateClob(Object content, Connection conn,
				String table, String clobColumn, String keyColumn, String keyValue,
				String dbName) throws SQLException, IOException
		{
	
		}
		
		/**
		 * 
		 * @param instream
		 * @param conn
		 * @param table
		 * @param blobColumn
		 * @param keyColumn
		 * @param keyValue
		 * @param dbName
		 * @throws SQLException
		 * @throws IOException
		 * @deprecated
		 */
		public  void updateBlob(InputStream instream, Connection conn,
				String table, String blobColumn, String keyColumn, String keyValue,
				String dbName) throws SQLException, IOException

		{
			
		}
		
		/**
		 * 根据输入流更新
		 * 
		 * @param value
		 * @param conn
		 * @param table
		 * @param blobColumn
		 * @param keyColumn
		 * @param keyValue
		 * @throws SQLException
		 * @throws IOException
		 * @deprecated
		 */
		public  void updateBlob(byte[] value, Connection conn, String table,
				String blobColumn, String keyColumn, String keyValue, String dbName)
				throws SQLException, IOException

		{
			
		}
		
		
		
		
		public  void updateBLOB(Blob blob,File file) throws SQLException
		{

		}
		
		public  void updateBLOB(Blob blob,InputStream instream) throws SQLException
		{

			
		}
		
		public  void updateCLOB(Clob clob,Object content) throws SQLException
		{

			
		}
		
		
		
		/**
		 * Returns the bytes from a result set
		 * 
		 * @param res
		 *            The ResultSet to read from
		 * @param columnName
		 *            The name of the column to read from
		 * 
		 * @return The byte value from the column
		 */
		public byte[] getBytesFromResultset(ResultSet res, String columnName)
				throws SQLException {
			return res.getBytes(columnName);
		}

		/**
		 * Returns the bytes from a result set
		 * 
		 * @param res
		 *            The ResultSet to read from
		 * @param columnName
		 *            The name of the column to read from
		 * 
		 * @return The byte value from the column
		 */
		public byte[] getBytesFromBlob(Blob blob) throws SQLException {
			// read the bytes from an oracle blob
			// oracle.sql.BLOB blob = ((OracleResultSet) res).getBLOB(columnName);
			byte[] content = new byte[(int) blob.length()];
			content = blob.getBytes(1, (int) blob.length());

			return content;
		}

		/**
		 * Returns the bytes from a result set
		 * 
		 * @param res
		 *            The ResultSet to read from
		 * @param columnName
		 *            The name of the column to read from
		 * 
		 * @return The byte value from the column
		 */
		public byte[] getBytesFromClob(Clob clob) {
			// read the bytes from an oracle blob
			// oracle.sql.CLOB clob = ((OracleResultSet) res).getBLOB(columnName);
			try
			{
				return ValueExchange.getByteArrayFromClob(clob);
			}
			catch (SQLException e)
			{
				e.printStackTrace();
				return null;
			}
		}

		/**
		 * Returns the bytes from a result set
		 * 
		 * @param res
		 *            The ResultSet to read from
		 * @param columnName
		 *            The name of the column to read from
		 * 
		 * @return The byte value from the column
		 */
		public String getStringFromClob(Clob clob) {
			// read the bytes from an oracle blob
			// oracle.sql.CLOB clob = ((OracleResultSet) res).getBLOB(columnName);
			
			try
			{
				return ValueExchange.getStringFromClob(clob);
			}
			catch (SQLException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace(); 
				return null;
			}
		}
		public boolean neadGetGenerateKeys()
		{
			return false;
		}
		  public String getStringPagineSql(String sql)
		  {
			  return sql;
		  }
		  
		  public void queryByNullRowHandler(NullRowHandler handler,String dbname,String pageinestatement,long offset,int pagesize) throws SQLException
		    {
		    	SQLExecutor.queryWithDBNameByNullRowHandler(handler, dbname, pageinestatement,offset + pagesize,offset + 1);
		    }
		  public String getStringPagineSql(String schema,String tablename,String pkname ,String columns)
		    {
		    	StringBuilder sqlbuilder = new StringBuilder();
				 	sqlbuilder.append("select * from (SELECT ");
				 	if(columns != null && ! columns.equals(""))
				 	{
				 		sqlbuilder.append( columns);
				 	}
				 	else
				 		sqlbuilder.append("t.* ");
				 	sqlbuilder.append(",ROW_NUMBER() OVER ( ORDER BY ").append(pkname).append(") rownum__  from   ");
				 	if(schema != null && !schema.equals(""))
				 		sqlbuilder.append(schema).append(".");
				 	sqlbuilder.append( tablename);
				 	if(columns != null && ! columns.equals(""))
				 		sqlbuilder.append( " ) bb where bb.rownum__ <=? and bb.rownum__ >=?");
				 	else
				 		sqlbuilder.append( " t) bb where bb.rownum__ <=? and bb.rownum__ >=?");
		        return sqlbuilder.toString();
		    }
		  
		  public void setObject(PreparedDBUtil dbutil,int i, Object o) throws SQLException
		  {
			  dbutil._setObject(i, o);
//			  if(o == null || o instanceof java.sql.Timestamp)
//			  {
//				  dbutil._setObject(i, o, Param.setObject_int_Object);
//			  }
//			  else if(o instanceof java.sql.Date)
//			  {
//				  o = new java.sql.Timestamp(((java.sql.Date)o).getTime());
//				  dbutil.addParam(i, o, Param.setObject_int_Object);
//			  }
//			  else if(o instanceof java.util.Date)
//			  {
//				  o = new java.sql.Timestamp(((java.util.Date)o).getTime());
//				  dbutil.addParam(i, o, Param.setObject_int_Object);
//			  }
//			  else
//			  {
//				  dbutil.addParam(i, o, Param.setObject_int_Object);
//			  }
			  
		  }
		  
		  public PagineSql getDBPagineSql(String sql, long offset, int maxsize,boolean prepared,String orderby) {
				
			  return new PagineSql(sql,-1L,-1L,offset,maxsize,prepared);
				 
			}
			
			  public String getStringPagineSql(String sql,String orderby)
			  {
				 return sql;
			  }
			  public String getStringPagineSql(String schema,String tablename,String pkname ,String columns,String orderby)
			    {
				  
				  StringBuilder newsql = new StringBuilder();
				  newsql.append("SELECT ");
				 	if(columns != null && ! columns.equals(""))
				 	{
				 		newsql.append( columns);
				 	}
				 	else
				 		newsql.append("* ");
				 	newsql.append(" from   ");
				 	if(schema != null && !schema.equals(""))
				 		newsql.append(schema).append(".");
				 	newsql.append( tablename)
					 ;
					return newsql.toString();
			    	
			    } 
        
}
