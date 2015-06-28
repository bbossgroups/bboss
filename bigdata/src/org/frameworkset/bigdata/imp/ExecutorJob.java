package org.frameworkset.bigdata.imp;

import java.io.File;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

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
	public void execute(TaskConfig config)
	{
		this.config = config;
		
		fileSystem = HDFSServer.getFileSystem(config.getHdfsserver());
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
		DBHelper.initDB(config);
		log.info("start run task:"+config +",task size:"+config.getTasks().length);
		 jobStatic = Imp.getImpStaticManager().addJobStatic(this);
		 if(config.getTasks() != null)
			 jobStatic.setTotaltasks(config.getTasks().length);
		 GenFileHelper  genFileWork = new GenFileHelper(this);
		 genFileWork.run(  config);		
		 StringBuilder errorinfo = new StringBuilder();
		 int pos = 0;
		 for(TaskInfo task:config.getTasks())
		 {
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
						continue;
					 segement.job = this;
					 segement.taskInfo = task;
					 TaskStatus taskStatus = Imp.getImpStaticManager().addJobTaskStatic(jobStatic, task,pos);
					 segement.setTaskStatus(taskStatus);
					 
					 taskStatus.setStatus(3);
					 pos++;
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
		 if(errorinfo.length() == 0)
		 {
			 jobStatic.setStatus(1);
		 }
		 else
		 {
			 jobStatic.setStatus(2);
			 jobStatic.setErrormsg(errorinfo.toString());
		 }
		 if(jobStatic.isforceStop())
		 {
			 this.upfileQueues.clear();
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
	
}
