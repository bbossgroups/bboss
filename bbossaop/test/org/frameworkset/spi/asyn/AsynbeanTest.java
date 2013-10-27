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

package org.frameworkset.spi.asyn;

import org.frameworkset.spi.async.annotation.Async;
import org.frameworkset.spi.async.annotation.Result;

/**
 * <p>Title: AsynbeanTest.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2007</p>
 * @Date 2011-4-20 下午05:18:54
 * @author biaoping.yin
 * @version 1.0
 */
public class AsynbeanTest {
	@Async
	public String testHelloworld(String message) {
		
		System.out.println(message);
		return "testHelloworld:"+message;
		
	}
	
	/**
	 * 5秒超时，但是不返回结果，也不指定回调函数（这种模式没有实际意义，只是在调用的时候超过5秒
	 * 后给出超时异常）
	 * @param message
	 * @return
	 */
	@Async(timeout=5000)
	public String testHelloworld0(String message) {
		
		System.out.println(message);
		return "testHelloworld:"+message;
		
	}
	
	/**
	 * 需要返回结果，等5秒超时
	 * @param message
	 */
	@Async(timeout=5000,result=Result.YES)
	public String testHelloworld1(String message) {
		
		System.out.println(message);
		return "testHelloworld1:"+message;
		
	}
	@Async(timeout=5000,result=Result.YES,callback="asyn.AsynbeanCallBackTest")
	public String testHelloworld2(String message) {
		
		System.out.println(message);
		return "testHelloworld2:"+message;
		
	}
	
	
	@Async(result=Result.YES)
	public String testHelloworld3(String message) {
		
		System.out.println(message);
		return "testHelloworld3:"+message;
	}
	
	
	@Async(result=Result.YES,callback="asyn.AsynbeanCallBackTest")
	public String testHelloworld4(String message) {
		
		System.out.println(message);
		return "testHelloworld4:"+message;
		
	}
	
	@Async(result=Result.YES,callback="asyn.AsynbeanCallBackTest")
	public String testHelloworldException(String message) throws Exception {
		
		System.out.println(message);
		throw new Exception(message);
		
	}

}
