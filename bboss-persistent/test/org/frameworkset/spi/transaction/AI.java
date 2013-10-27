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
package org.frameworkset.spi.transaction;

public interface AI {
	
	public void testTXInvoke(String msg)throws Exception;
	
	public void testTXInvoke() throws Exception;
	public void testNoTXInvoke()throws Exception;
	
	public String testTXInvokeWithReturn()throws Exception;
	
	public String testTXInvokeWithException() throws Exception;
	
	public void testSameName()throws Exception;
	public void testSameName(String msg)throws Exception;
	
	public void testSameName1()throws Exception;
	public void testSameName1(String msg)throws Exception;
	/**
	 * 混合异常测试，即包含实例异常，也包含子类和实例异常
	 * 所有的异常都将导致事务回滚
	 */
	public void testTXWithSpecialExceptions(String type) throws Exception;
	/**
	 * 只要是特定实例的异常就会回滚
	 * @param type
	 * @throws Exception
	 */
	public void testTXWithInstanceofExceptions(String type) throws Exception;
	/**
	 * 只有异常本身的实例异常才触发事务的回滚
	 * @param type
	 * @throws Exception
	 */
	public void testTXWithImplementsofExceptions(String type) throws Exception;
	
	public void testPatternTX1(String type) throws Exception;
	
	public void testPatternTX2(String type) throws Exception;
	
	public void testPatternTX3(String type) throws Exception;
	public void testPatternTX4(String type) throws Exception;
	public void testSystemException() throws Exception;
}
