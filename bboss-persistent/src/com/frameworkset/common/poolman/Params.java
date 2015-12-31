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

import java.util.ArrayList;
import java.util.List;

import com.frameworkset.common.poolman.PreparedDBUtil.UpdateKeyInfo;

/**
 * 
 * 
 * <p>Title: Params</p>
 *
 * <p>Description: 单条预编语句的的参数信息</p>
 *
 * <p>Copyright: Copyright (c) 2007</p>
 *

 * @Date Oct 15, 2008 9:40:17 AM
 * @author biaoping.yin
 * @version 1.0
 */
public class Params implements java.lang.Comparable
{
	private String pagineOrderby;
    public String getPagineOrderby() {
		return pagineOrderby;
	}
	public void setPagineOrderby(String pagineOrderby) {
		this.pagineOrderby = pagineOrderby;
	}
	/**
	 * 指定分页总记录数，通过外部传入，提升分页查询性能
	 */
	long totalsize = -1L;
	/**
	 * 指定分页查询sql语句，通过外部传入，提升分页查询性能
	 * 
	 * totalsize和totalsizesql两个属性一次只能使用一个
	 */
//	SQLInfo totalsizesql;
	public Params()
	{
		params = new ArrayList<Param>();
		
	}
	
	public Params(List<Param> params)
    {
        this.params = params;
        
    }
	/**
	 * 预编译参数列表,单独的预编语句参数
	 * List<Param>
	 */
	List<Param> params ;
	
	
	
	/**
	 * Clob /Blob等一系列大字段相关属性定义
	 */

	List bigdatas = null;

	/**
	 * 批量更新Clob /Blob一系列大字段记录内容时，保存更新记录的条件
	 */
	List conditions = null;

	/**
	 * 更新数据库记录时的主键信息
	 */
	UpdateKeyInfo updateKeyInfo;
	
	/**
	 * -1:无含义 0:insert 1:update 2:delete
	 */
	int action = -1;
	
	NewSQLInfo prepareSqlifo;
	
	public int hashCode()
	{
		return super.hashCode();
	}
	
	public boolean equal(Object other)
	{
		try
		{
			Params _o = (Params)other;
			if(_o.prepareSqlifo.getNewsql().equals(this.prepareSqlifo.getNewsql()))
				return true;
			else
				return false;
		}
		catch(Exception e)
		{
			return false;
		}
		
	}
	
	void clear()
	{
		params = null;
		bigdatas = null;
		conditions = null;
		updateKeyInfo = null;
		action = -1;
//		prepareselect_sql = null;
		prepareSqlifo = null;
		totalsize = -1L;
//		totalsizesql = null;
	}

	public int compareTo(Object other) {
		try
		{
			Params _o = (Params)other;
			return this.prepareSqlifo.getNewsql().compareTo(_o.prepareSqlifo.getNewsql());
			
		}
		catch(Exception e)
		{
			return 0;
		}
	}
	
	public Params copy()
	{
		Params params = new Params();
		params.action = this.action;
		params.bigdatas = this.bigdatas;
		params.conditions = this.conditions;
		params.params = this.params;
//		params.prepareselect_sql = this.prepareselect_sql;
		params.updateKeyInfo = this.updateKeyInfo;
		params.totalsize = this.totalsize;
//		params.totalsizesql = this.totalsizesql;
		params.prepareSqlifo = this.prepareSqlifo;
		return params;
	}

//	public SQLInfo getTotalsizesql() {
//		return totalsizesql;
//	}
//
//	public void setTotalsizesql(SQLInfo totalsizesql) {
//		this.totalsizesql = totalsizesql;
//	}

	public long getTotalsize() {
		return totalsize;
	}

	public void setTotalsize(long totalsize) {
		this.totalsize = totalsize;
	}
}
