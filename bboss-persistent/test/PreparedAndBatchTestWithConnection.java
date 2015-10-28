import java.io.File;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;

import javax.transaction.RollbackException;

import com.frameworkset.common.poolman.DBUtil;
import com.frameworkset.common.poolman.PreparedDBUtil;
import com.frameworkset.orm.transaction.TransactionException;
import com.frameworkset.orm.transaction.TransactionManager;

public class PreparedAndBatchTestWithConnection {
	/**
	 * 执行预编译更新操作,释放数据库资源实例
	 * 
	 * @throws SQLException
	 */
	public static void updateTest() throws SQLException {
		Connection con = null;
		PreparedDBUtil db = null;
		try {
			con = PreparedDBUtil.getConection();
			for (int i = 0; i < 1; i++) {
				db = new PreparedDBUtil();
				

					db
							.preparedUpdate(
									"update TD_REG_BANK_ACC_bak set create_acc_time=?,starttime=?,endtime=? where id=951");
					Date today = new Date(new java.util.Date().getTime());
					db.setTimestamp(1, new Timestamp(new java.util.Date()
							.getTime()));
					db.setTimestamp(2, new Timestamp(new java.util.Date()
							.getTime()));
					// if(true && i < 50)
					// {
					// //此处抛出业务异常，必须在相应的异常处理块或者finally块中调用db.resetPrepare()方法来释放数据库资源
					// //如果没有释放则会导致系统运行一段时候后抛出以下异常：
					// //Cannot get a connection, pool error Timeout waiting for
					// idle object
					// throw new Exception("e");
					// }

					db.setTimestamp(3, new Timestamp(new java.util.Date()
							.getTime()));
					db.executePrepared(							con);
					// db.addBatch(sql);
				

			}
		} finally {
			if(db != null)
				db.resetPrepare();
			if (con != null)
				con.close();
		}
	}

	/**
	 * 包含在事务环境中的预编译操作，执行抛出异常后
	 * 
	 * @throws SQLException
	 */
	public static void updateWithTXtest()  {
		Connection con = null;
		TransactionManager tm = new TransactionManager();
		PreparedDBUtil db = null;
		
		try {
			con = PreparedDBUtil.getConection();
			tm.begin();
			
			for (int i = 0; i < 1; i++) {
				

				db = new PreparedDBUtil();
				
					
					db
							.preparedUpdate(
									"update TD_REG_BANK_ACC_bak set create_acc_time=?,starttime=?,endtime=? where id=951");
					Date today = new Date(new java.util.Date().getTime());
					db.setTimestamp(1, new Timestamp(new java.util.Date()
							.getTime()));
					db.setTimestamp(2, new Timestamp(new java.util.Date()
							.getTime()));
					// if(true && i < 50)
					// {
					// throw new Exception("e");
					// }
					db.setTimestamp(3, new Timestamp(new java.util.Date()
							.getTime()));
					db.executePrepared(con);
					

				
			}
			tm.commit();// 事务提交后自动释放系统资源
		} catch (RollbackException e) {
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
		} finally {
			if(db != null)
				db.resetPrepare();
			if(con != null)
				try {
					con.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
	}

	public static void inserttest() throws SQLException {
		Connection con = PreparedDBUtil.getConection();
		PreparedDBUtil db = null;
		try {
			for (int i = 0; i < 1; i++) {
				db = new PreparedDBUtil();
				
					db
							.preparedInsert(
									"insert into td_reg_bank_acc_bak (create_acc_time,starttime,endtime) values(?,?,?)");
					Date today = new Date(new java.util.Date().getTime());
//					db.setInt(1, 1000 + i);

					db.setDate(1, new java.util.Date());
					db.setDate(2, new java.util.Date());
					db.setDate(3, new java.util.Date());

					// if(true && i < 50)
					// {
					// throw new Exception("e");
					// }

					db.executePrepared(con);

				
			}
		} finally {
			db.resetPrepare();
			con.close();
		}

	}

	public static void insertWithTXtest() throws SQLException {
		Connection con = PreparedDBUtil.getConection();
		PreparedDBUtil db = null;
		TransactionManager tm = new TransactionManager();
		try {
			tm.begin();
			for (int i = 0; i < 1; i++) {

				db = new PreparedDBUtil();
				
				
					
					db
							.preparedInsert(
									"insert into td_reg_bank_acc_bak (id,create_acc_time,starttime,endtime) values(?,?,?,?)");
					Date today = new Date(new java.util.Date().getTime());
					db.setInt(1, 1000 + i);

					db.setDate(2, new java.util.Date());
					db.setDate(3, new java.util.Date());
					db.setDate(4, new java.util.Date());

					//					
					// if(true && i < 50)
					// {
					// throw new Exception("e");
					// }

					db.executePrepared(con);
					

				

			}
			tm.commit();
		} catch (TransactionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			try {
				tm.rollback();
			} catch (RollbackException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} catch (RollbackException e) {
			try {
				tm.rollback();
			} catch (RollbackException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			e.printStackTrace();
		} finally {
			con.close();
		}
	}

	public static void deleteWithTXtest() throws SQLException {
		Connection con = PreparedDBUtil.getConection();
		PreparedDBUtil db = null;
		TransactionManager tm = new TransactionManager();
		
		try {
			tm.begin();
			for (int i = 0; i < 2; i++) {
				 db = new PreparedDBUtil();
				//TransactionManager tm = new TransactionManager();
				

					
					db.preparedDelete(
							"delete from TD_REG_BANK_ACC_bak where id=?");
					db.setInt(1, 1000 + i);
					// if(true && i < 50)
					// {
					// throw new Exception("e");
					// }

					db.executePrepared(con);
				

			}
			tm.commit();

		} catch (Exception e) {
			System.out.println(e.getMessage());
			try {

				tm.rollback();
			} catch (RollbackException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} finally {
			con.close();
		}
	}

	public static void deletetest() throws SQLException {
		Connection con = PreparedDBUtil.getConection();
		try {
			for (int i = 0; i < 2; i++) {
				PreparedDBUtil db = new PreparedDBUtil();
				try {

					db
							.preparedDelete("delete from TD_REG_BANK_ACC_bak where id=?");
					db.setInt(1, 1000 + i);
					// if(true && i < 50)
					// {
					// throw new Exception("e");
					// }

					db.executePrepared();

				} catch (Exception e) {
					System.out.println(e.getMessage());
				} finally {

					db.resetPrepare();
				}

			}
		} finally {
			con.close();
		}
	}
	
	public static void deletealltest() throws SQLException {
		Connection con = PreparedDBUtil.getConection();
		try {
			
				PreparedDBUtil db = new PreparedDBUtil();
				try {

					db
							.preparedDelete("delete from TD_REG_BANK_ACC_bak");
					
					// if(true && i < 50)
					// {
					// throw new Exception("e");
					// }

					db.executePrepared(con);
					

				} catch (Exception e) {
					System.out.println(e.getMessage());
				} finally {

					db.resetPrepare();
				}

			
		} finally {
			con.close();
		}
	}

	public static void selecttest() throws SQLException {
		Connection con = PreparedDBUtil.getConection();
		PreparedDBUtil db = null;
		try {
			for (int i = 0; i < 1; i++) {
				 db = new PreparedDBUtil();
				

					db
							.preparedSelect(
									"select create_acc_time,starttime,endtime from TD_REG_BANK_ACC_bak where id=?");
					db.setInt(1, 1000 + i);
					// if(true && i < 50)
					// {
					// throw new Exception("e");
					// }
					db.executePrepared(con);
					// db.executeSelect("select
					// create_acc_time,starttime,endtime from
					// TD_REG_BANK_ACC_bak where id=951");
					System.out.println("create_acc_time=" + db.getDate(0, 0));
					System.out.println("starttime=" + db.getDate(0, 1));
					System.out.println("endtime=" + db.getDate(0, 2));
				

			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			// db.resetPreparedResource();
			db.resetPrepare();
		
			con.close();
		}
	}

	public static void selectpaginetest() throws SQLException {
		long start = System.currentTimeMillis();
		Connection con = PreparedDBUtil.getConection();
//		TransactionManager tm = new TransactionManager();
		PreparedDBUtil db = new PreparedDBUtil();
		try {
//			tm.begin();
			for (int i = 0; i < 1; i++) {
				
				

					db
							.preparedSelect(
									"select create_acc_time,starttime,endtime from TD_REG_BANK_ACC_bak where id between ? and ?",
									0, 100);
					db.setInt(1, 1);
					db.setInt(2, 10000);
					// if(true && i < 50)
					// {
					// throw new Exception("e");
					// }
					db.executePrepared(con);
			

				// db.executePrepared();
				System.out.println("total size:" + db.getLongTotalSize());
				System.out.println("page size:" + db.size());

			}
			for(int i =0 ; i < 10000; i ++)
			{
				System.out.println("执行业务逻辑10000次：第" +i +"次" );
			}
//			tm.commit();
		} catch (Exception e) {
			try {
//				db
				db.resetPrepare();
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			e.printStackTrace();
		} finally {
			con.close();
		}
		long end = System.currentTimeMillis();
		System.out.println("执行selectpaginetest方法耗时：" + (end - start) / 1000
				+ "秒");

	}

	public static void batchWithTXTest() throws Exception {
		long start = System.currentTimeMillis();
		Connection con = null;
		PreparedDBUtil db = null;
		 TransactionManager tm = new TransactionManager();
		try {
			con = PreparedDBUtil.getConection();
			tm.begin();
			
			for (int i = 0; i < 1; i++) {
				
				db = new PreparedDBUtil();
				
					
					db.setBatchConnection(con);
					db.addBatch("delete from TD_REG_BANK_ACC_bak where id=951");
					db.addBatch("delete from TD_REG_BANK_ACC_bak where id=951");
					// if(i < 30)
					// throw new Exception("batch");
					db.addBatch("delete from TD_REG_BANK_ACC_bak where id=951");
					db.addBatch("delete from TD_REG_BANK_ACC_bak where id=951");
					db.executeBatch();
					
				
			}
			tm.commit();
			success ++;
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			tm.rollback();
			failed ++;
		}

		finally {
			total ++;
			db.resetBatch();
			try {
				if(con != null)
					con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		long end = System.currentTimeMillis();
		System.out.println("执行batchWithTXTest方法耗时：" + (end - start) / 1000
				+ "秒");
	}

	public static void batchTest() throws Exception {

		Connection con = null;
		PreparedDBUtil db = null;
		try {
			con = PreparedDBUtil.getConection();
			
			for (int i = 0; i < 60; i++) {
				db = new PreparedDBUtil();
				
					db.setBatchConnection(con);
					db.addBatch("delete from TD_REG_BANK_ACC_bak where id=951");
					db.addBatch("delete from TD_REG_BANK_ACC_bak where id=951");
					// if(i < 55)
					// //此处抛出业务异常，需要在相应的异常处理块或finally块中调用db.resetBatch()方法释放数据库资源
					// throw new Exception("batch");
					db.addBatch("delete from TD_REG_BANK_ACC_bak where id=951");
					db.addBatch("delete from TD_REG_BANK_ACC_bak where id=951");
					db.executeBatch();
				
			}
		

	} catch (Exception e) {
		e.printStackTrace();
		db.resetBatch();// 在相应的异常处理块或finally块中调用db.resetBatch()方法释放数据库资源
	} finally {
		// finally块中调用db.resetBatch()方法释放数据库资源，确保资源能够正常回收
		if(db != null)
			db.resetBatch();
	

			try {
				con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public static void test() throws SQLException {
		System.out
				.println("PreparedAndBatchTestWithConnection.deletetest() before;");
		PreparedAndBatchTestWithConnection.deletetest();
		System.out
				.println("PreparedAndBatchTestWithConnection.deletetest() end;");
		System.out
				.println("PreparedAndBatchTestWithConnection.inserttest() before;");
		PreparedAndBatchTestWithConnection.inserttest();
		System.out
				.println("PreparedAndBatchTestWithConnection.inserttest() end;");
		System.out
				.println("PreparedAndBatchTestWithConnection.selecttest() before;");
		PreparedAndBatchTestWithConnection.selecttest();
		System.out
				.println("PreparedAndBatchTestWithConnection.selecttest() end;");
		System.out
				.println("PreparedAndBatchTestWithConnection.updateTest() before;");
		PreparedAndBatchTestWithConnection.updateTest();
		System.out
				.println("PreparedAndBatchTestWithConnection.updateTest() end;");
		System.out
				.println("after update PreparedAndBatchTestWithConnection.selecttest() before;");
		PreparedAndBatchTestWithConnection.selecttest();
		System.out
				.println("after update PreparedAndBatchTestWithConnection.selecttest() end;");
		System.out
				.println("2 PreparedAndBatchTestWithConnection.deletetest() before;");
		PreparedAndBatchTestWithConnection.deletetest();
		System.out
				.println("2 PreparedAndBatchTestWithConnection.deletetest() end;");
	}

	public static void testTX() throws SQLException {
		System.out
				.println("PreparedAndBatchTestWithConnection.deletetest() before;");
		PreparedAndBatchTestWithConnection.deleteWithTXtest();
		System.out
				.println("PreparedAndBatchTestWithConnection.deletetest() end;");
		System.out
				.println("PreparedAndBatchTestWithConnection.inserttest() before;");
		PreparedAndBatchTestWithConnection.insertWithTXtest();
		System.out
				.println("PreparedAndBatchTestWithConnection.inserttest() end;");
		System.out
				.println("PreparedAndBatchTestWithConnection.selecttest() before;");
		PreparedAndBatchTestWithConnection.selecttest();
		System.out
				.println("PreparedAndBatchTestWithConnection.selecttest() end;");
		System.out
				.println("PreparedAndBatchTestWithConnection.updateTest() before;");
		PreparedAndBatchTestWithConnection.updateWithTXtest();
		System.out
				.println("PreparedAndBatchTestWithConnection.updateTest() end;");
		System.out
				.println("after update PreparedAndBatchTestWithConnection.selecttest() before;");
		PreparedAndBatchTestWithConnection.selecttest();
		System.out
				.println("after update PreparedAndBatchTestWithConnection.selecttest() end;");
		System.out
				.println("2 PreparedAndBatchTestWithConnection.deletetest() before;");
		PreparedAndBatchTestWithConnection.deleteWithTXtest();
		System.out
				.println("2 PreparedAndBatchTestWithConnection.deletetest() end;");
	}

	public static void insertBoubleClob() {

		Connection con = null;
		TransactionManager tm = new TransactionManager();
		PreparedDBUtil db = new PreparedDBUtil();
		try {
			
			con = PreparedDBUtil.getConection();
			
			
				tm.begin();
				db.preparedInsert(
								"insert into TD_REG_BANK_ACC_bak (clob1,clob2) values(?,?)");
				db.setClob(1, "aa", "clob1");
				db.setClob(2, "bb", "clob2");
				//db.setPrimaryKey(3, "10000", "id");
				db.executePrepared(con);
				tm.commit();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			
	
		e.printStackTrace();
		try {
			tm.rollback();
		} catch (RollbackException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		// db.resetBatch();
	}
		finally {
			db.resetPrepare();
			try {
				con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public static void updateBoubleClob() {

		Connection con = null;
		try {
			DBUtil dbUtil = new DBUtil();
			con = PreparedDBUtil.getConection();
			
			PreparedDBUtil db = new PreparedDBUtil();
			try {
				dbUtil.executeSelect("select id from TD_REG_BANK_ACC_bak", con);

				
				for (int i = 0; i < dbUtil.size(); i++) {
					db
							.preparedUpdate(
									"update TD_REG_BANK_ACC_bak set clob1 = ?,clob2 = ? where id=?");
					db.setClob(1, "aaa1", "clob1");
					db.setClob(2, "bbb2", "clob2");
					// db.setNull(1, java.sql.Types.CLOB);
					// db.setNull(2, java.sql.Types.CLOB);

					db.setPrimaryKey(3, dbUtil.getInt(i, "id"), "id");

					db.executePrepared(con);
				}
				
			} catch (Exception e) {
				e.printStackTrace();
				
				// db.resetBatch();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		finally {

			try {
				con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public static void updateBoubleClobWithOuterConnection() {

		Connection con = null;
		try {
			DBUtil dbUtil = new DBUtil();
			con = PreparedDBUtil.getConection();
			
			PreparedDBUtil db = new PreparedDBUtil();
			
				dbUtil.executeSelect("select id from TD_REG_BANK_ACC_bak", con);
				
				for (int i = 0; i < dbUtil.size(); i++) {
					db
							.preparedUpdate(
									"update TD_REG_BANK_ACC_bak set clob1 = ?,clob2 = ? where id=?");
					db.setClob(1, "aaa1", "clob1");
					db.setClob(2, "bbb2", "clob2");
					// db.setNull(1, java.sql.Types.CLOB);
					// db.setNull(2, java.sql.Types.CLOB);

					db.setPrimaryKey(3, dbUtil.getInt(i, "id"), "id");

					db.executePrepared(con);
				}
				
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		finally {

			try {
				con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public static void updateBoubleBlob() {

		Connection con = null;

		try {
			con = PreparedDBUtil.getConection();
			DBUtil dbUtil = new DBUtil();
			
			PreparedDBUtil db = new PreparedDBUtil();
			
			dbUtil.executeSelect("select id from TD_REG_BANK_ACC_bak", con);
			System.out.println("dbUtil.size()=" + dbUtil.size());
			for (int i = 0; i < dbUtil.size(); i++) {
				db
						.preparedUpdate(
								"update TD_REG_BANK_ACC_bak set blob1 = ? where id=?");
				db.setBlob(1, "7890".getBytes(), "blob1");
				// db.setClob(2, "bbb2", "clob2");
				// db.setNull(1, java.sql.Types.CLOB);
				// db.setNull(2, java.sql.Types.CLOB);

				db.setPrimaryKey(2, dbUtil.getInt(i, "id"), "id");

				db.executePrepared(con);
			}
				// dbUtil.execute("commit");
				// tm.rollback();
				
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		finally {

			try {
				con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public static void updateBoubleBlobFile() {

		Connection con = null;
		PreparedDBUtil db = new PreparedDBUtil();
		try {
			con = PreparedDBUtil.getConection();

			DBUtil dbUtil = new DBUtil();
			
			
			
				dbUtil.executeSelect("select id from TD_REG_BANK_ACC_bak", con);

				
				for (int i = 0; i < dbUtil.size(); i++) {
					db
							.preparedUpdate(
									"update TD_REG_BANK_ACC_bak set blob1 = ? where id=?");
					db
							.setBlob(
									1,
									new File(
											"D:\\workspace\\cms20080416\\creatorcms\\WEB-INF\\lib\\frameworkset-pool.jar"),
									"blob1");
					// db.setClob(2, "bbb2", "clob2");
					// db.setNull(1, java.sql.Types.CLOB);
					// db.setNull(2, java.sql.Types.CLOB);

					db.setPrimaryKey(2, dbUtil.getInt(i, "id"), "id");

					db.executePrepared(con);
				}
				// dbUtil.execute("commit");
				// tm.rollback();
				
			
		} catch (Exception e) {
			db.resetPrepare();
			e.printStackTrace();
		}

		finally {

			try {
				con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public static void insertBlobFile() {

		// DBUtil dbUtil = new DBUtil();
		Connection con = null;

		PreparedDBUtil db = new PreparedDBUtil();
		try {

			con = PreparedDBUtil.getConection();
			
		
				// dbUtil.executeSelect("select id from TD_REG_BANK_ACC_bak");

				
				// for(int i = 0; i < dbUtil.size();i ++)
				// {
				db
						.preparedInsert(
								"insert into TD_REG_BANK_ACC_bak(blob1) values(?)");
				db
						.setBlob(
								1,
								new File(
										"D:\\workspace\\cms20080416\\creatorcms\\WEB-INF\\lib\\frameworkset-pool.jar"),
								"blob1");
				// db.setClob(2, "bbb2", "clob2");
				// db.setNull(1, java.sql.Types.CLOB);
				// db.setNull(2, java.sql.Types.CLOB);

				// db.setPrimaryKey(2, dbUtil.getInt(i, "id"), "id");

				db.executePrepared(con);
				// }
				// dbUtil.execute("commit");
				// tm.rollback();
				
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		finally {
			db.resetPrepare();
			try {
				con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public static void updateBoubleBlobWithCommon() {

		Connection con = null;
		
		PreparedDBUtil db = new PreparedDBUtil();
		try {

			con = PreparedDBUtil.getConection();
			DBUtil dbUtil = new DBUtil();

			
				dbUtil.executeSelect("select id from TD_REG_BANK_ACC_bak", con);

				
				for (int i = 0; i < dbUtil.size(); i++) {
					db
							.preparedUpdate(
									"update TD_REG_BANK_ACC_bak set blob1 = ?,BANK_ACC=? where id=?");
					db.setBlob(1, "addddvvvbbbwwww1".getBytes(), "blob1");
					db.setString(2, "etst");
					// db.setClob(2, "bbb2", "clob2");
					// db.setNull(1, java.sql.Types.CLOB);
					// db.setNull(2, java.sql.Types.CLOB);

					db.setPrimaryKey(3, dbUtil.getInt(i, "id"), "id");

					db.executePrepared(con);
				}
				// dbUtil.execute("commit");
				// tm.rollback();
				// tm.commit();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		finally {

			db.resetPrepare();

			try {
				con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public static void updateWithCommon() {

		Connection con = null;
		PreparedDBUtil db = new PreparedDBUtil();
		
		try {
			con = PreparedDBUtil.getConection();
			DBUtil dbUtil = new DBUtil();
			
			
				dbUtil.executeSelect("select id from TD_REG_BANK_ACC_bak", con);

				
				for (int i = 0; i < dbUtil.size(); i++) {
					db = new PreparedDBUtil();
					db
							.preparedUpdate(
									"update TD_REG_BANK_ACC_bak set BANK_ACC=? where id=?");
					// db.setBlob(1, "addddvvvbbbwwww1".getBytes(), "blob1");
					db.setString(1, "qqqqqq");
					// db.setClob(2, "bbb2", "clob2");
					// db.setNull(1, java.sql.Types.CLOB);
					// db.setNull(2, java.sql.Types.CLOB);

					db.setPrimaryKey(2, dbUtil.getInt(i, "id"), "id");

					db.executePrepared(con);
				}
				// dbUtil.execute("commit");
				// tm.rollback();
				
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			db.resetPrepare();
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public static void getTableMetas() {
		// DBUtil.getTableMetaDatas();

		DBUtil.getTableMetaDatas();
	}

	public static void insertAutoID() {

		Connection con = null;
		PreparedDBUtil db = new PreparedDBUtil();
		try {
			con = PreparedDBUtil.getConection();
			
			
			
				db
						.preparedInsert(
								"insert into TD_REG_BANK_ACC_bak (clob1,clob2) values(?,?)");
				db.setClob(1, "aa", "clob1");
				db.setClob(2, "bb", "clob2");
				System.out.println(db.executePrepared(con));
				
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			db.resetPrepare();
			e.printStackTrace();
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public static void insertAutoIDRollback() {

		Connection con = null;
		PreparedDBUtil db = new PreparedDBUtil();
		try {
			con = PreparedDBUtil.getConection();
			
			
			
				db.preparedInsert(
						"insert into TD_REG_BANK_ACC_bak (BANK_ACC) values(?)");
				// db.setClob(1, "aa", "clob1");
				// db.setClob(2, "bb", "clob2");
				db.setString(1, "aa");
				System.out.println(db.executePrepared(con));
				// tm.rollback();
				
				// db.resetBatch();
			
		} catch (SQLException e) {
			db.resetPrepare();
			e.printStackTrace();
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public static void columnMetaDataTest() {
		// java.sql.Types.FLOAT;
		Connection con = null;
		try {
			con = PreparedDBUtil.getConection();
			System.out.println(DBUtil.getColumnMetaData("TABLEINFO",
					"LEVY_RATE", con));
			System.out.println(DBUtil.getColumnMetaData("TABLEINFO",
					"PARENT_ID", con));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	static volatile long time = 0;
	static volatile long end = 0;

	public static void main(String[] args) throws Exception {
		 java.io.PrintStream out = new java.io.PrintStream(new
		 java.io.FileOutputStream(new java.io.File("e:/out.txt")));
		 System.setOut(out);
		 System.setErr(out);
		
		MainThread main = new MainThread();
		main.start();

	}

	static class MainMonitor extends Thread {
		public void run() {
			while (true) {
				int i = DBUtil.getNumActive();
				int j = DBUtil.getNumIdle();
				System.out.println("DBUtil.getNumIdle():" + j);
				System.out.println("DBUtil.getNumActive():" + i);
				if (count == 0)
					break;
				try {
					sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			try {
				sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			end = System.currentTimeMillis();
			
			System.out.println("成功：" + success + "次");
			System.out.println("失败：" + failed + "次");
			System.out.println("总数：" + total + "次");
			
			System.out.println("带连接执行时间：" + (end - time) / 1000 + "秒");
			System.out.println();

		}
	}

	static class MainThread extends Thread {
		public void start() {

			super.start();
		}

		public void run() {
			for (int i = 0; i < maxthread; i++) {
				if(i == maxthread - 1)
					time = System.currentTimeMillis();
				Thread t = new BinFaTest();
				t.start();
			}
			MainMonitor m = new MainMonitor();
			m.start();

		}
	}

	public static void testIdleNumber() {
		System.out.println("DBUtil.getNumIdle():" + DBUtil.getNumIdle());

	}

	public static void testActiveNumber() {
		System.out.println("DBUtil.getNumActive():" + DBUtil.getNumActive());
	}

	static volatile int count = 0;
	static int maxthread = 1;
	static volatile int success = 0;
	static volatile int failed = 0;
	static volatile int total = 0;

	static class BinFaTest extends Thread {
		public void start() {
			count++;
			System.out.println(count);
			super.start();
		}

		public void run() {
			// testIdleNumber();
			// testActiveNumber();

			while (count < maxthread) {
				try {
					sleep(5);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			try {
//				deletealltest();
//				inserttest();
//				selectpaginetest();
//				 testTX();
//				 test();
//				 batchTest();
//				batchWithTXTest();
//				 // insertBoubleClob();
//				 // LoadThread t = new LoadThread();
//				 // t.start();
//				 // LoadThread t1 = new LoadThread();
//				 // t1.start();
//				// updateBoubleClob();
//				 try {
//				  updateBoubleBlob();
//				 } catch (Exception e) {
//				 e.printStackTrace();
//				 }
//				 updateWithCommon();
//				 insertAutoID();
//				 insertAutoIDRollback();
//
//				 insertBlobFile();
//				  updateBoubleBlobFile();
////				 // String test = ":s:";
////				 // String test1 = "s::";
////				 // String test2 = "::s";
////				 // String a[] = test.split(":");
////				 // System.out.println("a.length:" +a.length);
////				 // System.out.println("a[0]:"+a[0]);
////				 // System.out.println("a[1]:"+a[1]);
////				 //
////				 // String b[] = test1.split(":");
////				 // System.out.println("b.length:" +b.length);
////				 // System.out.println("b[0]:"+b[0]);
////				 //
////				 // String c[] = test2.split(":");
////				 // System.out.println("c.length:" +c.length);
////				 // System.out.println("c[0]:"+c[0]);
//				 columnMetaDataTest();
////				 // System.out.println("b[1]:"+b[1]);
////				 // System.out.println(a.length);
////				 // testIdleNumber();
////				 // testActiveNumber();
////				
//				 updateBoubleClobWithOuterConnection();
				updateTest();

//				insertAutoID() ;

			} catch (Exception e) {
				e.printStackTrace();
			}
			count--;

		}

	}

}
