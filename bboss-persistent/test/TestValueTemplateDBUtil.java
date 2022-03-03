import com.frameworkset.common.poolman.*;
import com.frameworkset.common.poolman.util.DBOptions;
import com.frameworkset.sqlexecutor.ListBean;

import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;


public class TestValueTemplateDBUtil {
	public static void testrollbackWithConnection() throws Throwable
	{
		Object ret = TemplateDBUtil.executeTemplate(
				new JDBCValueTemplate(){
					public Object execute() throws Exception {
						DBUtil dbUtil = new DBUtil();
						Connection con = PreparedDBUtil.getConection();
						PreparedDBUtil db = null;
						try {
							for (int i = 0; i < 1; i++) {
								db = new PreparedDBUtil();
								
									db
											.preparedInsert(
													"insert into td_reg_bank_acc_bak (create_acc_time,starttime,endtime) values(?,?,?)");
									Date today = new Date(new java.util.Date().getTime());
//									db.setInt(1, 1000 + i);

									db.setDate(1, new java.util.Date());
									db.setDate(2, new java.util.Date());
									db.setDate(3, new java.util.Date());

									// if(true && i < 50)
									// {
									// throw new Exception("e");
									// }

									db.executePrepared(con);

								
							}
							
							
						
						}
						catch(SQLException e)
						{
							con.rollback();
							throw e;
						}
						catch(Exception e)
						{
							con.rollback();
							throw e;
						}
						finally {
							db.resetPrepare();
							con.close();
						}
						
						dbUtil.executeInsert("insert into table values()");
						return "go";
					}

					@Override
					public Object execute(DBOptions dbOptions) throws Exception {
						return null;
					}

				}
			);
		System.out.println("testrollbackWithConnection return value:" + ret);
	}
	
	/**
	 * 
	 * @param beans
	 * @return
	 * @throws Throwable 
	 */
	public void stringarraytoList(final List<ListBean> beans) throws Throwable {
		List<ListBean> ret = TemplateDBUtil.executeTemplate(
				new JDBCValueTemplate<List<ListBean>>(){
					public List<ListBean>  execute() throws Exception {
						String sql = "INSERT INTO LISTBEAN (" + "ID," + "FIELDNAME," 
						+ "FIELDLABLE," + "FIELDTYPE," + "SORTORDER,"
						+ " ISPRIMARYKEY," + "REQUIRED," + "FIELDLENGTH,"
						+ "ISVALIDATED" + ")" + "VALUES"
						+ "(#[id],#[fieldName],#[fieldLable],#[fieldType],#[sortorder]"
						+ ",#[isprimaryKey],#[required],#[fieldLength],#[isvalidated])";
						SQLExecutor.delete("delete from LISTBEAN");
						SQLExecutor.insertBeans(sql, beans);
						return beans;
				}

					@Override
					public List<ListBean> execute(DBOptions dbOptions) throws Exception {
						return null;
					}
				}
		);
	}
	
	
	public static void testrollback() throws Throwable
	{
		Object ret = TemplateDBUtil.executeTemplate(
				new JDBCValueTemplate(){
					public Object execute() throws Exception {
						DBUtil dbUtil = new DBUtil();
						
						PreparedDBUtil db = null;
						try {
							for (int i = 0; i < 1; i++) {
								db = new PreparedDBUtil();
								
									db.preparedInsert(
													"insert into td_reg_bank_acc_bak (create_acc_time,starttime,endtime) values(?,?,?)");
									Date today = new Date(new java.util.Date().getTime());
//									db.setInt(1, 1000 + i);

									db.setDate(1, new java.util.Date());
									db.setDate(2, new java.util.Date());
									db.setDate(3, new java.util.Date());

									// if(true && i < 50)
									// {
									// throw new Exception("e");
									// }

									db.executePrepared();

								
							}
						} finally {
							
						}
						
						dbUtil.executeInsert("insert into table values()");
						return "go";
					}

					@Override
					public Object execute(DBOptions dbOptions) throws Exception {
						return null;
					}

				}
			);
		System.out.println("testrollback return value:" + ret);
	}
	
	public static void testcommit() throws Throwable
	{
		Object ret = TemplateDBUtil.executeTemplate(
				new JDBCValueTemplate(){
					public Object execute() throws Exception {
						DBUtil dbUtil = new DBUtil();
						
						PreparedDBUtil db = null;
						try {
							for (int i = 0; i < 1; i++) {
								db = new PreparedDBUtil();
								
									db.preparedInsert(
													"insert into td_reg_bank_acc_bak (create_acc_time,starttime,endtime) values(?,?,?)");
									Date today = new Date(new java.util.Date().getTime());
//									db.setInt(1, 1000 + i);

									db.setDate(1, new java.util.Date());
									db.setDate(2, new java.util.Date());
									db.setDate(3, new java.util.Date());

									// if(true && i < 50)
									// {
									// throw new Exception("e");
									// }

									db.executePrepared();

								
							}
						} finally {
							
						}
						
						dbUtil.executeInsert("insert into td_reg_bank_acc_bak (clob1,clob2) values('aa','bb')");
						return "go";
					}

					@Override
					public Object execute(DBOptions dbOptions) throws Exception {
						return null;
					}

				}
			);
		System.out.println("testcommit return value:" + ret);
	}
	
	
	public static void testcommitWithConnection() throws Throwable
	{
		Object ret = TemplateDBUtil.executeTemplate(
				new JDBCValueTemplate(){
					public Object execute() throws Exception {
						DBUtil dbUtil = new DBUtil();
						
						PreparedDBUtil db = null;
						try {
							for (int i = 0; i < 1; i++) {
								db = new PreparedDBUtil();
								
									db.preparedInsert(
													"insert into td_reg_bank_acc_bak (create_acc_time,starttime,endtime) values(?,?,?)");
									Date today = new Date(new java.util.Date().getTime());
//									db.setInt(1, 1000 + i);

									db.setDate(1, new java.util.Date());
									db.setDate(2, new java.util.Date());
									db.setDate(3, new java.util.Date());

									// if(true && i < 50)
									// {
									// throw new Exception("e");
									// }

									db.executePrepared(dbUtil.getConection());

								
							}
						} finally {
							
						}
						
						dbUtil.executeInsert("insert into td_reg_bank_acc_bak (clob1,clob2) values('conaa','conbb')");
						return "go";
					}

					@Override
					public Object execute(DBOptions dbOptions) throws Exception {
						return null;
					}

				}
			);
		System.out.println("testcommitWithConnection return value:" + ret);
	}
	
	public static void main(String[] args) 
	{
		try
		{
			TestValueTemplateDBUtil.testrollback();
		}
		catch(Throwable e)
		{
			e.printStackTrace();
		}
		try {
			TestValueTemplateDBUtil.testrollbackWithConnection();
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			TestValueTemplateDBUtil.testcommit();
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			TestValueTemplateDBUtil.testcommitWithConnection();
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(DBUtil.getNumActive());
		System.out.println(DBUtil.getNumIdle());
		System.out.println(DBUtil.getMaxNumActive());
		
	}

}
