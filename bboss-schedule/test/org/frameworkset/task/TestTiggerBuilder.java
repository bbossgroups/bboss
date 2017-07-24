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

import com.frameworkset.util.StringUtil;
import com.frameworkset.util.ValueObjectUtil;
import org.quartz.Trigger;
import org.quartz.impl.triggers.SimpleTriggerImpl;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * <p>Title: TestTiggerBuilder.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2007</p>
 * @Date 2013-1-29 下午4:40:48
 * @author biaoping.yin
 * @version 1.0
 */
public class TestTiggerBuilder implements TriggerBuilder {

	public TestTiggerBuilder() {
		// TODO Auto-generated constructor stub
	}

	public Trigger builder(SchedulejobInfo jobInfo) throws Exception {
		String s_startTime = jobInfo.getJobPro().getStringExtendAttribute("startTime");
		Date startTime = null;
		if(!StringUtil.isEmpty(s_startTime) )
		{
			SimpleDateFormat format = ValueObjectUtil.getDefaultDateFormat();
			startTime = format.parse(s_startTime);
		}
		else
			startTime = new Date();
		
			
		String s_endTime = jobInfo.getJobPro().getStringExtendAttribute("endTime");
        Date endTime = null; 
        if(!StringUtil.isEmpty(s_endTime) )
		{
			SimpleDateFormat format = ValueObjectUtil.getDefaultDateFormat();
			endTime = format.parse(s_endTime);
		}
        String s_repeatCount = jobInfo.getJobPro().getStringExtendAttribute("repeatCount");
        int repeatCount = -1; 
        if(!StringUtil.isEmpty(s_repeatCount) )
		{
        	repeatCount = Integer.parseInt(s_repeatCount);
		}
        else
        {
        	repeatCount = 5;
        }
        String s_repeatInterval = jobInfo.getJobPro().getStringExtendAttribute("repeatInterval");
        long repeatInterval = 0;
        if(!StringUtil.isEmpty(s_repeatInterval) )
		{
        	repeatInterval = Long.parseLong(s_repeatInterval);
		}
        else
        	repeatInterval = 2000;

		SimpleTriggerImpl simpletrigger = new SimpleTriggerImpl(jobInfo.getId(), jobInfo.getScheduleServiceInfo().getId());
		simpletrigger.setStartTime(startTime);
		simpletrigger.setEndTime(endTime);
		simpletrigger.setRepeatCount(repeatCount);
		simpletrigger.setRepeatInterval(repeatInterval);


		return simpletrigger;
	}

}
