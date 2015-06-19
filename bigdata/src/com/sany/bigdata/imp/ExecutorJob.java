package com.sany.bigdata.imp;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import org.apache.hadoop.fs.FileSystem;
import org.apache.log4j.Logger;
import org.frameworkset.runtime.CommonLauncher;

import com.frameworkset.util.SimpleStringUtil;
import com.sany.bigdata.imp.monitor.JobStatic;
import com.sany.bigdata.imp.monitor.TaskStatus;

public class ExecutorJob {
	private static Logger log = Logger.getLogger(ExecutorJob.class); 
	 private BlockingQueue<FileSegment> genfileQueues = null; 
	 private BlockingQueue<FileSegment> upfileQueues = null; 
	 FileSystem fileSystem=null;
	 TaskConfig config;
	 JobStatic jobStatic;
	 Map<String,TaskStatus> taskstatus = new HashMap<String,TaskStatus>();
	 
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
			boolean iswindows = CommonLauncher.isWindows();
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
		log.info("start run task:"+config +",task size:"+config.getTasks().length);
		 jobStatic = Imp.getImpStaticManager().addJobStatic(this);
		 GenFileHelper  genFileWork = new GenFileHelper(this);
		 genFileWork.run(  config);		
		 StringBuilder errorinfo = new StringBuilder(); 
		 for(TaskInfo task:config.getTasks())
		 {
			 try {
				 FileSegment segement = new FileSegment();
				 segement.job = this;
				 segement.taskInfo = task;
				 TaskStatus taskStatus = Imp.getImpStaticManager().addJobTaskStatic(jobStatic, task);
				 segement.setTaskStatus(taskStatus);
				 taskStatus.setStatus(3);
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
	
}
