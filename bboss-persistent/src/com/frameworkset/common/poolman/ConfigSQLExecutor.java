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

import java.sql.SQLException;
import java.util.List;

import org.frameworkset.persitent.util.SQLInfo;
import org.frameworkset.persitent.util.SQLUtil;

import com.frameworkset.common.poolman.handle.FieldRowHandler;
import com.frameworkset.common.poolman.handle.NullRowHandler;
import com.frameworkset.common.poolman.handle.RowHandler;
import com.frameworkset.util.ListInfo;

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
public class ConfigSQLExecutor  {
	
	private SQLUtil context ;
	public ConfigSQLExecutor(String sqlfile)
	{
		context = SQLUtil.getInstance(sqlfile);
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
	public void insertBeans(String dbname, String sqlname, List beans) throws SQLException {
		
		if(beans == null || beans.size() == 0)
			return ;
		SQLInfo sql = getSqlInfo(dbname,sqlname);
		SQLInfoExecutor.insertBeans( dbname,  sql,  beans);
	}
	public void insertBeans(String dbname, String sqlname, List beans,GetCUDResult getCUDResult) throws SQLException {
		
		if(beans == null || beans.size() == 0)
			return ;
		SQLInfo sql = getSqlInfo(dbname,sqlname);
		SQLInfoExecutor.insertBeans( dbname,  sql,  beans,getCUDResult);
	}
	
	public Object update( String sqlname, Object... fields) throws SQLException {
		SQLInfo sql = getSqlInfo(null,sqlname);
		return SQLInfoExecutor.update(sql, fields);
	}
	



	public Object delete(String sqlname, Object... fields) throws SQLException {
		SQLInfo sql = getSqlInfo(null,sqlname);
		return SQLInfoExecutor.delete(sql, fields);
		
	}
	
//	public static void deleteByKeys(String sql, Object... fields) throws SQLException {
//		executeBatch(null, sql,PreparedDBUtil.DELETE, fields);
//		
//	}
//	public static void deleteByKeysWithDBName(String dbname,String sql, Object... fields) throws SQLException {
//		executeBatch(null, sql,PreparedDBUtil.DELETE, fields);
//		
//	}
	
	
	public void deleteByKeys(String sqlname, int... fields) throws SQLException {
		SQLInfo sql = getSqlInfo(null,sqlname);
		SQLInfoExecutor.deleteByKeys(sql, fields);
//		executeBatch(null, sql,PreparedDBUtil.DELETE, fields);
		
	}
	public  void deleteByKeysWithDBName(String dbname,String sqlname, int... fields) throws SQLException {
		SQLInfo sql = getSqlInfo(dbname,sqlname);
		SQLInfoExecutor.deleteByKeysWithDBName(dbname,sql, fields);
		
	}
	
	public void deleteByLongKeys(String sqlname, long... fields) throws SQLException {
		SQLInfo sql = getSqlInfo(null,sqlname);
		SQLInfoExecutor.deleteByLongKeys(sql, fields);
//		executeBatch(null, sql,PreparedDBUtil.DELETE, fields);
		
	}
	public void deleteByLongKeysWithDBName(String dbname,String sqlname, long... fields) throws SQLException {
		SQLInfo sql = getSqlInfo(dbname,sqlname);
		SQLInfoExecutor.deleteByLongKeysWithDBName(dbname,sql, fields);
//		executeBatch(dbname, sql,PreparedDBUtil.DELETE, fields);
		
	}
	
	public void deleteByKeys(String sqlname, String... fields) throws SQLException {
		SQLInfo sql = getSqlInfo(null,sqlname);
		SQLInfoExecutor.deleteByKeys(sql, fields);
//		executeBatch(null, sql,PreparedDBUtil.DELETE, fields);
		
	}
	public void deleteByKeysWithDBName(String dbname,String sqlname, String... fields) throws SQLException {
		SQLInfo sql = getSqlInfo(dbname,sqlname);
		SQLInfoExecutor.deleteByKeysWithDBName(dbname,sql, fields);
//		executeBatch(dbname, sql,PreparedDBUtil.DELETE, fields);
		
	}
	
	public void deleteByShortKeys(String sqlname, short... fields) throws SQLException {
		SQLInfo sql = getSqlInfo(null,sqlname);
		SQLInfoExecutor.deleteByShortKeys(sql, fields);
//		executeBatch(null, sql,PreparedDBUtil.DELETE, fields);
		
	}
	public void deleteByShortKeysWithDBName(String dbname,String sqlname, short... fields) throws SQLException {
		SQLInfo sql = getSqlInfo(dbname,sqlname);
		SQLInfoExecutor.deleteByShortKeysWithDBName(dbname,sql, fields);
//		executeBatch(dbname, sql,PreparedDBUtil.DELETE, fields);
	}



	public Object insert(String sqlname, Object... fields) throws SQLException {
		SQLInfo sql = getSqlInfo(null,sqlname);
		return SQLInfoExecutor.insert(sql, fields);
//		return execute(null, sql,PreparedDBUtil.INSERT, fields);
	}
	
	public  Object updateWithDBName(String dbname, String sqlname, Object... fields) throws SQLException {
		SQLInfo sql = getSqlInfo(dbname,sqlname);
		return SQLInfoExecutor.updateWithDBName(dbname,sql, fields);
//		return execute(dbname, sql,PreparedDBUtil.UPDATE, fields);
	}
	
	public  Object deleteWithDBName(String dbname, String sqlname, Object... fields) throws SQLException {
		SQLInfo sql = getSqlInfo(dbname,sqlname);
		return SQLInfoExecutor.deleteWithDBName(dbname,sql, fields);
//		return execute(dbname, sql,PreparedDBUtil.DELETE, fields);
		
	}



	public  Object insertWithDBName(String dbname, String sqlname, Object... fields) throws SQLException {
		SQLInfo sql = getSqlInfo(dbname,sqlname);
		return SQLInfoExecutor.insertWithDBName(dbname,sql, fields);
//		return execute(dbname, sql,PreparedDBUtil.INSERT, fields);
	}

	public void updateBeans(String dbname, String sqlname, List beans,GetCUDResult GetCUDResult) throws SQLException {
		if(beans == null || beans.size() == 0)
			return ;
		SQLInfo sql = getSqlInfo(dbname,sqlname);
		SQLInfoExecutor.updateBeans(dbname,sql, beans,GetCUDResult);
//		execute( dbname,  sql,  beans,PreparedDBUtil.UPDATE);
	}
	
	public void updateBeans(String dbname, String sqlname, List beans) throws SQLException {
		if(beans == null || beans.size() == 0)
			return ;
		SQLInfo sql = getSqlInfo(dbname,sqlname);
		SQLInfoExecutor.updateBeans(dbname,sql, beans);
//		execute( dbname,  sql,  beans,PreparedDBUtil.UPDATE);
	}



	public void deleteBeans(String dbname, String sqlname, List beans) throws SQLException {
		if(beans == null || beans.size() == 0)
			return ;
		SQLInfo sql = getSqlInfo(dbname,sqlname);
		SQLInfoExecutor.deleteBeans(dbname,sql, beans);
//		execute( dbname,  sql,  beans,PreparedDBUtil.DELETE);
		
	}
	
	public void deleteBeans(String dbname, String sqlname, List beans,GetCUDResult GetCUDResult) throws SQLException {
		if(beans == null || beans.size() == 0)
			return ;
		SQLInfo sql = getSqlInfo(dbname,sqlname);
		SQLInfoExecutor.deleteBeans(dbname,sql, beans,GetCUDResult);
//		execute( dbname,  sql,  beans,PreparedDBUtil.DELETE);
		
	}


	public void insertBean(String dbname, String sqlname, Object bean) throws SQLException {
		insertBean(dbname, sqlname, bean,(GetCUDResult)null);
	}
	public void insertBean(String dbname, String sqlname, Object bean,GetCUDResult getCUDResult) throws SQLException {
		if(bean == null)
			return ;
//		List datas = new ArrayList();
//		datas.add(bean);
//		insertBeans( dbname,  sql,  datas);
		SQLInfo sql = getSqlInfo(dbname,sqlname);
		SQLInfoExecutor.insertBean(dbname,sql, bean,getCUDResult);
	}



	public  void updateBean(String dbname, String sqlname, Object bean) throws SQLException {
		if(bean == null )
			return ;
//		List datas = new ArrayList();
//		datas.add(bean);
//		updateBeans( dbname,  sql,  datas);
		SQLInfo sql = getSqlInfo(dbname,sqlname);
		SQLInfoExecutor.updateBean(dbname,sql, bean);
	}

	

	public void deleteBean(String dbname, String sqlname, Object bean) throws SQLException {
		
		if(bean == null)
			return ;
//		List datas = new ArrayList();
//		datas.add(bean);
//		deleteBeans( dbname,  sql,  datas);
		SQLInfo sql = getSqlInfo(dbname,sqlname);
		SQLInfoExecutor.deleteBean(dbname,sql, bean);
	}
	
	public void insertBeans(String sqlname, List beans) throws SQLException {
		insertBeans( null,sqlname, beans); 
	}
	
	



	public void updateBeans( String sqlname, List beans) throws SQLException {
		updateBeans( null,sqlname, beans); 
	}



	public void deleteBeans( String sqlname, List beans) throws SQLException {
		deleteBeans( null,sqlname, beans); 
		
	}
	
	public void insertBeans(String sqlname, List beans,GetCUDResult GetCUDResult) throws SQLException {
		insertBeans( null,sqlname, beans,GetCUDResult); 
	}
	
	



	public void updateBeans( String sqlname, List beans,GetCUDResult GetCUDResult) throws SQLException {
		updateBeans( null,sqlname, beans,GetCUDResult); 
	}



	public void deleteBeans( String sqlname, List beans,GetCUDResult GetCUDResult) throws SQLException {
		deleteBeans( (String)null,sqlname, beans,GetCUDResult); 
		
	}



	public void insertBean( String sqlname, Object bean) throws SQLException {
		if(bean == null)
			return ;
//		List datas = new ArrayList();
//		datas.add(bean);
//		insertBeans( null,  sqlname,  datas);
		insertBean( (String)null,sqlname, bean);
	}
	
	public void insertBean( String sqlname, Object bean,GetCUDResult getCUDResult) throws SQLException {
		if(bean == null)
			return ;
//		List datas = new ArrayList();
//		datas.add(bean);
//		insertBeans( null,  sqlname,  datas);
		insertBean( null,sqlname, bean, getCUDResult);
	}



	public void updateBean( String sqlname, Object bean) throws SQLException {
		if(bean == null )
			return ;
//		List datas = new ArrayList();
//		datas.add(bean);
//		updateBeans( null,  sqlname,  datas);
		updateBean( null,sqlname, bean);
	}

	

	public void deleteBean(String sqlname, Object bean) throws SQLException {
		
		if(bean == null)
			return ;
//		List datas = new ArrayList();
//		datas.add(bean);
//		deleteBeans( null,  sqlname,  datas);
		deleteBean(null,sqlname, bean);
	}
	
	
	public <T> List<T> queryList(Class<T> beanType, String sqlname, Object... fields) throws SQLException
	{
		
		return queryListWithDBName(beanType,null, sqlname, fields); 
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
	public ListInfo queryListInfoWithDBName(Class<?> beanType,String dbname, String sqlname, long offset,int pagesize,Object... fields) throws SQLException
	{
		
		SQLInfo sql = getSqlInfo(dbname, sqlname);
		return SQLInfoExecutor.queryListInfoWithDBName(beanType,dbname, sql, offset,pagesize,fields);  	 
	}
	
	public ListInfo queryListInfoWithDBName2ndTotalsize(Class<?> beanType,String dbname, String sqlname, long offset,int pagesize,long totalsize,Object... fields) throws SQLException
	{
		
		SQLInfo sql = getSqlInfo(dbname, sqlname);
		return SQLInfoExecutor.queryListInfoWithDBName2ndTotalsize(beanType,dbname, sql, offset,pagesize,totalsize,fields);  	 
	}
	public ListInfo queryListInfoWithDBName2ndTotalsizesql(Class<?> beanType,String dbname, String sqlname, long offset,int pagesize,String totalsizesqlname,Object... fields) throws SQLException
	{
		
		SQLInfo sql = getSqlInfo(dbname, sqlname);
		SQLInfo totalsizesql = this.getSqlInfo(dbname, totalsizesqlname);
		return SQLInfoExecutor.queryListInfoWithDBName2ndTotalsizesql(beanType,dbname, sql, offset,pagesize,totalsizesql,fields);  	 
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
	public ListInfo queryListInfo(Class<?> beanType, String sqlname, long offset,int pagesize,Object... fields) throws SQLException
	{
		return queryListInfoWithDBName(beanType, null,sqlname, offset,pagesize,fields);		 
	}
	public ListInfo queryListInfoWithTotalsize(Class<?> beanType, String sqlname, long offset,int pagesize,long totalsize,Object... fields) throws SQLException
	{
		return queryListInfoWithDBName2ndTotalsize(beanType, null,sqlname, offset,pagesize,totalsize,fields);		 
	}
	public ListInfo queryListInfoWithTotalsizesql(Class<?> beanType, String sqlname, long offset,int pagesize,String totalsizesqlname,Object... fields) throws SQLException
	{
		return queryListInfoWithDBName2ndTotalsizesql(beanType, null,sqlname, offset,pagesize,totalsizesqlname,fields);		 
	}
	
	
	public <T> T queryObject(Class<T> beanType, String sqlname, Object... fields) throws SQLException
	{
		return queryObjectWithDBName(beanType,null, sqlname, fields);
		
		 
	}
	
	public <T> List<T> queryListWithDBName(Class<T> beanType,String dbname, String sqlname, Object... fields) throws SQLException
	{		
		SQLInfo sql = getSqlInfo(dbname, sqlname);
		return SQLInfoExecutor.queryListWithDBName( beanType, dbname,  sql, fields);
	}
	
	
	public <T> T queryObjectWithDBName(Class<T> beanType,String dbname, String sqlname, Object... fields) throws SQLException
	{
		
		SQLInfo sql = getSqlInfo(dbname, sqlname);
		return SQLInfoExecutor.queryObjectWithDBName( beanType, dbname,  sql, fields);
		 
	}
	
	
	public <T> List<T> queryListByRowHandler(RowHandler rowhandler,Class<T> beanType, String sqlname, Object... fields) throws SQLException
	{
		
		return queryListWithDBNameByRowHandler(rowhandler,beanType,null, sqlname, fields); 
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
	public ListInfo queryListInfoWithDBNameByRowHandler(RowHandler rowhandler,Class<?> beanType,String dbname, String sqlname, long offset,int pagesize,Object... fields) throws SQLException
	{
		
		SQLInfo sql = getSqlInfo(dbname, sqlname);
		return SQLInfoExecutor.queryListInfoWithDBNameByRowHandler(rowhandler,beanType,dbname, sql, offset,pagesize,fields);  	  
	}
	public ListInfo queryListInfoWithDBName2ndTotalsizeByRowHandler(RowHandler rowhandler,Class<?> beanType,String dbname, String sqlname, long offset,int pagesize,long totalsize,Object... fields) throws SQLException
	{
		
		SQLInfo sql = getSqlInfo(dbname, sqlname);
		return SQLInfoExecutor.queryListInfoWithDBName2ndTotalsizeByRowHandler(rowhandler,beanType,dbname, sql, offset,pagesize,totalsize,fields);  	  
	}
	public ListInfo queryListInfoWithDBName2ndTotalsizesqlByRowHandler(RowHandler rowhandler,Class<?> beanType,String dbname, String sqlname, long offset,int pagesize,String totalsizesqlname,Object... fields) throws SQLException
	{
		
		SQLInfo sql = getSqlInfo(dbname, sqlname);
		SQLInfo totalsizesql = this.getSqlInfo(dbname, totalsizesqlname);
		return SQLInfoExecutor.queryListInfoWithDBName2ndTotalsizesqlByRowHandler(rowhandler,beanType,dbname, sql, offset,pagesize,totalsizesql,fields);  	  
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
	public ListInfo queryListInfoByRowHandler(RowHandler rowhandler,Class<?> beanType, String sqlname, long offset,int pagesize,Object... fields) throws SQLException
	{
		return queryListInfoWithDBNameByRowHandler( rowhandler,beanType, null,sqlname, offset,pagesize,fields);		 
	}
	
	public ListInfo queryListInfoWithTotalsizeByRowHandler(RowHandler rowhandler,Class<?> beanType, String sqlname, long offset,int pagesize,long totalsize,Object... fields) throws SQLException
	{
		return queryListInfoWithDBName2ndTotalsizeByRowHandler( rowhandler,beanType, null,sqlname, offset,pagesize,totalsize,fields);		 
	}
	public ListInfo queryListInfoWithTotalsizesqlByRowHandler(RowHandler rowhandler,Class<?> beanType, String sqlname, long offset,int pagesize,String totalsqlname,Object... fields) throws SQLException
	{
		return queryListInfoWithDBName2ndTotalsizesqlByRowHandler( rowhandler,beanType, null,sqlname, offset,pagesize,totalsqlname,fields);		 
	}
	
	public <T> T queryObjectByRowHandler(RowHandler rowhandler,Class<T> beanType, String sqlname, Object... fields) throws SQLException
	{
		return queryObjectWithDBNameByRowHandler(rowhandler,beanType,null, sqlname, fields);
		
		 
	}
	
	public <T> List<T> queryListWithDBNameByRowHandler(RowHandler rowhandler,Class<T> beanType,String dbname, String sqlname, Object... fields) throws SQLException
	{
		
		SQLInfo sql = getSqlInfo(dbname, sqlname);
		return SQLInfoExecutor.queryListWithDBNameByRowHandler(  rowhandler, beanType, dbname,  sql,  fields);
	}
	
	
	public <T> T queryObjectWithDBNameByRowHandler(RowHandler rowhandler,Class<T> beanType,String dbname, String sqlname, Object... fields) throws SQLException
	{
		
		SQLInfo sql = getSqlInfo(dbname, sqlname);
		return SQLInfoExecutor.queryObjectWithDBNameByRowHandler(  rowhandler, beanType, dbname,  sql,  fields);
		 
	}
	
	
	
	
	
	public void queryByNullRowHandler(NullRowHandler rowhandler, String sqlname, Object... fields) throws SQLException
	{
		
		 queryWithDBNameByNullRowHandler( rowhandler,null, sqlname, fields); 
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
	public ListInfo queryListInfoWithDBNameByNullRowHandler(NullRowHandler rowhandler,String dbname, String sqlname, long offset,int pagesize,Object... fields) throws SQLException
	{
		
		SQLInfo sql = getSqlInfo(dbname, sqlname);
		return SQLInfoExecutor.queryListInfoWithDBNameByNullRowHandler(  rowhandler,dbname, sql, offset,pagesize,fields);  
	}
	public ListInfo queryListInfoWithDBName2ndTotalsizeByNullRowHandler(NullRowHandler rowhandler,String dbname, String sqlname, long offset,int pagesize,long totalsize,Object... fields) throws SQLException
	{
		
		SQLInfo sql = getSqlInfo(dbname, sqlname);
		return SQLInfoExecutor.queryListInfoWithDBName2ndTotalsizeByNullRowHandler(  rowhandler,dbname, sql, offset,pagesize,totalsize,fields);  
	}
	public ListInfo queryListInfoWithDBName2ndTotalsizesqlByNullRowHandler(NullRowHandler rowhandler,String dbname, String sqlname, long offset,int pagesize,String totalsizesqlname,Object... fields) throws SQLException
	{
		
		SQLInfo sql = getSqlInfo(dbname, sqlname);
		SQLInfo totalsizesql = this.getSqlInfo(dbname, totalsizesqlname);
		return SQLInfoExecutor.queryListInfoWithDBName2ndTotalsizesqlByNullRowHandler(  rowhandler,dbname, sql, offset,pagesize,totalsizesql,fields);  
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
	public ListInfo queryListInfoByNullRowHandler(NullRowHandler rowhandler, String sqlname, long offset,int pagesize,Object... fields) throws SQLException
	{
		return queryListInfoWithDBNameByNullRowHandler( rowhandler, null,sqlname, offset,pagesize,fields);		 
	}
	
	public ListInfo queryListInfoWithTotalsizeByNullRowHandler(NullRowHandler rowhandler, String sqlname, long offset,int pagesize,long totalsize,Object... fields) throws SQLException
	{
		return queryListInfoWithDBName2ndTotalsizeByNullRowHandler( rowhandler, null,sqlname, offset,pagesize,totalsize,fields);		 
	}
	
	public ListInfo queryListInfoWithTotalsizesqlByNullRowHandler(NullRowHandler rowhandler, String sqlname, long offset,int pagesize,String totalsizesqlname,Object... fields) throws SQLException
	{
		return queryListInfoWithDBName2ndTotalsizesqlByNullRowHandler( rowhandler, null,sqlname, offset,pagesize,totalsizesqlname,fields);		 
	}
	
	
	
	public void queryWithDBNameByNullRowHandler(NullRowHandler rowhandler,String dbname, String sqlname, Object... fields) throws SQLException
	{
		
		SQLInfo sql = getSqlInfo(dbname, sqlname);
		SQLInfoExecutor.queryWithDBNameByNullRowHandler(  rowhandler, dbname,  sql,  fields);
	}
	
	
	public <T> List<T> queryListBean(Class<T> beanType, String sqlname, Object bean) throws SQLException
	{
		
		return queryListBeanWithDBName(beanType,null, sqlname, bean); 
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
	public ListInfo queryListInfoBeanWithDBName(Class<?> beanType,String dbname, String sqlname, long offset,int pagesize,long totalsize,Object bean) throws SQLException
	{
		
		SQLInfo sql = getSqlInfo(dbname, sqlname);
		return SQLInfoExecutor.queryListInfoBeanWithDBName(  beanType, dbname,  sql,  offset, pagesize, totalsize,bean); 
	}
	
	public ListInfo queryListInfoBeanWithDBName(Class<?> beanType,String dbname, String sqlname, long offset,int pagesize,String totalsizesqlname,Object bean) throws SQLException
	{
		
		SQLInfo sql = getSqlInfo(dbname, sqlname);
		SQLInfo totalsizesql = this.getSqlInfo(dbname, totalsizesqlname);
		return SQLInfoExecutor.queryListInfoBeanWithDBName(  beanType, dbname,  sql,  offset, pagesize, totalsizesql,bean); 
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
	public ListInfo queryListInfoBeanWithDBName(Class<?> beanType,String dbname, String sqlname, long offset,int pagesize,Object bean) throws SQLException
	{
		
		SQLInfo sql = getSqlInfo(dbname, sqlname);
		return SQLInfoExecutor.queryListInfoBeanWithDBName(  beanType, dbname,  sql,  offset, pagesize, -1L,bean); 
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
	public ListInfo queryListInfoBean(Class<?> beanType, String sqlname, long offset,int pagesize,long totalsize,Object bean) throws SQLException
	{
		return queryListInfoBeanWithDBName(beanType, null,sqlname, offset,pagesize,totalsize,bean);		 
	}
	
	public ListInfo queryListInfoBean(Class<?> beanType, String sqlname, long offset,int pagesize,String totalsizesqlname,Object bean) throws SQLException
	{
		return queryListInfoBeanWithDBName(beanType, null,sqlname, offset,pagesize,totalsizesqlname,bean);		 
	}
	
	public ListInfo queryListInfoBean(Class<?> beanType, String sqlname, long offset,int pagesize,Object bean) throws SQLException
	{
		return queryListInfoBeanWithDBName(beanType, null,sqlname, offset,pagesize,-1L,bean);		 
	}
	public String queryField( String sqlname, Object... fields) throws SQLException
	{
		return queryFieldWithDBName(null, sqlname, fields);
	}
	public  String queryFieldBean( String sqlname, Object bean) throws SQLException
	{
		return queryFieldBeanWithDBName(null, sqlname, bean);
	}
	
	public  String queryFieldBeanWithDBName(String dbname, String sqlname, Object bean) throws SQLException
	{
		
		SQLInfo sql = getSqlInfo(dbname, sqlname);
		return SQLInfoExecutor.queryFieldBeanWithDBName(    dbname,  sql,  bean); 
	}
	
	public  String queryFieldWithDBName(String dbname, String sqlname, Object... fields) throws SQLException
	{
		
		SQLInfo sql = getSqlInfo(dbname, sqlname);
		return SQLInfoExecutor.queryFieldWithDBName(    dbname,  sql,  fields); 
	}
	
	
	
	/**
	 * 
	 * @param <T>
	 * @param beanType
	 * @param sql
	 * @param bean
	 * @return
	 * @throws SQLException
	 */
	
	
	public <T> T queryTField( Class<T> type,String sql, Object... fields) throws SQLException
	{
		return queryTFieldWithDBName(null, type,sql, fields);
	}
	public  <T> T queryTFieldBean( Class<T> type,String sqlname, Object bean) throws SQLException
	{
		return queryTFieldBeanWithDBName(null, type,sqlname, bean);
	}
	
	public <T> T queryTFieldBeanWithDBName(String dbname, Class<T> type,String sqlname, Object bean) throws SQLException
	{
		
		SQLInfo sql = getSqlInfo(dbname, sqlname);
		return SQLInfoExecutor.queryTFieldBeanWithDBName(     dbname,  type, sql,  bean); 
	}
	
	public <T> T queryTFieldWithDBName(String dbname, Class<T> type,String sqlname, Object... fields) throws SQLException
	{
		
		SQLInfo sql = getSqlInfo(dbname, sqlname);
		return SQLInfoExecutor.queryTFieldWithDBName(     dbname,  type, sql,  fields); 
	}
	
	
	/**行处理器开始*/
	/**
	 * 
	 * @param <T>
	 * @param beanType
	 * @param sql
	 * @param bean
	 * @return
	 * @throws SQLException
	 */
	
	
	public <T> T queryTField( Class<T> type,FieldRowHandler<T> fieldRowHandler,String sqlname, Object... fields) throws SQLException
	{
		return queryTFieldWithDBName(null, type,fieldRowHandler,sqlname, fields);
	}
	public  <T> T queryTFieldBean( Class<T> type,FieldRowHandler<T> fieldRowHandler,String sqlname, Object bean) throws SQLException
	{
		return queryTFieldBeanWithDBName(null, type, fieldRowHandler,sqlname, bean);
	}
	
	public <T> T queryTFieldBeanWithDBName(String dbname, Class<T> type,FieldRowHandler<T> fieldRowHandler,String sqlname, Object bean) throws SQLException
	{
		
		SQLInfo sql = getSqlInfo(dbname, sqlname);
		return SQLInfoExecutor.queryTFieldBeanWithDBName(     dbname,  type,fieldRowHandler, sql,  bean); 
	}
	
	public <T> T queryTFieldWithDBName(String dbname, Class<T> type,FieldRowHandler<T> fieldRowHandler,String sqlname, Object... fields) throws SQLException
	{
		
		SQLInfo sql = getSqlInfo(dbname, sqlname);
		return SQLInfoExecutor.queryTFieldWithDBName(     dbname,  type,fieldRowHandler, sql,  fields); 
	}
	
	/**行处理器结束*/
	/**
	 * 
	 * @param <T>
	 * @param beanType
	 * @param sql
	 * @param bean
	 * @return
	 * @throws SQLException
	 */
	
	public <T> T queryObjectBean(Class<T> beanType, String sqlname, Object bean) throws SQLException
	{
		return queryObjectBeanWithDBName(beanType,null, sqlname, bean);
		
		 
	}
	
	public <T> List<T> queryListBeanWithDBName(Class<T> beanType,String dbname, String sqlname, Object bean) throws SQLException
	{
		
		SQLInfo sql = getSqlInfo(dbname, sqlname);
		return SQLInfoExecutor.queryListBeanWithDBName(   beanType,dbname, sql, bean); 
	}
	
	public <T> T queryObjectBeanWithDBName(Class<T> beanType,String dbname, String sqlname, Object bean) throws SQLException
	{
		
		SQLInfo sql = getSqlInfo(dbname, sqlname);
		return SQLInfoExecutor.queryObjectBeanWithDBName(   beanType,dbname, sql, bean); 
		 
	}
	
	
	public <T> List<T> queryListBeanByRowHandler(RowHandler rowhandler,Class<T> beanType, String sqlname, Object bean) throws SQLException
	{
		
		return queryListBeanWithDBNameByRowHandler(rowhandler,beanType,null, sqlname, bean); 
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
	public ListInfo queryListInfoBeanWithDBNameByRowHandler(RowHandler rowhandler,Class<?> beanType,String dbname, String sqlname, long offset,int pagesize,long totalsize,Object  bean) throws SQLException
	{
		
		SQLInfo sql = getSqlInfo(dbname, sqlname);
		return SQLInfoExecutor.queryListInfoBeanWithDBNameByRowHandler(  rowhandler,beanType,dbname, sql, offset,pagesize,totalsize,bean);  
	}
	public ListInfo queryListInfoBeanWithDBNameByRowHandler(RowHandler rowhandler,Class<?> beanType,String dbname, String sqlname, long offset,int pagesize,String totalsizesqlname,Object  bean) throws SQLException
	{
		
		SQLInfo sql = getSqlInfo(dbname, sqlname);
		SQLInfo totalsizesql = this.getSqlInfo(dbname, totalsizesqlname);
		return SQLInfoExecutor.queryListInfoBeanWithDBNameByRowHandler(  rowhandler,beanType,dbname, sql, offset,pagesize,totalsizesql ,bean);  
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
	public ListInfo queryListInfoBeanWithDBNameByRowHandler(RowHandler rowhandler,Class<?> beanType,String dbname, String sqlname, long offset,int pagesize,Object  bean) throws SQLException
	{
		
		SQLInfo sql = getSqlInfo(dbname, sqlname);
		return SQLInfoExecutor.queryListInfoBeanWithDBNameByRowHandler(  rowhandler,beanType,dbname, sql, offset,pagesize,-1L,bean);  
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
	public ListInfo queryListInfoBeanByRowHandler(RowHandler rowhandler,Class<?> beanType, String sqlname, long offset,int pagesize,String totalsizesqlname,Object bean) throws SQLException
	{
		return queryListInfoBeanWithDBNameByRowHandler( rowhandler,beanType, null,sqlname, offset,pagesize,totalsizesqlname,bean);		 
	}
	
	public ListInfo queryListInfoBeanByRowHandler(RowHandler rowhandler,Class<?> beanType, String sqlname, long offset,int pagesize,long totalsize,Object bean) throws SQLException
	{
		return queryListInfoBeanWithDBNameByRowHandler( rowhandler,beanType, null,sqlname, offset,pagesize,totalsize,bean);		 
	}
	
	
	
	public ListInfo queryListInfoBeanByRowHandler(RowHandler rowhandler,Class<?> beanType, String sqlname, long offset,int pagesize,Object bean) throws SQLException
	{
		return queryListInfoBeanWithDBNameByRowHandler( rowhandler,beanType, null,sqlname, offset,pagesize,-1L,bean);		 
	}
	
	
	public <T> T queryObjectBeanByRowHandler(RowHandler rowhandler,Class<T> beanType, String sqlname, Object bean) throws SQLException
	{
		return queryObjectBeanWithDBNameByRowHandler(rowhandler,beanType,null, sqlname, bean);
		
		 
	}
	
	public <T> List<T> queryListBeanWithDBNameByRowHandler(RowHandler rowhandler,Class<T> beanType,String dbname, String sqlname, Object bean) throws SQLException
	{
		
		SQLInfo sql = getSqlInfo(dbname, sqlname);
		return SQLInfoExecutor.queryListBeanWithDBNameByRowHandler(  rowhandler, beanType, dbname,  sql,  bean);  
	}
	
	
	public <T> T queryObjectBeanWithDBNameByRowHandler(RowHandler rowhandler,Class<T> beanType,String dbname, String sqlname, Object bean) throws SQLException
	{
		
		SQLInfo sql = getSqlInfo(dbname, sqlname);
		return SQLInfoExecutor.queryObjectBeanWithDBNameByRowHandler(  rowhandler, beanType, dbname,  sql,  bean);  
		 
	}
	
	
	
	
	
	public void queryBeanByNullRowHandler(NullRowHandler rowhandler, String sqlname, Object bean) throws SQLException
	{
		
		 queryBeanWithDBNameByNullRowHandler( rowhandler,null, sqlname, bean); 
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
	public ListInfo queryListInfoBeanWithDBNameByNullRowHandler(NullRowHandler rowhandler,String dbname, String sqlname, long offset,int pagesize,long totalsize,Object bean) throws SQLException
	{
		
		SQLInfo sql = getSqlInfo(dbname, sqlname);
		return SQLInfoExecutor.queryListInfoBeanWithDBNameByNullRowHandler(  rowhandler, dbname, sql, offset,pagesize,totalsize,bean);  	 
	}
	public ListInfo queryListInfoBeanWithDBNameByNullRowHandler(NullRowHandler rowhandler,String dbname, String sqlname, long offset,int pagesize,String totalsizesqlname,Object bean) throws SQLException
	{
		
		SQLInfo sql = getSqlInfo(dbname, sqlname);
		SQLInfo totalsizesql = this.getSqlInfo(dbname, totalsizesqlname);
		return SQLInfoExecutor.queryListInfoBeanWithDBNameByNullRowHandler(  rowhandler, dbname, sql, offset,pagesize,totalsizesql,bean);  	 
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
	public ListInfo queryListInfoBeanWithDBNameByNullRowHandler(NullRowHandler rowhandler,String dbname, String sqlname, long offset,int pagesize,Object bean) throws SQLException
	{
		
		SQLInfo sql = getSqlInfo(dbname, sqlname);
		return SQLInfoExecutor.queryListInfoBeanWithDBNameByNullRowHandler(  rowhandler, dbname, sql, offset,pagesize,-1L,bean);  	 
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
	public ListInfo queryListInfoBeanByNullRowHandler(NullRowHandler rowhandler, String sqlname, long offset,int pagesize,long totalsize,Object bean) throws SQLException
	{
		return queryListInfoBeanWithDBNameByNullRowHandler( rowhandler, null,sqlname, offset,pagesize,totalsize,bean);		 
	}
	
	public ListInfo queryListInfoBeanByNullRowHandler(NullRowHandler rowhandler, String sqlname, long offset,int pagesize,String totalsizesqlname,Object bean) throws SQLException
	{
		return queryListInfoBeanWithDBNameByNullRowHandler( rowhandler, null,sqlname, offset,pagesize,totalsizesqlname,bean);		 
	}
	public ListInfo queryListInfoBeanByNullRowHandler(NullRowHandler rowhandler, String sqlname, long offset,int pagesize,Object bean) throws SQLException
	{
		return queryListInfoBeanWithDBNameByNullRowHandler( rowhandler, null,sqlname, offset,pagesize,-1L,bean);		 
	}
	
	
	public void queryBeanWithDBNameByNullRowHandler(NullRowHandler rowhandler,String dbname, String sqlname, Object bean) throws SQLException
	{
		
		SQLInfo sql = getSqlInfo(dbname, sqlname);
		SQLInfoExecutor.queryBeanWithDBNameByNullRowHandler(  rowhandler,  dbname,  sql,  bean);  	 
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
	public ListInfo moreListInfoWithDBNameByRowHandler(RowHandler rowhandler,Class<?> beanType,String dbname, String sqlname, long offset,int pagesize,Object... fields) throws SQLException
	{
		
		SQLInfo sql = getSqlInfo(dbname, sqlname);
		return SQLInfoExecutor.moreListInfoWithDBNameByRowHandler(rowhandler,beanType,dbname, sql, offset,pagesize,fields);  	  
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
	public ListInfo moreListInfoWithDBNameByNullRowHandler(NullRowHandler rowhandler,String dbname, String sqlname, long offset,int pagesize,Object... fields) throws SQLException
	{
		
		SQLInfo sql = getSqlInfo(dbname, sqlname);
		return SQLInfoExecutor.moreListInfoWithDBNameByNullRowHandler(  rowhandler,dbname, sql, offset,pagesize,fields);  
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
	public ListInfo moreListInfoWithDBName(Class<?> beanType,String dbname, String sqlname, long offset,int pagesize,Object... fields) throws SQLException
	{
		
		SQLInfo sql = getSqlInfo(dbname, sqlname);
		return SQLInfoExecutor.moreListInfoWithDBName(beanType,dbname, sql, offset,pagesize,fields);  	 
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
	public ListInfo moreListInfoByRowHandler(RowHandler rowhandler,Class<?> beanType, String sqlname, long offset,int pagesize,Object... fields) throws SQLException
	{
		return moreListInfoWithDBNameByRowHandler( rowhandler,beanType, null,sqlname, offset,pagesize,fields);		 
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
	public ListInfo moreListInfoByNullRowHandler(NullRowHandler rowhandler, String sqlname, long offset,int pagesize,Object... fields) throws SQLException
	{
		return moreListInfoWithDBNameByNullRowHandler( rowhandler, null,sqlname, offset,pagesize,fields);		 
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
	public ListInfo moreListInfoBeanWithDBNameByRowHandler(RowHandler rowhandler,Class<?> beanType,String dbname, String sqlname, long offset,int pagesize,Object  bean) throws SQLException
	{
		
		SQLInfo sql = getSqlInfo(dbname, sqlname);
		return SQLInfoExecutor.moreListInfoBeanWithDBNameByRowHandler(  rowhandler,beanType,dbname, sql, offset,pagesize,bean);  
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
	public ListInfo moreListInfoBeanWithDBNameByNullRowHandler(NullRowHandler rowhandler,String dbname, String sqlname, long offset,int pagesize,Object bean) throws SQLException
	{
		
		SQLInfo sql = getSqlInfo(dbname, sqlname);
		return SQLInfoExecutor.moreListInfoBeanWithDBNameByNullRowHandler(  rowhandler, dbname, sql, offset,pagesize,bean);  	 
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
	public ListInfo moreListInfoBeanWithDBName(Class<?> beanType,String dbname, String sqlname, long offset,int pagesize,Object bean) throws SQLException
	{
		
		SQLInfo sql = getSqlInfo(dbname, sqlname);
		return SQLInfoExecutor.moreListInfoBeanWithDBName(  beanType, dbname,  sql,  offset, pagesize, bean); 
	}
	
		public ListInfo moreListInfoBeanByRowHandler(RowHandler rowhandler,Class<?> beanType, String sqlname, long offset,int pagesize,Object bean) throws SQLException
	{
			SQLInfo sql = getSqlInfo(null, sqlname);
			return SQLInfoExecutor.moreListInfoBeanWithDBNameByRowHandler(  rowhandler,beanType,(String)null, sql, offset,pagesize,bean);  		 
	}
	
		public ListInfo moreListInfoBeanByNullRowHandler(NullRowHandler rowhandler, String sqlname, long offset,int pagesize,Object bean) throws SQLException
	{
			SQLInfo sql = getSqlInfo(null, sqlname);
			return SQLInfoExecutor.moreListInfoBeanWithDBNameByNullRowHandler(  rowhandler, (String)null, sql, offset,pagesize,bean);  	
	}
	
		public ListInfo moreListInfoBean(Class<?> beanType, String sqlname, long offset,int pagesize,Object bean) throws SQLException
	{
			SQLInfo sql = getSqlInfo(null, sqlname);
			return SQLInfoExecutor.moreListInfoBeanWithDBName(  beanType, (String)null,  sql,  offset, pagesize, bean); 
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
	public ListInfo moreListInfo(Class<?> beanType, String sqlname, long offset,int pagesize,Object... fields) throws SQLException
	{
		return moreListInfoWithDBName(beanType, null,sqlname, offset,pagesize,fields);		 
	}
	
	

}
