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

/**
 * <p>Title: Test2.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2008</p>
 * @Date 2011-12-29
 * @author biaoping.yin
 * @version 1.0
 */
public class Test2  implements java.io.Serializable{
	Test1 test1;
	Test3 test3;

	public Test1 getTest1() {
		return test1;
	}

	public void setTest1(Test1 test1) {
		this.test1 = test1;
	}

	public Test3 getTest3() {
		return test3;
	}

	public void setTest3(Test3 test3) {
		this.test3 = test3;
	}
}
