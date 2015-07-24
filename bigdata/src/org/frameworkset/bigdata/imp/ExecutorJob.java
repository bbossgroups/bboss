package org.frameworkset.bigdata.imp;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.hadoop.fs.FileSystem;
import org.apache.log4j.Logger;
import org.frameworkset.bigdata.imp.monitor.JobStatic;
import org.frameworkset.bigdata.imp.monitor.TaskStatus;
import org.frameworkset.bigdata.util.DBHelper;

import com.frameworkset.util.SimpleStringUtil;

public class ExecutorJob {
	private static Logger log = Logger.getLogger(ExecutorJob.class); 
	 private BlockingQueue<FileSegment> genfileQueues = null; 
	 private BlockingQueue<FileSegment> upfileQueues = null; 
	 FileSystem fileSystem=null;
	 TaskConfig config;
	 JobStatic jobStatic;
	 AtomicInteger genfilecount;
	 AtomicInteger upfilecount ;
	 List<TaskInfo> tasks;
	 
	 public int getErrorrowslimit()
	 {
		 return this.config.getErrorrowslimit();
	 }
	 int pos;
	 boolean justassigned = false;
	 
	 private Object reassignLock = new Object();
	 
	 public boolean usepagine()
	 {
		 return this.config.isUsepagine();
	 }
	 public boolean genlocalfile()
	 {
		 return this.config.isGenlocalfile();
	 }
	 public String getHdfsserver() {
			return config.getHdfsserver();
		}
	 public String getHdfsdatadirpath() {
			return config.getHdfsdatadirpath();
		}
	 public void completeTask(String taskNo)
	 {
		 synchronized(jobStatic)
		 {
			 this.jobStatic.completeTask(taskNo);
		 }
	 }
	 
	 public void reassignTask(String taskNo)
	 {
		 completeTask( taskNo);
	 }
	 private void doSingleTask()
	 {
		 jobStatic = Imp.getImpStaticManager().addJobStatic(this);
		 GenFileHelper helper = new GenFileHelper(this);
		 WriteDataTask wtask = new WriteDataTask(helper);
		 wtask.run();
		
	 }
	 
	private void doMultiTasks()
	{
		genfileQueues = new ArrayBlockingQueue<FileSegment>(config.getGenqueques()); 
		if(config.isGenlocalfile())
		{
			upfileQueues = new ArrayBlockingQueue<FileSegment>(config.getUploadqueues());
			boolean iswindows = SimpleStringUtil.isWindows();
			if(config.localdirpath.indexOf("|") > 0)
			{
				if(iswindows)
					config.localdirpath = config.localdirpath.split("\\|")[0];
				else
					config.localdirpath = config.localdirpath.split("\\|")[1];
			}		
			File localdir = new File(config.localdirpath);
			 if(!localdir.exists())
				 localdir.mkdirs();
		}
		
		tasks = config.getTasks();
		
		log.info("start run task:"+config +",task size:"+tasks.size());
		 jobStatic = Imp.getImpStaticManager().addJobStatic(this);
		 if(tasks != null)
		 {
			 List<String> undotasknos = new ArrayList<String>();
			 for(int i = 0; i < tasks.size();i++)
			 {
				 undotasknos.add(tasks.get(i).taskNo);
			 }
			 jobStatic.setUndotaskNos(undotasknos); 
			 jobStatic.setTotaltasks(tasks.size());
		 }
		 genfilecount = new AtomicInteger(tasks.size());
		 if(config.isGenlocalfile())
			 upfilecount = new AtomicInteger(config.getTasks().size());
		 run(0);
	}
	public void execute(TaskConfig config)
	{
		try
		{
			this.config = config;
			
			fileSystem = HDFSServer.getFileSystem(config.getHdfsserver());
			DBHelper.initDB(config);
			if(!config.isOnejob())
				doMultiTasks();
			else
				doSingleTask();
		}
		finally
		{
			
		}
	}
	
	public FileSegment createSingleFileSegment(int fileNo,long startid)
	{
		 FileSegment segement = new FileSegment();
		 TaskInfo taskInfo = new TaskInfo();
		 taskInfo.taskNo = fileNo+"";
		 taskInfo.filename = this.config.getFilebasename()+"_"+fileNo;
		 taskInfo.startoffset = startid;
		 taskInfo.pagesize = this.config.getRowsperfile();
		 synchronized(jobStatic)//就是为了获得锁，如果在分配任务则忽略处理
		 {
			 
			 segement.job = this;
			 segement.taskInfo = taskInfo;
			 TaskStatus taskStatus = Imp.getImpStaticManager().addJobTaskStatic(jobStatic, taskInfo,fileNo);
			 segement.setTaskStatus(taskStatus);			 
			 taskStatus.setStatus(0);
			 jobStatic.incrementtotaltasks();
			
		 }
		 return segement;
		 
	}
	
	private void run(int startpos)
	{
		 GenFileHelper  genFileWork = new GenFileHelper(this);
		 genFileWork.run(  config);		
		 StringBuilder errorinfo = new StringBuilder();
//		 int pos = 0;
		 for(pos = startpos; pos < tasks.size();pos ++)
		 {
			 if(this.justassigned)
				 this.justassigned = false;
				 
			 TaskInfo task = tasks.get(pos); 
			 try {
				 if(jobStatic.isforceStop())
				 {
					 genfileQueues.notifyAll();
					 if(config.isGenlocalfile())
						 this.upfileQueues.notifyAll();
					 break;
				 }
				 FileSegment segement = new FileSegment();
				 synchronized(jobStatic)//就是为了获得锁，如果在分配任务则忽略处理
				 {
					if(task.isReassigned())
					{
						
						continue;
					}
					 segement.job = this;
					 segement.taskInfo = task;
					 TaskStatus taskStatus = Imp.getImpStaticManager().addJobTaskStatic(jobStatic, task,pos);
					 segement.setTaskStatus(taskStatus);
					 
					 taskStatus.setStatus(3);
					
				 }
				 
				genfileQueues.put(segement);
			} catch (Exception e) {
				log.error(task.toString(),e);
				genFileWork.genfilecount.set(0);
				if(config.isGenlocalfile())
					genFileWork.upfilecount.set(0);
				errorinfo.append(SimpleStringUtil.exceptionToString(e)).append("\r\n");
				break;
			}
		 }
		
		 genFileWork.join();
		synchronized(this.reassignLock)
		{
			clean( errorinfo);
		}
	}
	
	private void clean(StringBuilder errorinfo)
	{
		if(justassigned)
		{
			justassigned = false;
			return;
		}
		 if(errorinfo.length() == 0)
		 {
//			 jobStatic.setStatus(1);
			 if( jobStatic.getStatus() == 0 ||  jobStatic.getStatus() == -1)
				 jobStatic.setStatus(1);
		 }
		 else
		 {
			 jobStatic.setStatus(2);
			 jobStatic.setErrormsg(errorinfo.toString());
		 }
		 if(jobStatic.isforceStop())
		 {
			 if(upfileQueues != null)
				 this.upfileQueues.clear();
			 if(genfileQueues != null)
				 this.genfileQueues.clear();
		 }
		 jobStatic.setEndTime(System.currentTimeMillis());
		 
		
	}
	public BlockingQueue<FileSegment> getUpfileQueues() {
		return upfileQueues;
	}
	public void setUpfileQueues(BlockingQueue<FileSegment> upfileQueues) {
		this.upfileQueues = upfileQueues;
	}
	public BlockingQueue<FileSegment> getGenfileQueues() {
		return genfileQueues;
	}
	public void setGenfileQueues(BlockingQueue<FileSegment> genfileQueues) {
		this.genfileQueues = genfileQueues;
	}
	public FileSystem getFileSystem() {
		return fileSystem;
	}
	public void setFileSystem(FileSystem fileSystem) {
		this.fileSystem = fileSystem;
	}
	public TaskConfig getConfig() {
		return config;
	}
	public boolean isforcestop() {
		return this.jobStatic.isforceStop();
	}
	public void assignTasks(List<TaskInfo> tasks) {
		if(tasks == null || tasks.size() == 0)
			return;
		synchronized(this.reassignLock)
		{
			justassigned = false;
			jobStatic.setTotaltasks(jobStatic.getTotaltasks()+tasks.size());
			if(jobStatic.stopped())//如果作业已经停止，重新启动作业，将分派过来的任务添加的作业队列中
			{
				this.genfilecount.set(tasks.size());
				if(config.isGenlocalfile())
					 upfilecount.set(tasks.size());
				jobStatic.setStatus(0);
				jobStatic.setEndTime(0);
				this.pos = this.tasks.size(); 
				this.tasks.addAll(tasks);
				 
				 
				List<String> undotasknos = new ArrayList<String>();
				 for(int i = 0; i < tasks.size();i++)
				 {
					 undotasknos.add(tasks.get(i).taskNo);
				 }
				 jobStatic.setUndotaskNos(undotasknos);
			 
				
				 run(pos);
			}
			else //直接追加任务到处理队列中
			{
				
			
				this.genfilecount.addAndGet(tasks.size());
				if(config.isGenlocalfile())
					 upfilecount.addAndGet(tasks.size());
				synchronized(jobStatic)
				{
					this.tasks.addAll(tasks);
					List<String> undotasknos = new ArrayList<String>();
					 for(int i = 0; i < tasks.size();i++)
					 {
						 undotasknos.add(tasks.get(i).taskNo);
					 }
					 jobStatic.setUndotaskNos(undotasknos);
					
				}
				
			}
			justassigned = true;
		}
		
	}
	public List<TaskInfo> getTasks() {
		return tasks;
	}
	
}
