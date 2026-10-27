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

/**
 * @author biaoping.yin
 * @Date 2026/6/3
 */
public interface RetryCallback<T> {
	/**
	 * 执行和重试处理逻辑，需先调用assertRetryException判断异常是否需要重试，如果无需重试则直接抛出异常
	 * @param throwable 第一次正常执行为null，后续为异常对象
	 * @param retryCount 0 表示第一次正常执行，> 0 表示重试执行
	 * @return
	 */
    T doRetry(Throwable throwable,int retryCount,RetryContext retryContext);
	
	/**
	 * 判断异常是否需要重试，无需重试时，返回false，需要重试时返回true， 默认所有异常都重试
	 * @param throwable
	 * @return
	 */
	default boolean assertRetryException(Throwable throwable){
		return true;
	}
	
	default RetryContext getRetryContext(){
		return null;
	}
}
