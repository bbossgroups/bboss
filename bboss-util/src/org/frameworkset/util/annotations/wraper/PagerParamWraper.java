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
package org.frameworkset.util.annotations.wraper;

import org.frameworkset.util.annotations.AnnotationUtils;
import org.frameworkset.util.annotations.PagerParam;
import org.frameworkset.util.annotations.ValueConstants;

/**
 * <p>PagerParamWraper.java</p>
 * <p> Description: </p>
 * <p> bboss workgroup </p>
 * <p> Copyright (c) 2005-2013 </p>
 * 
 * @Date 2013年10月3日
 * @author biaoping.yin
 * @version 1.0
 */
public class PagerParamWraper {
	private String id;
	/**
	 * 分页参数名称
	 * @return
	 */
	private String name ;	
	private boolean required;
	private String editor;
	private String defaultvalue;
	public PagerParamWraper(PagerParam param)
	{
		id = param.id();
		if(id.equals(ValueConstants.DEFAULT_NONE))
			id = null;
				/**
		 * 分页参数名称
		 * @return
		 */
		name = param.name();	
		required = param.required();
		editor = param.editor();
		defaultvalue = AnnotationUtils.converDefaultValue(param.defaultvalue());
	}
	
	public String id() {
		return id;
	}
	/**
	 * 分页参数名称
	 * @return
	 */
	public String name(){
		return this.name;
	}
	public boolean required(){
		return this.required;
	}
	
	public String editor(){
		return this.editor;
	}
	
	public String defaultvalue(){
		return this.defaultvalue;
	}
	

}
