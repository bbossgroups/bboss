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
package com.frameworkset.orm.transaction;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Date;
import java.sql.ParameterMetaData;
import java.sql.PreparedStatement;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;

/**
 * 
 * 
 * <p>Title: InnerPreparedStatement.java</p>
 *
 * <p>Description: </p>
 *
 * <p>
 * bboss workgroup
 * </p>
 * <p>
 * Copyright (c) 2007
 * </p>
 * 
 * @Date 2009-6-1 下午08:58:51
 * @author biaoping.yin
 * @version 1.0
 */
class InnerPreparedStatement extends InnerStatement implements
		PreparedStatement {
	PreparedStatement pstmt;
	public InnerPreparedStatement(PreparedStatement stmt) {
		super(stmt);
		this.pstmt = stmt;
		// TODO Auto-generated constructor stub
	}

	public void addBatch() throws SQLException {
		this.pstmt.addBatch();

	}

	public void clearParameters() throws SQLException {
		this.pstmt.clearParameters();

	}

	public boolean execute() throws SQLException {
		// TODO Auto-generated method stub
		return this.pstmt.execute();
	}

	public ResultSet executeQuery() throws SQLException {
		ResultSet rs = this.pstmt.executeQuery();
		this.results.add(rs);
		return rs;
//		return this.pstmt.executeQuery();
	}

	public int executeUpdate() throws SQLException {
		// TODO Auto-generated method stub
		return this.pstmt.executeUpdate();
	}

	public ResultSetMetaData getMetaData() throws SQLException {
		// TODO Auto-generated method stub
		return this.pstmt.getMetaData();
	}

	public ParameterMetaData getParameterMetaData() throws SQLException {
		// TODO Auto-generated method stub
		return this.pstmt.getParameterMetaData();
	}

	public void setArray(int i, Array x) throws SQLException {
		this.pstmt.setArray(i, x);

	}

	public void setAsciiStream(int parameterIndex, InputStream x, int length)
			throws SQLException {
		this.pstmt.setAsciiStream(parameterIndex, x, length);

	}

	public void setBigDecimal(int parameterIndex, BigDecimal x)
			throws SQLException {
		this.pstmt.setBigDecimal(parameterIndex, x);

	}

	public void setBinaryStream(int parameterIndex, InputStream x, int length)
			throws SQLException {
		this.pstmt.setBinaryStream(parameterIndex, x, length);

	}

	public void setBlob(int i, Blob x) throws SQLException {
		this.pstmt.setBlob(i, x);

	}

	public void setBoolean(int parameterIndex, boolean x) throws SQLException {
		this.pstmt.setBoolean(parameterIndex, x);

	}

	public void setByte(int parameterIndex, byte x) throws SQLException {
		this.pstmt.setByte(parameterIndex, x);

	}

	public void setBytes(int parameterIndex, byte[] x) throws SQLException {
		this.pstmt.setBytes(parameterIndex, x);

	}

	public void setCharacterStream(int parameterIndex, Reader reader, int length)
			throws SQLException {
		this.pstmt.setCharacterStream(parameterIndex, reader, length);

	}

	public void setClob(int i, Clob x) throws SQLException {
		this.pstmt.setClob(i, x);

	}

	public void setDate(int parameterIndex, Date x) throws SQLException {
		this.pstmt.setDate(parameterIndex, x);

	}

	public void setDate(int parameterIndex, Date x, Calendar cal)
			throws SQLException {
		this.pstmt.setDate(parameterIndex, x, cal);

	}

	public void setDouble(int parameterIndex, double x) throws SQLException {
		this.pstmt.setDouble(parameterIndex, x);

	}

	public void setFloat(int parameterIndex, float x) throws SQLException {
		this.pstmt.setFloat(parameterIndex, x);

	}

	public void setInt(int parameterIndex, int x) throws SQLException {
		this.pstmt.setInt(parameterIndex, x);

	}

	public void setLong(int parameterIndex, long x) throws SQLException {
		this.pstmt.setLong(parameterIndex, x);

	}

	public void setNull(int parameterIndex, int sqlType) throws SQLException {
		this.pstmt.setNull(parameterIndex, sqlType);

	}

	public void setNull(int paramIndex, int sqlType, String typeName)
			throws SQLException {
		this.pstmt.setNull(paramIndex, sqlType, typeName);

	}

	public void setObject(int parameterIndex, Object x) throws SQLException {
		this.pstmt.setObject(parameterIndex, x);

	}

	public void setObject(int parameterIndex, Object x, int targetSqlType)
			throws SQLException {
		this.pstmt.setObject(parameterIndex, x, targetSqlType);

	}

	public void setObject(int parameterIndex, Object x, int targetSqlType,
			int scale) throws SQLException {
		this.pstmt.setObject(parameterIndex, x, targetSqlType, scale);

	}

	public void setRef(int i, Ref x) throws SQLException {
		this.pstmt.setRef(i, x);

	}

	public void setShort(int parameterIndex, short x) throws SQLException {
		this.pstmt.setShort(parameterIndex, x);

	}

	public void setString(int parameterIndex, String x) throws SQLException {
		this.pstmt.setString(parameterIndex, x);

	}

	public void setTime(int parameterIndex, Time x) throws SQLException {
		this.pstmt.setTime(parameterIndex, x);

	}

	public void setTime(int parameterIndex, Time x, Calendar cal)
			throws SQLException {
		this.pstmt.setTime(parameterIndex, x, cal);

	}

	public void setTimestamp(int parameterIndex, Timestamp x)
			throws SQLException {
		this.pstmt.setTimestamp(parameterIndex, x);

	}

	public void setTimestamp(int parameterIndex, Timestamp x, Calendar cal)
			throws SQLException {
		this.pstmt.setTimestamp(parameterIndex, x, cal);

	}

	public void setURL(int parameterIndex, URL x) throws SQLException {
		this.pstmt.setURL(parameterIndex, x);

	}

	public void setUnicodeStream(int parameterIndex, InputStream x, int length)
			throws SQLException {
		this.pstmt.setUnicodeStream(parameterIndex, x, length);

	}

}