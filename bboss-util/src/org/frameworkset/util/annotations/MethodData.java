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
package org.frameworkset.util.annotations;

import java.util.Map;

/**
 * <p>Title: MethodData.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2008</p>
 * @Date 2010-11-7
 * @author biaoping.yin
 * @version 1.0
 */
public class MethodData {
	private MethodInfo methodInfo;
	private Map pathVariableDatas;
//	private String pathPattern ;
//	private String baseurl;
//	private String lookuppath;
	public MethodData(MethodInfo methodInfo,Map pathVariableDatas)
	{
		this.methodInfo = methodInfo;
		this.pathVariableDatas = pathVariableDatas;
//		this.pathPattern = pathPattern;
//		this.baseurl = baseurl;
//		this.lookuppath = lookuppath;
		
	}
	public MethodInfo getMethodInfo() {
		return methodInfo;
	}
//	public void setMethodInfo(MethodInfo methodInfo) {
//		this.methodInfo = methodInfo;
//	}
	public Map getPathVariableDatas() {
		return pathVariableDatas;
	}
//	public void setPathVariableDatas(Map pathVariableDatas) {
//		this.pathVariables = pathVariables;
//	}
//	public String getPathPattern() {
//		return pathPattern;
//	}
//	public String getBaseurl() {
//		return baseurl;
//	}
//	public String getLookuppath() {
//		return lookuppath;
//	}

}
