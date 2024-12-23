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

public class A2 implements AI {

	/**
	 * 同步调用方法执行失败
	 */
	public void testNoTXSyn() throws Exception {
		
		System.out.println("A2.testNoTXSyn():");
		System.out.println("A2.testNoTXSyn() context tx:" + TransactionManager.getTransaction());
		DBUtil db = new DBUtil();
		db.executeInsert("insert into test(name1) values('A2.testNoTXSyn()')");
	}

	/**
	 * 事务执行失败
	 */
	public void testTXNoSyn() throws Exception {
		
		System.out.println("A2.testTXNoSyn():");
		System.out.println("A2.testTXNoSyn() context tx:" + TransactionManager.getTransaction());
		DBUtil db = new DBUtil();
		db.executeInsert("insert into test(name1) values('A2.testTXNoSyn()')");
		
	}

	/**
	 * 事务执行失败，同步方法的事务成功，但是整个事务失败
	 */
	public void testTXSynFailed() throws Exception {
		System.out.println("A2.testTXSynFailed():");
		System.out.println("A2.testTXSynFailed() context tx:" + TransactionManager.getTransaction());
		DBUtil db = new DBUtil();
		db.executeInsert("insert into test(name1) values('A2.testTXSynFailed()')");
	}

	public void testTXSynSuccess() throws Exception {
		System.out.println("A2.testTXSynSuccess():");
		System.out.println("A2.testTXSynSuccess() context tx:" + TransactionManager.getTransaction());
		DBUtil db = new DBUtil();
		String id = db.getNextStringPrimaryKey("test");
db.executeInsert("insert into test(id,name) values('"+id+"','A2.testTXSynSuccess()')");
	}
	
	public void testNoTXNoSyn() throws Exception
	{
		
		System.out.println("A2.testNoTXNoSyn():");
		System.out.println("A2.testNoTXNoSyn() context tx:" + TransactionManager.getTransaction());
		DBUtil db = new DBUtil();
		String id = db.getNextStringPrimaryKey("test");
db.executeInsert("insert into test(id,name) values('"+id+"','A2.testNoTXNoSyn()')");
		
	}
	
	public void testWithSpecialException(int type) throws Exception
	{
		System.out.println("A2.testWithSpecialException(int type):");
		System.out.println("A2.testWithSpecialException(int type) context tx:" + TransactionManager.getTransaction());
		DBUtil db = new DBUtil();
		if(type == 1)
		{
			String id = db.getNextStringPrimaryKey("test");
db.executeInsert("insert into test(id,name) values('"+id+"','A2.testWithSpecialException(A2 throw tx SynException)')");		
			throw new SynException("A2 throw tx SynException .");
		}
		else
		{
			String id = db.getNextStringPrimaryKey("test");
db.executeInsert("insert into test(id,name) values('"+id+"','A2.testWithSpecialException(A2 throw tx Exception)')");		
			throw new Exception("A2 throw tx Exception .");
		}
		
	}
	
	

}
