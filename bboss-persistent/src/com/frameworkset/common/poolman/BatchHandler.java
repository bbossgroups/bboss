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
/**
 * 轻量级jdbc批处理操作记录设置器
 */

import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface BatchHandler<T> {
	/**
	 *
	 * @param stmt jdbc PreparedStatement   parameterIndex the first parameter is 1, the second is 2, ...
	 *                                      x the parameter value
	 * @param record 当前操作的变量
	 * @param i 行索引
	 * @throws SQLException
	 */
	public void handler(PreparedStatement stmt,T record,int i) throws SQLException;

}
