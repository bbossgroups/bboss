package org.frameworkset.util;
/**
 * Copyright 2026 bboss
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
 *
 * @author biaoping.yin
 * @Date 2026/10/27
 */
public class RetryContext {
	private Map<String,Object> attributes;
	/**
	 * 是否终止重试，如果终止重试，则直接抛出当前异常
	 */
	private boolean terminateRetry;
	
	public boolean isTerminateRetry() {
		return terminateRetry;
	}
	public RetryContext setTerminateRetry(boolean terminateRetry) {
		this.terminateRetry = terminateRetry;
		return this;
	}
	
	public Object getAttribute(String name){
		if(attributes == null)
			return null;
		return attributes.get(name);
	}
	
	public RetryContext addAttribute(String name,Object value){
		if(attributes == null)
			attributes = new java.util.HashMap<String,Object>();
		attributes.put(name, value);
		return this;
	}
	
}
