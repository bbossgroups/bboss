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

import org.frameworkset.persitent.util.SQLInfo;
import org.frameworkset.persitent.util.SQLUtil;

import com.frameworkset.common.poolman.handle.FieldRowHandler;
import com.frameworkset.common.poolman.handle.NullRowHandler;
import com.frameworkset.common.poolman.handle.RowHandler;
import com.frameworkset.util.ListInfo;

/**
 * <p>Title: SQLExecutor.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2008</p>
 * @Date 2010-3-11 下午01:46:25
 * @author biaoping.yin
 * @version 1.0
 */
public class SQLExecutor
{
    /**
     * 数据库查询语句属性
     */
    protected String statement ;    
//    protected String pretoken;
//    protected String endtoken;
    protected String action;
    public static final String ACTION_INSERT = "insert";
    public static final String ACTION_UPDATE = "update";
    public static final String ACTION_DELETE = "delete";
    
    public static final String BATCH_COMMON = "common";
    public static final String BATCH_PREPARED = "prepared";
//    protected String dbname;
    

    /**
	 * 批处理预编译操作参数集，
	 * List<Params>
	 */
	private List<SQLParams> batchsqlparams;
	/**
	 * 批处理预编译操作参数集，
	 * List<String>
	 */
	private List<String> batchsqls;
	private String batchtype=BATCH_COMMON;
	
    /**
     * 预编译参数列表,单独的预编语句参数
     * Map<String,Param>
     */
    protected SQLParams sqlparams ;
	private String batchDBName;
	private boolean batchOptimize = false;
	
    
    public SQLParams getSQLParams() throws SetSQLParamException
    {
        if(sqlparams != null )
        {
            if((this.getPretoken() == null && this.getEndtoken()!= null) ||
                    (getPretoken() != null && getEndtoken() == null))
                throw new SetSQLParamException("非法的绑定变量分割符设置:pretoken = " + getPretoken() + "endtoken =" + getEndtoken() );            
        }
        
        return this.sqlparams;
    }
    
    /**
     * 添加sql参数，由DefaultDataInfoImpl进行处理
     * @param name
     * @param value
     * @param type
     * @throws SetSQLParamException 
     */
    public void addSQLParam(String name, String value, String type) throws SetSQLParamException
    {   
        addSQLParam( name,  value,  type, null);
    }
   
    private void init()
    {
        if(sqlparams == null)
        {
            sqlparams = new SQLParams();            
        }
    }
    /**
     * 添加sql参数，由DefaultDataInfoImpl进行处理
     * @param name
     * @param value
     * @param type
     * @throws SetSQLParamException 
     */
    public void addSQLParam(String name, Object value, String type,String dataformat) throws SetSQLParamException
    {   
        init();
       
        addSQLParam( sqlparams, name,  value,  type, dataformat);
    }
    
    /**
     * 添加sql参数，由DefaultDataInfoImpl进行处理
     * @param name
     * @param value
     * @param type
     * @throws SetSQLParamException 
     */
    public void addSQLParam(String name, Object value, String type,String dataformat,String charset) throws SetSQLParamException
    {   
        init();
       
        addSQLParam( sqlparams, name,  value,  type, dataformat,charset);
    }
    
    /**
     * 添加sql参数，由DefaultDataInfoImpl进行处理
     * @param name
     * @param value
     * @param type
     * @throws SetSQLParamException 
     */
    public static void addSQLParam(SQLParams sqlparams,String name, Object value, String type,String dataformat) throws SetSQLParamException
    {   
//        init();
       
        sqlparams.addSQLParam( name,  value,  type, dataformat);
    }
    /**
     * 添加sql参数，由DefaultDataInfoImpl进行处理
     * @param name
     * @param value
     * @param type
     * @throws SetSQLParamException 
     */
    public static void addSQLParam(SQLParams sqlparams,String name, Object value, String type,String dataformat,String charset) throws SetSQLParamException
    {   
//        init();
       
        sqlparams.addSQLParam( name,  value,  type, dataformat,charset);
    }
    
    public String getDbname()
    {
        init();
        return this.sqlparams.getDbname();
    }

    public void setDbname(String dbname)
    {
        init();
        this.sqlparams.setDbname(dbname);
    }

    public String getStatement()
    {
        return statement;
    }

    public void setStatement(String statement)
    {
        this.statement = statement;
    }

    public String getPretoken()
    {
        init();
        return this.sqlparams.getPretoken();
    }

    public void setPretoken(String pretoken)
    {
        init();
        sqlparams.setPretoken(pretoken);
    }

    public String getEndtoken()
    {
        init();
        return this.sqlparams.getEndtoken();
    }

    public void setEndtoken(String endtoken)
    {
        init();
        this.sqlparams.setEndtoken(endtoken);
    }

    public String getAction()
    {
        return action;
    }

    public void setAction(String action)
    {
        this.action = action;
    }
    
    public Object execute() throws SQLException
    {
    	if(this.batchsqlparams == null && this.batchsqls == null)
    	{
	        action = action.toLowerCase();
	        PreparedDBUtil dbutil = new PreparedDBUtil();
	        if(action.equals(ACTION_INSERT))
	        {
	            if(this.getSQLParams().size() > 0)
	            {
	            	
	                dbutil.preparedInsert(this.getSQLParams(), this.getDbname(), SQLUtil.getGlobalSQLUtil().getSQLInfo(this.getStatement(),true,true));
	            }
	            else
	            {
	                dbutil.executeInsert(this.getDbname(),this.getStatement());
	            }
	        }
	        else if(action.equals(ACTION_UPDATE))
	        {
	            if(this.getSQLParams().size() > 0)
	            {
	                dbutil.preparedUpdate(this.getSQLParams(), this.getDbname(),SQLUtil.getGlobalSQLUtil().getSQLInfo(this.getStatement(),true,true));
	            }
	            else
	            {
	                dbutil.executeUpdate(this.getDbname(),this.getStatement());
	            }
	                
	        }
	        else if(action.equals(ACTION_DELETE))
	        {
	            if(this.getSQLParams().size() > 0)
	            {
	                dbutil.preparedDelete(this.getSQLParams(), this.getDbname(),SQLUtil.getGlobalSQLUtil().getSQLInfo(this.getStatement(),true,true));
	            }
	            else
	            {
	                dbutil.executeDelete(this.getDbname(),this.getStatement());
	            }
	        }
	        else
	            throw new SQLException("不支持的数据库操作：" + action);
	        return dbutil.executePrepared();
    	}
    	else
    	{
    		if(this.batchtype.equals(BATCH_PREPARED))
    		{
    			PreparedDBUtil dbutil = new PreparedDBUtil();
    			dbutil.setBatchOptimize(isBatchOptimize());
    			dbutil.setPrepareDBName(batchDBName);
    			dbutil.addPreparedBatch(new ListSQLParams(batchsqlparams,null));
    			dbutil.executePreparedBatch();
    		}
    		else
    		{
    			DBUtil dbutil = new PreparedDBUtil();
    			
    			dbutil.addBatch(this.batchsqls);
    			dbutil.executeBatch(this.getBatchDBName());
    		}
    		return null;
    	}
        
        
    }
    
    public void addPreparedBatch()
    {
    	if(this.batchsqlparams == null)
		{
    		batchsqlparams = new ArrayList<SQLParams>();
//			batchparamsIDXBySQL = new HashMap();
		}
    	batchsqlparams.add(this.sqlparams);
//		batchparamsIDXBySQL.put(Params.prepareselect_sql, Params);
		SQLInfo old = this.sqlparams.getOldsql();
		String oldendtoken = this.sqlparams.getEndtoken();
		String oldpretoken = this.sqlparams.getPretoken();
		
		sqlparams = this.buildParams();
		sqlparams.setOldsql(old);
		sqlparams.setEndtoken(oldendtoken);
		sqlparams.setPretoken(oldpretoken);
    }
    
    public void addBatch(String statement)
    {
    	if(batchsqls == null)
    		batchsqls = new ArrayList<String>();
    	batchsqls.add(statement);
    }

	private SQLParams buildParams() {
		// TODO Auto-generated method stub
		return new SQLParams();
	}

	public String getBatchDBName() {
		return batchDBName;
	}

	public void setBatchDBName(String batchDBName) {
		this.batchDBName = batchDBName;
	}

	public boolean isBatchOptimize() {
		return batchOptimize;
	}

	public void setBatchOptimize(boolean batchOptimize) {
		this.batchOptimize = batchOptimize;
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
	
	public void init(String statement,String pretoken,String endtoken,String action)
	{
		this.sqlparams = new SQLParams();
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
		
		
		SQLInfoExecutor.init(sqlparams, SQLUtil.getGlobalSQLUtil().getSQLInfo(statement,true,true), pretoken, endtoken, action);
		
		
	}

	public String getBatchtype() {
		return batchtype;
	}

	public void setBatchtype(String batchtype) {
		this.batchtype = batchtype;
	}
	
	
	public static void insertBeans(String dbname, String sql, List beans) throws SQLException {
		
		if(beans == null || beans.size() == 0)
			return ;
//		execute( dbname,  sql,  beans,PreparedDBUtil.INSERT,(GetCUDResult)null);
		SQLInfo sqlinfo = SQLUtil.getGlobalSQLUtil().getSQLInfo(sql,true,true);
		SQLInfoExecutor.insertBeans(dbname,sqlinfo, beans);
	}
	
	public static void insertBeans(String dbname, String sql, List beans,GetCUDResult getCUDResult) throws SQLException {
		
		if(beans == null || beans.size() == 0)
			return ;
//		execute( dbname,  sql,  beans,PreparedDBUtil.INSERT,getCUDResult);
		SQLInfo sqlinfo = SQLUtil.getGlobalSQLUtil().getSQLInfo(sql,true,true);
		SQLInfoExecutor.insertBeans(dbname,sqlinfo, beans,getCUDResult);
	}
	
	
//	public static void execute(String dbname, String sql, List beans,boolean isBatchOptimize,int action) throws SQLException
//	{
//		execute(dbname, sql, beans,isBatchOptimize,action,(GetCUDResult)null) ;
//	}
	
//	public static void execute(String dbname, String sql, List beans,boolean isBatchOptimize,int action,GetCUDResult getCUDResult) throws SQLException
//	{
//		Connection con = null;
//		try
//		{
//			con = DBUtil.getConection(dbname);
//			List<SQLParams> batchsqlparams = SQLParams.convertBeansToSqlParams(beans,new SQLInfo(sql,true,true),dbname,action,con);
//			if(batchsqlparams == null)
//				return ;
//			PreparedDBUtil dbutil = new PreparedDBUtil();
//			dbutil.setBatchOptimize(isBatchOptimize);
//			dbutil.setPrepareDBName(dbname);
//			dbutil.addPreparedBatch(new ListSQLParams(batchsqlparams,null));
//			dbutil.executePreparedBatch(con,getCUDResult);
//		}
//		finally
//		{
//			try {
//				if (con != null)
//					con.close();
//			} catch (Exception e) {
//				// TODO: handle exception
//			}
//		}
//	}
//	private static Object CUDexecute(String dbname, String sql, Object bean,int action) throws SQLException
//	{
//		return CUDexecute(dbname, sql, bean,action,false) ;
//	}
//	/**
//	 * 针对增删改三种类型DB操作的统一处理方法
//	 * @param dbname
//	 * @param sql
//	 * @param bean
//	 * @param isBatchOptimize
//	 * @param action
//	 * @return
//	 * @throws SQLException
//	 */
//	private static Object CUDexecute(String dbname, String sql, Object bean,int action,boolean getCUDResult) throws SQLException
//	{
//		Connection con = null;
//		try
//		{
//			SQLInfo sqlinfo = new SQLInfo(sql,true,false);
//			con = DBUtil.getConection(dbname);
//			SQLParams batchsqlparams = SQLParams.convertBeanToSqlParams(bean,sqlinfo,dbname,action,con);
//			if(batchsqlparams == null)
//				return null;
////			PreparedDBUtil dbutil = new PreparedDBUtil();
////			dbutil.setBatchOptimize(isBatchOptimize);
////			dbutil.setPrepareDBName(dbname);
////			dbutil.addPreparedBatch(batchsqlparams);
////			dbutil.executePreparedBatch(con);
//			
//			
////			 action = action.toLowerCase();
//	        PreparedDBUtil dbutil = new PreparedDBUtil();
//	        if(action == PreparedDBUtil.INSERT)
//	        {
//	            if(batchsqlparams.size() > 0)
//	            {
//	                dbutil.preparedInsert(batchsqlparams, dbname,sqlinfo);
//	                return dbutil.executePrepared(con,getCUDResult);
//	            }
//	            else
//	            {
//	                return dbutil.executeInsert(dbname,sql,con);
//	            }
//	        }
//	        else if(action == PreparedDBUtil.UPDATE)
//	        {
//	            if(batchsqlparams.size() > 0)
//	            {
//	                dbutil.preparedUpdate(batchsqlparams, dbname,sqlinfo);
//	                return dbutil.executePrepared(con,getCUDResult);
//	            }
//	            else
//	            {
//	                return dbutil.executeUpdate(dbname,sql,con);
//	            }
//	                
//	        }
//	        else if(action == PreparedDBUtil.DELETE)
//	        {
//	            if(batchsqlparams.size() > 0)
//	            {
//	                dbutil.preparedDelete(batchsqlparams, dbname,sqlinfo);
//	                return dbutil.executePrepared(con,getCUDResult);
//	            }
//	            else
//	            {
//	                return dbutil.executeDelete(dbname,sql,con);
//	            }
//	        }
//	        else
//	            throw new SQLException("不支持的数据库操作：" + action);
//		        
//		}
//		finally
//		{
//			try {
//				if (con != null)
//					con.close();
//			} catch (Exception e) {
//				// TODO: handle exception
//			}
//		}
//	}
	
	
	
	
	

	
	
	
//	public static void execute(String dbname, String sql, List beans,int action) throws SQLException
//	{
//		execute(dbname, sql, beans,false,action,null);
//	}
//	
//	public static void execute(String dbname, String sql, List beans,int action,GetCUDResult getCUDResult) throws SQLException
//	{
//		execute(dbname, sql, beans,false,action,getCUDResult);
//	}
	
//	
	
	
	public static Object update( String sql, Object... fields) throws SQLException {
//		return execute(null, sql,PreparedDBUtil.UPDATE, fields);
		SQLInfo sqlinfo = SQLUtil.getGlobalSQLUtil().getSQLInfo(sql,false,false);		
		return SQLInfoExecutor.update(   sqlinfo,fields);
	}
	



	public static Object delete(String sql, Object... fields) throws SQLException {
//		return execute(null, sql,PreparedDBUtil.DELETE, fields);
		SQLInfo sqlinfo = SQLUtil.getGlobalSQLUtil().getSQLInfo(sql,false,false);
		return SQLInfoExecutor.delete(sqlinfo, fields);
		
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
		SQLInfo sqlinfo = SQLUtil.getGlobalSQLUtil().getSQLInfo(sql,false,false);
		SQLInfoExecutor.deleteByKeys(sqlinfo, fields);
		
	}
	public static void deleteByKeysWithDBName(String dbname,String sql, int... fields) throws SQLException {
//		executeBatch(dbname, sql,PreparedDBUtil.DELETE, fields);
		SQLInfo sqlinfo = SQLUtil.getGlobalSQLUtil().getSQLInfo(sql,false,false);
		SQLInfoExecutor.deleteByKeysWithDBName(dbname,sqlinfo, fields);
		
	}
	
	public static void deleteByLongKeys(String sql, long... fields) throws SQLException {
//		executeBatch(null, sql,PreparedDBUtil.DELETE, fields);
		SQLInfo sqlinfo = SQLUtil.getGlobalSQLUtil().getSQLInfo(sql,false,false);
		SQLInfoExecutor.deleteByLongKeys(sqlinfo, fields);
		
	}
	public static void deleteByLongKeysWithDBName(String dbname,String sql, long... fields) throws SQLException {
//		executeBatch(dbname, sql,PreparedDBUtil.DELETE, fields);
		SQLInfo sqlinfo = SQLUtil.getGlobalSQLUtil().getSQLInfo(sql,false,false);
		SQLInfoExecutor.deleteByLongKeysWithDBName(dbname,sqlinfo, fields);
		
	}
	
	public static void deleteByKeys(String sql, String... fields) throws SQLException {
//		executeBatch(null, sql,PreparedDBUtil.DELETE, fields);
		SQLInfo sqlinfo = SQLUtil.getGlobalSQLUtil().getSQLInfo(sql,false,false);
		SQLInfoExecutor.deleteByKeys(sqlinfo, fields);
		
	}
	public static void deleteByKeysWithDBName(String dbname,String sql, String... fields) throws SQLException {
//		executeBatch(dbname, sql,PreparedDBUtil.DELETE, fields);
		SQLInfo sqlinfo = SQLUtil.getGlobalSQLUtil().getSQLInfo(sql,false,false);
		SQLInfoExecutor.deleteByKeysWithDBName(dbname,sqlinfo, fields);
		
	}
	
	public static void deleteByShortKeys(String sql, short... fields) throws SQLException {
//		executeBatch(null, sql,PreparedDBUtil.DELETE, fields);
		SQLInfo sqlinfo = SQLUtil.getGlobalSQLUtil().getSQLInfo(sql,false,false);
		SQLInfoExecutor.deleteByShortKeys(sqlinfo, fields);
		
	}
	public static void deleteByShortKeysWithDBName(String dbname,String sql, short... fields) throws SQLException {
//		executeBatch(dbname, sql,PreparedDBUtil.DELETE, fields);
		SQLInfo sqlinfo = SQLUtil.getGlobalSQLUtil().getSQLInfo(sql,false,false);
		SQLInfoExecutor.deleteByShortKeysWithDBName(dbname,sqlinfo, fields);
	}



	public static Object insert(String sql, Object... fields) throws SQLException {
//		return execute(null, sql,PreparedDBUtil.INSERT, fields);
		SQLInfo sqlinfo = SQLUtil.getGlobalSQLUtil().getSQLInfo(sql,false,false);
		return SQLInfoExecutor.insert(sqlinfo, fields);
	}
	
	public static Object updateWithDBName(String dbname, String sql, Object... fields) throws SQLException {
//		return execute(dbname, sql,PreparedDBUtil.UPDATE, fields);
		SQLInfo sqlinfo = SQLUtil.getGlobalSQLUtil().getSQLInfo(sql,false,false);		
		return SQLInfoExecutor.updateWithDBName(  dbname,  sqlinfo,  fields);
	}
	
	public static Object deleteWithDBName(String dbname, String sql, Object... fields) throws SQLException {
//		return execute(dbname, sql,PreparedDBUtil.DELETE, fields);
		SQLInfo sqlinfo = SQLUtil.getGlobalSQLUtil().getSQLInfo(sql,false,false);
		return SQLInfoExecutor.deleteWithDBName(dbname,sqlinfo, fields);
		
	}



	public static Object insertWithDBName(String dbname, String sql, Object... fields) throws SQLException {
//		return execute(dbname, sql,PreparedDBUtil.INSERT, fields);
		SQLInfo sqlinfo = SQLUtil.getGlobalSQLUtil().getSQLInfo(sql,false,false);
		return SQLInfoExecutor.insertWithDBName(dbname,sqlinfo, fields);
	}

	public static void updateBeans(String dbname, String sql, List beans) throws SQLException {
		if(beans == null || beans.size() == 0)
			return ;
//		execute( dbname,  sql,  beans,PreparedDBUtil.UPDATE,(GetCUDResult)null);
		SQLInfo sqlinfo = SQLUtil.getGlobalSQLUtil().getSQLInfo(sql,true,true);		
		SQLInfoExecutor.updateBeans(  dbname,  sqlinfo,  beans);
	}
	
	public static void updateBeans(String dbname, String sql, List beans,GetCUDResult GetCUDResult) throws SQLException {
		if(beans == null || beans.size() == 0)
			return ;
//		execute( dbname,  sql,  beans,PreparedDBUtil.UPDATE,GetCUDResult);
		SQLInfo sqlinfo = SQLUtil.getGlobalSQLUtil().getSQLInfo(sql,true,true);		
		SQLInfoExecutor.updateBeans(  dbname,  sqlinfo,  beans,GetCUDResult);
	}



	public static void deleteBeans(String dbname, String sql, List beans) throws SQLException {
		if(beans == null || beans.size() == 0)
			return ;
		
		SQLInfo sqlinfo = SQLUtil.getGlobalSQLUtil().getSQLInfo(sql,true,true);
		SQLInfoExecutor.deleteBeans(dbname, sqlinfo, beans);
//		execute( dbname,  sql,  beans,PreparedDBUtil.DELETE);
		
	}
	
	public static void deleteBeans(String dbname, String sql, List beans,GetCUDResult GetCUDResult) throws SQLException {
		if(beans == null || beans.size() == 0)
			return ;
//		execute( dbname,  sql,  beans,PreparedDBUtil.DELETE, GetCUDResult);
		SQLInfo sqlinfo = SQLUtil.getGlobalSQLUtil().getSQLInfo(sql,true,true);
		SQLInfoExecutor.deleteBeans(dbname, sqlinfo, beans,GetCUDResult);
		
	}



	public static void insertBean(String dbname, String sql, Object bean) throws SQLException {
		if(bean == null)
			return ;
//		List datas = new ArrayList();
//		datas.add(bean);
//		insertBeans( dbname,  sql,  datas);
//		CUDexecute(dbname, sql, bean,PreparedDBUtil.INSERT,false);
		SQLInfo sqlinfo = SQLUtil.getGlobalSQLUtil().getSQLInfo(sql,true,false);
		SQLInfoExecutor.insertBean(dbname,sqlinfo, bean);
	}
	
	public static void insertBean(String dbname, String sql, Object bean,GetCUDResult getCUDResult) throws SQLException {
		if(bean == null)
			return ;
////		List datas = new ArrayList();
////		datas.add(bean);
////		insertBeans( dbname,  sql,  datas);
//		if(getCUDResult == null)
//		{
//			
//			CUDexecute(dbname, sql, bean,PreparedDBUtil.INSERT,false);
//		}
//		else
//		{
//			GetCUDResult getCUDResult_ = (GetCUDResult)CUDexecute(dbname, sql, bean,PreparedDBUtil.INSERT,true);
//			getCUDResult.setGetCUDResult(getCUDResult_);
//			
//		}
		SQLInfo sqlinfo = SQLUtil.getGlobalSQLUtil().getSQLInfo(sql,true,false);
		SQLInfoExecutor.insertBean(dbname,sqlinfo, bean,getCUDResult);
	}



	public static void updateBean(String dbname, String sql, Object bean) throws SQLException {
		if(bean == null )
			return ;
//		List datas = new ArrayList();
//		datas.add(bean);
//		updateBeans( dbname,  sql,  datas);
//		CUDexecute(dbname, sql, bean,PreparedDBUtil.UPDATE,false);
		SQLInfo sqlinfo = SQLUtil.getGlobalSQLUtil().getSQLInfo(sql,true,false);		
		SQLInfoExecutor.updateBean(   dbname,  sqlinfo,  bean);
	
	}
	
	public static void updateBean(String dbname, String sql, Object bean,GetCUDResult getCUDResult) throws SQLException {
		if(bean == null )
			return ;
//		List datas = new ArrayList();
//		datas.add(bean);
//		updateBeans( dbname,  sql,  datas);
		
//		if(getCUDResult != null)
//		{
//			GetCUDResult getCUDResult_ = (GetCUDResult)CUDexecute(dbname, sql, bean,PreparedDBUtil.UPDATE,true);
//			getCUDResult.setGetCUDResult(getCUDResult_);
//		}
//		else
//			CUDexecute(dbname, sql, bean,PreparedDBUtil.UPDATE,false);
		SQLInfo sqlinfo = SQLUtil.getGlobalSQLUtil().getSQLInfo(sql,true,false);		
		SQLInfoExecutor.updateBean(  dbname,  sqlinfo,  bean, getCUDResult);
	}
	
	public static void updateBean( String sql, Object bean,GetCUDResult getCUDResult) throws SQLException {
//		if(bean == null )
//			return ;
//		List datas = new ArrayList();
//		datas.add(bean);
//		updateBeans( null,  sql,  datas);
		updateBean((String)null,  sql,  bean, getCUDResult);
	}

	public static void deleteBean( String sql, Object bean,GetCUDResult getCUDResult) throws SQLException {
		deleteBean((String )null,sql, bean,getCUDResult) ;
	}

	public static void deleteBean(String dbname, String sql, Object bean) throws SQLException {
		
		if(bean == null)
			return ;
//		List datas = new ArrayList();
//		datas.add(bean);
//		deleteBeans( dbname,  sql,  datas);
		SQLInfo sqlinfo = SQLUtil.getGlobalSQLUtil().getSQLInfo(sql,true,false);
		SQLInfoExecutor.deleteBean(dbname, sqlinfo, bean);
//		CUDexecute(dbname, sql, bean,PreparedDBUtil.DELETE,false);
	}
	public static void deleteBean(String dbname, String sql, Object bean,GetCUDResult getCUDResult) throws SQLException {
		
		if(bean == null)
			return ;
//		List datas = new ArrayList();
//		datas.add(bean);
//		deleteBeans( dbname,  sql,  datas);
//		if(getCUDResult != null)
//		{
//			GetCUDResult getCUDResult_ = (GetCUDResult)CUDexecute(dbname, sql, bean,PreparedDBUtil.DELETE,true);
//			getCUDResult.setGetCUDResult(getCUDResult_);
//		}
//		else
//			CUDexecute(dbname, sql, bean,PreparedDBUtil.DELETE,false);
		SQLInfo sqlinfo = SQLUtil.getGlobalSQLUtil().getSQLInfo(sql,true,false);
		SQLInfoExecutor.deleteBean(dbname, sqlinfo, bean,getCUDResult);
	}
	
	public static void insertBeans(String sql, List beans) throws SQLException {
		insertBeans( null,sql, beans); 
	}
	
	public static void insertBeans(String sql, List beans,GetCUDResult getCUDResult) throws SQLException {
		insertBeans( (String)null,sql, beans,getCUDResult); 
	}
	
	



	public static void updateBeans( String sql, List beans) throws SQLException {
		updateBeans( null,sql, beans); 
	}



	public static void deleteBeans( String sql, List beans) throws SQLException {
		deleteBeans( null,sql, beans); 
		
	}



	public static void insertBean( String sql, Object bean) throws SQLException {
//		if(bean == null)
//			return ;
//		List datas = new ArrayList();
//		datas.add(bean);
//		insertBeans( null,  sql,  datas);
		insertBean( (String)null,sql, bean);
	}
	public static void insertBean( String sql, Object bean,GetCUDResult getCUDResult) throws SQLException {
//		if(bean == null)
//			return ;
//		List datas = new ArrayList();
//		datas.add(bean);
//		insertBeans( null,  sql,  datas);
		insertBean( (String)null,sql, bean,getCUDResult);
	}



	public static void updateBean( String sql, Object bean) throws SQLException {
//		if(bean == null )
//			return ;
//		List datas = new ArrayList();
//		datas.add(bean);
//		updateBeans( null,  sql,  datas);
		updateBean( (String)null,sql, bean);
	}

	

	public static void deleteBean(String sql, Object bean) throws SQLException {
		
//		if(bean == null)
//			return ;
//		List datas = new ArrayList();
//		datas.add(bean);
//		deleteBeans( null,  sql,  datas);
		deleteBean((String)null,sql, bean);
		
	}
	
	
	public static <T> List<T> queryList(Class<T> beanType, String sql, Object... fields) throws SQLException
	{
		
		return queryListWithDBName(beanType,null, sql, fields); 
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
		SQLInfo sqlinfo = SQLUtil.getGlobalSQLUtil().getSQLInfo(sql,false,false);
		return SQLInfoExecutor.queryListInfoWithDBName(  beanType, dbname,  sqlinfo,  offset, pagesize, fields);
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
		SQLInfo sqlinfo = SQLUtil.getGlobalSQLUtil().getSQLInfo(sql,false,false);
		return SQLInfoExecutor.queryListInfoWithDBName2ndTotalsize(  beanType, dbname,  sqlinfo,  offset, pagesize, totalsize, fields);
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
		SQLInfo sqlinfo = SQLUtil.getGlobalSQLUtil().getSQLInfo(sql,false,false);
		SQLInfo totalsizesqlinfo = SQLUtil.getGlobalSQLUtil().getSQLInfo(totalsizesql,false,false);
		
		return SQLInfoExecutor.queryListInfoWithDBName2ndTotalsizesql(   beanType, dbname,  sqlinfo,  offset, pagesize, totalsizesqlinfo, fields);
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
		return queryListInfoWithDBName(beanType, null,sql, offset,pagesize,fields);		 
	}
	
	public static ListInfo queryListInfoWithTotalsize(Class<?> beanType, String sql, long offset,int pagesize,long totalsize,Object... fields) throws SQLException
	{
		return queryListInfoWithDBName2ndTotalsize(beanType, null,sql, offset,pagesize,totalsize,fields);		 
	}
	
	public static ListInfo queryListInfoWithTotalsizesql(Class<?> beanType, String sql, long offset,int pagesize,String totalsizesql,Object... fields) throws SQLException
	{
		return queryListInfoWithDBName2ndTotalsizesql(beanType, null,sql, offset,pagesize,totalsizesql,fields);		 
	}
	
	
	public static <T> T queryObject(Class<T> beanType, String sql, Object... fields) throws SQLException
	{
		return queryObjectWithDBName(beanType,null, sql, fields);
		
		 
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
		SQLInfo sqlinfo = SQLUtil.getGlobalSQLUtil().getSQLInfo(sql,false,false);		
		return SQLInfoExecutor.queryListWithDBName(  beanType, dbname,  sqlinfo,  fields);
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
		SQLInfo sqlinfo = SQLUtil.getGlobalSQLUtil().getSQLInfo(sql,false,false);		
		return SQLInfoExecutor.queryObjectWithDBName( beanType, dbname,  sqlinfo,  fields);
		 
	}
	
	
	public static <T> List<T> queryListByRowHandler(RowHandler rowhandler,Class<T> beanType, String sql, Object... fields) throws SQLException
	{
		
		return queryListWithDBNameByRowHandler(rowhandler,beanType,null, sql, fields); 
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
		SQLInfo sqlinfo = SQLUtil.getGlobalSQLUtil().getSQLInfo(sql,false,false);		
		return SQLInfoExecutor.queryListInfoWithDBNameByRowHandler(  rowhandler, beanType, dbname, sqlinfo, offset,pagesize,fields);
	}
	
	public static ListInfo queryListInfoWithDBName2ndTotalsizeByRowHandler(RowHandler rowhandler,Class<?> beanType,String dbname, String sql, long offset,int pagesize,long totalsize,Object... fields) throws SQLException
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
		SQLInfo sqlinfo = SQLUtil.getGlobalSQLUtil().getSQLInfo(sql,false,false);
		return SQLInfoExecutor.queryListInfoWithDBName2ndTotalsizeByRowHandler(   rowhandler, beanType, dbname,  sqlinfo,  offset, pagesize, totalsize,fields);
	}
	
	public static ListInfo queryListInfoWithDBName2ndTotalsizesqlByRowHandler(RowHandler rowhandler,Class<?> beanType,String dbname, String sql, long offset,int pagesize,String totalsizesql,Object... fields) throws SQLException
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
		SQLInfo sqlinfo = SQLUtil.getGlobalSQLUtil().getSQLInfo(sql,false,false);
		SQLInfo totalsizesqlinfo = SQLUtil.getGlobalSQLUtil().getSQLInfo(totalsizesql,false,false);
		
		return SQLInfoExecutor.queryListInfoWithDBName2ndTotalsizesqlByRowHandler(   rowhandler, beanType, dbname,  sqlinfo,  offset, pagesize, totalsizesqlinfo, fields);
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
		return queryListInfoWithDBNameByRowHandler( rowhandler,beanType, null,sql, offset,pagesize,fields);		 
	}
	
	public static ListInfo queryListInfoWithTotalsizeByRowHandler(RowHandler rowhandler,Class<?> beanType, String sql, long offset,int pagesize,long totalsize,Object... fields) throws SQLException
	{
		return queryListInfoWithDBName2ndTotalsizeByRowHandler( rowhandler,beanType, null,sql, offset,pagesize,totalsize,fields);		 
	}
	
	public static ListInfo queryListInfoWithTotalsizesqlByRowHandler(RowHandler rowhandler,Class<?> beanType, String sql, long offset,int pagesize,String totalsizesql,Object... fields) throws SQLException
	{
		return queryListInfoWithDBName2ndTotalsizesqlByRowHandler( rowhandler,beanType, null,sql, offset,pagesize,totalsizesql,fields);		 
	}
	
	
	public static <T> T queryObjectByRowHandler(RowHandler rowhandler,Class<T> beanType, String sql, Object... fields) throws SQLException
	{
		return queryObjectWithDBNameByRowHandler(rowhandler,beanType,null, sql, fields);
		
		 
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
		SQLInfo sqlinfo = SQLUtil.getGlobalSQLUtil().getSQLInfo(sql,false,false);		
		return SQLInfoExecutor.queryListWithDBNameByRowHandler(   rowhandler,beanType, dbname,  sqlinfo,  fields);
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
		SQLInfo sqlinfo = SQLUtil.getGlobalSQLUtil().getSQLInfo(sql,false,false);		
		return SQLInfoExecutor.queryObjectWithDBNameByRowHandler(  rowhandler, beanType, dbname,  sqlinfo,  fields);
		 
	}
	
	
	
	
	
	public static void queryByNullRowHandler(NullRowHandler rowhandler, String sql, Object... fields) throws SQLException
	{
		
		 queryWithDBNameByNullRowHandler( rowhandler,null, sql, fields); 
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
		SQLInfo sqlinfo = SQLUtil.getGlobalSQLUtil().getSQLInfo(sql,false,false);
		
		return SQLInfoExecutor.queryListInfoWithDBNameByNullRowHandler(   rowhandler, dbname,  sqlinfo,  offset, pagesize, fields);
	}
	public static ListInfo queryListInfoWithDBName2ndTotalsizeByNullRowHandler(NullRowHandler rowhandler,String dbname, String sql, long offset,int pagesize,long totalsize,Object... fields) throws SQLException
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
		SQLInfo sqlinfo = SQLUtil.getGlobalSQLUtil().getSQLInfo(sql,false,false);
		return SQLInfoExecutor.queryListInfoWithDBName2ndTotalsizeByNullRowHandler(  rowhandler, dbname,  sqlinfo,  offset, pagesize, totalsize,fields);
	}
	public static ListInfo queryListInfoWithDBName2ndTotalsizesqlByNullRowHandler(NullRowHandler rowhandler,String dbname, String sql, long offset,int pagesize,String totalsizesql,Object... fields) throws SQLException
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
		SQLInfo sqlinfo = SQLUtil.getGlobalSQLUtil().getSQLInfo(sql,false,false);
		SQLInfo totalsizesqlinfo = SQLUtil.getGlobalSQLUtil().getSQLInfo(totalsizesql,false,false);
		
		return SQLInfoExecutor.queryListInfoWithDBName2ndTotalsizesqlByNullRowHandler(   rowhandler, dbname,  sqlinfo,  offset, pagesize, totalsizesqlinfo, fields);
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
		return queryListInfoWithDBNameByNullRowHandler( rowhandler, null,sql, offset,pagesize,fields);		 
	}
	public static ListInfo queryListInfoWithTotalsizeByNullRowHandler(NullRowHandler rowhandler, String sql, long offset,int pagesize,long totalsize,Object... fields) throws SQLException
	{
		return queryListInfoWithDBName2ndTotalsizeByNullRowHandler( rowhandler, null,sql, offset,pagesize,totalsize,fields);		 
	}
	public static ListInfo queryListInfoWithTotalsizesqlByNullRowHandler(NullRowHandler rowhandler, String sql, long offset,int pagesize,String totalsizesql,Object... fields) throws SQLException
	{
		return queryListInfoWithDBName2ndTotalsizesqlByNullRowHandler( rowhandler, null,sql, offset,pagesize,totalsizesql,fields);		 
	}
	
	
	
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
		SQLInfo sqlinfo = SQLUtil.getGlobalSQLUtil().getSQLInfo(sql,false,false);		
		SQLInfoExecutor.queryWithDBNameByNullRowHandler( rowhandler, dbname,  sqlinfo,fields);
	}
	
	
	public static <T> List<T> queryListBean(Class<T> beanType, String sql, Object bean) throws SQLException
	{
		
		return queryListBeanWithDBName(beanType,null, sql, bean); 
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
		SQLInfo sqlinfo = SQLUtil.getGlobalSQLUtil().getSQLInfo(sql,true,false);
		return SQLInfoExecutor.queryListInfoBeanWithDBName( beanType, dbname,  sqlinfo,  offset, pagesize, totalsize, bean);
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
		SQLInfo sqlinfo = SQLUtil.getGlobalSQLUtil().getSQLInfo(sql,true,false);
		SQLInfo totalsizesqlinfo = SQLUtil.getGlobalSQLUtil().getSQLInfo(totalsizesql,true,false);
		return SQLInfoExecutor.queryListInfoBeanWithDBName(beanType, dbname,  sqlinfo,  offset, pagesize, totalsizesqlinfo, bean);
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
		SQLInfo sqlinfo = SQLUtil.getGlobalSQLUtil().getSQLInfo(sql,true,false);
		return SQLInfoExecutor.queryListInfoBeanWithDBName( beanType, dbname,  sqlinfo,  offset, pagesize,  bean);
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
		return queryListInfoBeanWithDBName(beanType, null,sql, offset,pagesize,totalsize,bean);		 
	}
	
	public static ListInfo queryListInfoBean(Class<?> beanType, String sql, long offset,int pagesize,String totalsizesql,Object bean) throws SQLException
	{
		return queryListInfoBeanWithDBName(beanType, null,sql, offset,pagesize,totalsizesql,bean);		 
	}
	
	public static ListInfo queryListInfoBean(Class<?> beanType, String sql, long offset,int pagesize,Object bean) throws SQLException
	{
		return queryListInfoBeanWithDBName(beanType, null,sql, offset,pagesize,-1L,bean);		 
	}
	
	public static String queryField( String sql, Object... fields) throws SQLException
	{
		return queryFieldWithDBName(null, sql, fields);
	}
	public static String queryFieldBean( String sql, Object bean) throws SQLException
	{
		return queryFieldBeanWithDBName(null, sql, bean);
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
		SQLInfo sqlinfo = SQLUtil.getGlobalSQLUtil().getSQLInfo(sql,true,false);
		return SQLInfoExecutor.queryFieldBeanWithDBName(  dbname,  sqlinfo,  bean);
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
		SQLInfo sqlinfo = SQLUtil.getGlobalSQLUtil().getSQLInfo(sql,false,false);
		return SQLInfoExecutor.queryFieldWithDBName(  dbname,  sqlinfo,  fields);
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
	
	
	public static <T> T queryTField( Class<T> type,String sql, Object... fields) throws SQLException
	{
		return queryTFieldWithDBName(null, type,sql, fields);
	}
	public static <T> T queryTFieldBean( Class<T> type,String sql, Object bean) throws SQLException
	{
		return queryTFieldBeanWithDBName(null, type,sql, bean);
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
		return queryTFieldBeanWithDBName(dbname, type,(FieldRowHandler<T>)null,sql, bean) ;
	}
	
	public static <T> T queryTFieldWithDBName(String dbname, Class<T> type,String sql, Object... fields) throws SQLException
	{
		
		return queryTFieldWithDBName(dbname, type,(FieldRowHandler<T>)null,sql, fields);
	}
	
	public static <T> T queryTField( Class<T> type,FieldRowHandler<T> fieldRowHandler,String sql, Object... fields) throws SQLException
	{
		return queryTFieldWithDBName(null, type,fieldRowHandler,sql, fields);
	}
	public static <T> T queryTFieldBean( Class<T> type,FieldRowHandler<T> fieldRowHandler,String sql, Object bean) throws SQLException
	{
		return queryTFieldBeanWithDBName(null, type,fieldRowHandler,sql, bean);
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
		SQLInfo sqlinfo = SQLUtil.getGlobalSQLUtil().getSQLInfo(sql,true,false);		
		return SQLInfoExecutor.queryTFieldBeanWithDBName(  dbname, type,fieldRowHandler,sqlinfo, bean);
		 
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
		SQLInfo sqlinfo = SQLUtil.getGlobalSQLUtil().getSQLInfo(sql,false,false);		
		return SQLInfoExecutor.queryTFieldWithDBName(  dbname, type,fieldRowHandler,sqlinfo, fields);
	}
	
	
	public static <T> T queryObjectBean(Class<T> beanType, String sql, Object bean) throws SQLException
	{
		return queryObjectBeanWithDBName(beanType,null, sql, bean);
		
		 
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
		SQLInfo sqlinfo = SQLUtil.getGlobalSQLUtil().getSQLInfo(sql,true,false);
		return SQLInfoExecutor.queryListBeanWithDBName(beanType, dbname,  sqlinfo,  bean);
	}
	
	public static <T> T queryObjectBeanWithDBName(Class<T> beanType,String dbname, String sql, Object bean) throws SQLException
	{
		
//		PreparedDBUtil dbutil = new PreparedDBUtil();
//		SQLParams params = SQLParams.convertBeanToSqlParams(bean,  new SQLInfo(sql,true,false), dbname, PreparedDBUtil.SELECT, null);
//		dbutil.preparedSelect(params,dbname, sql);
//		return (T)dbutil.executePreparedForObject(beanType);
		SQLInfo sqlinfo = SQLUtil.getGlobalSQLUtil().getSQLInfo(sql,true,false);		
		return SQLInfoExecutor.queryObjectBeanWithDBName( beanType, dbname,  sqlinfo, bean);
		 
	}
	
	
	public static <T> List<T> queryListBeanByRowHandler(RowHandler rowhandler,Class<T> beanType, String sql, Object bean) throws SQLException
	{
		
		return queryListBeanWithDBNameByRowHandler(rowhandler,beanType,null, sql, bean); 
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
	public static ListInfo queryListInfoBeanWithDBNameByRowHandler(RowHandler rowhandler,Class<?> beanType,String dbname, String sql, long offset,int pagesize,long totalsize,Object  bean) throws SQLException
	{	
		SQLInfo sqlinfo = SQLUtil.getGlobalSQLUtil().getSQLInfo(sql,true,false);
		return SQLInfoExecutor.queryListInfoBeanWithDBNameByRowHandler(  rowhandler,beanType,dbname, sqlinfo, offset,pagesize,totalsize,bean);	 
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
		SQLInfo sqlinfo = SQLUtil.getGlobalSQLUtil().getSQLInfo(sql,true,false);
		SQLInfo totalsizesqlinfo = SQLUtil.getGlobalSQLUtil().getSQLInfo(totalsizesql,true,false);
		return SQLInfoExecutor.queryListInfoBeanWithDBNameByRowHandler(  rowhandler, beanType, dbname,  sqlinfo,  offset, pagesize, totalsizesqlinfo,  bean);
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
		SQLInfo sqlinfo = SQLUtil.getGlobalSQLUtil().getSQLInfo(sql,true,false);
		return SQLInfoExecutor.queryListInfoBeanWithDBNameByRowHandler(  rowhandler,beanType,dbname, sqlinfo, offset,pagesize,bean);
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
		return queryListInfoBeanWithDBNameByRowHandler( rowhandler,beanType, null,sql, offset,pagesize,totalsize,bean);		 
	}
	
	public static ListInfo queryListInfoBeanByRowHandler(RowHandler rowhandler,Class<?> beanType, String sql, long offset,int pagesize,String totalsizesql,Object bean) throws SQLException
	{
		return queryListInfoBeanWithDBNameByRowHandler( rowhandler,beanType, null,sql, offset,pagesize,totalsizesql,bean);		 
	}
	public static ListInfo queryListInfoBeanByRowHandler(RowHandler rowhandler,Class<?> beanType, String sql, long offset,int pagesize,Object bean) throws SQLException
	{
		return queryListInfoBeanWithDBNameByRowHandler( rowhandler,beanType, null,sql, offset,pagesize,-1L,bean);		 
	}
	
	public static <T> T queryObjectBeanByRowHandler(RowHandler rowhandler,Class<T> beanType, String sql, Object bean) throws SQLException
	{
		return queryObjectBeanWithDBNameByRowHandler(rowhandler,beanType,null, sql, bean);
		
		 
	}
	
	public static <T> List<T> queryListBeanWithDBNameByRowHandler(RowHandler rowhandler,Class<T> beanType,String dbname, String sql, Object bean) throws SQLException
	{
		
//		PreparedDBUtil dbutil = new PreparedDBUtil();
//		SQLParams params = SQLParams.convertBeanToSqlParams(bean,  new SQLInfo(sql,true,false), dbname, PreparedDBUtil.SELECT, null);
//		dbutil.preparedSelect(params,dbname, sql);
//		
//		return dbutil.executePreparedForList(beanType,rowhandler);
		SQLInfo sqlinfo = SQLUtil.getGlobalSQLUtil().getSQLInfo(sql,true,false);
		return SQLInfoExecutor.queryListBeanWithDBNameByRowHandler( rowhandler, beanType, dbname,  sqlinfo,  bean);
	}
	
	
	public static <T> T queryObjectBeanWithDBNameByRowHandler(RowHandler rowhandler,Class<T> beanType,String dbname, String sql, Object bean) throws SQLException
	{
		
//		PreparedDBUtil dbutil = new PreparedDBUtil();
//		SQLParams params = SQLParams.convertBeanToSqlParams(bean,  new SQLInfo(sql,true,false), dbname, PreparedDBUtil.SELECT, null);
//		dbutil.preparedSelect(params,dbname, sql);		
//		return (T)dbutil.executePreparedForObject(beanType,rowhandler);
		
		SQLInfo sqlinfo = SQLUtil.getGlobalSQLUtil().getSQLInfo(sql,true,false);		
		return SQLInfoExecutor.queryObjectBeanWithDBNameByRowHandler( rowhandler,beanType,dbname, sqlinfo, bean);
	}
	
	
	
	
	
	public static void queryBeanByNullRowHandler(NullRowHandler rowhandler, String sql, Object bean) throws SQLException
	{
		
		 queryBeanWithDBNameByNullRowHandler( rowhandler,null, sql, bean); 
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
		SQLInfo sqlinfo = SQLUtil.getGlobalSQLUtil().getSQLInfo(sql,true,false);
		return SQLInfoExecutor.queryListInfoBeanWithDBNameByNullRowHandler( rowhandler, dbname,  sqlinfo,  offset, pagesize, totalsize, bean);
	}
	
	public static ListInfo queryListInfoBeanWithDBNameByNullRowHandler(NullRowHandler rowhandler,String dbname, String sql, long offset,int pagesize,String totalsizesql,Object bean) throws SQLException
	{
		
		SQLInfo sqlinfo = SQLUtil.getGlobalSQLUtil().getSQLInfo(sql,true,false);
		SQLInfo totalsizesqlsqlinfo = SQLUtil.getGlobalSQLUtil().getSQLInfo(totalsizesql,true,false);
		return SQLInfoExecutor.queryListInfoBeanWithDBNameByNullRowHandler(  rowhandler, dbname,  sqlinfo,  offset, pagesize, totalsizesqlsqlinfo, bean);	 
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
		
		SQLInfo sqlinfo = SQLUtil.getGlobalSQLUtil().getSQLInfo(sql,true,false);
		return SQLInfoExecutor.queryListInfoBeanWithDBNameByNullRowHandler( rowhandler, dbname,  sqlinfo,  offset, pagesize, bean);	 
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
		return queryListInfoBeanWithDBNameByNullRowHandler( rowhandler, null,sql, offset,pagesize,totalsize,bean);		 
	}
	
	public static ListInfo queryListInfoBeanByNullRowHandler(NullRowHandler rowhandler, String sql, long offset,int pagesize,String totalsizesql,Object bean) throws SQLException
	{
		return queryListInfoBeanWithDBNameByNullRowHandler( rowhandler, null,sql, offset,pagesize,totalsizesql,bean);		 
	}
	
	public static ListInfo queryListInfoBeanByNullRowHandler(NullRowHandler rowhandler, String sql, long offset,int pagesize,Object bean) throws SQLException
	{
		return queryListInfoBeanWithDBNameByNullRowHandler( rowhandler, null,sql, offset,pagesize,-1L,bean);		 
	}
	
	public static void queryBeanWithDBNameByNullRowHandler(NullRowHandler rowhandler,String dbname, String sql, Object bean) throws SQLException
	{
		
//		PreparedDBUtil dbutil = new PreparedDBUtil();
//		SQLParams params = SQLParams.convertBeanToSqlParams(bean,  new SQLInfo(sql,true,false), dbname, PreparedDBUtil.SELECT, null);
//		dbutil.preparedSelect(params,dbname, sql);
//		 dbutil.executePreparedWithRowHandler(rowhandler);
		SQLInfo sqlinfo = SQLUtil.getGlobalSQLUtil().getSQLInfo(sql,true,false);
		SQLInfoExecutor.queryBeanWithDBNameByNullRowHandler( rowhandler, dbname,  sqlinfo,  bean);
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
	public static ListInfo moreListInfoWithDBNameByRowHandler(RowHandler rowhandler,Class<?> beanType,String dbname, String sql, long offset,int pagesize,Object... fields) throws SQLException
	{
		SQLInfo sqlinfo = SQLUtil.getGlobalSQLUtil().getSQLInfo(sql,false,false);		
//		return SQLInfoExecutor.queryListInfoWithDBNameByRowHandler(  rowhandler, beanType, dbname, sqlinfo, offset,pagesize,fields);
		
//		SQLInfo sql = getSqlInfo(dbname, sqlname);
		return SQLInfoExecutor.moreListInfoWithDBNameByRowHandler(rowhandler,beanType,dbname, sqlinfo, offset,pagesize,fields);  	  
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
	public static ListInfo moreListInfoWithDBNameByNullRowHandler(NullRowHandler rowhandler,String dbname, String sql, long offset,int pagesize,Object... fields) throws SQLException
	{
		SQLInfo sqlinfo = SQLUtil.getGlobalSQLUtil().getSQLInfo(sql,false,false);
		
//		return SQLInfoExecutor.queryListInfoWithDBNameByNullRowHandler(   rowhandler, dbname,  sqlinfo,  offset, pagesize, fields);
//		SQLInfo sql = getSqlInfo(dbname, sqlname);
		return SQLInfoExecutor.moreListInfoWithDBNameByNullRowHandler(  rowhandler,dbname, sqlinfo, offset,pagesize,fields);  
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
	public static ListInfo moreListInfoWithDBName(Class<?> beanType,String dbname, String sql, long offset,int pagesize,Object... fields) throws SQLException
	{
		SQLInfo sqlinfo = SQLUtil.getGlobalSQLUtil().getSQLInfo(sql,false,false);
//		return SQLInfoExecutor.queryListInfoWithDBName(  beanType, dbname,  sqlinfo,  offset, pagesize, fields);
//		SQLInfo sql = getSqlInfo(dbname, sqlname);
		return SQLInfoExecutor.moreListInfoWithDBName(beanType,dbname, sqlinfo, offset,pagesize,fields);  	 
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
		return moreListInfoWithDBNameByRowHandler( rowhandler,beanType, null,sql, offset,pagesize,fields);		 
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
	public static ListInfo moreListInfoByNullRowHandler(NullRowHandler rowhandler, String sqlname, long offset,int pagesize,Object... fields) throws SQLException
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
	public static ListInfo moreListInfoBeanWithDBNameByRowHandler(RowHandler rowhandler,Class<?> beanType,String dbname, String sql, long offset,int pagesize,Object  bean) throws SQLException
	{
		SQLInfo sqlinfo = SQLUtil.getGlobalSQLUtil().getSQLInfo(sql,true,false);
//		return SQLInfoExecutor.queryListInfoBeanWithDBNameByRowHandler(  rowhandler,beanType,dbname, sqlinfo, offset,pagesize,bean);
//		SQLInfo sql = getSqlInfo(dbname, sqlname);
		return SQLInfoExecutor.moreListInfoBeanWithDBNameByRowHandler(  rowhandler,beanType,dbname, sqlinfo, offset,pagesize,bean);  
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
	public static ListInfo moreListInfoBeanWithDBNameByNullRowHandler(NullRowHandler rowhandler,String dbname, String sql, long offset,int pagesize,Object bean) throws SQLException
	{
		SQLInfo sqlinfo = SQLUtil.getGlobalSQLUtil().getSQLInfo(sql,true,false);
//		return SQLInfoExecutor.queryListInfoBeanWithDBNameByNullRowHandler( rowhandler, dbname,  sqlinfo,  offset, pagesize, bean);	 
//		SQLInfo sql = getSqlInfo(dbname, sqlname);
		return SQLInfoExecutor.moreListInfoBeanWithDBNameByNullRowHandler(  rowhandler, dbname, sqlinfo, offset,pagesize,bean);  	 
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
	public static ListInfo moreListInfoBeanWithDBName(Class<?> beanType,String dbname, String sql, long offset,int pagesize,Object bean) throws SQLException
	{
		SQLInfo sqlinfo = SQLUtil.getGlobalSQLUtil().getSQLInfo(sql,true,false);
//		return SQLInfoExecutor.queryListInfoBeanWithDBName( beanType, dbname,  sqlinfo,  offset, pagesize,  bean);
//		SQLInfo sql = getSqlInfo(dbname, sqlname);
		return SQLInfoExecutor.moreListInfoBeanWithDBName(  beanType, dbname,  sqlinfo,  offset, pagesize, bean); 
	}
	
		public static ListInfo moreListInfoBeanByRowHandler(RowHandler rowhandler,Class<?> beanType, String sql, long offset,int pagesize,Object bean) throws SQLException
	{
			SQLInfo sqlinfo = SQLUtil.getGlobalSQLUtil().getSQLInfo(sql,true,false);
//			return SQLInfoExecutor.queryListInfoBeanWithDBNameByRowHandler(  rowhandler,beanType,dbname, sqlinfo, offset,pagesize,totalsize,bean);	 
			return SQLInfoExecutor.moreListInfoBeanWithDBNameByRowHandler(  rowhandler,beanType,(String)null, sqlinfo, offset,pagesize,bean);  		 
	}
	
		public static ListInfo moreListInfoBeanByNullRowHandler(NullRowHandler rowhandler, String sql, long offset,int pagesize,Object bean) throws SQLException
	{
			
			SQLInfo sqlinfo = SQLUtil.getGlobalSQLUtil().getSQLInfo(sql,true,false);
//			return SQLInfoExecutor.queryListInfoBeanWithDBNameByNullRowHandler( rowhandler, dbname,  sqlinfo,  offset, pagesize, totalsize, bean);
//			SQLInfo sql = getSqlInfo(null, sqlname);
			return SQLInfoExecutor.moreListInfoBeanWithDBNameByNullRowHandler(  rowhandler, (String)null, sqlinfo, offset,pagesize,bean);  	
	}
	
		public static ListInfo moreListInfoBean(Class<?> beanType, String sql, long offset,int pagesize,Object bean) throws SQLException
	{
			SQLInfo sqlinfo = SQLUtil.getGlobalSQLUtil().getSQLInfo(sql,true,false);
//			return SQLInfoExecutor.queryListInfoBeanWithDBName( beanType, dbname,  sqlinfo,  offset, pagesize, totalsize, bean);
//			SQLInfo sql = getSqlInfo(null, sqlname);
			return SQLInfoExecutor.moreListInfoBeanWithDBName(  beanType, (String)null,  sqlinfo,  offset, pagesize, bean); 
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
	public static ListInfo moreListInfo(Class<?> beanType, String sqlname, long offset,int pagesize,Object... fields) throws SQLException
	{
		return moreListInfoWithDBName(beanType, null,sqlname, offset,pagesize,fields);		 
	}
	
	
}
