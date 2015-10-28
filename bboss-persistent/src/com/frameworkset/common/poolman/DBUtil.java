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
package com.frameworkset.common.poolman;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;



import org.apache.log4j.Logger;

import com.frameworkset.common.poolman.handle.NullRowHandler;
import com.frameworkset.common.poolman.handle.RowHandler;
import com.frameworkset.common.poolman.handle.ValueExchange;
import com.frameworkset.common.poolman.handle.XMLMark;
import com.frameworkset.common.poolman.sql.PrimaryKey;
import com.frameworkset.common.poolman.sql.PrimaryKeyCacheManager;
import com.frameworkset.common.poolman.sql.UpdateSQL;
import com.frameworkset.common.poolman.util.SQLManager;
import com.frameworkset.common.poolman.util.SQLUtil;
import com.frameworkset.common.poolman.util.StatementParser;
import com.frameworkset.orm.transaction.JDBCTransaction;

/**
 * @author biaoping.yin 2005-3-24 version 1.0
 * 
 * 扩充SQLUtil，实现分页查询的功能,执行数据库批处理操作
 */
public class DBUtil extends SQLUtil implements Serializable {
	/**
	 * 保留链接之前的事务状态： preparedCon batchCon
	 * 
	 */
	protected boolean oldcommited = true;   
	
//	static 
//	{
//		DBUtil dbutil = new DBUtil();
//		dbutil = null;
//	}
//	
	// /**
	// * 存放执行大事务的数据库链接
	// */
	// private static ThreadLocal threadLocal = new ThreadLocal();

	private static Logger log = Logger.getLogger(DBUtil.class);

	protected String oraclerownum;

	/** 缺省的数据库链接池名称 */
	// protected final static String DEFAULT_DBNAME = "oa";
//	/** 执行批处理插入，删除，更新操作标识变量 */
//	protected boolean batchStart = true;

//	/** 执行批处理插入，删除，更新语句句柄 */
//	protected Statement batchStmt;
//
//	/** 执行批处理插入，删除，更新操作数据库连接句柄 */
//	protected Connection batchCon;
	
//	/**标识批处理链接是否使用外部链接,缺省为false*/
//	protected boolean outbatchcon = false;

//	protected String batchDBName = SQLManager.getInstance().getDefaultDBName();
	protected String batchDBName = null;

	/**
	 * 控制数据库查询是否是分块的数据库查询
	 */
	protected boolean isRobustQuery = false;

	/** 每块数据的最大size */
	protected int fetchsize = 0;

	/** 定义每次分块查询的起始地址 */
	protected int fetchoffset = 0;

	/** 记录当前行号 */
	protected int fetcholdrowid = 0;

	/**
	 * 保存分块数据查询的数据库链接池的名称
	 */
//	protected String fetchdbName = SQLManager.getInstance().getDefaultDBName();
	protected String fetchdbName = null;

	/**
	 * 保存分块数据库查询语句
	 */
	protected String fetchsql;

//	/** 存放执行批处理命令后的结果集 */
//	protected List batchResult;
//
//	/** 存放执行批处理插入命令后的表的更新语句集 */
//	protected Set batchUpdateSqls;

	/** 保存每次查询的结果集 */
	protected Record[] allResults;
	
	/**
	 * 批处理sqls语句列表，消除嵌套获取链接和链接泄露的问题
	 * 修改addBatch方法中的处理逻辑，不创建数据库链接，只是将sql语句添加到
	 * batchSQLS变量中，所有的数据库操作统一放到executeBatch方法中执行
	 * 同时扩展executeBatch方法：
	 * executeBatch()
	 * executeBatch(String dbname)
	 * executeBatch(String dbname,java.sql.Connection con)
	 * added by biaoping.yin on 20080715
	 */
	protected List batchSQLS;
	
	

	public DBUtil() {
		super();
	}
	
	/**
	 * 已经废弃
	 * @param con
	 * @deprecated 
	 */
	public void setBatchConnection(Connection con)
	{
//		this.batchCon = con;
//		if(con != null)
//			this.outbatchcon = true;
	}

	/** 分页查询时，保存记录总条数 */
	protected long totalSize = 0;

	

	/** 返回每次查询数据库获取的实际记录条数 */
	public int size() {
		// 如果是robust查询将返回总的记录条数，然后分块返回数据
		return isRobustQuery ? (int)totalSize : size;
	}
	
	/** 返回每次查询数据库获取的实际记录条数 */
	public long longsize() {
		// 如果是robust查询将返回总的记录条数，然后分块返回数据
		return isRobustQuery ? totalSize : size;
	}

	/**
	 * @deprecated
	 * please use method getLongTotalSize()
	 * 获取记录总条数 
	 */
	
	public int getTotalSize() {
		return (int)this.totalSize;
	}
	/** 获取记录总条数 */
	public long getLongTotalSize() {
		return this.totalSize;
	}
	
	

	/**
	 * 判断是否开时进行下一个数据块的获取工作
	 * 
	 * @param rowid
	 *            当前行号
	 * @throws SQLException
	 */
	private void assertLoaded(int rowid) throws SQLException {
		// 如果分块获取数据，判断是否开时进行下一个数据块的获取工作，
		// 如果是则获取，否则不作任何操作
		if (this.isRobustQuery) {
			URLEncoder d;
			// d.encode("");
			// 如果行号未发生变化时直接返回
			if (rowid == fetcholdrowid)
				return;

			// 定义数据块边界
			int bound = this.fetchoffset + fetchsize;
			int newOffset = fetchoffset;
			// 如果结果集之前没有获取时,
			// 如果已经获取过但是又回退到fetchoffset之前的某些记录时
			// 如果需要获取fetchoffset + fetchsize之后的记录时
			// 如果需要获取fetchoffset + fetchsize的记录已经取完时
			// 都需要执行方法executeSelect(fetchdbName,fetchsql,newOffset,fetchsize)从数据库中获取数据

			if (this.allResults == null) {
				newOffset = rowid - rowid % fetchsize;
				// System.out.println("this.allResults == null
				// newOffset:"+newOffset);
			} else if (rowid >= bound) {
				newOffset = rowid - rowid % fetchsize;
				// System.out.println("rowid >= (bound) newOffset:"+newOffset);
			} else if (rowid < fetchoffset) {
				newOffset = rowid - rowid % fetchsize;
				// System.out.println("rowid < fetchoffset
				// newOffset:"+newOffset);
			}
			// else if(rowid == bound && rowid < this.getTotalSize())
			// {
			// newOffset = bound;
			// System.out.println("rowid == bound && rowid < this.getTotalSize()
			// newOffset:"+newOffset);
			// }

			// 如果当前块的数据已经读取完毕并且还有数据块，继续从数据库中获取下一个块数据
			if (newOffset != fetchoffset) {
				this.executeSelect(fetchdbName, fetchsql, newOffset,
						this.fetchsize);
				// 设置下次获取数据的起点
				fetchoffset = newOffset;
			}
			// 如果行号发生变化，记录变化后的行号
			fetcholdrowid = rowid;

		}
	}

	/**
	 * 如果采用分块获取数据模式则需要重新计算行号，否则不需要，将计算好的行号返回
	 * 
	 * @param rowid
	 * @return int 新计算行号
	 */
	private int getTrueRowid(int rowid) {
		if (!isRobustQuery)
			return rowid;
		else
			return rowid - fetchoffset;
	}

	/**
	 * 
	 * @param rowid
	 *            从0开始
	 * @param column
	 *            从0开始
	 * @return float
	 */
	public float getFloat(int rowid, int column) throws SQLException {

//		Object value = this.getObject(rowid, column);
//		if (value != null) {
//			return Float.parseFloat(value.toString());
//		} else
//			return 0.0f;
		inrange(rowid, column);
		return allResults[getTrueRowid(rowid)].getFloat(column);
	}

	/**
	 * 
	 * @param rowid
	 *            从0开始
	 * @param column
	 *            从0开始
	 * @return String
	 */
	public String getValue(int rowid, int column) {
//		if (rowid >= size() || rowid < 0)
//			return "out of row range";
//		if (column >= this.meta.getColumnCounts() || column < 0)
//			return "out of column range";
//		try {
//			Object value = allResults[getTrueRowid(rowid)].getObject(column);
//			if(value == null)
//				return "";
//			else
//				return value.toString();
//		} catch (SQLException e) {
//			e.printStackTrace();
//			return "";
//		}
		try
		{
			inrange(rowid, column);
			return allResults[getTrueRowid(rowid)].getString(column);
		}
		catch(SQLException e)
		{
			e.printStackTrace();
			return "";
		}
	}

	/**
	 * 
	 * @param rowid
	 *            从0开始
	 * @param column
	 *            从0开始
	 * @return String
	 */
	public String getString(int rowid, int column, String defalueValue)
			throws SQLException {

		inrange(rowid, column);
		String value = allResults[getTrueRowid(rowid)].getString(column);

		if(value != null)
			return value;
		return defalueValue;
	}

	/**
	 * 
	 * @param rowid
	 *            从0开始
	 * @param column
	 *            从0开始
	 * @return String
	 * @throws SQLException
	 */
	public String getValue(int rowid, int column, String defaultValue)
			throws SQLException {
		
		inrange(rowid, column);
		String value = allResults[getTrueRowid(rowid)].getString(column);
		return value != null ?value:"";
		
	}
	
	
	
	/**
	 * 
	 * @param rowid
	 *            从0开始
	 * @param column
	 *            从0开始
	 * @return String
	 */
	public String getString(int rowid, int column) throws SQLException {
		inrange(rowid, column);
		return allResults[getTrueRowid(rowid)].getString(column);
	}

	public void getFile(int rowid, String column, File file)
			throws SQLException, IOException {
		inrange(rowid, column);
		allResults[getTrueRowid(rowid)].getFile(column,file);
	}
	
	public void getFile(int rowid, int column, File file)
	throws SQLException, IOException {
		inrange(rowid, column);
		allResults[getTrueRowid(rowid)].getFile(column,file);
	}


	/**
	 * 
	 * @param rowid
	 *            从0开始
	 * @param field
	 * @return String
	 */
	public String getValue(int rowid, String field) throws SQLException {
//		Object value = getObject(rowid, field);
//		if (value != null)
//			return value.toString();
//		else
//			return "";
		this.inrange(rowid, field);
		return this.allResults[this.getTrueRowid(rowid)].getString(field);

	}

	/**
	 * 
	 * @param rowid
	 *            从0开始
	 * @param field
	 * @return String
	 */
	public String getValue(int rowid, String field, String defaultValue)
			throws SQLException {
//		Object value = getObject(rowid, field);
//		if (value != null)
//			return value.toString();
//		else
//			return defaultValue;
		String value = this.getValue(rowid, field);
		if(value != null)
			return field;
		else
			return defaultValue;

	}

	/**
	 * 
	 * @param rowid
	 *            从0开始
	 * @param field
	 * @return int
	 */
	public int getInt(int rowid, String field) throws SQLException {
//		Object value = this.getObject(rowid, field);
//		// System.out.println("value.getClass():"+value.getClass());
//		if (value != null)
//			if (!(value instanceof Object[])) {
//				return Integer.parseInt(value.toString());
//			} else {
//
//				return Integer.parseInt(((Object[]) value)[0].toString());
//			}
//		else
//			return 0;
		inrange(rowid, field);
		return allResults[getTrueRowid(rowid)].getInt(field);

	}

	/**
	 * 
	 * @param rowid
	 *            从0开始
	 * @param column
	 *            start from zero
	 * @return int
	 */
	public int getInt(int rowid, int column) throws SQLException {
//		Object value = this.getObject(rowid, column);
//		// System.out.println("value.getClass():"+value.getClass());
//		if (value != null) {
//			if (!(value instanceof Object[])) {
//				return Integer.parseInt(value.toString());
//			} else {
//
//				return Integer.parseInt(((Object[]) value)[0].toString());
//			}
//
//		} else
//			return 0;
		inrange(rowid, column);
		return allResults[getTrueRowid(rowid)].getInt(column);

	}

	/**
	 * 
	 * @param rowid
	 *            从0开始
	 * @param field
	 * @return float
	 */
	public float getFloat(int rowid, String field) throws SQLException {
//		Object value = this.getObject(rowid, field);
//		if (value != null)
//			if (!(value instanceof Object[])) {
//				return Float.parseFloat(value.toString());
//			} else {
//
//				return Float.parseFloat(((Object[]) value)[0].toString());
//			}
//		else
//			return 0.0f;
		inrange(rowid, field);
		return allResults[getTrueRowid(rowid)].getFloat(field);
	}

	/**
	 * 
	 * @param rowid
	 *            从0开始
	 * @param field
	 * @return double
	 */
	public double getDouble(int rowid, String field) throws SQLException {
//		Object value = this.getObject(rowid, field);
//		if (value != null)
//			if (!(value instanceof Object[])) {
//				return Double.parseDouble(value.toString());
//			} else {
//
//				return Double.parseDouble(((Object[]) value)[0].toString());
//			}
//		else
//			return 0;
		inrange(rowid, field);
		return allResults[getTrueRowid(rowid)].getDouble(field);
	}

	/**
	 * 
	 * @param rowid
	 *            从零开始
	 * @param column
	 *            从零开始
	 * @return double
	 */

	public double getDouble(int rowid, int column) throws SQLException {
//		Object value = this.getObject(rowid, column);
//		if (value != null) {
//			if (!(value instanceof Object[])) {
//				return Double.parseDouble(value.toString());
//			} else {
//
//				return Double.parseDouble(((Object[]) value)[0].toString());
//			}
//		} else
//			return 0;
		inrange(rowid, column);
		return allResults[getTrueRowid(rowid)].getDouble(column);

	}

	/**
	 * 
	 * @param rowid
	 *            从0开始
	 * @param field
	 * @return long
	 */
	public long getLong(int rowid, String field) throws SQLException {
//		Object temp = this.getObject(rowid, field);
//		if (temp == null)
//			return 0l;
//		if (temp instanceof Object[]) {
//			Object[] value = (Object[]) temp;
//
//			if (value != null)
//				return Long.parseLong(value[0].toString());
//			else
//				return 0l;
//		} else {
//
//			return Long.parseLong(temp.toString());
//
//		}
		inrange(rowid, field);
		return allResults[getTrueRowid(rowid)].getLong(field);
	}

	/**
	 * 
	 * @param rowid
	 *            从零开始
	 * @param column
	 *            从零开始
	 * @return long
	 * @throws SQLException
	 */
	public long getLong(int rowid, int column) throws SQLException {
//		Object temp = this.getObject(rowid, column);
//		if (temp == null)
//			return 0l;
//		if (temp instanceof Object[]) {
//			Object[] value = (Object[]) temp;
//
//			if (value != null)
//				return Long.parseLong(value[0].toString());
//			else
//				return 0l;
//		} else {
//
//			return Long.parseLong(temp.toString());
//
//		}
		inrange(rowid, column);
		return allResults[getTrueRowid(rowid)].getLong(column);
	}

	/**
	 * 
	 * @param rowid
	 *            从0开始
	 * @param field
	 * @return byte[]
	 */
	public byte[] getByteArray(int rowid, String field) throws SQLException {
//		Object value = getObject(rowid, field);
//		if (value != null && value instanceof byte[]) {
//			return (byte[]) value;
//		}
//
//		else
//			throw new SQLException("field [" + field + "] classcast error:"
//					+ value.getClass());
		inrange(rowid,field);
		return allResults[getTrueRowid(rowid)].getBytes(field);
	}

	/**
	 * 
	 * @param rowid
	 *            从零开始
	 * @param column
	 *            从零开始
	 * @return byte[]
	 * @throws SQLException
	 */
	public byte[] getByteArray(int rowid, int column) throws SQLException {
		inrange(rowid, column);
		return allResults[getTrueRowid(rowid)].getBytes(column);
//		Object value = this.getObject(rowid, column);
//		if (value != null && value instanceof byte[]) {
//			return (byte[]) value;
//		}
//
//		else
//			throw new SQLException("field index [" + column
//					+ "] classcast error:" + value.getClass());
	}

	/**
	 * @param rowid
	 *            从0开始
	 * @param field
	 * @return String
	 */
	public String getString(int rowid, String field) throws SQLException {
//		Object value = this.getObject(rowid, field);
//		if (value != null) {
////			if (value instanceof byte[]) {
////				return new String(((byte[]) value));
////			} else
////				return value.toString();
//			return ValueExchange.getStringFromObject(value);
//		} else
//			return "";
		inrange(rowid, field);
		String value = allResults[getTrueRowid(rowid)].getString(field);
		return value != null?value:"";
	}

	/**
	 * @param rowid
	 *            从0开始
	 * @param field
	 * @return String
	 */
	public String getString(int rowid, String field, String defaultValue)
			throws SQLException {
		Object value = this.getObject(rowid, field);
		if (value != null) {
//			if (value instanceof byte[]) {
//				return new String(((byte[]) value));
//			} else
//				return value.toString();
			return ValueExchange.getStringFromObject(value);
		}

		else
			return defaultValue;
	}

	/**
	 * 
	 * @param rowid
	 *            从0开始
	 * @param field
	 * @return Object
	 */
	public Object getObject(int rowid, String field) throws SQLException {
		inrange(rowid, field);
		return allResults[getTrueRowid(rowid)].getObject(field);

//		throw new SQLException("获取字段[" + field
//				+ "]的值失败：字段可能出现多次，或者本字段没有包含在对应的sql语句中，请检查你的sql语句是否正确");

	}
	
	protected void inrange(int rowid, int column) throws SQLException
	{
		if (rowid >= size() || rowid < 0)
			throw new SQLException("out of row range:" + rowid);
		if (column >= this.meta.getColumnCounts() || column < 0)
			throw new SQLException("out of column range:" + column);
		try {
			assertLoaded(rowid);
		} catch (SQLException e) {
			throw e;
		}
	}
	protected void inrange(int rowid, String columnName) throws SQLException
	{
//		if (rowid >= size() || rowid < 0)
//			throw new SQLException("out of row range:" + rowid);
//		if (column >= this.meta.getColumnCounts() || column < 0)
//			throw new SQLException("out of column range:" + column);
//		try {
//			assertLoaded(rowid);
//		} catch (SQLException e) {
//			throw e;
//		}
		if (rowid >= size() || rowid < 0)
			throw new SQLException("out of row range: " + rowid);
		if (columnName == null || columnName.trim().equals(""))
			throw new SQLException("field name error:[field=" + columnName + "]");
		try {
			assertLoaded(rowid);
		} catch (SQLException e) {
			throw e;
		}

		if (!check(columnName)) 
			throw new SQLException("Field [" + columnName + "] is not in the query list.");
			
	}
	
	protected void inrange(int rowid) throws SQLException
	{
		if (rowid >= size() || rowid < 0)
			throw new SQLException("out of row range:" + rowid);
		
		try {
			assertLoaded(rowid);
		} catch (SQLException e) {
			throw e;
		}
	}

	/**
	 * 
	 * @param rowid
	 *            从0开始
	 * @param column
	 *            从0开始
	 * @return Object
	 */
	public Object getObject(int rowid, int column) throws SQLException {
		inrange(rowid, column);
		Object object = allResults[getTrueRowid(rowid)].getObject(column);
		return object;
	}

	/**
	 * @param rowid
	 *            从零开始
	 * @param field
	 * @return Date
	 */
	public Date getDate(int rowid, String field) throws SQLException {
		inrange(rowid, field);
		return allResults[getTrueRowid(rowid)].getDate(field);
	}

	/**
	 * 根据行id和列id获取字段值
	 * 
	 * @param rowid
	 *            从0开始
	 * @param column
	 *            从零开始
	 * @return Date
	 */

	public Date getDate(int rowid, int column) throws SQLException {
		inrange(rowid, column);
		return allResults[getTrueRowid(rowid)].getDate(column);
	}

	private boolean check(String field) {
//		String fields[] = this.meta.get_columnLabel_upper();
//		String field_ = field.trim().toUpperCase();
//		for (int i = 0; fields != null && i < fields.length; i++) {
//			if (fields[i].equals(field_))
//				return true;
//		}
		return true;
	}



	/**
	 * 将所有的字段名称转换为大写
	 * 
	 */
//	private void fieldsToUPPERCASE() {
//		if (fields != null) {
//			f_temps = new String[fields.length];
//			for (int i = 0; i < fields.length; i++)
//				f_temps[i] = fields[i].toUpperCase();
//		}
//
//	}


	/**
	 * Executes a statement and returns results in the form of a Hashtable
	 * array. 本方法执行完毕后无需对结果集进行缓冲
	 * 
	 * @param dbname
	 *            数据库连接池名称
	 * @param sql
	 *            数据查询或者更新语句
	 * 
	 * @param goNative
	 *            是否使用原始的数据库api
	 * @param offset
	 *            返回记录的起始地址
	 * @param maxsize
	 *            返回记录条数
	 * @return 结果集
	 * @throws SQLException
	 */


	protected Record[] doJDBC(String dbname, String sql,
			// Connection con,
					boolean goNative, long offset, int maxsize, Connection con_,
					Class objectType,RowHandler rowHandler,int resultType)
					throws SQLException {
		StatementInfo stmtInfo = null;
		try
		{
			stmtInfo = new StatementInfo(dbname, new NewSQLInfo(sql),
			// Connection con,
					goNative, offset, maxsize, isRobotQuery(dbname), con_,oraclerownum);

			return doJDBC(stmtInfo, objectType, rowHandler);
		}
		catch(SQLException e)
		{
			throw e;
		}
		finally
		{
			if(stmtInfo != null)
			{
				stmtInfo.dofinally();
				stmtInfo = null;
			}
		}

	}
	
	protected ResultMap innerExecutePagineJDBC(StatementInfo stmtInfo,
				Class objectType,RowHandler rowhandler,int result_type) throws SQLException
	{
		
		
		ResultMap resultMap = new ResultMap();

		try {
			
			ResultSet res = null;
			Statement s = null;
			Statement s1 = null;
			ResultSet rs = null;
			stmtInfo.init();
			
			/**
			 * 终于解决了 
                            原因是Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY); 
                               这句话错写成了Statement stmt=conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                               com.microsoft.sqlserver.jdbc.SQLServerException: 不支持此游标类型/并发组合。 
			 */
			
//			s = stmtInfo.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
//					ResultSet.CONCUR_UPDATABLE);
			
			s = stmtInfo.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
			                             stmtInfo.getCursorType(stmtInfo.getDbname()));


			// See if this was a select statement
			String count = stmtInfo.countSql();
//			s1 = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
//					ResultSet.CONCUR_UPDATABLE);
			s1 = stmtInfo.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
					stmtInfo.getCursorType(stmtInfo.getDbname()));
			rs = s1.executeQuery(count);
			stmtInfo.addResultSet(rs);
			// log.debug("Get count by sql:" + count);
			if (rs.next()) {
				totalSize = rs.getInt(1);				
			}


			stmtInfo.rebuildOffset(totalSize);

			if (totalSize > 0) {


				String paginesql = stmtInfo.paginesql(false).getSql();
				if(showsql(stmtInfo.getDbname()))
				{
					log.debug("JDBC pageine origine statement:" + stmtInfo.getSql());
					log.debug("JDBC pageine statement:" + paginesql);
				}
//				log.debug("paginesql:" + paginesql);
				s.execute(paginesql);
//				results = new DBHashtable[stmtInfo.getMaxsize()];
				res = s.getResultSet();
				stmtInfo.addResultSet(res);
				stmtInfo.absolute(res);
				stmtInfo.cacheResultSetMetaData( res,true);

				this.meta = stmtInfo.getMeta();
				resultMap = stmtInfo.buildResultMap(res, objectType, 
															 rowhandler, stmtInfo.getMaxsize(),
															 true, result_type);
				if(resultMap != null)
					this.size = resultMap.getSize();

			}
			return resultMap;
			

		} catch (SQLException sqle) {
			try{
				
				log.error(sqle.getMessage(),sqle);
			}
			catch(Exception ei)
			{
				
			}
			if(stmtInfo != null)
				stmtInfo.errorHandle(sqle);
			throw sqle;
		} catch (Exception e) {
			try{
				
				log.error(e.getMessage(),e);
			}
			catch(Exception ei)
			{
				
			}
			if(stmtInfo != null)
				stmtInfo.errorHandle(e);
			throw new NestedSQLException(e.getMessage(),e);
		} finally {
			if(stmtInfo != null)
				stmtInfo.dofinally();
//			stmtInfo = null;

			
		}

//		return results;
	}
	
	

	/**
	 * Executes a statement and returns results in the form of a Hashtable
	 * array. 本方法执行完毕后无需对结果集进行缓冲
	 * 
	 * @param dbname
	 *            数据库连接池名称
	 * @param sql
	 *            数据查询或者更新语句
	 * 
	 * @param goNative
	 *            是否使用原始的数据库api
	 * @param offset
	 *            返回记录的起始地址
	 * @param maxsize
	 *            返回记录条数
	 * @return 结果集
	 * @throws SQLException
	 */
	
	protected Record[] doJDBC(StatementInfo stmtInfo,
			Class objectType,RowHandler rowHandler) throws SQLException {
		ResultMap resultMap = this.innerExecutePagineJDBC(stmtInfo,
														  objectType, 
														  rowHandler, 
														  ResultMap.type_maparray);
		return (Record[])resultMap.getCommonresult();
//		// log.debug("doJDBC sql:" + sql + ",offset=" + offset + ",maxsize="
//		// + maxsize);
////		StatementInfo stmtInfo = null;
//		Record[] results = null;
//		ResultSet res = null;
//		Statement s = null;
//		Statement s1 = null;
//		ResultSet rs = null;
//		
//
//
//		try {
//			
//			stmtInfo.init();
//			
//
//			s = stmtInfo.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
//					ResultSet.CONCUR_UPDATABLE);
//
//
//			// See if this was a select statement
//			String count = stmtInfo.countSql();
////			s1 = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
////					ResultSet.CONCUR_UPDATABLE);
//			s1 = stmtInfo.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
//					ResultSet.CONCUR_UPDATABLE);
//			rs = s1.executeQuery(count);
//			stmtInfo.addResultSet(rs);
//			// log.debug("Get count by sql:" + count);
//			if (rs.next()) {
//				totalSize = rs.getInt(1);				
//			}
//
//
//			stmtInfo.rebuildOffset(totalSize);
//
//			if (totalSize > 0) {
//
//
//				String paginesql = stmtInfo.paginesql();
//				log.debug("paginesql:" + paginesql);
//				s.execute(paginesql);
////				results = new DBHashtable[stmtInfo.getMaxsize()];
//				res = s.getResultSet();
//				stmtInfo.addResultSet(res);
//				stmtInfo.absolute(res);
//				stmtInfo.cacheResultSetMetaData( res);
//
//				this.meta = stmtInfo.getMeta();
//				results = stmtInfo.buildResult(res,stmtInfo.getMaxsize(),true);
//
//			}
//			
//
//		} catch (SQLException sqle) {
//			if(stmtInfo != null)
//				stmtInfo.errorHandle(sqle);
//			throw sqle;
//		} catch (Exception e) {
//			if(stmtInfo != null)
//				stmtInfo.errorHandle(e);
//			throw new NestedSQLException(e.getMessage(),e);
//		} finally {
//			if(stmtInfo != null)
//				stmtInfo.dofinally();
//
//			
//		}
//
//		return results;

	}

	

	
	

	/**
	 * 往dbname对应的数据库中插入记录
	 * 
	 * @param dbname
	 *            数据库链接池名称
	 * @param sql
	 *            插入语句
	 * @return Object 主键
	 * @throws SQLException
	 */
	public Object executeInsert(String dbname, String sql) throws SQLException {

		return executeInsert( dbname,  sql,(Connection )null);

	}
	
	/**
	 * 往dbname对应的数据库中插入记录
	 * 
	 * @param dbname
	 *            数据库链接池名称
	 * @param sql
	 *            插入语句
	 * @return Object 主键
	 * @throws SQLException
	 */
	public Object executeInsert(String dbname, String sql,Connection con) throws SQLException {

		return doJDBCInsert(sql, dbname, false,con);

	}

	public Object executeInsert(String sql) throws SQLException {

		return executeInsert( sql,(Connection)null);
	}
	public Object executeInsert(String sql,Connection con) throws SQLException {

		return executeInsert(null, sql, con);
	}

	/**
	 * 执行删除操作
	 * 
	 * @param sql
	 * @return 删除信息
	 * @throws SQLException
	 */
	public Object executeDelete(String sql) throws SQLException {
		// Connection con =
		// SQLManager.getInstance().requestConnection(DEFAULT_DBNAME);
		return executeDelete(SQLManager.getInstance().getDefaultDBName(), sql);
		// return doJDBCInsert(sql,con,false);
	}

	/**
	 * 执行删除操作
	 * 
	 * @param sql
	 *            删除语句
	 * @param dbName
	 *            数据库连接池名称
	 * @return 删除信息
	 * @throws SQLException
	 */
	public Object executeDelete(String dbName, String sql, Connection con)
			throws SQLException {
		// Connection con =
		// SQLManager.getInstance().requestConnection(DEFAULT_DBNAME);
		return executeSql(dbName, sql, con);
		// return doJDBCInsert(sql,con,false);
	}

	/**
	 * 执行删除操作
	 * 
	 * @param sql
	 *            删除语句
	 * @param dbName
	 *            数据库连接池名称
	 * @return 删除信息
	 * @throws SQLException
	 */
	public Object executeDelete(String dbName, String sql) throws SQLException {
		// Connection con =
		// SQLManager.getInstance().requestConnection(DEFAULT_DBNAME);
		return executeDelete(dbName, sql, (Connection)null);
		// return doJDBCInsert(sql,con,false);
	}

	/**
	 * 执行删除操作
	 * 
	 * @param sql
	 *            删除语句
	 * @param goNative
	 *            是否使用原始数据库连接
	 * @return 删除信息
	 * @throws SQLException
	 */
	public Object executeDelete(String sql, boolean goNative, Connection con)
			throws SQLException {
		// Connection con =
		// SQLManager.getInstance().requestConnection(DEFAULT_DBNAME);
		return executeDelete(SQLManager.getInstance().getDefaultDBName(), sql,
				goNative, con);
		// return doJDBCInsert(sql,con,false);
	}

	/**
	 * 执行删除语句。
	 * 
	 * @param dbName
	 *            数据库链接池名称
	 * @param sql
	 *            数据库删除语句
	 * @param goNative
	 *            是否使用原始的数据库链接
	 * @return 删除信息
	 * @throws SQLException
	 */
	public Object executeDelete(String dbName, String sql, boolean goNative,
			Connection con) throws SQLException {
		// Connection con =
		// SQLManager.getInstance().requestConnection(DEFAULT_DBNAME);
		return doJDBC(dbName, sql, goNative, con);
		// return doJDBCInsert(sql,con,false);
	}

	/**
	 * 优化大数据表数据的查询， 根据fethchsize的大小决定每批数据获取记录的条数 提升数据库查询的效率
	 * 
	 * @param dbName
	 *            数据库连接池的名称
	 * @param sql
	 *            待查询的sql语句
	 * @param fetchsize
	 *            每批数据最大的数据条数
	 * @throws SQLException
	 */
	public void executeSelect(String dbName, String sql, int fetchsize)
			throws SQLException {
		this.isRobustQuery = true;
		this.fetchsize = fetchsize;
		this.fetchdbName = dbName;
		this.fetchsql = sql;
		this.executeSelect(dbName, sql, fetchoffset, fetchsize);

	}

	/**
	 * 优化大数据表数据的查询， 根据fethchsize的大小决定每批数据获取记录的条数 提升数据库查询的效率
	 * 
	 * @param sql
	 *            待查询的sql语句
	 * @param fetchsize
	 *            每批数据最大的数据条数
	 * @throws SQLException
	 */
	public void executeSelect(String sql, int fetchsize) throws SQLException {
		this.executeSelect(null, sql, fetchsize);
	}

	/**
	 * 重置fetch参数
	 * 
	 */
	public void resetFetch() {
		this.isRobustQuery = false;
		this.fetchsize = 0;
		this.fetchdbName = null;
		this.fetchsql = null;
		this.totalSize = 0;
		this.allResults = null;
//		this.f_temps = null;
//		this.fields = null;
		this.size = 0;
		this.fetcholdrowid = 0;
	}

	/**
	 * 重置分页、列表参数
	 * 
	 */
	public void resetPager() {
		this.totalSize = 0;
		this.size = 0;
//		this.f_temps = null;
//		this.fields = null;
		this.allResults = null;
	}

	/**
	 * 重置批处理参数
	 * 
	 */
	public void resetBatch() {
		this.autocommit = true;
		this.batchautocommit = false;
		if(this.batchSQLS != null)
		{
			this.batchSQLS.clear();
			this.batchSQLS = null;
		}
		setBatchDBName(SQLManager.getInstance().getDefaultDBName());
	}

	/**
	 * 执行数据库插入操作，
	 * 
	 * @param dbname
	 * @param sql
	 * 
	 * @param goNative
	 * @return 产生的数据库主键
	 * @throws SQLException
	 */
	public Object doJDBCInsert(String sql_, String dbname_, boolean goNative_,Connection con_)
			throws SQLException {
		StatementInfo stmtInfo = null;

		Statement s = null;

		try {
			stmtInfo =  new StatementInfo(dbname_,
					new NewSQLInfo(sql_),
					goNative_,
					 con_,
					 false);
			stmtInfo.init();			
			boolean autokey = isAutoprimarykey(dbname_);
			s = stmtInfo.createStatement();	
			if(autokey)
			{
				Object[] temp;
				temp = StatementParser.refactorInsertStatement(stmtInfo.getCon(), stmtInfo.getSql(), stmtInfo.getDbname());
			
				PrimaryKey primaryKey = (PrimaryKey) temp[3];
				if (temp[1] != null) {
	
					try {
						String changesqlString = (String) temp[0];
						// String ret = s.executeUpdate(sql) + "";
						if(showsql(dbname_))
						{
							log.debug("JDBC Insert statement:" + stmtInfo.getSql());
						}
						s.executeUpdate(changesqlString);
	
						// return ret;
						if (temp[2] != null && temp[3] != null) {
	
							UpdateSQL updateTableinfo = (UpdateSQL) temp[2];
							execute(stmtInfo.getCon(), updateTableinfo);
						}
						stmtInfo.commit();
	
						return temp[1];
	
					} catch (Exception e) {
						// if(tx != null)
						// tx.setRollbackOnly();
						primaryKey.restoreKey(temp[1]);
						log.error(temp[0], e);
						throw e;
					}
	
				} else {
					int i = s.executeUpdate(stmtInfo.getSql());
					stmtInfo.commit();
					return new Integer(i);
				}
			}
			else
			{
				if(showsql(dbname_))
				{
					log.debug("JDBC Insert statement:" + stmtInfo.getSql());
				}
				int i = s.executeUpdate(stmtInfo.getSql());
				stmtInfo.commit();
				return new Integer(i);
			}
		} catch (SQLException e) {
			try{
				
				log.error(stmtInfo.getSql(), e);
			}
			catch(Exception ei)
			{
				
			}
			if(stmtInfo != null)
				stmtInfo.errorHandle(e);
			// e.printStackTrace();
			
			throw e;
		} catch (Exception e) {
			try{
				
				log.error(stmtInfo.getSql(), e);
			}
			catch(Exception ei)
			{
				
			}
			if(stmtInfo != null)
				stmtInfo.errorHandle(e);
			throw new NestedSQLException(e.getMessage(),e);
		} finally {
			if(stmtInfo != null)
				stmtInfo.dofinally();
			stmtInfo = null;
		}
	}

	/**
	 * 执行数据库查询操作
	 * 
	 * @param sql
	 *            查询语句
	 * @return hash数组，封装查询结果
	 * @throws SQLException
	 * @deprecated
	 */
	public Hashtable[] executeQuery(String sql) throws SQLException {
		return executeQuery(sql, (Connection) null);
	}

	/**
	 * 执行数据库查询操作
	 * 
	 * @param sql
	 *            查询语句
	 * @return hash数组，封装查询结果
	 * @throws SQLException
	 *  @deprecated
	 */
	public Hashtable[] executeQuery(String sql, Connection con)
			throws SQLException {
		return executeSql(SQLManager.getInstance().getDefaultDBName(), sql, con);
	}

	/**
	 * 执行数据库查询操作
	 * 
	 * @param sql
	 *            查询语句
	 * @param fields
	 *            查询字段数组
	 * @throws SQLException
	 *  @deprecated
	 */
	public void executeQuery(String sql, String fields[], Connection con)
			throws SQLException {

		allResults = executeSql(SQLManager.getInstance().getDefaultDBName(),
				sql, con);
		this.size = allResults == null ? 0 : allResults.length;
//		this.fields = fields;
//		fieldsToUPPERCASE();
	}

	/**
	 * 执行数据库查询操作
	 * 
	 * @param sql
	 *            查询语句
	 * @param fields
	 *            查询字段数组
	 * @throws SQLException
	 *  @deprecated
	 */
	public void executeQuery(String sql, String fields[]) throws SQLException {

		executeQuery(sql, fields, (Connection) null);
	}

	/**
	 * 执行数据库查询操作
	 * 
	 * @param sql
	 *            查询语句
	 * @throws SQLException
	 */
	public void executeSelect(String sql) throws SQLException {
		executeSelect(sql, (Connection) null);
	}

	/**
	 * 执行数据库查询操作
	 * 
	 * @param sql
	 *            查询语句
	 * @throws SQLException
	 */
	public void executeSelect(String sql, Connection con) throws SQLException {
		executeSelect(SQLManager.getInstance().getDefaultDBName(), sql, con);
	}

	/**
	 * 执行数据库查询操作，
	 * 
	 * @param sql
	 *            查询语句
	 * @throws SQLException
	 */
	public void executeSelectLimit(String sql, int limit) throws SQLException {
		executeSelectLimit(SQLManager.getInstance().getDefaultDBName(), sql,
				limit);
	}

	/**
	 * 执行oracle特定高效数据库查询操作，本方法利用oracle自身提供的机制，提升oracle的高效取top n条记录查询
	 * 获取高效的oracle查询语句，sql中已经写好ROW_NUMBER() OVER ( ORDER BY cjrq ) rownum
	 * 否则不能调用本方法生成oralce的分页语句,其中rownum就对应了本方法的参数rownum
	 * 
	 * @param sql
	 *            查询语句
	 * @throws SQLException
	 */
	public void executeSelectLimitForOracle(String sql, int limit, String rownum)
			throws SQLException {
		executeSelectLimitForOracle(
				SQLManager.getInstance().getDefaultDBName(), sql, limit, rownum);
	}

	/**
	 * 执行限制数据库结果数的数据库查询操作
	 * 
	 * @param dbName
	 *            数据库链接池名称
	 * @param sql
	 *            查询语句
	 * @param limit
	 *            记录条数
	 * @throws SQLException
	 */
	public void executeSelectLimit(String dbName, String sql, int limit)
			throws SQLException {
		executeSelectLimit(dbName, sql, limit, (Connection) null);

		// this.fields = fields;
		// fieldsToUPPERCASE();
	}

	/**
	 * 执行限制数据库结果数的数据库查询操作
	 * 
	 * @param dbName
	 *            数据库链接池名称
	 * @param sql
	 *            查询语句
	 * @param limit
	 *            记录条数
	 * @throws SQLException
	 */
	public void executeSelectLimit(String dbName, String sql, int limit,
			Connection con) throws SQLException {
		sql = getPool(dbName).getDbAdapter().getLimitSelect(sql, limit);
		allResults = executeSql(dbName, sql, con);
		this.size = allResults == null ? 0 : allResults.length;

		// this.fields = fields;
		// fieldsToUPPERCASE();
	}

	/**
	 * 执行oracle高效特定限制数据库结果数的数据库查询操作
	 * 
	 * @param dbName
	 *            数据库链接池名称
	 * @param sql
	 *            查询语句
	 * @param limit
	 *            记录条数
	 * @throws SQLException
	 */
	public void executeSelectLimitForOracle(String dbName, String sql,
			int limit, String rownum) throws SQLException {

		executeSelectLimitForOracle(dbName, sql, limit, rownum,
				(Connection) null);

		// this.fields = fields;
		// fieldsToUPPERCASE();
	}

	/**
	 * 执行oracle高效特定限制数据库结果数的数据库查询操作
	 * 
	 * @param dbName
	 *            数据库链接池名称
	 * @param sql
	 *            查询语句
	 * @param limit
	 *            记录条数
	 * @throws SQLException
	 */
	public void executeSelectLimitForOracle(String dbName, String sql,
			int limit, String rownum, Connection con) throws SQLException {

		if (rownum == null || rownum.equals(""))
			sql = getPool(dbName).getDbAdapter().getLimitSelect(sql, limit);
		else
			sql = getPool(dbName).getDbAdapter().getOracleLimitSelect(sql,
					limit, rownum);
		allResults = executeSql(dbName, sql, con);
		this.size = allResults == null ? 0 : allResults.length;

		// this.fields = fields;
		// fieldsToUPPERCASE();
	}

	/**
	 * 执行数据库查询操作
	 * 
	 * @param dbName
	 *            数据库链接池名称
	 * @param sql
	 *            查询语句
	 * @throws SQLException
	 */
	public void executeSelect(String dbName, String sql) throws SQLException {

		executeSelect(dbName, sql, (Connection) null);

		// this.fields = fields;
		// fieldsToUPPERCASE();
	}

	/**
	 * 执行数据库查询操作
	 * 
	 * @param dbName
	 *            数据库链接池名称
	 * @param sql
	 *            查询语句
	 * @throws SQLException
	 */
	public void executeSelect(String dbName, String sql, Connection con)
			throws SQLException {

		allResults = executeSql(dbName, sql, con);
		this.size = allResults == null ? 0 : allResults.length;
		
		// this.fields = fields;
		// fieldsToUPPERCASE();
	}
	public void clear()
	{
		allResults = null;
	}

	/**
	 * 执行数据库查询操作
	 * 
	 * @param dbName
	 *            查询的数据库连接池
	 * @param sql
	 *            查询语句
	 * @return hash数组，封装查询结果
	 * @throws SQLException
	 *  @deprecated
	 */
	public Hashtable[] executeQuery(String dbName, String sql)
			throws SQLException {
		return executeQuery(dbName, sql, (Connection) null);
	}

	/**
	 * 执行数据库查询操作
	 * 
	 * @param dbName
	 *            查询的数据库连接池
	 * @param sql
	 *            查询语句
	 * @return hash数组，封装查询结果
	 * @throws SQLException
	 *  @deprecated
	 */
	public Hashtable[] executeQuery(String dbName, String sql, Connection con)
			throws SQLException {
		return executeSql(dbName, sql, con);
	}

	/**
	 * 执行数据库查询操作
	 * 
	 * @param dbName
	 *            查询的数据库连接池
	 * @param sql
	 *            查询语句
	 * @param fields
	 *            查询字段数组
	 * @throws SQLException
	 *  @deprecated
	 */
	public void executeQuery(String dbName, String sql, String fields[])
			throws SQLException {
		executeQuery(dbName, sql, fields, (Connection) null);
	}

	/**
	 * 执行数据库查询操作
	 * 
	 * @param dbName
	 *            查询的数据库连接池
	 * @param sql
	 *            查询语句
	 * @param fields
	 *            查询字段数组
	 * @throws SQLException
	 *  @deprecated
	 */
	public void executeQuery(String dbName, String sql, String fields[],
			Connection con) throws SQLException {
		this.allResults = executeSql(dbName, sql, con);
		this.size = allResults == null ? 0 : allResults.length;
//		this.fields = fields;
//		fieldsToUPPERCASE();
	}

	/**
	 * 执行分页查询操作
	 * 
	 * @param sql
	 *            查询语句
	 * @param offset
	 *            分页参数－－获取记录的起始位置
	 * @param maxsize
	 *            分页参数－－获取最大记录数量
	 * @return hash数组，封装查询结果
	 * @throws SQLException
	 *  @deprecated
	 */
	public Hashtable[] executeQuery(String sql, int offset, int maxsize)
			throws SQLException {
		return executeQuery(sql, offset, maxsize, (Connection) null);
	}

	/**
	 * 执行分页查询操作
	 * 
	 * @param sql
	 *            查询语句
	 * @param offset
	 *            分页参数－－获取记录的起始位置
	 * @param maxsize
	 *            分页参数－－获取最大记录数量
	 * @return hash数组，封装查询结果
	 * @throws SQLException
	 *  @deprecated
	 */
	public Hashtable[] executeQuery(String sql, int offset, int maxsize,
			Connection con) throws SQLException {
		return executeSql(SQLManager.getInstance().getDefaultDBName(), sql,
				offset, maxsize, con,null,null,ResultMap.type_maparray);
	}

	/**
	 * 执行分页查询操作
	 * 
	 * @param sql
	 *            查询语句
	 * @param offset
	 *            分页参数－－获取记录的起始位置
	 * @param maxsize
	 *            分页参数－－获取最大记录数量
	 * @return hash数组，封装查询结果
	 * @throws SQLException
	 *  @deprecated
	 */
	public void executeQuery(String sql, int offset, int maxsize,
			String fields[]) throws SQLException {

		executeQuery(sql, offset, maxsize, fields, (Connection) null);
	}

	/**
	 * 执行分页查询操作
	 * 
	 * @param sql
	 *            查询语句
	 * @param offset
	 *            分页参数－－获取记录的起始位置
	 * @param maxsize
	 *            分页参数－－获取最大记录数量
	 * @return hash数组，封装查询结果
	 * @throws SQLException
	 *  @deprecated
	 */
	public void executeQuery(String sql, int offset, int maxsize,
			String fields[], Connection con) throws SQLException {

		this.allResults = executeSql(SQLManager.getInstance()
				.getDefaultDBName(), sql, offset, maxsize, con,null,null,ResultMap.type_maparray);
		this.size = allResults == null ? 0 : allResults.length;
//		this.fields = fields;
//		fieldsToUPPERCASE();
	}

	/**
	 * 执行分页查询操作
	 * 
	 * @param dbName
	 *            查询的数据库连接池
	 * @param sql
	 *            查询语句
	 * @param offset
	 *            分页参数－－获取记录的起始位置
	 * @param maxsize
	 *            分页参数－－获取最大记录数量
	 * @return hash数组，封装查询结果
	 * @throws SQLException
	 *  @deprecated
	 */
	public Hashtable[] executeQuery(String dbName, String sql, long offset,
			int maxsize) throws SQLException {
		return executeQuery(dbName, sql, offset, maxsize, (Connection) null);
	}

	/**
	 * 执行分页查询操作
	 * 
	 * @param dbName
	 *            查询的数据库连接池
	 * @param sql
	 *            查询语句
	 * @param offset
	 *            分页参数－－获取记录的起始位置
	 * @param maxsize
	 *            分页参数－－获取最大记录数量
	 * @return hash数组，封装查询结果
	 * @throws SQLException
	 *  @deprecated
	 */
	public Hashtable[] executeQuery(String dbName, String sql, long offset,
			int maxsize, Connection con) throws SQLException {
		return executeSql(dbName, sql, offset, maxsize, con,null,null,ResultMap.type_maparray);
	}

	/**
	 * 执行分页查询操作
	 * 
	 * @param dbName
	 *            查询的数据库连接池
	 * @param sql
	 *            查询语句
	 * @param offset
	 *            分页参数－－获取记录的起始位置
	 * @param maxsize
	 *            分页参数－－获取最大记录数量
	 * @return hash数组，封装查询结果
	 * @throws SQLException
	 */
	public void executeSelect(String dbName, String sql, long offset,
			int maxsize) throws SQLException {
		executeSelect(dbName, sql, offset, maxsize, (Connection) null);
	}

	/**
	 * 执行分页查询操作
	 * 
	 * @param dbName
	 *            查询的数据库连接池
	 * @param sql
	 *            查询语句
	 * @param offset
	 *            分页参数－－获取记录的起始位置
	 * @param maxsize
	 *            分页参数－－获取最大记录数量
	 * @return hash数组，封装查询结果
	 * @throws SQLException
	 */
	public void executeSelect(String dbName, String sql, long offset,
			int maxsize, Connection con) throws SQLException {
		this.allResults = executeSql(dbName, sql, offset, maxsize, con,null,null,ResultMap.type_maparray);

		this.size = allResults == null ? 0 : allResults.length;
	}

	/**
	 * 
	 * 执行分页查询操作,
	 * 
	 * @param dbName
	 *            查询的数据库连接池
	 * @param sql
	 *            查询语句
	 * @param offset
	 *            分页参数－－获取记录的起始位置
	 * @param maxsize
	 *            分页参数－－获取最大记录数量
	 * 
	 * 
	 * @param robotquery
	 *            区分是否是
	 * @throws SQLException
	 */
	public void executeSelect(String dbName, String sql, long offset,
			int maxsize, boolean robotquery) throws SQLException {

		executeSelect( dbName,  sql,  offset,
				 maxsize,  robotquery,(Connection )null);
	}
	
	/**
	 * 
	 * 执行分页查询操作,
	 * 
	 * @param dbName
	 *            查询的数据库连接池
	 * @param sql
	 *            查询语句
	 * @param offset
	 *            分页参数－－获取记录的起始位置
	 * @param maxsize
	 *            分页参数－－获取最大记录数量
	 * 
	 * 
	 * @param robotquery
	 *            区分是否是
	 * @throws SQLException
	 */
	public void executeSelect(String dbName, String sql, long offset,
			int maxsize, boolean robotquery,Connection con) throws SQLException {

		this.allResults = executeSql(dbName, sql, offset, maxsize, robotquery,con,null,null,ResultMap.type_maparray);

		this.size = allResults == null ? 0 : allResults.length;
	}

	/**
	 * 在指定dbName的数据库中执行分页查询操作，本方法利用oracle自身提供的分页机制，提升oracle的高效分页查询
	 * 获取高效的oracle分页语句，sql中已经写好ROW_NUMBER() OVER ( ORDER BY cjrq ) rownum
	 * 否则不能调用本方法生成oralce的分页语句,其中rownum就对应了本方法的参数rownum 例如： String sql = "select
	 * name,row_number() over (order by id,name) rownum_ from test"; DBUtil
	 * dbUtil = new DBUtil();
	 * dbUtil.executeSelectForOracle("bspf",sql,offset,maxsize,"rownum_"); .....
	 * dbUtil.size(); dbUtil.getTotalSize();
	 * 
	 * 
	 * @param dbName
	 *            查询的数据库连接池
	 * @param sql
	 *            查询语句
	 * @param offset
	 *            分页参数－－获取记录的起始位置
	 * @param maxsize
	 *            分页参数－－获取最大记录数量
	 * @param rownum
	 *            oracle的行号别名，用于提升oracle的高效分页查询
	 * @return hash数组，封装查询结果
	 * @throws SQLException
	 */
	public void executeSelectForOracle(String dbName, String sql, long offset,
			int maxsize, String rownum) throws SQLException {
		executeSelectForOracle(dbName, sql, offset, maxsize, rownum,
				(Connection) null);

	}

	/**
	 * 在指定dbName的数据库中执行分页查询操作，本方法利用oracle自身提供的分页机制，提升oracle的高效分页查询
	 * 获取高效的oracle分页语句，sql中已经写好ROW_NUMBER() OVER ( ORDER BY cjrq ) rownum
	 * 否则不能调用本方法生成oralce的分页语句,其中rownum就对应了本方法的参数rownum 例如： String sql = "select
	 * name,row_number() over (order by id,name) rownum_ from test"; DBUtil
	 * dbUtil = new DBUtil();
	 * dbUtil.executeSelectForOracle("bspf",sql,offset,maxsize,"rownum_"); .....
	 * dbUtil.size(); dbUtil.getTotalSize();
	 * 
	 * 
	 * @param dbName
	 *            查询的数据库连接池
	 * @param sql
	 *            查询语句
	 * @param offset
	 *            分页参数－－获取记录的起始位置
	 * @param maxsize
	 *            分页参数－－获取最大记录数量
	 * @param rownum
	 *            oracle的行号别名，用于提升oracle的高效分页查询
	 * @return hash数组，封装查询结果
	 * @throws SQLException
	 */
	public void executeSelectForOracle(String dbName, String sql, long offset,
			int maxsize, String rownum, Connection con) throws SQLException {
		this.oraclerownum = rownum;
		this.allResults = executeSql(dbName, sql, offset, maxsize, con,null,null,ResultMap.type_maparray);

		this.size = allResults == null ? 0 : allResults.length;
		oraclerownum = null;

	}

	/**
	 * 在缺省的数据库中执行分页查询操作，本方法利用oracle自身提供的分页机制，提升oracle的高效分页查询
	 * 获取高效的oracle分页语句，sql中已经写好ROW_NUMBER() OVER ( ORDER BY cjrq ) rownum
	 * 否则不能调用本方法生成oralce的分页语句,其中rownum就对应了本方法的参数rownum
	 * 
	 * @param sql
	 *            查询语句
	 * @param offset
	 *            分页参数－－获取记录的起始位置
	 * @param maxsize
	 *            分页参数－－获取最大记录数量
	 * @param rownum
	 *            oracle的行号别名，用于提升oracle的高效分页查询
	 * @return hash数组，封装查询结果
	 * @throws SQLException
	 */
	public void executeSelectForOracle(String sql, long offset, int maxsize,
			String rownum) throws SQLException {
		executeSelectForOracle(null, sql, offset, maxsize, rownum);

	}

	/**
	 * 执行分页查询操作
	 * 
	 * @param sql
	 *            查询语句
	 * @param offset
	 *            分页参数－－获取记录的起始位置
	 * @param maxsize
	 *            分页参数－－获取最大记录数量
	 * @return hash数组，封装查询结果
	 * @throws SQLException
	 */
	public void executeSelect(String sql, long offset, int maxsize)
			throws SQLException {
		executeSelect(null, sql, offset, maxsize);
	}

	/**
	 * 执行分页查询操作
	 * 
	 * @param dbName
	 *            查询的数据库连接池
	 * @param sql
	 *            查询语句
	 * @param offset
	 *            分页参数－－获取记录的起始位置
	 * @param maxsize
	 *            分页参数－－获取最大记录数量
	 * @return hash数组，封装查询结果
	 * @throws SQLException
	 *  @deprecated
	 */
	public void executeQuery(String dbName, String sql, long offset,
			int maxsize, String fields[]) throws SQLException {
		executeQuery(dbName, sql, offset, maxsize, fields, (Connection) null);
	}

	/**
	 * 执行分页查询操作
	 * 
	 * @param dbName
	 *            查询的数据库连接池
	 * @param sql
	 *            查询语句
	 * @param offset
	 *            分页参数－－获取记录的起始位置
	 * @param maxsize
	 *            分页参数－－获取最大记录数量
	 * @return hash数组，封装查询结果
	 * @throws SQLException
	 *  @deprecated
	 */
	public void executeQuery(String dbName, String sql, long offset,
			int maxsize, String fields[], Connection con) throws SQLException {
		this.allResults = executeSql(dbName, sql, offset, maxsize, con,null,null,ResultMap.type_maparray);

		this.size = allResults == null ? 0 : allResults.length;
//		this.fields = fields;
//		fieldsToUPPERCASE();
	}

	

	public static void closeConection(Connection connection)
			throws SQLException {
		closeConection(null, connection);

	}

	public static void closeConection(String dbName, Connection connection)
			throws SQLException {
		JDBCTransaction tx = getTransaction();
		if (tx == null) {
			SQLManager datab = getSQLManager();
			datab.returnConnection(dbName, connection);
		} else {
			// try {
			// return tx.getConnection(dbName);
			// } catch (TransactionException e) {
			//				
			// throw new SQLException(e.getMessage());
			// }
		}
	}

	public static void closeResources(Statement stmt, ResultSet rs)
			throws SQLException {
		// SQLManager datab = getSQLManager();
		SQLManager.closeResources(stmt, rs);
	}

	/**
	 * 执行数据库的更新语句
	 * 
	 * @param updateSql
	 *            update语句
	 * @return Hashtable[] 包含更新结果信息
	 * @throws SQLException
	 */
	public Hashtable[] executeUpdate(String updateSql) throws SQLException {
		return executeSql(updateSql);
	}

	/**
	 * 执行数据库的更新语句
	 * 
	 * @param updateSql
	 *            update语句
	 * @param dbName
	 *            数据库名称
	 * @return Hashtable[] 包含更新结果信息
	 * @throws SQLException
	 */
	public Hashtable[] executeUpdate(String dbName, String updateSql)
			throws SQLException {
		return executeUpdate(dbName, updateSql, (Connection) null);
	}

	/**
	 * 执行数据库的更新语句
	 * 
	 * @param updateSql
	 *            update语句
	 * @param dbName
	 *            数据库名称
	 * @return Hashtable[] 包含更新结果信息
	 * @throws SQLException
	 */
	public Hashtable[] executeUpdate(String dbName, String updateSql,
			Connection con) throws SQLException {
		return this.executeSql(dbName, updateSql, con);
	}

	/**
	 * Begins the actual database operation by preparing resources. It calls
	 * doJDBC() to perform the actual operation.
	 * 
	 * @param dbname
	 *            查询的数据库连接池
	 * @param sql
	 *            查询语句
	 * @param offset
	 *            分页参数－－获取记录的起始位置
	 * @param maxsize
	 *            分页参数－－获取最大记录数量
	 * @return hash数组，封装查询结果
	 * @throws SQLException
	 */

	protected Record[] executeSql(String dbname, String sql, long offset,
			int maxsize, Connection con,Class objectType,RowHandler rowHandler,int resultType) throws SQLException {


		return executeSql(dbname, sql, offset,
				maxsize, isRobotQuery(dbname), con, objectType, rowHandler, resultType);

	}

	/**
	 * Begins the actual database operation by preparing resources. It calls
	 * doJDBC() to perform the actual operation.
	 * 
	 * @param dbname
	 *            查询的数据库连接池
	 * @param sql
	 *            查询语句
	 * @param offset
	 *            分页参数－－获取记录的起始位置
	 * @param maxsize
	 *            分页参数－－获取最大记录数量
	 * @return hash数组，封装查询结果
	 * @throws SQLException
	 */

	protected Record[] executeSql(String dbname, String sql, long offset,
			int maxsize, boolean robotquery,Connection con,Class objectType,RowHandler rowHandler,int resultType) throws SQLException {

		Record[] hashResults = null;
		/**
		 * 如果是分块处理,那么重置totalSize
		 */
		if (isRobustQuery) {
			totalSize = 0;
		}

		
		StatementInfo stmtInfo = null;
		try {
			
			stmtInfo = new StatementInfo(dbname, new NewSQLInfo(sql),
					// Connection con,
							false, offset, maxsize, robotquery, con,oraclerownum);
			hashResults = doJDBC(stmtInfo, objectType, rowHandler);
		} catch (SQLException e) {
			
			throw e;
		} catch (Exception e) {
			
			throw new NestedSQLException(e);
		} finally {
			stmtInfo = null;
		}

		return hashResults;

	}
	
	/**
	 * 
	 * @param sqls
	 */
	public void setBatchSQLS(List sqls)
	{
		this.batchSQLS = sqls;
	}
	
	
	/**
	 * 添加批处理sql语句
	 * 
	 * @param sql
	 * @throws SQLException
	 */
	public void addBatch(String sql) throws SQLException {
		if(this.batchSQLS == null)
			this.batchSQLS = new ArrayList();
		if(sql == null || sql.equals(""))
		{
			this.batchSQLS.clear();
			throw new SQLException("Add batch SQL failed: sql is [" + sql + "]");
		}
		this.batchSQLS.add(sql);
	}



	/**
	 * 更新一批表的主键信息
	 * 
	 * @param batchUpdateSqls
	 * @throws SQLException
	 */
	private void updateTableInfo(Set batchUpdateSqls,Connection batchCon) throws SQLException {
		if (batchUpdateSqls == null)
			return;
		// DBUtil dbUtil = new DBUtil();
		try {
			// // 设置数据库操作的提交模式
			// dbUtil.setAutoCommit(isAutoCommit());

			for (Iterator i = batchUpdateSqls.iterator(); i.hasNext();) {
				execute(batchCon, (UpdateSQL) i.next());
				// dbUtil.addBatch(((UpdateSQL) i.next()).getUpdateSql());
			}

			// dbUtil.executeBatch();
		} catch (SQLException e) {

			throw e;
		
		} catch (Exception e) {

			throw new SQLException(e.getMessage());
		}

	}


	/**
	 * 回滚事务
	 */
	public void rollbackTransaction() {

	}
	/**
	 * 执行批处理命令，执行批处理命令之前可以指定特定的数据库连接池的名称
	 * 
	 * @return 如果是插入批处理那么返回所有产生的主键
	 * @throws Exception
	 */
	public Object[] executeBatch() throws SQLException {
		return this.executeBatch((String)null);
	}
	
	
	/**
	 * 执行批处理命令，执行批处理命令之前可以指定特定的数据库连接池的名称
	 * 
	 * @return 如果是插入批处理那么返回所有产生的主键
	 * @throws Exception
	 */
	public Object[] executeBatch(Connection con) throws SQLException {
		return this.executeBatch(null,con);
	}
	
	/**
	 * 在特定的数据库上面执行批处理操作
	 * @param dbname
	 * @return
	 * @throws Exception
	 */
	public Object[] executeBatch(String dbname) throws SQLException {
		return executeBatch(dbname,null);
	}
	
	
	/**
	 * 执行批处理命令，执行批处理命令之前可以指定特定的数据库连接池的名称
	 * 
	 * @return 如果是插入批处理那么返回所有产生的主键
	 * @throws Exception
	 */
	public Object[] executeBatch(boolean needtransaction) throws SQLException {
		return this.executeBatch(null,needtransaction);
	}
	
	/**
	 * 在特定的数据库上面执行批处理操作
	 * @param dbname
	 * @return
	 * @throws Exception
	 */
	public Object[] executeBatch(String dbname,boolean needtransaction) throws SQLException {
		return executeBatch(dbname,null,needtransaction);
	}
	
	
	/**
	 * 在特定的数据库和链接上执行数据库批处理操作
	 * @param dbname
	 * @param con
	 * @return
	 * @throws Exception
	 */
	protected Object[] executeBatch(String dbname,Connection con,boolean needtransaction ) throws SQLException {
		try
		{
			return this.executeBatch(this.batchSQLS,
									 dbname == null?batchDBName :dbname,
									 con,
									 needtransaction);
		}
		finally
		{
			if(this.batchSQLS != null)
				this.batchSQLS.clear();
			
			setBatchDBName(SQLManager.getInstance().getDefaultDBName());
		}		
	}
	
	/**
	 * 判断指定的数据库是否启用了自动生成数据库主键的模式
	 * 
	 * @return true 标识启用
	 *         false 标识不启用
	 */
	public static boolean isAutoprimarykey(String dbname)
	{
		boolean autokey = getSQLManager().getPool(dbname).isAutoprimarykey();
		return autokey;
		
	}
	/**
	 * 在特定的数据库和链接上执行数据库批处理操作，
	 * @param batchSQLS 批处理的sql语句集
	 * @param dbname dbname == null?batchDBName:dbname 数据库逻辑库名称
	 * @param con 外部传入的数据库链接，如果为空则重新向链接池申请链接
	 *        使用外部链接时，本方法不做任何事务性的操作，如果出现数据库的异常，直接抛出这个异常。
	 * @param needtransaction 标识批处理操作是否采用手动控制的事务，true为需要事务，只有所有的sql语句都执行成功才提交，否则
	 * 		  全部回滚
	 *        false为不需要事务，按顺序执行批处理操作，直到全部执行完或者碰到数据库异常后终止
	 *        需要注意的是如果在该方法是在事务上下文环境中执行，则needtransaction参数将被忽略
	 * @return
	 * @throws SQLException
	 */
	protected Object[] executeBatch(List batchSQLS,
								 String dbname_,
								 Connection con_,
								 boolean needtransaction_ ) throws SQLException {
		if (batchSQLS == null || batchSQLS.size() == 0) {
			System.out.println("没有要处理的批处理命令行！");
			return null;
		}
		StatementInfo stmtInfo = null;
		Object[] ret_keys = null;
		// 如果批处理指令执行的数据库插入操作，保存该插入操作生成的主键值
		List batchResult = new ArrayList();
		// 如果批处理指令执行的数据库插入操作，保存该插入操作执行后需要更新表的主键信息的语句
		Set batchUpdateSqls = new TreeSet();

		Statement batchStmt = null;
		if(dbname_ == null)
			dbname_ = SQLManager.getInstance().getDefaultDBName();
		try {	
			stmtInfo = new StatementInfo(dbname_,
					null,
					false,
					 con_,
					 needtransaction_);
			stmtInfo.init();

			// 初始化批处理数据库执行语句
			batchStmt = stmtInfo.createStatement();
			boolean autokey = isAutoprimarykey(dbname_);
			for(int i = 0; i < batchSQLS.size(); i ++)
			{
				String sql = (String)batchSQLS.get(i);
				/**
				 * must be removed.
				 */
				sql = DBUtil.getInterceptorInf(dbname_).convertSQL(sql, DBUtil.getDBAdapter(dbname_).getDBTYPE(), dbname_);
				if(autokey)
				{
					Object[] objs = StatementParser.refactorInsertStatement(
							stmtInfo.getCon(), sql, stmtInfo.getDbname());
					/**
					 * ret[0] 存放insert语句 ret[1] 存放新的主键值 ret[2] 更新表tableinfo中插入表对应主键值语句
					 * ret[3] PrimaryKey对象
					 */
		
					// 重新设置处理后的sql语句
					sql = (String) objs[0];
					if(showsql(stmtInfo.getDbname()))
					{
						log.debug("Execute JDBC batch statement:"+sql);
					}
//					log.info("Add Batch Statement:" + sql);
		
					// 执行statement的添加批处理命令
		
					batchStmt.addBatch(sql);
					// 如果sql为insert语句并且有新的主键值生成，则保存该主键值
					if (objs[1] != null ) {
						batchResult.add(objs[1]);
					}
					if (objs[2] != null) {
						batchUpdateSqls.add(objs[2]);
					}
				}
				else
				{
//					sql = (String) objs[0];
//					log.info("Add Batch Statement:" + sql);
					if(showsql(stmtInfo.getDbname()))
					{
						log.debug("Execute JDBC batch statement:"+sql);
					}
					// 执行statement的添加批处理命令		
					batchStmt.addBatch(sql);
				}
			}	
			
			
			int[] ret = batchStmt.executeBatch();
			// batchUpdateSqls
			if(autokey)
			{
				if (batchUpdateSqls.size() > 0)
					updateTableInfo(batchUpdateSqls,stmtInfo.getCon());
				// 如果执行的是数据库插入操作，而且自动生成数据库表的主键，则返回所有的插入操作的主键
	
				if (batchResult != null && batchResult.size() > 0) {
					ret_keys = new Object[batchResult.size()];
					for (int i = 0; i < batchResult.size(); i++)
						ret_keys[i] = batchResult.get(i);
					// System.arraycopy(null,0,null,1,0);
				}
			}
			else
			{
				if(ret != null )
				{
					ret_keys = new Object[ret.length];
					for(int i = 0;  i < ret.length; i ++)
					{
						ret_keys[i] = new Integer(ret[i]);
					}
				}
			}

			// 如果是手动提交数据库事务模式，所有的操作完成后调用batchCon提交方法提交事务

			stmtInfo.commit();
		} 
		catch(java.sql.BatchUpdateException e)
		{
			try{
				
				log.error("success batch update statements:" + e.getUpdateCounts(),e);
			}
			catch(Exception ei)
			{
				
			}
			
			if(stmtInfo != null)
				stmtInfo.errorHandle(e);
			
			throw e;
		}
		catch (SQLException e) {
			try{
				
				log.error(e.getMessage(),e);
			}
			catch(Exception ei)
			{
				
			}
			
			if(stmtInfo != null)
				stmtInfo.errorHandle(e);
			
			throw e;
		
			// return ret;
		} catch (Exception e) {
			try{
				
				log.error(e.getMessage(),e);
			}
			catch(Exception ei)
			{
				
			}
			if(stmtInfo != null)
				stmtInfo.errorHandle(e);			
//			log.error(e.getMessage(),e);
			throw new NestedSQLException(e.getMessage(),e);
			
					
			
		} finally {
			if(stmtInfo != null)
				stmtInfo.dofinally();
			stmtInfo = null;
			// 重置批处理执行标识
			if(batchSQLS != null)
				batchSQLS.clear();
			if (batchResult != null) {
				batchResult.clear();
				batchResult = null;
				batchUpdateSqls.clear();
				batchUpdateSqls = null;
			}

			setBatchDBName(SQLManager.getInstance().getDefaultDBName());
		}
		return ret_keys;
	}
	
	/**
	 * 在特定的数据库和链接上执行数据库批处理操作
	 * @param dbname
	 * @param con
	 * @return
	 * @throws Exception
	 */
	public Object[] executeBatch(String dbname,Connection con ) throws SQLException {
		try
		{
			return this.executeBatch(batchSQLS, dbname == null?batchDBName :dbname, con, !isBatchAutoCommit());
		}
		finally
		{
			if(this.batchSQLS != null)
				this.batchSQLS.clear();
			this.batchautocommit = false;
			this.autocommit = true;
			setBatchDBName(SQLManager.getInstance().getDefaultDBName());
		}
	}
	
	

	/**
	 * @return Returns the batchDBName.
	 */
	public String getBatchDBName() {
		return batchDBName;
	}

	/**
	 * @param batchDBName
	 *            The batchDBName to set.
	 */
	public void setBatchDBName(String batchDBName) {
		this.batchDBName = batchDBName;
	}

	// ======================================================================
	// Methods for accessing results by column index
	// ======================================================================

	/**
	 * Retrieves the value of the designated column in the current row of this
	 * <code>ResultSet</code> object as a <code>boolean</code> in the Java
	 * programming language.
	 * 
	 * @param rowid
	 *            the first row is 0, the second is 1
	 * @param columnIndex
	 *            the first column is 0, the second is 1, ...
	 * @return the column value; if the value is SQL <code>NULL</code>, the
	 *         value returned is <code>false</code>
	 * @exception SQLException
	 *                if a database access error occurs
	 */
	public boolean getBoolean(int rowid, int columnIndex) throws SQLException {
		inrange(rowid, columnIndex);
		return allResults[getTrueRowid(rowid)].getBoolean(columnIndex);
//		Object value = this.getObject(rowid, columnIndex);
//		
//		if (value != null)
//			return Boolean.getBoolean(value.toString());
//		else
//			return false;
	}

	/**
	 * Retrieves the value of the designated column in the current row of this
	 * <code>ResultSet</code> object as a <code>byte</code> in the Java
	 * programming language.
	 * 
	 * @param rowid
	 *            the first row is 0, the second is 1
	 * @param columnIndex
	 *            the first column is 0, the second is 1, ...
	 * @return the column value; if the value is SQL <code>NULL</code>, the
	 *         value returned is <code>0</code>
	 * @exception SQLException
	 *                if a database access error occurs
	 */
	public byte getByte(int rowid, int columnIndex) throws SQLException {
//		Object value = this.getObject(rowid, columnIndex);
//		try
//		{
//			if(value != null)
//			{
//				Byte byte_ = (Byte)value;
//				return byte_.byteValue();
//			}
//			else
//				return 1;
//		}
//		catch(Exception e)
//		{
//			try
//			{
//				if (value != null)
//					return Byte.parseByte(value.toString());
//				else
//					return 1;
//			}
//			catch(Exception ie)
//			{
//				throw new NestedSQLException(ie);
//				
//			}
////			throw new NestedSQLException(e); 
//		}
		
		inrange(rowid, columnIndex);
		return allResults[getTrueRowid(rowid)].getByte(columnIndex);
	}

	/**
	 * Retrieves the value of the designated column in the current row of this
	 * <code>ResultSet</code> object as a <code>short</code> in the Java
	 * programming language.
	 * 
	 * @param rowid
	 *            the first row is 0, the second is 1
	 * @param columnIndex
	 *            the first column is 0, the second is 1, ...
	 * @return the column value; if the value is SQL <code>NULL</code>, the
	 *         value returned is <code>0</code>
	 * @exception SQLException
	 *                if a database access error occurs
	 */
	public short getShort(int rowid, int columnIndex) throws SQLException {
//		Object value = this.getObject(rowid, columnIndex);
//		if (value != null)
//			return Short.parseShort(value.toString());
//		else
//			return 0;
		inrange(rowid, columnIndex);
		return allResults[getTrueRowid(rowid)].getShort(columnIndex);
	}

	/**
	 * Retrieves the value of the designated column in the current row of this
	 * <code>ResultSet</code> object as a <code>java.sql.Time</code> object
	 * in the Java programming language.
	 * 
	 * @param rowid
	 *            the first row is 0, the second is 1
	 * @param columnIndex
	 *            the first column is 0, the second is 1, ...
	 * @return the column value; if the value is SQL <code>NULL</code>, the
	 *         value returned is <code>null</code>
	 * @exception SQLException
	 *                if a database access error occurs
	 */
	public java.sql.Time getTime(int rowid, int columnIndex)
			throws SQLException {
		inrange(rowid, columnIndex);
		return allResults[getTrueRowid(rowid)].getTime(columnIndex);
		
//		return (java.sql.Time) this.getObject(rowid, columnIndex);

	}

	/**
	 * Retrieves the value of the designated column in the current row of this
	 * <code>ResultSet</code> object as a <code>java.sql.Timestamp</code>
	 * object in the Java programming language.
	 * 
	 * @param rowid
	 *            the first row is 0, the second is 1
	 * @param columnIndex
	 *            the first column is 0, the second is 1, ...
	 * @return the column value; if the value is SQL <code>NULL</code>, the
	 *         value returned is <code>null</code>
	 * @exception SQLException
	 *                if a database access error occurs
	 */
	public java.sql.Timestamp getTimestamp(int rowid, int columnIndex)
			throws SQLException {
//		return (java.sql.Timestamp) this.getObject(rowid, columnIndex);
		inrange(rowid, columnIndex);
		return allResults[getTrueRowid(rowid)].getTimestamp(columnIndex);
	}

	/**
	 * Retrieves the value of the designated column in the current row of this
	 * <code>ResultSet</code> object as a stream of ASCII characters. The
	 * value can then be read in chunks from the stream. This method is
	 * particularly suitable for retrieving large <char>LONGVARCHAR</char>
	 * values. The JDBC driver will do any necessary conversion from the
	 * database format into ASCII.
	 * 
	 * <P>
	 * <B>Note:</B> All the data in the returned stream must be read prior to
	 * getting the value of any other column. The next call to a getter method
	 * implicitly closes the stream. Also, a stream may return <code>0</code>
	 * when the method <code>InputStream.available</code> is called whether
	 * there is data available or not.
	 * 
	 * @param rowid
	 *            the first row is 0, the second is 1
	 * @param columnIndex
	 *            the first column is 0, the second is 1, ...
	 * @return a Java input stream that delivers the database column value as a
	 *         stream of one-byte ASCII characters; if the value is SQL
	 *         <code>NULL</code>, the value returned is <code>null</code>
	 * @exception SQLException
	 *                if a database access error occurs
	 */
	public java.io.InputStream getAsciiStream(int rowid, int columnIndex)
			throws SQLException {
		inrange(rowid, columnIndex);
		return allResults[getTrueRowid(rowid)].getAsciiStream(columnIndex);
//		return (java.io.InputStream) getObject(rowid, columnIndex);
	}

	/**
	 * Retrieves the value of the designated column in the current row of this
	 * <code>ResultSet</code> object as as a stream of two-byte Unicode
	 * characters. The first byte is the high byte; the second byte is the low
	 * byte.
	 * 
	 * The value can then be read in chunks from the stream. This method is
	 * particularly suitable for retrieving large <code>LONGVARCHAR</code>values.
	 * The JDBC driver will do any necessary conversion from the database format
	 * into Unicode.
	 * 
	 * <P>
	 * <B>Note:</B> All the data in the returned stream must be read prior to
	 * getting the value of any other column. The next call to a getter method
	 * implicitly closes the stream. Also, a stream may return <code>0</code>
	 * when the method <code>InputStream.available</code> is called, whether
	 * there is data available or not.
	 * 
	 * @param rowid
	 *            the first row is 0, the second is 1
	 * @param columnIndex
	 *            the first column is 0, the second is 1, ...
	 * @return a Java input stream that delivers the database column value as a
	 *         stream of two-byte Unicode characters; if the value is SQL
	 *         <code>NULL</code>, the value returned is <code>null</code>
	 * 
	 * @exception SQLException
	 *                if a database access error occurs
	 * @deprecated use <code>getCharacterStream</code> in place of
	 *             <code>getUnicodeStream</code>
	 */
	public java.io.InputStream getUnicodeStream(int rowid, int columnIndex)
			throws SQLException {
		inrange(rowid, columnIndex);
		return allResults[getTrueRowid(rowid)].getUnicodeStream(columnIndex);
//		return (java.io.InputStream) this.getObject(rowid, columnIndex);
	}

	/**
	 * Retrieves the value of the designated column in the current row of this
	 * <code>ResultSet</code> object as a binary stream of uninterpreted
	 * bytes. The value can then be read in chunks from the stream. This method
	 * is particularly suitable for retrieving large <code>LONGVARBINARY</code>
	 * values.
	 * 
	 * <P>
	 * <B>Note:</B> All the data in the returned stream must be read prior to
	 * getting the value of any other column. The next call to a getter method
	 * implicitly closes the stream. Also, a stream may return <code>0</code>
	 * when the method <code>InputStream.available</code> is called whether
	 * there is data available or not.
	 * 
	 * @param rowid
	 *            the first row is 0, the second is 1
	 * @param columnIndex
	 *            the first column is 0, the second is 1, ...
	 * @return a Java input stream that delivers the database column value as a
	 *         stream of uninterpreted bytes; if the value is SQL
	 *         <code>NULL</code>, the value returned is <code>null</code>
	 * @exception SQLException
	 *                if a database access error occurs
	 */
	public java.io.InputStream getBinaryStream(int rowid, int columnIndex)
			throws SQLException {
		inrange(rowid, columnIndex);
		return allResults[getTrueRowid(rowid)].getBinaryStream(columnIndex);
	}

	// ======================================================================
	// Methods for accessing results by column name
	// ======================================================================

	/**
	 * Retrieves the value of the designated column in the current row of this
	 * <code>ResultSet</code> object as a <code>boolean</code> in the Java
	 * programming language.
	 * 
	 * @param rowid
	 *            the first row is 0, the second is 1
	 * @param colName
	 *            the SQL name of the column
	 * @return the column value; if the value is SQL <code>NULL</code>, the
	 *         value returned is <code>false</code>
	 * @exception SQLException
	 *                if a database access error occurs
	 */
	public boolean getBoolean(int rowid, String colName) throws SQLException {
//		Object value = this.getObject(rowid, colName);
//		if (value != null)
//			return Boolean.getBoolean(value.toString());
//		return false;
		inrange(rowid, colName);
		return allResults[getTrueRowid(rowid)].getBoolean(colName);

	}
	
	

	/**
	 * Retrieves the value of the designated column in the current row of this
	 * <code>ResultSet</code> object as a <code>byte</code> in the Java
	 * programming language.
	 * 
	 * @param rowid
	 *            the first row is 0, the second is 1
	 * @param colName
	 *            the SQL name of the column
	 * @return the column value; if the value is SQL <code>NULL</code>, the
	 *         value returned is <code>0</code>
	 * @exception SQLException
	 *                if a database access error occurs
	 */
	public byte getByte(int rowid, String colName) throws SQLException {
		inrange(rowid, colName);
		return allResults[getTrueRowid(rowid)].getByte(colName);
	}

	/**
	 * Retrieves the value of the designated column in the current row of this
	 * <code>ResultSet</code> object as a <code>short</code> in the Java
	 * programming language.
	 * 
	 * @param rowid
	 *            start with 0
	 * @param colName
	 *            the SQL name of the column
	 * @return the column value; if the value is SQL <code>NULL</code>, the
	 *         value returned is <code>0</code>
	 * @exception SQLException
	 *                if a database access error occurs
	 */
	public short getShort(int rowid, String colName) throws SQLException {
		inrange(rowid, colName);
		return allResults[getTrueRowid(rowid)].getShort(colName);
	}

	/**
	 * Retrieves the value of the designated column in the current row of this
	 * <code>ResultSet</code> object as a <code>java.sql.Time</code> object
	 * in the Java programming language.
	 * 
	 * @param rowid
	 *            start with 0
	 * @param colName
	 *            the SQL name of the column
	 * @return the column value; if the value is SQL <code>NULL</code>, the
	 *         value returned is <code>null</code>
	 * @exception SQLException
	 *                if a database access error occurs
	 */
	public java.sql.Time getTime(int rowid, String colName) throws SQLException {
		inrange(rowid, colName);
		return allResults[getTrueRowid(rowid)].getTime(colName);
	}

	/**
	 * Retrieves the value of the designated column in the current row of this
	 * <code>ResultSet</code> object as a <code>java.sql.Timestamp</code>
	 * object.
	 * 
	 * @param rowid
	 *            start with 0
	 * @param colName
	 *            the SQL name of the column
	 * @return the column value; if the value is SQL <code>NULL</code>, the
	 *         value returned is <code>null</code>
	 * @exception SQLException
	 *                if a database access error occurs
	 */
	public java.sql.Timestamp getTimestamp(int rowid, String colName)
			throws SQLException {
//		return (java.sql.Timestamp) this.getObject(rowid, colName);
		inrange(rowid, colName);
		return allResults[getTrueRowid(rowid)].getTimestamp(colName);
	}

	/**
	 * Retrieves the value of the designated column in the current row of this
	 * <code>ResultSet</code> object as a stream of ASCII characters. The
	 * value can then be read in chunks from the stream. This method is
	 * particularly suitable for retrieving large <code>LONGVARCHAR</code>
	 * values. The JDBC driver will do any necessary conversion from the
	 * database format into ASCII.
	 * 
	 * <P>
	 * <B>Note:</B> All the data in the returned stream must be read prior to
	 * getting the value of any other column. The next call to a getter method
	 * implicitly closes the stream. Also, a stream may return <code>0</code>
	 * when the method <code>available</code> is called whether there is data
	 * available or not.
	 * 
	 * @param rowid
	 *            start with 0
	 * @param colName
	 *            the SQL name of the column
	 * @return a Java input stream that delivers the database column value as a
	 *         stream of one-byte ASCII characters. If the value is SQL
	 *         <code>NULL</code>, the value returned is <code>null</code>.
	 * @exception SQLException
	 *                if a database access error occurs
	 */
	public java.io.InputStream getAsciiStream(int rowid, String colName)
			throws SQLException {
		inrange(rowid, colName);
		return allResults[getTrueRowid(rowid)].getAsciiStream(colName);
//		return (java.io.InputStream) this.getObject(rowid, colName);
	}

	/**
	 * Retrieves the value of the designated column in the current row of this
	 * <code>ResultSet</code> object as a stream of two-byte Unicode
	 * characters. The first byte is the high byte; the second byte is the low
	 * byte.
	 * 
	 * The value can then be read in chunks from the stream. This method is
	 * particularly suitable for retrieving large <code>LONGVARCHAR</code>
	 * values. The JDBC technology-enabled driver will do any necessary
	 * conversion from the database format into Unicode.
	 * 
	 * <P>
	 * <B>Note:</B> All the data in the returned stream must be read prior to
	 * getting the value of any other column. The next call to a getter method
	 * implicitly closes the stream. Also, a stream may return <code>0</code>
	 * when the method <code>InputStream.available</code> is called, whether
	 * there is data available or not.
	 * 
	 * @param rowid
	 *            start with 0
	 * @param colName
	 *            the SQL name of the column
	 * @return a Java input stream that delivers the database column value as a
	 *         stream of two-byte Unicode characters. If the value is SQL
	 *         <code>NULL</code>, the value returned is <code>null</code>.
	 * @exception SQLException
	 *                if a database access error occurs
	 * @deprecated use <code>getCharacterStream</code> instead
	 */
	public java.io.InputStream getUnicodeStream(int rowid, String colName)
			throws SQLException {
		inrange(rowid, colName);
		return allResults[getTrueRowid(rowid)].getUnicodeStream(colName);
	}

	/**
	 * Retrieves the value of the designated column in the current row of this
	 * <code>ResultSet</code> object as a stream of uninterpreted
	 * <code>byte</code>s. The value can then be read in chunks from the
	 * stream. This method is particularly suitable for retrieving large
	 * <code>LONGVARBINARY</code> values.
	 * 
	 * <P>
	 * <B>Note:</B> All the data in the returned stream must be read prior to
	 * getting the value of any other column. The next call to a getter method
	 * implicitly closes the stream. Also, a stream may return <code>0</code>
	 * when the method <code>available</code> is called whether there is data
	 * available or not.
	 * 
	 * @param rowid
	 *            the first row is 0, the second is 1
	 * @param colName
	 *            the SQL name of the column
	 * @return a Java input stream that delivers the database column value as a
	 *         stream of uninterpreted bytes; if the value is SQL
	 *         <code>NULL</code>, the result is <code>null</code>
	 * @exception SQLException
	 *                if a database access error occurs
	 */
	public java.io.InputStream getBinaryStream(int rowid, String colName)
			throws SQLException {
//		return (java.io.InputStream) this.getObject(rowid, colName);
		inrange(rowid, colName);
		return allResults[getTrueRowid(rowid)].getBinaryStream(colName);
	}

	// --------------------------JDBC 2.0-----------------------------------

	// ---------------------------------------------------------------------
	// Getters and Setters
	// ---------------------------------------------------------------------

	/**
	 * Retrieves the value of the designated column in the current row of this
	 * <code>ResultSet</code> object as a <code>java.io.Reader</code>
	 * object.
	 * 
	 * @return a <code>java.io.Reader</code> object that contains the column
	 *         value; if the value is SQL <code>NULL</code>, the value
	 *         returned is <code>null</code> in the Java programming language.
	 * @param rowid
	 *            start with 0
	 * @param columnIndex
	 *            the first column is 0, the second is 1, ...
	 * @exception SQLException
	 *                if a database access error occurs
	 * @since 1.2
	 */
	public java.io.Reader getCharacterStream(int rowid, int columnIndex)
			throws SQLException {
//		Object[] value = (Object[]) this.getObject(rowid, columnIndex);
//		Reader reader = (java.io.Reader) value[1];
//		return reader;
		inrange(rowid, columnIndex);
		return allResults[getTrueRowid(rowid)].getCharacterStream(columnIndex);
		
	}

	/**
	 * Retrieves the value of the designated column in the current row of this
	 * <code>ResultSet</code> object as a <code>java.io.Reader</code>
	 * object.
	 * 
	 * @param rowid
	 *            start with 0
	 * @param colName
	 *            the name of the column
	 * @return a <code>java.io.Reader</code> object that contains the column
	 *         value; if the value is SQL <code>NULL</code>, the value
	 *         returned is <code>null</code> in the Java programming language
	 * @exception SQLException
	 *                if a database access error occurs
	 * @since 1.2
	 */
	public java.io.Reader getCharacterStream(int rowid, String colName)
			throws SQLException {
		inrange(rowid, colName);
		return allResults[getTrueRowid(rowid)].getCharacterStream(colName);
	}

	/**
	 * Retrieves the value of the designated column in the current row of this
	 * <code>ResultSet</code> object as a <code>java.math.BigDecimal</code>
	 * with full precision.
	 * 
	 * @param rowid
	 *            start with 0
	 * @param columnIndex
	 *            the first column is 0, the second is 1, ...
	 * @return the column value (full precision); if the value is SQL
	 *         <code>NULL</code>, the value returned is <code>null</code>
	 *         in the Java programming language.
	 * @exception SQLException
	 *                if a database access error occurs
	 * @since 1.2
	 */
	public BigDecimal getBigDecimal(int rowid, int columnIndex)
			throws SQLException {
		return (BigDecimal) this.getObject(rowid, columnIndex);
	}

	/**
	 * Retrieves the value of the designated column in the current row of this
	 * <code>ResultSet</code> object as a <code>java.math.BigDecimal</code>
	 * with full precision.
	 * 
	 * @param rowid
	 *            start with 0
	 * @param colName
	 *            the column name
	 * @return the column value (full precision); if the value is SQL
	 *         <code>NULL</code>, the value returned is <code>null</code>
	 *         in the Java programming language.
	 * @exception SQLException
	 *                if a database access error occurs
	 * @since 1.2
	 * 
	 */
	public BigDecimal getBigDecimal(int rowid, String colName)
			throws SQLException {
		return (BigDecimal) this.getObject(rowid, colName);
	}

	/**
	 * Retrieves the value of the designated column in the current row of this
	 * <code>ResultSet</code> object as a <code>byte[]</code> object in the
	 * Java programming language.
	 * 
	 * @param rowid
	 *            the first row is 0, the second is 1
	 * @param columnIndex
	 *            the first column is 0, the second is 1, ...
	 * @return a <code>byte[]</code> object representing the SQL
	 *         <code>byte[]</code> value in the specified column
	 * @exception SQLException
	 *                if a database access error occurs
	 * @since 1.2
	 */
	public Blob getBlob(int rowid, int columnIndex) throws SQLException {
		return (Blob) this.getObject(rowid, columnIndex);
	}

	/**
	 * Retrieves the value of the designated column in the current row of this
	 * <code>ResultSet</code> object as a <code>Clob</code> object in the
	 * Java programming language.
	 * 
	 * @param rowid
	 *            the first row is 0, the second is 1
	 * @param columnIndex
	 *            the first column is 0, the second is 1, ...
	 * @return a <code>Clob</code> object representing the SQL
	 *         <code>CLOB</code> value in the specified column
	 * @exception SQLException
	 *                if a database access error occurs
	 * @since 1.2
	 */
	public Clob getClob(int rowid, int columnIndex) throws SQLException {
		inrange(rowid, columnIndex);
		return allResults[getTrueRowid(rowid)].getClob(columnIndex);
//		return (Clob) this.getObject(rowid, columnIndex);
	}

	/**
	 * Retrieves the value of the designated column in the current row of this
	 * <code>ResultSet</code> object as an <code>Array</code> object in the
	 * Java programming language.
	 * 
	 * @param rowid
	 *            the first row is 0,the second is 1
	 * @param column
	 *            the first column is 0, the second is 1, ...
	 * @return an <code>Array</code> object representing the SQL
	 *         <code>ARRAY</code> value in the specified column
	 * @exception SQLException
	 *                if a database access error occurs
	 * @since 1.2
	 */
	public Array getArray(int rowid, int column) throws SQLException {
		return (Array) this.getObject(rowid, column);
	}

	/**
	 * Retrieves the value of the designated column in the current row of this
	 * <code>ResultSet</code> object as a <code>Ref</code> object in the
	 * Java programming language.
	 * 
	 * @param rowid
	 *            the first row is 0, the second is 1
	 * @param colName
	 *            the column name
	 * @return a <code>Ref</code> object representing the SQL <code>REF</code>
	 *         value in the specified column
	 * @exception SQLException
	 *                if a database access error occurs
	 * @since 1.2
	 */
	public Ref getRef(int rowid, String colName) throws SQLException {
		return (Ref) this.getObject(rowid, colName);
	}

	/**
	 * Retrieves the value of the designated column in the current row of this
	 * <code>ResultSet</code> object as a <code>Blob</code> object in the
	 * Java programming language.
	 * 
	 * @param rowid
	 *            the first row is 0, the second is 1
	 * @param colName
	 *            the name of the column from which to retrieve the value
	 * @return a <code>byte[]</code> object representing the SQL
	 *         <code>byte[]</code> value in the specified column
	 * @exception SQLException
	 *                if a database access error occurs
	 * @since 1.2
	 */
	public Blob getBlob(int rowid, String colName) throws SQLException {
		return (Blob) this.getObject(rowid, colName);
	}

	/**
	 * Retrieves the value of the designated column in the current row of this
	 * <code>ResultSet</code> object as a <code>Clob</code> object in the
	 * Java programming language.
	 * 
	 * @param rowid
	 *            the first row is 0, the second is 1
	 * @param colName
	 *            the name of the column from which to retrieve the value
	 * @return a <code>Clob</code> object representing the SQL
	 *         <code>CLOB</code> value in the specified column
	 * @exception SQLException
	 *                if a database access error occurs
	 * @since 1.2
	 */
	public Clob getClob(int rowid, String colName) throws SQLException {
		inrange(rowid, colName);
		return allResults[getTrueRowid(rowid)].getClob(colName);
	}

	/**
	 * Retrieves the value of the designated column in the current row of this
	 * <code>ResultSet</code> object as an <code>Array</code> object in the
	 * Java programming language.
	 * 
	 * @param rowid
	 *            the first row is 0, the second is 1
	 * @param colName
	 *            the name of the column from which to retrieve the value
	 * @return an <code>Array</code> object representing the SQL
	 *         <code>ARRAY</code> value in the specified column
	 * @exception SQLException
	 *                if a database access error occurs
	 * @since 1.2
	 */
	public Array getArray(int rowid, String colName) throws SQLException {
		return (Array) this.getObject(rowid, colName);
	}

	/**
	 * Retrieves the value of the designated column in the current row of this
	 * <code>ResultSet</code> object as a <code>java.net.URL</code> object
	 * in the Java programming language.
	 * 
	 * @param rowid
	 *            the first row is 0, the second is 1
	 * @param columnIndex
	 *            the index of the column 0 is the first, 1 is the second,...
	 * @return the column value as a <code>java.net.URL</code> object; if the
	 *         value is SQL <code>NULL</code>, the value returned is
	 *         <code>null</code> in the Java programming language
	 * @exception SQLException
	 *                if a database access error occurs, or if a URL is
	 *                malformed
	 * @since 1.4
	 */
	public java.net.URL getURL(int rowid, int columnIndex) throws SQLException {
//		return (java.net.URL) this.getObject(rowid, columnIndex);
		inrange(rowid, columnIndex);
		return allResults[getTrueRowid(rowid)].getURL(columnIndex);
	}

	/**
	 * Retrieves the value of the designated column in the current row of this
	 * <code>ResultSet</code> object as a <code>java.net.URL</code> object
	 * in the Java programming language.
	 * 
	 * @param rowid
	 *            the first row is 0, the second is 1
	 * @param colName
	 *            the SQL name of the column
	 * @return the column value as a <code>java.net.URL</code> object; if the
	 *         value is SQL <code>NULL</code>, the value returned is
	 *         <code>null</code> in the Java programming language
	 * @exception SQLException
	 *                if a database access error occurs or if a URL is malformed
	 * @since 1.4
	 */
	public java.net.URL getURL(int rowid, String colName) throws SQLException {
//		return (java.net.URL) this.getObject(rowid, colName);
		inrange(rowid, colName);
		return allResults[getTrueRowid(rowid)].getURL(colName);
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
	protected byte[] getBytesFromResultset(ResultSet res, String columnName)
			throws SQLException {
		// read the bytes from an oracle blob
		try
		{
			initOracleDB();
			return super.oracleDB.getBytesFromResultset(res, columnName);
		}
		catch (Exception e)
		{
			throw new NestedSQLException(e);
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
	protected byte[] getBytesFromBlob(Blob blob) throws SQLException {
		try
		{
			initOracleDB();
			return super.oracleDB.getBytesFromBlob(blob);
		}
		catch (Exception e)
		{
			throw new NestedSQLException(e);
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
	protected byte[] getBytesFromClob(Clob clob) {
		try
		{
			initOracleDB();
			return super.oracleDB.getBytesFromClob(clob);
		}
		catch (Exception e)
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
	protected String getStringFromClob(Clob clob) {
		try
		{
			initOracleDB();
			return super.oracleDB.getStringFromClob( clob);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 获取数据库表的下一个主键值
	 * 
	 * @param tableName
	 * @return
	 * @throws SQLException
	 */
	public static long getNextPrimaryKey(String tableName) throws SQLException {
		return getNextPrimaryKey(SQLManager.getInstance().getDefaultDBName(),
				tableName);
	}

	/**
	 * 获取数据库表的下一个主键值
	 * 
	 * @param tableName
	 * @return
	 * @throws SQLException
	 */
	public static long getNextPrimaryKey(Connection con, String tableName)
			throws SQLException {
		return getNextPrimaryKey(con, SQLManager.getInstance()
				.getDefaultDBName(), tableName);
	}

	/**
	 * 获取数据库表的下一个主键值
	 * 
	 * @param tableName
	 * @return
	 */
	public static long getNextPrimaryKey(String dbName, String tableName)
			throws SQLException {
		return getNextPrimaryKey(null, dbName, tableName);
	}

	/**
	 * 获取数据库表的下一个主键值
	 * 
	 * @param tableName
	 * @return
	 */
	public static long getNextPrimaryKey(Connection con, String dbName,
			String tableName) throws SQLException {
		try {
			
			PrimaryKey primaryKey = PrimaryKeyCacheManager.getInstance()
					.getPrimaryKeyCache(dbName).getIDTable(
							tableName.toLowerCase());
			long ret = ((Long) primaryKey.generateObjectKey(con).getPrimaryKey())
					.longValue();
			//primaryKey.updateTableinfo(con);
			return ret;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new NestedSQLException("Get Next long PrimaryKey error for dbName="
					+ dbName + ",tableName=" + tableName + ":" + e.getMessage(),e);	
		} catch (Exception e) {
			e.printStackTrace();
			throw new NestedSQLException("Get Next long PrimaryKey error for dbName="
					+ dbName + ",tableName=" + tableName + ":" + e.getMessage(),e);
		}
	}

	/**
	 * 获取数据库表的下一个主键值
	 * 
	 * @param tableName
	 * @return
	 * @throws SQLException
	 */
	public static String getNextStringPrimaryKey(String tableName)
			throws SQLException {
		return getNextStringPrimaryKey(null, SQLManager.getInstance()
				.getDefaultDBName(), tableName);
	}

	/**
	 * 获取数据库表的下一个主键值
	 * 
	 * @param tableName
	 * @return
	 * @throws SQLException
	 */
	public static String getNextStringPrimaryKey(Connection con,
			String tableName) throws SQLException {
		return getNextStringPrimaryKey(con, SQLManager.getInstance()
				.getDefaultDBName(), tableName);
	}

	/**
	 * 获取数据库表的下一个主键值
	 * 
	 * @param tableName
	 * @return
	 */
	public static String getNextStringPrimaryKey(String dbName, String tableName)
			throws SQLException {
		return getNextStringPrimaryKey(null, dbName, tableName);
	}

	/**
	 * 获取数据库表的下一个主键值
	 * 
	 * @param tableName
	 * @return
	 */
	public static String getNextStringPrimaryKey(Connection con, String dbName,
			String tableName) throws SQLException {
		try {
			PrimaryKey primaryKey = PrimaryKeyCacheManager.getInstance()
					.getPrimaryKeyCache(dbName).getIDTable(
							tableName.toLowerCase());
			String key = primaryKey.generateObjectKey(con).getPrimaryKey()
					.toString();
			//primaryKey.updateTableinfo(con);
			return key;
		} catch (Exception e) {
			e.printStackTrace();
			throw new NestedSQLException(
					"Get Next String PrimaryKey error for dbName=" + dbName
							+ ",tableName=" + tableName+ ":" + e.getMessage(),e);
		}
	}
	
	/**
	 * 直接获取所有的数据库查询结果集，无需封装即可给分页列表标签展示
	 * @return
	 */
	public Record[] getAllResults()
	{
		return this.allResults;
	}
	
	/*****************************************************************************************
	 * for object
	 */
	public Object executeSelectForObject(String sql,Class objectType) throws SQLException
	{
		return executeSelectForObject(sql,objectType,(Connection)null);
	}
	
	/*****************************************************************************************
	 * for object
	 */
	public Object[] executeSelectForObjectArray(String sql,Class objectType) throws SQLException
	{
		return executeSelectForObjectArray(sql,objectType,(Connection)null);
	}
	
	
	/*****************************************************************************************
	 * for object
	 */
	public Object[] executeSelectForObjectArray(String dbname,String sql,Class objectType) throws SQLException
	{
		return executeSelectForObjectArray(dbname,sql,objectType,(Connection)null,(RowHandler)null);
	}
	
	/*****************************************************************************************
	 * for object
	 */
	public Object[] executeSelectForObjectArray(String sql,Class objectType,Connection con) throws SQLException
	{
		return executeSelectForObjectArray(sql,objectType,con,null);
	}
	
	/*****************************************************************************************
	 * for object
	 */
	public Object[] executeSelectForObjectArray(String sql,Class objectType,Connection con,RowHandler rowhandler) throws SQLException
	{
		return executeSelectForObjectArray(null,sql,objectType,con,rowhandler);
	}
	
	
	public Object[] executeSelectForObjectArray(String dbname,String sql,
												Class objectType,Connection con,
												RowHandler rowhandler) throws SQLException
	{
		
		StatementInfo stmtInfo = null;
		try{
			stmtInfo = new StatementInfo(dbname, new NewSQLInfo(sql), false,con,false);
			ResultMap resultMap = innerExecuteJDBC(stmtInfo,
					objectType,rowhandler,ResultMap.type_objectarray);
			return (Object[])resultMap.getCommonresult();
		}
		finally
		{
			if(stmtInfo != null)
			{
				stmtInfo.dofinally();
				stmtInfo = null;
			}
		}
	}
	
	
	/*****************************************************************************************
	 * for object array pagine
	 */
	public Object[] executeSelectForObjectArray(String sql,long offset,int pagesize,Class objectType) throws SQLException
	{
		return executeSelectForObjectArray(sql,offset,pagesize,objectType,(Connection)null);
	}
	
	public Object[] executeSelectForObjectArray(String dbname,String sql,long offset,int pagesize,Class objectType) throws SQLException
	{
		return executeSelectForObjectArray(dbname,sql, offset, pagesize, objectType,null,null);
//		return executeSelectForObjectArray(dbname,sql,offset,pagesize,objectType,(Connection)null);
	}
	
	public Object[] executeSelectForObjectArray(String dbname,String sql,long offset,int pagesize,Class objectType,Connection con) throws SQLException
	{
		return executeSelectForObjectArray(dbname,sql, offset, pagesize, objectType,con,null);
	}
	
	/*****************************************************************************************
	 * for object
	 */
	public Object[] executeSelectForObjectArray(String sql,long offset,int pagesize,Class objectType,Connection con) throws SQLException
	{
		return executeSelectForObjectArray(sql,offset,pagesize,objectType,con,null);
	}
	
	/*****************************************************************************************
	 * for object 
	 */
	public Object[] executeSelectForObjectArray(String sql,long offset,int pagesize,Class objectType,Connection con,RowHandler rowhandler) throws SQLException
	{
		return executeSelectForObjectArray((String)null,sql,offset,pagesize,objectType,con,rowhandler);
	}
	
	
	public Object[] executeSelectForObjectArray(String dbname,String sql,long offset,int pagesize,Class objectType,Connection con,RowHandler rowhandler) throws SQLException
	{
		StatementInfo stmtInfo = new StatementInfo(dbname, new NewSQLInfo(sql),
				// Connection con,
						false, offset, pagesize, isRobotQuery(dbname), con,oraclerownum);
		ResultMap resultMap = innerExecutePagineJDBC(stmtInfo, objectType, rowhandler, ResultMap.type_objectarray);
		return (Object[])resultMap.getCommonresult();
	}
	
	
	
	
	
	public Object executeSelectForObject(String sql,Class objectType, Connection con)  throws SQLException
	{
		return executeSelectForObject(sql,objectType,con,(RowHandler)null);
	}
	
	public void executeSelectWithRowHandler(String sql,Connection con,NullRowHandler rowhandler)  throws SQLException
    {
        executeSelectWithRowhandler(null, sql, con,rowhandler) ;
    }
	
	public void executeSelectWithRowHandler(String sql,NullRowHandler rowhandler)  throws SQLException
    {
	    executeSelectWithRowhandler(null, sql, null,rowhandler) ;
    }
	
	
	public void executeSelectWithRowHandler(String dbname,String sql,NullRowHandler rowhandler)  throws SQLException
    {
	    executeSelectWithRowhandler(dbname, sql, null,rowhandler) ;
    }
	
	public void executeSelectWithRowhandler(String dbname, String sql, Connection con,NullRowHandler rowhandler) throws SQLException
    {
        StatementInfo stmtInfo = null;
        try
        {
            stmtInfo = new StatementInfo(dbname, new NewSQLInfo(sql), false,con,false);
            ResultMap resultMap = innerExecuteJDBC(stmtInfo,
                    null,rowhandler,ResultMap.type_null);
//            return (List)resultMap.getCommonresult();
        }
        finally
        {
            if(stmtInfo != null)
            {
                stmtInfo.dofinally();
                stmtInfo = null;
            }
        }
    }
	
	public void executeSelectWithRowHandler(String sql,long offset,int pagesize,Connection con,NullRowHandler rowhandler)  throws SQLException
    {
        executeSelectWithRowhandler(null, sql, offset, pagesize, con,rowhandler) ;
    }
	public void executeSelectWithRowHandler(String sql,long offset,int pagesize,NullRowHandler rowhandler)  throws SQLException
    {
	    executeSelectWithRowhandler(null, sql, offset, pagesize, null,rowhandler) ;
    }
	public void executeSelectWithRowHandler(String dbname,String sql,long offset,int pagesize,NullRowHandler rowhandler)  throws SQLException
    {
	    executeSelectWithRowhandler(dbname, sql,  offset, pagesize,null,rowhandler) ;
    }
	
	public void executeSelectWithRowhandler(String dbname, String sql,long offset,int pagesize, Connection con,NullRowHandler rowhandler) throws SQLException
    {
		StatementInfo stmtInfo = new StatementInfo(dbname, new NewSQLInfo(sql),
					
							false, offset, pagesize, true, con,oraclerownum);
			this.innerExecutePagineJDBC(stmtInfo, null, rowhandler, ResultMap.type_null);
		
    }
	
	
	
//	executeSelect(String, int)
//	executeSelectForList(String, long, int)
	public Object executeSelectForObject(String dbname, String sql,Class objectType)  throws SQLException
	{
		return executeSelectForObject( dbname,  sql, objectType,(RowHandler)null);
	}
	
	public Object executeSelectForObject(String dbname, String sql, Connection con,Class objectType) throws SQLException
	{
		return executeSelectForObject( dbname,  sql,  con, objectType,(RowHandler)null);
	}
	
	/***************************************************************************
	 * 	for list
	 ***************************************************************************/
	
	public List executeSelectForList(String sql,Class objectType) throws SQLException
	{
		return executeSelectForList( sql, objectType, (Connection )null);
	}
	
	public List executeSelectForList(String sql,Class objectType, Connection con)  throws SQLException
	{
		return executeSelectForList( sql, objectType, con,(RowHandler)null);
	}
	
	
	
//	executeSelect(String, int)
//	executeSelectForList(String, long, int)
	public List executeSelectForList(String dbname, String sql,Class objectType)  throws SQLException
	{
		return executeSelectForList( dbname,  sql, (Connection)null,objectType);
	}
	
	public List executeSelectForList(String dbname, String sql, Connection con,Class objectType) throws SQLException
	{
		return executeSelectForList( dbname,  sql, con,objectType,(RowHandler)null);
	}
	
	
	/***************************************************************************
	 * 	for xml
	 ***************************************************************************/
	


	
	
	
//	executeSelect(String, int)
//	executeSelectForList(String, long, int)

	

	
	/*********************************************************************************************
	 * 分页forList
	 ********************************************************/
	
//	executeSelect(String, String, int)
	public List executeSelectForList(String sql, long offset, int pagesize,Class objectType) throws SQLException
	{
		return executeSelectForList((String )null, sql, offset,  pagesize, objectType);
	}
	public List executeSelectForList(String dbname, String sql,long offset, int pagesize,Class objectType) throws SQLException
	{
		return executeSelectForList( dbname,  sql,  offset,  pagesize, isRobotQuery(dbname) , objectType);
	}
	public List executeSelectForList(String dbname, String sql, long offset, int pagesize, boolean robotquery,Class objectType) throws SQLException
	{
		return executeSelectForList( dbname,  sql,  offset,  pagesize, robotquery , (Connection)null,objectType);
	}
	public List executeSelectForList(String dbname, String sql,  long offset, int pagesize, boolean robotquery,Connection con,Class objectType) throws SQLException
	{
		return executeSelectForList( dbname,  sql,   offset,  pagesize,  robotquery, con, objectType,(RowHandler)null);
	}
	public List executeSelectForList(String dbname, String sql, long offset, int pagesize,Connection con,Class objectType) throws SQLException
	{
		return executeSelectForList( dbname,  sql,  offset,  pagesize, isRobotQuery(dbname) , con,objectType);
	}
	public List executeSelectForOracleList(String sql, long offset, int pagesize, String oraclerownum,Class objectType) throws SQLException
	{
		return executeSelectForOracleList((String )null,  sql,  offset,  pagesize,  oraclerownum, objectType) ;
	}
	public List executeSelectForOracleList(String dbname, String sql, long offset, int pagesize, String oraclerownum,Class objectType) throws SQLException
	{
		return executeSelectForOracleList( dbname,  sql,  offset,  pagesize,  oraclerownum,(Connection )null,objectType) ;
	}
	public List executeSelectForOracleList(String dbname, String sql, long offset, int pagesize, String oraclerownum,Connection con,Class objectType) throws SQLException
	{
		return executeSelectForOracleList( dbname,  sql,  offset,  pagesize,  oraclerownum, con, objectType,(RowHandler)null);
	}
	
	
	/*********************************************************************************************
	 * 分页for xml
	 ********************************************************/
	







	/*****************************************************************
	 * with rowhandler
	 ****************************************************************/
	/*****************************************************************************************
	 * for object
	 */
	public Object executeSelectForObject(String sql,Class objectType,RowHandler rowhandler) throws SQLException
	{
		return executeSelectForObject((String)null, sql,objectType,rowhandler) ;
	}
	
	public Object executeSelectForObject(String sql,Class objectType, Connection con,RowHandler rowhandler)  throws SQLException
	{
		return executeSelectForObject( (String)null,sql,con, objectType,   rowhandler)  ;
	}
	
	
	
//	executeSelect(String, int)
//	executeSelectForList(String, long, int)
	public Object executeSelectForObject(String dbname, String sql,Class objectType,RowHandler rowhandler)  throws SQLException
	{
		return executeSelectForObject( dbname,  sql,(Connection)null, objectType, rowhandler);
	}
	
	public Object executeSelectForObject(String dbname, String sql, 
										 Connection con,Class objectType,
										 RowHandler rowhandler) throws SQLException
	{
		StatementInfo stmtInfo = null;
		try
		{
			stmtInfo = new StatementInfo(dbname, new NewSQLInfo(sql), false,con,false);
			ResultMap resultMap = innerExecuteJDBC(stmtInfo,
					objectType,rowhandler,ResultMap.type_objcet);
			return (Object)resultMap.getCommonresult();
		}
		finally
		{
			if(stmtInfo != null)
			{
				stmtInfo.dofinally();
				stmtInfo = null;
			}
		}
	}
	
	/***************************************************************************
	 * 	for list
	 ***************************************************************************/
	
	public List executeSelectForList(String sql,Class objectType,RowHandler rowhandler) throws SQLException
	{
		return executeSelectForList( sql, objectType,(Connection)null, rowhandler);
	}
	
	public List executeSelectForList(String sql,Class objectType, Connection con,RowHandler rowhandler)  throws SQLException
	{
		return executeSelectForList( (String)null,sql,   con, objectType,rowhandler);
	}
	
	
	
//	executeSelect(String, int)
//	executeSelectForList(String, long, int)
	public List executeSelectForList(String dbname, String sql,Class objectType,RowHandler rowhandler)  throws SQLException
	{
		return executeSelectForList( dbname,  sql, (Connection )null,objectType, rowhandler) ;
	}
	
	public List executeSelectForList(String dbname, String sql, Connection con,Class objectType,RowHandler rowhandler) throws SQLException
	{
		StatementInfo stmtInfo = null;
		try
		{
			stmtInfo = new StatementInfo(dbname, new NewSQLInfo(sql), false,con,false);
			ResultMap resultMap = innerExecuteJDBC(stmtInfo,
					objectType,rowhandler,ResultMap.type_list);
			return (List)resultMap.getCommonresult();
		}
		finally
		{
			if(stmtInfo != null)
			{
				stmtInfo.dofinally();
				stmtInfo = null;
			}
		}
	}
	
	
	
	
	/***************************************************************************
	 * 	for xml
	 ***************************************************************************/
	

	
	
	

	public String executeSelectForXML(String dbname, String sql, Connection con,RowHandler rowhandler) throws SQLException
	{
		StatementInfo stmtInfo = null;
		try{
			stmtInfo = new StatementInfo(dbname, new NewSQLInfo(sql), false,con,false);
			ResultMap resultMap = innerExecuteJDBC(stmtInfo,
					XMLMark.class,rowhandler,ResultMap.type_xml);
			return (String)resultMap.getCommonresult();
		}
		finally
		{
			if(stmtInfo != null)
			{
				stmtInfo.dofinally();
				stmtInfo = null;
			}
		}
	}
	
	public String executeSelectForXML( String sql, Connection con,RowHandler rowhandler) throws SQLException
	{
		return executeSelectForXML(null, sql, con,rowhandler);
	}
	
	public String executeSelectForXML( String sql, RowHandler rowhandler) throws SQLException
	{
		return executeSelectForXML(null, sql, null,rowhandler);
	}
	
	public String executeSelectForXML( String sql) throws SQLException
	{
		return executeSelectForXML(null, sql, null,null);
	}
	
	public String executeSelectForXML( String dbname,String sql) throws SQLException
	{
		return executeSelectForXML(dbname, sql, null,null);
	}
	
	public String executeSelectForXML( String dbname,String sql,Connection con) throws SQLException
	{
		return executeSelectForXML(dbname, sql, con,null);
	}
	
	
//	---------------------
	public String executeSelectForXML(String dbname,String sql,long offset,int pagesize,  Connection con,RowHandler rowhandler) throws SQLException
	{
		return executeSelectForXML( dbname, sql, offset, pagesize,  isRobotQuery(dbname) ,con, rowhandler);
	}
	
	public String executeSelectForXML( String sql,long offset,int pagesize,  Connection con,RowHandler rowhandler) throws SQLException
	{
		return executeSelectForXML(null, sql,offset,pagesize, con,rowhandler);
	}
	
	public String executeSelectForXML( String sql,long offset,int pagesize,  RowHandler rowhandler) throws SQLException
	{
		return executeSelectForXML(null, sql,offset,pagesize, null,rowhandler);
	}
	
	public String executeSelectForXML( String sql,long offset,int pagesize) throws SQLException
	{
		return executeSelectForXML(null, sql,offset,pagesize, null,null);
	}
	
	public String executeSelectForXML( String dbname,String sql,long offset,int pagesize) throws SQLException
	{
		return executeSelectForXML(dbname, sql,offset,pagesize, null,null);
	}
	
	public String executeSelectForXML( String dbname,String sql,long offset,int pagesize,Connection con) throws SQLException
	{
		return executeSelectForXML(dbname, sql,offset,pagesize, con,null);
	}
	
//	-----------------------
	public String executeSelectForXML(String dbname,String sql,long offset,int pagesize,boolean robotquery , Connection con,RowHandler rowhandler) throws SQLException
	{
		StatementInfo stmtInfo = new StatementInfo(dbname, new NewSQLInfo(sql),
				// Connection con,
						false, offset, pagesize, robotquery, con,oraclerownum);
		ResultMap resultMap = innerExecutePagineJDBC(stmtInfo, XMLMark.class, rowhandler, ResultMap.type_xml);
		return (String)resultMap.getCommonresult();
	}
	
	public String executeSelectForXML( String sql,long offset,int pagesize,boolean robotquery,  Connection con,RowHandler rowhandler) throws SQLException
	{
		return executeSelectForXML(null, sql,offset,pagesize, robotquery,con,rowhandler);
	}
	
	public String executeSelectForXML( String sql,long offset,int pagesize,boolean robotquery,  RowHandler rowhandler) throws SQLException
	{
		return executeSelectForXML(null, sql,offset,pagesize, robotquery,null,rowhandler);
	}
	
	public String executeSelectForXML( String sql,long offset,int pagesize,boolean robotquery) throws SQLException
	{
		return executeSelectForXML(null, sql,offset,pagesize,robotquery,null,null);
	}
	
	public String executeSelectForXML( String dbname,String sql,long offset,int pagesize,boolean robotquery) throws SQLException
	{
		return executeSelectForXML(dbname, sql,offset,pagesize, robotquery,null,null);
	}
	
	public String executeSelectForXML( String dbname,String sql,long offset,int pagesize,boolean robotquery,Connection con) throws SQLException
	{
		return executeSelectForXML(dbname, sql,offset,pagesize, robotquery,con,null);
	}
	
	
	/*********************************************************************************************
	 * 分页forList with handler
	 ********************************************************/
	
//	executeSelect(String, String, int)
	public List executeSelectForList(String sql, long offset, int pagesize,Class objectType,RowHandler rowhandler) throws SQLException
	{
		return executeSelectForList((String )null, sql, offset,  pagesize, objectType, rowhandler) ;
	}
	public List executeSelectForList(String dbname, String sql,long offset, int pagesize,Class objectType,RowHandler rowhandler) throws SQLException
	{
		return executeSelectForList(dbname, sql, offset,  pagesize, isRobotQuery(dbname),objectType, rowhandler) ;
	}
	public List executeSelectForList(String dbname, String sql, long offset, int pagesize, boolean robotquery,Class objectType,RowHandler rowhandler) throws SQLException
	{
		return executeSelectForList(dbname, sql, offset,  pagesize, isRobotQuery(dbname),(Connection)null,objectType, rowhandler) ;
	}
	public List executeSelectForList(String dbname, String sql,  long offset, int pagesize, boolean robotquery,Connection con,Class objectType,RowHandler rowhandler) throws SQLException
	{
		StatementInfo stmtInfo = new StatementInfo(dbname, new NewSQLInfo(sql),
				// Connection con,
						false, offset, pagesize, robotquery, con,oraclerownum);
		ResultMap resultMap = this.innerExecutePagineJDBC(stmtInfo, objectType, rowhandler, ResultMap.type_list);
		return (List)resultMap.getCommonresult();
	}
	public List executeSelectForList(String dbname, String sql, long offset, int pagesize,Connection con,Class objectType,RowHandler rowhandler) throws SQLException
	{
		return executeSelectForList(dbname, sql, offset,  pagesize, isRobotQuery(dbname),con,objectType, rowhandler) ;
	}
	public List executeSelectForOracleList(String sql, long offset, int pagesize, String oraclerownum,Class objectType,RowHandler rowhandler) throws SQLException
	{
		return executeSelectForOracleList((String)null,sql,  offset,  pagesize,  oraclerownum, objectType, rowhandler) ;
	}
	public List executeSelectForOracleList(String dbname, String sql, long offset, int pagesize, String oraclerownum,Class objectType,RowHandler rowhandler) throws SQLException
	{
		return executeSelectForOracleList( dbname,  sql,  offset,  pagesize,  oraclerownum,(Connection)null, objectType, rowhandler) ;
	}
	public List executeSelectForOracleList(String dbname, String sql, long offset, int pagesize, String oraclerownum,Connection con,Class objectType,RowHandler rowhandler) throws SQLException
	{
		StatementInfo stmtInfo = new StatementInfo(dbname, new NewSQLInfo(sql),
				// Connection con,
						false, offset, pagesize, isRobotQuery(dbname), con,oraclerownum);
		ResultMap resultMap = this.innerExecutePagineJDBC(stmtInfo, objectType, rowhandler, ResultMap.type_list);
		return (List)resultMap.getCommonresult();
	}
	
	
	/*********************************************************************************************
	 * 分页for xml
	 ********************************************************/
	
	public Serializable getSerializable(int rowid,String fieldname) throws SQLException
	{
	    inrange(rowid, fieldname);
            return allResults[getTrueRowid(rowid)].getSerializable(fieldname);
	}
	
	public Serializable getSerializable(int rowid,int columnIndex)throws SQLException
        {
	    inrange(rowid, columnIndex);
            return allResults[getTrueRowid(rowid)].getSerializable(columnIndex);
        }

	public void addBatch(List<String> batchsqls) {
		this.batchSQLS = batchsqls;
		
	}




	


}
