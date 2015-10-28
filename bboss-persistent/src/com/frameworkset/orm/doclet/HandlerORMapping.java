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
package com.frameworkset.orm.doclet;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

import org.apache.log4j.Logger;

import xjavadoc.XClass;
import xjavadoc.XDoc;
import xjavadoc.XField;
import xjavadoc.XJavaDoc;
import xjavadoc.XTag;
import xjavadoc.filesystem.FileSourceSet;

import com.frameworkset.orm.ORMappingException;
import com.frameworkset.orm.ORMappingManager;
import com.frameworkset.orm.engine.model.Column;
import com.frameworkset.orm.engine.model.Database;
import com.frameworkset.orm.engine.model.Table;

/**
 * <p>Title: HandlerORMapping</p>
 *
 * <p>Description: 初始化解析系统中所有的javabean中与数据库关系表之间存在的对象属性和表字段的映射关系.
 *                 分析o/r mapping元模型,并且对元模型进行持久化
 *                 根据以下规则进行解析：
 *                 在javabean的类注释中定义数据库名称和表名称，这种定义是可选的
 *                 在javabean的属性定义的注释中定义字段对应的表的字段，这种定义是必须的
 * </p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: </p>
 *
 * @author biaoping.yin
 * @version 1.0
 */
public class HandlerORMapping {
    private static Logger log = Logger.getLogger(HandlerORMapping.class);

    private final XJavaDoc _xJavaDoc = new XJavaDoc();
    private File srcDir;
    private Collection allClasses = null;
    public static void main(String[] args) {
        HandlerORMapping handlerormapping = new HandlerORMapping();
//        handlerormapping.init("src");
//        handlerormapping.execute();
        String str = null;
        try {
            str = handlerormapping.restoreFromXml("d:/schemas").getAnonymityDatabase().
                  toString();
        } catch (ORMappingException ex) {
        }
        log.debug(str);

    }

    /**
     * 根据传入的源文件的相对根目录（绝对目录通过保存在系统属性中basedir+relativeSrcdir而得），
     * 初始化xjavadoc，将所有的java文件转换成XClass对象，为解析java对象中属性与数据库表之间得关系做准备
     * @param relativeSrcdir String
     */
    public void init(String relativeSrcdir)
    {
        long start = System.currentTimeMillis();
        srcDir = new File(System.getProperty("basedir"),relativeSrcdir);
        log.debug("Load java source in " + srcDir.getAbsolutePath());
        _xJavaDoc.reset(true);
        _xJavaDoc.addSourceSet(new FileSourceSet(srcDir));
        allClasses = _xJavaDoc.getSourceClasses();
        long end = System.currentTimeMillis();
        log.debug("Load java source complete! Total cost:" + (end - start)/1000 + " seconds");
    }

    /**
     * 根据传入的源文件的根目录，
     * 初始化xjavadoc，将所有的java文件转换成XClass对象，为解析java对象中属性与数据库表之间得关系做准备
     * @param srcdir String
     */
    public void initByCompletePath(String srcdir)
    {
        long start = System.currentTimeMillis();
        log.debug("Load java source in " + srcdir);
        srcDir = new File(srcdir);
        _xJavaDoc.reset(true);
        _xJavaDoc.addSourceSet(new FileSourceSet(srcDir));
        allClasses = _xJavaDoc.getSourceClasses();
        long end = System.currentTimeMillis();
        log.debug("Load java source complete! Total cost:" + (end - start)/1000 + " seconds");
    }

    /**
     * 解析开始
     */
    public void execute()
    {
        ORMappingManager.getInstance().lock();
        log.debug("Handler ORMapping start...");
        long start = System.currentTimeMillis();
        if(allClasses == null)
        {
            log.debug("No java source in dir:" + srcDir.getAbsoluteFile());
            return;
        }

        Iterator it = allClasses.iterator();
        XClass xclass = null;
        for(;it.hasNext();)
        {
            Table table = null;
            xclass = (XClass)it.next();
            //如果xclass对应的类没有指定表的字段信息，继续进行下一次循环
            if(!needHandler(xclass))
            {
                //log.debug("ignore parseing " + xclass.getQualifiedName() + ".");
                continue;
            }
            log.debug("Start parseing " + xclass.getQualifiedName() + ".....");
            Database dataBase = null;
            String dbName = null;
            String dbType = null;
            String tableName = null;

            String tableSchema = null;

            XDoc doc = xclass.getDoc();
            XTag databaseTag = doc.getTag(TagConst.TAG_DATABASE);
            XTag tableTag = doc.getTag(TagConst.TAG_TABLE);

            if(databaseTag == null && tableTag == null)
                dataBase = ORMappingManager.getInstance().creatAnonymityDataBase();
            else
            {
                if (databaseTag != null) {
                    dbName = databaseTag.getAttributeValue(TagConst.
                        TAG_DATABASE_NAME);
                    dbType = databaseTag.getAttributeValue(TagConst.
                        TAG_DATABASE_TYPE);
                    tableName = databaseTag.getAttributeValue(TagConst.
                        TAG_DATABASE_TABLE);
                }

                if(tableTag != null)
                {
                    tableName = tableTag.getAttributeValue(TagConst.
                        TAG_TABLE_NAME);

                    //保留属性
                    tableSchema = tableTag.getAttributeValue(TagConst.
                        TAG_TABLE_SCHEMA);
                }
                //创建数据库实例

                //如果没有指定数据库标签，但是表标签指定了，则根据表标签的属性创建数据库实例
                //否则
                if (databaseTag == null && tableTag != null) {
                    if (tableSchema != null)
                        dataBase = ORMappingManager.getInstance().creatDataBase(tableSchema);
                    else
                        dataBase = ORMappingManager.getInstance().creatDefaultDataBase();
                } else
                    dataBase = ORMappingManager.getInstance().creatDataBase(dbName, dbType);
            }
            try {                //创建表实例



                log.debug("Parseing table ....." + tableName);
                table = ORMappingManager.getInstance().creatTable(tableName);
                table.setDatabase(dataBase);

                table.setBaseClass(xclass.getSuperclass().getQualifiedName());
                table.setJavaName(xclass.getQualifiedName());
                table.setPackage(xclass.getContainingPackage().getName());
                Iterator xFields = xclass.getFields().iterator();
                for(;xFields.hasNext();)
                {
                    XField field = (XField)xFields.next();
                    XDoc fdoc = field.getDoc();
                    if(fdoc != null)
                    {
                        XTag columnTag = fdoc.getTag(TagConst.TAG_COLUMN);
                        if(columnTag == null)
                            continue;

                        String columnName = columnTag.getAttributeValue(TagConst.TAG_COLUMN_NAME);

                        String columnDescription = columnTag.getAttributeValue(TagConst.TAG_COLUMN_DESCRIPTION);
                        String columnPrimarykey = columnTag.getAttributeValue(TagConst.TAG_COLUMN_PRIMARYKEY);
                        String columnRequired = columnTag.getAttributeValue(TagConst.TAG_COLUMN_REQUIRED);
                        String columnType = columnTag.getAttributeValue(TagConst.TAG_COLUMN_TYPE);
                        Column column = ORMappingManager.getInstance().creatColumn(columnName);
                        column.setTable(table);
                        column.setDescription(columnDescription);
                        if(columnPrimarykey != null)
                            column.setPrimaryKey(new Boolean(columnPrimarykey).booleanValue());
                        if(columnRequired != null)
                            column.setNotNull(new Boolean(columnRequired).booleanValue());
                        column.setType(columnType);
                        column.setJavaName(field.getName());
                        log.debug("Parseing table column '" + columnName + "' with field '" + field.getName() + "'");
                        column.setJavaType(field.getType().getQualifiedName());

                        table.addColumn(column);

                    }
                }

                //table.setDatabase(dataBase);
                //将表实例添加到数据库中
                dataBase.addTable(table);
                //System.out.println(dataBase);
                log.debug("Complete parseing " + xclass.getQualifiedName() + ".");
            } catch (ORMappingException ex) {
                ex.printStackTrace();
            }
        }
        ORMappingManager.getInstance().unlock();
        long end = System.currentTimeMillis();
        log.debug("Complete parseing all java sources! Total cost:" + (end - start) + " ms");
    }

    /**
     * 判断是否需要解析xclass中的数据库相关信息
     * @param xclass XClass
     * @return boolean
     */
    private boolean needHandler(XClass xclass)
    {
        boolean need = false;
        if(xclass == null || xclass.getFields() == null || xclass.getFields().size() == 0)
            need = false;
        else
        {
            Iterator fields = xclass.getFields().iterator();
            for(;fields.hasNext();)
            {
                XField field = (XField)fields.next();
                if(field.getDoc().getTag(TagConst.TAG_COLUMN) != null)
                {
                    need = true;
                    break;
                }
            }

        }
        return need;
    }

    private String getPackage(XClass xclass)
    {
        String name = xclass.getModifiers();
        return null;
    }

    /**
     * cache
     */
    public boolean cache() {
        try {
            ORMappingManager.getInstance().cache();
            return true;
        } catch (IOException ex) {
            return false;
        }
    }

    /**
    * cache
    */
   public boolean cache(String cachePath) {
       try {
           ORMappingManager.getInstance().cache(cachePath);
           return true;
       } catch (IOException ex) {
           return false;
       }
   }


    /**
     * restore
     */
    public ORMappingManager restore() {
        try {
            return ORMappingManager.restore();
        } catch (ClassNotFoundException ex) {
            return null;
        } catch (IOException ex) {
            return null;
        }
    }

    /**
     * restore from path
     */
    public ORMappingManager restore(String path) {
        try {
            return ORMappingManager.restore(path);
        } catch (ClassNotFoundException ex) {
            return null;
        } catch (IOException ex) {
            return null;
        }
    }

    /**
     * 将数据库存放在目录xmlDir中的schema文件转换成
     * @param xmlDir String
     * @return ORMappingManager
     */
    public boolean cachetoXml(String xmlDir)
    {
        try {
             ORMappingManager.getInstance().cacheToXml(xmlDir);
             return true;
        } catch (ORMappingException ex) {
            return false;
        }
    }

    /**
     * 从存放在目录xmlDir中的schema文件转换成数据库
     * @param xmlDir String
     * @return ORMappingManager
     */
    public ORMappingManager restoreFromXml(String xmlDir)
    {
        return ORMappingManager.getInstance().restoreFromXml(xmlDir);
    }





}
