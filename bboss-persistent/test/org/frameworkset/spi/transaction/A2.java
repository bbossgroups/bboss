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

public class A2 implements AI{

	public void testTXInvoke(String msg) {
		System.out.println("A2:" + msg);
		
	}
	
	public void testTXInvoke() {
		System.out.println("A2:no param");
		
	}
	public void testNoTXInvoke()
	{
		System.out.println("A2:NoTXInvoke");
	}

	public String testTXInvokeWithReturn() {
		System.out.println("call A2.testTXInvokeWithReturn()");
		return "return is A2";
	}

	public String testTXInvokeWithException() throws Exception {
		System.out.println("call A2.testTXInvokeWithException()");
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
	
	public void testTXWithSpecialExceptions(String type) throws Exception
	{
		
	}

	public void testTXWithImplementsofExceptions(String type) throws Exception {
		// TODO Auto-generated method stub
		
	}

	public void testTXWithInstanceofExceptions(String type) throws Exception {
		// TODO Auto-generated method stub
		
	}

	public void testPatternTX1(String type) throws Exception {

		
	}

	public void testPatternTX2(String type) throws Exception {

		
	}

	public void testPatternTX3(String type) throws Exception {

		
	}

	public void testPatternTX4(String type) throws Exception {
		// TODO Auto-generated method stub
		
	}

	public void testSystemException() throws Exception {
		// TODO Auto-generated method stub
		
	}
	
	

}
