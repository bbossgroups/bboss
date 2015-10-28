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
 *  
 *  http://blog.csdn.net/yin_bp
 *  http://yin-bp.javaeye.com/
 */
package com.frameworkset.common.poolman;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.Date;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.frameworkset.common.poolman.handle.RowHandler;
import com.frameworkset.common.poolman.handle.XMLMark;

/**
 * 
 * 
 * <p>Title: CallableDBUtil.java</p>
 *
 * <p>Description: 存储过程</p>
 *
 * <p>Copyright: Copyright (c) 2007</p>
 *

 * @Date Oct 3, 2008 11:48:32 AM
 * @author biaoping.yin
 * @version 1.0
 */
public class CallableDBUtil extends PreparedDBUtil {
	public static int CALL_PROCEDURE = 0;
	public static int CALL_FUNCTION = 1;
	
	private static Logger log = Logger.getLogger(CallableDBUtil.class);
	/**
	 * 覆盖父类的参数构造方法
	 */
	protected Param buildParam()
	{
		return buildCallableParam();
	}
	
	/**
	 * 覆盖父类的参数构造方法
	 */
	protected Params buildParams()
	{
		return buildCallableParams(CALL_PROCEDURE);
	}
	
	protected Params buildCallableParams(int calltype)
	{
		CallableParams temp_ = new CallableParams();
		temp_.call_type = calltype;
		return temp_;
	}
	
	protected CallableParam buildCallableParam()
	{
		return new CallableParam();
	}


	/**
	 *  构造预编译参数
	 * @param index
	 * @param sqlType
	 * @param method
	 * @throws SQLException
	 */
	 
	private void  addCallableParam(int index,int sqlType,String method) throws SQLException
	{
		
		CallableParam param = buildCallableParam();
		param.sqlType = sqlType;
		param.index = index;
		param.method = method;
		param.isOut = true;
		((CallableParams)Params).callParams.add(param);
		((CallableParams)Params).outParams.add(param);
		
	}
	
	private void  addCallableParam(int index,int sqlType,String typeName,String method) throws SQLException
	{
		
		CallableParam param = buildCallableParam();
		param.sqlType = sqlType;
		param.index = index;
		param.method = method;
		param.typeName = typeName;
		param.isOut = true;
		((CallableParams)Params).callParams.add(param);
		((CallableParams)Params).outParams.add(param);
		
	}
	
	/**
	 *  构造预编译参数
	 * @param index
	 * @param sqlType
	 * @param scale
	 * @param method
	 * @throws SQLException
	 */
	private void  addCallableParam(int index,int sqlType,int scale,String method) throws SQLException
	{
		
		CallableParam param = buildCallableParam();
		param.sqlType = sqlType;
		param.index = index;
		param.method = method;
		param.isOut = true;
		((CallableParams)Params).callParams.add(param);
		((CallableParams)Params).outParams.add(param);
	}
	
	private void  addCallableParam(String paramName,int sqlType,String method) throws SQLException
	{
		
		CallableParam param = buildCallableParam();
		param.parameterName = paramName;
		param.sqlType = sqlType;		
		param.method = method;
		param.isOut = true;
		((CallableParams)Params).callParams.add(param);
		((CallableParams)Params).outParams.add(param);
		
	}
	
	private void  addCallableParam(String parameterName,int sqlType,String typeName,String method) throws SQLException
	{
		CallableParam param = buildCallableParam();
		param.parameterName = parameterName;
		param.sqlType = sqlType;	
		param.typeName = typeName;
		param.method = method;
		param.isOut = true;
		((CallableParams)Params).callParams.add(param);
		((CallableParams)Params).outParams.add(param);
		
	}
	
	private void  addCallableParam(String paramName,int sqlType,int scale,String method) throws SQLException
	{
		CallableParam param = buildCallableParam();
		param.parameterName = paramName;
		param.sqlType = sqlType;
		param.scale = scale;
		param.method = method;
		param.isOut = true;
		((CallableParams)Params).callParams.add(param);
		((CallableParams)Params).outParams.add(param);
	}
	
	private void  addCallableParam(String paramName,Object value,int scale,String method) throws SQLException
	{
		CallableParam param = buildCallableParam();
		param.parameterName = paramName;
		param.data = value;
		param.scale = scale;
		param.method = method;		
		((CallableParams)Params).callParams.add(param);
	}
	
	private void  addCallableParam(String parameterName,Object value,String method) throws SQLException
	{
		CallableParam param = buildCallableParam();
		param.parameterName = parameterName;
		param.data = value;		
		param.method = method;		
		((CallableParams)Params).callParams.add(param);
	}
	
	private void  addCallableParam(String parameterName,Object value,int targetSqlType,int scale,String method) throws SQLException
	{
		CallableParam param = buildCallableParam();
		param.parameterName = parameterName;
		param.data = value;		
		param.sqlType = targetSqlType;
		param.scale = scale;
		param.method = method;		
		((CallableParams)Params).callParams.add(param);
	}
	
	
	

	
	/**
     * Registers the OUT parameter in ordinal position 
     * <code>parameterIndex</code> to the JDBC type 
     * <code>sqlType</code>.  All OUT parameters must be registered
     * before a stored procedure is executed.
     * <p>
     * The JDBC type specified by <code>sqlType</code> for an OUT
     * parameter determines the Java type that must be used
     * in the <code>get</code> method to read the value of that parameter.
     * <p>
     * If the JDBC type expected to be returned to this output parameter
     * is specific to this particular database, <code>sqlType</code>
     * should be <code>java.sql.Types.OTHER</code>.  The method 
     * {@link #getObject} retrieves the value.
     *
     * @param parameterIndex the first parameter is 1, the second is 2, 
     *        and so on
     * @param sqlType the JDBC type code defined by <code>java.sql.Types</code>.
     *        If the parameter is of JDBC type <code>NUMERIC</code>
     *        or <code>DECIMAL</code>, the version of
     *        <code>registerOutParameter</code> that accepts a scale value 
     *        should be used.
     *
     * @exception SQLException if a database access error occurs
     * @see Types 
     */
    public void registerOutParameter(int parameterIndex, int sqlType)
	throws SQLException
	{
    	this.addCallableParam(parameterIndex, 
    						  sqlType, 
    						  CallableParam.registerOutParameter_int_parameterIndex_int_sqlType);
	}

    /**
     * Registers the parameter in ordinal position
     * <code>parameterIndex</code> to be of JDBC type
     * <code>sqlType</code>.  This method must be called
     * before a stored procedure is executed.
     * <p>
     * The JDBC type specified by <code>sqlType</code> for an OUT
     * parameter determines the Java type that must be used
     * in the <code>get</code> method to read the value of that parameter.
     * <p>
     * This version of <code>registerOutParameter</code> should be
     * used when the parameter is of JDBC type <code>NUMERIC</code>
     * or <code>DECIMAL</code>.
     *
     * @param parameterIndex the first parameter is 1, the second is 2,
     * and so on
     * @param sqlType the SQL type code defined by <code>java.sql.Types</code>.
     * @param scale the desired number of digits to the right of the
     * decimal point.  It must be greater than or equal to zero.
     * @exception SQLException if a database access error occurs
     * @see Types 
     */
    public void registerOutParameter(int parameterIndex, int sqlType, int scale)
	throws SQLException
	{
    	this.addCallableParam(parameterIndex, 
				  sqlType, scale,
				  CallableParam.registerOutParameter_int_parameterIndex_int_sqlType_int_scale);
	}

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
    	return false;
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
     * @param parameterIndex the first parameter is 1, the second is 2, 
     * and so on
     * @return the parameter value. If the value is SQL <code>NULL</code>, 
     *         the result 
     *         is <code>null</code>.
     * @exception SQLException if a database access error occurs
     * @see #setString
     */
    public String getString(int parameterIndex) throws SQLException
    {
    	if(this.callableResult != null && this.callableResult.getOrigineprocresult() != null)
    	{
    		return this.callableResult.getOrigineprocresult().getString(parameterIndex);
    		
    	}
    	return null;
    	
    }

    /**
     * Retrieves the value of the designated JDBC <code>BIT</code> parameter as a 
     * <code>boolean</code> in the Java programming language.
     *
     * @param parameterIndex the first parameter is 1, the second is 2, 
     *        and so on
     * @return the parameter value.  If the value is SQL <code>NULL</code>, 
     *         the result is <code>false</code>.
     * @exception SQLException if a database access error occurs
     * @see #setBoolean
     */
    public boolean getBoolean(int parameterIndex) throws SQLException
    {
    	if(this.callableResult != null && this.callableResult.getOrigineprocresult() != null)
    	{
    		return this.callableResult.getOrigineprocresult().getBoolean(parameterIndex);
    		
    	}
    	throw new SQLException("getBoolean(" + parameterIndex + ") failed:value=null.");
    	
    }

    /**
     * Retrieves the value of the designated JDBC <code>TINYINT</code> parameter 
     * as a <code>byte</code> in the Java programming language.
     *
     * @param parameterIndex the first parameter is 1, the second is 2, 
     * and so on
     * @return the parameter value.  If the value is SQL <code>NULL</code>, the result 
     * is <code>0</code>.
     * @exception SQLException if a database access error occurs
     * @see #setByte
     */
    public byte getByte(int parameterIndex) throws SQLException
    {
    	if(this.callableResult != null && this.callableResult.getOrigineprocresult() != null)
    	{
    		return this.callableResult.getOrigineprocresult().getByte(parameterIndex);
    		
    	}
    	throw new SQLException("getByte(" + parameterIndex + ") failed:value=null.");
    }

    /**
     * Retrieves the value of the designated JDBC <code>SMALLINT</code> parameter 
     * as a <code>short</code> in the Java programming language.
     *
     * @param parameterIndex the first parameter is 1, the second is 2, 
     * and so on
     * @return the parameter value.  If the value is SQL <code>NULL</code>, the result 
     * is <code>0</code>.
     * @exception SQLException if a database access error occurs
     * @see #setShort
     */
    public short getShort(int parameterIndex) throws SQLException
    {
    	if(this.callableResult != null && this.callableResult.getOrigineprocresult() != null)
    	{
    		return this.callableResult.getOrigineprocresult().getShort(parameterIndex);
    		
    	}
    	
    	throw new SQLException("getShort(" + parameterIndex + ") failed:value=null.");
    }

    /**
     * Retrieves the value of the designated JDBC <code>INTEGER</code> parameter 
     * as an <code>int</code> in the Java programming language.
     *
     * @param parameterIndex the first parameter is 1, the second is 2, 
     * and so on
     * @return the parameter value.  If the value is SQL <code>NULL</code>, the result 
     * is <code>0</code>.
     * @exception SQLException if a database access error occurs
     * @see #setInt
     */
    public int getInt(int parameterIndex) throws SQLException
    {
    	if(this.callableResult != null && this.callableResult.getOrigineprocresult() != null)
    	{
    		return this.callableResult.getOrigineprocresult().getInt(parameterIndex);
    		
    	}
    	throw new SQLException("getInt(" + parameterIndex + ") failed:value=null.");
    }

    /**
     * Retrieves the value of the designated JDBC <code>BIGINT</code> parameter 
     * as a <code>long</code> in the Java programming language.
     *
     * @param parameterIndex the first parameter is 1, the second is 2, 
     * and so on
     * @return the parameter value.  If the value is SQL <code>NULL</code>, the result 
     * is <code>0</code>.
     * @exception SQLException if a database access error occurs
     * @see #setLong
     */
    public long getLong(int parameterIndex) throws SQLException
    {
    	if(this.callableResult != null && this.callableResult.getOrigineprocresult() != null)
    	{
    		return this.callableResult.getOrigineprocresult().getLong(parameterIndex);
    		
    	}
    	throw new SQLException("getInt(" + parameterIndex + ") failed:value=null.");
    }

    /**
     * Retrieves the value of the designated JDBC <code>FLOAT</code> parameter 
     * as a <code>float</code> in the Java programming language.
     *
     * @param parameterIndex the first parameter is 1, the second is 2, 
     *        and so on
     * @return the parameter value.  If the value is SQL <code>NULL</code>, the result 
     *         is <code>0</code>.
     * @exception SQLException if a database access error occurs
     * @see #setFloat
     */
    public float getFloat(int parameterIndex) throws SQLException
    {
    	if(this.callableResult != null && this.callableResult.getOrigineprocresult() != null)
    	{
    		return this.callableResult.getOrigineprocresult().getFloat(parameterIndex);
    		
    	}
    	throw new SQLException("getFloat(" + parameterIndex + ") failed:value=null.");
    }
    /**
     * Retrieves the value of the designated JDBC <code>DOUBLE</code> parameter as a <code>double</code>
     * in the Java programming language.
     * @param parameterIndex the first parameter is 1, the second is 2,
     *        and so on
     * @return the parameter value.  If the value is SQL <code>NULL</code>, the result 
     *         is <code>0</code>.
     * @exception SQLException if a database access error occurs
     * @see #setDouble
     */
    public double getDouble(int parameterIndex) throws SQLException
    {
    	if(this.callableResult != null && this.callableResult.getOrigineprocresult() != null)
    	{
    		return this.callableResult.getOrigineprocresult().getDouble(parameterIndex);
    		
    	}
    	
    	throw new SQLException("getDouble(" + parameterIndex + ") failed:value=null.");
    }

    /** 
     * Retrieves the value of the designated JDBC <code>NUMERIC</code> parameter as a 
     * <code>java.math.BigDecimal</code> object with <i>scale</i> digits to
     * the right of the decimal point.
     * @param parameterIndex the first parameter is 1, the second is 2, 
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
    	if(this.callableResult != null && this.callableResult.getOrigineprocresult() != null)
    	{
    		return this.callableResult.getOrigineprocresult().getBigDecimal(parameterIndex,scale);
    		
    	}
    	return null;
    }

    /**
     * Retrieves the value of the designated JDBC <code>BINARY</code> or 
     * <code>VARBINARY</code> parameter as an array of <code>byte</code> 
     * values in the Java programming language.
     * @param parameterIndex the first parameter is 1, the second is 2, 
     *        and so on
     * @return the parameter value.  If the value is SQL <code>NULL</code>, the result 
     *         is <code>null</code>.
     * @exception SQLException if a database access error occurs
     * @see #setBytes
     */
    public byte[] getBytes(int parameterIndex) throws SQLException

    {
    	if(this.callableResult != null && this.callableResult.getOrigineprocresult() != null)
    	{
    		return this.callableResult.getOrigineprocresult().getBytes(parameterIndex);
    		
    	}
    	return null;
    }

    /**
     * Retrieves the value of the designated JDBC <code>DATE</code> parameter as a 
     * <code>java.sql.Date</code> object.
     * @param parameterIndex the first parameter is 1, the second is 2, 
     *        and so on
     * @return the parameter value.  If the value is SQL <code>NULL</code>, the result 
     *         is <code>null</code>.
     * @exception SQLException if a database access error occurs
     * @see #setDate
     */
    public java.sql.Date getDate(int parameterIndex) throws SQLException
    {
    	if(this.callableResult != null && this.callableResult.getOrigineprocresult() != null)
    	{
    		return this.callableResult.getOrigineprocresult().getDate(parameterIndex);
    		
    	}
    	return null;
    }

    /**
     * Retrieves the value of the designated JDBC <code>TIME</code> parameter as a 
     * <code>java.sql.Time</code> object.
     *
     * @param parameterIndex the first parameter is 1, the second is 2, 
     *        and so on
     * @return the parameter value.  If the value is SQL <code>NULL</code>, the result 
     *         is <code>null</code>.
     * @exception SQLException if a database access error occurs
     * @see #setTime
     */
    public java.sql.Time getTime(int parameterIndex) throws SQLException
    {
    	if(this.callableResult != null && this.callableResult.getOrigineprocresult() != null)
    	{
    		return this.callableResult.getOrigineprocresult().getTime(parameterIndex);
    		
    	}
    	return null;
    }

    /**
     * Retrieves the value of the designated JDBC <code>TIMESTAMP</code> parameter as a 
     * <code>java.sql.Timestamp</code> object.
     *
     * @param parameterIndex the first parameter is 1, the second is 2, 
     *        and so on
     * @return the parameter value.  If the value is SQL <code>NULL</code>, the result 
     *         is <code>null</code>.
     * @exception SQLException if a database access error occurs
     * @see #setTimestamp
     */
    public java.sql.Timestamp getTimestamp(int parameterIndex) 
	throws SQLException
    {
    	if(this.callableResult != null && this.callableResult.getOrigineprocresult() != null)
    	{
    		return this.callableResult.getOrigineprocresult().getTimestamp(parameterIndex);
    		
    	}
    	return null;
    }

    //----------------------------------------------------------------------
    // Advanced features:


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
     * @param parameterIndex the first parameter is 1, the second is 2, 
     *        and so on
     * @return A <code>java.lang.Object</code> holding the OUT parameter value
     * @exception SQLException if a database access error occurs
     * @see Types 
     * @see #setObject
     */
    public Object getObject(int parameterIndex) throws SQLException
    {
    	if(this.callableResult != null && this.callableResult.getOrigineprocresult() != null)
    	{
    		return this.callableResult.getOrigineprocresult().getObject(parameterIndex);
    	}
    	return null;
    }


    //--------------------------JDBC 2.0-----------------------------

    /**
     * Retrieves the value of the designated JDBC <code>NUMERIC</code> parameter as a 
     * <code>java.math.BigDecimal</code> object with as many digits to the
     * right of the decimal point as the value contains.
     * @param parameterIndex the first parameter is 1, the second is 2,
     * and so on
     * @return the parameter value in full precision.  If the value is 
     * SQL <code>NULL</code>, the result is <code>null</code>. 
     * @exception SQLException if a database access error occurs
     * @see #setBigDecimal
     * @since 1.2
     */
    public BigDecimal getBigDecimal(int parameterIndex) throws SQLException
    {
    	if(this.callableResult != null && this.callableResult.getOrigineprocresult() != null)
    	{
    		return this.callableResult.getOrigineprocresult().getBigDecimal(parameterIndex);
    		
    	}
    	return null;
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
     * @param i the first parameter is 1, the second is 2, and so on
     * @param map the mapping from SQL type names to Java classes
     * @return a <code>java.lang.Object</code> holding the OUT parameter value
     * @exception SQLException if a database access error occurs
     * @see #setObject
     * @since 1.2
     */
    public Object  getObject (int i, java.util.Map map) throws SQLException
    {
    	if(this.callableResult != null && this.callableResult.getOrigineprocresult() != null)
    	{
    		return this.callableResult.getOrigineprocresult().getObject(i,map);
    	}
    	return null;
    }

    /**
     * Retrieves the value of the designated JDBC <code>REF(&lt;structured-type&gt;)</code>
     * parameter as a {@link Ref} object in the Java programming language.
     * @param i the first parameter is 1, the second is 2, 
     * and so on
     * @return the parameter value as a <code>Ref</code> object in the
     * Java programming language.  If the value was SQL <code>NULL</code>, the value
     * <code>null</code> is returned.
     * @exception SQLException if a database access error occurs
     * @since 1.2
     */
    public Ref getRef (int i) throws SQLException
    {
    	if(this.callableResult != null && this.callableResult.getOrigineprocresult() != null)
    	{
    		return this.callableResult.getOrigineprocresult().getRef(i);
    		
    	}
    	return null;
    }

    /**
     * Retrieves the value of the designated JDBC <code>BLOB</code> parameter as a
     * {@link Blob} object in the Java programming language.
     * @param i the first parameter is 1, the second is 2, and so on
     * @return the parameter value as a <code>Blob</code> object in the
     * Java programming language.  If the value was SQL <code>NULL</code>, the value
     * <code>null</code> is returned.
     * @exception SQLException if a database access error occurs
     * @since 1.2
     */
    public Blob getBlob (int i) throws SQLException
    {
    	if(this.callableResult != null && this.callableResult.getOrigineprocresult() != null)
    	{
    		return this.callableResult.getOrigineprocresult().getBlob(i);
    		
    	}
    	return null;
    }

    /**
     * Retrieves the value of the designated JDBC <code>CLOB</code> parameter as a
     * <code>Clob</code> object in the Java programming language.
     * @param i the first parameter is 1, the second is 2, and
     * so on
     * @return the parameter value as a <code>Clob</code> object in the
     * Java programming language.  If the value was SQL <code>NULL</code>, the
     * value <code>null</code> is returned.
     * @exception SQLException if a database access error occurs
     * @since 1.2
     */
    public Clob getClob (int i) throws SQLException
    {
    	if(this.callableResult != null && this.callableResult.getOrigineprocresult() != null)
    	{
    		return this.callableResult.getOrigineprocresult().getClob(i);
    		
    	}
    	return null;
    }

    /**
     *
     * Retrieves the value of the designated JDBC <code>ARRAY</code> parameter as an
     * {@link Array} object in the Java programming language.
     * @param i the first parameter is 1, the second is 2, and 
     * so on
     * @return the parameter value as an <code>Array</code> object in
     * the Java programming language.  If the value was SQL <code>NULL</code>, the
     * value <code>null</code> is returned.
     * @exception SQLException if a database access error occurs
     * @since 1.2
     */
    public Array getArray (int i) throws SQLException
    {
    	if(this.callableResult != null && this.callableResult.getOrigineprocresult() != null)
    	{
    		return this.callableResult.getOrigineprocresult().getArray(i);
    		
    	}
    	return null;
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
     * @param parameterIndex the first parameter is 1, the second is 2, 
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
    	if(this.callableResult != null && this.callableResult.getOrigineprocresult() != null)
    	{
    		return this.callableResult.getOrigineprocresult().getDate(parameterIndex,cal);
    		
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
     * @param parameterIndex the first parameter is 1, the second is 2,
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
    	if(this.callableResult != null && this.callableResult.getOrigineprocresult() != null)
    	{
    		return this.callableResult.getOrigineprocresult().getTime(parameterIndex,cal);
    		
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
     * @param parameterIndex the first parameter is 1, the second is 2, 
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
    	if(this.callableResult != null && this.callableResult.getOrigineprocresult() != null)
    	{
    		return this.callableResult.getOrigineprocresult().getTimestamp(parameterIndex,cal);
    		
    	}
    	return null;
    }


    /**
     * Registers the designated output parameter.  This version of 
     * the method <code>registerOutParameter</code>
     * should be used for a user-defined or <code>REF</code> output parameter.  Examples
     * of user-defined types include: <code>STRUCT</code>, <code>DISTINCT</code>,
     * <code>JAVA_OBJECT</code>, and named array types.
     *
     * Before executing a stored procedure call, you must explicitly
     * call <code>registerOutParameter</code> to register the type from
     * <code>java.sql.Types</code> for each
     * OUT parameter.  For a user-defined parameter, the fully-qualified SQL
     * type name of the parameter should also be given, while a <code>REF</code>
     * parameter requires that the fully-qualified type name of the
     * referenced type be given.  A JDBC driver that does not need the
     * type code and type name information may ignore it.   To be portable,
     * however, applications should always provide these values for
     * user-defined and <code>REF</code> parameters.
     *
     * Although it is intended for user-defined and <code>REF</code> parameters,
     * this method may be used to register a parameter of any JDBC type.
     * If the parameter does not have a user-defined or <code>REF</code> type, the
     * <i>typeName</i> parameter is ignored.
     *
     * <P><B>Note:</B> When reading the value of an out parameter, you
     * must use the getter method whose Java type corresponds to the
     * parameter's registered SQL type.
     *
     * @param paramIndex the first parameter is 1, the second is 2,...
     * @param sqlType a value from {@link java.sql.Types}
     * @param typeName the fully-qualified name of an SQL structured type
     * @exception SQLException if a database access error occurs
     * @see Types
     * @since 1.2
     */
    public void registerOutParameter (int paramIndex, int sqlType, String typeName)
	throws SQLException
    {
    	this.addCallableParam(paramIndex, sqlType, typeName,CallableParam.registerOutParameter_int_paramIndex_int_sqlType_String_typeName);
    }

  //--------------------------JDBC 3.0-----------------------------

    /**
     * Registers the OUT parameter named 
     * <code>parameterName</code> to the JDBC type 
     * <code>sqlType</code>.  All OUT parameters must be registered
     * before a stored procedure is executed.
     * <p>
     * The JDBC type specified by <code>sqlType</code> for an OUT
     * parameter determines the Java type that must be used
     * in the <code>get</code> method to read the value of that parameter.
     * <p>
     * If the JDBC type expected to be returned to this output parameter
     * is specific to this particular database, <code>sqlType</code>
     * should be <code>java.sql.Types.OTHER</code>.  The method 
     * {@link #getObject} retrieves the value.
     * @param parameterName the name of the parameter
     * @param sqlType the JDBC type code defined by <code>java.sql.Types</code>.
     * If the parameter is of JDBC type <code>NUMERIC</code>
     * or <code>DECIMAL</code>, the version of
     * <code>registerOutParameter</code> that accepts a scale value 
     * should be used.
     * @exception SQLException if a database access error occurs
     * @since 1.4
     * @see Types 
     */
    public void registerOutParameter(String parameterName, int sqlType)
	throws SQLException
    {
    	this.addCallableParam(parameterName, sqlType, CallableParam.registerOutParameter_String_parameterName_int_sqlType);
    }

    /**
     * Registers the parameter named 
     * <code>parameterName</code> to be of JDBC type
     * <code>sqlType</code>.  This method must be called
     * before a stored procedure is executed.
     * <p>
     * The JDBC type specified by <code>sqlType</code> for an OUT
     * parameter determines the Java type that must be used
     * in the <code>get</code> method to read the value of that parameter.
     * <p>
     * This version of <code>registerOutParameter</code> should be
     * used when the parameter is of JDBC type <code>NUMERIC</code>
     * or <code>DECIMAL</code>.
     * @param parameterName the name of the parameter
     * @param sqlType SQL type code defined by <code>java.sql.Types</code>.
     * @param scale the desired number of digits to the right of the
     * decimal point.  It must be greater than or equal to zero.
     * @exception SQLException if a database access error occurs
     * @since 1.4
     * @see Types 
     */
    public void registerOutParameter(String parameterName, int sqlType, int scale)
	throws SQLException
    {
    	this.addCallableParam(parameterName, sqlType, scale,CallableParam.registerOutParameter_String_parameterName_int_sqlType);
    }

    /**
     * Registers the designated output parameter.  This version of 
     * the method <code>registerOutParameter</code>
     * should be used for a user-named or REF output parameter.  Examples
     * of user-named types include: STRUCT, DISTINCT, JAVA_OBJECT, and
     * named array types.
     *
     * Before executing a stored procedure call, you must explicitly
     * call <code>registerOutParameter</code> to register the type from
     * <code>java.sql.Types</code> for each
     * OUT parameter.  For a user-named parameter the fully-qualified SQL
     * type name of the parameter should also be given, while a REF
     * parameter requires that the fully-qualified type name of the
     * referenced type be given.  A JDBC driver that does not need the
     * type code and type name information may ignore it.   To be portable,
     * however, applications should always provide these values for
     * user-named and REF parameters.
     *
     * Although it is intended for user-named and REF parameters,
     * this method may be used to register a parameter of any JDBC type.
     * If the parameter does not have a user-named or REF type, the
     * typeName parameter is ignored.
     *
     * <P><B>Note:</B> When reading the value of an out parameter, you
     * must use the <code>getXXX</code> method whose Java type XXX corresponds to the
     * parameter's registered SQL type.
     *
     * @param parameterName the name of the parameter
     * @param sqlType a value from {@link java.sql.Types}
     * @param typeName the fully-qualified name of an SQL structured type
     * @exception SQLException if a database access error occurs
     * @see Types
     * @since 1.4
     */
    public void registerOutParameter (String parameterName, int sqlType, String typeName)
	throws SQLException
    {
    	this.addCallableParam(parameterName, sqlType, typeName, CallableParam.registerOutParameter_String_parameterName_int_sqlType_String_typeName);
    }

    /**
     * Retrieves the value of the designated JDBC <code>DATALINK</code> parameter as a
     * <code>java.net.URL</code> object.
     * 
     * @param parameterIndex the first parameter is 1, the second is 2,...
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
    	if(this.callableResult != null && this.callableResult.getOrigineprocresult() != null)
    	{
    		return this.callableResult.getOrigineprocresult().getURL(parameterIndex);
    		
    	}
    	return null;
    }
    

    /**
     * Sets the designated parameter to the given <code>java.net.URL</code> object.
     * The driver converts this to an SQL <code>DATALINK</code> value when
     * it sends it to the database.
     *
     * @param parameterName the name of the parameter
     * @param val the parameter value
     * @exception SQLException if a database access error occurs,
     *            or if a URL is malformed
     * @see #getURL
     * @since 1.4
     */
    public void setURL(String parameterName, java.net.URL val) throws SQLException
    {
    	this.addCallableParam(parameterName, 
    						  val, 
    						  CallableParam.setURL_String_parameterName_URL_val);
    }
    
    /**
     * Sets the designated parameter to SQL <code>NULL</code>.
     *
     * <P><B>Note:</B> You must specify the parameter's SQL type.
     *
     * @param parameterName the name of the parameter
     * @param sqlType the SQL type code defined in <code>java.sql.Types</code>
     * @exception SQLException if a database access error occurs
     * @since 1.4
     */
    public void setNull(String parameterName, int sqlType) throws SQLException
    {
    	this.addCallableParam(parameterName, 
    						  new Integer(sqlType), 
    						  CallableParam.setNull_String_parameterName_int_sqlType);
    }

    /**
     * Sets the designated parameter to the given Java <code>boolean</code> value.
     * The driver converts this
     * to an SQL <code>BIT</code> value when it sends it to the database.
     *
     * @param parameterName the name of the parameter
     * @param x the parameter value
     * @exception SQLException if a database access error occurs
     * @see #getBoolean
     * @since 1.4
     */
    public void setBoolean(String parameterName, boolean x) throws SQLException
    {
    	this.addCallableParam(parameterName, new Boolean(x), CallableParam.setBoolean_String_parameterName_boolean_x);
    }

    /**
     * Sets the designated parameter to the given Java <code>byte</code> value.  
     * The driver converts this
     * to an SQL <code>TINYINT</code> value when it sends it to the database.
     *
     * @param parameterName the name of the parameter
     * @param x the parameter value
     * @exception SQLException if a database access error occurs
     * @see #getByte
     * @since 1.4
     */
    public void setByte(String parameterName, byte x) throws SQLException
    {
    	this.addCallableParam(parameterName, new Byte(x), CallableParam.setByte_String_parameterName_byte_x);
    }

    /**
     * Sets the designated parameter to the given Java <code>short</code> value. 
     * The driver converts this
     * to an SQL <code>SMALLINT</code> value when it sends it to the database.
     *
     * @param parameterName the name of the parameter
     * @param x the parameter value
     * @exception SQLException if a database access error occurs
     * @see #getShort
     * @since 1.4
     */
    public void setShort(String parameterName, short x) throws SQLException
    {
    	this.addCallableParam(parameterName, new Short(x), CallableParam.setShort_String_parameterName_short_x);
    }

    /**
     * Sets the designated parameter to the given Java <code>int</code> value.  
     * The driver converts this
     * to an SQL <code>INTEGER</code> value when it sends it to the database.
     *
     * @param parameterName the name of the parameter
     * @param x the parameter value
     * @exception SQLException if a database access error occurs
     * @see #getInt
     * @since 1.4
     */
    public void setInt(String parameterName, int x) throws SQLException
    {
    	this.addCallableParam(parameterName, new Integer(x), CallableParam.setInt_String_parameterName_int_x);
    }

    /**
     * Sets the designated parameter to the given Java <code>long</code> value. 
     * The driver converts this
     * to an SQL <code>BIGINT</code> value when it sends it to the database.
     *
     * @param parameterName the name of the parameter
     * @param x the parameter value
     * @exception SQLException if a database access error occurs
     * @see #getLong
     * @since 1.4
     */
    public void setLong(String parameterName, long x) throws SQLException
    {
    	this.addCallableParam(parameterName, new Long(x), CallableParam.setLong_String_parameterName_long_x);
    }

    /**
     * Sets the designated parameter to the given Java <code>float</code> value. 
     * The driver converts this
     * to an SQL <code>FLOAT</code> value when it sends it to the database.
     *
     * @param parameterName the name of the parameter
     * @param x the parameter value
     * @exception SQLException if a database access error occurs
     * @see #getFloat
     * @since 1.4
     */
    public void setFloat(String parameterName, float x) throws SQLException
    {
    	this.addCallableParam(parameterName, new Float(x), CallableParam.setFloat_String_parameterName_float_x);
    }
    /**
     * Sets the designated parameter to the given Java <code>double</code> value.  
     * The driver converts this
     * to an SQL <code>DOUBLE</code> value when it sends it to the database.
     *
     * @param parameterName the name of the parameter
     * @param x the parameter value
     * @exception SQLException if a database access error occurs
     * @see #getDouble
     * @since 1.4
     */
    public void setDouble(String parameterName, double x) throws SQLException
    {
    	this.addCallableParam(parameterName, new Double(x), CallableParam.setDouble_String_parameterName_double_x);
    }

    /**
     * Sets the designated parameter to the given
     * <code>java.math.BigDecimal</code> value.  
     * The driver converts this to an SQL <code>NUMERIC</code> value when
     * it sends it to the database.
     *
     * @param parameterName the name of the parameter
     * @param x the parameter value
     * @exception SQLException if a database access error occurs
     * @see #getBigDecimal
     * @since 1.4
     */
    public void setBigDecimal(String parameterName, BigDecimal x) throws SQLException
    {
    	this.addCallableParam(parameterName, x, CallableParam.setBigDecimal_String_parameterName_BigDecimal_x);
    }
    /**
     * Sets the designated parameter to the given Java <code>String</code> value. 
     * The driver converts this
     * to an SQL <code>VARCHAR</code> or <code>LONGVARCHAR</code> value
     * (depending on the argument's
     * size relative to the driver's limits on <code>VARCHAR</code> values)
     * when it sends it to the database.
     *
     * @param parameterName the name of the parameter
     * @param x the parameter value
     * @exception SQLException if a database access error occurs
     * @see #getString
     * @since 1.4
     */
    public void setString(String parameterName, String x) throws SQLException
    {
    	this.addCallableParam(parameterName, x, CallableParam.setString_String_parameterName_String_x);
    }

    /**
     * Sets the designated parameter to the given Java array of bytes.  
     * The driver converts this to an SQL <code>VARBINARY</code> or 
     * <code>LONGVARBINARY</code> (depending on the argument's size relative 
     * to the driver's limits on <code>VARBINARY</code> values) when it sends 
     * it to the database.
     *
     * @param parameterName the name of the parameter
     * @param x the parameter value 
     * @exception SQLException if a database access error occurs
     * @see #getBytes
     * @since 1.4
     */
    public void setBytes(String parameterName, byte x[]) throws SQLException
    {
    	this.addCallableParam(parameterName, x, CallableParam.setBytes_String_parameterName_byteArray_x);
    }

    /**
     * Sets the designated parameter to the given <code>java.sql.Date</code> value.  
     * The driver converts this
     * to an SQL <code>DATE</code> value when it sends it to the database.
     *
     * @param parameterName the name of the parameter
     * @param x the parameter value
     * @exception SQLException if a database access error occurs
     * @see #getDate
     * @since 1.4
     */
    public void setDate(String parameterName, java.sql.Date x)
	throws SQLException
    {
    	this.addCallableParam(parameterName, x, CallableParam.setDate_String_parameterName_Date_x);
    }

    /**
     * Sets the designated parameter to the given <code>java.sql.Time</code> value.  
     * The driver converts this
     * to an SQL <code>TIME</code> value when it sends it to the database.
     *
     * @param parameterName the name of the parameter
     * @param x the parameter value
     * @exception SQLException if a database access error occurs
     * @see #getTime
     * @since 1.4
     */
    public void setTime(String parameterName, java.sql.Time x) 
	throws SQLException
    {
    	this.addCallableParam(parameterName, x, CallableParam.setTime_String_parameterName_Time_x);
    }

    /**
     * Sets the designated parameter to the given <code>java.sql.Timestamp</code> value.  
     * The driver
     * converts this to an SQL <code>TIMESTAMP</code> value when it sends it to the
     * database.
     *
     * @param parameterName the name of the parameter
     * @param x the parameter value 
     * @exception SQLException if a database access error occurs
     * @see #getTimestamp
     * @since 1.4
     */
    public void setTimestamp(String parameterName, java.sql.Timestamp x)
	throws SQLException
    {
    	this.addCallableParam(parameterName, x, CallableParam.setTimestamp_String_parameterName_Timestamp_x);
    }

    /**
     * Sets the designated parameter to the given input stream, which will have 
     * the specified number of bytes.
     * When a very large ASCII value is input to a <code>LONGVARCHAR</code>
     * parameter, it may be more practical to send it via a
     * <code>java.io.InputStream</code>. Data will be read from the stream
     * as needed until end-of-file is reached.  The JDBC driver will
     * do any necessary conversion from ASCII to the database char format.
     * 
     * <P><B>Note:</B> This stream object can either be a standard
     * Java stream object or your own subclass that implements the
     * standard interface.
     *
     * @param parameterName the name of the parameter
     * @param x the Java input stream that contains the ASCII parameter value
     * @param length the number of bytes in the stream 
     * @exception SQLException if a database access error occurs
     * @since 1.4
     */
    public void setAsciiStream(String parameterName, java.io.InputStream x, int length)
	throws SQLException
    {
    	this.addCallableParam(parameterName, new Object[] {x,new Integer(length)}, CallableParam.setAsciiStream_String_parameterName_InputStream_x_int_length);
    }

    /**
     * Sets the designated parameter to the given input stream, which will have 
     * the specified number of bytes.
     * When a very large binary value is input to a <code>LONGVARBINARY</code>
     * parameter, it may be more practical to send it via a
     * <code>java.io.InputStream</code> object. The data will be read from the stream
     * as needed until end-of-file is reached.
     * 
     * <P><B>Note:</B> This stream object can either be a standard
     * Java stream object or your own subclass that implements the
     * standard interface.
     *
     * @param parameterName the name of the parameter
     * @param x the java input stream which contains the binary parameter value
     * @param length the number of bytes in the stream 
     * @exception SQLException if a database access error occurs
     * @since 1.4
     */
    public void setBinaryStream(String parameterName, java.io.InputStream x, 
			 int length) throws SQLException
    {
    	this.addCallableParam(parameterName, new Object[] {x,new Integer(length)}, CallableParam.setBinaryStream_String_parameterName_InputStream_x_int_length);
    }

    /**
     * Sets the value of the designated parameter with the given object. The second
     * argument must be an object type; for integral values, the
     * <code>java.lang</code> equivalent objects should be used.
     *
     * <p>The given Java object will be converted to the given targetSqlType
     * before being sent to the database.
     *
     * If the object has a custom mapping (is of a class implementing the 
     * interface <code>SQLData</code>),
     * the JDBC driver should call the method <code>SQLData.writeSQL</code> to write it 
     * to the SQL data stream.
     * If, on the other hand, the object is of a class implementing
     * <code>Ref</code>, <code>Blob</code>, <code>Clob</code>, <code>Struct</code>, 
     * or <code>Array</code>, the driver should pass it to the database as a 
     * value of the corresponding SQL type.
     * <P>
     * Note that this method may be used to pass datatabase-
     * specific abstract data types. 
     *
     * @param parameterName the name of the parameter
     * @param x the object containing the input parameter value
     * @param targetSqlType the SQL type (as defined in java.sql.Types) to be 
     * sent to the database. The scale argument may further qualify this type.
     * @param scale for java.sql.Types.DECIMAL or java.sql.Types.NUMERIC types,
     *          this is the number of digits after the decimal point.  For all other
     *          types, this value will be ignored.
     * @exception SQLException if a database access error occurs
     * @see Types
     * @see #getObject
     * @since 1.4 
     */
    public void setObject(String parameterName, Object x, int targetSqlType, int scale)
	throws SQLException
    {
    	this.addCallableParam(parameterName, x, targetSqlType,scale,CallableParam.setObject_String_parameterName_Object_x_int_targetSqlType_int_scale);
    }

    /**
     * Sets the value of the designated parameter with the given object.
     * This method is like the method <code>setObject</code>
     * above, except that it assumes a scale of zero.
     *
     * @param parameterName the name of the parameter
     * @param x the object containing the input parameter value
     * @param targetSqlType the SQL type (as defined in java.sql.Types) to be 
     *                      sent to the database
     * @exception SQLException if a database access error occurs
     * @see #getObject
     * @since 1.4
     */
    public void setObject(String parameterName, Object x, int targetSqlType) 
	throws SQLException
    {
    	this.addCallableParam(parameterName, x, targetSqlType,CallableParam.setObject_String_parameterName_Object_x_int_targetSqlType);
    }

    /**
     * Sets the value of the designated parameter with the given object. 
     * The second parameter must be of type <code>Object</code>; therefore, the
     * <code>java.lang</code> equivalent objects should be used for built-in types.
     *
     * <p>The JDBC specification specifies a standard mapping from
     * Java <code>Object</code> types to SQL types.  The given argument 
     * will be converted to the corresponding SQL type before being
     * sent to the database.
     *
     * <p>Note that this method may be used to pass datatabase-
     * specific abstract data types, by using a driver-specific Java
     * type.
     *
     * If the object is of a class implementing the interface <code>SQLData</code>,
     * the JDBC driver should call the method <code>SQLData.writeSQL</code>
     * to write it to the SQL data stream.
     * If, on the other hand, the object is of a class implementing
     * <code>Ref</code>, <code>Blob</code>, <code>Clob</code>, <code>Struct</code>, 
     * or <code>Array</code>, the driver should pass it to the database as a 
     * value of the corresponding SQL type.
     * <P>
     * This method throws an exception if there is an ambiguity, for example, if the
     * object is of a class implementing more than one of the interfaces named above.
     *
     * @param parameterName the name of the parameter
     * @param x the object containing the input parameter value 
     * @exception SQLException if a database access error occurs or if the given
     *            <code>Object</code> parameter is ambiguous
     * @see #getObject
     * @since 1.4
     */
    public void setObject(String parameterName, Object x) throws SQLException
    {
    	this.addCallableParam(parameterName, x, CallableParam.setObject_String_parameterName_Object_x);
    }
   

    /**
     * Sets the designated parameter to the given <code>Reader</code>
     * object, which is the given number of characters long.
     * When a very large UNICODE value is input to a <code>LONGVARCHAR</code>
     * parameter, it may be more practical to send it via a
     * <code>java.io.Reader</code> object. The data will be read from the stream
     * as needed until end-of-file is reached.  The JDBC driver will
     * do any necessary conversion from UNICODE to the database char format.
     * 
     * <P><B>Note:</B> This stream object can either be a standard
     * Java stream object or your own subclass that implements the
     * standard interface.
     *
     * @param parameterName the name of the parameter
     * @param reader the <code>java.io.Reader</code> object that
     *        contains the UNICODE data used as the designated parameter
     * @param length the number of characters in the stream 
     * @exception SQLException if a database access error occurs
     * @since 1.4
     */
    public void setCharacterStream(String parameterName,
			    java.io.Reader reader,
			    int length) throws SQLException
    {
    	this.addCallableParam(parameterName, new Object[] {reader,new Integer(length)}, CallableParam.setCharacterStream_String_parameterName_Reader_reader_int_length);
    }

    /**
     * Sets the designated parameter to the given <code>java.sql.Date</code> value,
     * using the given <code>Calendar</code> object.  The driver uses
     * the <code>Calendar</code> object to construct an SQL <code>DATE</code> value,
     * which the driver then sends to the database.  With a
     * a <code>Calendar</code> object, the driver can calculate the date
     * taking into account a custom timezone.  If no
     * <code>Calendar</code> object is specified, the driver uses the default
     * timezone, which is that of the virtual machine running the application.
     *
     * @param parameterName the name of the parameter
     * @param x the parameter value
     * @param cal the <code>Calendar</code> object the driver will use
     *            to construct the date
     * @exception SQLException if a database access error occurs
     * @see #getDate
     * @since 1.4
     */
    public void setDate(String parameterName, java.sql.Date x, Calendar cal)
	throws SQLException
    {
    	this.addCallableParam(parameterName, new Object[]{x,cal}, CallableParam.setDate_String_parameterName_Date_x_Calendar_cal);
    }

    /**
     * Sets the designated parameter to the given <code>java.sql.Time</code> value,
     * using the given <code>Calendar</code> object.  The driver uses
     * the <code>Calendar</code> object to construct an SQL <code>TIME</code> value,
     * which the driver then sends to the database.  With a
     * a <code>Calendar</code> object, the driver can calculate the time
     * taking into account a custom timezone.  If no
     * <code>Calendar</code> object is specified, the driver uses the default
     * timezone, which is that of the virtual machine running the application.
     *
     * @param parameterName the name of the parameter
     * @param x the parameter value
     * @param cal the <code>Calendar</code> object the driver will use
     *            to construct the time
     * @exception SQLException if a database access error occurs
     * @see #getTime
     * @since 1.4
     */
    public void setTime(String parameterName, java.sql.Time x, Calendar cal) 
	throws SQLException
    {
    	this.addCallableParam(parameterName, new Object[] {x,cal}, CallableParam.setTime_String_parameterName_Time_x_Calendar_cal);
    }

    /**
     * Sets the designated parameter to the given <code>java.sql.Timestamp</code> value,
     * using the given <code>Calendar</code> object.  The driver uses
     * the <code>Calendar</code> object to construct an SQL <code>TIMESTAMP</code> value,
     * which the driver then sends to the database.  With a
     * a <code>Calendar</code> object, the driver can calculate the timestamp
     * taking into account a custom timezone.  If no
     * <code>Calendar</code> object is specified, the driver uses the default
     * timezone, which is that of the virtual machine running the application.
     *
     * @param parameterName the name of the parameter
     * @param x the parameter value 
     * @param cal the <code>Calendar</code> object the driver will use
     *            to construct the timestamp
     * @exception SQLException if a database access error occurs
     * @see #getTimestamp
     * @since 1.4
     */
    public void setTimestamp(String parameterName, java.sql.Timestamp x, Calendar cal)
	throws SQLException
    {
    	this.addCallableParam(parameterName, new Object[] {x,cal}, CallableParam.setTimestamp_String_parameterName_Timestamp_x_Calendar_cal);
    }

    /**
     * Sets the designated parameter to SQL <code>NULL</code>.
     * This version of the method <code>setNull</code> should
     * be used for user-defined types and REF type parameters.  Examples
     * of user-defined types include: STRUCT, DISTINCT, JAVA_OBJECT, and 
     * named array types.
     *
     * <P><B>Note:</B> To be portable, applications must give the
     * SQL type code and the fully-qualified SQL type name when specifying
     * a NULL user-defined or REF parameter.  In the case of a user-defined type 
     * the name is the type name of the parameter itself.  For a REF 
     * parameter, the name is the type name of the referenced type.  If 
     * a JDBC driver does not need the type code or type name information, 
     * it may ignore it.     
     *
     * Although it is intended for user-defined and Ref parameters,
     * this method may be used to set a null parameter of any JDBC type.
     * If the parameter does not have a user-defined or REF type, the given
     * typeName is ignored.
     *
     *
     * @param parameterName the name of the parameter
     * @param sqlType a value from <code>java.sql.Types</code>
     * @param typeName the fully-qualified name of an SQL user-defined type;
     *        ignored if the parameter is not a user-defined type or 
     *        SQL <code>REF</code> value
     * @exception SQLException if a database access error occurs
     * @since 1.4
     */
    public void setNull (String parameterName, int sqlType, String typeName) 
	throws SQLException
    {
    	this.addCallableParam(parameterName, new Object[] {new Integer(sqlType), typeName},CallableParam.setNull_String_parameterName_int_sqlType_String_typeName);
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
    	if(this.callableResult != null && this.callableResult.getOrigineprocresult() != null)
    	{
    		return this.callableResult.getOrigineprocresult().getString(parameterName);
    		
    	}
    	return null;
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
    	if(this.callableResult != null && this.callableResult.getOrigineprocresult() != null)
    	{
    		return this.callableResult.getOrigineprocresult().getBoolean(parameterName);
    		
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
    	if(this.callableResult != null && this.callableResult.getOrigineprocresult() != null)
    	{
    		return this.callableResult.getOrigineprocresult().getByte(parameterName);
    		
    	}
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
    	if(this.callableResult != null && this.callableResult.getOrigineprocresult() != null)
    	{
    		return this.callableResult.getOrigineprocresult().getShort(parameterName);
    		
    	}
    	throw new SQLException("getShort(" + parameterName + ") failed:value=null.");
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
    	if(this.callableResult != null && this.callableResult.getOrigineprocresult() != null)
    	{
    		return this.callableResult.getOrigineprocresult().getInt(parameterName);
    		
    	}
    	throw new SQLException("getInt(" + parameterName + ") failed:value=null.");
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
    	if(this.callableResult != null && this.callableResult.getOrigineprocresult() != null)
    	{
    		return this.callableResult.getOrigineprocresult().getLong(parameterName);
    		
    	}
    	throw new SQLException("getInt(" + parameterName + ") failed:value=null.");
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
    	if(this.callableResult != null && this.callableResult.getOrigineprocresult() != null)
    	{
    		return this.callableResult.getOrigineprocresult().getFloat(parameterName);
    		
    	}
    	throw new SQLException("getFloat(" + parameterName + ") failed:value=null.");
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
    	if(this.callableResult != null && this.callableResult.getOrigineprocresult() != null)
    	{
    		return this.callableResult.getOrigineprocresult().getDouble(parameterName);
    		
    	}
    	throw new SQLException("getDouble(" + parameterName + ") failed:value=null.");
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
    	if(this.callableResult != null && this.callableResult.getOrigineprocresult() != null)
    	{
    		return this.callableResult.getOrigineprocresult().getBytes(parameterName);
    		
    	}
    	return null;
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
    	if(this.callableResult != null && this.callableResult.getOrigineprocresult() != null)
    	{
    		return this.callableResult.getOrigineprocresult().getDate(parameterName);
    		
    	}
    	return null;
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
    	if(this.callableResult != null && this.callableResult.getOrigineprocresult() != null)
    	{
    		return this.callableResult.getOrigineprocresult().getTime(parameterName);
    		
    	}
    	return null;
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
    	if(this.callableResult != null && this.callableResult.getOrigineprocresult() != null)
    	{
    		return this.callableResult.getOrigineprocresult().getTimestamp(parameterName);
    		
    	}
    	return null;
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
    	if(this.callableResult != null && this.callableResult.getOrigineprocresult() != null)
    	{
    		return this.callableResult.getOrigineprocresult().getObject(parameterName);
    		
    	}
    	return null;
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
    	if(this.callableResult != null && this.callableResult.getOrigineprocresult() != null)
    	{
    		return this.callableResult.getOrigineprocresult().getBigDecimal(parameterName);
    		
    	}
    	return null;
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
    	if(this.callableResult != null && this.callableResult.getOrigineprocresult() != null)
    	{
    		return this.callableResult.getOrigineprocresult().getObject(parameterName,map);
    		
    	}
    	return null;
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
    	if(this.callableResult != null && this.callableResult.getOrigineprocresult() != null)
    	{
    		return this.callableResult.getOrigineprocresult().getRef(parameterName);
    		
    	}
    	return null;
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
    	if(this.callableResult != null && this.callableResult.getOrigineprocresult() != null)
    	{
    		return this.callableResult.getOrigineprocresult().getBlob(parameterName);
    		
    	}
    	return null;
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
    	if(this.callableResult != null && this.callableResult.getOrigineprocresult() != null)
    	{
    		return this.callableResult.getOrigineprocresult().getClob(parameterName);
    		
    	}
    	return null;
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
    	if(this.callableResult != null && this.callableResult.getOrigineprocresult() != null)
    	{
    		return this.callableResult.getOrigineprocresult().getArray(parameterName);
    		
    	}
    	return null;
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
    	if(this.callableResult != null && this.callableResult.getOrigineprocresult() != null)
    	{
    		return this.callableResult.getOrigineprocresult().getDate(parameterName,cal);
    		
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
    	if(this.callableResult != null && this.callableResult.getOrigineprocresult() != null)
    	{
    		return this.callableResult.getOrigineprocresult().getTime(parameterName,cal);
    		
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
    	if(this.callableResult != null && this.callableResult.getOrigineprocresult() != null)
    	{
    		return this.callableResult.getOrigineprocresult().getTimestamp(parameterName,cal);
    		
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
    	if(this.callableResult != null && this.callableResult.getOrigineprocresult() != null)
    	{
    		return this.callableResult.getOrigineprocresult().getURL(parameterName);
    		
    	}
    	return null;
    }
    
    public void getFile(int index,File file) throws SQLException, IOException
    {
    	if(this.callableResult != null && this.callableResult.getOrigineprocresult() != null)
    	{
    		this.callableResult.getOrigineprocresult().getFile(index,file);
    		
    	}
    }
    
    
    public void getFile(String parameterName,File file) throws SQLException, IOException
    {
    	if(this.callableResult != null && this.callableResult.getOrigineprocresult() != null)
    	{
    		this.callableResult.getOrigineprocresult().getFile(parameterName,file);
    		
    	}
    }
    
    /**
     * 
     * @param sql
     */
    public void prepareCallable(String sql)
    {
    	this.Params = this.buildCallableParams(CALL_PROCEDURE);
    	Params.prepareSqlifo = new NewSQLInfo(sql);
    }
    
    public void prepareCallable(String preparedDBName,String sql) 
    {
    	this.prepareDBName = preparedDBName;
    	this.Params = this.buildCallableParams(CALL_PROCEDURE);
    	Params.prepareSqlifo = new NewSQLInfo(sql);
    }
    
    /**
     * 
     * @param sql
     * @throws SQLException 
     */
    private void prepareCallable(String sql,int calltype) throws SQLException
    {
    	if(calltype != CALL_FUNCTION && calltype != CALL_PROCEDURE)
    		throw new SQLException("CALL_TYPE error: Must be CALL_FUNCTION or CALL_PROCEDURE.");
    	this.Params = this.buildCallableParams(calltype);
    	Params.prepareSqlifo = new NewSQLInfo(sql);
    	
    }
    
    private void prepareCallable(String preparedDBName,String sql,int calltype) throws SQLException
    {
    	if(calltype != CALL_FUNCTION && calltype != CALL_PROCEDURE)
    		throw new SQLException("CALL_TYPE error: Must be CALL_FUNCTION or CALL_PROCEDURE.");
    	this.prepareDBName = preparedDBName;
    	this.Params = this.buildCallableParams(calltype);
    	Params.prepareSqlifo = new NewSQLInfo(sql);
    }
	public void executeCallable() throws java.sql.SQLException
	{
		executeCallable(null,Map.class,(RowHandler)null);
	}
	
	public String executeCallableForXML() throws java.sql.SQLException
	{
		return executeCallableForXML((Connection)null,(RowHandler)null);
	}
	
	public String executeCallableForXML(RowHandler rowhandler) throws java.sql.SQLException
	{
		return executeCallableForXML(null,rowhandler);
	}
	
	
	public String executeCallableForXML(Connection con) throws java.sql.SQLException
	{
		return executeCallableForXML(con,null);
	}
	
	public String executeCallableForXML(Connection con,RowHandler rowhandler) throws java.sql.SQLException
	{
		executeCallable(con,XMLMark.class,rowhandler);
		if(this.callableResult != null)
			return (String)callableResult.getCommonresult();
		return null;
	}
	
	public Object executeCallableForObject(Class objectClass,RowHandler rowHandler) throws java.sql.SQLException
	{
		
		return executeCallableForObject(null,objectClass,rowHandler) ;
	}
	
	public Object executeCallableForObject(Class objectClass) throws java.sql.SQLException
	{
		
		return executeCallableForObject(null,objectClass) ;
	}
	
	public Object executeCallableForObject(Connection con,Class objectClass) throws java.sql.SQLException
	{
		return executeCallableForObject( con, objectClass,null);
	}
	
	public Object executeCallableForObject(Connection con,Class objectClass,RowHandler rowHandler) throws java.sql.SQLException
	{
		executeCallable(con,objectClass,rowHandler);
		if(callableResult != null)
			return this.callableResult.getCommonresult();
		return null;
	}
	
	public void executeCallable(Connection con) throws java.sql.SQLException
	{
		executeCallable(con,Map.class,(RowHandler)null);
	}
	public void executeCallable(RowHandler rowHander) throws java.sql.SQLException
	{
		executeCallable(null,Map.class,rowHander);
	}
	
	public void executeCallable(Connection con,RowHandler rowHander) throws java.sql.SQLException
	{
		executeCallable(con,Map.class,rowHander);
	}
	
	/**
	 * 外部链接
	 * @param con
	 * @throws java.sql.SQLException
	 */
	protected void executeCallable(Connection con_,Class objectclass,RowHandler rowHander) throws java.sql.SQLException
	{
		if(this.Params == null || !(Params instanceof CallableParams))
			throw new SQLException("Callable statement do not been set corrected.");
		StatementInfo stmtInfo = null;

		CallableStatement cstmt = null;
		if(objectclass == null)
			objectclass = Map.class;

		List resources = null;
		try
		{
			stmtInfo = new StatementInfo(this.prepareDBName,
					Params.prepareSqlifo,
					false,
					 con_,
					 false);
			stmtInfo.init();

			CallableParams _params = (CallableParams)Params;
			
			cstmt = stmtInfo.prepareCallableStatement();
			resources = new ArrayList();
			super.setUpParams(Params, cstmt,resources);
			this.setUpCallableParams(_params, cstmt);
			if(showsql(stmtInfo.getDbname()))
			{
				log.debug("Execute JDBC callable statement:"+stmtInfo.getSql());
			}
			boolean success = cstmt.execute();
			
			if(_params.outParams.size() > 0)
			{
				this.callableResult = new ResultMap();
			}
			if(_params.call_type == CALL_FUNCTION)
			{
				if(success)
				{
					ResultSet res = cstmt.getResultSet();			
					stmtInfo.addResultSet(res);
				}									
				if(callableResult != null)
					callableResult.handle(cstmt, null, objectclass, _params,stmtInfo,rowHander);				
			}
			else
			{
				if(callableResult != null)
					callableResult.handle(cstmt, null, objectclass, _params,stmtInfo,rowHander);	
			}		
			
		}
		catch(Exception e)
		{
			try{
				String error = new StringBuffer("Execute ")
				.append(Params.prepareSqlifo.getNewsql()).append(" on ")
				.append(prepareDBName)
				.append(" failed:").append(e.getMessage()).toString();
				System.out.println(error);
				log.error(error,e);
			}
			catch(Exception ie)
			{
				
			}
			if(stmtInfo != null)
				stmtInfo.errorHandle(e);
			if(e instanceof SQLException)
				throw (SQLException)e;
			else
			{
				throw new NestedSQLException(e.getMessage(),e);
			}
			
		}
		finally
		{
			if(stmtInfo != null)
				stmtInfo.dofinally();
			stmtInfo = null;
			if(Params != null)
				Params.clear();
			this.releaseResources(resources);
			resources = null;
		}
	}
	
	private ResultMap callableResult;
	
	public Object getFunctionResult()
	{
		if(callableResult != null)
			return callableResult.getFunctionResult();
		else
			return null;
	}
	
	
	
	protected void setUpCallableParams(CallableParams params,CallableStatement cstmt) throws SQLException
	{
		for(int i = 0; i < params.callParams.size(); i ++ )
		{
			CallableParam param = (CallableParam)params.callParams.get(i);
			/**
		     * Registers the OUT parameter in ordinal position 
		     * <code>parameterIndex</code> to the JDBC type 
		     * <code>sqlType</code>.  All OUT parameters must be registered
		     * before a stored procedure is executed.
		     * <p>
		     * The JDBC type specified by <code>sqlType</code> for an OUT
		     * parameter determines the Java type that must be used
		     * in the <code>get</code> method to read the value of that parameter.
		     * <p>
		     * If the JDBC type expected to be returned to this output parameter
		     * is specific to this particular database, <code>sqlType</code>
		     * should be <code>java.sql.Types.OTHER</code>.  The method 
		     * {@link #getObject} retrieves the value.
		     *
		     * @param parameterIndex the first parameter is 1, the second is 2, 
		     *        and so on
		     * @param sqlType the JDBC type code defined by <code>java.sql.Types</code>.
		     *        If the parameter is of JDBC type <code>NUMERIC</code>
		     *        or <code>DECIMAL</code>, the version of
		     *        <code>registerOutParameter</code> that accepts a scale value 
		     *        should be used.
		     *
		     * @exception SQLException if a database access error occurs
		     * @see Types 
		     */
			
		    if(param.method.equals(CallableParam.registerOutParameter_int_parameterIndex_int_sqlType))
		    {
		    	cstmt.registerOutParameter(param.index, param.sqlType);
		    }
		    
			

		    /**
		     * Registers the parameter in ordinal position
		     * <code>parameterIndex</code> to be of JDBC type
		     * <code>sqlType</code>.  This method must be called
		     * before a stored procedure is executed.
		     * <p>
		     * The JDBC type specified by <code>sqlType</code> for an OUT
		     * parameter determines the Java type that must be used
		     * in the <code>get</code> method to read the value of that parameter.
		     * <p>
		     * This version of <code>registerOutParameter</code> should be
		     * used when the parameter is of JDBC type <code>NUMERIC</code>
		     * or <code>DECIMAL</code>.
		     *
		     * @param parameterIndex the first parameter is 1, the second is 2,
		     * and so on
		     * @param sqlType the SQL type code defined by <code>java.sql.Types</code>.
		     * @param scale the desired number of digits to the right of the
		     * decimal point.  It must be greater than or equal to zero.
		     * @exception SQLException if a database access error occurs
		     * @see Types 
		     */
		    else if (param.method.equals(CallableParam.registerOutParameter_int_parameterIndex_int_sqlType_int_scale))
		    {
		    	cstmt.registerOutParameter(param.index, param.sqlType, param.scale);
		    }
			
		    
		    /**
		     * Registers the designated output parameter.  This version of 
		     * the method <code>registerOutParameter</code>
		     * should be used for a user-defined or <code>REF</code> output parameter.  Examples
		     * of user-defined types include: <code>STRUCT</code>, <code>DISTINCT</code>,
		     * <code>JAVA_OBJECT</code>, and named array types.
		     *
		     * Before executing a stored procedure call, you must explicitly
		     * call <code>registerOutParameter</code> to register the type from
		     * <code>java.sql.Types</code> for each
		     * OUT parameter.  For a user-defined parameter, the fully-qualified SQL
		     * type name of the parameter should also be given, while a <code>REF</code>
		     * parameter requires that the fully-qualified type name of the
		     * referenced type be given.  A JDBC driver that does not need the
		     * type code and type name information may ignore it.   To be portable,
		     * however, applications should always provide these values for
		     * user-defined and <code>REF</code> parameters.
		     *
		     * Although it is intended for user-defined and <code>REF</code> parameters,
		     * this method may be used to register a parameter of any JDBC type.
		     * If the parameter does not have a user-defined or <code>REF</code> type, the
		     * <i>typeName</i> parameter is ignored.
		     *
		     * <P><B>Note:</B> When reading the value of an out parameter, you
		     * must use the getter method whose Java type corresponds to the
		     * parameter's registered SQL type.
		     *
		     * @param paramIndex the first parameter is 1, the second is 2,...
		     * @param sqlType a value from {@link java.sql.Types}
		     * @param typeName the fully-qualified name of an SQL structured type
		     * @exception SQLException if a database access error occurs
		     * @see Types
		     * @since 1.2
		     */
		    else if (param.method.equals(CallableParam.registerOutParameter_int_paramIndex_int_sqlType_String_typeName))
		    {
		    	cstmt.registerOutParameter(param.index, param.sqlType, param.typeName);
		    }

		  //--------------------------JDBC 3.0-----------------------------

		    /**
		     * Registers the OUT parameter named 
		     * <code>parameterName</code> to the JDBC type 
		     * <code>sqlType</code>.  All OUT parameters must be registered
		     * before a stored procedure is executed.
		     * <p>
		     * The JDBC type specified by <code>sqlType</code> for an OUT
		     * parameter determines the Java type that must be used
		     * in the <code>get</code> method to read the value of that parameter.
		     * <p>
		     * If the JDBC type expected to be returned to this output parameter
		     * is specific to this particular database, <code>sqlType</code>
		     * should be <code>java.sql.Types.OTHER</code>.  The method 
		     * {@link #getObject} retrieves the value.
		     * @param parameterName the name of the parameter
		     * @param sqlType the JDBC type code defined by <code>java.sql.Types</code>.
		     * If the parameter is of JDBC type <code>NUMERIC</code>
		     * or <code>DECIMAL</code>, the version of
		     * <code>registerOutParameter</code> that accepts a scale value 
		     * should be used.
		     * @exception SQLException if a database access error occurs
		     * @since 1.4
		     * @see Types 
		     */
		    else if (param.method.equals(CallableParam.registerOutParameter_String_parameterName_int_sqlType))
		    {
		    	cstmt.registerOutParameter(param.parameterName, param.sqlType);
		    }
			

		    /**
		     * Registers the parameter named 
		     * <code>parameterName</code> to be of JDBC type
		     * <code>sqlType</code>.  This method must be called
		     * before a stored procedure is executed.
		     * <p>
		     * The JDBC type specified by <code>sqlType</code> for an OUT
		     * parameter determines the Java type that must be used
		     * in the <code>get</code> method to read the value of that parameter.
		     * <p>
		     * This version of <code>registerOutParameter</code> should be
		     * used when the parameter is of JDBC type <code>NUMERIC</code>
		     * or <code>DECIMAL</code>.
		     * @param parameterName the name of the parameter
		     * @param sqlType SQL type code defined by <code>java.sql.Types</code>.
		     * @param scale the desired number of digits to the right of the
		     * decimal point.  It must be greater than or equal to zero.
		     * @exception SQLException if a database access error occurs
		     * @since 1.4
		     * @see Types 
		     */
		    else if (param.method.equals(CallableParam.registerOutParameter_String_parameterName_int_sqlType_int_scale ))
		    {
		    	cstmt.registerOutParameter(param.parameterName, param.sqlType, param.scale);
		    }

		    /**
		     * Registers the designated output parameter.  This version of 
		     * the method <code>registerOutParameter</code>
		     * should be used for a user-named or REF output parameter.  Examples
		     * of user-named types include: STRUCT, DISTINCT, JAVA_OBJECT, and
		     * named array types.
		     *
		     * Before executing a stored procedure call, you must explicitly
		     * call <code>registerOutParameter</code> to register the type from
		     * <code>java.sql.Types</code> for each
		     * OUT parameter.  For a user-named parameter the fully-qualified SQL
		     * type name of the parameter should also be given, while a REF
		     * parameter requires that the fully-qualified type name of the
		     * referenced type be given.  A JDBC driver that does not need the
		     * type code and type name information may ignore it.   To be portable,
		     * however, applications should always provide these values for
		     * user-named and REF parameters.
		     *
		     * Although it is intended for user-named and REF parameters,
		     * this method may be used to register a parameter of any JDBC type.
		     * If the parameter does not have a user-named or REF type, the
		     * typeName parameter is ignored.
		     *
		     * <P><B>Note:</B> When reading the value of an out parameter, you
		     * must use the <code>getXXX</code> method whose Java type XXX corresponds to the
		     * parameter's registered SQL type.
		     *
		     * @param parameterName the name of the parameter
		     * @param sqlType a value from {@link java.sql.Types}
		     * @param typeName the fully-qualified name of an SQL structured type
		     * @exception SQLException if a database access error occurs
		     * @see Types
		     * @since 1.4
		     */
		    else if (param.method.equals(CallableParam.registerOutParameter_String_parameterName_int_sqlType_String_typeName))
		    {
		    	cstmt.registerOutParameter(param.parameterName, param.sqlType, param.typeName);
		    }
		    
		    /**
		     * Sets the designated parameter to the given input stream, which will have 
		     * the specified number of bytes.
		     * When a very large ASCII value is input to a <code>LONGVARCHAR</code>
		     * parameter, it may be more practical to send it via a
		     * <code>java.io.InputStream</code>. Data will be read from the stream
		     * as needed until end-of-file is reached.  The JDBC driver will
		     * do any necessary conversion from ASCII to the database char format.
		     * 
		     * <P><B>Note:</B> This stream object can either be a standard
		     * Java stream object or your own subclass that implements the
		     * standard interface.
		     *
		     * @param parameterName the name of the parameter
		     * @param x the Java input stream that contains the ASCII parameter value
		     * @param length the number of bytes in the stream 
		     * @exception SQLException if a database access error occurs
		     * @since 1.4
		     */
		    else if (param.method.equals(CallableParam.setAsciiStream_String_parameterName_InputStream_x_int_length ))
		    {
		    	Object[] value = (Object[])param.data ;
		    	cstmt.setAsciiStream(param.parameterName, (java.io.InputStream)value[0], ((Integer)value[1]).intValue());
		    	
		    }
		    
		    /**
		     * Sets the designated parameter to the given
		     * <code>java.math.BigDecimal</code> value.  
		     * The driver converts this to an SQL <code>NUMERIC</code> value when
		     * it sends it to the database.
		     *
		     * @param parameterName the name of the parameter
		     * @param x the parameter value
		     * @exception SQLException if a database access error occurs
		     * @see #getBigDecimal
		     * @since 1.4
		     */
		    else if (param.method.equals(CallableParam.setBigDecimal_String_parameterName_BigDecimal_x))
		    {
		    	BigDecimal value = (BigDecimal)param.data;
		    	cstmt.setBigDecimal(param.parameterName, value);
		    }
		    
		    
		    /**
		     * Sets the designated parameter to the given input stream, which will have 
		     * the specified number of bytes.
		     * When a very large binary value is input to a <code>LONGVARBINARY</code>
		     * parameter, it may be more practical to send it via a
		     * <code>java.io.InputStream</code> object. The data will be read from the stream
		     * as needed until end-of-file is reached.
		     * 
		     * <P><B>Note:</B> This stream object can either be a standard
		     * Java stream object or your own subclass that implements the
		     * standard interface.
		     *
		     * @param parameterName the name of the parameter
		     * @param x the java input stream which contains the binary parameter value
		     * @param length the number of bytes in the stream 
		     * @exception SQLException if a database access error occurs
		     * @since 1.4
		     */
		    else if (param.method.equals(CallableParam.setBinaryStream_String_parameterName_InputStream_x_int_length))
		    {
		    	Object[] value = (Object[])param.data;
		    	cstmt.setBinaryStream(param.parameterName,(InputStream)value[0], ((Integer)value[1]).intValue());
		    }
					    
					    
					    
		/**
		     * Sets the designated parameter to the given Java <code>boolean</code> value.
		     * The driver converts this
		     * to an SQL <code>BIT</code> value when it sends it to the database.
		     *
		     * @param parameterName the name of the parameter
		     * @param x the parameter value
		     * @exception SQLException if a database access error occurs
		     * @see #getBoolean
		     * @since 1.4
		     */
		    else if (param.method.equals(CallableParam.setBoolean_String_parameterName_boolean_x))
		    {
		    	cstmt.setBoolean(param.parameterName,((Boolean)param.data).booleanValue());
		    }
		    
		    /**
		     * Sets the designated parameter to the given Java <code>byte</code> value.  
		     * The driver converts this
		     * to an SQL <code>TINYINT</code> value when it sends it to the database.
		     *
		     * @param parameterName the name of the parameter
		     * @param x the parameter value
		     * @exception SQLException if a database access error occurs
		     * @see #getByte
		     * @since 1.4
		     */
		    else if (param.method.equals(CallableParam.setByte_String_parameterName_byte_x))
		    {
		    	cstmt.setByte(param.parameterName,((Byte)param.data).byteValue());
		    }
		    
		        /**
		     * Sets the designated parameter to the given Java array of bytes.  
		     * The driver converts this to an SQL <code>VARBINARY</code> or 
		     * <code>LONGVARBINARY</code> (depending on the argument's size relative 
		     * to the driver's limits on <code>VARBINARY</code> values) when it sends 
		     * it to the database.
		     *
		     * @param parameterName the name of the parameter
		     * @param x the parameter value 
		     * @exception SQLException if a database access error occurs
		     * @see #getBytes
		     * @since 1.4
		     */
		    else if (param.method.equals(CallableParam.setBytes_String_parameterName_byteArray_x))
		    {
		    	cstmt.setBytes(param.parameterName,((byte[])param.data));
		    }
		    
		    
		    /**
		     * Sets the designated parameter to the given <code>Reader</code>
		     * object, which is the given number of characters long.
		     * When a very large UNICODE value is input to a <code>LONGVARCHAR</code>
		     * parameter, it may be more practical to send it via a
		     * <code>java.io.Reader</code> object. The data will be read from the stream
		     * as needed until end-of-file is reached.  The JDBC driver will
		     * do any necessary conversion from UNICODE to the database char format.
		     * 
		     * <P><B>Note:</B> This stream object can either be a standard
		     * Java stream object or your own subclass that implements the
		     * standard interface.
		     *
		     * @param parameterName the name of the parameter
		     * @param reader the <code>java.io.Reader</code> object that
		     *        contains the UNICODE data used as the designated parameter
		     * @param length the number of characters in the stream 
		     * @exception SQLException if a database access error occurs
		     * @since 1.4
		     */
		    else if (param.method.equals(CallableParam.setCharacterStream_String_parameterName_Reader_reader_int_length))
		    {
		    	Object[] value = (Object[])param.data;
		    	cstmt.setCharacterStream(param.parameterName, (java.io.Reader)value[0], ((Integer)value[1]).intValue());
		    }

		 /**
		     * Sets the designated parameter to the given <code>java.sql.Date</code> value.  
		     * The driver converts this
		     * to an SQL <code>DATE</code> value when it sends it to the database.
		     *
		     * @param parameterName the name of the parameter
		     * @param x the parameter value
		     * @exception SQLException if a database access error occurs
		     * @see #getDate
		     * @since 1.4
		     */
		    else if (param.method.equals(CallableParam.setDate_String_parameterName_Date_x))
		    {
		    	cstmt.setDate(param.parameterName,((Date)param.data));
		    }
			
		    
		    /**
		     * Sets the designated parameter to the given <code>java.sql.Date</code> value,
		     * using the given <code>Calendar</code> object.  The driver uses
		     * the <code>Calendar</code> object to construct an SQL <code>DATE</code> value,
		     * which the driver then sends to the database.  With a
		     * a <code>Calendar</code> object, the driver can calculate the date
		     * taking into account a custom timezone.  If no
		     * <code>Calendar</code> object is specified, the driver uses the default
		     * timezone, which is that of the virtual machine running the application.
		     *
		     * @param parameterName the name of the parameter
		     * @param x the parameter value
		     * @param cal the <code>Calendar</code> object the driver will use
		     *            to construct the date
		     * @exception SQLException if a database access error occurs
		     * @see #getDate
		     * @since 1.4
		     */
		    else if (param.method.equals(CallableParam.setDate_String_parameterName_Date_x_Calendar_cal))
		    {
		    	Object[] value = (Object[])param.data;
		    	cstmt.setDate(param.parameterName, (Date)value[0], (Calendar)value[1]);
		    }
		    
		        /**
		     * Sets the designated parameter to the given Java <code>double</code> value.  
		     * The driver converts this
		     * to an SQL <code>DOUBLE</code> value when it sends it to the database.
		     *
		     * @param parameterName the name of the parameter
		     * @param x the parameter value
		     * @exception SQLException if a database access error occurs
		     * @see #getDouble
		     * @since 1.4
		     */
		    else if (param.method.equals(CallableParam.setDouble_String_parameterName_double_x))
		    {
		    	cstmt.setDouble(param.parameterName, ((Double)param.data).doubleValue());
		    }
		    
		      /**
		     * Sets the designated parameter to the given Java <code>float</code> value. 
		     * The driver converts this
		     * to an SQL <code>FLOAT</code> value when it sends it to the database.
		     *
		     * @param parameterName the name of the parameter
		     * @param x the parameter value
		     * @exception SQLException if a database access error occurs
		     * @see #getFloat
		     * @since 1.4
		     */
		    else if (param.method.equals(CallableParam.setFloat_String_parameterName_float_x))
		    {
		    	cstmt.setFloat(param.parameterName, ((Float)param.data).floatValue());
		    }
		    
		        /**
		     * Sets the designated parameter to the given Java <code>int</code> value.  
		     * The driver converts this
		     * to an SQL <code>INTEGER</code> value when it sends it to the database.
		     *
		     * @param parameterName the name of the parameter
		     * @param x the parameter value
		     * @exception SQLException if a database access error occurs
		     * @see #getInt
		     * @since 1.4
		     */
		    else if (param.method.equals(CallableParam.setInt_String_parameterName_int_x))
		    {
		    	cstmt.setInt(param.parameterName, ((Integer)param.data).intValue());
		    }
		    
		    
		        /**
		     * Sets the designated parameter to the given Java <code>long</code> value. 
		     * The driver converts this
		     * to an SQL <code>BIGINT</code> value when it sends it to the database.
		     *
		     * @param parameterName the name of the parameter
		     * @param x the parameter value
		     * @exception SQLException if a database access error occurs
		     * @see #getLong
		     * @since 1.4
		     */
		    else if (param.method.equals(CallableParam.setLong_String_parameterName_long_x))
		    {
		    	cstmt.setLong(param.parameterName, ((Long)param.data).longValue());
		    }
		    
		        /**
		     * Sets the designated parameter to SQL <code>NULL</code>.
		     *
		     * <P><B>Note:</B> You must specify the parameter's SQL type.
		     *
		     * @param parameterName the name of the parameter
		     * @param sqlType the SQL type code defined in <code>java.sql.Types</code>
		     * @exception SQLException if a database access error occurs
		     * @since 1.4
		     */
		    else if (param.method.equals(CallableParam.setNull_String_parameterName_int_sqlType))
		    {
		    	cstmt.setNull(param.parameterName, ((Integer)param.data).intValue());
		    }
		    /**
		     * Sets the designated parameter to SQL <code>NULL</code>.
		     * This version of the method <code>setNull</code> should
		     * be used for user-defined types and REF type parameters.  Examples
		     * of user-defined types include: STRUCT, DISTINCT, JAVA_OBJECT, and 
		     * named array types.
		     *
		     * <P><B>Note:</B> To be portable, applications must give the
		     * SQL type code and the fully-qualified SQL type name when specifying
		     * a NULL user-defined or REF parameter.  In the case of a user-defined type 
		     * the name is the type name of the parameter itself.  For a REF 
		     * parameter, the name is the type name of the referenced type.  If 
		     * a JDBC driver does not need the type code or type name information, 
		     * it may ignore it.     
		     *
		     * Although it is intended for user-defined and Ref parameters,
		     * this method may be used to set a null parameter of any JDBC type.
		     * If the parameter does not have a user-defined or REF type, the given
		     * typeName is ignored.
		     *
		     *
		     * @param parameterName the name of the parameter
		     * @param sqlType a value from <code>java.sql.Types</code>
		     * @param typeName the fully-qualified name of an SQL user-defined type;
		     *        ignored if the parameter is not a user-defined type or 
		     *        SQL <code>REF</code> value
		     * @exception SQLException if a database access error occurs
		     * @since 1.4
		     */
		    else if (param.method.equals(CallableParam.setNull_String_parameterName_int_sqlType_String_typeName))
		    {
		    	Object[] value = (Object[])param.data;
		    	cstmt.setNull(param.parameterName, ((Integer)value[0]).intValue(),(String)value[1]);
		    }
			


		    /**
		     * Sets the value of the designated parameter with the given object. 
		     * The second parameter must be of type <code>Object</code>; therefore, the
		     * <code>java.lang</code> equivalent objects should be used for built-in types.
		     *
		     * <p>The JDBC specification specifies a standard mapping from
		     * Java <code>Object</code> types to SQL types.  The given argument 
		     * will be converted to the corresponding SQL type before being
		     * sent to the database.
		     *
		     * <p>Note that this method may be used to pass datatabase-
		     * specific abstract data types, by using a driver-specific Java
		     * type.
		     *
		     * If the object is of a class implementing the interface <code>SQLData</code>,
		     * the JDBC driver should call the method <code>SQLData.writeSQL</code>
		     * to write it to the SQL data stream.
		     * If, on the other hand, the object is of a class implementing
		     * <code>Ref</code>, <code>Blob</code>, <code>Clob</code>, <code>Struct</code>, 
		     * or <code>Array</code>, the driver should pass it to the database as a 
		     * value of the corresponding SQL type.
		     * <P>
		     * This method throws an exception if there is an ambiguity, for example, if the
		     * object is of a class implementing more than one of the interfaces named above.
		     *
		     * @param parameterName the name of the parameter
		     * @param x the object containing the input parameter value 
		     * @exception SQLException if a database access error occurs or if the given
		     *            <code>Object</code> parameter is ambiguous
		     * @see #getObject
		     * @since 1.4
		     */
		    else if (param.method.equals(CallableParam.setObject_String_parameterName_Object_x))
		    {
		    	cstmt.setObject(param.parameterName, param.data);
		    }
		    
		    
		        /**
		     * Sets the value of the designated parameter with the given object.
		     * This method is like the method <code>setObject</code>
		     * above, except that it assumes a scale of zero.
		     *
		     * @param parameterName the name of the parameter
		     * @param x the object containing the input parameter value
		     * @param targetSqlType the SQL type (as defined in java.sql.Types) to be 
		     *                      sent to the database
		     * @exception SQLException if a database access error occurs
		     * @see #getObject
		     * @since 1.4
		     */
		    else if (param.method.equals(CallableParam.setObject_String_parameterName_Object_x_int_targetSqlType))
		    {
		    	cstmt.setObject(param.parameterName, param.data,param.sqlType);
		    }
		    		
		    
		    
		        /**
		     * Sets the value of the designated parameter with the given object. The second
		     * argument must be an object type; for integral values, the
		     * <code>java.lang</code> equivalent objects should be used.
		     *
		     * <p>The given Java object will be converted to the given targetSqlType
		     * before being sent to the database.
		     *
		     * If the object has a custom mapping (is of a class implementing the 
		     * interface <code>SQLData</code>),
		     * the JDBC driver should call the method <code>SQLData.writeSQL</code> to write it 
		     * to the SQL data stream.
		     * If, on the other hand, the object is of a class implementing
		     * <code>Ref</code>, <code>Blob</code>, <code>Clob</code>, <code>Struct</code>, 
		     * or <code>Array</code>, the driver should pass it to the database as a 
		     * value of the corresponding SQL type.
		     * <P>
		     * Note that this method may be used to pass datatabase-
		     * specific abstract data types. 
		     *
		     * @param parameterName the name of the parameter
		     * @param x the object containing the input parameter value
		     * @param targetSqlType the SQL type (as defined in java.sql.Types) to be 
		     * sent to the database. The scale argument may further qualify this type.
		     * @param scale for java.sql.Types.DECIMAL or java.sql.Types.NUMERIC types,
		     *          this is the number of digits after the decimal point.  For all other
		     *          types, this value will be ignored.
		     * @exception SQLException if a database access error occurs
		     * @see Types
		     * @see #getObject
		     * @since 1.4 
		     */
		    else if (param.method.equals(CallableParam.setObject_String_parameterName_Object_x_int_targetSqlType_int_scale))
		    {
		    	cstmt.setObject(param.parameterName, param.data, param.sqlType, param.scale);
		    }
		    
		    
		        /**
		     * Sets the designated parameter to the given Java <code>short</code> value. 
		     * The driver converts this
		     * to an SQL <code>SMALLINT</code> value when it sends it to the database.
		     *
		     * @param parameterName the name of the parameter
		     * @param x the parameter value
		     * @exception SQLException if a database access error occurs
		     * @see #getShort
		     * @since 1.4
		     */
		    else if (param.method.equals(CallableParam.setShort_String_parameterName_short_x ))
		    {
		    	cstmt.setShort(param.parameterName, ((Short)param.data).shortValue());
		    }
		    
		        /**
		     * Sets the designated parameter to the given Java <code>String</code> value. 
		     * The driver converts this
		     * to an SQL <code>VARCHAR</code> or <code>LONGVARCHAR</code> value
		     * (depending on the argument's
		     * size relative to the driver's limits on <code>VARCHAR</code> values)
		     * when it sends it to the database.
		     *
		     * @param parameterName the name of the parameter
		     * @param x the parameter value
		     * @exception SQLException if a database access error occurs
		     * @see #getString
		     * @since 1.4
		     */
		    else if (param.method.equals(CallableParam.setString_String_parameterName_String_x))
		    {
		    	cstmt.setString(param.parameterName, (String)param.data);
		    }
		    
		        /**
		     * Sets the designated parameter to the given <code>java.sql.Time</code> value.  
		     * The driver converts this
		     * to an SQL <code>TIME</code> value when it sends it to the database.
		     *
		     * @param parameterName the name of the parameter
		     * @param x the parameter value
		     * @exception SQLException if a database access error occurs
		     * @see #getTime
		     * @since 1.4
		     */
		    else if (param.method.equals(CallableParam.setTime_String_parameterName_Time_x))
		    {
		    	cstmt.setTime(param.parameterName, (Time)param.data);
		    }
			
		    
		        /**
		     * Sets the designated parameter to the given <code>java.sql.Time</code> value,
		     * using the given <code>Calendar</code> object.  The driver uses
		     * the <code>Calendar</code> object to construct an SQL <code>TIME</code> value,
		     * which the driver then sends to the database.  With a
		     * a <code>Calendar</code> object, the driver can calculate the time
		     * taking into account a custom timezone.  If no
		     * <code>Calendar</code> object is specified, the driver uses the default
		     * timezone, which is that of the virtual machine running the application.
		     *
		     * @param parameterName the name of the parameter
		     * @param x the parameter value
		     * @param cal the <code>Calendar</code> object the driver will use
		     *            to construct the time
		     * @exception SQLException if a database access error occurs
		     * @see #getTime
		     * @since 1.4
		     */
		    else if (param.method.equals(CallableParam.setTime_String_parameterName_Time_x_Calendar_cal))
		    {
		    	Object[] value = (Object[])param.data;
		    	cstmt.setTime(param.parameterName, (Time)value[0],(Calendar)value[1]);
		    }
			
		    
		        /**
		     * Sets the designated parameter to the given <code>java.sql.Timestamp</code> value.  
		     * The driver
		     * converts this to an SQL <code>TIMESTAMP</code> value when it sends it to the
		     * database.
		     *
		     * @param parameterName the name of the parameter
		     * @param x the parameter value 
		     * @exception SQLException if a database access error occurs
		     * @see #getTimestamp
		     * @since 1.4
		     */
		    else if (param.method.equals(CallableParam.setTimestamp_String_parameterName_Timestamp_x))
		    {
		    	cstmt.setTimestamp(param.parameterName, (Timestamp)param.data);
		    }
			
		    
		        /**
		     * Sets the designated parameter to the given <code>java.sql.Timestamp</code> value,
		     * using the given <code>Calendar</code> object.  The driver uses
		     * the <code>Calendar</code> object to construct an SQL <code>TIMESTAMP</code> value,
		     * which the driver then sends to the database.  With a
		     * a <code>Calendar</code> object, the driver can calculate the timestamp
		     * taking into account a custom timezone.  If no
		     * <code>Calendar</code> object is specified, the driver uses the default
		     * timezone, which is that of the virtual machine running the application.
		     *
		     * @param parameterName the name of the parameter
		     * @param x the parameter value 
		     * @param cal the <code>Calendar</code> object the driver will use
		     *            to construct the timestamp
		     * @exception SQLException if a database access error occurs
		     * @see #getTimestamp
		     * @since 1.4
		     */
		    else if (param.method.equals(CallableParam.setTimestamp_String_parameterName_Timestamp_x_Calendar_cal))
		    {
		    	Object[] value = (Object[])param.data;
		    	cstmt.setTimestamp(param.parameterName, (Timestamp)value[0],(Calendar)value[1]);
		    }
		    
		        /**
		     * Sets the designated parameter to the given <code>java.net.URL</code> object.
		     * The driver converts this to an SQL <code>DATALINK</code> value when
		     * it sends it to the database.
		     *
		     * @param parameterName the name of the parameter
		     * @param val the parameter value
		     * @exception SQLException if a database access error occurs,
		     *            or if a URL is malformed
		     * @see #getURL
		     * @since 1.4
		     */
		    else if (param.method.equals(CallableParam.setURL_String_parameterName_URL_val))
		    {
		    	cstmt.setURL(param.parameterName, (URL)param.data);
		    }
		        		
		}
	}	
}
