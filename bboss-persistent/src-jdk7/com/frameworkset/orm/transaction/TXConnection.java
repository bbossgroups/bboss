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

import java.sql.Connection;
import java.sql.SQLException;

import javax.transaction.SystemException;

/**
 * 
 * 
 * <p>Title: TXConnection.java</p>
 *
 * <p>Description: 客户程序直接</p>
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
public class TXConnection extends InnerConnection implements Connection {

	public TXConnection(Connection con) {
		super(con);
		
		
	}
	
	/**
	 * 获取内部链接
	 */
	public Connection getInnerConnection()
	{
		return super.getInnerConnection();
	}
	
	public void close() throws SQLException {
//		closeStatements();
		//do nothing
	}
	
	public void setAutoCommit(boolean autoCommit) throws SQLException {		
		//do nothing.
	}
	
	/**
	 * 在事务环境下，不做任何处理
	 */
	public void commit() throws SQLException {
		
		//do nothing.

	}
	
	/**
	 * 重载父类方法，设置事务的状态标识为回滚状态
	 */
	public void rollback() throws SQLException {
		JDBCTransaction tx = TransactionManager.getTransaction();
		try {
			if(tx != null)
				tx.setRollbackOnly();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SystemException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

	}
	
	
	
//	public 

	

}
