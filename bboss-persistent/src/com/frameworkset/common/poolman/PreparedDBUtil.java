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

import com.frameworkset.common.poolman.handle.NullRowHandler;
import com.frameworkset.common.poolman.handle.RowHandler;
import com.frameworkset.common.poolman.util.DBOptions;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * 执行预编sql语句
 * set i parameterIndex the first parameter is 1, the second is 2, ...
 * @author biaoping.yin created on 2005-6-8 version 1.0
 */
public class PreparedDBUtil extends DBOptionsPreparedDBUtil {


	//	/**
//	 * 批处理预编译操作参数集，如果在一次批处理预编操作中出现多条
//	 * Map<sql,Params>
//	 */
//	private Map batchparamsIDXBySQL;


//	protected String[] preparedfields;


	/*******************************************************************
	 * 通用预编译处理开始
	 * 
	 */
	public Object executePrepared() throws SQLException {

		return executePrepared((DBOptions)null);
	}
	/**
	 * 如果getCUDResult参数为true，则返回GetCUDResult类型对象，GetCUDResult的属性含义如下：
	 * result：操作结果，如果数据源autoprimarykey为true，并且在tableinfo表中保存了表的主键信息result为自增的主键，反之result为更新的记录数
	 * updateCount:更新的记录数
	 * keys:自动产生的主键，如果只有一条记录则为普通对象，如果有多条记录则为List<Object>类型
	 * @param getCUDResult
	 * @return
	 * @throws SQLException
	 */
	public Object executePreparedGetCUDResult(boolean getCUDResult) throws SQLException {

		return executePreparedGetCUDResult((DBOptions)null, getCUDResult);
	}
	
	
	public <T> T executePreparedForObject(Class<T> objectType) throws SQLException {

		return executePreparedForObject((DBOptions)null, objectType);
	}
	
	public <T> T[] executePreparedForObjectArray(Class<T> objectType) throws SQLException {

		return executePreparedForObjectArray((DBOptions)null, objectType);
	}
	
	public <T> List<T> executePreparedForList(Class<T> objectType) throws SQLException {

		return executePreparedForList((DBOptions)null, objectType);
	}
	
	public String executePreparedForXML() throws SQLException {

		return executePreparedForXML((DBOptions)null) ;
	}
	
	
	public <T> T executePreparedForObject(Connection con,Class<T> objectType) throws SQLException {

		return executePreparedForObject((DBOptions)null, con, objectType);
	}
	
	public <T> T[] executePreparedForObjectArray(Connection con,Class<T> objectType) throws SQLException {

		return executePreparedForObjectArray((DBOptions)null, con,objectType);
	}
	
	public <T> List<T> executePreparedForList(Connection con,Class<T> objectType) throws SQLException {

		return executePreparedForList((DBOptions)null, con,objectType);
	}
	
	public String executePreparedForXML(Connection con) throws SQLException {

		return executePreparedForXML((DBOptions)null, con);
	}
	
	public void executePreparedWithRowHandler(NullRowHandler rowhandler) throws SQLException
	{

				executePreparedWithRowHandler((DBOptions)null,rowhandler);
	}
	
	public void executePreparedWithRowHandler(Connection con,NullRowHandler rowhandler) throws SQLException
    {

				executePreparedWithRowHandler((DBOptions)null, con,  rowhandler) ;
    }
	
	/******************************************************************
	 * 带行处理器的方法开始
	 *****************************************************************/
	public <T> T executePreparedForObject(Class<T> objectType,RowHandler rowhandler) throws SQLException {

		return executePreparedForObject((DBOptions)null, objectType,  rowhandler);
	}
	
	public <T> T[] executePreparedForObjectArray(Class<T> objectType,RowHandler rowhandler) throws SQLException {

		return executePreparedForObjectArray((DBOptions)null, objectType,  rowhandler);
	}
	
	public <T> List<T> executePreparedForList(Class<T> objectType,RowHandler rowhandler) throws SQLException {

		return executePreparedForList((DBOptions)null, objectType,  rowhandler) ;
	}
	
	public String executePreparedForXML(RowHandler rowhandler) throws SQLException {

		return executePreparedForXML((DBOptions)null, rowhandler);
	}
	
	
	public <T> T executePreparedForObject(Connection con,Class<T> objectType,RowHandler rowhandler) throws SQLException {

		    return executePreparedForObject((DBOptions)null, con,  objectType,  rowhandler);
	}
	
	
	
	public  <T> T[] executePreparedForObjectArray(Connection con,Class<T> objectType,RowHandler rowhandler) throws SQLException {

		return executePreparedForObjectArray((DBOptions)null, con,  objectType,  rowhandler);
	}
	
	public <T> List<T> executePreparedForList(Connection con,Class<T> objectType,RowHandler rowhandler) throws SQLException {

		return executePreparedForList((DBOptions)null, con,  objectType,  rowhandler);
	}
	
	public String executePreparedForXML(Connection con,RowHandler rowhandler) throws SQLException {

		return executePreparedForXML((DBOptions)null, con,  rowhandler) ;
	}


	/************************************************************************
	 * 带行处理器的方法结束
	 **************************************************************************/
	/**
	 * 执行预编译批出理操作,支持事务
	 * @return
	 * @throws SQLException
	 */
	public void executePreparedBatch() throws SQLException
	{

		executePreparedBatch((DBOptions)null);
	}
	
	public void executePreparedBatchGetCUDResult(GetCUDResult getCUDResult) throws SQLException
	{

		executePreparedBatchGetCUDResult((DBOptions)null, getCUDResult);
	}

	public Object executePrepared(Connection con) throws SQLException
	{

		return executePrepared((DBOptions)null, con) ;
	}
	/**
	 * 执行prepare语句，并且返回执行后的结果(例如插入语句后生成的主键值)
	 * 
	 * @return Object 主键值
	 * @throws SQLException
	 */
	public Object executePrepared(Connection con,boolean getCUDResult) throws SQLException {


		return executePrepared((DBOptions)null, con,  getCUDResult);
	}



	public void executePreparedBatch(Connection con_) throws SQLException
	{
		executePreparedBatch((DBOptions)null, con_);
	}
	/**
	 * 执行预编译批出理操作,支持事务，如果con参数本身就是事务链接，则使用该事务链接
	 * 如果con == null则判断外部事务是否存在，如果存在从外部事务中获取一个事务链接来完成批处理操作
	 * 如果不存在则从链接池中获取一个链接,所有的批处理操作不会包含在一个事务之中。
	 * 如果想指定特定数据库的事务则需要调用this.setPrepareDBName(prepareDBName);方法指定执行的逻辑
	 * 数据库名称	
	 * @param con_ 外部传入的数据库链接
	 * @param CUDResult 是否返回处理结果：批处理数据的处理情况，比如更新记录数，自动产生的主键信息
	 * @return
	 * @throws SQLException
	 */
	public void executePreparedBatch(Connection con_,GetCUDResult CUDResult) throws SQLException
	{
		executePreparedBatch((DBOptions)null, con_,  CUDResult);
	}


}

