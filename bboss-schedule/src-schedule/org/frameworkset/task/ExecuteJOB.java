package org.frameworkset.task;

import java.io.Serializable;
import java.util.Map;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.frameworkset.orm.transaction.TransactionManager;

/**
 * 
 * <p>Title: ExecuteJOB.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2007</p>
 * @Date 2010-3-26 下午02:04:00
 * @author biaoping.yin,gao.tang
 * @version 1.0
 */
public class ExecuteJOB implements Job, Serializable{

	private static final Logger log = LoggerFactory.getLogger(ExecuteJOB.class);
    /**
     * execute
     *
     * @param jobExecutionContext JobExecutionContext
     * @throws JobExecutionException    
     */
    public void execute(JobExecutionContext jobExecutionContext) throws
            JobExecutionException {
    	
        try {
			JobDataMap data = jobExecutionContext.getJobDetail().getJobDataMap();
			Execute action = (Execute)data.get("action");
			try
	    	{
	    		boolean state = TransactionManager.destroyTransaction();
				if(state){
					log.debug("A DB transaction leaked before Job ["+ action.getClass() +"] been forcibly destoried. ");
				}
	    	}
			catch(Throwable e)
			{
				
			}
			Map parameters = (Map)data.get("parameters");
			action.execute(parameters);
			try
	    	{
	    		boolean state = TransactionManager.destroyTransaction();
				if(state){
					log.debug("A DB transaction leaked in Job ["+ action.getClass() +"] been forcibly destoried. ");
				}
	    	}
			catch(Throwable e)
			{
				
			}
		} catch (IllegalArgumentException e) {
			log.error(e.getMessage(),e);
		} catch (Throwable e) {
			log.error(e.getMessage(),e);
		}
    }
}
