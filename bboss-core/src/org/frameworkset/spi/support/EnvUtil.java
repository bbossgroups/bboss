package org.frameworkset.spi.support;
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

import com.frameworkset.util.VariableHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * <p>Description: 解析属性值中的环境变量工具类</p>
 * <p></p>
 * <p>Copyright (c) 2018</p>
 * @Date 2019/7/17 10:00
 * @author biaoping.yin
 * @version 1.0
 */
public abstract class EnvUtil {
	private static Logger log = LoggerFactory.getLogger(EnvUtil.class);

	/**
	 * 解析属性值中的环境变量
	 * @param properties
	 * @return
	 */
	public static Map evalEnvVariable(Map properties){

		if(!properties.isEmpty()) {
			Map allProperties = new HashMap(properties.size());
					Iterator<Map.Entry<Object, Object>> temp = properties.entrySet().iterator();
			StringBuilder builder = new StringBuilder();
			while(temp.hasNext()) {
				Map.Entry<Object, Object> entry = temp.next();
				String key = (String)entry.getKey();
				try {
					EnvUtil.getSystemEnv(builder, null,key, null, properties);
					allProperties.put(key, builder.toString());
					builder.setLength(0);
				}
				catch (Throwable e){
					if(log.isWarnEnabled()){
						log.warn("",e);
					}
				}

			}
			return allProperties;
		}
		return null;
	}

	/**
	 * 解析属性值中的环境变量
	 * @param properties
	 * @return
	 */
	public static Map evalEnvVariableForObjectContainer(Map properties){

		if(!properties.isEmpty()) {
			Map allProperties = new HashMap(properties.size());
			Iterator<Map.Entry<Object, Object>> temp = properties.entrySet().iterator();
			StringBuilder builder = new StringBuilder();
			while(temp.hasNext()) {
				Map.Entry<Object, Object> entry = temp.next();
				String key = (String)entry.getKey();
				Object value = entry.getValue();
				if(value instanceof String) {
					try {
						EnvUtil.getSystemEnv(builder, null,key, null, properties);
						allProperties.put(key, builder.toString());
						builder.setLength(0);
					} catch (Throwable e) {
						if (log.isWarnEnabled()) {
							log.warn("", e);
						}
					}
				}else{
					allProperties.put(key, value);
				}

			}
			return allProperties;
		}
		return null;
	}
	/**
	 * 首先从配置文件中查找属性值，然后从jvm系统熟悉和系统环境变量中查找属性值
	 * @param property
	 * @return
	 */
	public static void getSystemEnv(StringBuilder propertiesValue, VariableHandler.Variable variable ,String property, String parentName, Map properties)
	{
		Object value = properties.get( property);

		if(value != null){
			String value_ = String.valueOf(value);
			VariableHandler.URLStruction urlStruction = VariableHandler.parserDefaultValueVariableStruction(value_);
			if(urlStruction == null){
				propertiesValue.append( value_);
			}
			else {
				if (!urlStruction.hasVars()) {
					propertiesValue.append(value_);
				} else {
					evalStruction(propertiesValue, urlStruction, properties, property);
				}
			}

		}
		else{ //Get value from jvm system propeties,just like -Dproperty=value
//			Properties pros = System.getProperties();
			String value_ =System.getProperty(property);
			if(value_ == null) {
				//Get value from os env ,just like property=value in user profile
				value_ = System.getenv(property);

			}
			if(value_ == null) {
				if( variable != null ){
					value_ = variable.getDefaultValue();
				}

			}

			if(value_ != null){
				propertiesValue.append(value_);
			}
			else {

				if (parentName == null) {
					if(variable == null) {
						throw new IllegalArgumentException("Eval property " + property + " value failed:not set variable value in config file or system environment.");
					}
					else{
						throw new IllegalArgumentException("Eval property " + property + " value failed:not set variable value in config file or system environment. variable is "+variable.getOriginVariableName());
					}
				}
				else {
					if(log.isWarnEnabled()) {
						if(variable == null) {
							log.warn("Eval property " + property + " for " + parentName + " value failed:not set variable value in config file or system environment.");
						}
						else{
							log.warn("Eval property " + property + " for " + parentName + " value failed:not set variable value in config file or system environment. variable is "+variable.getOriginVariableName());
						}

					}
					propertiesValue.append("#[").append(property).append("]");
				}
			}
		}
	}

	private static void evalStruction(StringBuilder builder,VariableHandler.URLStruction templateStruction,Map properties,String parentName){
		List<String> tokens = templateStruction.getTokens();
		List<VariableHandler.Variable> variables = templateStruction.getVariables();
		for(int i = 0; i < tokens.size(); i ++){
			builder.append(tokens.get(i));
			if(i < variables.size()) {
				VariableHandler.Variable variable = variables.get(i);
				if(parentName.equals(variable.getVariableName())){
					throw new IllegalArgumentException("Eval property " + variable.getOriginVariableName() + " for " + parentName + " value failed:loop reference ocour." );
				}
				getSystemEnv(builder,variable, variable.getVariableName(), parentName, properties);


			}

		}

	}

}
