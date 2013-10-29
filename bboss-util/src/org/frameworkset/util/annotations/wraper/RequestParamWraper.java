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
import org.frameworkset.util.annotations.RequestParam;

/**
 * <p>RequestParamWraper.java</p>
 * <p> Description: </p>
 * <p> bboss workgroup </p>
 * <p> Copyright (c) 2005-2013 </p>
 * 
 * @Date 2013年10月3日
 * @author biaoping.yin
 * @version 1.0
 */
public class RequestParamWraper {
	/**
	 * 参数名称，默认为""
	 * @return
	 */
	private String name;
	
	private boolean required;
	private String editor;
	/**
     * 数据格式
     */
	private String dataformat;
	/**
	 * 日期格式
     */
	private String dateformat;
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
	public RequestParamWraper(RequestParam param) {
		/**
		 * 参数名称，默认为""
		 * @return
		 */
		 name = param.name();
		
		 required = param.required();
		 editor = param.editor();
		/**
	     * 数据格式
	     */
		 dataformat = param.dataformat();
		/**
		 * 日期格式
	     */
		 dateformat = param.dateformat();
		 defaultvalue = AnnotationUtils.converDefaultValue(param.defaultvalue());
		/**
		 * 解码字符集
		 * @return
		 */
		 decodeCharset = AnnotationUtils.converDefaultValue(param.decodeCharset());
		
		/**
		 * 参数原始字符集
		 * @return
		 */
		 charset = AnnotationUtils.converDefaultValue(param.charset());
		
		/**
		 * 参数转换字符集
		 * @return
		 */
		 convertcharset = AnnotationUtils.converDefaultValue(param.convertcharset());
	}
	/**
	 * 参数名称，默认为""
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
	/**
     * 数据格式
     */
	public String dataformat(){
		return this.dataformat;
	}
	/**
	 * 日期格式
     */
	public String dateformat(){
		return this.dateformat;
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

}
