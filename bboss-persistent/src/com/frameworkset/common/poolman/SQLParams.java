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
import java.lang.reflect.Method;
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
import java.util.Set;

import org.apache.log4j.Logger;
import org.apache.velocity.VelocityContext;
import org.frameworkset.util.BigFile;
import org.frameworkset.util.ClassUtil;
import org.frameworkset.util.ClassUtil.ClassInfo;
import org.frameworkset.util.ClassUtil.PropertieDescription;
import org.frameworkset.util.annotations.ValueConstants;

import com.frameworkset.orm.annotation.Column;
import com.frameworkset.orm.annotation.PrimaryKey;
import com.frameworkset.util.VariableHandler;
import com.frameworkset.util.VariableHandler.SQLStruction;
import com.frameworkset.util.VelocityUtil;

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
    private String pretoken = "#\\[";
    private String endtoken = "\\]";
    private Map<String,Param> sqlparams = null;
    private Params realParams = null;
    private String newsql = null;
    private String dbname = null;
    private static Logger log = Logger.getLogger(SQLParams.class);
    /**
     * 用于预编译批处理操作
     */
    private String oldsql = null;
    
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
    private static final Map<String,SQLStruction> parserSQLStructions = new java.util.WeakHashMap<String,SQLStruction>();
    public String getNewsql()
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
        ret.append("sql{").append(this.newsql).append("},params");
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
    
    private String evaluateSqlTemplate(String sql)
    {
    	try {
    		//@Fixed me
			String realsql = VelocityUtil.evaluate(buildVelocityContext(),
					"evaluateSqlTemplate:" + sql, sql);
			return realsql;
		} catch (Exception e) {
			log.error(e.getMessage());
			return sql;
		}
    	
    	
    }
    
    public  VelocityContext buildVelocityContext()
    {
    	
    	
		VelocityContext context_ = new VelocityContext();
	
		Param temp = null;
        if(sqlparams != null && sqlparams.size()>0)
        {
            
    		Iterator<String> it = sqlparams.keySet().iterator();
    		while(it.hasNext())
    		{
    			String key = it.next();
    			temp = this.sqlparams.get(key);

    			if(!temp.getType().equals(NULL))
    				context_.put(key, temp.getData());
    		}
        }
    	return context_;
    	
    }
    public void buildParams(String sql,String dbname) throws SetSQLParamException
    {
        if(realParams != null)
            return;
        if(sqlparams == null || this.sqlparams.size() <=0)
        {
            this.newsql = sql;
            return;
        }
        if(realParams == null)
        {
            List<Param> _realParams = new ArrayList<Param>();       
            sql = this.evaluateSqlTemplate(sql);
            String[][] args =  parserResults.get(sql);
            if(args == null)
            {
                synchronized(lock)
                {
                    if(args == null)
                    {
                        args = VariableHandler.parser2ndSubstitution(sql, this.pretoken,this.endtoken, "?");
                        parserResults.put(sql,args);
                    }
                }
            }            
            newsql = args[0][0];
            String vars[] = args[1];  
            if(vars.length == 0 )
            {
            	log.info("预编译sql语句提示：指定了预编译参数,sql语句中没有包含符合要求的预编译变量，" + this);
//                throw new SetSQLParamException("预编译sql语句非法：指定了预编译参数,sql语句中没有包含符合要求的预编译变量，" + this);
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
        addSQLParam( name,  value, -100, type, null);
    }
    
    public void addSQLParam(String name, Object value,long size, String type) throws SetSQLParamException
    {   
        addSQLParam( name,  value,  size,type, null);
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
    	else if(Date.class.isAssignableFrom(clazz))
    		return DATE;
    	
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
    
    public static List<SQLParams> convertBeansToSqlParams(List beans,String sql,String dbname,int action
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
    public static SQLParams convertMaptoSqlParams(Map<String,Object> bean,String sql) throws SetSQLParamException
    {
    	if(bean == null || bean.size() == 0)
			return null;
    	SQLParams temp = new SQLParams();
		temp.setOldsql(sql);
		Iterator<Map.Entry<String,Object>> its = bean.entrySet().iterator();
		while(its.hasNext())
		{
			Map.Entry<String,Object> entrie = its.next();
			temp.addSQLParam(entrie.getKey(), entrie.getValue(), SQLParams.OBJECT);
		}		
		return temp;
    }
	public static SQLParams convertBeanToSqlParams(Object bean,String sql,String dbname,int action,Connection con) throws SQLException
	{
		if(bean == null)
			return null;
		if(bean instanceof SQLParams)
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
		SQLParams params = new SQLParams();
		
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
		Object value =  null;
		Class type = null;
		Class beantype = bean.getClass();
		Method readMethod = null;
//		Method writeMethod = null;
		String sqltype = null;
		List<PropertieDescription> attributes = beanInfo.getPropertyDescriptors();
		for(int i = 0; attributes != null && i < attributes.size();i ++ )
		{
			PropertieDescription property = attributes.get(i);
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
					} catch (Exception e1) {
						log.info(e1);
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
					PrimaryKey pka = property.findAnnotation(PrimaryKey.class);
					if( pka != null)
					{
						if(pka.auto() && action == PreparedDBUtil.INSERT)
						{
							String pkname = pka.pkname();
							if(type == long.class || type == int.class
									|| type == Long.class || type == Integer.class)
							{
								
								long _value = DBUtil.getNextPrimaryKey(con,dbname,pkname);
								if(type == int.class)
									value = (int)_value;
								else  if(type == Integer.class)
									value = new Integer((int)_value);
							}
							else 
								value = DBUtil.getNextStringPrimaryKey(con,dbname,pkname);
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
					
					Column column = property.findAnnotation(Column.class);
					if(column != null)
					{
						dataformat = column.dataformat();
						if(dataformat.equals(ValueConstants.DEFAULT_NONE) )
							dataformat = null;
						String type_ = column.type();
						if(!type_.equals(ValueConstants.DEFAULT_NONE) )
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
					
					sqltype = SQLParams.getParamJavaType(name,type);
					params.addSQLParam(name, value, sqltype, dataformat);
					name = null; value = null; sqltype = null; 
					dataformat = null;
				}
				
				
				
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
            if(value instanceof java.sql.Date)
                return value;
            if(value instanceof java.util.Date)
            {
            	return new java.sql.Date(((java.util.Date)value).getTime());
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
    	addSQLParam(name, value, -100,type,dataformat) ;
    }
    /**
     * 添加sql参数，由DefaultDataInfoImpl进行处理
     * @param name
     * @param value
     * @param type
     * @throws SetSQLParamException 
     */
    public void addSQLParam(String name, Object value, long size,String type,String dataformat) throws SetSQLParamException
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
            data_ = new Integer(this.converttypeToSqltype(type));
            type = NULL;            
        }
        else
        {            
            data_ = handleData(name,value, type,dataformat);           
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
        
        else              
            return java.sql.Types.OTHER; 
    }
    
    public int size()
    {
        return sqlparams != null ?this.sqlparams.size():0;
    }
	public String getOldsql() {
		return oldsql;
	}
	public void setOldsql(String oldsql) {
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

}
