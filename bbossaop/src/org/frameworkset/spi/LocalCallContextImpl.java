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

import org.frameworkset.spi.assemble.Context;

/**
 * <p>Title: LocalCallContext.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2007</p>
 * @Date 2014年8月14日 下午12:08:35
 * @author biaoping.yin
 * @version 1.0
 */
public class LocalCallContextImpl implements CallContext {

	public LocalCallContextImpl() {
		// TODO Auto-generated constructor stub
	}
	private boolean isSOAApplication = false;
	private int  containerType = BaseApplicationContext.container_type_simple;
	private transient BaseApplicationContext applicationContext;
	private String  applicationContextPath ;
	
   
    /**
     * 服务调用循环依赖注入上下文
     */
    private transient Context loopcontext;
    /**
     * 应用模块上下文
     * @param applicationContext
     */
    public LocalCallContextImpl(BaseApplicationContext applicationContext)
    {
    	
        this.applicationContext = applicationContext;
        this.isSOAApplication = this.applicationContext instanceof SOAApplicationContext;
    }
    
    public LocalCallContextImpl(String applicationContext,int containerType)
    {
    	
        this.applicationContextPath = applicationContext;
        this.containerType = containerType;
        
    }
    
   
    public Context getLoopContext()
    {
        return loopcontext;
    }
    public void setLoopContext(Context loopcontext)
    {
        this.loopcontext = loopcontext;
    }
    
   
    public BaseApplicationContext getApplicationContext()
    {
    	if(applicationContext != null)
    	{
    		return applicationContext;
    	}
    	
    	return applicationContext = BaseApplicationContext.getBaseApplicationContext(this.applicationContextPath,this.containerType);
    	
    }
    
	
	public boolean isSOAApplication()
	{
	
		return isSOAApplication;
	}
}
