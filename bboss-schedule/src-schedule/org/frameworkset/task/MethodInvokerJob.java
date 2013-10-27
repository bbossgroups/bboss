/*
 *  Copyright 2008 biaoping.yin
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package org.frameworkset.task;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;

import org.apache.log4j.Logger;
import org.frameworkset.spi.assemble.MethodInvoker;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.frameworkset.orm.transaction.TransactionManager;


/**
 * <p>Title: MethodInvokerJob.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2007</p>
 * @Date 2011-1-22 下午06:25:14
 * @author biaoping.yin
 * @version 1.0
 */
public class MethodInvokerJob implements Job, Serializable{
	private static final Logger log = Logger.getLogger(MethodInvokerJob.class);
	public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
		 
		 try {
			 JobDataMap data = jobExecutionContext.getJobDetail().getJobDataMap();
			 MethodInvoker action = (MethodInvoker)data.get("JobMethod");
			 try
		    	{
				 
		    		boolean state = TransactionManager.destroyTransaction();
					if(state){
						log.debug("A DB transaction leaked before Job ["+ action.getProviderManagerInfo().getStringExtendAttribute("jobid") +"] been forcibly destoried. ");
					}
		    	}
				catch(Throwable e)
				{
					
				}
			 action.invoker();
			 try
		    	{
				 
		    		boolean state = TransactionManager.destroyTransaction();
					if(state){
						log.debug("A DB transaction leaked in Job ["+ action.getProviderManagerInfo().getStringExtendAttribute("jobid") +"] been forcibly destoried. ");
					}
		    	}
				catch(Throwable e)
				{
					
				}
		} catch (IllegalArgumentException e) {
			log.error(e.getMessage(),e);
		} catch (IllegalAccessException e) {
			log.error(e.getMessage(),e);
		} catch (InvocationTargetException e) {
			log.error(e.getTargetException().getMessage(),e.getTargetException());
//			throw new JobExecutionException(e.getTargetException());
		} catch (Throwable e) {
//			log.error(e);
			log.error(e.getMessage(),e);			
		}
		 
		 
		 
//	        Map parameters = (Map)data.get("parameters");
		
	}

}
