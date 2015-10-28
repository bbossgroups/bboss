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
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Date;
import java.sql.NClob;
import java.sql.Ref;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLXML;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Map;

/**
 * 
 * 
 * <p>Title: InnerCallableStatement.java</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2007</p>
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
public class InnerCallableStatement extends InnerPreparedStatement implements CallableStatement {
	CallableStatement cstmt;
	public InnerCallableStatement(CallableStatement stmt) {
		super(stmt);
		this.cstmt = stmt;
		
	}

	public Array getArray(int i) throws SQLException {
		
		return cstmt.getArray(i);
	}

	public Array getArray(String parameterName) throws SQLException {
		
		return this.cstmt.getArray(parameterName);
	}

	public BigDecimal getBigDecimal(int parameterIndex) throws SQLException {
	
		return this.cstmt.getBigDecimal(parameterIndex);
	}

	public BigDecimal getBigDecimal(String parameterName) throws SQLException {
	
		return this.cstmt.getBigDecimal(parameterName);
	}

	public BigDecimal getBigDecimal(int parameterIndex, int scale)
			throws SQLException {
		
		return this.cstmt.getBigDecimal(parameterIndex, scale);
	}

	public Blob getBlob(int i) throws SQLException {

		return this.cstmt.getBlob(i);
	}

	public Blob getBlob(String parameterName) throws SQLException {

		return this.cstmt.getBlob(parameterName);
	}

	public boolean getBoolean(int parameterIndex) throws SQLException {

		return this.cstmt.getBoolean(parameterIndex);
	}

	public boolean getBoolean(String parameterName) throws SQLException {

		return this.cstmt.getBoolean(parameterName);
	}

	public byte getByte(int parameterIndex) throws SQLException {

		return this.cstmt.getByte(parameterIndex);
	}

	public byte getByte(String parameterName) throws SQLException {

		return this.cstmt.getByte(parameterName);
	}

	public byte[] getBytes(int parameterIndex) throws SQLException {

		return this.cstmt.getBytes(parameterIndex);
	}

	public byte[] getBytes(String parameterName) throws SQLException {

		return this.cstmt.getBytes(parameterName);
	}

	public Clob getClob(int i) throws SQLException {

		return this.cstmt.getClob(i);
	}

	public Clob getClob(String parameterName) throws SQLException {

		return this.cstmt.getClob(parameterName);
	}

	public Date getDate(int parameterIndex) throws SQLException {

		return this.cstmt.getDate(parameterIndex);
	}

	public Date getDate(String parameterName) throws SQLException {
		
		return this.cstmt.getDate(parameterName);
	}

	public Date getDate(int parameterIndex, Calendar cal) throws SQLException {
		
		return this.cstmt.getDate(parameterIndex, cal);
	}

	public Date getDate(String parameterName, Calendar cal) throws SQLException {
		
		return this.cstmt.getDate(parameterName, cal);
	}

	public double getDouble(int parameterIndex) throws SQLException {
		
		return this.cstmt.getDouble(parameterIndex);
	}

	public double getDouble(String parameterName) throws SQLException {
		
		return this.cstmt.getDouble(parameterName);
	}

	public float getFloat(int parameterIndex) throws SQLException {
		
		return this.cstmt.getFloat(parameterIndex);
	}

	public float getFloat(String parameterName) throws SQLException {
		
		return this.cstmt.getFloat(parameterName);
	}

	public int getInt(int parameterIndex) throws SQLException {
		
		return this.cstmt.getInt(parameterIndex);
	}

	public int getInt(String parameterName) throws SQLException {
		
		return this.cstmt.getInt(parameterName);
	}

	public long getLong(int parameterIndex) throws SQLException {
		 
		return this.cstmt.getLong(parameterIndex);
	}

	public long getLong(String parameterName) throws SQLException {
		 
		return this.cstmt.getLong(parameterName);
	}

	public Object getObject(int parameterIndex) throws SQLException {
		 
		return this.cstmt.getObject(parameterIndex);
	}

	public Object getObject(String parameterName) throws SQLException {
		 
		return this.cstmt.getObject(parameterName);
	}

//	public Object getObject(int i, Map map) throws SQLException {
//		 
//		return this.cstmt.getObject(i, map);
//	}
//
//	public Object getObject(String parameterName, Map map) throws SQLException {
//		 
//		return this.cstmt.getObject(parameterName, map);
//	}

	public Ref getRef(int i) throws SQLException {
		 
		return this.cstmt.getRef(i);
	}

	public Ref getRef(String parameterName) throws SQLException {
		 
		return this.cstmt.getRef(parameterName);
	}

	public short getShort(int parameterIndex) throws SQLException {
		 
		return this.cstmt.getShort(parameterIndex);
	}

	public short getShort(String parameterName) throws SQLException {
		 
		return this.cstmt.getShort(parameterName);
	}

	public String getString(int parameterIndex) throws SQLException {
		 
		return this.cstmt.getString(parameterIndex);
	}

	public String getString(String parameterName) throws SQLException {
		 
		return this.cstmt.getString(parameterName);
	}

	public Time getTime(int parameterIndex) throws SQLException {
		
		return this.cstmt.getTime(parameterIndex);
	}

	public Time getTime(String parameterName) throws SQLException {
		
		return this.cstmt.getTime(parameterName);
	}

	public Time getTime(int parameterIndex, Calendar cal) throws SQLException {
	
		return this.cstmt.getTime(parameterIndex, cal);
	}

	public Time getTime(String parameterName, Calendar cal) throws SQLException {
		
		return this.cstmt.getTime(parameterName, cal);
	}

	public Timestamp getTimestamp(int parameterIndex) throws SQLException {
		
		return this.cstmt.getTimestamp(parameterIndex);
	}

	public Timestamp getTimestamp(String parameterName) throws SQLException {
		
		return this.cstmt.getTimestamp(parameterName);
	}

	public Timestamp getTimestamp(int parameterIndex, Calendar cal)
			throws SQLException {
		
		return this.cstmt.getTimestamp(parameterIndex, cal);
	}

	public Timestamp getTimestamp(String parameterName, Calendar cal)
			throws SQLException {
		
		return this.cstmt.getTimestamp(parameterName, cal);
	}

	public URL getURL(int parameterIndex) throws SQLException {
		
		return cstmt.getURL(parameterIndex);
	}

	public URL getURL(String parameterName) throws SQLException {
		
		return cstmt.getURL(parameterName);
	}

	public void registerOutParameter(int parameterIndex, int sqlType)
			throws SQLException {
		cstmt.registerOutParameter(parameterIndex, sqlType);
		
	}

	public void registerOutParameter(String parameterName, int sqlType)
			throws SQLException {
		cstmt.registerOutParameter(parameterName, sqlType);
	}

	public void registerOutParameter(int parameterIndex, int sqlType, int scale)
			throws SQLException {
		cstmt.registerOutParameter(parameterIndex, sqlType, scale);
		
	}

	public void registerOutParameter(int paramIndex, int sqlType,
			String typeName) throws SQLException {
		cstmt.registerOutParameter(paramIndex, sqlType, typeName);
		
	}

	public void registerOutParameter(String parameterName, int sqlType,
			int scale) throws SQLException {
		cstmt.registerOutParameter(parameterName, sqlType, scale);
		
	}

	public void registerOutParameter(String parameterName, int sqlType,
			String typeName) throws SQLException {
		cstmt.registerOutParameter(parameterName, sqlType, typeName);
		
	}

	public void setAsciiStream(String parameterName, InputStream x, int length)
			throws SQLException {
		cstmt.setAsciiStream(parameterName, x, length);
		
	}

	public void setBigDecimal(String parameterName, BigDecimal x)
			throws SQLException {
		cstmt.setBigDecimal(parameterName, x);
		
	}

	public void setBinaryStream(String parameterName, InputStream x, int length)
			throws SQLException {
		cstmt.setBinaryStream(parameterName, x, length);
		
	}

	public void setBoolean(String parameterName, boolean x) throws SQLException {
		cstmt.setBoolean(parameterName, x);
		
	}

	public void setByte(String parameterName, byte x) throws SQLException {
		cstmt.setByte(parameterName, x);
		
	}

	public void setBytes(String parameterName, byte[] x) throws SQLException {
		cstmt.setBytes(parameterName, x);
		
	}

	public void setCharacterStream(String parameterName, Reader reader,
			int length) throws SQLException {
		cstmt.setCharacterStream(parameterName, reader, length);
		
	}

	public void setDate(String parameterName, Date x) throws SQLException {
		cstmt.setDate(parameterName, x);
		
	}

	public void setDate(String parameterName, Date x, Calendar cal)
			throws SQLException {
		cstmt.setDate(parameterName, x, cal);
		
	}

	public void setDouble(String parameterName, double x) throws SQLException {
		cstmt.setDouble(parameterName, x);
		
	}

	public void setFloat(String parameterName, float x) throws SQLException {
		cstmt.setFloat(parameterName, x);
		
	}

	public void setInt(String parameterName, int x) throws SQLException {
		cstmt.setInt(parameterName, x);
		
	}

	public void setLong(String parameterName, long x) throws SQLException {
		cstmt.setLong(parameterName, x);
		
	}

	public void setNull(String parameterName, int sqlType) throws SQLException {
		cstmt.setNull(parameterName, sqlType);
		
	}

	public void setNull(String parameterName, int sqlType, String typeName)
			throws SQLException {
		cstmt.setNull(parameterName, sqlType, typeName);
		
	}

	public void setObject(String parameterName, Object x) throws SQLException {
		cstmt.setObject(parameterName, x);
		
	}

	public void setObject(String parameterName, Object x, int targetSqlType)
			throws SQLException {
		cstmt.setObject(parameterName, x, targetSqlType);
		
	}

	public void setObject(String parameterName, Object x, int targetSqlType,
			int scale) throws SQLException {
		cstmt.setObject(parameterName, x, targetSqlType, scale);
		
	}

	public void setShort(String parameterName, short x) throws SQLException {
		cstmt.setShort(parameterName, x);
		
	}

	public void setString(String parameterName, String x) throws SQLException {
		cstmt.setString(parameterName, x);
		
	}

	public void setTime(String parameterName, Time x) throws SQLException {
		cstmt.setTime(parameterName, x);
		
	}

	public void setTime(String parameterName, Time x, Calendar cal)
			throws SQLException {
		cstmt.setTime(parameterName, x,cal);
		
	}

	public void setTimestamp(String parameterName, Timestamp x)
			throws SQLException {
		cstmt.setTimestamp(parameterName, x);
		
	}

	public void setTimestamp(String parameterName, Timestamp x, Calendar cal)
			throws SQLException {
		cstmt.setTimestamp(parameterName, x, cal);
		
	}

	public void setURL(String parameterName, URL val) throws SQLException {
		cstmt.setURL(parameterName, val);
		
	}

	public boolean wasNull() throws SQLException {
		
		return cstmt.wasNull();
	}

	public void setRowId(int parameterIndex, RowId x) throws SQLException {
		// TODO Auto-generated method stub
		cstmt.setRowId(parameterIndex, x);
		
	}

	public void setNString(int parameterIndex, String value)
			throws SQLException {
		// TODO Auto-generated method stub
		cstmt.setNString(parameterIndex, value);
	}

	public void setNCharacterStream(int parameterIndex, Reader value,
			long length) throws SQLException {
		// TODO Auto-generated method stub
		cstmt.setNCharacterStream(parameterIndex, value,length);
	}

	public void setNClob(int parameterIndex, NClob value) throws SQLException {
		// TODO Auto-generated method stub
		cstmt.setNClob(parameterIndex, value);
		
	}

	public void setClob(int parameterIndex, Reader reader, long length)
			throws SQLException {
		// TODO Auto-generated method stub
		cstmt.setClob(parameterIndex, reader, length);
	}

	public void setBlob(int parameterIndex, InputStream inputStream, long length)
			throws SQLException {
		cstmt.setBlob(parameterIndex, inputStream, length);
		
	}

	public void setNClob(int parameterIndex, Reader reader, long length)
			throws SQLException {
		// TODO Auto-generated method stub
		cstmt.setNClob(parameterIndex, reader,length);
	}

	public void setSQLXML(int parameterIndex, SQLXML xmlObject)
			throws SQLException {
		// TODO Auto-generated method stub
		cstmt.setSQLXML(parameterIndex, xmlObject);
	}

	public void setAsciiStream(int parameterIndex, InputStream x, long length)
			throws SQLException {
		// TODO Auto-generated method stub
		cstmt.setAsciiStream(parameterIndex, x, length);
	}

	public void setBinaryStream(int parameterIndex, InputStream x, long length)
			throws SQLException {
		// TODO Auto-generated method stub
		cstmt.setBinaryStream(parameterIndex, x, length);
	}

	public void setCharacterStream(int parameterIndex, Reader reader,
			long length) throws SQLException {
		// TODO Auto-generated method stub
		cstmt.setCharacterStream(parameterIndex, reader, length);
	}

	public void setAsciiStream(int parameterIndex, InputStream x)
			throws SQLException {
		// TODO Auto-generated method stub
		cstmt.setAsciiStream(parameterIndex, x);
	}

	public void setBinaryStream(int parameterIndex, InputStream x)
			throws SQLException {
		// TODO Auto-generated method stub
		cstmt.setBinaryStream(parameterIndex, x);
	}

	public void setCharacterStream(int parameterIndex, Reader reader)
			throws SQLException {
		// TODO Auto-generated method stub
		cstmt.setCharacterStream(parameterIndex, reader);
	}

	public void setNCharacterStream(int parameterIndex, Reader value)
			throws SQLException {
		// TODO Auto-generated method stub
		cstmt.setNCharacterStream(parameterIndex, value);
	}

	public void setClob(int parameterIndex, Reader reader) throws SQLException {
		// TODO Auto-generated method stub
		cstmt.setClob(parameterIndex, reader);
	}

	public void setBlob(int parameterIndex, InputStream inputStream)
			throws SQLException {
		// TODO Auto-generated method stub
		cstmt.setBlob(parameterIndex, inputStream);
	}

	public void setNClob(int parameterIndex, Reader reader) throws SQLException {
		// TODO Auto-generated method stub
		cstmt.setNClob(parameterIndex, reader);
	}

	public boolean isClosed() throws SQLException {
		// TODO Auto-generated method stub
		return cstmt.isClosed();
	}

	public void setPoolable(boolean poolable) throws SQLException {
		// TODO Auto-generated method stub
		cstmt.setPoolable(poolable);
	}

	public boolean isPoolable() throws SQLException {
		// TODO Auto-generated method stub
		return cstmt.isPoolable();
	}

	public <T> T unwrap(Class<T> iface) throws SQLException {
		// TODO Auto-generated method stub
		return cstmt.unwrap(iface);
	}

	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		// TODO Auto-generated method stub
		return cstmt.isWrapperFor(iface);
	}

	public Object getObject(int parameterIndex, Map<String, Class<?>> map)
			throws SQLException {
		return this.cstmt.getObject(parameterIndex, map);
	}

	public Object getObject(String parameterName, Map<String, Class<?>> map)
			throws SQLException {
		return this.cstmt.getObject(parameterName, map);
	}

	public RowId getRowId(int parameterIndex) throws SQLException {
		// TODO Auto-generated method stub
		return cstmt.getRowId(parameterIndex);
	}

	public RowId getRowId(String parameterName) throws SQLException {
		// TODO Auto-generated method stub
		return cstmt.getRowId(parameterName);
	}

	public void setRowId(String parameterName, RowId x) throws SQLException {
		// TODO Auto-generated method stub
		cstmt.setRowId(parameterName, x);
	}

	public void setNString(String parameterName, String value)
			throws SQLException {
		// TODO Auto-generated method stub
		cstmt.setNString(parameterName, value);
	}

	public void setNCharacterStream(String parameterName, Reader value,
			long length) throws SQLException {
		// TODO Auto-generated method stub
		cstmt.setNCharacterStream(parameterName, value, length);
	}

	public void setNClob(String parameterName, NClob value) throws SQLException {
		// TODO Auto-generated method stub
		cstmt.setNClob(parameterName, value);
	}

	public void setClob(String parameterName, Reader reader, long length)
			throws SQLException {
		// TODO Auto-generated method stub
		cstmt.setClob(parameterName, reader, length);
	}

	public void setBlob(String parameterName, InputStream inputStream,
			long length) throws SQLException {
		// TODO Auto-generated method stub
		cstmt.setBlob(parameterName, inputStream, length);
	}

	public void setNClob(String parameterName, Reader reader, long length)
			throws SQLException {
		// TODO Auto-generated method stub
		cstmt.setNClob(parameterName, reader, length);
	}

	public NClob getNClob(int parameterIndex) throws SQLException {
		// TODO Auto-generated method stub
		return cstmt.getNClob(parameterIndex);
	}

	public NClob getNClob(String parameterName) throws SQLException {
		// TODO Auto-generated method stub
		return cstmt.getNClob(parameterName);
	}

	public void setSQLXML(String parameterName, SQLXML xmlObject)
			throws SQLException {
		// TODO Auto-generated method stub
		cstmt.setSQLXML(parameterName, xmlObject);
	}

	public SQLXML getSQLXML(int parameterIndex) throws SQLException {
		// TODO Auto-generated method stub
		return cstmt.getSQLXML(parameterIndex);
	}

	public SQLXML getSQLXML(String parameterName) throws SQLException {
		// TODO Auto-generated method stub
		return cstmt.getSQLXML(parameterName);
	}

	public String getNString(int parameterIndex) throws SQLException {
		// TODO Auto-generated method stub
		return cstmt.getNString(parameterIndex);
	}

	public String getNString(String parameterName) throws SQLException {
		// TODO Auto-generated method stub
		return cstmt.getNString(parameterName);
	}

	public Reader getNCharacterStream(int parameterIndex) throws SQLException {
		// TODO Auto-generated method stub
		return cstmt.getNCharacterStream(parameterIndex);
	}

	public Reader getNCharacterStream(String parameterName) throws SQLException {
		// TODO Auto-generated method stub
		return cstmt.getNCharacterStream(parameterName);
	}

	public Reader getCharacterStream(int parameterIndex) throws SQLException {
		// TODO Auto-generated method stub
		return cstmt.getCharacterStream(parameterIndex);
	}

	public Reader getCharacterStream(String parameterName) throws SQLException {
		// TODO Auto-generated method stub
		return cstmt.getCharacterStream(parameterName);
	}

	public void setBlob(String parameterName, Blob x) throws SQLException {
		// TODO Auto-generated method stub
		cstmt.setBlob(parameterName, x);
	}

	public void setClob(String parameterName, Clob x) throws SQLException {
		// TODO Auto-generated method stub
		cstmt.setClob(parameterName, x);
	}

	public void setAsciiStream(String parameterName, InputStream x, long length)
			throws SQLException {
		// TODO Auto-generated method stub
		cstmt.setAsciiStream(parameterName, x, length);
	}

	public void setBinaryStream(String parameterName, InputStream x, long length)
			throws SQLException {
		// TODO Auto-generated method stub
		cstmt.setBinaryStream(parameterName, x, length);
	}

	public void setCharacterStream(String parameterName, Reader reader,
			long length) throws SQLException {
		// TODO Auto-generated method stub
		cstmt.setCharacterStream(parameterName, reader);
	}

	public void setAsciiStream(String parameterName, InputStream x)
			throws SQLException {
		// TODO Auto-generated method stub
		cstmt.setAsciiStream(parameterName, x);
	}

	public void setBinaryStream(String parameterName, InputStream x)
			throws SQLException {
		// TODO Auto-generated method stub
		cstmt.setBinaryStream(parameterName, x);
	}

	public void setCharacterStream(String parameterName, Reader reader)
			throws SQLException {
		// TODO Auto-generated method stub
		cstmt.setCharacterStream(parameterName, reader);
	}

	public void setNCharacterStream(String parameterName, Reader value)
			throws SQLException {
		// TODO Auto-generated method stub
		cstmt.setNCharacterStream(parameterName, value);
	}

	public void setClob(String parameterName, Reader reader)
			throws SQLException {
		// TODO Auto-generated method stub
		cstmt.setClob(parameterName, reader);
	}

	public void setBlob(String parameterName, InputStream inputStream)
			throws SQLException {
		// TODO Auto-generated method stub
		cstmt.setBlob(parameterName, inputStream);
	}

	public void setNClob(String parameterName, Reader reader)
			throws SQLException {
		// TODO Auto-generated method stub
		cstmt.setNClob(parameterName, reader);
	}

}
