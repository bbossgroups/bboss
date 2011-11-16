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

import org.apache.oro.text.regex.Perl5Compiler;

/**
 * <p>Title: TestVaribleHandler.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2008</p>
 * @Date 2010-3-10 ÏÂÎç02:31:03
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
}
