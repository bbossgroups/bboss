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

package org.frameworkset.spi.properties.injectbean;

import com.frameworkset.util.EditorInf;

/**
 * <p>Title: ProEditor.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2007</p>
 * @Date 2010-2-21 下午05:50:42
 * @author biaoping.yin
 * @version 1.0
 */
public class ProEditor implements EditorInf<String>
{

    /* (non-Javadoc)
     * @see com.frameworkset.util.EditorInf#getValue(java.lang.Object)
     */
    public String getValueFromObject(Object fromValue)
    {          
        if(fromValue == null)
            return "未知";
        if(fromValue.equals("0"))
            return "男";
        else if(fromValue.equals("1"))
            return "女";
        else 
            return "未知";
    }
    
    /* (non-Javadoc)
     * @see com.frameworkset.util.EditorInf#getValue(String)
     */
    public String getValueFromString(String fromValue)
    {   
        if(fromValue == null)
            return "未知";
        if(fromValue.equals("0"))
            return "男";
        else if(fromValue.equals("1"))
            return "女";
        else 
            return "未知";
    }
}
