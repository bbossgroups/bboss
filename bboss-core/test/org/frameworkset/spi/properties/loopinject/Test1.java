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
package org.frameworkset.spi.properties.loopinject;

import java.util.List;
import java.util.Map;

/**
 * <p>Title: Test1.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2008</p>
 * @Date 2011-12-29
 * @author biaoping.yin
 * @version 1.0
 */
public class Test1 implements java.io.Serializable{
	Test2 test2;
	Test3 test3;
	Test1 test4;
	Test1 test5;
	Test2 test6;
	Test2 test7;
	Test2 test8;
	Test1 test9;
	Test1 test10;
	Test1 test11;
	Test2 test12;
	Map testmap;
	List testlist;
	Test1[] testarray;
	String xmlvalue ;
	Test1 innerelement;
	Test1[] testarraybasic;

	public Test2 getTest2() {
		return test2;
	}
	public void setTest2(Test2 test2) {
		this.test2 = test2;
	}
	public Test3 getTest3() {
		return test3;
	}
	public void setTest3(Test3 test3) {
		this.test3 = test3;
	}
	public Test1 getTest4() {
		return test4;
	}
	public void setTest4(Test1 test4) {
		this.test4 = test4;
	}
	public Map getTestmap() {
		return testmap;
	}
	public void setTestmap(Map testmap) {
		this.testmap = testmap;
	}
	public List getTestlist() {
		return testlist;
	}
	public void setTestlist(List testlist) {
		this.testlist = testlist;
	}
	public Test1[] getTestarray() {
		return testarray;
	}
	public void setTestarray(Test1[] testarray) {
		this.testarray = testarray;
	}
	public Test1 getTest5() {
		return test5;
	}
	public void setTest5(Test1 test5) {
		this.test5 = test5;
	}
	public String getXmlvalue() {
		return xmlvalue;
	}
	public void setXmlvalue(String xmlvalue) {
		this.xmlvalue = xmlvalue;
	}
	

}
