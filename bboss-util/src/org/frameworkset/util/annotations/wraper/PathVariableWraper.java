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
import org.frameworkset.util.annotations.PathVariable;

/**
 * <p>PathVariableWraper.java</p>
 * <p> Description: </p>
 * <p> bboss workgroup </p>
 * <p> Copyright (c) 2005-2013 </p>
 * 
 * @Date 2013年10月3日
 * @author biaoping.yin
 * @version 1.0
 */
public class PathVariableWraper {
	private String value;
	private String editor;
	private String defaultvalue;
	/**
	 * 解码字符集
	 * @return
	 */
	private String decodeCharset;
	
	/**
	 * 参数原始字符集
	 * @return
	 */
	private String charset;
	
	/**
	 * 参数转换字符集
	 * @return
	 */
	private String convertcharset;
	/**
	 * 指定日期格式
	 * @return
	 */
	private String dateformat;
	public PathVariableWraper(PathVariable pv) {
		 value = pv.value();
		 editor = pv.editor();
		 defaultvalue = AnnotationUtils.converDefaultValue(pv.defaultvalue());
		/**
		 * 解码字符集
		 * @return
		 */
		 decodeCharset = AnnotationUtils.converDefaultValue(pv.decodeCharset());
		
		/**
		 * 参数原始字符集
		 * @return
		 */
		 charset = AnnotationUtils.converDefaultValue(pv.charset());
		
		/**
		 * 参数转换字符集
		 * @return
		 */
		 convertcharset = AnnotationUtils.converDefaultValue(pv.convertcharset());
		/**
		 * 指定日期格式
		 * @return
		 */
		 dateformat = AnnotationUtils.converDefaultValue(pv.dateformat());
	}
	
	public String value(){
		return this.value;
	}
	public String editor(){
		return this.editor;
	}
	public String defaultvalue(){
		return this.defaultvalue;
	}
	/**
	 * 解码字符集
	 * @return
	 */
	public String decodeCharset(){
		return this.decodeCharset;
	}
	
	/**
	 * 参数原始字符集
	 * @return
	 */
	public String charset(){
		return this.charset;
	}
	
	/**
	 * 参数转换字符集
	 * @return
	 */
	public String convertcharset(){
		return this.convertcharset;
	}
	/**
	 * 指定日期格式
	 * @return
	 */
	public String dateformat(){
		return this.dateformat;
	}

}
