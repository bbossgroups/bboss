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
package com.frameworkset.common.poolman;

import javax.transaction.RollbackException;

import com.frameworkset.orm.transaction.TransactionManager;

/**
 * 
 * <p>Title: CurrentTest.java</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2007</p>
 * @Date 2010-8-2 下午12:02:44
 * @author biaoping.yin
 * @version 1.0
 */
public class CurrentTest {
	public static void main(String[] args) throws InterruptedException {

//		for(int j = 0; j < 300; j ++)
//		{
//			CurrentTest i = new CurrentTest();
//			i.test(false);
//		}
		
		for(int j = 0; j < 300; j ++)
		{
			CurrentTest i = new CurrentTest();
			i.test(true);
		}

	}
	
	public void test(boolean usetx)
	{
		if(!usetx)
		{
			Task t = new Task();
			t.start();
		}
		else
		{
			TXTask t = new TXTask();
			t.start();
		}
			
	}
	static class Task extends Thread
	{

		/* (non-Javadoc)
		 * @see java.lang.Thread#run()
		 */
		@Override
		public void run() {
			DBUtil db = new DBUtil();
		
			try {

				db.executeInsert("insert into TEST_CURRENT(id,name) values(10,'test')");
				
				db.executeInsert("insert into TEST_CURRENT(id,name) values(10,'test')");
				
				db.executeInsert("insert into TEST_CURRENT(id,name) values(10,'test')");
				
				db.executeInsert("insert into TEST_CURRENT(id,name) values(10,'test')");

			} catch (Exception e) {

				e.printStackTrace();
			}
			
		}
		
	}
	
	static class TXTask extends Thread
	{

		/* (non-Javadoc)
		 * @see java.lang.Thread#run()
		 */
		@Override
		public void run() {
			DBUtil db = new DBUtil();
			TransactionManager tm = new TransactionManager();
			try {
				tm.begin(tm.RW_TRANSACTION);
				db.executeInsert("insert into TEST_CURRENT(id,name) values(10,'test')");
				
				db.executeInsert("insert into TEST_CURRENT(id,name) values(10,'test')");
				
				db.executeInsert("insert into TEST_CURRENT(id,name) values(10,'test')");
				
				db.executeInsert("insert into TEST_CURRENT(id,name) values(10,'test')");
				tm.commit();
			} catch (Exception e) {
				try {
					tm.rollback();
				} catch (RollbackException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				e.printStackTrace();
			}
			
		}
		
	}
}
