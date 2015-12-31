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

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.frameworkset.util.ClassUtil;
import org.frameworkset.util.ClassUtil.ClassInfo;

import com.frameworkset.common.poolman.handle.RowHandler;
import com.frameworkset.common.poolman.handle.XMLRowHandler;
import com.frameworkset.common.poolman.interceptor.InterceptorInf;
import com.frameworkset.common.poolman.sql.PoolManResultSetMetaData;
import com.frameworkset.common.poolman.util.JDBCPool;
import com.frameworkset.common.poolman.util.SQLManager;
import com.frameworkset.common.poolman.util.SQLUtil;
import com.frameworkset.orm.adapter.DB;
import com.frameworkset.orm.adapter.DB.PagineSql;
import com.frameworkset.orm.transaction.JDBCTransaction;
import com.frameworkset.orm.transaction.TXConnection;
import com.frameworkset.orm.transaction.TransactionException;
import com.frameworkset.orm.transaction.TransactionManager;

public class StatementInfo {
	private static final Logger log = Logger.getLogger(StatementInfo.class);
	private String dbname;
	private String sql;
	private NewSQLInfo newsqlinfo;
	private long totalsize = -1L;
	private String totalsizesql;

	// Connection con,
	private boolean goNative;
	private long offset = -1;
	private int maxsize = -1;
	private boolean robotquery = true;
	private Connection con;

	private List statements;
	private JDBCTransaction tx = null;
	private boolean outcon = false;

	private String rownum;

	private boolean prepared = false;
	private PagineSql paginesql;
	private String pagineOrderBy;
	public void setPagineOrderBy(String pagineOrderBy) {
		this.pagineOrderBy = pagineOrderBy;
	}
	List resultSets;

	private boolean oldautocommit = true;

	private boolean needTransaction = false;
	
//	private boolean neadGetGenerateKeys = false;

	
	// protected String[] fields = null;

	private PoolManResultSetMetaData meta = null;

	public StatementInfo(String dbname_, NewSQLInfo sql_, boolean goNative_,
			Connection con_, boolean needTransaction) {
		this(dbname_, sql_, goNative_, -1, -1, SQLUtil.isRobotQuery(dbname_),
				con_, needTransaction, null, false);
	}

	public StatementInfo(String dbname_, NewSQLInfo sql_, boolean goNative_,
			long offset_, int maxsize_, boolean robotquery_, Connection con_,
			String rownum, boolean prepared) {
		this(dbname_, sql_, goNative_, offset_, maxsize_, robotquery_, con_,
				false, rownum, prepared);
	}
	private InterceptorInf interceptorInf;
	private DB dbadapter ;
	private JDBCPool pool ;
	public StatementInfo(String dbname_, NewSQLInfo sql_, boolean goNative_,
			long offset_, int maxsize_, boolean robotquery_, Connection con_,
			boolean needTransaction, String rownum, boolean prepared) {
		this.dbname = dbname_;
		if(this.dbname == null)
			this.dbname = SQLManager.getInstance().getDefaultDBName();
		newsqlinfo = sql_;
		/**
		 * must be removed.
		 */
		pool = SQLUtil.getSQLManager().getPool(dbname);
		interceptorInf = pool.getInterceptor();
		dbadapter = pool.getDbAdapter();
		if(sql_ != null)
			sql = interceptorInf.convertSQL(sql_.getNewsql(), dbadapter.getDBTYPE(), dbname_);
//		this.sql = sql_;

		this.goNative = goNative_;
		this.offset = offset_;
		this.maxsize = maxsize_;
		this.robotquery = robotquery_;
		this.con = con_;
		this.outcon = con_ == null ? false : true;
		statements = new ArrayList();
		this.rownum = rownum;
		this.prepared = prepared;
		resultSets = new ArrayList();
		
//		neadGetGenerateKeys = DBUtil.getJDBCPoolMetaData(dbname).isNeadGetGenerateKeys();
//		if (dbname == null)
//			this.dbname = SQLManager.getInstance().getDefaultDBName();
		this.needTransaction = needTransaction;
	}

	/**
	 * protected Hashtable[] doJDBC(String dbname_, String sql_, // Connection
	 * con, boolean goNative_, long offset_, int maxsize_, boolean robotquery_,
	 * Connection con) throws SQLException
	 * 
	 * @param dbname_
	 * @param sql_
	 * @param goNative_
	 * @param offset_
	 * @param maxsize_
	 * @param robotquery_
	 * @param con_
	 */
	public StatementInfo(String dbname_, NewSQLInfo sql_, boolean goNative_,
			long offset_, int maxsize_, boolean robotquery_, Connection con_,
			String rownum) {
		this(dbname_, sql_, goNative_, offset_, maxsize_, robotquery_, con_,
				rownum, false);

	}

	public StatementInfo(String dbname_, NewSQLInfo sql_, boolean goNative_,
			long offset_, int maxsize_, boolean robotquery_, Connection con_,
			boolean neadTransaction, String rownum) {
		this(dbname_, sql_, goNative_, offset_, maxsize_, robotquery_, con_,
				rownum, false);

	}

	/**
	 * protected Hashtable[] doJDBC(String dbname_, String sql_, // Connection
	 * con, boolean goNative_, long offset_, int maxsize_, boolean robotquery_,
	 * Connection con) throws SQLException
	 */
	public void init() throws Exception {

		try {
			if (!outcon) {
				tx = TransactionManager.getTransaction();
				if (tx == null) {
					con = SQLUtil.getSQLManager().requestConnection(dbname);// 事务无处理，批处理的默认事务处理需要设定
					if (needTransaction) {
						this.oldautocommit = con.getAutoCommit();
						con.setAutoCommit(false);
					}
				} else {
					try {
						con = tx.getConnection(dbname);
					} catch (TransactionException e) {
						try {
							tx.setRollbackOnly();
						} catch (Exception ei) {

						}
						throw e;
					}
				}
			}
			else
			{
				if(this.con != null && this.con instanceof TXConnection)
				{
					tx = TransactionManager.getTransaction();
				}
			}
		} catch (Exception e) {
			throw e;
		}
	}

	public Statement createStatement(int resultSetType, int resultSetConcurrency)
			throws SQLException {
		Statement stmt = this.con.createStatement(resultSetType,
				resultSetConcurrency);
		statements.add(stmt);
		return stmt;
	}

	public PreparedStatement prepareStatement() throws SQLException {
//		PreparedStatement pstmt = this.con.prepareStatement(this.sql,this.getScrollType(dbname),this.getCursorType(dbname));
//		this.statements.add(pstmt);
//		return pstmt;
		return _prepareStatement(false);
	}
	
	private PreparedStatement _prepareStatement(boolean isquery) throws SQLException {
		PreparedStatement pstmt = this.con.prepareStatement(this.sql,this.getScrollType(dbname),this.getCursorType(dbname));
		if(isquery)
		{
			int fetchsize = this.pool.getJDBCPoolMetadata().getQueryfetchsize();
			if(fetchsize > 0)
				pstmt.setFetchSize(fetchsize);
		}
		this.statements.add(pstmt);
		return pstmt;
	}
	
	public PreparedStatement prepareQueryStatement() throws SQLException {
//		PreparedStatement pstmt = this.con.prepareStatement(this.sql,this.getScrollType(dbname),this.getCursorType(dbname));
//		int fetchsize = this.pool.getJDBCPoolMetadata().getQueryfetchsize();
//		if(fetchsize > 0)
//			pstmt.setFetchSize(fetchsize);
//		this.statements.add(pstmt);
//		return pstmt;
		return _prepareStatement(true);
	}

	private PreparedStatement _prepareStatement(String sql,boolean isquery) throws SQLException {
		/**
		 * must be removed.
		 */
		if(dbname == null)
			dbname = SQLManager.getInstance().getDefaultDBName();
		sql = this.interceptorInf.convertSQL(sql, this.dbadapter.getDBTYPE(), dbname);
		PreparedStatement pstmt = this.con.prepareStatement(sql,this.getScrollType(dbname),this.getCursorType(dbname));
		if(isquery)
		{
			int fetchsize = this.pool.getJDBCPoolMetadata().getQueryfetchsize();
			if(fetchsize > 0)
				pstmt.setFetchSize(fetchsize);
		}
		this.statements.add(pstmt);
		return pstmt;
	}
	public PreparedStatement prepareStatement(String sql) throws SQLException {
//		/**
//		 * must be removed.
//		 */
//		if(dbname == null)
//			dbname = SQLManager.getInstance().getDefaultDBName();
//		sql = this.interceptorInf.convertSQL(sql, this.dbadapter.getDBTYPE(), dbname);
//		PreparedStatement pstmt = this.con.prepareStatement(sql,this.getScrollType(dbname),this.getCursorType(dbname));
//		this.statements.add(pstmt);
//		return pstmt;
		return _prepareStatement(sql,false);
	}
	
	public PreparedStatement prepareStatement(String sql,boolean getgenkeys) throws SQLException {
		if(dbname == null)
			dbname = SQLManager.getInstance().getDefaultDBName();
		sql = this.interceptorInf.convertSQL(sql, this.dbadapter.getDBTYPE(), dbname);
		if(!getgenkeys)
		{
			/**
			 * must be removed.
			 */
			
			PreparedStatement pstmt = this.con.prepareStatement(sql,this.getScrollType(dbname),this.getCursorType(dbname));
			this.statements.add(pstmt);
			return pstmt;
		}
		else
		{
			if(this.RETURN_GENERATED_KEYS)
			{
				PreparedStatement pstmt = this.con.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
				this.statements.add(pstmt);
				return pstmt;
			}
			else
			{
				
				PreparedStatement pstmt = this.con.prepareStatement(sql,this.getScrollType(dbname),this.getCursorType(dbname));
				this.statements.add(pstmt);
				return pstmt;
			}
			
		}
	}

	public PreparedStatement preparePagineStatement(boolean showsql) throws SQLException {
		paginesql = new PagineSql(this.sql,true);
		if (this.rownum == null) {
			// if (this.isRobotQuery(prepareDBName))
			paginesql = getDBPagineSql(true);
			if(showsql && paginesql != null)
			{
				log.debug("Execute JDBC prepared pagine query statement:"+paginesql.getSql());
			}
		} else {
			paginesql = getDBPagineSqlForOracle(true);
		}
		
		return _prepareStatement(paginesql.getSql(),true);

	}

	public PreparedStatement prepareCountStatement(boolean showsql) throws SQLException {
		String countsql = null;
		if(this.totalsizesql == null)
		{
			countsql = countSql();
		}
		else
		{
			countsql = totalsizesql;
		}
		if(showsql)
		{
			log.debug("Execute JDBC prepared pagine query count statement:"+countsql);
		}
		return prepareStatement(countsql);
	}
	

	/**
	 * 分页查询，构建查询数据库总记录数的sql语句
	 * 
	 * @param sql
	 *            分页查询的sql语句
	 * @return 查询总记录数的sql语句
	 * @throws SQLException
	 */
	public String countSql() throws SQLException {

		// tempSql = sql.trim().toLowerCase();
		// if(!tempSql.startsWith("select "))
		// throw new SQLException("Get count sql error:Statement [" + sql + "]
		// is not a select statement");
		//
		// String fromPart = sql.substring(tempSql.indexOf("from"));
		String selectCount = "select count(1) from (" + sql
				+ ") countsql_daul_forpagination";

		return selectCount;

	}

	public void addResultSet(ResultSet rs) {
		if (rs != null)
			resultSets.add(rs);
	}

	public void absolute(ResultSet rs) throws SQLException {
//		if (paginesql.getSql().equals(getSql()) && rs != null) {
//			if (getOffset() > 0L) {
//				rs.absolute((int) getOffset());
//			}
//		}
		if (!paginesql.isRebuilded() && rs != null) {
			if (getOffset() > 0L) {
				rs.absolute((int) getOffset());
			}
		}
	}

	public PagineSql paginesql(boolean prepared) {
		if (paginesql == null) {
			
			if (this.rownum == null) {
				if (isRobotquery())
					paginesql = getDBPagineSql(prepared);
				else
					paginesql = new PagineSql(getSql(),prepared);
			} else {
				paginesql = getDBPagineSqlForOracle(prepared);
			}
		}
		return paginesql;
	}

//	public void errorHandle(Exception sqle) throws SQLException {
//		
//		if (tx != null && !outcon) {
//			try {
//				tx.setRollbackOnly();
//			} catch (Exception ei) {
//
//			}
//		} else if (!outcon && con != null) {
//			if (this.needTransaction) {
//
//				try {
//					con.rollback();
//				} catch (Exception e) {
//
//				}
//				try {
//					con.setAutoCommit(this.oldautocommit);
//				} catch (Exception e) {
//
//				}
//			}
//		}
//
//		// if(!outcon)
//		// {
//		// if (tx == null) {
//		// // if (!isAutoCommit())
//		// // preparedCon.rollback();
//		// } else {
//		// try {
//		// tx.setRollbackOnly();
//		// } catch (IllegalStateException e1) {
//		// // TODO Auto-generated catch block
//		// e1.printStackTrace();
//		// } catch (SystemException e1) {
//		// // TODO Auto-generated catch block
//		// e1.printStackTrace();
//		// }
//		// }
//		// }
//		// log.error("预编译执行报错", sqle);
//		// throw e;
//		if (sqle instanceof SQLException)
//			throw (SQLException) sqle;
//		else
//			throw new NestedSQLException(sqle.getMessage(), sqle);
//
//	}
	
	public void errorHandle(Exception sqle) throws SQLException {
		
		if(outcon )//使用外部链接
		{
			
			if(tx != null && this.con != null && this.con instanceof TXConnection)
			{
				try {
					tx.setRollbackOnly();
				} catch (Exception ei) {

				}
			}
		}
		else//没有使用外部链接
		{
			if (tx != null) {
				try {
					tx.setRollbackOnly();
				} catch (Exception ei) {
	
				}
			} 
			else if (con != null) {
				if (this.needTransaction) {	
					try {
						con.rollback();
					} catch (Exception e) {
	
					}
					try {
						con.setAutoCommit(this.oldautocommit);
					} catch (Exception e) {
	
					}
				}
			}
		}

		
		if (sqle instanceof SQLException)
			throw (SQLException) sqle;
		else if(sqle instanceof TransactionException)
		{
			Throwable e = ((TransactionException)sqle).getCause();
			if(e == null)
			{
				throw new NestedSQLException(sqle.getMessage(), sqle);
			}
			else if(e instanceof SQLException)
				throw (SQLException)e;
			else
				throw new NestedSQLException(sqle.getMessage(), sqle);
				
		}
		else
			throw new NestedSQLException(sqle.getMessage(), sqle);

	}

	public void closeResultSet(ResultSet rs) {
		try {
			if (rs != null) {
				rs.close();
			}
		} catch (SQLException e) {
		}
		// //added by biaoping.yin on 2005.03.28
		catch (Exception e) {
			// System.out.println("");
		}
		rs = null;
	}

	/**
	 * Closes the given statement. It is here to get rid of the extra try block
	 * in finally blocks that need to close statements
	 * 
	 * @param statement
	 *            the statement to be closed. may be null
	 */
	public void closeStatement(Statement statement) {
		try {
			if (statement != null) {
				statement.close();
			}
		} catch (SQLException e) {
		}
		statement = null;
	}

	public void dofinally() {
		try
		{
			if (resultSets != null && resultSets.size() > 0) {
				for (int i = 0; i < resultSets.size(); i++) {
					try
					{
						
						closeResultSet((ResultSet) resultSets.get(i));
					}
					catch(Exception e)
					{
						
					}
				}
				resultSets = null;
				
			}
			if (this.statements != null && this.statements.size() > 0) {
				for (int i = 0; i < statements.size(); i++) {
					try
					{	
						
						closeStatement((Statement) statements.get(i));
					}
					catch(Exception e)
					{
						
					}
					
				}
				statements = null;
				
			}
			
			// try {
			// JDBCPool.closeResources(s, res);
			//
			// s = null;
			// res = null;
			// }
			//
			// catch (Exception e) {
			// // e.printStackTrace();
			// }
			//
			// try {
			// JDBCPool.closeResources(s1, rs);
			//
			// s1 = null;
			// rs = null;
			// }
			//
			// catch (Exception e) {
			// // e.printStackTrace();
			// }
			//
			try { // System.out.println("dbutil doJDBC release poolman
				// connection:" + con);
				if (!outcon) {
					if (tx == null && con != null) {
						con.close();
						con = null;
	
					}
				}
				con = null;
			} catch (Exception e) {
				// e.printStackTrace();
			}
		}
		catch(Exception e)
		{
			
		}
	}

	public String getSql() {
		return sql;
	}

	public boolean isGoNative() {
		return goNative;
	}

	public long getOffset() {
		return offset;
	}

	public int getMaxsize() {
		return maxsize;
	}

	public boolean isRobotquery() {
		return robotquery;
	}

	public void setOffset(long offset) {
		this.offset = offset > 0 ? offset : 0;

	}

	public void commit() throws SQLException {
		if (!outcon) {
			if (tx == null) {
				// 如果是手动提交数据库模式，所有的操作完成后调用batchCon提交方法提交操作
				if (this.needTransaction)
					con.commit();
			}
		}
	}

	public Record[] buildCommonResult(Statement s) throws SQLException {
		Record[] results = new Record[1];
		results[0] = new Record(1);
		int num;
		String str = "Rows Affected";
		switch (num = s.getUpdateCount()) {
		case 0:
			results[0].put(str, "No rows affected");
			break;
		case 1:
			results[0].put(str, "1 row affected");
			break;
		default:
			results[0].put(str, num + " rows affected");
		}
		return results;
	}

	public Object[] buildResultForObjectArray(ResultSet res, int containersize,
			boolean ispagine, Class objectType, RowHandler rowHandler)
			throws SQLException {
		int rowcount = 0;
		if (meta == null)
			this.cacheResultSetMetaData(res,ispagine);

		Object[] results = new Object[containersize];

		boolean go = true;
		if (ispagine)
			go = res.next() && rowcount < getMaxsize();
		else
			go = res.next();

		// 从结果集中获取当前游标后maxsize条记录
		
		boolean ismap = Map.class.isAssignableFrom(objectType);
		ClassInfo beanInfo = ClassUtil.getClassInfo(objectType);
		while (go) {

			if (rowcount == results.length) {
				Object[] temp = new Object[results.length + 10];
				for (int t = 0; t < results.length; t++) {
					temp[t] = results[t];
				}
				results = temp;
			}

			Object record = ResultMap.buildValueObject(res, objectType, this,
					rowHandler,ismap,beanInfo);
			results[rowcount] = record;
			rowcount++;
			if (ispagine)
				go = res.next() && rowcount < getMaxsize();
			else
				go = res.next();

		}

		if (results[0] == null) {
			// modified by biaoping.yin on 2005.03.28
			return null;

		}

		Object[] temp = null;
		for (int i = 0; i < results.length; i++) {
			if (results[i] == null) {
				temp = new Object[i];
				break;
			} else {
				temp = new Object[results.length];
			}
		}
		for (int i = 0; i < temp.length; i++) {
			temp[i] = results[i];
		}
		results = temp;
		temp = null;
		return results;
	}

	public <T> List<T> buildResultForList(ResultSet res, int containersize,
			boolean ispagine, Class<T> objectType, RowHandler rowHandler)
			throws SQLException {
		int rowcount = 0;
		if (meta == null)
			this.cacheResultSetMetaData(res,ispagine);

		List<T> results = new ArrayList<T>(containersize);
		// Object[] results = new Object[containersize];

		boolean go = true;
		if (ispagine)
			go = res.next() && rowcount < getMaxsize();
		else
			go = res.next();
		// 从结果集中获取当前游标后maxsize条记录
		
		boolean ismap = Map.class.isAssignableFrom(objectType);
		ClassInfo beanInfo = ClassUtil.getClassInfo(objectType);
		while (go) {
			T record = ResultMap.buildValueObject(res, objectType, this,
					rowHandler,ismap,beanInfo);
			results.add(record);
			rowcount++;
			if (ispagine)
				go = res.next() && rowcount < getMaxsize();
			else
				go = res.next();

		}

		return results;
	}
	
	public int buildResult(ResultSet res, 
            boolean ispagine, RowHandler rowHandler)
            throws SQLException {
        rowcount = 0;
        if (meta == null)
            this.cacheResultSetMetaData(res,ispagine);

        
        

        boolean go = true;
        if (ispagine)
            go = res.next() && rowcount < getMaxsize();
        else
            go = res.next();
        // 从结果集中获取当前游标后maxsize条记录
        while (go) {
            ResultMap.buildRecord(res, this,
                    rowHandler,this.dbadapter);
            rowcount++;
            if (ispagine)
                go = res.next() && rowcount < getMaxsize();
            else
                go = res.next();

        }

        return rowcount;
    }
	private int rowcount = 0;
	public String buildResultForXml(ResultSet res, int containersize,
			boolean ispagine, Class objectType, RowHandler rowHandler)
			throws SQLException {
		StringBuffer results = new StringBuffer();
		XMLRowHandler xhdl = null;
		try
		{
        		boolean isxmlhandler = false;        		
        		if(rowHandler == null)
        		{
        		    xhdl = new XMLRowHandler();
        		    xhdl.init(this,this.getMeta(), this.getDbname());
        		    rowHandler = xhdl;
        		    isxmlhandler = true;
        		}
        		else if(rowHandler instanceof XMLRowHandler)
        		{
        		    isxmlhandler = true;
        		    xhdl = (XMLRowHandler)rowHandler;
        		    xhdl.init(this,this.getMeta(), this.getDbname());
        		}
        		
        		
        		    
        //		boolean isxmlhandler = rowHandler != null && rowHandler instanceof XMLRowHandler; 
        		
        		if(!isxmlhandler) //行处理器不是从XMLRowHandler处理器继承时进入这个分支
        		{
        		    results.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n");
                            results.append("<records>\r\n");
        		    
        		}
        		else
        		{
        		    
                            results.append("<?xml version=\"")
                                   .append(xhdl.getVersion())
                                   .append("\" encoding=\"")
                                   .append(xhdl.getEncoding())
                                   .append("\"?>\r\n");
                            results.append("<")
                                   .append(xhdl.getRootName())
                                   .append(">\r\n");
        		}
        		rowcount = 0;
        		if (meta == null)
        			this.cacheResultSetMetaData(res,ispagine);
        		boolean go = true;
        		if (ispagine)
        			go = res.next() && rowcount < getMaxsize();
        		else
        			go = res.next();
        		// 从结果集中获取当前游标后maxsize条记录
        		while (go) {
        			StringBuffer record = ResultMap.buildSingleRecordXMLString(res,
        					this, rowHandler,this.dbadapter);
        			results.append(record);
        			rowcount++;
        			if (ispagine)
        				go = res.next() && rowcount < getMaxsize();
        			else
        				go = res.next();
        
        		}
        		if(!isxmlhandler)
        		{
        		    results.append("\r\n").append("</records>");
        		}
        		else
        		{
        		    results.append("\r\n").append("</")
                                   .append(xhdl.getRootName())
                                   .append(">");
        		}
		}
		finally
		{
		    if(xhdl != null)
		        xhdl.destroy();
		}
		return results.toString();
	}

	public Record[] buildResult(ResultSet res, int containersize,
			boolean ispagine) throws SQLException {
		int rowcount = 0;
		if (meta == null)
			this.cacheResultSetMetaData(res,ispagine);

		Record[] results = new Record[containersize];

		boolean go = true;
		if (ispagine)
			go = res.next() && rowcount < getMaxsize();
		else
			go = res.next();

		// 从结果集中获取当前游标后maxsize条记录
		while (go) {

			if (rowcount == results.length) {
				Record[] temp = new Record[results.length + 10];
				for (int t = 0; t < results.length; t++) {
					temp[t] = results[t];
				}
				results = temp;
			}

			Record record = ResultMap.buildMap(res, this,this.dbadapter);

			results[rowcount] = record;
			rowcount++;
			if (ispagine)
				go = res.next() && rowcount < getMaxsize();
			else
				go = res.next();

		}

		if (results[0] == null) {
			// modified by biaoping.yin on 2005.03.28
			return null;

		}

		Record[] temp = null;
		for (int i = 0; i < results.length; i++) {
			if (results[i] == null) {
				temp = new Record[i];
				break;
			} else {
				temp = new Record[results.length];
			}
		}
		for (int i = 0; i < temp.length; i++) {
			temp[i] = results[i];
		}
		results = temp;
		temp = null;
		return results;
	}
	
	public <T> ResultMap buildResultMap(ResultSet res,
									   Class<T> objectType,
									   RowHandler rowhandler,
									   int containersize,
									   boolean ispagine, 
									   int result_type) throws SQLException
	{
		ResultMap resultMap = new ResultMap();
		if(result_type == ResultMap.type_maparray || 
				(result_type == ResultMap.type_objectarray 
						&& Map.class.isAssignableFrom(objectType)))
		{
			Record[] results = buildResult(res,containersize,ispagine);
			resultMap.setCommonresult(results);
			if(results != null)
				resultMap.setSize(results.length);
		}
		else if(result_type == ResultMap.type_list)
		{
			List<T> results = buildResultForList(res, containersize, ispagine, objectType, rowhandler);
			resultMap.setCommonresult(results);
			if(results != null)
				resultMap.setSize(results.size());
		}
		else if(result_type == ResultMap.type_objcet) //分页时，不能指定返回值类型为type_objcet
		{
			if(!ispagine)
			{
				if(res.next())
				{
					ClassInfo beanInfo = ClassUtil.getClassInfo(objectType);
					boolean ismap = Map.class.isAssignableFrom(objectType);
					T result = ResultMap.buildValueObject(res, objectType, this, rowhandler,ismap,beanInfo);
					resultMap.setCommonresult(result);
					if(result != null)
					{
						resultMap.setSize(1);
					}
				}
			}
//			throw new SQLException("");
		}
		else if(result_type == ResultMap.type_objectarray)
		{
			Object[] results = buildResultForObjectArray(res, containersize, ispagine, objectType, rowhandler);
			resultMap.setCommonresult(results);
			if(results != null)
			{
				resultMap.setSize(results.length);
			}
		}
		else if(result_type == ResultMap.type_xml)
		{
		        if(rowhandler == null)
		        {
		            rowhandler = new XMLRowHandler();
		        }
			resultMap.setCommonresult(buildResultForXml(res, containersize, ispagine, objectType, rowhandler));
			resultMap.setSize(getRowcount());
		}
		else if(result_type == ResultMap.type_null)
        {
                if(rowhandler == null)
                {
                    throw new NestedSQLException("rowhandler == null");
                }
                
//            resultMap.setCommonresult(buildResultForXml(res, containersize, ispagine, objectType, rowhandler));
            this.rowcount = buildResult(res,  ispagine,  rowhandler);
            resultMap.setSize(getRowcount());
        }
		return resultMap;
	}

	public static long rebuildOffset(long offset,int pagesize,long totalSize) {
//		if (totalSize > 0) {
//			if (getOffset() > 0) {
//				if (totalSize > getOffset())
//					;
//
//				else {
//					setOffset(totalSize - getMaxsize() - 1);
//
//				}
//
//			} else {
//				setOffset(0);
//			}
//
//		}
		long offset_ = offset;
		if (totalSize > 0) {
			if (offset > 0) {
				if (totalSize > offset)
					;

				else {
					offset_ = totalSize - pagesize - 1;

				}

			} else {
				offset_ = 0;
			}

		}
		return offset_;

	}
	
	public long rebuildOffset(long totalSize) {
//		if (totalSize > 0) {
//			if (getOffset() > 0) {
//				if (totalSize > getOffset())
//					;
//
//				else {
//					setOffset(totalSize - getMaxsize() - 1);
//
//				}
//
//			} else {
//				setOffset(0);
//			}
//
//		}
		this.setOffset(rebuildOffset(this.getOffset(),this.getMaxsize(),totalSize));
		return this.getOffset();

	}
	
	public void resetPostion( PreparedStatement statement,int startidx,int endidx,long offset) throws SQLException
	{
		this.dbadapter.resetPostion( statement,startidx,endidx,this.getOffset(),this.getMaxsize());
	}

	/**
	 * 获取指定数据库的分页数据sql语句
	 * 
	 * @param dbName
	 * @param sql
	 * @return
	 */
	public PagineSql getDBPagineSql(boolean prepared) {
		if(this.pagineOrderBy == null || pagineOrderBy.trim().equals(""))
			return this.dbadapter
					.getDBPagineSql(sql, offset, maxsize,prepared);
		else
			return this.dbadapter
					.getDBPagineSql(sql, offset, maxsize,prepared,pagineOrderBy);

	}

	/**
	 * 获取指定数据库的分页数据sql语句，通过oracle的高效查询语句
	 * 
	 * @param dbName
	 * @param sql
	 * @return
	 */
	public PagineSql getDBPagineSqlForOracle(boolean prepared) {
		return this.dbadapter
				.getOracleDBPagineSql(sql, offset, maxsize, rownum, prepared);

	}

	public String getDbname() {
		return dbname;
	}

	public Connection getCon() {
		return con;
	}

	public boolean isPrepared() {
		return prepared;
	}

	public void cacheResultSetMetaData(ResultSet rs,boolean pagine) throws SQLException {
//		JDBCPool pool = DBUtil.getPool(this.dbname);
		String key = getSql();
		if(pagine)
			key = key + "__pagine" ;
		if(pool.getJDBCPoolMetadata().cachequerymetadata())
		{
			meta = this.newsqlinfo.getPoolManResultSetMetaData(dbname, key, rs.getMetaData());			
		}
		else
		{
			meta = PoolManResultSetMetaData.getCopy(rs.getMetaData());
		}
		
	}

	public PoolManResultSetMetaData getMeta() {
		return meta;
	}

	// public String[] getFields() {
	// return fields;
	// }

	public Statement createStatement() throws SQLException {
		Statement stmt = this.con.createStatement(this.getScrollType(dbname),this.getCursorType(dbname));
//	    Statement stmt = this.con.createStatement();
		this.statements.add(stmt);
		return stmt;
	}

	public CallableStatement prepareCallableStatement() throws SQLException {
		CallableStatement cstmt = this.con.prepareCall(this.getSql(),this.getScrollType(dbname),this.getCursorType(dbname));
		this.statements.add(cstmt);
		return cstmt;
	}

	public static void main(String[] args) {
		Hashtable[] t = new Record[10];
		Record[] tt = (Record[]) t;
		System.out.println(t == tt);
	}

	public int getRowcount() {
		return rowcount;
	}
	
	public int getCursorType(String dbname)
	{
	    try
	    {
//	        JDBCPool pool = ((JDBCPool)SQLUtil.getSQLManager().getPool(dbname));
	        
	        return this.dbadapter.getCusorType(pool.getDriver());
	    }
	    catch(Exception e)
	    {
	        log.error(dbname,e);
	        JDBCPool pool = ((JDBCPool)SQLUtil.getSQLManager().getPool(null));
	        return pool.getDbAdapter().getCusorType(pool.getDriver());
	    }
	}
	
//	public int getScrollType(String dbname)
//    {
//        try
//        {
//            JDBCPool pool = ((JDBCPool)SQLUtil.getSQLManager().getPool(dbname));
//            
//            return pool.getDbAdapter().getSCROLLType(pool.getDriver());
//        }
//        catch(Exception e)
//        {
//            log.error(e);
//            JDBCPool pool = ((JDBCPool)SQLUtil.getSQLManager().getPool(null));
//            return pool.getDbAdapter().getSCROLLType(pool.getDriver());
//        }
//    }
	
	public int getScrollType(String dbname)
    {
        try
        {
//            JDBCPool pool = ((JDBCPool)SQLUtil.getSQLManager().getPool(dbname));
            
            return this.dbadapter.getSCROLLType(pool.getDriver());
        }
        catch(Exception e)
        {
            log.error(dbname,e);
            JDBCPool pool = ((JDBCPool)SQLUtil.getSQLManager().getPool(null));
            return pool.getDbAdapter().getSCROLLType(pool.getDriver());
        }
    }

	public long getTotalsize() {
		return totalsize;
	}

	public void setTotalsize(long totalsize) {
		this.totalsize = totalsize;
	}

	public String getTotalsizesql() {
		return totalsizesql;
	}

	public void setTotalsizesql(String totalsizesql) {
		this.totalsizesql = totalsizesql;
	}

	public PagineSql getPaginesql() {
		return paginesql;
	}

//	public boolean isNeadGetGenerateKeys() {
//		return neadGetGenerateKeys;
//	}
	private boolean RETURN_GENERATED_KEYS;
	public void setRETURN_GENERATED_KEYS(boolean rETURN_GENERATED_KEYS) {
		this.RETURN_GENERATED_KEYS = rETURN_GENERATED_KEYS;
	}

	public boolean isRETURN_GENERATED_KEYS() {
		return RETURN_GENERATED_KEYS;
	}

	public DB getDbadapter() {
		return dbadapter;
	}

}
