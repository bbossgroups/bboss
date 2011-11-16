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

import com.frameworkset.proxy.Interceptor;

/**
 * 
 * 
 * <p>Title: InterceptorChain.java</p>
 *
 * <p>Description: À¹½ØÆ÷Ö´ÐÐË³ÐòÁ´</p>
 *
 * <p>Copyright: Copyright (c) 2007</p>
 *
 * <p>bboss workgroup</p>
 * @Date Aug 11, 2008 3:51:49 PM
 * @author biaoping.yin,Òü±êÆ½
 * @version 1.0
 */
public class InterceptorChain implements Interceptor {
	
	protected Interceptor next;
	protected Interceptor current;
	protected boolean istx = false;

	public InterceptorChain(Interceptor current,Interceptor next)
	{
		this(current,next,false);
	}
	public InterceptorChain(Interceptor current,Interceptor next,boolean istx)
	{
		this.current = current;
		this.next = next;
		this.istx = istx;
	}

	public void after(Method method, Object[] args) throws Throwable {
		
		try
		{
			
			this.next.after(method, args);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				this.current.after(method, args);
			}
			catch(Exception e)
			{
				if(this.istx)
					throw e;
				e.printStackTrace();
			}
		}

	}

	public void afterFinally(Method method, Object[] args) throws Throwable {
		
		try
		{
			this.next.afterFinally(method, args);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			
		}
		finally
		{
			try
			{
				this.current.afterFinally(method, args);
			}
			catch(Exception e)
			{
				if(this.istx )
					throw e;
				
				e.printStackTrace();
				
			}
			
		}

	}

	public void afterThrowing(Method method, Object[] args, Throwable throwable) throws Throwable {
		
		try
		{
			this.next.afterThrowing(method, args, throwable);
		}
		catch(Exception e)
		{
			
			e.printStackTrace();
		}
		finally
		{
			
			try
			{
				this.current.afterThrowing(method, args, throwable);
			}
			catch(Exception e)
			{
				if(this.istx)
					throw e;
				e.printStackTrace();
			}
			
			
		}
		

	}
	
	public void afterThrowing(Method method, Object[] args) throws Throwable {
		
		try
		{
			this.next.afterThrowing(method, args, null);
		}
		catch(Exception e)
		{
			
			e.printStackTrace();
		}
		finally
		{
			
			try
			{
				this.current.afterThrowing(method, args, null);
			}
			catch(Exception e)
			{
				if(this.istx)
					throw e;
				e.printStackTrace();
			}
			
			
		}
		

	}

	public void before(Method method, Object[] args) throws Throwable {
		
		try
		{
			this.current.before(method, args);
		}
		catch(Exception e)
		{
			if(istx)
				throw e;
			e.printStackTrace();
		}
		finally
		{
			try
			{
				this.next.before(method, args);
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}		
			
		}
	}
	
	
	

}
