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
import org.frameworkset.persitent.util.SQLInfo;
import org.frameworkset.persitent.util.SQLUtil;
import org.frameworkset.spi.BaseApplicationContext;

import java.sql.SQLException;
import java.util.List;

/**
 * 
 * <p>Title: ConfigSQLExecutor.java</p>
 *
 * <p>Description: 从根据id从配置文件读取sql语句，执行相关数据库操作</p>
 *
 * <p>Copyright: Copyright (c) 2007</p>
 * @Date 2011-4-7 下午02:28:41
 * @author biaoping.yin
 * @version 1.0
 */
public class DBOptionsConfigSQLExecutor {

	private SQLUtil context ;

	public DBOptionsConfigSQLExecutor(String sqlfile)
	{
		context = SQLUtil.getInstance(sqlfile);
	}

	public BaseApplicationContext getSQLContext() {
		return context.getSqlcontext();
	}

	public SQLInfo getSqlInfo(String sqlname) throws SQLException
	{
		return getSqlInfo(null,sqlname);
	}
	public SQLInfo getSqlInfo(String dbname,String sqlname) throws SQLException
	{
		SQLInfo sql = context.getSQLInfo(dbname, sqlname);
		if(sql == null)
			throw new NestedSQLException("名称为[" + sqlname + "]的sql语句不存在，请检查配置文件[" + context.getSQLFile() + "]配置是否正确.");
		return sql;
	}
	public String getSql(String sqlname) throws SQLException
	{
		return getSql(null,sqlname);
	}
	public String getSql(String dbname,String sqlname) throws SQLException
	{
		SQLInfo sql = context.getSQLInfo(dbname, sqlname);
		if(sql == null)
			throw new NestedSQLException("名称为[" + sqlname + "]的sql语句不存在，请检查配置文件[" + context.getSQLFile() + "]配置是否正确.");
		return sql.getSql();
	}
	public void insertBeans(DBOptions dbOptions,String dbname, String sqlname, List beans) throws SQLException {
		
		if(beans == null || beans.size() == 0)
			return ;
		SQLInfo sql = getSqlInfo(dbname,sqlname);
		SQLInfoExecutor.insertBeans(   dbOptions,dbname,  sql,  beans);
	}
	public void insertBeans(DBOptions dbOptions,String dbname, String sqlname, List beans,GetCUDResult getCUDResult) throws SQLException {
		
		if(beans == null || beans.size() == 0)
			return ;
		SQLInfo sql = getSqlInfo(dbname,sqlname);
		SQLInfoExecutor.insertBeans(  dbOptions, dbname,  sql,  beans,getCUDResult);
	}
	
	public Object update( DBOptions dbOptions,String sqlname, Object... fields) throws SQLException {
		SQLInfo sql = getSqlInfo(null,sqlname);
		return SQLInfoExecutor.update(  dbOptions,sql, fields);
	}
	



	public Object delete(DBOptions dbOptions, String sqlname, Object... fields) throws SQLException {
		SQLInfo sql = getSqlInfo(null,sqlname);
		return SQLInfoExecutor.delete(  dbOptions,sql, fields);
		
	}
	
//	public static void deleteByKeys(String sql, Object... fields) throws SQLException {
//		executeBatch(null, sql,PreparedDBUtil.DELETE, fields);
//		
//	}
//	public static void deleteByKeysWithDBName(String dbname,String sql, Object... fields) throws SQLException {
//		executeBatch(null, sql,PreparedDBUtil.DELETE, fields);
//		
//	}
	
	
	public void deleteByKeys(DBOptions dbOptions,String sqlname, int... fields) throws SQLException {
		SQLInfo sql = getSqlInfo(null,sqlname);
		SQLInfoExecutor.deleteByKeys(  dbOptions,sql, fields);
//		executeBatch(null, sql,PreparedDBUtil.DELETE, fields);
		
	}
	public  void deleteByKeysWithDBName(DBOptions dbOptions,String dbname,String sqlname, int... fields) throws SQLException {
		SQLInfo sql = getSqlInfo(dbname,sqlname);
		SQLInfoExecutor.deleteByKeysWithDBName(  dbOptions,dbname,sql, fields);
		
	}
	
	public void updateByKeys(DBOptions dbOptions,String sqlname, int... fields) throws SQLException {
		SQLInfo sql = getSqlInfo(null,sqlname);
		SQLInfoExecutor.updateByKeys(  dbOptions,sql, fields);
//		executeBatch(null, sql,PreparedDBUtil.DELETE, fields);
		
	}
	public  void updateByKeysWithDBName(DBOptions dbOptions,String dbname,String sqlname, int... fields) throws SQLException {
		SQLInfo sql = getSqlInfo(dbname,sqlname);
		SQLInfoExecutor.updateByKeysWithDBName(  dbOptions, dbname,sql, fields);
		
	}
	
	public void deleteByLongKeys(DBOptions dbOptions,String sqlname, long... fields) throws SQLException {
		SQLInfo sql = getSqlInfo(null,sqlname);
		SQLInfoExecutor.deleteByLongKeys(  dbOptions,sql, fields);
//		executeBatch(null, sql,PreparedDBUtil.DELETE, fields);
		
	}
	public void deleteByLongKeysWithDBName(DBOptions dbOptions,String dbname,String sqlname, long... fields) throws SQLException {
		SQLInfo sql = getSqlInfo(dbname,sqlname);
		SQLInfoExecutor.deleteByLongKeysWithDBName(  dbOptions,dbname,sql, fields);
//		executeBatch(dbname, sql,PreparedDBUtil.DELETE, fields);
		
	}
	
	public void updateByLongKeys(DBOptions dbOptions,String sqlname, long... fields) throws SQLException {
		SQLInfo sql = getSqlInfo(null,sqlname);
		SQLInfoExecutor.updateByLongKeys(  dbOptions,sql, fields);
//		executeBatch(null, sql,PreparedDBUtil.DELETE, fields);
		
	}
	public void updateByLongKeysWithDBName(DBOptions dbOptions,String dbname,String sqlname, long... fields) throws SQLException {
		SQLInfo sql = getSqlInfo(dbname,sqlname);
		SQLInfoExecutor.updateByLongKeysWithDBName(  dbOptions,dbname,sql, fields);
//		executeBatch(dbname, sql,PreparedDBUtil.DELETE, fields);
		
	}
	
	public void deleteByKeys(DBOptions dbOptions,String sqlname, String... fields) throws SQLException {
		SQLInfo sql = getSqlInfo(null,sqlname);
		SQLInfoExecutor.deleteByKeys(  dbOptions,sql, fields);
//		executeBatch(null, sql,PreparedDBUtil.DELETE, fields);
		
	}
	public void deleteByKeysWithDBName(DBOptions dbOptions,String dbname,String sqlname, String... fields) throws SQLException {
		SQLInfo sql = getSqlInfo(dbname,sqlname);
		SQLInfoExecutor.deleteByKeysWithDBName(  dbOptions,dbname,sql, fields);
//		executeBatch(dbname, sql,PreparedDBUtil.DELETE, fields);
		
	}
	
	public void updateByKeys(DBOptions dbOptions,String sqlname, String... fields) throws SQLException {
		SQLInfo sql = getSqlInfo(null,sqlname);
		SQLInfoExecutor.updateByKeys(  dbOptions,sql, fields);
//		executeBatch(null, sql,PreparedDBUtil.DELETE, fields);
		
	}
	public void updateByKeysWithDBName(DBOptions dbOptions,String dbname,String sqlname, String... fields) throws SQLException {
		SQLInfo sql = getSqlInfo(dbname,sqlname);
		SQLInfoExecutor.updateByKeysWithDBName(  dbOptions,dbname,sql, fields);
//		executeBatch(dbname, sql,PreparedDBUtil.DELETE, fields);
		
	}
	
	public void deleteByShortKeys(DBOptions dbOptions,String sqlname, short... fields) throws SQLException {
		SQLInfo sql = getSqlInfo(null,sqlname);
		SQLInfoExecutor.deleteByShortKeys(  dbOptions,sql, fields);
//		executeBatch(null, sql,PreparedDBUtil.DELETE, fields);
		
	}
	public void deleteByShortKeysWithDBName(DBOptions dbOptions,String dbname,String sqlname, short... fields) throws SQLException {
		SQLInfo sql = getSqlInfo(dbname,sqlname);
		SQLInfoExecutor.deleteByShortKeysWithDBName(  dbOptions,dbname,sql, fields);
//		executeBatch(dbname, sql,PreparedDBUtil.DELETE, fields);
	}
	public void updateByShortKeys(DBOptions dbOptions,String sqlname, short... fields) throws SQLException {
		SQLInfo sql = getSqlInfo(null,sqlname);
		SQLInfoExecutor.updateByShortKeys(  dbOptions,sql, fields);
//		executeBatch(null, sql,PreparedDBUtil.DELETE, fields);
		
	}
	public void updateByShortKeysWithDBName(DBOptions dbOptions,String dbname,String sqlname, short... fields) throws SQLException {
		SQLInfo sql = getSqlInfo(dbname,sqlname);
		SQLInfoExecutor.updateByShortKeysWithDBName(  dbOptions,dbname,sql, fields);
//		executeBatch(dbname, sql,PreparedDBUtil.DELETE, fields);
	}


	public Object insert(DBOptions dbOptions,String sqlname, Object... fields) throws SQLException {
		SQLInfo sql = getSqlInfo(null,sqlname);
		return SQLInfoExecutor.insert(  dbOptions,sql, fields);
//		return execute(null, sql,PreparedDBUtil.INSERT, fields);
	}
	
	public  Object updateWithDBName(DBOptions dbOptions,String dbname, String sqlname, Object... fields) throws SQLException {
		SQLInfo sql = getSqlInfo(dbname,sqlname);
		return SQLInfoExecutor.updateWithDBName(  dbOptions,dbname,sql, fields);
//		return execute(dbname, sql,PreparedDBUtil.UPDATE, fields);
	}
	
	public  Object deleteWithDBName(DBOptions dbOptions,String dbname, String sqlname, Object... fields) throws SQLException {
		SQLInfo sql = getSqlInfo(dbname,sqlname);
		return SQLInfoExecutor.deleteWithDBName(  dbOptions,dbname,sql, fields);
//		return execute(dbname, sql,PreparedDBUtil.DELETE, fields);
		
	}



	public  Object insertWithDBName(DBOptions dbOptions,String dbname, String sqlname, Object... fields) throws SQLException {
		SQLInfo sql = getSqlInfo(dbname,sqlname);
		return SQLInfoExecutor.insertWithDBName(  dbOptions,dbname,sql, fields);
//		return execute(dbname, sql,PreparedDBUtil.INSERT, fields);
	}

	public void updateBeans(DBOptions dbOptions,String dbname, String sqlname, List beans,GetCUDResult GetCUDResult) throws SQLException {
		if(beans == null || beans.size() == 0)
			return ;
		SQLInfo sql = getSqlInfo(dbname,sqlname);
		SQLInfoExecutor.updateBeans(  dbOptions,dbname,sql, beans,GetCUDResult);
//		execute( dbname,  sql,  beans,PreparedDBUtil.UPDATE);
	}
	
	public void updateBeans(DBOptions dbOptions,String dbname, String sqlname, List beans) throws SQLException {
		if(beans == null || beans.size() == 0)
			return ;
		SQLInfo sql = getSqlInfo(dbname,sqlname);
		SQLInfoExecutor.updateBeans(  dbOptions,dbname,sql, beans);
//		execute( dbname,  sql,  beans,PreparedDBUtil.UPDATE);
	}



	public void deleteBeans(DBOptions dbOptions,String dbname, String sqlname, List beans) throws SQLException {
		if(beans == null || beans.size() == 0)
			return ;
		SQLInfo sql = getSqlInfo(dbname,sqlname);
		SQLInfoExecutor.deleteBeans(  dbOptions,dbname,sql, beans);
//		execute( dbname,  sql,  beans,PreparedDBUtil.DELETE);
		
	}
	
	public void deleteBeans(DBOptions dbOptions,String dbname, String sqlname, List beans,GetCUDResult GetCUDResult) throws SQLException {
		if(beans == null || beans.size() == 0)
			return ;
		SQLInfo sql = getSqlInfo(dbname,sqlname);
		SQLInfoExecutor.deleteBeans(  dbOptions,dbname,sql, beans,GetCUDResult);
//		execute( dbname,  sql,  beans,PreparedDBUtil.DELETE);
		
	}
	
	public <T> void executeBatch(DBOptions dbOptions,String sqlname,List<T> datas,int batchsize, BatchHandler<T> batchHandler) throws SQLException{
		executeBatch(  dbOptions,(String)null,sqlname, datas, batchsize, batchHandler);
	}
	
	public <T> void executeBatch(DBOptions dbOptions,String dbname,String sqlname,List<T> datas,int batchsize, BatchHandler<T> batchHandler) throws SQLException{
		SQLInfo sqlinfo = getSqlInfo(dbname,sqlname);
		SQLInfoExecutor.executeBatch(   dbOptions, dbname,  sqlinfo,datas, batchsize,batchHandler) ;
	}


	public void insertBean(DBOptions dbOptions,String dbname, String sqlname, Object bean) throws SQLException {
		insertBean(  dbOptions,dbname, sqlname, bean,(GetCUDResult)null);
	}
	public void insertBean(DBOptions dbOptions,String dbname, String sqlname, Object bean,GetCUDResult getCUDResult) throws SQLException {
		if(bean == null)
			return ;
//		List datas = new ArrayList();
//		datas.add(bean);
//		insertBeans( dbname,  sql,  datas);
		SQLInfo sql = getSqlInfo(dbname,sqlname);
		SQLInfoExecutor.insertBean(  dbOptions,dbname,sql, bean,getCUDResult);
	}



	public  void updateBean(DBOptions dbOptions,String dbname, String sqlname, Object bean) throws SQLException {
		if(bean == null )
			return ;
//		List datas = new ArrayList();
//		datas.add(bean);
//		updateBeans( dbname,  sql,  datas);
		SQLInfo sql = getSqlInfo(dbname,sqlname);
		SQLInfoExecutor.updateBean(  dbOptions,dbname,sql, bean);
	}

	

	public void deleteBean(DBOptions dbOptions,String dbname, String sqlname, Object bean) throws SQLException {
		
		if(bean == null)
			return ;
//		List datas = new ArrayList();
//		datas.add(bean);
//		deleteBeans( dbname,  sql,  datas);
		SQLInfo sql = getSqlInfo(dbname,sqlname);
		SQLInfoExecutor.deleteBean(  dbOptions,dbname,sql, bean);
	}
	
	public void insertBeans(DBOptions dbOptions,String sqlname, List beans) throws SQLException {
		insertBeans(   dbOptions,null,sqlname, beans);
	}
	
	



	public void updateBeans( DBOptions dbOptions,String sqlname, List beans) throws SQLException {
		updateBeans(   dbOptions,null,sqlname, beans);
	}



	public void deleteBeans( DBOptions dbOptions,String sqlname, List beans) throws SQLException {
		deleteBeans(   dbOptions,null,sqlname, beans);
		
	}
	
	public void insertBeans(DBOptions dbOptions,String sqlname, List beans,GetCUDResult GetCUDResult) throws SQLException {
		insertBeans(   dbOptions,null,sqlname, beans,GetCUDResult);
	}
	
	



	public void updateBeans( DBOptions dbOptions,String sqlname, List beans,GetCUDResult GetCUDResult) throws SQLException {
		updateBeans(   dbOptions,null,sqlname, beans,GetCUDResult);
	}



	public void deleteBeans( DBOptions dbOptions,String sqlname, List beans,GetCUDResult GetCUDResult) throws SQLException {
		deleteBeans(   dbOptions,(String)null,sqlname, beans,GetCUDResult);
		
	}



	public void insertBean( DBOptions dbOptions,String sqlname, Object bean) throws SQLException {
		if(bean == null)
			return ;
//		List datas = new ArrayList();
//		datas.add(bean);
//		insertBeans( null,  sqlname,  datas);
		insertBean(   dbOptions,(String)null,sqlname, bean);
	}
	
	public void insertBean( DBOptions dbOptions,String sqlname, Object bean,GetCUDResult getCUDResult) throws SQLException {
		if(bean == null)
			return ;
//		List datas = new ArrayList();
//		datas.add(bean);
//		insertBeans( null,  sqlname,  datas);
		insertBean(   dbOptions,null,sqlname, bean, getCUDResult);
	}



	public void updateBean( DBOptions dbOptions,String sqlname, Object bean) throws SQLException {
		if(bean == null )
			return ;
//		List datas = new ArrayList();
//		datas.add(bean);
//		updateBeans( null,  sqlname,  datas);
		updateBean(   dbOptions,null,sqlname, bean);
	}

	

	public void deleteBean(DBOptions dbOptions,String sqlname, Object bean) throws SQLException {
		
		if(bean == null)
			return ;
//		List datas = new ArrayList();
//		datas.add(bean);
//		deleteBeans( null,  sqlname,  datas);
		deleteBean(  dbOptions,null,sqlname, bean);
	}
	
	
	public <T> List<T> queryList(DBOptions dbOptions,Class<T> beanType, String sqlname, Object... fields) throws SQLException
	{
		
		return queryListWithDBName(  dbOptions,beanType,null, sqlname, fields);
	}
	/**
	 * 
	 * @param beanType
	 * @param dbname
	 * @param sqlname
	 * @param offset
	 * @param pagesize
	 * @param fields
	 * @return
	 * @throws SQLException
	 */
	public ListInfo queryListInfoWithDBName(DBOptions dbOptions,Class<?> beanType,String dbname, String sqlname, long offset,int pagesize,Object... fields) throws SQLException
	{
		
		SQLInfo sql = getSqlInfo(dbname, sqlname);
		return SQLInfoExecutor.queryListInfoWithDBName(  dbOptions,beanType,dbname, sql, offset,pagesize,fields);
	}
	
	public ListInfo queryListInfoWithDBName2ndTotalsize(DBOptions dbOptions,Class<?> beanType,String dbname, String sqlname, long offset,int pagesize,long totalsize,Object... fields) throws SQLException
	{
		
		SQLInfo sql = getSqlInfo(dbname, sqlname);
		return SQLInfoExecutor.queryListInfoWithDBName2ndTotalsize(  dbOptions,beanType,dbname, sql, offset,pagesize,totalsize,fields);
	}
	public ListInfo queryListInfoWithDBName2ndTotalsizesql(DBOptions dbOptions,Class<?> beanType,String dbname, String sqlname, long offset,int pagesize,String totalsizesqlname,Object... fields) throws SQLException
	{
		
		SQLInfo sql = getSqlInfo(dbname, sqlname);
		SQLInfo totalsizesql = this.getSqlInfo(dbname, totalsizesqlname);
		return SQLInfoExecutor.queryListInfoWithDBName2ndTotalsizesql(  dbOptions,beanType,dbname, sql, offset,pagesize,totalsizesql,fields);
	}
	/**
	 * 
	 * @param beanType
	 * @param sqlname
	 * @param offset
	 * @param pagesize
	 * @param fields
	 * @return
	 * @throws SQLException
	 */
	public ListInfo queryListInfo(DBOptions dbOptions,Class<?> beanType, String sqlname, long offset,int pagesize,Object... fields) throws SQLException
	{
		return queryListInfoWithDBName(  dbOptions,beanType, null,sqlname, offset,pagesize,fields);
	}
	public ListInfo queryListInfoWithTotalsize(DBOptions dbOptions,Class<?> beanType, String sqlname, long offset,int pagesize,long totalsize,Object... fields) throws SQLException
	{
		return queryListInfoWithDBName2ndTotalsize(  dbOptions,beanType, null,sqlname, offset,pagesize,totalsize,fields);
	}
	public ListInfo queryListInfoWithTotalsizesql(DBOptions dbOptions,Class<?> beanType, String sqlname, long offset,int pagesize,String totalsizesqlname,Object... fields) throws SQLException
	{
		return queryListInfoWithDBName2ndTotalsizesql(  dbOptions,beanType, null,sqlname, offset,pagesize,totalsizesqlname,fields);
	}
	
	
	public <T> T queryObject(DBOptions dbOptions,Class<T> beanType, String sqlname, Object... fields) throws SQLException
	{
		return queryObjectWithDBName(  dbOptions,beanType,null, sqlname, fields);
		
		 
	}
	
	public <T> List<T> queryListWithDBName(DBOptions dbOptions,Class<T> beanType,String dbname, String sqlname, Object... fields) throws SQLException
	{		
		SQLInfo sql = getSqlInfo(dbname, sqlname);
		return SQLInfoExecutor.queryListWithDBName(  dbOptions, beanType, dbname,  sql, fields);
	}
	
	
	public <T> T queryObjectWithDBName(DBOptions dbOptions,Class<T> beanType,String dbname, String sqlname, Object... fields) throws SQLException
	{
		
		SQLInfo sql = getSqlInfo(dbname, sqlname);
		return SQLInfoExecutor.queryObjectWithDBName(  dbOptions, beanType, dbname,  sql, fields);
		 
	}
	
	
	public <T> List<T> queryListByRowHandler(DBOptions dbOptions,RowHandler rowhandler,Class<T> beanType, String sqlname, Object... fields) throws SQLException
	{
		
		return queryListWithDBNameByRowHandler(  dbOptions,rowhandler,beanType,null, sqlname, fields);
	}
	/**
	 * 
	 * @param rowhandler
	 * @param beanType
	 * @param dbname
	 * @param sqlname
	 * @param offset
	 * @param pagesize
	 * @param fields
	 * @return
	 * @throws SQLException
	 */
	public ListInfo queryListInfoWithDBNameByRowHandler(DBOptions dbOptions,RowHandler rowhandler,Class<?> beanType,String dbname, String sqlname, long offset,int pagesize,Object... fields) throws SQLException
	{
		
		SQLInfo sql = getSqlInfo(dbname, sqlname);
		return SQLInfoExecutor.queryListInfoWithDBNameByRowHandler(  dbOptions,rowhandler,beanType,dbname, sql, offset,pagesize,fields);
	}
	public ListInfo queryListInfoWithDBName2ndTotalsizeByRowHandler(DBOptions dbOptions,RowHandler rowhandler,Class<?> beanType,String dbname, String sqlname, long offset,int pagesize,long totalsize,Object... fields) throws SQLException
	{
		
		SQLInfo sql = getSqlInfo(dbname, sqlname);
		return SQLInfoExecutor.queryListInfoWithDBName2ndTotalsizeByRowHandler(  dbOptions,rowhandler,beanType,dbname, sql, offset,pagesize,totalsize,fields);
	}
	public ListInfo queryListInfoWithDBName2ndTotalsizesqlByRowHandler(DBOptions dbOptions,RowHandler rowhandler,Class<?> beanType,String dbname, String sqlname, long offset,int pagesize,String totalsizesqlname,Object... fields) throws SQLException
	{
		
		SQLInfo sql = getSqlInfo(dbname, sqlname);
		SQLInfo totalsizesql = this.getSqlInfo(dbname, totalsizesqlname);
		return SQLInfoExecutor.queryListInfoWithDBName2ndTotalsizesqlByRowHandler(  dbOptions,rowhandler,beanType,dbname, sql, offset,pagesize,totalsizesql,fields);
	}
	/**
	 * 
	 * @param rowhandler
	 * @param beanType
	 * @param sqlname
	 * @param offset
	 * @param pagesize
	 * @param fields
	 * @return
	 * @throws SQLException
	 */
	public ListInfo queryListInfoByRowHandler(DBOptions dbOptions,RowHandler rowhandler,Class<?> beanType, String sqlname, long offset,int pagesize,Object... fields) throws SQLException
	{
		return queryListInfoWithDBNameByRowHandler(   dbOptions,rowhandler,beanType, null,sqlname, offset,pagesize,fields);
	}
	
	public ListInfo queryListInfoWithTotalsizeByRowHandler(DBOptions dbOptions,RowHandler rowhandler,Class<?> beanType, String sqlname, long offset,int pagesize,long totalsize,Object... fields) throws SQLException
	{
		return queryListInfoWithDBName2ndTotalsizeByRowHandler(   dbOptions,rowhandler,beanType, null,sqlname, offset,pagesize,totalsize,fields);
	}
	public ListInfo queryListInfoWithTotalsizesqlByRowHandler(DBOptions dbOptions,RowHandler rowhandler,Class<?> beanType, String sqlname, long offset,int pagesize,String totalsqlname,Object... fields) throws SQLException
	{
		return queryListInfoWithDBName2ndTotalsizesqlByRowHandler(  dbOptions, rowhandler,beanType, null,sqlname, offset,pagesize,totalsqlname,fields);
	}
	
	public <T> T queryObjectByRowHandler(DBOptions dbOptions,RowHandler rowhandler,Class<T> beanType, String sqlname, Object... fields) throws SQLException
	{
		return queryObjectWithDBNameByRowHandler(  dbOptions,rowhandler,beanType,null, sqlname, fields);
		
		 
	}
	
	public <T> List<T> queryListWithDBNameByRowHandler(DBOptions dbOptions,RowHandler rowhandler,Class<T> beanType,String dbname, String sqlname, Object... fields) throws SQLException
	{
		
		SQLInfo sql = getSqlInfo(dbname, sqlname);
		return SQLInfoExecutor.queryListWithDBNameByRowHandler(   dbOptions, rowhandler, beanType, dbname,  sql,  fields);
	}
	
	
	public <T> T queryObjectWithDBNameByRowHandler(DBOptions dbOptions,RowHandler rowhandler,Class<T> beanType,String dbname, String sqlname, Object... fields) throws SQLException
	{
		
		SQLInfo sql = getSqlInfo(dbname, sqlname);
		return SQLInfoExecutor.queryObjectWithDBNameByRowHandler(   dbOptions, rowhandler, beanType, dbname,  sql,  fields);
		 
	}
	
	
	
	
	
	public void queryByNullRowHandler(DBOptions dbOptions,NullRowHandler rowhandler, String sqlname, Object... fields) throws SQLException
	{
		
		 queryWithDBNameByNullRowHandler(   dbOptions,rowhandler,null, sqlname, fields);
	}
	/**
	 * 
	 * @param rowhandler
	 * @param dbname
	 * @param sqlname
	 * @param offset
	 * @param pagesize
	 * @param fields
	 * @return
	 * @throws SQLException
	 */
	public ListInfo queryListInfoWithDBNameByNullRowHandler(DBOptions dbOptions,NullRowHandler rowhandler,String dbname, String sqlname, long offset,int pagesize,Object... fields) throws SQLException
	{
		
		SQLInfo sql = getSqlInfo(dbname, sqlname);
		return SQLInfoExecutor.queryListInfoWithDBNameByNullRowHandler(    dbOptions,rowhandler,dbname, sql, offset,pagesize,fields);
	}
	public ListInfo queryListInfoWithDBName2ndTotalsizeByNullRowHandler(DBOptions dbOptions,NullRowHandler rowhandler,String dbname, String sqlname, long offset,int pagesize,long totalsize,Object... fields) throws SQLException
	{
		
		SQLInfo sql = getSqlInfo(dbname, sqlname);
		return SQLInfoExecutor.queryListInfoWithDBName2ndTotalsizeByNullRowHandler(   dbOptions, rowhandler,dbname, sql, offset,pagesize,totalsize,fields);
	}
	public ListInfo queryListInfoWithDBName2ndTotalsizesqlByNullRowHandler(DBOptions dbOptions,NullRowHandler rowhandler,String dbname, String sqlname, long offset,int pagesize,String totalsizesqlname,Object... fields) throws SQLException
	{
		
		SQLInfo sql = getSqlInfo(dbname, sqlname);
		SQLInfo totalsizesql = this.getSqlInfo(dbname, totalsizesqlname);
		return SQLInfoExecutor.queryListInfoWithDBName2ndTotalsizesqlByNullRowHandler(  dbOptions,  rowhandler,dbname, sql, offset,pagesize,totalsizesql,fields);
	}
	/**
	 * 
	 * @param rowhandler
	 * @param sqlname
	 * @param offset
	 * @param pagesize
	 * @param fields
	 * @return
	 * @throws SQLException
	 */
	public ListInfo queryListInfoByNullRowHandler(DBOptions dbOptions,NullRowHandler rowhandler, String sqlname, long offset,int pagesize,Object... fields) throws SQLException
	{
		return queryListInfoWithDBNameByNullRowHandler(   dbOptions,rowhandler, null,sqlname, offset,pagesize,fields);
	}
	
	public ListInfo queryListInfoWithTotalsizeByNullRowHandler(DBOptions dbOptions,NullRowHandler rowhandler, String sqlname, long offset,int pagesize,long totalsize,Object... fields) throws SQLException
	{
		return queryListInfoWithDBName2ndTotalsizeByNullRowHandler(  dbOptions, rowhandler, null,sqlname, offset,pagesize,totalsize,fields);
	}
	
	public ListInfo queryListInfoWithTotalsizesqlByNullRowHandler(DBOptions dbOptions,NullRowHandler rowhandler, String sqlname, long offset,int pagesize,String totalsizesqlname,Object... fields) throws SQLException
	{
		return queryListInfoWithDBName2ndTotalsizesqlByNullRowHandler(   dbOptions,rowhandler, null,sqlname, offset,pagesize,totalsizesqlname,fields);
	}
	
	
	
	public void queryWithDBNameByNullRowHandler(DBOptions dbOptions,NullRowHandler rowhandler,String dbname, String sqlname, Object... fields) throws SQLException
	{
		
		SQLInfo sql = getSqlInfo(dbname, sqlname);
		SQLInfoExecutor.queryWithDBNameByNullRowHandler(   dbOptions, rowhandler, dbname,  sql,  fields);
	}
	
	
	public <T> List<T> queryListBean(DBOptions dbOptions,Class<T> beanType, String sqlname, Object bean) throws SQLException
	{
		
		return queryListBeanWithDBName(  dbOptions,beanType,null, sqlname, bean);
	}
	/**
	 * 
	 * @param beanType
	 * @param dbname
	 * @param sqlname
	 * @param offset
	 * @param pagesize
	 * @param totalsize
	 * @param bean
	 * @return
	 * @throws SQLException
	 */
	public ListInfo queryListInfoBeanWithDBName(DBOptions dbOptions,Class<?> beanType,String dbname, String sqlname, long offset,int pagesize,long totalsize,Object bean) throws SQLException
	{
		
		SQLInfo sql = getSqlInfo(dbname, sqlname);
		return SQLInfoExecutor.queryListInfoBeanWithDBName(   dbOptions, beanType, dbname,  sql,  offset, pagesize, totalsize,bean);
	}
	
	public ListInfo queryListInfoBeanWithDBName(DBOptions dbOptions,Class<?> beanType,String dbname, String sqlname, long offset,int pagesize,String totalsizesqlname,Object bean) throws SQLException
	{
		
		SQLInfo sql = getSqlInfo(dbname, sqlname);
		SQLInfo totalsizesql = this.getSqlInfo(dbname, totalsizesqlname);
		return SQLInfoExecutor.queryListInfoBeanWithDBName(    dbOptions,beanType, dbname,  sql,  offset, pagesize, totalsizesql,bean);
	}
	
	/**
	 * 
	 * @param beanType
	 * @param dbname
	 * @param sqlname
	 * @param offset
	 * @param pagesize
	 * @param bean
	 * @return
	 * @throws SQLException
	 */
	public ListInfo queryListInfoBeanWithDBName(DBOptions dbOptions,Class<?> beanType,String dbname, String sqlname, long offset,int pagesize,Object bean) throws SQLException
	{
		
		SQLInfo sql = getSqlInfo(dbname, sqlname);
		return SQLInfoExecutor.queryListInfoBeanWithDBName(    dbOptions,beanType, dbname,  sql,  offset, pagesize, -1L,bean);
	}
	/**
	 * 
	 * @param beanType
	 * @param sqlname
	 * @param offset
	 * @param pagesize
	 * @param totalsize
	 * @param bean
	 * @return
	 * @throws SQLException
	 */
	public ListInfo queryListInfoBean(DBOptions dbOptions,Class<?> beanType, String sqlname, long offset,int pagesize,long totalsize,Object bean) throws SQLException
	{
		return queryListInfoBeanWithDBName(  dbOptions,beanType, null,sqlname, offset,pagesize,totalsize,bean);
	}
	
	public ListInfo queryListInfoBean(DBOptions dbOptions,Class<?> beanType, String sqlname, long offset,int pagesize,String totalsizesqlname,Object bean) throws SQLException
	{
		return queryListInfoBeanWithDBName(  dbOptions,beanType, null,sqlname, offset,pagesize,totalsizesqlname,bean);
	}
	
	public ListInfo queryListInfoBean(DBOptions dbOptions,Class<?> beanType, String sqlname, long offset,int pagesize,Object bean) throws SQLException
	{
		return queryListInfoBeanWithDBName(  dbOptions,beanType, null,sqlname, offset,pagesize,-1L,bean);
	}
	public String queryField( DBOptions dbOptions,String sqlname, Object... fields) throws SQLException
	{
		return queryFieldWithDBName(  dbOptions,null, sqlname, fields);
	}
	public  String queryFieldBean( DBOptions dbOptions,String sqlname, Object bean) throws SQLException
	{
		return queryFieldBeanWithDBName(  dbOptions,null, sqlname, bean);
	}
	
	public  String queryFieldBeanWithDBName(DBOptions dbOptions,String dbname, String sqlname, Object bean) throws SQLException
	{
		
		SQLInfo sql = getSqlInfo(dbname, sqlname);
		return SQLInfoExecutor.queryFieldBeanWithDBName(    dbOptions,  dbname,  sql,  bean);
	}
	
	public  String queryFieldWithDBName(DBOptions dbOptions,String dbname, String sqlname, Object... fields) throws SQLException
	{
		
		SQLInfo sql = getSqlInfo(dbname, sqlname);
		return SQLInfoExecutor.queryFieldWithDBName(    dbOptions,  dbname,  sql,  fields);
	}
	
	
	
	/**
	 * 
	 * @param <T>
	 * @param beanType
	 * @param sqlname
	 * @param fields
	 * @return
	 * @throws SQLException
	 */
	
	
	public <T> T queryTField( DBOptions dbOptions,Class<T> beanType,String sqlname, Object... fields) throws SQLException
	{
		return queryTFieldWithDBName(  dbOptions,null, beanType,sqlname, fields);
	}
	public  <T> T queryTFieldBean( DBOptions dbOptions,Class<T> type,String sqlname, Object bean) throws SQLException
	{
		return queryTFieldBeanWithDBName(  dbOptions,null, type,sqlname, bean);
	}
	
	public <T> T queryTFieldBeanWithDBName(DBOptions dbOptions,String dbname, Class<T> type,String sqlname, Object bean) throws SQLException
	{
		
		SQLInfo sql = getSqlInfo(dbname, sqlname);
		return SQLInfoExecutor.queryTFieldBeanWithDBName(    dbOptions,   dbname,  type, sql,  bean);
	}
	
	public <T> T queryTFieldWithDBName(DBOptions dbOptions,String dbname, Class<T> type,String sqlname, Object... fields) throws SQLException
	{
		
		SQLInfo sql = getSqlInfo(dbname, sqlname);
		return SQLInfoExecutor.queryTFieldWithDBName(     dbOptions,  dbname,  type, sql,  fields);
	}
	
	
	/**行处理器开始*/
	/**
	 * 
	 * @param <T>
	 * @param beanType
	 * @param sqlname
	 * @param fields
	 * @return
	 * @throws SQLException
	 */
	
	
	public <T> T queryTField( DBOptions dbOptions,Class<T> beanType,FieldRowHandler<T> fieldRowHandler,String sqlname, Object... fields) throws SQLException
	{
		return queryTFieldWithDBName(  dbOptions,null, beanType,fieldRowHandler,sqlname, fields);
	}
	public  <T> T queryTFieldBean( DBOptions dbOptions,Class<T> type,FieldRowHandler<T> fieldRowHandler,String sqlname, Object bean) throws SQLException
	{
		return queryTFieldBeanWithDBName(  dbOptions,null, type, fieldRowHandler,sqlname, bean);
	}
	
	public <T> T queryTFieldBeanWithDBName(DBOptions dbOptions,String dbname, Class<T> type,FieldRowHandler<T> fieldRowHandler,String sqlname, Object bean) throws SQLException
	{
		
		SQLInfo sql = getSqlInfo(dbname, sqlname);
		return SQLInfoExecutor.queryTFieldBeanWithDBName(    dbOptions,   dbname,  type,fieldRowHandler, sql,  bean);
	}
	
	public <T> T queryTFieldWithDBName(DBOptions dbOptions,String dbname, Class<T> type,FieldRowHandler<T> fieldRowHandler,String sqlname, Object... fields) throws SQLException
	{
		
		SQLInfo sql = getSqlInfo(dbname, sqlname);
		return SQLInfoExecutor.queryTFieldWithDBName(     dbOptions, dbname,  type,fieldRowHandler, sql,  fields);
	}
	
	/**行处理器结束*/
	/**
	 * 
	 * @param <T>
	 * @param beanType
	 * @param sqlname
	 * @param bean
	 * @return
	 * @throws SQLException
	 */
	
	public <T> T queryObjectBean(DBOptions dbOptions,Class<T> beanType, String sqlname, Object bean) throws SQLException
	{
		return queryObjectBeanWithDBName(  dbOptions,beanType,null, sqlname, bean);
		
		 
	}
	
	public <T> List<T> queryListBeanWithDBName(DBOptions dbOptions,Class<T> beanType,String dbname, String sqlname, Object bean) throws SQLException
	{
		
		SQLInfo sql = getSqlInfo(dbname, sqlname);
		return SQLInfoExecutor.queryListBeanWithDBName(   dbOptions,  beanType,dbname, sql, bean);
	}
	
	public <T> T queryObjectBeanWithDBName(DBOptions dbOptions,Class<T> beanType,String dbname, String sqlname, Object bean) throws SQLException
	{
		
		SQLInfo sql = getSqlInfo(dbname, sqlname);
		return SQLInfoExecutor.queryObjectBeanWithDBName(    dbOptions, beanType,dbname, sql, bean);
		 
	}
	
	
	public <T> List<T> queryListBeanByRowHandler(DBOptions dbOptions,RowHandler rowhandler,Class<T> beanType, String sqlname, Object bean) throws SQLException
	{
		
		return queryListBeanWithDBNameByRowHandler(  dbOptions,rowhandler,beanType,null, sqlname, bean);
	}
	/**
	 * 
	 * @param rowhandler
	 * @param beanType
	 * @param dbname
	 * @param sqlname
	 * @param offset
	 * @param pagesize
	 * @param totalsize
	 * @param bean
	 * @return
	 * @throws SQLException
	 */
	public ListInfo queryListInfoBeanWithDBNameByRowHandler(DBOptions dbOptions,RowHandler rowhandler,Class<?> beanType,String dbname, String sqlname, long offset,int pagesize,long totalsize,Object  bean) throws SQLException
	{
		
		SQLInfo sql = getSqlInfo(dbname, sqlname);
		return SQLInfoExecutor.queryListInfoBeanWithDBNameByRowHandler(   dbOptions, rowhandler,beanType,dbname, sql, offset,pagesize,totalsize,bean);
	}
	public ListInfo queryListInfoBeanWithDBNameByRowHandler(DBOptions dbOptions,RowHandler rowhandler,Class<?> beanType,String dbname, String sqlname, long offset,int pagesize,String totalsizesqlname,Object  bean) throws SQLException
	{
		
		SQLInfo sql = getSqlInfo(dbname, sqlname);
		SQLInfo totalsizesql = this.getSqlInfo(dbname, totalsizesqlname);
		return SQLInfoExecutor.queryListInfoBeanWithDBNameByRowHandler(   dbOptions, rowhandler,beanType,dbname, sql, offset,pagesize,totalsizesql ,bean);
	}
	/**
	 * 
	 * @param rowhandler
	 * @param beanType
	 * @param dbname
	 * @param sqlname
	 * @param offset
	 * @param pagesize
	 * @param bean
	 * @return
	 * @throws SQLException
	 */
	public ListInfo queryListInfoBeanWithDBNameByRowHandler(DBOptions dbOptions,RowHandler rowhandler,Class<?> beanType,String dbname, String sqlname, long offset,int pagesize,Object  bean) throws SQLException
	{
		
		SQLInfo sql = getSqlInfo(dbname, sqlname);
		return SQLInfoExecutor.queryListInfoBeanWithDBNameByRowHandler(    dbOptions,rowhandler,beanType,dbname, sql, offset,pagesize,-1L,bean);
	}
	/**
	 * 
	 * @param rowhandler
	 * @param beanType
	 * @param sqlname
	 * @param offset
	 * @param pagesize
	 * @param totalsizesqlname
	 * @param bean
	 * @return
	 * @throws SQLException
	 */
	public ListInfo queryListInfoBeanByRowHandler(DBOptions dbOptions,RowHandler rowhandler,Class<?> beanType, String sqlname, long offset,int pagesize,String totalsizesqlname,Object bean) throws SQLException
	{
		return queryListInfoBeanWithDBNameByRowHandler(   dbOptions,rowhandler,beanType, null,sqlname, offset,pagesize,totalsizesqlname,bean);
	}
	
	public ListInfo queryListInfoBeanByRowHandler(DBOptions dbOptions,RowHandler rowhandler,Class<?> beanType, String sqlname, long offset,int pagesize,long totalsize,Object bean) throws SQLException
	{
		return queryListInfoBeanWithDBNameByRowHandler(   dbOptions,rowhandler,beanType, null,sqlname, offset,pagesize,totalsize,bean);
	}
	
	
	
	public ListInfo queryListInfoBeanByRowHandler(DBOptions dbOptions,RowHandler rowhandler,Class<?> beanType, String sqlname, long offset,int pagesize,Object bean) throws SQLException
	{
		return queryListInfoBeanWithDBNameByRowHandler(   dbOptions,rowhandler,beanType, null,sqlname, offset,pagesize,-1L,bean);
	}
	
	
	public <T> T queryObjectBeanByRowHandler(DBOptions dbOptions,RowHandler rowhandler,Class<T> beanType, String sqlname, Object bean) throws SQLException
	{
		return queryObjectBeanWithDBNameByRowHandler(  dbOptions,rowhandler,beanType,null, sqlname, bean);
		
		 
	}
	
	public <T> List<T> queryListBeanWithDBNameByRowHandler(DBOptions dbOptions,RowHandler rowhandler,Class<T> beanType,String dbname, String sqlname, Object bean) throws SQLException
	{
		
		SQLInfo sql = getSqlInfo(dbname, sqlname);
		return SQLInfoExecutor.queryListBeanWithDBNameByRowHandler(    dbOptions,rowhandler, beanType, dbname,  sql,  bean);
	}
	
	
	public <T> T queryObjectBeanWithDBNameByRowHandler(DBOptions dbOptions,RowHandler rowhandler,Class<T> beanType,String dbname, String sqlname, Object bean) throws SQLException
	{
		
		SQLInfo sql = getSqlInfo(dbname, sqlname);
		return SQLInfoExecutor.queryObjectBeanWithDBNameByRowHandler(  dbOptions,  rowhandler, beanType, dbname,  sql,  bean);
		 
	}
	
	
	
	
	
	public void queryBeanByNullRowHandler(DBOptions dbOptions,NullRowHandler rowhandler, String sqlname, Object bean) throws SQLException
	{
		
		 queryBeanWithDBNameByNullRowHandler(   dbOptions,rowhandler,null, sqlname, bean);
	}
	/**
	 * 
	 * @param rowhandler
	 * @param dbname
	 * @param sqlname
	 * @param offset
	 * @param pagesize
	 * @param totalsize
	 * @param bean
	 * @return
	 * @throws SQLException
	 */
	public ListInfo queryListInfoBeanWithDBNameByNullRowHandler(DBOptions dbOptions,NullRowHandler rowhandler,String dbname, String sqlname, long offset,int pagesize,long totalsize,Object bean) throws SQLException
	{
		
		SQLInfo sql = getSqlInfo(dbname, sqlname);
		return SQLInfoExecutor.queryListInfoBeanWithDBNameByNullRowHandler(   dbOptions, rowhandler, dbname, sql, offset,pagesize,totalsize,bean);
	}
	public ListInfo queryListInfoBeanWithDBNameByNullRowHandler(DBOptions dbOptions,NullRowHandler rowhandler,String dbname, String sqlname, long offset,int pagesize,String totalsizesqlname,Object bean) throws SQLException
	{
		
		SQLInfo sql = getSqlInfo(dbname, sqlname);
		SQLInfo totalsizesql = this.getSqlInfo(dbname, totalsizesqlname);
		return SQLInfoExecutor.queryListInfoBeanWithDBNameByNullRowHandler(    dbOptions,rowhandler, dbname, sql, offset,pagesize,totalsizesql,bean);
	}
	/**
	 * 
	 * @param rowhandler
	 * @param dbname
	 * @param sqlname
	 * @param offset
	 * @param pagesize

	 * @param bean
	 * @return
	 * @throws SQLException
	 */
	public ListInfo queryListInfoBeanWithDBNameByNullRowHandler(DBOptions dbOptions,NullRowHandler rowhandler,String dbname, String sqlname, long offset,int pagesize,Object bean) throws SQLException
	{
		
		SQLInfo sql = getSqlInfo(dbname, sqlname);
		return SQLInfoExecutor.queryListInfoBeanWithDBNameByNullRowHandler(    dbOptions,rowhandler, dbname, sql, offset,pagesize,-1L,bean);
	}
	/**
	 * 
	 * @param rowhandler
	 * @param sqlname
	 * @param offset
	 * @param pagesize
	 * @param totalsize
	 * @param bean
	 * @return
	 * @throws SQLException
	 */
	public ListInfo queryListInfoBeanByNullRowHandler(DBOptions dbOptions,NullRowHandler rowhandler, String sqlname, long offset,int pagesize,long totalsize,Object bean) throws SQLException
	{
		return queryListInfoBeanWithDBNameByNullRowHandler(   dbOptions,rowhandler, null,sqlname, offset,pagesize,totalsize,bean);
	}
	
	public ListInfo queryListInfoBeanByNullRowHandler(DBOptions dbOptions,NullRowHandler rowhandler, String sqlname, long offset,int pagesize,String totalsizesqlname,Object bean) throws SQLException
	{
		return queryListInfoBeanWithDBNameByNullRowHandler(   dbOptions,rowhandler, null,sqlname, offset,pagesize,totalsizesqlname,bean);
	}
	public ListInfo queryListInfoBeanByNullRowHandler(DBOptions dbOptions,NullRowHandler rowhandler, String sqlname, long offset,int pagesize,Object bean) throws SQLException
	{
		return queryListInfoBeanWithDBNameByNullRowHandler(   dbOptions,rowhandler, null,sqlname, offset,pagesize,-1L,bean);
	}
	
	
	public void queryBeanWithDBNameByNullRowHandler(DBOptions dbOptions,NullRowHandler rowhandler,String dbname, String sqlname, Object bean) throws SQLException
	{
		
		SQLInfo sql = getSqlInfo(dbname, sqlname);
		SQLInfoExecutor.queryBeanWithDBNameByNullRowHandler(   dbOptions, rowhandler,  dbname,  sql,  bean);
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
	 * @param sqlname
	 * @param offset
	 * @param pagesize
	 * @param fields
	 * @return
	 * @throws SQLException
	 */
	public ListInfo moreListInfoWithDBNameByRowHandler(DBOptions dbOptions,RowHandler rowhandler,Class<?> beanType,String dbname, String sqlname, long offset,int pagesize,Object... fields) throws SQLException
	{
		
		SQLInfo sql = getSqlInfo(dbname, sqlname);
		return SQLInfoExecutor.moreListInfoWithDBNameByRowHandler(  dbOptions,rowhandler,beanType,dbname, sql, offset,pagesize,fields);
	}
	
	/**
	 * 
	 * @param rowhandler
	 * @param dbname
	 * @param sqlname
	 * @param offset
	 * @param pagesize
	 * @param fields
	 * @return
	 * @throws SQLException
	 */
	public ListInfo moreListInfoWithDBNameByNullRowHandler(DBOptions dbOptions,NullRowHandler rowhandler,String dbname, String sqlname, long offset,int pagesize,Object... fields) throws SQLException
	{
		
		SQLInfo sql = getSqlInfo(dbname, sqlname);
		return SQLInfoExecutor.moreListInfoWithDBNameByNullRowHandler(   dbOptions, rowhandler,dbname, sql, offset,pagesize,fields);
	}
	
		/**
	 * 
	 * @param beanType
	 * @param dbname
	 * @param sqlname
	 * @param offset
	 * @param pagesize
	 * @param fields
	 * @return
	 * @throws SQLException
	 */
	public ListInfo moreListInfoWithDBName(DBOptions dbOptions,Class<?> beanType,String dbname, String sqlname, long offset,int pagesize,Object... fields) throws SQLException
	{
		
		SQLInfo sql = getSqlInfo(dbname, sqlname);
		return SQLInfoExecutor.moreListInfoWithDBName(  dbOptions,beanType,dbname, sql, offset,pagesize,fields);
	}
	
		/**
	 * 
	 * @param rowhandler
	 * @param beanType
	 * @param sqlname
	 * @param offset
	 * @param pagesize
	 * @param fields
	 * @return
	 * @throws SQLException
	 */
	public ListInfo moreListInfoByRowHandler(DBOptions dbOptions,RowHandler rowhandler,Class<?> beanType, String sqlname, long offset,int pagesize,Object... fields) throws SQLException
	{
		return moreListInfoWithDBNameByRowHandler(   dbOptions,rowhandler,beanType, null,sqlname, offset,pagesize,fields);
	}
	
		/**
	 * 
	 * @param rowhandler
	 * @param sqlname
	 * @param offset
	 * @param pagesize
	 * @param fields
	 * @return
	 * @throws SQLException
	 */
	public ListInfo moreListInfoByNullRowHandler(DBOptions dbOptions,NullRowHandler rowhandler, String sqlname, long offset,int pagesize,Object... fields) throws SQLException
	{
		return moreListInfoWithDBNameByNullRowHandler(   dbOptions,rowhandler, null,sqlname, offset,pagesize,fields);
	}
	
		/**
	 * 
	 * @param rowhandler
	 * @param beanType
	 * @param dbname
	 * @param sqlname
	 * @param offset
	 * @param pagesize
	 * @param bean
	 * @return
	 * @throws SQLException
	 */
	public ListInfo moreListInfoBeanWithDBNameByRowHandler(DBOptions dbOptions,RowHandler rowhandler,Class<?> beanType,String dbname, String sqlname, long offset,int pagesize,Object  bean) throws SQLException
	{
		
		SQLInfo sql = getSqlInfo(dbname, sqlname);
		return SQLInfoExecutor.moreListInfoBeanWithDBNameByRowHandler(   dbOptions, rowhandler,beanType,dbname, sql, offset,pagesize,bean);
	}
	
		/**
	 * 
	 * @param rowhandler
	 * @param dbname
	 * @param sqlname
	 * @param offset
	 * @param pagesize

	 * @param bean
	 * @return
	 * @throws SQLException
	 */
	public ListInfo moreListInfoBeanWithDBNameByNullRowHandler(DBOptions dbOptions,NullRowHandler rowhandler,String dbname, String sqlname, long offset,int pagesize,Object bean) throws SQLException
	{
		
		SQLInfo sql = getSqlInfo(dbname, sqlname);
		return SQLInfoExecutor.moreListInfoBeanWithDBNameByNullRowHandler(   dbOptions, rowhandler, dbname, sql, offset,pagesize,bean);
	}
	
		/**
	 * 
	 * @param beanType
	 * @param dbname
	 * @param sqlname
	 * @param offset
	 * @param pagesize
	 * @param bean
	 * @return
	 * @throws SQLException
	 */
	public ListInfo moreListInfoBeanWithDBName(DBOptions dbOptions,Class<?> beanType,String dbname, String sqlname, long offset,int pagesize,Object bean) throws SQLException
	{
		
		SQLInfo sql = getSqlInfo(dbname, sqlname);
		return SQLInfoExecutor.moreListInfoBeanWithDBName(    dbOptions,beanType, dbname,  sql,  offset, pagesize, bean);
	}
	
		public ListInfo moreListInfoBeanByRowHandler(DBOptions dbOptions,RowHandler rowhandler,Class<?> beanType, String sqlname, long offset,int pagesize,Object bean) throws SQLException
	{
			SQLInfo sql = getSqlInfo(null, sqlname);
			return SQLInfoExecutor.moreListInfoBeanWithDBNameByRowHandler(   dbOptions, rowhandler,beanType,(String)null, sql, offset,pagesize,bean);
	}
	
		public ListInfo moreListInfoBeanByNullRowHandler(DBOptions dbOptions,NullRowHandler rowhandler, String sqlname, long offset,int pagesize,Object bean) throws SQLException
	{
			SQLInfo sql = getSqlInfo(null, sqlname);
			return SQLInfoExecutor.moreListInfoBeanWithDBNameByNullRowHandler(   dbOptions, rowhandler, (String)null, sql, offset,pagesize,bean);
	}
	
		public ListInfo moreListInfoBean(DBOptions dbOptions,Class<?> beanType, String sqlname, long offset,int pagesize,Object bean) throws SQLException
	{
			SQLInfo sql = getSqlInfo(null, sqlname);
			return SQLInfoExecutor.moreListInfoBeanWithDBName(    dbOptions,beanType, (String)null,  sql,  offset, pagesize, bean);
	}
	
		/**
	 * 
	 * @param beanType
	 * @param sqlname
	 * @param offset
	 * @param pagesize
	 * @param fields
	 * @return
	 * @throws SQLException
	 */
	public ListInfo moreListInfo(DBOptions dbOptions,Class<?> beanType, String sqlname, long offset,int pagesize,Object... fields) throws SQLException
	{
		return moreListInfoWithDBName(  dbOptions,beanType, null,sqlname, offset,pagesize,fields);
	}
	
	

}
