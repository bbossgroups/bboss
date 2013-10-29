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

package org.frameworkset.spi.support.validate;

/**
 * <p>Title: BindException.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2007</p>
 * @Date 2010-12-25 下午01:06:36
 * @author biaoping.yin
 * @version 1.0
 */
public class BindException extends Exception {

	private BindingResult bindingResult;

	public BindException() {
		// TODO Auto-generated constructor stub
	}

	public BindException(BindingResult bindingResult) {
		this.bindingResult = bindingResult;
		// TODO Auto-generated constructor stub
	}

	public BindException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	public BindException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	protected BindingResult getBindingResult() {
		return bindingResult;
	}

}
