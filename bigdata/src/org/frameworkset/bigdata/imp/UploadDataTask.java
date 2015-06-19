package org.frameworkset.bigdata.imp;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.apache.log4j.Logger;


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
	@Override
	public void run() {
		OutputStream out= null;
		FileInputStream in = null;
		try {
			fileSegment.upstarttimestamp =  System.currentTimeMillis();
			
			
			log.info("上传文件："+fileSegment.toString());
			out=fileSystem.create(new Path(fileSegment.hdfsdir,fileSegment.taskInfo.filename));       
			in=new FileInputStream(new File(fileSegment.localdir,fileSegment.taskInfo.filename));         
			IOUtils.copyBytes(in, out, 1024, true); 
			fileSegment.clear();
			fileSegment.upendtimestamp =  System.currentTimeMillis();
			log.info("上传文件结束："+fileSegment.toString());
			
		} catch (Exception e) {
			log.error("上传文件异常结束："+fileSegment.toString(),e);
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


