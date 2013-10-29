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
package org.frameworkset.spi.syn;

public class A2 implements AI{

	public void testSynInvoke(String msg) {
		System.out.println("A2:" + msg);
		
	}
	
	public void testSynInvoke() {
		System.out.println("A2:no param");
		
	}
	public void testNoSynInvoke()
	{
		System.out.println("A2:NoSynInvoke");
	}

	public String testSynInvokeWithReturn() {
		System.out.println("call A2.testSynInvokeWithReturn()");
		return "return is A2";
	}

	public String testSynInvokeWithException() throws Exception {
		System.out.println("call A2.testSynInvokeWithException()");
		return "A2 successed";
	}

	public void testSameName() {
		System.out.println("call A2.testSameName()");
		
	}

	public void testSameName(String msg) {
		System.out.println("call A2.testSameName(String msg):" + msg);
		
	}

	public void testSameName1() {
		System.out.println("call A2.testSameName1()");
		
	}

	public void testSameName1(String msg) {
		System.out.println("call A2.testSameName1(String msg):" + msg);
		
	}
	
	public int testInt(int i) {
		System.out.println("call A2.testInt(int i)：" + i);
		return i;		
	}
	
	public int testIntNoSyn(int i) {
		System.out.println("call A2.testIntNoSyn(int i)：" + i);
		return i;		
	}

}
