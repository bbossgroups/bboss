package org.frameworkset.util.annotations.wraper;
/**
 * Copyright 2008 biaoping.yin
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import com.frameworkset.util.ValueObjectUtil;
import org.frameworkset.util.annotations.AnnotationUtils;

/**
 * <p>Description: </p>
 * <p></p>
 * <p>Copyright (c) 2018</p>
 * @Date 2018/11/6 20:47
 * @author biaoping.yin
 * @version 1.0
 */
public abstract class BaseWraper {
	protected Class paramType;
	protected boolean numeric;
	protected Class componentType;
	protected Object defaultvalue;
	protected BaseWraper(Class paramType){
		this.paramType = paramType;
		numeric = ValueObjectUtil.isNumeric(paramType);
		componentType = ValueObjectUtil.getComponentType(paramType);
	}
	public Class getParamType() {
		return paramType;
	}

	public boolean isNumeric() {
		return numeric;
	}

	public Object defaultvalue() {
		return defaultvalue;
	}

	protected void convertValue(String _defaultvalue){
		_defaultvalue = AnnotationUtils.converDefaultValue(_defaultvalue);
		if(_defaultvalue != null)
			defaultvalue = ValueObjectUtil.typeCast(_defaultvalue,this.componentType);
	}




}
