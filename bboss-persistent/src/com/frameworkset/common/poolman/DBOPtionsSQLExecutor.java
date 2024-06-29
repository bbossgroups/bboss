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

import java.sql.SQLException;
import java.util.ArrayList;
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
public class DBOPtionsSQLExecutor
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
    
    public Object execute(DBOptions dbOptions) throws SQLException
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
	        return dbutil.executePrepared(dbOptions);
    	}
    	else
    	{
    		if(this.batchtype.equals(BATCH_PREPARED))
    		{
    			PreparedDBUtil dbutil = new PreparedDBUtil();
    			dbutil.setBatchOptimize(isBatchOptimize());
    			dbutil.setPrepareDBName(batchDBName);
    			dbutil.addPreparedBatch(new ListSQLParams(batchsqlparams,null));
    			dbutil.executePreparedBatch(dbOptions);
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
	
	
	public static void insertBeans(DBOptions dbOptions,String dbname, String sql, List beans) throws SQLException {
		
		if(beans == null || beans.size() == 0)
			return ;
//		execute( dbname,  sql,  beans,PreparedDBUtil.INSERT,(GetCUDResult)null);
		SQLInfo sqlinfo = SQLUtil.getGlobalSQLUtil().getSQLInfo(sql,true,true);
		SQLInfoExecutor.insertBeans(dbOptions,dbname,sqlinfo, beans);
	}
	
	public static void insertBeans(DBOptions dbOptions,String dbname, String sql, List beans,GetCUDResult getCUDResult) throws SQLException {
		
		if(beans == null || beans.size() == 0)
			return ;
//		execute( dbname,  sql,  beans,PreparedDBUtil.INSERT,getCUDResult);
		SQLInfo sqlinfo = SQLUtil.getGlobalSQLUtil().getSQLInfo(sql,true,true);
		SQLInfoExecutor.insertBeans(dbOptions,dbname,sqlinfo, beans,getCUDResult);
	}
	

	
	public static Object update( DBOptions dbOptions,String sql, Object... fields) throws SQLException {
//		return execute(null, sql,PreparedDBUtil.UPDATE, fields);
//		SQLInfo sqlinfo = SQLUtil.getGlobalSQLUtil().getSQLInfo(sql,false,false);
        SQLInfo sqlinfo = SQLUtil.getGlobalSQLUtil().getPureSQLInfo(sql);	
		return SQLInfoExecutor.update( dbOptions,  sqlinfo,fields);
	}
	



	public static Object delete(DBOptions dbOptions,String sql, Object... fields) throws SQLException {
//		SQLInfo sqlinfo = SQLUtil.getGlobalSQLUtil().getSQLInfo(sql,false,false);
        SQLInfo sqlinfo = SQLUtil.getGlobalSQLUtil().getPureSQLInfo(sql);
		return SQLInfoExecutor.delete(dbOptions,sqlinfo, fields);
		
	}

	
	public static void deleteByKeys(DBOptions dbOptions,String sql, int... fields) throws SQLException {
        SQLInfo sqlinfo = SQLUtil.getGlobalSQLUtil().getPureSQLInfo(sql);
		SQLInfoExecutor.deleteByKeys(dbOptions,sqlinfo, fields);
		
	}
	public static void deleteByKeysWithDBName(DBOptions dbOptions,String dbname,String sql, int... fields) throws SQLException {
//		executeBatch(dbname, sql,PreparedDBUtil.DELETE, fields);
//		SQLInfo sqlinfo = SQLUtil.getGlobalSQLUtil().getSQLInfo(sql,false,false);
        SQLInfo sqlinfo = SQLUtil.getGlobalSQLUtil().getPureSQLInfo(sql);
		SQLInfoExecutor.deleteByKeysWithDBName(dbOptions,dbname,sqlinfo, fields);
		
	}
	
	public static void deleteByLongKeys(DBOptions dbOptions,String sql, long... fields) throws SQLException {
//		executeBatch(null, sql,PreparedDBUtil.DELETE, fields);
//		SQLInfo sqlinfo = SQLUtil.getGlobalSQLUtil().getSQLInfo(sql,false,false);
        SQLInfo sqlinfo = SQLUtil.getGlobalSQLUtil().getPureSQLInfo(sql);
		SQLInfoExecutor.deleteByLongKeys(dbOptions,sqlinfo, fields);
		
	}
	public static void deleteByLongKeysWithDBName(DBOptions dbOptions,String dbname,String sql, long... fields) throws SQLException {
//		executeBatch(dbname, sql,PreparedDBUtil.DELETE, fields);
//		SQLInfo sqlinfo = SQLUtil.getGlobalSQLUtil().getSQLInfo(sql,false,false);
        SQLInfo sqlinfo = SQLUtil.getGlobalSQLUtil().getPureSQLInfo(sql);
		SQLInfoExecutor.deleteByLongKeysWithDBName(dbOptions,dbname,sqlinfo, fields);
		
	}
	
	public static <T> void executeBatch(DBOptions dbOptions,String sql,List<T> datas,int batchsize, BatchHandler<T> batchHandler) throws SQLException{
		executeBatch(  dbOptions,(String)null,sql,datas,batchsize, batchHandler);
	}
	
	public static <T> void executeBatch(DBOptions dbOptions,String dbname,String sql,List<T> datas,int batchsize, BatchHandler<T> batchHandler) throws SQLException{
//		SQLInfo sqlinfo = SQLUtil.getGlobalSQLUtil().getSQLInfo(sql,false,false);
        SQLInfo sqlinfo = SQLUtil.getGlobalSQLUtil().getPureSQLInfo(sql);
		SQLInfoExecutor.executeBatch(dbOptions,  dbname,  sqlinfo,datas,batchsize,batchHandler) ;
	}
	
	
	public static void updateByKeys(DBOptions dbOptions,String sql, int... fields) throws SQLException {
//		executeBatch(null, sql,PreparedDBUtil.DELETE, fields);
//		SQLInfo sqlinfo = SQLUtil.getGlobalSQLUtil().getSQLInfo(sql,false,false);
        SQLInfo sqlinfo = SQLUtil.getGlobalSQLUtil().getPureSQLInfo(sql);
		SQLInfoExecutor.updateByKeys(dbOptions,sqlinfo, fields);
		
	}
	public static void updateByKeysWithDBName(DBOptions dbOptions,String dbname,String sql, int... fields) throws SQLException {
//		executeBatch(dbname, sql,PreparedDBUtil.DELETE, fields);
//		SQLInfo sqlinfo = SQLUtil.getGlobalSQLUtil().getSQLInfo(sql,false,false);
        SQLInfo sqlinfo = SQLUtil.getGlobalSQLUtil().getPureSQLInfo(sql);
		SQLInfoExecutor.updateByKeysWithDBName(dbOptions,dbname,sqlinfo, fields);
		
	}
	
	public static void updateByLongKeys(DBOptions dbOptions,String sql, long... fields) throws SQLException {
//		executeBatch(null, sql,PreparedDBUtil.DELETE, fields);
//		SQLInfo sqlinfo = SQLUtil.getGlobalSQLUtil().getSQLInfo(sql,false,false);
        SQLInfo sqlinfo = SQLUtil.getGlobalSQLUtil().getPureSQLInfo(sql);
		SQLInfoExecutor.updateByLongKeys(dbOptions,sqlinfo, fields);
		
	}
	public static void updateByLongKeysWithDBName(DBOptions dbOptions,String dbname,String sql, long... fields) throws SQLException {
//		executeBatch(dbname, sql,PreparedDBUtil.DELETE, fields);
//		SQLInfo sqlinfo = SQLUtil.getGlobalSQLUtil().getSQLInfo(sql,false,false);
        SQLInfo sqlinfo = SQLUtil.getGlobalSQLUtil().getPureSQLInfo(sql);
		SQLInfoExecutor.updateByLongKeysWithDBName(dbOptions,dbname,sqlinfo, fields);
		
	}
	
	public static void updateByKeys(DBOptions dbOptions,String sql, String... fields) throws SQLException {
//		executeBatch(null, sql,PreparedDBUtil.DELETE, fields);
//		SQLInfo sqlinfo = SQLUtil.getGlobalSQLUtil().getSQLInfo(sql,false,false);
        SQLInfo sqlinfo = SQLUtil.getGlobalSQLUtil().getPureSQLInfo(sql);
		SQLInfoExecutor.updateByKeys(dbOptions,sqlinfo, fields);
		
	}
	public static void updateByKeysWithDBName(DBOptions dbOptions,String dbname,String sql, String... fields) throws SQLException {
//		executeBatch(dbname, sql,PreparedDBUtil.DELETE, fields);
//		SQLInfo sqlinfo = SQLUtil.getGlobalSQLUtil().getSQLInfo(sql,false,false);
        SQLInfo sqlinfo = SQLUtil.getGlobalSQLUtil().getPureSQLInfo(sql);
		SQLInfoExecutor.updateByKeysWithDBName(dbOptions,dbname,sqlinfo, fields);
		
	}
	
	public static void deleteByKeys(DBOptions dbOptions,String sql, String... fields) throws SQLException {
//		executeBatch(null, sql,PreparedDBUtil.DELETE, fields);
//		SQLInfo sqlinfo = SQLUtil.getGlobalSQLUtil().getSQLInfo(sql,false,false);
        SQLInfo sqlinfo = SQLUtil.getGlobalSQLUtil().getPureSQLInfo(sql);
		SQLInfoExecutor.deleteByKeys(dbOptions,sqlinfo, fields);
		
	}
	public static void deleteByKeysWithDBName(DBOptions dbOptions,String dbname,String sql, String... fields) throws SQLException {
//		executeBatch(dbname, sql,PreparedDBUtil.DELETE, fields);
//		SQLInfo sqlinfo = SQLUtil.getGlobalSQLUtil().getSQLInfo(sql,false,false);
        SQLInfo sqlinfo = SQLUtil.getGlobalSQLUtil().getPureSQLInfo(sql);
		SQLInfoExecutor.deleteByKeysWithDBName(dbOptions,dbname,sqlinfo, fields);
		
	}
	
	
	public static void deleteByShortKeys(DBOptions dbOptions,String sql, short... fields) throws SQLException {
//		executeBatch(null, sql,PreparedDBUtil.DELETE, fields);
//		SQLInfo sqlinfo = SQLUtil.getGlobalSQLUtil().getSQLInfo(sql,false,false);
        SQLInfo sqlinfo = SQLUtil.getGlobalSQLUtil().getPureSQLInfo(sql);
		SQLInfoExecutor.deleteByShortKeys(dbOptions,sqlinfo, fields);
		
	}
	public static void deleteByShortKeysWithDBName(DBOptions dbOptions,String dbname,String sql, short... fields) throws SQLException {
//		executeBatch(dbname, sql,PreparedDBUtil.DELETE, fields);
//		SQLInfo sqlinfo = SQLUtil.getGlobalSQLUtil().getSQLInfo(sql,false,false);
        SQLInfo sqlinfo = SQLUtil.getGlobalSQLUtil().getPureSQLInfo(sql);
		SQLInfoExecutor.deleteByShortKeysWithDBName(dbOptions,dbname,sqlinfo, fields);
	}
	
	public static void updateByShortKeys(DBOptions dbOptions,String sql, short... fields) throws SQLException {
//		executeBatch(null, sql,PreparedDBUtil.DELETE, fields);
//		SQLInfo sqlinfo = SQLUtil.getGlobalSQLUtil().getSQLInfo(sql,false,false);
        SQLInfo sqlinfo = SQLUtil.getGlobalSQLUtil().getPureSQLInfo(sql);
		SQLInfoExecutor.updateByShortKeys(dbOptions,sqlinfo, fields);
		
	}
	public static void updateByShortKeysWithDBName(DBOptions dbOptions,String dbname,String sql, short... fields) throws SQLException {
//		executeBatch(dbname, sql,PreparedDBUtil.DELETE, fields);
//		SQLInfo sqlinfo = SQLUtil.getGlobalSQLUtil().getSQLInfo(sql,false,false);
        SQLInfo sqlinfo = SQLUtil.getGlobalSQLUtil().getPureSQLInfo(sql);
		SQLInfoExecutor.updateByShortKeysWithDBName(dbOptions,dbname,sqlinfo, fields);
	}



	public static Object insert(DBOptions dbOptions,String sql, Object... fields) throws SQLException {
//		return execute(null, sql,PreparedDBUtil.INSERT, fields);
//		SQLInfo sqlinfo = SQLUtil.getGlobalSQLUtil().getSQLInfo(sql,false,false);
        SQLInfo sqlinfo = SQLUtil.getGlobalSQLUtil().getPureSQLInfo(sql);
		return SQLInfoExecutor.insert(dbOptions,sqlinfo, fields);
	}
	
	public static Object updateWithDBName(DBOptions dbOptions,String dbname, String sql, Object... fields) throws SQLException {
//		return execute(dbname, sql,PreparedDBUtil.UPDATE, fields);
//		SQLInfo sqlinfo = SQLUtil.getGlobalSQLUtil().getSQLInfo(sql,false,false);
        SQLInfo sqlinfo = SQLUtil.getGlobalSQLUtil().getPureSQLInfo(sql);        
		return SQLInfoExecutor.updateWithDBName( dbOptions, dbname,  sqlinfo,  fields);
	}
	
	public static Object deleteWithDBName(DBOptions dbOptions,String dbname, String sql, Object... fields) throws SQLException {
//		return execute(dbname, sql,PreparedDBUtil.DELETE, fields);
//		SQLInfo sqlinfo = SQLUtil.getGlobalSQLUtil().getSQLInfo(sql,false,false);
        SQLInfo sqlinfo = SQLUtil.getGlobalSQLUtil().getPureSQLInfo(sql);
		return SQLInfoExecutor.deleteWithDBName(dbOptions,dbname,sqlinfo, fields);
		
	}



	public static Object insertWithDBName(DBOptions dbOptions,String dbname, String sql, Object... fields) throws SQLException {
//		return execute(dbname, sql,PreparedDBUtil.INSERT, fields);
//		SQLInfo sqlinfo = SQLUtil.getGlobalSQLUtil().getSQLInfo(sql,false,false);
        SQLInfo sqlinfo = SQLUtil.getGlobalSQLUtil().getPureSQLInfo(sql);
		return SQLInfoExecutor.insertWithDBName(dbOptions,dbname,sqlinfo, fields);
	}

	public static void updateBeans(DBOptions dbOptions,String dbname, String sql, List beans) throws SQLException {
		if(beans == null || beans.size() == 0)
			return ;
//		execute( dbname,  sql,  beans,PreparedDBUtil.UPDATE,(GetCUDResult)null);
		SQLInfo sqlinfo = SQLUtil.getGlobalSQLUtil().getSQLInfo(sql,true,true);		
		SQLInfoExecutor.updateBeans(dbOptions,  dbname,  sqlinfo,  beans);
	}
	
	public static void updateBeans(DBOptions dbOptions,String dbname, String sql, List beans,GetCUDResult GetCUDResult) throws SQLException {
		if(beans == null || beans.size() == 0)
			return ;
//		execute( dbname,  sql,  beans,PreparedDBUtil.UPDATE,GetCUDResult);
		SQLInfo sqlinfo = SQLUtil.getGlobalSQLUtil().getSQLInfo(sql,true,true);		
		SQLInfoExecutor.updateBeans( dbOptions, dbname,  sqlinfo,  beans,GetCUDResult);
	}



	public static void deleteBeans(DBOptions dbOptions,String dbname, String sql, List beans) throws SQLException {
		if(beans == null || beans.size() == 0)
			return ;
		
		SQLInfo sqlinfo = SQLUtil.getGlobalSQLUtil().getSQLInfo(sql,true,true);
		SQLInfoExecutor.deleteBeans(dbOptions,dbname, sqlinfo, beans);
//		execute( dbname,  sql,  beans,PreparedDBUtil.DELETE);
		
	}
	
	public static void deleteBeans(DBOptions dbOptions,String dbname, String sql, List beans,GetCUDResult GetCUDResult) throws SQLException {
		if(beans == null || beans.size() == 0)
			return ;
//		execute( dbname,  sql,  beans,PreparedDBUtil.DELETE, GetCUDResult);
		SQLInfo sqlinfo = SQLUtil.getGlobalSQLUtil().getSQLInfo(sql,true,true);
		SQLInfoExecutor.deleteBeans(dbOptions,dbname, sqlinfo, beans,GetCUDResult);
		
	}



	public static void insertBean(DBOptions dbOptions,String dbname, String sql, Object bean) throws SQLException {
		if(bean == null)
			return ;
//		List datas = new ArrayList();
//		datas.add(bean);
//		insertBeans( dbname,  sql,  datas);
//		CUDexecute(dbname, sql, bean,PreparedDBUtil.INSERT,false);
		SQLInfo sqlinfo = SQLUtil.getGlobalSQLUtil().getSQLInfo(sql,true,false);
		SQLInfoExecutor.insertBean(dbOptions,dbname,sqlinfo, bean);
	}
	
	public static void insertBean(DBOptions dbOptions,String dbname, String sql, Object bean,GetCUDResult getCUDResult) throws SQLException {
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
		SQLInfoExecutor.insertBean(dbOptions,dbname,sqlinfo, bean,getCUDResult);
	}



	public static void updateBean(DBOptions dbOptions,String dbname, String sql, Object bean) throws SQLException {
		if(bean == null )
			return ;
//		List datas = new ArrayList();
//		datas.add(bean);
//		updateBeans( dbname,  sql,  datas);
//		CUDexecute(dbname, sql, bean,PreparedDBUtil.UPDATE,false);
		SQLInfo sqlinfo = SQLUtil.getGlobalSQLUtil().getSQLInfo(sql,true,false);		
		SQLInfoExecutor.updateBean(  dbOptions, dbname,  sqlinfo,  bean);
	
	}
	
	public static void updateBean(DBOptions dbOptions,String dbname, String sql, Object bean,GetCUDResult getCUDResult) throws SQLException {
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
		SQLInfoExecutor.updateBean( dbOptions, dbname,  sqlinfo,  bean, getCUDResult);
	}
	
	public static void updateBean(DBOptions dbOptions, String sql, Object bean,GetCUDResult getCUDResult) throws SQLException {
//		if(bean == null )
//			return ;
//		List datas = new ArrayList();
//		datas.add(bean);
//		updateBeans( null,  sql,  datas);
		updateBean(  dbOptions,(String)null,  sql,  bean, getCUDResult);
	}

	public static void deleteBean(DBOptions dbOptions, String sql, Object bean,GetCUDResult getCUDResult) throws SQLException {
		deleteBean(  dbOptions,(String )null,sql, bean,getCUDResult) ;
	}

	public static void deleteBean(DBOptions dbOptions,String dbname, String sql, Object bean) throws SQLException {
		
		if(bean == null)
			return ;
//		List datas = new ArrayList();
//		datas.add(bean);
//		deleteBeans( dbname,  sql,  datas);
		SQLInfo sqlinfo = SQLUtil.getGlobalSQLUtil().getSQLInfo(sql,true,false);
		SQLInfoExecutor.deleteBean(dbOptions,dbname, sqlinfo, bean);
//		CUDexecute(dbname, sql, bean,PreparedDBUtil.DELETE,false);
	}
	public static void deleteBean(DBOptions dbOptions,String dbname, String sql, Object bean,GetCUDResult getCUDResult) throws SQLException {
		
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
		SQLInfoExecutor.deleteBean(dbOptions,dbname, sqlinfo, bean,getCUDResult);
	}
	
	public static void insertBeans(DBOptions dbOptions,String sql, List beans) throws SQLException {
		insertBeans( dbOptions,(String)null,sql, beans);
	}
	
	public static void insertBeans(DBOptions dbOptions,String sql, List beans,GetCUDResult getCUDResult) throws SQLException {
		insertBeans(   dbOptions,(String)null,sql, beans,getCUDResult);
	}
	
	



	public static void updateBeans( DBOptions dbOptions,String sql, List beans) throws SQLException {
		updateBeans(   dbOptions,(String)null,sql, beans);
	}



	public static void deleteBeans( DBOptions dbOptions,String sql, List beans) throws SQLException {
		deleteBeans(   dbOptions,(String)null,sql, beans);
		
	}



	public static void insertBean(DBOptions dbOptions, String sql, Object bean) throws SQLException {
//		if(bean == null)
//			return ;
//		List datas = new ArrayList();
//		datas.add(bean);
//		insertBeans( null,  sql,  datas);
		insertBean(   dbOptions,(String)null,sql, bean);
	}
	public static void insertBean( DBOptions dbOptions,String sql, Object bean,GetCUDResult getCUDResult) throws SQLException {
//		if(bean == null)
//			return ;
//		List datas = new ArrayList();
//		datas.add(bean);
//		insertBeans( null,  sql,  datas);
		insertBean(   dbOptions,(String)null,sql, bean,getCUDResult);
	}



	public static void updateBean( DBOptions dbOptions,String sql, Object bean) throws SQLException {
//		if(bean == null )
//			return ;
//		List datas = new ArrayList();
//		datas.add(bean);
//		updateBeans( null,  sql,  datas);
		updateBean(   dbOptions,(String)null,sql, bean);
	}

	

	public static void deleteBean(DBOptions dbOptions,String sql, Object bean) throws SQLException {
		
//		if(bean == null)
//			return ;
//		List datas = new ArrayList();
//		datas.add(bean);
//		deleteBeans( null,  sql,  datas);
		deleteBean(  dbOptions,(String)null,sql, bean);
		
	}
	
	
	public static <T> List<T> queryList(DBOptions dbOptions,Class<T> beanType, String sql, Object... fields) throws SQLException
	{
		
		return queryListWithDBName(  dbOptions,beanType,(String)null, sql, fields);
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
	public static ListInfo queryListInfoWithDBName(DBOptions dbOptions,Class<?> beanType,String dbname, String sql, long offset,int pagesize,Object... fields) throws SQLException
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
//		SQLInfo sqlinfo = SQLUtil.getGlobalSQLUtil().getSQLInfo(sql,false,false);
        SQLInfo sqlinfo = SQLUtil.getGlobalSQLUtil().getPureSQLInfo(sql);
		return SQLInfoExecutor.queryListInfoWithDBName( dbOptions, beanType, dbname,  sqlinfo,  offset, pagesize, fields);
	}
	public static ListInfo queryListInfoWithDBName2ndTotalsize(DBOptions dbOptions,Class<?> beanType,String dbname, String sql, long offset,int pagesize,long totalsize,Object... fields) throws SQLException
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
//		SQLInfo sqlinfo = SQLUtil.getGlobalSQLUtil().getSQLInfo(sql,false,false);
        SQLInfo sqlinfo = SQLUtil.getGlobalSQLUtil().getPureSQLInfo(sql);
		return SQLInfoExecutor.queryListInfoWithDBName2ndTotalsize( dbOptions, beanType, dbname,  sqlinfo,  offset, pagesize, totalsize, fields);
	}
	public static ListInfo queryListInfoWithDBName2ndTotalsizesql(DBOptions dbOptions,Class<?> beanType,String dbname, String sql, long offset,int pagesize,String totalsizesql,Object... fields) throws SQLException
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
//		SQLInfo sqlinfo = SQLUtil.getGlobalSQLUtil().getSQLInfo(sql,false,false);
        SQLInfo sqlinfo = SQLUtil.getGlobalSQLUtil().getPureSQLInfo(sql);
//		SQLInfo totalsizesqlinfo = SQLUtil.getGlobalSQLUtil().getSQLInfo(totalsizesql,false,false);
        SQLInfo totalsizesqlinfo = SQLUtil.getGlobalSQLUtil().getPureSQLInfo(totalsizesql);
		
		return SQLInfoExecutor.queryListInfoWithDBName2ndTotalsizesql( dbOptions,  beanType, dbname,  sqlinfo,  offset, pagesize, totalsizesqlinfo, fields);
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
	public static ListInfo queryListInfo(DBOptions dbOptions,Class<?> beanType, String sql, long offset,int pagesize,Object... fields) throws SQLException
	{
		return queryListInfoWithDBName(  dbOptions,beanType, (String)null,sql, offset,pagesize,fields);
	}
	
	public static ListInfo queryListInfoWithTotalsize(DBOptions dbOptions,Class<?> beanType, String sql, long offset,int pagesize,long totalsize,Object... fields) throws SQLException
	{
		return queryListInfoWithDBName2ndTotalsize(  dbOptions,beanType, (String)null,sql, offset,pagesize,totalsize,fields);
	}
	
	public static ListInfo queryListInfoWithTotalsizesql(DBOptions dbOptions,Class<?> beanType, String sql, long offset,int pagesize,String totalsizesql,Object... fields) throws SQLException
	{
		return queryListInfoWithDBName2ndTotalsizesql(  dbOptions,beanType, (String)null,sql, offset,pagesize,totalsizesql,fields);
	}
	
	
	public static <T> T queryObject(DBOptions dbOptions,Class<T> beanType, String sql, Object... fields) throws SQLException
	{
		return queryObjectWithDBName(dbOptions,beanType,(String)null, sql, fields);
		
		 
	}
	
	public static <T> List<T> queryListWithDBName(DBOptions dbOptions,Class<T> beanType,String dbname, String sql, Object... fields) throws SQLException
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
//		SQLInfo sqlinfo = SQLUtil.getGlobalSQLUtil().getSQLInfo(sql,false,false);		
        SQLInfo sqlinfo = SQLUtil.getGlobalSQLUtil().getPureSQLInfo(sql);
		return SQLInfoExecutor.queryListWithDBName(  dbOptions,beanType, dbname,  sqlinfo,  fields);
	}
	
	
	public static <T> T queryObjectWithDBName(DBOptions dbOptions,Class<T> beanType,String dbname, String sql, Object... fields) throws SQLException
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
//		SQLInfo sqlinfo = SQLUtil.getGlobalSQLUtil().getSQLInfo(sql,false,false);	
        SQLInfo sqlinfo = SQLUtil.getGlobalSQLUtil().getPureSQLInfo(sql);        
		return SQLInfoExecutor.queryObjectWithDBName(dbOptions, beanType, dbname,  sqlinfo,  fields);
		 
	}
	
	
	public static <T> List<T> queryListByRowHandler(DBOptions dbOptions,RowHandler rowhandler,Class<T> beanType, String sql, Object... fields) throws SQLException
	{
		
		return queryListWithDBNameByRowHandler(  dbOptions,rowhandler,beanType,(String)null, sql, fields);
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
	public static ListInfo queryListInfoWithDBNameByRowHandler(DBOptions dbOptions,RowHandler rowhandler,Class<?> beanType,String dbname, String sql, long offset,int pagesize,Object... fields) throws SQLException
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
//		SQLInfo sqlinfo = SQLUtil.getGlobalSQLUtil().getSQLInfo(sql,false,false);	
        SQLInfo sqlinfo = SQLUtil.getGlobalSQLUtil().getPureSQLInfo(sql);        
		return SQLInfoExecutor.queryListInfoWithDBNameByRowHandler( dbOptions, rowhandler, beanType, dbname, sqlinfo, offset,pagesize,fields);
	}
	
	public static ListInfo queryListInfoWithDBName2ndTotalsizeByRowHandler(DBOptions dbOptions,RowHandler rowhandler,Class<?> beanType,String dbname, String sql, long offset,int pagesize,long totalsize,Object... fields) throws SQLException
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
//		SQLInfo sqlinfo = SQLUtil.getGlobalSQLUtil().getSQLInfo(sql,false,false);
        SQLInfo sqlinfo = SQLUtil.getGlobalSQLUtil().getPureSQLInfo(sql);
		return SQLInfoExecutor.queryListInfoWithDBName2ndTotalsizeByRowHandler( dbOptions,  rowhandler, beanType, dbname,  sqlinfo,  offset, pagesize, totalsize,fields);
	}
	
	public static ListInfo queryListInfoWithDBName2ndTotalsizesqlByRowHandler(DBOptions dbOptions,RowHandler rowhandler,Class<?> beanType,String dbname, String sql, long offset,int pagesize,String totalsizesql,Object... fields) throws SQLException
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
//		SQLInfo sqlinfo = SQLUtil.getGlobalSQLUtil().getSQLInfo(sql,false,false);
        SQLInfo sqlinfo = SQLUtil.getGlobalSQLUtil().getPureSQLInfo(sql);
//		SQLInfo totalsizesqlinfo = SQLUtil.getGlobalSQLUtil().getSQLInfo(totalsizesql,false,false);
        SQLInfo totalsizesqlinfo = SQLUtil.getGlobalSQLUtil().getPureSQLInfo(totalsizesql);
		
		return SQLInfoExecutor.queryListInfoWithDBName2ndTotalsizesqlByRowHandler(  dbOptions, rowhandler, beanType, dbname,  sqlinfo,  offset, pagesize, totalsizesqlinfo, fields);
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
	public static ListInfo queryListInfoByRowHandler(DBOptions dbOptions,RowHandler rowhandler,Class<?> beanType, String sql, long offset,int pagesize,Object... fields) throws SQLException
	{
		return queryListInfoWithDBNameByRowHandler(  dbOptions, rowhandler,beanType, (String)null,sql, offset,pagesize,fields);
	}
	
	public static ListInfo queryListInfoWithTotalsizeByRowHandler(DBOptions dbOptions,RowHandler rowhandler,Class<?> beanType, String sql, long offset,int pagesize,long totalsize,Object... fields) throws SQLException
	{
		return queryListInfoWithDBName2ndTotalsizeByRowHandler(   dbOptions,rowhandler,beanType,(String) null,sql, offset,pagesize,totalsize,fields);
	}
	
	public static ListInfo queryListInfoWithTotalsizesqlByRowHandler(DBOptions dbOptions,RowHandler rowhandler,Class<?> beanType, String sql, long offset,int pagesize,String totalsizesql,Object... fields) throws SQLException
	{
		return queryListInfoWithDBName2ndTotalsizesqlByRowHandler(   dbOptions,rowhandler,beanType, (String)null,sql, offset,pagesize,totalsizesql,fields);
	}
	
	
	public static <T> T queryObjectByRowHandler(DBOptions dbOptions,RowHandler rowhandler,Class<T> beanType, String sql, Object... fields) throws SQLException
	{
		return queryObjectWithDBNameByRowHandler(  dbOptions,rowhandler,beanType,(String)null, sql, fields);
		
		 
	}
	
	public static <T> List<T> queryListWithDBNameByRowHandler(DBOptions dbOptions,RowHandler rowhandler,Class<T> beanType,String dbname, String sql, Object... fields) throws SQLException
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
//		SQLInfo sqlinfo = SQLUtil.getGlobalSQLUtil().getSQLInfo(sql,false,false);
        SQLInfo sqlinfo = SQLUtil.getGlobalSQLUtil().getPureSQLInfo(sql);		
		return SQLInfoExecutor.queryListWithDBNameByRowHandler(  dbOptions, rowhandler,beanType, dbname,  sqlinfo,  fields);
	}
	
	
	public static <T> T queryObjectWithDBNameByRowHandler(DBOptions dbOptions,RowHandler rowhandler,Class<T> beanType,String dbname, String sql, Object... fields) throws SQLException
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
//		SQLInfo sqlinfo = SQLUtil.getGlobalSQLUtil().getSQLInfo(sql,false,false);
        SQLInfo sqlinfo = SQLUtil.getGlobalSQLUtil().getPureSQLInfo(sql);	
		return SQLInfoExecutor.queryObjectWithDBNameByRowHandler( dbOptions, rowhandler, beanType, dbname,  sqlinfo,  fields);
		 
	}
	
	
	
	
	/**
	 * 采用Null行处理器的通用查询，适用于单个Object查询，List查询等等
	 * @param rowhandler
	 * @param sql
	 * @param fields
	 * @throws SQLException
	 */
	public static void queryByNullRowHandler(DBOptions dbOptions,NullRowHandler rowhandler, String sql, Object... fields) throws SQLException
	{
		
		 queryWithDBNameByNullRowHandler(  dbOptions, rowhandler,null, sql, fields);
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
	public static ListInfo queryListInfoWithDBNameByNullRowHandler(DBOptions dbOptions,NullRowHandler rowhandler,String dbname, String sql, long offset,int pagesize,Object... fields) throws SQLException
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
//		SQLInfo sqlinfo = SQLUtil.getGlobalSQLUtil().getSQLInfo(sql,false,false);
        SQLInfo sqlinfo = SQLUtil.getGlobalSQLUtil().getPureSQLInfo(sql);
		
		return SQLInfoExecutor.queryListInfoWithDBNameByNullRowHandler(   dbOptions,rowhandler, dbname,  sqlinfo,  offset, pagesize, fields);
	}
	public static ListInfo queryListInfoWithDBName2ndTotalsizeByNullRowHandler(DBOptions dbOptions,NullRowHandler rowhandler,String dbname, String sql, long offset,int pagesize,long totalsize,Object... fields) throws SQLException
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
//		SQLInfo sqlinfo = SQLUtil.getGlobalSQLUtil().getSQLInfo(sql,false,false);
        SQLInfo sqlinfo = SQLUtil.getGlobalSQLUtil().getPureSQLInfo(sql);
		return SQLInfoExecutor.queryListInfoWithDBName2ndTotalsizeByNullRowHandler(  dbOptions,rowhandler, dbname,  sqlinfo,  offset, pagesize, totalsize,fields);
	}
	public static ListInfo queryListInfoWithDBName2ndTotalsizesqlByNullRowHandler(DBOptions dbOptions,NullRowHandler rowhandler,String dbname, String sql, long offset,int pagesize,String totalsizesql,Object... fields) throws SQLException
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
//		SQLInfo sqlinfo = SQLUtil.getGlobalSQLUtil().getSQLInfo(sql,false,false);
        SQLInfo sqlinfo = SQLUtil.getGlobalSQLUtil().getPureSQLInfo(sql);
//		SQLInfo totalsizesqlinfo = SQLUtil.getGlobalSQLUtil().getSQLInfo(totalsizesql,false,false);
        SQLInfo totalsizesqlinfo = SQLUtil.getGlobalSQLUtil().getPureSQLInfo(totalsizesql);
		
		return SQLInfoExecutor.queryListInfoWithDBName2ndTotalsizesqlByNullRowHandler(   dbOptions,rowhandler, dbname,  sqlinfo,  offset, pagesize, totalsizesqlinfo, fields);
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
	public static ListInfo queryListInfoByNullRowHandler(DBOptions dbOptions,NullRowHandler rowhandler, String sql, long offset,int pagesize,Object... fields) throws SQLException
	{
		return queryListInfoWithDBNameByNullRowHandler(   dbOptions,rowhandler, (String)null,sql, offset,pagesize,fields);
	}
	public static ListInfo queryListInfoWithTotalsizeByNullRowHandler(DBOptions dbOptions,NullRowHandler rowhandler, String sql, long offset,int pagesize,long totalsize,Object... fields) throws SQLException
	{
		return queryListInfoWithDBName2ndTotalsizeByNullRowHandler(   dbOptions,rowhandler, null,sql, offset,pagesize,totalsize,fields);
	}
	public static ListInfo queryListInfoWithTotalsizesqlByNullRowHandler(DBOptions dbOptions,NullRowHandler rowhandler, String sql, long offset,int pagesize,String totalsizesql,Object... fields) throws SQLException
	{
		return queryListInfoWithDBName2ndTotalsizesqlByNullRowHandler(   dbOptions,rowhandler,(String) null,sql, offset,pagesize,totalsizesql,fields);
	}
	
	
	/**
	 * 采用Null行处理器的通用查询，适用于单个Object查询，List查询等等
	 * @param rowhandler
	 * @param dbname
	 * @param sql
	 * @param fields
	 * @throws SQLException
	 */
	public static void queryWithDBNameByNullRowHandler(DBOptions dbOptions,NullRowHandler rowhandler,String dbname, String sql, Object... fields) throws SQLException
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
//		SQLInfo sqlinfo = SQLUtil.getGlobalSQLUtil().getSQLInfo(sql,false,false);
        SQLInfo sqlinfo = SQLUtil.getGlobalSQLUtil().getPureSQLInfo(sql);		
		SQLInfoExecutor.queryWithDBNameByNullRowHandler( dbOptions,rowhandler, dbname,  sqlinfo,fields);
	}
	
	
	public static <T> List<T> queryListBean(DBOptions dbOptions,Class<T> beanType, String sql, Object bean) throws SQLException
	{
		
		return queryListBeanWithDBName(  dbOptions,beanType,(String)null, sql, bean);
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
	public static ListInfo queryListInfoBeanWithDBName(DBOptions dbOptions,Class<?> beanType,String dbname, String sql, long offset,int pagesize,long totalsize,Object bean) throws SQLException
	{
		
//		PreparedDBUtil dbutil = new PreparedDBUtil();
//		SQLParams params = SQLParams.convertBeanToSqlParams(bean, new SQLInfo(sql,true,false), dbname, PreparedDBUtil.SELECT, null);
//		dbutil.preparedSelect(params,dbname, sql,offset,pagesize,totalsize);
//		ListInfo datas = new ListInfo();
//		datas.setDatas(dbutil.executePreparedForList(beanType));
//		datas.setTotalSize(dbutil.getLongTotalSize());
//		return datas;
		SQLInfo sqlinfo = SQLUtil.getGlobalSQLUtil().getSQLInfo(sql,true,false);
		return SQLInfoExecutor.queryListInfoBeanWithDBName( dbOptions,beanType, dbname,  sqlinfo,  offset, pagesize, totalsize, bean);
	}
	
	public static ListInfo queryListInfoBeanWithDBName(DBOptions dbOptions,Class<?> beanType,String dbname, String sql, long offset,int pagesize,String totalsizesql,Object bean) throws SQLException
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
		return SQLInfoExecutor.queryListInfoBeanWithDBName(dbOptions,beanType, dbname,  sqlinfo,  offset, pagesize, totalsizesqlinfo, bean);
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
	public static ListInfo queryListInfoBeanWithDBName(DBOptions dbOptions,Class<?> beanType,String dbname, String sql, long offset,int pagesize,Object bean) throws SQLException
	{
		
//		PreparedDBUtil dbutil = new PreparedDBUtil();
//		SQLParams params = SQLParams.convertBeanToSqlParams(bean, new SQLInfo(sql,true,false), dbname, PreparedDBUtil.SELECT, null);
//		dbutil.preparedSelect(params,dbname, sql,offset,pagesize,-1L);
//		ListInfo datas = new ListInfo();
//		datas.setDatas(dbutil.executePreparedForList(beanType));
//		datas.setTotalSize(dbutil.getLongTotalSize());
//		return datas;
		SQLInfo sqlinfo = SQLUtil.getGlobalSQLUtil().getSQLInfo(sql,true,false);
		return SQLInfoExecutor.queryListInfoBeanWithDBName( dbOptions,beanType, dbname,  sqlinfo,  offset, pagesize,  bean);
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
	public static ListInfo queryListInfoBean(DBOptions dbOptions,Class<?> beanType, String sql, long offset,int pagesize,long totalsize,Object bean) throws SQLException
	{
		return queryListInfoBeanWithDBName(  dbOptions,beanType, (String)null,sql, offset,pagesize,totalsize,bean);
	}
	
	public static ListInfo queryListInfoBean(DBOptions dbOptions,Class<?> beanType, String sql, long offset,int pagesize,String totalsizesql,Object bean) throws SQLException
	{
		return queryListInfoBeanWithDBName(  dbOptions,beanType, (String)null,sql, offset,pagesize,totalsizesql,bean);
	}
	
	public static ListInfo queryListInfoBean(DBOptions dbOptions,Class<?> beanType, String sql, long offset,int pagesize,Object bean) throws SQLException
	{
		return queryListInfoBeanWithDBName(  dbOptions,beanType, (String)null,sql, offset,pagesize,-1L,bean);
	}
	
	public static String queryField( DBOptions dbOptions,String sql, Object... fields) throws SQLException
	{
		return queryFieldWithDBName(  dbOptions,(String)null, sql, fields);
	}
	public static String queryFieldBean( DBOptions dbOptions,String sql, Object bean) throws SQLException
	{
		return queryFieldBeanWithDBName(  dbOptions,(String)null, sql, bean);
	}
	
	public static String queryFieldBeanWithDBName(DBOptions dbOptions,String dbname, String sql, Object bean) throws SQLException
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
		return SQLInfoExecutor.queryFieldBeanWithDBName(  dbOptions,dbname,  sqlinfo,  bean);
	}
	
	public static String queryFieldWithDBName(DBOptions dbOptions,String dbname, String sql, Object... fields) throws SQLException
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
//		SQLInfo sqlinfo = SQLUtil.getGlobalSQLUtil().getSQLInfo(sql,false,false);

        SQLInfo sqlinfo = SQLUtil.getGlobalSQLUtil().getPureSQLInfo(sql);
		return SQLInfoExecutor.queryFieldWithDBName(  dbOptions,dbname,  sqlinfo,  fields);
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
	
	
	public static <T> T queryTField( DBOptions dbOptions,Class<T> type,String sql, Object... fields) throws SQLException
	{
		return queryTFieldWithDBName(dbOptions,(String)null, type,sql, fields);
	}
	public static <T> T queryTFieldBean( DBOptions dbOptions,Class<T> type,String sql, Object bean) throws SQLException
	{
		return queryTFieldBeanWithDBName( dbOptions,(String)null, type,sql, bean);
	}
	
	public static <T> T queryTFieldBeanWithDBName(DBOptions dbOptions,String dbname, Class<T> type,String sql, Object bean) throws SQLException
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
		return queryTFieldBeanWithDBName(dbOptions,dbname, type,(FieldRowHandler<T>)null,sql, bean) ;
	}
	
	public static <T> T queryTFieldWithDBName(DBOptions dbOptions,String dbname, Class<T> type,String sql, Object... fields) throws SQLException
	{
		
		return queryTFieldWithDBName(  dbOptions,dbname, type,(FieldRowHandler<T>)null,sql, fields);
	}
	
	public static <T> T queryTField(DBOptions dbOptions, Class<T> type,FieldRowHandler<T> fieldRowHandler,String sql, Object... fields) throws SQLException
	{
		return queryTFieldWithDBName(dbOptions,(String)null, type,fieldRowHandler,sql, fields);
	}
	public static <T> T queryTFieldBean(DBOptions dbOptions, Class<T> type,FieldRowHandler<T> fieldRowHandler,String sql, Object bean) throws SQLException
	{
		return queryTFieldBeanWithDBName(dbOptions,(String)null, type,fieldRowHandler,sql, bean);
	}
	
	public static <T> T queryTFieldBeanWithDBName(DBOptions dbOptions,String dbname, Class<T> type,FieldRowHandler<T> fieldRowHandler,String sql, Object bean) throws SQLException
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
		return SQLInfoExecutor.queryTFieldBeanWithDBName(  dbOptions,dbname, type,fieldRowHandler,sqlinfo, bean);
		 
	}
	
	public static <T> T queryTFieldWithDBName(DBOptions dbOptions,String dbname, Class<T> type,FieldRowHandler<T> fieldRowHandler,String sql, Object... fields) throws SQLException
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
//		SQLInfo sqlinfo = SQLUtil.getGlobalSQLUtil().getSQLInfo(sql,false,false);
        SQLInfo sqlinfo = SQLUtil.getGlobalSQLUtil().getPureSQLInfo(sql);
		return SQLInfoExecutor.queryTFieldWithDBName(  dbOptions,dbname, type,fieldRowHandler,sqlinfo, fields);
	}
	
	
	public static <T> T queryObjectBean(DBOptions dbOptions,Class<T> beanType, String sql, Object bean) throws SQLException
	{
		return queryObjectBeanWithDBName(  dbOptions,beanType,(String)null, sql, bean);
		
		 
	}
	
	public static <T> List<T> queryListBeanWithDBName(DBOptions dbOptions,Class<T> beanType,String dbname, String sql, Object bean) throws SQLException
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
		return SQLInfoExecutor.queryListBeanWithDBName(dbOptions,beanType, dbname,  sqlinfo,  bean);
	}
	
	public static <T> T queryObjectBeanWithDBName(DBOptions dbOptions,Class<T> beanType,String dbname, String sql, Object bean) throws SQLException
	{
		
//		PreparedDBUtil dbutil = new PreparedDBUtil();
//		SQLParams params = SQLParams.convertBeanToSqlParams(bean,  new SQLInfo(sql,true,false), dbname, PreparedDBUtil.SELECT, null);
//		dbutil.preparedSelect(params,dbname, sql);
//		return (T)dbutil.executePreparedForObject(beanType);
		SQLInfo sqlinfo = SQLUtil.getGlobalSQLUtil().getSQLInfo(sql,true,false);		
		return SQLInfoExecutor.queryObjectBeanWithDBName(dbOptions, beanType, dbname,  sqlinfo, bean);
		 
	}
	
	
	public static <T> List<T> queryListBeanByRowHandler(DBOptions dbOptions,RowHandler rowhandler,Class<T> beanType, String sql, Object bean) throws SQLException
	{
		
		return queryListBeanWithDBNameByRowHandler(  dbOptions,rowhandler,beanType,(String)null, sql, bean);
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
	public static ListInfo queryListInfoBeanWithDBNameByRowHandler(DBOptions dbOptions,RowHandler rowhandler,Class<?> beanType,String dbname, String sql, long offset,int pagesize,long totalsize,Object  bean) throws SQLException
	{	
		SQLInfo sqlinfo = SQLUtil.getGlobalSQLUtil().getSQLInfo(sql,true,false);
		return SQLInfoExecutor.queryListInfoBeanWithDBNameByRowHandler(dbOptions,  rowhandler,beanType,dbname, sqlinfo, offset,pagesize,totalsize,bean);
	}
	
	public static ListInfo queryListInfoBeanWithDBNameByRowHandler(DBOptions dbOptions,RowHandler rowhandler,Class<?> beanType,String dbname, String sql, long offset,int pagesize,String totalsizesql,Object  bean) throws SQLException
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
		return SQLInfoExecutor.queryListInfoBeanWithDBNameByRowHandler( dbOptions, rowhandler, beanType, dbname,  sqlinfo,  offset, pagesize, totalsizesqlinfo,  bean);
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
	public static ListInfo queryListInfoBeanWithDBNameByRowHandler(DBOptions dbOptions,RowHandler rowhandler,Class<?> beanType,String dbname, String sql, long offset,int pagesize,Object  bean) throws SQLException
	{
		
//		PreparedDBUtil dbutil = new PreparedDBUtil();
//		SQLParams params = SQLParams.convertBeanToSqlParams(bean,  new SQLInfo(sql,true,false), dbname, PreparedDBUtil.SELECT, null);
//		dbutil.preparedSelect(params,dbname, sql,offset,pagesize,-1L);
//		ListInfo datas = new ListInfo();
//		datas.setDatas(dbutil.executePreparedForList(beanType,rowhandler));
//		datas.setTotalSize(dbutil.getLongTotalSize());
//		return datas;
		SQLInfo sqlinfo = SQLUtil.getGlobalSQLUtil().getSQLInfo(sql,true,false);
		return SQLInfoExecutor.queryListInfoBeanWithDBNameByRowHandler( dbOptions, rowhandler,beanType,dbname, sqlinfo, offset,pagesize,bean);
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
	public static ListInfo queryListInfoBeanByRowHandler(DBOptions dbOptions,RowHandler rowhandler,Class<?> beanType, String sql, long offset,int pagesize,long totalsize,Object bean) throws SQLException
	{
		return queryListInfoBeanWithDBNameByRowHandler(   dbOptions,rowhandler,beanType, (String)null,sql, offset,pagesize,totalsize,bean);
	}
	
	public static ListInfo queryListInfoBeanByRowHandler(DBOptions dbOptions,RowHandler rowhandler,Class<?> beanType, String sql, long offset,int pagesize,String totalsizesql,Object bean) throws SQLException
	{
		return queryListInfoBeanWithDBNameByRowHandler(  dbOptions, rowhandler,beanType,(String) null,sql, offset,pagesize,totalsizesql,bean);
	}
	public static ListInfo queryListInfoBeanByRowHandler(DBOptions dbOptions,RowHandler rowhandler,Class<?> beanType, String sql, long offset,int pagesize,Object bean) throws SQLException
	{
		return queryListInfoBeanWithDBNameByRowHandler(   dbOptions,rowhandler,beanType, (String)null,sql, offset,pagesize,-1L,bean);
	}
	
	public static <T> T queryObjectBeanByRowHandler(DBOptions dbOptions,RowHandler rowhandler,Class<T> beanType, String sql, Object bean) throws SQLException
	{
		return queryObjectBeanWithDBNameByRowHandler(  dbOptions,rowhandler,beanType,(String)null, sql, bean);
		
		 
	}
	
	public static <T> List<T> queryListBeanWithDBNameByRowHandler(DBOptions dbOptions,RowHandler rowhandler,Class<T> beanType,String dbname, String sql, Object bean) throws SQLException
	{
		
//		PreparedDBUtil dbutil = new PreparedDBUtil();
//		SQLParams params = SQLParams.convertBeanToSqlParams(bean,  new SQLInfo(sql,true,false), dbname, PreparedDBUtil.SELECT, null);
//		dbutil.preparedSelect(params,dbname, sql);
//		
//		return dbutil.executePreparedForList(beanType,rowhandler);
		SQLInfo sqlinfo = SQLUtil.getGlobalSQLUtil().getSQLInfo(sql,true,false);
		return SQLInfoExecutor.queryListBeanWithDBNameByRowHandler( dbOptions,rowhandler, beanType, dbname,  sqlinfo,  bean);
	}
	
	
	public static <T> T queryObjectBeanWithDBNameByRowHandler(DBOptions dbOptions,RowHandler rowhandler,Class<T> beanType,String dbname, String sql, Object bean) throws SQLException
	{
		
//		PreparedDBUtil dbutil = new PreparedDBUtil();
//		SQLParams params = SQLParams.convertBeanToSqlParams(bean,  new SQLInfo(sql,true,false), dbname, PreparedDBUtil.SELECT, null);
//		dbutil.preparedSelect(params,dbname, sql);		
//		return (T)dbutil.executePreparedForObject(beanType,rowhandler);
		
		SQLInfo sqlinfo = SQLUtil.getGlobalSQLUtil().getSQLInfo(sql,true,false);		
		return SQLInfoExecutor.queryObjectBeanWithDBNameByRowHandler(dbOptions, rowhandler,beanType,dbname, sqlinfo, bean);
	}
	
	
	
	
	
	public static void queryBeanByNullRowHandler(DBOptions dbOptions,NullRowHandler rowhandler, String sql, Object bean) throws SQLException
	{
		
		 queryBeanWithDBNameByNullRowHandler(   dbOptions,rowhandler,(String)null, sql, bean);
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
	public static ListInfo queryListInfoBeanWithDBNameByNullRowHandler(DBOptions dbOptions,NullRowHandler rowhandler,String dbname, String sql, long offset,int pagesize,long totalsize,Object bean) throws SQLException
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
		return SQLInfoExecutor.queryListInfoBeanWithDBNameByNullRowHandler( dbOptions,rowhandler, dbname,  sqlinfo,  offset, pagesize, totalsize, bean);
	}
	
	public static ListInfo queryListInfoBeanWithDBNameByNullRowHandler(DBOptions dbOptions,NullRowHandler rowhandler,String dbname, String sql, long offset,int pagesize,String totalsizesql,Object bean) throws SQLException
	{
		
		SQLInfo sqlinfo = SQLUtil.getGlobalSQLUtil().getSQLInfo(sql,true,false);
		SQLInfo totalsizesqlsqlinfo = SQLUtil.getGlobalSQLUtil().getSQLInfo(totalsizesql,true,false);
		return SQLInfoExecutor.queryListInfoBeanWithDBNameByNullRowHandler( dbOptions, rowhandler, dbname,  sqlinfo,  offset, pagesize, totalsizesqlsqlinfo, bean);
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
	public static ListInfo queryListInfoBeanWithDBNameByNullRowHandler(DBOptions dbOptions,NullRowHandler rowhandler,String dbname, String sql, long offset,int pagesize,Object bean) throws SQLException
	{
		
		SQLInfo sqlinfo = SQLUtil.getGlobalSQLUtil().getSQLInfo(sql,true,false);
		return SQLInfoExecutor.queryListInfoBeanWithDBNameByNullRowHandler( dbOptions,rowhandler, dbname,  sqlinfo,  offset, pagesize, bean);
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
	public static ListInfo queryListInfoBeanByNullRowHandler(DBOptions dbOptions,NullRowHandler rowhandler, String sql, long offset,int pagesize,long totalsize,Object bean) throws SQLException
	{
		return queryListInfoBeanWithDBNameByNullRowHandler(  dbOptions, rowhandler, (String)null,sql, offset,pagesize,totalsize,bean);
	}
	
	public static ListInfo queryListInfoBeanByNullRowHandler(DBOptions dbOptions,NullRowHandler rowhandler, String sql, long offset,int pagesize,String totalsizesql,Object bean) throws SQLException
	{
		return queryListInfoBeanWithDBNameByNullRowHandler(   dbOptions,rowhandler,(String) null,sql, offset,pagesize,totalsizesql,bean);
	}
	
	public static ListInfo queryListInfoBeanByNullRowHandler(DBOptions dbOptions,NullRowHandler rowhandler, String sql, long offset,int pagesize,Object bean) throws SQLException
	{
		return queryListInfoBeanWithDBNameByNullRowHandler(   dbOptions,rowhandler,(String) null,sql, offset,pagesize,-1L,bean);
	}
	
	public static void queryBeanWithDBNameByNullRowHandler(DBOptions dbOptions,NullRowHandler rowhandler,String dbname, String sql, Object bean) throws SQLException
	{
		
//		PreparedDBUtil dbutil = new PreparedDBUtil();
//		SQLParams params = SQLParams.convertBeanToSqlParams(bean,  new SQLInfo(sql,true,false), dbname, PreparedDBUtil.SELECT, null);
//		dbutil.preparedSelect(params,dbname, sql);
//		 dbutil.executePreparedWithRowHandler(rowhandler);
		SQLInfo sqlinfo = SQLUtil.getGlobalSQLUtil().getSQLInfo(sql,true,false);
		SQLInfoExecutor.queryBeanWithDBNameByNullRowHandler( dbOptions,rowhandler, dbname,  sqlinfo,  bean);
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
	public static ListInfo moreListInfoWithDBNameByRowHandler(DBOptions dbOptions,RowHandler rowhandler,Class<?> beanType,String dbname, String sql, long offset,int pagesize,Object... fields) throws SQLException
	{
//		SQLInfo sqlinfo = SQLUtil.getGlobalSQLUtil().getSQLInfo(sql,false,false);		
//		return SQLInfoExecutor.queryListInfoWithDBNameByRowHandler(  rowhandler, beanType, dbname, sqlinfo, offset,pagesize,fields);
		
//		SQLInfo sql = getSqlInfo(dbname, sqlname);
        SQLInfo sqlinfo = SQLUtil.getGlobalSQLUtil().getPureSQLInfo(sql);
		return SQLInfoExecutor.moreListInfoWithDBNameByRowHandler(dbOptions,rowhandler,beanType,dbname, sqlinfo, offset,pagesize,fields);
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
	public static ListInfo moreListInfoWithDBNameByNullRowHandler(DBOptions dbOptions,NullRowHandler rowhandler,String dbname, String sql, long offset,int pagesize,Object... fields) throws SQLException
	{
//		SQLInfo sqlinfo = SQLUtil.getGlobalSQLUtil().getSQLInfo(sql,false,false);
        SQLInfo sqlinfo = SQLUtil.getGlobalSQLUtil().getPureSQLInfo(sql);
		
//		return SQLInfoExecutor.queryListInfoWithDBNameByNullRowHandler(   rowhandler, dbname,  sqlinfo,  offset, pagesize, fields);
//		SQLInfo sql = getSqlInfo(dbname, sqlname);
		return SQLInfoExecutor.moreListInfoWithDBNameByNullRowHandler( dbOptions, rowhandler,dbname, sqlinfo, offset,pagesize,fields);
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
	public static ListInfo moreListInfoWithDBName(DBOptions dbOptions,Class<?> beanType,String dbname, String sql, long offset,int pagesize,Object... fields) throws SQLException
	{
//		SQLInfo sqlinfo = SQLUtil.getGlobalSQLUtil().getSQLInfo(sql,false,false);
        SQLInfo sqlinfo = SQLUtil.getGlobalSQLUtil().getPureSQLInfo(sql);
//		return SQLInfoExecutor.queryListInfoWithDBName(  beanType, dbname,  sqlinfo,  offset, pagesize, fields);
//		SQLInfo sql = getSqlInfo(dbname, sqlname);
		return SQLInfoExecutor.moreListInfoWithDBName(dbOptions,beanType,dbname, sqlinfo, offset,pagesize,fields);
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
	public static ListInfo moreListInfoByRowHandler(DBOptions dbOptions,RowHandler rowhandler,Class<?> beanType, String sql, long offset,int pagesize,Object... fields) throws SQLException
	{
		return moreListInfoWithDBNameByRowHandler(   dbOptions,rowhandler,beanType, (String)null,sql, offset,pagesize,fields);
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
	public static ListInfo moreListInfoByNullRowHandler(DBOptions dbOptions,NullRowHandler rowhandler, String sql, long offset,int pagesize,Object... fields) throws SQLException
	{
		return moreListInfoWithDBNameByNullRowHandler(   dbOptions,rowhandler, (String)null,sql, offset,pagesize,fields);
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
	public static ListInfo moreListInfoBeanWithDBNameByRowHandler(DBOptions dbOptions,RowHandler rowhandler,Class<?> beanType,String dbname, String sql, long offset,int pagesize,Object  bean) throws SQLException
	{
		SQLInfo sqlinfo = SQLUtil.getGlobalSQLUtil().getSQLInfo(sql,true,false);
//		return SQLInfoExecutor.queryListInfoBeanWithDBNameByRowHandler(  rowhandler,beanType,dbname, sqlinfo, offset,pagesize,bean);
//		SQLInfo sql = getSqlInfo(dbname, sql);
		return SQLInfoExecutor.moreListInfoBeanWithDBNameByRowHandler(  dbOptions,rowhandler,beanType,dbname, sqlinfo, offset,pagesize,bean);
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
	public static ListInfo moreListInfoBeanWithDBNameByNullRowHandler(DBOptions dbOptions,NullRowHandler rowhandler,String dbname, String sql, long offset,int pagesize,Object bean) throws SQLException
	{
		SQLInfo sqlinfo = SQLUtil.getGlobalSQLUtil().getSQLInfo(sql,true,false);
//		return SQLInfoExecutor.queryListInfoBeanWithDBNameByNullRowHandler( rowhandler, dbname,  sqlinfo,  offset, pagesize, bean);	 
//		SQLInfo sql = getSqlInfo(dbname, sql);
		return SQLInfoExecutor.moreListInfoBeanWithDBNameByNullRowHandler(  dbOptions,rowhandler, dbname, sqlinfo, offset,pagesize,bean);
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
	public static ListInfo moreListInfoBeanWithDBName(DBOptions dbOptions,Class<?> beanType,String dbname, String sql, long offset,int pagesize,Object bean) throws SQLException
	{
		SQLInfo sqlinfo = SQLUtil.getGlobalSQLUtil().getSQLInfo(sql,true,false);
//		return SQLInfoExecutor.queryListInfoBeanWithDBName( beanType, dbname,  sqlinfo,  offset, pagesize,  bean);
//		SQLInfo sql = getSqlInfo(dbname, sql);
		return SQLInfoExecutor.moreListInfoBeanWithDBName(  dbOptions,beanType, dbname,  sqlinfo,  offset, pagesize, bean);
	}
	
		public static ListInfo moreListInfoBeanByRowHandler(DBOptions dbOptions,RowHandler rowhandler,Class<?> beanType, String sql, long offset,int pagesize,Object bean) throws SQLException
	{
			SQLInfo sqlinfo = SQLUtil.getGlobalSQLUtil().getSQLInfo(sql,true,false);
//			return SQLInfoExecutor.queryListInfoBeanWithDBNameByRowHandler(  rowhandler,beanType,dbname, sqlinfo, offset,pagesize,totalsize,bean);	 
			return SQLInfoExecutor.moreListInfoBeanWithDBNameByRowHandler( dbOptions, rowhandler,beanType,(String)null, sqlinfo, offset,pagesize,bean);
	}
	
		public static ListInfo moreListInfoBeanByNullRowHandler(DBOptions dbOptions,NullRowHandler rowhandler, String sql, long offset,int pagesize,Object bean) throws SQLException
	{
			
			SQLInfo sqlinfo = SQLUtil.getGlobalSQLUtil().getSQLInfo(sql,true,false);
//			return SQLInfoExecutor.queryListInfoBeanWithDBNameByNullRowHandler( rowhandler, dbname,  sqlinfo,  offset, pagesize, totalsize, bean);
//			SQLInfo sql = getSqlInfo(null, sql);
			return SQLInfoExecutor.moreListInfoBeanWithDBNameByNullRowHandler( dbOptions, rowhandler, (String)null, sqlinfo, offset,pagesize,bean);
	}
	
		public static ListInfo moreListInfoBean(DBOptions dbOptions,Class<?> beanType, String sql, long offset,int pagesize,Object bean) throws SQLException
	{
			SQLInfo sqlinfo = SQLUtil.getGlobalSQLUtil().getSQLInfo(sql,true,false);
//			return SQLInfoExecutor.queryListInfoBeanWithDBName( beanType, dbname,  sqlinfo,  offset, pagesize, totalsize, bean);
//			SQLInfo sql = getSqlInfo(null, sql);
			return SQLInfoExecutor.moreListInfoBeanWithDBName(  dbOptions,beanType, (String)null,  sqlinfo,  offset, pagesize, bean);
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
	public static ListInfo moreListInfo(DBOptions dbOptions,Class<?> beanType, String sql, long offset,int pagesize,Object... fields) throws SQLException
	{
		return moreListInfoWithDBName(  dbOptions,beanType, (String)null,sql, offset,pagesize,fields);
	}
	
	
}
