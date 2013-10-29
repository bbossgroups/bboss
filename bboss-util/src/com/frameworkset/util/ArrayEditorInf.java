/**
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
package com.frameworkset.util;


/**
 * <p> ArrayEditorInf.java</p>
 * <p> Description: mvc框架参数绑定时，如果指定了类型为ArrayEditorInf的属性编辑器
 * 则要求mvc框架传入的参数为参数数组，否则只能传入单个值
 * 例如：
 * public class EditorExampleBean {
	@RequestParam(editor="org.frameworkset.mvc.Editor")
	private String[] name;

	public String[] getName() {
		return name;
	}

	public void setName(String[] name) {
		this.name = name;
	}

}
 * </p>
 * <p> bboss workgroup </p>
 * <p> Copyright (c) 2009 </p>
 * 
 * @Date 2012-11-21 上午11:48:40
 * @author biaoping.yin
 * @version 1.0
 */
public interface ArrayEditorInf<T> extends EditorInf<T> {	

}
