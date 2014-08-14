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

package org.frameworkset.spi;

import org.frameworkset.spi.remote.Headers;
import org.frameworkset.spi.security.SecurityContext;

/**
 * <p>Title: RemoteCallContextImpl.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2007</p>
 * @Date 2014年8月14日 下午12:46:28
 * @author biaoping.yin
 * @version 1.0
 */
public class RemoteCallContextImpl extends LocalCallContextImpl implements RemoteCallContext {

	public RemoteCallContextImpl() {
		// TODO Auto-generated constructor stub
	}

	
	
    /**
     * 服务调用安全上下文
     */
    private SecurityContext secutiryContext; 
    /**
     * 服务调用的消息头属性集
     */
    private Headers headers;
   
    /**
     * 应用模块上下文
     * @param applicationContext
     */
    public RemoteCallContextImpl(BaseApplicationContext applicationContext)
    {
    	super(applicationContext);
        
    }
    
    public RemoteCallContextImpl(String applicationContext,int containerType)
    {
    	
    	super(applicationContext,containerType);
        
    }
    
    public SecurityContext getSecutiryContext()
    {
        return secutiryContext;
    }
    public void setSecutiryContext(SecurityContext secutiryContext)
    {
        this.secutiryContext = secutiryContext;
    }
    public Headers getHeaders()
    {
        return headers;
    }
    public void setHeaders(Headers headers)
    {
        this.headers = headers;
    }
   
    
    public boolean containHeaders()
    {
        return this.headers != null && this.headers.size() > 0;
    }
   

}
