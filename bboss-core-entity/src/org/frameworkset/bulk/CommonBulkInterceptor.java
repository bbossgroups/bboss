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
 * @Date 2019/12/7 9:36
 * @author biaoping.yin
 * @version 1.0
 */
public interface CommonBulkInterceptor {
	public void beforeBulk(CommonBulkCommand bulkCommand);

	/**
	 * 成功处理bulk任务回调方法
	 * @param bulkCommand
	 * @param result Elasticsearch返回的response报文
	 */
	public void afterBulk(CommonBulkCommand bulkCommand, BulkResult result);

	/**
	 * 有数据处理失败回调方法
	 * @param bulkCommand
	 * @param result Elasticsearch返回的response报文,包含的失败记录情况
	 */
	public void errorBulk(CommonBulkCommand bulkCommand, BulkResult result);

	/**
	 * 处理异常回调方法
	 * @param bulkCommand
	 * @param exception
	 */
	public void exceptionBulk(CommonBulkCommand bulkCommand, Throwable exception);
}
