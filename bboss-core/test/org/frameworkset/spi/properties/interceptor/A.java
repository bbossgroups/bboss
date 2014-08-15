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

package org.frameworkset.spi.properties.interceptor;


/**
 * <p>Title: A.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2007</p>
 * @Date 2010-1-28 下午10:21:08
 * @author biaoping.yin
 * @version 1.0
 */
public class A implements AI
{

	public void test(String msg)
	{

		System.out.println("A.test:" + msg);
		
	}
	
	public void notest(String msg)
	{

		System.out.println("A.notest:" + msg);
		
	}

}
