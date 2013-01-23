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

import org.junit.Test;

/**
 * <p>Title: TestScheduleTaskService.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2007</p>
 * @Date Mar 26, 2010 11:30:19 AM
 * @author biaoping.yin
 * @version 1.0
 */
public class TestScheduleTaskService {
	@Test
	public void testTaskService()
	{
//		TaskService.getTaskService().startService();//org/frameworkset/task/quarts-task.xml
//		TaskService.getTaskService().stopService();
		
		TaskService taskService = TaskService.getTaskService("org/frameworkset/task/test-quarts-task.xml");
		taskService.startService();
		taskService.deleteJob("workbroker", "default");
		taskService.startExecuteJob("default", "workbroker");
		taskService.updateExecuteJob("default", "workbroker");
		taskService.stopService();
		taskService.startService();
		System.out.println();

	}
	
	@Test
	public void testTaskServiceException()
	{
//		TaskService.getTaskService().startService();//org/frameworkset/task/quarts-task.xml
//		TaskService.getTaskService().stopService();
		
		TaskService taskService = TaskService.getTaskService("org/frameworkset/task/task-quartz-exception.xml");
		taskService.startService();
//		taskService.deleteJob("workbroker", "default");
//		taskService.startExecuteJob("default", "workbroker");
//		taskService.updateExecuteJob("default", "workbroker");
//		taskService.stopService();
//		taskService.startService();
		System.out.println();

	}
	
	public static void main(String[] args){
		new TestScheduleTaskService().testTaskServiceException();
	}

}
