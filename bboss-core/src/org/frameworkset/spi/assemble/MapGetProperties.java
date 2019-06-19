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

import java.util.Map;

public class MapGetProperties implements GetProperties{
	private Map<String,String> values;
	public MapGetProperties(Map<String,String> values){
		this.values = values;
	}
	public String getExternalProperty(String property){
		return values.get(property);
	}
	public String getExternalProperty(String property, String defaultValue){
		String value = values.get(property);
		if(value != null)
			return value;
		else
			return defaultValue;
	}
}
