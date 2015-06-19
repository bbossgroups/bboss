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
import org.frameworkset.event.Event;
import org.frameworkset.event.EventHandle;
import org.frameworkset.event.EventImpl;
import org.frameworkset.event.SimpleEventType;
import org.frameworkset.remote.EventUtils;
import org.frameworkset.spi.BaseApplicationContext;
import org.frameworkset.spi.DefaultApplicationContext;
import org.jgroups.Address;

import com.frameworkset.common.poolman.DBUtil;
import com.frameworkset.common.poolman.PreparedDBUtil;

public class HDFSUploadData {
	public String HADOOP_PATH = "hdfs://10.0.15.40:9000";

	private static Logger log = Logger.getLogger(HDFSUploadData.class);

	public static final SimpleEventType hdfsuploadevent = new SimpleEventType(
			"hdfsuploadevent");
	public static final SimpleEventType hdfs_upload_finish_event = new SimpleEventType(
			"hdfs_upload_finish_event");
	public static final SimpleEventType hdfs_upload_monitor = new SimpleEventType(
			"hdfs_upload_monitor");
	public static final SimpleEventType hdfs_upload_monitor_request_commond = new SimpleEventType(
			"hdfs_upload_monitor_request_commond");
	public static final SimpleEventType hdfs_upload_monitor_response_commond = new SimpleEventType(
			"hdfs_upload_monitor_response_commond");

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
	int subblocks = -1;
	private Map<String, TaskConfig> tasks;

	TaskConfig buildTaskConfig() {
		TaskConfig config = new TaskConfig();
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

	public static TaskConfig buildTaskConfig(String jobname) {
		BaseApplicationContext context = DefaultApplicationContext
				.getApplicationContext("tasks.xml");
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
		String blocks_ = context.getStringExtendAttribute(jobname, "blocks");
		int subblocks = context.getIntExtendAttribute(jobname, "subblocks", -1);
		int[] blocks = null;
		Map<String,List<Integer>> blocksplits = new HashMap<String,List<Integer>>(); 
		if (blocks_ != null && !blocks_.equals("")) {
			String[] blocks_str = blocks_.trim().split(",");
			blocks = new int[blocks_str.length];
			for (int i = 0; i < blocks_str.length; i++) {
				String block = blocks_str[i];
				if(block.indexOf(".") > 0)//判断是否需要过滤数据块
				{
					String[] tt = block.split("\\.");
					blocks[i] = Integer.parseInt(tt[0]);
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
					blocks[i] = Integer.parseInt(block);
				}
			}
		}
		TaskConfig config = new TaskConfig();
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
		config.blocksplits = blocksplits;
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
				// if(schema != null && !schema.equals(""))
				// sqlbuilder.append(config.schema).append(".");
				// sqlbuilder.append( config.tablename);
				// sqlbuilder.append(
				// "t) bb where bb.rowno_ <? and bb.rowno_ >?");
				// config.pageinestatement = sqlbuilder.toString();
				config.pageinestatement = DBUtil.getDBAdapter(dbname)
						.getStringPagineSql(schema, tablename, pkName, columns);

				// DBUtil.getDBAdapter(this.dbname).getStringPagineSql(sqlbuilder.toString());
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
//				
//				for (int i = 0; i < temp.size(); i++) {
//					segments[i] = temp.get(i);
//				}
				
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

		String blocks_ = context.getStringExtendAttribute(jobname, "blocks");
		subblocks = context.getIntExtendAttribute(jobname, "subblocks", -1);
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
	}
	
	/**
	 * 用来存放块的子块
	 */
	private Map<String,List<Integer>> blocksplits = new HashMap<String,List<Integer>>(); 

	public void uploadData(String jobname) throws Exception {
		BaseApplicationContext context = DefaultApplicationContext
				.getApplicationContext("tasks.xml");
		uploadData( context,  jobname);
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
						if (clearhdfsfiles && (blocks == null || blocks.length == 0))// 如果指定了特定数据块任务，则不删除hdfs文件目录
						{
							fileSystem.delete(path, true);
							fileSystem.mkdirs(path);
						}
					}
					buildJobChunks();
					log.info("启动数据上传任务[jobname=" + jobname + "],[hdfsdatadir="
							+ hdfsdatadir + "] on hdfsserver[" + hdfsserver + "],"
							+ "[localdir=" + localpath + "],[dbname=" + dbname
							+ "],[tablename=" + tablename + "]," + "[schema=" + schema
							+ "],[pkName=" + pkName + "],[columns=" + columns
							+ "],[datablocks=" + datablocks + "]成功.");
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).start();
		
		
	}

}
