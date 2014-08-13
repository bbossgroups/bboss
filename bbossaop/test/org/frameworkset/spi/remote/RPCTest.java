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

package org.frameworkset.spi.remote;

import org.frameworkset.spi.remote.ConcurrentTest.O;
import org.frameworkset.spi.remote.context.RequestContext;
import org.frameworkset.spi.security.SecurityContext;


/**
 * <p>Title: MinaRPCTest.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2007</p>
 * @Date 2009-10-17 下午05:11:41
 * @author biaoping.yin
 * @version 1.0
 */
public class RPCTest implements RPCTestInf
{
	int ret = 100;
	public Object getCount()
	{
	    SecurityContext securityContext = SecurityContext.getSecurityContext();	    
	    String value = RequestContext.getRequestContext().getStringParameter("server_uuid");
		System.out.println("receve server_uuid:"+value);
		
		return ret ++;
	}
    /* (non-Javadoc)
     * @see org.frameworkset.spi.remote.RPCTestInf#sayHello()
     */
    public void sayHello(String name)
    {
        System.out.println("Hello " + name);
        
    }
    
    public String sayHelloWorld(String name)
    {
        return name;
        
    }
    public void throwexcpetion() throws Exception
    {
    	throw new TestRPCException("throwexcpetion:invoker.");
    }
	public byte[] getBytes(String name) {
		// TODO Auto-generated method stub
		return name.getBytes();
	}
	public String getString(String name) {
		// TODO Auto-generated method stub
		return name;
	}
	
	public O getO()
	{
//		try {
//			
//			synchronized (this) {
//				Thread.currentThread().sleep(2000);
//			}
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		return new O("ooooooooOoo哦哦");
	}
	
	public Object getParameter()
	{
		String value = RequestContext.getRequestContext().getStringParameter("server_uuid");
		System.out.println("ret value:" + value);
		return value + ":aa";
	}
	
	public byte[] echo18M(byte[] datas)
	{
		return datas;
	}
}
