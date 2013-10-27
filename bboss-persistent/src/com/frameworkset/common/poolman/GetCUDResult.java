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
 * <p>Title: CUDResult.java</p>
 *
 * <p>Description: 保存数据库增删改的结果信息，GetCUDResult的属性含义如下：
	 * result：操作结果，如果数据源autoprimarykey为true，并且在tableinfo表中保存了表的主键信息result为自增的主键，反之result为更新的记录数
	 * updateCount:更新的记录数
	 * keys:自动产生的主键，如果只有一条记录则为普通对象，如果有多条记录则为List<Object>类型</p>
 *
 * <p>Copyright: Copyright (c) 2007</p>
 * @Date 2012-11-15 下午2:23:17
 * @author biaoping.yin
 * @version 1.0
 */
public class GetCUDResult {
	private Object result;
	private Object updatecount;
	private Object keys;
	public GetCUDResult()
	{
		
	}
	
	public void setGetCUDResult(GetCUDResult origine)
	{
		if(origine == null)
			return;
		this.result = origine.getResult();
		this.keys = origine.getKeys();
		this.updatecount = origine.getUpdatecount();
	}
	
	public GetCUDResult(Object result,Object updatecount,Object keys) {
		super();
		this.result = result;
		this.updatecount = updatecount;
		this.keys = keys;
	}
	

	public Object getResult() {
		return result;
	}

	public Object getUpdatecount() {
		return updatecount;
	}
	public Object getKeys() {
		return this.keys;
	}

	public void setResult(Object result) {
		this.result = result;
	}

	public void setUpdatecount(Object updatecount) {
		this.updatecount = updatecount;
	}

	public void setKeys(Object keys) {
		this.keys = keys;
	}
	

}
