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

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.frameworkset.common.poolman.handle.FieldRowHandler;
import com.frameworkset.common.poolman.handle.NullRowHandler;
import com.frameworkset.common.poolman.handle.RowHandler;
import com.frameworkset.util.ListInfo;
import com.frameworkset.util.ValueObjectUtil;

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
    public static void addSQLParam(SQLParams sqlparams,String name, Object value, String type,String dataformat) throws SetSQLParamException
    {   
//        init();
       
        sqlparams.addSQLParam( name,  value,  type, dataformat);
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
	                dbutil.preparedInsert(this.getSQLParams(), this.getDbname(),this.getStatement());
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
	                dbutil.preparedUpdate(this.getSQLParams(), this.getDbname(),this.getStatement());
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
	                dbutil.preparedDelete(this.getSQLParams(), this.getDbname(),this.getStatement());
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
    			dbutil.addPreparedBatch(batchsqlparams);
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
		String old = this.sqlparams.getOldsql();
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
	
	public static void init(SQLParams sqlparams,String statement,String pretoken,String endtoken,String action)
	{
//		this.sqlparams = new SQLParams();
		sqlparams.setOldsql( statement);
		if(action != null)
		{
			if(action.equals(ACTION_INSERT))
				sqlparams.setAction(PreparedDBUtil.INSERT);
			else if(action.equals(ACTION_DELETE))
				sqlparams.setAction(PreparedDBUtil.DELETE);
			else if(action.equals(ACTION_UPDATE))
				sqlparams.setAction(PreparedDBUtil.UPDATE);
		}
		sqlparams.setPretoken(pretoken);
		sqlparams.setEndtoken(endtoken);
		//http://changsha.koubei.com/store/detail--storeId-b227fc4aee6e4862909ea7bf62556a7a
		
		
	}
	
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
		
		init(sqlparams, statement, pretoken, endtoken, action);
		
		
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
		execute( dbname,  sql,  beans,PreparedDBUtil.INSERT);
	}
	
	
	
	
	public static void execute(String dbname, String sql, List beans,boolean isBatchOptimize,int action) throws SQLException
	{
		Connection con = null;
		try
		{
			con = DBUtil.getConection(dbname);
			List<SQLParams> batchsqlparams = SQLParams.convertBeansToSqlParams(beans,sql,dbname,action,con);
			if(batchsqlparams == null)
				return ;
			PreparedDBUtil dbutil = new PreparedDBUtil();
			dbutil.setBatchOptimize(isBatchOptimize);
			dbutil.setPrepareDBName(dbname);
			dbutil.addPreparedBatch(batchsqlparams);
			dbutil.executePreparedBatch(con);
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
	private static Object CUDexecute(String dbname, String sql, Object bean,int action) throws SQLException
	{
		return CUDexecute(dbname, sql, bean,action,false) ;
	}
	/**
	 * 针对增删改三种类型DB操作的统一处理方法
	 * @param dbname
	 * @param sql
	 * @param bean
	 * @param isBatchOptimize
	 * @param action
	 * @return
	 * @throws SQLException
	 */
	private static Object CUDexecute(String dbname, String sql, Object bean,int action,boolean getCUDResult) throws SQLException
	{
		Connection con = null;
		try
		{
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
	        PreparedDBUtil dbutil = new PreparedDBUtil();
	        if(action == PreparedDBUtil.INSERT)
	        {
	            if(batchsqlparams.size() > 0)
	            {
	                dbutil.preparedInsert(batchsqlparams, dbname,sql);
	                return dbutil.executePrepared(con,getCUDResult);
	            }
	            else
	            {
	                return dbutil.executeInsert(dbname,sql,con);
	            }
	        }
	        else if(action == PreparedDBUtil.UPDATE)
	        {
	            if(batchsqlparams.size() > 0)
	            {
	                dbutil.preparedUpdate(batchsqlparams, dbname,sql);
	                return dbutil.executePrepared(con,getCUDResult);
	            }
	            else
	            {
	                return dbutil.executeUpdate(dbname,sql,con);
	            }
	                
	        }
	        else if(action == PreparedDBUtil.DELETE)
	        {
	            if(batchsqlparams.size() > 0)
	            {
	                dbutil.preparedDelete(batchsqlparams, dbname,sql);
	                return dbutil.executePrepared(con,getCUDResult);
	            }
	            else
	            {
	                return dbutil.executeDelete(dbname,sql,con);
	            }
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
	
	
	
	
	

	
	
	
	public static void execute(String dbname, String sql, List beans,int action) throws SQLException
	{
		execute(dbname, sql, beans,false,action);
	}
	
	protected static Object execute(String dbname, String sql,int action, Object... fields) throws SQLException {
//		if(fields == null || fields.length == 0)
//			return null;
		PreparedDBUtil dbutil = new PreparedDBUtil();
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
		
		
		
		return dbutil.executePrepared();	
	}
	
	protected static void executeBatch(String dbname, String sql,int action, Object fields_) throws SQLException {
//		if(fields == null || fields.length == 0)
//			return ;
		PreparedDBUtil dbutil = new PreparedDBUtil();
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
		
		
		
		dbutil.executePreparedBatch();	
	}
	
	
	public static Object update( String sql, Object... fields) throws SQLException {
		return execute(null, sql,PreparedDBUtil.UPDATE, fields);
	}
	



	public static Object delete(String sql, Object... fields) throws SQLException {
		return execute(null, sql,PreparedDBUtil.DELETE, fields);
		
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
		executeBatch(null, sql,PreparedDBUtil.DELETE, fields);
		
	}
	public static void deleteByKeysWithDBName(String dbname,String sql, int... fields) throws SQLException {
		executeBatch(dbname, sql,PreparedDBUtil.DELETE, fields);
		
	}
	
	public static void deleteByLongKeys(String sql, long... fields) throws SQLException {
		executeBatch(null, sql,PreparedDBUtil.DELETE, fields);
		
	}
	public static void deleteByLongKeysWithDBName(String dbname,String sql, long... fields) throws SQLException {
		executeBatch(dbname, sql,PreparedDBUtil.DELETE, fields);
		
	}
	
	public static void deleteByKeys(String sql, String... fields) throws SQLException {
		executeBatch(null, sql,PreparedDBUtil.DELETE, fields);
		
	}
	public static void deleteByKeysWithDBName(String dbname,String sql, String... fields) throws SQLException {
		executeBatch(dbname, sql,PreparedDBUtil.DELETE, fields);
		
	}
	
	public static void deleteByShortKeys(String sql, short... fields) throws SQLException {
		executeBatch(null, sql,PreparedDBUtil.DELETE, fields);
		
	}
	public static void deleteByShortKeysWithDBName(String dbname,String sql, short... fields) throws SQLException {
		executeBatch(dbname, sql,PreparedDBUtil.DELETE, fields);
	}



	public static Object insert(String sql, Object... fields) throws SQLException {
		return execute(null, sql,PreparedDBUtil.INSERT, fields);
	}
	
	public static Object updateWithDBName(String dbname, String sql, Object... fields) throws SQLException {
		return execute(dbname, sql,PreparedDBUtil.UPDATE, fields);
	}
	
	public static Object deleteWithDBName(String dbname, String sql, Object... fields) throws SQLException {
		return execute(dbname, sql,PreparedDBUtil.DELETE, fields);
		
	}



	public static Object insertWithDBName(String dbname, String sql, Object... fields) throws SQLException {
		return execute(dbname, sql,PreparedDBUtil.INSERT, fields);
	}

	public static void updateBeans(String dbname, String sql, List beans) throws SQLException {
		if(beans == null || beans.size() == 0)
			return ;
		execute( dbname,  sql,  beans,PreparedDBUtil.UPDATE);
	}



	public static void deleteBeans(String dbname, String sql, List beans) throws SQLException {
		if(beans == null || beans.size() == 0)
			return ;
		execute( dbname,  sql,  beans,PreparedDBUtil.DELETE);
		
	}



	public static Object insertBean(String dbname, String sql, Object bean) throws SQLException {
		if(bean == null)
			return null;
//		List datas = new ArrayList();
//		datas.add(bean);
//		insertBeans( dbname,  sql,  datas);
		return CUDexecute(dbname, sql, bean,PreparedDBUtil.INSERT);
	}
	
	public static Object insertBean(String dbname, String sql, Object bean,boolean getCUDResult) throws SQLException {
		if(bean == null)
			return null;
//		List datas = new ArrayList();
//		datas.add(bean);
//		insertBeans( dbname,  sql,  datas);
		return CUDexecute(dbname, sql, bean,PreparedDBUtil.INSERT,getCUDResult);
	}



	public static Object updateBean(String dbname, String sql, Object bean) throws SQLException {
		if(bean == null )
			return null;
//		List datas = new ArrayList();
//		datas.add(bean);
//		updateBeans( dbname,  sql,  datas);
		return CUDexecute(dbname, sql, bean,PreparedDBUtil.UPDATE);
	}

	

	public static Object deleteBean(String dbname, String sql, Object bean) throws SQLException {
		
		if(bean == null)
			return null;
//		List datas = new ArrayList();
//		datas.add(bean);
//		deleteBeans( dbname,  sql,  datas);
		return CUDexecute(dbname, sql, bean,PreparedDBUtil.DELETE);
	}
	
	public static void insertBeans(String sql, List beans) throws SQLException {
		insertBeans( null,sql, beans); 
	}
	
	



	public static void updateBeans( String sql, List beans) throws SQLException {
		updateBeans( null,sql, beans); 
	}



	public static void deleteBeans( String sql, List beans) throws SQLException {
		deleteBeans( null,sql, beans); 
		
	}



	public static Object insertBean( String sql, Object bean) throws SQLException {
//		if(bean == null)
//			return ;
//		List datas = new ArrayList();
//		datas.add(bean);
//		insertBeans( null,  sql,  datas);
		return insertBean( (String)null,sql, bean);
	}
	public static Object insertBean( String sql, Object bean,boolean getCUDResult) throws SQLException {
//		if(bean == null)
//			return ;
//		List datas = new ArrayList();
//		datas.add(bean);
//		insertBeans( null,  sql,  datas);
		return insertBean( (String)null,sql, bean,getCUDResult);
	}



	public static Object updateBean( String sql, Object bean) throws SQLException {
//		if(bean == null )
//			return ;
//		List datas = new ArrayList();
//		datas.add(bean);
//		updateBeans( null,  sql,  datas);
		return updateBean( (String)null,sql, bean);
	}

	

	public static Object deleteBean(String sql, Object bean) throws SQLException {
		
//		if(bean == null)
//			return ;
//		List datas = new ArrayList();
//		datas.add(bean);
//		deleteBeans( null,  sql,  datas);
		return deleteBean((String)null,sql, bean);
		
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
		
		PreparedDBUtil dbutil = new PreparedDBUtil();
		dbutil.preparedSelect(dbname, sql,offset,pagesize);
		if(fields != null && fields.length > 0)
		{
			for(int i = 0; i < fields.length ; i ++)
			{
				
				Object field = fields[i];
				dbutil.setObject(i + 1, field);
			}
		}
		
		
		ListInfo datas = new ListInfo();
		datas.setDatas(dbutil.executePreparedForList(beanType));
		datas.setTotalSize(dbutil.getLongTotalSize());
		return datas;		 
	}
	public static ListInfo queryListInfoWithDBName2ndTotalsize(Class<?> beanType,String dbname, String sql, long offset,int pagesize,long totalsize,Object... fields) throws SQLException
	{
		
		PreparedDBUtil dbutil = new PreparedDBUtil();
		dbutil.preparedSelect(dbname, sql,offset,pagesize,totalsize);
		if(fields != null && fields.length > 0)
		{
			for(int i = 0; i < fields.length ; i ++)
			{
				
				Object field = fields[i];
				dbutil.setObject(i + 1, field);
			}
		}
		
		
		ListInfo datas = new ListInfo();
		datas.setDatas(dbutil.executePreparedForList(beanType));
		datas.setTotalSize(dbutil.getLongTotalSize());
		return datas;		 
	}
	public static ListInfo queryListInfoWithDBName2ndTotalsizesql(Class<?> beanType,String dbname, String sql, long offset,int pagesize,String totalsizesql,Object... fields) throws SQLException
	{
		
		PreparedDBUtil dbutil = new PreparedDBUtil();
		dbutil.preparedSelectWithTotalsizesql(dbname, sql,offset,pagesize,totalsizesql);
		if(fields != null && fields.length > 0)
		{
			for(int i = 0; i < fields.length ; i ++)
			{
				
				Object field = fields[i];
				dbutil.setObject(i + 1, field);
			}
		}
		
		
		ListInfo datas = new ListInfo();
		datas.setDatas(dbutil.executePreparedForList(beanType));
		datas.setTotalSize(dbutil.getLongTotalSize());
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
		
		PreparedDBUtil dbutil = new PreparedDBUtil();
		dbutil.preparedSelect(dbname, sql);
		if(fields != null && fields.length > 0)
		{
			for(int i = 0; i < fields.length ; i ++)
			{
				
				Object field = fields[i];
				dbutil.setObject(i + 1, field);
			}
		}
		
		
		
		return dbutil.executePreparedForList(beanType);		 
	}
	
	
	public static <T> T queryObjectWithDBName(Class<T> beanType,String dbname, String sql, Object... fields) throws SQLException
	{
		
		PreparedDBUtil dbutil = new PreparedDBUtil();
		dbutil.preparedSelect(dbname, sql);
		if(fields != null && fields.length > 0)
		{
			for(int i = 0; i < fields.length ; i ++)
			{
				
				Object field = fields[i];
				dbutil.setObject(i + 1, field);
			}
		}
		
		return (T)dbutil.executePreparedForObject(beanType);
		 
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
		
		PreparedDBUtil dbutil = new PreparedDBUtil();
		dbutil.preparedSelect(dbname, sql,offset,pagesize);
		if(fields != null && fields.length > 0)
		{
			for(int i = 0; i < fields.length ; i ++)
			{
				
				Object field = fields[i];
				dbutil.setObject(i + 1, field);
			}
		}
		
		
		ListInfo datas = new ListInfo();
		datas.setDatas(dbutil.executePreparedForList(beanType,rowhandler));
		datas.setTotalSize(dbutil.getLongTotalSize());
		return datas;		 
	}
	
	public static ListInfo queryListInfoWithDBName2ndTotalsizeByRowHandler(RowHandler rowhandler,Class<?> beanType,String dbname, String sql, long offset,int pagesize,long totalsize,Object... fields) throws SQLException
	{
		
		PreparedDBUtil dbutil = new PreparedDBUtil();
		dbutil.preparedSelect(dbname, sql,offset,pagesize,totalsize);
		if(fields != null && fields.length > 0)
		{
			for(int i = 0; i < fields.length ; i ++)
			{
				
				Object field = fields[i];
				dbutil.setObject(i + 1, field);
			}
		}
		
		
		ListInfo datas = new ListInfo();
		datas.setDatas(dbutil.executePreparedForList(beanType,rowhandler));
		datas.setTotalSize(dbutil.getLongTotalSize());
		return datas;		 
	}
	
	public static ListInfo queryListInfoWithDBName2ndTotalsizesqlByRowHandler(RowHandler rowhandler,Class<?> beanType,String dbname, String sql, long offset,int pagesize,String totalsizesql,Object... fields) throws SQLException
	{
		
		PreparedDBUtil dbutil = new PreparedDBUtil();
		dbutil.preparedSelectWithTotalsizesql(dbname, sql,offset,pagesize,totalsizesql);
		if(fields != null && fields.length > 0)
		{
			for(int i = 0; i < fields.length ; i ++)
			{
				
				Object field = fields[i];
				dbutil.setObject(i + 1, field);
			}
		}
		
		
		ListInfo datas = new ListInfo();
		datas.setDatas(dbutil.executePreparedForList(beanType,rowhandler));
		datas.setTotalSize(dbutil.getLongTotalSize());
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
		
		PreparedDBUtil dbutil = new PreparedDBUtil();
		dbutil.preparedSelect(dbname, sql);
		if(fields != null && fields.length > 0)
		{
			for(int i = 0; i < fields.length ; i ++)
			{
				
				Object field = fields[i];
				dbutil.setObject(i + 1, field);
			}
		}
		
		
		
		return dbutil.executePreparedForList(beanType,rowhandler);		 
	}
	
	
	public static <T> T queryObjectWithDBNameByRowHandler(RowHandler rowhandler,Class<T> beanType,String dbname, String sql, Object... fields) throws SQLException
	{
		
		PreparedDBUtil dbutil = new PreparedDBUtil();
		dbutil.preparedSelect(dbname, sql);
		if(fields != null && fields.length > 0)
		{
			for(int i = 0; i < fields.length ; i ++)
			{
				
				Object field = fields[i];
				dbutil.setObject(i + 1, field);
			}
		}
		
		return (T)dbutil.executePreparedForObject(beanType,rowhandler);
		 
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
		
		PreparedDBUtil dbutil = new PreparedDBUtil();
		dbutil.preparedSelect(dbname, sql,offset,pagesize);
		if(fields != null && fields.length > 0)
		{
			for(int i = 0; i < fields.length ; i ++)
			{
				
				Object field = fields[i];
				dbutil.setObject(i + 1, field);
			}
		}
		
		dbutil.executePreparedWithRowHandler(rowhandler);
		ListInfo datas = new ListInfo();
		
		datas.setTotalSize(dbutil.getLongTotalSize());
		return datas;		 
	}
	public static ListInfo queryListInfoWithDBName2ndTotalsizeByNullRowHandler(NullRowHandler rowhandler,String dbname, String sql, long offset,int pagesize,long totalsize,Object... fields) throws SQLException
	{
		
		PreparedDBUtil dbutil = new PreparedDBUtil();
		dbutil.preparedSelect(dbname, sql,offset,pagesize,totalsize);
		if(fields != null && fields.length > 0)
		{
			for(int i = 0; i < fields.length ; i ++)
			{
				
				Object field = fields[i];
				dbutil.setObject(i + 1, field);
			}
		}
		
		dbutil.executePreparedWithRowHandler(rowhandler);
		ListInfo datas = new ListInfo();
		
		datas.setTotalSize(dbutil.getLongTotalSize());
		return datas;		 
	}
	public static ListInfo queryListInfoWithDBName2ndTotalsizesqlByNullRowHandler(NullRowHandler rowhandler,String dbname, String sql, long offset,int pagesize,String totalsizesql,Object... fields) throws SQLException
	{
		
		PreparedDBUtil dbutil = new PreparedDBUtil();
		dbutil.preparedSelectWithTotalsizesql(dbname, sql,offset,pagesize,totalsizesql);
		if(fields != null && fields.length > 0)
		{
			for(int i = 0; i < fields.length ; i ++)
			{
				
				Object field = fields[i];
				dbutil.setObject(i + 1, field);
			}
		}
		
		dbutil.executePreparedWithRowHandler(rowhandler);
		ListInfo datas = new ListInfo();
		
		datas.setTotalSize(dbutil.getLongTotalSize());
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
		
		PreparedDBUtil dbutil = new PreparedDBUtil();
		dbutil.preparedSelect(dbname, sql);
		if(fields != null && fields.length > 0)
		{
			for(int i = 0; i < fields.length ; i ++)
			{
				
				Object field = fields[i];
				dbutil.setObject(i + 1, field);
			}
		}
		
		
		
		 dbutil.executePreparedWithRowHandler(rowhandler);		 
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
		
		PreparedDBUtil dbutil = new PreparedDBUtil();
		SQLParams params = SQLParams.convertBeanToSqlParams(bean, sql, dbname, PreparedDBUtil.SELECT, null);
		dbutil.preparedSelect(params,dbname, sql,offset,pagesize,totalsize);
		ListInfo datas = new ListInfo();
		datas.setDatas(dbutil.executePreparedForList(beanType));
		datas.setTotalSize(dbutil.getLongTotalSize());
		return datas;		 
	}
	
	public static ListInfo queryListInfoBeanWithDBName(Class<?> beanType,String dbname, String sql, long offset,int pagesize,String totalsizesql,Object bean) throws SQLException
	{
		
		PreparedDBUtil dbutil = new PreparedDBUtil();
		SQLParams params = SQLParams.convertBeanToSqlParams(bean, sql, dbname, PreparedDBUtil.SELECT, null);
		dbutil.preparedSelectWithTotalsizesql(params,dbname, sql,offset,pagesize,totalsizesql);
		ListInfo datas = new ListInfo();
		datas.setDatas(dbutil.executePreparedForList(beanType));
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
	 * @param bean
	 * @return
	 * @throws SQLException
	 */
	public static ListInfo queryListInfoBeanWithDBName(Class<?> beanType,String dbname, String sql, long offset,int pagesize,Object bean) throws SQLException
	{
		
		PreparedDBUtil dbutil = new PreparedDBUtil();
		SQLParams params = SQLParams.convertBeanToSqlParams(bean, sql, dbname, PreparedDBUtil.SELECT, null);
		dbutil.preparedSelect(params,dbname, sql,offset,pagesize,-1L);
		ListInfo datas = new ListInfo();
		datas.setDatas(dbutil.executePreparedForList(beanType));
		datas.setTotalSize(dbutil.getLongTotalSize());
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
		
		PreparedDBUtil dbutil = new PreparedDBUtil();
		SQLParams params = SQLParams.convertBeanToSqlParams(bean, sql, dbname, PreparedDBUtil.SELECT, null);
		dbutil.preparedSelect(params,dbname, sql);
		
		
		
		
		dbutil.executePrepared();
		if(dbutil.size() > 0)
			return dbutil.getString(0, 0);
		else
		{
			return null;
		}
	}
	
	public static String queryFieldWithDBName(String dbname, String sql, Object... fields) throws SQLException
	{
		
		PreparedDBUtil dbutil = new PreparedDBUtil();
		dbutil.preparedSelect(dbname, sql);
		if(fields != null && fields.length > 0)
		{
			for(int i = 0; i < fields.length ; i ++)
			{
				
				Object field = fields[i];
				dbutil.setObject(i + 1, field);
			}
		}
		
		
		
		dbutil.executePrepared();
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
		
		PreparedDBUtil dbutil = new PreparedDBUtil();
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
			return (T)dbutil.executePreparedForObject(type, fieldRowHandler);
		}
	}
	
	public static <T> T queryTFieldWithDBName(String dbname, Class<T> type,FieldRowHandler<T> fieldRowHandler,String sql, Object... fields) throws SQLException
	{
		
		PreparedDBUtil dbutil = new PreparedDBUtil();
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
			return (T)dbutil.executePreparedForObject(type, fieldRowHandler);
		}
	}
	
	
	public static <T> T queryObjectBean(Class<T> beanType, String sql, Object bean) throws SQLException
	{
		return queryObjectBeanWithDBName(beanType,null, sql, bean);
		
		 
	}
	
	public static <T> List<T> queryListBeanWithDBName(Class<T> beanType,String dbname, String sql, Object bean) throws SQLException
	{
		
		PreparedDBUtil dbutil = new PreparedDBUtil();
		SQLParams params = SQLParams.convertBeanToSqlParams(bean, sql, dbname, PreparedDBUtil.SELECT, null);
		dbutil.preparedSelect(params,dbname, sql);

		
		
		
		
		return dbutil.executePreparedForList(beanType);		 
	}
	
	public static <T> T queryObjectBeanWithDBName(Class<T> beanType,String dbname, String sql, Object bean) throws SQLException
	{
		
		PreparedDBUtil dbutil = new PreparedDBUtil();
		SQLParams params = SQLParams.convertBeanToSqlParams(bean, sql, dbname, PreparedDBUtil.SELECT, null);
		dbutil.preparedSelect(params,dbname, sql);
		return (T)dbutil.executePreparedForObject(beanType);
		 
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
		
		PreparedDBUtil dbutil = new PreparedDBUtil();
		SQLParams params = SQLParams.convertBeanToSqlParams(bean, sql, dbname, PreparedDBUtil.SELECT, null);
		dbutil.preparedSelect(params,dbname, sql,offset,pagesize,totalsize);
		ListInfo datas = new ListInfo();
		datas.setDatas(dbutil.executePreparedForList(beanType,rowhandler));
		datas.setTotalSize(dbutil.getLongTotalSize());
		return datas;		 
	}
	
	public static ListInfo queryListInfoBeanWithDBNameByRowHandler(RowHandler rowhandler,Class<?> beanType,String dbname, String sql, long offset,int pagesize,String totalsizesql,Object  bean) throws SQLException
	{
		
		PreparedDBUtil dbutil = new PreparedDBUtil();
		SQLParams params = SQLParams.convertBeanToSqlParams(bean, sql, dbname, PreparedDBUtil.SELECT, null);
		dbutil.preparedSelectWithTotalsizesql(params,dbname, sql,offset,pagesize,totalsizesql);
		ListInfo datas = new ListInfo();
		datas.setDatas(dbutil.executePreparedForList(beanType,rowhandler));
		datas.setTotalSize(dbutil.getLongTotalSize());
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
	public static ListInfo queryListInfoBeanWithDBNameByRowHandler(RowHandler rowhandler,Class<?> beanType,String dbname, String sql, long offset,int pagesize,Object  bean) throws SQLException
	{
		
		PreparedDBUtil dbutil = new PreparedDBUtil();
		SQLParams params = SQLParams.convertBeanToSqlParams(bean, sql, dbname, PreparedDBUtil.SELECT, null);
		dbutil.preparedSelect(params,dbname, sql,offset,pagesize,-1L);
		ListInfo datas = new ListInfo();
		datas.setDatas(dbutil.executePreparedForList(beanType,rowhandler));
		datas.setTotalSize(dbutil.getLongTotalSize());
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
		
		PreparedDBUtil dbutil = new PreparedDBUtil();
		SQLParams params = SQLParams.convertBeanToSqlParams(bean, sql, dbname, PreparedDBUtil.SELECT, null);
		dbutil.preparedSelect(params,dbname, sql);
		
		return dbutil.executePreparedForList(beanType,rowhandler);		 
	}
	
	
	public static <T> T queryObjectBeanWithDBNameByRowHandler(RowHandler rowhandler,Class<T> beanType,String dbname, String sql, Object bean) throws SQLException
	{
		
		PreparedDBUtil dbutil = new PreparedDBUtil();
		SQLParams params = SQLParams.convertBeanToSqlParams(bean, sql, dbname, PreparedDBUtil.SELECT, null);
		dbutil.preparedSelect(params,dbname, sql);		
		return (T)dbutil.executePreparedForObject(beanType,rowhandler);
		 
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
		
		PreparedDBUtil dbutil = new PreparedDBUtil();
		SQLParams params = SQLParams.convertBeanToSqlParams(bean, sql, dbname, PreparedDBUtil.SELECT, null);
		dbutil.preparedSelect(params,dbname, sql,offset,pagesize,totalsize);
		dbutil.executePreparedWithRowHandler(rowhandler);
		ListInfo datas = new ListInfo();
		
		datas.setTotalSize(dbutil.getLongTotalSize());
		return datas;		 
	}
	
	public static ListInfo queryListInfoBeanWithDBNameByNullRowHandler(NullRowHandler rowhandler,String dbname, String sql, long offset,int pagesize,String totalsizesql,Object bean) throws SQLException
	{
		
		PreparedDBUtil dbutil = new PreparedDBUtil();
		SQLParams params = SQLParams.convertBeanToSqlParams(bean, sql, dbname, PreparedDBUtil.SELECT, null);
		dbutil.preparedSelectWithTotalsizesql(params,dbname, sql,offset,pagesize,totalsizesql);
		dbutil.executePreparedWithRowHandler(rowhandler);
		ListInfo datas = new ListInfo();
		
		datas.setTotalSize(dbutil.getLongTotalSize());
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
	public static ListInfo queryListInfoBeanWithDBNameByNullRowHandler(NullRowHandler rowhandler,String dbname, String sql, long offset,int pagesize,Object bean) throws SQLException
	{
		
		PreparedDBUtil dbutil = new PreparedDBUtil();
		SQLParams params = SQLParams.convertBeanToSqlParams(bean, sql, dbname, PreparedDBUtil.SELECT, null);
		dbutil.preparedSelect(params,dbname, sql,offset,pagesize,-1L);
		dbutil.executePreparedWithRowHandler(rowhandler);
		ListInfo datas = new ListInfo();
		
		datas.setTotalSize(dbutil.getLongTotalSize());
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
		
		PreparedDBUtil dbutil = new PreparedDBUtil();
		SQLParams params = SQLParams.convertBeanToSqlParams(bean, sql, dbname, PreparedDBUtil.SELECT, null);
		dbutil.preparedSelect(params,dbname, sql);
		 dbutil.executePreparedWithRowHandler(rowhandler);		 
	}
	
	
}
