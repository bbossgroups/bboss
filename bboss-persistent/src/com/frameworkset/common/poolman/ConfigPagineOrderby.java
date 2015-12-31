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
 * 
 * <p>Title: ConfigPagineOrderby.java</p>
 *
 * <p>Description: 从sql配置文件中读取row_over排序条件</p>
 *
 * <p>Copyright: Copyright (c) 2007</p>
 * @Date 2015年12月31日 上午10:46:12
 * @author biaoping.yin
 * @version 1.0
 */
public class ConfigPagineOrderby extends PagineOrderby {

	public ConfigPagineOrderby(String pagineOrderbyName, Object conditionBean) {
		super(pagineOrderbyName, conditionBean,true);
		// TODO Auto-generated constructor stub
	}

	 

	/**
	 * 
	 * @param pagineOrderby 
	 * @param conditionBean
	 * @param plain
	 */
	
	public ConfigPagineOrderby(String pagineOrderbyName, Object conditionBean,  boolean plain) {
		super(pagineOrderbyName, conditionBean, true, plain);
		// TODO Auto-generated constructor stub
	}

	public ConfigPagineOrderby(String pagineOrderbyName) {
		super(pagineOrderbyName);
		// TODO Auto-generated constructor stub
	}

}
