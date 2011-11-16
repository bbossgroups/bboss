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
package org.frameworkset.spi.txsyn;

import javax.transaction.RollbackException;

import org.frameworkset.spi.BaseSPIManager;
import org.frameworkset.spi.SPIException;

import com.frameworkset.orm.transaction.TransactionManager;

public class TestTXSyn {
	public static void testTX2ndSyn()
	{
		try {
			AI a = (AI) BaseSPIManager.getProvider("txsyn.a","A1");
			try {
				/**
				 * 两个提供者方法都调用成功，共用相同的事务
				 */
				a.testTXSynSuccess();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				/**
				 * A1事务执行成功，A2同步调用的方法事务执行失败，所以整个事务执行失败
				 */
				a.testTXSynFailed();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				/**
				 * 执行事务，不同步调用其他服务方法
				 */
				a.testTXNoSyn();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			try {
				/**
				 * 同步调用A1和A2的方法，没有事务环境
				 */
				a.testNoTXSyn();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				/**
				 * 既没有同步控制，也没有事务控制
				 */
				a.testNoTXNoSyn();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			try {
				/**
				 * 包含声明式事务的同步方法，并且定义了事务回滚的异常类型为SynException 
				 * 但是同步方法抛出异常类型为SynException，所以事务会回滚
				 */
				a.testWithSpecialException(1);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
//			
			try {
				/**
				 * 包含声明式事务的同步方法，并且定义了事务回滚的异常类型为SynException 
				 * 但是同步方法抛出异常类型为Exception，所以事务不会回滚
				 */
				a.testWithSpecialException(2);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (SPIException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void testTX2ndSynWithOuterTX()
	{
		TransactionManager tm = new TransactionManager();
		try {
			tm.begin();
			AI a = (AI) BaseSPIManager.getProvider("txsyn.a","A1");
			try {
				/**
				 * 两个提供者方法都调用成功，共用相同的事务
				 */
				a.testTXSynSuccess();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				/**
				 * A1事务执行成功，A2同步调用的方法事务执行失败，所以整个事务执行失败
				 */
				a.testTXSynFailed();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				/**
				 * 执行事务，不同步调用其他服务方法
				 */
				a.testTXNoSyn();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			try {
				/**
				 * 同步调用A1和A2的方法，没有事务环境
				 */
				a.testNoTXSyn();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				/**
				 * 既没有同步控制，也没有事务控制
				 */
				a.testNoTXNoSyn();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			try {
				/**
				 * 包含声明式事务的同步方法，并且定义了事务回滚的异常类型为SynException 
				 * 但是同步方法抛出异常类型为SynException，所以事务会回滚
				 */
				a.testWithSpecialException(1);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
//			
			try {
				/**
				 * 包含声明式事务的同步方法，并且定义了事务回滚的异常类型为SynException 
				 * 但是同步方法抛出异常类型为Exception，所以事务不会回滚
				 */
				a.testWithSpecialException(2);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
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
		}
		catch(Exception e)
		{
			e.printStackTrace();
			try {
				tm.rollback();
			} catch (RollbackException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}
	
	public static void testSingle()
	{
		try {
			AI a = (AI) BaseSPIManager.getProvider("txsyn.a","A1");
			AI a1 = (AI) BaseSPIManager.getProvider("txsyn.a","A1");
			AI a2 = (AI) BaseSPIManager.getProvider("txsyn.a","A2");
			AI a21 = (AI) BaseSPIManager.getProvider("txsyn.a","A2");
			
			System.out.println("a:" + a);
			System.out.println("a1:" + a1);
			System.out.println("a2:" + a2);
			System.out.println("a21:" + a21);
		} catch (SPIException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args)
	{
		testSingle();
		testTX2ndSyn();
		
		
//		testTX2ndSynWithOuterTX();
	}
}
