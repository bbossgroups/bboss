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
package org.frameworkset.spi.reference;

import org.frameworkset.spi.BaseSPIManager;
import org.frameworkset.spi.SPIException;

/**
 * 
 * 
 * <p>Title: TestReference.java</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2007</p>
 *
 * <p>bboss workgroup</p>
 * @Date Aug 19, 2008 10:27:28 AM
 * @author biaoping.yin,尹标平
 * @version 1.0
 */
public class TestReference {
	
	/**
	 * 测试本方法时，需要将D对a的引用配置注释，避免重复引用情况发生
	 */
	/**
	 * @org.junit.Test
	 */
	 @org.junit.Test
	public  void testReference()
	{
		
		try {
			AI a = (AI)BaseSPIManager.getProvider("a");
			System.out.println("a:" + a);
			System.out.println("a:" +a.getB());
			System.out.println("a.getB().getC():" +a.getB().getC());
			System.out.println("a.getB().getC().getD():" +a.getB().getC().getD());
			System.out.println("a.getB().getC().getInt_i():" +a.getB().getC().getInt_i());
			System.out.println("a.getB().getC().getD().getA():" +a.getB().getC().getD().getA());
		} catch (SPIException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * @org.junit.Test
	 */
	public static void testReferenceClass()
	{
		try {
			AI a = (AI)BaseSPIManager.getProvider("clazz");
			
//			System.out.println("a:" + a);
//			System.out.println("a:" +a.getB());
//			System.out.println("a.getB().getC():" +a.getB().getC());
//			System.out.println("a.getB().getC().getD():" +a.getB().getC().getD());
//			System.out.println("a.getB().getC().getInt_i():" +a.getB().getC().getInt_i());
//			System.out.println("a.getB().getC().getD().getA():" +a.getB().getC().getD().getA());
		} catch (SPIException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 测试本方法时，需要将D对a的引用配置注释去掉，重复引用情况发生
	 */
	/**
	 * @org.junit.Test
	 */
	public static void testLoopReference()
	{
		try {
			DI d = (DI)BaseSPIManager.getProvider("d");
		} catch (SPIException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void main(String[] args)
	{
//		testReference();
		testLoopReference();
		testReferenceClass();
	}
	
	
	

}
