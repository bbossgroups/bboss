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

import com.frameworkset.util.variable.DefaultvalueVariableStructionBuiler;
import org.frameworkset.util.ClassUtil;
import org.frameworkset.util.ClassUtil.PropertieDescription;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * <p>Title: VariableHandler.java</p> 
 * <p>Description: 变量解析程序 </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2008</p>
 * @Date 2009-12-16 上午11:34:31
 * @author biaoping.yin
 * @version 1.0
 */
public class VariableHandler
{
    public static String default_regex = "\\$\\{(.+?)\\}";
    
    
    private static String buildVariableRegex(String pretoken,String endtoken)
    {
        StringBuilder ret = new StringBuilder();
//        ret.append(pretoken).append("([^").append(endtoken).append("]+)").append(endtoken);
        ret.append(pretoken).append("(.+?)").append(endtoken);
        return ret.toString();
        
    }
    /**
     * 可以根据默认的正则式default_regex = "\\$\\{([^\\}]+)\\}"获取输入串中的变量，作为数组返回
     * @param inputString
     * @return
     */
    public static String[] variableParser(String inputString)
    {
        String[] vars = RegexUtil.containWithPatternMatcherInput(inputString, default_regex);
        return vars;
    }
    
    /**
     * 可以根据指定的变量的前导符和后导符获取输入串中的变量，作为数组返回
     * @param inputString
     * @param pretoken
     * @param endtoken
     * @return
     */
    public static String[] variableParser(String inputString,String pretoken,String endtoken)
    {
//        String regex = buildVariableRegex(pretoken,endtoken);   
//        String[] vars = RegexUtil.containWithPatternMatcherInput(inputString, regex);
//        return vars;
    	return variableParser( inputString,pretoken,endtoken,RegexUtil.default_mask);
    }
    
    /**
     * 可以根据指定的变量的前导符和后导符获取输入串中的变量，作为数组返回
     * @param inputString
     * @param pretoken
     * @param endtoken
     * @return
     */
    public static String[] variableParser(String inputString,String pretoken,String endtoken,int mask)
    {
        String regex = buildVariableRegex(pretoken,endtoken);   
        String[] vars = RegexUtil.containWithPatternMatcherInput(inputString, regex,mask);
        return vars;
    }
    
    /**
     * 可以根据指定的正则式获取输入串中的变量，作为数组返回
     * @param inputString
     * @param regex
     * @return
     */
    public static String[] variableParser(String inputString,String regex)
    {
//        String regex = buildVariableRegex(pretoken,endtoken);   
        String[] vars = RegexUtil.containWithPatternMatcherInput(inputString, regex);
        return vars;
    }
    /**
     * 从串src中析取匹配regex模式的所有字符串，并且用substitution替换匹配上模式的子符串
     * @param inputString
     * @param regex
     * @param substitution
     * @return String[][]二维数组，第一维表示替换后的src，第二维表示匹配regex的所有的子串数组
     */
    public static String[][] parser2ndSubstitution(String inputString,String regex,String substitution)
    {
        return RegexUtil.contain2ndReplaceWithPatternMatcherInput(inputString, regex, substitution);
    }
    
    /**
     * 从串src中析取匹配regex模式的所有字符串，并且用substitution替换匹配上模式的子符串
     * @param inputString
     * @param substitution
     * @return String[][]二维数组，第一维表示替换后的src，第二维表示匹配regex的所有的子串数组
     */
    public static String[][] parser2ndSubstitution(String inputString,String substitution)
    {
        return RegexUtil.contain2ndReplaceWithPatternMatcherInput(inputString,default_regex , substitution);
    }
    
    
    /**
     * 从串src中析取匹配pretoken 变量定义前缀和endtoken 变量定义后缀指定模式所有字符串，并且用substitution替换匹配上模式的子符串
     * @param inputString 输入的串 
     * @param pretoken 变量定义前缀
     * @param endtoken 变量定义后缀
     * @param substitution
     * @return String[][]二维数组，第一维表示替换后的src，第二维表示匹配regex的所有的子串数组
     */
    public static String[][] parser2ndSubstitution(String inputString,String pretoken,String endtoken ,String substitution)
    {
        String regex = buildVariableRegex(pretoken,endtoken);  
        return RegexUtil.contain2ndReplaceWithPatternMatcherInput(inputString, regex, substitution,RegexUtil.default_mask);
    }
    
    /**
     * 从串src中析取匹配pretoken 变量定义前缀和endtoken 变量定义后缀指定模式所有字符串，并且用substitution替换匹配上模式的子符串
     * @param inputString 输入的串 
     * @param pretoken 变量定义前缀
     * @param endtoken 变量定义后缀
     * @param substitution
     * @return String[][]二维数组，第一维表示替换后的src，第二维表示匹配regex的所有的子串数组
     */
    public static String[][] parser2ndSubstitution(String inputString,String pretoken,String endtoken ,String substitution,int mask)
    {
        String regex = buildVariableRegex(pretoken,endtoken);  
        return RegexUtil.contain2ndReplaceWithPatternMatcherInput(inputString, regex, substitution,mask);
    }
    
    /**
     * 替换变量为制定的值
     * @param inputString
     * @param substitution
     * @return
     */
    public static String substitution(String inputString,String substitution)
    {
    	return SimpleStringUtil.replaceAll(inputString, default_regex, substitution);
        
    }
    
    /**
     * 替换变量为制定的值
     * @param inputString
     * @param substitution
     * @return
     */
    public static String substitution(String inputString,String regex,String substitution)
    {
    	return SimpleStringUtil.replaceAll(inputString, regex, substitution);
    }
    
    public static class URLStruction {
		protected List<String> tokens;
		protected List<Variable> variables;
		protected boolean hasVars;
		public URLStruction(){

		}
		public URLStruction(String url){
			this.url = url;
		}
		public String getUrl() {
			return url;
		}


		protected String url;
		public boolean hasVars()
		{
			return this.hasVars;
		}
		protected void after(){
			if(variables != null && variables.size() >= 0){
				this.hasVars = true;
//				for(Variable var:variables){
//					var.after();
//				}
			}
		}
		public List<String> getTokens() {
			return tokens;
		}

		public void setTokens(List<String> tokens) {
			this.tokens = tokens;
		}

		public List<Variable> getVariables() {
			return variables;
		}

		public void setVariables(List<Variable> variables) {
			this.variables = variables;

		}
		
		

	}
    
    public static class SQLStruction extends URLStruction{
    	private String sql;

    	public SQLStruction()
    	{
    		super();
    	}
    	public SQLStruction(String sql)
    	{
    		this.sql = sql;
    	}

		protected void after(){
			super.after();
			buildSQL();
		}
    	

    	public String buildSQL()
    	{
    		if(sql == null)
    		{
    			if(this.variables != null && variables.size() > 0)
    			{
    				
	    			StringBuilder sql_ = new StringBuilder();
	    			int tsize = this.tokens.size();
	    			int vsize = this.variables.size();
	    			if(tsize == vsize)
	    			{
		    			for(int i = 0; i < vsize; i ++)
		    			{
		    				sql_.append(tokens.get(i)).append("?");
		    			}
	    			}
	    			else //tsize = vsize + 1;
	    			{
	    				for(int i = 0; i < vsize; i ++)
		    			{	    					
		    				sql_.append(tokens.get(i)).append("?");
		    			}
	    				sql_.append(tokens.get(vsize));
	    			}
	    			this.sql = sql_.toString();
	    			
    			}
    			
    		}
    		return sql;
    	}
		public String getSql() {
			return sql;
		}
		public void setSql(String sql) {
			this.sql = sql;
		}
    	
	}

	public static class Variable {
		protected String defaultValue ;
		protected String originVariableName;
		protected String variableName;
		protected int position;
		protected List<Index> indexs;

		protected Variable parent;
		protected Variable next;
		protected String attributes;
		public String getVariableName() {
			return variableName;
		}

		public String getDefaultValue() {
			return defaultValue;
		}

		public String getOriginVariableName() {
			return originVariableName;
		}

		public void setAttributes(String attributes){
			this.attributes = attributes;
		}
		public String getAttributes(){
			return this.attributes;
		}

		public void setVariableName(String variableName) {
			this.variableName = variableName;

		}

		public int getPosition() {
			return position;
		}

		public void setPosition(int position) {
			this.position = position;
		}

		public List<Index> getIndexs() {
			return indexs;
		}

		public void setIndexs(List<Index> indexs) {
			this.indexs = indexs;
		}

		public Variable getParent() {
			return parent;
		}

		public void setParent(Variable parent) {
			this.parent = parent;
		}

		public Variable getNext() {
			return next;
		}

		public void setNext(Variable next) {
			this.next = next;
		}
		
		public String toString()
		{
			StringBuilder ret = new StringBuilder();
			ret.append(this.variableName);
			if(this.indexs != null && this.indexs.size() >0)
			{
				for(Index idx :indexs)
				{
					ret.append("[").append(idx.toString()).append("]");
				}
			}
			if(next != null)
			{
				ret.append("->").append(next.toString());
			}
			if(this.parent == null)
				ret.append(",position=").append(this.position);
			return ret.toString();
		}

		/**
		 * 变量解析完毕后，对变量定义信息进行额外处理
		 */
		public void after(){
			this.originVariableName = variableName;
		}
		

		
	}
	
	public static class Index
	{
//		private Object index;
		private int int_idx = -1;
		private String string_idx;
		public Index(int index) {
			super();
			this.int_idx = index;
			
		}
		public Index(String index) {
			super();
			this.string_idx = index;
			
		}
		
		public int getInt_idx() {
			return int_idx;
		}
		public String getString_idx() {
			return string_idx;
		}
		public String toString()
		{
			if(int_idx != -1)
				return int_idx + "";
			else
				return string_idx;
		}
	}
	
	
    
    /**
     * 将包含变量的url路径解析成常量字符串列表和变量名称两个列表
     * 变量的分界符为#[和],如果url中没有包含变量那么返回null值
     * @param url
     * @return
     */
    public static URLStruction parserURLStruction(String url) {
		if(url == null || url.trim().length() == 0)
			return null;
		int len = url.length();
		int i = 0;
		StringBuilder token = new StringBuilder();
		StringBuilder var = new StringBuilder();
		boolean varstart = false;
		int varstartposition = -1;

		List<Variable> variables = new ArrayList<Variable>();
		int varcount = 0;
		List<String> tokens = new ArrayList<String>();
		while (i < len) {
			if (url.charAt(i) == '#') {
				if(i + 1 < len)
				{
					if( url.charAt(i + 1) == '[')
					{
				
						if (varstart) {
							token.append("#[").append(var);
							var.setLength(0);
						}
		
						varstart = true;
		
						i = i + 2;
		
						varstartposition = i;
						var.setLength(0);
						continue;
					}
					
				}
				
			}

			if (varstart) {
				if (url.charAt(i) == '&') {
					varstart = false;
					i++;
					token.append("#[").append(var);
					var.setLength(0);
					continue;
				} else if (url.charAt(i) == ']') {
					if (i == varstartposition) {
						varstart = false;
						i++;
						token.append("#[]");
						continue;
					} else {
						Variable variable = new Variable();
						variable.setPosition(varcount);
						variable.setVariableName(var.toString());
						variables.add(variable);
						tokens.add(token.toString());
						token.setLength(0);
						var.setLength(0);
						varcount++;
						varstart = false;
						i++;
					}
				} else {
					var.append(url.charAt(i));
					i ++;
				}

			} else {
				token.append(url.charAt(i));
				i ++;
			}
		}
		if (token.length() > 0) {
			if (var.length() > 0) {
				token.append("#[").append(var);
			}
			tokens.add(token.toString());
		} else {
			if (var.length() > 0) {
//				token.append("#[").append(var);
//				tokens.add(token.toString());
				token.append("#[").append(var);
				int idx = tokens.size() - 1;
				tokens.set(idx,tokens.get(idx)+token.toString());
			}

		}

		if (variables.size() == 0)
			return null;
		else {
			URLStruction itemUrlStruction = new URLStruction();
			itemUrlStruction.setTokens(tokens);
			itemUrlStruction.setVariables(variables);
			return itemUrlStruction;
		}

	}
	static SQLStructionBuiler  sqlStructionBuiler = new SQLStructionBuiler ();
	static URLStructionBuiler urlStructionBuiler = new URLStructionBuiler();
	static DefaultvalueVariableStructionBuiler defaultvalueVariableStructionBuiler = new DefaultvalueVariableStructionBuiler();
	/**
	 * 将包含变量的sql语句解析成常量字符串列表和变量名称两个列表
	 * 变量的分界符为#[和],如果url中没有包含变量那么返回null值
	 * 变量数组、list、map的元素取值采用[]结合数字下标和key名称
	 * 变量引用采用->连接符
	 * @param sql
	 * @return
	 */
	public static URLStruction parserTempateStruction(String sql){
		return VariableHandler._parserStruction(sql,urlStructionBuiler);
	}

	/**
	 * asdfasdf#[aaaaa:0]fasdfaasdf
	 * @param sql
	 * @return
	 */
	public static URLStruction parserDefaultValueVariableStruction(String sql){
		return VariableHandler._parserStruction(sql,defaultvalueVariableStructionBuiler);
	}

	/**
	 * 将包含变量的sql语句解析成常量字符串列表和变量名称两个列表
	 * 变量的分界符为#[和],如果url中没有包含变量那么返回null值
	 * 变量数组、list、map的元素取值采用[]结合数字下标和key名称
	 * 变量引用采用->连接符
	 * @param sql
	 * @return
	 */
	public static SQLStruction parserSQLStruction(String sql) {
		return (SQLStruction)VariableHandler._parserStruction(sql,sqlStructionBuiler);
	}
	public static abstract interface StructionBuiler{
		abstract URLStruction buildStruction();
		abstract URLStruction buildStruction(String token);
		abstract Variable buildVariable();
	}
	static class SQLStructionBuiler implements StructionBuiler{

		@Override
		public URLStruction buildStruction() {
			return new SQLStruction();
		}

		@Override
		public URLStruction buildStruction(String token) {
			return  new SQLStruction(token);
		}

		@Override
		public Variable buildVariable() {
			return new Variable();
		}

	}

	public static class URLStructionBuiler implements StructionBuiler{

		@Override
		public URLStruction buildStruction() {
			return new URLStruction();
		}
		@Override
		public URLStruction buildStruction(String token) {
			return  new URLStruction(token);
		}
		@Override
		public Variable buildVariable() {
			return new Variable();
		}
	}
	/**
	 * 将包含变量的sql语句解析成常量字符串列表和变量名称两个列表
	 * 变量的分界符为#[和],如果url中没有包含变量那么返回null值
	 * 变量数组、list、map的元素取值采用[]结合数字下标和key名称
	 * 变量引用采用->连接符
	 * @param sql
	 * @return
	 */
	public static URLStruction parserStruction(String sql,StructionBuiler structionBuiler){
		return _parserStruction(  sql,  structionBuiler);
	}
	/**
     * 将包含变量的sql语句解析成常量字符串列表和变量名称两个列表
     * 变量的分界符为#[和],如果url中没有包含变量那么返回null值
     * 变量数组、list、map的元素取值采用[]结合数字下标和key名称
     * 变量引用采用->连接符
     * @param sql
     * @return
     */
    public static URLStruction _parserStruction(String sql,StructionBuiler structionBuiler) {
		if(sql == null || sql.trim().length() == 0)
			return null;
		int len = sql.length();
		int i = 0;
		StringBuilder token = new StringBuilder();
		StringBuilder var = new StringBuilder();
//		StringBuilder index = new StringBuilder();
		
		boolean varstart = false;
		int varstartposition = -1;//记录变量的开始位置
		//集合索引开始
		boolean index_start = false;

		//变量属性定义开始
		boolean attr_start = false;
		Variable header = null;
		Variable hh = null;
		Variable variable = null;
		List<Index> indexs = null;
		/**
		 * 对象属性引用位置开始
		 */
		boolean ref_start = false;

		List<Variable> variables = new ArrayList<Variable>();
		int varcount = 0;
		List<String> tokens = new ArrayList<String>();
		while (i < len) {
			if (sql.charAt(i) == '#') {
				if(i + 1 < len)
				{
					if( sql.charAt(i + 1) == '[')
					{
						if(attr_start ){
							 throw new VariableParserException(sql +",非法属性变量定义语法at position["+ i +"]：属性中不允许出现#[组合串!");
						}
						if (varstart) {//fixed me
							String partvar = sql.substring(varstartposition,i);
//							token.append("#[").append(var);							
							token.append("#[").append(partvar);
							var.setLength(0);
						}
						index_start = false;
						varstart = true;
						attr_start = false;
						variable = null;
						header = null;
						hh = null;
						indexs = null;
						/**
						 * 对象属性引用位置开始
						 */
						ref_start = false;
						i = i + 2;
		
						varstartposition = i;
						var.setLength(0);
						continue;
					}
					
				}
				
			}

			if (varstart) {
				if (sql.charAt(i) == '[') {
					
					if(!ref_start)
					{				
						if(!index_start)
						{
							header = structionBuiler.buildVariable();
							header.setPosition(varcount);
							header.setVariableName(var.toString());
//							variables.add(header);
							var.setLength(0);
							tokens.add(token.toString());
							token.setLength(0);
							varcount++;
							index_start = true;
							indexs = new ArrayList<Index>();
							header.setIndexs(indexs);
							header.after();
							hh = header;
						}
						else
						{
							//]
						}
						
					}
					else
					{
						if(!index_start)
						{
							variable = structionBuiler.buildVariable();
							//variable.setPosition(varcount);
							variable.setVariableName(var.toString());
							var.setLength(0);
							header.setNext(variable);
							variable.setParent(header);
							header = variable;
							index_start = true;
							indexs = new ArrayList<Index>();
							header.setIndexs(indexs);
							header.after();
						}
					}
					i++;
//					token.append("#[").append(var);
					var.setLength(0);
					continue;
				} else if (sql.charAt(i) == ']') {
					if (i == varstartposition) {
						varstart = false;
						i++;
						token.append("#[]");
						continue;
					} else {
						if(index_start)
						{
						
							String t = var.toString();
							try{
								int idx = Integer.parseInt(t);
								indexs.add(new Index(idx));
							}
							catch(Exception e)
							{
								indexs.add(new Index(t));
							}
							var.setLength(0);
							if(i + 1 < len)
							{
								if(sql.charAt(i + 1) == ']' || sql.charAt(i + 1) == ',')
								{
									index_start = false;
									indexs = null;
								}
							}
							i++;
							
						}
						else if(ref_start)//引用结束，变量定义结束a->b[0]
						{
							ref_start = false;
							varstart = false;
							if(variable == null)
							{
								variable = structionBuiler.buildVariable();
								//variable.setPosition(varcount);
								variable.setVariableName(var.toString());
								var.setLength(0);
								header.setNext(variable);
								variable.setParent(header);
								header = variable;
								header.after();
							}
							else
							{
								if(var.length() > 0)
								{
									variable = structionBuiler.buildVariable();
									//variable.setPosition(varcount);
									variable.setVariableName(var.toString());
									var.setLength(0);
									indexs = null;
									header.setNext(variable);
									variable.setParent(header);
									header = variable;
									header.after();
								}
							}
							variables.add(hh);	
							hh = null;
							i++;
						}
						else if(attr_start){
							if(var.length() > 0){
								if(hh == null){
									throw new VariableParserException(sql +",非法变量定义语法["+(i - var.length()) +","+var.toString()+"]");
								}
								else{
									hh.setAttributes(var.toString());
									hh.after();
									var.setLength(0);
									attr_start = false;
									varstart = false;
									variables.add(hh);
									hh = null;
									i++;
								}
							}
							else{//没有属性，自然结束
								attr_start = false;
								i++;
							}
						}
						else //if(varstart)
						{
							if(header == null)
							{
								header = structionBuiler.buildVariable();
								header.setPosition(varcount);
								header.setVariableName(var.toString());
//								variables.add(header);								
								var.setLength(0);
								tokens.add(token.toString());
								token.setLength(0);
								varcount++;
								hh = header;
								header.after();
							}							
							varstart = false;
							variables.add(hh);	
							hh = null;
							i++;
						}

					}
				}
				else if (sql.charAt(i) == '-')
				{
					if(i + 1 < len )
					{
						if(sql.charAt(i+1) == '>')
						{
//							if(varstart)
							{
								if(!ref_start)
								{
									ref_start = true;
									if(index_start)
									{
										index_start = false;
										indexs = null;
										var.setLength(0);
									}
									else 
									{
										if(header == null)
										{
											header = structionBuiler.buildVariable();
											header.setPosition(varcount);
											header.setVariableName(var.toString());
		//									variables.add(header);
											var.setLength(0);
											//fixed
											tokens.add(token.toString());
											token.setLength(0);
											varcount++;
											hh = header;
											header.after();
										}
										else
										{
											if(var.length() > 0)
											{
												variable = structionBuiler.buildVariable();
												//variable.setPosition(varcount);
												variable.setVariableName(var.toString());
												var.setLength(0);
												indexs = null;
												header.setNext(variable);
												variable.setParent(header);
												header = variable;
												header.after();
											}
										}
									}
								}
								else
								{
									if(variable == null)//没有因为索引下标导致引用对象已经创建，则开始创建对象
									{
										variable = structionBuiler.buildVariable();
										//variable.setPosition(varcount);
										variable.setVariableName(var.toString());
										var.setLength(0);
										indexs = null;
										header.setNext(variable);
										variable.setParent(header);
										header = variable;
										header.after();
									}
									else
									{
										if(var.length() > 0)
										{
											variable = structionBuiler.buildVariable();
											//variable.setPosition(varcount);
											variable.setVariableName(var.toString());
											var.setLength(0);
											indexs = null;
											header.setNext(variable);
											variable.setParent(header);
											header = variable;
											header.after();
										}
									}
								}
								index_start = false;
								indexs = null;
							}
//							else
//							{
//								token.append("->");
//							}
							i++;
							i++;
							continue;
						}
						else
						{
							var.append(sql.charAt(i)); 
							i ++;
						}
					}
				}
				else if(sql.charAt(i) == ','){
					if(attr_start) {
						var.append(sql.charAt(i));
					}
					else{
						if(index_start){
							throw new VariableParserException(sql +",非法变量定义语法at position["+ i +"],index中不允许出现逗号!");
						}
						else if(ref_start)
						{
							ref_start = false;
							if(variable == null)
							{
								variable = structionBuiler.buildVariable();
								//variable.setPosition(varcount);
								variable.setVariableName(var.toString());
								var.setLength(0);
								header.setNext(variable);
								variable.setParent(header);
								header = variable;
								header.after();
							}
							else
							{
								if(var.length() > 0)
								{
									variable = structionBuiler.buildVariable();
									//variable.setPosition(varcount);
									variable.setVariableName(var.toString());
									var.setLength(0);
									indexs = null;
									header.setNext(variable);
									variable.setParent(header);
									header = variable;
									header.after();
								}
							}
//							variables.add(hh);
//							hh = null;
//							i++;
						}
						else{
							if(header == null && var.length() > 0)
							{
								header = structionBuiler.buildVariable();
								header.setPosition(varcount);
								header.setVariableName(var.toString());
								//									variables.add(header);
								var.setLength(0);
								//fixed
								tokens.add(token.toString());
								token.setLength(0);
								varcount++;
								hh = header;
								header.after();
							}
							//只有引用的场景下head才不会为null
//							else
//							{
//								if(var.length() > 0)
//								{
//									variable = structionBuiler.buildVariable();
//									//variable.setPosition(varcount);
//									variable.setVariableName(var.toString());
//									var.setLength(0);
//									indexs = null;
//									header.setNext(variable);
//									variable.setParent(header);
//									header = variable;
//									header.after();
//								}
//							}
						}
						attr_start = true;
					}
					i ++;
				}
				else {
					char c = sql.charAt(i);
					if(attr_start ){
						if(c == '#' && sql.charAt(i+1) == '[')
							throw new VariableParserException(sql +",非法属性变量定义语法at position["+ i +"]：属性中不允许出现#[组合串!");
					}
					var.append(c );
					i ++;
				}

			} else {
				token.append(sql.charAt(i));
				i ++;
			}
		}
		/**
		 * 容错处理：
		 * 情况1.变量没有完全结束(需要反转header对象)
		 * 情况2.后面的字符串没有变量
		 * a.完全没有变量的相关关键字
		 * b.有部分变量定义，但是不全
		 * 
		 */
		if(attr_start ){
			throw new VariableParserException(sql +",非法变量定义语法：变量属性定义没有结束!");
		}
		if (token.length() > 0) {//情况2.后面的字符串没有变量
			if (var.length() > 0) {// b.有部分变量定义，但是不全，从变量开始的位置恢复token
				String partvar = sql.substring(varstartposition);
				token.append("#[").append(partvar);
//				token.append("#[").append(var);
			}
			tokens.add(token.toString());
		} 
		
		else {
			if (var.length() > 0) {//情况1.变量没有完全结束，从变量开始的位置恢复token
//				token.append("#[").append(var);
				String partvar = sql.substring(varstartposition);
				token.append("#[").append(partvar);
				int idx = tokens.size() - 1;
				tokens.set(idx, tokens.get(idx) + token.toString());
//				tokens.add(token.toString());
			}

		}

		if (variables.size() == 0)
		{
			return structionBuiler.buildStruction(sql);
		}
		else {
			URLStruction itemUrlStruction = structionBuiler.buildStruction();
			itemUrlStruction.setTokens(tokens);
			itemUrlStruction.setVariables(variables);
			itemUrlStruction.after();
			return itemUrlStruction;
		}

	}
    
    /**
     * 根据引用的维度获取其对应的Pro对象
     * @param var
	 * @param bean
     * @return
     */
    public static Object evaluateVariableValue(Variable var,Object bean)
    {	
    	if(bean == null)
    		return null;

    	Object temp = null;
    	PropertieDescription pro = null;
    	List<Index> indexs = null;
    	//if(var.getNext() == null )//不存在引用
    	
		indexs = var.getIndexs() ;
		if(indexs == null || indexs.size() == 0)//直接返回当前数据
		{
			if(var.getNext() == null)
				return bean;
			else
			{
				temp = bean;
				var = var.getNext();
			}
		}
		else//获取数组/map/list或者任意组合中的元素数据
		{
			Class tcls = null;
			temp = bean;
			for(Index idx:indexs)
			{
				if(idx.getInt_idx() != -1 )
					try {
						tcls = temp.getClass();
						if(tcls.isArray())
						{
							temp = Array.get(temp, idx.getInt_idx());
						}
						else 
//								if(List.class.isAssignableFrom(tcls))
						{
							temp = ((List)temp).get(idx.getInt_idx());
						}
//							else 
//							{
//								
//								temp = ((Set)temp).get(idx.getInt_idx());
//							}
					} catch (ArrayIndexOutOfBoundsException e) {
						throw new EvalVariableValueFailedException("获取数组元素失败：数组越界"+temp+"["+idx.getInt_idx()+"]",e);
					} catch (Throwable e) {
						throw new EvalVariableValueFailedException("获取数组元素失败："+temp+"["+idx.getInt_idx()+"]",e);
					}
				else
					try {
						temp = ((Map)temp).get(idx.getString_idx());
					} catch (Throwable e) {
						throw new EvalVariableValueFailedException("获取Map元素失败："+temp+"["+idx.getString_idx()+"]",e);
					}
				if(temp == null)
					break;
			}
			if(var.getNext() == null )
				return temp;
			else
				var = var.getNext();
		}
    	
root:  	do
    	{
			if(!(temp instanceof Map))
			{
				pro = ClassUtil.getPropertyDescriptor(temp.getClass(), var.getVariableName());
				if(pro == null)
					throw new EvalVariableValueFailedException("计算变量值失败：class["+bean.getClass().getCanonicalName()+"]没有定义属性["+var.getVariableName()+"]");
				try {
					temp = pro.getValue(temp);
				} catch (EvalVariableValueFailedException e) {
					throw e;
				} catch (IllegalAccessException e) {
					throw new EvalVariableValueFailedException(e);
				} catch (InvocationTargetException e) {
					throw new EvalVariableValueFailedException(e.getTargetException());
				}
				catch (Throwable e) {
					throw new EvalVariableValueFailedException(e);
				}
			}
			else
			{
				temp = ((Map)temp).get(var.getVariableName());
			}
			if(temp == null)
				break;
			indexs = var.getIndexs();
			if(indexs != null && indexs.size() > 0)
			{    			
				Class tcls = null;
				for(Index idx:indexs)
				{
					if(idx.getInt_idx() != -1 )
						try {
							tcls = temp.getClass();
							if(tcls.isArray())
							{
								temp = Array.get(temp, idx.getInt_idx());
							}
							else 
//									if(List.class.isAssignableFrom(tcls))
							{
								temp = ((List)temp).get(idx.getInt_idx());
							}
						} catch (ArrayIndexOutOfBoundsException e) {
							throw new EvalVariableValueFailedException("获取数组元素失败：数组越界"+temp+"["+idx.getInt_idx()+"]",e);
						} catch (Throwable e) {
							throw new EvalVariableValueFailedException("获取数组元素失败："+temp+"["+idx.getInt_idx()+"]",e);
						}
					else
						try {
							temp = ((Map)temp).get(idx.getString_idx());
						} catch (Throwable e) {
							throw new EvalVariableValueFailedException("获取Map元素失败："+temp+"["+idx.getString_idx()+"]",e);
						}
					if(temp == null)
						break root;
				}
			}
			var = var.getNext();
			if(var == null)
				break;
    	}while(true);
	        
        return temp;
    }    
    
    
}
