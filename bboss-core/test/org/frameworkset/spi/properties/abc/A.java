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

package org.frameworkset.spi.properties.abc;

/**
 * <p>Title: A.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2007</p>
 * @Date 2011-5-6 下午09:15:26
 * @author biaoping.yin
 * @version 1.0
 */
public class A {
	AA aa;
	B b;
	public AA getAa() {
		return aa;
	}
	public void setAa(AA aa) {
		this.aa = aa;
	}
	public B getB() {
		return b;
	}
	public void setB(B b) {
		this.b = b;
	}

}
