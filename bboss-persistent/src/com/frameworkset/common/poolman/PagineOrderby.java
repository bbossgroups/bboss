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

import java.io.Serializable;

/**
 * 
 * <p>Title: PagineOrderby.java</p>
 *
 * <p>Description:
 * 封装用于传递ms sql,db2，oracle，mysql，mariadb，postgres,sqlite，derby高效分页sql机制 
 * ROW_NUMBER () OVER (ORDER BY bm)中的排序条件
 * </p>
 *
 * <p>Copyright: Copyright (c) 2007</p>
 * @Date 2015年12月29日 下午3:25:15
 * @author biaoping.yin
 * @version 1.0
 */
public class PagineOrderby implements Serializable {
	private boolean plain = true;
	public boolean isPlain() {
		return plain;
	}
	private boolean config;
	public boolean isConfig() {
		return config;
	}
	public String toString(String errorinfo)
	{
		StringBuilder ret = new StringBuilder();
		ret.append("pagine Order by:").append(pagineOrderby)
		.append(",is plain :").append(plain)
		.append(",is from config:").append(config);
		if(errorinfo != null)
		{
			ret.append(",")		
			.append(errorinfo);
		}
		return ret.toString();
	}
 
	
	/**
	 * 模板排序条件和
	 * @param pagineOrderby
	 * @param conditionBean
	 */
	
	public PagineOrderby(String pagineOrderby, Object conditionBean) {
//		plain = false;
//		this.pagineOrderby = pagineOrderby;
//		this.conditionBean = conditionBean;
		this(pagineOrderby, conditionBean,false,false);
	}
	/**
	 * 
	 * @param pagineOrderby 分页行号排序条件
	 * @param conditionBean 查询条件对象
	 * @param config 条件是否需要读取配置文件
	 */
	public PagineOrderby(String pagineOrderby, Object conditionBean,boolean config ) {
//		plain = false;
//		this.pagineOrderby = pagineOrderby;
//		this.conditionBean = conditionBean;
//		this.config = config; 
//		
		this(pagineOrderby, conditionBean,config ,false);
	}
	
	public PagineOrderby(String pagineOrderby, Object conditionBean,boolean config ,boolean plain) {
		this.plain = plain;
		this.pagineOrderby = pagineOrderby;
		this.conditionBean = conditionBean;
		this.config = config; 
	}
	public PagineOrderby(String pagineOrderby) {
		plain = true;
		this.pagineOrderby = pagineOrderby;
		
	}
	private String pagineOrderby;
	public String getPagineOrderby() {
		return pagineOrderby;
	}
	public Object getConditionBean() {
		return conditionBean;
	}
	protected Object conditionBean;

}
