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

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * <p>Title: ScheduleRepository.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2007</p>
 * @Date 2011-1-19 下午12:34:18
 * @author biaoping.yin
 * @version 1.0
 */
public class ScheduleRepository {
	private Map<String,TaskService> scheduleRepository = new HashMap<String,TaskService>();
	public final static String taskconfig = "org/frameworkset/task/quarts-task.xml";
	
	
//	private Object lock = new Object();
	public void addTaskService(String configfile,TaskService service)
	{	
		scheduleRepository.put(configfile, service);
	}
	
	public TaskService getTaskService(String configfile)
	{
		return scheduleRepository.get(configfile);
	}
	
	public void stopTaskServices()
	{
		Set<String> keys = scheduleRepository.keySet();
		if(keys.size() <= 0)
		{
			return ;
		}
		Iterator<String> keys_ = keys.iterator();
		while(keys_.hasNext())
		{
			String key = keys_.next();
			TaskService service = this.scheduleRepository.get(key);
			service.stopService();
		}
		scheduleRepository.clear();
		scheduleRepository = null;
		
			
		
	}
	
}
