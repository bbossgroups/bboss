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

import org.apache.log4j.Logger;
import org.quartz.JobExecutionContext;
import org.quartz.Trigger;

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
	private static Logger log = Logger.getLogger(DefaultTriggerListener.class);
	
	public DefaultTriggerListener() {
		// TODO Auto-generated constructor stub
	}
	

	public void triggerFired(Trigger trigger, JobExecutionContext context) {
		log.info("Default trigger Fired:["+context.toString()+"]");

	}

	public boolean vetoJobExecution(Trigger trigger, JobExecutionContext context) {
		// TODO Auto-generated method stub
		return false;
	}

	public void triggerMisfired(Trigger trigger) {
		log.info("Default trigger Misfired:[" + trigger.toString()+"]");

	}

	public void triggerComplete(Trigger trigger, JobExecutionContext context,
			int triggerInstructionCode) {
		log.info("Default trigger Complete:耗时"+context.getJobRunTime()+" milliseconds,["+context.toString()+"],[triggerInstructionCode="+triggerInstructionCode+"]");

	}

	
	

}
