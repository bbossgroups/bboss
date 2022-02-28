package com.frameworkset.common.poolman.util;
/**
 * Copyright 2020 bboss
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
 * <p>Description: 数据库操作参数配置</p>
 * <p></p>
 * <p>Copyright (c) 2020</p>
 * @Date 2022/2/28 12:44
 * @author biaoping.yin
 * @version 1.0
 */
public class DBOptions {
	private Integer fetchSize;

	public Integer getFetchSize() {
		return fetchSize;
	}

	public void setFetchSize(Integer fetchSize) {
		this.fetchSize = fetchSize;
	}

	public boolean hasFetchSize(){
		return getFetchSize() != null && getFetchSize() != 0;
	}

}
