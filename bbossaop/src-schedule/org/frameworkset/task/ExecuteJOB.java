package org.frameworkset.task;

import java.io.Serializable;
import java.util.Map;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * 
 * <p>Title: ExecuteJOB.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2007</p>
 * @Date 2010-3-26 ÏÂÎç02:04:00
 * @author biaoping.yin,gao.tang
 * @version 1.0
 */
public class ExecuteJOB implements Job, Serializable{


    /**
     * execute
     *
     * @param jobExecutionContext JobExecutionContext
     * @throws JobExecutionException    
     */
    public void execute(JobExecutionContext jobExecutionContext) throws
            JobExecutionException {
        JobDataMap data = jobExecutionContext.getJobDetail().getJobDataMap();
        Execute action = (Execute)data.get("action");
        Map parameters = (Map)data.get("parameters");
        action.execute(parameters);
    }
}
