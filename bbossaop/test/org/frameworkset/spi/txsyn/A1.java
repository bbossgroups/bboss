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

import com.frameworkset.common.poolman.DBUtil;
import com.frameworkset.orm.transaction.TransactionManager;

public class A1 implements AI {

	/**
	 * 调用方法执行成功,同步方法执行失败
	 */
	public void testNoTXSyn() throws Exception {
		System.out.println("A1.testNoTXSyn():");
		System.out.println("A1.testNoTXSyn() context tx:" + TransactionManager.getTransaction());
		DBUtil db = new DBUtil();
		String id = db.getNextStringPrimaryKey("test");
		db.executeInsert("insert into test(id,name) values('"+id+"','A1.testNoTXSyn()')");
		
	}

	/**
	 * 事务成功
	 */
	public void testTXNoSyn() throws Exception {
		
		System.out.println("A1.testTXNoSyn():");
		System.out.println("A1.testTXNoSyn() context tx:" + TransactionManager.getTransaction());
		DBUtil db = new DBUtil();
		String id = db.getNextStringPrimaryKey("test");
		db.executeInsert("insert into test(id,name) values('"+id+"','A1.testTXNoSyn()')");
	}

	/**
	 * 事务执行成功，同步调用的方法事务执行失败，所以整个事务执行失败
	 */
	public void testTXSynFailed() throws Exception {
		System.out.println("A1.testTXSynFailed():");
		System.out.println("A1.testTXSynFailed() context tx:" + TransactionManager.getTransaction());
		DBUtil db = new DBUtil();
		String id = db.getNextStringPrimaryKey("test");
db.executeInsert("insert into test(id,name) values('"+id+"','A1.testTXSynFailed()')");
		
	}

	public void testTXSynSuccess() throws Exception {
		System.out.println("A1.testTXSynSuccess():");
		System.out.println("A1.testTXSynSuccess() context tx:" + TransactionManager.getTransaction());
		DBUtil db = new DBUtil();
		String id = db.getNextStringPrimaryKey("test");
db.executeInsert("insert into test(id,name) values('"+id+"','A1.testTXSynSuccess()')");
		
	}
	
	public void testNoTXNoSyn() throws Exception
	{
		System.out.println("A1.testNoTXNoSyn():");
		System.out.println("A1.testNoTXNoSyn() context tx:" + TransactionManager.getTransaction());
		DBUtil db = new DBUtil();
		String id = db.getNextStringPrimaryKey("test");
db.executeInsert("insert into test(id,name) values('"+id+"','A1.testNoTXNoSyn()')");
	}
	
	public void testWithSpecialException(int type) throws Exception
	{
		System.out.println("A1.testWithSpecialException():");
		System.out.println("A1.testWithSpecialException() context tx:" + TransactionManager.getTransaction());
		DBUtil db = new DBUtil();
		String id = db.getNextStringPrimaryKey("test");
db.executeInsert("insert into test(id,name) values('"+id+"','A1.testWithSpecialException()')");
	}

}
