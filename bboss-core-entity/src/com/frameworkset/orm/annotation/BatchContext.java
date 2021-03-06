package com.frameworkset.orm.annotation;
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

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>Description: eval onlyone time indexname and avoid multibuilding indexname and indextype</p>
 * <p></p>
 * <p>Copyright (c) 2018</p>
 * @Date 2019/5/7 9:48
 * @author biaoping.yin
 * @version 1.0
 */
public class BatchContext implements Serializable {
//	private String indexName;
	private String indexType;
	private Map<String,String> realNames = new HashMap<String, String>();

	public String getIndexName(String originName) {
		return realNames.get(originName);
	}

	public void setIndexName(String originName,String indexName) {
		realNames.put(originName,indexName);
//		this.indexName = indexName;
	}

	public String getIndexType() {
		return indexType;
	}

	public void setIndexType(String indexType) {
		this.indexType = indexType;
	}
}
