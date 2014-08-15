/*
 *  Copyright 2008-2010 biaoping.yin
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
package org.frameworkset.soa;

/**
 * <p>Title: ArrayBean.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2008</p>
 * @Date 2011-5-14 ÉÏÎç11:31:32
 * @author biaoping.yin
 * @version 1.0
 */
public class ArrayBean {
	private byte[] arrays;
	private Exception e;

	/**
	 * @return the arrays
	 */
	public byte[] getArrays() {
		return arrays;
	}

	/**
	 * @param arrays the arrays to set
	 */
	public void setArrays(byte[] arrays) {
		this.arrays = arrays;
	}

	/**
	 * @return the e
	 */
	public Exception getE() {
		return e;
	}

	/**
	 * @param e the e to set
	 */
	public void setE(Exception e) {
		this.e = e;
	}
	

}
