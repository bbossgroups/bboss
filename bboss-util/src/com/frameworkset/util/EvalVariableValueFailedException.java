/**
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
package com.frameworkset.util;

/**
 * <p> EvalVariableValueFailedException.java</p>
 * <p> Description: </p>
 * <p> bboss workgroup </p>
 * <p> Copyright (c) 2009 </p>
 * 
 * @Date 2012-4-6 下午4:31:39
 * @author biaoping.yin
 * @version 1.0
 */
public class EvalVariableValueFailedException extends IllegalArgumentException {
	
	public EvalVariableValueFailedException(String msg)
	{
		super(msg);
	}

	public EvalVariableValueFailedException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public EvalVariableValueFailedException(String arg0, Throwable arg1) {
		super(arg0, arg1);
		// TODO Auto-generated constructor stub
	}

	public EvalVariableValueFailedException(Throwable arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

}
