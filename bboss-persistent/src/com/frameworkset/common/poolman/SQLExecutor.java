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

import com.frameworkset.common.poolman.handle.FieldRowHandler;
import com.frameworkset.common.poolman.handle.NullRowHandler;
import com.frameworkset.common.poolman.handle.RowHandler;
import com.frameworkset.common.poolman.util.DBOptions;
import com.frameworkset.util.ListInfo;

import java.sql.SQLException;
import java.util.List;

/**
 * <p>Title: SQLExecutor.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2008</p>
 * @Date 2010-3-11 下午01:46:25
 * @author biaoping.yin
 * @version 1.0
 */
public class SQLExecutor extends DBOPtionsSQLExecutor
{

	//    protected String dbname;


	public Object execute() throws SQLException
    {
    	return execute((DBOptions)null);
        
        
    }

	//	public static void init(SQLParams sqlparams,String statement,String pretoken,String endtoken,String action)
//	{
////		this.sqlparams = new SQLParams();
//		sqlparams.setOldsql( statement);
//		if(action != null)
//		{
//			if(action.equals(ACTION_INSERT))
//				sqlparams.setAction(PreparedDBUtil.INSERT);
//			else if(action.equals(ACTION_DELETE))
//				sqlparams.setAction(PreparedDBUtil.DELETE);
//			else if(action.equals(ACTION_UPDATE))
//				sqlparams.setAction(PreparedDBUtil.UPDATE);
//		}
//		sqlparams.setPretoken(pretoken);
//		sqlparams.setEndtoken(endtoken);
//		//http://changsha.koubei.com/store/detail--storeId-b227fc4aee6e4862909ea7bf62556a7a
//		
//		
//	}


	public static void insertBeans(String dbname, String sql, List beans) throws SQLException {


				insertBeans((DBOptions) null, dbname,   sql,   beans);
	}
	
	public static void insertBeans(String dbname, String sql, List beans,GetCUDResult getCUDResult) throws SQLException {


				insertBeans((DBOptions) null, dbname,   sql,   beans,  getCUDResult);
	}
	

	
	public static Object update( String sql, Object... fields) throws SQLException {
//		return execute(null, sql,PreparedDBUtil.UPDATE, fields);

				return update( (DBOptions) null, sql, fields) ;
	}
	



	public static Object delete(String sql, Object... fields) throws SQLException {
//		return execute(null, sql,PreparedDBUtil.DELETE, fields);
		return delete((DBOptions) null, sql,fields);
		
	}
	
//	public static void deleteByKeys(String sql, Object... fields) throws SQLException {
//		executeBatch(null, sql,PreparedDBUtil.DELETE, fields);
//		
//	}
//	public static void deleteByKeysWithDBName(String dbname,String sql, Object... fields) throws SQLException {
//		executeBatch(null, sql,PreparedDBUtil.DELETE, fields);
//		
//	}
	
	
	public static void deleteByKeys(String sql, int... fields) throws SQLException {
//		executeBatch(null, sql,PreparedDBUtil.DELETE, fields);

				deleteByKeys((DBOptions) null, sql, fields);
		
	}
	public static void deleteByKeysWithDBName(String dbname,String sql, int... fields) throws SQLException {
//		executeBatch(dbname, sql,PreparedDBUtil.DELETE, fields);

				deleteByKeysWithDBName((DBOptions) null, dbname,  sql,   fields);
		
	}
	
	public static void deleteByLongKeys(String sql, long... fields) throws SQLException {
//		executeBatch(null, sql,PreparedDBUtil.DELETE, fields);

				deleteByLongKeys((DBOptions) null, sql,  fields) ;
		
	}
	public static void deleteByLongKeysWithDBName(String dbname,String sql, long... fields) throws SQLException {
//		executeBatch(dbname, sql,PreparedDBUtil.DELETE, fields);

				deleteByLongKeysWithDBName((DBOptions) null, dbname,  sql,  fields);
		
	}
	
	public static <T> void executeBatch(String sql,List<T> datas,int batchsize, BatchHandler<T> batchHandler) throws SQLException{

				executeBatch((DBOptions) null, sql, datas,  batchsize,  batchHandler) ;
	}
	
	public static <T> void executeBatch(String dbname,String sql,List<T> datas,int batchsize, BatchHandler<T> batchHandler) throws SQLException{

				executeBatch((DBOptions) null, dbname,  sql, datas,  batchsize,   batchHandler);
	}
	
	
	public static void updateByKeys(String sql, int... fields) throws SQLException {
//		executeBatch(null, sql,PreparedDBUtil.DELETE, fields);

				updateByKeys((DBOptions) null, sql,  fields);
		
	}
	public static void updateByKeysWithDBName(String dbname,String sql, int... fields) throws SQLException {
//		executeBatch(dbname, sql,PreparedDBUtil.DELETE, fields);

				updateByKeysWithDBName((DBOptions) null, dbname,  sql,   fields) ;
		
	}
	
	public static void updateByLongKeys(String sql, long... fields) throws SQLException {
//		executeBatch(null, sql,PreparedDBUtil.DELETE, fields);

				updateByLongKeys((DBOptions) null, sql,   fields);
		
	}
	public static void updateByLongKeysWithDBName(String dbname,String sql, long... fields) throws SQLException {
//		executeBatch(dbname, sql,PreparedDBUtil.DELETE, fields);

				updateByLongKeysWithDBName((DBOptions) null, dbname,  sql,  fields);
		
	}
	
	public static void updateByKeys(String sql, String... fields) throws SQLException {
//		executeBatch(null, sql,PreparedDBUtil.DELETE, fields);

				updateByKeys((DBOptions) null, sql,  fields);
		
	}
	public static void updateByKeysWithDBName(String dbname,String sql, String... fields) throws SQLException {
//		executeBatch(dbname, sql,PreparedDBUtil.DELETE, fields);

				updateByKeysWithDBName((DBOptions) null, dbname,  sql,   fields) ;
		
	}
	
	public static void deleteByKeys(String sql, String... fields) throws SQLException {
//		executeBatch(null, sql,PreparedDBUtil.DELETE, fields);

				deleteByKeys((DBOptions) null, sql,   fields);
		
	}
	public static void deleteByKeysWithDBName(String dbname,String sql, String... fields) throws SQLException {
//		executeBatch(dbname, sql,PreparedDBUtil.DELETE, fields);

				deleteByKeysWithDBName((DBOptions) null, dbname,  sql,   fields);
		
	}
	
	
	public static void deleteByShortKeys(String sql, short... fields) throws SQLException {
//		executeBatch(null, sql,PreparedDBUtil.DELETE, fields);

				deleteByShortKeys((DBOptions) null, sql,   fields);
		
	}
	public static void deleteByShortKeysWithDBName(String dbname,String sql, short... fields) throws SQLException {
//		executeBatch(dbname, sql,PreparedDBUtil.DELETE, fields);

				deleteByShortKeysWithDBName((DBOptions) null, dbname,  sql,   fields) ;
	}
	
	public static void updateByShortKeys(String sql, short... fields) throws SQLException {
//		executeBatch(null, sql,PreparedDBUtil.DELETE, fields);

				updateByShortKeys((DBOptions) null, sql,  fields);
		
	}
	public static void updateByShortKeysWithDBName(String dbname,String sql, short... fields) throws SQLException {
//		executeBatch(dbname, sql,PreparedDBUtil.DELETE, fields);

				updateByShortKeysWithDBName((DBOptions) null, dbname,  sql,  fields);
	}



	public static Object insert(String sql, Object... fields) throws SQLException {
//		return execute(null, sql,PreparedDBUtil.INSERT, fields);

		return insert((DBOptions) null, sql, fields) ;
	}
	
	public static Object updateWithDBName(String dbname, String sql, Object... fields) throws SQLException {
//		return execute(dbname, sql,PreparedDBUtil.UPDATE, fields);

		return updateWithDBName((DBOptions) null, dbname,   sql,  fields) ;
	}
	
	public static Object deleteWithDBName(String dbname, String sql, Object... fields) throws SQLException {
//		return execute(dbname, sql,PreparedDBUtil.DELETE, fields);

		return deleteWithDBName((DBOptions) null, dbname,   sql,  fields) ;
		
	}



	public static Object insertWithDBName(String dbname, String sql, Object... fields) throws SQLException {
//		return execute(dbname, sql,PreparedDBUtil.INSERT, fields);

		return insertWithDBName((DBOptions) null, dbname,   sql,   fields);
	}

	public static void updateBeans(String dbname, String sql, List beans) throws SQLException {

				updateBeans((DBOptions) null, dbname,   sql,   beans);
	}
	
	public static void updateBeans(String dbname, String sql, List beans,GetCUDResult GetCUDResult) throws SQLException {


				updateBeans(	(DBOptions) null, dbname,   sql,   beans,  GetCUDResult) ;
	}



	public static void deleteBeans(String dbname, String sql, List beans) throws SQLException {

				deleteBeans((DBOptions) null, dbname,   sql,   beans);
		
	}
	
	public static void deleteBeans(String dbname, String sql, List beans,GetCUDResult GetCUDResult) throws SQLException {

				deleteBeans((DBOptions) null, dbname,   sql,   beans,  GetCUDResult);
		
	}



	public static void insertBean(String dbname, String sql, Object bean) throws SQLException {

				insertBean((DBOptions) null, dbname,   sql,   bean) ;
	}
	
	public static void insertBean(String dbname, String sql, Object bean,GetCUDResult getCUDResult) throws SQLException {

				insertBean((DBOptions) null, dbname,   sql,   bean,  getCUDResult);
	}



	public static void updateBean(String dbname, String sql, Object bean) throws SQLException {

				updateBean((DBOptions) null, dbname,   sql,   bean) ;
	
	}
	
	public static void updateBean(String dbname, String sql, Object bean,GetCUDResult getCUDResult) throws SQLException {


				updateBean( (DBOptions) null, dbname,   sql,   bean,  getCUDResult) ;
	}
	
	public static void updateBean( String sql, Object bean,GetCUDResult getCUDResult) throws SQLException {
//		if(bean == null )
//			return ;
//		List datas = new ArrayList();
//		datas.add(bean);
//		updateBeans( null,  sql,  datas);

				updateBean( (DBOptions) null, sql,   bean,  getCUDResult) ;
	}

	public static void deleteBean( String sql, Object bean,GetCUDResult getCUDResult) throws SQLException {

				deleteBean( (DBOptions) null, sql,   bean,  getCUDResult) ;
	}

	public static void deleteBean(String dbname, String sql, Object bean) throws SQLException {

				deleteBean(	(DBOptions) null, dbname,   sql,   bean);
	}
	public static void deleteBean(String dbname, String sql, Object bean,GetCUDResult getCUDResult) throws SQLException {

				deleteBean((DBOptions) null, dbname,   sql,   bean,  getCUDResult) ;
	}
	
	public static void insertBeans(String sql, List beans) throws SQLException {

				insertBeans( (DBOptions) null,sql, beans);
	}
	
	public static void insertBeans(String sql, List beans,GetCUDResult getCUDResult) throws SQLException {

				insertBeans( (DBOptions) null,sql, beans,getCUDResult);
	}
	
	



	public static void updateBeans( String sql, List beans) throws SQLException {
		updateBeans( (DBOptions) null,sql, beans);
	}



	public static void deleteBeans( String sql, List beans) throws SQLException {
		deleteBeans( (DBOptions) null,sql, beans);
		
	}



	public static void insertBean( String sql, Object bean) throws SQLException {
//		if(bean == null)
//			return ;
//		List datas = new ArrayList();
//		datas.add(bean);
//		insertBeans( null,  sql,  datas);

				insertBean( (DBOptions) null,sql, bean);
	}
	public static void insertBean( String sql, Object bean,GetCUDResult getCUDResult) throws SQLException {
//		if(bean == null)
//			return ;
//		List datas = new ArrayList();
//		datas.add(bean);
//		insertBeans( null,  sql,  datas);

				insertBean( (DBOptions) null,sql, bean,getCUDResult);
	}



	public static void updateBean( String sql, Object bean) throws SQLException {
//		if(bean == null )
//			return ;
//		List datas = new ArrayList();
//		datas.add(bean);
//		updateBeans( null,  sql,  datas);

				updateBean((DBOptions) null,sql, bean);
	}

	

	public static void deleteBean(String sql, Object bean) throws SQLException {

//		if(bean == null)
//			return ;
//		List datas = new ArrayList();
//		datas.add(bean);
//		deleteBeans( null,  sql,  datas);
		deleteBean(  (DBOptions) null,sql,   bean);
		
	}
	
	
	public static <T> List<T> queryList(Class<T> beanType, String sql, Object... fields) throws SQLException
	{


		return queryList((DBOptions) null, beanType,   sql,  fields);
	}
	/**
	 * 
	 * @param beanType
	 * @param dbname
	 * @param sql
	 * @param offset
	 * @param pagesize
	 * @param fields
	 * @return
	 * @throws SQLException
	 */
	public static ListInfo queryListInfoWithDBName(Class<?> beanType,String dbname, String sql, long offset,int pagesize,Object... fields) throws SQLException
	{
		
//		PreparedDBUtil dbutil = new PreparedDBUtil();
//		dbutil.preparedSelect(dbname, sql,offset,pagesize);
//		if(fields != null && fields.length > 0)
//		{
//			for(int i = 0; i < fields.length ; i ++)
//			{
//				
//				Object field = fields[i];
//				dbutil.setObject(i + 1, field);
//			}
//		}
//		
//		
//		ListInfo datas = new ListInfo();
//		datas.setDatas(dbutil.executePreparedForList(beanType));
//		datas.setTotalSize(dbutil.getLongTotalSize());
//		return datas;

		return queryListInfoWithDBName((DBOptions) null,beanType,  dbname,   sql,   offset,  pagesize, fields);
	}
	public static ListInfo queryListInfoWithDBName2ndTotalsize(Class<?> beanType,String dbname, String sql, long offset,int pagesize,long totalsize,Object... fields) throws SQLException
	{
		
//		PreparedDBUtil dbutil = new PreparedDBUtil();
//		dbutil.preparedSelect(dbname, sql,offset,pagesize,totalsize);
//		if(fields != null && fields.length > 0)
//		{
//			for(int i = 0; i < fields.length ; i ++)
//			{
//				
//				Object field = fields[i];
//				dbutil.setObject(i + 1, field);
//			}
//		}
//		
//		
//		ListInfo datas = new ListInfo();
//		datas.setDatas(dbutil.executePreparedForList(beanType));
//		datas.setTotalSize(dbutil.getLongTotalSize());
//		return datas;	
		return queryListInfoWithDBName2ndTotalsize((DBOptions) null, beanType,  dbname,   sql,   offset,  pagesize,  totalsize,fields) ;
	}
	public static ListInfo queryListInfoWithDBName2ndTotalsizesql(Class<?> beanType,String dbname, String sql, long offset,int pagesize,String totalsizesql,Object... fields) throws SQLException
	{
		
//		PreparedDBUtil dbutil = new PreparedDBUtil();
//		dbutil.preparedSelectWithTotalsizesql(dbname, sql,offset,pagesize,totalsizesql);
//		if(fields != null && fields.length > 0)
//		{
//			for(int i = 0; i < fields.length ; i ++)
//			{
//				
//				Object field = fields[i];
//				dbutil.setObject(i + 1, field);
//			}
//		}
//		
//		
//		ListInfo datas = new ListInfo();
//		datas.setDatas(dbutil.executePreparedForList(beanType));
//		datas.setTotalSize(dbutil.getLongTotalSize());
//		return datas;


		return queryListInfoWithDBName2ndTotalsizesql((DBOptions) null,beanType,  dbname,   sql,   offset,  pagesize,  totalsizesql,  fields) ;
	}
	/**
	 * 
	 * @param beanType
	 * @param sql
	 * @param offset
	 * @param pagesize
	 * @param fields
	 * @return
	 * @throws SQLException
	 */
	public static ListInfo queryListInfo(Class<?> beanType, String sql, long offset,int pagesize,Object... fields) throws SQLException
	{

		return queryListInfo((DBOptions) null, beanType,   sql,   offset,  pagesize, fields);
	}
	
	public static ListInfo queryListInfoWithTotalsize(Class<?> beanType, String sql, long offset,int pagesize,long totalsize,Object... fields) throws SQLException
	{

		return queryListInfoWithTotalsize((DBOptions) null, beanType,   sql,   offset,  pagesize,  totalsize, fields);
	}
	
	public static ListInfo queryListInfoWithTotalsizesql(Class<?> beanType, String sql, long offset,int pagesize,String totalsizesql,Object... fields) throws SQLException
	{

		return queryListInfoWithDBName2ndTotalsizesql((DBOptions) null,beanType, null,sql, offset,pagesize,totalsizesql,fields);
	}
	
	
	public static <T> T queryObject(Class<T> beanType, String sql, Object... fields) throws SQLException
	{

		return queryObjectWithDBName((DBOptions) null,beanType,null, sql, fields);
		
		 
	}
	
	public static <T> List<T> queryListWithDBName(Class<T> beanType,String dbname, String sql, Object... fields) throws SQLException
	{
		
//		PreparedDBUtil dbutil = new PreparedDBUtil();
//		dbutil.preparedSelect(dbname, sql);
//		if(fields != null && fields.length > 0)
//		{
//			for(int i = 0; i < fields.length ; i ++)
//			{
//				
//				Object field = fields[i];
//				dbutil.setObject(i + 1, field);
//			}
//		}
//		
//		
//		
//		return dbutil.executePreparedForList(beanType);

		return queryListWithDBName((DBOptions) null, beanType,  dbname,   sql,   fields) ;
	}
	
	
	public static <T> T queryObjectWithDBName(Class<T> beanType,String dbname, String sql, Object... fields) throws SQLException
	{
		
//		PreparedDBUtil dbutil = new PreparedDBUtil();
//		dbutil.preparedSelect(dbname, sql);
//		if(fields != null && fields.length > 0)
//		{
//			for(int i = 0; i < fields.length ; i ++)
//			{
//				
//				Object field = fields[i];
//				dbutil.setObject(i + 1, field);
//			}
//		}
//		
//		return (T)dbutil.executePreparedForObject(beanType);

		return queryObjectWithDBName((DBOptions) null, beanType,  dbname,   sql, fields);
		 
	}
	
	
	public static <T> List<T> queryListByRowHandler(RowHandler rowhandler,Class<T> beanType, String sql, Object... fields) throws SQLException
	{


		return queryListWithDBNameByRowHandler((DBOptions) null,rowhandler,beanType,null, sql, fields);
	}
	/**
	 * 
	 * @param rowhandler
	 * @param beanType
	 * @param dbname
	 * @param sql
	 * @param offset
	 * @param pagesize
	 * @param fields
	 * @return
	 * @throws SQLException
	 */
	public static ListInfo queryListInfoWithDBNameByRowHandler(RowHandler rowhandler,Class<?> beanType,String dbname, String sql, long offset,int pagesize,Object... fields) throws SQLException
	{
		
//		PreparedDBUtil dbutil = new PreparedDBUtil();
//		dbutil.preparedSelect(dbname, sql,offset,pagesize);
//		if(fields != null && fields.length > 0)
//		{
//			for(int i = 0; i < fields.length ; i ++)
//			{
//				
//				Object field = fields[i];
//				dbutil.setObject(i + 1, field);
//			}
//		}
//		
//		
//		ListInfo datas = new ListInfo();
//		datas.setDatas(dbutil.executePreparedForList(beanType,rowhandler));
//		datas.setTotalSize(dbutil.getLongTotalSize());
//		return datas;

		return queryListInfoWithDBNameByRowHandler((DBOptions) null, rowhandler, beanType,  dbname,   sql,   offset,  pagesize, fields);
	}
	
	public static ListInfo queryListInfoWithDBName2ndTotalsizeByRowHandler(RowHandler rowhandler,Class<?> beanType,String dbname, String sql,
																		   long offset,int pagesize,long totalsize,Object... fields) throws SQLException
	{
		
//		PreparedDBUtil dbutil = new PreparedDBUtil();
//		dbutil.preparedSelect(dbname, sql,offset,pagesize,totalsize);
//		if(fields != null && fields.length > 0)
//		{
//			for(int i = 0; i < fields.length ; i ++)
//			{
//				
//				Object field = fields[i];
//				dbutil.setObject(i + 1, field);
//			}
//		}
//		
//		
//		ListInfo datas = new ListInfo();
//		datas.setDatas(dbutil.executePreparedForList(beanType,rowhandler));
//		datas.setTotalSize(dbutil.getLongTotalSize());
//		return datas;

		return queryListInfoWithDBName2ndTotalsizeByRowHandler((DBOptions) null, rowhandler,  beanType,  dbname,   sql,
		  offset,  pagesize,  totalsize, fields);
	}
	
	public static ListInfo queryListInfoWithDBName2ndTotalsizesqlByRowHandler(RowHandler rowhandler,Class<?> beanType,String dbname, String sql,
																			  long offset,int pagesize,String totalsizesql,Object... fields) throws SQLException
	{
		
//		PreparedDBUtil dbutil = new PreparedDBUtil();
//		dbutil.preparedSelectWithTotalsizesql(dbname, sql,offset,pagesize,totalsizesql);
//		if(fields != null && fields.length > 0)
//		{
//			for(int i = 0; i < fields.length ; i ++)
//			{
//				
//				Object field = fields[i];
//				dbutil.setObject(i + 1, field);
//			}
//		}
//		
//		
//		ListInfo datas = new ListInfo();
//		datas.setDatas(dbutil.executePreparedForList(beanType,rowhandler));
//		datas.setTotalSize(dbutil.getLongTotalSize());
//		return datas;


		return queryListInfoWithDBName2ndTotalsizesqlByRowHandler((DBOptions) null, rowhandler, beanType,  dbname,   sql,
		  offset,  pagesize,  totalsizesql,  fields);
	}
	/**
	 * 
	 * @param rowhandler
	 * @param beanType
	 * @param sql
	 * @param offset
	 * @param pagesize
	 * @param fields
	 * @return
	 * @throws SQLException
	 */
	public static ListInfo queryListInfoByRowHandler(RowHandler rowhandler,Class<?> beanType, String sql, long offset,int pagesize,Object... fields) throws SQLException
	{

		return queryListInfoWithDBNameByRowHandler((DBOptions) null, rowhandler,beanType, null,sql, offset,pagesize,fields);
	}
	
	public static ListInfo queryListInfoWithTotalsizeByRowHandler(RowHandler rowhandler,Class<?> beanType, String sql, long offset,int pagesize,long totalsize,Object... fields) throws SQLException
	{

		return queryListInfoWithDBName2ndTotalsizeByRowHandler((DBOptions) null, rowhandler,beanType, null,sql, offset,pagesize,totalsize,fields);
	}
	
	public static ListInfo queryListInfoWithTotalsizesqlByRowHandler(RowHandler rowhandler,Class<?> beanType, String sql, long offset,int pagesize,String totalsizesql,Object... fields) throws SQLException
	{

		return queryListInfoWithDBName2ndTotalsizesqlByRowHandler( (DBOptions) null,rowhandler,beanType, null,sql, offset,pagesize,totalsizesql,fields);
	}
	
	
	public static <T> T queryObjectByRowHandler(RowHandler rowhandler,Class<T> beanType, String sql, Object... fields) throws SQLException
	{

		return queryObjectWithDBNameByRowHandler(	(DBOptions) null,rowhandler,beanType,null, sql, fields);
		
		 
	}
	
	public static <T> List<T> queryListWithDBNameByRowHandler(RowHandler rowhandler,Class<T> beanType,String dbname, String sql, Object... fields) throws SQLException
	{
		
//		PreparedDBUtil dbutil = new PreparedDBUtil();
//		dbutil.preparedSelect(dbname, sql);
//		if(fields != null && fields.length > 0)
//		{
//			for(int i = 0; i < fields.length ; i ++)
//			{
//				
//				Object field = fields[i];
//				dbutil.setObject(i + 1, field);
//			}
//		}
//		
//		
//		
//		return dbutil.executePreparedForList(beanType,rowhandler);

		return queryListWithDBNameByRowHandler((DBOptions) null, rowhandler, beanType,  dbname,   sql,   fields);
	}
	
	
	public static <T> T queryObjectWithDBNameByRowHandler(RowHandler rowhandler,Class<T> beanType,String dbname, String sql, Object... fields) throws SQLException
	{
		
//		PreparedDBUtil dbutil = new PreparedDBUtil();
//		dbutil.preparedSelect(dbname, sql);
//		if(fields != null && fields.length > 0)
//		{
//			for(int i = 0; i < fields.length ; i ++)
//			{
//				
//				Object field = fields[i];
//				dbutil.setObject(i + 1, field);
//			}
//		}
//		
//		return (T)dbutil.executePreparedForObject(beanType,rowhandler);

		return queryObjectWithDBNameByRowHandler((DBOptions) null, rowhandler,  beanType,  dbname,   sql,   fields);
		 
	}
	
	
	
	
	/**
	 * 采用Null行处理器的通用查询，适用于单个Object查询，List查询等等
	 * @param rowhandler
	 * @param sql
	 * @param fields
	 * @throws SQLException
	 */
	public static void queryByNullRowHandler(NullRowHandler rowhandler, String sql, Object... fields) throws SQLException
	{


				queryWithDBNameByNullRowHandler((DBOptions) null, rowhandler,null, sql, fields);
	}
	
	/**
	 * 
	 * @param rowhandler
	 * @param dbname
	 * @param sql
	 * @param offset
	 * @param pagesize
	 * @param fields
	 * @return
	 * @throws SQLException
	 */
	public static ListInfo queryListInfoWithDBNameByNullRowHandler(NullRowHandler rowhandler,String dbname, String sql, long offset,int pagesize,Object... fields) throws SQLException
	{
		
//		PreparedDBUtil dbutil = new PreparedDBUtil();
//		dbutil.preparedSelect(dbname, sql,offset,pagesize);
//		if(fields != null && fields.length > 0)
//		{
//			for(int i = 0; i < fields.length ; i ++)
//			{
//				
//				Object field = fields[i];
//				dbutil.setObject(i + 1, field);
//			}
//		}
//		
//		dbutil.executePreparedWithRowHandler(rowhandler);
//		ListInfo datas = new ListInfo();
//		
//		datas.setTotalSize(dbutil.getLongTotalSize());
//		return datas;


		return queryListInfoWithDBNameByNullRowHandler((DBOptions) null, rowhandler,  dbname,   sql,   offset,  pagesize,  fields);
	}
	public static ListInfo queryListInfoWithDBName2ndTotalsizeByNullRowHandler(NullRowHandler rowhandler,String dbname, String sql, long offset,
																			   int pagesize,long totalsize,Object... fields) throws SQLException
	{
		
//		PreparedDBUtil dbutil = new PreparedDBUtil();
//		dbutil.preparedSelect(dbname, sql,offset,pagesize,totalsize);
//		if(fields != null && fields.length > 0)
//		{
//			for(int i = 0; i < fields.length ; i ++)
//			{
//				
//				Object field = fields[i];
//				dbutil.setObject(i + 1, field);
//			}
//		}
//		
//		dbutil.executePreparedWithRowHandler(rowhandler);
//		ListInfo datas = new ListInfo();
//		
//		datas.setTotalSize(dbutil.getLongTotalSize());
//		return datas;

		return queryListInfoWithDBName2ndTotalsizeByNullRowHandler((DBOptions) null, rowhandler,  dbname,   sql,   offset,
		  pagesize,  totalsize, fields) ;
	}
	public static ListInfo queryListInfoWithDBName2ndTotalsizesqlByNullRowHandler(NullRowHandler rowhandler,String dbname, String sql, long offset,
																				  int pagesize,String totalsizesql,Object... fields) throws SQLException
	{
		
//		PreparedDBUtil dbutil = new PreparedDBUtil();
//		dbutil.preparedSelectWithTotalsizesql(dbname, sql,offset,pagesize,totalsizesql);
//		if(fields != null && fields.length > 0)
//		{
//			for(int i = 0; i < fields.length ; i ++)
//			{
//				
//				Object field = fields[i];
//				dbutil.setObject(i + 1, field);
//			}
//		}
//		
//		dbutil.executePreparedWithRowHandler(rowhandler);
//		ListInfo datas = new ListInfo();
//		
//		datas.setTotalSize(dbutil.getLongTotalSize());
//		return datas;


		return queryListInfoWithDBName2ndTotalsizesqlByNullRowHandler((DBOptions) null, rowhandler,  dbname,   sql,   offset,
		  pagesize,  totalsizesql,  fields) ;
	}
	/**
	 * 
	 * @param rowhandler
	 * @param sql
	 * @param offset
	 * @param pagesize
	 * @param fields
	 * @return
	 * @throws SQLException
	 */
	public static ListInfo queryListInfoByNullRowHandler(NullRowHandler rowhandler, String sql, long offset,int pagesize,Object... fields) throws SQLException
	{

		return queryListInfoWithDBNameByNullRowHandler( (DBOptions) null,rowhandler, null,sql, offset,pagesize,fields);
	}
	public static ListInfo queryListInfoWithTotalsizeByNullRowHandler(NullRowHandler rowhandler, String sql, long offset,int pagesize,long totalsize,Object... fields) throws SQLException
	{

		return queryListInfoWithDBName2ndTotalsizeByNullRowHandler( (DBOptions) null,rowhandler, null,sql, offset,pagesize,totalsize,fields);
	}
	public static ListInfo queryListInfoWithTotalsizesqlByNullRowHandler(NullRowHandler rowhandler, String sql, long offset,int pagesize,String totalsizesql,Object... fields) throws SQLException
	{
		return queryListInfoWithDBName2ndTotalsizesqlByNullRowHandler( (DBOptions) null,rowhandler, null,sql, offset,pagesize,totalsizesql,fields);
	}
	
	
	/**
	 * 采用Null行处理器的通用查询，适用于单个Object查询，List查询等等
	 * @param rowhandler
	 * @param dbname
	 * @param sql
	 * @param fields
	 * @throws SQLException
	 */
	public static void queryWithDBNameByNullRowHandler(NullRowHandler rowhandler,String dbname, String sql, Object... fields) throws SQLException
	{
		
//		PreparedDBUtil dbutil = new PreparedDBUtil();
//		dbutil.preparedSelect(dbname, sql);
//		if(fields != null && fields.length > 0)
//		{
//			for(int i = 0; i < fields.length ; i ++)
//			{
//				
//				Object field = fields[i];
//				dbutil.setObject(i + 1, field);
//			}
//		}
//		
//		
//		
//		 dbutil.executePreparedWithRowHandler(rowhandler);

		queryWithDBNameByNullRowHandler((DBOptions) null, rowhandler,  dbname,   sql,  fields) ;
	}
	
	
	public static <T> List<T> queryListBean(Class<T> beanType, String sql, Object bean) throws SQLException
	{


		return queryListBeanWithDBName((DBOptions) null,beanType,null, sql, bean);
	}
	/**
	 * 
	 * @param beanType
	 * @param dbname
	 * @param sql
	 * @param offset
	 * @param pagesize
	 * @param totalsize
	 * @param bean
	 * @return
	 * @throws SQLException
	 */
	public static ListInfo queryListInfoBeanWithDBName(Class<?> beanType,String dbname, String sql, long offset,int pagesize,long totalsize,Object bean) throws SQLException
	{
		
//		PreparedDBUtil dbutil = new PreparedDBUtil();
//		SQLParams params = SQLParams.convertBeanToSqlParams(bean, new SQLInfo(sql,true,false), dbname, PreparedDBUtil.SELECT, null);
//		dbutil.preparedSelect(params,dbname, sql,offset,pagesize,totalsize);
//		ListInfo datas = new ListInfo();
//		datas.setDatas(dbutil.executePreparedForList(beanType));
//		datas.setTotalSize(dbutil.getLongTotalSize());
//		return datas;

		return queryListInfoBeanWithDBName((DBOptions) null, beanType,  dbname,   sql,   offset,  pagesize,  totalsize,  bean) ;
	}
	
	public static ListInfo queryListInfoBeanWithDBName(Class<?> beanType,String dbname, String sql, long offset,int pagesize,String totalsizesql,Object bean) throws SQLException
	{
		
//		PreparedDBUtil dbutil = new PreparedDBUtil();
//		SQLParams params = SQLParams.convertBeanToSqlParams(bean, new SQLInfo(sql,true,false), dbname, PreparedDBUtil.SELECT, null);
//		dbutil.preparedSelectWithTotalsizesql(params,dbname, sql,offset,pagesize,totalsizesql);
//		ListInfo datas = new ListInfo();
//		datas.setDatas(dbutil.executePreparedForList(beanType));
//		datas.setTotalSize(dbutil.getLongTotalSize());
//		return datas;

		return queryListInfoBeanWithDBName((DBOptions) null, beanType,  dbname,   sql,   offset,  pagesize,  totalsizesql,  bean) ;
	}
	
	/**
	 * 
	 * @param beanType
	 * @param dbname
	 * @param sql
	 * @param offset
	 * @param pagesize
	 * @param bean
	 * @return
	 * @throws SQLException
	 */
	public static ListInfo queryListInfoBeanWithDBName(Class<?> beanType,String dbname, String sql, long offset,int pagesize,Object bean) throws SQLException
	{
		
//		PreparedDBUtil dbutil = new PreparedDBUtil();
//		SQLParams params = SQLParams.convertBeanToSqlParams(bean, new SQLInfo(sql,true,false), dbname, PreparedDBUtil.SELECT, null);
//		dbutil.preparedSelect(params,dbname, sql,offset,pagesize,-1L);
//		ListInfo datas = new ListInfo();
//		datas.setDatas(dbutil.executePreparedForList(beanType));
//		datas.setTotalSize(dbutil.getLongTotalSize());
//		return datas;

		return queryListInfoBeanWithDBName((DBOptions) null,beanType,  dbname,   sql,   offset,  pagesize,  bean) ;
	}
	/**
	 * 
	 * @param beanType
	 * @param sql
	 * @param offset
	 * @param pagesize
	 * @param totalsize
	 * @param bean
	 * @return
	 * @throws SQLException
	 */
	public static ListInfo queryListInfoBean(Class<?> beanType, String sql, long offset,int pagesize,long totalsize,Object bean) throws SQLException
	{

		return queryListInfoBeanWithDBName((DBOptions) null,beanType, null,sql, offset,pagesize,totalsize,bean);
	}
	
	public static ListInfo queryListInfoBean(Class<?> beanType, String sql, long offset,int pagesize,String totalsizesql,Object bean) throws SQLException
	{
		return queryListInfoBeanWithDBName((DBOptions) null,beanType, null,sql, offset,pagesize,totalsizesql,bean);
	}
	
	public static ListInfo queryListInfoBean(Class<?> beanType, String sql, long offset,int pagesize,Object bean) throws SQLException
	{
		return queryListInfoBeanWithDBName((DBOptions) null,beanType, null,sql, offset,pagesize,-1L,bean);
	}
	
	public static String queryField( String sql, Object... fields) throws SQLException
	{
		return queryFieldWithDBName((DBOptions) null,(String)null, sql, fields);
	}
	public static String queryFieldBean( String sql, Object bean) throws SQLException
	{
		return queryFieldBeanWithDBName((DBOptions) null,(String)null, sql, bean);
	}
	
	public static String queryFieldBeanWithDBName(String dbname, String sql, Object bean) throws SQLException
	{
		
//		PreparedDBUtil dbutil = new PreparedDBUtil();
//		SQLParams params = SQLParams.convertBeanToSqlParams(bean,  new SQLInfo(sql,true,false), dbname, PreparedDBUtil.SELECT, null);
//		dbutil.preparedSelect(params,dbname, sql);
//		
//		
//		
//		
//		dbutil.executePrepared();
//		if(dbutil.size() > 0)
//			return dbutil.getString(0, 0);
//		else
//		{
//			return null;
//		}

		return queryFieldBeanWithDBName((DBOptions) null, dbname,   sql,   bean) ;
	}
	
	public static String queryFieldWithDBName(String dbname, String sql, Object... fields) throws SQLException
	{
		
//		PreparedDBUtil dbutil = new PreparedDBUtil();
//		dbutil.preparedSelect(dbname, sql);
//		if(fields != null && fields.length > 0)
//		{
//			for(int i = 0; i < fields.length ; i ++)
//			{
//				
//				Object field = fields[i];
//				dbutil.setObject(i + 1, field);
//			}
//		}
//		
//		
//		
//		dbutil.executePrepared();
//		if(dbutil.size() > 0)
//			return dbutil.getString(0, 0);
//		else
//		{
//			return null;
//		}

		return queryFieldWithDBName((DBOptions) null, dbname,   sql,  fields) ;
	}
	
	
	
	/**
	 * 
	 * @param <T>
	 * @param type
	 * @param sql
	 * @param fields
	 * @return
	 * @throws SQLException
	 */
	
	
	public static <T> T queryTField( Class<T> type,String sql, Object... fields) throws SQLException
	{

		return queryTFieldWithDBName((DBOptions) null,(String)null, type,sql, fields);
	}
	public static <T> T queryTFieldBean( Class<T> type,String sql, Object bean) throws SQLException
	{

		return queryTFieldBeanWithDBName((DBOptions) null,(String)null, type,sql, bean);
	}
	
	public static <T> T queryTFieldBeanWithDBName(String dbname, Class<T> type,String sql, Object bean) throws SQLException
	{
		
//		PreparedDBUtil dbutil = new PreparedDBUtil();
//		SQLParams params = SQLParams.convertBeanToSqlParams(bean, sql, dbname, PreparedDBUtil.SELECT, null);
//		dbutil.preparedSelect(params,dbname, sql);
//		
//		
//		
//		
//		dbutil.executePrepared();
//		if(dbutil.size() > 0)
//			return (T)ValueObjectUtil.typeCast(dbutil.getObject(0, 0),type);
//		else
//		{
//			return (T)ValueObjectUtil.getDefaultValue(type);
//		}

		return queryTFieldBeanWithDBName((DBOptions) null,dbname, type,(FieldRowHandler<T>)null,sql, bean) ;
	}
	
	public static <T> T queryTFieldWithDBName(String dbname, Class<T> type,String sql, Object... fields) throws SQLException
	{

		return queryTFieldWithDBName((DBOptions) null,dbname, type,(FieldRowHandler<T>)null,sql, fields);
	}
	
	public static <T> T queryTField( Class<T> type,FieldRowHandler<T> fieldRowHandler,String sql, Object... fields) throws SQLException
	{
		return queryTFieldWithDBName((DBOptions) null,(String)null, type,fieldRowHandler,sql, fields);
	}
	public static <T> T queryTFieldBean( Class<T> type,FieldRowHandler<T> fieldRowHandler,String sql, Object bean) throws SQLException
	{
		return queryTFieldBeanWithDBName((DBOptions) null,(String)null, type,fieldRowHandler,sql, bean);
	}
	
	public static <T> T queryTFieldBeanWithDBName(String dbname, Class<T> type,FieldRowHandler<T> fieldRowHandler,String sql, Object bean) throws SQLException
	{
		
//		PreparedDBUtil dbutil = new PreparedDBUtil();
//		SQLParams params = SQLParams.convertBeanToSqlParams(bean,  new SQLInfo(sql,true,false), dbname, PreparedDBUtil.SELECT, null);
//		dbutil.preparedSelect(params,dbname, sql);
//		
//		
//		
//		if(fieldRowHandler == null)
//		{
//			dbutil.executePrepared();
//			if(dbutil.size() > 0)
//				return (T)ValueObjectUtil.typeCast(dbutil.getObject(0, 0),type);
//			else
//			{
//				return (T)ValueObjectUtil.getDefaultValue(type);
//			}
//			
//		}
//		else
//		{
//			return (T)dbutil.executePreparedForObject(type, fieldRowHandler);
//		}

		return queryTFieldBeanWithDBName((DBOptions) null, dbname,  type, fieldRowHandler,  sql,   bean) ;
		 
	}
	
	public static <T> T queryTFieldWithDBName(String dbname, Class<T> type,FieldRowHandler<T> fieldRowHandler,String sql, Object... fields) throws SQLException
	{
		
//		PreparedDBUtil dbutil = new PreparedDBUtil();
//		dbutil.preparedSelect(dbname, sql);
//		if(fields != null && fields.length > 0)
//		{
//			for(int i = 0; i < fields.length ; i ++)
//			{
//				
//				Object field = fields[i];
//				dbutil.setObject(i + 1, field);
//			}
//		}
//		
//		
//		
//		if(fieldRowHandler == null)
//		{
//			dbutil.executePrepared();
//			if(dbutil.size() > 0)
//				return (T)ValueObjectUtil.typeCast(dbutil.getObject(0, 0),type);
//			else
//			{
//				return (T)ValueObjectUtil.getDefaultValue(type);
//			}
//		}
//		else
//		{
//			return (T)dbutil.executePreparedForObject(type, fieldRowHandler);
//		}

		return queryTFieldWithDBName((DBOptions) null, dbname,  type,  fieldRowHandler,  sql,   fields);
	}
	
	
	public static <T> T queryObjectBean(Class<T> beanType, String sql, Object bean) throws SQLException
	{

		return queryObjectBeanWithDBName((DBOptions) null,beanType,null, sql, bean);
		
		 
	}
	
	public static <T> List<T> queryListBeanWithDBName(Class<T> beanType,String dbname, String sql, Object bean) throws SQLException
	{


//		PreparedDBUtil dbutil = new PreparedDBUtil();
//		SQLParams params = SQLParams.convertBeanToSqlParams(bean,  new SQLInfo(sql,true,false), dbname, PreparedDBUtil.SELECT, null);
//		dbutil.preparedSelect(params,dbname, sql);
//
//		
//		
//		
//		
//		return dbutil.executePreparedForList(beanType);
		return queryListBeanWithDBName((DBOptions) null,beanType,  dbname,   sql,   bean) ;
	}
	
	public static <T> T queryObjectBeanWithDBName(Class<T> beanType,String dbname, String sql, Object bean) throws SQLException
	{
		
//		PreparedDBUtil dbutil = new PreparedDBUtil();
//		SQLParams params = SQLParams.convertBeanToSqlParams(bean,  new SQLInfo(sql,true,false), dbname, PreparedDBUtil.SELECT, null);
//		dbutil.preparedSelect(params,dbname, sql);
//		return (T)dbutil.executePreparedForObject(beanType);

		return queryObjectBeanWithDBName((DBOptions) null, beanType,  dbname,   sql,   bean);
		 
	}
	
	
	public static <T> List<T> queryListBeanByRowHandler(RowHandler rowhandler,Class<T> beanType, String sql, Object bean) throws SQLException
	{


		return queryListBeanWithDBNameByRowHandler((DBOptions) null,rowhandler,beanType,(String)null, sql, bean);
	}
	/**
	 * 
	 * @param rowhandler
	 * @param beanType
	 * @param dbname
	 * @param sql
	 * @param offset
	 * @param pagesize
	 * @param totalsize
	 * @param bean
	 * @return
	 * @throws SQLException
	 */
	public static ListInfo queryListInfoBeanWithDBNameByRowHandler(RowHandler rowhandler,Class<?> beanType,String dbname, String sql, long offset,
																   int pagesize,long totalsize,Object  bean) throws SQLException
	{

		return queryListInfoBeanWithDBNameByRowHandler((DBOptions) null, rowhandler, beanType,  dbname,   sql,   offset,
		  pagesize,  totalsize,   bean);
	}
	
	public static ListInfo queryListInfoBeanWithDBNameByRowHandler(RowHandler rowhandler,Class<?> beanType,String dbname, String sql, long offset,int pagesize,String totalsizesql,Object  bean) throws SQLException
	{
		
//		PreparedDBUtil dbutil = new PreparedDBUtil();
//		SQLParams params = SQLParams.convertBeanToSqlParams(bean,  new SQLInfo(sql,true,false), dbname, PreparedDBUtil.SELECT, null);
//		dbutil.preparedSelectWithTotalsizesql(params,dbname, sql,offset,pagesize,totalsizesql);
//		ListInfo datas = new ListInfo();
//		datas.setDatas(dbutil.executePreparedForList(beanType,rowhandler));
//		datas.setTotalSize(dbutil.getLongTotalSize());
//		return datas;

		return queryListInfoBeanWithDBNameByRowHandler(	(DBOptions) null, rowhandler, beanType,  dbname,   sql,   offset,  pagesize,  totalsizesql,   bean) ;
	}
	/**
	 * 
	 * @param rowhandler
	 * @param beanType
	 * @param dbname
	 * @param sql
	 * @param offset
	 * @param pagesize
	 * @param bean
	 * @return
	 * @throws SQLException
	 */
	public static ListInfo queryListInfoBeanWithDBNameByRowHandler(RowHandler rowhandler,Class<?> beanType,String dbname, String sql, long offset,int pagesize,Object  bean) throws SQLException
	{
		
//		PreparedDBUtil dbutil = new PreparedDBUtil();
//		SQLParams params = SQLParams.convertBeanToSqlParams(bean,  new SQLInfo(sql,true,false), dbname, PreparedDBUtil.SELECT, null);
//		dbutil.preparedSelect(params,dbname, sql,offset,pagesize,-1L);
//		ListInfo datas = new ListInfo();
//		datas.setDatas(dbutil.executePreparedForList(beanType,rowhandler));
//		datas.setTotalSize(dbutil.getLongTotalSize());
//		return datas;

		return queryListInfoBeanWithDBNameByRowHandler((DBOptions) null, rowhandler, beanType,  dbname,   sql,   offset,  pagesize,   bean);
	}
	/**
	 * 
	 * @param rowhandler
	 * @param beanType
	 * @param sql
	 * @param offset
	 * @param pagesize
	 * @param totalsize
	 * @param bean
	 * @return
	 * @throws SQLException
	 */
	public static ListInfo queryListInfoBeanByRowHandler(RowHandler rowhandler,Class<?> beanType, String sql, long offset,int pagesize,long totalsize,Object bean) throws SQLException
	{

		return queryListInfoBeanWithDBNameByRowHandler( (DBOptions) null,rowhandler,beanType, (String)null,sql, offset,pagesize,totalsize,bean);
	}
	
	public static ListInfo queryListInfoBeanByRowHandler(RowHandler rowhandler,Class<?> beanType, String sql, long offset,int pagesize,String totalsizesql,Object bean) throws SQLException
	{
		return queryListInfoBeanWithDBNameByRowHandler( (DBOptions) null,rowhandler,beanType, (String)null,sql, offset,pagesize,totalsizesql,bean);
	}
	public static ListInfo queryListInfoBeanByRowHandler(RowHandler rowhandler,Class<?> beanType, String sql, long offset,int pagesize,Object bean) throws SQLException
	{
		return queryListInfoBeanWithDBNameByRowHandler( (DBOptions) null,rowhandler,beanType, (String)null,sql, offset,pagesize,-1L,bean);
	}
	
	public static <T> T queryObjectBeanByRowHandler(RowHandler rowhandler,Class<T> beanType, String sql, Object bean) throws SQLException
	{
		return queryObjectBeanWithDBNameByRowHandler((DBOptions) null,rowhandler,beanType,(String)null, sql, bean);
		
		 
	}
	
	public static <T> List<T> queryListBeanWithDBNameByRowHandler(RowHandler rowhandler,Class<T> beanType,String dbname, String sql, Object bean) throws SQLException
	{


		return queryListBeanWithDBNameByRowHandler((DBOptions) null, rowhandler,  beanType,  dbname,   sql,   bean) ;
	}
	
	
	public static <T> T queryObjectBeanWithDBNameByRowHandler(RowHandler rowhandler,Class<T> beanType,String dbname, String sql, Object bean) throws SQLException
	{
		
//		PreparedDBUtil dbutil = new PreparedDBUtil();
//		SQLParams params = SQLParams.convertBeanToSqlParams(bean,  new SQLInfo(sql,true,false), dbname, PreparedDBUtil.SELECT, null);
//		dbutil.preparedSelect(params,dbname, sql);		
//		return (T)dbutil.executePreparedForObject(beanType,rowhandler);


		return queryObjectBeanWithDBNameByRowHandler((DBOptions) null, rowhandler,  beanType,  dbname,   sql,   bean);
	}
	
	
	
	
	
	public static void queryBeanByNullRowHandler(NullRowHandler rowhandler, String sql, Object bean) throws SQLException
	{


				queryBeanWithDBNameByNullRowHandler((DBOptions) null, rowhandler,(String)null, sql, bean);
	}
	/**
	 * 
	 * @param rowhandler
	 * @param dbname
	 * @param sql
	 * @param offset
	 * @param pagesize
	 * @param totalsize
	 * @param bean
	 * @return
	 * @throws SQLException
	 */
	public static ListInfo queryListInfoBeanWithDBNameByNullRowHandler(NullRowHandler rowhandler,String dbname, String sql, long offset,int pagesize,long totalsize,Object bean) throws SQLException
	{
		
//		PreparedDBUtil dbutil = new PreparedDBUtil();
//		SQLParams params = SQLParams.convertBeanToSqlParams(bean,  new SQLInfo(sql,true,false), dbname, PreparedDBUtil.SELECT, null);
//		dbutil.preparedSelect(params,dbname, sql,offset,pagesize,totalsize);
//		dbutil.executePreparedWithRowHandler(rowhandler);
//		ListInfo datas = new ListInfo();
//		
//		datas.setTotalSize(dbutil.getLongTotalSize());
//		return datas;		 

		return queryListInfoBeanWithDBNameByNullRowHandler((DBOptions) null, rowhandler,  dbname,   sql,   offset,  pagesize,  totalsize,  bean);
	}
	
	public static ListInfo queryListInfoBeanWithDBNameByNullRowHandler(NullRowHandler rowhandler,String dbname, String sql,
																	   long offset,int pagesize,String totalsizesql,Object bean) throws SQLException
	{


		return queryListInfoBeanWithDBNameByNullRowHandler((DBOptions) null, rowhandler,  dbname,   sql,
		  offset,  pagesize,  totalsizesql,  bean);
	}
	/**
	 * 
	 * @param rowhandler
	 * @param dbname
	 * @param sql
	 * @param offset
	 * @param pagesize
	 * @param bean
	 * @return
	 * @throws SQLException
	 */
	public static ListInfo queryListInfoBeanWithDBNameByNullRowHandler(NullRowHandler rowhandler,String dbname, String sql, long offset,int pagesize,Object bean) throws SQLException
	{


		return queryListInfoBeanWithDBNameByNullRowHandler((DBOptions) null, rowhandler,  dbname,   sql,   offset,  pagesize,  bean) ;
	}
	/**
	 * 
	 * @param rowhandler
	 * @param sql
	 * @param offset
	 * @param pagesize
	 * @param totalsize
	 * @param bean
	 * @return
	 * @throws SQLException
	 */
	public static ListInfo queryListInfoBeanByNullRowHandler(NullRowHandler rowhandler, String sql, long offset,int pagesize,long totalsize,Object bean) throws SQLException
	{

		return queryListInfoBeanWithDBNameByNullRowHandler((DBOptions) null, rowhandler, (String)null,sql, offset,pagesize,totalsize,bean);
	}
	
	public static ListInfo queryListInfoBeanByNullRowHandler(NullRowHandler rowhandler, String sql, long offset,int pagesize,String totalsizesql,Object bean) throws SQLException
	{

		return queryListInfoBeanWithDBNameByNullRowHandler( (DBOptions) null,rowhandler, (String)null,sql, offset,pagesize,totalsizesql,bean);
	}
	
	public static ListInfo queryListInfoBeanByNullRowHandler(NullRowHandler rowhandler, String sql, long offset,int pagesize,Object bean) throws SQLException
	{
		return queryListInfoBeanWithDBNameByNullRowHandler( (DBOptions) null,rowhandler, (String)null,sql, offset,pagesize,-1L,bean);
	}
	
	public static void queryBeanWithDBNameByNullRowHandler(NullRowHandler rowhandler,String dbname, String sql, Object bean) throws SQLException
	{
		
//		PreparedDBUtil dbutil = new PreparedDBUtil();
//		SQLParams params = SQLParams.convertBeanToSqlParams(bean,  new SQLInfo(sql,true,false), dbname, PreparedDBUtil.SELECT, null);
//		dbutil.preparedSelect(params,dbname, sql);
//		 dbutil.executePreparedWithRowHandler(rowhandler);

				queryBeanWithDBNameByNullRowHandler(	(DBOptions) null, rowhandler,  dbname,   sql,   bean) ;
	}
	
	/**
	 * more分页查询，不会计算总记录数，如果没有记录那么返回的ListInfo的datas的size为0,
	 * 提升性能，同时前台标签库也会做响应的调整
	 */
	/**
	 * 
	 * @param rowhandler
	 * @param beanType
	 * @param dbname
	 * @param sql
	 * @param offset
	 * @param pagesize
	 * @param fields
	 * @return
	 * @throws SQLException
	 */
	public static ListInfo moreListInfoWithDBNameByRowHandler(RowHandler rowhandler,Class<?> beanType,String dbname, String sql, long offset,int pagesize,Object... fields) throws SQLException
	{

//		return SQLInfoExecutor.queryListInfoWithDBNameByRowHandler(  rowhandler, beanType, dbname, sqlinfo, offset,pagesize,fields);
		
//		SQLInfo sql = getSqlInfo(dbname, sqlname);
		return moreListInfoWithDBNameByRowHandler((DBOptions) null, rowhandler,  beanType,  dbname,   sql,   offset,  pagesize,  fields);
	}
	
	/**
	 * 
	 * @param rowhandler
	 * @param dbname
	 * @param sql
	 * @param offset
	 * @param pagesize
	 * @param fields
	 * @return
	 * @throws SQLException
	 */
	public static ListInfo moreListInfoWithDBNameByNullRowHandler(NullRowHandler rowhandler,String dbname, String sql, long offset,int pagesize,Object... fields) throws SQLException
	{


//		return SQLInfoExecutor.queryListInfoWithDBNameByNullRowHandler(   rowhandler, dbname,  sqlinfo,  offset, pagesize, fields);
//		SQLInfo sql = getSqlInfo(dbname, sqlname);
		return moreListInfoWithDBNameByNullRowHandler((DBOptions) null, rowhandler,  dbname,   sql,   offset,  pagesize,  fields) ;
	}
	
		/**
	 * 
	 * @param beanType
	 * @param dbname
	 * @param sql
	 * @param offset
	 * @param pagesize
	 * @param fields
	 * @return
	 * @throws SQLException
	 */
	public static ListInfo moreListInfoWithDBName(Class<?> beanType,String dbname, String sql, long offset,int pagesize,Object... fields) throws SQLException
	{

//		return SQLInfoExecutor.queryListInfoWithDBName(  beanType, dbname,  sqlinfo,  offset, pagesize, fields);
//		SQLInfo sql = getSqlInfo(dbname, sqlname);
		return moreListInfoWithDBName((DBOptions) null, beanType,  dbname,   sql,   offset,  pagesize,  fields) ;
	}
	
		/**
	 * 
	 * @param rowhandler
	 * @param beanType
	 * @param sql
	 * @param offset
	 * @param pagesize
	 * @param fields
	 * @return
	 * @throws SQLException
	 */
	public static ListInfo moreListInfoByRowHandler(RowHandler rowhandler,Class<?> beanType, String sql, long offset,int pagesize,Object... fields) throws SQLException
	{

		return moreListInfoWithDBNameByRowHandler( (DBOptions) null,rowhandler,beanType, (String)null,sql, offset,pagesize,fields);
	}
	
		/**
	 * 
	 * @param rowhandler
	 * @param sql
	 * @param offset
	 * @param pagesize
	 * @param fields
	 * @return
	 * @throws SQLException
	 */
	public static ListInfo moreListInfoByNullRowHandler(NullRowHandler rowhandler, String sql, long offset,int pagesize,Object... fields) throws SQLException
	{

		return moreListInfoWithDBNameByNullRowHandler((DBOptions) null, rowhandler, (String)null,sql, offset,pagesize,fields);
	}
	
		/**
	 * 
	 * @param rowhandler
	 * @param beanType
	 * @param dbname
	 * @param sql
	 * @param offset
	 * @param pagesize
	 * @param bean
	 * @return
	 * @throws SQLException
	 */
	public static ListInfo moreListInfoBeanWithDBNameByRowHandler(RowHandler rowhandler,Class<?> beanType,String dbname, String sql, long offset,int pagesize,Object  bean) throws SQLException
	{

//		return SQLInfoExecutor.queryListInfoBeanWithDBNameByRowHandler(  rowhandler,beanType,dbname, sqlinfo, offset,pagesize,bean);
//		SQLInfo sql = getSqlInfo(dbname, sql);
		return moreListInfoBeanWithDBNameByRowHandler((DBOptions) null, rowhandler, beanType,  dbname,   sql,   offset,  pagesize,   bean);
	}
	
		/**
	 * 
	 * @param rowhandler
	 * @param dbname

	 * @param offset
	 * @param pagesize

	 * @param bean
	 * @return
	 * @throws SQLException
	 */
	public static ListInfo moreListInfoBeanWithDBNameByNullRowHandler(NullRowHandler rowhandler,String dbname, String sql, long offset,int pagesize,Object bean) throws SQLException
	{

		return moreListInfoBeanWithDBNameByNullRowHandler((DBOptions) null, rowhandler,  dbname,   sql,   offset,  pagesize,  bean) ;
	}
	
		/**
	 * 
	 * @param beanType
	 * @param dbname
	 * @param sql
	 * @param offset
	 * @param pagesize
	 * @param bean
	 * @return
	 * @throws SQLException
	 */
	public static ListInfo moreListInfoBeanWithDBName(Class<?> beanType,String dbname, String sql, long offset,int pagesize,Object bean) throws SQLException
	{

//		return SQLInfoExecutor.queryListInfoBeanWithDBName( beanType, dbname,  sqlinfo,  offset, pagesize,  bean);
//		SQLInfo sql = getSqlInfo(dbname, sql);
		return moreListInfoBeanWithDBName((DBOptions) null, beanType,  dbname,   sql,   offset,  pagesize,  bean) ;
	}
	
		public static ListInfo moreListInfoBeanByRowHandler(RowHandler rowhandler,Class<?> beanType, String sql, long offset,int pagesize,Object bean) throws SQLException
	{

//			return SQLInfoExecutor.queryListInfoBeanWithDBNameByRowHandler(  rowhandler,beanType,dbname, sqlinfo, offset,pagesize,totalsize,bean);
			return moreListInfoBeanByRowHandler((DBOptions) null, rowhandler,  beanType,   sql,   offset,  pagesize,  bean);
	}
	
		public static ListInfo moreListInfoBeanByNullRowHandler(NullRowHandler rowhandler, String sql, long offset,int pagesize,Object bean) throws SQLException
	{


//			return SQLInfoExecutor.queryListInfoBeanWithDBNameByNullRowHandler( rowhandler, dbname,  sqlinfo,  offset, pagesize, totalsize, bean);
//			SQLInfo sql = getSqlInfo(null, sql);
			return moreListInfoBeanByNullRowHandler((DBOptions) null, rowhandler,   sql,   offset,  pagesize,  bean);
	}
	
		public static ListInfo moreListInfoBean(Class<?> beanType, String sql, long offset,int pagesize,Object bean) throws SQLException
	{

//			return SQLInfoExecutor.queryListInfoBeanWithDBName( beanType, dbname,  sqlinfo,  offset, pagesize, totalsize, bean);
//			SQLInfo sql = getSqlInfo(null, sql);
			return moreListInfoBean((DBOptions) null, beanType,   sql,   offset,  pagesize,  bean) ;
	}
	
		/**
	 * 
	 * @param beanType
	 * @param sql
	 * @param offset
	 * @param pagesize
	 * @param fields
	 * @return
	 * @throws SQLException
	 */
	public static ListInfo moreListInfo(Class<?> beanType, String sql, long offset,int pagesize,Object... fields) throws SQLException
	{

		return moreListInfoWithDBName((DBOptions) null,beanType,(String) null,sql, offset,pagesize,fields);
	}
	
	
}
