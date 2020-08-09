package org.frameworkset.spi.assemble;/*
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

import com.frameworkset.util.ValueCastUtil;
import org.frameworkset.spi.support.EnvUtil;

import java.util.Map;

public class MapGetProperties extends AbstractGetProperties{

	private Map<String,Object> values;
	public MapGetProperties(Map<String,Object> values){
		//解析环境变量
		this.values = EnvUtil.evalEnvVariableForObjectContainer(values);
	}
	public String getProperty(String property){
		return getExternalProperty(property);
	}
	public boolean getBooleanProperty(String property,boolean defaultValue){

		Object value = values.get(property);
		if(value == null)
			return defaultValue;
		return ValueCastUtil.toBoolean(value,defaultValue);
	}

	@Override
	public void reset() {

	}

	public String getExternalProperty(String property){
		Object value = values.get(property);
		if(value == null)
			return null;

		return value.toString();
	}
	public String getExternalProperty(String property, String defaultValue){
		Object value = values.get(property);

		if(value != null)
			return value.toString();
		else
			return defaultValue;
	}

	public Object getExternalObjectProperty(String property){
		Object value = values.get(property);
		if(value == null)
			return null;

		return value;
	}
	public Object getExternalObjectProperty(String property, Object defaultValue){
		Object value = values.get(property);

		if(value != null)
			return value;
		else
			return defaultValue;
	}

	@Override
	public Map getAllExternalProperties() {
		return values;
	}
}
