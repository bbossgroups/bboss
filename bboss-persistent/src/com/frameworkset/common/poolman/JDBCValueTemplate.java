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

/**
 * 
 * 
 * <p>Title: JDBCValueTemplate.java</p>
 *
 * <p>Description: 事务执行模板类， 提供执行数据库操作的模板方法，该方法有返回值
 * 该模板方法将方法中的所有数据库操作涵盖在一个数据库事务中</p>
 * @see com.frameworkset.common.poolman.TemplateDBUtil中的方法
 * public static Object executeTemplate(JDBCValueTemplate template) throws Throwable
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
public interface JDBCValueTemplate<T> {
	/**
	 * 用来实现需要控制的数据库事务的数据库操作的模板方法，避免用户直接在代码中使用
	 * 数据库事务管理代码。
	 * 该方法允许用户返回一个对象类型的返回值
	 * @throws Exception
	 * @return Object
	 * 
	 */
	public T execute() throws Exception;

}
