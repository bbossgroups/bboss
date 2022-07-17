package org.frameworkset.util;
/**
 * Copyright 2022 bboss
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.util.Map;

/**
 * <p>Description: </p>
 * <p></p>
 * <p>Copyright (c) 2020</p>
 * @Date 2022/6/30
 * @author biaoping.yin
 * @version 1.0
 */
public abstract class ResourceStartResult {
	private Map<String,Object> resourceStartResult;
	public boolean contain(String dbname){
		if(resourceStartResult == null)
			return false;
		return resourceStartResult.containsKey(dbname);
	}
	public ResourceStartResult addResourceStartResult(String resourceName){
		if(resourceStartResult == null){
			resourceStartResult = new java.util.LinkedHashMap<>();

		}
		resourceStartResult.put(resourceName,resourceName);
		return this;
	}

	public ResourceStartResult addResourceStartResults(Map<String,Object> _resourceStartResult){
		if(_resourceStartResult == null || _resourceStartResult.size() == 0){
			return this;
		}
		if(resourceStartResult == null){
			resourceStartResult = new java.util.LinkedHashMap<>();

		}
		resourceStartResult.putAll(_resourceStartResult);
		return this;
	}


	public Map<String,Object> getResourceStartResult() {
		return resourceStartResult;
	}
}
