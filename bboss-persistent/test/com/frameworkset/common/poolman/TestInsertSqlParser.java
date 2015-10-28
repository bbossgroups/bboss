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

import org.apache.oro.text.regex.MalformedPatternException;
import org.apache.oro.text.regex.MatchResult;
import org.apache.oro.text.regex.Pattern;
import org.apache.oro.text.regex.PatternCompiler;
import org.apache.oro.text.regex.PatternMatcher;
import org.apache.oro.text.regex.Perl5Compiler;
import org.apache.oro.text.regex.Perl5Matcher;

/**
 * @author biaoping.yin
 * created on 2005-3-31
 * version 1.0 
 */
public class TestInsertSqlParser {
    
    public static void main(String[] args) throws MalformedPatternException
    {
        String insert = "Insert  into  oa_meetingpromptsound ( soundCode , soundName , soundFileName ) values ( '。.尹标平','bb','d()d' )";
        //String pattern = "(insert) \\t*(into) \\s*([a-z0-9A-Z_\\-]+)\\s*\\([a-z0-9A-Z_\\-]+\\s*,\\s*[a-z0-9A-Z_\\-]+)";
        //String pattern = "(insert)\\s+(into)\\s+([a-z0-9A-Z_\\-]+)\\s*(\\([^\\)]\\))\\s+([^\\s])";
        String patternStr = "(insert)\\s+" + 		//解析insert关键词
        		         "(into)\\s+" +   			//解析into关键词
        		         "([^\\(]+)\\s*" + 			//解析表名称
        		         "(\\([^\\)]+\\))\\s+" +	//解析表字段        		         
        		         "(values)\\s*" + 			//解析value关键词        		         
        		         "(\\(.+)";					//解析字段值
        		         
        PatternCompiler compiler = new Perl5Compiler(); 
        Pattern pattern = compiler.compile(patternStr,Perl5Compiler.CASE_INSENSITIVE_MASK);
        PatternMatcher matcher = new Perl5Matcher();
        MatchResult result = null; 
        if(matcher.matches(insert.trim(),pattern))
        {
            result = matcher.getMatch();
            System.out.println(result.group(1));
            System.out.println(result.group(2));
            System.out.println(result.group(3));
            System.out.println(result.group(4));
            System.out.println(result.group(5));
            System.out.println(result.group(6));
            
        }
        
    }

}
