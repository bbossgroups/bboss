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
package org.frameworkset.spi.assemble;

import java.lang.reflect.Method;
import java.util.List;

import org.frameworkset.spi.BaseApplicationContext;

/**
 * 
 * 
 * <p>Title: InterceptorInfo.java</p>
 *
 * <p>Description: 拦截器类配置信息</p>
 *
 * <p>Copyright: Copyright (c) 2007</p>
 *
 * <p>bboss workgroup</p>
 * @Date Sep 5, 2008 9:37:29 AM
 * @author biaoping.yin,尹标平
 * @version 1.0
 */
public class InterceptorInfo extends Pro{
	private AOPMethods interceptMethods;
	public InterceptorInfo() {
		super();
		interceptMethods = new AOPMethods();
	}

	public InterceptorInfo(BaseApplicationContext applicationContext) {
		super(applicationContext);
		interceptMethods = new AOPMethods();
	}
//	private String clazz;
//	private String re
//
//	public String getClazz() {
//		return clazz;
//	}
//
//	public void setClazz(String clazz) {
//		this.clazz = clazz;
//	}
	

  
    /**
     * 判断方法是否是需要进行事务控制,如果是则返回包含事务控制策略信息的方法对象， 不是就返回null
     * 
     * @param method
     * @return SynchronizedMethod 包含事务控制策略信息
     */
    public SynchronizedMethod isInterceptMethod(Method method,String muuid)
    {
        return interceptMethods.isTransactionMethod(method,muuid);
    }
    
    /**
     * 判断所有的方法都需要被拦截器拦截，如果为true，则所有的都需要被拦截，否则
     * 需要根据isInterceptMethod(Method method)来判别方法是否需要被拦截
     * @return
     */
    public boolean allWillBeIntercept()
    {
    	return this.interceptMethods.size() == 0;
    }

    public int size()
    {
        return this.interceptMethods.size();
    }

    public void addInterceptMethod(SynchronizedMethod method)
    {
        this.interceptMethods.addTransactionMethod(method);
    }
    
    

//    private boolean containTransactionMethod(SynchronizedMethod method)
//    {
//       return this.interceptMethods.containTransactionMethod(method);
//        
//    }

    public List<SynchronizedMethod> getInterceptMethods()
    {
        return this.interceptMethods.getTransactionMethods();
    }

    public void unmodify()
    {
    	super.unmodify();
        this.interceptMethods.unmodify();
//        this.fullTXMethodInfoIDX = java.util.Collections.unmodifiableMap(this.fullTXMethodInfoIDX);
        
    }

}
