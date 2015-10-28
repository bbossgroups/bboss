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

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Savepoint;
import java.sql.Statement;
import java.util.List;
import java.util.Map;

/**
 * 
 * 
 * <p>Title: InnerConnection.java</p>
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
class InnerConnection implements java.sql.Connection {
	private Connection con;
	private List statements = new java.util.ArrayList();

	InnerConnection(Connection con) {
		this.con = con;
		
	}

	public void clearWarnings() throws SQLException {
		this.con.clearWarnings();

	}

	protected void closeStatements() {
		try {
			for (int i = 0; i < this.statements.size(); i++) {
				Statement stmt = (Statement) statements.get(i);
				try {

					stmt.close();
				} catch (SQLException e) {

				}
			}
			statements.clear();
		} catch (Exception e) {
			statements.clear();
		}
	}

	public void close() throws SQLException {
		closeStatements();
		if(con != null)
			this.con.close();
		this.con = null;

	}

	public void commit() throws SQLException {
		this.con.commit();

	}

	public Statement createStatement() throws SQLException {
		Statement stmt = new InnerStatement(con.createStatement());			
		
		this.statements.add(stmt);
		return stmt;
	}

	public Statement createStatement(int resultSetType, int resultSetConcurrency)
			throws SQLException {
		Statement stmt = new InnerStatement(con.createStatement(resultSetType,
				resultSetConcurrency));
		this.statements.add(stmt);
		return stmt;

	}

	public Statement createStatement(int resultSetType,
			int resultSetConcurrency, int resultSetHoldability)
			throws SQLException {
		Statement stmt = new InnerStatement(this.con.createStatement(resultSetType,
				resultSetConcurrency, resultSetHoldability));
		this.statements.add(stmt);
		return stmt;

	}

	public boolean getAutoCommit() throws SQLException {

		return this.con.getAutoCommit();
	}

	public String getCatalog() throws SQLException {

		return this.con.getCatalog();
	}

	public int getHoldability() throws SQLException {

		return this.con.getHoldability();
	}

	public DatabaseMetaData getMetaData() throws SQLException {

		return this.con.getMetaData();
	}

	public int getTransactionIsolation() throws SQLException {

		return this.con.getTransactionIsolation();
	}

	public Map getTypeMap() throws SQLException {

		return this.con.getTypeMap();
	}

	public SQLWarning getWarnings() throws SQLException {

		return this.con.getWarnings();
	}

	public boolean isClosed() throws SQLException {

		return this.con.isClosed();
	}

	public boolean isReadOnly() throws SQLException {

		return this.con.isReadOnly();
	}

	public String nativeSQL(String sql) throws SQLException {

		return this.con.nativeSQL(sql);
	}

	public CallableStatement prepareCall(String sql) throws SQLException {
		CallableStatement cstmt = new InnerCallableStatement(this.con.prepareCall(sql));
		this.statements.add(cstmt);
		return cstmt;
	}

	public CallableStatement prepareCall(String sql, int resultSetType,
			int resultSetConcurrency) throws SQLException {
		CallableStatement cstmt = new InnerCallableStatement(this.con.prepareCall(sql, resultSetType,
				resultSetConcurrency));
		this.statements.add(cstmt);
		return cstmt;
		//			return this.con.prepareCall(sql,resultSetType,resultSetConcurrency);
	}

	public CallableStatement prepareCall(String sql, int resultSetType,
			int resultSetConcurrency, int resultSetHoldability)
			throws SQLException {
		CallableStatement cstmt = new InnerCallableStatement(this.con.prepareCall(sql, resultSetType,
				resultSetConcurrency, resultSetHoldability));
		this.statements.add(cstmt);
		return cstmt;
		//			return this.con.prepareCall(sql,resultSetType,resultSetConcurrency,resultSetHoldability);
	}

	public PreparedStatement prepareStatement(String sql) throws SQLException {
		PreparedStatement pstmt = new InnerPreparedStatement(this.con.prepareStatement(sql));
		this.statements.add(pstmt);
		return pstmt;
		//			return this.con.prepareStatement(sql);
	}

	public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys)
			throws SQLException {
		PreparedStatement pstmt = new InnerPreparedStatement(this.con.prepareStatement(sql,
				autoGeneratedKeys));
		this.statements.add(pstmt);
		return pstmt;
		//			return this.con.prepareStatement(sql, autoGeneratedKeys);
	}

	public PreparedStatement prepareStatement(String sql, int[] columnIndexes)
			throws SQLException {
		PreparedStatement pstmt = new InnerPreparedStatement(this.con.prepareStatement(sql, columnIndexes));
		this.statements.add(pstmt);
		return pstmt;
		//			return this.con.prepareStatement(sql, columnIndexes);
	}

	public PreparedStatement prepareStatement(String sql, String[] columnNames)
			throws SQLException {
		PreparedStatement pstmt = new InnerPreparedStatement(this.con.prepareStatement(sql, columnNames));
		this.statements.add(pstmt);
		return pstmt;
		//			return this.con.prepareStatement(sql, columnNames);
	}

	public PreparedStatement prepareStatement(String sql, int resultSetType,
			int resultSetConcurrency) throws SQLException {
		PreparedStatement pstmt = new InnerPreparedStatement(this.con.prepareStatement(sql, resultSetType,
				resultSetConcurrency));
		this.statements.add(pstmt);
		return pstmt;
		//			return this.con.prepareStatement(sql, resultSetType, resultSetConcurrency);
	}

	public PreparedStatement prepareStatement(String sql, int resultSetType,
			int resultSetConcurrency, int resultSetHoldability)
			throws SQLException {
		PreparedStatement pstmt = new InnerPreparedStatement(this.con.prepareStatement(sql, resultSetType,
				resultSetConcurrency, resultSetHoldability));
		this.statements.add(pstmt);
		return pstmt;
		//			return this.con.prepareStatement(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
	}

	public void releaseSavepoint(Savepoint savepoint) throws SQLException {
		this.con.releaseSavepoint(savepoint);

	}

	public void rollback() throws SQLException {
		this.con.rollback();

	}

	public void rollback(Savepoint savepoint) throws SQLException {
		this.con.rollback(savepoint);

	}

	public void setAutoCommit(boolean autoCommit) throws SQLException {
		this.con.setAutoCommit(autoCommit);

	}

	public void setCatalog(String catalog) throws SQLException {
		this.con.setCatalog(catalog);

	}

	public void setHoldability(int holdability) throws SQLException {
		this.con.setHoldability(holdability);

	}

	public void setReadOnly(boolean readOnly) throws SQLException {
		this.con.setReadOnly(readOnly);

	}

	public Savepoint setSavepoint() throws SQLException {

		return this.con.setSavepoint();
	}

	public Savepoint setSavepoint(String name) throws SQLException {

		return this.con.setSavepoint(name);
	}

	public void setTransactionIsolation(int level) throws SQLException {
		this.con.setTransactionIsolation(level);

	}

	public void setTypeMap(Map map) throws SQLException {
		this.con.setTypeMap(map);

	}
	
	public Connection getInnerConnection()
	{
		return this.con;
	}
}