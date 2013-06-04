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
package com.frameworkset.common.poolman.interceptor;

import com.frameworkset.common.poolman.util.SQLManager;

public class DummyInterceptor implements InterceptorInf {

	public String convertSQL(String sql, String dbtype, String dbname) {
		return sql;
	}

	public String getDefaultDBName() {
		return SQLManager.getInstance().getPool(null).getDBName();
	}

}
