package org.frameworkset.bigdata.impl.monitor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.apache.log4j.Logger;
import org.frameworkset.bigdata.imp.Imp;
import org.frameworkset.bigdata.imp.monitor.SpecialMonitorObject;
import org.frameworkset.util.annotations.ResponseBody;
import org.frameworkset.web.servlet.ModelMap;

import com.frameworkset.common.poolman.SQLExecutor;
import com.frameworkset.common.poolman.handle.ResultSetNullRowHandler;
import com.frameworkset.orm.transaction.TransactionManager;
import com.frameworkset.util.StringUtil;

public class JobstaticMonitorController {
	private static Logger log = Logger
			.getLogger(JobstaticMonitorController.class);

	public String index(String job, ModelMap model) {
		SpecialMonitorObject specialMonitorObject = Imp.getImpStaticManager()
				.getSpecialMonitorObject(job);
		model.addAttribute("jobInfo", specialMonitorObject);

		model.addAttribute("adminNode", Imp.getImpStaticManager()
				.getLocalNode());
		List<String> allnodes = Imp.getImpStaticManager()
				.getAllDataNodeString();
		model.addAttribute("allDataNodes", allnodes);

		return "path:index";
	}

	public @ResponseBody
	String synJobStatus() {
		Imp.getImpStaticManager().synJobStatus();
		return "success";
	}
	
	public @ResponseBody
	String stopJob(String jobname) {
		Imp.getImpStaticManager().stopJob(jobname);
		return "success";
	}

	public @ResponseBody
	String executeJob(String job) {
		if (StringUtil.isEmpty(job))
			return "没有要执行的作业";
		try {
			Imp.executeJob(job);
		} catch (Exception e) {
			log.error("执行作业失败：" + job, e);
			return StringUtil.exceptionToString(e);
		}
		return "success";
	}

	/**
	 * 创建并提交一个新作业
	 * 
	 * @param jobdef
	 * @return
	 */
	public @ResponseBody
	String submitNewJob(String jobdef) {
		if (StringUtil.isEmpty(jobdef))
			return "没有提交要执行的作业";
		try {
			String msg = Imp.submitNewJob(jobdef);
			return msg;
		} catch (Exception e) {
			log.error("提交执行作业失败：" + jobdef, e);
			return StringUtil.exceptionToString(e);
		}

	}

	public static void main(String[] args) throws SQLException {
		// Connection connection = DBUtil.getConection("test");
		// Statement statement = connection.createStatement();
		// statement.setQueryTimeout(30); // set timeout to 30 sec.
		//
		// statement.executeUpdate("drop table if exists person");
		// statement.executeUpdate("create table person (id integer, name string)");
		// statement.executeUpdate("insert into person values(1, 'leo')");
		// statement.executeUpdate("insert into person values(2, 'yui')");
		// ResultSet rs = statement.executeQuery("select * from person");
		// while(rs.next())
		// {
		// // read the result set
		// System.out.println("name = " + rs.getString("name"));
		// System.out.println("id = " + rs.getInt("id"));
		// }
		//
		TransactionManager tx = new TransactionManager();
		try {
			tx.begin(TransactionManager.RW_TRANSACTION);
			SQLExecutor.updateWithDBName("defaultp", "drop table if exists person");

			SQLExecutor.updateWithDBName("defaultp",
					"create table person (id integer, name string)");
			SQLExecutor.insert("insert into person values(1, 'leo')");
			SQLExecutor.insert("insert into person values(2, 'yui')");
			SQLExecutor.queryWithDBNameByNullRowHandler(
					new ResultSetNullRowHandler() {

						@Override
						public void handleRow(ResultSet rs) throws Exception {
							System.out.println("name = " + rs.getString("name"));
							System.out.println("id = " + rs.getInt("id"));

						}
					}, "defaultp", "select * from person");
			
			SQLExecutor.queryListInfoWithDBNameByNullRowHandler(
					new ResultSetNullRowHandler() {

						@Override
						public void handleRow(ResultSet rs) throws Exception {
							System.out.println("name = " + rs.getString("name"));
							System.out.println("id = " + rs.getInt("id"));

						}
					}, "defaultp", "select * from person", 0, 2);
			
			SQLExecutor.queryListInfoWithDBNameByNullRowHandler(
					new ResultSetNullRowHandler() {

						@Override
						public void handleRow(ResultSet rs) throws Exception {
							System.out.println("tet name = " + rs.getString("name"));
							

						}
					}, "test", "select 1 as name from dual", 0, 2);
			tx.commit();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			tx.release();
		}
	}

}
