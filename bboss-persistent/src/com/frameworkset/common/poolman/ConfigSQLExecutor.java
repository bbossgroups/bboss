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
import java.util.ArrayList;
import java.util.List;

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
	
	
   
	public String getSql(String sqlname) throws SQLException
	{
		return getSql(null,sqlname);
	}
	public String getSql(String dbname,String sqlname) throws SQLException
	{
		String sql = context.getSQL(dbname, sqlname);
		if(sql == null)
			throw new NestedSQLException("名称为[" + sqlname + "]的sql语句不存在，请检查配置文件[" + context.getSQLFile() + "]配置是否正确.");
		return sql;
	}
	public void insertBeans(String dbname, String sqlname, List beans) throws SQLException {
		
		if(beans == null || beans.size() == 0)
			return ;
		String sql = getSql(dbname,sqlname);
		SQLExecutor.insertBeans( dbname,  sql,  beans);
	}
	
	public Object update( String sqlname, Object... fields) throws SQLException {
		String sql = getSql(null,sqlname);
		return SQLExecutor.update(sql, fields);
	}
	



	public Object delete(String sqlname, Object... fields) throws SQLException {
		String sql = getSql(null,sqlname);
		return SQLExecutor.delete(sql, fields);
		
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
		String sql = getSql(null,sqlname);
		SQLExecutor.deleteByKeys(sql, fields);
//		executeBatch(null, sql,PreparedDBUtil.DELETE, fields);
		
	}
	public  void deleteByKeysWithDBName(String dbname,String sqlname, int... fields) throws SQLException {
		String sql = getSql(dbname,sqlname);
		SQLExecutor.deleteByKeysWithDBName(dbname,sql, fields);
		
	}
	
	public void deleteByLongKeys(String sqlname, long... fields) throws SQLException {
		String sql = getSql(null,sqlname);
		SQLExecutor.deleteByLongKeys(sql, fields);
//		executeBatch(null, sql,PreparedDBUtil.DELETE, fields);
		
	}
	public void deleteByLongKeysWithDBName(String dbname,String sqlname, long... fields) throws SQLException {
		String sql = getSql(dbname,sqlname);
		SQLExecutor.deleteByLongKeysWithDBName(dbname,sql, fields);
//		executeBatch(dbname, sql,PreparedDBUtil.DELETE, fields);
		
	}
	
	public void deleteByKeys(String sqlname, String... fields) throws SQLException {
		String sql = getSql(null,sqlname);
		SQLExecutor.deleteByKeys(sql, fields);
//		executeBatch(null, sql,PreparedDBUtil.DELETE, fields);
		
	}
	public void deleteByKeysWithDBName(String dbname,String sqlname, String... fields) throws SQLException {
		String sql = getSql(dbname,sqlname);
		SQLExecutor.deleteByKeysWithDBName(dbname,sql, fields);
//		executeBatch(dbname, sql,PreparedDBUtil.DELETE, fields);
		
	}
	
	public void deleteByShortKeys(String sqlname, short... fields) throws SQLException {
		String sql = getSql(null,sqlname);
		SQLExecutor.deleteByShortKeys(sql, fields);
//		executeBatch(null, sql,PreparedDBUtil.DELETE, fields);
		
	}
	public void deleteByShortKeysWithDBName(String dbname,String sqlname, short... fields) throws SQLException {
		String sql = getSql(dbname,sqlname);
		SQLExecutor.deleteByShortKeysWithDBName(dbname,sql, fields);
//		executeBatch(dbname, sql,PreparedDBUtil.DELETE, fields);
	}



	public Object insert(String sqlname, Object... fields) throws SQLException {
		String sql = getSql(null,sqlname);
		return SQLExecutor.insert(sql, fields);
//		return execute(null, sql,PreparedDBUtil.INSERT, fields);
	}
	
	public  Object updateWithDBName(String dbname, String sqlname, Object... fields) throws SQLException {
		String sql = getSql(dbname,sqlname);
		return SQLExecutor.updateWithDBName(dbname,sql, fields);
//		return execute(dbname, sql,PreparedDBUtil.UPDATE, fields);
	}
	
	public  Object deleteWithDBName(String dbname, String sqlname, Object... fields) throws SQLException {
		String sql = getSql(dbname,sqlname);
		return SQLExecutor.deleteWithDBName(dbname,sql, fields);
//		return execute(dbname, sql,PreparedDBUtil.DELETE, fields);
		
	}



	public  Object insertWithDBName(String dbname, String sqlname, Object... fields) throws SQLException {
		String sql = getSql(dbname,sqlname);
		return SQLExecutor.insertWithDBName(dbname,sql, fields);
//		return execute(dbname, sql,PreparedDBUtil.INSERT, fields);
	}

	public void updateBeans(String dbname, String sqlname, List beans) throws SQLException {
		if(beans == null || beans.size() == 0)
			return ;
		String sql = getSql(dbname,sqlname);
		SQLExecutor.updateBeans(dbname,sql, beans);
//		execute( dbname,  sql,  beans,PreparedDBUtil.UPDATE);
	}



	public void deleteBeans(String dbname, String sqlname, List beans) throws SQLException {
		if(beans == null || beans.size() == 0)
			return ;
		String sql = getSql(dbname,sqlname);
		SQLExecutor.deleteBeans(dbname,sql, beans);
//		execute( dbname,  sql,  beans,PreparedDBUtil.DELETE);
		
	}


	public Object insertBean(String dbname, String sqlname, Object bean) throws SQLException {
		return insertBean(dbname, sqlname, bean,false);
	}
	public Object insertBean(String dbname, String sqlname, Object bean,boolean getCUDResult) throws SQLException {
		if(bean == null)
			return null;
//		List datas = new ArrayList();
//		datas.add(bean);
//		insertBeans( dbname,  sql,  datas);
		String sql = getSql(dbname,sqlname);
		return SQLExecutor.insertBean(dbname,sql, bean,getCUDResult);
	}



	public  Object updateBean(String dbname, String sqlname, Object bean) throws SQLException {
		if(bean == null )
			return null;
//		List datas = new ArrayList();
//		datas.add(bean);
//		updateBeans( dbname,  sql,  datas);
		String sql = getSql(dbname,sqlname);
		return SQLExecutor.updateBean(dbname,sql, bean);
	}

	

	public Object deleteBean(String dbname, String sqlname, Object bean) throws SQLException {
		
		if(bean == null)
			return null;
//		List datas = new ArrayList();
//		datas.add(bean);
//		deleteBeans( dbname,  sql,  datas);
		String sql = getSql(dbname,sqlname);
		return SQLExecutor.deleteBean(dbname,sql, bean);
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



	public Object insertBean( String sqlname, Object bean) throws SQLException {
		if(bean == null)
			return null;
//		List datas = new ArrayList();
//		datas.add(bean);
//		insertBeans( null,  sqlname,  datas);
		return insertBean( null,sqlname, bean);
	}
	
	public Object insertBean( String sqlname, Object bean,boolean getCUDResult) throws SQLException {
		if(bean == null)
			return null;
//		List datas = new ArrayList();
//		datas.add(bean);
//		insertBeans( null,  sqlname,  datas);
		return insertBean( null,sqlname, bean, getCUDResult);
	}



	public Object updateBean( String sqlname, Object bean) throws SQLException {
		if(bean == null )
			return null;
//		List datas = new ArrayList();
//		datas.add(bean);
//		updateBeans( null,  sqlname,  datas);
		return updateBean( null,sqlname, bean);
	}

	

	public Object deleteBean(String sqlname, Object bean) throws SQLException {
		
		if(bean == null)
			return null;
//		List datas = new ArrayList();
//		datas.add(bean);
//		deleteBeans( null,  sqlname,  datas);
		return deleteBean(null,sqlname, bean);
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
		
		String sql = this.getSql(dbname, sqlname);
		return SQLExecutor.queryListInfoWithDBName(beanType,dbname, sql, offset,pagesize,fields);  	 
	}
	
	public ListInfo queryListInfoWithDBName2ndTotalsize(Class<?> beanType,String dbname, String sqlname, long offset,int pagesize,long totalsize,Object... fields) throws SQLException
	{
		
		String sql = this.getSql(dbname, sqlname);
		return SQLExecutor.queryListInfoWithDBName2ndTotalsize(beanType,dbname, sql, offset,pagesize,totalsize,fields);  	 
	}
	public ListInfo queryListInfoWithDBName2ndTotalsizesql(Class<?> beanType,String dbname, String sqlname, long offset,int pagesize,String totalsizesqlname,Object... fields) throws SQLException
	{
		
		String sql = this.getSql(dbname, sqlname);
		String totalsizesql = this.getSql(dbname, totalsizesqlname);
		return SQLExecutor.queryListInfoWithDBName2ndTotalsizesql(beanType,dbname, sql, offset,pagesize,totalsizesql,fields);  	 
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
	public ListInfo queryListInfo(Class<?> beanType, String sql, long offset,int pagesize,Object... fields) throws SQLException
	{
		return queryListInfoWithDBName(beanType, null,sql, offset,pagesize,fields);		 
	}
	public ListInfo queryListInfoWithTotalsize(Class<?> beanType, String sql, long offset,int pagesize,long totalsize,Object... fields) throws SQLException
	{
		return queryListInfoWithDBName2ndTotalsize(beanType, null,sql, offset,pagesize,totalsize,fields);		 
	}
	public ListInfo queryListInfoWithTotalsizesql(Class<?> beanType, String sql, long offset,int pagesize,String totalsizesqlname,Object... fields) throws SQLException
	{
		return queryListInfoWithDBName2ndTotalsizesql(beanType, null,sql, offset,pagesize,totalsizesqlname,fields);		 
	}
	
	
	public <T> T queryObject(Class<T> beanType, String sql, Object... fields) throws SQLException
	{
		return queryObjectWithDBName(beanType,null, sql, fields);
		
		 
	}
	
	public <T> List<T> queryListWithDBName(Class<T> beanType,String dbname, String sqlname, Object... fields) throws SQLException
	{		
		String sql = this.getSql(dbname, sqlname);
		return SQLExecutor.queryListWithDBName( beanType, dbname,  sql, fields);
	}
	
	
	public <T> T queryObjectWithDBName(Class<T> beanType,String dbname, String sqlname, Object... fields) throws SQLException
	{
		
		String sql = this.getSql(dbname, sqlname);
		return SQLExecutor.queryObjectWithDBName( beanType, dbname,  sql, fields);
		 
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
		
		String sql = this.getSql(dbname, sqlname);
		return SQLExecutor.queryListInfoWithDBNameByRowHandler(rowhandler,beanType,dbname, sql, offset,pagesize,fields);  	  
	}
	public ListInfo queryListInfoWithDBName2ndTotalsizeByRowHandler(RowHandler rowhandler,Class<?> beanType,String dbname, String sqlname, long offset,int pagesize,long totalsize,Object... fields) throws SQLException
	{
		
		String sql = this.getSql(dbname, sqlname);
		return SQLExecutor.queryListInfoWithDBName2ndTotalsizeByRowHandler(rowhandler,beanType,dbname, sql, offset,pagesize,totalsize,fields);  	  
	}
	public ListInfo queryListInfoWithDBName2ndTotalsizesqlByRowHandler(RowHandler rowhandler,Class<?> beanType,String dbname, String sqlname, long offset,int pagesize,String totalsizesqlname,Object... fields) throws SQLException
	{
		
		String sql = this.getSql(dbname, sqlname);
		String totalsizesql = this.getSql(dbname, totalsizesqlname);
		return SQLExecutor.queryListInfoWithDBName2ndTotalsizesqlByRowHandler(rowhandler,beanType,dbname, sql, offset,pagesize,totalsizesql,fields);  	  
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
	public ListInfo queryListInfoByRowHandler(RowHandler rowhandler,Class<?> beanType, String sql, long offset,int pagesize,Object... fields) throws SQLException
	{
		return queryListInfoWithDBNameByRowHandler( rowhandler,beanType, null,sql, offset,pagesize,fields);		 
	}
	
	public ListInfo queryListInfoWithTotalsizeByRowHandler(RowHandler rowhandler,Class<?> beanType, String sql, long offset,int pagesize,long totalsize,Object... fields) throws SQLException
	{
		return queryListInfoWithDBName2ndTotalsizeByRowHandler( rowhandler,beanType, null,sql, offset,pagesize,totalsize,fields);		 
	}
	public ListInfo queryListInfoWithTotalsizesqlByRowHandler(RowHandler rowhandler,Class<?> beanType, String sql, long offset,int pagesize,String totalsqlname,Object... fields) throws SQLException
	{
		return queryListInfoWithDBName2ndTotalsizesqlByRowHandler( rowhandler,beanType, null,sql, offset,pagesize,totalsqlname,fields);		 
	}
	
	public <T> T queryObjectByRowHandler(RowHandler rowhandler,Class<T> beanType, String sql, Object... fields) throws SQLException
	{
		return queryObjectWithDBNameByRowHandler(rowhandler,beanType,null, sql, fields);
		
		 
	}
	
	public <T> List<T> queryListWithDBNameByRowHandler(RowHandler rowhandler,Class<T> beanType,String dbname, String sqlname, Object... fields) throws SQLException
	{
		
		String sql = this.getSql(dbname, sqlname);
		return SQLExecutor.queryListWithDBNameByRowHandler(  rowhandler, beanType, dbname,  sql,  fields);
	}
	
	
	public <T> T queryObjectWithDBNameByRowHandler(RowHandler rowhandler,Class<T> beanType,String dbname, String sqlname, Object... fields) throws SQLException
	{
		
		String sql = this.getSql(dbname, sqlname);
		return SQLExecutor.queryObjectWithDBNameByRowHandler(  rowhandler, beanType, dbname,  sql,  fields);
		 
	}
	
	
	
	
	
	public void queryByNullRowHandler(NullRowHandler rowhandler, String sql, Object... fields) throws SQLException
	{
		
		 queryWithDBNameByNullRowHandler( rowhandler,null, sql, fields); 
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
		
		String sql = this.getSql(dbname, sqlname);
		return SQLExecutor.queryListInfoWithDBNameByNullRowHandler(  rowhandler,dbname, sql, offset,pagesize,fields);  
	}
	public ListInfo queryListInfoWithDBName2ndTotalsizeByNullRowHandler(NullRowHandler rowhandler,String dbname, String sqlname, long offset,int pagesize,long totalsize,Object... fields) throws SQLException
	{
		
		String sql = this.getSql(dbname, sqlname);
		return SQLExecutor.queryListInfoWithDBName2ndTotalsizeByNullRowHandler(  rowhandler,dbname, sql, offset,pagesize,totalsize,fields);  
	}
	public ListInfo queryListInfoWithDBName2ndTotalsizesqlByNullRowHandler(NullRowHandler rowhandler,String dbname, String sqlname, long offset,int pagesize,String totalsizesqlname,Object... fields) throws SQLException
	{
		
		String sql = this.getSql(dbname, sqlname);
		String totalsizesql = this.getSql(dbname, totalsizesqlname);
		return SQLExecutor.queryListInfoWithDBName2ndTotalsizesqlByNullRowHandler(  rowhandler,dbname, sql, offset,pagesize,totalsizesql,fields);  
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
	public ListInfo queryListInfoByNullRowHandler(NullRowHandler rowhandler, String sql, long offset,int pagesize,Object... fields) throws SQLException
	{
		return queryListInfoWithDBNameByNullRowHandler( rowhandler, null,sql, offset,pagesize,fields);		 
	}
	
	public ListInfo queryListInfoWithTotalsizeByNullRowHandler(NullRowHandler rowhandler, String sql, long offset,int pagesize,long totalsize,Object... fields) throws SQLException
	{
		return queryListInfoWithDBName2ndTotalsizeByNullRowHandler( rowhandler, null,sql, offset,pagesize,totalsize,fields);		 
	}
	
	public ListInfo queryListInfoWithTotalsizesqlByNullRowHandler(NullRowHandler rowhandler, String sql, long offset,int pagesize,String totalsizesqlname,Object... fields) throws SQLException
	{
		return queryListInfoWithDBName2ndTotalsizesqlByNullRowHandler( rowhandler, null,sql, offset,pagesize,totalsizesqlname,fields);		 
	}
	
	
	
	public void queryWithDBNameByNullRowHandler(NullRowHandler rowhandler,String dbname, String sqlname, Object... fields) throws SQLException
	{
		
		String sql = this.getSql(dbname, sqlname);
		SQLExecutor.queryWithDBNameByNullRowHandler(  rowhandler, dbname,  sql,  fields);
	}
	
	
	public <T> List<T> queryListBean(Class<T> beanType, String sql, Object bean) throws SQLException
	{
		
		return queryListBeanWithDBName(beanType,null, sql, bean); 
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
		
		String sql = this.getSql(dbname, sqlname);
		return SQLExecutor.queryListInfoBeanWithDBName(  beanType, dbname,  sql,  offset, pagesize, totalsize,bean); 
	}
	
	public ListInfo queryListInfoBeanWithDBName(Class<?> beanType,String dbname, String sqlname, long offset,int pagesize,String totalsizesqlname,Object bean) throws SQLException
	{
		
		String sql = this.getSql(dbname, sqlname);
		String totalsizesql = this.getSql(dbname, totalsizesqlname);
		return SQLExecutor.queryListInfoBeanWithDBName(  beanType, dbname,  sql,  offset, pagesize, totalsizesql,bean); 
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
		
		String sql = this.getSql(dbname, sqlname);
		return SQLExecutor.queryListInfoBeanWithDBName(  beanType, dbname,  sql,  offset, pagesize, -1L,bean); 
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
	public ListInfo queryListInfoBean(Class<?> beanType, String sql, long offset,int pagesize,long totalsize,Object bean) throws SQLException
	{
		return queryListInfoBeanWithDBName(beanType, null,sql, offset,pagesize,totalsize,bean);		 
	}
	
	public ListInfo queryListInfoBean(Class<?> beanType, String sql, long offset,int pagesize,String totalsizesqlname,Object bean) throws SQLException
	{
		return queryListInfoBeanWithDBName(beanType, null,sql, offset,pagesize,totalsizesqlname,bean);		 
	}
	
	public ListInfo queryListInfoBean(Class<?> beanType, String sql, long offset,int pagesize,Object bean) throws SQLException
	{
		return queryListInfoBeanWithDBName(beanType, null,sql, offset,pagesize,-1L,bean);		 
	}
	public String queryField( String sql, Object... fields) throws SQLException
	{
		return queryFieldWithDBName(null, sql, fields);
	}
	public  String queryFieldBean( String sql, Object bean) throws SQLException
	{
		return queryFieldBeanWithDBName(null, sql, bean);
	}
	
	public  String queryFieldBeanWithDBName(String dbname, String sqlname, Object bean) throws SQLException
	{
		
		String sql = this.getSql(dbname, sqlname);
		return SQLExecutor.queryFieldBeanWithDBName(    dbname,  sql,  bean); 
	}
	
	public  String queryFieldWithDBName(String dbname, String sqlname, Object... fields) throws SQLException
	{
		
		String sql = this.getSql(dbname, sqlname);
		return SQLExecutor.queryFieldWithDBName(    dbname,  sql,  fields); 
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
	public  <T> T queryTFieldBean( Class<T> type,String sql, Object bean) throws SQLException
	{
		return queryTFieldBeanWithDBName(null, type,sql, bean);
	}
	
	public <T> T queryTFieldBeanWithDBName(String dbname, Class<T> type,String sqlname, Object bean) throws SQLException
	{
		
		String sql = this.getSql(dbname, sqlname);
		return SQLExecutor.queryTFieldBeanWithDBName(     dbname,  type, sql,  bean); 
	}
	
	public <T> T queryTFieldWithDBName(String dbname, Class<T> type,String sqlname, Object... fields) throws SQLException
	{
		
		String sql = this.getSql(dbname, sqlname);
		return SQLExecutor.queryTFieldWithDBName(     dbname,  type, sql,  fields); 
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
	
	
	public <T> T queryTField( Class<T> type,FieldRowHandler<T> fieldRowHandler,String sql, Object... fields) throws SQLException
	{
		return queryTFieldWithDBName(null, type,fieldRowHandler,sql, fields);
	}
	public  <T> T queryTFieldBean( Class<T> type,FieldRowHandler<T> fieldRowHandler,String sql, Object bean) throws SQLException
	{
		return queryTFieldBeanWithDBName(null, type, fieldRowHandler,sql, bean);
	}
	
	public <T> T queryTFieldBeanWithDBName(String dbname, Class<T> type,FieldRowHandler<T> fieldRowHandler,String sqlname, Object bean) throws SQLException
	{
		
		String sql = this.getSql(dbname, sqlname);
		return SQLExecutor.queryTFieldBeanWithDBName(     dbname,  type,fieldRowHandler, sql,  bean); 
	}
	
	public <T> T queryTFieldWithDBName(String dbname, Class<T> type,FieldRowHandler<T> fieldRowHandler,String sqlname, Object... fields) throws SQLException
	{
		
		String sql = this.getSql(dbname, sqlname);
		return SQLExecutor.queryTFieldWithDBName(     dbname,  type,fieldRowHandler, sql,  fields); 
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
	
	public <T> T queryObjectBean(Class<T> beanType, String sql, Object bean) throws SQLException
	{
		return queryObjectBeanWithDBName(beanType,null, sql, bean);
		
		 
	}
	
	public <T> List<T> queryListBeanWithDBName(Class<T> beanType,String dbname, String sqlname, Object bean) throws SQLException
	{
		
		String sql = this.getSql(dbname, sqlname);
		return SQLExecutor.queryListBeanWithDBName(   beanType,dbname, sql, bean); 
	}
	
	public <T> T queryObjectBeanWithDBName(Class<T> beanType,String dbname, String sqlname, Object bean) throws SQLException
	{
		
		String sql = this.getSql(dbname, sqlname);
		return SQLExecutor.queryObjectBeanWithDBName(   beanType,dbname, sql, bean); 
		 
	}
	
	
	public <T> List<T> queryListBeanByRowHandler(RowHandler rowhandler,Class<T> beanType, String sql, Object bean) throws SQLException
	{
		
		return queryListBeanWithDBNameByRowHandler(rowhandler,beanType,null, sql, bean); 
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
		
		String sql = this.getSql(dbname, sqlname);
		return SQLExecutor.queryListInfoBeanWithDBNameByRowHandler(  rowhandler,beanType,dbname, sql, offset,pagesize,totalsize,bean);  
	}
	public ListInfo queryListInfoBeanWithDBNameByRowHandler(RowHandler rowhandler,Class<?> beanType,String dbname, String sqlname, long offset,int pagesize,String totalsizesqlname,Object  bean) throws SQLException
	{
		
		String sql = this.getSql(dbname, sqlname);
		String totalsizesql = this.getSql(dbname, totalsizesqlname);
		return SQLExecutor.queryListInfoBeanWithDBNameByRowHandler(  rowhandler,beanType,dbname, sql, offset,pagesize,totalsizesql ,bean);  
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
		
		String sql = this.getSql(dbname, sqlname);
		return SQLExecutor.queryListInfoBeanWithDBNameByRowHandler(  rowhandler,beanType,dbname, sql, offset,pagesize,-1L,bean);  
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
	public ListInfo queryListInfoBeanByRowHandler(RowHandler rowhandler,Class<?> beanType, String sql, long offset,int pagesize,String totalsizesqlname,Object bean) throws SQLException
	{
		return queryListInfoBeanWithDBNameByRowHandler( rowhandler,beanType, null,sql, offset,pagesize,totalsizesqlname,bean);		 
	}
	
	public ListInfo queryListInfoBeanByRowHandler(RowHandler rowhandler,Class<?> beanType, String sql, long offset,int pagesize,long totalsize,Object bean) throws SQLException
	{
		return queryListInfoBeanWithDBNameByRowHandler( rowhandler,beanType, null,sql, offset,pagesize,totalsize,bean);		 
	}
	
	
	
	public ListInfo queryListInfoBeanByRowHandler(RowHandler rowhandler,Class<?> beanType, String sql, long offset,int pagesize,Object bean) throws SQLException
	{
		return queryListInfoBeanWithDBNameByRowHandler( rowhandler,beanType, null,sql, offset,pagesize,-1L,bean);		 
	}
	
	
	public <T> T queryObjectBeanByRowHandler(RowHandler rowhandler,Class<T> beanType, String sql, Object bean) throws SQLException
	{
		return queryObjectBeanWithDBNameByRowHandler(rowhandler,beanType,null, sql, bean);
		
		 
	}
	
	public <T> List<T> queryListBeanWithDBNameByRowHandler(RowHandler rowhandler,Class<T> beanType,String dbname, String sqlname, Object bean) throws SQLException
	{
		
		String sql = this.getSql(dbname, sqlname);
		return SQLExecutor.queryListBeanWithDBNameByRowHandler(  rowhandler, beanType, dbname,  sql,  bean);  
	}
	
	
	public <T> T queryObjectBeanWithDBNameByRowHandler(RowHandler rowhandler,Class<T> beanType,String dbname, String sqlname, Object bean) throws SQLException
	{
		
		String sql = this.getSql(dbname, sqlname);
		return SQLExecutor.queryObjectBeanWithDBNameByRowHandler(  rowhandler, beanType, dbname,  sql,  bean);  
		 
	}
	
	
	
	
	
	public void queryBeanByNullRowHandler(NullRowHandler rowhandler, String sql, Object bean) throws SQLException
	{
		
		 queryBeanWithDBNameByNullRowHandler( rowhandler,null, sql, bean); 
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
		
		String sql = this.getSql(dbname, sqlname);
		return SQLExecutor.queryListInfoBeanWithDBNameByNullRowHandler(  rowhandler, dbname, sql, offset,pagesize,totalsize,bean);  	 
	}
	public ListInfo queryListInfoBeanWithDBNameByNullRowHandler(NullRowHandler rowhandler,String dbname, String sqlname, long offset,int pagesize,String totalsizesqlname,Object bean) throws SQLException
	{
		
		String sql = this.getSql(dbname, sqlname);
		String totalsizesql = this.getSql(dbname, totalsizesqlname);
		return SQLExecutor.queryListInfoBeanWithDBNameByNullRowHandler(  rowhandler, dbname, sql, offset,pagesize,totalsizesql,bean);  	 
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
		
		String sql = this.getSql(dbname, sqlname);
		return SQLExecutor.queryListInfoBeanWithDBNameByNullRowHandler(  rowhandler, dbname, sql, offset,pagesize,-1L,bean);  	 
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
	public ListInfo queryListInfoBeanByNullRowHandler(NullRowHandler rowhandler, String sql, long offset,int pagesize,long totalsize,Object bean) throws SQLException
	{
		return queryListInfoBeanWithDBNameByNullRowHandler( rowhandler, null,sql, offset,pagesize,totalsize,bean);		 
	}
	
	public ListInfo queryListInfoBeanByNullRowHandler(NullRowHandler rowhandler, String sql, long offset,int pagesize,String totalsizesqlname,Object bean) throws SQLException
	{
		return queryListInfoBeanWithDBNameByNullRowHandler( rowhandler, null,sql, offset,pagesize,totalsizesqlname,bean);		 
	}
	public ListInfo queryListInfoBeanByNullRowHandler(NullRowHandler rowhandler, String sql, long offset,int pagesize,Object bean) throws SQLException
	{
		return queryListInfoBeanWithDBNameByNullRowHandler( rowhandler, null,sql, offset,pagesize,-1L,bean);		 
	}
	
	
	public void queryBeanWithDBNameByNullRowHandler(NullRowHandler rowhandler,String dbname, String sqlname, Object bean) throws SQLException
	{
		
		String sql = this.getSql(dbname, sqlname);
		SQLExecutor.queryBeanWithDBNameByNullRowHandler(  rowhandler,  dbname,  sql,  bean);  	 
	}

	
	

}
