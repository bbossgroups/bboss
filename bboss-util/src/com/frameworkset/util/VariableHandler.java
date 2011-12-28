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

import java.util.ArrayList;
import java.util.List;


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
        StringBuffer ret = new StringBuffer();
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
     * @param src
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
     * @param src
     * @param regex
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
		private List<String> tokens;
		private List<Variable> variables;

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

	public static class Variable {
		private String variableName;

		public String getVariableName() {
			return variableName;
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

		private int position;
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
		StringBuffer token = new StringBuffer();
		StringBuffer var = new StringBuffer();
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
				token.append("#[").append(var);
				tokens.add(token.toString());
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
    
    public static void main(String[] args)
    {
//        String pretoken = "\\{";
//        String endtoken = "\\}";
//        String url = "${context}/${context0}/${context0}creatorepp";
//        String regex = buildVariableRegex(pretoken,endtoken);
//        System.out.print(regex);
//        String[] vars = variableParser(url,pretoken,endtoken);
//        System.out.println(vars);
//        vars = variableParser(url,regex);
//        
//        System.out.println(vars);
        
        
        String url = "http://localhost:80/detail.html?user=#[account]&password=#[password]love";
		
		 url =
		 "http://localhost:80/detail.html?user=#[account&password=password]&love=#[account]";
		 url =
			 "http://localhost:80/detail.html?user=#[account&password=password]&love=#[account";
//		 
		 url =
			 "http://localhost:80/detail.html?user=#[account&password=#[password&love=#[account";
//		 url =
//			 "http://localhost:80/detail.html";
//		 
//		 url =
//			 "http://localhost:80/#[]detail.html";
//		 url =
//			 "#[account";
		 System.out.println("url:"+url);
		// Item item = new Item();
		 URLStruction a = parserURLStruction(url);
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
    
    
}
