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

package org.frameworkset.spi.beans.factory;

/**
 * <p>Title: TestFactoryBeanCreate.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2007</p>
 * @Date 2011-1-14 10:13:31
 * @author biaoping.yin
 * @version 1.0
 */
public class TestFactoryBeanCreate {
	private String factorydata1;
	private String factorydata2;
	public TestBean createNoArgs()
	{
		return new TestBean();
	}
	
	public TestBean createWithArgs(String name,int id)
	{
		return new TestBean( name,id);
	}
	
	
	public TestBean createNoArgsThrowException() throws Exception
	{
		throw new Exception("createNoArgsThrowException " );
	}
	
	public TestBean createWithArgsThrowException(String name,int id) throws Exception
	{
		throw new Exception("createWithArgsThrowException name:"+name+",id" + id );
	}
	

	public String getFactorydata1() {
		return factorydata1;
	}

	public void setFactorydata1(String factorydata1) {
		this.factorydata1 = factorydata1;
	}

	public String getFactorydata2() {
		return factorydata2;
	}

	public void setFactorydata2(String factorydata2) {
		this.factorydata2 = factorydata2;
	}
	

}
