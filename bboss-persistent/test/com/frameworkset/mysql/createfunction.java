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

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Test;

import com.frameworkset.common.poolman.PreparedDBUtil;

public class createfunction {
	static Connection con;
	static {
		try {

			con = PreparedDBUtil.getConection("mysql");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			// con = null;
		}
	}

	@Test
	public static void createfunction() {

		for (int i = 0; i < 1; i++) {
			try {
				PreparedDBUtil dbutil = new PreparedDBUtil();
				// Connection con = dbutil.getConection("mysql");
				// dbutil.executeInsert("mysql",
				// "INSERT INTO sequence VALUES (0)",con);
				dbutil.executeUpdate("mysql",
						"UPDATE sequence SET id=LAST_INSERT_ID(id+1)", con);
				dbutil.executeSelect("mysql", "SELECT LAST_INSERT_ID()", con);
				long va = dbutil.getLong(0, 0);
				// System.out.println(con);
				// con.close();
				System.out.println(va);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	@Test
	public static void getNextValue() {

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
					+ PreparedDBUtil.getNextPrimaryKey(con, "mysql",
							"cim_dbpool"));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	static class T extends Thread {
		public void run() {

			createfunction();
		}
	}

	static class Tt extends Thread {
		public void run() {

			getNextValue();
		}
	}

	public static void main(String[] args) {
//		T t = new T();
//		t.start();
//		Tt tt = new Tt();
//		tt.start();
		// String sql = "CREATE PROCEDURE sp1 (x VARCHAR(5)) "
		// +"BEGIN "
		// +" DECLARE xname VARCHAR(5) DEFAULT 'bob';"
		// +" DECLARE newname VARCHAR(5);"
		// +" DECLARE xid INT;"
		// +" SELECT xname,id INTO newname,xid "
		// +" FROM table1 WHERE xname = xname;"
		// +" SELECT newname;"
		// +" END;";
		//		
		String sql_ = "CREATE FUNCTION nextval (seq_name varchar(100)) "
				+ " RETURNS bigint(20) NOT DETERMINISTIC  "
				+ " BEGIN "
				+ " DECLARE cur_val bigint(20);  "
				+ " SELECT "
				+ " sequence_cur_value INTO cur_val  "
				+ " FROM "
				+ " sequence.sequence_data  "
				+ " WHERE "
				+ " sequence_name = seq_name ; "
				+ " IF cur_val IS NOT NULL THEN "
				+ " UPDATE "
				+ " sequence.sequence_data  "
				+ " SET "
				+ " sequence_cur_value = IF (  "
				+ " (sequence_cur_value + sequence_increment) > sequence_max_value,  "
				+ " IF (  " + " sequence_cycle = TRUE,  "
				+ " sequence_min_value,  " + " NULL " + " ),  "
				+ " sequence_cur_value + sequence_increment )  " + " WHERE "
				+ " sequence_name = seq_name ;  " + " END IF;  "
				+ " RETURN cur_val;  " + " END;";
		//		
		PreparedDBUtil dbutil = new PreparedDBUtil();

		try {
			dbutil.preparedUpdate("mysql", sql_);
			dbutil.executePrepared();

		} catch (SQLException e) {  
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
