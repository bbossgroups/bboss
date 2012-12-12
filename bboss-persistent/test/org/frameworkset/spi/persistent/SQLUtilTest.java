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

package org.frameworkset.spi.persistent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.frameworkset.persitent.util.SQLUtil;
import org.frameworkset.spi.assemble.Pro;
import org.junit.Test;

import com.frameworkset.util.SimpleStringUtil;

/**
 * <p>Title: SQLUtilTest.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2007</p>
 * @Date 2010-7-23 ÉÏÎç11:39:22
 * @author biaoping.yin
 * @version 1.0
 */
public class SQLUtilTest {
	@Test
	public void testListSQLVariable()
	{
		SQLUtil context = SQLUtil.getInstance("org/frameworkset/spi/persistent/test-sql.xml");
		List<Pro> lists = context.getListSQLs("sys_tableinfo_list");
		int i = 0;
		for(Pro pro:lists)
		{
			String sql = pro.toString();
			Map variablevalues = new HashMap();
			variablevalues.put("table_id_generator", "test");
			variablevalues.put("table_id_type", "sequence");
			System.out.println(context.evaluateSQL("sys_tableinfo_list" + i, sql, variablevalues));
			i ++;
		}
		
		
	}
	
	@Test
	public void testListSQLKeys()
	{
		SQLUtil context = SQLUtil.getInstance("org/frameworkset/spi/persistent/test-sql.xml");
		String[] rets = context.getPropertyKeys();
		System.out.println(SimpleStringUtil.tostring(rets));
		
		
	}
	
	@Test
	public void testtestsqlvariable()
	{
		SQLUtil context = SQLUtil.getInstance("org/frameworkset/spi/persistent/test-sql.xml");
		String sql = context.getSQL("testsqlvariable");
		Map variablevalues = new HashMap();
		variablevalues.put("table_id_generator", "test");
		variablevalues.put("table_id_type", "sequence");
		System.out.println(context.evaluateSQL("testsqlvariable" , sql, variablevalues));
//		System.out.println(sql);
		
		
		
	}
	
	@Test
	public void testtesterrorsqlvariable()
	{
		SQLUtil context = SQLUtil.getInstance("org/frameworkset/spi/persistent/test-sql.xml");
		String sql = context.getSQL("testerrsqlvariable");
		Map variablevalues = new HashMap();
		variablevalues.put("table_id_generator", "test");
		variablevalues.put("table_id_type", "sequence");
		System.out.println(context.evaluateSQL("testerrsqlvariable" , sql, variablevalues));
		
	}
	
	public static void main(String[] args)
	{
		SQLUtilTest test = new SQLUtilTest();
		long start = System.currentTimeMillis();
		test.testtestsqlvariable();
		long end = System.currentTimeMillis();
		System.out.println(end -start);
		start = System.currentTimeMillis();
		for(int i = 0; i < 10000; i ++)
		test.testtestsqlvariable();
		end = System.currentTimeMillis();
		System.out.println(end -start);
	}
	@Test
	public void testrefresch()
	{
		SQLUtil context = SQLUtil.getInstance("org/frameworkset/spi/persistent/test-sql.xml");
		SQLUtil context2 = SQLUtil.getInstance("org/frameworkset/spi/persistent/test-sql2.xml");
		String sql = context.getSQL("testerrsqlvariable");
		String sql2 = context2.getSQL("testerrsqlvariable");
		System.out.println("sql:"+sql);
		System.out.println("sql2:"+sql2);
		
		sql = context.getSQL("testerrsqlvariable");
		sql2 = context2.getSQL("testerrsqlvariable");
		System.out.println("sql:"+sql);
		System.out.println("sql2:"+sql2);
	}

}
