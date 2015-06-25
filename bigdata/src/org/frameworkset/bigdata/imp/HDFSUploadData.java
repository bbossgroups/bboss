package org.frameworkset.bigdata.imp;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.log4j.Logger;
import org.frameworkset.bigdata.imp.monitor.ImpStaticManager;
import org.frameworkset.bigdata.imp.monitor.JobStatic;
import org.frameworkset.bigdata.util.DBHelper;
import org.frameworkset.bigdata.util.DBJob;
import org.frameworkset.event.Event;
import org.frameworkset.event.EventHandle;
import org.frameworkset.event.EventImpl;
import org.frameworkset.event.SimpleEventType;
import org.frameworkset.remote.EventUtils;
import org.frameworkset.spi.BaseApplicationContext;
import org.frameworkset.spi.DefaultApplicationContext;
import org.frameworkset.spi.SOAApplicationContext;
import org.jgroups.Address;

import com.frameworkset.common.poolman.DBUtil;
import com.frameworkset.common.poolman.PreparedDBUtil;
import com.frameworkset.util.SimpleStringUtil;

public class HDFSUploadData {
	public String HADOOP_PATH ;

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
	private Map<String,List<Integer>> blocksplits = null; 
	
	/**
	 * 指定需要排除哪几个任务
	 */
	int[] excludeblocks = null;
	String excludeblocks_str;
	String blocks_str;
	/**
	 * 用来存放块的子块
	 */
	Map<String,List<Integer>> excludeblocksplits = null; 
	int subblocks = -1;
	private Map<String, TaskConfig> tasks;

	/**
	 * 构建分派任务时调用，只需要设置基本信息即可
	 * @return
	 */
	TaskConfig buildTaskConfig() {
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
		 
		config.excludeblocks = this.excludeblocks_str;
		config.blocks = this.blocks_str;
		
		if (!this.usepagine) {
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
				config.querystatement = sqlbuilder.toString();
			} else {
				config.querystatement = this.querystatement;
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

		return config;
	}
	
	private static LinkBlocks buildLinkBlocks(String blocks_)
	{
		int blocks[] = null;
		LinkBlocks linkBlocks = new LinkBlocks();
		Map<String,List<Integer>> blocksplits = new HashMap<String,List<Integer>>(); 
		if (blocks_ != null && !blocks_.equals("")) {
			String[] blocks_str = blocks_.trim().split(",");
			Set<Integer> blockset = new java.util.TreeSet<Integer>();
			
			for (int i = 0; i < blocks_str.length; i++) {
				String block = blocks_str[i];
				if(block.indexOf(".") > 0)//判断是否需要过滤数据块
				{
					String[] tt = block.split("\\.");
//					blocks[i] = Integer.parseInt(tt[0]);
					blockset.add(Integer.parseInt(tt[0]));
					List<Integer> subtasks = blocksplits.get(tt[0]);
					if(subtasks == null)
					{
						subtasks = new ArrayList<Integer>();
						blocksplits.put(tt[0], subtasks);
					}
					subtasks.add(Integer.parseInt(tt[1]));
					
				}
				else
				{
//					blocks[i] = Integer.parseInt(block);
					blockset.add(Integer.parseInt(block));
				}
			}
			blocks = new int[blockset.size()];
			int i = 0;
			for(Integer b:blockset)
			{
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
	 * @param jobname
	 * @return
	 */
	public static TaskConfig buildTaskConfig(String jobname) {
		TaskConfig config = new TaskConfig();
		BaseApplicationContext context = DefaultApplicationContext
				.getApplicationContext("tasks.xml");
		if(context.getProBean(jobname) == null)//如果作业在固化配置中没有，则需要从db配置表中获取组件配置
		{
			try {
				DBJob job = DBHelper.getDBJob(jobname);
				if(job != null)
				{
					config.setJobdef(job.getJobdef());
					context = new SOAApplicationContext(job.getJobdef());
				}
				else
					log.info("jobname["+jobname+"]在数据库中没有定义,在固化配置文件tasks。xml中也没有定义.");
			} catch (Exception e) {
				log.error("jobname["+jobname+"]在数据库中没有定义,在固化配置文件tasks。xml中也没有定义.",e);
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
		String excludeblocks_ = context.getStringExtendAttribute(jobname, "excludeblocks");
		
		String blocks_ = context.getStringExtendAttribute(jobname, "blocks");
//		LinkBlocks linkBlocks = buildLinkBlocks(blocks_);
//		int[] blocks = linkBlocks.blocks;
//		Map<String,List<Integer>> blocksplits = linkBlocks.blocksplits;
		int subblocks = context.getIntExtendAttribute(jobname, "subblocks", -1);
		 
		
		String driver = context.getStringExtendAttribute(jobname,
				"driver");
		String dburl = context.getStringExtendAttribute(jobname,
				"dburl");
		String dbpassword = context.getStringExtendAttribute(jobname,
				"dbpassword","");
		String dbuser = context.getStringExtendAttribute(jobname,
				"dbuser","");
		String validatesql = context.getStringExtendAttribute(jobname,
				"validatesql","");
		
		
		boolean usepool = context.getBooleanExtendAttribute(jobname,
				"usepool",false);
		
		String readOnly = context.getStringExtendAttribute(jobname,
				"readOnly");
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
//		config.blocksplits = blocksplits;
		config.subblocks = subblocks;
		
		if (!usepagine) {
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
				config.querystatement = sqlbuilder.toString();
			} else {
				config.querystatement = querystatement;
			}
		} else {
			config.tablerows = tablerows;
			config.countstatement = countstatement;
			if (pageinestatement == null || pageinestatement.equals("")) {
			
				
//				DBHelper.initDB(config);
//				config.pageinestatement = DBUtil.getDBAdapter(dbname)
//						.getStringPagineSql(schema, tablename, pkName, columns);

				
			} else {
				config.pageinestatement = pageinestatement;
			}

		}

		return config;
	}

	static class SplitTasks {
		TaskInfo[] segments;
		long segement = 0l;
	}

	/**
	 * segments , startid, subblocks,segement,  div,  usepagine
	 * @param segments
	 * @param startid
	 * @param datablocks
	 * @param segement
	 * @param div
	 * @param usepagine
	 */
	private void spiltTask_(TaskInfo[] segments,long startid ,long endid ,int datablocks,long segement,long div,boolean usepagine,String filebasename)
	{
		if(!usepagine)
		{
			for (int i = 0; i < datablocks; i++) {
				TaskInfo task = new TaskInfo();
				if (i < div) {
					task.startoffset = startid + i * segement + i;
					task.endoffset = task.startoffset + segement - 1
							+ 1;
					task.pagesize = task.endoffset - task.startoffset
							+ 1;
	
				} else {
					task.startoffset = startid + i * segement + div;
					if (i == segments.length - 1)
						task.endoffset = endid;
					else
						task.endoffset = task.startoffset + segement
								- 1;
					task.pagesize = task.endoffset - task.startoffset
							+ 1;
				}
				task.filename = filebasename + "_" + i;
				segments[i] = task;
			}
		}
		else
		{
			for (int i = 0; i < datablocks; i++) {
				TaskInfo task = new TaskInfo();
				if (i < div) {
					task.startoffset = startid+i * segement + i;
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
				segments[i] = task;
			}
		}
	}
	
	static class LinkTasks
	{
		int block;
		TaskInfo taskInfo;
	}
	
	static class LinkBlocks
	{
		int[] blocks;
		Map<String,List<Integer>> blocksplits;
	}
	SplitTasks spiltTask() throws Exception {
		SplitTasks splitTasks = new SplitTasks();
		TaskInfo[] segments = new TaskInfo[this.datablocks];
		long segement = 0l;
		if (!this.usepagine) // 按主键范围切割数据，可能导致数据不均匀
		{

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

					spiltTask_(segments ,startid,endid,this.datablocks,segement,div,usepagine,filebasename);
				} else // 数据量小于块数，那么直接按一块数据进行处理，不需要进行分块处理
				{
					TaskInfo task = new TaskInfo();
					task.startoffset = startid;
					task.endoffset = endid;
					task.pagesize = datas;
					task.filename = filebasename + "_0";
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
		} else // 按分页方式切割任务
		{
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
				spiltTask_(segments ,0,0,this.datablocks,segement,div,usepagine,filebasename);
			} else {
				TaskInfo task = new TaskInfo();
				task.startoffset = 0;

				task.pagesize = datas;
				task.filename = filebasename + "_0";
				segement = datas;
				segments = new TaskInfo[1];
				segments[0] = task;
			}
		}
		List<LinkTasks> temp = null;
		List<TaskInfo> retTasks = null;
		if (this.blocks != null && this.blocks.length > 0)// 如果指定了任务块，则只执行指定的任务块
		{
			temp = new ArrayList<LinkTasks>(blocks.length);
			int blockslen = this.blocks.length;
			for (int i = 0; i < blockslen; i++) {
				if (blocks[i] < segments.length) {
					LinkTasks linkTasks = new LinkTasks();
					linkTasks.block =blocks[i];
					linkTasks.taskInfo = segments[blocks[i]];
					temp.add(linkTasks);
					
				}
			}
			if (temp.size() > 0) {
				if(this.subblocks > 0)//如果需要对子任务进行切分，则需要进一步切分子任务，并且需要清除原来的大块数据对应的hdfs文件
				{
					//进一步切分子任务
					List<TaskInfo> subchunks = new ArrayList<TaskInfo>();
					for (int i = 0; i < temp.size(); i++) {
						LinkTasks linkTasks = temp.get(i);  
						TaskInfo t = linkTasks.taskInfo;
						SplitTasks subsplitTasks = buildJobSubChunks(t);
						if(subsplitTasks == null)
						{
							subchunks.add(t);
						}
						else
						{
							segement = subsplitTasks.segement;
							List<Integer> filters = this.blocksplits.get(linkTasks.block+"");//指定了要过滤的数据块
							if(filters != null && filters.size() > 0)
							{
								List<TaskInfo> subsgs = new ArrayList<TaskInfo>(filters.size());
								TaskInfo[] sgs = subsplitTasks.segments;
								for(int j = 0; j < filters.size(); j++)
								{
									int pos = filters.get(j).intValue();
									if(pos < sgs.length)
									{
										subsgs.add(sgs[pos]);
									}
								}
								subchunks.addAll(subsgs);
							}
							else
							{
								subchunks.addAll(Arrays.asList(subsplitTasks.segments));
							}
							
						}
					}
					retTasks = subchunks;
					
				}
				else
				{
					retTasks = new ArrayList<TaskInfo>();
					for (int i = 0; i < temp.size(); i++) {
						LinkTasks linkTasks = temp.get(i); 
						retTasks.add(linkTasks.taskInfo);
					}
				}
				
				
				segments = new TaskInfo[retTasks.size()];				
			
				retTasks.toArray(segments);
				
			}
		}
		else if(this.excludeblocks != null && this.excludeblocks.length > 0)//排除要清除的数据块
		{
			temp = new ArrayList<LinkTasks>();
			int blockslen = this.excludeblocks.length;
			for (int i = 0; i < blockslen; i++) {//计算有效的排除块
				if (excludeblocks[i] < segments.length) {
					LinkTasks linkTasks = new LinkTasks();
					linkTasks.block =excludeblocks[i];
					linkTasks.taskInfo = segments[linkTasks.block];
					temp.add(linkTasks);
					
				}
			}
			if (temp.size() > 0) {
				if(this.subblocks > 0)//如果需要对子任务进行切分，则需要进一步切分子任务，并且需要清除原来的大块数据对应的hdfs文件
				{
					//进一步切分子任务
					List<TaskInfo> subchunks = new ArrayList<TaskInfo>();
					for(int j = 0; j < segments.length; j ++ )
					{
						SplitTasks subsplitTasks = buildJobSubChunks(segments[j]);
						if(subsplitTasks != null)
							segement = subsplitTasks.segement;
						boolean isexclude = false;
						for (int i = 0; i < temp.size(); i++){
							LinkTasks linkTasks = temp.get(i);  
							if(linkTasks.block == j)//不是排除的块，需要重新抽取
							{
								isexclude = true;
								break;
							}
						}
						if(!isexclude)
						{
							if(subsplitTasks == null)
							{
								subchunks.add(segments[j]);
							}
							else
							{								
								subchunks.addAll(Arrays.asList(subsplitTasks.segments));
							}
						}
						else
						{
							if(subsplitTasks == null)//没有子块，整块排除
							{
								
							}
							else //有子块，判断要排除的子块
							{
								List<Integer> excludes =  this.excludeblocksplits.get(j+"");
								if(excludes == null)//没有自定子块，整块的所有子块都排除
								{
									
								}
								else //识别子块中要排除的块，将不需要排除的块添加到任务列表中
								{
									
									for(int k = 0; k < subsplitTasks.segments.length;k ++)
									{
										boolean issubexclude = false;
										for(Integer epos:excludes )
										{
											if(k == epos.intValue())
											{
												issubexclude = true;
												break;
											}
										
										}
										if(!issubexclude)
											subchunks.add(subsplitTasks.segments[k]);
									}
									
								}
								
							}
						} 
						 
					}
					retTasks = subchunks;
					
				}
				else
				{
					retTasks = new ArrayList<TaskInfo>();
					for (int j = 0; j < segments.length; j++) {
						boolean isexclude  = false;
						for(int i = 0; i < temp.size(); i ++)
						{
							LinkTasks linkTasks = temp.get(i);
							if(linkTasks.block == j)
							{
								isexclude = true; 
							}
						}
						if(!isexclude) 
							retTasks.add(segments[j]);
					}
				}
				
				
				segments = new TaskInfo[retTasks.size()];				
			
				retTasks.toArray(segments);
				
			}
		}
		splitTasks.segement = segement;
		splitTasks.segments = segments;
		return splitTasks;
	}

	private List<String> deleteParentBlockHDFS ;
	/**
	 * 如果指定了子数据块，则对子数据块进行进一步任务切分
	 */
	SplitTasks buildJobSubChunks(TaskInfo taskInfo) {

		
			SplitTasks splitTasks = new SplitTasks();
			TaskInfo[] segments = new TaskInfo[this.subblocks];
			long segement = 0l;
			long startid = taskInfo.startoffset;
			long datas = taskInfo.pagesize;
			long endid = startid + datas-1;
			if(deleteParentBlockHDFS == null)
				deleteParentBlockHDFS = new ArrayList<String>();
			if (!this.usepagine) {
				
				if (datas > subblocks) {
					segement = datas / subblocks;

					long div = datas % this.subblocks;

					deleteParentBlockHDFS.add(taskInfo.filename);
					spiltTask_(  segments ,startid,endid,subblocks,  segement,  div,  usepagine,taskInfo.filename);
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
					spiltTask_(  segments , startid, endid,subblocks,segement,  div,  usepagine,taskInfo.filename);
					splitTasks.segement = segement;
					splitTasks.segments = segments;
					return splitTasks;
				} else
					return null;
			}
			
		
	}
	public void deleteHDFSFile(String filename) throws Exception
	{
		Path hdfsdatafile = new Path(this.hdfsdatadir,filename);
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
		SplitTasks splitTasks = spiltTask();//切分数据块
		if (splitTasks == null)
			return;
		TaskInfo[] segments = splitTasks.segments;
		long segement = splitTasks.segement;
		if(this.deleteParentBlockHDFS != null && deleteParentBlockHDFS.size() > 0)//删除大块数据对应的hdfs文件，因为对应的大块数据文件已经被切分为更小的hdfs数据块文件
		{
			for(String filename:deleteParentBlockHDFS)
			{
				deleteHDFSFile( filename);
			}
		}
		// 将任务分给所有的服务器处理
		if (!this.rundirect) {
			tasks = new HashMap<String, TaskConfig>();
			int alltasks = segments.length;
			int servertasks = alltasks / workservers;
			int taskdiv = alltasks % workservers;
			for (int i = 0; i < workservers && i < alltasks; i++) {
				TaskConfig config = buildTaskConfig();

				config.setPagesize(segement);
				if (i < taskdiv) {

					TaskInfo task[] = new TaskInfo[servertasks + 1];
					System.arraycopy(segments, i * servertasks + i, task, 0,
							servertasks + 1);
					config.setTasks(task);

				} else {

					TaskInfo task[] = new TaskInfo[servertasks];
					System.arraycopy(segments, i * servertasks + taskdiv, task,
							0, servertasks);
					config.setTasks(task);

				}

				tasks.put(allAddress.get(i).toString(), config);
				// tasks.put(""+i, config);

			}
			Event<Map<String, TaskConfig>> event = new EventImpl<Map<String, TaskConfig>>(
					tasks, hdfsuploadevent);
			/**
			 * 消息以异步方式传递
			 */

			EventHandle.getInstance().change(event, false);
		} else {
			TaskConfig config = buildTaskConfig();

			config.setPagesize(segement);
			config.setTasks(segments);
			tasks = new HashMap<String, TaskConfig>();
			tasks.put("rundirect", config);
			Event<Map<String, TaskConfig>> event = new EventImpl<Map<String, TaskConfig>>(
					tasks, hdfsuploadevent, Event.LOCAL);
			EventHandle.getInstance().change(event, false);
		}

	}

	private void initJob(BaseApplicationContext context,String jobname) {
		
		this.localpath = context.getStringExtendAttribute(jobname, "localdir");
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
		this.fileSystem = HDFSServer.getFileSystem(hdfsserver);
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
		excludeblocks_str = context.getStringExtendAttribute(jobname, "excludeblocks");
		linkBlocks = buildLinkBlocks(excludeblocks_str);
		excludeblocks = linkBlocks.blocks;
		excludeblocksplits = linkBlocks.blocksplits;
		subblocks = context.getIntExtendAttribute(jobname, "subblocks", -1);
		
		driver = context.getStringExtendAttribute(jobname,
				"driver");
		dburl = context.getStringExtendAttribute(jobname,
				"dburl");
		dbpassword = context.getStringExtendAttribute(jobname,
				"dbpassword","");
		dbuser = context.getStringExtendAttribute(jobname,
				"dbuser","");
		readOnly = context.getStringExtendAttribute(jobname,
				"readOnly");
		
		validatesql = context.getStringExtendAttribute(jobname,
				"validatesql","");
		usepool = context.getBooleanExtendAttribute(jobname,
				"usepool",false);
	}
	
	

	public void uploadData(String jobname) throws Exception {
		BaseApplicationContext context = DefaultApplicationContext
				.getApplicationContext("tasks.xml");
		if(context.containsBean(jobname))
		{
			uploadData( context,  jobname);
		}
		else
		{
			DBJob dbjob = DBHelper.getDBJob(jobname);
			if(dbjob == null)
			{
				throw new Exception("作业"+jobname+"未定义!");
			}
			context = new SOAApplicationContext(dbjob.getJobdef());
			uploadData( context,  jobname);
		}
		
	}

	public void deleteData(String jobname) throws Exception {
		BaseApplicationContext context = DefaultApplicationContext
				.getApplicationContext("tasks.xml");
		initJob(context,jobname);
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

	public void uploadData(final BaseApplicationContext ioccontext, final  String jobname) throws Exception {
		new Thread(new Runnable(){
			public void run(){
				long start = System.currentTimeMillis();
				try {
					initJob(ioccontext,jobname);
					if (genlocalfile) {
						File file = new File(localpath);
						if (!file.exists())
							file.mkdirs();
					}
					
					Path path = new Path(hdfsdatadir);
					if (!fileSystem.exists(path))
						fileSystem.mkdirs(path);
					else {
						if (clearhdfsfiles )// 如果指定了特定数据块任务，则不删除hdfs文件目录
						{
							if((excludeblocks == null || excludeblocks.length == 0)&& (blocks == null || blocks.length == 0))
							{
								fileSystem.delete(path, true);
								fileSystem.mkdirs(path);
							}
						}
					}
					DBHelper.initDB(HDFSUploadData.this);
					buildJobChunks();
					log.info("启动数据上传任务[jobname=" + jobname + "],[hdfsdatadir="
							+ hdfsdatadir + "] on hdfsserver[" + hdfsserver + "],"
							+ "[localdir=" + localpath + "],[dbname=" + dbname
							+ "],[tablename=" + tablename + "]," + "[schema=" + schema
							+ "],[pkName=" + pkName + "],[columns=" + columns
							+ "],[datablocks=" + datablocks + "]成功.");
				} catch (IllegalArgumentException e) {
					TaskConfig config = buildTaskConfig(jobname) ;
					JobStatic jobStatic = new JobStatic();
					jobStatic.setConfig(config.toString());
					jobStatic.setStatus(2);
					jobStatic.setStartTime(start);
					jobStatic.setEndTime(System.currentTimeMillis());
					jobStatic.setJobname(jobname);
					jobStatic.setErrormsg(SimpleStringUtil.exceptionToString(e));
					Imp.getImpStaticManager().addJobStatic(jobStatic);
					log.error("",e);
				} catch (IOException e) {
					TaskConfig config = buildTaskConfig(jobname) ;
					JobStatic jobStatic = new JobStatic();
					jobStatic.setConfig(config.toString());
					jobStatic.setStatus(2);
					jobStatic.setStartTime(start);
					jobStatic.setEndTime(System.currentTimeMillis());
					jobStatic.setJobname(jobname);
					jobStatic.setErrormsg(SimpleStringUtil.exceptionToString(e));
					Imp.getImpStaticManager().addJobStatic(jobStatic);
					log.error("",e);
				} catch (Exception e) {
					TaskConfig config = buildTaskConfig(jobname) ;
					JobStatic jobStatic = new JobStatic();
					jobStatic.setConfig(config.toString());
					jobStatic.setStatus(2);
					jobStatic.setStartTime(start);
					jobStatic.setEndTime(System.currentTimeMillis());
					jobStatic.setJobname(jobname);
					jobStatic.setErrormsg(SimpleStringUtil.exceptionToString(e));
					Imp.getImpStaticManager().addJobStatic(jobStatic);
					log.error("",e);
				}
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

}
