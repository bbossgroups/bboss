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

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: </p>
 *
 * @author biaoping.yin
 * @version 1.0
 */
//$Id: JDBCTransaction.java,v 1.12 2005/05/03 21:50:17 oneovthafew Exp $


import com.frameworkset.common.poolman.NestedSQLException;
import com.frameworkset.common.poolman.util.SQLManager;
import com.frameworkset.orm.adapter.DB;
import com.frameworkset.orm.adapter.DBNone;
import com.frameworkset.orm.annotation.TransactionType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import javax.transaction.*;
import javax.transaction.xa.XAResource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

/**
 * Implements a basic transaction strategy for JDBC connections.This is the
 * default <tt>Transaction</tt> implementation used if none is explicitly
 * specified.
 * @since 2007.05.16
 * @author biaoping.yin
 */
public class JDBCTransaction {
	private static Logger logger = LoggerFactory.getLogger(JDBCTransaction.class);
	/**
	 * 事务类型，默认为无事务
	 */
	
	
	
	private Map<Object,TransactionEntity> txentities = new HashMap<Object,TransactionEntity>(1);
	private Stack<TransactionEntity> executeStack = new Stack<TransactionEntity>();
	private int count = 0;
	TransactionType currenttxtype;
	public TransactionType getTXType()
	{
	    return this.currenttxtype;
	}
	
	
	JDBCTransaction parent_tx = null;
	
	private int status = Status.STATUS_UNKNOWN;
	
	
	
	
	
	
	JDBCTransaction(TransactionType currenttxtype)
	{		
		this.currenttxtype = currenttxtype;
	}
	
	JDBCTransaction(JDBCTransaction parent_tx,TransactionType currenttxtype)
	{		
		this.parent_tx = parent_tx;
		this.currenttxtype = currenttxtype;
	}
	
    public Connection getConnection() throws TransactionException {
    	return this.getConnection(null);
    }
    
    
    public void printStackTrace()
    {
    	if(this.txentities != null && this.txentities.size() > 0)
    	{
    		Iterator<Map.Entry<Object, TransactionEntity>> entries = txentities.entrySet().iterator();
    		while(entries.hasNext())
    		{
    			Map.Entry<Object, TransactionEntity> entry = entries.next();
    			if(entry.getValue() != null)
    				entry.getValue().printStackTrace();
    		}
    	}
    	
    }
    
    
    
    
    
    
    class TransactionEntity
    {
		//    	super.
    	Connection con;
    	private Exception createBy;
    	
    	private int status = Status.STATUS_UNKNOWN;
    	private boolean toggleAutoCommit = true;
    	private int count;
    	boolean errorflag = false;
        private String dbName;
        private DB dbAdapter;
    	
    	private static final String info = "Transaction Entity created on ";
    	TransactionEntity(Connection originecon, String dbName, DB dbAdapter) throws SQLException
    	{
    		this.dbAdapter = dbAdapter;
            if(dbAdapter == null){
                this.dbAdapter = new DBNone();
            }
            this.dbName = dbName;
//    		this.con = new InnerConnection(originecon);
    		this.con = originecon;
    		createBy = new Exception("dbName:"+dbName+","+info + new Date());
    		
    		try
    		{
    			toggleAutoCommit = con.getAutoCommit();
    		}
    		catch(SQLException e)
    		{
    			errorflag = true;
    			throw e;
    		}
    		if(toggleAutoCommit && currenttxtype != TransactionType.RW_TRANSACTION)
    		{
                dbAdapter.setAutoCommit(con,false);//dbAdapter.setAutoCommit(con,false);
    		}
    		
    	}    

        public void printStackTrace()
        {
        	logger.error("",createBy);
        }
    	protected void increament()
    	{
    		this.count ++;
    	}
    	
    	//计数器减一，目前没有调用
    	protected void decreament()
    	{
    		this.count --;
    		if(count == 0)
    			this.status = Status.STATUS_COMMITTING;
    	}
    	
    	protected int getStatus()
    	{
    		return this.status;
    	}
    	
    	protected void setStatus(int status)
    	{
    		this.status = status;
    	}
    	
    	protected void commit() throws SQLException
    	{
    		try {
    			if(con != null && currenttxtype != TransactionType.RW_TRANSACTION)
                    dbAdapter.commit(this.con);
//				this.status = Status.STATUS_COMMITTED;
			} catch (SQLException e) {
				// TODO Auto-generated catch block
//				this.status = Status.STATUS_COMMITTED;
				throw e;
			
			} catch (Exception e) {
				// TODO Auto-generated catch block
		//			this.status = Status.STATUS_COMMITTED;
			
				throw new NestedSQLException(e.getMessage(),e);
			}
			finally
			{
	    		try {
	    			if(con != null && currenttxtype != TransactionType.RW_TRANSACTION)
                        dbAdapter.setAutoCommit(con,this.toggleAutoCommit);//this.con.setAutoCommit(this.toggleAutoCommit);
				} catch (SQLException e) {
					logger.error("this.con.setAutoCommit("+toggleAutoCommit+") failed:",e);
				}
				try
				{
					if(con != null)
	    				this.con.close();
					
				}
				catch(Exception e)
				{
					
				}
				con  = null;
			}
			this.status = Status.STATUS_COMMITTED;
    	}
    	
    	protected void rollback()
    	{
    		try {
    			this.status = Status.STATUS_ROLLING_BACK;
    			if(con != null && currenttxtype != TransactionType.RW_TRANSACTION)
    				this.con.rollback();
				this.status = Status.STATUS_ROLLEDBACK;
				
			} catch (SQLException e) {

				logger.error("this.con.rollback() failed:",e);
			
	    	} catch (Exception e) {

				logger.error("this.con.rollback() failed:",e);
			}
			finally
			{
				this.count = 0;
				try {
					if(con != null && currenttxtype != TransactionType.RW_TRANSACTION)
                        dbAdapter.setAutoCommit(con,this.toggleAutoCommit);//this.con.setAutoCommit(this.toggleAutoCommit);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					logger.error("this.con.setAutoCommit("+toggleAutoCommit+") failed:",e);
				}
				try {
					if(con != null)
						this.con.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					logger.error("this.con.close() failed:",e);
				}
				con = null;
				this.status = Status.STATUS_ROLLEDBACK;
			}
    	}
    	protected void destroy()
    	{
    		if(this.con != null)
    		{
    			if(!this.errorflag)
    			{
    				try {
    				    if(currenttxtype != TransactionType.RW_TRANSACTION)
    				    {
                            dbAdapter.setAutoCommit(con,this.toggleAutoCommit);//this.con.setAutoCommit(this.toggleAutoCommit);
    				    }
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						logger.error("this.con.setAutoCommit("+toggleAutoCommit+") failed:",e);
					}
					try
					{
						if(!con.isClosed())
						{
							con.close();
						}
					}
					catch(Exception e)
					{
						
					}
    			}
    		}
    	}

    	protected Connection getConnection() {
			// TODO Auto-generated method stub
			return this.con;
		}
    }
    
    protected void begin() throws TransactionException
    {
    	if(wasRolledBack())
		{
			throw new TransactionException("JDBCTransaction.begin() failed:TX has been RollBacked.");
		}
		
		if(this.wasCommitted())
			throw new TransactionException("JDBCTransaction.begin() failed:TX has been commited.");
		if(this.status == Status.STATUS_MARKED_ROLLBACK)
			throw new TransactionException("JDBCTransaction.begin() failed:TX has been MARKED ROLLBACK.");
		
    	increament();
    }
    
    protected void increament()
    {
    	
    	this.count ++;
    }
    
   
    
	public Connection getConnectionFromDS(DataSource datasource) throws TransactionException {
		if(wasRolledBack())
		{
			throw new TransactionException(new SQLException("JDBCTransaction.getConnectionFromDS()：TX was RolledBack."));
		}
		
		if(this.wasCommitted())
			throw new TransactionException(new SQLException("JDBCTransaction.getConnectionFromDS：TX was committed."));
		//注释：对应RW事务，然后允许进行相应的数据库操作,保留修改痕迹而注释
//		if(this.status == Status.STATUS_MARKED_ROLLBACK)
//		{
//			throw new TransactionException(new SQLException("JDBCTransaction.getConnectionFromDS：TX was remarked rollingback."));
//		}
		if(this.currenttxtype != TransactionManager.RW_TRANSACTION && this.status == Status.STATUS_MARKED_ROLLBACK)
		{
			throw new TransactionException(new SQLException("JDBCTransaction.getConnectionFromDS：TX was remarked rollingback."));
		}
		TransactionEntity txentity = null;
//		String t_dbName = (dbName == null ?SQLManager.getInstance().getDefaultDBName():dbName);
    	try
    	{
//    		String t_dbName = (dbName == null ?"NULL":dbName);
//    		t_dbName = SQLManager.getRealDBNameFromExternalDBName(t_dbName);
    		txentity = txentities.get(datasource);
    		if(this.status != Status.STATUS_ACTIVE)
    			this.status = Status.STATUS_ACTIVE;
    		Connection con = null;
    		if(txentity == null)
    		{    			
	    		con = datasource.getConnection();
	    		if(con == null)
	    			throw new TransactionException(new SQLException("JDBCTransaction.getConnectionFromDS：Request connection  from datasource return null!"));
	    		txentity = new TransactionEntity(con,null,null);
	    		txentity.increament();
	    		txentity.setStatus(Status.STATUS_ACTIVE);
	    		txentities.put(datasource,txentity);
	    		executeStack.push(txentity);
	    		
    		}
    		else
    		{
    			if(txentity.getStatus() == Status.STATUS_COMMITTED 
    					
    					|| txentity.getStatus() == Status.STATUS_ROLLEDBACK
    					|| txentity.getStatus() == Status.STATUS_ROLLING_BACK)
    				throw new TransactionException(new SQLException("JDBCTransaction.getConnectionFromDS：" + "TransactionEntity.getStatus() == Status.STATUS_COMMITTED "
        					+ "|| TransactionEntity.getStatus() == Status.STATUS_MARKED_ROLLBACK"
        					+ "|| TransactionEntity.getStatus() == Status.STATUS_ROLLEDBACK"
        					+ "|| TransactionEntity.getStatus() == Status.STATUS_ROLLING_BACK"));
    			else if(this.currenttxtype != TransactionManager.RW_TRANSACTION &&  txentity.getStatus() == Status.STATUS_MARKED_ROLLBACK)
    			{
    				throw new TransactionException(new SQLException("JDBCTransaction.getConnectionFromDS：" + "TransactionEntity.getStatus() == Status.STATUS_COMMITTED "
        					+ "|| TransactionEntity.getStatus() == Status.STATUS_MARKED_ROLLBACK"
        					+ "|| TransactionEntity.getStatus() == Status.STATUS_ROLLEDBACK"
        					+ "|| TransactionEntity.getStatus() == Status.STATUS_ROLLING_BACK"));
    			}
    			txentity.increament();
    		}
    		return txentity.getConnection();
    	}
    	catch(Throwable e)
    	{
    		
    		if(txentity != null)
    		{
    			txentity.destroy();
    			try
    			{
    				this.executeStack.removeElement(txentity);
    			}
    			catch(Exception ie)
    			{
					logger.error("this.executeStack.removeElement(txentity) failed:",ie);
    			}
    			try
    			{
    				this.txentities.remove(datasource);
    			}
    			catch(Exception ie)
    			{
					logger.error("this.txentities.remove(datasource) failed:",ie);
    			}
    		}
    		throw new TransactionException(e);
    	}
	}
	
    
    public Connection getConnection(String dbName) throws TransactionException {
    	
    	if(wasRolledBack())
		{
			throw new TransactionException(new SQLException("JDBCTransaction.getConnection(dbName="+ dbName +")：TX was RolledBack."));
		}
		
		if(this.wasCommitted())
			throw new TransactionException(new SQLException("JDBCTransaction.getConnection(dbName="+ dbName +")：TX was committed."));
		if(this.currenttxtype != TransactionManager.RW_TRANSACTION && this.status == Status.STATUS_MARKED_ROLLBACK)
		{
			throw new TransactionException(new SQLException("JDBCTransaction.getConnection(dbName="+ dbName +")：TX was remarked rollingback."));
		}
		TransactionEntity txentity = null;
		String t_dbName = (dbName == null ?SQLManager.getInstance().getDefaultDBName():dbName);
    	try
    	{
//    		String t_dbName = (dbName == null ?"NULL":dbName);
            DB dbAdapter = SQLManager.getInstance().getDBAdapter(t_dbName);
    		t_dbName = SQLManager.getRealDBNameFromExternalDBName(t_dbName);
    		txentity = txentities.get(t_dbName);
    		if(this.status != Status.STATUS_ACTIVE)
    			this.status = Status.STATUS_ACTIVE;
    		Connection con = null;
    		if(txentity == null)
    		{    			
	    		con = SQLManager.getInstance().requestConnection(t_dbName);
	    		if(con == null)
	    			throw new TransactionException(new SQLException("JDBCTransaction.getConnection(dbName="+ dbName +")：Request connection  from pool return null!"));
	    		txentity = new TransactionEntity(con,t_dbName,dbAdapter);
	    		txentity.increament();
	    		txentity.setStatus(Status.STATUS_ACTIVE);
	    		txentities.put(t_dbName,txentity);
	    		executeStack.push(txentity);
	    		
    		}
    		else
    		{
    			if(txentity.getStatus() == Status.STATUS_COMMITTED     					
    					|| txentity.getStatus() == Status.STATUS_ROLLEDBACK
    					|| txentity.getStatus() == Status.STATUS_ROLLING_BACK)
    				throw new TransactionException(new SQLException("JDBCTransaction.getConnection(dbName="+ dbName +")：" + "TransactionEntity.getStatus() == Status.STATUS_COMMITTED "
        					+ "|| TransactionEntity.getStatus() == Status.STATUS_MARKED_ROLLBACK"
        					+ "|| TransactionEntity.getStatus() == Status.STATUS_ROLLEDBACK"
        					+ "|| TransactionEntity.getStatus() == Status.STATUS_ROLLING_BACK"));
    			else if(this.currenttxtype != TransactionManager.RW_TRANSACTION &&  txentity.getStatus() == Status.STATUS_MARKED_ROLLBACK)
    			{
    				throw new TransactionException(new SQLException("JDBCTransaction.getConnection(dbName="+ dbName +")：" + "TransactionEntity.getStatus() == Status.STATUS_COMMITTED "
        					+ "|| TransactionEntity.getStatus() == Status.STATUS_MARKED_ROLLBACK"
        					+ "|| TransactionEntity.getStatus() == Status.STATUS_ROLLEDBACK"
        					+ "|| TransactionEntity.getStatus() == Status.STATUS_ROLLING_BACK"));
    			}
    			txentity.increament();
    		}
    		return txentity.getConnection();
    	}
    	catch(Exception e)
    	{
    		
    		if(txentity != null)
    		{
    			txentity.destroy();
    			try
    			{
    				this.executeStack.removeElement(txentity);
    			}
    			catch(Exception ie)
    			{
					logger.error("this.executeStack.removeElement(txentity) failed:",ie);
    			}
    			try
    			{
    				this.txentities.remove(t_dbName);
    			}
    			catch(Exception ie)
    			{
					logger.error("this.txentities.remove("+t_dbName+") failed:",ie);
    			}
    		}
    		throw new TransactionException(e);
    	}
    }

    public void closeIfRequired() throws TransactionException {
    }

//    public void commit() {
//    	try {
//			this.con.commit();
//			this.con.setAutoCommit(this.toggleAutoCommit);
//		} catch (SQLException e) {
//			throw new TransactionException(e);
//		}
//    }

    public void commitAndResetAutoCommit() throws SQLException {
    	
    }

//    public void rollback() throws TransactionException {
//    	try
//    	{
//    		this.con.rollback();
//    		this.con.setAutoCommit(this.toggleAutoCommit);
//    	}
//    	catch(Exception e)
//    	{
//    		throw new TransactionException(e);
//    	}
//    }

    public void rollbackAndResetAutoCommit() throws SQLException {
    	
    }

    public void toggleAutoCommit() {
    }

    public boolean wasRolledBack() {
        return this.status == Status.STATUS_ROLLEDBACK 
       ;
    }

    public boolean wasCommitted() {
        return this.status == Status.STATUS_COMMITTED 
        ;
    }

    public boolean isActive() {
        return false;
    }

//    public void registerSynchronization(Synchronization sync) throws
//        TransactionException {
//    }

    public void beforeTransactionCompletion() {
    }

    public void afterTransactionCompletion(int status) {
    }

	public void createTransaction() {
		
		
	}
	

	public boolean delistResource(XAResource arg0, int arg1) throws IllegalStateException, SystemException {
		// TODO Auto-generated method stub
		return false;
	}
	public boolean enlistResource(XAResource arg0) throws RollbackException, IllegalStateException, SystemException {
		
		return false;
	}
	public int getStatus()  {
		
		return this.status;
	}
	public void setRollbackOnly() throws IllegalStateException, SystemException {
		this.status = Status.STATUS_MARKED_ROLLBACK;
//		this.rollback();
		
	}
	protected void commit() throws RollbackException, HeuristicMixedException, HeuristicRollbackException, SecurityException, IllegalStateException, SystemException {
		this.commit(null);
		
	}
	
	private void updataStatus(int status)
	{
		this.status = status;
	}
	

	/**
	 * 事务引用计数器减一
	 */
	public void decreament()
	{
		this.count --;
	}
	protected void commit(String dbName) throws RollbackException, HeuristicMixedException, HeuristicRollbackException, SecurityException, IllegalStateException, SystemException {

		if(this.status == Status.STATUS_MARKED_ROLLBACK)
		{
			System.out.println("标记为待回滚的事务");
//			this.rollback();
			throw new RollbackException("Commit " + dbName + " TX failed: TX MARKED_ROLLBACK" );
		}

		decreament();
		if(count <= 0)
		{
			this.status = Status.STATUS_PREPARING;
			try {
				this.commitAll();
				this.status = Status.STATUS_COMMITTED;
			} catch (SQLException e) {
//				this.status = Status.STATUS_MARKED_ROLLBACK;
				try {
					this.setRollbackOnly();
				} catch (SystemException se) {
					logger.error("this.setRollbackOnly(); failed:",se);
				}
				this.increament(); //重新加1防止在回滚时重复减1
				throw new RollbackException(e.getMessage());						
			}
		}

	}
	
	private void commitAll() throws SQLException
	{
		
		while (executeStack.size() > 0 )
		{
			TransactionEntity txentity = this.executeStack.pop() ;
			if(txentity == null)
				break;
			else
			{
				txentity.commit();
			}
		}		
		this.txentities.clear();
	}

	private void rollbackAll() 
	{
		
		/**
		 * 每次显示地调用回滚操作后，立即释放所有的资源，回滚所有的数据库事务
		 */
		while (executeStack.size() > 0)
		{
			TransactionEntity txentity = this.executeStack.pop() ;
			if(txentity == null)
				break;
			else
			{
				txentity.rollback();
				
			}
		}	
		this.txentities.clear();
	}
	public void registerSynchronization(Synchronization arg0) throws RollbackException, IllegalStateException, SystemException {
		
		
	}
	
	protected void rollback() throws IllegalStateException, SystemException {
		this.rollback(null);
		
	}
	/**
	 * 一个回滚全部回滚
	 * @param dbName
	 * @throws IllegalStateException
	
	 */
	protected void rollback(String dbName) throws IllegalStateException {
		
		
		if(this.status == Status.STATUS_ROLLEDBACK)
		{
//			if(executeStack.size() > 0)
//			{
//				System.out.println("executeStack ");
//			}
			throw new IllegalStateException("事务已经回滚");
		}
		
		if(this.status == Status.STATUS_COMMITTED)
		{
//			if(executeStack.size() > 0)
//			{
//				System.out.println("executeStack");
//			}
			throw new IllegalStateException("事务已经提交");
		}
		/**
		 * 计数器减一
		 */
		this.decreament();
		if(this.count <= 0)
		{
			this.rollbackAll();
			this.status = Status.STATUS_ROLLEDBACK;
		}
		else
		{
//			this.status = Status.STATUS_MARKED_ROLLBACK;
			try {
				this.setRollbackOnly();
			} catch (SystemException e) {
				logger.error("this.setRollbackOnly(); failed:",e);
			}
			if(this.currenttxtype != TransactionManager.RW_TRANSACTION)
			{
				this.rollbackAll();
			}
		}
		
		
		
	}

	protected JDBCTransaction getParent_tx() {
		return parent_tx;
	}



 
}
