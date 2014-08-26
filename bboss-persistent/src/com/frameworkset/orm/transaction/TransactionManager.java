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

import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.InvalidTransactionException;
import javax.transaction.RollbackException;
import javax.transaction.Status;
import javax.transaction.SystemException;

import org.apache.log4j.Logger;

import com.frameworkset.orm.annotation.TransactionType;

/**
 * 
 * <p>Title: TransactionManager</p>
 *
 * <p>Description: 实现数据库的事务框架</p>
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
public class TransactionManager implements java.io.Serializable {
	
	private static final Logger log = Logger.getLogger(TransactionManager.class);
	

	static final ThreadLocal thread_local = new ThreadLocal();
	private boolean started = false;
	private boolean committed = false;
	private boolean rollbacked = false;
	private JDBCTransaction context_tx = null;
	
	private JDBCTransaction suspend_tx = null;
	
	
	/**
	 * 始终创建新事务
	 */
	public static final TransactionType NEW_TRANSACTION = TransactionType.NEW_TRANSACTION;
	
	/**
	 * 如果没有事务创建新事务，有事务加入当前事务
	 */
	public static final TransactionType REQUIRED_TRANSACTION = TransactionType.REQUIRED_TRANSACTION;
	/**
	 * 有事务就加入事务，没有不创建事务,默认情况
	 */
	public static final TransactionType MAYBE_TRANSACTION = TransactionType.MAYBE_TRANSACTION;
	/**
	 * 没有事务
	 */
	public static final TransactionType NO_TRANSACTION = TransactionType.NO_TRANSACTION;
	/**
     * 读写事务
     */
    public static final TransactionType RW_TRANSACTION = TransactionType.RW_TRANSACTION;
	
	/**
	 * 未知事务
	 */
	public static final TransactionType UNKNOWN_TRANSACTION = TransactionType.UNKNOWN_TRANSACTION;

	private TransactionType currenttxtype = TransactionType.REQUIRED_TRANSACTION;

	private static final long serialVersionUID = -6716097342564237376l;

	/**
	 * Starts a new transaction, and associate it with the calling thread.
	 * 
	 * @throws TransactionException
	 * 
	 * @throws javax.transaction.NotSupportedException
	 *             If the calling thread is already associated with a
	 *             transaction, and nested transactions are not supported.
	 * @throws javax.transaction.SystemException
	 *             If the transaction service fails in an unexpected way.
	 */
	public void begin() throws TransactionException {
//		if(this.committed )
//			throw new TransactionException("事务已经提交");
//		
//		if(this.rollbacked)
//			throw new TransactionException("事务已经回滚");
//		if(this.started == true)
//			throw new TransactionException("事务已经启动，并且事务正在执行中。");
//		
//		JDBCTransaction currentTx = (JDBCTransaction) thread_local.get();
//
//		if (currentTx != null) {
//
//			currentTx.begin();
//
//		} else {
//			currentTx = new JDBCTransaction(this.currenttxtype);
//			thread_local.set(currentTx);
//			currentTx.begin();
//
//		}
//		this.started = true;
	    begin(REQUIRED_TRANSACTION);

	}

	/**
	 * Starts a new transaction, and associate it with the calling thread.
	 * 
	 * @param int
	 *            tx_type (默认值为REQUIRED_TRANSACTION）
	 * 
	 * 
	 * 
	 * 始终创建新事务
	 * 
	 * JDBCTransaction.public static final int NEW_TRANSACTION = 0;
	 * 
	 * 
	 * 如果没有事务创建新事务，有事务加入当前事务(默认值)
	 * 
	 * JDBCTransaction.public static final int REQUIRED_TRANSACTION = 2;  
	 * 
	 * 
	 * 有事务就加入事务，没有不创建事务,默认情况
	 * 
	 * JDBCTransaction.public static final int MAYBE_TRANSACTION = 3;
	 * 
	 * 没有事务
	 * 
	 * JDBCTransaction.public static final int NO_TRANSACTION = 4;
	 * @throws TransactionException 
	 * 
	 * @throws javax.transaction.NotSupportedException
	 *             If the calling thread is already associated with a
	 *             transaction, and nested transactions are not supported.
	 * @throws javax.transaction.SystemException
	 *             If the transaction service fails in an unexpected way.
	 */
	public void begin(TransactionType tx_type) throws TransactionException {
		if(this.committed )
			throw new TransactionException("事务已经提交");
		
		if(this.rollbacked)
			throw new TransactionException("事务已经回滚");
		if(this.started == true)
			throw new TransactionException("事务已经启动，并且事务正在执行中。");
		
		JDBCTransaction currentTx = (JDBCTransaction) thread_local.get();
		currenttxtype = tx_type;
		switch (tx_type) {
			case NEW_TRANSACTION:
				if(currentTx == null)
				{
					JDBCTransaction newTx = new JDBCTransaction(NEW_TRANSACTION);
					thread_local.set(newTx);
					newTx.begin();
				}
				else
				{
					this.context_tx = currentTx;
					JDBCTransaction newTx = new JDBCTransaction(currentTx,NEW_TRANSACTION);
					newTx.begin();
					thread_local.set(newTx);
				}
				break;
			case RW_TRANSACTION:
			    if (currentTx != null) {
			        if(currentTx.getTXType() == RW_TRANSACTION)
			        {
			            currentTx.begin();
			        }
			        else
			        {
			            throw new TransactionException("当前存在类型为["+ currentTx.getTXType() +"]的事务，与RW_TRANSACTION类型事务不兼容，无法创建RW_TRANSACTION类型事务。");
			        }
                    

                } else {
                    currentTx = new JDBCTransaction(RW_TRANSACTION);
                    thread_local.set(currentTx);
                    currentTx.begin();
                }
                break;
			case MAYBE_TRANSACTION:
	
				if (currentTx == null)
					;
				else
					currentTx.begin();
				break;
	
			case REQUIRED_TRANSACTION:
				
				if (currentTx != null ) 
				{
				    if(currentTx.getTXType() != RW_TRANSACTION)
				    {
				        currentTx.begin();
				    }
				    else
				    {
				        throw new TransactionException("当前存在类型为["+ currentTx.getTXType() +"]的事务，与REQUIRED_TRANSACTION类型事务不兼容，无法创建REQUIRED_TRANSACTION类型事务。");
				    }
				} 
				else 
				{
					currentTx = new JDBCTransaction(REQUIRED_TRANSACTION);
					thread_local.set(currentTx);
					currentTx.begin();
				}	
				break;			
			case NO_TRANSACTION:				
				try {
					suspend_tx = suspendAll();
				} catch (SystemException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	
				break;
	
			default:
		
		}
		started = true;
		
	}
	private void assertTX() throws RollbackException
	{
		if(!this.started )
			throw new RollbackException("事务没有开始");
		if(this.committed )
			throw new RollbackException("事务已经提交");
		
		if(this.rollbacked)
			throw new RollbackException("事务已经回滚");
	}

	/**
	 * Commit the transaction associated with the calling thread.
	 * 
	 * @throws javax.transaction.RollbackException
	 *             If the transaction was marked for rollback only, the
	 *             transaction is rolled back and this exception is thrown.
	 * @throws IllegalStateException
	 *             If the calling thread is not associated with a transaction.
	 * @throws javax.transaction.SystemException
	 *             If the transaction service fails in an unexpected way.
	 * @throws javax.transaction.HeuristicMixedException
	 *             If a heuristic decision was made and some some parts of the
	 *             transaction have been committed while other parts have been
	 *             rolled back.
	 * @throws javax.transaction.HeuristicRollbackException
	 *             If a heuristic decision to roll back the transaction was
	 *             made.
	 * @throws SecurityException
	 *             If the caller is not allowed to commit this transaction.
	 */
	public void commit() throws RollbackException {
		assertTX() ;
		JDBCTransaction tx = getTransaction();
		
		try
		{
			if(this.currenttxtype == NO_TRANSACTION)
			{
				try {
					this.resume(this.suspend_tx);
				} catch (InvalidTransactionException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IllegalStateException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (SystemException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				this.committed = true;
				return;
				
			}
			
				
			int status;			
			
			if (tx == null)
			{
				//added by biaoping.yin on 2008.09.02
				if(this.currenttxtype == MAYBE_TRANSACTION)
				{
					this.committed = true;
					return ;
				}
				//added end
				throw new IllegalStateException(
						"thread not associated with transaction");
			}
			if(this.context_tx != null)
			{
				if(context_tx == tx)
					return;
			}
			
			
			
			
			status = tx.getStatus();
			if (status == Status.STATUS_MARKED_ROLLBACK)
			{
				try
				{
					log.debug("Commit execute rolling back action.");
					//如果事务被标识为回滚，则直接回滚事务，提交过程转入回滚过程
					this.rollback();
					this.rollbacked = true;
					return ;
				}
				catch(Exception e)
				{
					this.rollbacked = true;
				}
			}
			else
			{
				try {
					tx.commit();
	
				} catch (SecurityException e) {
					try
					{						
						this.rollback();
						this.rollbacked = true;
						
					}
					catch(Exception ei)
					{
						this.rollbacked = true;
					}
					throw new RollbackException(e.getMessage());
				} catch (IllegalStateException e) {
					try
					{
						
						this.rollback();
						this.rollbacked = true;
						
					}
					catch(Exception ei)
					{
						this.rollbacked = true;
					}
					throw new RollbackException(e.getMessage());
				} catch (RollbackException e) {
					try
					{
						
						this.rollback();
						this.rollbacked = true;
						
					}
					catch(Exception ei)
					{
						this.rollbacked = true;
					}
					throw e;
				} catch (HeuristicMixedException e) {
					try
					{
						
						this.rollback();
						this.rollbacked = true;
						
					}
					catch(Exception ei)
					{
						this.rollbacked = true;
					}
					throw new RollbackException(e.getMessage());
				} catch (HeuristicRollbackException e) {
					try
					{
						
						this.rollback();
						this.rollbacked = true;
						
					}
					catch(Exception ei)
					{
						this.rollbacked = true;
					}
					throw new RollbackException(e.getMessage());
				} catch (SystemException e) {
					try
					{
						
						this.rollback();
						this.rollbacked = true;
						
					}
					catch(Exception ei)
					{
						this.rollbacked = true;
					}
					throw new RollbackException(e.getMessage());
				}
			}
			
		}
		finally {
//			if (tx.wasCommitted() || flag || rollbacked) {
//			if (tx.wasCommitted() || tx.wasRolledBack()) {
			if (tx != null && tx.wasCommitted() ) {
				if(this.context_tx == null)
				{
					
					setTransaction(null);
				}
				else
				{
					
					
					this.setTransaction(context_tx);
					//this.context_tx = null;
				}
			}
			this.committed = true;
		}

	}

	/**
	 * Rolls back the transaction associated with the calling thread.
	 * 
	 * @throws IllegalStateException
	 *             If the transaction is in a state where it cannot be rolled
	 *             back. This could be because the calling thread is not
	 *             associated with a transaction, or because it is in the
	 *             {@link javax.transaction.Status#STATUS_PREPARED prepared state}.
	 * @throws SecurityException
	 *             If the caller is not allowed to roll back this transaction.
	 * @throws javax.transaction.SystemException
	 *             If the transaction service fails in an unexpected way.
	 */
	public void rollback() throws RollbackException {
		_rollback(true);
	}
	
	private void _rollback(boolean check) throws RollbackException {
		if(check)
			assertTX() ;
		if(this.currenttxtype == NO_TRANSACTION)
		{
			try {				
				this.resume(this.suspend_tx);
			} catch (InvalidTransactionException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IllegalStateException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (SystemException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			this.rollbacked = true;
			return;
		}
		JDBCTransaction tx = getTransaction();
		if (tx == null)
		{
			//added by biaoping.yin on 2008.09.02
			if(this.currenttxtype == MAYBE_TRANSACTION)
			{
				this.rollbacked = true;
				return;
			}
			//added end
			throw new IllegalStateException(
					"no transaction associated with thread or tx has been commited or rollbacked.");
		}
		
		if(this.context_tx != null)
		{
			if(context_tx == tx) //该条件是否会成立呢
			{
			
				this.rollbacked = true;
				return;
			}
		}
		try {
			tx.rollback();
		} catch (IllegalStateException e) {
			
			e.printStackTrace();
		} catch (SystemException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			this.rollbacked = true;
			if(tx != null && tx.wasRolledBack() )
			{
				if(this.context_tx == null)
				{
					setTransaction(null);
				}
				else
				{
					this.setTransaction(this.context_tx);
	//				this.context_tx = null;
				}
			}
		}
	}

//	/**
//	 * Mark the transaction associated with the calling thread for rollback
//	 * only.
//	 * 
//	 * @throws IllegalStateException
//	 *             If the transaction is in a state where it cannot be rolled
//	 *             back. This could be because the calling thread is not
//	 *             associated with a transaction, or because it is in the
//	 *             {@link javax.transaction.Status#STATUS_PREPARED prepared state}.
//	 * @throws javax.transaction.SystemException
//	 *             If the transaction service fails in an unexpected way.
//	 */
//	public void setRollbackOnly() throws IllegalStateException, SystemException {
//		
//		JDBCTransaction tx = getTransaction();
//		if (tx == null)
//			throw new IllegalStateException(
//					"no transaction associated with calling thread");
//		tx.setRollbackOnly();
//	}

	/**
	 * Get the status of the transaction associated with the calling thread.
	 * 
	 * @return The status of the transaction. This is one of the
	 *         {@link javax.transaction.Status} constants. If no transaction is
	 *         associated with the calling thread,
	 *         {@link javax.transaction.Status#STATUS_NO_TRANSACTION} is
	 *         returned.
	 * @throws javax.transaction.SystemException
	 *             If the transaction service fails in an unexpected way.
	 */
	public int getStatus() {
		JDBCTransaction tx = getTransaction();
		return tx != null ? tx.getStatus() : Status.STATUS_NO_TRANSACTION;
		
		
	}

	/**
	 * Get the transaction associated with the calling thread.
	 * 
	 * @return The transaction associated with the calling thread, or
	 *         <code>null</code> if the calling thread is not associated with
	 *         a transaction.
	 * @throws javax.transaction.SystemException
	 *             If the transaction service fails in an unexpected way.
	 */
	public static JDBCTransaction getTransaction() {
		JDBCTransaction tx = (JDBCTransaction) thread_local.get();
	
		return tx;
	}

	/**
	 * Change the transaction timeout for transactions started by the calling
	 * thread with the {@link #begin()} method.
	 * 
	 * @param seconds
	 *            The new timeout value, in seconds. If this parameter is
	 *            <code>0</code>, the timeout value is reset to the default
	 *            value.
	 * @throws javax.transaction.SystemException
	 *             If the transaction service fails in an unexpected way.
	 */
	public void setTransactionTimeout(int seconds) throws SystemException {
		throw new SystemException("not supported");
	}

	/**
	 * Suspend the association the calling thread has to a transaction, and
	 * return the suspended transaction. When returning from this method, the
	 * calling thread is no longer associated with a transaction.
	 * 
	 * @return The transaction that the calling thread was associated with, or
	 *         <code>null</code> if the calling thread was not associated with
	 *         a transaction.
	 * @throws javax.transaction.SystemException
	 *             If the transaction service fails in an unexpected way.
	 */
	public JDBCTransaction suspend() throws SystemException {
		JDBCTransaction retval = getTransaction();
		if(this.context_tx != null)
		{
			setTransaction(context_tx);
		}
		else
		{
			setTransaction(null);
		}
		return retval;
	}
	
	/**
	 * Suspend the association the calling thread has to a transaction, and
	 * return the suspended transaction. When returning from this method, the
	 * calling thread is no longer associated with a transaction.
	 * 
	 * @return The transaction that the calling thread was associated with, or
	 *         <code>null</code> if the calling thread was not associated with
	 *         a transaction.
	 * @throws javax.transaction.SystemException
	 *             If the transaction service fails in an unexpected way.
	 */
	public JDBCTransaction suspendAll() throws SystemException {
		JDBCTransaction retval = getTransaction();
		setTransaction(null);
		
		return retval;
	}

	/**
	 * Resume the association of the calling thread with the given transaction.
	 * 
	 * @param tx
	 *            The transaction to be associated with the calling thread.
	 * @throws javax.transaction.InvalidTransactionException
	 *             If the argument does not represent a valid transaction.
	 * @throws IllegalStateException
	 *             If the calling thread is already associated with a
	 *             transaction.
	 * @throws javax.transaction.SystemException
	 *             If the transaction service fails in an unexpected way.
	 */
	public void resume(JDBCTransaction tx) throws InvalidTransactionException,
			IllegalStateException, SystemException {
		
		setTransaction(tx);
	}

	/**
	 * Just used for unit tests
	 * 
	 * @param tx
	 */
	protected void setTransaction(JDBCTransaction tx) {
		thread_local.set(tx);		
	}
	
	/**
	 * 如果成功销毁事务，则返回true
	 * 如果没有需要销毁的事务则返回false
	 * @return boolean
	 */
	public static boolean destroyTransaction()
	{
		JDBCTransaction tx = getTransaction();
		
		if(tx != null)
		{
			tx.printStackTrace();
			try {
				tx.rollback();
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
			} catch (SystemException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
			}
			thread_local.set(null);
			return true;
			
		}
		return false;
	}
	
	/**
	 * 获取当前事务的类型
	 * @return
	 */
	public TransactionType getCurrenttxtype() {
		return this.currenttxtype;
	}
	
	/**
	 * 获取当前事务的类型名称
	 * @return
	 */
	public String getCurrenttxtypeName() {
		switch(currenttxtype)
		{
			case NEW_TRANSACTION:
				return "NEW_TRANSACTION";
			case REQUIRED_TRANSACTION:
				return "REQUIRED_TRANSACTION";
			case MAYBE_TRANSACTION:
				return "MAYBE_TRANSACTION";
			case NO_TRANSACTION:
				return "NO_TRANSACTION";
			case RW_TRANSACTION:
                return "RW_TRANSACTION";
			default:				
				return "UNKNOWN_TRANSACTION";
		}
	}
	/**
	 * 在final方法中调用，用来在出现异常时对事务资源进行回收，首先对事务进行回滚，
	 * 然后回收资源，如果事务已经提交和回滚，则不做任何操作
	 */
	public void release()
	{
		if(!this.started )
//			throw new RollbackException("事务没有开始");
			return ;
		if(this.committed )
//			throw new RollbackException("事务已经提交");
			return ;
		
		if(this.rollbacked)
//			throw new RollbackException("事务已经回滚");
			return;
		try {
			log.debug("Warnning:Detected uncommit Transaction,force rollbacked this transaction by system.",new Exception());
			this._rollback(false);
		} catch (Throwable e) {
			
//			e.printStackTrace();
		}
		
	}
	
	/**
	 * 在final方法中调用，用来在出现异常时对事务资源进行回收，首先对事务进行回滚，
	 * 然后回收资源，如果事务已经提交和回滚，则不做任何操作
	 */
	public void releasenolog()
	{
		if(!this.started )
//			throw new RollbackException("事务没有开始");
			return ;
		if(this.committed )
//			throw new RollbackException("事务已经提交");
			return ;
		
		if(this.rollbacked)
//			throw new RollbackException("事务已经回滚");
			return;
		try {
//			log.debug("Warnning:Detected uncommit Transaction,force rollbacked this transaction by system.",new Exception());
			this._rollback(false);
		} catch (Throwable e) {
			
//			e.printStackTrace();
		}
		
	}
	

}
