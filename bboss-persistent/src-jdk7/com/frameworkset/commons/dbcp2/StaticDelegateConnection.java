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
package com.frameworkset.commons.dbcp2;

import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.NClob;
import java.sql.PreparedStatement;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Savepoint;
import java.sql.Statement;
import java.sql.Struct;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executor;

import com.frameworkset.common.poolman.monitor.AbandonedTraceExt;
import com.frameworkset.commons.dbcp2.NativeDataSource.StaticCount;
import com.frameworkset.util.SimpleStringUtil;

public class StaticDelegateConnection implements java.sql.Connection{

	private StaticCount staticCount;
	private Connection c;
	private AbandonedTraceExt traceObject;
	
	/**
     * Create a wrapper for the Connectin which traces this
     * Connection in the AbandonedObjectPool.
     *
     * @param c the {@link Connection} to delegate all calls to.
     */
    public StaticDelegateConnection(Connection c,StaticCount staticCount) {
       this.c = c;
        this.staticCount = staticCount;
        long time = System.currentTimeMillis();
        traceObject = new AbandonedTraceExt("NOPooledConnection");
        traceObject.setDburl(this.c.toString());
        traceObject.setLabel("Connection-Native");
        traceObject.setCreateTime(time);
		 
        traceObject.setLastUsed( time);
        traceObject.setLastBorrowTime(time);
        traceObject.setLastReturnTime(time);
        traceObject.setBorrowedCount(1);
		Exception e = new Exception();
		traceObject.setStackInfo(SimpleStringUtil.exceptionToString(e));
		this.staticCount.increments(traceObject);
		 
    }

   
    
    
    public void close() throws SQLException
    {
    	try
    	{
    		c.close();
    	}
    	finally
    	{
    		this.staticCount.decrements(traceObject);
    	}
    }




	public AbandonedTraceExt getTraceObject() {
		return traceObject;
	}




	@Override
	public <T> T unwrap(Class<T> iface) throws SQLException {
		// TODO Auto-generated method stub
		return c.unwrap(iface);
	}




	@Override
	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		// TODO Auto-generated method stub
		return c.isWrapperFor(iface);
	}




	@Override
	public Statement createStatement() throws SQLException {
		// TODO Auto-generated method stub
		return c.createStatement();
	}




	@Override
	public PreparedStatement prepareStatement(String sql) throws SQLException {
		// TODO Auto-generated method stub
		return c.prepareStatement(sql);
	}




	@Override
	public CallableStatement prepareCall(String sql) throws SQLException {
		// TODO Auto-generated method stub
		return c.prepareCall(sql);
	}




	@Override
	public String nativeSQL(String sql) throws SQLException {
		// TODO Auto-generated method stub
		return c.nativeSQL(sql);
	}




	@Override
	public void setAutoCommit(boolean autoCommit) throws SQLException {
		c.setAutoCommit(autoCommit);
		
	}




	@Override
	public boolean getAutoCommit() throws SQLException {
		// TODO Auto-generated method stub
		return c.getAutoCommit();
	}




	@Override
	public void commit() throws SQLException {
		c.commit();
		
	}




	@Override
	public void rollback() throws SQLException {
		// TODO Auto-generated method stub
		c.rollback();
	}




	@Override
	public boolean isClosed() throws SQLException {
		// TODO Auto-generated method stub
		return c.isClosed();
	}




	@Override
	public DatabaseMetaData getMetaData() throws SQLException {
		// TODO Auto-generated method stub
		return c.getMetaData();
	}




	@Override
	public void setReadOnly(boolean readOnly) throws SQLException {
		c.setReadOnly(readOnly);
		
	}




	@Override
	public boolean isReadOnly() throws SQLException {
		// TODO Auto-generated method stub
		return c.isReadOnly();
	}




	@Override
	public void setCatalog(String catalog) throws SQLException {
		// TODO Auto-generated method stub
		c.setCatalog(catalog);
	}




	@Override
	public String getCatalog() throws SQLException {
		// TODO Auto-generated method stub
		return c.getCatalog();
	}




	@Override
	public void setTransactionIsolation(int level) throws SQLException {
		// TODO Auto-generated method stub
		c.setTransactionIsolation(level);
	}




	@Override
	public int getTransactionIsolation() throws SQLException {
		// TODO Auto-generated method stub
		return c.getTransactionIsolation();
	}




	@Override
	public SQLWarning getWarnings() throws SQLException {
		// TODO Auto-generated method stub
		return c.getWarnings();
	}




	@Override
	public void clearWarnings() throws SQLException {
		// TODO Auto-generated method stub
		c.clearWarnings();
	}




	@Override
	public Statement createStatement(int resultSetType, int resultSetConcurrency)
			throws SQLException {
		// TODO Auto-generated method stub
		return c.createStatement(resultSetType, resultSetConcurrency);
	}




	@Override
	public PreparedStatement prepareStatement(String sql, int resultSetType,
			int resultSetConcurrency) throws SQLException {
		// TODO Auto-generated method stub
		return c.prepareStatement(  sql,   resultSetType,
				  resultSetConcurrency);
	}




	@Override
	public CallableStatement prepareCall(String sql, int resultSetType,
			int resultSetConcurrency) throws SQLException {
		// TODO Auto-generated method stub
		return c.prepareCall(sql, resultSetType, resultSetConcurrency);
	}




	@Override
	public Map<String, Class<?>> getTypeMap() throws SQLException {
		// TODO Auto-generated method stub
		return c.getTypeMap();
	}




	@Override
	public void setTypeMap(Map<String, Class<?>> map) throws SQLException {
		  c.setTypeMap(map);
		
	}




	@Override
	public void setHoldability(int holdability) throws SQLException {
		c.setHoldability(holdability);
		
	}




	@Override
	public int getHoldability() throws SQLException {
		// TODO Auto-generated method stub
		return c.getHoldability();
	}




	@Override
	public Savepoint setSavepoint() throws SQLException {
		// TODO Auto-generated method stub
		return c.setSavepoint();
	}




	@Override
	public Savepoint setSavepoint(String name) throws SQLException {
		// TODO Auto-generated method stub
		return c.setSavepoint(name);
	}




	@Override
	public void rollback(Savepoint savepoint) throws SQLException {
		// TODO Auto-generated method stub
		c.rollback(savepoint);
	}




	@Override
	public void releaseSavepoint(Savepoint savepoint) throws SQLException {
		// TODO Auto-generated method stub
		c.releaseSavepoint(savepoint);
	}




	@Override
	public Statement createStatement(int resultSetType,
			int resultSetConcurrency, int resultSetHoldability)
			throws SQLException {
		// TODO Auto-generated method stub
		return c.createStatement(resultSetType, resultSetConcurrency, resultSetHoldability);
	}




	@Override
	public PreparedStatement prepareStatement(String sql, int resultSetType,
			int resultSetConcurrency, int resultSetHoldability)
			throws SQLException {
		// TODO Auto-generated method stub
		return c.prepareStatement(  sql,   resultSetType,
				  resultSetConcurrency,   resultSetHoldability);
	}




	@Override
	public CallableStatement prepareCall(String sql, int resultSetType,
			int resultSetConcurrency, int resultSetHoldability)
			throws SQLException {
		// TODO Auto-generated method stub
		return c.prepareCall(  sql,   resultSetType,
				  resultSetConcurrency,   resultSetHoldability);
	}




	@Override
	public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys)
			throws SQLException {
		// TODO Auto-generated method stub
		return c.prepareStatement(sql, autoGeneratedKeys);
	}




	@Override
	public PreparedStatement prepareStatement(String sql, int[] columnIndexes)
			throws SQLException {
		// TODO Auto-generated method stub
		return c.prepareStatement(sql, columnIndexes);
	}




	@Override
	public PreparedStatement prepareStatement(String sql, String[] columnNames)
			throws SQLException {
		// TODO Auto-generated method stub
		return c.prepareStatement(sql, columnNames);
	}




	@Override
	public Clob createClob() throws SQLException {
		// TODO Auto-generated method stub
		return c.createClob();
	}




	@Override
	public Blob createBlob() throws SQLException {
		// TODO Auto-generated method stub
		return c.createBlob();
	}




	@Override
	public NClob createNClob() throws SQLException {
		// TODO Auto-generated method stub
		return c.createNClob();
	}




	@Override
	public SQLXML createSQLXML() throws SQLException {
		// TODO Auto-generated method stub
		return c.createSQLXML();
	}




	@Override
	public boolean isValid(int timeout) throws SQLException {
		// TODO Auto-generated method stub
		return c.isValid(timeout);
	}




	@Override
	public void setClientInfo(String name, String value)
			throws SQLClientInfoException {
		// TODO Auto-generated method stub
		c.setClientInfo(name, value);
	}




	@Override
	public void setClientInfo(Properties properties)
			throws SQLClientInfoException {
		// TODO Auto-generated method stub
		c.setClientInfo(properties);
	}




	@Override
	public String getClientInfo(String name) throws SQLException {
		// TODO Auto-generated method stub
		return c.getClientInfo(name);
	}




	@Override
	public Properties getClientInfo() throws SQLException {
		// TODO Auto-generated method stub
		return c.getClientInfo();
	}




	@Override
	public Array createArrayOf(String typeName, Object[] elements)
			throws SQLException {
		// TODO Auto-generated method stub
		return c.createArrayOf(typeName, elements);
	}




	@Override
	public Struct createStruct(String typeName, Object[] attributes)
			throws SQLException {
		// TODO Auto-generated method stub
		return c.createStruct(typeName, attributes);
	}




	@Override
	public void setSchema(String schema) throws SQLException {
		// TODO Auto-generated method stub
		c.setSchema(schema);
	}




	@Override
	public String getSchema() throws SQLException {
		// TODO Auto-generated method stub
		return c.getSchema();
	}




	@Override
	public void abort(Executor executor) throws SQLException {
		// TODO Auto-generated method stub
		c.abort(executor);
	}




	@Override
	public void setNetworkTimeout(Executor executor, int milliseconds)
			throws SQLException {
		// TODO Auto-generated method stub
		c.setNetworkTimeout(executor, milliseconds);
	}




	@Override
	public int getNetworkTimeout() throws SQLException {
		// TODO Auto-generated method stub
		return c.getNetworkTimeout();
	}
    
    

	

}
