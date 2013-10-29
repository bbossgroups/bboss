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
import org.quartz.JobDetail;
import org.quartz.SchedulerException;
import org.quartz.SchedulerListener;
import org.quartz.Trigger;


/**
 * <p>Title: DefaultSchedulerListener.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2007</p>
 * @Date 2013-1-23 ÉÏÎç9:01:09
 * @author biaoping.yin
 * @version 1.0
 */
public class DefaultSchedulerListener implements SchedulerListener {
	private static Logger log = Logger.getLogger(DefaultSchedulerListener.class) ;
	public DefaultSchedulerListener() {
		
	}

	public void jobScheduled(Trigger trigger) {
		log.info("job Scheduled:"+trigger.toString());

	}

	public void jobUnscheduled(String triggerName, String triggerGroup) {
		log.info("job Unscheduled:triggerName="+triggerName + ",triggerGroup="+triggerGroup);

	}

	public void triggerFinalized(Trigger trigger) {
		log.info("trigger Finalized:"+trigger.toString());

	}

	public void triggersPaused(String triggerName, String triggerGroup) {
		log.info("triggers Paused:triggerName="+triggerName + ",triggerGroup="+triggerGroup);

	}

	public void triggersResumed(String triggerName, String triggerGroup) {
		log.info("triggers Resumed:triggerName="+triggerName + ",triggerGroup="+triggerGroup);

	}

	public void jobAdded(JobDetail jobDetail) {
		log.info("job Added:"+jobDetail.toString());

	}

	public void jobDeleted(String jobName, String groupName) {
		log.info("job Deleted:jobName="+jobName + ",groupName="+groupName);

	}

	public void jobsPaused(String jobName, String jobGroup) {
		log.info("jobs Paused:jobName="+jobName + ",jobGroup="+jobGroup);

	}

	public void jobsResumed(String jobName, String jobGroup) {
		log.info("jobs Resumed:jobName="+jobName + ",jobGroup="+jobGroup);

	}

	public void schedulerError(String msg, SchedulerException cause) {
		log.error("scheduler Error:msg="+msg ,cause);

	}

	public void schedulerInStandbyMode() {
		log.info("scheduler In Standby Mode.");

	}

	public void schedulerStarted() {
		log.info("scheduler Started");

	}

	public void schedulerShutdown() {
		log.info("scheduler Shutdown .");

	}

	public void schedulerShuttingdown() {
		log.info("scheduler Shutdown.");

	}

}
