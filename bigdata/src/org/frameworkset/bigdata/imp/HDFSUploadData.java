package org.frameworkset.bigdata.imp;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.log4j.Logger;
import org.frameworkset.bigdata.imp.monitor.JobStatic;
import org.frameworkset.bigdata.imp.monitor.SpecialMonitorObject;
import org.frameworkset.bigdata.util.DBHelper;
import org.frameworkset.bigdata.util.DBJob;
import org.frameworkset.event.Event;
import org.frameworkset.event.EventHandle;
import org.frameworkset.event.EventImpl;
import org.frameworkset.event.EventTarget;
import org.frameworkset.event.SimpleEventType;
import org.frameworkset.remote.EventUtils;
import org.frameworkset.spi.BaseApplicationContext;
import org.frameworkset.spi.DefaultApplicationContext;
import org.frameworkset.spi.SOAApplicationContext;
import org.jgroups.Address;

import com.frameworkset.common.poolman.DBUtil;
import com.frameworkset.common.poolman.PreparedDBUtil;
import com.frameworkset.common.poolman.SQLExecutor;
import com.frameworkset.util.SimpleStringUtil;

public class HDFSUploadData {
	public String HADOOP_PATH;

	private static Logger log = Logger.getLogger(HDFSUploadData.class);

	public static final SimpleEventType hdfsuploadevent = new SimpleEventType(
			"hdfsuploadevent");
	public static final SimpleEventType hdfs_upload_finish_event = new SimpleEventType(
			"hdfs_upload_finish_event");
	public static final SimpleEventType hdfs_upload_monitor = new SimpleEventType(
			"hdfs_upload_monitor");
	public static final SimpleEventType hdfs_upload_monitor_request_commond = new SimpleEventType(
			"hdfs_upload_monitor_request_commond");
	public static final SimpleEventType hdfs_upload_monitor_jobstop_commond = new SimpleEventType(
			"hdfs_upload_monitor_jobstop_commond");
	public static final SimpleEventType hdfs_upload_monitor_response_commond = new SimpleEventType(
			"hdfs_upload_monitor_response_commond");

	public static final SimpleEventType hdfs_upload_monitor_stopdatasource_commond = new SimpleEventType(
			"hdfs_upload_monitor_stopdatasource_commond");

	public static final SimpleEventType hdfs_upload_monitor_reassigntasks_request_commond = new SimpleEventType(
			"hdfs_upload_monitor_reassigntasks_request_commond");
	
	public static final SimpleEventType hdfs_upload_monitor_reassigntasks_response_commond = new SimpleEventType(
			"hdfs_upload_monitor_reassigntasks_response_commond");

	/**
	 * 重新分派某个节点的排队任务配置
	 */
	private String reassigntaskNode;
	
	private String reassigntaskJobname;
	// /**
	// * 一般不需要指定
	// */
	// private int needassigntasks;
	// /**
	// * 节点正在处理的任务情况
	// */
	// private String inhandle;
	/**
	 * 数据库连接信息
	 */
	private String driver;
	private String dburl;
	private String dbpassword;
	private String dbuser;
	private String validatesql;
	private String readOnly;
	private boolean usepool;
	// public static final String FILE_PATH="/10.0.15.71.1433174400004";
	private FileSystem fileSystem = null;
	private String hdfsserver;
	private String hdfsdatadir;
	/**
	 * 指定一个删除作业，如果设定deletefiles属性，那么只做hdfs文件删除操作，不会做其他事情
	 */
	private String deletefiles;

	/**
	 * 指定要停止的数据源清单，多个用逗号分隔
	 */
	private String stopdbnames;

	private String localpath;
	private String tablename;
	private String schema;
	private String pkName;
	private String columns;
	private String filebasename;
	// 切分的数据块
	private int datablocks;
	private String dbname;
	private int workservers;
	private boolean rundirect;
	int geneworkthreads = 20;
	int uploadeworkthreads = 20;
	int genqueques = 5;
	int uploadqueues = 5;
	int genquequetimewait = 10;
	int uploadqueuetimewait = 10;
	long startid;
	long endid;
	String datatype = "json";
	boolean genlocalfile;
	boolean clearhdfsfiles;
	String querystatement;
	String limitstatement;
	String countstatement;
	String pageinestatement;
	long tablerows;
	boolean usepagine = false;
	boolean adminnodeasdatanode;
	String jobname;
	/**
	 * 指定需要执行哪几个任务
	 */
	int[] blocks = null;
	/**
	 * 用来存放块的子块
	 */
	private Map<String, List<Integer>> blocksplits = null;

	/**
	 * 指定需要排除哪几个任务
	 */
	int[] excludeblocks = null;
	String excludeblocks_str;
	String blocks_str;
	/**
	 * 用来存放块的子块
	 */
	Map<String, List<Integer>> excludeblocksplits = null;
	int subblocks = -1;
	private Map<String, TaskConfig> tasks;
	
	/**
	 * 关联附加字段表记录-通过joinby字段与tablename中对应的表进行关联查询
	 */
	private String subtablename;
	/**
	 * 使用表的分区进行任务切割
	 */
	private boolean usepartition;
	private String partitions;
	private boolean usesubpartition = true;
	private String excludepartitions;
	private String leftJoinby;
	private String rightJoinby;
	/**
	 * 子查询sql
	 */
	String subquerystatement;
	
	/**
	 * 单任务属性
	 */
	boolean onejob;
	String target ;
	int rowsperfile;
	private int errorrowslimit = -1;
	/**
	 * 开始文件号
	 */
	int startfileNo = -1;
	
	/**
	 * 构建分派任务时调用，只需要设置基本信息即可
	 * 
	 * @return
	 */
	TaskConfig buildTaskConfigWithID(String jobstaticid) {
		TaskConfig config = new TaskConfig();
		config.driver = this.driver;
		config.dburl = this.dburl;
		config.dbpassword = this.dbpassword;
		config.dbuser = this.dbuser;
		config.validatesql = this.validatesql;
		config.setReadOnly(readOnly);
		config.usepool = this.usepool;
		config.filebasename = filebasename;
		config.hdfsdatadirpath = this.hdfsdatadir;
		config.hdfsserver = this.hdfsserver;
		config.localdirpath = this.localpath;
		config.pkname = this.pkName;
		config.tablename = this.tablename;
		config.geneworkthreads = this.geneworkthreads;
		config.uploadeworkthreads = this.uploadeworkthreads;
		config.genqueques = this.genqueques;
		config.uploadqueues = this.uploadqueues;
		config.genquequetimewait = this.genquequetimewait;
		config.uploadqueuetimewait = this.uploadqueuetimewait;
		config.datatype = this.datatype;
		config.datablocks = this.datablocks;
		config.columns = this.columns;
		config.dbname = this.dbname;
		config.genlocalfile = genlocalfile;
		config.datatype = this.datatype;
		config.schema = this.schema;
		config.usepagine = this.usepagine;
		config.adminnodeasdatanode = this.adminnodeasdatanode;
		config.jobname = jobname;
		config.subblocks = this.subblocks;
		config.setDeletefiles(deletefiles);
		config.setStopdbnames(stopdbnames);
		config.setReassigntaskNode(reassigntaskNode);
		config.setReassigntaskJobname(reassigntaskJobname);
		config.excludeblocks = this.excludeblocks_str;
		config.blocks = this.blocks_str;
		config.setUsepartition(this.usepartition);
		config.setPartitions(partitions);
		config.setExcludepartitions(excludepartitions);
		config.setUsesubpartition(usesubpartition);
		config.setSubtablename(subtablename);;
		config.setLeftJoinby(leftJoinby);
		config.setRightJoinby(rightJoinby);
		config.setSubquerystatement(subquerystatement);
		 config.setTarget(target);
		 config.setErrorrowslimit(errorrowslimit);
		 config.setRowsperfile(rowsperfile);
		 config.setOnejob(onejob);
		 config.setStartfileNo(startfileNo);
		 config.startid = this.startid;
		 config.endid = this.endid;
		 config.setJobstaticid(jobstaticid);
		if(this.onejob)
		{
			if (this.querystatement == null || this.querystatement.equals("")) {
				StringBuilder sqlbuilder = new StringBuilder();
				sqlbuilder.append("select ");
				if (config.columns != null && !config.columns.equals("")) {
					sqlbuilder.append(config.columns);
				} else
					sqlbuilder.append("* ");
				sqlbuilder.append(" from  ");
				if (this.schema != null && !this.schema.equals(""))
					sqlbuilder.append(config.schema).append(".");
				sqlbuilder.append(config.tablename);
				config.setQuerystatement(sqlbuilder.toString());
			} else {
				config.setQuerystatement(this.querystatement);
			}
		}
		else if(this.usepartition)
		{
			if (this.querystatement == null || this.querystatement.equals("")) {
				StringBuilder sqlbuilder = new StringBuilder();
				sqlbuilder.append("select ");
				if (config.columns != null && !config.columns.equals("")) {
					sqlbuilder.append(config.columns);
				} else
					sqlbuilder.append("* ");
				sqlbuilder.append(" from  ");
				if (this.schema != null && !this.schema.equals(""))
					sqlbuilder.append(config.schema).append(".");
				sqlbuilder.append(config.tablename).append(" #{partition}");
				config.setQuerystatement(sqlbuilder.toString());
			} else {
				config.setQuerystatement(this.querystatement);
			}
			
			
		}
		else if (!this.usepagine) {
			config.limitstatement = limitstatement;
			if (this.querystatement == null || this.querystatement.equals("")) {
				StringBuilder sqlbuilder = new StringBuilder();
				sqlbuilder.append("select ");
				if (config.columns != null && !config.columns.equals("")) {
					sqlbuilder.append(config.columns);
				} else
					sqlbuilder.append("* ");
				sqlbuilder.append(" from  ");
				if (this.schema != null && !this.schema.equals(""))
					sqlbuilder.append(config.schema).append(".");
				sqlbuilder.append(config.tablename).append(" where ")
						.append(config.pkname).append("<=? and ")
						.append(config.pkname).append(">=?");
				config.setQuerystatement(sqlbuilder.toString());
			} else {
				config.setQuerystatement(this.querystatement);
			}
			
			 
		} else {
			config.tablerows = tablerows;
			config.countstatement = countstatement;
			if (pageinestatement == null || pageinestatement.equals("")) {
				// select * from (SELECT t.*,
				// ROW_NUMBER() OVER ( ORDER BY TID) rowno_ from testbigdata t)
				// bb where bb.rowno_ <100 and bb.rowno_ >10
				// StringBuilder sqlbuilder = new StringBuilder();
				// sqlbuilder.append("select * from (SELECT ");
				// if(config.columns != null && ! config.columns.equals(""))
				// {
				// sqlbuilder.append( config.columns);
				// }
				// else
				// sqlbuilder.append("t.* ");
				// sqlbuilder.append(",ROW_NUMBER() OVER ( ORDER BY TID) rowno_  from   ");
				// if(this.schema != null && !this.schema.equals(""))
				// sqlbuilder.append(config.schema).append(".");
				// sqlbuilder.append( config.tablename);
				// sqlbuilder.append(
				// "t) bb where bb.rowno_ <? and bb.rowno_ >?");
				// config.pageinestatement = sqlbuilder.toString();
				config.pageinestatement = DBUtil.getDBAdapter(this.dbname)
						.getStringPagineSql(schema, tablename, pkName, columns);

				// DBUtil.getDBAdapter(this.dbname).getStringPagineSql(sqlbuilder.toString());
			} else {
				config.pageinestatement = pageinestatement;
			}
			
			 

		}
		if (this.subquerystatement == null || this.subquerystatement.equals("")) {
			if(!SimpleStringUtil.isEmpty(subtablename))
			{
				StringBuilder sqlbuilder = new StringBuilder();
				
				sqlbuilder.append("select * ");
				sqlbuilder.append(" from  ");
				if (this.schema != null && !this.schema.equals(""))
					sqlbuilder.append(config.schema).append(".");
				sqlbuilder.append(this.subtablename).append(" where ").append(this.leftJoinby).append("=?");
				config.subquerystatement= sqlbuilder.toString();
			}
		} else {
			config.subquerystatement = this.subquerystatement;
		}

		return config;
	}

	private static LinkBlocks buildLinkBlocks(String blocks_) {
		int blocks[] = null;
		LinkBlocks linkBlocks = new LinkBlocks();
		Map<String, List<Integer>> blocksplits = new HashMap<String, List<Integer>>();
		if (blocks_ != null && !blocks_.equals("")) {
			String[] blocks_str = blocks_.trim().split(",");
			Set<Integer> blockset = new java.util.TreeSet<Integer>();

			for (int i = 0; i < blocks_str.length; i++) {
				String block = blocks_str[i];
				if (block.indexOf(".") > 0)// 判断是否需要过滤数据块
				{
					String[] tt = block.split("\\.");
					// blocks[i] = Integer.parseInt(tt[0]);
					blockset.add(Integer.parseInt(tt[0]));
					List<Integer> subtasks = blocksplits.get(tt[0]);
					if (subtasks == null) {
						subtasks = new ArrayList<Integer>();
						blocksplits.put(tt[0], subtasks);
					}
					subtasks.add(Integer.parseInt(tt[1]));

				} else {
					// blocks[i] = Integer.parseInt(block);
					blockset.add(Integer.parseInt(block));
				}
			}
			blocks = new int[blockset.size()];
			int i = 0;
			for (Integer b : blockset) {
				blocks[i] = b.intValue();
				i++;
			}
		}
		linkBlocks.blocks = blocks;
		linkBlocks.blocksplits = blocksplits;
		return linkBlocks;
	}

	/**
	 * 初始化作业需要的所有信息
	 * 
	 * @param jobname
	 * @return
	 */
	public static TaskConfig buildTaskConfig(String jobname) {
		TaskConfig config = new TaskConfig();
		BaseApplicationContext context = DefaultApplicationContext
				.getApplicationContext("tasks.xml");
		if (context.getProBean(jobname) == null)// 如果作业在固化配置中没有，则需要从db配置表中获取组件配置
		{
			try {
				DBJob job = DBHelper.getDBJob(jobname);
				if (job != null) {
					config.setJobdef(job.getJobdef());
					context = new SOAApplicationContext(job.getJobdef());
				} else
					log.info("jobname[" + jobname
							+ "]在数据库中没有定义,在固化配置文件tasks。xml中也没有定义.");
			} catch (Exception e) {
				log.error("jobname[" + jobname
						+ "]在数据库中没有定义,在固化配置文件tasks。xml中也没有定义.", e);
			}
		}
		String localpath = context
				.getStringExtendAttribute(jobname, "localdir");
		String hdfsdatadir = context.getStringExtendAttribute(jobname,
				"hdfsdatadir");
		String dbname = context.getStringExtendAttribute(jobname, "dbname");
		String hdfsserver = context.getStringExtendAttribute(jobname,
				"hdfsserver");
		String tablename = context.getStringExtendAttribute(jobname,
				"tablename");
		String schema = context.getStringExtendAttribute(jobname, "schema");
		String pkName = context.getStringExtendAttribute(jobname, "pkname");
		String columns = context.getStringExtendAttribute(jobname, "columns");
		int datablocks = context.getIntExtendAttribute(jobname, "datablocks");

		int geneworkthreads = context.getIntExtendAttribute(jobname,
				"geneworkthreads", 20);
		int uploadeworkthreads = context.getIntExtendAttribute(jobname,
				"uploadeworkthreads", 20);
		int genqueques = context
				.getIntExtendAttribute(jobname, "genqueques", 5);
		int uploadqueues = context.getIntExtendAttribute(jobname,
				"uploadqueues", 5);
		int genquequetimewait = context.getIntExtendAttribute(jobname,
				"genquequetimewait", 10);
		int uploadqueuetimewait = context.getIntExtendAttribute(jobname,
				"uploadqueuetimewait", 10);
		String datatype = context.getStringExtendAttribute(jobname, "datatype",
				"json");
		boolean genlocalfile = context.getBooleanExtendAttribute(jobname,
				"genlocalfile", false);
		String filebasename = context.getStringExtendAttribute(jobname,
				"filebasename", tablename);
		boolean clearhdfsfiles = context.getBooleanExtendAttribute(jobname,
				"clearhdfsfiles", false);
		String limitstatement = context.getStringExtendAttribute(jobname,
				"limitstatement");
		String querystatement = context.getStringExtendAttribute(jobname,
				"querystatement");
		long tablerows = context
				.getLongExtendAttribute(jobname, "tablerows", 0);
		boolean usepagine = context.getBooleanExtendAttribute(jobname,
				"usepagine", false);

		String countstatement = context.getStringExtendAttribute(jobname,
				"countstatement");
		String pageinestatement = context.getStringExtendAttribute(jobname,
				"pageinestatement");

		long startid = context
				.getLongExtendAttribute(jobname, "startid", -9999);
		long endid = context.getLongExtendAttribute(jobname, "endid", -9999);

		/**
		 * 指定需要排除哪几个任务
		 */
		String excludeblocks_ = context.getStringExtendAttribute(jobname,
				"excludeblocks");

		String blocks_ = context.getStringExtendAttribute(jobname, "blocks");
		// LinkBlocks linkBlocks = buildLinkBlocks(blocks_);
		// int[] blocks = linkBlocks.blocks;
		// Map<String,List<Integer>> blocksplits = linkBlocks.blocksplits;
		int subblocks = context.getIntExtendAttribute(jobname, "subblocks", -1);

		String driver = context.getStringExtendAttribute(jobname, "driver");
		String dburl = context.getStringExtendAttribute(jobname, "dburl");
		String dbpassword = context.getStringExtendAttribute(jobname,
				"dbpassword", "");
		String dbuser = context.getStringExtendAttribute(jobname, "dbuser", "");
		String validatesql = context.getStringExtendAttribute(jobname,
				"validatesql", "");

		String deletefiles = context.getStringExtendAttribute(jobname,
				"deletefiles");
		String stopdbnames = context.getStringExtendAttribute(jobname,
				"stopdbnames");
		config.setStopdbnames(stopdbnames);

		String reassigntaskNode = context.getStringExtendAttribute(jobname,
				"reassigntaskNode");
		String reassigntaskJobname = context.getStringExtendAttribute(jobname,
				"reassigntaskJobname");
		boolean usepartition = context.getBooleanExtendAttribute(jobname,
				"usepartition",false);
		String partitions = context.getStringExtendAttribute(jobname,
				"partitions");
		config.setPartitions(partitions);
		
		String excludepartitions = context.getStringExtendAttribute(jobname,
				"excludepartitions");
		boolean usesubpartition = context.getBooleanExtendAttribute(jobname,
				"usesubpartition",true);
		config.setUsesubpartition(usesubpartition);
		config.setExcludepartitions(excludepartitions);
		config.setUsepartition(usepartition);
		String subtablename = context.getStringExtendAttribute(jobname,
				"subtablename");
		config.setSubtablename(subtablename);
		String leftJoinby = context.getStringExtendAttribute(jobname,
				"leftJoinby");
		config.setLeftJoinby(leftJoinby);
		String rightJoinby = context.getStringExtendAttribute(jobname,
				"rightJoinby");
		
		String subquerystatement = context.getStringExtendAttribute(jobname,
				"subquerystatement"); 
		config.setSubquerystatement(subquerystatement);
		config.setRightJoinby(rightJoinby);
		config.setReassigntaskNode(reassigntaskNode);
		config.setReassigntaskJobname(reassigntaskJobname);
		
		 String target = context.getStringExtendAttribute(jobname,
					"target");
		 int errorrowslimit = context.getIntExtendAttribute(jobname,
					"errorrowslimit",-1);
		 config.setErrorrowslimit(errorrowslimit);
		 boolean onejob = context.getBooleanExtendAttribute(jobname,
					"single",false); 	 
		 int rowsperfile = context.getIntExtendAttribute(jobname, "rowsperfile", 0);
		 
		 config.setTarget(target);
		 config.setRowsperfile(rowsperfile);
		 config.setOnejob(onejob);
		 int startfileNo = context.getIntExtendAttribute(jobname, "startfileNo", -1);
		 config.setStartfileNo(startfileNo);
		boolean usepool = context.getBooleanExtendAttribute(jobname, "usepool",
				false);

		String readOnly = context.getStringExtendAttribute(jobname, "readOnly");

		config.setReadOnly(readOnly);
		config.driver = driver;
		config.dburl = dburl;
		config.dbpassword = dbpassword;
		config.dbuser = dbuser;
		config.validatesql = validatesql;

		config.usepool = usepool;
		config.filebasename = filebasename;
		config.hdfsdatadirpath = hdfsdatadir;
		config.hdfsserver = hdfsserver;
		config.localdirpath = localpath;
		config.pkname = pkName;
		config.tablename = tablename;
		config.geneworkthreads = geneworkthreads;
		config.uploadeworkthreads = uploadeworkthreads;
		config.genqueques = genqueques;
		config.uploadqueues = uploadqueues;
		config.genquequetimewait = genquequetimewait;
		config.uploadqueuetimewait = uploadqueuetimewait;
		config.datatype = datatype;
		config.datablocks = datablocks;
		config.columns = columns;
		config.dbname = dbname;
		config.genlocalfile = genlocalfile;
		config.datatype = datatype;
		config.schema = schema;

		config.usepagine = usepagine;
		config.clearhdfsfiles = clearhdfsfiles;
		config.jobname = jobname;
		config.startid = startid;
		config.endid = endid;
		config.blocks = blocks_;
		config.excludeblocks = excludeblocks_;
		// config.blocksplits = blocksplits;
		config.subblocks = subblocks;
		if(onejob)
		{
			if (querystatement == null || querystatement.equals("")) {
				StringBuilder sqlbuilder = new StringBuilder();
				sqlbuilder.append("select ");
				if (config.columns != null && !config.columns.equals("")) {
					sqlbuilder.append(config.columns);
				} else
					sqlbuilder.append("* ");
				sqlbuilder.append(" from  ");
				if ( schema != null && ! schema.equals(""))
					sqlbuilder.append(config.schema).append(".");
				sqlbuilder.append(config.tablename);
				config.setQuerystatement(sqlbuilder.toString());
			} else {
				config.setQuerystatement( querystatement);
			}
		}
		else if( usepartition)
		{
			if ( querystatement == null ||  querystatement.equals("")) {
				StringBuilder sqlbuilder = new StringBuilder();
				sqlbuilder.append("select ");
				if (config.columns != null && !config.columns.equals("")) {
					sqlbuilder.append(config.columns);
				} else
					sqlbuilder.append("* ");
				sqlbuilder.append(" from  ");
				if ( schema != null && ! schema.equals(""))
					sqlbuilder.append(config.schema).append(".");
				sqlbuilder.append(config.tablename).append(" #{partition}");
				config.setQuerystatement(sqlbuilder.toString());
			} else {
				config.setQuerystatement( querystatement);
			}
			
			
		}
		else if (!usepagine) {
			config.limitstatement = limitstatement;
			if (querystatement == null || querystatement.equals("")) {
				StringBuilder sqlbuilder = new StringBuilder();
				sqlbuilder.append("select ");
				if (config.columns != null && !config.columns.equals("")) {
					sqlbuilder.append(config.columns);
				} else
					sqlbuilder.append("* ");
				sqlbuilder.append(" from  ");
				if (schema != null && !schema.equals(""))
					sqlbuilder.append(config.schema).append(".");
				sqlbuilder.append(config.tablename).append(" where ")
						.append(config.pkname).append("<=? and ")
						.append(config.pkname).append(">=?");
				config.setQuerystatement(sqlbuilder.toString());
			} else {
				config.setQuerystatement( querystatement);
			}
		} else {
			config.tablerows = tablerows;
			config.countstatement = countstatement;
			if (pageinestatement == null || pageinestatement.equals("")) {

				// DBHelper.initDB(config);
				// config.pageinestatement = DBUtil.getDBAdapter(dbname)
				// .getStringPagineSql(schema, tablename, pkName, columns);

			} else {
				config.pageinestatement = pageinestatement;
			}

		}
		
		if (subquerystatement == null || subquerystatement.equals("") ) {
			if(!SimpleStringUtil.isEmpty(subtablename))
			{
				StringBuilder sqlbuilder = new StringBuilder();
				
				sqlbuilder.append("select * ");
				sqlbuilder.append(" from  ");
				if (schema != null && !schema.equals(""))
					sqlbuilder.append(config.schema).append(".");
				sqlbuilder.append(subtablename).append(" where ").append(leftJoinby).append("=?");
				config.subquerystatement= sqlbuilder.toString();
			}
			
		} else {
			config.subquerystatement = subquerystatement;
		}


		return config;
	}

	static class SplitTasks {
		TaskInfo[] segments;
		long segement = 0l;
	}

	/**
	 * segments , startid, subblocks,segement, div, usepagine
	 * 
	 * @param segments
	 * @param startid
	 * @param datablocks
	 * @param segement
	 * @param div
	 * @param usepagine
	 */
	private void spiltTask_(TaskInfo[] segments, long startid, long endid,
			int datablocks, long segement, long div, boolean usepagine,
			String filebasename, String parentTaskNo) {
		if (!usepagine) {
			for (int i = 0; i < datablocks; i++) {
				TaskInfo task = new TaskInfo();
				if (i < div) {
					task.startoffset = startid + i * segement + i;
					task.endoffset = task.startoffset + segement - 1 + 1;
					task.pagesize = task.endoffset - task.startoffset + 1;

				} else {
					task.startoffset = startid + i * segement + div;
					if (i == segments.length - 1)
						task.endoffset = endid;
					else
						task.endoffset = task.startoffset + segement - 1;
					task.pagesize = task.endoffset - task.startoffset + 1;
				}
				task.filename = filebasename + "_" + i;
				task.taskNo = parentTaskNo == null ? "" + i : parentTaskNo
						+ "." + i;
				segments[i] = task;
			}
		} else {
			for (int i = 0; i < datablocks; i++) {
				TaskInfo task = new TaskInfo();
				if (i < div) {
					task.startoffset = startid + i * segement + i;
					// task.endoffset = task.startoffset + segement-1+1;
					task.pagesize = segement + 1;

				} else {
					task.startoffset = startid + i * segement + div;
					// if(i == segments.length - 1)
					// task.endoffset = endid;
					// else
					// task.endoffset = task.startoffset + segement-1;
					task.pagesize = segement;
				}
				task.filename = filebasename + "_" + i;
				task.taskNo = parentTaskNo == null ? "" + i : parentTaskNo
						+ "." + i;
				segments[i] = task;
			}
		}
	}

	static class LinkTasks {
		int block;
		TaskInfo taskInfo;
	}

	static class LinkBlocks {
		int[] blocks;
		Map<String, List<Integer>> blocksplits;
	}

	/**
	 * 根据条件选择要执行的任务
	 * @param segments
	 * @param segement
	 * @return
	 */
	SplitTasks filterTasks(TaskInfo[] segments ,long segement)
	{
		List<LinkTasks> temp = null;
		List<TaskInfo> retTasks = null;
		if (this.blocks != null && this.blocks.length > 0)// 如果指定了任务块，则只执行指定的任务块
		{
			temp = new ArrayList<LinkTasks>(blocks.length);
			int blockslen = this.blocks.length;
			for (int i = 0; i < blockslen; i++) {
				if (blocks[i] < segments.length) {
					LinkTasks linkTasks = new LinkTasks();
					linkTasks.block = blocks[i];
					linkTasks.taskInfo = segments[blocks[i]];
					temp.add(linkTasks);

				}
			}
			if (temp.size() > 0) {
				if (!this.usepartition && this.subblocks > 0)// 如果需要对子任务进行切分，则需要进一步切分子任务，并且需要清除原来的大块数据对应的hdfs文件
				{
					// 进一步切分子任务
					List<TaskInfo> subchunks = new ArrayList<TaskInfo>();
					for (int i = 0; i < temp.size(); i++) {
						LinkTasks linkTasks = temp.get(i);
						TaskInfo t = linkTasks.taskInfo;
						SplitTasks subsplitTasks = buildJobSubChunks(t);
						if (subsplitTasks == null) {
							subchunks.add(t);
						} else {
							segement = subsplitTasks.segement;
							List<Integer> filters = this.blocksplits
									.get(linkTasks.block + "");// 指定了要过滤的数据块
							if (filters != null && filters.size() > 0) {
								List<TaskInfo> subsgs = new ArrayList<TaskInfo>(
										filters.size());
								TaskInfo[] sgs = subsplitTasks.segments;
								for (int j = 0; j < filters.size(); j++) {
									int pos = filters.get(j).intValue();
									if (pos < sgs.length) {
										subsgs.add(sgs[pos]);
									}
								}
								subchunks.addAll(subsgs);
							} else {
								subchunks.addAll(Arrays
										.asList(subsplitTasks.segments));
							}

						}
					}
					retTasks = subchunks;

				} else {
					retTasks = new ArrayList<TaskInfo>();
					for (int i = 0; i < temp.size(); i++) {
						LinkTasks linkTasks = temp.get(i);
						retTasks.add(linkTasks.taskInfo);
					}
				}

				segments = new TaskInfo[retTasks.size()];

				retTasks.toArray(segments);

			}
		} else if (this.excludeblocks != null && this.excludeblocks.length > 0)// 排除要清除的数据块
		{
			temp = new ArrayList<LinkTasks>();
			int blockslen = this.excludeblocks.length;
			for (int i = 0; i < blockslen; i++) {// 计算有效的排除块
				if (excludeblocks[i] < segments.length) {
					LinkTasks linkTasks = new LinkTasks();
					linkTasks.block = excludeblocks[i];
					linkTasks.taskInfo = segments[linkTasks.block];
					temp.add(linkTasks);

				}
			}
			if (temp.size() > 0) {
				if (!this.usepartition && this.subblocks > 0)// 如果需要对子任务进行切分，则需要进一步切分子任务，并且需要清除原来的大块数据对应的hdfs文件
				{
					// 进一步切分子任务
					List<TaskInfo> subchunks = new ArrayList<TaskInfo>();
					for (int j = 0; j < segments.length; j++) {
						SplitTasks subsplitTasks = buildJobSubChunks(segments[j]);
						if (subsplitTasks != null)
							segement = subsplitTasks.segement;
						boolean isexclude = false;
						for (int i = 0; i < temp.size(); i++) {
							LinkTasks linkTasks = temp.get(i);
							if (linkTasks.block == j)// 不是排除的块，需要重新抽取
							{
								isexclude = true;
								break;
							}
						}
						if (!isexclude) {
							if (subsplitTasks == null) {
								subchunks.add(segments[j]);
							} else {
								subchunks.addAll(Arrays
										.asList(subsplitTasks.segments));
							}
						} else {
							if (subsplitTasks == null)// 没有子块，整块排除
							{

							} else // 有子块，判断要排除的子块
							{
								List<Integer> excludes = this.excludeblocksplits
										.get(j + "");
								if (excludes == null)// 没有自定子块，整块的所有子块都排除
								{

								} else // 识别子块中要排除的块，将不需要排除的块添加到任务列表中
								{

									for (int k = 0; k < subsplitTasks.segments.length; k++) {
										boolean issubexclude = false;
										for (Integer epos : excludes) {
											if (k == epos.intValue()) {
												issubexclude = true;
												break;
											}

										}
										if (!issubexclude)
											subchunks
													.add(subsplitTasks.segments[k]);
									}

								}

							}
						}

					}
					retTasks = subchunks;

				} else {
					retTasks = new ArrayList<TaskInfo>();
					for (int j = 0; j < segments.length; j++) {
						boolean isexclude = false;
						for (int i = 0; i < temp.size(); i++) {
							LinkTasks linkTasks = temp.get(i);
							if (linkTasks.block == j) {
								isexclude = true;
							}
						}
						if (!isexclude)
							retTasks.add(segments[j]);
					}
				}

				segments = new TaskInfo[retTasks.size()];

				retTasks.toArray(segments);

			}
		}
		SplitTasks splitTasks = new SplitTasks();
		splitTasks.segement = segement;
		splitTasks.segments = segments;
		return splitTasks;
		
	}
	private List<PartitionInfo> handlePartitions(List<String> partitions) throws SQLException
	{
		List<PartitionInfo> result = null;
		if(this.usesubpartition )
		{
			if(partitions != null && partitions.size() > 0 )
			{
				result = new ArrayList<PartitionInfo>();
				StringBuilder queryPartitions = new StringBuilder();
				if(!SimpleStringUtil.isEmpty(schema))
				{
					queryPartitions.append("SELECT SUBPARTITION_NAME FROM ALL_TAB_SUBPARTITIONS WHERE TABLE_NAME=? and PARTITION_NAME=?");
				}
				else
					queryPartitions.append("SELECT SUBPARTITION_NAME FROM USER_TAB_SUBPARTITIONS WHERE TABLE_NAME=? and PARTITION_NAME=?");
				String t = queryPartitions.toString();
				String tab = this.tablename.toUpperCase();
				List<String> subparts = null;
				for(String partition:partitions)
				{
					if(!partition.startsWith("sub:"))
					{
						subparts = SQLExecutor.queryListWithDBName(String.class, this.dbname,t,tab,partition);
						if(subparts == null || subparts.size() == 0)
						{
							PartitionInfo partitionInfo = new PartitionInfo();
							partitionInfo.setIssubpartition(false);
							partitionInfo.setPartition(partition);
							result.add(partitionInfo);
						}
						else
						{
							for(String subpart:subparts)
							{
								PartitionInfo partitionInfo = new PartitionInfo();
								partitionInfo.setIssubpartition(true);
								partitionInfo.setPartition(partition);
								partitionInfo.setSubpartition(subpart);
								result.add(partitionInfo);
							}
						}
					}
					else
					{
						partition = partition.substring("sub:".length());
						PartitionInfo partitionInfo = new PartitionInfo();
						partitionInfo.setIssubpartition(true);
						partitionInfo.setPartition(null);
						partitionInfo.setSubpartition(partition);
						result.add(partitionInfo);
					}
						
					
				}
			}
		}
		else
		{
			if(partitions != null && partitions.size() > 0 )
			{
				result = new ArrayList<PartitionInfo>(partitions.size());
				for(String partition:partitions)
				{					 
					if(!partition.startsWith("sub:"))
					{
						PartitionInfo partitionInfo = new PartitionInfo();
						partitionInfo.setIssubpartition(false);
						partitionInfo.setPartition(partition);
						result.add(partitionInfo);
					}
					else
					{
						partition = partition.substring("sub:".length());
						PartitionInfo partitionInfo = new PartitionInfo();
						partitionInfo.setIssubpartition(true);
						partitionInfo.setSubpartition(partition);
						result.add(partitionInfo);
						
					}
				}
			}
		}
		return result;
	}
	private List<PartitionInfo> queryPartitions() throws SQLException
	{
		List<String> partitions = null;
		
		StringBuilder queryPartitions = new StringBuilder();
		if(!SimpleStringUtil.isEmpty(schema))
		{
			queryPartitions.append("SELECT PARTITION_NAME FROM ALL_TAB_PARTITIONS WHERE TABLE_NAME=upper('").append(this.tablename).append("') and table_owner='").append(this.schema).append("'");
		}
		else
			queryPartitions.append("SELECT PARTITION_NAME FROM USER_TAB_PARTITIONS WHERE TABLE_NAME=upper('").append(this.tablename).append("')");
		
		partitions = SQLExecutor.queryListWithDBName(String.class, this.dbname,queryPartitions.toString());
		return handlePartitions( partitions);
	}
	/**
	 * 计算和分解任务
	 * @return
	 * @throws Exception
	 */
	SplitTasks spiltTask() throws Exception {
		SplitTasks splitTasks = new SplitTasks();
		TaskInfo[] segments = null;
		long segement = 0l;
		if(this.usepartition)//分区操作
		{
			List<PartitionInfo> partitions = null;
			if(SimpleStringUtil.isEmpty(this.partitions))//没有指定执行的分区
			{
//				StringBuilder queryPartitions = new StringBuilder();
//				if(!SimpleStringUtil.isEmpty(schema))
//				{
//					queryPartitions.append("SELECT PARTITION_NAME FROM ALL_TAB_PARTITIONS WHERE TABLE_NAME=upper('").append(this.tablename).append("') and table_owner='").append(this.schema).append("'");
//				}
//				else
//					queryPartitions.append("SELECT PARTITION_NAME FROM USER_TAB_PARTITIONS WHERE TABLE_NAME=upper('").append(this.tablename).append("')");
//				
//				partitions = SQLExecutor.queryListWithDBName(String.class, this.dbname,queryPartitions.toString());
				partitions = queryPartitions();
				if(partitions != null && partitions.size() > 0)
				{
					if(!SimpleStringUtil.isEmpty(excludepartitions))//如果指定了不需要执行的分区，则过滤排除的分区，这个和excludeblocks功能类似，但是用于第一次执行作业时使用，excludeblocks不能用于第一次作业
					{
						String[] epts = this.excludepartitions.split(",");						
						List<PartitionInfo> temppts = new ArrayList<PartitionInfo>();
						for(int i = 0; i < partitions.size(); i ++)
						{
							PartitionInfo part = partitions.get(i);
							boolean exclude = false;
							for(String ept:epts )
							{
								if(ept.startsWith("sub:"))
									ept = ept.substring("sub:".length());
								String pt = part.isIssubpartition()?part.getSubpartition():part.getPartition();
								if(pt.equalsIgnoreCase(ept))
								{
									exclude = true;
									break;
								}
							}
							if(!exclude)
							{
								temppts.add(part);
							}
						}
						partitions = temppts;
					}
					
					
					segments = new TaskInfo[partitions.size()];
					for(int i = 0; i < partitions.size(); i ++)
					{
						PartitionInfo partition = partitions.get(i);
						TaskInfo taskInfo = new TaskInfo();
						taskInfo.partitionName = partition.getPartition();
						taskInfo.setIssubpartition(partition.isIssubpartition());
						taskInfo.setSubpartition(partition.getSubpartition());
						taskInfo.taskNo = i + "";
						taskInfo.filename = partition.isIssubpartition()?partition.getSubpartition():partition.getPartition();
						segments[i] = taskInfo;
					}
				}
			}
			else //指定了执行的分区，有时候有新的分区，需要独立执行，就可以使用这个办法
			{
				
				String[] pts = this.partitions.split(",");
				partitions = handlePartitions(Arrays.asList(pts));
				segments = new TaskInfo[partitions.size()];
				for(int i = 0; i < partitions.size(); i ++)
				{
					PartitionInfo partition = partitions.get(i);
					TaskInfo taskInfo = new TaskInfo();
					taskInfo.partitionName = partition.getPartition(); 
					taskInfo.setIssubpartition(partition.isIssubpartition());
					taskInfo.setSubpartition(partition.getSubpartition());
					taskInfo.taskNo = i + "";
					taskInfo.filename = partition.isIssubpartition()?partition.getSubpartition():partition.getPartition();
					segments[i] = taskInfo;
				}
			}
			
			
		}
		else if (!this.usepagine) // 按主键范围切割数据，可能导致数据不均匀
		{
			segments = new TaskInfo[this.datablocks];
			if (this.startid == -9999 || this.endid == -9999) {
				StringBuilder limitsql = new StringBuilder();
				if (this.limitstatement == null
						|| this.limitstatement.equals("")) {

					String tableName = null;
					if (this.schema == null) {
						tableName = this.tablename;
					} else
						tableName = this.schema + "." + this.tablename;
					limitsql.append("select min(").append(this.pkName)
							.append(") as startid,max(").append(this.pkName)
							.append(") as endid from ").append(tableName);
					this.limitstatement = limitsql.toString();
				}

				log.info("Admin Data Node evaluate data scope for job["
						+ this.jobname + "] use pk[" + this.pkName
						+ "] partitions:" + limitstatement + " on datasource "
						+ dbname);
				PreparedDBUtil db = new PreparedDBUtil();
				db.preparedSelect(this.dbname, limitstatement);
				db.executePrepared();
				if (db.size() > 0) {

					startid = db.getLong(0, "startid");
					endid = db.getLong(0, "endid");
				}
			}
			if (this.startid != -9999 && this.endid != -9999) {
				log.info("PK[" + this.pkName + "] partition job["
						+ this.jobname + "]  base infomation:start data id="
						+ startid + ",endid=" + endid + ",datablocks="
						+ datablocks);
				long datas = endid - startid + 1;

				// segments[this.workservers-1] = segments[this.workservers-1] +
				// div;

				// 构建所有的处理任务

				if (datas > datablocks) {
					segement = datas / this.datablocks;

					long div = datas % this.datablocks;

					spiltTask_(segments, startid, endid, this.datablocks,
							segement, div, usepagine, filebasename, null);
				} else // 数据量小于块数，那么直接按一块数据进行处理，不需要进行分块处理
				{
					TaskInfo task = new TaskInfo();
					task.startoffset = startid;
					task.endoffset = endid;
					task.pagesize = datas;
					task.filename = filebasename + "_0";
					task.taskNo = "0";
					segement = datas;
					segments = new TaskInfo[1];
					segments[0] = task;
				}
			} else {
				log.info("PK[" + this.pkName + "] partition job["
						+ this.jobname + "]  base infomation:start data id="
						+ startid + ",endid=" + endid + ",datablocks="
						+ datablocks + ",没有数据需要上传，任务结束.");
				return null;
			}
		} 
		else // 按分页方式切割任务
		{
			segments = new TaskInfo[this.datablocks];
			if (this.tablerows <= 0) {
				StringBuilder countsql = new StringBuilder();
				if (this.countstatement == null
						|| this.countstatement.equals("")) {

					String tableName = null;
					if (this.schema == null) {
						tableName = this.tablename;
					} else
						tableName = this.schema + "." + this.tablename;
					countsql.append("select count(1) as tablerows from ")
							.append(tableName);
					this.countstatement = countsql.toString();
				}

				PreparedDBUtil db = new PreparedDBUtil();
				db.preparedSelect(this.dbname, countstatement);
				db.executePrepared();
				tablerows = db.getLong(0, 0);
			}

			long datas = tablerows;
			if (datas == 0) {
				log.info("Pagine job[" + this.jobname + "] end:tablerows="
						+ datas + ",datablocks=" + datablocks + ",没有需要上传的数据！");
				return null;
			}

			log.info("Pagine job[" + this.jobname
					+ "] base infomation:tablerows=" + datas + ",datablocks="
					+ datablocks);
			// segments[this.workservers-1] = segments[this.workservers-1] +
			// div;

			// 构建所有的处理任务

			if (datas > datablocks) {
				segement = datas / this.datablocks;

				long div = datas % this.datablocks;
				spiltTask_(segments, 0, 0, this.datablocks, segement, div,
						usepagine, filebasename, null);
			} else {
				TaskInfo task = new TaskInfo();
				task.startoffset = 0;

				task.pagesize = datas;
				task.filename = filebasename + "_0";
				task.taskNo = "0";
				segement = datas;
				segments = new TaskInfo[1];
				segments[0] = task;
			}
		}
		//start
//		List<LinkTasks> temp = null;
//		List<TaskInfo> retTasks = null;
//		if (this.blocks != null && this.blocks.length > 0)// 如果指定了任务块，则只执行指定的任务块
//		{
//			temp = new ArrayList<LinkTasks>(blocks.length);
//			int blockslen = this.blocks.length;
//			for (int i = 0; i < blockslen; i++) {
//				if (blocks[i] < segments.length) {
//					LinkTasks linkTasks = new LinkTasks();
//					linkTasks.block = blocks[i];
//					linkTasks.taskInfo = segments[blocks[i]];
//					temp.add(linkTasks);
//
//				}
//			}
//			if (temp.size() > 0) {
//				if (this.subblocks > 0)// 如果需要对子任务进行切分，则需要进一步切分子任务，并且需要清除原来的大块数据对应的hdfs文件
//				{
//					// 进一步切分子任务
//					List<TaskInfo> subchunks = new ArrayList<TaskInfo>();
//					for (int i = 0; i < temp.size(); i++) {
//						LinkTasks linkTasks = temp.get(i);
//						TaskInfo t = linkTasks.taskInfo;
//						SplitTasks subsplitTasks = buildJobSubChunks(t);
//						if (subsplitTasks == null) {
//							subchunks.add(t);
//						} else {
//							segement = subsplitTasks.segement;
//							List<Integer> filters = this.blocksplits
//									.get(linkTasks.block + "");// 指定了要过滤的数据块
//							if (filters != null && filters.size() > 0) {
//								List<TaskInfo> subsgs = new ArrayList<TaskInfo>(
//										filters.size());
//								TaskInfo[] sgs = subsplitTasks.segments;
//								for (int j = 0; j < filters.size(); j++) {
//									int pos = filters.get(j).intValue();
//									if (pos < sgs.length) {
//										subsgs.add(sgs[pos]);
//									}
//								}
//								subchunks.addAll(subsgs);
//							} else {
//								subchunks.addAll(Arrays
//										.asList(subsplitTasks.segments));
//							}
//
//						}
//					}
//					retTasks = subchunks;
//
//				} else {
//					retTasks = new ArrayList<TaskInfo>();
//					for (int i = 0; i < temp.size(); i++) {
//						LinkTasks linkTasks = temp.get(i);
//						retTasks.add(linkTasks.taskInfo);
//					}
//				}
//
//				segments = new TaskInfo[retTasks.size()];
//
//				retTasks.toArray(segments);
//
//			}
//		} else if (this.excludeblocks != null && this.excludeblocks.length > 0)// 排除要清除的数据块
//		{
//			temp = new ArrayList<LinkTasks>();
//			int blockslen = this.excludeblocks.length;
//			for (int i = 0; i < blockslen; i++) {// 计算有效的排除块
//				if (excludeblocks[i] < segments.length) {
//					LinkTasks linkTasks = new LinkTasks();
//					linkTasks.block = excludeblocks[i];
//					linkTasks.taskInfo = segments[linkTasks.block];
//					temp.add(linkTasks);
//
//				}
//			}
//			if (temp.size() > 0) {
//				if (this.subblocks > 0)// 如果需要对子任务进行切分，则需要进一步切分子任务，并且需要清除原来的大块数据对应的hdfs文件
//				{
//					// 进一步切分子任务
//					List<TaskInfo> subchunks = new ArrayList<TaskInfo>();
//					for (int j = 0; j < segments.length; j++) {
//						SplitTasks subsplitTasks = buildJobSubChunks(segments[j]);
//						if (subsplitTasks != null)
//							segement = subsplitTasks.segement;
//						boolean isexclude = false;
//						for (int i = 0; i < temp.size(); i++) {
//							LinkTasks linkTasks = temp.get(i);
//							if (linkTasks.block == j)// 不是排除的块，需要重新抽取
//							{
//								isexclude = true;
//								break;
//							}
//						}
//						if (!isexclude) {
//							if (subsplitTasks == null) {
//								subchunks.add(segments[j]);
//							} else {
//								subchunks.addAll(Arrays
//										.asList(subsplitTasks.segments));
//							}
//						} else {
//							if (subsplitTasks == null)// 没有子块，整块排除
//							{
//
//							} else // 有子块，判断要排除的子块
//							{
//								List<Integer> excludes = this.excludeblocksplits
//										.get(j + "");
//								if (excludes == null)// 没有自定子块，整块的所有子块都排除
//								{
//
//								} else // 识别子块中要排除的块，将不需要排除的块添加到任务列表中
//								{
//
//									for (int k = 0; k < subsplitTasks.segments.length; k++) {
//										boolean issubexclude = false;
//										for (Integer epos : excludes) {
//											if (k == epos.intValue()) {
//												issubexclude = true;
//												break;
//											}
//
//										}
//										if (!issubexclude)
//											subchunks
//													.add(subsplitTasks.segments[k]);
//									}
//
//								}
//
//							}
//						}
//
//					}
//					retTasks = subchunks;
//
//				} else {
//					retTasks = new ArrayList<TaskInfo>();
//					for (int j = 0; j < segments.length; j++) {
//						boolean isexclude = false;
//						for (int i = 0; i < temp.size(); i++) {
//							LinkTasks linkTasks = temp.get(i);
//							if (linkTasks.block == j) {
//								isexclude = true;
//							}
//						}
//						if (!isexclude)
//							retTasks.add(segments[j]);
//					}
//				}
//
//				segments = new TaskInfo[retTasks.size()];
//
//				retTasks.toArray(segments);
//
//			}
//		}
//		splitTasks.segement = segement;
//		splitTasks.segments = segments;
		//end
		splitTasks = filterTasks(segments ,segement);
		return splitTasks;
	}

	private List<String> deleteParentBlockHDFS;

	/**
	 * 如果指定了子数据块，则对子数据块进行进一步任务切分
	 */
	SplitTasks buildJobSubChunks(TaskInfo taskInfo) {

		SplitTasks splitTasks = new SplitTasks();
		TaskInfo[] segments = new TaskInfo[this.subblocks];
		long segement = 0l;
		long startid = taskInfo.startoffset;
		long datas = taskInfo.pagesize;
		long endid = startid + datas - 1;
		if (deleteParentBlockHDFS == null)
			deleteParentBlockHDFS = new ArrayList<String>();
		if (!this.usepagine) {

			if (datas > subblocks) {
				segement = datas / subblocks;

				long div = datas % this.subblocks;

				deleteParentBlockHDFS.add(taskInfo.filename);
				spiltTask_(segments, startid, endid, subblocks, segement, div,
						usepagine, taskInfo.filename, taskInfo.taskNo);
				splitTasks.segement = segement;
				splitTasks.segments = segments;
				return splitTasks;
			} else
				return null;
		} else {
			if (datas > subblocks) {
				segement = datas / this.subblocks;

				long div = datas % this.subblocks;
				deleteParentBlockHDFS.add(taskInfo.filename);
				spiltTask_(segments, startid, endid, subblocks, segement, div,
						usepagine, taskInfo.filename, taskInfo.taskNo);
				splitTasks.segement = segement;
				splitTasks.segments = segments;
				return splitTasks;
			} else
				return null;
		}

	}

	public void deleteHDFSFile(String filename) throws Exception {
		Path hdfsdatafile = new Path(this.hdfsdatadir, filename);
		this.fileSystem.delete(hdfsdatafile, true);
	}

	void buildJobChunks() throws Exception {
		Address locaAddress = EventUtils.getLocalAddress();
		List<Address> allAddress = EventUtils.getRPCAddresses();
		if (locaAddress == null) {
			workservers = 1;
			rundirect = true;
		} else {
			workservers = allAddress.size();
			if (!this.adminnodeasdatanode) {
				workservers = workservers - 1;// admin节点不处理数据，只做调度
				if (workservers == 0) {
					workservers = 1;
					rundirect = true;
				} else {
					allAddress = EventUtils.removeSelf(allAddress);
				}
			}

			log.info("Data Node for job[" + this.jobname + "] workservers="
					+ workservers);
		}
		SplitTasks splitTasks = spiltTask();// 切分数据块
		if (splitTasks == null || splitTasks.segments == null)
		{
			TaskConfig config = buildTaskConfig(jobname);
			JobStatic jobStatic = new JobStatic();
			String jobstaticid = java.util.UUID.randomUUID().toString();
			jobStatic.setJobstaticid(jobstaticid);
			jobStatic.setConfig(config.toString());
			jobStatic.setStatus(1);
			jobStatic.setStartTime(System.currentTimeMillis());
			jobStatic.setEndTime(System.currentTimeMillis());
			jobStatic.setJobname(jobname);
			jobStatic.setErrormsg("没有需要处理的数据");
			 
			Imp.getImpStaticManager().addJobStatic(jobStatic);
			return;
		}
		TaskInfo[] segments = splitTasks.segments;
		long segement = splitTasks.segement;
		if (this.deleteParentBlockHDFS != null
				&& deleteParentBlockHDFS.size() > 0)// 删除大块数据对应的hdfs文件，因为对应的大块数据文件已经被切分为更小的hdfs数据块文件
		{
			for (String filename : deleteParentBlockHDFS) {
				deleteHDFSFile(filename);
			}
		}
		String jobstaticid = java.util.UUID.randomUUID().toString();
		// 将任务分给所有的服务器处理
		if (!this.rundirect) {
			tasks = new HashMap<String, TaskConfig>();
			int alltasks = segments.length;
			int servertasks = alltasks / workservers;
			int taskdiv = alltasks % workservers;
			for (int i = 0; i < workservers && i < alltasks; i++) {
				TaskConfig config = buildTaskConfigWithID(jobstaticid);

				config.setPagesize(segement);
				if (i < taskdiv) {

					TaskInfo task[] = new TaskInfo[servertasks + 1];
					System.arraycopy(segments, i * servertasks + i, task, 0,
							servertasks + 1);
					config.setTasks(Arrays.asList(task));

				} else {

					TaskInfo task[] = new TaskInfo[servertasks];
					System.arraycopy(segments, i * servertasks + taskdiv, task,
							0, servertasks);
					config.setTasks(Arrays.asList(task));

				}

				tasks.put(allAddress.get(i).toString(), config);

			}
			Event<Map<String, TaskConfig>> event = new EventImpl<Map<String, TaskConfig>>(
					tasks, hdfsuploadevent);
			/**
			 * 消息以异步方式传递
			 */

			EventHandle.getInstance().change(event, false);
		} else {
			TaskConfig config =buildTaskConfigWithID(jobstaticid);

			config.setPagesize(segement);
			config.setTasks(Arrays.asList(segments));
			tasks = new HashMap<String, TaskConfig>();
			tasks.put("rundirect", config);
			Event<Map<String, TaskConfig>> event = new EventImpl<Map<String, TaskConfig>>(
					tasks, hdfsuploadevent, Event.LOCAL);
			EventHandle.getInstance().change(event, false);
		}

	}

	private void initJob(BaseApplicationContext context, String jobname) {

		this.localpath = context.getStringExtendAttribute(jobname, "localdir");
		this.deletefiles = context.getStringExtendAttribute(jobname,
				"deletefiles");
		stopdbnames = context.getStringExtendAttribute(jobname, "stopdbnames");
		reassigntaskNode = context.getStringExtendAttribute(jobname,
				"reassigntaskNode");
		
		reassigntaskJobname = context.getStringExtendAttribute(jobname,
				"reassigntaskJobname");

		this.hdfsdatadir = context.getStringExtendAttribute(jobname,
				"hdfsdatadir");
		this.dbname = context.getStringExtendAttribute(jobname, "dbname");
		this.hdfsserver = context.getStringExtendAttribute(jobname,
				"hdfsserver");
		this.tablename = context.getStringExtendAttribute(jobname, "tablename");
		this.schema = context.getStringExtendAttribute(jobname, "schema");
		this.pkName = context.getStringExtendAttribute(jobname, "pkname");
		this.columns = context.getStringExtendAttribute(jobname, "columns");
		this.datablocks = context.getIntExtendAttribute(jobname, "datablocks");

		geneworkthreads = context.getIntExtendAttribute(jobname,
				"geneworkthreads", 20);
		uploadeworkthreads = context.getIntExtendAttribute(jobname,
				"uploadeworkthreads", 20);
		genqueques = context.getIntExtendAttribute(jobname, "genqueques", 5);
		uploadqueues = context
				.getIntExtendAttribute(jobname, "uploadqueues", 5);
		genquequetimewait = context.getIntExtendAttribute(jobname,
				"genquequetimewait", 10);
		uploadqueuetimewait = context.getIntExtendAttribute(jobname,
				"uploadqueuetimewait", 10);
		this.datatype = context.getStringExtendAttribute(jobname, "datatype",
				"json");
		this.genlocalfile = context.getBooleanExtendAttribute(jobname,
				"genlocalfile", false);
		this.filebasename = context.getStringExtendAttribute(jobname,
				"filebasename", tablename);
		this.clearhdfsfiles = context.getBooleanExtendAttribute(jobname,
				"clearhdfsfiles", false);

		this.tablerows = context
				.getLongExtendAttribute(jobname, "tablerows", 0);
		this.usepagine = context.getBooleanExtendAttribute(jobname,
				"usepagine", false);
		this.limitstatement = context.getStringExtendAttribute(jobname,
				"limitstatement");
		this.querystatement = context.getStringExtendAttribute(jobname,
				"querystatement");
		this.countstatement = context.getStringExtendAttribute(jobname,
				"countstatement");
		this.pageinestatement = context.getStringExtendAttribute(jobname,
				"pageinestatement");
		this.adminnodeasdatanode = Imp.getImpStaticManager()
				.isAdminasdatanode();
		this.jobname = jobname;
		this.startid = context
				.getLongExtendAttribute(jobname, "startid", -9999);
		this.endid = context.getLongExtendAttribute(jobname, "endid", -9999);

		blocks_str = context.getStringExtendAttribute(jobname, "blocks");

		LinkBlocks linkBlocks = buildLinkBlocks(blocks_str);
		blocks = linkBlocks.blocks;
		blocksplits = linkBlocks.blocksplits;
		excludeblocks_str = context.getStringExtendAttribute(jobname,
				"excludeblocks");
		linkBlocks = buildLinkBlocks(excludeblocks_str);
		excludeblocks = linkBlocks.blocks;
		excludeblocksplits = linkBlocks.blocksplits;
		subblocks = context.getIntExtendAttribute(jobname, "subblocks", -1);

		driver = context.getStringExtendAttribute(jobname, "driver");
		dburl = context.getStringExtendAttribute(jobname, "dburl");
		dbpassword = context
				.getStringExtendAttribute(jobname, "dbpassword", "");
		dbuser = context.getStringExtendAttribute(jobname, "dbuser", "");
		readOnly = context.getStringExtendAttribute(jobname, "readOnly");

		validatesql = context.getStringExtendAttribute(jobname, "validatesql",
				"");
		usepool = context.getBooleanExtendAttribute(jobname, "usepool", false);
		usepartition = context.getBooleanExtendAttribute(jobname,
				"usepartition",false);
		partitions = context.getStringExtendAttribute(jobname,
				"partitions");
		excludepartitions = context.getStringExtendAttribute(jobname,
				"excludepartitions");
		usesubpartition = context.getBooleanExtendAttribute(jobname,
				"usesubpartition",true);
		
		subtablename = context.getStringExtendAttribute(jobname,
				"subtablename");
		  leftJoinby = context.getStringExtendAttribute(jobname,
				"leftJoinby");
		  rightJoinby = context.getStringExtendAttribute(jobname,
				"rightJoinby");
			 subquerystatement = context.getStringExtendAttribute(jobname,
					"subquerystatement");
			 this.target = context.getStringExtendAttribute(jobname,
						"target");
			 errorrowslimit = context.getIntExtendAttribute(jobname,
						"errorrowslimit",-1);
			 this.onejob = context.getBooleanExtendAttribute(jobname,
						"single",false); 	 
			 this.rowsperfile = context.getIntExtendAttribute(jobname, "rowsperfile", 0);
			 startfileNo = context.getIntExtendAttribute(jobname, "startfileNo", -1);
			 
		this.fileSystem = HDFSServer.getFileSystem(hdfsserver);
	}

	public void executeJob(String jobname) throws Exception {
		BaseApplicationContext context = DefaultApplicationContext
				.getApplicationContext("tasks.xml");
		if (context.containsBean(jobname)) {
			executeJob(context, jobname);
		} else {
			DBJob dbjob = DBHelper.getDBJob(jobname);
			if (dbjob == null) {
				throw new Exception("作业" + jobname + "未定义!");
			}
			context = new SOAApplicationContext(dbjob.getJobdef());
			executeJob(context, jobname);
		}

	}

	public void deleteData(String jobname) throws Exception {
		BaseApplicationContext context = DefaultApplicationContext
				.getApplicationContext("tasks.xml");
		initJob(context, jobname);
		Path path = new Path(hdfsdatadir);
		fileSystem.delete(path, true);

		log.info("删除作业hdfs 数据文件[jobname=" + jobname + "],[hdfsdatadir="
				+ hdfsdatadir + "] on hdfsserver[" + hdfsserver + "],"
				+ "[localdir=" + localpath + "],[dbname=" + dbname
				+ "],[tablename=" + tablename + "]," + "[schema=" + schema
				+ "],[pkName=" + pkName + "],[columns=" + columns
				+ "],[datablocks=" + datablocks + "]成功.");
	}

	public String getQuerystatement() {
		return querystatement;
	}

	public void setQuerystatement(String querystatement) {
		this.querystatement = querystatement;
	}

	public String getLimitstatement() {
		return limitstatement;
	}

	public void setLimitstatement(String limitstatement) {
		this.limitstatement = limitstatement;
	}

	public String getJobname() {
		return jobname;
	}

	public void setJobname(String jobname) {
		this.jobname = jobname;
	}

	private void doDeleteFiles() throws Exception {
		JobStatic jobStatic = new JobStatic();
		String jobstaticid = java.util.UUID.randomUUID().toString();
		jobStatic.setJobstaticid(jobstaticid);
		jobStatic.setStartTime(System.currentTimeMillis());
		String[] deletefiles_ = deletefiles.split("\\,");
		for (String file : deletefiles_) {
			Path path = new Path(file);
			fileSystem.delete(path, true);
		}
		String info = "删除作业hdfs 数据文件[jobname=" + jobname + "],[deletefiles="
				+ deletefiles + "] on hdfsserver[" + hdfsserver + "]";

		jobStatic.setConfig(info);
		jobStatic.setStatus(1);
		jobStatic.setEndTime(System.currentTimeMillis());
		jobStatic.setJobname(jobname);

		Imp.getImpStaticManager().addJobStatic(jobStatic);
		log.info(info + "成功.");
	}

	private void doStopdbnames() throws Exception {
		String jobstaticid = java.util.UUID.randomUUID().toString();
		StopDS stopds = new StopDS();
		stopds.setJobname(this.jobname);
		stopds.setStopdbnames(stopdbnames);
		stopds.setJobstaticid(jobstaticid);
		Event<StopDS> event = new EventImpl<StopDS>(stopds,
				hdfs_upload_monitor_stopdatasource_commond);
		/**
		 * 消息以异步方式传递
		 */

		EventHandle.getInstance().change(event, false);
		log.info("提交停止数据源[" + this.stopdbnames + "]作业请求成功.");
	}

	
	/**
	 * 重新分派任务：排除正在运行和队列中等待处理的任务，其他未执行的任务将被重新分配给其他空闲节点处理
	 * 
	 * @throws Exception
	 */
	private void doReassignTasks() throws Exception {

		SpecialMonitorObject monitorJob = Imp.getImpStaticManager()
				.getSpecialMonitorObject(reassigntaskJobname);
		Map<String, JobStatic> hostJobs = monitorJob.getJobstaticsIdxByHost();
		String jobstaticid = java.util.UUID.randomUUID().toString();
		JobStatic jobstatic = hostJobs.get(this.reassigntaskNode);
		if (jobstatic.getUnruntasks() == 0  ) {
			JobStatic result = new JobStatic();
			
			result.setJobstaticid(jobstaticid);
			result.setStartTime(System.currentTimeMillis());
			result.setStatus(1);
			result.setConfig(new StringBuilder().append("reassigntaskNode=").append(reassigntaskNode).append("reassigntaskJobname=").append(this.reassigntaskJobname).toString());
			String msg = new StringBuilder().append("提交重新分派数据节点[").append( this.reassigntaskNode ).append( "]作业["
					).append( reassigntaskJobname ).append( "]任务结束：所有作业已经被分派，无法重新进行任务分派.").toString();
			result.setErrormsg(msg);
			result.setJobname(jobname);
			result.setEndTime(System.currentTimeMillis());
			Imp.getImpStaticManager().addJobStatic(result);
			log.info(msg);
			return;
		} else {
			Address address = EventUtils.getAddress(reassigntaskNode);
			if(address== null)
			{
				JobStatic result = new JobStatic();
				 
				result.setJobstaticid(jobstaticid);
				result.setStartTime(System.currentTimeMillis());
				result.setStatus(1);
				result.setConfig("reassigntaskNode=" + reassigntaskNode);
				String msg = "提交重新分派数据节点[" + this.reassigntaskNode + "]作业["
						+ reassigntaskJobname + "]任务结束：任务所在服务器["+reassigntaskNode+"]不存在，无法重新进行任务分派.";
				result.setErrormsg(msg);
				result.setJobname(jobname);
				result.setEndTime(System.currentTimeMillis());
				Imp.getImpStaticManager().addJobStatic(result);
				log.info(msg);
				return;
			}
			ReassignTask reassignTask = new ReassignTask();
			reassignTask.setJobname(jobname);
			reassignTask.setReassigntaskNode(this.reassigntaskNode);
			reassignTask.setReassigntaskJobname(reassigntaskJobname);
			reassignTask.setAdminasdatanode(Imp.getImpStaticManager().isAdminasdatanode());
			reassignTask.setAdminnode(Imp.getImpStaticManager().getLocalNode());
			reassignTask.setJobstaticid(jobstaticid);
			Map<String, Integer> taskinfos = new HashMap<String, Integer>();// 存放其他节点正在处理和排队等待执行的任务数，作为重新分派任务的参考数据
			for (Map.Entry<String, JobStatic> entry : hostJobs.entrySet()) {
				String host = entry.getKey();
				
				if(!host.equals(reassigntaskNode))//忽略任务所属节点
				{
					if(host.equals(reassignTask.getAdminnode()))
					{
						if (reassignTask.isAdminasdatanode()) //忽略非数据节点的管理节点
						{
							JobStatic other = entry.getValue();
							int unhandletasks = other.getWaittasks()
									+ other.getRuntasks() + other.getUnruntasks();
							taskinfos.put(host, unhandletasks);
						}
					}
					else
					{
						JobStatic other = entry.getValue();
						int unhandletasks = other.getWaittasks()
								+ other.getRuntasks() + other.getUnruntasks();
						taskinfos.put(host, unhandletasks);
					}
					
				}
			}
			reassignTask.setHostTaskInfos(taskinfos);
			
			EventTarget target = new EventTarget(address);
			Event<ReassignTask> event = new EventImpl<ReassignTask>(
					reassignTask, hdfs_upload_monitor_reassigntasks_request_commond,target);
			/**
			 * 消息以异步方式传递
			 */

			EventHandle.getInstance().change(event, false);
			log.info("提交重新分派数据节点[" + this.reassigntaskNode + "]作业[" + reassigntaskJobname
					+ "]任务请求成功.");
		}
	}

	private void doUploadData() throws Exception {
		if (genlocalfile) {
			File file = new File(localpath);
			if (!file.exists())
				file.mkdirs();
		}

		Path path = new Path(hdfsdatadir);
		if (!fileSystem.exists(path))
			fileSystem.mkdirs(path);
		else {
			if (clearhdfsfiles)// 如果指定了特定数据块任务，则不删除hdfs文件目录
			{
				if ((excludeblocks == null || excludeblocks.length == 0)
						&& (blocks == null || blocks.length == 0)) {
					fileSystem.delete(path, true);
					fileSystem.mkdirs(path);
				}
			}
		}
		DBHelper.initDB(this);
		if(!this.onejob)
		{
			buildJobChunks();
		}
		else
		{
			doOnejob();
		}
		log.info("启动数据上传任务[jobname=" + jobname + "],[hdfsdatadir="
				+ hdfsdatadir + "] on hdfsserver[" + hdfsserver + "],"
				+ "[localdir=" + localpath + "],[dbname=" + dbname
				+ "],[tablename=" + tablename + "]," + "[schema=" + schema
				+ "],[pkName=" + pkName + "],[columns=" + columns
				+ "],[datablocks=" + datablocks + "]成功.");
	}
	
	private void doOnejob()
	{
		String jobstaticid = java.util.UUID.randomUUID().toString();
		TaskConfig config = buildTaskConfigWithID(jobstaticid);
		tasks = new HashMap<String, TaskConfig>();
		if(SimpleStringUtil.isEmpty(target))
		{
			if(Imp.getImpStaticManager().isAdminasdatanode())
			{
				tasks.put("rundirect", config);
				Event<Map<String, TaskConfig>> event = new EventImpl<Map<String, TaskConfig>>(
						tasks, hdfsuploadevent, Event.LOCAL);
				EventHandle.getInstance().change(event, false);
			}
			else
			{
				List<Address> ads = EventUtils.getRPCAddresses();
				Address local = EventUtils.getLocalAddress();
				Address tgt = null;
				for(int i =0; i < ads.size(); i ++)
				{
					Address tmp = ads.get(i);
					if(!tmp.toString().equals(local.toString()))
					{
						tgt = tmp;
						break;
					}
				}
				if(tgt != null)
				{
					tasks.put(tgt.toString(), config);
					Address address = tgt;
					EventTarget target = new EventTarget(address);
					Event<Map<String, TaskConfig>> event = new EventImpl<Map<String, TaskConfig>>(
							tasks, hdfsuploadevent,target);
					EventHandle.getInstance().change(event, false);
				}
				else
				{
					JobStatic jobStatic = new JobStatic();
					
					jobStatic.setJobstaticid(jobstaticid);
					jobStatic.setStartTime(System.currentTimeMillis());
					 
					

					jobStatic.setConfig(config.toString());
					jobStatic.setStatus(2);
					jobStatic.setEndTime(System.currentTimeMillis());
					jobStatic.setJobname(jobname);
					jobStatic.setErrormsg("管理节点不能作业未数据处理节点，同时没有指定数据作业节点");
					Imp.getImpStaticManager().addJobStatic(jobStatic);
					log.info(jobname+"作业执行完毕.");
				}
			}
		}
		else
		{
			tasks.put(target, config);
			Address address = EventUtils.getAddress(this.target);
			EventTarget target = new EventTarget(address);
			Event<Map<String, TaskConfig>> event = new EventImpl<Map<String, TaskConfig>>(
					tasks, hdfsuploadevent,target);
			EventHandle.getInstance().change(event, false);
		}
		
	}

	private void runjob(BaseApplicationContext ioccontext, String jobname) {
		long start = System.currentTimeMillis();
		try {
			initJob(ioccontext, jobname);
			if (this.deletefiles != null && !this.deletefiles.trim().equals(""))// 如果是删除文件指令，则删除文件
			{
				doDeleteFiles();
			} else if (this.stopdbnames != null
					&& !this.stopdbnames.trim().equals("")) {
				this.doStopdbnames();
			} else if (this.reassigntaskNode != null
					&& !this.reassigntaskNode.trim().equals("")) {
				this.doReassignTasks();
			} 
			else // 如果是上传数据指令，则上传数据
			{
				doUploadData();
			}
		} catch (IllegalArgumentException e) {
			TaskConfig config = buildTaskConfig(jobname);
			JobStatic jobStatic = new JobStatic();	
			String jobstaticid = java.util.UUID.randomUUID().toString();
			jobStatic.setJobstaticid(jobstaticid);
			jobStatic.setConfig(config.toString());
			jobStatic.setStatus(2);
			jobStatic.setStartTime(start);
			jobStatic.setEndTime(System.currentTimeMillis());
			jobStatic.setJobname(jobname);
			jobStatic.setErrormsg(SimpleStringUtil.exceptionToString(e));
			Imp.getImpStaticManager().addJobStatic(jobStatic);
			log.error("", e);
		} catch (IOException e) {
			TaskConfig config = buildTaskConfig(jobname);
			JobStatic jobStatic = new JobStatic();
			String jobstaticid = java.util.UUID.randomUUID().toString();
			jobStatic.setJobstaticid(jobstaticid);
			jobStatic.setConfig(config.toString());
			jobStatic.setStatus(2);
			jobStatic.setStartTime(start);
			jobStatic.setEndTime(System.currentTimeMillis());
			jobStatic.setJobname(jobname);
			jobStatic.setErrormsg(SimpleStringUtil.exceptionToString(e));
			Imp.getImpStaticManager().addJobStatic(jobStatic);
			log.error("", e);
		} catch (Exception e) {
			TaskConfig config = buildTaskConfig(jobname);
			JobStatic jobStatic = new JobStatic();
			String jobstaticid = java.util.UUID.randomUUID().toString();
			jobStatic.setJobstaticid(jobstaticid);
			jobStatic.setConfig(config.toString());
			jobStatic.setStatus(2);
			jobStatic.setStartTime(start);
			jobStatic.setEndTime(System.currentTimeMillis());
			jobStatic.setJobname(jobname);
			jobStatic.setErrormsg(SimpleStringUtil.exceptionToString(e));
			Imp.getImpStaticManager().addJobStatic(jobStatic);
			log.error("", e);
		}
	}

	public void executeJob(final BaseApplicationContext ioccontext,
			final String jobname) throws Exception {
		new Thread(new Runnable() {
			public void run() {
				runjob(ioccontext, jobname);
			}
		}).start();

	}

	public String getDriver() {
		return driver;
	}

	public void setDriver(String driver) {
		this.driver = driver;
	}

	public String getDburl() {
		return dburl;
	}

	public void setDburl(String dburl) {
		this.dburl = dburl;
	}

	public String getDbpassword() {
		return dbpassword;
	}

	public void setDbpassword(String dbpassword) {
		this.dbpassword = dbpassword;
	}

	public String getDbuser() {
		return dbuser;
	}

	public void setDbuser(String dbuser) {
		this.dbuser = dbuser;
	}

	public String getValidatesql() {
		return validatesql;
	}

	public void setValidatesql(String validatesql) {
		this.validatesql = validatesql;
	}

	public boolean isUsepool() {
		return usepool;
	}

	public void setUsepool(boolean usepool) {
		this.usepool = usepool;
	}

	public String getHADOOP_PATH() {
		return HADOOP_PATH;
	}

	public FileSystem getFileSystem() {
		return fileSystem;
	}

	public String getHdfsserver() {
		return hdfsserver;
	}

	public String getHdfsdatadir() {
		return hdfsdatadir;
	}

	public String getLocalpath() {
		return localpath;
	}

	public String getTablename() {
		return tablename;
	}

	public String getSchema() {
		return schema;
	}

	public String getPkName() {
		return pkName;
	}

	public String getColumns() {
		return columns;
	}

	public String getFilebasename() {
		return filebasename;
	}

	public int getDatablocks() {
		return datablocks;
	}

	public String getDbname() {
		return dbname;
	}

	public int getWorkservers() {
		return workservers;
	}

	public boolean isRundirect() {
		return rundirect;
	}

	public int getGeneworkthreads() {
		return geneworkthreads;
	}

	public int getUploadeworkthreads() {
		return uploadeworkthreads;
	}

	public int getGenqueques() {
		return genqueques;
	}

	public int getUploadqueues() {
		return uploadqueues;
	}

	public int getGenquequetimewait() {
		return genquequetimewait;
	}

	public int getUploadqueuetimewait() {
		return uploadqueuetimewait;
	}

	public long getStartid() {
		return startid;
	}

	public long getEndid() {
		return endid;
	}

	public String getDatatype() {
		return datatype;
	}

	public boolean isGenlocalfile() {
		return genlocalfile;
	}

	public boolean isClearhdfsfiles() {
		return clearhdfsfiles;
	}

	public String getCountstatement() {
		return countstatement;
	}

	public String getPageinestatement() {
		return pageinestatement;
	}

	public long getTablerows() {
		return tablerows;
	}

	public boolean isUsepagine() {
		return usepagine;
	}

	public boolean isAdminnodeasdatanode() {
		return adminnodeasdatanode;
	}

	public int[] getBlocks() {
		return blocks;
	}

	public int getSubblocks() {
		return subblocks;
	}

	public Map<String, TaskConfig> getTasks() {
		return tasks;
	}

	public List<String> getDeleteParentBlockHDFS() {
		return deleteParentBlockHDFS;
	}

	public Map<String, List<Integer>> getBlocksplits() {
		return blocksplits;
	}

	public String isReadOnly() {
		// TODO Auto-generated method stub
		return readOnly;
	}

	public String getDeletefiles() {
		return deletefiles;
	}

	public void setDeletefiles(String deletefiles) {
		this.deletefiles = deletefiles;
	}

	public String getStopdbnames() {
		return stopdbnames;
	}

	public void setStopdbnames(String stopdbnames) {
		this.stopdbnames = stopdbnames;
	}

	public String getReassigntaskNode() {
		return reassigntaskNode;
	}

	public void setReassigntaskNode(String reassigntaskNode) {
		this.reassigntaskNode = reassigntaskNode;
	}

	public String getReassigntaskJobname() {
		return reassigntaskJobname;
	}

	public void setReassigntaskJobname(String reassigntaskJobname) {
		this.reassigntaskJobname = reassigntaskJobname;
	}

	public String getSubtablename() {
		return subtablename;
	}

	public void setSubtablename(String subtablename) {
		this.subtablename = subtablename;
	}

	public boolean isUsepartition() {
		return usepartition;
	}

	public void setUsepartition(boolean usepartition) {
		this.usepartition = usepartition;
	}

	

	public String getSubquerystatement() {
		return subquerystatement;
	}

	public void setSubquerystatement(String subquerystatement) {
		this.subquerystatement = subquerystatement;
	}

	public String getLeftJoinby() {
		return leftJoinby;
	}

	public void setLeftJoinby(String leftJoinby) {
		this.leftJoinby = leftJoinby;
	}

	public String getRightJoinby() {
		return rightJoinby;
	}

	public void setRightJoinby(String rightJoinby) {
		this.rightJoinby = rightJoinby;
	}

	public boolean isOnejob() {
		return onejob;
	}

	public void setOnejob(boolean onejob) {
		this.onejob = onejob;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public int getRowsperfile() {
		return rowsperfile;
	}

	public void setRowsperfile(int rowsperfile) {
		this.rowsperfile = rowsperfile;
	}

	public String getPartitions() {
		return partitions;
	}

	public void setPartitions(String partitions) {
		this.partitions = partitions;
	}

	public String getExcludepartitions() {
		return excludepartitions;
	}

	public void setExcludepartitions(String excludepartitions) {
		this.excludepartitions = excludepartitions;
	}

	public boolean isUsesubpartition() {
		return usesubpartition;
	}

	public void setUsesubpartition(boolean usesubpartition) {
		this.usesubpartition = usesubpartition;
	}

}
