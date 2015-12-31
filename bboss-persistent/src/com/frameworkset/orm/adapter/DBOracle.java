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

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

import oracle.jdbc.driver.OracleResultSet;
import oracle.sql.BLOB;
import oracle.sql.CLOB;

import com.frameworkset.common.poolman.NestedSQLException;
import com.frameworkset.common.poolman.SQLExecutor;
import com.frameworkset.common.poolman.handle.NullRowHandler;
import com.frameworkset.common.poolman.security.DBInfoEncrypt;
import com.frameworkset.common.poolman.util.JDBCPoolMetaData;
import com.frameworkset.common.poolman.util.SQLUtil;
import com.frameworkset.orm.adapter.DB.PagineSql;
import com.frameworkset.orm.engine.model.Domain;
import com.frameworkset.orm.engine.model.SchemaType;
import com.frameworkset.orm.platform.PlatformOracleImpl;

/**
 * This code should be used for an Oracle database pool.
 * 
 * @author <a href="mailto:jon@clearink.com">Jon S. Stevens</a>
 * @author <a href="mailto:bmclaugh@algx.net">Brett McLaughlin</a>
 * @author <a href="mailto:bschneider@vecna.com">Bill Schneider</a>
 * @author <a href="mailto:dlr@finemaltcoding.com">Daniel Rall</a>
 * @version $Id: DBOracle.java,v 1.19 2005/01/31 19:43:55 tfischer Exp $
 */
public class DBOracle extends DB
{
    /** date format used in getDateString() */
    public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
 // DD-MM-YYYY HH24:MI:SS old format
//    public static final String db_format = "yyyy-MM-dd HH24:mi:ss";

    /**
     * Empty constructor.
     */
    protected DBOracle()
    {
        this.platform = new PlatformOracleImpl();
        date_format = "yyyy-MM-dd HH24:mi:ss";

    }

    /**
     * This method is used to ignore case.
     * 
     * @param in
     *            The string to transform to upper case.
     * @return The upper case string.
     */
    public String toUpperCase(String in)
    {
        return new StringBuilder("UPPER(").append(in).append(")").toString();
    }

    /**
     * This method is used to ignore case.
     * 
     * @param in
     *            The string whose case to ignore.
     * @return The string in a case that can be ignored.
     */
    public String ignoreCase(String in)
    {
        return new StringBuilder("UPPER(").append(in).append(")").toString();
    }

    
    
    public String to_char(String date,String format)
    {
    	 StringBuilder ret = new StringBuilder();
    	 ret.append("to_char(")
    	 	.append(date)
    	 	.append(",'")
    	 	.append(format != null?format:this.FORMART_ALL)
    	 	.append("')"); 
    	 
    	 return ret.toString();
	         
    }
    
    public String to_char(String date)
    {
    	return to_char(date,this.FORMART_ALL);
	    
    }
    
    
    /**
     * This method is used to format any date string.
     * 
     * @param date
     *            the Date to format
     * @return The date formatted String for Oracle.
     * @deprecated use to_date function.
     */
    public String getDateString(Date date)
    {
    	return to_date( date,this.date_format);
    }
/**
 * @deprecated use to_date function.
 */
    public String getDateString(String date)
    {
    	return to_date( date,date_format);
       
    }
    
    /**
     * This method is used to format any date string.
     * 
     * @param date
     *            the Date to format
     * @return The date formatted String for Oracle.
     * @deprecated use to_date function.
     */
    public String getDateString(Date date,String format)
    {
    	return to_date( date,format);
    }

    /**
     * @deprecated use to_date function.
     */
    public String getDateString(String date,String format)
    {
    	return to_date( date,format);

    }
    
    
    
    /**
     * This method is used to format any date string.
     * 
     * @param date
     *            the Date to format
     * @return The date formatted String for Oracle.
     */
    public String to_date(Date date)
    {
    	return to_date( date,this.date_format);
    }

    public String to_date(String date)
    {
        return to_date( date,this.date_format);
    }
    
    /**
     * This method is used to format any date string.
     * 
     * @param date
     *            the Date to format
     * @return The date formatted String for Oracle.
     */
    public String to_date(Date date,String format)
    {
        if (date == null)
            return null;
        // DD-MM-YYYY HH24:MI:SS
        return "TO_DATE('" + new SimpleDateFormat(DATE_FORMAT).format(date) + "', '"+format+"')";
    }

    public String to_date(String date,String format)
    {
        if (date == null)
            return null;

        return "TO_DATE('" + date
                + "', '"+format+"')";

    }
    
    

    /**
     * @see com.frameworkset.orm.adapter.DB#getIDMethodType()
     */
    public String getIDMethodType()
    {
        return SEQUENCE;
    }

    /**
     * Returns the next key from a sequence. Uses the following implementation:
     * 
     * <blockquote><code><pre>
     * select sequenceName.nextval from dual
     * </pre></code></blockquote>
     * 
     * @param sequenceName
     *            The name of the sequence (should be of type
     *            <code>String</code>).
     * @return SQL to retreive the next database key.
     * @see com.frameworkset.orm.adapter.DB#getIDMethodSQL(Object)
     */
    public String getIDMethodSQL(Object sequenceName)
    {
        return ("select " + sequenceName + ".nextval from dual");
    }

    /**
     * Locks the specified table.
     * 
     * @param con
     *            The JDBC connection to use.
     * @param table
     *            The name of the table to lock.
     * @exception SQLException
     *                No Statement could be created or executed.
     */
    public void lockTable(Connection con, String table) throws SQLException
    {
        Statement statement = con.createStatement();

        StringBuilder stmt = new StringBuilder();
        stmt.append("SELECT next_id FROM ").append(table).append(" FOR UPDATE");

        statement.executeQuery(stmt.toString());
    }

    /**
     * Unlocks the specified table.
     * 
     * @param con
     *            The JDBC connection to use.
     * @param table
     *            The name of the table to unlock.
     * @exception SQLException
     *                No Statement could be created or executed.
     */
    public void unlockTable(Connection con, String table) throws SQLException
    {
        // Tables in Oracle are unlocked when a commit is issued. The
        // user may have issued a commit but do it here to be sure.
        con.commit();
    }

    /**
     * This method is used to check whether the database natively supports
     * limiting the size of the resultset.
     * 
     * @return True.
     */
    public boolean supportsNativeLimit()
    {
        return true;
    }

    /**
     * This method is used to check whether the database supports limiting the
     * size of the resultset.
     * 
     * @return LIMIT_STYLE_ORACLE.
     */
    public int getLimitStyle()
    {
        return DB.LIMIT_STYLE_ORACLE;
    }

    /**
     * This method is for the SqlExpression.quoteAndEscape rules. The rule is,
     * any string in a SqlExpression with a BACKSLASH will either be changed to
     * "\\" or left as "\". SapDB does not need the escape character.
     * 
     * @return false.
     */
    public boolean escapeText()
    {
        return false;
    }

    // public Object getCharValue(ResultSet res, int i, String value)
    // {
    // if(value == null)
    // return null;
    // byte[] bytes = null;
    // try {
    // bytes = res.getBytes(i);
    // int zhcount = countZHWord(bytes);
    //	        
    // if(zhcount > 0)
    // return value.substring(0,value.length() - zhcount);
    // } catch (SQLException e) {
    // // // TODO Auto-generated catch block
    // // e.printStackTrace();
    // }
    // return value;
    //    	
    // }

    public Object getCharValue(ResultSet res, int i, String value)
    {
        if (value == null)
            return null;
        // byte[] bytes = null;
        // try {
        // bytes = res.getBytes(i);
        // int zhcount = countZHWord(bytes);
        //	        
        // if(zhcount > 0)
        // return value.substring(0,value.length() - zhcount);
        // } catch (SQLException e) {
        // // // TODO Auto-generated catch block
        // // e.printStackTrace();
        // }
        return value;

    }

    public String getOROPR()
    {
        return "||";
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

    /**
     * 数据库主键最大值的获取方法
     */
    public String getIDMAXSql(String table_name, String table_id_name, String table_id_prefix, String type)
    {
        // SUBSTR(table_id_name,LENGTH(table_id_prefix))
        String maxSql = "select nvl(max(" + table_id_name + "),0) from " + table_name;
        if (type.equalsIgnoreCase("string") || type.equalsIgnoreCase("java.lang.string"))
        {
            if (table_id_prefix != null && !table_id_prefix.trim().equals(""))
            {
                maxSql = "select nvl(max(TO_NUMBER(SUBSTR(" + table_id_name + ",LENGTH('" + table_id_prefix
                        + "') + 1))),0) from " + table_name;
            }
            else
            {
                maxSql = "select nvl(max(TO_NUMBER(" + table_id_name + ")),0) from " + table_name;
            }
        }

        return maxSql;
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

    /**
     * 获取受限制结果条数的sql语句，默认为mysql语法规则 不同的数据库需要重载本方法
     * 
     * @param selectSql
     *            String
     * @param limit
     *            int
     * @return String
     */
    public String getLimitSelect(String selectSql, int limit)
    {
        // selectSql += " LIMIT " + limit;
        StringBuilder ret = new StringBuilder();
        ret.append("select * from (").append(selectSql).append(") where rownum <=").append(limit);
        return ret.toString();
    }

    /**
     * 获取指定数据的分页数据sql语句
     * 
     * @param sql
     * @return
     */
    public PagineSql getDBPagineSql(String sql, long offset, int maxsize,boolean prepared)
    {
        // StringBuilder ret = new
        // StringBuilder("select ss1.* from (select tt1.*,rownum rowno_ from (")
        // .append(sql)
        // .append(") tt1) ss1 where ss1.rowno_ between ")
        // .append((offset + 1) + "")
        // .append(" and ")
        // .append((offset + maxsize) + "");
//        StringBuilder ret = new StringBuilder("select ss1.* from (select tt1.*,rownum rowno_ from (").append(sql).append(
//                ") tt1 where rownum <= ").append((offset + maxsize)).append(") ss1 where ss1.rowno_ >= ").append(
//                (offset + 1));
//        return ret.toString();
    	StringBuilder ret = null;
    	if(prepared)
    		ret = new StringBuilder().append("select ss1.* from (select tt1.*,rownum rowno_ from (").append(sql).append(
                ") tt1 where rownum <= ?) ss1 where ss1.rowno_ >= ?");
    	else
    		ret = new StringBuilder("select ss1.* from (select tt1.*,rownum rowno_ from (").append(sql).append(
                  ") tt1 where rownum <= ").append((offset + maxsize)).append(") ss1 where ss1.rowno_ >= ").append(
                  (offset + 1));
        return new PagineSql(ret.toString(),offset + maxsize,offset + 1,offset, maxsize, prepared);
    }
    
    public String getStringPagineSql(String sql)
    {
        // StringBuilder ret = new
        // StringBuilder("select ss1.* from (select tt1.*,rownum rowno_ from (")
        // .append(sql)
        // .append(") tt1) ss1 where ss1.rowno_ between ")
        // .append((offset + 1) + "")
        // .append(" and ")
        // .append((offset + maxsize) + "");
//        StringBuilder ret = new StringBuilder("select ss1.* from (select tt1.*,rownum rowno_ from (").append(sql).append(
//                ") tt1 where rownum <= ").append((offset + maxsize)).append(") ss1 where ss1.rowno_ >= ").append(
//                (offset + 1));
//        return ret.toString();
    	StringBuilder ret = new StringBuilder().append("select ss1.* from (select tt1.*,rownum rowno_ from (").append(sql).append(
                ") tt1 where rownum <= ?) ss1 where ss1.rowno_ >= ?");
    	
        return ret.toString();
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
		 		sqlbuilder.append("* ");
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
    
    
    public void queryByNullRowHandler(NullRowHandler handler,String dbname,String pageinestatement,long offset,int pagesize) throws SQLException
    {
    	SQLExecutor.queryWithDBNameByNullRowHandler(handler, dbname, pageinestatement,offset + pagesize,offset + 1);
    }
    public void resetPostion( PreparedStatement statement,int startidx,int endidx,long offset,int maxsize) throws SQLException
    {
    	statement.setLong(startidx, offset + maxsize);
		statement.setLong(endidx, offset + 1);
    }

    /**
     * 获取受限制结果条数的sql语句，要求selectSql的语法，按oracle自定义受限语句语法，例如 SELECT
     * a.cpmc,a.ggxh,a.
     * cjdd,a.ph,a.jgxs,a.dj,a.jldw,a.cd,a.cddm,a.flag,cjrq,a.pricepsj_id
     * ,b.user_name, ROW_NUMBER() OVER ( ORDER BY cjrq ) aa from
     * td_price_pricepsj_jmrygypjg a,td_sm_user b
     * 
     * @param selectSql
     *            String
     * @param limit
     *            int
     * @param rownum
     *            行号的别名
     * @return String
     */
    public String getOracleLimitSelect(String selectSql, int limit, String rownum)
    {
        // selectSql += " LIMIT " + limit;
        StringBuilder ret = new StringBuilder();
        ret.append("select * from (").append(selectSql).append(") where ").append(rownum).append(" <=").append(limit);
        return ret.toString();
    }

    /**
	 * 获取指定数据的分页数据sql语句
	 * @param sql
	 * @return
	 */
	public PagineSql getDBPagineSql(String sql, long offset, int maxsize,boolean prepared,String orderby) {
		
		StringBuilder ret = null;
  	if(prepared)
	        ret = new StringBuilder().append("SELECT *  FROM (SELECT b.*, ROW_NUMBER () OVER (").append(orderby).append(") AS rownums FROM (").append(sql).append(") b) WHERE rownums <= ? and rownums >=?");
  	else
  	{
  		ret = new StringBuilder().append("SELECT *  FROM (SELECT b.*, ROW_NUMBER () OVER (").append(orderby).append(") AS rownums FROM (").append(sql).append(") b) WHERE rownums <= ").append(offset + maxsize).append(" and rownums >=").append(offset + 1);
  	}
      return new PagineSql(ret.toString(),offset + maxsize,offset + 1,offset, maxsize, prepared).setRebuilded(true);
		
////		return new StringBuilder(sql).append(" limit ").append(offset).append(",").append(maxsize).toString();
//		StringBuilder newsql = new StringBuilder();
//		if(prepared)
//		{
//			newsql.append("SELECT t.* FROM (SELECT res.* ,row_number() over(").append(orderby).append(") r FROM (").append(sql)
//			.append(")) t where t.r <= ? and t.r >= ?");
//			 
//			
//			/**
//			 * StringBuilder ret = null;
//  	if(prepared)
//  		ret = new StringBuilder().append("select ss1.* from (select tt1.*,rownum rowno_ from (").append(sql).append(
//              ") tt1 where rownum <= ?) ss1 where ss1.rowno_ >= ?");
//  	else
//  		ret = new StringBuilder("select ss1.* from (select tt1.*,rownum rowno_ from (").append(sql).append(
//                ") tt1 where rownum <= ").append((offset + maxsize)).append(") ss1 where ss1.rowno_ >= ").append(
//                (offset + 1));
//      return new PagineSql(ret.toString(),offset + maxsize,offset + 1,offset, maxsize, prepared);
//			 */
//			return new PagineSql(newsql.toString(),offset + maxsize,offset + 1,offset, maxsize, prepared);
//		}
//		else
//		{
//			newsql.append("SELECT t.* FROM (SELECT res.* ,row_number() over(").append(orderby).append(") r FROM (").append(sql)
//			.append(")) t where t.r <= ").append(offset + maxsize).append(" and t.r >= ").append(offset + 1).append("");
//			return new PagineSql(newsql.toString(),offset + maxsize,offset + 1,offset, maxsize, prepared);
//		}
		 
	}
	
	  public String getStringPagineSql(String sql,String orderby)
	  {
//		  StringBuilder newsql = new StringBuilder();
//		  newsql.append("SELECT t.* FROM (SELECT res.* ,row_number() over(").append(orderby).append(") r FROM (").append(sql)
//			.append(")) t where t.r <= ? and t.r >= ?");
//			return newsql.toString();
		  StringBuilder ret  = new StringBuilder().append("SELECT *  FROM (SELECT b.*, ROW_NUMBER () OVER (").append(orderby).append(") AS rownums FROM (").append(sql).append(") b) WHERE rownums <= ? and rownums >=?");
	    	 
		
	    	return ret.toString();
	  }
	  public String getStringPagineSql(String schema,String tablename,String pkname ,String columns,String orderby)
	    {
		  
//		  StringBuilder newsql = new StringBuilder();
//		  newsql.append("SELECT t.* FROM (SELECT res.* ,row_number() over(").append(orderby).append(") r FROM (").append("SELECT ");
//		 	if(columns != null && ! columns.equals(""))
//		 	{
//		 		newsql.append( columns);
//		 	}
//		 	else
//		 		newsql.append("* ");
//		 	newsql.append(" from   ");
//		 	if(schema != null && !schema.equals(""))
//		 		newsql.append(schema).append(".");
//		 	newsql.append( tablename)
//			.append(")) t where t.r <= ? and t.r >= ?");
//		 	return newsql.toString();
		  StringBuilder newsql  = new StringBuilder().append("SELECT *  FROM (SELECT b.*, ROW_NUMBER () OVER (").append(orderby).append(") AS rownums FROM (").append("SELECT ");
		 	if(columns != null && ! columns.equals(""))
		 	{
		 		newsql.append( columns);
		 	}
		 	else
		 		newsql.append("* ");
		 	newsql.append(" from   ");
		 	if(schema != null && !schema.equals(""))
		 		newsql.append(schema).append(".");
		 	newsql.append( tablename).append(") b) WHERE rownums <= ? and rownums >=?");
		    
		 	 
		    	 
		 	 
	    	return newsql.toString();
			
	    	
	    }

    /**
     * 获取高效的oracle分页语句，sql中已经写好ROW_NUMBER() OVER ( ORDER BY cjrq ) rownum
     * 否则不能调用本方法生成oralce的分页语句
     */
    public PagineSql getOracleDBPagineSql(String sql, long offset, int maxsize, String rownum,boolean prepared)
    {
    	StringBuilder ret = null;
    	if(prepared)
	        ret = new StringBuilder().append("select * from (").append(sql).append(") where ").append(rownum).append(
	                " between ? and ?");
    	else
    	{
    		ret = new StringBuilder().append("select * from (").append(sql).append(") where ").append(rownum).append(
	                " between ").append(offset + 1).append(" and ").append(offset + maxsize);
    	}
        return new PagineSql(ret.toString(),offset + 1,offset + maxsize,offset, maxsize, prepared);
    }

    private static final String columncommentsql = "select c.comments from user_col_comments c where NLS_LOWER(c.table_name)=? and NLS_LOWER(c.column_name)=?";

    private static final String tablecommentsql = "select comments from user_tab_comments where NLS_LOWER(table_name)=? and table_type='TABLE'";

    public String getTableRemarks(Connection con, String tableName, String tableRemark)
    {
        PreparedStatement pstmt = null;

        ResultSet rs = null;
        try
        {
            pstmt = con.prepareStatement(tablecommentsql);
            pstmt.setString(1, tableName.toLowerCase());
            rs = pstmt.executeQuery();
            while (rs.next())
            {
                String remark = rs.getString(1);
                if (remark == null)
                    remark = "";
                // System.out.println(remark);
                return remark;
            }

        }
        catch (SQLException e)
        {

        }
        finally
        {
            if (rs != null)
            {
                try
                {
                    rs.close();
                }
                catch (SQLException e)
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

            if (pstmt != null)
            {
                try
                {
                    pstmt.close();
                }
                catch (SQLException e)
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

        }
        return tableRemark;
    }

    public String getColumnRemarks(Connection con, String tableName, String columnName, String remarks_c)
    {
        PreparedStatement pstmt = null;

        ResultSet rs = null;
        try
        {
            pstmt = con.prepareStatement(columncommentsql);
            pstmt.setString(1, tableName.toLowerCase());
            pstmt.setString(2, columnName.toLowerCase());

            rs = pstmt.executeQuery();
            while (rs.next())
            {
                String remark = rs.getString(1);
                if (remark == null)
                    remark = "";

                return remark;
            }

        }
        catch (SQLException e)
        {

        }
        finally
        {
            if (rs != null)
            {
                try
                {
                    rs.close();
                }
                catch (SQLException e)
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

            if (pstmt != null)
            {
                try
                {
                    pstmt.close();
                }
                catch (SQLException e)
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

        }
        return remarks_c;
    }

    public int getSCROLLType(String dbdriver)
    {
        return ResultSet.TYPE_FORWARD_ONLY;
    }

    public int getCusorType(String dbdriver)
    {

        return ResultSet.CONCUR_READ_ONLY;
    }
//    public String getDateFormat(){
//        return date_format; 
//    }
    
    
    /**
	 * 
	 * @param content
	 * @param conn
	 * @param table
	 * @param clobColumn
	 * @param keyColumn
	 * @param keyValue
	 * @param dbName
	 * @throws SQLException
	 * @throws IOException
	 * @deprecated
	 */
	public  void updateClob(Object content, Connection conn,
			String table, String clobColumn, String keyColumn, String keyValue,
			String dbName) throws SQLException, IOException

	{
		// nullBlob(conn, table,blobColumn, keyColumn,keyValue);
		PreparedStatement stmt = null;
	
		ResultSet rs = null;

		Writer wr = null;
		StringBuilder sqlBuffer = new StringBuilder();
		boolean flag = false;
		
		boolean oldAutoCommit = false;
		try {
		
			
			oldAutoCommit = conn.getAutoCommit();
			if (oldAutoCommit) {
				conn.setAutoCommit(false);
				flag = true;
			}
			

			sqlBuffer.append("select ");

			sqlBuffer.append(clobColumn);

			sqlBuffer.append(" from ");

			sqlBuffer.append(table);

			sqlBuffer.append(" where ");

			sqlBuffer.append(keyColumn);

			sqlBuffer.append("=");

			sqlBuffer.append("?");

			// 注意这里的”for update”

			sqlBuffer.append(" for update nowait");

			stmt = conn.prepareStatement(sqlBuffer.toString());
			SQLUtil.setValue(stmt, 1, table, keyColumn, keyValue, dbName,conn);
			rs = stmt.executeQuery();

			if (!rs.next())

			{
				
				commitBig(oldAutoCommit,conn,flag) ;

				throw new IllegalArgumentException(
						"no record found for keyValue: '" + keyValue + "'");

			}

			oracle.sql.CLOB clob = (oracle.sql.CLOB) rs.getClob(clobColumn);

			if (clob == null) {

				clob = initClob(conn, table, clobColumn, keyColumn, keyValue,
						dbName);
			}

			wr = clob.getCharacterOutputStream();
			if(content instanceof String)
			{
				wr.write((String)content);
			}
			else if(content instanceof File)
			{
				File f = (File)content;
				FileReader reader = new FileReader(f);
			    char[] cbuf = new char[1024];
			    int i = 0;
			    while((i = reader.read(cbuf)) > 0)
			    {
			    	wr.write(cbuf,0,i);
			    }
			}
			else
			{
				String temp = String.valueOf(content);
				wr.write(temp);
			}
			wr.flush();			
			commitBig(oldAutoCommit,conn,flag) ;

		} catch (SQLException e) {
			rollbackBig(oldAutoCommit,conn,flag) ;

			throw e;
		}
		// catch()
		catch (IOException e) {
			rollbackBig(oldAutoCommit,conn,flag) ;
			throw e;
		} catch (Exception e) {
			rollbackBig(oldAutoCommit,conn,flag) ;
			throw new SQLException(e.getMessage());
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
			} catch (Exception e) {
			}

			try {
				if (stmt != null) {
					stmt.close();
				}
			} catch (Exception e) {
			}

			

			try {
				if (conn != null) {
					if (oldAutoCommit)
						conn.setAutoCommit(true);
				}
			} catch (Exception e) {
			}

			try {
				if (wr != null) {
					wr.close();
				}
			} catch (Exception e) {
			}

		}
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
		// nullBlob(conn, table,blobColumn, keyColumn,keyValue);
		PreparedStatement stmt = null;

		ResultSet rs = null;
		
		OutputStream outstream = null;
		StringBuilder sqlBuffer = new StringBuilder();
//		PreparedStatement nextStatement = null;
		boolean oldAutoCommit = false;
		
		boolean flag = false;
		try {
			
			oldAutoCommit = conn.getAutoCommit();
			if (oldAutoCommit) {
				conn.setAutoCommit(false);
				flag = true;
			}
			

			sqlBuffer.append("select ");

			sqlBuffer.append(blobColumn);

			sqlBuffer.append(" from ");

			sqlBuffer.append(table);

			sqlBuffer.append(" where ");

			sqlBuffer.append(keyColumn);

			sqlBuffer.append("=");

			sqlBuffer.append("?");

			// 注意这里的”for update”

			sqlBuffer.append(" for update nowait");

			stmt = conn.prepareStatement(sqlBuffer.toString());
			SQLUtil.setValue(stmt, 1, table, keyColumn, keyValue, dbName,conn);
			rs = stmt.executeQuery();

			if (!rs.next())

			{

				commitBig(oldAutoCommit,conn,flag) ;

				throw new SQLException(

				"no record found for keyValue: '" + keyValue + "'");

			}

			oracle.sql.BLOB blob = (oracle.sql.BLOB) rs.getBlob(blobColumn);
			// ByteArrayInputStream binstream = new
			// ByteArrayInputStream(instream);
			if (blob == null) {
//				nextStatement = conn.prepareStatement("COMMIT");
//				nextStatement.execute();
//				nextStatement.close();
				blob = initBlob(conn, table, blobColumn, keyColumn, keyValue,
						dbName);
			}
			
			outstream = blob.getBinaryOutputStream();
			int bufferSize = blob.getChunkSize();
			byte[] buffer = new byte[bufferSize];

			int bytesRead = -1;

			while ((bytesRead = instream.read(buffer)) > 0) {
				outstream.write(buffer, 0, bytesRead);

			}
			outstream.flush();

			commitBig(oldAutoCommit,conn,flag) ;


		} catch (SQLException e) {


			rollbackBig(oldAutoCommit,conn,flag);


			throw e;
		}
		// catch()
		catch (IOException e) {

			rollbackBig(oldAutoCommit,conn,flag);
			throw new SQLException(e.getMessage());
		} catch (Exception e) {
			rollbackBig(oldAutoCommit,conn,flag);
			throw new SQLException(e.getMessage());
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
			} catch (Exception e) {
			}

			try {
				if (stmt != null) {
					stmt.close();
				}
			} catch (Exception e) {
			}
			

			try {
				if (instream != null) {
					instream.close();
				}
			} catch (Exception e) {
			}

			try {
				if (outstream != null) {
					outstream.close();
				}
			} catch (Exception e) {
			}
			try {
				if (conn != null) {
					if (oldAutoCommit)
						conn.setAutoCommit(true);
				}
			} catch (Exception e) {
			}

		}
			
	}
	
	private static void commitBig(boolean oldAutoCommit,Connection conn,boolean flag) throws SQLException
	{
		if(oldAutoCommit && flag)
		{
			SQLException ee = null;
			try
			{
				conn.commit();
			}
			catch(SQLException e)
			{
				ee = e;
			}
			try
			{
				conn.setAutoCommit(oldAutoCommit);
			}
			catch(SQLException e)
			{
				ee = e;
			}
			if (ee != null)
				throw ee;
			
			
		}
	}
	
	private static void rollbackBig(boolean oldAutoCommit,Connection conn,boolean flag)
	{
		if (oldAutoCommit && flag) {
			SQLException ee = null;
			try
			{
				conn.rollback();
			}
			catch(SQLException e1)
			{
				ee = e1;
			}
			
			try
			{
				conn.setAutoCommit(oldAutoCommit);
			}
			catch(SQLException e1)
			{
				ee = e1;
			}

		}
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
		// nullBlob(conn, table,blobColumn, keyColumn,keyValue);
		PreparedStatement stmt = null;

		ResultSet rs = null;
		ByteArrayInputStream instream = null;
		OutputStream outstream = null;
		StringBuilder sqlBuffer = new StringBuilder();
//		PreparedStatement nextStatement = null;
		boolean oldAutoCommit = false;
		
		boolean flag = false;
		try {
			
			oldAutoCommit = conn.getAutoCommit();
			if (oldAutoCommit) {
				conn.setAutoCommit(false);
				flag = true;
			}
			

			sqlBuffer.append("select ");

			sqlBuffer.append(blobColumn);

			sqlBuffer.append(" from ");

			sqlBuffer.append(table);

			sqlBuffer.append(" where ");

			sqlBuffer.append(keyColumn);

			sqlBuffer.append("=");

			sqlBuffer.append("?");

			// 注意这里的”for update”

			sqlBuffer.append(" for update nowait");

			stmt = conn.prepareStatement(sqlBuffer.toString());
			SQLUtil.setValue(stmt, 1, table, keyColumn, keyValue, dbName,conn);
			rs = stmt.executeQuery();

			if (!rs.next())

			{

			
				commitBig(oldAutoCommit,conn,flag) ;

				throw new SQLException(

				"no record found for keyValue: '" + keyValue + "'");

			}

			oracle.sql.BLOB blob = (oracle.sql.BLOB) rs.getBlob(blobColumn);
			// ByteArrayInputStream binstream = new
			// ByteArrayInputStream(instream);
			if (blob == null) {
//				nextStatement = conn.prepareStatement("COMMIT");
//				nextStatement.execute();
//				nextStatement.close();
				blob = initBlob(conn, table, blobColumn, keyColumn, keyValue,
						dbName);
			}
			instream = new ByteArrayInputStream(value);
			outstream = blob.getBinaryOutputStream();
			int bufferSize = blob.getChunkSize();
			byte[] buffer = new byte[bufferSize];

			int bytesRead = -1;

			while ((bytesRead = instream.read(buffer)) != -1) {
				outstream.write(buffer, 0, bytesRead);

			}
			outstream.flush();
//			if (tx == null) {
			//nextStatement = conn.prepareStatement("COMMIT");
			//nextStatement.execute();
			commitBig(oldAutoCommit,conn,flag) ;
//			}

//			rs.close();
//			stmt.close();
//			if(nextStatement != null)
//				nextStatement.close();
//			instream.close();
//			outstream.close();

		} catch (SQLException e) {
//			if (tx != null) {
//				try {
//					tx.setRollbackOnly();
//				} catch (IllegalStateException e1) {
//					// TODO Auto-generated catch block
//					e1.printStackTrace();
//				} catch (SystemException e1) {
//					// TODO Auto-generated catch block
//					e1.printStackTrace();
//				}
//			} else {

			rollbackBig(oldAutoCommit,conn,flag);

//			}
			throw e;
		}
		// catch()
		catch (IOException e) {
//			if (tx != null) {
//				try {
//					tx.setRollbackOnly();
//				} catch (IllegalStateException e1) {
//					// TODO Auto-generated catch block
//					e1.printStackTrace();
//				} catch (SystemException e1) {
//					// TODO Auto-generated catch block
//					e1.printStackTrace();
//				}
//			} else {
			rollbackBig(oldAutoCommit,conn,flag);

//			}
				throw new SQLException(e.getMessage());
		} catch (Exception e) {
			rollbackBig(oldAutoCommit,conn,flag);
			throw new SQLException(e.getMessage());
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
			} catch (Exception e) {
			}

			try {
				if (stmt != null) {
					stmt.close();
				}
			} catch (Exception e) {
			}

//			try {
//				if (nextStatement != null) {
//					nextStatement.close();
//				}
//			} catch (Exception e) {
//			}

			
				
			

			try {
				if (instream != null) {
					instream.close();
				}
			} catch (Exception e) {
			}

			try {
				if (outstream != null) {
					outstream.close();
				}
			} catch (Exception e) {
			}
			try {
				if (conn != null) {
					if (oldAutoCommit)
						conn.setAutoCommit(true);
				}
			} catch (Exception e) {
			}

		}
	}

	/**
	 * 初始化blob字段
	 * 
	 * @param conn
	 * @param table
	 * @param blobColumn
	 * @param keyColumn
	 * @param keyValue
	 * @throws SQLException
	 * @deprecated
	 */
	private static BLOB initBlob(Connection conn, String table,
			String blobColumn, String keyColumn, String keyValue, String dbName)
			throws SQLException {

		StringBuilder sql = new StringBuilder();
		PreparedStatement pstmt = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			sql.append("update ").append(table).append(" set ").append(
					blobColumn).append("=?").append(" where ").append(
					keyColumn).append("=").append("'").append(keyValue).append(
					"'");
			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setBlob(1, BLOB.empty_lob());
			pstmt.executeUpdate();
			/**
			 * 获取刚刚初始化的字段clob字段
			 */
			StringBuilder sqlBuffer = new StringBuilder();
			sqlBuffer.append("select ");

			sqlBuffer.append(blobColumn);

			sqlBuffer.append(" from ");

			sqlBuffer.append(table);

			sqlBuffer.append(" where ");

			sqlBuffer.append(keyColumn);

			sqlBuffer.append("=");

			sqlBuffer.append("?");

			// 注意这里的”for update”

			sqlBuffer.append("  ");

			stmt = conn.prepareStatement(sqlBuffer.toString());

			SQLUtil.setValue(stmt, 1, table, keyColumn, keyValue, dbName,conn);
			rs = stmt.executeQuery();
			if(rs.next())
			{
				oracle.sql.BLOB blob = (oracle.sql.BLOB) rs.getBlob(blobColumn);
	
				return blob;
			}
			else
			{
				
				throw new SQLException(

						"no record found for keyValue: '" + keyValue + "'");
			}
		} catch (SQLException e) {
			throw e;
		} 
		catch(Exception e)
		{
			e.printStackTrace();
			throw new SQLException(e.getMessage());
		}
		finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					throw e;
				}
			}
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
					throw e;
				}
			}

			if (stmt != null) {

				try {
					stmt.close();
				} catch (SQLException e) {
					throw e;
				}
			}

		}
	}

	/**
	 * 初始化blob字段
	 * 
	 * @param conn
	 * @param table
	 * @param blobColumn
	 * @param keyColumn
	 * @param keyValue
	 * @throws SQLException
	 */
	private  CLOB initClob(Connection conn, String table,
			String clobColumn, String keyColumn, String keyValue, String dbName)
			throws SQLException {
		PreparedStatement pstmt = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			StringBuilder sql = new StringBuilder();
			sql.append("update ").append(table).append(" set ").append(
					clobColumn).append("=?").append(" where ").append(
					keyColumn).append("=").append("'").append(keyValue).append(
					"'");
			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setClob(1, CLOB.empty_lob());

			pstmt.executeUpdate();

			/**
			 * 获取刚刚初始化的字段clob字段
			 */
			StringBuilder sqlBuffer = new StringBuilder();
			sqlBuffer.append("select ");

			sqlBuffer.append(clobColumn);

			sqlBuffer.append(" from ");

			sqlBuffer.append(table);

			sqlBuffer.append(" where ");

			sqlBuffer.append(keyColumn);

			sqlBuffer.append("=");

			sqlBuffer.append("?");

			// 注意这里的”for update”

			sqlBuffer.append("  ");

			stmt = conn.prepareStatement(sqlBuffer.toString());

			SQLUtil.setValue(stmt, 1, table, keyColumn, keyValue, dbName,conn);
			rs = stmt.executeQuery();
			if(rs.next())
			{
				oracle.sql.CLOB clob = (oracle.sql.CLOB) rs.getClob(clobColumn);
	
				return clob;
			}
			else
			{
				throw new SQLException(

						"no record found for keyValue: '" + keyValue + "'");
			}
		} catch (SQLException e) {
			throw e;
		}
		 catch (Exception e) {
				throw new SQLException(e.getMessage());
		}
		 finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					throw e;
				}
			}
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
					throw e;
				}
			}

			if (stmt != null) {

				try {
					stmt.close();
				} catch (SQLException e) {
					throw e;
				}
			}

		}
	}
	
	
	public  void updateBLOB(Blob blob_,File file) throws SQLException
	{
		BLOB blob = (BLOB)blob_;
		if(blob == null)
		{
			return ;
		}
		if(file == null || !file.exists())
		{
			return ;
		}
		InputStream instream = null;
		OutputStream  outstream = null;
		try
		{
			
			outstream = blob.getBinaryOutputStream();
			instream = new java.io.BufferedInputStream(new java.io.FileInputStream(file));
			int bufferSize = blob.getChunkSize();
			byte[] buffer = new byte[bufferSize];
	
			int bytesRead = -1;
	
			while ((bytesRead = instream.read(buffer)) > 0) {
				outstream.write(buffer, 0, bytesRead);
	
			}
			outstream.flush();
		}
		catch(SQLException e)
		{
			throw e;
		}
		catch(Exception e)
		{
			throw new NestedSQLException(e);
		}
		finally
		{
			try {
				if (instream != null) {
					instream.close();
					
				}
			} catch (Exception e) {
			}
			instream = null;

			try {
				if (outstream != null) {
					outstream.close();
					
				}
			} catch (Exception e) {
			}
			outstream = null;
		}
	}
	
	public  void updateBLOB(Blob blob_,InputStream instream) throws SQLException
	{
		BLOB blob = (BLOB)blob_;
		if(blob == null)
		{
			return ;
		}
		if(instream == null )
		{
			return ;
		}
//		InputStream instream = null;
		OutputStream  outstream = null;
		try
		{
			
			outstream = blob.getBinaryOutputStream();
//			instream = new java.io.FileInputStream(file);
			int bufferSize = blob.getChunkSize();
			byte[] buffer = new byte[bufferSize];
	
			int bytesRead = -1;
	
			while ((bytesRead = instream.read(buffer)) > 0) {
				outstream.write(buffer, 0, bytesRead);
	
			}
			outstream.flush();
		}
		catch(SQLException e)
		{
			throw e;
		}
		catch(Exception e)
		{
			throw new NestedSQLException(e);
		}
		finally
		{
			try {
				if (instream != null) {
					instream.close();
					
				}
			} catch (Exception e) {
			}
			instream = null;

			try {
				if (outstream != null) {
					outstream.close();
					
				}
			} catch (Exception e) {
			}
			outstream = null;
			
		}
	}
	
	public  void updateCLOB(Clob clob_,Object content) throws SQLException
	{
		CLOB clob = (CLOB)clob_;
		if(clob == null)
		{
			return ;
		}
		if(content == null )
		{
			return ;
		}
//		InputStream instream = null;
//		OutputStream  outstream = null;
		Reader reader = null;
		Writer wr = null;
		try
		{
			
			wr = clob.getCharacterOutputStream();
			if(content instanceof String)
			{
				reader = new java.io.BufferedReader(new java.io.StringReader((String)content));
//				wr.write((String)content);
			}
			else if(content instanceof File)
			{
				File f = (File)content;
				reader = new java.io.BufferedReader(new FileReader(f));
//				File f = (File)content;
//				FileReader reader = new FileReader(f);
//			    char[] cbuf = new char[1024];
//			    int i = 0;
//			    while((i = reader.read(cbuf)) > 0)
//			    {
//			    	wr.write(cbuf,0,i);
//			    }
			}
			else  if(content instanceof Reader)
			{
				reader = (Reader)content;
//				String temp = String.valueOf(content);
//				wr.write(temp);
			}
			else 
			{
				String s = content.toString();
				reader = new java.io.BufferedReader(new java.io.StringReader(s));
			}
			char[] cbuf = new char[1024];
		    int i = 0;
		    while((i = reader.read(cbuf)) > 0)
		    {
		    	wr.write(cbuf,0,i);
		    }
			wr.flush();			
		}
		catch(SQLException e)
		{
			throw e;
		}
		catch(Exception e)
		{
			throw new NestedSQLException(e);
		}
		finally
		{
			if(reader != null)
				try {
					reader.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			if(wr != null)
				try {
					wr.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			reader = null;
			wr = null;
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
	public byte[] getBytesFromResultset(ResultSet res, String columnName)
			throws SQLException {
		// read the bytes from an oracle blob
		oracle.sql.BLOB blob = ((OracleResultSet) res).getBLOB(columnName);
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
	public byte[] getBytesFromClob(Clob clob_) {
		// read the bytes from an oracle blob
		// oracle.sql.CLOB clob = ((OracleResultSet) res).getBLOB(columnName);
		
		oracle.sql.CLOB clob = (CLOB)clob_; 
		byte[] content = clob.getBytes();

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
	public String getStringFromClob(Clob clob_) {
		// read the bytes from an oracle blob
		// oracle.sql.CLOB clob = ((OracleResultSet) res).getBLOB(columnName);
		
		oracle.sql.CLOB clob = (CLOB)clob_; 
		byte[] content = clob.getBytes();

		String ret = new String(content);
		return ret;
	}

}
