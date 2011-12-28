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

import org.frameworkset.spi.ApplicationContext;
import org.frameworkset.spi.BaseSPIManager;
import org.frameworkset.spi.SPIException;

public class TestInterceptor {
	ApplicationContext context = ApplicationContext.getApplicationContext("org/frameworkset/spi/interceptor/simplemanager-interceptor.xml"); 
    @org.junit.Test
	public void testInterceptor() throws Exception
	{
		try {
			AI a = (AI)BaseSPIManager.getProvider("interceptor.a");
			try {
				a.testInterceptorsBeforeAfter();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				a.testInterceptorsBeforeThrowing();
			} catch (Exception e) {
			    
				throw e;
			}
		} catch (SPIException e) {
			throw e;
		}
	}
    
    @org.junit.Test
	public void testSimpleInterceptor() throws Exception
	{
		try {
			AI a = (AI)context.getBeanObject("serviceName");
			try {
				a.testInterceptorsBeforeAfter();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				a.testInterceptorsBeforeThrowing();
			} catch (Exception e) {
			    
				throw e;
			}
		} catch (SPIException e) {
			throw e;
		}
	}
	
	public static void main(String[] args)
	{

	}
	
	

}
