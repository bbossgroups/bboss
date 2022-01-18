package org.frameworkset.runtime;
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
 * @Date 2022/1/17 11:09
 * @author biaoping.yin
 * @version 1.0
 */
public class EnvirmentUtil {
	public static void main(String[] args){
		String mainclalss = "#[mainclass:org.frameworkset.elasticsearch.imp.DB2CSVFile]";
		String value = parserMainClass(mainclalss);
		System.out.println(value);

		mainclalss = "#[mainclass:]";
		value = parserMainClass(mainclalss);
		System.out.println(value);

		mainclalss = "#[:org.frameworkset.elasticsearch.imp.DB2CSVFile]";
		value = parserMainClass(mainclalss);
		System.out.println(value);
	}
	private static String parserMainClassVariable(String mainClassVariable){
		//Get mainClass from jvm system propeties,just like -Dproperty=value
//			Properties pros = System.getProperties();
		String mainClass =System.getProperty(mainClassVariable);
		if(mainClass == null) {
			//Get mainClass from os env ,just like property=value in user profile
			mainClass = System.getenv(mainClassVariable);

		}
		return mainClass;
	}

	/**
	 * 如果mainclass是变量定义格式，则从系统属性和系统环境变量中解析mainclass
	 * @param configedMainclass
	 * @return
	 */
	public static String parserMainClass(String configedMainclass){
		if(configedMainclass == null || configedMainclass.equals("")){
			return configedMainclass;
		}
		if(configedMainclass.startsWith("#[") && configedMainclass.endsWith("]")){
			String mainclass = configedMainclass.substring(2,configedMainclass.length() -1);
			int idx = mainclass.indexOf(":");
			if(idx >= 0){
				String defaultMainClass = mainclass.substring(idx+1).trim();
				String mainClassVariable = mainclass.substring(0, idx).trim();
				if(!mainClassVariable.equals("")){
					String varValue = parserMainClassVariable(mainClassVariable);
					if(varValue == null || varValue.equals("")){
						return defaultMainClass;
					}
					else{
						return varValue.trim();
					}
				}
				else{
					return defaultMainClass;
				}


			}
			else{
				String varValue = parserMainClassVariable(mainclass.trim());
				if(varValue == null || varValue.equals("")){
					return null;
				}
				else{
					return varValue.trim();
				}
			}
		}
		else{
			return configedMainclass.trim();
		}

	}
}
