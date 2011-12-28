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
import java.util.Map;

/**
 * <p>Title: TestJob.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2007</p>
 * @Date Mar 26, 2010 11:24:27 AM
 * @author biaoping.yin
 * @version 1.0
 */
public class TestJob implements Execute, Serializable {

	

	public void execute(Map parameters) {
		System.out.println("send_count = "+parameters.get("send_count"));
	}

}
