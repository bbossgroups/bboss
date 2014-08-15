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

package org.frameworkset.spi.serviceid;

import org.frameworkset.spi.remote.restful.RestfulServiceConvertor;

/**
 * <p>Title: RestfulServiceConvertorImplTest.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2007</p>
 * @Date 2010-3-5 下午04:41:30
 * @author biaoping.yin
 * @version 1.0
 */
public class RestfulServiceConvertorImplTest implements RestfulServiceConvertor
{

    public String convert(String restfuluddi, String serviceid)
    {
    	System.out.println("restfuluddi:" + restfuluddi);
        if(restfuluddi.equals("a"))
        {
            String uri = "192.168.1.102:12345";
            String user = "admin";
            String password = "123456";
            String protocol = "netty";
            String returl = "(" + protocol + "::" +  uri + ")/" + serviceid + "?user=" + user + "&password=" + password;
            
            return returl;
        }
        else if(restfuluddi.equals("b"))
        {
            String uri = "192.168.1.102:12347";
            String user = "admin";
            String password = "1234567";
            String protocol = "netty";
            String returl = "(" + protocol + "::" +  uri + ")/" + serviceid + "?user=" + user + "&password=" + password;
            
            return returl;
        }
        else if(restfuluddi.equals("c"))
        {
            String uri = "172.16.17.216:1187";
            String user = "admin";
            String password = "123456";
            String protocol = "mina";
            String returl = "(" + protocol + "::" +  uri + ")/" + serviceid + "?user=" + user + "&password=" + password;
            
            return returl;
        }
        
        else if(restfuluddi.equals("d"))
        {
            String uri = "172.16.17.216:1187";
            String user = "admin";
            String password = "123456";
            String protocol = "mina";
            String returl = "(" + protocol + "::" +  uri + ")/" + serviceid + "?user=" + user + "&password=" + password;
            
            return returl;
        }
        else 
        {
            String uri = "172.16.17.216:1187";
            String user = "admin";
            String password = "123456";
            String protocol = "jgroup";
            String returl = "(" + protocol + "::" +  uri + ")/" + serviceid + "?user=" + user + "&password=" + password;
            
            return returl;
        }
            
    }

}
