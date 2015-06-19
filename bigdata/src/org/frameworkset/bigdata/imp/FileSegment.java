package org.frameworkset.bigdata.imp;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.hadoop.fs.Path;
import org.apache.log4j.Logger;

import org.frameworkset.bigdata.imp.monitor.TaskStatus;

public class FileSegment {
	 private static Logger log = Logger.getLogger(FileSegment.class);
	 ExecutorJob job;
	 TaskInfo taskInfo;
	long genstarttimestamp;
	 long genendtimestamp;
	 long upstarttimestamp;
	 long upendtimestamp;
//	 long start;
//	 long end;
	 long rows;
	 long errorrows;
//	 String filename;
	 
	 Path hdfsdir ;
	 Path hdfsdatafile ;
	 File localdir;
	 File datafile;
	 PrintWriter writer;
	 TaskStatus taskStatus;
	 OutputStream out;
	 public boolean usepagine()
	 {
		 return this.job.usepagine();
	 }
	 public String getPageinestatement() {
			return this.job.config.getPageinestatement();
		}
	 public String getQuerystatement() {
			 return this.job.config.getQuerystatement();
		}
	 
	 public long getEndoffset() {
			return taskInfo.getEndoffset();
		}
	 public long getStartoffset() {
			return taskInfo.getStartoffset();
		}
	 public long getPagesize() {
		 return taskInfo.getPagesize();
		}
	 public String getDBName()
	 {
		 return this.job.config.getDbname();
	 }
	 public String toString()
	 {
		 SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
		 StringBuilder builder = new StringBuilder();
		 builder.append("filename="+taskInfo.filename).append("\r\n")
			.append("pagesize=").append(taskInfo.pagesize).append("\r\n")
			.append("start=").append(taskInfo.startoffset).append("\r\n")
			.append("end=").append(taskInfo.endoffset).append("\r\n")
			
			.append("gen start timestamp=").append(genstarttimestamp > 0?format.format(new Date(genstarttimestamp)):"").append("\r\n")
			.append("gen end timestamp=").append(genendtimestamp > 0?format.format(new Date(genendtimestamp)):"").append("\r\n");
			if(this.job.genlocalfile())
			{
				builder.append("up start timestamp=").append(upstarttimestamp > 0?format.format(new Date(upstarttimestamp)):"").append("\r\n")
				.append("up end timestamp=").append(upendtimestamp > 0?format.format(new Date(upendtimestamp)):"").append("\r\n");
			}
			builder.append("totalrows=").append(rows);
		 return builder.toString();
	 }
	 void init() throws Exception
	 {
//		 FileOutputStream out = new FileOutputStream(new File(tempdir,filename));
//		 localdir = new File(this.job.config.localdirpath);
//		 if(!localdir.exists())
//			 localdir.mkdirs();
		 
		 
		 this.hdfsdir = new Path(job.config.hdfsdatadirpath);
		 if(this.job.genlocalfile())
		 {
			 datafile = new File(localdir,taskInfo.filename);
			 if(datafile.exists())
				 datafile.delete();
			 datafile.createNewFile();
			 writer
			   = new PrintWriter(new BufferedWriter(new FileWriter(datafile)));
			
		 }
		 else
		 {
			 hdfsdatafile = new Path(job.getHdfsdatadirpath(),taskInfo.filename);
			 out=job.getFileSystem().create(hdfsdatafile);
			 
			 writer
			   = new PrintWriter(new BufferedWriter(new OutputStreamWriter(out,"UTF-8")));
			  
		 }
		
		 
	 }
	 void writeLine(String line) throws Exception
	 {
		 this.writer.println(line);
		 rows ++;
		 this.taskStatus.setHandlerows(rows);
		 log.debug(taskInfo.filename+"-rows["+rows+"]:"+line);
	 }
	 void errorrow()
	 {
		 this.errorrows ++;
		 this.taskStatus.setErrorrows(errorrows);
	 }
	 void flush() throws Exception
	 {
		 this.writer.flush();
	 }
	 void close()
	 {
		
		 if(out != null )
		 {
			 try {
				out.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			 out = null;
		 }
		 if(writer != null)
		 {
			 try {
				writer.close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			 writer = null;
		 }
		 
		 if(this.rows == 0) //删除空文件
		 {
			 if(!this.job.genlocalfile())
			 {
				 try {
					log.info("清除没有数据的hdfs file["+hdfsdatafile+"]，");
					job.getFileSystem().delete(hdfsdatafile, true);
					log.info("清除没有数据的hdfs file["+hdfsdatafile+"]完成，");
				} catch (Exception e) {
					log.info("清除没有数据的hdfs file["+hdfsdatafile+"]失败，",e);
					
				}
			 }
			 else
			 {
				 
			 }
		 }
	 }
	public long getGenstarttimestamp() {
		return genstarttimestamp;
	}
	public void setGenstarttimestamp(long genstarttimestamp) {
		this.genstarttimestamp = genstarttimestamp;
	}
	public long getGenendtimestamp() {
		return genendtimestamp;
	}
	public void setGenendtimestamp(long genendtimestamp) {
		this.genendtimestamp = genendtimestamp;
	}
	public long getUpstarttimestamp() {
		return upstarttimestamp;
	}
	public void setUpstarttimestamp(long upstarttimestamp) {
		this.upstarttimestamp = upstarttimestamp;
	}
	public long getUpendtimestamp() {
		return upendtimestamp;
	}
	public void setUpendtimestamp(long upendtimestamp) {
		this.upendtimestamp = upendtimestamp;
	}
	 
	 
	 
	public Path getHdfsdir() {
		return hdfsdir;
	}
	
	public File getLocaldir() {
		return localdir;
	}
	
	public PrintWriter getWriter() {
		return writer;
	}
	
	public void clear() {
		 try {
			if(datafile.exists())
				 datafile.delete();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public long getRows() {
		return rows;
	}
	public TaskStatus getTaskStatus() {
		return taskStatus;
	}
	public void setTaskStatus(TaskStatus taskStatus) {
		this.taskStatus = taskStatus;
	}
	public long getErrorrows() {
		return errorrows;
	}
	public void setErrorrows(long errorrows) {
		this.errorrows = errorrows;
	}
}
