package com.frameworkset.common.poolman.handle;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.frameworkset.common.poolman.Record;
import com.frameworkset.common.poolman.util.SQLUtil;
import com.frameworkset.orm.engine.model.SchemaType;

/**
 * 
 * 
 * <p>Title: XMLRowHandler.java</p>
 *
 * <p>Description: xml行处理器</p>
 *
 * <p>Copyright: Copyright (c) 2007</p>
 *

 * @Date Oct 24, 2008 2:29:31 PM
 * @author biaoping.yin
 * @version 1.0
 */
public class XMLRowHandler extends BaseRowHandler<StringBuffer> {
       
        /**
         * rowValue类型为StringBuffer
         */
	public void handleRow(StringBuffer rowValue,Record origine) 
	{
	    if(meta == null)
            {
                throw new RowHandlerException("源数据对象[meta]未初始化,无法进行行处理.");
            }
	    StringBuffer record = (StringBuffer)rowValue;
	    record.append("    <record>\r\n");
	    
	    try
	    {
	        
    	        for (int i = 0; i < meta.getColumnCounts(); i++) {
                    String columnName = meta.getColumnLabelUpper(i + 1);  
                    int sqltype = meta.getColumnType(i + 1);
                    
                    String object = origine.getString(columnName);
                    SchemaType schemaType = SQLUtil.getSchemaType(dbname, sqltype);
                    record.append(buildNode("column",
                                      columnName,
                                      schemaType.getName(),
                                      schemaType.getJavaType(),
                                      object,
                                      "\r\n"));
//                    record.append("\r\n\t\t<column name=\"").append(
//                                    columnName).append(
//                                    "\" type=\"").append(schemaType.getName())
//                                    .append("\" javatype=\"").append(
//                                                    schemaType.getJavaType()).append("\"")
//                                    .append(">\r\n")
//                                    .append("\t\t\t<![CDATA[")
//                                    // .append(ResultMap.getStringFromObject(object))//需要转换成String类型
//                                    .append(object)// 需要转换成String类型
//                                    .append("]]>\r\n").append(
//                                                    "\t\t</column>");                                    
                    
                }
	    }
	    catch(Exception e)
	    {
	        throw new RowHandlerException(e);
	    }
            record.append("    </record>\r\n");
	}
	
	
	/**
	 * 返回xml串的根节点名称
	 * 缺省为records，用户可以扩展这个方法
	 * @return
	 */
	public String getRootName()
	{
	    return "records";
	}
	
	/**
         * 返回xml的编码字符集
         * 缺省为UTF-8，用户可以扩展这个方法
         * @return
         */
        public String getEncoding()
        {
            return "UTF-8";
        }
        
        
        /**
         * 返回xml语法的版本号
         * 缺省为1.0，用户可以扩展这个方法
         * @return
         */
        public String getVersion()
        {
            return "1.0";
        }
        
        public static String buildNode(String columnNodeName,
                                      String columnName,
                                      String columnType,
                                      String columnJavaType,
                                      String value,
                                      String split)
        {
            Map attributes = new HashMap();
            attributes.put("name", columnName);
            attributes.put("type", columnType);
            attributes.put("javatype", columnJavaType);
            
            return buildNode(columnNodeName,
                              attributes,
                              value,
                              split);
//            StringBuffer record = new StringBuffer();
//            record.append("\t\t<").append(columnNodeName).append(" name=\"")
//                                                             .append(columnName)
//                                                             .append("\" type=\"").append(columnType)
//                                                            .append("\" javatype=\"")
//                                                            .append(columnJavaType)
//                                                            .append("\"")
//                                                            .append(">\r\n")
//                                                            .append("\t\t\t<![CDATA[")                                                            
//                                                            .append(value)// 需要转换成String类型
//                                                            .append("]]>\r\n")
//                                                            .append("\t\t</")
//                                                            .append(columnNodeName)
//                                                            .append(">")
//                                                            .append(split);
//            return record.toString();
        }
        
        public static String buildNode(String columnNodeName,
                                Map attributes,
                                String value,
                                String split)
      {
          StringBuffer record = new StringBuffer();
          
          record.append("\t<").append(columnNodeName);
          if(attributes != null && attributes.size() > 0)
          {
              Set entrys = attributes.entrySet();
              Iterator it = entrys.iterator();
              while(it.hasNext())
              {
                  Map.Entry e = (Map.Entry)it.next();
                  String aname = e.getKey().toString();                 
                  record.append(" ").append(aname).append("=\"")
                      .append(e.getValue())
                      .append("\"");
//                      .append("\" type=\"").append(columnType)
                 
              }
          }
          
          record.append(">");
          if(value != null)
          {
              record.append("\r\n\t    <![CDATA[")                                                            
                      .append(value)// 需要转换成String类型
                      .append("]]>\r\n")
                      .append("\t</")
                      .append(columnNodeName)
                      .append(">")
                      .append(split);
          }
          else
          {
              record.append("</")
              .append(columnNodeName)
              .append(">")
              .append(split);
          }
          return record.toString();
      }


		
}
