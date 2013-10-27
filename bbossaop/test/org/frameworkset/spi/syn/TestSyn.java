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

import org.frameworkset.spi.BaseSPIManager;
import org.frameworkset.spi.SPIException;
import org.junit.Test;


public class TestSyn {
	/**
	 * 在默认的提供者上面执行所有的同步方法
	 */
	@Test
	public void testSynmethod()
	{
		try {
			AI a = (AI)BaseSPIManager.getProvider("syn.a");
			//测试不带参数和带参数的同名同步方法，同时在默认db和ldap两个提供者上面执行
			a.testSynInvoke();
			a.testSynInvoke("hello word.");
			
			//测试非同步方法，只在默认的提供者上面执行本方法
			a.testNoSynInvoke();
			
			
			
			//测试场景：
			//接口中定义了两个testSameName方法，一个带参数（不需要同步调用），一个不带参数（需要同步调用）
			a.testSameName();
			a.testSameName("hello word.");
			
			//测试场景：
			//接口中定义了两个testSameName1方法，一个带参数（需要同步调用），一个不带参数（不需要同步调用）
			a.testSameName1();
			a.testSameName1("hello word.");
			
			//测试带返回值的同步方法，同时在默认db和ldap两个提供者上面执行，
			//但只返回默认提供者方法的返回值
			System.out.println(a.testSynInvokeWithReturn());
			a.testInt(111);
			a.testInt(22);
			a.testInt(33);
			a.testInt(44);
			
			//测试抛出异常并且有返回值的同步方法，同时在默认db和ldap两个提供者上面执行，不管是否有异常，所有的方法都会
			//执行一遍，默认提供者的方法抛出异常，而ldap类型的提供者执行正确
			System.out.println(a.testSynInvokeWithException());
		
		} catch (SPIException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void testSameMethod()
	{
		try {
			AI a = (AI)BaseSPIManager.getProvider("syn.a");
			
			
			
			//测试场景：
			//接口中定义了两个testSameName方法，一个带参数（不需要同步调用），一个不带参数（需要同步调用）
			a.testSameName();
			a.testSameName("hello word.");
			
			//测试场景：
			//接口中定义了两个testSameName1方法，一个带参数（需要同步调用），一个不带参数（不需要同步调用）
			a.testSameName1();
			a.testSameName1("hello word.");
			
			
		
		} catch (SPIException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void testSynmethodWithType()
	{
		try {
			AI a = (AI)BaseSPIManager.getProvider("syn.a","ldap");
			//测试不带参数和带参数的同名同步方法，同时在默认db和ldap两个提供者上面执行
			a.testSynInvoke();
			a.testSynInvoke("hello word.");
			
			//测试非同步方法，只在ldap提供者上面执行本方法
			a.testNoSynInvoke();
			
			//测试场景：
			//接口中定义了两个testSameName方法，一个带参数（不需要同步调用），一个不带参数（需要同步调用）
			a.testSameName();
			a.testSameName("hello word.");
			
			//测试场景：
			//接口中定义了两个testSameName1方法，一个带参数（需要同步调用），一个不带参数（不需要同步调用）
			a.testSameName1();
			a.testSameName1("hello word.");
			
			//测试带返回值的同步方法，同时在默认db和ldap两个提供者上面执行，
			//但只返回ldap提供者方法的返回值
			System.out.println(a.testSynInvokeWithReturn());
			
			//测试抛出异常并且有返回值的同步方法，同时在默认db和ldap两个提供者上面执行，不管是否有异常，所有的方法都会
			//执行一遍，默认提供者的方法抛出异常，而ldap类型的提供者执行正确
			System.out.println(a.testSynInvokeWithException());
			a.testInt(22);
		} catch (SPIException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	public static void testSameMethodWithType()
	{
		try {
			AI a = (AI)BaseSPIManager.getProvider("syn.a","ldap");
			
			
			//测试场景：
			//接口中定义了两个testSameName方法，一个带参数（不需要同步调用），一个不带参数（需要同步调用）
			a.testSameName();
			a.testSameName("hello word.");
			
			//测试场景：
			//接口中定义了两个testSameName1方法，一个带参数（需要同步调用），一个不带参数（不需要同步调用）
			a.testSameName1();
			a.testSameName1("hello word.");
			
			
		} catch (SPIException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void testIntWithType()
	{
		try {
			AI a = (AI)BaseSPIManager.getProvider("syn.a","ldap");
			
			
			
			System.out.println(a.testInt(10));
			System.out.println(a.testIntNoSyn(11));
			
			
			
		} catch (SPIException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void testInt()
	{
		try {
			AI a = (AI)BaseSPIManager.getProvider("syn.a");
			System.out.println(a.testInt(10));
			System.out.println(a.testIntNoSyn(11));
		} catch (SPIException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
	
	
	
	public static void main(String[] args)
	{
//		testSynmethod();
//		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
//		testSynmethodWithType();
//		testSameMethod();
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
//		testSameMethodWithType();
		testInt();
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		testIntWithType();
	}
	
}
