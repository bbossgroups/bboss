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


import org.quartz.JobExecutionContext;
import org.quartz.Scheduler;
import org.quartz.Trigger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>Title: DefaultTriggerListener.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2007</p>
 * @Date 2013-1-22 下午4:33:20
 * @author biaoping.yin
 * @version 1.0
 */
public class DefaultTriggerListener  extends BaseTriggerListener {
	private static Logger log = LoggerFactory.getLogger(DefaultTriggerListener.class);
	
	public DefaultTriggerListener() {
		// TODO Auto-generated constructor stub
	}
	

	public void triggerFired(Trigger trigger, JobExecutionContext context) {
		if(log.isInfoEnabled())
			log.info("Default trigger Fired:["+context.toString()+"]");

	}

	public boolean vetoJobExecution(Trigger trigger, JobExecutionContext context) {
		// TODO Auto-generated method stub
		return false;
	}

	public void triggerMisfired(Trigger trigger) {
		if(log.isInfoEnabled())
			log.info("Default trigger Misfired:[" + trigger.toString()+"]");

	}

//	public void triggerComplete(Trigger trigger, JobExecutionContext context,
//			int triggerInstructionCode) {
//		if(log.isInfoEnabled())
//			log.info("Default trigger Complete:耗时"+context.getJobRunTime()+" milliseconds,["+context.toString()+"],[triggerInstructionCode="+triggerInstructionCode+"]");
//
//	}




	/*
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     *
     * Interface.
     *
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */





    /**
     * <p>
     * Called by the <code>{@link Scheduler}</code> when a <code>{@link Trigger}</code>
     * has fired, it's associated <code>{@link org.quartz.JobDetail}</code>
     * has been executed, and it's <code>triggered(xx)</code> method has been
     * called.
     * </p>
     *
     * @param trigger
     *          The <code>Trigger</code> that was fired.
     * @param context
     *          The <code>JobExecutionContext</code> that was passed to the
     *          <code>Job</code>'s<code>execute(xx)</code> method.
     * @param triggerInstructionCode
     *          the result of the call on the <code>Trigger</code>'s<code>triggered(xx)</code>
     *          method.
     */
   public void triggerComplete(Trigger trigger, JobExecutionContext context,
            Trigger.CompletedExecutionInstruction triggerInstructionCode){
	   if(log.isInfoEnabled())
		   log.info("Default trigger Complete:耗时"+context.getJobRunTime()+" milliseconds,["+context.toString()+"],[triggerInstructionCode="+triggerInstructionCode+"]");
   }

	
	

}
