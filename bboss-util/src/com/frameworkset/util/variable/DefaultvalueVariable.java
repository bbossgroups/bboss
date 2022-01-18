package com.frameworkset.util.variable;
/**
 * Copyright 2020 bboss
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


import com.frameworkset.util.VariableHandler;

/**
 * <p>Description: </p>
 * <p></p>
 * <p>Copyright (c) 2020</p>
 * @Date 2022/1/18 11:43
 * @author biaoping.yin
 * @version 1.0
 */
public class DefaultvalueVariable extends VariableHandler.Variable {

	/**
	 * 变量解析完毕后，对变量定义信息进行额外处理,识别默认值
	 */
	@Override
	public void after(){
		super.after();
		int idx = variableName.indexOf(":");
		if(idx >= 0){
			String defaultValue = this.variableName.substring(idx+1);
			String variableName = this.variableName.substring(0, idx).trim();
			this.variableName = variableName;
			this.defaultValue = defaultValue;
		}
	}


}
