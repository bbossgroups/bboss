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

/**
 * <p>Description: </p>
 * <p></p>
 * <p>Copyright (c) 2018</p>
 * @Date 2019/5/7 10:39
 * @author biaoping.yin
 * @version 1.0
 */
public abstract class BaseESGetVariableValue implements ESIndexWrapper.GetVariableValue{
	@Override
	public BatchContext getBatchContext() {
		return batchContext;
	}

	@Override
	public void setBatchContext(BatchContext batchContext) {
		this.batchContext = batchContext;
	}

	protected BatchContext batchContext;
}
