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
package org.frameworkset.spi.transaction;

import javax.transaction.RollbackException;

import org.frameworkset.spi.BaseApplicationContext;
import org.frameworkset.spi.DefaultApplicationContext;
import org.frameworkset.spi.SPIException;
import org.junit.Before;
import org.junit.Test;

import com.frameworkset.common.poolman.DBUtil;
import com.frameworkset.orm.transaction.TransactionManager;

public class TestTX {
	private BaseApplicationContext context ;
	@Before
	public void init()
	{
//		context = ApplicationContext.getApplicationContext("org/frameworkset/spi/transaction" +
//				"/manager-transaction.xml");
		context = DefaultApplicationContext.getApplicationContext("org/frameworkset/spi/transaction" +
		"/manager-transaction.xml");
	}
	/**
	 * 通过aop测试声明式事务管理功能，
	 * 事务一执行成功
	 * 事务二执行失败
	 */
	@Test
	public void testTX()
	{
		try {
			A1 a = context.getTBeanObject("tx.a",A1.class);
			//事务一
			a.testTXInvoke();
			//事务二
			a.testTXInvoke("hello.");
			
		} catch (SPIException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 通过aop测试声明式事务管理功能，对声明的不同类型的事务进行组合测试
	 * 首先构造外部事务对不同的事务进行交叉测试
	 * 事务一执行成功
	 * 事务二执行成功
	 */
	@Test
	public void testTRANSACTION_TYPEWithOuterTX()
	{
		int type = 2;
		TransactionManager tm = new TransactionManager();
		try {
			
			switch(type)
			{
				case 1:
					tm.begin(TransactionManager.NEW_TRANSACTION);
					break;
				case 2:
					tm.begin(TransactionManager.REQUIRED_TRANSACTION);
					break;
				case 3:
					tm.begin(TransactionManager.MAYBE_TRANSACTION);
					break;
				case 4:
					tm.begin(TransactionManager.NO_TRANSACTION);
					break;
				default:
					tm.begin(TransactionManager.REQUIRED_TRANSACTION);
					
			}
			System.out.println("testNEW_TRANSACTION()外部事务类型：" + tm.getCurrenttxtypeName());
			System.out.println("testNEW_TRANSACTION()外部事务对象：" + TransactionManager.getTransaction());
			AI a = (AI)context.getBeanObject("tx.a");
			
			try
			{
				//事务一 REQUIRED_TRANSACTION，事务与外部事务保持一致 内外部成功则成功
				a.testTXInvoke();
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			try
			{
				//事务二 NEW_TRANSACTION 内部失败，不影响外部事务
				a.testTXInvoke("hello.");
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			
			try
			{
				//事务三 REQUIRED_TRANSACTION,事务与外部事务保持一致 内外部成功则成功,内部事务失败
				a.testTXInvokeWithReturn();
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			
			
			try
			{
				//事务四 MAYBE_TRANSACTION，与外部事务一致，外部事务提交成功，则成功
				//内部执行失败，导致外部事务失败，因此所有与外部事务相关的事务都会失败
				a.testTXInvokeWithException();
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			
			//事务四 MAYBE_TRANSACTION
			try
			{
				//事务五 NO_TRANSACTION，与外部事务无关，没有事务环境，内部执行成功则成功，否则失败
				a.testSameName();
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			
			tm.commit();
			
		} catch (SPIException e) {
			try {
				tm.rollback();
			} catch (RollbackException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			try {
				tm.rollback();
			} catch (RollbackException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 通过aop测试声明式事务管理功能，对声明的不同类型的事务进行组合测试
	 * 对不同的事务进行交叉测试
	 * 该方法没有外部事务
	 * 测试结果test表记录：

testTXInvoke()	test185		(HugeBlob)
testTXInvokeWithException()	test188		(HugeBlob)
testSameName()	test189		(HugeBlob)

test1表结果
testTXInvoke
	 */
	@Test
	public void testTRANSACTION_TYPE()
	{
		try
		{
			AI a = (AI)context.getBeanObject("tx.a");
			
			try
			{
				//事务一 REQUIRED_TRANSACTION
				a.testTXInvoke();
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			try
			{
				//事务二 NEW_TRANSACTION 内部失败
				a.testTXInvoke("hello.");
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			
			try
			{
				//事务三 REQUIRED_TRANSACTION,
				a.testTXInvokeWithReturn();
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			
			
			try
			{
				//事务四 MAYBE_TRANSACTION
				a.testTXInvokeWithException();
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			
			
			try
			{
				//事务五 NO_TRANSACTION，没有事务环境，内部执行成功则成功，否则失败
				a.testSameName();
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
			
			
			
		
	}
	
	
	
	
	
	@Test
	public void testTXWithReturn()
	{
		try {
			AI a = (AI)context.getBeanObject("tx.a");
			//事务一
			System.out.println("a.testTXInvokeWithReturn():" +a.testTXInvokeWithReturn());
		} catch (SPIException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	/**
	 * 通过aop测试声明式事务管理功能，同时在外层又嵌了一层事务这样事务一和事务二
	 * 有被包含在了外层事务中
	 * 事务一执行成功
	 * 事务二执行失败
	 * 
	 * 整个外层事务执行失败，事务一和事务二都被回滚
	 */
	@Test
	public void testOutTX()
	{
		TransactionManager tm = new TransactionManager();
		try {
			tm.begin();
			AI a = (AI)context.getBeanObject("tx.a");
			//事务一
			a.testTXInvoke();
			//事务二
			a.testTXInvoke("hello.");
			tm.commit();
		} catch (SPIException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			try {
				tm.rollback();
			} catch (RollbackException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			try {
				tm.rollback();
			} catch (RollbackException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}
	
	@Test
	public void testNoTX()
	{
		AI a = null;
		try {
			a = (AI)context.getBeanObject("tx.a");
			a.testNoTXInvoke();
		} catch (SPIException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	@Test
	public void testTXWithException()
	{
		AI a = null;
		try {
			a = (AI)context.getBeanObject("tx.a");
			a.testTXInvokeWithException();
		} catch (SPIException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	@Test
	public void testTXWithSpecialExceptions()
	{
		AI a = null;
		try {
			a = (AI)context.getBeanObject("tx.a");
			
		} catch (SPIException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			a.testTXWithSpecialExceptions("IMPLEMENTS");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			a.testTXWithSpecialExceptions("exception1");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			a.testTXWithSpecialExceptions("INSTANCEOF");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			a.testTXWithSpecialExceptions("notxexception");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void testTXWithInstanceofExceptions()
	{
		AI a = null;
		try {
			a = (AI)context.getBeanObject("tx.a");
			
		} catch (SPIException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			a.testTXWithInstanceofExceptions("IMPLEMENTS");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			a.testTXWithInstanceofExceptions("INSTANCEOF");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			a.testTXWithInstanceofExceptions("notxexception");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void testTXWithImplementsofExceptions()
	{
		AI a = null;
		try {
			a = (AI)context.getBeanObject("tx.a");
			
		} catch (SPIException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			a.testTXWithImplementsofExceptions("IMPLEMENTS");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			a.testTXWithImplementsofExceptions("INSTANCEOF");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			a.testTXWithImplementsofExceptions("notxexception");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 测试模式方法事务控制
	 */
	@Test
	public void testPatternTX()
	{
		AI a = null;
		try {
			a = (AI)context.getBeanObject("tx.a");
			
		} catch (SPIException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int i = 1;
		switch(i)
		{
		case 1:
			try {
				a.testPatternTX1("IMPLEMENTS");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				a.testPatternTX1("INSTANCEOF");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				a.testPatternTX1("notxexception");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		case 2:
			try {
				a.testPatternTX2("IMPLEMENTS");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				a.testPatternTX2("INSTANCEOF");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				a.testPatternTX2("notxexception");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		case 3:
			try {
				a.testPatternTX3("IMPLEMENTS");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				a.testPatternTX3("INSTANCEOF");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				a.testPatternTX3("notxexception");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		case 4:
			try {
				a.testPatternTX4("IMPLEMENTS");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				a.testPatternTX4("INSTANCEOF");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				a.testPatternTX4("notxexception");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		}
	}
	
	@Test
	public void testSystemException()
	{
		
		try {
			AI a = (AI)context.getBeanObject("tx.a");
			a.testSystemException();
			
		} catch (SPIException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args)
	{
		System.out.println("0DBUtil.getNumIdle():"+DBUtil.getNumIdle());
		System.out.println("0DBUtil.getNumActive():"+DBUtil.getNumActive());
//		testOutTX();
//		testTX();
//		testNoTX();
//		testTXWithReturn();
//		testTXWithException();
		
//		System.out.println("****************************************************************************");
//		System.out.println("****************************************************************************");
//		testTXWithSpecialExceptions();
//		System.out.println("****************************************************************************");
//		System.out.println("****************************************************************************");
//		testTXWithInstanceofExceptions();
//		System.out.println("****************************************************************************");
//		System.out.println("****************************************************************************");
//		testTXWithImplementsofExceptions();
//		System.out.println("****************************************************************************");
//		System.out.println("****************************************************************************");
//		testPatternTX(1);
//		System.out.println("****************************************************************************");
//		System.out.println("****************************************************************************");
//		testPatternTX(2);
//		System.out.println("****************************************************************************");
//		System.out.println("****************************************************************************");
//		testPatternTX(3);
//		System.out.println("****************************************************************************");
//		System.out.println("****************************************************************************");
//		testPatternTX(4);
//		System.out.println("****************************************************************************");
//		System.out.println("****************************************************************************");
		
//		testSystemException();
		System.out.println("****************************************************************************");
		System.out.println("****************************************************************************");
//		testTRANSACTION_TYPEWithOuterTX(1);
//		testTRANSACTION_TYPE();
		System.out.println("****************************************************************************");
		System.out.println("****************************************************************************");
		
		
		System.out.println("end DBUtil.getNumIdle():"+DBUtil.getNumIdle());
		System.out.println("end DBUtil.getNumActive():"+DBUtil.getNumActive());
	}

}
