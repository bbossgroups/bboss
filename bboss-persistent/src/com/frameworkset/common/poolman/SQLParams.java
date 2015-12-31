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

import java.io.File;
import java.io.InputStream;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.log4j.Logger;
import org.frameworkset.persitent.util.SQLInfo;
import org.frameworkset.persitent.util.SQLUtil;
import org.frameworkset.util.BigFile;
import org.frameworkset.util.ClassUtil;
import org.frameworkset.util.ClassUtil.ClassInfo;
import org.frameworkset.util.ClassUtil.PropertieDescription;
import org.frameworkset.util.annotations.wraper.ColumnWraper;

import com.frameworkset.common.poolman.sql.IdGenerator;
import com.frameworkset.common.poolman.util.JDBCPool;
import com.frameworkset.common.poolman.util.SQLManager;
import com.frameworkset.orm.annotation.PrimaryKey;
import com.frameworkset.util.ColumnEditorInf;
import com.frameworkset.util.ColumnToFieldEditor;
import com.frameworkset.util.ColumnType;
import com.frameworkset.util.StringUtil;
import com.frameworkset.util.VariableHandler;
import com.frameworkset.util.VariableHandler.SQLStruction;
import com.frameworkset.util.VariableHandler.Variable;

import bboss.org.apache.velocity.VelocityContext;

/**
 * <p>Title: SQLParams.java</p>
 *
 * <p>Description: 封装sql上层应用传递的预编译参数</p>
 *
 * <p>Copyright: Copyright (c) 2007</p>
 * @Date 2010-3-11 上午09:17:38
 * @author biaoping.yin
 * @version 1.0
 */
public class SQLParams
{
	private PagineOrderby pagineOrderby;
    
	public void setPagineOrderby(PagineOrderby pagineOrderby) {
		this.pagineOrderby = pagineOrderby;
	}
	private String pretoken = "#\\[";
    private String endtoken = "\\]";
    private Map<String,Param> sqlparams = null;
    private Params realParams = null;
    private NewSQLInfo newsql = null;

	private String dbname = null;
    private static Logger log = Logger.getLogger(SQLParams.class);
    /**
     * 用于预编译批处理操作
     */
    private SQLInfo oldsql = null;
    private boolean frommap = false;
    
    public String getDbname()
    {
        return dbname;
    }
    public void setDbname(String dbname)
    {
        this.dbname = dbname;
    }
    /**
     * 数据库操作类型
     * 
     */
    private int action = -1;
    
    
    
    public int getAction()
    {
        return action;
    }
    public void setAction(int action)
    {
        this.action = action;
    }
    /**
     * 不建议使用，该变量已经被parserSQLStructions所取代
     * parserResults任然保留用来存放非#[]类型的变量数据
     */
    @Deprecated 
    private static final Map<String,String[][]> parserResults = new java.util.WeakHashMap<String,String[][]>();
 
//    private static final Map<String,SQLStruction> parserSQLStructions = new java.util.WeakHashMap<String,SQLStruction>();
//    private static final Map<String,SQLStruction> parsertotalsizeSQLStructions = new java.util.WeakHashMap<String,SQLStruction>();
    public NewSQLInfo getNewsql()
    {
        return newsql;
    }
    public Params getRealParams()
    {
        return realParams;
    }
    
    public String toString()
    {
        StringBuffer ret = new StringBuffer();
        ret.append("sql{").append(this.newsql.getNewsql()).append(",").append(this.pagineOrderby == null?"":pagineOrderby.toString(null)).append("},params");
        if(sqlparams != null && sqlparams.size() > 0)
        {
            ret.append("{");
            Set<Map.Entry<String,Param>> ent = sqlparams.entrySet();
            Iterator<Map.Entry<String,Param>> it = ent.iterator();
            while(it.hasNext())
            {
                Map.Entry<String,Param> entry = it.next();                
                ret.append(entry.getValue());
            }               
            ret.append("},action{").append(PreparedDBUtil.convertOperationType(action)).append("},");
        }
        ret.append("Variable identity{pretoken=").append(pretoken).append(",endtoken=").append(endtoken).append("}");
        return ret.toString();
    }
    private final static Object lock = new Object();
    public void buildParams(String dbname) throws SetSQLParamException
    {
    	buildParams(this.oldsql, dbname);
    }
    public void buildParamsNewSQLInfo(String dbname,NewSQLInfo newsql) throws SetSQLParamException
    {
    	buildParams(this.oldsql, dbname,newsql);
    }
    
    

    
    public  VelocityContext buildVelocityContext()
    {
    	
    	
		VelocityContext context_ = new VelocityContext();
	
		Param temp = null;
        if(sqlparams != null && sqlparams.size()>0)
        {
            
    		Iterator<Entry<String, Param>> it = sqlparams.entrySet().iterator();
    		while(it.hasNext())
    		{
    			Entry<String, Param> entry = it.next();
    			temp = entry.getValue();

    			if(!temp.getType().equals(NULL))
    				context_.put(entry.getKey(), temp.getData());
    		}
        }
    	return context_;
    	
    }
    public void buildParams(String sql,String dbname) throws SetSQLParamException
    {
        if(realParams != null)
            return;
//        if(sqlparams == null || this.sqlparams.size() <=0)
//        {
//            this.newsql = new NewSQLInfo(sql);
//            return;
//        }
        if(realParams == null)
        {
        	SQLInfo sqlinfo = SQLUtil.getGlobalSQLUtil().getSQLInfo(sql,true,true);
        	if(this.pretoken.equals("#\\[") && this.endtoken.equals("\\]"))
        		buildParamsByVariableParser(sqlinfo,null,dbname,(NewSQLInfo)null);
        	else
        		buildParamsByRegex( sqlinfo,null,dbname);
            
        }
        
    }
    
    public void buildParams(SQLInfo sql,String dbname) throws SetSQLParamException
    {
    	buildParams( sql,dbname,(NewSQLInfo)null);
        
    }
    public void buildParams(SQLInfo sql,String dbname,NewSQLInfo firstnewsql) throws SetSQLParamException
    {
        if(realParams != null)
            return;
//        if(sqlparams == null || this.sqlparams.size() <=0)
//        {
//        	if(firstnewsql != null)
//        	{
//        		this.newsql = firstnewsql;
//        		return;
//        	}
//        	else
//        	{
//	            this.newsql = new NewSQLInfo(sql.getSql());
//	            newsql.setOldsql(sql);
//	            return;
//        	}
//        }
        if(realParams == null)
        {
        	if(this.pretoken.equals("#\\[") && this.endtoken.equals("\\]"))
        		buildParamsByVariableParser(sql,null,dbname, firstnewsql);
        	else
        		buildParamsByRegex( sql,null,dbname);
            
        }
        
    }
    
    public void buildParams(String sql,String totalsizesql,String dbname) throws SetSQLParamException
    {
        if(realParams != null)
            return;
//        if(sqlparams == null || this.sqlparams.size() <=0)
//        {
//            this.newsql = new NewSQLInfo(sql);
//            newsql.setNewtotalsizesql(totalsizesql);
//            return;
//        }
//        if(realParams == null)
        {
        	if(totalsizesql == null)
        	{
        		SQLInfo sqlinfo = SQLUtil.getGlobalSQLUtil().getSQLInfo(sql,true,true);
	        	if(this.pretoken.equals("#\\[") && this.endtoken.equals("\\]"))
	        		buildParamsByVariableParser(sqlinfo,null,dbname,(NewSQLInfo)null);
	        	else
	        		buildParamsByRegex( sqlinfo,null,dbname);
        	}
        	else
        	{
        		SQLInfo sqlinfo = SQLUtil.getGlobalSQLUtil().getSQLInfo(sql,true,true);
        		SQLInfo totalsizesqlinfo = SQLUtil.getGlobalSQLUtil().getSQLInfo(totalsizesql,true,true);
        		if(this.pretoken.equals("#\\[") && this.endtoken.equals("\\]"))
	        		buildParamsByVariableParser(sqlinfo,totalsizesqlinfo,dbname,(NewSQLInfo)null);
	        	else
	        		buildParamsByRegex( sqlinfo,totalsizesqlinfo,dbname);
        	}
            
        }
        
    }
    
    public void buildParams(SQLInfo sql,SQLInfo totalsizesql,String dbname) throws SetSQLParamException
    {
        if(realParams != null)
            return;
//        if(sqlparams == null || this.sqlparams.size() <=0)
//        {
//            this.newsql = new NewSQLInfo(sql.getSql());
//            this.newsql .setNewtotalsizesql(totalsizesql.getSql());
//            this.newsql.setOldsql(sql);
//            this.newsql.setOldtotalsizesql(totalsizesql);
//            return;
//        }
//        if(realParams == null)
        {
        	if(this.pretoken.equals("#\\[") && this.endtoken.equals("\\]"))
        		buildParamsByVariableParser(sql,totalsizesql,dbname,(NewSQLInfo)null);
        	else
        		buildParamsByRegex( sql,totalsizesql,dbname);
            
        }
        
    }
    
//    private void buildParamsByRegex(String sql,String totalsizesql,String dbname) throws SetSQLParamException
//    {
//    	List<Param> _realParams = new ArrayList<Param>();   
//    	VelocityContext vcontext = buildVelocityContext();
//        sql = this.evaluateSqlTemplate(vcontext,sql);
//        String[][] args =  parserResults.get(sql);
//        if(args == null)
//        {
//            synchronized(lock)
//            {
//            	args =  parserResults.get(sql);
//                if(args == null)
//                {
//                    args = VariableHandler.parser2ndSubstitution(sql, this.pretoken,this.endtoken, "?");
//                    parserResults.put(sql,args);
//                }
//            }
//        }            
//        newsql = args[0][0];
//        if(totalsizesql != null)
//        {
//        	totalsizesql = this.evaluateSqlTemplate(vcontext,totalsizesql);
//	        String[][] totalsizesqlargs =  parserResults.get(totalsizesql);
//	        if(totalsizesqlargs == null)
//	        {
//	            synchronized(lock)
//	            {
//	            	totalsizesqlargs =  parserResults.get(totalsizesql);
//	                if(totalsizesqlargs == null)
//	                {
//	                	totalsizesqlargs = VariableHandler.parser2ndSubstitution(totalsizesql, this.pretoken,this.endtoken, "?");
//	                    parserResults.put(totalsizesql,totalsizesqlargs);
//	                }
//	            }
//	        }            
//	        newtotalsizesql = totalsizesqlargs[0][0];
//        }
//        String vars[] = args[1];  
//        if(vars.length == 0 )
//        {
//        	log.debug("预编译sql语句提示：指定了预编译参数,sql语句中没有包含符合要求的预编译变量，" + this);
////            throw new SetSQLParamException("预编译sql语句非法：指定了预编译参数,sql语句中没有包含符合要求的预编译变量，" + this);
//        }
//        Param temp = null;
//        for(int i = 0;i < vars.length; i ++)
//        {
//            temp = this.sqlparams.get(vars[i]);
//            if(temp == null)
//                throw new SetSQLParamException("未指定绑定变量的值：" 
//                                                + vars[i] 
//                                                + "\r\n" 
//                                                + this);
//            Param newparam = temp.clone();
//            //绑定变量索引从1开始
//            newparam.index = i + 1;
//            _realParams.add(newparam);
//        }
//        
//        this.realParams = new Params(_realParams);
//    }
    
    private void buildParamsByRegex(SQLInfo sqlinfo,SQLInfo totalsizesqlinfo,String dbname) throws SetSQLParamException
    {
    	String sql = null;
    	String totalsizesql = null;
    	List<Param> _realParams = new ArrayList<Param>();   
    	VelocityContext vcontext = null;
    	if(sqlinfo.istpl())
    	{
    		sqlinfo.getSqltpl().process();
    		if(sqlinfo.istpl())
    		{
	    		vcontext = buildVelocityContext();
	    		StringWriter sw = new StringWriter();
	    		sqlinfo.getSqltpl().merge(vcontext, sw);
	    		sql = sw.toString();
    		}
    		else
    		{
    			sql = sqlinfo.getSql();
    		}
    		
    	}
    	else
    	{
    		sql = sqlinfo.getSql();
    	}
//        sql = this.evaluateSqlTemplate(vcontext,sql);
        String[][] args =  parserResults.get(sql);
        if(args == null)
        {
            synchronized(lock)
            {
            	args =  parserResults.get(sql);
                if(args == null)
                {
                    args = VariableHandler.parser2ndSubstitution(sql, this.pretoken,this.endtoken, "?");
                    parserResults.put(sql,args);
                }
            }
        }            
        newsql = new NewSQLInfo(args[0][0]);
        newsql.setOldsql(sqlinfo);
        if(totalsizesqlinfo != null)
        {
        	if(totalsizesqlinfo.istpl())
        	{
        		totalsizesqlinfo.getSqltpl().process();
        		if(totalsizesqlinfo.istpl())
        		{
	        		if(vcontext == null)
	        			vcontext = buildVelocityContext();
	        		StringWriter sw = new StringWriter();
	        		totalsizesqlinfo.getSqltpl().merge(vcontext, sw);
	        		totalsizesql = sw.toString();
        		}
        		else
        		{
        			totalsizesql = totalsizesqlinfo.getSql();
        		}
        		
        	}
        	else
        	{
        		totalsizesql = totalsizesqlinfo.getSql();
        	}
//        	totalsizesql = this.evaluateSqlTemplate(vcontext,totalsizesql);
        	
	        String[][] totalsizesqlargs =  parserResults.get(totalsizesql);
	        if(totalsizesqlargs == null)
	        {
	            synchronized(lock)
	            {
	            	totalsizesqlargs =  parserResults.get(totalsizesql);
	                if(totalsizesqlargs == null)
	                {
	                	totalsizesqlargs = VariableHandler.parser2ndSubstitution(totalsizesql, this.pretoken,this.endtoken, "?");
	                    parserResults.put(totalsizesql,totalsizesqlargs);
	                }
	            }
	        }            
	        newsql.setNewtotalsizesql(totalsizesqlargs[0][0]);
	        newsql.setOldtotalsizesql(totalsizesqlinfo);
	        
        }
        String vars[] = args[1];  
        if(vars.length == 0 )
        {
        	log.debug("预编译sql语句提示：指定了预编译参数,sql语句中没有包含符合要求的预编译变量，" + this);
//            throw new SetSQLParamException("预编译sql语句非法：指定了预编译参数,sql语句中没有包含符合要求的预编译变量，" + this);
        }
        Param temp = null;
        for(int i = 0;i < vars.length; i ++)
        {
            temp = this.sqlparams.get(vars[i]);
            if(temp == null)
                throw new SetSQLParamException("未指定绑定变量的值：" 
                                                + vars[i] 
                                                + "\r\n" 
                                                + this);
            Param newparam = temp.clone();
            //绑定变量索引从1开始
            newparam.index = i + 1;
            _realParams.add(newparam);
        }
        
        this.realParams = new Params(_realParams);
//        if(this.oldsql.fromConfig() && pagineOrderby.isConfig())
//        {
//        	
//        }
//        this.realParams.setPagineOrderby(pagineOrderby);
    }
    

    
    private void buildParamsByVariableParser(SQLInfo sqlinfo,SQLInfo totalsizesqlinfo,String dbname,NewSQLInfo firstnewsql) throws SetSQLParamException
    {
    	String sql = null;
    	String totalsizesql = null;
    	List<Param> _realParams = new ArrayList<Param>();
    	SQLStruction sqlstruction =  null;
    	VelocityContext vcontext = null;
    	if(firstnewsql == null)
    	{
	    	
	    	if(sqlinfo.istpl())
	    	{
	    		sqlinfo.getSqltpl().process();//识别sql语句是不是真正的velocity sql模板
	    		if(sqlinfo.istpl())
	    		{
	    			vcontext = buildVelocityContext();//一个context是否可以被同时用于多次运算呢？
			    	
			    	StringWriter sw = new StringWriter();
			       sqlinfo.getSqltpl().merge(vcontext,sw);
			       sql = sw.toString();
	    		}
	    		else
	    		{
	    			sql = sqlinfo.getSql();
	    		}
		    	
	    	}
	    	else
	    	{
	    		sql = sqlinfo.getSql();
	    	}
	    	
	    	if(sqlinfo.getSqlutil() == null)
	    	{
		        sqlstruction =  SQLUtil.getGlobalSQLUtil().getSQLStruction(sqlinfo,sql);
	    	}
	    	else
	    	{
	    		sqlstruction = sqlinfo.getSqlutil().getSQLStruction(sqlinfo,sql);
	    	}
	        newsql = new NewSQLInfo(sqlstruction.getSql());
	        newsql.setOldsql(sqlinfo);
	        newsql.setSqlstruction(sqlstruction);
	        if(totalsizesqlinfo != null)
	        {
	        	if(totalsizesqlinfo.istpl())
	        	{
	        		totalsizesqlinfo.getSqltpl().process();
	        		if(totalsizesqlinfo.istpl())
	        		{
		        		if(vcontext == null)
		        			vcontext = buildVelocityContext();
		        		StringWriter sw = new StringWriter();
		        		totalsizesqlinfo.getSqltpl().merge(vcontext,sw);
		        		totalsizesql = sw.toString();
	        		}
	        		else
	        			totalsizesql = totalsizesqlinfo.getSql();
	        	}
	        	else
	        	{
	        		totalsizesql = totalsizesqlinfo.getSql();
	        	}
	        	SQLStruction totalsizesqlstruction =  null;
	        	if(totalsizesqlinfo.getSqlutil() == null)//如果sql语句时从配置文件读取，则为每个配置文件定义了一个sql语句结构缓存容器
	        	{
			        totalsizesqlstruction =   SQLUtil.getGlobalSQLUtil().getTotalsizeSQLStruction(totalsizesqlinfo,totalsizesql);
    
	        	}
	        	else{
	        		totalsizesqlstruction = totalsizesqlinfo.getSqlutil().getTotalsizeSQLStruction(totalsizesqlinfo,totalsizesql);
	        	}
	        	newsql.setOldtotalsizesql(totalsizesqlinfo);
		        String newtotalsizesql = totalsizesqlstruction.getSql();
		        newsql.setNewtotalsizesql(newtotalsizesql);
	        }
    	}
    	else//对于配置文件中读取的sql语句进行批处理增删改时，如果sql语句中没有
    	{
    		this.newsql = firstnewsql;
    		sqlstruction = this.newsql.getSqlstruction();
    	}
//        String vars[] = args[1];  
        if(!sqlstruction.hasVars())
        {
        	//log.debug("预编译sql语句提示：指定了预编译参数,sql语句中没有包含符合要求的预编译变量，" + this.toString());
//            throw new SetSQLParamException("预编译sql语句非法：指定了预编译参数,sql语句中没有包含符合要求的预编译变量，" + this);
        }
        else
        {
	        Param temp = null;
	        List<Variable> vars = sqlstruction.getVariables();
	        for(int i = 0;i < vars.size(); i ++)
	        {
	        	Variable var = vars.get(i);
	            temp = this.sqlparams.get(var.getVariableName());
	            if(temp == null)
	                throw new SetSQLParamException("未指定绑定变量的值：" 
	                                                + var.getVariableName() 
	                                                + "\r\n" 
	                                                + this.toString());
	            Param newparam = temp.clone(var);
	            //绑定变量索引从1开始
	            newparam.index = i + 1;
	            _realParams.add(newparam);
	        }
	        
        }
        
        this.realParams = new Params(_realParams);
        //如果是高效分页查询，则需要计算rownum_over中的orderby条件
        if(pagineOrderby != null )
        {
        	
        	String _pagineOrderby = null;
        	if(!pagineOrderby.isPlain())
        	{
	        	SQLInfo conditionsqlinfo = null;
	        	if(pagineOrderby.isConfig())
	        	{
	        		conditionsqlinfo = sqlinfo.getSQLInfo(dbname, pagineOrderby.getPagineOrderby());
	        		
	        	}
	        	else
	        	{
	        		conditionsqlinfo = SQLUtil.getGlobalSQLUtil().getSQLInfo(pagineOrderby.getPagineOrderby(),true,true);
	        	}
	        	if(conditionsqlinfo == null)
	        		throw new SetSQLParamException(pagineOrderby.toString(":没有找到对应的ROW_NUMBER () OVER() order by 条件语句。"));
	        	if( conditionsqlinfo.istpl())
		    	{
	    			conditionsqlinfo.getSqltpl().process();//识别sql语句是不是真正的velocity sql模板
		    		if(conditionsqlinfo.istpl())
		    		{
		    			if(vcontext == null)
		        			vcontext = buildVelocityContext();
				    	
				    	StringWriter sw = new StringWriter();
				    	conditionsqlinfo.getSqltpl().merge(vcontext,sw);
				    	_pagineOrderby = sw.toString();
		    		}
		    		else
		    		{
		    			_pagineOrderby = conditionsqlinfo.getSql();
		    		}
			    	
		    	}
		    	else
		    	{
		    		_pagineOrderby = conditionsqlinfo.getSql();
		    	}
        	}
        	else
        	{
        		if(pagineOrderby.isConfig())
        			_pagineOrderby = sqlinfo.getPlainSQL(dbname, pagineOrderby.getPagineOrderby());
        		else 
        			_pagineOrderby = pagineOrderby.getPagineOrderby(); 
        	}
        	this.realParams.setPagineOrderby(_pagineOrderby.trim());
        }
        if(sqlstruction.hasVars() )
        {
        	JDBCPool pool = SQLManager.getInstance().getPool(dbname);
        	if(pool != null && pool.showsql())
        	{
        		log.debug("SQL INFO:" + this.toString() );
        	}
        }
        
    }
   
    
    public String getPretoken()
    {
        return pretoken;
    }
    public void setPretoken(String pretoken)
    {
    	if(pretoken != null && !pretoken.equals("")) this.pretoken = pretoken;
    }
    public String getEndtoken()
    {
        return endtoken;
    }
    public void setEndtoken(String endtoken)
    {
    	if(endtoken != null && !endtoken.equals(""))this.endtoken = endtoken;
    }
    
    public Map<String, Param> getParams()
    {
        return sqlparams;
    }
    
    
    /**
     * 添加sql参数，由DefaultDataInfoImpl进行处理
     * @param name
     * @param value
     * @param type
     * @throws SetSQLParamException 
     */
    public void addSQLParam(String name, Object value, String type) throws SetSQLParamException
    {   
        addSQLParam( name,  value, -100, type, (String)null,(String)null);
    }
    
    /**
     * 添加sql参数，由DefaultDataInfoImpl进行处理
     * @param name
     * @param value
     * @param type
     * @throws SetSQLParamException 
     */
    public void addSQLParamWithCharset(String name, Object value, String type,String charset) throws SetSQLParamException
    {   
        addSQLParam( name,  value, -100, type, (String)null,charset);
    }
    
    public void addSQLParam(String name, Object value,long size, String type) throws SetSQLParamException
    {   
        addSQLParam( name,  value,  size,type, (String)null,(String)null);
    }
    
    public static final String STRING ="string";
    public static final String INT = "int";
    public static final String LONG = "long";
    public static final String DOUBLE = "double";
    public static final String FLOAT = "float";
    public static final String SHORT = "short";
    public static final String DATE = "date";
    public static final String TIMESTAMP = "timestamp";
    public static final String BIGDECIMAL = "bigdecimal";
    public static final String BOOLEAN = "boolean";
    public static final String BYTE = "byte";
    public static final String TIME = "time";
    public static final String BYTEARRAY = "byte[]";
    public static final String BLOBBYTEARRAY = "blobbyte[]";
    public static final String BLOBFILE = "blobfile";
    public static final String BLOB = "blob";
    public static final String CLOBFILE = "clobfile";
    public static final String CLOB = "clob";
    public static final String OBJECT = "object";
    public static final String NULL = "null";
    public final static class blobbyte
    {
    	public blobbyte(byte data)
    	{
    		this.data = data;
    	}
    	byte data;
    	public byte getData()
    	{
    		return this.data;
    	}
    }
    
    public final static class blobfile
    {
    	public blobfile(File data)
    	{
    		this.data = data;
    	}
    	private File data;
    	public File getData()
    	{
    		return this.data;
    	}
    }
    
    public final static class clobfile
    {
    	public clobfile(File data)
    	{
    		this.data = data;
    	}
    	private File data;
    	public File getData()
    	{
    		return this.data;
    	}
    }
    /**
     * in order to resolver lose hh:mm:ss of date
     * @param value
     * @return
     */
    public static Object handleDate(Object value)
    {
    	if(value == null || value instanceof java.sql.Timestamp)
    		return value;
    	else if(value instanceof java.sql.Date)
    	{
    		return new java.sql.Timestamp(((java.sql.Date)value).getTime());
    	}
    	else  if(value instanceof java.util.Date)
    	{
    		return new java.sql.Timestamp(((java.util.Date)value).getTime());
    	}
    	else
    	{
    		return value;
    	}
    }
    /**
     * 根据java数据类型，获取中性的数据库类型
     * @param clazz
     * @return
     * @throws NestedSQLException 
     */
    public static String getParamJavaType(String fieldName,Class clazz) throws NestedSQLException
    {
    	if(String.class.isAssignableFrom(clazz))
    		return STRING;
    	else if(int.class.isAssignableFrom(clazz) || Integer.class.isAssignableFrom(clazz))
    		return INT;
    	else if(long.class.isAssignableFrom(clazz) || Long.class.isAssignableFrom(clazz))
    		return LONG;
    	else if(double.class.isAssignableFrom(clazz)  || Double.class.isAssignableFrom(clazz))
    		return DOUBLE;
    	else if(float.class.isAssignableFrom(clazz) || Float.class.isAssignableFrom(clazz))
    		return FLOAT;
    	else if(short.class.isAssignableFrom(clazz) || Short.class.isAssignableFrom(clazz))
    		return SHORT;
    	else if(java.sql.Timestamp.class.isAssignableFrom(clazz))
    		return TIMESTAMP;
    	else if(java.sql.Date.class.isAssignableFrom(clazz))
    		return DATE;
//    		return TIMESTAMP;//fixed bug lose hh:mm:ss infomation of date
    	else if(Date.class.isAssignableFrom(clazz))
    		return TIMESTAMP;
    	
    	else if(boolean.class.isAssignableFrom(clazz) || Boolean.class.isAssignableFrom(clazz))
    		return BOOLEAN;
    	else if(byte.class.isAssignableFrom(clazz))
    		return BYTE;
    	else if(java.sql.Time.class.isAssignableFrom(clazz))
    		return TIME;
    	else if(byte[].class.isAssignableFrom(clazz))
    		return BYTEARRAY;
    	else if(blobbyte[].class.isAssignableFrom(clazz))
    		return BLOBBYTEARRAY;
    	else if(blobfile.class.isAssignableFrom(clazz))
    		return BLOBFILE;
    	else if(clobfile.class.isAssignableFrom(clazz))
    		return CLOBFILE;
    	
    	else if(Clob.class.isAssignableFrom(clazz))
    		return CLOB;
    	else if(Blob.class.isAssignableFrom(clazz))
    		return BLOB;
    	else if(BigFile.class.isAssignableFrom(clazz))
    	{
    		log.warn("属性["+fieldName+"]数据类型为："+clazz.getCanonicalName() + ",必须通过@Column注解来指定字段对应的数据库类型为blobfile或者clobfile,例如：@Column(type=\"blobfile\")");
    		return OBJECT;
    	}
    	else
    		return OBJECT;
    	
    	
    }
    
//    public static List<SQLParams> convertBeansToSqlParams(List beans,String sql,String dbname,int action
//    		,Connection con) throws SQLException
//	{
//		if(beans == null)
//			return null;
////		List<SQLParams> batchparams = new ArrayList<SQLParams>(beans.size());
////		for(Object bean:beans)
////		{
////			SQLParams params = convertBeanToSqlParams(bean,sql,dbname,action,con);
////			batchparams.add(params);
////			
////		}
////		return batchparams;
//		return convertBeansToSqlParams(beans,new SQLInfo(sql,true,false),dbname,action
//	    		,con);
//	}
    
    public static List<SQLParams> convertBeansToSqlParams(List beans,SQLInfo sql,String dbname,int action
    		,Connection con) throws SQLException
	{
		if(beans == null)
			return null;
		List<SQLParams> batchparams = new ArrayList<SQLParams>(beans.size());
		for(Object bean:beans)
		{
			SQLParams params = convertBeanToSqlParams(bean,sql,dbname,action,con);
			batchparams.add(params);
			
		}
		return batchparams;
	}
//    public static SQLParams convertMaptoSqlParams(Map<String,Object> bean,String sql) throws SetSQLParamException
//    {
//    	if(bean == null || bean.size() == 0)
//			return null;
////    	SQLParams temp = new SQLParams();
////		temp.setOldsql(sql);
////		Iterator<Map.Entry<String,Object>> its = bean.entrySet().iterator();
////		while(its.hasNext())
////		{
////			Map.Entry<String,Object> entrie = its.next();
////			temp.addSQLParam(entrie.getKey(), entrie.getValue(), SQLParams.OBJECT);
////		}		
////		return temp;
//    	return convertMaptoSqlParams(bean,new SQLInfo (sql,true,false));
//    }
    public static SQLParams convertMaptoSqlParams(Map<String,Object> bean,SQLInfo sql) throws SetSQLParamException
    {
    	if(bean == null || bean.size() == 0)
    	{
//			return null;
    		SQLParams temp = new SQLParams();
        	temp.setFrommap(true);
    		temp.setOldsql(sql);
    		return temp;
    	}
//    	if(bean.size() == 0)
//			return null;
    	SQLParams temp = new SQLParams();
    	temp.setFrommap(true);
		temp.setOldsql(sql);
		Iterator<Map.Entry<String,Object>> its = bean.entrySet().iterator();
		while(its.hasNext())
		{
			Map.Entry<String,Object> entrie = its.next();
			temp.addSQLParam(entrie.getKey(), entrie.getValue(), SQLParams.OBJECT);
		}		
		return temp;
    }
	public static SQLParams convertBeanToSqlParams(Object bean,SQLInfo sql,String dbname,int action,Connection con) throws SQLException
	{
		PagineOrderby pagineOrderby = null;
		if(bean == null)
		{
//			return null;
			SQLParams temp = new SQLParams();
        	temp.setFrommap(true);
    		temp.setOldsql(sql);
    		return temp;
		}
		else if(bean instanceof SQLParams)
		{
			SQLParams temp = (SQLParams)bean;
			if(temp.getOldsql() == null)
			{
				temp.setOldsql(sql);
			}
			return temp;
		}
		else if(bean instanceof Map)
		{
			
			SQLParams temp = convertMaptoSqlParams((Map )bean,sql);
			return temp;
		}
		else if(action == PreparedDBUtil.SELECT && bean instanceof PagineOrderby)
		{
			  pagineOrderby = ((PagineOrderby)bean);
			Object condition = pagineOrderby.getConditionBean();
			if(condition == null)
			{
//				return null;
				SQLParams temp = new SQLParams();
	        	temp.setFrommap(true);
	    		temp.setOldsql(sql);
	    		temp.setPagineOrderby(pagineOrderby);
	    		return temp;
			}
			else if(condition instanceof SQLParams)
			{
				SQLParams temp = (SQLParams)condition;
				if(temp.getOldsql() == null)
				{
					temp.setOldsql(sql);
				}
				temp.setPagineOrderby(pagineOrderby );
				return temp;
			}
			else if(condition instanceof Map)
			{
				
				SQLParams temp = convertMaptoSqlParams((Map )condition,sql);
				temp.setPagineOrderby(pagineOrderby );
				return temp;
			}
			else
			{
				 
				bean = pagineOrderby.getConditionBean();
			}
		}
		SQLParams params = new SQLParams();
		if(pagineOrderby != null)
			params.setPagineOrderby(pagineOrderby);
//		BeanInfo beanInfo = null;
//		try {
//			beanInfo = Introspector.getBeanInfo(bean.getClass());
//		} catch (Exception e) {
//			throw new PropertyAccessException(new PropertyChangeEvent(bean, "",
//				     null, null),"获取bean 信息失败",e);
//		}
		ClassInfo beanInfo = ClassUtil.getClassInfo(bean.getClass());
		params.setOldsql(sql);
		String name = null;
		String dataformat = null;
		String charset = null;
		Object value =  null;
		Class type = null;
	
//		Method writeMethod = null;
		String sqltype = null;
		List<PropertieDescription> attributes = beanInfo.getPropertyDescriptors();
		for(int i = 0; attributes != null && i < attributes.size();i ++ )
		{
			PropertieDescription property = attributes.get(i);
			ColumnWraper column = property.getColumn();
			if(column!= null && (column.ignoreCUDbind() || column.ignorebind()))
				continue;
//			if(property.getName().equals("class"))
//				continue;
			type = property.getPropertyType();
//			try
//			{
//				readMethod = property.getReadMethod();
//				if(readMethod == null)
//					continue;
//			}
//			catch(Exception e)
//			{
//				continue;
//			}
			
			
			
			
			try {
				if(property.canread())
				{
					
						
						
					try {						
						value =  property.getValue(bean);
					}
					catch(InvocationTargetException e1)
					{
						log.error("获取属性["+beanInfo.getClazz().getName()+"."+property.getName()+"]值失败：",e1.getTargetException());
					} catch (Exception e1) {
						log.error("获取属性["+beanInfo.getClazz().getName()+"."+property.getName()+"]值失败：",e1);
					}
//					Field field = null;
//					try
//					{
//						
//						field = ClassUtil.getDeclaredField(beantype,property.getName());
//	//					if(field == null)
//	//					{
//	//						continue;
//	//					}
//					}
//					catch(Exception e)
//					{
//						log.info(property.getName() + " is not a field of bean[" +bean.getClass().getCanonicalName() + "].");
//	//					continue;
//					}
					name = property.getName();
					PrimaryKey pka = property.getPk();
					if( pka != null)
					{
						if(pka.auto() && action == PreparedDBUtil.INSERT)
						{
							String pkname = pka.pkname();
							if(StringUtil.isNotEmpty(pkname))
							{
							
								if(type == long.class || type == int.class
										|| type == Long.class || type == Integer.class)
								{
									
									long _value = DBUtil.getNextPrimaryKey(con,dbname,pkname);
									if(type == int.class)
										value = (int)_value;
									else  if(type == Integer.class)
										value = new Integer((int)_value);
									else if(type == Long.class)
										value = new Long(_value);
									else 
										value = _value;
								}
								else 
									value = DBUtil.getNextStringPrimaryKey(con,dbname,pkname);
							}
							else
							{
								IdGenerator idGenerator = SQLManager.getInstance().getPool(dbname).getIdGenerator();
								value = idGenerator.getNextId();
							}
							//设置主键到对象中
//								Method writeMethod = null;
//								try
//								{
//									writeMethod = property.getWriteMethod();
//									if(writeMethod == null)
//										continue;
//								}
//								catch(Exception e)
//								{
//									continue;
//								}
//								writeMethod.invoke(bean, value);
							if(property.canwrite())
							{
								
								property.setValue(bean, value);
//								
							}
							else
							{
								continue;
							}
						}
					}
					
					
					if(column != null)
					{
						ColumnEditorInf editor = column.editor();
						if(editor == null || editor instanceof ColumnToFieldEditor)
						{

							dataformat = column.dataformat();
							charset = column.charset();
							
							String type_ = column.type();
							if(type_ != null )
							{
								if(type_.equals("clob"))
								{
									type = Clob.class;
								}
								else if(type_.equals("blob"))
								{
									type = Blob.class;
								}
								else if(type_.equals("blobfile"))
								{
									type = blobfile.class;
								}
								else if(type_.equals("clobfile"))
								{
									type = clobfile.class;
								}
								else if(type_.equals("blobbyte[]"))
								{
									type = blobbyte[].class;
								}
							}
						}
						else
						{	
							Object cv = editor.toColumnValue(column, value);
							if(cv == null)
								throw new NestedSQLException("转换属性["+beanInfo.getClazz().getName()+"."+property.getName()+"]值失败：值为null时，转换器必须返回ColumnType类型的对象,用来指示表字段对应的java类型。");
							 
							if(!(cv instanceof ColumnType))
							{
								value = cv;
								type = value.getClass();
								
							}
							else
							{
								type = ((ColumnType)cv).getType();
							}
						}
						
					}
					
					sqltype = SQLParams.getParamJavaType(name,type);
					params.addSQLParam(name, value, sqltype, dataformat,charset);
					
				}
				name = null; value = null; sqltype = null; 
				dataformat = null;
				charset = null;
				
				
			} catch (SecurityException e) {
				throw new NestedSQLException(e);
			} catch (IllegalArgumentException e) {
				throw new NestedSQLException(e);
			} catch (Exception e) {
				throw new NestedSQLException(e);
			} 
//			catch (InvocationTargetException e) {
//				throw new NestedSQLException(e);
//			}
			
		
			
		}
		
		return params;
		
	}
    private Object handleData(String name,Object value, String type,String dataformat) throws SetSQLParamException 
    {
        if(type.equals(STRING))  
        {
            if(value instanceof String)
                return value ;
            else
                return String.valueOf(value);
        }
        else if(type.equals(INT))   
        {
            if(value instanceof Integer)
                return value;
            return Integer.parseInt(value.toString()) ;
        }
        else if(type.equals(LONG))   
        {
            if(value instanceof Long)
                return value;
            return Long.parseLong(value.toString()) ;
        }
        else if(type.equals(DOUBLE))
        {
            if(value instanceof Double)
                return value;
            return Double.parseDouble(value.toString());
        }
        else if(type.equals(FLOAT))
        {
            if(value instanceof Float)
                return value;
            return Float.parseFloat(value.toString()) ;
        }
        else if(type.equals(SHORT))
        {
            if(value instanceof Short)
                return value;
            return Short.parseShort(value.toString()) ;
        }
        else if(type.equals(DATE))
        {
        	if(value instanceof java.sql.Timestamp)
            	return value;
        	else if(value instanceof java.sql.Date)
            	return new java.sql.Timestamp(((java.sql.Date)value).getTime());
        	else if(value instanceof java.util.Date)
            {
            	return new java.sql.Timestamp(((java.util.Date)value).getTime());
            }
            try
            {
                return PreparedDBUtil.getDBAdapter(dbname).getDate(value.toString(), dataformat);
            }
            catch (Exception e)
            {
                throw new SetSQLParamException("非法绑定变量的值或格式：name"                        
                        + "="
                        + name 
                        + ",value"                        
                        + "="
                        + value
                        + ",type"                        
                        + "="
                        + type
                        + ",dataformat"                        
                        + "="
                        + dataformat
                        ,e);
            }
        }
        else if(type.equals(TIMESTAMP))
        {
            if(value instanceof java.sql.Timestamp)
                return value;
            else if(value instanceof java.sql.Date)
            	return new java.sql.Timestamp(((java.sql.Date)value).getTime());
            else if(value instanceof java.util.Date)
            {
            	return new java.sql.Timestamp(((java.util.Date)value).getTime());
            }
            try
            {
                return PreparedDBUtil.getDBAdapter(dbname).getTimestamp(value.toString(), dataformat);
            }
            catch (Exception e)
            {
                throw new SetSQLParamException("非法绑定变量的值或格式：name"                        
                        + "="
                        + name 
                        + ",value"                        
                        + "="
                        + value
                        + ",type"                        
                        + "="
                        + type
                        + ",dataformat"                        
                        + "="
                        + dataformat
                        ,e);
            }
        }
        else if(type.equals(BIGDECIMAL))
        {
            if(value instanceof Long)
                return value;
            return Long.parseLong(value.toString());
        }
        
        else if(type.equals(BOOLEAN))    
        {
            if(value instanceof Boolean)
                return value;
            return Boolean.parseBoolean(value.toString());
        }
        else if(type.equals(BYTE))    
        {
            if(value instanceof Byte)
                return value;
            return Byte.parseByte(value.toString());
        }
        else if(type.equals(TIME))
        {
            if(value instanceof java.sql.Time)
                return value;
            return Time.valueOf(value.toString()) ;
        }
        else if(type.equals(BYTEARRAY))
        {
            if(value instanceof byte[])
            {
                return value;
            }
            else
            {
                return value.toString().getBytes();
            }
        }
        
        else if(type.equals(BLOBBYTEARRAY))
        {
        	if(value instanceof byte[])
            {
                return value;
            }
            else
            {
                return value.toString().getBytes();
            }
        }
        else if(type.equals(BLOBFILE))
        {
            if(value instanceof File)
            {
                return value;
            }
            else if(value instanceof InputStream)
            {
               return value;
            }
            else if(value instanceof BigFile)
            {
               return value;
            }
            else
            {
            	 return new File(value.toString());
            }
        }
        else if(type.equals(CLOBFILE))
        {
            if(value instanceof File)
            {
                return value;
            }
            else if(value instanceof InputStream)
            {
               return value;
            }
            else if(value instanceof BigFile)
            {
               return value;
            }
            else
            {
                return new File(value.toString());
            }
        }
        
        else if(type.equals(OBJECT))
        {
            return SQLParams.handleDate(value);
        }
        else
        {
        	return value;
        }
    }
    
    public static void main(String[] args)
    {
        System.out.println(Time.valueOf("10:10:10")) ; 
    }
    
    public void addSQLParam(String name, Object value, String type,String dataformat) throws SetSQLParamException
    {
    	addSQLParam(name, value, -100,type,dataformat,(String)null) ;
    }
    
    public void addSQLParam(String name, Object value, String type,String dataformat,String charset) throws SetSQLParamException
    {
    	addSQLParam(name, value, -100,type,dataformat,charset) ;
    }
    /**
     * 添加sql参数，由DefaultDataInfoImpl进行处理
     * @param name
     * @param value
     * @param type
     * @param charset 指定clob字段读取文件时的字符集utf-8，或者UTF-8
     * @throws SetSQLParamException 
     */
    public void addSQLParam(String name, Object value, long size,String type,String dataformat) throws SetSQLParamException
    {
    	addSQLParam(name, value, size,type,dataformat,(String )null);
        
    }
    /**
     * 添加sql参数，由DefaultDataInfoImpl进行处理
     * @param name
     * @param value
     * @param type
     * @param charset 指定clob字段读取文件时的字符集utf-8，或者UTF-8
     * @throws SetSQLParamException 
     */
    public void addSQLParam(String name, Object value, long size,String type,String dataformat,String charset) throws SetSQLParamException
    {   
        if(sqlparams == null)
        {
            sqlparams = new HashMap<String,Param>();
        }
        Param param = new Param();
        Object data_ = null;
        if(type == null)
        {
            type = STRING;
        }
        type = type.toLowerCase();
        if(value == null)
        {
        	if(!type.equals(OBJECT))
        	{
	            data_ = new Integer(this.converttypeToSqltype(type));
	            type = NULL;            
        	}
        }
        else
        {            
        	
            data_ = handleData(name,value, type,dataformat);
            if(type.equals(DATE))
        	{
        		type = TIMESTAMP;
        	}
        }
        param.setName(name);
        if(size < 0)
        {
        	param.setData(data_);
        }
        else
        {
        	param.setData(new Object[] {data_,size});
        }
        param.setType(type);
        param.setCharset(charset);
        String method = this.converttypeToMethod(type);
        param.setMethod(method);
        param.setDataformat(dataformat);
        this.sqlparams.put(param.getName(), param);
    }
    
    public String converttypeToMethod(String type)
    {
       if(type.equals(STRING))    
            return Param.setString_int_String ;
        else if(type.equals(INT))    
            return Param.setInt_int_int ;
        else if(type.equals(LONG))    
            return Param.setLong_int_long;
        
        else if(type.equals(FLOAT))    
            return Param.setFloat_int_float ;
        else if(type.equals(DOUBLE))    
            return Param.setDouble_int_double ;
        else if(type.equals(SHORT))    
            return Param.setShort_int_short ;
        else if(type.equals(DATE))    
            return Param.setDate_int_sqlDate ;
        else if(type.equals(TIME))    
            return Param.setTime_int_Time ;
        
        else if(type.equals(TIMESTAMP))    
            return Param.setTimestamp_int_Timestamp ;
        else if(type.equals(BLOBFILE))
        {
            return Param.setBlob_int_File;
        }
        else if(type.equals(CLOBFILE))
        {
            return Param.setClob_int_File;
        }
        else if(type.equals(CLOB))
        {
            return Param.setClob_int_String;
        }
        else if(type.equals(BLOB))
        {
            return Param.setBlob_int_blob;
        }
        else if(type.equals(BYTE))    
            return Param.setByte_int_byte ;
        else if(type.equals(BYTEARRAY))    
            return Param.setBytes_int_bytearray;
        else if(type.equals(BLOBBYTEARRAY))
        {
        	return Param.setBlob_int_bytearray;
        }
        else if(type.equals(BOOLEAN))    
            return Param.setBoolean_int_boolean;
        else if(type.equals(BIGDECIMAL))    
            return Param.SET_BigDecimal_INT_BigDecimal;
        else if(type.equals(NULL))    
            return Param.setNull_int_int;
        else              
            return Param.setObject_int_Object ; 
    }
    public int converttypeToSqltype(String type)
    {
        if(type.equals(STRING))    
            return java.sql.Types.VARCHAR;
        else if(type.equals(INT))    
            return java.sql.Types.INTEGER;
        else if(type.equals(DOUBLE))    
            return java.sql.Types.DOUBLE;
        else if(type.equals(FLOAT))    
            return java.sql.Types.FLOAT;
        
        else if(type.equals(LONG))    
            return java.sql.Types.BIGINT;          
        else if(type.equals(SHORT))    
            return java.sql.Types.SMALLINT;
        
        else if(type.equals(DATE))    
            return java.sql.Types.DATE ;
        else if(type.equals(TIME))    
            return java.sql.Types.TIME;
        
        else if(type.equals(TIMESTAMP))    
            return java.sql.Types.TIMESTAMP;
        else if(type.equals(BYTEARRAY))
        {
            return java.sql.Types.VARBINARY;
        }
        
        else if(type.equals(BLOBBYTEARRAY))
        {
            return java.sql.Types.BLOB;
        }
        else if(type.equals(BYTE))    
            return java.sql.Types.BIT; 
        else if(type.equals(BOOLEAN))    
            return java.sql.Types.BOOLEAN;
        else if(type.equals(BIGDECIMAL))    
            return java.sql.Types.BIGINT;
        else if(type.equals(OBJECT))    
            return java.sql.Types.OTHER;
        else              
            return java.sql.Types.OTHER; 
    }
    
    public int size()
    {
        return sqlparams != null ?this.sqlparams.size():0;
    }
	public SQLInfo getOldsql() {
		return oldsql;
	}
	public void setOldsql(String oldsql) {
		
		this.oldsql = SQLUtil.getGlobalSQLUtil().getSQLInfo(oldsql,true,true);
	}
	public void setOldsql(SQLInfo oldsql) {
		this.oldsql = oldsql;
	}
	
	public SQLParams copy()
	{
		SQLParams sqlparams = new SQLParams();
		sqlparams.action = this.action;
		sqlparams.dbname = this.dbname;
		sqlparams.endtoken = this.endtoken;
		sqlparams.newsql = this.newsql;
		sqlparams.oldsql = this.oldsql;
		sqlparams.pretoken = this.pretoken;
		if(this.realParams != null)
			sqlparams.realParams = this.realParams.copy();
		sqlparams.sqlparams = this.sqlparams;		
		return sqlparams;
	}
//	public String getNewtotalsizesql() {
//		return newtotalsizesql;
//	}
//	public void setNewtotalsizesql(String newtotalsizesql) {
//		this.newtotalsizesql = newtotalsizesql;
//	}
	public boolean isFrommap() {
		return frommap;
	}
	public void setFrommap(boolean frommap) {
		this.frommap = frommap;
	}


}
