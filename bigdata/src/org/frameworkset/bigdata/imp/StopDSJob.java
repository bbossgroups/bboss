package org.frameworkset.bigdata.imp;

import org.frameworkset.bigdata.imp.monitor.JobStatic;

import com.frameworkset.common.poolman.DBUtil;
import com.frameworkset.util.SimpleStringUtil;

public class StopDSJob {
	
	public StopDSJob() {
		// TODO Auto-generated constructor stub
	}
	public void execute(StopDS ds)
	{
		 JobStatic jobStatic = Imp.getImpStaticManager().addStopDSJobStatic(ds);
		 String[] dbnames = ds.getStopdbnames().split(",");
		 StringBuilder builder = new StringBuilder();
		 for(String dbname:dbnames)
		 {
			 try {
				DBUtil.stopPool(dbname);
				
			} catch (Exception e) {
				builder.append("停止dbname[").append(dbname).append("]失败:\r\n").append(SimpleStringUtil.exceptionToString(e)).append("\r\n");
			}
		 }
		 jobStatic.setEndTime(System.currentTimeMillis());
		 jobStatic.setStatus(1);
		 if(builder.length() > 0)
		 {
			 jobStatic.setErrormsg(builder.toString());
			
		 }
		 
	}

}
