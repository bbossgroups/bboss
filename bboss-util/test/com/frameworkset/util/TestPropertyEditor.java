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

/**
 * <p>Title: TestPropertyEditor.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2008</p>
 * @Date 2009-12-7 10:57:50
 * @author biaoping.yin
 * @version 1.0
 */
public class TestPropertyEditor
{
    public void test()
    {
        EditorInf editor = new ProEditor();
     
    }
    public static class ProEditor implements EditorInf<String>
    {

        /* (non-Javadoc)
         * @see com.frameworkset.util.EditorInf#getValue(java.lang.Object)
         */
        public String getValueFromObject(Object fromValue)
        {          
            if(fromValue == null)
                return "1";
            if(fromValue.equals("0"))
                return "0";
            else if(fromValue.equals("1"))
                return "1";
            else 
                return "2";
        }
        
        /* (non-Javadoc)
         * @see com.frameworkset.util.EditorInf#getValue(String)
         */
        public String getValueFromString(String fromValue)
        {   
            if(fromValue == null)
                return "1";
            if(fromValue.equals("0"))
                return "0";
            else if(fromValue.equals("1"))
                return "1";
            else 
                return "2";
        }
        
        
        
    }
}


