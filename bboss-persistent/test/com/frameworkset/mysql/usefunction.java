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
package com.frameworkset.mysql;

import java.sql.SQLException;

import org.junit.Test;

import com.frameworkset.common.poolman.PreparedDBUtil;
import com.frameworkset.orm.transaction.TransactionManager;

public class usefunction {
	
	public static void main(String[] args)
	{
		t t = new t();
		t.start();
		t = new t();
		t.start();
	}
	
	public static void test()
	{
		TransactionManager tm = new TransactionManager();
		
		PreparedDBUtil dbutil = new PreparedDBUtil();
		
		try {
			tm.begin();
			for (int i = 0; i < 1000; i++) {
				try {
					String sql_ = "select nextval('seq_dbpool')";
					dbutil.preparedSelect("mysql", sql_);
					dbutil.executePrepared();
					System.out.println(dbutil.getLong(0, 0));

				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			tm.commit();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	
	@Test
	public  void getNextValue() {

		// for(int i = 0; i < 1; i ++)
		// {
		// try {
		// PreparedDBUtil dbutil = new PreparedDBUtil();
		//				
		// // dbutil.executeInsert("mysql",
		// "INSERT INTO seq_dbpool VALUES (0)",con);
		// dbutil.executeUpdate("mysql",
		// "UPDATE seq_dbpool SET id=LAST_INSERT_ID(id+1)",con);
		// dbutil.executeSelect("mysql","SELECT LAST_INSERT_ID()",con);
		// long va = dbutil.getLong(0, 0);
		// // con.close();
		// System.out.println("seq_dbpool:"+va);
		// } catch (SQLException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		// }
		try {
			
			System.out.println("seq_dbpool:"
					+ PreparedDBUtil.getNextPrimaryKey("mysql",
							"cim_dbpool"));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	static class t extends Thread
	{
		public void run()
		{
			test();
		}
	}
}
