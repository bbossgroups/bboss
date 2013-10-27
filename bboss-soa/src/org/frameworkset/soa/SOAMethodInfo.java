/*
 *  Copyright 2008-2010 biaoping.yin
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
package org.frameworkset.soa;


/**
 * <p>Title: SOAMethodInfo.java</p> 
 * <p>Description: 描述服务调用中</p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2008</p>
 * @Date 2011-5-10 上午11:39:10
 * @author biaoping.yin
 * @version 1.0
 */
public class SOAMethodInfo {
	private String methodName;
	private Object[] params;
	private Class[] paramTypes;
	public SOAMethodInfo()
	{
		
	}
	public SOAMethodInfo(String methodName, Object[] params, Class[] paramTypes) {
		super();
		this.methodName = methodName;
		this.params = params;
		this.paramTypes = paramTypes;
	}
	/**
	 * @return the methodName
	 */
	public String getMethodName() {
		return methodName;
	}
	/**
	 * @param methodName the methodName to set
	 */
	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}
	/**
	 * @return the params
	 */
	public Object[] getParams() {
		return params;
	}
	/**
	 * @param params the params to set
	 */
	public void setParams(Object[] params) {
		this.params = params;
	}
	/**
	 * @return the paramTypes
	 */
	public Class[] getParamTypes() {
		return paramTypes;
	}
	/**
	 * @param paramTypes the paramTypes to set
	 */
	public void setParamTypes(Class[] paramTypes) {
		this.paramTypes = paramTypes;
	}

}
