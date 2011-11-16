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

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.frameworkset.util.BigFile;
import org.frameworkset.util.ClassUtil;
import org.frameworkset.util.annotations.ValueConstants;

import com.frameworkset.common.poolman.handle.FieldRowHandler;
import com.frameworkset.common.poolman.handle.RowHandler;
import com.frameworkset.common.poolman.handle.ValueExchange;
import com.frameworkset.common.poolman.handle.XMLMark;
import com.frameworkset.common.poolman.handle.XMLRowHandler;
import com.frameworkset.common.poolman.sql.PoolManResultSetMetaData.WrapInteger;
import com.frameworkset.common.poolman.util.SQLUtil;
import com.frameworkset.orm.annotation.Column;
import com.frameworkset.orm.annotation.PrimaryKey;
import com.frameworkset.orm.engine.model.SchemaType;

/**
 * 
 * 
 * <p>
 * Title: CallableDBUtil.java
 * </p>
 * 
 * <p>
 * Description:
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2007
 * </p>
 * 
 * 
 * @Date Oct 22, 2008 2:10:42 PM
 * @author biaoping.yin
 * @version 1.0
 */
public class ResultMap {
	public static int type_list = 0;
	public static int type_objcet = 1;
	public static int type_objectarray = 2;
	public static int type_xml = 3;
	public static int type_maparray = 4;
	public static int type_null = 5;
	private static Logger log = Logger.getLogger(ResultMap.class);
	private Object functionResult;

	private Record origineprocresult;

	private Object commonresult;
	
	private int size;
	
	public void setCommonresult(Object commonresult)
	{
		this.commonresult = commonresult;
	}
	
	

	/**
	 * 
	 * @param cstmt
	 * @param rs
	 * @param outparams
	 * @param valueobjectType
	 * @throws SQLException
	 */
	public void handle(CallableStatement cstmt, ResultSet rs,
			Class valueobjectType, CallableParams outparams,
			StatementInfo stmtInfo, RowHandler rowHander) throws SQLException {
		if (rs != null) {
			rs.next();
			functionResult = rs.getObject(1);
		}

		if (valueobjectType == Map.class || valueobjectType == List.class)// 映射为map对象
		{

			Record record = buildMap(cstmt, outparams, stmtInfo);
			if (rowHander != null) {
				try
                {
                    rowHander.handleRow(record, record);
                }
				catch (SQLException e)
	            {
	               throw e; 
	            }
	            catch (Exception e)
	            {
	               throw new NestedSQLException(e); 
	            }
				this.commonresult = record;
				return;
			}
			if (valueobjectType == List.class) {
				List datas = new ArrayList();
				datas.add(record);
				this.commonresult = datas;
			} else {
				this.commonresult = record;
				this.origineprocresult = record;
			}
		}

		else if (valueobjectType == XMLMark.class)// 映射为xml格式化串
		{
//		    if(rowHander == null)
//		    {
//		        rowHander = new XMLRowHandler()
//		    }
		    this.commonresult = buildXMLString(cstmt, outparams, stmtInfo,
					rowHander);
		} else // 映射为普通的值对象,为值对象时，要求out参数全部通过outparameter名称注册，不能通过位置索引来指定
		{
			
			this.commonresult = buildValueObject(cstmt, valueobjectType,
					outparams, stmtInfo, rowHander);
		}
	}

	public static <T> T buildValueObject(ResultSet rs,
			Class<T> valueObjectType, 
			StatementInfo stmtInfo, RowHandler rowHander)
			throws SQLException {
		
		if (rs == null ||valueObjectType == null || stmtInfo == null)
			return null;
		T valueObject = null;
		
		
		if (rowHander != null) {
			boolean isfieldRowHandler = isFieldRowHandler(rowHander);
			try {
				if(!isfieldRowHandler)
					valueObject = valueObjectType.newInstance();
			} catch (InstantiationException e1) {
				throw new NestedSQLException(e1);
			} catch (IllegalAccessException e1) {
				throw new NestedSQLException(e1);
			}
			Record data = buildMap(rs,  stmtInfo);
			try
            {
				if(!isfieldRowHandler)
					rowHander.handleRow(valueObject, data);
				else
					valueObject = ((FieldRowHandler<T>)rowHander).handleField_(data);
            }
			catch (SQLException e)
            {
               throw e; 
            }
            catch (Exception e)
            {
               throw new NestedSQLException(e); 
            }
			return valueObject;

		}
		
		if( !Map.class.isAssignableFrom(valueObjectType))
		{
			try {
				valueObject = valueObjectType.newInstance();
			} catch (InstantiationException e1) {
				throw new NestedSQLException(e1);
			} catch (IllegalAccessException e1) {
				throw new NestedSQLException(e1);
			}
			BeanInfo beanInfo;
			try {
				
				beanInfo = Introspector.getBeanInfo(valueObjectType);
			} catch (IntrospectionException e1) {
				throw new NestedSQLException(e1);
			}
			PropertyDescriptor[] attributes = beanInfo.getPropertyDescriptors();
			for (int n = 0; n < attributes.length; n++) {
				PropertyDescriptor attribute = attributes[n];
				String attrName = attribute.getName();
				if(attrName.equals("class"))
					continue;
				String annotationName = null;
				if(BigFile.class.isAssignableFrom(attribute.getPropertyType()) )//不支持大字段转换为BigFile接口
					continue;
				try {
					
					Field field = ClassUtil.getDeclaredField(valueObjectType,attrName);
					if(field != null)
					{
						
					
						if(field.isAnnotationPresent(PrimaryKey.class))
						{
							PrimaryKey apk = field.getAnnotation(PrimaryKey.class);
							annotationName = apk.name();
							if(annotationName == null 
									|| annotationName.equals(ValueConstants.DEFAULT_NONE) || annotationName.equals(""))
							{
								
							}
							else
							{
								attrName = annotationName;
							}
							
						}
						else if(field.isAnnotationPresent(Column.class))
						{
							Column apk = field.getAnnotation(Column.class);
							annotationName = apk.name();
							if(annotationName == null 
									|| annotationName.equals(ValueConstants.DEFAULT_NONE) || annotationName.equals(""))
							{
								
							}
							else
							{
								attrName = annotationName;
							}
						}
					}
				} catch (Exception e1) {
					log.info(attribute.getName() + " is not a field of bean[" +valueObjectType.getClass().getCanonicalName() + "].");
				} 
				for (int i = 0; i < stmtInfo.getMeta().getColumnCounts(); i++) {
					String columnName = stmtInfo.getMeta().getColumnLabelUpper(i+1);
	
					if (!attrName.equalsIgnoreCase(columnName))
						continue;
	
					Class type = attribute.getPropertyType();
					Object propsVal = null;
					
					try {
	//					propsVal = ValueExchange.getValueFromResultSet(rs, columnName, 
	//														stmtInfo.getMeta().getColumnType(i + 1), 
	//														type, 
	//														stmtInfo.getDbname());
						propsVal = ValueExchange.getValueFromResultSet(rs, i + 1, 
								stmtInfo.getMeta().getColumnType(i + 1), 
								type, 
								stmtInfo.getDbname());
	
					} catch (Exception e) {
						StringBuffer err = new StringBuffer(
								"Build ValueObject for ResultSet[").append(
										stmtInfo.getSql()).append("] Get Column[")
								.append(columnName).append("] from  ResultSet to ").append(valueObject).append(".")
								.append(attrName).append("[")
								.append(type.getName()).append("] failed:").append(
										e.getMessage());
						log.error(err.toString(), e);
						break;
					}
	
					try {
						attribute.getWriteMethod().invoke(valueObject,
								new Object[] { propsVal });
						break;
					} catch (Exception e) {
						StringBuffer err = new StringBuffer(
						"Build ValueObject for ResultSet[").append(
								stmtInfo.getSql()).append("] Get Column[")
						.append(columnName).append("] from  ResultSet to ").append(valueObject).append(".")
						.append(attrName).append("[")
						.append(type.getName()).append("] failed:").append(
								e.getMessage());
//						System.out.println(err);
						log.error(err.toString(), e);
						break;
					}
					
				}

			}
		}
		else
		{
			valueObject = (T)buildMap(valueObjectType,rs,stmtInfo);
		}
		return valueObject;
	}
	
	
	public static void buildRecord(ResultSet rs,
           
            StatementInfo stmtInfo, RowHandler rowHander)
            throws SQLException {
        
        if (rs == null || stmtInfo == null)
            return ;
       
       
        if (rowHander != null) {

            Record data = buildMap(rs,  stmtInfo);
            try
            {
                rowHander.handleRow(null, data);
            }
            catch (SQLException e)
            {
               throw e; 
            }
            catch (Exception e)
            {
               throw new NestedSQLException(e); 
            }
            

        }

    }

	private static boolean isFieldRowHandler(RowHandler rowHander)
	{
		return rowHander instanceof FieldRowHandler;
	}
	public static Object buildValueObject(CallableStatement cstmt,
			Class valueObjectType, CallableParams outparams,
			StatementInfo stmtInfo, RowHandler rowHander)
			throws SQLException {
		// Record data = buildMap( cstmt, outparams, stmtInfo);

		// if(data == null)
		// return null;
		// this.origineprocresult = new Record(data);
		if (outparams == null || outparams.outParams == null
				|| outparams.outParams.size() == 0)
			return null;
		Object valueObject = null;
		
		if (rowHander != null) {
			boolean isfieldRowHandler = isFieldRowHandler(rowHander);
			try {
				if(!isfieldRowHandler)
					valueObject = valueObjectType.newInstance();
			} catch (InstantiationException e1) {
				throw new NestedSQLException(e1);
			} catch (IllegalAccessException e1) {
				throw new NestedSQLException(e1);
			}
			Record data = buildMap(cstmt, outparams, stmtInfo);
			try
            {
				if(!isfieldRowHandler)
				{
					rowHander.handleRow(valueObject, data);
				}
				else
				{
					valueObject = ((FieldRowHandler)rowHander).handleField_(data);
				}
            }
			catch (SQLException e)
            {
               throw e; 
            }
            catch (Exception e)
            {
               throw new NestedSQLException(e); 
            }
			return valueObject;

		}
		if( !Map.class.isAssignableFrom(valueObjectType))
		{
			try {
				valueObject = valueObjectType.newInstance();
			} catch (InstantiationException e1) {
				throw new NestedSQLException(e1);
			} catch (IllegalAccessException e1) {
				throw new NestedSQLException(e1);
			}
			BeanInfo beanInfo;
			try {
				beanInfo = Introspector.getBeanInfo(valueObjectType);
			} catch (IntrospectionException e1) {
				throw new NestedSQLException(e1);
			}
			PropertyDescriptor[] attributes = beanInfo.getPropertyDescriptors();
			for (int n = 0; n < attributes.length; n++) {
				PropertyDescriptor attribute = attributes[n];
				String attrName = attribute.getName();
				if(attrName.equals("class"))
					continue;
				if(BigFile.class.isAssignableFrom(attribute.getPropertyType()) )//不支持大字段转换为BigFile接口
					continue;
				String annotationName = null;
				try {
					Field field = ClassUtil.getDeclaredField(valueObjectType,attrName);
					if(field != null)
					{
						
					
						if(field.isAnnotationPresent(PrimaryKey.class))
						{
							PrimaryKey apk = field.getAnnotation(PrimaryKey.class);
							annotationName = apk.name();
							if(annotationName == null 
									|| annotationName.equals(ValueConstants.DEFAULT_NONE) || annotationName.equals(""))
							{
								
							}
							else
							{
								attrName = annotationName;
							}
							
						}
						else if(field.isAnnotationPresent(Column.class))
						{
							Column apk = field.getAnnotation(Column.class);
							annotationName = apk.name();
							if(annotationName == null 
									|| annotationName.equals(ValueConstants.DEFAULT_NONE) || annotationName.equals(""))
							{
								
							}
							else
							{
								attrName = annotationName;
							}
						}
					}
				} catch (Exception e1) {
					log.info(attribute.getName() + " is not a field of bean[" +valueObjectType.getClass().getCanonicalName() + "].");
				} 
				for (int i = 0; i < outparams.outParams.size(); i++) {
					CallableParam param = (CallableParam) outparams.outParams
							.get(i);
					if (param.parameterName == null) {
						String msg = new StringBuffer(
								"Build ValueObject for callablestatement[").append(
								outparams.prepareselect_sql).append(
								"] need named binding variable,ignore handle ")
								.append(param).append(".").toString();
						log.warn(msg);
						System.out.println(msg);
						outparams.outParams.remove(i);
						continue;
					}
					if (!attrName.equalsIgnoreCase(param.parameterName))
						continue;
	
					Class type = attribute.getPropertyType();
					Object propsVal = null;
					// Object temp_ = getValueFromCallableStatement(cstmt,
					// param.parameterName,
					// param.sqlType,
					// stmtInfo.getDbname());
					try {
						propsVal = ValueExchange.getValueFromCallableStatement(
								cstmt, param.parameterName, param.sqlType, type,
								stmtInfo.getDbname());
					} catch (Exception e) {
						StringBuffer err = new StringBuffer(
								"Build ValueObject for callablestatement[").append(
								outparams.prepareselect_sql).append("] Get Param[")
								.append(param).append("] from  ").append(cstmt)
								.append(" to ").append(valueObject).append(".")
								.append(attrName).append("[")
								.append(type.getName()).append("] failed:").append(
										e.getMessage());
						log.error(err.toString(), e);
						// 处理一个就外部参数剔除一个
						outparams.outParams.remove(i);
						break;
					}
					// if(propsVal == null)
					// {
					// outparams.outParams.remove(i);
					// }
	
					try {
						attribute.getWriteMethod().invoke(valueObject,
								new Object[] { propsVal });
						// 处理一个就外部参数剔除一个
						outparams.outParams.remove(i);
						break;
					} catch (Exception e) {
						StringBuffer err = new StringBuffer("set Param[").append(
								param).append("] into ").append(valueObject)
								.append(".").append(attrName).append("[").append(
										type.getName()).append("] failed:").append(
										e.getMessage());
						System.out.println(err);
						log.error(err.toString(), e);
						// 处理一个就外部参数剔除一个
						outparams.outParams.remove(i);
						break;
					}
					
				}
				
	
			}
			if (outparams.outParams.size() > 0) {
				StringBuffer msg = new StringBuffer();
				msg.append("Following outparams not mapping attributes in ")
						.append(valueObjectType.getName()).append(":\r\n");
				for (int i = 0; i < outparams.outParams.size(); i++) {
					msg.append("    ").append(outparams.outParams.remove(i));
				}
			}
		}
		else
		{
			valueObject = buildMap(valueObjectType,cstmt, outparams, stmtInfo);
		}
		return valueObject;
	}
	
	public static Map buildMap(Class valueObjectType,CallableStatement cstmt,
			CallableParams outparams, StatementInfo stmtInfo)
			throws SQLException {
		Map data = null;
		if (outparams.outParams != null && outparams.outParams.size() > 0) {
			try {
				data = ResultMap.findMapObject(valueObjectType, outparams.outParams.size());
			} catch (InstantiationException e) {
				throw new NestedSQLException(e);
			} catch (IllegalAccessException e) {
				throw new NestedSQLException(e);
			}
//			data = new Record(outparams.outParams.size());
			for (int i = 0; outparams.outParams != null
					&& i < outparams.outParams.size(); i++) {
				Object param = outparams.outParams.get(i);
				if (param instanceof CallableParam) {
					CallableParam _param = (CallableParam) param;
					if (_param.parameterName != null) {
						Object object = ValueExchange
								.getValueFromCallableStatement(cstmt,
										_param.parameterName, _param.sqlType,
										stmtInfo.getDbname());
						data.put(_param.parameterName.toLowerCase(), object);
					} else {
						Object object = ValueExchange
								.getValueFromCallableStatement(cstmt,
										_param.index, _param.sqlType, stmtInfo
												.getDbname());
						data.put(new Integer(_param.index), object);
					}
				} else {
					throw new SQLException(new StringBuffer("Param[").append(
							param).append("] is not an out parameter.")
							.toString());
				}
			}
		}
		return data;

	}
	public static Record buildMap(CallableStatement cstmt,
			CallableParams outparams, StatementInfo stmtInfo)
			throws SQLException {
		Record data = null;
		if (outparams.outParams != null && outparams.outParams.size() > 0) {

			data = new Record(outparams.outParams.size());
			for (int i = 0; outparams.outParams != null
					&& i < outparams.outParams.size(); i++) {
				Object param = outparams.outParams.get(i);
				if (param instanceof CallableParam) {
					CallableParam _param = (CallableParam) param;
					if (_param.parameterName != null) {
						Object object = ValueExchange
								.getValueFromCallableStatement(cstmt,
										_param.parameterName, _param.sqlType,
										stmtInfo.getDbname());
						data.put(_param.parameterName.toLowerCase(), object);
					} else {
						Object object = ValueExchange
								.getValueFromCallableStatement(cstmt,
										_param.index, _param.sqlType, stmtInfo
												.getDbname());
						data.put(new Integer(_param.index), object);
					}
				} else {
					throw new SQLException(new StringBuffer("Param[").append(
							param).append("] is not an out parameter.")
							.toString());
				}
			}
		}
		return data;

	}
	
	
	public static Record buildMap(ResultSet rs,StatementInfo stmtInfo)
			throws SQLException {
		Record record = null;
		if (rs != null && stmtInfo != null) {
		        int cols = stmtInfo.getMeta().getColumnCounts();
			record = new Record(cols,stmtInfo.getMeta().get_columnLabel_upper(),stmtInfo.getMeta().getSamecols());
			record.setRowid(rs.getRow());	
			for (int i = 1; i <= cols; i++) {
				Object value = ValueExchange.getValueFromRS(rs, i, stmtInfo.getMeta()
						.getColumnType(i), stmtInfo.getDbname());

				
				// 将属性名称全部转换为大写，统一不同数据库之间的差异
				if (value != null)
				{
				    
				    WrapInteger wi = stmtInfo.getMeta().getSameColumns(i);
				    
				    if(wi == null || i == 1)
				    {
				        record.put(stmtInfo.getMeta()
    				               .getColumnLabelUpper(i),
    						value);
				    }
				    
				    else
				    {
				        record.put(wi.getColumnName(i - 1) ,
	                                                value);
//				        wi.getColumnName();
				    }
//				    /**
//				     * 直接以列索引存放数据，列索引从0
//				     */
//				    record.put(new Integer(i-1),
//                                                value);
				}
				
			}

		}
		return record;

	}
	
	private static Map findMapObject(Class valueObjectType,int initialCapacity) throws InstantiationException, IllegalAccessException
	{
		try {
			Constructor constructor = valueObjectType.getConstructor(int.class);
			return (Map)constructor.newInstance(initialCapacity);
			
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return (Map)valueObjectType.newInstance();
	}
	public static <T> T buildMap(Class<T> valueObjectType,ResultSet rs,StatementInfo stmtInfo)
	throws SQLException {
		 Map valueObject = null;
		if (rs != null && stmtInfo != null) {
		        int cols = stmtInfo.getMeta().getColumnCounts();
		       
				try {
					valueObject = findMapObject(valueObjectType,cols);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					throw new NestedSQLException(e);
				}
//			record = new Record(cols,stmtInfo.getMeta().get_columnLabel_upper(),stmtInfo.getMeta().getSamecols());
//			record.setRowid(rs.getRow());	
			for (int i = 1; i <= cols; i++) {
				Object value = ValueExchange.getValueFromRS(rs, i, stmtInfo.getMeta()
						.getColumnType(i), stmtInfo.getDbname());
		
				
				// 将属性名称全部转换为大写，统一不同数据库之间的差异
				if (value != null)
				{
				    
				    WrapInteger wi = stmtInfo.getMeta().getSameColumns(i);
				    
				    if(wi == null || i == 1)
				    {
				    	valueObject.put(stmtInfo.getMeta()
					               .getColumnLabelUpper(i),
							value);
				    }
				    
				    else
				    {
				    	valueObject.put(wi.getColumnName(i - 1) ,
		                                            value);
		//		        wi.getColumnName();
				    }
		//		    /**
		//		     * 直接以列索引存放数据，列索引从0
		//		     */
		//		    record.put(new Integer(i-1),
		//                                        value);
				}
				
			}
		
		}
//		return record;
		return (T)valueObject;
		
	}
	
	
	
	
	public static StringBuffer buildSingleRecordXMLString(ResultSet rs,
			StatementInfo stmtInfo,
			RowHandler rowHander) throws SQLException {			
		StringBuffer record = new StringBuffer();
//		if (rowHander != null)
//		{		        
			Record data = buildMap(rs, stmtInfo);
			try
            {
                rowHander.handleRow(record, data);
            }
			catch (SQLException e)
            {
               throw e; 
            }
            catch (Exception e)
            {
               throw new NestedSQLException(e); 
            }
			return record;
//		}
//		record.append("\r\n\t<record>");
//		for (int i = 0; i < stmtInfo.getMeta().getColumnCounts(); i++) {
//			String columnName = stmtInfo.getMeta().getColumnLabelUpper(i + 1);
//			
//				
//			int sqltype = stmtInfo.getMeta().getColumnType(i + 1);
//			
//			String object = (String) ValueExchange
//					.getValueFromResultSet(rs,
//							columnName, sqltype,
//							String.class, stmtInfo.getDbname());
//			SchemaType schemaType = SQLUtil.getSchemaType(stmtInfo
//					.getDbname(), sqltype);
//			record.append("\r\n\t\t<column name=\"").append(
//					columnName).append(
//					"\" type=\"").append(schemaType.getName())
//					.append("\" javatype=\"").append(
//							schemaType.getJavaType()).append("\"")
//					.append(">\r\n")
//					.append("\t\t\t<![CDATA[")
//					// .append(ResultMap.getStringFromObject(object))//需要转换成String类型
//					.append(object)// 需要转换成String类型
//					.append("]]>\r\n").append(
//							"\t\t</column>");
//					
//			
//		}
//		record.append("\r\n\t</record>");		
//		return record;

	}
	public String buildXMLString(CallableStatement cstmt,
			CallableParams outparams, StatementInfo stmtInfo,
			RowHandler rowHander) throws SQLException {
		// Map data = buildMap(cstmt, outparams, stmtInfo);
		// this.origineprocresult = new Record(data);
		if (outparams.outParams != null && outparams.outParams.size() > 0) {

			StringBuffer records = new StringBuffer(2000);

			boolean isxmlhandler = rowHander != null && rowHander instanceof XMLRowHandler; 
	                XMLRowHandler xhdl = null;
	                if(!isxmlhandler) //行处理器不是从XMLRowHandler处理器继承时进入这个分支
	                {
        			records.append("<?xml version=\"1.0\" encoding=\"gb2312\"?>\r\n");
        			records.append("<records>\r\n");
	                }
	                else
	                {
	                    xhdl = (XMLRowHandler)rowHander;
	                    records.append("<?xml version=\"")
	                           .append(xhdl.getVersion())
	                           .append("\" encoding=\"")
	                           .append(xhdl.getEncoding())
	                           .append("\"?>\r\n");
	                    records.append("<")
	                           .append(xhdl.getRootName())
	                           .append(">\r\n");
	                }
			StringBuffer record = new StringBuffer(100);
			if (rowHander != null)
			{
				Record origineprocresult = buildMap(cstmt, outparams, stmtInfo);
				try
                {
                    rowHander.handleRow(record, origineprocresult);
                }
				catch (SQLException e)
	            {
	               throw e; 
	            }
	            catch (Exception e)
	            {
	               throw new NestedSQLException(e); 
	            }
			}
			else
			{
				record.append("    <record>\r\n");
				for (int i = 0; i < outparams.outParams.size(); i++) {
					Object param = outparams.outParams.get(i);
					if (param instanceof CallableParam) {
						CallableParam _param = (CallableParam) param;
						if (_param.parameterName != null) {
							// Object object = getValueFromCallableStatement(cstmt,
							// _param.parameterName,
							// _param.sqlType,stmtInfo.getDbname());
							String object = (String) ValueExchange
									.getValueFromCallableStatement(cstmt,
											_param.parameterName, _param.sqlType,
											String.class, stmtInfo.getDbname());
							SchemaType schemaType = SQLUtil.getSchemaType(stmtInfo
									.getDbname(), _param.sqlType);
							record.append("        <column name=\"").append(
									_param.parameterName.toLowerCase()).append(
									"\" type=\"").append(schemaType.getName())
									.append("\" javatype=\"").append(
											schemaType.getJavaType()).append("\"")
									.append(">\r\n")
									.append("            <![CDATA[")
									// .append(ResultMap.getStringFromObject(object))//需要转换成String类型
									.append(object)// 需要转换成String类型
									.append("]]>\r\n").append(
											"        </column>\r\n");
							// data.put(_param.parameterName, object);
						} else {
							// Object object = getValueFromCallableStatement(cstmt,
							// _param.index, _param.sqlType,stmtInfo.getDbname());
							String object = (String) ValueExchange
									.getValueFromCallableStatement(cstmt,
											_param.index, _param.sqlType,
											String.class, stmtInfo.getDbname());
							SchemaType schemaType = SQLUtil.getSchemaType(stmtInfo
									.getDbname(), _param.sqlType);
							record.append("        <column index=\"").append(
									_param.index).append("\" type=\"").append(
									schemaType.getName()).append("\" javatype=\"")
									.append(schemaType.getJavaType()).append("\"")
									.append(">\r\n")
									.append("            <![CDATA[")
									// .append(ResultMap.getStringFromObject(object))//需要转换成String类型
									.append(object).append("]]>\r\n").append(
											"        </column>\r\n");
							// data.put(new Integer(_param.index), object);
						}
					} else {
						throw new SQLException(new StringBuffer("Param[").append(
								param).append("] is not an out parameter.")
								.toString());
					}
				}
				record.append("    </record>");
			}
			if(!isxmlhandler)
	                {
			    records.append(record).append("\r\n").append("</records>");
	                }
	                else
	                {
	                    records.append(record).append("\r\n").append("</")
                                   .append(xhdl.getRootName())
                                   .append(">");
	                    
	                }
			
			return records.toString();
		}
		return null;

	}

	public Object getFunctionResult() {
		return functionResult;
	}

	public Record getOrigineprocresult() {
		return origineprocresult;
	}

	public Object getCommonresult() {
		return commonresult;
	}



	public int getSize() {
		return size;
	}



	public void setSize(int size) {
		this.size = size;
	}
	
	
}
