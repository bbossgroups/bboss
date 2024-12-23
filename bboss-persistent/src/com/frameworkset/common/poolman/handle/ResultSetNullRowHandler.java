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

import java.sql.ResultSet;

import com.frameworkset.common.poolman.Record;

public abstract class ResultSetNullRowHandler extends NullRowHandler {

	@Override
	public final void handleRow(Record origine) throws Exception {
		// TODO Auto-generated method stub
		
	}
	
	public abstract void handleRow(ResultSet rs) throws Exception;

	@Override
	public void handleRow(Object rowValue, Record origine) throws Exception {
		handleRow((ResultSet) rowValue);
	}
}
