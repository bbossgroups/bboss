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

import com.frameworkset.util.SimpleStringUtil;
import com.frameworkset.util.VariableHandler;
import com.frameworkset.util.VariableHandler.Variable;
import org.frameworkset.persitent.type.*;
import org.frameworkset.persitent.util.PersistentSQLVariable;


/**
 * 
 * 
 * <p>Title: PreparedDBUtil.java</p>
 *
 * <p>Description: 预编译参数封装对象</p>
 *
 * <p>
 * bboss workgroup
 * </p>
 * <p>
 * Copyright (c) 2007
 * </p>
 * 
 * @Date 2009-6-1 下午08:58:51
 * @author biaoping.yin
 * @version 1.0
 */
public class Param
{
	/**
	 * 定义参数对应的方法,总共35个方法
	 */
//	public static final String SET_ARRAY_INT_ARRAY = "setArray(int, Array)";
	public static final BaseTypeMethod SET_ARRAY_INT_ARRAY = new ArrayTypeMethod();
	/**setAsciiStream(int i, InputStream x, int length)*/
//	public static final String SET_AsciiStream_INT_InputStream_INT = "setAsciiStream(int, InputStream, int)";
	public static final BaseTypeMethod SET_AsciiStream_INT_InputStream_INT = new AsciiStreamInputStreamTypeMethod();

//	public static final String SET_BigDecimal_INT_BigDecimal = "setBigDecimal(int, BigDecimal)";
	public static final BaseTypeMethod SET_BigDecimal_INT_BigDecimal = new BigDecimalTypeMethod();
	public static final BaseTypeMethod setBinaryStream_int_InputStream_int = new BinaryStream_int_InputStreamTypeMethod();//"setBinaryStream(int, InputStream, int)";
	public static final BaseTypeMethod setBlob_int_InputStream_int = new Blob_int_InputStream_intTypeMethod();//"setBinaryStream(int, InputStream, int)";
	public static final BaseTypeMethod setBlob_int_bytearray = new Blob_int_bytearrayTypeMethod();//"setBlob(int, byte[])";
	public static final BaseTypeMethod setBlob_int_blob = new Blob_int_blobTypeMethod();//"setBlob(int,Blob])";
	public static final BaseTypeMethod setBlob_int_bytearray_String = new Blob_int_bytearray_StringTypeMethod();//"setBlob(int, byte[], String)";
	public static final BaseTypeMethod setBlob_int_File = new Blob_int_FileTypeMethod();//"setBlob(int, File)";
	public static final BaseTypeMethod setBlob_int_File_String = new Blob_int_File_StringTypeMethod();//"setBlob(int, File, String)";
	public static final BaseTypeMethod setBoolean_int_boolean = new BooleanTypeMethod();//"setBoolean(int, boolean)";
	public static final BaseTypeMethod setByte_int_byte = new ByteTypeMethod();//"setByte(int, byte)";
	public static final BaseTypeMethod setBytes_int_bytearray = new Bytes_int_bytearrayTypeMethod();//"setBytes(int, byte[])";
	public static final BaseTypeMethod setCharacterStream_int_Reader_int = new CharacterStream_int_Reader_intTypeMethod();//"setCharacterStream(int, Reader, int)";
	public static final BaseTypeMethod setClob_int_String = new Clob_int_StringTypeMethod();//"setClob(int, String)";
	public static final BaseTypeMethod setClob_int_Clob = new Clob_int_ClobTypeMethod();//"setClob(int, Clob)";
	public static final BaseTypeMethod setClob_int_String_String = new Clob_int_String_StringTypeMethod();//"setClob(int, String, String)";
	public static final BaseTypeMethod setDate_int_sqlDate = new DateSQLDateTypeMethod();//"setDate(int, java.sql.Date)";
	public static final BaseTypeMethod setDate_int_Date_Calendar = new DateIntDateCalandorTypeMethod();//"setDate(int, Date, Calendar)";
	public static final BaseTypeMethod setDate_int_utilDate = new DateUtilDateTypeMethod();//"setDate(int, java.util.Date)";
	public static final BaseTypeMethod setDouble_int_double = new DoubleTypeMethod();//"setDouble(int, double)";
	public static final BaseTypeMethod setFloat_int_float = new FloatTypeMethod();//"setFloat(int, float)";
	public static final BaseTypeMethod setInt_int_int = new IntTypeMethod();//"setInt(int, int)";
	public static final BaseTypeMethod setLong_int_long = new LongTypeMethod();//"setLong(int, long)";
	public static final BaseTypeMethod setNull_int_int = new NullIntTypeMethod();//"setNull(int, int)";
	public static final BaseTypeMethod setNull_int_int_String = new NullIntStringTypeMethod();//"setNull(int, int, String)";
	public static final BaseTypeMethod setObject_int_Object = new ObjectTypeMethod();//"setObject(int, Object)";
	public static final BaseTypeMethod setObject_int_Object_int = new Object_int_Object_intTypeMethod();//"setObject(int, Object, int)";
	public static final BaseTypeMethod setObject_int_Object_int_int = new Object_int_Object_int_intTypeMethod();//"setObject(int, Object, int, int)";

	public static final BaseTypeMethod setRef_int_Ref = new Ref_int_RefTypeMethod();//"setRef(int, Ref)";
	public static final BaseTypeMethod setShort_int_short = new ShortTypeMethod();//"setShort(int, short)";
	public static final BaseTypeMethod setString_int_String =new StringTypeMethod();// "setString(int, String)";
	public static final BaseTypeMethod setTime_int_Time = new Time_int_TimeTypeMethod();//"setTime(int, Time)";
	public static final BaseTypeMethod setTime_int_Time_Calendar = new Time_int_Time_CalendarTypeMethod();//"setTime(int, Time, Calendar)";
	//	public static final String setTimestamp_int_Timestamp = "setTimestamp(int, Timestamp)";
	public static final BaseTypeMethod setTimestamp_int_Timestamp = new TimestampTypeMethod();
	public static final BaseTypeMethod setTimestamp_int_Timestamp_Calendar = new TimestampIntTimestampCalandorTypeMethod();//"setTimestamp(int, Timestamp, Calendar)";
	public static final BaseTypeMethod setUnicodeStream_int_InputStream_int = new UnicodeStream_int_InputStream_intTypeMethod();//"setUnicodeStream(int, InputStream, int)";
	public static final BaseTypeMethod setClob_int_File = new Clob_int_FileTypeMethod();//"setClob(int i, File file)";
	public static final BaseTypeMethod setClob_int_File_String = new Clob_int_File_StringTypeMethod();//"setClob(int i, File file, String string)";
//	public static final String setBinaryStream_int_InputStream_int = "setBinaryStream(int, InputStream, int)";
//	public static final String setBlob_int_InputStream_int = "setBinaryStream(int, InputStream, int)";
//	public static final String setBlob_int_bytearray = "setBlob(int, byte[])";
//	public static final String setBlob_int_blob = "setBlob(int,Blob])";
//	public static final String setBlob_int_bytearray_String = "setBlob(int, byte[], String)";
//	public static final String setBlob_int_File = "setBlob(int, File)";
//	public static final String setBlob_int_File_String = "setBlob(int, File, String)";
//	public static final String setBoolean_int_boolean = "setBoolean(int, boolean)";
//	public static final String setByte_int_byte = "setByte(int, byte)";
//	public static final String setBytes_int_bytearray = "setBytes(int, byte[])";
//	public static final String setCharacterStream_int_Reader_int = "setCharacterStream(int, Reader, int)";
//	public static final String setClob_int_String = "setClob(int, String)";
//	public static final String setClob_int_Clob = "setClob(int, Clob)";
//	public static final String setClob_int_String_String = "setClob(int, String, String)";
//	public static final String setDate_int_sqlDate = "setDate(int, java.sql.Date)";
//	public static final String setDate_int_Date_Calendar = "setDate(int, Date, Calendar)";
//	public static final String setDate_int_utilDate = "setDate(int, java.util.Date)";
//	public static final String setDouble_int_double = "setDouble(int, double)";
//	public static final String setFloat_int_float = "setFloat(int, float)";
//	public static final String setInt_int_int = "setInt(int, int)";
//	public static final String setLong_int_long = "setLong(int, long)";
//	public static final String setNull_int_int = "setNull(int, int)";
//	public static final String setNull_int_int_String = "setNull(int, int, String)";
//	public static final String setObject_int_Object = "setObject(int, Object)";
//	public static final String setObject_int_Object_int = "setObject(int, Object, int)";
//	public static final String setObject_int_Object_int_int = "setObject(int, Object, int, int)";
//
//	public static final String setRef_int_Ref = "setRef(int, Ref)";
//	public static final String setShort_int_short = "setShort(int, short)";
//	public static final String setString_int_String = "setString(int, String)";
//	public static final String setTime_int_Time = "setTime(int, Time)";
//	public static final String setTime_int_Time_Calendar = "setTime(int, Time, Calendar)";
//	//	public static final String setTimestamp_int_Timestamp = "setTimestamp(int, Timestamp)";
//	public static final BaseTypeMethod setTimestamp_int_Timestamp = new TimestampTypeMethod();
//	public static final String setTimestamp_int_Timestamp_Calendar = "setTimestamp(int, Timestamp, Calendar)";
//	public static final String setUnicodeStream_int_InputStream_int = "setUnicodeStream(int, InputStream, int)";
//	public static final String setClob_int_File = "setClob(int i, File file)";
//	public static final String setClob_int_File_String = "setClob(int i, File file, String string)";
	private String type;
	BaseTypeMethod method;
	private String charset;
	public BaseTypeMethod getMethod()
    {
        return method;
    }

    public void setMethod(BaseTypeMethod method)
    {
        this.method = method;
    }

    public int getIndex()
    {
        return index;
    }

    public void setIndex(int index)
    {
        this.index = index;
    }

    public Object getData()
    {
        return data;
    }

    public void setData(Object data)
    {
        this.data = data;
    }

    public Param clone()
    {
        Param newparam = new Param();
        newparam.data = this.data;
        newparam.name = this.name;
        newparam.dataformat = this.dataformat;
        newparam.index = this.index;
        newparam.method = this.method;
        newparam.type = this.type;
        newparam.charset = this.charset;
        
        return newparam;
    }
    public Param clone(Variable variable)
    {
    	if(variable == null)
    	{
    		return this.clone();
    	}
        Param newparam = new Param();
        newparam.data = VariableHandler.evaluateVariableValue(variable, data);
        newparam.name = this.name;
        newparam.dataformat = this.dataformat;
        newparam.index = this.index;
		PersistentSQLVariable sqlVariable = (PersistentSQLVariable)variable;
        if(sqlVariable.getMethod() != null)
			newparam.method = sqlVariable.getMethod();
        else
        	newparam.method = this.method;
        newparam.type = this.type;
        newparam.charset = this.charset;
        newparam.variable = variable; 
        return newparam;
    }
    int index;
	Object data;
	String dataformat;
	String name;

	public Variable getVariable() {
		return variable;
	}

	public void setVariable(Variable variable) {
		this.variable = variable;
	}

	/**
	 * 引用变量，适用于变量引用，list，set，map，数组以及多级
	 * 变量引用
	 */
	protected Variable variable;
	public String toString()
	{
		if(variable == null)
		{
			StringBuilder ret = new StringBuilder();
			ret.append("[name=").append(name)
			.append(",index=").append(index)
			.append(",value=");
			SimpleStringUtil.tostring(ret,data);
			ret.append(",dataformat=").append(dataformat)
			.append(",charset=").append(this.charset== null?"":charset)
			.append(",method=").append(method).append("]");
			return ret.toString();
		}
		else
		{
			StringBuilder ret = new StringBuilder();
			ret.append("[name=").append(name)
			.append(",index=").append(index)
			.append(",value=");
			SimpleStringUtil.tostring(ret,data);
			ret.append(",variable=").append(variable.toString())
			.append(",dataformat=").append(dataformat)
			.append(",charset=").append(this.charset== null?"":charset)
			.append(",method=").append(method).append("]");
			return ret.toString();
		}
	}
	
	
//
//	public static String getParamType(int sqltype,boolean isnull)
//	{
//	    if(isnull)
//	        return Param.setNull_int_int;
//	    switch(sqltype)
//	    {
//
////	        case java.sql.Types.
////    	    /**setAsciiStream(int i, InputStream x, int length)*/
////    	    static final String SET_AsciiStream_INT_InputStream_INT = "setAsciiStream(int, InputStream, int)";
//	        case java.sql.Types.BIGINT:
//	            return SET_BigDecimal_INT_BigDecimal ;
//
//	        case java.sql.Types.BOOLEAN:
//                return setBoolean_int_boolean ;
//	        case java.sql.Types.BIT:
//	            return setByte_int_byte ;
//
//
//
//
//
//	        case java.sql.Types.DATE:
//	            return setDate_int_sqlDate ;
//
//
//	        case java.sql.Types.DOUBLE:
//	            return setDouble_int_double ;
//	        case java.sql.Types.FLOAT:
//	            return setFloat_int_float ;
//	        case java.sql.Types.INTEGER:
//	            return setInt_int_int ;
//
//
//	        case java.sql.Types.NUMERIC:
//	            return setLong_int_long;
//
//
//
//
//
//
//
//	        case java.sql.Types.SMALLINT:
//	            return setShort_int_short ;
//	        case java.sql.Types.VARCHAR:
//	            return setString_int_String ;
//	        case java.sql.Types.TIME:
//	            return setTime_int_Time ;
//
//	        case java.sql.Types.TIMESTAMP:
//	            return setTimestamp_int_Timestamp ;
//	        default:
//                return setObject_int_Object ;
//
//
//
//
//	    }
//	}

	public String getDataformat() {
		return dataformat;
	}

	public void setDataformat(String dataformat) {
		this.dataformat = dataformat;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getCharset() {
		return charset;
	}

	public void setCharset(String charset) {
		this.charset = charset;
	}
	
	
	
	
}
