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

package org.frameworkset.spi.assemble;

import org.junit.Test;

/**
 * <p>Title: TestRefIDUtil.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2007</p>
 * @Date 2012-1-2 下午10:42:20
 * @author biaoping.yin
 * @version 1.0
 */
public class TestRefIDUtil {
	@Test
	public void test()
	{
		String addr = "test1->test2";
		RefID refid = RefIDUtil.parsedRefID(addr);
		
		addr = "test1->test2->test3";
		refid = RefIDUtil.parsedRefID(addr);
		System.out.println();
		
	}
	
	@Test
	public void test1()
	{
		String addr = "test1[0]->test2[0][key]";
		RefID refid = RefIDUtil.parsedRefID(addr);
		System.out.println();
		
		addr = "test1[0][qq]->test2[0][key][1]";
		 refid = RefIDUtil.parsedRefID(addr);
		System.out.println();
		
	}
	
	@Test
	public void test3()
	{
		String addr = "test1";
		RefID refid = RefIDUtil.parsedRefID(addr);
		System.out.println();
		
		addr = "test1[0][qq]->test2[0][key][1]";
		 refid = RefIDUtil.parsedRefID(addr);
		System.out.println();
		
	}
	
	@Test
	public void test4()
	{
		String addr = "test1{0}";
		RefID refid = RefIDUtil.parsedRefID(addr);
		System.out.println();
		
		addr = "test1[0][qq]->test2[0]{2}[2]";
		 refid = RefIDUtil.parsedRefID(addr);
		System.out.println();
		
	}

}
