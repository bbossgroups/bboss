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

import org.frameworkset.spi.constructor.ConstructorInf;

/**
 * 
 * <p>Title: A.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2007</p>
 * @Date Nov 9, 2008 7:57:14 PM
 * @author biaoping.yin
 * @version 1.0
 */
public class A implements AI{

	public void testInterceptorsBeforeAfter() throws Exception {
//		System.out.println("testInterceptorsBeforeAfter()");
	}

	public void testInterceptorsBeforeThrowing() throws Exception {
//		System.out.println("testInterceptorsBeforeThrowing()");
		throw new Exception("testInterceptorsBeforeThrowing");
		
	}

	public void testInterceptorsBeforeThrowingWithTX() throws Exception {
		
//		System.out.println("testInterceptorsBeforeThrowingWithTX()");
		throw new Exception("testInterceptorsBeforeThrowingWithTX");
	}

	public void testInterceptorsBeforeafterWithTX() throws Exception {
//		System.out.println("testInterceptorsBeforeafterWithTX()");
	}

	public void setConst(ConstructorInf inf) {
		// TODO Auto-generated method stub
		
	}

	
}
