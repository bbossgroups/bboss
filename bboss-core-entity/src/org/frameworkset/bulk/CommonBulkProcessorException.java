package org.frameworkset.bulk;
/**
 * Copyright 2008 biaoping.yin
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


/**
 * <p>Description: </p>
 * <p></p>
 * <p>Copyright (c) 2018</p>
 * @Date 2019/12/7 9:53
 * @author biaoping.yin
 * @version 1.0
 */
public class CommonBulkProcessorException extends RuntimeException {
	public CommonBulkProcessorException() {
		super();
	}



	public CommonBulkProcessorException(String message, Throwable cause) {
		super(message, cause);
	}

	public CommonBulkProcessorException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}



	public CommonBulkProcessorException(String message) {
		super(message);
	}

	public CommonBulkProcessorException(Throwable cause) {
		super(cause);
	}



}
