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

package com.frameworkset.util;

import java.util.List;

import org.apache.oro.text.regex.Perl5Compiler;

import com.frameworkset.util.VariableHandler.Index;
import com.frameworkset.util.VariableHandler.SQLStruction;
import com.frameworkset.util.VariableHandler.URLStruction;
import com.frameworkset.util.VariableHandler.Variable;

/**
 * <p>Title: TestVaribleHandler.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2008</p>
 * @Date 2010-3-10 下午02:31:03
 * @author biaoping.yin
 * @version 1.0
 */
public class TestVaribleHandler
{
    @org.junit.Test
    public void testStringReplace()
    {
        String pretoken = "#\\[";
        String endtoken = "\\]";
        String url = "#[context]/#[context0]/#[context0]creatorepp";
        String[][] vars = VariableHandler.parser2ndSubstitution(url, pretoken, endtoken, "?");
        System.out.println(vars[0][0]);
        
    }
    
    @org.junit.Test
    public void testVariableParser()
    {
        String pretoken = "#\\[";
        String endtoken = "\\]";
        String url = "#[context]/#[context0]/#[context1]creatorepp";
        String[] vars = VariableHandler.variableParser(url, pretoken, endtoken);
        
        System.out.println(vars[0]);
        System.out.println(vars[1]);
        System.out.println(vars[2]);
        
    }
    
    
    @org.junit.Test
    public void testMutiVariableParser()
    {
        String pretoken = "<clob>";
        String endtoken = "</clob>";
        String url = "<clob>a\nbc</clob></clob>b<clob>abc</clob>";
        String[][] vars = VariableHandler.parser2ndSubstitution(url, pretoken, endtoken,"?",Perl5Compiler.SINGLELINE_MASK | Perl5Compiler.DEFAULT_MASK);
        
        System.out.println(vars[0][0]);
        System.out.println(vars[1][0]);
        System.out.println(vars[1][1]);
        
    }
    
    @org.junit.Test
    public void testMutidefaltVariableParser()
    {
        
        String url = "${abc}${abcd}";
        String[][] vars = VariableHandler.parser2ndSubstitution(url,"?");
        
        System.out.println(vars[0][0]);
        System.out.println(vars[1][0]);
        System.out.println(vars[1][1]);
        
    }
    @org.junit.Test
    public void testUrlParser()
    {
    	String url = "http://localhost:80/detail.html?user=#[account[0][0]]&password=#[password->aaa[0]->bb->cc[0]]love";
        URLStruction a = VariableHandler.parserURLStruction(url);
		 url =
		 "http://localhost:80/detail.html?user=#[account&password=password]&love=#[account[key]]";
		 a = VariableHandler.parserURLStruction(url);
		 url =
			 "http://localhost:80/detail.html?user=#[account&password=password]&love=#[account";
//		 
		 a = VariableHandler.parserURLStruction(url);
		 url =
			 "http://localhost:80/detail.html?user=#[account&password=#[password&love=#[account";
		 a = VariableHandler.parserURLStruction(url);
//		 url =
//			 "http://localhost:80/detail.html";
//		 
//		 url =
//			 "http://localhost:80/#[]detail.html";
//		 url =
//			 "#[account";
		 System.out.println("url:"+url);
		// Item item = new Item();
		
		// Map<String,String> map = new HashMap<String, String>();
		// map.put("account", "aaa");
		// map.put("password", "123");
		// item.combinationItemUrlStruction(a, map);

		if(a != null){
			
		
		List<String> tokens = a.getTokens();
		for (int k = 0; k < tokens.size(); k++) {
			System.out.println("tokens[" + k + "]:" + tokens.get(k));
		}
		List<Variable> variables = a.getVariables();

		for (int k = 0; k < variables.size(); k++) {

			Variable as = variables.get(k);

			System.out.println("变量名称：" + as.getVariableName());
			System.out.println("变量对应位置：" + as.getPosition());

		}
		}
    }
    @org.junit.Test
    public void testSQLParser()
    {
    	 String url = "http://localhost:80/detail.html?user=#[account[0][0]]&password=#[password->aaa[0]->bb->cc[0]]love";
         URLStruction a = VariableHandler.parserSQLStruction(url);
 		 url =
 		 "http://localhost:80/detail.html?user=#[account]&password=#[password]&love=#[account[key]]";
 		 a = VariableHandler.parserSQLStruction(url);
 		url =
 	 		 "http://localhost:80/detail.html?user=#[account]&password=#[password]&love=#[account[0";
 	 		 a = VariableHandler.parserSQLStruction(url);
 	 		 
 	 		url =
 	 	 		 "http://localhost:80/detail.html?user=account&password=password&love=account";
 	 	 		 a = VariableHandler.parserSQLStruction(url);
 	 	 		url =
 	 	 	 		 "http://localhost:80/detail.html?user=account&password=password]&love=account]";
 	 	 	 		 a = VariableHandler.parserSQLStruction(url);
 		 url =
 			 "http://localhost:80/detail.html,user=#[account],password=#[password],account=#[account]";
 		 a = VariableHandler.parserSQLStruction(url);
 		 
 		 url =
 			 "http://localhost:80/#[detail.html,user=#[account],password=#[password],account=#[account]";
 		 a = VariableHandler.parserSQLStruction(url);
// 		 url =
// 			 "http://localhost:80/detail.html";
// 		 
// 		 url =
// 			 "http://localhost:80/#[]detail.html";
// 		 url =
// 			 "#[account";
 		 System.out.println("url:"+url);
 		// Item item = new Item();
 		
 		// Map<String,String> map = new HashMap<String, String>();
 		// map.put("account", "aaa");
 		// map.put("password", "123");
 		// item.combinationItemUrlStruction(a, map);

 		if(a != null){
 			
 		
	 		List<String> tokens = a.getTokens();
	 		for (int k = 0; k < tokens.size(); k++) {
	 			System.out.println("tokens[" + k + "]:" + tokens.get(k));
	 		}
	 		List<Variable> variables = a.getVariables();
	
	 		for (int k = 0; k < variables.size(); k++) {
	
	 			Variable as = variables.get(k);
	
	 			System.out.println("变量名称：" + as.getVariableName());
	 			System.out.println("变量对应位置：" + as.getPosition());
	 			//如果变量是对应的数组或者list、set、map中元素的应用，则解析相应的元素索引下标信息
	 			List<Index> idxs = as.getIndexs();
	 			if(idxs != null)
	 			{
	 				for(int h = 0; h < idxs.size(); h ++)
	 				{
	 					Index idx = idxs.get(h);
	 					if(idx.getInt_idx() > 0)
	 					{
	 						System.out.println("元素索引下标："+idx.getInt_idx());
	 					}
	 					else
	 					{
	 						System.out.println("map key："+idx.getString_idx());
	 					}
	 				}
	 			}
	
	 		}
 		}
    }
    /**
     * 对比正则表达式解析变量和VariableHandler解析sql变量的性能差异性
     * 正则表达式解析变量和VariableHandler解析sql变量的功能区别：
     * VariableHandler支持属性引用（层级不限），支持数组、list、map、set元素的引用,容错性比正则表达式方式要好
     * 从测试的性能数据上来看，正则表达式和VariableHandler parserSQLStruction方法的性能基本差不多
     */
    @org.junit.Test
    public void regexUtilvsVarialparserUtil()
    {
    	String listRepositorySql = "select *  from CIM_ETL_REPOSITORY  where 1=1 " +
		"#if($HOST_ID && !$HOST_ID.equals(\"\")) " +
		"	and HOST_ID = #[HOST_ID->bb[0]]" +
		"#end  " +
		" and PLUGIN_ID = #[PLUGIN_ID] " +
		" and CATEGORY_ID = #[CATEGORY_ID] and APP = #[APP] ";
    	
    
    	StringBuilder b = new StringBuilder();
    	b.append(listRepositorySql).append(listRepositorySql)
    	.append(listRepositorySql)
    	.append(listRepositorySql)
    	.append( listRepositorySql)
    	.append(listRepositorySql)
    	.append(listRepositorySql)
    	.append(listRepositorySql)
    	.append(listRepositorySql)
    	.append(listRepositorySql)
    	.append(listRepositorySql)
    	.append(listRepositorySql)
    	.append(listRepositorySql)
    	.append(listRepositorySql)
    	.append(listRepositorySql)
    	.append(listRepositorySql)
    	.append(listRepositorySql)
    	.append(listRepositorySql)
    	.append(listRepositorySql)
    	.append(listRepositorySql)
    	.append(listRepositorySql)
    	.append(listRepositorySql)
    	.append(listRepositorySql)
    	.append(listRepositorySql)
    	.append(listRepositorySql)
    	.append(listRepositorySql)
    	.append(listRepositorySql)
    	.append(listRepositorySql)
    	.append(listRepositorySql);
    	listRepositorySql = b.toString();
    	
    	SQLStruction a = VariableHandler.parserSQLStruction(listRepositorySql);
    	 long start = System.currentTimeMillis();
    	 String[][] sqls = VariableHandler.parser2ndSubstitution(listRepositorySql, "#\\[","\\]","?");
    	 long end = System.currentTimeMillis();
    	 System.out.println(sqls[0][0]);
    	 System.out.println("--------------------------");
    	 System.out.println(a.getSql());
    	 System.out.println(sqls[0][0].equals(a.getSql()));
    	 
    	 
    	 start = System.currentTimeMillis();
    	  sqls = VariableHandler.parser2ndSubstitution(listRepositorySql, "#\\[","\\]","?");
    	 end = System.currentTimeMillis();
    	 System.out.println("正则表达式耗时：" + (end - start));
    	 start = System.currentTimeMillis();
    	 a = VariableHandler.parserSQLStruction(listRepositorySql);
	   	 end = System.currentTimeMillis();
	   	System.out.println("变量解析耗时：" + (end - start));
    	
    	
    	
    	 
    }
    
    
    /**
     * 正则表达式只能解析简单的变量，无法解析复杂的变量格式
     * #[HOST_ID]这种格式正则表达式能够解析
     * #[HOST_ID->bb[0]]这种带引用的格式，正则表达就不能解析了
     * VariableHandler.parserSQLStruction方法可以解析上述两种格式的变量，并且能够将复杂的变量的信息
     * 以Variable列表的方式存储，以供持久层框架对这些变量求值
     */
    @org.junit.Test
    public void varialparserUtil()
    {
    	String listRepositorySql = "select *  from CIM_ETL_REPOSITORY  where 1=1 " +
		"#if($HOST_ID && !$HOST_ID.equals(\"\")) " +
		"	and HOST_ID = #[host_id->bb[2]->bb[aa]]" +
		"#end  " +
		" and PLUGIN_ID in (#[PLUGIN_ID[0]], #[PLUGIN_ID[1]])" +
		" and CATEGORY_ID = #[CATEGORY_ID] and APP = #[APP] ";
    	String deleteAllsql = "delete from LISTBEAN where FIELDNAME in (#[bean->fss],#[bean->ftestttt],#[bean->fsdds]," +
		"#[bean->finsertOpreation],#[bean->fss556])";
    	SQLStruction b = VariableHandler.parserSQLStruction(deleteAllsql);
    	SQLStruction a = VariableHandler.parserSQLStruction(listRepositorySql);
    	Variable hostid = a.getVariables().get(0);
    	Object value = VariableHandler.evaluateVariableValue(hostid, new Host());
    	System.out.println(value);
    	 long start = System.currentTimeMillis();
    	 String[][] sqls = VariableHandler.parser2ndSubstitution(listRepositorySql, "#\\[","\\]","?");
    	 long end = System.currentTimeMillis();
    	 System.out.println("正则表达式解析的错误sql:" + sqls[0][0]);
    	 System.out.println("--------------------------");
    	 System.out.println("bboss变量解析器的分析出的正确sql:" + a.getSql());
    	 System.out.println("bboss变量:" + a.getVariables().get(0).toString());
    	 System.out.println(sqls[0][0].equals(a.getSql()));
    	 
    	 
    	 start = System.currentTimeMillis();
    	  sqls = VariableHandler.parser2ndSubstitution(listRepositorySql, "#\\[","\\]","?");
    	 end = System.currentTimeMillis();
    	 System.out.println("正则表达式耗时：" + (end - start));
    	 start = System.currentTimeMillis();
    	 a = VariableHandler.parserSQLStruction(listRepositorySql);
	   	 end = System.currentTimeMillis();
	   	System.out.println("变量解析耗时：" + (end - start));
    	
    	
    	
    	 
    }
    @org.junit.Test
    public void testListBeanField()
    {
    	String sql = "select * from TD_ORDER_REPAYMENT_PLAN where order_id in(#[orders[0]->order_id] )";
    	SQLStruction ss = VariableHandler.parserSQLStruction(sql);
    	System.out.println();
    }
    
    @org.junit.Test
    public void testListBeanField1()
    {
    	String sql = "select * from TD_ORDER_REPAYMENT_PLAN where order_id in(#[orders[0]->order_id[0]],#[orders2[0]->order_id[0]] )";
    	SQLStruction ss = VariableHandler.parserSQLStruction(sql);
    	System.out.println();
    }
    	
    
}
