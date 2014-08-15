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

package org.frameworkset.spi.remote.webservice;

import org.apache.cxf.transports.http.configuration.ProxyServerType;

import com.frameworkset.util.EditorInf;

/**
 * <p>Title: ProxyServerTypeEditor.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2007</p>
 * @Date 2009-12-7 ÉÏÎç11:39:25
 * @author biaoping.yin
 * @version 1.0
 */
public class ProxyServerTypeEditor implements EditorInf
{

    public Object getValueFromObject(Object fromValue)
    {
        
        return getValueFromString((String)fromValue);
    }

    public Object getValueFromString(String fromValue)
    {
        if(fromValue == null) 
        {
            return ProxyServerType.HTTP;
        }
        else if(fromValue.equals("HTTP"))
        {
            return ProxyServerType.HTTP; 
        }
        else if(fromValue.equals("SOCKS"))
        {
            return ProxyServerType.SOCKS; 
        }
        else 
        {
            throw new java.lang.IllegalArgumentException(); 
        }
    }

}
