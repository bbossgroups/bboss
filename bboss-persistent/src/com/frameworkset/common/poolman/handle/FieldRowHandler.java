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
package com.frameworkset.common.poolman.handle;

import com.frameworkset.common.poolman.Record;


public abstract class FieldRowHandler<T> extends BaseRowHandler<T> {

	
	public T handleField_(Record record) throws Exception
	{
		return handleField( record);
	}
	
	public abstract T handleField(Record record) throws Exception;

	@Override
	public void handleRow(T rowValue, Record record) throws Exception
	{

		throw new UnsupportedOperationException("handleRow(T rowValue, Record record)");
		
	}
	
}

