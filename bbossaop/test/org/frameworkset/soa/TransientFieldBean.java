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
package org.frameworkset.soa;

import org.frameworkset.soa.annotation.ExcludeField;

/**
 * <p>Title: TransientFieldBean.java</p> 
 * <p>Description:用来测试</p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2008</p>
 * @Date 2012-1-4
 * @author biaoping.yin
 * @version 1.0
 */
public class TransientFieldBean {
	private transient String transientField;//被忽略
	private static String staticFiled;//静态，被忽略
	private static final String finalField = "123456"; //常量，被忽略
	private String onlyField ;//没有set/get方法，通过构造函数初始化，能被序列化
	@ExcludeField
	private String excludeField = null;//有get/set方法，但是有@ExcludeField注解，所以被忽略，不被序列化
	public TransientFieldBean()
	{
		
	}
	public TransientFieldBean(String onlyField)
	{
		this.onlyField = onlyField;
	}
	public  String getStaticFiled() {
		return staticFiled;
	}
	public void setStaticFiled(String staticFiled) {
		TransientFieldBean.staticFiled = staticFiled;
	}
	public String getTransientField() {
		return transientField;
	}
	public void setTransientField(String transientField) {
		this.transientField = transientField;
	}
	public String getExcludeField() {
		return excludeField;
	}
	public void setExcludeField(String excludeField) {
		this.excludeField = excludeField;
	}

}
