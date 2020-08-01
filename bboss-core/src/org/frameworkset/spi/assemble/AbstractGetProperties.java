package org.frameworkset.spi.assemble;
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

/**
 * <p>Description: </p>
 * <p></p>
 * <p>Copyright (c) 2020</p>
 * @Date 2020/5/13 23:54
 * @author biaoping.yin
 * @version 1.0
 */
public  abstract class AbstractGetProperties implements GetProperties{

	public boolean getExternalBooleanProperty(String property, boolean defaultValue) {
		Object value = getExternalObjectProperty(  property,null);
		if(value == null)
			return defaultValue;
		if(value instanceof Boolean ){
			return ((Boolean)value).booleanValue();
		}
		else if(value instanceof String){
			return ((String)value).equals("true");

		}
		else{
			return true;
		}
	}
	/**
	 * 首先从配置文件中查找属性值，然后从jvm系统熟悉和系统环境变量中查找属性值
	 * @param property
	 * @return
	 */
	public String getSystemEnvProperty(String property)
	{
		String value = getExternalProperty(  property);

		if(value == null){ //Get value from jvm system propeties,just like -Dproperty=value
//			Properties pros = System.getProperties();
			value =System.getProperty(property);
			if(value == null) {
				//Get value from os env ,just like property=value in user profile
				value = System.getenv(property);
			}
		}
		return value;
	}
	@Override
	public String getExternalPropertyWithNS(String namespace, String property) {
		return getExternalProperty(  property);
	}

	@Override
	public String getExternalPropertyWithNS(String namespace, String property, String defaultValue) {
		return getExternalProperty(  property,   defaultValue);
	}

	@Override
	public Object getExternalObjectPropertyWithNS(String namespace, String property) {
		return getExternalObjectProperty(   property);
	}

	@Override
	public Object getExternalObjectPropertyWithNS(String namespace, String property, Object defaultValue) {
		return getExternalObjectProperty(   property,   defaultValue);
	}
}
