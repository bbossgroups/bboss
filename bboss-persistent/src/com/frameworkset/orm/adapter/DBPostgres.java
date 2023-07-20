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

import com.frameworkset.common.poolman.PreparedDBUtil;
import com.frameworkset.common.poolman.util.DBOptions;
import com.frameworkset.common.poolman.util.JDBCPoolMetaData;
import com.frameworkset.orm.platform.PlatformPostgresqlImpl;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * This is used to connect to PostgresQL databases.
 *
 * <a href="http://www.postgresql.org/">http://www.postgresql.org/</a>
 *
 * @author <a href="mailto:hakan42@gmx.de">Hakan Tandogan</a>
 * @author <a href="mailto:hps@intermeta.de">Henning P. Schmiedehausen</a>
 * @version $Id: DBPostgres.java,v 1.19 2005/05/08 15:56:49 tfischer Exp $
 */
public class DBPostgres extends DB {

	/**
	 * A specialized date format for PostgreSQL.
	 */
	public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

	private SimpleDateFormat sdf = null;

//    /**
//     * Empty constructor.
//     */
//    protected DBPostgres()
//    {
//        sdf = new SimpleDateFormat(DATE_FORMAT);
//    }

	public DBPostgres() {
		sdf = new SimpleDateFormat(DATE_FORMAT);
		this.platform = new PlatformPostgresqlImpl();
	}

	/**
	 * This method is used to ignore case.
	 *
	 * @param in The string to transform to upper case.
	 * @return The upper case string.
	 */
	public String toUpperCase(String in) {
		String s = new StringBuilder("UPPER(").append(in).append(")").toString();
		return s;
	}

	/**
	 * This method is used to ignore case.
	 *
	 * @param in The string whose case to ignore.
	 * @return The string in a case that can be ignored.
	 */
	public String ignoreCase(String in) {
		String s = new StringBuilder("UPPER(").append(in).append(")").toString();
		return s;
	}

	/**
	 * @see com.frameworkset.orm.adapter.DB#getIDMethodType()
	 */
	public String getIDMethodType() {
		return SEQUENCE;
	}

	/**
	 * @param name The name of the field (should be of type
	 *             <code>String</code>).
	 * @return SQL to retreive the next database key.
	 * @see com.frameworkset.orm.adapter.DB#getIDMethodSQL(Object)
	 */
	public String getIDMethodSQL(Object name) {
		return ("select nextval('" + name + "')");
	}

	/**
	 * Locks the specified table.
	 *
	 * @param con   The JDBC connection to use.
	 * @param table The name of the table to lock.
	 * @throws SQLException No Statement could be created or executed.
	 */
	public void lockTable(Connection con, String table) throws SQLException {
	}

	/**
	 * Unlocks the specified table.
	 *
	 * @param con   The JDBC connection to use.
	 * @param table The name of the table to unlock.
	 * @throws SQLException No Statement could be created or executed.
	 */
	public void unlockTable(Connection con, String table) throws SQLException {
	}

	/**
	 * This method is used to chek whether the database natively
	 * supports limiting the size of the resultset.
	 *
	 * @return True.
	 */
	public boolean supportsNativeLimit() {
		return true;
	}

	/**
	 * This method is used to chek whether the database natively
	 * supports returning results starting at an offset position other
	 * than 0.
	 *
	 * @return True.
	 */
	public boolean supportsNativeOffset() {
		return true;
	}

	/**
	 * This method is used to chek whether the database supports
	 * limiting the size of the resultset.
	 *
	 * @return LIMIT_STYLE_POSTGRES.
	 */
	public int getLimitStyle() {
		return DB.LIMIT_STYLE_POSTGRES;
	}

	/**
	 * Override the default behavior to associate b with null?
	 *
	 * @see DB#getBooleanString(Boolean)
	 */
	public String getBooleanString(Boolean b) {
		return (b == null) ? "FALSE" : (Boolean.TRUE.equals(b) ? "TRUE" : "FALSE");
	}

	/**
	 * This method overrides the JDBC escapes used to format dates
	 * using a <code>DateFormat</code>.
	 * <p>
	 * This generates the timedate format defined in
	 * http://www.postgresql.org/docs/7.3/static/datatype-datetime.html
	 * which defined PostgreSQL dates as YYYY-MM-DD hh:mm:ss
	 *
	 * @param date the date to format
	 * @return The properly formatted date String.
	 */
	public String getDateString(Date date) {
		StringBuilder dateBuf = new StringBuilder();
		char delim = getStringDelimiter();
		dateBuf.append(delim);
		dateBuf.append(sdf.format(date));
		dateBuf.append(delim);
		return dateBuf.toString();
	}

	/**
	 * 获取指定数据的分页数据sql语句
	 *
	 * @param sql
	 * @return
	 */
	public PagineSql getDBPagineSql(String sql, long offset, int maxsize, boolean prepared) {

//		return new StringBuilder(sql).append(" limit ").append(offset).append(",").append(maxsize).toString();
		StringBuilder newsql = null;
		if (prepared)
			newsql = new StringBuilder().append(sql).append(" limit ?,?");
		else
			newsql = new StringBuilder().append(sql).append(" limit ").append(offset).append(",").append(maxsize);
		return new PagineSql(newsql.toString(), offset, (long) maxsize, offset, maxsize, prepared).setRebuilded(true);
	}

	public String getStringPagineSql(String sql) {
		StringBuilder newsql = new StringBuilder().append(sql).append(" limit ?,?");
		return newsql.toString();
	}

	public String getStringPagineSql(String schema, String tablename, String pkname, String columns) {
		StringBuilder sqlbuilder = new StringBuilder();
		sqlbuilder.append("SELECT ");
		if (columns != null && !columns.equals("")) {
			sqlbuilder.append(columns);
		} else
			sqlbuilder.append("* ");
		sqlbuilder.append(" from   ");
		if (schema != null && !schema.equals(""))
			sqlbuilder.append(schema).append(".");
		sqlbuilder.append(tablename);
		sqlbuilder.append(" limit ?,?");
		return sqlbuilder.toString();
	}

	/**
	 * 获取指定数据的分页数据sql语句
	 *
	 * @param sql
	 * @return
	 */
	public PagineSql getDBPagineSql(String sql, long offset, int maxsize, boolean prepared, String orderBy) {

//			return new StringBuilder(sql).append(" limit ").append(offset).append(",").append(maxsize).toString();
		StringBuilder newsql = null;
		if (prepared)
			newsql = new StringBuilder().append(sql).append(" ").append(orderBy).append(" limit ?,?");
		else
			newsql = new StringBuilder().append(sql).append(" ").append(orderBy).append(" limit ").append(offset).append(",").append(maxsize);
		return new PagineSql(newsql.toString(), offset, (long) maxsize, offset, maxsize, prepared).setRebuilded(true);
	}

	public String getStringPagineSql(String sql, String orderBy) {
		StringBuilder newsql = new StringBuilder().append(sql).append(" ").append(orderBy).append(" limit ?,?");
		return newsql.toString();
	}

	public String getStringPagineSql(String schema, String tablename, String pkname, String columns, String orderBy) {
		StringBuilder sqlbuilder = new StringBuilder();
		sqlbuilder.append("SELECT ");
		if (columns != null && !columns.equals("")) {
			sqlbuilder.append(columns);
		} else
			sqlbuilder.append("* ");
		sqlbuilder.append(" from   ");
		if (schema != null && !schema.equals(""))
			sqlbuilder.append(schema).append(".");
		sqlbuilder.append(tablename);
		sqlbuilder.append(" ").append(orderBy).append(" limit ?,?");
		return sqlbuilder.toString();
	}
	@Override
	public void handleConnection(Integer fetchSize, Connection con) throws SQLException {
		if (fetchSize != null && fetchSize > 0) {
			if(con.getAutoCommit())
				con.setAutoCommit(false);

		}
		return;
	}
    @Override
	public void recoverConnection(Integer fetchSize, Connection con,boolean oldautocommit) throws SQLException {
        if (fetchSize != null && fetchSize > 0) {
			con.setAutoCommit(oldautocommit);
		}
		return ;
	}
	@Override
	public void setObject(PreparedStatement statement, PreparedStatement statement_count, int parameterIndex, Object x) throws SQLException {
		if (x != null) {
			if (x instanceof Date) {
				Timestamp timestamp = new Timestamp(((Date) x).getTime());
				statement.setTimestamp(parameterIndex, timestamp);
				if (statement_count != null) {
					statement_count.setTimestamp(parameterIndex, timestamp);
				}
			} else if (x instanceof java.sql.Date) {
				statement.setDate(parameterIndex, (java.sql.Date) x);
				if (statement_count != null) {
					statement_count.setDate(parameterIndex, (java.sql.Date) x);
				}
			} else if (x instanceof Timestamp) {
				statement.setTimestamp(parameterIndex, (Timestamp) x);
				if (statement_count != null) {
					statement_count.setTimestamp(parameterIndex, (Timestamp) x);
				}
			} else if (x instanceof Short) {
				statement.setShort(parameterIndex, (Short) x);
				if (statement_count != null) {
					statement_count.setShort(parameterIndex, (Short) x);
				}
			} else if (x instanceof Double) {
				statement.setDouble(parameterIndex, (Double) x);
				if (statement_count != null) {
					statement_count.setDouble(parameterIndex, (Double) x);
				}
			} else if (x instanceof Float) {
				statement.setFloat(parameterIndex, (Float) x);
				if (statement_count != null) {
					statement_count.setFloat(parameterIndex, (Float) x);
				}
			} else {
				statement.setObject(parameterIndex, x);
				if (statement_count != null) {
					statement_count.setObject(parameterIndex, x);
				}

			}
		} else {
			statement.setNull(parameterIndex, Types.NULL);
			if (statement_count != null) {
				statement_count.setNull(parameterIndex, Types.NULL);
			}
		}
	}

	@Override
	public void setObject(PreparedDBUtil dbutil, int parameterIndex, Object x) throws SQLException {
		if (x != null) {
            if (x instanceof String ) {
                dbutil.setString(parameterIndex, (String) x);

            } else if (x instanceof Date) {
				Timestamp timestamp = new Timestamp(((Date) x).getTime());
				dbutil.setTimestamp(parameterIndex, timestamp);

			} else if (x instanceof java.sql.Date) {
				dbutil.setDate(parameterIndex, (java.sql.Date) x);

			} else if (x instanceof Timestamp) {
				dbutil.setTimestamp(parameterIndex, (Timestamp) x);

			} else if (x instanceof Short) {
				dbutil.setShort(parameterIndex, (Short) x);

			} else if (x instanceof Double) {
				dbutil.setDouble(parameterIndex, (Double) x);

			} else if (x instanceof Float) {
				dbutil.setFloat(parameterIndex, (Float) x);

			} else {
				dbutil._setObject(parameterIndex, x);


			}
		} else {
			dbutil.setNull(parameterIndex, Types.NULL);

		}

	}
    public Integer getFetchSize(DBOptions dbOptions, JDBCPoolMetaData jdbcPoolMetaData){
        Integer fetchsize = null;
        if(dbOptions != null && dbOptions.getFetchSize() != null && dbOptions.getFetchSize() != 0){
            fetchsize = dbOptions.getFetchSize();
        }
//        else{
//            fetchsize = jdbcPoolMetaData.getQueryfetchsize();
//        }
        return fetchsize;
    }

    public String getSchemaTableTableName(JDBCPoolMetaData info, String tablename) {
        return tablename;
    }

    public String getSchema(JDBCPoolMetaData info,Connection con) {
        if(con == null)
            return DB.NULL_SCHEMA;
        try {

            return con.getSchema();
        } catch (SQLException e) {
//            throw new RuntimeException(e);
            return DB.NULL_SCHEMA;
        }
    }
}
