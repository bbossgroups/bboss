package org.frameworkset.bigdata.util;

import java.sql.SQLException;
import java.util.List;

import org.apache.log4j.Logger;
import org.frameworkset.bigdata.imp.HDFSUploadData;
import org.frameworkset.bigdata.imp.TaskConfig;

import com.frameworkset.common.poolman.SQLExecutor;
import com.frameworkset.common.poolman.util.SQLUtil;

public class DBHelper {
	private static Logger log = Logger.getLogger(DBHelper.class);
	public static List<String> getAllJobNames() throws Exception
	{
		List<String> names = SQLExecutor.queryList(String.class, "select jobname from jobconfig");
		return names;
	}
	public static void addOrUdate(String jobname,String jobdef) throws Exception
	{
		int count = SQLExecutor.queryObjectWithDBName(int.class,"bigdata_conf", "select count(1) from jobconfig where jobname=?", jobname);
		if(count == 0)
		{
			String insert = "insert into jobconfig(jobname,jobdef) values(?,?)";
			SQLExecutor.insertWithDBName("bigdata_conf",insert, jobname,jobdef);
		}
		else
		{
			String update = "update jobconfig set jobdef=? where  jobname=?";
			SQLExecutor.updateWithDBName("bigdata_conf",update, jobdef,jobname);
		}
	}
	
	public static DBJobstatic getDBJobstatic(String jobname,String jobstaticid) throws Exception
	{
		return SQLExecutor.queryObjectWithDBName(DBJobstatic.class,"bigdata_conf", "select * from jobstatic where jobname=? and jobstaticid=?", jobname,jobstaticid);
	}
	
	
	public static List<DBJobstatic> getDBJobstatics(String jobname) throws Exception
	{
		return SQLExecutor.queryListWithDBName(DBJobstatic.class,"bigdata_conf", "select jobname,savetime,jobstaticid from jobstatic where jobname=? order by savetime desc", jobname);
	}
	
	public static void saveDBJobstatic(DBJobstatic DBJobstatic) throws Exception
	{
		int count = SQLExecutor.queryObjectWithDBName(int.class,"bigdata_conf", "select count(1) from jobstatic where jobstaticid=?", DBJobstatic.getJobstaticid());
		if(count == 0)
		{
			 SQLExecutor.insertBean("bigdata_conf","insert into jobstatic(jobname,savetime,jobstatic,jobstaticid) values(#[jobname],#[savetime],#[jobstatic],#[jobstaticid])",DBJobstatic);
		}
		else
		{
			SQLExecutor.updateBean("bigdata_conf","update jobstatic set jobname=#[jobname],savetime=#[savetime],jobstatic=#[jobstatic] where jobstaticid=#[jobstaticid]",DBJobstatic);
		}
		
	}
	public static DBJob getDBJob(String jobname) throws Exception
	{
		return SQLExecutor.queryObject(DBJob.class, "select * from jobconfig where jobname=?", jobname);
	}
	public static void initConfgDB(String dbpath)
	{
		SQLUtil.startPool("bigdata_conf","org.sqlite.JDBC","jdbc:sqlite://"+dbpath,"root","root",
				 null,//"false",
				
				 null,// "READ_UNCOMMITTED",
				 "select 1",
				 "jndi-bigdata_conf" ,   
	        		10,
	        		10,
	        		20,
	        		false,
	        		false,
	        		null        ,false,false
	        		);
		String exist = "select 1 from jobconfig";
		
		try {
			SQLExecutor.queryObjectWithDBName(int.class,"bigdata_conf", exist);
		} catch (Exception e) {
			log.info("jobconfig table 不存在，创建jobconfig表：create table jobconfig (jobname string, jobdef string,PRIMARY KEY (jobname))。",e);
			try {
				SQLExecutor.updateWithDBName("bigdata_conf","create table jobconfig (jobname string, jobdef string,PRIMARY KEY (jobname))");
				log.info("创建jobconfig表成功：create table jobconfig (jobname string, jobdef string,PRIMARY KEY (jobname))。");
			} catch (SQLException e1) {
				log.info("创建jobconfig表失败：create table jobconfig (jobname string, jobdef string,PRIMARY KEY (jobname))。",e1);
				e1.printStackTrace();
			}
		}
		
		 exist = "select 1 from jobstatic";
			
			try {
//				SQLExecutor.updateWithDBName("bigdata_conf","drop table jobstatic ");
				SQLExecutor.queryObjectWithDBName(int.class,"bigdata_conf", exist);
			} catch (Exception e) {
				log.info("jobstatic table 不存在，创建jobconfig表：create table jobstatic (jobstaticid string,jobname string, jobstatic TEXT,savetime number(10), PRIMARY KEY (jobstaticid))。",e);
				try {
					SQLExecutor.updateWithDBName("bigdata_conf","create table jobstatic (jobstaticid string,jobname string, jobstatic TEXT,savetime number(10),PRIMARY KEY (jobstaticid))");
					log.info("创建jobstatic表成功：create table jobstatic (jobstaticid string,jobname string, jobstatic TEXT,savetime number(10), PRIMARY KEY (jobstaticid))。");
				} catch (SQLException e1) {
					log.info("创建jobstatic表失败：create table jobstatic (jobstaticid string,jobname string, jobstatic TEXT,savetime number(10), PRIMARY KEY (jobstaticid))。",e1);
					e1.printStackTrace();
				}
			}
	}
	
	public static void initDB(HDFSUploadData HDFSUploadData)
	{
		if(HDFSUploadData.getDriver() != null && HDFSUploadData.getDriver().trim().length() > 0)
		{
			int poolsize = HDFSUploadData.isUsepool()? evalpoolsize(HDFSUploadData.getGeneworkthreads()):0;
			SQLUtil.startPool(HDFSUploadData.getDbname(),HDFSUploadData.getDriver(),HDFSUploadData.getDburl(),HDFSUploadData.getDbuser(),HDFSUploadData.getDbpassword(),
					 HDFSUploadData.isReadOnly(),
					 null,//"READ_COMMITTED",
					 HDFSUploadData.getValidatesql(),
					 "jndi-"+HDFSUploadData.getDbname(),   
					 poolsize/2,
					 poolsize/2,
					 poolsize,
		        		HDFSUploadData.isUsepool(),
		        		false,
		        		null        ,true,false
		        		);
		}
 
	}
	private static int evalpoolsize(int genworkthread)
	{
		int poolsize = genworkthread + 2;
//		if(poolsize  < 10 )
//			poolsize  = 10;
		return poolsize;
	}
	public static void initDB(TaskConfig taskConfig)
	{
		if(taskConfig.getDriver() != null && taskConfig.getDriver().trim().length() > 0)
		{
			int poolsize = taskConfig.isUsepool()? evalpoolsize(taskConfig.getGeneworkthreads()):0;
			SQLUtil.startPool(taskConfig.getDbname(),taskConfig.getDriver(),taskConfig.getDburl(),taskConfig.getDbuser(),taskConfig.getDbpassword(),
					 taskConfig.getReadOnly(),
					 null,//"READ_COMMITTED",
					 taskConfig.getValidatesql(),
					 "jndi-"+taskConfig.getDbname(),   
					 poolsize/2,
					 poolsize/2,
		        		poolsize,
		        		taskConfig.isUsepool(),
		        		false,
		        		null        ,true,false
		        		);
		}
 
	}

}
