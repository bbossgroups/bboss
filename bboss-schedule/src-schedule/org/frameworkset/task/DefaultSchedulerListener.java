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
import org.quartz.*;


/**
 * <p>Title: DefaultSchedulerListener.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2007</p>
 * @Date 2013-1-23 9:01:09
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

	public void jobUnscheduled(TriggerKey triggerKey) {
		log.info("job Unscheduled:triggerName="+triggerKey.getName() + ",triggerGroup="+triggerKey.getGroup());

	}

	public void triggerFinalized(Trigger trigger) {
		log.info("trigger Finalized:"+trigger.toString());

	}

	@Override
	public void triggerPaused(TriggerKey triggerKey) {
		log.info("trigger Paused"+triggerKey.getName() + ",triggerGroup="+triggerKey.getGroup());
	}

	@Override
	public void triggersPaused(String triggerGroup) {
		log.info("trigger Paused" + " triggerGroup="+triggerGroup);
	}

	@Override
	public void triggerResumed(TriggerKey triggerKey) {
		log.info("trigger Resumed"+triggerKey.getName() + ",triggerGroup="+triggerKey.getGroup());
	}

	@Override
	public void triggersResumed(String triggerGroup) {
		log.info("trigger Resumed" + " triggerGroup="+triggerGroup);
	}

	public void triggersPaused(TriggerKey triggerKey) {
		log.info("triggers Paused:triggerName="+triggerKey.getName() + ",triggerGroup="+triggerKey.getGroup());

	}

	public void triggersResumed(TriggerKey triggerKey) {
		log.info("triggers Resumed:triggerName="+triggerKey.getName() + ",triggerGroup="+triggerKey.getGroup());

	}
	@Override
	public void jobAdded(JobDetail jobDetail) {
		log.info("job Added:"+jobDetail.toString());

	}

	@Override
	public void jobDeleted(JobKey jobKey) {
		log.info("job Deleted:"+jobKey.toString());
	}

	@Override
	public void jobPaused(JobKey jobKey) {
		log.info("job Paused:"+jobKey.toString());
	}

	@Override
	public void jobsPaused(String jobGroup) {
		log.info("job Paused:"+jobGroup);
	}

	@Override
	public void jobResumed(JobKey jobKey) {
		log.info("job Resumed:"+jobKey.toString());
	}

	@Override
	public void jobsResumed(String jobGroup) {
		log.info("job Resumed:"+jobGroup);
	}

	public void jobDeleted(TriggerKey triggerKey) {
		log.info("job Deleted:jobName="+triggerKey.getName() + ",triggerGroup="+triggerKey.getGroup());

	}

	public void jobsPaused(TriggerKey triggerKey) {
		log.info("jobs Paused:jobName="+triggerKey.getName() + ",triggerGroup="+triggerKey.getGroup());

	}

	public void jobsResumed(TriggerKey triggerKey) {
		log.info("jobs Resumed:jobName="+triggerKey.getName() + ",triggerGroup="+triggerKey.getGroup());

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

	@Override
	public void schedulerStarting() {
		log.info("scheduler  Starting .");
	}

	public void schedulerShutdown() {
		log.info("scheduler Shutdown .");

	}

	public void schedulerShuttingdown() {
		log.info("scheduler Shutdown.");

	}

	@Override
	public void schedulingDataCleared() {
		log.info("scheduler  Data Cleared .");
	}

}
