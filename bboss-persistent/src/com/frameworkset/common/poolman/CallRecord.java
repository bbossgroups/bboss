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
package com.frameworkset.common.poolman;

import java.sql.SQLException;
import java.util.Map;

public class CallRecord extends Record {

	public CallRecord(int i) {
		super(i);
		// TODO Auto-generated constructor stub
	}

	public CallRecord(int i, String[] fields, Map sameCols) {
		super(i, fields, sameCols);
		// TODO Auto-generated constructor stub
	}

	public CallRecord(String[] fields, Map sameCols) {
		super(fields, sameCols);
		// TODO Auto-generated constructor stub
	}

	public CallRecord() {
		// TODO Auto-generated constructor stub
	}

	public CallRecord(int initialCapacity, float loadFactor, String[] fields,
			Map sameCols) {
		super(initialCapacity, loadFactor, fields, sameCols);
		// TODO Auto-generated constructor stub
	}

	public CallRecord(int initialCapacity, float loadFactor) {
		super(initialCapacity, loadFactor);
		// TODO Auto-generated constructor stub
	}

	public CallRecord(Map t) {
		super(t);
		// TODO Auto-generated constructor stub
	}

	public CallRecord(Map t, String[] fields) {
		super(t, fields);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Object getObject(String parameterName) throws SQLException {
		// TODO Auto-generated method stub
		return get(parameterName);
	}

	@Override
	public Object getObject(String parameterName, Map map) throws SQLException {
		// TODO Auto-generated method stub
		return get(parameterName);
	}

}
