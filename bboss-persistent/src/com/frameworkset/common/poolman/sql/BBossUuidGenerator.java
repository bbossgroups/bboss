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
package com.frameworkset.common.poolman.sql;

import com.frameworkset.common.poolman.DBUtil;
import com.frameworkset.orm.annotation.PrimaryKey;
import com.frameworkset.util.UUID;

/**
 * <p>Title: BBossUuidGenerator.java</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2007</p>
 * @Date 2016年12月4日 上午12:37:22
 * @author biaoping.yin
 * @version 1.0
 */
public class BBossUuidGenerator   extends BaseIdGenerator {

	/**
	 * 
	 */
	public BBossUuidGenerator() {
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see com.frameworkset.common.poolman.sql.IdGenerator#getNextId()
	 */
	@Override
	public String getNextId() {
		
		return UUID.randomUUID().toString();
	}



}
