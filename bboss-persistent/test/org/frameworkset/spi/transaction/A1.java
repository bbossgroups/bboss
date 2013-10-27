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

import java.sql.SQLException;

import com.frameworkset.common.poolman.DBUtil;

public class A1 implements AI{

	public void testTXInvoke(String msg) throws Exception {
		System.out.println("A1:" + msg);
		
		
		DBUtil db = new DBUtil();
		System.out.println("db.getNumIdle():" +db.getNumIdle());
		System.out.println("db.getNumActive():" +db.getNumActive());
		String id = db.getNextStringPrimaryKey("test");
		db.executeInsert("insert into test(id,name) values('"+id+"','testTXInvoke(" + msg +")')");
		db.executeInsert("insert into test1(A) values('testTXInvoke(" + msg +")')");
		System.out.println("1db.getNumActive():" +db.getNumActive());
		System.out.println("1db.getNumIdle():" +db.getNumIdle());
		
	}
	
	public void testTXInvoke() throws SQLException {
		System.out.println("A1.testTXInvoke():no param");
		DBUtil db = new DBUtil();
		System.out.println("db.getNumIdle():" +db.getNumIdle());
		System.out.println("db.getNumActive():" +db.getNumActive());
		db.executeDelete("delete from test");
		String id = "aa";
		db.executeInsert("insert into test(id,name) values('"+id+"','testTXInvoke()')");
		db.executeInsert("insert into test1(A) values('testTXInvoke')");
		System.out.println("1db.getNumActive():" +db.getNumActive());
		System.out.println("1db.getNumIdle():" +db.getNumIdle());
		
		
	}
	
	public void testNoTXInvoke()
	{
		System.out.println("A1:NoTXInvoke");
		DBUtil db = new DBUtil();
		try {
			String id = db.getNextStringPrimaryKey("test");
			db.executeInsert("insert into test(id,name) values('"+id+"','testTXInvoke()')");
			db.executeInsert("insert into test1(A) values('testTXInvoke()')");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public String testTXInvokeWithReturn() {
		System.out.println("call A1.testTXInvokeWithReturn()");
		DBUtil db = new DBUtil();
		try {
			String id = db.getNextStringPrimaryKey("test");
			db.executeInsert("insert into test(id,name) values('"+id+"','testTXInvokeWithReturn()')");
			db.executeInsert("insert into test1(A) values('testTXInvokeWithReturn()')");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "return is A1";
	}
	
	/**
	 * 只要抛出异常，事务就回滚
	 */
	public String testTXInvokeWithException() throws Exception {
		System.out.println("call A1.testTXInvokeWithException()");
		DBUtil db = new DBUtil();
		
		String id = db.getNextStringPrimaryKey("test");
		db.executeInsert("insert into test(id,name) values('"+id+"','testTXInvokeWithException()')");
		if(true)
			throw new Exception1("A1 throw a exception");
		return "A1 exception find.";
	}

	public void testSameName() throws SQLException {
		System.out.println("call A1.testSameName()");
		DBUtil db = new DBUtil();
		String id = db.getNextStringPrimaryKey("test");
		
		db.executeInsert("insert into test(id,name) values('"+id+"','testSameName()')");
		
		
	}

	public void testSameName(String msg) throws SQLException {
		System.out.println("call A1.testSameName("+msg+")");
		DBUtil db = new DBUtil();
		String id = db.getNextStringPrimaryKey("test");
		db.executeInsert("insert into test(id,name) values('"+id+"','testSameName("+msg+")')");
		
	}

	public void testSameName1() throws SQLException {
		System.out.println("call A1.testSameName1()");
		
		DBUtil db = new DBUtil();
		
		String id = db.getNextStringPrimaryKey("test");
		db.executeInsert("insert into test(id,name) values('"+id+"','testSameName1()')");
		
	}

	public void testSameName1(String msg) throws SQLException {
		System.out.println("call A1.testSameName1(String msg):" + msg);
		DBUtil db = new DBUtil();
		
		String id = db.getNextStringPrimaryKey("test");
		db.executeInsert("insert into test(id,name) values('"+id+"','testSameName1("+msg+")')");
		
	}

	public int testInt(int i) {
		System.out.println("call A1.testInt(int i)：" + i);
		return i;
		
	}
	
	public int testIntNoTX(int i) {
		System.out.println("call A1.testIntNoTX(int i)：" + i);
		return i;		
	}
	
	/**
	 * 混合异常测试，即包含实例异常，也包含子类和实例异常
	 * 所有的异常都将导致事务回滚
	 */
	public void testTXWithSpecialExceptions(String type) throws Exception
	{
		
		DBUtil db = new DBUtil();
		String id = db.getNextStringPrimaryKey("test");
		db.executeInsert("insert into test(id,name) values('"+id+"','testTXWithSpecialExceptions("+type+")')");	
		//事务回滚
		if(type.equals("IMPLEMENTS"))
		{
			throw new RollbackInstanceofException("IMPLEMENTS RollbackInstanceofException");
		}
		
		//事务回滚
		if(type.equals("INSTANCEOF"))
		{
			throw new SubRollbackInstanceofException("INSTANCEOF RollbackInstanceofException");
		}
		
		//事务回滚
		if(type.equals("exception1"))
		{
			throw new Exception1("IMPLEMENTS exception1");
		}
		/**
		 * 事务不会回滚，没有进行配置
		 */
		if(type.equals("notxexception"))
		{
			throw new Exception3("notxexception Exception3");
		}
		
	}
	
	/**
	 * 只要是特定实例的异常就会回滚
	 * @param type
	 * @throws Exception
	 */
	public void testTXWithInstanceofExceptions(String type) throws Exception
	{
		
		DBUtil db = new DBUtil();
		String id = db.getNextStringPrimaryKey("test");
		db.executeInsert("insert into test(id,name) values('"+id+"','testTXWithInstanceofExceptions(" + type + ")')");
		
		//事务回滚
		if(type.equals("IMPLEMENTS"))
		{
			throw new RollbackInstanceofException("IMPLEMENTS RollbackInstanceofException");
		}
		
		//事务回滚
		if(type.equals("INSTANCEOF"))
		{
			throw new SubRollbackInstanceofException("INSTANCEOF RollbackInstanceofException");
		}
		/**
		 * 事务不会回滚，提交
		 */
		if(type.equals("notxexception"))
		{
			throw new Exception3("notxexception Exception3");
		}
	}
	
	/**
	 * 只有异常本身的实例异常才触发事务的回滚
	 * @param type
	 * @throws Exception
	 */
	public void testTXWithImplementsofExceptions(String type) throws Exception
	{
		
		DBUtil db = new DBUtil();
		String id = db.getNextStringPrimaryKey("test");
		db.executeInsert("insert into test(id,name) values('"+id+"','testTXWithImplementsofExceptions(" + type + ")')");
		//事务回滚
		if(type.equals("IMPLEMENTS"))
		{
			throw new RollbackInstanceofException("IMPLEMENTS RollbackInstanceofException");
		}
		
		//事务不会回滚，提交
		if(type.equals("INSTANCEOF"))
		{
			throw new SubRollbackInstanceofException("INSTANCEOF RollbackInstanceofException");
		}
		/**
		 * 事务不会回滚，提交
		 */
		if(type.equals("notxexception"))
		{
			throw new Exception3("notxexception Exception3");
		}
	}
	
	public void testPatternTX1(String type) throws Exception
	{
		DBUtil db = new DBUtil();
		String id = db.getNextStringPrimaryKey("test");
		db.executeInsert("insert into test(id,name) values('"+id+"','testPatternTX1(" + type + ")')");
		//事务回滚
		if(type.equals("IMPLEMENTS"))
		{
			throw new RollbackInstanceofException("IMPLEMENTS RollbackInstanceofException");
		}
		
		//事务不会回滚，提交
		if(type.equals("INSTANCEOF"))
		{
			throw new SubRollbackInstanceofException("INSTANCEOF RollbackInstanceofException");
		}
		/**
		 * 事务不会回滚，提交
		 */
		if(type.equals("notxexception"))
		{
			throw new Exception3("notxexception Exception3");
		}
	}
	
	public void testPatternTX2(String type) throws Exception
	{
		DBUtil db = new DBUtil();
		String id = db.getNextStringPrimaryKey("test");
		db.executeInsert("insert into test(id,name) values('"+id+"','testPatternTX2(" + type + ")')");
		//事务回滚
		if(type.equals("IMPLEMENTS"))
		{
			throw new RollbackInstanceofException("IMPLEMENTS RollbackInstanceofException");
		}
		
		//事务不会回滚，提交
		if(type.equals("INSTANCEOF"))
		{
			throw new SubRollbackInstanceofException("INSTANCEOF RollbackInstanceofException");
		}
		/**
		 * 事务不会回滚，提交
		 */
		if(type.equals("notxexception"))
		{
			throw new Exception3("notxexception Exception3");
		}
	}
	
	public void testPatternTX3(String type) throws Exception
	{
		DBUtil db = new DBUtil();
		String id = db.getNextStringPrimaryKey("test");
		db.executeInsert("insert into test(id,name) values('"+id+"','testPatternTX3(" + type + ")')");
		//事务回滚
		if(type.equals("IMPLEMENTS"))
		{
			throw new RollbackInstanceofException("IMPLEMENTS RollbackInstanceofException");
		}
		
		//事务不会回滚，提交
		if(type.equals("INSTANCEOF"))
		{
			throw new SubRollbackInstanceofException("INSTANCEOF RollbackInstanceofException");
		}
		/**
		 * 事务不会回滚，提交
		 */
		if(type.equals("notxexception"))
		{
			throw new Exception3("notxexception Exception3");
		}
	}
	public void testPatternTX4(String type) throws Exception
	{
		DBUtil db = new DBUtil();
		String id = db.getNextStringPrimaryKey("test");
		db.executeInsert("insert into test(id,name) values('"+id+"','testPatternTX4(" + type + ")')");
		//事务回滚
		if(type.equals("IMPLEMENTS"))
		{
			throw new RollbackInstanceofException("IMPLEMENTS RollbackInstanceofException");
		}
		
		//事务不会回滚，提交
		if(type.equals("INSTANCEOF"))
		{
			throw new SubRollbackInstanceofException("INSTANCEOF RollbackInstanceofException");
		}
		/**
		 * 事务不会回滚，提交
		 */
		if(type.equals("notxexception"))
		{
			throw new Exception3("notxexception Exception3");
		}
	}
	
	/**
	 * 针对系统级别的异常，事务自动回滚
	 * 本方法声明了事务回滚异常
	 * <method name="testSystemException">	
				<rollbackexceptions>
					<exception class="org.frameworkset.spi.transaction.RollbackInstanceofException" 
					type="IMPLEMENTS"/>
				</rollbackexceptions>
			</method>
			方法中抛出了系统级别的空指针异常，将导致事务回滚
	 */
	public void testSystemException() throws Exception
	{
		DBUtil db = new DBUtil();
		String id = db.getNextStringPrimaryKey("test");
		db.executeInsert("insert into test(id,name) values('"+id+"','testSystemException()')");
//		throw new java.lang.NullPointerException("空指针异常。事务回滚");
		
	}

}
