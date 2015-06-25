package org.frameworkset.bigdata.imp;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;

import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.apache.log4j.Logger;

import com.frameworkset.util.SimpleStringUtil;


public class UploadDataTask  implements Runnable
{
	 private static Logger log = Logger.getLogger(UploadDataTask.class);
	 FileSegment fileSegment;
	FileSystem fileSystem;
	public UploadDataTask(FileSystem fileSystem,FileSegment fileSegment)
	{
		this.fileSegment = fileSegment;
		this.fileSystem = fileSystem;
	}
	/**
	   * Copies from one stream to another.
	   * 
	   * @param in InputStrem to read from
	   * @param out OutputStream to write to
	   * @param buffSize the size of the buffer 
	   */
	  public  void copyBytes(InputStream in, OutputStream out, int buffSize) 
	    throws Exception {
	    PrintStream ps = out instanceof PrintStream ? (PrintStream)out : null;
	    byte buf[] = new byte[buffSize];
	    int bytesRead = in.read(buf);
	    while (bytesRead >= 0) {
	    	
	      out.write(buf, 0, bytesRead);
	      if ((ps != null) && ps.checkError()) {
	        throw new IOException("Unable to write to output stream.");
	      }
	      if(this.fileSegment.isforcestop())
	    	  throw new ForceStopException();
	      bytesRead = in.read(buf);
	    }
	  }
	@Override
	public void run() {
		OutputStream out= null;
		FileInputStream in = null;
		try {
			fileSegment.upstarttimestamp =  System.currentTimeMillis();
			
			fileSegment.taskStatus.setTaskInfo(fileSegment.toString());
			log.info("上传文件："+fileSegment.toString());
			out=fileSystem.create(new Path(fileSegment.hdfsdir,fileSegment.taskInfo.filename));       
			in=new FileInputStream(new File(fileSegment.localdir,fileSegment.taskInfo.filename));  
		
			copyBytes(in, out, 1024); 
			fileSegment.clear();
			fileSegment.upendtimestamp =  System.currentTimeMillis();
			log.info("上传文件结束："+fileSegment.toString());
			if(fileSegment.taskStatus.getStatus() != 2)
				fileSegment.taskStatus.setStatus(1);
			fileSegment.taskStatus.setTaskInfo(fileSegment.toString());
			
		}
		catch (ForceStopException e) {
			log.error("上传文件异常结束："+fileSegment.toString(),e);
			fileSegment.taskStatus.setStatus(2);
			fileSegment.upendtimestamp =  System.currentTimeMillis();
			fileSegment.taskStatus.setTaskInfo(fileSegment.toString());
			fileSegment.taskStatus.setErrorInfo("强制停止任务");
			
		}
		catch (Exception e) {
			log.error("上传文件异常结束："+fileSegment.toString(),e);
			fileSegment.taskStatus.setStatus(2);
			fileSegment.upendtimestamp =  System.currentTimeMillis();
			fileSegment.taskStatus.setTaskInfo(fileSegment.toString());
			fileSegment.taskStatus.setErrorInfo(SimpleStringUtil.exceptionToString(e));
			
		}
		finally
		{
			 if(out != null)
				try {
					out.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			 if(in != null)
					try {
						in.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		}
		
	}
	
}


