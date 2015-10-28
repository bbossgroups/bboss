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

import java.sql.SQLException;

import javax.transaction.RollbackException;

import com.frameworkset.orm.transaction.TransactionManager;

/**
 * 
 * 
 * <p>Title: TemplateDBUtil.java</p>
 *
 * <p>Description: 执行数据库操作的模板类</p>
 *
 * <p>Copyright: Copyright (c) 2007</p>
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
public class TemplateDBUtil extends PreparedDBUtil{
	
	/**
	 * 执行JDBCTemplate的execute方法，整个方法的执行过程被包含在
	 * 一个事务中，如果有异常发生则事务将被回滚，如果整个事务被正常结束，则被提交
	 * 如果模板方法在执行的过程当中抛出异常，则该异常将被继续抛出给业务层
	 * @param template
	 * @throws Throwable
	 */
	public static void executeTemplate(JDBCTemplate template) throws Throwable
	{
		TransactionManager tm = new TransactionManager();
		try
		{
			tm.begin();
			template.execute();
			tm.commit();
		}
		catch(SQLException e)
		{
			try {
				tm.rollback();
			} catch (RollbackException e1) {
			}
			throw e;
		}
		catch(Throwable e)
		{
			try {
				tm.rollback();
			} catch (RollbackException e1) {

			}
			throw e;
		}
		
	}
	
	 /**
	 * 执行JDBCValueTemplate的execute方法，该方法有返回值
	 * 整个方法的执行过程被包含在一个事务中，如果有异常发生则事务将被回滚，如果整个事务被正常结束，则被提交
	 * 并且将返回值返回给业务层。
	 * 如果模板方法在执行的过程当中抛出异常，则该异常将被继续抛出给业务层
	 * 
	 * @param template
	 * @throws Throwable
	 * @return Object
	 */
	public static <T> T executeTemplate(JDBCValueTemplate<T> template) throws Throwable
	{
		TransactionManager tm = new TransactionManager();
		T value = null;
		try
		{
			tm.begin();
			value = template.execute();
			tm.commit();
			return value;
		}
		catch(SQLException e)
		{
			try {
				tm.rollback();
			} catch (RollbackException e1) {
			}
			throw e;
		}
		catch(Throwable e)
		{
			try {
				tm.rollback();
			} catch (RollbackException e1) {

			}
			throw e;
		}
	}
	
	
}
