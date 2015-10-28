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
package com.frameworkset.orm;

import java.util.Set;

import com.frameworkset.common.poolman.DBUtil;

/**
 * <p>Title: 实现数据库记录向对象转换功能</p>
 *
 * <p>Description: 实现纯对象插入、删除、更新数据库操作
 *                 实现sql语句结合业务对象的查询操作（查询结果集封装以业务对象的格式封装）
 * </p>
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
public class ORMUtil
    extends DBUtil {
    public ORMUtil() {

    }

    /**
     * 封装数据的对象的Class类型
     * 执行查询操作时可以直接将返回的结果集封装成一个包含该类型对象的列表来使用
     * @param dbName String 数据库链接池的名称
     * @param newObj Object 插入记录数据封装对象
     * @return Object 如果自动产生数据库主键则返回数据库主键
     */

    public Object executeInsert(String dbName,Object newObj) throws ORMappingException
    {
        return null;
    }

    /**
     * 封装数据的对象的Class类型
     * 执行查询操作时可以直接将返回的结果集封装成一个包含该类型对象的列表来使用
     * @param newObj Object 插入记录数据封装对象
     * @return Object 如果自动产生数据库主键则返回数据库主键
     */

    public Object executeInsert(Object newObj) throws ORMappingException
    {
        //传入缺省的数据库连接池
        return executeInsert(null,newObj);
    }

    /**
     * 批量插入操作
     *
     * @param newObj Object 插入记录数据封装对象
     * @return Object 如果自动产生数据库主键则返回数据库主键
     */

    public Object[] executeBatchInsert(Set newObj) throws ORMappingException
    {
        //传入缺省的数据库连接池
        return executeBatchInsert(null,newObj);
    }

    /**
     * executeBatchInsert
     *
     * @param object Object
     * @param newObj Set
     * @return Object[]
     */
    private Object[] executeBatchInsert(String dbName, Set newObj) throws ORMappingException{
        return null;
    }


    /**
     * 更新操作
     * @param dbName String 数据库链接池的名称
     * @param newObj Object 更新记录数据封装对象
     */

    public void executeUpdate(String dbName,Object newObj) throws ORMappingException
    {

    }

    /**
     * 更新操作
     * @param newObj Object 更新记录数据封装对象
     */

    public void executeUpdate(Object newObj) throws ORMappingException
    {
        //传入缺省的数据库连接池
        executeUpdate(null,newObj);
    }

    /**
     * 批量更新操作
     *
     * @param newObj Object 批处理更新记录数据封装对象

     */

    public void executeBatchUpdate(Set newObj) throws ORMappingException
    {
        //传入缺省的数据库连接池
        executeBatchUpdate(null,newObj);
    }

    /**
     * 批量更新操作
     * @param dbName String 数据库链接池的名称
     * @param newObj Set 批处理更新记录数据封装对象
     */
    public void executeBatchUpdate(String dbName, Set newObj) throws ORMappingException{

    }

}
