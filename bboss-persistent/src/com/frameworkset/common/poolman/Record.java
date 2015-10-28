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
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Ref;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Calendar;
import java.util.Map;

import org.apache.log4j.Logger;

import com.frameworkset.common.poolman.handle.ValueExchange;
import com.frameworkset.common.poolman.sql.PoolManResultSetMetaData.WrapInteger;
import com.frameworkset.common.poolman.util.SQLUtil.DBHashtable;
import com.frameworkset.util.ValueObjectUtil;
/**
 * 
 * 
 * <p>Title: Record.java</p>
 *
 * <p>Description: 封装每条记录的详细信息</p>
 *
 * <p>Copyright: Copyright (c) 2007</p>
 *

 * @Date Oct 28, 2008 10:30:27 AM
 * @author biaoping.yin
 * @version 1.0
 */
public class Record extends DBHashtable{
	private String[] fields;
	private Map sameCols = null;
	public Record(int i) {
		super(i);
	}
	
	public Record(int i,String[] fields,Map sameCols) {
		super(i);
		this.fields = fields;
		if(sameCols != null && sameCols.size() > 0)
		    this.sameCols = sameCols;
		
	}
	
	public Record(String[] fields,Map sameCols)
	{
		super();
		this.fields = fields;
		if(sameCols != null && sameCols.size() > 0)
                    this.sameCols = sameCols;
	}
	
	public Record()
	{
		super();
	}
	
	/**
	 * 设置记录对应的数据库原始记录行号
	 */
	private int rowid;
	public void setRowid(int rowid)
	{
	    this.rowid = rowid;
	}
	public int getRowid()
        {
            return rowid;
        }
        
	public Record(int initialCapacity, float loadFactor,String[] fields,Map sameCols)
	{
		super(initialCapacity,loadFactor);
		this.fields = fields;
		if(sameCols != null && sameCols.size() > 0)
                    this.sameCols = sameCols;
	}
	
	public Record(int initialCapacity, float loadFactor)
	{
		super(initialCapacity,loadFactor);
		
	}
	private boolean isnull = true;
	public Record(Map t)
	{
		super(t);
		if(t != null)
			isnull = false;
			
		
	}
	
	public Record(Map t,String[] fields)
	{
		super(t);
		if(t != null)
			isnull = false;
		this.fields = fields;	
		
	}
	
//	private Map data = new HashMap();
	
	
	private static Logger log = Logger.getLogger(Record.class);
	
	
	

    /**
     * Retrieves whether the last OUT parameter read had the value of
     * SQL <code>NULL</code>.  Note that this method should be called only after
     * calling a getter method; otherwise, there is no value to use in 
     * determining whether it is <code>null</code> or not.
     *
     * @return <code>true</code> if the last parameter read was SQL
     * <code>NULL</code>; <code>false</code> otherwise 
     * @exception SQLException if a database access error occurs
     */
    public boolean wasNull() throws SQLException
    {
    	return isnull;
    }

    /**
     * Retrieves the value of the designated JDBC <code>CHAR</code>, 
     * <code>VARCHAR</code>, or <code>LONGVARCHAR</code> parameter as a 
     * <code>String</code> in the Java programming language.
     * <p>
     * For the fixed-length type JDBC <code>CHAR</code>,
     * the <code>String</code> object
     * returned has exactly the same value the JDBC
     * <code>CHAR</code> value had in the
     * database, including any padding added by the database.
     *
     * @param parameterIndex 
     * 					存储过程返回的数据：the first parameter is 1, the second is 2 一般查询的结果集： the first parameter is 0, the second is 1,
     * and so on
     * @return the parameter value. If the value is SQL <code>NULL</code>, 
     *         the result 
     *         is <code>null</code>.
     * @exception SQLException if a database access error occurs
     * @see #setString
     */
    public String getString(int parameterIndex) throws SQLException
    {
    	Object object = this.getObject(parameterIndex);
    	return ValueExchange.getStringFromObject(object);
    	
    }

    /**
     * Retrieves the value of the designated JDBC <code>BIT</code> parameter as a 
     * <code>boolean</code> in the Java programming language.
     *
     * @param parameterIndex 存储过程返回的数据：the first parameter is 1, the second is 2 一般查询的结果集： the first parameter is 0, the second is 1, 
     *        and so on
     * @return the parameter value.  If the value is SQL <code>NULL</code>, 
     *         the result is <code>false</code>.
     * @exception SQLException if a database access error occurs
     * @see #setBoolean
     */
    public boolean getBoolean(int parameterIndex) throws SQLException
    {
    	Boolean value = (Boolean)this.getObject(parameterIndex);
    	if(value != null)
    	{
    		return value.booleanValue();
    	}
    	throw new SQLException("getBoolean(" + parameterIndex + ") failed:value=null.");
    	
    }

    /**
     * Retrieves the value of the designated JDBC <code>TINYINT</code> parameter 
     * as a <code>byte</code> in the Java programming language.
     *
     * @param parameterIndex 存储过程返回的数据：the first parameter is 1, the second is 2 一般查询的结果集： the first parameter is 0, the second is 1, 
     * and so on
     * @return the parameter value.  If the value is SQL <code>NULL</code>, the result 
     * is <code>0</code>.
     * @exception SQLException if a database access error occurs
     * @see #setByte
     */
    public byte getByte(int parameterIndex) throws SQLException
    {
    	Byte byte_ = (Byte)this.getObject(parameterIndex);
    	if(byte_ != null)
    		return byte_.byteValue();
    	throw new SQLException("getByte(" + parameterIndex + ") failed:value=null.");
    }

    /**
     * Retrieves the value of the designated JDBC <code>SMALLINT</code> parameter 
     * as a <code>short</code> in the Java programming language.
     *
     * @param parameterIndex 存储过程返回的数据：the first parameter is 1, the second is 2 一般查询的结果集： the first parameter is 0, the second is 1, 
     * and so on
     * @return the parameter value.  If the value is SQL <code>NULL</code>, the result 
     * is <code>0</code>.
     * @exception SQLException if a database access error occurs
     * @see #setShort
     */
    public short getShort(int parameterIndex) throws SQLException
    {
    	Object value = this.getObject(parameterIndex);
    	return getShortFromObject(value);
//    	Short byte_ = (Short)this.getObject(parameterIndex);
//    	if(byte_ != null)
//    		return byte_.byteValue();
//    	throw new SQLException("getShort(" + parameterIndex + ") failed:value=null.");
    }
    
    public short getShortFromObject(Object value) throws SQLException
    {
    	if(value == null)
    		return 0;
    	else if(value instanceof Short)
    	{
    		Short s = (Short)value;
    		return s.shortValue();
    	}
    	if(value instanceof Integer)
    	{
    		Integer data = (Integer)value;
        	return data.shortValue();
    	}
    	else if(value instanceof BigDecimal)
    	{
    		BigDecimal b = (BigDecimal)value;
    		return b.shortValue();
    	}
    	else if(value instanceof Double)
    	{
    		Double b = (Double)value;
    		return b.shortValue();
    	}
    	
    	else if(value instanceof Float)
    	{
    		Float f = (Float)value;
    		return f.shortValue();
    	}
    	else if(value instanceof Long)
    	{
    		Long l = (Long)value;
    		return l.shortValue();
    	}
    	else
    	{
    		String i = String.valueOf(value);
    		try
    		{
    			return Short.parseShort(i);
    		}
    		catch(Exception e)
    		{
    			throw new NestedSQLException(e);
    		}
    	}
    }
    
    public int getIntFromObject(Object value) throws SQLException
    {
    	if(value == null)
    		return 0;
    	else if(value instanceof Integer)
    	{
    		Integer data = (Integer)value;
        	return data.intValue();
    	}
    	else if(value instanceof Short)
    	{
    		Short s = (Short)value;
    		return s.intValue();
    	}
    	
    	else if(value instanceof BigDecimal)
    	{
    		BigDecimal b = (BigDecimal)value;
    		return b.intValue();
    	}
    	else if(value instanceof Double)
    	{
    		Double b = (Double)value;
    		return b.intValue();
    	}
    	
    	else if(value instanceof Float)
    	{
    		Float f = (Float)value;
    		return f.intValue();
    	}
    	else if(value instanceof Long)
    	{
    		Long l = (Long)value;
    		return l.intValue();
    	}
    	else
    	{
    		String i = String.valueOf(value);
    		try
    		{
    			return Integer.parseInt(i);
    		}
    		catch(Exception e)
    		{
    			throw new NestedSQLException(e);
    		}
    	}
    }
    
    public float getFloatFromObject(Object value) throws SQLException
    {
    	if(value == null)
    		return 0;
    	else if(value instanceof Float)
    	{
    		Float f = (Float)value;
    		return f.floatValue();
    	}
    	if(value instanceof Integer)
    	{
    		Integer data = (Integer)value;
        	return data.floatValue();
    	}
    	else if(value instanceof BigDecimal)
    	{
    		BigDecimal b = (BigDecimal)value;
    		return b.floatValue();
    	}
    	else if(value instanceof Short)
    	{
    		Short s = (Short)value;
    		return s.floatValue();
    	}
    	
    	else if(value instanceof Long)
    	{
    		Long l = (Long)value;
    		return l.floatValue();
    	}
    	else if(value instanceof Double)
    	{
    		Double l = (Double)value;
    		return l.floatValue();
    	}
    	else
    	{
    		String i = String.valueOf(value);
    		try
    		{
    			return Float.parseFloat(i);
    		}
    		catch(Exception e)
    		{
    			throw new NestedSQLException(e);
    		}
    	}
    }
    
    public double getDoubleFromObject(Object value) throws SQLException
    {
    	if(value == null)
    		return 0;
    	else if(value instanceof Double)
    	{
    		Double l = (Double)value;
    		return l.doubleValue();
    	}
    	else if(value instanceof Float)
    	{
    		Float f = (Float)value;
    		return f.doubleValue();
    	}
    	if(value instanceof Integer)
    	{
    		Integer data = (Integer)value;
        	return data.doubleValue();
    	}
    	else if(value instanceof BigDecimal)
    	{
    		BigDecimal b = (BigDecimal)value;
    		return b.doubleValue();
    	}
    	else if(value instanceof Short)
    	{
    		Short s = (Short)value;
    		return s.doubleValue();
    	}
    	
    	else if(value instanceof Long)
    	{
    		Long l = (Long)value;
    		return l.doubleValue();
    	}
    	
    	else
    	{
    		String i = String.valueOf(value);
    		try
    		{
    			return Double.parseDouble(i);
    		}
    		catch(Exception e)
    		{
    			throw new NestedSQLException(e);
    		}
    	}
    }
    /**
     * Retrieves the value of the designated JDBC <code>INTEGER</code> parameter 
     * as an <code>int</code> in the Java programming language.
     *
     * @param parameterIndex 存储过程返回的数据：the first parameter is 1, the second is 2 一般查询的结果集： the first parameter is 0, the second is 1, 
     * and so on
     * @return the parameter value.  If the value is SQL <code>NULL</code>, the result 
     * is <code>0</code>.
     * @exception SQLException if a database access error occurs
     * @see #setInt
     */
    public int getInt(int parameterIndex) throws SQLException
    {
//    	if(Integer )
    	Object value = this.getObject(parameterIndex);
    	return getIntFromObject(value);
    	
//    	throw new SQLException("getInt(" + parameterIndex + ") failed:value=null.");
    }

    /**
     * Retrieves the value of the designated JDBC <code>BIGINT</code> parameter 
     * as a <code>long</code> in the Java programming language.
     *
     * @param parameterIndex 存储过程返回的数据：the first parameter is 1, the second is 2 一般查询的结果集： the first parameter is 0, the second is 1, 
     * and so on
     * @return the parameter value.  If the value is SQL <code>NULL</code>, the result 
     * is <code>0</code>.
     * @exception SQLException if a database access error occurs
     * @see #setLong
     */
    public long getLong(int parameterIndex) throws SQLException
    {
    	Object value = this.getObject(parameterIndex);
    	return getLongFromObject(value);
//    	Long data = (Long)this.getObject(parameterIndex);
//    	if(data != null)
//    	{
//    		return data.longValue();
//    	}
//    	throw new SQLException("getInt(" + parameterIndex + ") failed:value=null.");
    }
    
    public long getLongFromObject(Object value) throws SQLException
    {
    	if(value == null)
    		return 0;
    	if(value instanceof Long)
    	{
    		Long l = (Long)value;
    		return l.longValue();
    	}
    	else if(value instanceof Double)
    	{
    		Double l = (Double)value;
    		return l.longValue();
    	}
    	else if(value instanceof Integer)
    	{
    		Integer data = (Integer)value;
        	return data.longValue();
    	}
    	else if(value instanceof BigDecimal)
    	{
    		BigDecimal b = (BigDecimal)value;
    		return b.longValue();
    	}
    	else if(value instanceof Short)
    	{
    		Short s = (Short)value;
    		return s.longValue();
    	}
    	else if(value instanceof Float)
    	{
    		Float f = (Float)value;
    		return f.longValue();
    	}    	 
    	else
    	{
    		String i = String.valueOf(value);
    		try
    		{
    			return Long.parseLong(i);
    		}
    		catch(Exception e)
    		{
    			throw new NestedSQLException(e);
    		}
    	}
    }

    /**
     * Retrieves the value of the designated JDBC <code>FLOAT</code> parameter 
     * as a <code>float</code> in the Java programming language.
     *
     * @param parameterIndex 存储过程返回的数据：the first parameter is 1, the second is 2 一般查询的结果集： the first parameter is 0, the second is 1, 
     *        and so on
     * @return the parameter value.  If the value is SQL <code>NULL</code>, the result 
     *         is <code>0</code>.
     * @exception SQLException if a database access error occurs
     * @see #setFloat
     */
    public float getFloat(int parameterIndex) throws SQLException
    {
    	Object value = this.getObject(parameterIndex);
    	return getFloatFromObject( value);
    	
    }
    /**
     * Retrieves the value of the designated JDBC <code>DOUBLE</code> parameter as a <code>double</code>
     * in the Java programming language.
     * @param parameterIndex 存储过程返回的数据：the first parameter is 1, the second is 2 一般查询的结果集： the first parameter is 0, the second is 1,
     *        and so on
     * @return the parameter value.  If the value is SQL <code>NULL</code>, the result 
     *         is <code>0</code>.
     * @exception SQLException if a database access error occurs
     * @see #setDouble
     */
    public double getDouble(int parameterIndex) throws SQLException
    {
    	Object value = this.getObject(parameterIndex);
    	return getDoubleFromObject(value);
//    	if(data != null)
//    	{
//    		return data.doubleValue();
//    	}
//    	throw new SQLException("getDouble(" + parameterIndex + ") failed:value=null.");
    }

    /** 
     * Retrieves the value of the designated JDBC <code>NUMERIC</code> parameter as a 
     * <code>java.math.BigDecimal</code> object with <i>scale</i> digits to
     * the right of the decimal point.
     * @param parameterIndex 存储过程返回的数据：the first parameter is 1, the second is 2 一般查询的结果集： the first parameter is 0, the second is 1, 
     *        and so on
     * @param scale the number of digits to the right of the decimal point 
     * @return the parameter value.  If the value is SQL <code>NULL</code>, the result 
     *         is <code>null</code>. 
     * @exception SQLException if a database access error occurs
     * @deprecated use <code>getBigDecimal(int parameterIndex)</code>
     *             or <code>getBigDecimal(String parameterName)</code>
     * @see #setBigDecimal
     */
    public BigDecimal getBigDecimal(int parameterIndex, int scale)
	throws SQLException
    {
    	BigDecimal big = this.getBigDecimal(parameterIndex); 
    	if(big != null)
    		return big.setScale(scale);
    	return null;
    }

    /**
     * Retrieves the value of the designated JDBC <code>BINARY</code> or 
     * <code>VARBINARY</code> parameter as an array of <code>byte</code> 
     * values in the Java programming language.
     * @param parameterIndex 存储过程返回的数据：the first parameter is 1, the second is 2 一般查询的结果集： the first parameter is 0, the second is 1, 
     *        and so on
     * @return the parameter value.  If the value is SQL <code>NULL</code>, the result 
     *         is <code>null</code>.
     * @exception SQLException if a database access error occurs
     * @see #setBytes
     */
    public byte[] getBytes(int parameterIndex) throws SQLException

    {
    	Object bytes = this.getObject(parameterIndex);
    	if(bytes instanceof byte[])
    		return (byte[])bytes;
    	else
    	{
    		return  ValueExchange.convertObjectToBytes(bytes);
    	}
//    	return (byte[]) this.getObject(parameterIndex);
    }

    /**
     * Retrieves the value of the designated JDBC <code>DATE</code> parameter as a 
     * <code>java.sql.Date</code> object.
     * @param parameterIndex 存储过程返回的数据：the first parameter is 1, the second is 2 一般查询的结果集： the first parameter is 0, the second is 1, 
     *        and so on
     * @return the parameter value.  If the value is SQL <code>NULL</code>, the result 
     *         is <code>null</code>.
     * @exception SQLException if a database access error occurs
     * @see #setDate
     */
    public java.sql.Date getDate(int parameterIndex) throws SQLException
    {
    	Object date = this.getObject(parameterIndex);
    	if(date == null)
    		return null;
    	if(date instanceof java.sql.Date)
    	{
    		return (java.sql.Date)date;
    	}
    	else if(date instanceof java.util.Date)
    	{
    		return new java.sql.Date(((java.util.Date)date).getTime());
    	}
    	throw new SQLException("Record.getDate(" + parameterIndex +") failed: error data type["+date.getClass().getName()+","+ date +"]");
//    	return (java.sql.Date)this.getObject(parameterIndex);
    }

    /**
     * Retrieves the value of the designated JDBC <code>TIME</code> parameter as a 
     * <code>java.sql.Time</code> object.
     *
     * @param parameterIndex 存储过程返回的数据：the first parameter is 1, the second is 2 一般查询的结果集： the first parameter is 0, the second is 1, 
     *        and so on
     * @return the parameter value.  If the value is SQL <code>NULL</code>, the result 
     *         is <code>null</code>.
     * @exception SQLException if a database access error occurs
     * @see #setTime
     */
    public java.sql.Time getTime(int parameterIndex) throws SQLException
    {
    	Object object = this.getObject(parameterIndex);
    	if(object == null)
    		return null;
    	if(object instanceof java.sql.Time)
    		return (java.sql.Time)object;
    	if(object instanceof java.util.Date)
    	{
    		return new java.sql.Time(((java.util.Date)object).getTime());
    	}
//    	return (java.sql.Time)this.getObject(parameterIndex);
    	throw new SQLException("Record.getTime(" + parameterIndex +") failed: error data type["+object.getClass().getName()+","+ object +"]");
    }

    /**
     * Retrieves the value of the designated JDBC <code>TIMESTAMP</code> parameter as a 
     * <code>java.sql.Timestamp</code> object.
     *
     * @param parameterIndex 存储过程返回的数据：the first parameter is 1, the second is 2 一般查询的结果集： the first parameter is 0, the second is 1, 
     *        and so on
     * @return the parameter value.  If the value is SQL <code>NULL</code>, the result 
     *         is <code>null</code>.
     * @exception SQLException if a database access error occurs
     * @see #setTimestamp
     */
    public java.sql.Timestamp getTimestamp(int parameterIndex) 
	throws SQLException
    {
//    	return (java.sql.Timestamp)this.getObject(parameterIndex);
    	Object object = this.getObject(parameterIndex);
    	if(object == null)
    		return null;
    	if(object instanceof java.sql.Timestamp)
    		return (java.sql.Timestamp)object;
    	if(object instanceof java.util.Date)
    	{
    		return new java.sql.Timestamp(((java.util.Date)object).getTime());
    	}
//    	return (java.sql.Time)this.getObject(parameterIndex);
    	throw new SQLException("Record.getTimestamp(" + parameterIndex +") failed: error data type["+object.getClass().getName()+","+ object +"]");
    }

    //----------------------------------------------------------------------
    // Advanced features:
	/**
	 * 
	 * @param column
	 *            start from zero
	 * @return 列索引对应的属性名称
	 * @throws SQLException 
	 */
	private String seekField(int column) throws SQLException {
//		return f_temps[column];
		if(column > fields.length || column < 0)
			throw new SQLException(new StringBuffer("column out of range:column=").append(column)
					.append(",fields.length=").append(fields.length ).toString());
		if(this.sameCols == null || column == 0)
		    return this.fields[column];
		String temp = fields[column];
		WrapInteger wi = (WrapInteger)sameCols.get(temp );
		if(wi == null)
		    return temp ;
		else
		    return wi.getColumnName(column);
	}
	
	 

    /**
     * Retrieves the value of the designated parameter as an <code>Object</code> 
     * in the Java programming language. If the value is an SQL <code>NULL</code>,
     * the driver returns a Java <code>null</code>.
     * <p>
     * This method returns a Java object whose type corresponds to the JDBC
     * type that was registered for this parameter using the method
     * <code>registerOutParameter</code>.  By registering the target JDBC
     * type as <code>java.sql.Types.OTHER</code>, this method can be used
     * to read database-specific abstract data types.
     *
     * @param parameterIndex 存储过程返回的数据：the first parameter is 1, the second is 2 一般查询的结果集： the first parameter is 0, the second is 1, 
     *        and so on
     * @return A <code>java.lang.Object</code> holding the OUT parameter value
     * @exception SQLException if a database access error occurs
     * @see Types 
     * @see #setObject
     */
    public Object getObject(int parameterIndex) throws SQLException
    {

    	if(this.fields != null)
    	{
    		return get(seekField(parameterIndex));
//    	    return get(new Integer(parameterIndex));
    		
    	}
    	else
    	{ 
    	    return get(new Integer(parameterIndex));
    	
    	}

    }


    //--------------------------JDBC 2.0-----------------------------

    /**
     * Retrieves the value of the designated JDBC <code>NUMERIC</code> parameter as a 
     * <code>java.math.BigDecimal</code> object with as many digits to the
     * right of the decimal point as the value contains.
     * @param parameterIndex 存储过程返回的数据：the first parameter is 1, the second is 2 一般查询的结果集： the first parameter is 0, the second is 1,
     * and so on
     * @return the parameter value in full precision.  If the value is 
     * SQL <code>NULL</code>, the result is <code>null</code>. 
     * @exception SQLException if a database access error occurs
     * @see #setBigDecimal
     * @since 1.2
     */
    public BigDecimal getBigDecimal(int parameterIndex) throws SQLException
    {
    	return (BigDecimal)getObject(parameterIndex);
    }

    /**
     * Returns an object representing the value of OUT parameter 
     * <code>i</code> and uses <code>map</code> for the custom
     * mapping of the parameter value.
     * <p>
     * This method returns a Java object whose type corresponds to the
     * JDBC type that was registered for this parameter using the method
     * <code>registerOutParameter</code>.  By registering the target
     * JDBC type as <code>java.sql.Types.OTHER</code>, this method can
     * be used to read database-specific abstract data types.  
     * @param i 存储过程返回的数据：the first parameter is 1, the second is 2 一般查询的结果集： the first parameter is 0, the second is 1, and so on
     * @param map the mapping from SQL type names to Java classes
     * @return a <code>java.lang.Object</code> holding the OUT parameter value
     * @exception SQLException if a database access error occurs
     * @see #setObject
     * @since 1.2
     */
    public Object  getObject (int i, java.util.Map map) throws SQLException
    {
    	return this.getObject(i);
    }
    
    public java.io.Reader getCharacterStream( int columnIndex) throws SQLException
    {
    	Object object = this.getObject(columnIndex);
    	if(object == null)
    		return null;
    	if(object instanceof java.io.Reader)
    		return (java.io.Reader)object;
    	else if(object instanceof java.sql.Clob)
    	{
    		Clob clob = (java.sql.Clob)object;
    		return clob.getCharacterStream();
    	}
//    	else if(object instanceof java.sql.Blob)
//    	{
//    		Blob clob = (java.sql.Blob)object;
//    		return clob.getCharacterStream();
//    	} 
    	throw new SQLException("Error type cast column index["+columnIndex+"]:From [" + object.getClass().getName() + "] to [" + java.io.Reader.class.getName() + "]");
    		
    		
    }
    
    /**
	 * Retrieves the value of the designated column in the current row of this
	 * <code>ResultSet</code> object as a stream of ASCII characters. The
	 * value can then be read in chunks from the stream. This method is
	 * particularly suitable for retrieving large <char>LONGVARCHAR</char>
	 * values. The JDBC driver will do any necessary conversion from the
	 * database format into ASCII.
	 * 
	 * <P>
	 * <B>Note:</B> All the data in the returned stream must be read prior to
	 * getting the value of any other column. The next call to a getter method
	 * implicitly closes the stream. Also, a stream may return <code>0</code>
	 * when the method <code>InputStream.available</code> is called whether
	 * there is data available or not.
	 * 
	 * @param rowid
	 *            the first row is 0, the second is 1
	 * @param columnIndex
	 *            the first column is 0, the second is 1, ...
	 * @return a Java input stream that delivers the database column value as a
	 *         stream of one-byte ASCII characters; if the value is SQL
	 *         <code>NULL</code>, the value returned is <code>null</code>
	 * @exception SQLException
	 *                if a database access error occurs
	 */
	public java.io.InputStream getAsciiStream(int columnIndex)
			throws SQLException {
//		inrange(rowid, colName);
		Object value = this.getObject(columnIndex);
		if(value == null)
		{
			return null;
		}
		if(value instanceof java.io.InputStream)
			return (java.io.InputStream)value;
		if(value instanceof Clob)
		{
			Clob clob = (Clob)value;
			return clob.getAsciiStream();
//			java.io.InputStream
		}
		else if(value instanceof Blob)
		{
			Blob clob = (Blob)value;
			return clob.getBinaryStream();
//			java.io.InputStream
		}
		throw new SQLException("Error type cast column index["+columnIndex+"]:From [" + value.getClass().getName() + "] to [" + java.io.InputStream.class.getName() + "]");
//		return allResults[getTrueRowid(rowid)].getBoolean(colName);
//		return (java.io.InputStream) getObject(rowid, columnIndex);
	}
	
	public java.io.InputStream getAsciiStream(String colName)
	throws SQLException {
//inrange(rowid, colName);
		Object value = this.getObject(colName);
		if(value == null)
		{
			return null;
		}
		if(value instanceof java.io.InputStream)
			return (java.io.InputStream)value;
		if(value instanceof Clob)
		{
			Clob clob = (Clob)value;
			return clob.getAsciiStream();
		//	java.io.InputStream
		}
		else if(value instanceof Blob)
		{
			Blob clob = (Blob)value;
			return clob.getBinaryStream();
		//	java.io.InputStream
		}
		throw new SQLException("Error type cast column["+colName+"]:From [" + value.getClass().getName() + "] to [" + java.io.InputStream.class.getName() + "]");
		//return allResults[getTrueRowid(rowid)].getBoolean(colName);
		//return (java.io.InputStream) getObject(rowid, columnIndex);
	}
    
    public java.io.Reader getCharacterStream(String columnName) throws SQLException
    {
    	Object object = this.getObject(columnName);
    	if(object == null)
    		return null;
    	if(object instanceof java.io.Reader)
    		return (java.io.Reader)object;
    	else if(object instanceof java.sql.Clob)
    	{
    		Clob clob = (java.sql.Clob)object;
    		return clob.getCharacterStream();
    	}
//    	else if(object instanceof java.sql.Blob)
//    	{
//    		Blob clob = (java.sql.Blob)object;
//    		return clob.getCharacterStream();
//    	} 
    	throw new SQLException("Error type cast column["+columnName+"]:From [" + object.getClass().getName() + "] to [" + java.io.Reader.class.getName() + "]");
    		
    }
    
    /**
	 * Retrieves the value of the designated column in the current row of this
	 * <code>ResultSet</code> object as as a stream of two-byte Unicode
	 * characters. The first byte is the high byte; the second byte is the low
	 * byte.
	 * 
	 * The value can then be read in chunks from the stream. This method is
	 * particularly suitable for retrieving large <code>LONGVARCHAR</code>values.
	 * The JDBC driver will do any necessary conversion from the database format
	 * into Unicode.
	 * 
	 * <P>
	 * <B>Note:</B> All the data in the returned stream must be read prior to
	 * getting the value of any other column. The next call to a getter method
	 * implicitly closes the stream. Also, a stream may return <code>0</code>
	 * when the method <code>InputStream.available</code> is called, whether
	 * there is data available or not.
	 * 
	 * @param rowid
	 *            the first row is 0, the second is 1
	 * @param columnIndex
	 *            the first column is 0, the second is 1, ...
	 * @return a Java input stream that delivers the database column value as a
	 *         stream of two-byte Unicode characters; if the value is SQL
	 *         <code>NULL</code>, the value returned is <code>null</code>
	 * 
	 * @exception SQLException
	 *                if a database access error occurs
	 * @deprecated use <code>getCharacterStream</code> in place of
	 *             <code>getUnicodeStream</code>
	 */
	public java.io.InputStream getUnicodeStream( int columnIndex)
			throws SQLException {
		Object value = this.getObject( columnIndex);
		if(value == null)
			return null;
		if(value instanceof Blob)
		{
			Blob blob = (Blob)value;
			return blob.getBinaryStream();
		}
		else if(value instanceof Clob)
		{
			Clob blob = (Clob)value;
			return blob.getAsciiStream();
		}
			
		throw new SQLException("Error type cast column index["+columnIndex+"]:From [" + value.getClass().getName() + "] to [" + java.io.InputStream.class.getName() + "]");
//		return (java.io.InputStream) this.getObject(rowid, columnIndex);
	}
	
	/**
	 * Retrieves the value of the designated column in the current row of this
	 * <code>ResultSet</code> object as a binary stream of uninterpreted
	 * bytes. The value can then be read in chunks from the stream. This method
	 * is particularly suitable for retrieving large <code>LONGVARBINARY</code>
	 * values.
	 * 
	 * <P>
	 * <B>Note:</B> All the data in the returned stream must be read prior to
	 * getting the value of any other column. The next call to a getter method
	 * implicitly closes the stream. Also, a stream may return <code>0</code>
	 * when the method <code>InputStream.available</code> is called whether
	 * there is data available or not.
	 * 
	 * @param rowid
	 *            the first row is 0, the second is 1
	 * @param columnIndex
	 *            the first column is 0, the second is 1, ...
	 * @return a Java input stream that delivers the database column value as a
	 *         stream of uninterpreted bytes; if the value is SQL
	 *         <code>NULL</code>, the value returned is <code>null</code>
	 * @exception SQLException
	 *                if a database access error occurs
	 */
	public java.io.InputStream getBinaryStream(int columnIndex)
			throws SQLException {
		Object value = this.getObject( columnIndex);
		if(value == null)
			return null;
		if(value instanceof Blob)
		{
			Blob blob = (Blob)value;
			return blob.getBinaryStream();
		}
		else if(value instanceof Clob)
		{
			Clob blob = (Clob)value;
			return blob.getAsciiStream();
		}
			
		throw new SQLException("Error type cast column index["+columnIndex+"]:From [" + value.getClass().getName() + "] to [" + java.io.InputStream.class.getName() + "]");
//		return (java.io.InputStream) this.getObject(rowid, columnIndex);
	}
	
	public java.io.InputStream getBinaryStream(String columnName)
	throws SQLException {
		Object value = this.getObject( columnName);
		if(value == null)
			return null;
		if(value instanceof Blob)
		{
			Blob blob = (Blob)value;
			return blob.getBinaryStream();
		}
		else if(value instanceof Clob)
		{
			Clob blob = (Clob)value;
			return blob.getAsciiStream();
		}
			
		throw new SQLException("Error type cast column["+columnName+"]:From [" + value.getClass().getName() + "] to [" + java.io.InputStream.class.getName() + "]");
	//return (java.io.InputStream) this.getObject(rowid, columnIndex);
	}

	
	
	/**
	 * Retrieves the value of the designated column in the current row of this
	 * <code>ResultSet</code> object as as a stream of two-byte Unicode
	 * characters. The first byte is the high byte; the second byte is the low
	 * byte.
	 * 
	 * The value can then be read in chunks from the stream. This method is
	 * particularly suitable for retrieving large <code>LONGVARCHAR</code>values.
	 * The JDBC driver will do any necessary conversion from the database format
	 * into Unicode.
	 * 
	 * <P>
	 * <B>Note:</B> All the data in the returned stream must be read prior to
	 * getting the value of any other column. The next call to a getter method
	 * implicitly closes the stream. Also, a stream may return <code>0</code>
	 * when the method <code>InputStream.available</code> is called, whether
	 * there is data available or not.
	 * 
	 * @param rowid
	 *            the first row is 0, the second is 1
	 * @param columnIndex
	 *            the first column is 0, the second is 1, ...
	 * @return a Java input stream that delivers the database column value as a
	 *         stream of two-byte Unicode characters; if the value is SQL
	 *         <code>NULL</code>, the value returned is <code>null</code>
	 * 
	 * @exception SQLException
	 *                if a database access error occurs
	 * @deprecated use <code>getCharacterStream</code> in place of
	 *             <code>getUnicodeStream</code>
	 */
	public java.io.InputStream getUnicodeStream( String columnName)
			throws SQLException {
		Object value = this.getObject( columnName);
		if(value == null)
			return null;
		if(value instanceof Blob)
		{
			Blob blob = (Blob)value;
			return blob.getBinaryStream();
		}
		else if(value instanceof Clob)
		{
			Clob blob = (Clob)value;
			return blob.getAsciiStream();
		}
			
		throw new SQLException("Error type cast column["+columnName+"]:From [" + value.getClass().getName() + "] to [" + java.io.InputStream.class.getName() + "]");
//		return (java.io.InputStream) this.getObject(rowid, columnIndex);
	}

    /**
     * Retrieves the value of the designated JDBC <code>REF(&lt;structured-type&gt;)</code>
     * parameter as a {@link Ref} object in the Java programming language.
     * @param i 存储过程返回的数据：the first parameter is 1, the second is 2 一般查询的结果集： the first parameter is 0, the second is 1, 
     * and so on
     * @return the parameter value as a <code>Ref</code> object in the
     * Java programming language.  If the value was SQL <code>NULL</code>, the value
     * <code>null</code> is returned.
     * @exception SQLException if a database access error occurs
     * @since 1.2
     */
    public Ref getRef (int i) throws SQLException
    {
    	return (Ref)this.getObject(i);
    }

    /**
     * Retrieves the value of the designated JDBC <code>BLOB</code> parameter as a
     * {@link Blob} object in the Java programming language.
     * @param i 存储过程返回的数据：the first parameter is 1, the second is 2 一般查询的结果集： the first parameter is 0, the second is 1, and so on
     * @return the parameter value as a <code>Blob</code> object in the
     * Java programming language.  If the value was SQL <code>NULL</code>, the value
     * <code>null</code> is returned.
     * @exception SQLException if a database access error occurs
     * @since 1.2
     */
    public Blob getBlob (int i) throws SQLException
    {
    	return (Blob)this.getObject(i);
    }

    /**
     * Retrieves the value of the designated JDBC <code>CLOB</code> parameter as a
     * <code>Clob</code> object in the Java programming language.
     * @param i 存储过程返回的数据：the first parameter is 1, the second is 2 一般查询的结果集： the first parameter is 0, the second is 1, and
     * so on
     * @return the parameter value as a <code>Clob</code> object in the
     * Java programming language.  If the value was SQL <code>NULL</code>, the
     * value <code>null</code> is returned.
     * @exception SQLException if a database access error occurs
     * @since 1.2
     */
    public Clob getClob (int i) throws SQLException
    {
    	return (Clob)this.getObject(i);
    }

    /**
     *
     * Retrieves the value of the designated JDBC <code>ARRAY</code> parameter as an
     * {@link Array} object in the Java programming language.
     * @param i 存储过程返回的数据：the first parameter is 1, the second is 2 一般查询的结果集： the first parameter is 0, the second is 1, and 
     * so on
     * @return the parameter value as an <code>Array</code> object in
     * the Java programming language.  If the value was SQL <code>NULL</code>, the
     * value <code>null</code> is returned.
     * @exception SQLException if a database access error occurs
     * @since 1.2
     */
    public Array getArray (int i) throws SQLException
    {
    	return (Array)this.getObject(i);
    }

    /**
     * Retrieves the value of the designated JDBC <code>DATE</code> parameter as a 
     * <code>java.sql.Date</code> object, using
     * the given <code>Calendar</code> object
     * to construct the date.
     * With a <code>Calendar</code> object, the driver
     * can calculate the date taking into account a custom timezone and locale.
     * If no <code>Calendar</code> object is specified, the driver uses the
     * default timezone and locale.
     *
     * @param parameterIndex 存储过程返回的数据：the first parameter is 1, the second is 2 一般查询的结果集： the first parameter is 0, the second is 1, 
     * and so on
     * @param cal the <code>Calendar</code> object the driver will use
     *            to construct the date
     * @return the parameter value.  If the value is SQL <code>NULL</code>, the result 
     *         is <code>null</code>.
     * @exception SQLException if a database access error occurs
     * @see #setDate
     * @since 1.2
     */
    public java.sql.Date getDate(int parameterIndex, Calendar cal) 
	throws SQLException
    {
    	java.sql.Date date = this.getDate(parameterIndex);
    	
    	if(date != null)
    	{
    		cal.setTime(date);
    		
    		return new java.sql.Date(cal.getTimeInMillis());
//    		java.util.Date date_ =  cal.getTime();
//    		return new java.sql.Date(date,cal);
    	}
    	return null;
    }

    /**
     * Retrieves the value of the designated JDBC <code>TIME</code> parameter as a 
     * <code>java.sql.Time</code> object, using
     * the given <code>Calendar</code> object
     * to construct the time.
     * With a <code>Calendar</code> object, the driver
     * can calculate the time taking into account a custom timezone and locale.
     * If no <code>Calendar</code> object is specified, the driver uses the
     * default timezone and locale.
     *
     * @param parameterIndex 存储过程返回的数据：the first parameter is 1, the second is 2 一般查询的结果集： the first parameter is 0, the second is 1,
     * and so on
     * @param cal the <code>Calendar</code> object the driver will use
     *            to construct the time
     * @return the parameter value; if the value is SQL <code>NULL</code>, the result 
     *         is <code>null</code>.
     * @exception SQLException if a database access error occurs
     * @see #setTime
     * @since 1.2
     */
    public java.sql.Time getTime(int parameterIndex, Calendar cal) 
	throws SQLException
    {
    	java.sql.Time time = this.getTime(parameterIndex);
    	if(time != null)
    	{
    		
    		cal.setTimeInMillis(time.getTime());
    		return new java.sql.Time(cal.getTimeInMillis());
    	}
    	return null;
    }

    /**
     * Retrieves the value of the designated JDBC <code>TIMESTAMP</code> parameter as a
     * <code>java.sql.Timestamp</code> object, using
     * the given <code>Calendar</code> object to construct
     * the <code>Timestamp</code> object.
     * With a <code>Calendar</code> object, the driver
     * can calculate the timestamp taking into account a custom timezone and locale.
     * If no <code>Calendar</code> object is specified, the driver uses the
     * default timezone and locale.
     *
     *
     * @param parameterIndex 存储过程返回的数据：the first parameter is 1, the second is 2 一般查询的结果集： the first parameter is 0, the second is 1, 
     * and so on
     * @param cal the <code>Calendar</code> object the driver will use
     *            to construct the timestamp
     * @return the parameter value.  If the value is SQL <code>NULL</code>, the result 
     *         is <code>null</code>.
     * @exception SQLException if a database access error occurs
     * @see #setTimestamp
     * @since 1.2
     */
    public java.sql.Timestamp getTimestamp(int parameterIndex, Calendar cal) 
	throws SQLException
    {
    	java.sql.Timestamp date = this.getTimestamp(parameterIndex);
    	if(date != null)
    	{
    		cal.setTimeInMillis(date.getTime());
    		return new java.sql.Timestamp(cal.getTimeInMillis());
    		
    	}
    	return null;
    }


    /**
     * Retrieves the value of the designated JDBC <code>DATALINK</code> parameter as a
     * <code>java.net.URL</code> object.
     * 
     * @param parameterIndex 存储过程返回的数据：the first parameter is 1, the second is 2 一般查询的结果集： the first parameter is 0, the second is 1,...
     * @return a <code>java.net.URL</code> object that represents the 
     *         JDBC <code>DATALINK</code> value used as the designated
     *         parameter
     * @exception SQLException if a database access error occurs,
     *            or if the URL being returned is
     *            not a valid URL on the Java platform
     * @see #setURL
     * @since 1.4
     */
    public java.net.URL getURL(int parameterIndex) throws SQLException
    {
    	return (java.net.URL)this.getObject(parameterIndex);
    }
    


    /**
     * Retrieves the value of a JDBC <code>CHAR</code>, <code>VARCHAR</code>, 
     * or <code>LONGVARCHAR</code> parameter as a <code>String</code> in 
     * the Java programming language.
     * <p>
     * For the fixed-length type JDBC <code>CHAR</code>,
     * the <code>String</code> object
     * returned has exactly the same value the JDBC
     * <code>CHAR</code> value had in the
     * database, including any padding added by the database.
     * @param parameterName the name of the parameter
     * @return the parameter value. If the value is SQL <code>NULL</code>, the result 
     * is <code>null</code>.
     * @exception SQLException if a database access error occurs
     * @see #setString
     * @since 1.4
     */
    public String getString(String parameterName) throws SQLException
    {
    	Object object = this.getObject(parameterName);
    	return ValueExchange.getStringFromObject(object);
    }

    /**
     * Retrieves the value of a JDBC <code>BIT</code> parameter as a
     * <code>boolean</code> in the Java programming language.
     * @param parameterName the name of the parameter
     * @return the parameter value.  If the value is SQL <code>NULL</code>, the result 
     * is <code>false</code>.
     * @exception SQLException if a database access error occurs
     * @see #setBoolean
     * @since 1.4
     */
    public boolean getBoolean(String parameterName) throws SQLException
    {
    	Boolean value = (Boolean)this.getObject(parameterName);
    	if(value != null)
    	{
    		return value.booleanValue();
    	}
    	throw new SQLException("getBoolean(" + parameterName + ") failed:value=null.");
    	
    }

    /**
     * Retrieves the value of a JDBC <code>TINYINT</code> parameter as a <code>byte</code> 
     * in the Java programming language.
     * @param parameterName the name of the parameter
     * @return the parameter value.  If the value is SQL <code>NULL</code>, the result 
     * is <code>0</code>.
     * @exception SQLException if a database access error occurs
     * @see #setByte
     * @since 1.4
     */
    public byte getByte(String parameterName) throws SQLException
    {
    	Byte byte_ = (Byte)this.getObject(parameterName);
    	if(byte_ != null)
    		return byte_.byteValue();
    	throw new SQLException("getByte(" + parameterName + ") failed:value=null.");
    }

    /**
     * Retrieves the value of a JDBC <code>SMALLINT</code> parameter as a <code>short</code>
     * in the Java programming language.
     * @param parameterName the name of the parameter
     * @return the parameter value.  If the value is SQL <code>NULL</code>, the result 
     * is <code>0</code>.
     * @exception SQLException if a database access error occurs
     * @see #setShort
     * @since 1.4
     */
    public short getShort(String parameterName) throws SQLException
    {
    	Object value = this.getObject(parameterName);
    	return getShortFromObject(value);
    }

    /**
     * Retrieves the value of a JDBC <code>INTEGER</code> parameter as an <code>int</code>
     * in the Java programming language.
     *
     * @param parameterName the name of the parameter
     * @return the parameter value.  If the value is SQL <code>NULL</code>, 
     *         the result is <code>0</code>.
     * @exception SQLException if a database access error occurs
     * @see #setInt
     * @since 1.4
     */
    public int getInt(String parameterName) throws SQLException
    {
//    	Integer data = (Integer)this.getObject(parameterName);
//    	if(data != null)
//    	{
//    		return data.intValue();
//    	}
//    	throw new SQLException("getInt(" + parameterName + ") failed:value=null.");
    	Object value = this.getObject(parameterName);
    	return getIntFromObject(value);
    }

    /**
     * Retrieves the value of a JDBC <code>BIGINT</code> parameter as a <code>long</code>
     * in the Java programming language.
     *
     * @param parameterName the name of the parameter
     * @return the parameter value.  If the value is SQL <code>NULL</code>, 
     *         the result is <code>0</code>.
     * @exception SQLException if a database access error occurs
     * @see #setLong
     * @since 1.4
     */
    public long getLong(String parameterName) throws SQLException
    {
//    	Long data = (Long)this.getObject(parameterName);
//    	if(data != null)
//    	{
//    		return data.longValue();
//    	}
//    	throw new SQLException("getInt(" + parameterName + ") failed:value=null.");
    	Object value = this.getObject(parameterName);
    	return getLongFromObject(value);
    }

    /**
     * Retrieves the value of a JDBC <code>FLOAT</code> parameter as a <code>float</code>
     * in the Java programming language.
     * @param parameterName the name of the parameter
     * @return the parameter value.  If the value is SQL <code>NULL</code>, 
     *         the result is <code>0</code>.
     * @exception SQLException if a database access error occurs
     * @see #setFloat
     * @since 1.4
     */
    public float getFloat(String parameterName) throws SQLException
    {
    	Object value = this.getObject(parameterName);
    	return getFloatFromObject( value);
    }

    /**
     * Retrieves the value of a JDBC <code>DOUBLE</code> parameter as a <code>double</code>
     * in the Java programming language.
     * @param parameterName the name of the parameter
     * @return the parameter value.  If the value is SQL <code>NULL</code>, 
     *         the result is <code>0</code>.
     * @exception SQLException if a database access error occurs
     * @see #setDouble
     * @since 1.4
     */
    public double getDouble(String parameterName) throws SQLException
    {
    	Object value = this.getObject(parameterName);
    	return getDoubleFromObject(value);
    }

    /**
     * Retrieves the value of a JDBC <code>BINARY</code> or <code>VARBINARY</code> 
     * parameter as an array of <code>byte</code> values in the Java
     * programming language.
     * @param parameterName the name of the parameter
     * @return the parameter value.  If the value is SQL <code>NULL</code>, the result is 
     *  <code>null</code>.
     * @exception SQLException if a database access error occurs
     * @see #setBytes
     * @since 1.4
     */
    public byte[] getBytes(String parameterName) throws SQLException
    {
    	Object bytes = this.getObject(parameterName);
    	if(bytes instanceof byte[])
    		return (byte[])bytes;
    	else
    	{
    		return  ValueExchange.convertObjectToBytes(bytes);
    	}
    }

    /**
     * Retrieves the value of a JDBC <code>DATE</code> parameter as a 
     * <code>java.sql.Date</code> object.
     * @param parameterName the name of the parameter
     * @return the parameter value.  If the value is SQL <code>NULL</code>, the result 
     * is <code>null</code>.
     * @exception SQLException if a database access error occurs
     * @see #setDate
     * @since 1.4
     */
    public java.sql.Date getDate(String parameterName) throws SQLException
    {
//    	return (java.sql.Date)this.getObject(parameterName);
    	Object date = this.getObject(parameterName);
    	if(date == null)
    		return null;
    	if(date instanceof java.sql.Date)
    	{
    		return (java.sql.Date)date;
    	}
    	else if(date instanceof java.util.Date)
    	{
    		return new java.sql.Date(((java.util.Date)date).getTime());
    	}
    	throw new SQLException("Record.getDate(" + parameterName +") failed: error data type["+date.getClass().getName()+","+ date +"]");
    }

    /**
     * Retrieves the value of a JDBC <code>TIME</code> parameter as a 
     * <code>java.sql.Time</code> object.
     * @param parameterName the name of the parameter
     * @return the parameter value.  If the value is SQL <code>NULL</code>, the result 
     * is <code>null</code>.
     * @exception SQLException if a database access error occurs
     * @see #setTime
     * @since 1.4
     */
    public java.sql.Time getTime(String parameterName) throws SQLException
    {
//    	return (java.sql.Time)this.getObject(parameterName);
    	Object object = this.getObject(parameterName);
    	if(object == null)
    		return null;
    	if(object instanceof java.sql.Time)
    		return (java.sql.Time)object;
    	if(object instanceof java.util.Date)
    	{
    		return new java.sql.Time(((java.util.Date)object).getTime());
    	}
//    	return (java.sql.Time)this.getObject(parameterIndex);
    	throw new SQLException("Record.getTime(" + parameterName +") failed: error data type["+object.getClass().getName()+","+ object +"]");
    }

    /**
     * Retrieves the value of a JDBC <code>TIMESTAMP</code> parameter as a 
     * <code>java.sql.Timestamp</code> object.
     * @param parameterName the name of the parameter
     * @return the parameter value.  If the value is SQL <code>NULL</code>, the result 
     * is <code>null</code>.
     * @exception SQLException if a database access error occurs
     * @see #setTimestamp
     * @since 1.4
     */
    public java.sql.Timestamp getTimestamp(String parameterName) throws SQLException
    {
//    	return (java.sql.Timestamp)this.getObject(parameterName);
    	Object object = this.getObject(parameterName);
    	if(object == null)
    		return null;
    	if(object instanceof java.sql.Timestamp)
    		return (java.sql.Timestamp)object;
    	if(object instanceof java.util.Date)
    	{
    		return new java.sql.Timestamp(((java.util.Date)object).getTime());
    	}
//    	return (java.sql.Time)this.getObject(parameterIndex);
    	throw new SQLException("Record.getTimestamp(" + parameterName +") failed: error data type["+object.getClass().getName()+","+ object +"]");
    }

    /**
     * Retrieves the value of a parameter as an <code>Object</code> in the Java 
     * programming language. If the value is an SQL <code>NULL</code>, the 
     * driver returns a Java <code>null</code>.
     * <p>
     * This method returns a Java object whose type corresponds to the JDBC
     * type that was registered for this parameter using the method
     * <code>registerOutParameter</code>.  By registering the target JDBC
     * type as <code>java.sql.Types.OTHER</code>, this method can be used
     * to read database-specific abstract data types.
     * @param parameterName the name of the parameter
     * @return A <code>java.lang.Object</code> holding the OUT parameter value.
     * @exception SQLException if a database access error occurs
     * @see Types
     * @see #setObject
     * @since 1.4
     */
    public Object getObject(String parameterName) throws SQLException
    {
//    	if(data != null)
//    	{
//    		return data.get(parameterName.toLowerCase());
//    		
//    	}
//    	return null;
    	return get(parameterName.toUpperCase());
    }

    /**
     * Retrieves the value of a JDBC <code>NUMERIC</code> parameter as a 
     * <code>java.math.BigDecimal</code> object with as many digits to the
     * right of the decimal point as the value contains.
     * @param parameterName the name of the parameter
     * @return the parameter value in full precision.  If the value is 
     * SQL <code>NULL</code>, the result is <code>null</code>. 
     * @exception SQLException if a database access error occurs
     * @see #setBigDecimal
     * @since 1.4
     */
    public BigDecimal getBigDecimal(String parameterName) throws SQLException
    {
    	return (BigDecimal)this.getObject(parameterName);
    }

    /**
     * Returns an object representing the value of OUT parameter 
     * <code>i</code> and uses <code>map</code> for the custom
     * mapping of the parameter value.
     * <p>
     * This method returns a Java object whose type corresponds to the
     * JDBC type that was registered for this parameter using the method
     * <code>registerOutParameter</code>.  By registering the target
     * JDBC type as <code>java.sql.Types.OTHER</code>, this method can
     * be used to read database-specific abstract data types.  
     * @param parameterName the name of the parameter
     * @param map the mapping from SQL type names to Java classes
     * @return a <code>java.lang.Object</code> holding the OUT parameter value
     * @exception SQLException if a database access error occurs
     * @see #setObject
     * @since 1.4
     */
    public Object  getObject (String parameterName, java.util.Map map) throws SQLException
    {
//    	if(data != null)
//    	{
//    		return data.get(parameterName.toLowerCase());
//    		
//    	}
//    	return null;
    	return get(parameterName.toLowerCase());
    }

    /**
     * Retrieves the value of a JDBC <code>REF(&lt;structured-type&gt;)</code>
     * parameter as a {@link Ref} object in the Java programming language.
     *
     * @param parameterName the name of the parameter
     * @return the parameter value as a <code>Ref</code> object in the
     *         Java programming language.  If the value was SQL <code>NULL</code>, 
     *         the value <code>null</code> is returned.
     * @exception SQLException if a database access error occurs
     * @since 1.4
     */
    public Ref getRef (String parameterName) throws SQLException
    {
    	return (Ref)this.getObject(parameterName);
    }

    /**
     * Retrieves the value of a JDBC <code>BLOB</code> parameter as a
     * {@link Blob} object in the Java programming language.
     *
     * @param parameterName the name of the parameter
     * @return the parameter value as a <code>Blob</code> object in the
     *         Java programming language.  If the value was SQL <code>NULL</code>, 
     *         the value <code>null</code> is returned.
     * @exception SQLException if a database access error occurs
     * @since 1.4
     */
    public Blob getBlob (String parameterName) throws SQLException
    {
    	return (Blob)this.getObject(parameterName);
    }

    /**
     * Retrieves the value of a JDBC <code>CLOB</code> parameter as a
     * <code>Clob</code> object in the Java programming language.
     * @param parameterName the name of the parameter
     * @return the parameter value as a <code>Clob</code> object in the
     *         Java programming language.  If the value was SQL <code>NULL</code>, 
     *         the value <code>null</code> is returned.
     * @exception SQLException if a database access error occurs
     * @since 1.4
     */
    public Clob getClob (String parameterName) throws SQLException
    {
    	return (Clob)this.getObject(parameterName);
    }

    /**
     * Retrieves the value of a JDBC <code>ARRAY</code> parameter as an
     * {@link Array} object in the Java programming language.
     *
     * @param parameterName the name of the parameter
     * @return the parameter value as an <code>Array</code> object in
     *         Java programming language.  If the value was SQL <code>NULL</code>, 
     *         the value <code>null</code> is returned.
     * @exception SQLException if a database access error occurs
     * @since 1.4
     */
    public Array getArray (String parameterName) throws SQLException
    {
    	return (Array)this.getObject(parameterName);
    }

    /**
     * Retrieves the value of a JDBC <code>DATE</code> parameter as a 
     * <code>java.sql.Date</code> object, using
     * the given <code>Calendar</code> object
     * to construct the date.
     * With a <code>Calendar</code> object, the driver
     * can calculate the date taking into account a custom timezone and locale.
     * If no <code>Calendar</code> object is specified, the driver uses the
     * default timezone and locale.
     *
     * @param parameterName the name of the parameter
     * @param cal the <code>Calendar</code> object the driver will use
     *            to construct the date
     * @return the parameter value.  If the value is SQL <code>NULL</code>, 
     * the result is <code>null</code>.
     * @exception SQLException if a database access error occurs
     * @see #setDate
     * @since 1.4
     */
    public java.sql.Date getDate(String parameterName, Calendar cal) 
	throws SQLException
    {
    	java.sql.Date date = this.getDate(parameterName);
    	
    	if(date != null)
    	{
    		cal.setTime(date);
    		
    		return new java.sql.Date(cal.getTimeInMillis());
//    		java.util.Date date_ =  cal.getTime();
//    		return new java.sql.Date(date,cal);
    	}
    	return null;
    }

    /**
     * Retrieves the value of a JDBC <code>TIME</code> parameter as a 
     * <code>java.sql.Time</code> object, using
     * the given <code>Calendar</code> object
     * to construct the time.
     * With a <code>Calendar</code> object, the driver
     * can calculate the time taking into account a custom timezone and locale.
     * If no <code>Calendar</code> object is specified, the driver uses the
     * default timezone and locale.
     *
     * @param parameterName the name of the parameter
     * @param cal the <code>Calendar</code> object the driver will use
     *            to construct the time
     * @return the parameter value; if the value is SQL <code>NULL</code>, the result is 
     * <code>null</code>.
     * @exception SQLException if a database access error occurs
     * @see #setTime
     * @since 1.4
     */
    public java.sql.Time getTime(String parameterName, Calendar cal) 
	throws SQLException
    {
    	java.sql.Time time = this.getTime(parameterName);
    	if(time != null)
    	{
    		
    		cal.setTimeInMillis(time.getTime());
    		return new java.sql.Time(cal.getTimeInMillis());
    	}
    	return null;
    }

    /**
     * Retrieves the value of a JDBC <code>TIMESTAMP</code> parameter as a
     * <code>java.sql.Timestamp</code> object, using
     * the given <code>Calendar</code> object to construct
     * the <code>Timestamp</code> object.
     * With a <code>Calendar</code> object, the driver
     * can calculate the timestamp taking into account a custom timezone and locale.
     * If no <code>Calendar</code> object is specified, the driver uses the
     * default timezone and locale.
     *
     *
     * @param parameterName the name of the parameter
     * @param cal the <code>Calendar</code> object the driver will use
     *            to construct the timestamp
     * @return the parameter value.  If the value is SQL <code>NULL</code>, the result is 
     * <code>null</code>.
     * @exception SQLException if a database access error occurs
     * @see #setTimestamp
     * @since 1.4
     */
    public java.sql.Timestamp getTimestamp(String parameterName, Calendar cal) 
	throws SQLException
    {
    	java.sql.Timestamp date = this.getTimestamp(parameterName);
    	if(date != null)
    	{
    		cal.setTimeInMillis(date.getTime());
    		return new java.sql.Timestamp(cal.getTimeInMillis());
    		
    	}
    	return null;
    }

    /**
     * Retrieves the value of a JDBC <code>DATALINK</code> parameter as a
     * <code>java.net.URL</code> object.
     *
     * @param parameterName the name of the parameter
     * @return the parameter value as a <code>java.net.URL</code> object in the
     * Java programming language.  If the value was SQL <code>NULL</code>, the
     * value <code>null</code> is returned.
     * @exception SQLException if a database access error occurs,
     *            or if there is a problem with the URL
     * @see #setURL
     * @since 1.4
     */
    public java.net.URL getURL(String parameterName) throws SQLException
    {
    	return (java.net.URL)this.getObject(parameterName);
    }
    
    public void getFile(int index,File file) throws SQLException, IOException
    {
    	Object value = this.getObject(index);
    	this.getFile(value, file);
    }
    
    public void getFile(Object value,File file) throws SQLException
    {
    	
		if (value == null)
		{
			return;
		}
		else 
		{
			if (value instanceof Blob) {
				ValueObjectUtil.getFileFromBlob((Blob)value, file);
			}
			else if(value instanceof Clob)
			{
				ValueObjectUtil.getFileFromClob((Clob)value, file);
			}
			else if (value instanceof byte[]) 
			{
				ValueObjectUtil.getFileFromBytes((byte[])value, file);
			}
			else if (value instanceof String) 
			{
				ValueObjectUtil.getFileFromString((String)value, file);
			}
			else
			{
				ValueObjectUtil.getFileFromString(value.toString(), file); 
			}
		}
    }
    
    public void getFile(String parameterName,File file) throws SQLException, IOException
    {
    	Object value = this.getObject(parameterName);
    	this.getFile(value, file);
    }
    
    /*
     * 获取序列化对象
     */
    public Serializable getSerializable(String field) throws SQLException
    {
        Blob blog = this.getBlob(field);
        if(blog == null)
            return null;
        Serializable object = null;
        InputStream in = null;        
        java.io.ObjectInputStream objectin = null;
        try
        {
            
            in = blog.getBinaryStream();
            objectin = new ObjectInputStream(in);
            
            object = (Serializable)objectin.readObject();
        }
        catch(SQLException e)
        {
            throw e;
        }
        catch(Exception e)
        {
            throw new NestedSQLException(e);
        }
        finally
        {
            if(in != null)
                try
                {
                    in.close();
                }
                catch (IOException e)
                {
//                    // TODO Auto-generated catch block
//                    e.printStackTrace();
                }
            if(objectin != null)
                try
                {
                    objectin.close();
                }
                catch (IOException e)
                {
//                    // TODO Auto-generated catch block
//                    e.printStackTrace();
                }
        }
        return object;
    }
    
    public Serializable getSerializable(int parameterIndex) throws SQLException
    {
        Blob blog = this.getBlob(parameterIndex);
        if(blog == null)
            return null;
        Serializable object = null;
        InputStream in = null;        
        java.io.ObjectInputStream objectin = null;
        try
        {
            
            in = blog.getBinaryStream();
            objectin = new ObjectInputStream(in);
            
            object = (Serializable)objectin.readObject();
        }
        catch(SQLException e)
        {
            throw e;
        }
        catch(Exception e)
        {
            throw new NestedSQLException(e);
        }
        finally
        {
            if(in != null)
                try
                {
                    in.close();
                }
                catch (IOException e)
                {
//                    // TODO Auto-generated catch block
//                    e.printStackTrace();
                }
            if(objectin != null)
                try
                {
                    objectin.close();
                }
                catch (IOException e)
                {
//                    // TODO Auto-generated catch block
//                    e.printStackTrace();
                }
        }
        
        return object;
        
    }
}
