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

package org.frameworkset.spi.interceptor;

/**
 * <p>Title: InterruptionException.java</p> 
 * <p>Description: 如果在拦截器中抛出InterruptionException，将导致程序的正常逻辑结束，主要应用于before
 * 方法，如果后续还有拦截器没有执行则都不执行，
 * 并且所有已经执行before方法的拦截器的后续方法也全部都不执行</p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2007</p>
 * @Date 2012-11-27 下午3:04:29
 * @author biaoping.yin
 * @version 1.0
 */
public class InterruptionException extends RuntimeException{

	public InterruptionException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public InterruptionException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public InterruptionException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public InterruptionException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

}
