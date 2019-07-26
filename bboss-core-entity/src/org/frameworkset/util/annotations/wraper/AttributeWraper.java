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

import com.frameworkset.util.BaseSimpleStringUtil;
import org.frameworkset.util.annotations.AnnotationUtils;
import org.frameworkset.util.annotations.Attribute;
import org.frameworkset.util.annotations.AttributeScope;

import java.util.Locale;

/**
 * 
 * <p>AttributeWraper.java</p>
 * <p> Description: 对注解Attribute的封装类，将默认值和初始值转换到Wrapper类中</p>
 * <p> bboss workgroup </p>
 * <p> Copyright (c) 2005-2013 </p>
 * 
 * @Date 2013年9月30日
 * @author biaoping.yin
 * @version 1.0
 */
public class AttributeWraper extends BaseWraper{
	private String name;
	private boolean required;
	private String editor;
	private AttributeScope scope;

	/**
	 * 指定日期格式
	 * @return
	 */
	private String dateformat;
	private Locale locale;
	public AttributeWraper(Attribute attr,Class paramType) {
		super(paramType);
		convertValue(  attr.defaultvalue());
		required = attr.required();
		name = attr.name();
		this.editor = attr.editor();
		this.scope = attr.scope();
		this.dateformat =  AnnotationUtils.converDefaultValue(attr.dateformat());

		if(BaseSimpleStringUtil.isNotEmpty(attr.locale() ))
		 {
			 try
			 {
				 locale = new Locale(attr.locale());
			 }
			 catch(Exception e)
			 {
				 
			 }
		 }
//		name = attr。name();
//		required =  attr。required();
//		editor =  attr。editor() ;
//		scope =  attr。scope() ;
//		defaultvalue =  attr。defaultvalue();// default ValueConstants.DEFAULT_NONE;
//		/**
//		 * 指定日期格式
//		 * @return
//		 */
//		dateformat = attr。dateformat();// default ValueConstants.DEFAULT_NONE;
	}
	public String name() {
		return name;
	}
	public boolean required() {
		return required;
	}
	public String editor() {
		return editor;
	}
	public AttributeScope scope() {
		return scope;
	}

	public String dateformat() {
		return dateformat;
	}
	public Locale getLocale() {
		return locale;
	}

}
