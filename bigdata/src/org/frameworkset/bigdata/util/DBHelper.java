package org.frameworkset.bigdata.util;

import org.frameworkset.bigdata.imp.HDFSUploadData;
import org.frameworkset.bigdata.imp.TaskConfig;

import com.frameworkset.common.poolman.util.SQLUtil;

public class DBHelper {

	
	public static void initDB(HDFSUploadData HDFSUploadData)
	{
		if(HDFSUploadData.getDriver() != null && HDFSUploadData.getDriver().trim().length() > 0)
		{

			SQLUtil.startPool(HDFSUploadData.getDbname(),HDFSUploadData.getDriver(),HDFSUploadData.getDburl(),HDFSUploadData.getDbuser(),HDFSUploadData.getDbpassword(),
					 HDFSUploadData.isReadOnly(),
					 "READ_COMMITTED",
					 HDFSUploadData.getValidatesql(),
					 "jndi-"+HDFSUploadData.getDbname(),   
		        		10,
		        		10,
		        		20,
		        		HDFSUploadData.isUsepool(),
		        		false,
		        		null        ,true,false
		        		);
		}
 
	}
	
	public static void initDB(TaskConfig taskConfig)
	{
		if(taskConfig.getDriver() != null && taskConfig.getDriver().trim().length() > 0)
		{

		SQLUtil.startPool(taskConfig.getDbname(),taskConfig.getDriver(),taskConfig.getDburl(),taskConfig.getDbuser(),taskConfig.getDbpassword(),
				 taskConfig.getReadOnly(),
				 "READ_COMMITTED",
				 taskConfig.getValidatesql(),
				 "jndi-"+taskConfig.getDbname(),   
	        		10,
	        		10,
	        		20,
	        		taskConfig.isUsepool(),
	        		false,
	        		null        ,true,false
	        		);
		}
 
	}

}
