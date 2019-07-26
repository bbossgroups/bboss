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
import org.frameworkset.util.annotations.RequestHeader;

import java.util.Locale;

/**
 * <p>RequestHeaderWraper.java</p>
 * <p> Description: </p>
 * <p> bboss workgroup </p>
 * <p> Copyright (c) 2005-2013 </p>
 * 
 * @Date 2013年10月3日
 * @author biaoping.yin
 * @version 1.0
 */
public class RequestHeaderWraper extends BaseWraper {
	/**
	 * The name of the request header to bind to.
	 */
	private String name;

	/**
	 * Whether the header is required.
	 * <p>Default is <code>true</code>, leading to an exception thrown in case
	 * of the header missing in the request. Switch this to <code>false</code>
	 * if you prefer a <code>null</value> in case of the header missing.
	 * <p>Alternatively, provide a {@link #defaultvalue() defaultValue},
	 * which implicitely sets this flag to <code>false</code>.
	 */
	private  boolean required;



	private   String  editor;

	private   String dateformat;
	private Locale locale;
	public RequestHeaderWraper(RequestHeader header,Class paramType) {
		super(paramType);
		convertValue(  header.defaultvalue());
		/**
		 * The name of the request header to bind to.
		 */
		 name = header.name();

		/**
		 * Whether the header is required.
		 * <p>Default is <code>true</code>, leading to an exception thrown in case
		 * of the header missing in the request. Switch this to <code>false</code>
		 * if you prefer a <code>null</value> in case of the header missing.
		 * <p>Alternatively, provide a {@link #defaultValue() defaultValue},
		 * which implicitely sets this flag to <code>false</code>.
		 */
		 required = header.required();



		  editor = header.editor();

		dateformat = AnnotationUtils.converDefaultValue(header.dateformat());
		if(BaseSimpleStringUtil.isNotEmpty(header.locale() ))
		 {
			 try
			 {
				 locale = new Locale(header.locale());
			 }
			 catch(Exception e)
			 {
				 
			 }
		 }
	}
	/**
	 * The name of the request header to bind to.
	 */
	public String name(){
		return this.name;
	}

	/**
	 * Whether the header is required.
	 * <p>Default is <code>true</code>, leading to an exception thrown in case
	 * of the header missing in the request. Switch this to <code>false</code>
	 * if you prefer a <code>null</value> in case of the header missing.
	 * <p>Alternatively, provide a {@link #defaultvalue() defaultValue},
	 * which implicitely sets this flag to <code>false</code>.
	 */
	public boolean required(){
		return this.required;
	}


	public String  editor(){
		return this.editor;
	}

	public String dateformat(){
		return this.dateformat;
	}
	public Locale getLocale() {
		return locale;
	}

}
