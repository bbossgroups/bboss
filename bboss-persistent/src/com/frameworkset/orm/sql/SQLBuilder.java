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
package com.frameworkset.orm.sql;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import com.frameworkset.orm.ORMappingException;
import com.frameworkset.orm.ORMappingManager;
import com.frameworkset.orm.adapter.DBAdapter;
import com.frameworkset.orm.engine.model.Column;
import com.frameworkset.orm.engine.model.Database;
import com.frameworkset.orm.engine.model.SchemaType;
import com.frameworkset.orm.engine.model.Table;
import com.frameworkset.orm.engine.model.TypeMap;
import com.frameworkset.util.SimpleStringUtil;
import com.frameworkset.util.ValueObjectUtil;

public class SQLBuilder {
	private static Logger log = Logger.getLogger(SQLBuilder.class);
    public static final int SQLBUILD_TYPE_INSERT = 0;
    public static final int SQLBUILD_TYPE_DELETE = 1;
    public static final int SQLBUILD_TYPE_UPDATE = 2;
    public static final int SQLBUILD_TYPE_SELECT = 3;

    private final static ORMappingManager ormManager = ORMappingManager.getInstance();


    public String build(Object newObj,String dbName,int type) throws SQLBuildException
    {
		switch(type)
		{
			case SQLBUILD_TYPE_SELECT:
				return "";
			case SQLBUILD_TYPE_DELETE:
                return buildDelete(dbName,newObj);

		}
        return null;
    }

    public String build(Object newObj,int type) throws SQLBuildException
    {
        return build(newObj,null,type);
    }

	public String build(Object newObj) throws SQLBuildException
	{
		return build(newObj,null,SQLBUILD_TYPE_INSERT);
	}

	private String buildDelete(String dbName,Object deleteObject) throws SQLBuildException
	{
		String className = deleteObject.getClass().getName();
		String deleteSql = "";
        try {
			Database db = ormManager.getDatabase(dbName);
			Table table = db.getTableByJavaName(className);
            List primaryKeys = table.getPrimaryKey();
            String condition = this.getDeleteCondition(dbName,primaryKeys,deleteObject);
            String tableName = table.getName();
            deleteSql = "delete from " + tableName + " where " + condition;
        } catch (ORMappingException ex) {
			log.error(ex.getMessage());
			throw new SQLBuildException(ex.getMessage());
        }
		return deleteSql;
	}

    /**
     * 获取删除条件
     * @param dbName String
     * @param primayKeys List
     * @return String
     */
    private String getDeleteCondition(String dbName,List primayKeys,Object deleteObject)
    {
        String ret = "";
        Iterator cols = primayKeys.iterator();
        boolean first = true;
        while(cols.hasNext())
        {
            Column col = (Column)cols.next();
            String colName = col.getName();
            String javaName = col.getJavaName();
            String javaType = col.getJavaType();
            SchemaType colType = (SchemaType)col.getType();
            Object value = ValueObjectUtil.getValue(deleteObject,javaName);
            if(value != null)
            {
                String expression = this.getExpression(colName,value,javaType,colType,dbName,0);
                if(first)
                {
                    ret = expression;
                    first = false;
                }
                else
                    ret += " and " + expression;

            }


            //col
        }
        return ret;
    }

    /**
     * 构建表达式
     * @param colName
     * @param value
     * @param javaType
     * @param colType
     * @return
     */
    private String getExpression(String colName,
            					 Object value,
            					 String javaType,
            					 SchemaType colType,
            					 String dbName,
            					 int type) {



        String svalue = "";
        if(TypeMap.isDate(colType))
        {
            Date date = null;
            if(value instanceof String)
                date = SimpleStringUtil.stringToDate((String)value);
            else
                date = (Date)value;
            svalue = DBAdapter.getDateString((Date)value,dbName);
        }
        else if(TypeMap.isTextType(colType))
        {

            svalue = DBAdapter.getStringDelimiter(dbName)  + "" + value + DBAdapter.getStringDelimiter(dbName);
        }
        else
            svalue = value.toString();
        return colName + "=" + svalue;
    }
}
