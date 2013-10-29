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

import com.frameworkset.orm.annotation.Column;

/**
 * <p>ColumnWraper.java</p>
 * <p> Description: </p>
 * <p> bboss workgroup </p>
 * <p> Copyright (c) 2005-2013 </p>
 * 
 * @Date 2013年10月3日
 * @author biaoping.yin
 * @version 1.0
 */
public class ColumnWraper {
	private String dataformat;
	private String name;
	private String type;
	private String charset;
	public ColumnWraper(Column column) {
		dataformat =  AnnotationUtils.converDefaultValue(column.dataformat());
		name = column.name();
		type =  AnnotationUtils.converDefaultValue(column.type());
		charset =  AnnotationUtils.converDefaultValue(column.charset());
	}
	public String dataformat(){
		return dataformat;
	}
	public String name() {
		return name;
	}
	public String type() {
		return type;
	}
	public String charset() {
		return charset;
	}

}
