/**
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

package org.frameworkset.gencode.web.service;

/**
 * <p>
 * Title: GencodeException
 * </p>
 * <p>
 * Description: 代码生成管理异常处理类
 * </p>
 * <p>
 * bboss
 * </p>
 * <p>
 * Copyright (c) 2007
 * </p>
 * 
 * @Date 2015-04-18 20:44:21
 * @author yinbp
 * @version v1.0
 */
public class GencodeException extends RuntimeException {

	public GencodeException() {
		super();
	}

	public GencodeException(String message, Throwable cause) {
		super(message, cause);
	}

	public GencodeException(String message) {
		super(message);
	}

	public GencodeException(Throwable cause) {
		super(cause);
	}

}