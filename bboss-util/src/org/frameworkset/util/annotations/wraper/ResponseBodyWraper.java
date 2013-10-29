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
import org.frameworkset.util.annotations.ResponseBody;

/**
 * <p>ResponseBodyWraper.java</p>
 * <p> Description: </p>
 * <p> bboss workgroup </p>
 * <p> Copyright (c) 2005-2013 </p>
 * 
 * @Date 2013年10月3日
 * @author biaoping.yin
 * @version 1.0
 */
public class ResponseBodyWraper {
	/**
	 * 指定响应的数据类型
	 * @return
	 */
	private String datatype;
	/**
	 * 指定响应的数据编码字符集
	 * @return
	 */
	private String charset;
	public ResponseBodyWraper(ResponseBody body) {
		datatype = AnnotationUtils.converDefaultValue(body.datatype());
		charset =AnnotationUtils.converDefaultValue( body.charset());
	}
	
	/**
	 * 指定响应的数据类型
	 * @return
	 */
	public String datatype(){
		return this.datatype;
	}
	/**
	 * 指定响应的数据编码字符集
	 * @return
	 */
	public String charset(){
		return this.charset;
	}
}
