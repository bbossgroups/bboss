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
import com.frameworkset.common.poolman.util.JDBCPool;
import com.frameworkset.common.poolman.util.SQLManager;
import com.frameworkset.util.ListInfo;
import com.frameworkset.util.ValueObjectUtil;
import org.frameworkset.persitent.util.SQLInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.BatchUpdateException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class SQLInfoExecutor {
	private static Logger log = LoggerFactory.getLogger(SQLInfoExecutor.class);
	public static int DEFAULT_BATCHSIZE = -1;
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
    
    

	
	public static void init(SQLParams sqlparams,SQLInfo statement,String pretoken,String endtoken,String action)
	{
//		this.sqlparams = new SQLParams();
		sqlparams.setOldsql( statement);
		if(action != null)
		{
			if(action.equals(SQLExecutor.ACTION_INSERT))
				sqlparams.setAction(PreparedDBUtil.INSERT);
			else if(action.equals(SQLExecutor.ACTION_DELETE))
				sqlparams.setAction(PreparedDBUtil.DELETE);
			else if(action.equals(SQLExecutor.ACTION_UPDATE))
				sqlparams.setAction(PreparedDBUtil.UPDATE);
		}
		sqlparams.setPretoken(pretoken);
		sqlparams.setEndtoken(endtoken);
		//http://changsha.koubei.com/store/detail--storeId-b227fc4aee6e4862909ea7bf62556a7a
		
		
	}
	
	

	
	
	public static void insertBeans(DBOptions dbOptions,String dbname, SQLInfo sql, List beans) throws SQLException {
		
		if(beans == null || beans.size() == 0)
			return ;
		execute(   dbOptions,dbname,  sql,  beans,PreparedDBUtil.INSERT,(GetCUDResult)null);
	}
	
	public static void insertBeans(DBOptions dbOptions,String dbname, SQLInfo sql, List beans,GetCUDResult getCUDResult) throws SQLException {
		
		if(beans == null || beans.size() == 0)
			return ;
		execute(   dbOptions,dbname,  sql,  beans,PreparedDBUtil.INSERT,getCUDResult);
	}
	
	
	public static void execute(DBOptions dbOptions,String dbname, SQLInfo sql, List beans,boolean isBatchOptimize,int action) throws SQLException
	{
		execute(  dbOptions,dbname, sql, beans,isBatchOptimize,action,(GetCUDResult)null) ;
	}
	
	public static void execute(DBOptions dbOptions,String dbname, SQLInfo sql, List beans,boolean isBatchOptimize,int action,GetCUDResult getCUDResult) throws SQLException
	{
		Connection con = null;
		if(beans == null || beans.size() == 0)
			return;
		
			try
			{
				con = DBUtil.getConection(dbname);
				if(beans.size() < DEFAULT_BATCHSIZE || DEFAULT_BATCHSIZE == -1){
					
					List<SQLParams> batchsqlparams = SQLParams.convertBeansToSqlParams(beans,sql,dbname,action,con);
					if(batchsqlparams == null)
						return ;
					PreparedDBUtil dbutil = new PreparedDBUtil();
					dbutil.setBatchOptimize(isBatchOptimize);
					dbutil.setPrepareDBName(dbname);
					dbutil.addPreparedBatch(new ListSQLParams(batchsqlparams,sql));
					dbutil.executePreparedBatch(dbOptions,con,getCUDResult);
				}
				else//如果大于批处理size,则按批次进行批处理操作
				{
					
					int start = 0;
					int totalsize = beans.size();
					int left  = totalsize - start;
					int end = 0;
					int step = 0;
					do{
						
						 end = left >= DEFAULT_BATCHSIZE?start + DEFAULT_BATCHSIZE:totalsize;
						 step = left >= DEFAULT_BATCHSIZE?DEFAULT_BATCHSIZE:left;
						List<SQLParams> batchsqlparams = SQLParams.convertBeansToSqlParams(beans,sql,dbname,action,con,start,end,step);
						if(batchsqlparams == null)
							break ;
						PreparedDBUtil dbutil = new PreparedDBUtil();
						dbutil.setBatchOptimize(isBatchOptimize);
						dbutil.setPrepareDBName(dbname);
						dbutil.addPreparedBatch(new ListSQLParams(batchsqlparams,sql));
						dbutil.executePreparedBatch(dbOptions,con,getCUDResult);
						start = start + DEFAULT_BATCHSIZE;
						left  = totalsize - start;
					}while(left > 0);
					
				}
			}
			finally
			{
				try {
					if (con != null)
						con.close();
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
		
		
	}
	private static Object CUDexecute(DBOptions dbOptions, String dbname, SQLInfo sql, Object bean, int action) throws SQLException
	{
		return CUDexecute(  dbOptions,dbname, sql, bean,action,false) ;
	}
	/**
	 * 针对增删改三种类型DB操作的统一处理方法
	 * @param dbname
	 * @param sql
	 * @param bean
	 * @param getCUDResult
	 * @param action
	 * @return
	 * @throws SQLException
	 */
	private static Object CUDexecute(DBOptions dbOptions,String dbname, SQLInfo sql, Object bean,int action,boolean getCUDResult) throws SQLException
	{
		Connection con = null;
		try
		{
			if(action ==  PreparedDBUtil.INSERT)//如果bean中使用PrimaryKey注解，并且要求自动设置主键，则要求后续所有的操作共用同一个connection来完成所有操作，以便提升系统性能
				con = DBUtil.getConection(dbname);
			SQLParams batchsqlparams = SQLParams.convertBeanToSqlParams(bean,sql,dbname,action,con);
			if(batchsqlparams == null)
				return null;
//			PreparedDBUtil dbutil = new PreparedDBUtil();
//			dbutil.setBatchOptimize(isBatchOptimize);
//			dbutil.setPrepareDBName(dbname);
//			dbutil.addPreparedBatch(batchsqlparams);
//			dbutil.executePreparedBatch(con);
			
			
//			 action = action.toLowerCase();
			SQLInfoDBUtil dbutil = new SQLInfoDBUtil();
	        if(action == PreparedDBUtil.INSERT)
	        {
//	            if(batchsqlparams.size() > 0)
	            {
	                dbutil.preparedInsert(batchsqlparams, dbname,sql);
	                return dbutil.executePrepared(dbOptions,con,getCUDResult);
	            }
//	            else
//	            {
//	                return dbutil.executeInsert(dbname,sql,con);
//	            }
	        }
	        else if(action == PreparedDBUtil.UPDATE)
	        {
//	            if(batchsqlparams.size() > 0)
	            {
	                dbutil.preparedUpdate(batchsqlparams, dbname,sql);
	                return dbutil.executePrepared(dbOptions,con,getCUDResult);
	            }
//	            else
//	            {
//	                return dbutil.executeUpdate(dbname,sql,con);
//	            }
	                
	        }
	        else if(action == PreparedDBUtil.DELETE)
	        {
//	            if(batchsqlparams.size() > 0)
	            {
	                dbutil.preparedDelete(batchsqlparams, dbname,sql);
	                return dbutil.executePrepared(dbOptions,con,getCUDResult);
	            }
//	            else
//	            {
//	                return dbutil.executeDelete(dbname,sql,con);
//	            }
	        }
	        else
	            throw new SQLException("不支持的数据库操作：" + action);
		        
		}
		finally
		{
			try {
				if (con != null)
					con.close();
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
	}
	
	/**
	 * 
	 * @param dbname
	 * @param sql
	 * @param datas
	 * @param batchsize
	 * @param batchHandler
	 * @throws SQLException
	 */
	public static <T> void executeBatch(DBOptions dbOptions,String dbname,SQLInfo sql,List<T> datas,
                                        int batchsize, BatchHandler<T> batchHandler) throws SQLException{
		if(datas == null || datas.size() == 0){
			return;
		}
		StatementInfo stmtInfo = null;		
		PreparedStatement statement = null;
		List resources = null;
		Connection con_ = null;
//		GetCUDResult CUDResult = null;
		try
		{	
			con_ = DBUtil.getConection(dbname);
			stmtInfo = new StatementInfo(dbname,
					null,
					false,
					 con_,
					 false);
			stmtInfo.init();
			 
			JDBCPool pool = SQLManager.getInstance().getPool(dbname);
            SQLManager.assertPoolSQLException(pool,dbname);
//			boolean showsql = pool.getJDBCPoolMetadata().isShowsql();
//			if(showsql)
//			{
//				if(log.isInfoEnabled())
//        			log.info("Execute JDBC prepared batch statement:"+sql.getSql());
//			}

			statement = stmtInfo
					.prepareStatement(sql.getSql(),"Execute JDBC prepared batch statement:{}");
			if(batchsize <= 1 ){//如果batchsize被设置为0或者1直接一次性批处理所有记录
				for(int i = 0;i < datas.size(); i ++ )
				{
					T param = datas.get(i);
					batchHandler.handler(statement, param, i);
					statement.addBatch();					
					
				}
				statement.executeBatch();
			}
			else
			{
				int point = batchsize - 1;
				int count = 0;
				for(int i = 0 ;i < datas.size(); i ++ )
				{
					T param = datas.get(i);
					batchHandler.handler(statement, param, i);
					statement.addBatch();						
					if((count > 0 && count % point == 0 ) ){
						statement.executeBatch();
						statement.clearBatch();
						count = 0;
						continue;
					}
					count ++;
												
				}
				if(count > 0)
					statement.executeBatch();
			}
				
		}
		catch(BatchUpdateException error)
		{

			
			if(stmtInfo != null)
				stmtInfo.errorHandle(error);		
			
			throw error;
		}
	    catch (Exception e) {
//	    	try{
//				
//	    		log.error("Execuete batch prepared Error:" + e.getMessage(), e);
//			}
//			catch(Exception ei)
//			{
//				
//			}
			
	    	
			if(stmtInfo != null)
				stmtInfo.errorHandle(e);
			if(e instanceof SQLException)
				throw (SQLException)e;
			else
				throw new NestedSQLException(e.getMessage(),e);
		} finally {
			try {
				if (stmtInfo != null) {
					stmtInfo.dofinally();
					stmtInfo = null;
				}
			}
			catch (Exception e){

			}
			try {
				if(con_ != null){
					con_.close();
					con_ = null;
				}
			}
			catch (Exception e){

			}

		}
	}
	
	

	
	
	
	public static void execute(DBOptions dbOptions,String dbname, SQLInfo sql, List beans,int action) throws SQLException
	{
		execute(  dbOptions,dbname, sql, beans,false,action,null);
	}
	
	public static void execute(DBOptions dbOptions,String dbname, SQLInfo sql, List beans,int action,GetCUDResult getCUDResult) throws SQLException
	{
		execute(  dbOptions,dbname, sql, beans,false,action,getCUDResult);
	}
	
	protected static Object execute(DBOptions dbOptions,String dbname, SQLInfo sql,int action, Object... fields) throws SQLException {
//		if(fields == null || fields.length == 0)
//			return null;
		SQLInfoDBUtil dbutil = new SQLInfoDBUtil();
		if(action == PreparedDBUtil.INSERT)
			dbutil.preparedInsert(dbname, sql);
		else if(action == PreparedDBUtil.DELETE)
			dbutil.preparedDelete(dbname, sql);
		else if(action == PreparedDBUtil.UPDATE)
			dbutil.preparedUpdate(dbname, sql);
		else 
			dbutil.preparedUpdate(dbname, sql);
		if(fields != null && fields.length > 0)
		{
			for(int i = 0; i < fields.length ; i ++)
			{
				
				Object field = fields[i];
				dbutil.setObject(i + 1, field);
			}
		}
		
		
		
		return dbutil.executePrepared(dbOptions);
	}
	
	protected static void executeBatch(DBOptions dbOptions,String dbname, SQLInfo sql,int action, Object fields_) throws SQLException {
//		if(fields == null || fields.length == 0)
//			return ;
		SQLInfoDBUtil dbutil = new SQLInfoDBUtil();
		if(action == PreparedDBUtil.INSERT)
			dbutil.preparedInsert(dbname, sql);
		else if(action == PreparedDBUtil.DELETE)
			dbutil.preparedDelete(dbname, sql);
		else if(action == PreparedDBUtil.UPDATE)
			dbutil.preparedUpdate(dbname, sql);
		else 
			dbutil.preparedUpdate(dbname, sql);
		if(fields_ != null )
		{
			Class componetType = fields_.getClass().getComponentType();
			if(componetType == int.class)
			{
				int[] fields = (int[])fields_;
				if(fields != null && fields.length > 0)
				{
					
					{
						for(int i = 0; i < fields.length ; i ++)
						{
							
							int field = fields[i];
							dbutil.setInt(1, field);
							dbutil.addPreparedBatch();
							
						}
					}
				}
			}
			else if(componetType == long.class)
			{
				long[] fields = (long[])fields_;
				if(fields != null && fields.length > 0)
				{
					
					{
						for(int i = 0; i < fields.length ; i ++)
						{
							
							long field = fields[i];
							dbutil.setLong(1, field);
							dbutil.addPreparedBatch();
							
						}
					}
				}
			}
			else if(componetType == String.class)
			{
				String[] fields = (String[])fields_;
				if(fields != null && fields.length > 0)
				{
					
					{
						for(int i = 0; i < fields.length ; i ++)
						{
							
							String field = fields[i];
							dbutil.setString(1, field);
							dbutil.addPreparedBatch();
							
						}
					}
				}
			}
			else if(componetType == short.class)
			{
				short[] fields = (short[])fields_;
				if(fields != null && fields.length > 0)
				{
					for(int i = 0; i < fields.length ; i ++)
					{	
						short field = fields[i];
						dbutil.setShort(1, field);
						dbutil.addPreparedBatch();
					}
				}
			}
		}
		
		
		
		dbutil.executePreparedBatch(dbOptions);
	}
	
	
	public static Object update( DBOptions dbOptions,SQLInfo sql, Object... fields) throws SQLException {
		return execute(  dbOptions,(String)null, sql,PreparedDBUtil.UPDATE, fields);
	}
	



	public static Object delete(DBOptions dbOptions,SQLInfo sql, Object... fields) throws SQLException {
		return execute(  dbOptions,(String)null, sql,PreparedDBUtil.DELETE, fields);
		
	}
	

	
	public static void deleteByKeys(DBOptions dbOptions,SQLInfo sql, int... fields) throws SQLException {
		executeBatch(  dbOptions,(String)null, sql,PreparedDBUtil.DELETE, fields);
		
	}
	public static void deleteByKeysWithDBName(DBOptions dbOptions,String dbname,SQLInfo sql, int... fields) throws SQLException {
		executeBatch(  dbOptions,dbname, sql,PreparedDBUtil.DELETE, fields);
		
	}
	
	public static void updateByKeys(DBOptions dbOptions,SQLInfo sql, int... fields) throws SQLException {
		executeBatch(  dbOptions,(String)null, sql,PreparedDBUtil.UPDATE, fields);
		
	}
	public static void updateByKeysWithDBName(DBOptions dbOptions,String dbname,SQLInfo sql, int... fields) throws SQLException {
		executeBatch(  dbOptions,dbname, sql,PreparedDBUtil.UPDATE, fields);
		
	}
	
	public static void deleteByLongKeys(DBOptions dbOptions,SQLInfo sql, long... fields) throws SQLException {
		executeBatch(  dbOptions,(String)null, sql,PreparedDBUtil.DELETE, fields);
		
	}
	public static void deleteByLongKeysWithDBName(DBOptions dbOptions,String dbname,SQLInfo sql, long... fields) throws SQLException {
		executeBatch(  dbOptions,dbname, sql,PreparedDBUtil.DELETE, fields);
		
	}
	
	public static void updateByLongKeys(DBOptions dbOptions,SQLInfo sql, long... fields) throws SQLException {
		executeBatch(dbOptions,(String)null, sql,PreparedDBUtil.UPDATE, fields);
		
	}
	public static void updateByLongKeysWithDBName(DBOptions dbOptions,String dbname,SQLInfo sql, long... fields) throws SQLException {
		executeBatch(  dbOptions,dbname, sql,PreparedDBUtil.UPDATE, fields);
		
	}
	
	public static void deleteByKeys(DBOptions dbOptions,SQLInfo sql, String... fields) throws SQLException {
		executeBatch(  dbOptions,(String)null, sql,PreparedDBUtil.DELETE, fields);
		
	}
	public static void deleteByKeysWithDBName(DBOptions dbOptions,String dbname,SQLInfo sql, String... fields) throws SQLException {
		executeBatch(  dbOptions,dbname, sql,PreparedDBUtil.DELETE, fields);
		
	}
	
	public static void updateByKeys(DBOptions dbOptions,SQLInfo sql, String... fields) throws SQLException {
		executeBatch(  dbOptions,(String)null, sql,PreparedDBUtil.UPDATE, fields);
		
	}
	public static void updateByKeysWithDBName(DBOptions dbOptions,String dbname,SQLInfo sql, String... fields) throws SQLException {
		executeBatch(  dbOptions,dbname, sql,PreparedDBUtil.UPDATE, fields);
		
	}
	
	public static void deleteByShortKeys(DBOptions dbOptions,SQLInfo sql, short... fields) throws SQLException {
		executeBatch(dbOptions,(String)null, sql,PreparedDBUtil.DELETE, fields);
		
	}
	public static void deleteByShortKeysWithDBName(DBOptions dbOptions,String dbname,SQLInfo sql, short... fields) throws SQLException {
		executeBatch(  dbOptions,dbname, sql,PreparedDBUtil.DELETE, fields);
	}
	
	
	public static void updateByShortKeys(DBOptions dbOptions,SQLInfo sql, short... fields) throws SQLException {
		executeBatch(  dbOptions,(String)null, sql,PreparedDBUtil.UPDATE, fields);
		
	}
	public static void updateByShortKeysWithDBName(DBOptions dbOptions,String dbname,SQLInfo sql, short... fields) throws SQLException {
		executeBatch(  dbOptions,dbname, sql,PreparedDBUtil.UPDATE, fields);
	}



	public static Object insert(DBOptions dbOptions,SQLInfo sql, Object... fields) throws SQLException {
		return execute(  dbOptions,(String)null, sql,PreparedDBUtil.INSERT, fields);
	}
	
	public static Object updateWithDBName(DBOptions dbOptions,String dbname, SQLInfo sql, Object... fields) throws SQLException {
		return execute(  dbOptions,dbname, sql,PreparedDBUtil.UPDATE, fields);
	}
	
	public static Object deleteWithDBName(DBOptions dbOptions,String dbname, SQLInfo sql, Object... fields) throws SQLException {
		return execute(  dbOptions,dbname, sql,PreparedDBUtil.DELETE, fields);
		
	}



	public static Object insertWithDBName(DBOptions dbOptions,String dbname, SQLInfo sql, Object... fields) throws SQLException {
		return execute(  dbOptions,dbname, sql,PreparedDBUtil.INSERT, fields);
	}

	public static void updateBeans(DBOptions dbOptions,String dbname, SQLInfo sql, List beans) throws SQLException {
		if(beans == null || beans.size() == 0)
			return ;
		execute(   dbOptions,dbname,  sql,  beans,PreparedDBUtil.UPDATE,(GetCUDResult)null);
	}
	
	public static void updateBeans(  DBOptions dbOptions,String dbname, SQLInfo sql, List beans,GetCUDResult GetCUDResult) throws SQLException {
		if(beans == null || beans.size() == 0)
			return ;
		execute(   dbOptions,dbname,  sql,  beans,PreparedDBUtil.UPDATE,GetCUDResult);
	}



	public static void deleteBeans(DBOptions dbOptions,String dbname, SQLInfo sql, List beans) throws SQLException {
		if(beans == null || beans.size() == 0)
			return ;
		execute(   dbOptions,dbname,  sql,  beans,PreparedDBUtil.DELETE);
		
	}
	
	public static void deleteBeans(DBOptions dbOptions,String dbname, SQLInfo sql, List beans,GetCUDResult GetCUDResult) throws SQLException {
		if(beans == null || beans.size() == 0)
			return ;
		execute(   dbOptions,dbname,  sql,  beans,PreparedDBUtil.DELETE, GetCUDResult);
		
	}



	public static void insertBean(DBOptions dbOptions,String dbname, SQLInfo sql, Object bean) throws SQLException {
		if(bean == null)
			return ;
//		List datas = new ArrayList();
//		datas.add(bean);
//		insertBeans( dbname,  sql,  datas);
		CUDexecute(  dbOptions,dbname, sql, bean,PreparedDBUtil.INSERT,false);
	}
	
	public static void insertBean(DBOptions dbOptions,String dbname, SQLInfo sql, Object bean,GetCUDResult getCUDResult) throws SQLException {
		if(bean == null)
			return ;
//		List datas = new ArrayList();
//		datas.add(bean);
//		insertBeans( dbname,  sql,  datas);
		if(getCUDResult == null)
		{
			
			CUDexecute(  dbOptions,dbname, sql, bean,PreparedDBUtil.INSERT,false);
		}
		else
		{
			GetCUDResult getCUDResult_ = (GetCUDResult)CUDexecute(  dbOptions,dbname, sql, bean,PreparedDBUtil.INSERT,true);
			getCUDResult.setGetCUDResult(getCUDResult_);
			
		}
	}



	public static void updateBean(DBOptions dbOptions,String dbname, SQLInfo sql, Object bean) throws SQLException {
		if(bean == null )
			return ;
//		List datas = new ArrayList();
//		datas.add(bean);
//		updateBeans( dbname,  sql,  datas);
		CUDexecute(  dbOptions,dbname, sql, bean,PreparedDBUtil.UPDATE,false);
	}
	
	public static void updateBean(DBOptions dbOptions,String dbname, SQLInfo sql, Object bean,GetCUDResult getCUDResult) throws SQLException {
		if(bean == null )
			return ;
//		List datas = new ArrayList();
//		datas.add(bean);
//		updateBeans( dbname,  sql,  datas);
		
		if(getCUDResult != null)
		{
			GetCUDResult getCUDResult_ = (GetCUDResult)CUDexecute(  dbOptions,dbname, sql, bean,PreparedDBUtil.UPDATE,true);
			getCUDResult.setGetCUDResult(getCUDResult_);
		}
		else
			CUDexecute(  dbOptions,dbname, sql, bean,PreparedDBUtil.UPDATE,false);
	}
	
	public static void updateBean( DBOptions dbOptions,SQLInfo sql, Object bean,GetCUDResult getCUDResult) throws SQLException {
//		if(bean == null )
//			return ;
//		List datas = new ArrayList();
//		datas.add(bean);
//		updateBeans( null,  sql,  datas);
		updateBean(  dbOptions,(String)null,  sql,  bean, getCUDResult);
	}

	public static void deleteBean( DBOptions dbOptions,SQLInfo sql, Object bean,GetCUDResult getCUDResult) throws SQLException {
		deleteBean(  dbOptions,(String )null,sql, bean,getCUDResult) ;
	}

	public static void deleteBean(DBOptions dbOptions,String dbname, SQLInfo sql, Object bean) throws SQLException {
		
		if(bean == null)
			return ;
//		List datas = new ArrayList();
//		datas.add(bean);
//		deleteBeans( dbname,  sql,  datas);
		CUDexecute(  dbOptions,dbname, sql, bean,PreparedDBUtil.DELETE,false);
	}
	public static void deleteBean(DBOptions dbOptions,String dbname, SQLInfo sql, Object bean,GetCUDResult getCUDResult) throws SQLException {
		
		if(bean == null)
			return ;
//		List datas = new ArrayList();
//		datas.add(bean);
//		deleteBeans( dbname,  sql,  datas);
		if(getCUDResult != null)
		{
			GetCUDResult getCUDResult_ = (GetCUDResult)CUDexecute(  dbOptions,dbname, sql, bean,PreparedDBUtil.DELETE,true);
			getCUDResult.setGetCUDResult(getCUDResult_);
		}
		else
			CUDexecute(  dbOptions,dbname, sql, bean,PreparedDBUtil.DELETE,false);
	}
	
	public static void insertBeans(DBOptions dbOptions,SQLInfo sql, List beans) throws SQLException {
		insertBeans(  dbOptions, null,sql, beans);
	}
	
	public static void insertBeans(DBOptions dbOptions,SQLInfo sql, List beans,GetCUDResult getCUDResult) throws SQLException {
		insertBeans(   dbOptions,(String)null,sql, beans,getCUDResult);
	}
	
	



	public static void updateBeans( DBOptions dbOptions,SQLInfo sql, List beans) throws SQLException {
		updateBeans(dbOptions,(String) null,sql, beans);
	}



	public static void deleteBeans( DBOptions dbOptions,SQLInfo sql, List beans) throws SQLException {
		deleteBeans(   dbOptions,(String)null,sql, beans);
		
	}



	public static void insertBean( DBOptions dbOptions,SQLInfo sql, Object bean) throws SQLException {
//		if(bean == null)
//			return ;
//		List datas = new ArrayList();
//		datas.add(bean);
//		insertBeans( null,  sql,  datas);
		insertBean(   dbOptions,(String)null,sql, bean);
	}
	public static void insertBean( DBOptions dbOptions,SQLInfo sql, Object bean,GetCUDResult getCUDResult) throws SQLException {
//		if(bean == null)
//			return ;
//		List datas = new ArrayList();
//		datas.add(bean);
//		insertBeans( null,  sql,  datas);
		insertBean(   dbOptions,(String)null,sql, bean,getCUDResult);
	}



	public static void updateBean( DBOptions dbOptions,SQLInfo sql, Object bean) throws SQLException {
//		if(bean == null )
//			return ;
//		List datas = new ArrayList();
//		datas.add(bean);
//		updateBeans( null,  sql,  datas);
		updateBean(   dbOptions,(String)null,sql, bean);
	}

	

	public static void deleteBean(DBOptions dbOptions,SQLInfo sql, Object bean) throws SQLException {
		
//		if(bean == null)
//			return ;
//		List datas = new ArrayList();
//		datas.add(bean);
//		deleteBeans( null,  sql,  datas);
		deleteBean(  dbOptions,(String)null,sql, bean);
		
	}
	
	
	public static <T> List<T> queryList(DBOptions dbOptions,Class<T> beanType, SQLInfo sql, Object... fields) throws SQLException
	{
		
		return queryListWithDBName(  dbOptions,beanType,null, sql, fields);
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
	public static ListInfo queryListInfoWithDBName(DBOptions dbOptions,Class<?> beanType,String dbname, SQLInfo sql, long offset,int pagesize,Object... fields) throws SQLException
	{
		
		SQLInfoDBUtil dbutil = new SQLInfoDBUtil();
		dbutil.preparedSelect(dbname, sql,offset,pagesize);
		if(fields != null && fields.length > 0)
		{
			int length = fields.length ;
			Object last = fields[length-1];
		
			
			if(last instanceof PagineOrderby)
			{
				  
				dbutil.setPagineOrderby( ((PagineOrderby)last).getPagineOrderby());
				length = length - 1;
			}
			for(int i = 0; i < length ; i ++)
			{
				
				Object field = fields[i];
				dbutil.setObject(i + 1, field);
			}
		}
		
		
		ListInfo datas = new ListInfo();
		datas.setMaxPageItems(pagesize);
		datas.setDatas(dbutil.executePreparedForList(dbOptions,beanType));
		datas.setTotalSize(dbutil.getLongTotalSize());
		return datas;		 
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
	public static ListInfo moreListInfoWithDBName(DBOptions dbOptions,Class<?> beanType,String dbname, SQLInfo sql, long offset,int pagesize,Object... fields) throws SQLException
	{
		
		SQLInfoDBUtil dbutil = new SQLInfoDBUtil();
		dbutil.setMore(true);
		dbutil.preparedSelect(dbname, sql,offset,pagesize);
		
		if(fields != null && fields.length > 0)
		{
			int length = fields.length ;
			Object last = fields[length-1];
		
			
			if(last instanceof PagineOrderby)
			{
				  
				dbutil.setPagineOrderby( ((PagineOrderby)last).getPagineOrderby());
				length = length - 1;
			}
			for(int i = 0; i < length ; i ++)
			{
				
				Object field = fields[i];
				dbutil.setObject(i + 1, field);
				
			}
		
		}
		
		
		ListInfo datas = new ListInfo();
		datas.setDatas(dbutil.executePreparedForList(dbOptions,beanType));
//		datas.setTotalSize(dbutil.getLongTotalSize());
		datas.setMore(true);
		datas.setResultSize(dbutil.size());
		datas.setMaxPageItems(pagesize);
		return datas;		 
	}
	public static ListInfo queryListInfoWithDBName2ndTotalsize(DBOptions dbOptions,Class<?> beanType,String dbname, SQLInfo sql, long offset,int pagesize,long totalsize,Object... fields) throws SQLException
	{
		
		SQLInfoDBUtil dbutil = new SQLInfoDBUtil();
		dbutil.preparedSelect(dbname, sql,offset,pagesize,totalsize);
		if(fields != null && fields.length > 0)
		{
			int length = fields.length ;
			Object last = fields[length-1];
		
			
			if(last instanceof PagineOrderby)
			{
				  
				dbutil.setPagineOrderby( ((PagineOrderby)last).getPagineOrderby());
				length = length - 1;
			}
			for(int i = 0; i < length ; i ++)
			{
				
				Object field = fields[i];
				dbutil.setObject(i + 1, field);
			}
		}
		
		
		ListInfo datas = new ListInfo();
		datas.setDatas(dbutil.executePreparedForList(dbOptions,beanType));
		datas.setTotalSize(dbutil.getLongTotalSize());
		datas.setMaxPageItems(pagesize);
		return datas;		 
	}
	public static ListInfo queryListInfoWithDBName2ndTotalsizesql(DBOptions dbOptions,Class<?> beanType,String dbname, SQLInfo sql, long offset,int pagesize,SQLInfo totalsizesql,Object... fields) throws SQLException
	{
		
		SQLInfoDBUtil dbutil = new SQLInfoDBUtil();
		dbutil.preparedSelectWithTotalsizesql(dbname, sql,offset,pagesize,totalsizesql);
		if(fields != null && fields.length > 0)
		{
			int length = fields.length ;
			Object last = fields[length-1];
		
			
			if(last instanceof PagineOrderby)
			{
				  
				dbutil.setPagineOrderby( ((PagineOrderby)last).getPagineOrderby());
				length = length - 1;
			}
			for(int i = 0; i < length ; i ++)
			{
				
				Object field = fields[i];
				dbutil.setObject(i + 1, field);
			}
		}
		
		
		ListInfo datas = new ListInfo();
		datas.setDatas(dbutil.executePreparedForList(dbOptions,beanType));
		datas.setTotalSize(dbutil.getLongTotalSize());
		datas.setMaxPageItems(pagesize);
		return datas;		 
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
	public static ListInfo queryListInfo(DBOptions dbOptions,Class<?> beanType, SQLInfo sql, long offset,int pagesize,Object... fields) throws SQLException
	{
		return queryListInfoWithDBName(  dbOptions,beanType, null,sql, offset,pagesize,fields);
	}
	
	public static ListInfo queryListInfoWithTotalsize(DBOptions dbOptions,Class<?> beanType, SQLInfo sql, long offset,int pagesize,long totalsize,Object... fields) throws SQLException
	{
		return queryListInfoWithDBName2ndTotalsize(  dbOptions,beanType, null,sql, offset,pagesize,totalsize,fields);
	}
	
	public static ListInfo queryListInfoWithTotalsizesql(DBOptions dbOptions,Class<?> beanType, SQLInfo sql, long offset,int pagesize,SQLInfo totalsizesql,Object... fields) throws SQLException
	{
		return queryListInfoWithDBName2ndTotalsizesql(  dbOptions,beanType, null,sql, offset,pagesize,totalsizesql,fields);
	}
	
	
	public static <T> T queryObject(DBOptions dbOptions,Class<T> beanType, SQLInfo sql, Object... fields) throws SQLException
	{
		return queryObjectWithDBName(  dbOptions,beanType,null, sql, fields);
		
		 
	}
	
	public static <T> List<T> queryListWithDBName(DBOptions dbOptions,Class<T> beanType,String dbname, SQLInfo sql, Object... fields) throws SQLException
	{
		
		SQLInfoDBUtil dbutil = new SQLInfoDBUtil();
		dbutil.preparedSelect(dbname, sql);
		if(fields != null && fields.length > 0)
		{
			for(int i = 0; i < fields.length ; i ++)
			{
				
				Object field = fields[i];
				dbutil.setObject(i + 1, field);
			}
		}
		
		
		
		return dbutil.executePreparedForList(dbOptions,beanType);
	}
	
	
	public static <T> T queryObjectWithDBName(DBOptions dbOptions,Class<T> beanType,String dbname, SQLInfo sql, Object... fields) throws SQLException
	{
		
		SQLInfoDBUtil dbutil = new SQLInfoDBUtil();
		dbutil.preparedSelect(dbname, sql);
		if(fields != null && fields.length > 0)
		{
			for(int i = 0; i < fields.length ; i ++)
			{
				
				Object field = fields[i];
				dbutil.setObject(i + 1, field);
			}
		}
		
		return (T)dbutil.executePreparedForObject(dbOptions,beanType);
		 
	}
	
	
	public static <T> List<T> queryListByRowHandler(DBOptions dbOptions,RowHandler rowhandler,Class<T> beanType, SQLInfo sql, Object... fields) throws SQLException
	{
		
		return queryListWithDBNameByRowHandler(  dbOptions,rowhandler,beanType,null, sql, fields);
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
	public static ListInfo queryListInfoWithDBNameByRowHandler(DBOptions dbOptions,RowHandler rowhandler,Class<?> beanType,String dbname, SQLInfo sql, long offset,int pagesize,Object... fields) throws SQLException
	{
		
		SQLInfoDBUtil dbutil = new SQLInfoDBUtil();
		dbutil.preparedSelect(dbname, sql,offset,pagesize);
		if(fields != null && fields.length > 0)
		{
			int length = fields.length ;
			Object last = fields[length-1];
		
			
			if(last instanceof PagineOrderby)
			{
				  
				dbutil.setPagineOrderby( ((PagineOrderby)last).getPagineOrderby());
				length = length - 1;
			}
			for(int i = 0; i < length ; i ++)
			{
				
				Object field = fields[i];
				dbutil.setObject(i + 1, field);
			}
		}
		
		
		ListInfo datas = new ListInfo();
		datas.setDatas(dbutil.executePreparedForList(dbOptions,beanType,rowhandler));
		datas.setTotalSize(dbutil.getLongTotalSize());
		datas.setMaxPageItems(pagesize);
		return datas;		 
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
	public static ListInfo moreListInfoWithDBNameByRowHandler(DBOptions dbOptions,RowHandler rowhandler,Class<?> beanType,String dbname, SQLInfo sql, long offset,int pagesize,Object... fields) throws SQLException
	{
		
		SQLInfoDBUtil dbutil = new SQLInfoDBUtil();
		dbutil.setMore(true);
		dbutil.preparedSelect(dbname, sql,offset,pagesize);
		if(fields != null && fields.length > 0)
		{
			int length = fields.length ;
			Object last = fields[length-1];
		
			
			if(last instanceof PagineOrderby)
			{
				  
				dbutil.setPagineOrderby( ((PagineOrderby)last).getPagineOrderby());
				length = length - 1;
			}
			for(int i = 0; i < length ; i ++)
			{
				
				Object field = fields[i];
				dbutil.setObject(i + 1, field);
			}
		}
		
		
		ListInfo datas = new ListInfo();
		datas.setDatas(dbutil.executePreparedForList(dbOptions,beanType,rowhandler));
//		datas.setTotalSize(dbutil.getLongTotalSize());
		datas.setMore(true);
		datas.setResultSize(dbutil.size());
		datas.setMaxPageItems(pagesize);
		return datas;		 
	}
	
	public static ListInfo queryListInfoWithDBName2ndTotalsizeByRowHandler(DBOptions dbOptions,RowHandler rowhandler,Class<?> beanType,String dbname, SQLInfo sql, long offset,int pagesize,long totalsize,Object... fields) throws SQLException
	{
		
		SQLInfoDBUtil dbutil = new SQLInfoDBUtil();
		dbutil.preparedSelect(dbname, sql,offset,pagesize,totalsize);
		if(fields != null && fields.length > 0)
		{
			int length = fields.length ;
			Object last = fields[length-1];
		
			
			if(last instanceof PagineOrderby)
			{
				  
				dbutil.setPagineOrderby( ((PagineOrderby)last).getPagineOrderby());
				length = length - 1;
			}
			for(int i = 0; i < length ; i ++)
			{
				
				Object field = fields[i];
				dbutil.setObject(i + 1, field);
			}
		}
		
		
		ListInfo datas = new ListInfo();
		datas.setDatas(dbutil.executePreparedForList(dbOptions,beanType,rowhandler));
		datas.setTotalSize(dbutil.getLongTotalSize());
		datas.setMaxPageItems(pagesize);
		return datas;		 
	}
	
	public static ListInfo queryListInfoWithDBName2ndTotalsizesqlByRowHandler(DBOptions dbOptions,RowHandler rowhandler,Class<?> beanType,String dbname, SQLInfo sql, long offset,int pagesize,SQLInfo totalsizesql,Object... fields) throws SQLException
	{
		
		SQLInfoDBUtil dbutil = new SQLInfoDBUtil();
		dbutil.preparedSelectWithTotalsizesql(dbname, sql,offset,pagesize,totalsizesql);
		if(fields != null && fields.length > 0)
		{
			int length = fields.length ;
			Object last = fields[length-1];
		
			
			if(last instanceof PagineOrderby)
			{
				  
				dbutil.setPagineOrderby( ((PagineOrderby)last).getPagineOrderby());
				length = length - 1;
			}
			for(int i = 0; i < length ; i ++)
			{
				
				Object field = fields[i];
				dbutil.setObject(i + 1, field);
			}
		}
		
		
		ListInfo datas = new ListInfo();
		datas.setDatas(dbutil.executePreparedForList(dbOptions,beanType,rowhandler));
		datas.setTotalSize(dbutil.getLongTotalSize());
		datas.setMaxPageItems(pagesize);
		return datas;		 
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
	public static ListInfo queryListInfoByRowHandler(DBOptions dbOptions,RowHandler rowhandler,Class<?> beanType, SQLInfo sql, long offset,int pagesize,Object... fields) throws SQLException
	{
		return queryListInfoWithDBNameByRowHandler(   dbOptions,rowhandler,beanType, null,sql, offset,pagesize,fields);
	}
	
	public static ListInfo queryListInfoWithTotalsizeByRowHandler(DBOptions dbOptions,RowHandler rowhandler,Class<?> beanType, SQLInfo sql, long offset,int pagesize,long totalsize,Object... fields) throws SQLException
	{
		return queryListInfoWithDBName2ndTotalsizeByRowHandler(   dbOptions,rowhandler,beanType, null,sql, offset,pagesize,totalsize,fields);
	}
	
	public static ListInfo queryListInfoWithTotalsizesqlByRowHandler(DBOptions dbOptions,RowHandler rowhandler,Class<?> beanType, SQLInfo sql, long offset,int pagesize,SQLInfo totalsizesql,Object... fields) throws SQLException
	{
		return queryListInfoWithDBName2ndTotalsizesqlByRowHandler(   dbOptions,rowhandler,beanType, null,sql, offset,pagesize,totalsizesql,fields);
	}
	
	
	public static <T> T queryObjectByRowHandler(DBOptions dbOptions,RowHandler rowhandler,Class<T> beanType, SQLInfo sql, Object... fields) throws SQLException
	{
		return queryObjectWithDBNameByRowHandler(  dbOptions,rowhandler,beanType,(String)null, sql, fields);
		
		 
	}
	
	public static <T> List<T> queryListWithDBNameByRowHandler(DBOptions dbOptions,RowHandler rowhandler,Class<T> beanType,String dbname, SQLInfo sql, Object... fields) throws SQLException
	{
		
		SQLInfoDBUtil dbutil = new SQLInfoDBUtil();
		dbutil.preparedSelect(dbname, sql);
		if(fields != null && fields.length > 0)
		{
			for(int i = 0; i < fields.length ; i ++)
			{
				
				Object field = fields[i];
				dbutil.setObject(i + 1, field);
			}
		}
		
		
		
		return dbutil.executePreparedForList(dbOptions,beanType,rowhandler);
	}
	
	
	public static <T> T queryObjectWithDBNameByRowHandler(DBOptions dbOptions,RowHandler rowhandler,Class<T> beanType,String dbname, SQLInfo sql, Object... fields) throws SQLException
	{
		
		SQLInfoDBUtil dbutil = new SQLInfoDBUtil();
		dbutil.preparedSelect(dbname, sql);
		if(fields != null && fields.length > 0)
		{
			for(int i = 0; i < fields.length ; i ++)
			{
				
				Object field = fields[i];
				dbutil.setObject(i + 1, field);
			}
		}
		
		return (T)dbutil.executePreparedForObject(dbOptions,beanType,rowhandler);
		 
	}
	
	
	
	
	
	public static void queryByNullRowHandler(DBOptions dbOptions,NullRowHandler rowhandler, SQLInfo sql, Object... fields) throws SQLException
	{
		
		 queryWithDBNameByNullRowHandler(   dbOptions,rowhandler,null, sql, fields);
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
	public static ListInfo queryListInfoWithDBNameByNullRowHandler(DBOptions dbOptions,NullRowHandler rowhandler,String dbname, SQLInfo sql, long offset,int pagesize,Object... fields) throws SQLException
	{
		
		SQLInfoDBUtil dbutil = new SQLInfoDBUtil();
		dbutil.preparedSelect(dbname, sql,offset,pagesize);
		if(fields != null && fields.length > 0)
		{
			int length = fields.length ;
			Object last = fields[length-1];
		
			
			if(last instanceof PagineOrderby)
			{
				  
				dbutil.setPagineOrderby( ((PagineOrderby)last).getPagineOrderby());
				length = length - 1;
			}
			for(int i = 0; i < length ; i ++)
			{
				
				Object field = fields[i];
				dbutil.setObject(i + 1, field);
			}
		}
		
		dbutil.executePreparedWithRowHandler(dbOptions,rowhandler);
		ListInfo datas = new ListInfo();
		
		datas.setTotalSize(dbutil.getLongTotalSize());
		datas.setMaxPageItems(pagesize);
		return datas;		 
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
	public static ListInfo moreListInfoWithDBNameByNullRowHandler(DBOptions dbOptions,NullRowHandler rowhandler,String dbname, SQLInfo sql, long offset,int pagesize,Object... fields) throws SQLException
	{
		
		SQLInfoDBUtil dbutil = new SQLInfoDBUtil();
		dbutil.setMore(true);
		dbutil.preparedSelect(dbname, sql,offset,pagesize);
		if(fields != null && fields.length > 0)
		{
			int length = fields.length ;
			Object last = fields[length-1];
		
			
			if(last instanceof PagineOrderby)
			{
				  
				dbutil.setPagineOrderby( ((PagineOrderby)last).getPagineOrderby());
				length = length - 1;
			}
			for(int i = 0; i < length ; i ++)
			{
				
				Object field = fields[i];
				dbutil.setObject(i + 1, field);
			}
		}
		
		dbutil.executePreparedWithRowHandler(dbOptions,rowhandler);
		ListInfo datas = new ListInfo();
		
//		datas.setTotalSize(dbutil.getLongTotalSize());
		datas.setMore(true);
		datas.setResultSize(dbutil.size());
		datas.setMaxPageItems(pagesize);
		return datas;		 
	}
	public static ListInfo queryListInfoWithDBName2ndTotalsizeByNullRowHandler(DBOptions dbOptions,NullRowHandler rowhandler,String dbname, SQLInfo sql, long offset,int pagesize,long totalsize,Object... fields) throws SQLException
	{
		
		SQLInfoDBUtil dbutil = new SQLInfoDBUtil();
		dbutil.preparedSelect(dbname, sql,offset,pagesize,totalsize);
		if(fields != null && fields.length > 0)
		{
			int length = fields.length ;
			Object last = fields[length-1];
		
			
			if(last instanceof PagineOrderby)
			{
				  
				dbutil.setPagineOrderby( ((PagineOrderby)last).getPagineOrderby());
				length = length - 1;
			}
			for(int i = 0; i < length ; i ++)
			{
				
				Object field = fields[i];
				dbutil.setObject(i + 1, field);
			}
		}
		
		dbutil.executePreparedWithRowHandler(dbOptions,rowhandler);
		ListInfo datas = new ListInfo();
		
		datas.setTotalSize(dbutil.getLongTotalSize());
		datas.setMaxPageItems(pagesize);
		return datas;		 
	}
	public static ListInfo queryListInfoWithDBName2ndTotalsizesqlByNullRowHandler(DBOptions dbOptions,NullRowHandler rowhandler,String dbname, SQLInfo sql, long offset,int pagesize,SQLInfo totalsizesql,Object... fields) throws SQLException
	{
		
		SQLInfoDBUtil dbutil = new SQLInfoDBUtil();
		dbutil.preparedSelectWithTotalsizesql(dbname, sql,offset,pagesize,totalsizesql);
		if(fields != null && fields.length > 0)
		{
			int length = fields.length ;
			Object last = fields[length-1];
		
			
			if(last instanceof PagineOrderby)
			{
				  
				dbutil.setPagineOrderby( ((PagineOrderby)last).getPagineOrderby());
				length = length - 1;
			}
			for(int i = 0; i < length ; i ++)
			{
				
				Object field = fields[i];
				dbutil.setObject(i + 1, field);
			}
		}
		
		dbutil.executePreparedWithRowHandler(dbOptions,rowhandler);
		ListInfo datas = new ListInfo();
		
		datas.setTotalSize(dbutil.getLongTotalSize());
		datas.setMaxPageItems(pagesize);
		return datas;		 
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
	public static ListInfo queryListInfoByNullRowHandler(DBOptions dbOptions,NullRowHandler rowhandler, SQLInfo sql, long offset,int pagesize,Object... fields) throws SQLException
	{
		return queryListInfoWithDBNameByNullRowHandler(   dbOptions,rowhandler,(String) null,sql, offset,pagesize,fields);
	}
	public static ListInfo queryListInfoWithTotalsizeByNullRowHandler(DBOptions dbOptions,NullRowHandler rowhandler, SQLInfo sql, long offset,int pagesize,long totalsize,Object... fields) throws SQLException
	{
		return queryListInfoWithDBName2ndTotalsizeByNullRowHandler(   dbOptions,rowhandler, (String)null,sql, offset,pagesize,totalsize,fields);
	}
	public static ListInfo queryListInfoWithTotalsizesqlByNullRowHandler(DBOptions dbOptions,NullRowHandler rowhandler, SQLInfo sql, long offset,int pagesize,SQLInfo totalsizesql,Object... fields) throws SQLException
	{
		return queryListInfoWithDBName2ndTotalsizesqlByNullRowHandler(  dbOptions, rowhandler,(String) null,sql, offset,pagesize,totalsizesql,fields);
	}
	
	
	
	public static void queryWithDBNameByNullRowHandler(DBOptions dbOptions,NullRowHandler rowhandler,String dbname, SQLInfo sql, Object... fields) throws SQLException
	{
		
		SQLInfoDBUtil dbutil = new SQLInfoDBUtil();
		dbutil.preparedSelect(dbname, sql);
		if(fields != null && fields.length > 0)
		{
			for(int i = 0; i < fields.length ; i ++)
			{
				
				Object field = fields[i];
				dbutil.setObject(i + 1, field);
			}
		}
		
		
		
		 dbutil.executePreparedWithRowHandler(dbOptions,rowhandler);
	}
	
	
	public static <T> List<T> queryListBean(DBOptions dbOptions,Class<T> beanType, SQLInfo sql, Object bean) throws SQLException
	{
		
		return queryListBeanWithDBName(  dbOptions,beanType,null, sql, bean);
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
	public static ListInfo queryListInfoBeanWithDBName(DBOptions dbOptions,Class<?> beanType,String dbname, SQLInfo sql, long offset,int pagesize,long totalsize,Object bean) throws SQLException
	{
		
		SQLInfoDBUtil dbutil = new SQLInfoDBUtil();
		SQLParams params = SQLParams.convertBeanToSqlParams(bean, sql, dbname, PreparedDBUtil.SELECT, null);
		dbutil.preparedSelect(params,dbname, sql,offset,pagesize,totalsize);
		ListInfo datas = new ListInfo();
		datas.setDatas(dbutil.executePreparedForList(dbOptions,beanType));
		datas.setTotalSize(dbutil.getLongTotalSize());
		datas.setMaxPageItems(pagesize);
		return datas;		 
	}
	
	public static ListInfo queryListInfoBeanWithDBName(DBOptions dbOptions,Class<?> beanType,String dbname, SQLInfo sql, long offset,int pagesize,SQLInfo totalsizesql,Object bean) throws SQLException
	{
		
		SQLInfoDBUtil dbutil = new SQLInfoDBUtil();
		SQLParams params = SQLParams.convertBeanToSqlParams(bean, sql, dbname, PreparedDBUtil.SELECT, null);
		dbutil.preparedSelectWithTotalsizesql(params,dbname, sql,offset,pagesize,totalsizesql);
		ListInfo datas = new ListInfo();
		datas.setDatas(dbutil.executePreparedForList(dbOptions,beanType));
		datas.setTotalSize(dbutil.getLongTotalSize());
		datas.setMaxPageItems(pagesize);
		return datas;		 
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
	public static ListInfo queryListInfoBeanWithDBName(DBOptions dbOptions,Class<?> beanType,String dbname, SQLInfo sql, long offset,int pagesize,Object bean) throws SQLException
	{
		
		SQLInfoDBUtil dbutil = new SQLInfoDBUtil();
		SQLParams params = SQLParams.convertBeanToSqlParams(bean, sql, dbname, PreparedDBUtil.SELECT, null);
		dbutil.preparedSelect(params,dbname, sql,offset,pagesize,-1L);
		ListInfo datas = new ListInfo();
		datas.setDatas(dbutil.executePreparedForList(dbOptions,beanType));
		datas.setTotalSize(dbutil.getLongTotalSize());
		datas.setMaxPageItems(pagesize);
		return datas;		 
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
	public static ListInfo moreListInfoBeanWithDBName(DBOptions dbOptions,Class<?> beanType,String dbname, SQLInfo sql, long offset,int pagesize,Object bean) throws SQLException
	{
		
		SQLInfoDBUtil dbutil = new SQLInfoDBUtil();
		SQLParams params = SQLParams.convertBeanToSqlParams(bean, sql, dbname, PreparedDBUtil.SELECT, null);
		dbutil.setMore(true);
		dbutil.preparedSelect(params,dbname, sql,offset,pagesize,-1L);
		ListInfo datas = new ListInfo();
		datas.setDatas(dbutil.executePreparedForList(dbOptions,beanType));
//		datas.setTotalSize(dbutil.getLongTotalSize());
		datas.setMore(true);
		datas.setResultSize(dbutil.size());
		datas.setMaxPageItems(pagesize);
		return datas;		 
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
	public static ListInfo queryListInfoBean(DBOptions dbOptions,Class<?> beanType, SQLInfo sql, long offset,int pagesize,long totalsize,Object bean) throws SQLException
	{
		return queryListInfoBeanWithDBName(  dbOptions,beanType, null,sql, offset,pagesize,totalsize,bean);
	}
	
	public static ListInfo queryListInfoBean(DBOptions dbOptions,Class<?> beanType, SQLInfo sql, long offset,int pagesize,SQLInfo totalsizesql,Object bean) throws SQLException
	{
		return queryListInfoBeanWithDBName(  dbOptions,beanType, null,sql, offset,pagesize,totalsizesql,bean);
	}
	
	public static ListInfo queryListInfoBean(DBOptions dbOptions,Class<?> beanType, SQLInfo sql, long offset,int pagesize,Object bean) throws SQLException
	{
		return queryListInfoBeanWithDBName(  dbOptions,beanType, null,sql, offset,pagesize,-1L,bean);
	}
	
	public static String queryField( DBOptions dbOptions,SQLInfo sql, Object... fields) throws SQLException
	{
		return queryFieldWithDBName(  dbOptions,(String)null, sql, fields);
	}
	public static String queryFieldBean( DBOptions dbOptions,SQLInfo sql, Object bean) throws SQLException
	{
		return queryFieldBeanWithDBName(  dbOptions,(String)null, sql, bean);
	}
	
	public static String queryFieldBeanWithDBName(DBOptions dbOptions,String dbname, SQLInfo sql, Object bean) throws SQLException
	{
		
		SQLInfoDBUtil dbutil = new SQLInfoDBUtil();
		SQLParams params = SQLParams.convertBeanToSqlParams(bean, sql, dbname, PreparedDBUtil.SELECT, null);
		dbutil.preparedSelect(params,dbname, sql);
		
		
		
		
		dbutil.executePrepared(dbOptions);
		if(dbutil.size() > 0)
			return dbutil.getString(0, 0);
		else
		{
			return null;
		}
	}
	
	public static String queryFieldWithDBName(DBOptions dbOptions,String dbname, SQLInfo sql, Object... fields) throws SQLException
	{
		
		SQLInfoDBUtil dbutil = new SQLInfoDBUtil();
		dbutil.preparedSelect(dbname, sql);
		if(fields != null && fields.length > 0)
		{
			for(int i = 0; i < fields.length ; i ++)
			{
				
				Object field = fields[i];
				dbutil.setObject(i + 1, field);
			}
		}
		
		
		
		dbutil.executePrepared(dbOptions);
		if(dbutil.size() > 0)
			return dbutil.getString(0, 0);
		else
		{
			return null;
		}
	}
	
	
	
	/**
	 * 
	 * @param <T>
	 * @param beanType
	 * @param sql
	 * @param fields
	 * @return
	 * @throws SQLException
	 */
	
	
	public static <T> T queryTField( DBOptions dbOptions,Class<T> beanType,SQLInfo sql, Object... fields) throws SQLException
	{
		return queryTFieldWithDBName(  dbOptions,(String)null, beanType,sql, fields);
	}
	public static <T> T queryTFieldBean(DBOptions dbOptions, Class<T> type,SQLInfo sql, Object bean) throws SQLException
	{
		return queryTFieldBeanWithDBName(  dbOptions,(String)null, type,sql, bean);
	}
	
	public static <T> T queryTFieldBeanWithDBName(DBOptions dbOptions,String dbname, Class<T> type,SQLInfo sql, Object bean) throws SQLException
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
		return queryTFieldBeanWithDBName(  dbOptions,dbname, type,(FieldRowHandler<T>)null,sql, bean) ;
	}
	
	public static <T> T queryTFieldWithDBName(DBOptions dbOptions,String dbname, Class<T> type,SQLInfo sql, Object... fields) throws SQLException
	{
		
		return queryTFieldWithDBName(  dbOptions,dbname, type,(FieldRowHandler<T>)null,sql, fields);
	}
	
	public static <T> T queryTField( DBOptions dbOptions,Class<T> type,FieldRowHandler<T> fieldRowHandler,SQLInfo sql, Object... fields) throws SQLException
	{
		return queryTFieldWithDBName(  dbOptions,(String)null, type,fieldRowHandler,sql, fields);
	}
	public static <T> T queryTFieldBean( DBOptions dbOptions,Class<T> type,FieldRowHandler<T> fieldRowHandler,SQLInfo sql, Object bean) throws SQLException
	{
		return queryTFieldBeanWithDBName(  dbOptions,(String)null, type,fieldRowHandler,sql, bean);
	}
	
	public static <T> T queryTFieldBeanWithDBName(DBOptions dbOptions,String dbname, Class<T> type,FieldRowHandler<T> fieldRowHandler,SQLInfo sql, Object bean) throws SQLException
	{
		
		SQLInfoDBUtil dbutil = new SQLInfoDBUtil();
		SQLParams params = SQLParams.convertBeanToSqlParams(bean, sql, dbname, PreparedDBUtil.SELECT, null);
		dbutil.preparedSelect(params,dbname, sql);
		
		
		
		if(fieldRowHandler == null)
		{
			dbutil.executePrepared();
			if(dbutil.size() > 0)
				return (T)ValueObjectUtil.typeCast(dbutil.getObject(0, 0),type);
			else
			{
				return (T)ValueObjectUtil.getDefaultValue(type);
			}
			
		}
		else
		{
			return (T)dbutil.executePreparedForObject(dbOptions,type, fieldRowHandler);
		}
	}
	
	public static <T> T queryTFieldWithDBName(DBOptions dbOptions,String dbname, Class<T> type,FieldRowHandler<T> fieldRowHandler,SQLInfo sql, Object... fields) throws SQLException
	{
		
		SQLInfoDBUtil dbutil = new SQLInfoDBUtil();
		dbutil.preparedSelect(dbname, sql);
		if(fields != null && fields.length > 0)
		{
			for(int i = 0; i < fields.length ; i ++)
			{
				
				Object field = fields[i];
				dbutil.setObject(i + 1, field);
			}
		}
		
		
		
		if(fieldRowHandler == null)
		{
			dbutil.executePrepared();
			if(dbutil.size() > 0)
				return (T)ValueObjectUtil.typeCast(dbutil.getObject(0, 0),type);
			else
			{
				return (T)ValueObjectUtil.getDefaultValue(type);
			}
		}
		else
		{
			return (T)dbutil.executePreparedForObject(dbOptions,type, fieldRowHandler);
		}
	}
	
	
	public static <T> T queryObjectBean(DBOptions dbOptions,Class<T> beanType, SQLInfo sql, Object bean) throws SQLException
	{
		return queryObjectBeanWithDBName(  dbOptions,beanType,null, sql, bean);
		
		 
	}
	
	public static <T> List<T> queryListBeanWithDBName(DBOptions dbOptions,Class<T> beanType,String dbname, SQLInfo sql, Object bean) throws SQLException
	{
		
		SQLInfoDBUtil dbutil = new SQLInfoDBUtil();
		SQLParams params = SQLParams.convertBeanToSqlParams(bean, sql, dbname, PreparedDBUtil.SELECT, null);
		dbutil.preparedSelect(params,dbname, sql);

		
		
		
		
		return dbutil.executePreparedForList(dbOptions,beanType);
	}
	
	public static <T> T queryObjectBeanWithDBName(DBOptions dbOptions,Class<T> beanType,String dbname, SQLInfo sql, Object bean) throws SQLException
	{
		
		SQLInfoDBUtil dbutil = new SQLInfoDBUtil();
		SQLParams params = SQLParams.convertBeanToSqlParams(bean, sql, dbname, PreparedDBUtil.SELECT, null);
		dbutil.preparedSelect(params,dbname, sql);
		return (T)dbutil.executePreparedForObject(dbOptions,beanType);
		 
	}
	
	
	public static <T> List<T> queryListBeanByRowHandler(DBOptions dbOptions,RowHandler rowhandler,Class<T> beanType, SQLInfo sql, Object bean) throws SQLException
	{
		
		return queryListBeanWithDBNameByRowHandler(  dbOptions,rowhandler,beanType,null, sql, bean);
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
	public static ListInfo queryListInfoBeanWithDBNameByRowHandler(DBOptions dbOptions,RowHandler rowhandler,Class<?> beanType,String dbname, SQLInfo sql, long offset,int pagesize,long totalsize,Object  bean) throws SQLException
	{
		
		SQLInfoDBUtil dbutil = new SQLInfoDBUtil();
		SQLParams params = SQLParams.convertBeanToSqlParams(bean, sql, dbname, PreparedDBUtil.SELECT, null);
		dbutil.preparedSelect(params,dbname, sql,offset,pagesize,totalsize);
		ListInfo datas = new ListInfo();
		datas.setDatas(dbutil.executePreparedForList(dbOptions,beanType,rowhandler));
		datas.setTotalSize(dbutil.getLongTotalSize());
		datas.setMaxPageItems(pagesize);
		return datas;		 
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
	public static ListInfo moreListInfoBeanWithDBNameByRowHandler(DBOptions dbOptions,RowHandler rowhandler,Class<?> beanType,String dbname, SQLInfo sql, long offset,int pagesize,Object  bean) throws SQLException
	{
		
		SQLInfoDBUtil dbutil = new SQLInfoDBUtil();
		SQLParams params = SQLParams.convertBeanToSqlParams(bean, sql, dbname, PreparedDBUtil.SELECT, null);
		dbutil.setMore(true);
		dbutil.preparedSelect(params,dbname, sql,offset,pagesize,-1);
		ListInfo datas = new ListInfo();
		datas.setDatas(dbutil.executePreparedForList(dbOptions,beanType,rowhandler));
//		datas.setTotalSize(dbutil.getLongTotalSize());
		datas.setMore(true);
		datas.setResultSize(dbutil.size());
		datas.setMaxPageItems(pagesize);
		return datas;		 
	}
	
	public static ListInfo queryListInfoBeanWithDBNameByRowHandler(DBOptions dbOptions,RowHandler rowhandler,Class<?> beanType,String dbname, SQLInfo sql, long offset,int pagesize,SQLInfo totalsizesql,Object  bean) throws SQLException
	{
		
		SQLInfoDBUtil dbutil = new SQLInfoDBUtil();
		SQLParams params = SQLParams.convertBeanToSqlParams(bean, sql, dbname, PreparedDBUtil.SELECT, null);
		dbutil.preparedSelectWithTotalsizesql(params,dbname, sql,offset,pagesize,totalsizesql);
		ListInfo datas = new ListInfo();
		datas.setDatas(dbutil.executePreparedForList(dbOptions,beanType,rowhandler));
		datas.setTotalSize(dbutil.getLongTotalSize());
		datas.setMaxPageItems(pagesize);
		return datas;		 
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
	public static ListInfo queryListInfoBeanWithDBNameByRowHandler(DBOptions dbOptions,RowHandler rowhandler,Class<?> beanType,String dbname, SQLInfo sql, long offset,int pagesize,Object  bean) throws SQLException
	{
		
		SQLInfoDBUtil dbutil = new SQLInfoDBUtil();
		SQLParams params = SQLParams.convertBeanToSqlParams(bean, sql, dbname, PreparedDBUtil.SELECT, null);
		dbutil.preparedSelect(params,dbname, sql,offset,pagesize,-1L);
		ListInfo datas = new ListInfo();
		datas.setDatas(dbutil.executePreparedForList(dbOptions,beanType,rowhandler));
		datas.setTotalSize(dbutil.getLongTotalSize());
		datas.setMaxPageItems(pagesize);
		return datas;		 
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
	public static ListInfo queryListInfoBeanByRowHandler(DBOptions dbOptions,RowHandler rowhandler,Class<?> beanType, SQLInfo sql, long offset,int pagesize,long totalsize,Object bean) throws SQLException
	{
		return queryListInfoBeanWithDBNameByRowHandler(   dbOptions,rowhandler,beanType, null,sql, offset,pagesize,totalsize,bean);
	}
	
	public static ListInfo queryListInfoBeanByRowHandler(  DBOptions dbOptions,RowHandler rowhandler,Class<?> beanType, SQLInfo sql, long offset,int pagesize,SQLInfo totalsizesql,Object bean) throws SQLException
	{
		return queryListInfoBeanWithDBNameByRowHandler(  dbOptions, rowhandler,beanType, null,sql, offset,pagesize,totalsizesql,bean);
	}
	public static ListInfo queryListInfoBeanByRowHandler(DBOptions dbOptions,RowHandler rowhandler,Class<?> beanType, SQLInfo sql, long offset,int pagesize,Object bean) throws SQLException
	{
		return queryListInfoBeanWithDBNameByRowHandler(  dbOptions, rowhandler,beanType, null,sql, offset,pagesize,-1L,bean);
	}
	
	public static <T> T queryObjectBeanByRowHandler(DBOptions dbOptions,RowHandler rowhandler,Class<T> beanType, SQLInfo sql, Object bean) throws SQLException
	{
		return queryObjectBeanWithDBNameByRowHandler(  dbOptions,rowhandler,beanType,null, sql, bean);
		
		 
	}
	
	public static <T> List<T> queryListBeanWithDBNameByRowHandler(DBOptions dbOptions,RowHandler rowhandler,Class<T> beanType,String dbname, SQLInfo sql, Object bean) throws SQLException
	{
		
		SQLInfoDBUtil dbutil = new SQLInfoDBUtil();
		SQLParams params = SQLParams.convertBeanToSqlParams(bean, sql, dbname, PreparedDBUtil.SELECT, null);
		dbutil.preparedSelect(params,dbname, sql);
		
		return dbutil.executePreparedForList(dbOptions,beanType,rowhandler);
	}
	
	
	public static <T> T queryObjectBeanWithDBNameByRowHandler(DBOptions dbOptions,RowHandler rowhandler,Class<T> beanType,String dbname, SQLInfo sql, Object bean) throws SQLException
	{
		
		SQLInfoDBUtil dbutil = new SQLInfoDBUtil();
		SQLParams params = SQLParams.convertBeanToSqlParams(bean, sql, dbname, PreparedDBUtil.SELECT, null);
		dbutil.preparedSelect(params,dbname, sql);		
		return (T)dbutil.executePreparedForObject(dbOptions,beanType,rowhandler);
		 
	}
	
	
	
	
	
	public static void queryBeanByNullRowHandler(DBOptions dbOptions,NullRowHandler rowhandler, SQLInfo sql, Object bean) throws SQLException
	{
		
		 queryBeanWithDBNameByNullRowHandler(  dbOptions, rowhandler,null, sql, bean);
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
	public static ListInfo queryListInfoBeanWithDBNameByNullRowHandler(DBOptions dbOptions,NullRowHandler rowhandler,String dbname, SQLInfo sql, long offset,int pagesize,long totalsize,Object bean) throws SQLException
	{
		
		SQLInfoDBUtil dbutil = new SQLInfoDBUtil();
		SQLParams params = SQLParams.convertBeanToSqlParams(bean, sql, dbname, PreparedDBUtil.SELECT, null);
		dbutil.preparedSelect(params,dbname, sql,offset,pagesize,totalsize);
		dbutil.executePreparedWithRowHandler(dbOptions,rowhandler);
		ListInfo datas = new ListInfo();
		
		datas.setTotalSize(dbutil.getLongTotalSize());
		datas.setMaxPageItems(pagesize);
		return datas;		 
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
	public static ListInfo moreListInfoBeanWithDBNameByNullRowHandler(DBOptions dbOptions,NullRowHandler rowhandler,String dbname, SQLInfo sql, long offset,int pagesize,Object bean) throws SQLException
	{
		
		SQLInfoDBUtil dbutil = new SQLInfoDBUtil();
		dbutil.setMore(true);
		SQLParams params = SQLParams.convertBeanToSqlParams(bean, sql, dbname, PreparedDBUtil.SELECT, null);
		dbutil.preparedSelect(params,dbname, sql,offset,pagesize,-1);
		dbutil.executePreparedWithRowHandler(dbOptions,rowhandler);
		ListInfo datas = new ListInfo();
		
		datas.setMore(true);
		datas.setResultSize(dbutil.size());
		datas.setMaxPageItems(pagesize);
		return datas;		 
	}

	
	public static ListInfo queryListInfoBeanWithDBNameByNullRowHandler(DBOptions dbOptions,NullRowHandler rowhandler,String dbname, SQLInfo sql, long offset,int pagesize,SQLInfo totalsizesql,Object bean) throws SQLException
	{
		
		SQLInfoDBUtil dbutil = new SQLInfoDBUtil();
		SQLParams params = SQLParams.convertBeanToSqlParams(bean, sql, dbname, PreparedDBUtil.SELECT, null);
		dbutil.preparedSelectWithTotalsizesql(params,dbname, sql,offset,pagesize,totalsizesql);
		dbutil.executePreparedWithRowHandler(dbOptions,rowhandler);
		ListInfo datas = new ListInfo();
		
		datas.setTotalSize(dbutil.getLongTotalSize());
		datas.setMaxPageItems(pagesize);
		return datas;		 
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
	public static ListInfo queryListInfoBeanWithDBNameByNullRowHandler(DBOptions dbOptions,NullRowHandler rowhandler,String dbname, SQLInfo sql, long offset,int pagesize,Object bean) throws SQLException
	{
		
		SQLInfoDBUtil dbutil = new SQLInfoDBUtil();
		SQLParams params = SQLParams.convertBeanToSqlParams(bean, sql, dbname, PreparedDBUtil.SELECT, null);
		dbutil.preparedSelect(params,dbname, sql,offset,pagesize,-1L);
		dbutil.executePreparedWithRowHandler(dbOptions,rowhandler);
		ListInfo datas = new ListInfo();
		
		datas.setTotalSize(dbutil.getLongTotalSize());
		datas.setMaxPageItems(pagesize);
		return datas;		 
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
	public static ListInfo queryListInfoBeanByNullRowHandler(DBOptions dbOptions,NullRowHandler rowhandler, SQLInfo sql, long offset,int pagesize,long totalsize,Object bean) throws SQLException
	{
		return queryListInfoBeanWithDBNameByNullRowHandler(   dbOptions,rowhandler, null,sql, offset,pagesize,totalsize,bean);
	}
	
	public static ListInfo queryListInfoBeanByNullRowHandler(DBOptions dbOptions,NullRowHandler rowhandler, SQLInfo sql, long offset,int pagesize,SQLInfo totalsizesql,Object bean) throws SQLException
	{
		return queryListInfoBeanWithDBNameByNullRowHandler(   dbOptions,rowhandler,(String) null,sql, offset,pagesize,totalsizesql,bean);
	}
	
	public static ListInfo queryListInfoBeanByNullRowHandler(DBOptions dbOptions,NullRowHandler rowhandler, SQLInfo sql, long offset,int pagesize,Object bean) throws SQLException
	{
		return queryListInfoBeanWithDBNameByNullRowHandler(   dbOptions,rowhandler, null,sql, offset,pagesize,-1L,bean);
	}
	
	public static void queryBeanWithDBNameByNullRowHandler(DBOptions dbOptions,NullRowHandler rowhandler,String dbname, SQLInfo sql, Object bean) throws SQLException
	{
		
		SQLInfoDBUtil dbutil = new SQLInfoDBUtil();
		SQLParams params = SQLParams.convertBeanToSqlParams(bean, sql, dbname, PreparedDBUtil.SELECT, null);
		dbutil.preparedSelect(params,dbname, sql);
		 dbutil.executePreparedWithRowHandler(dbOptions,rowhandler);
	}

}
