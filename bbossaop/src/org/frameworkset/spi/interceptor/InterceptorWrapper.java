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
package org.frameworkset.spi.interceptor;

import java.lang.reflect.Method;
import java.util.List;

import com.frameworkset.proxy.Interceptor;

/**
 * 
 * <p>Title: InterceptorWrapper.java</p> 
 * <p>Description: 按照拦截器的顺序执行所有的拦截器</p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2007</p>
 * @Date Dec 14, 2008 2:31:54 PM
 * @author biaoping.yin
 * @version 1.0
 */
public class InterceptorWrapper implements Interceptor {
	List interceptors;
	Interceptor txnterceptor;
	public InterceptorWrapper(List interceptors)
	{
		this.interceptors = interceptors;
	}
	

	public InterceptorWrapper(Interceptor txnterceptor, List interceptors) {
		this.txnterceptor = txnterceptor;
		this.interceptors = interceptors;
	}


	public void after(Method arg0, Object[] arg1) throws Throwable {
		
		for(int i = 0;  i < interceptors.size(); i ++)
		{
			Interceptor it = (Interceptor)interceptors.get(i);
			try
			{
				it.after(arg0, arg1);
			}
			catch(Exception e)
			{
				
			}
		}
		if(txnterceptor != null)
			txnterceptor.after(arg0, arg1);
	}

	public void afterFinally(Method arg0, Object[] arg1) throws Throwable {
		for(int i = 0;  i < interceptors.size(); i ++)
		{
			Interceptor it = (Interceptor)interceptors.get(i);
			try
			{
				it.afterFinally(arg0, arg1);
			}
			catch(Exception e)
			{
				
			}
		}
		if(txnterceptor != null)
			txnterceptor.afterFinally(arg0, arg1);

	}

//	public void afterThrowing(Method arg0, Object[] arg1) throws Throwable {
//		for(int i = 0;  i < interceptors.size(); i ++)
//		{
//			Interceptor it = (Interceptor)interceptors.get(i);
//			try
//			{
//				it.afterThrowing(arg0, arg1);
//			}
//			catch(Exception e)
//			{
//				
//			}
//		}
//		if(txnterceptor != null)
//			txnterceptor.afterThrowing(arg0, arg1);
//
//	}

	public void afterThrowing(Method arg0, Object[] arg1, Throwable arg2)
			throws Throwable {
		for(int i = 0;  i < interceptors.size(); i ++)
		{
			Interceptor it = (Interceptor)interceptors.get(i);
			try
			{
				it.afterThrowing(arg0, arg1,arg2);
			}
			catch(Exception e)
			{
				
			}
		}
		
		if(txnterceptor != null)
			txnterceptor.afterThrowing(arg0, arg1,arg2);

	}

	public void before(Method arg0, Object[] arg1) throws Throwable {
		for(int i = 0;  i < interceptors.size(); i ++)
		{
			Interceptor it = (Interceptor)interceptors.get(i);
			try
			{
				it.before(arg0, arg1);
			}
			catch(Exception e)
			{
				
			}
		}
		if(txnterceptor != null)
			txnterceptor.before(arg0, arg1);

	}

}
