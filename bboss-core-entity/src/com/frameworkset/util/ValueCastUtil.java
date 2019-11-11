package com.frameworkset.util;
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

/**
 * <p>Description: </p>
 * <p></p>
 * <p>Copyright (c) 2018</p>
 * @Date 2019/11/11 10:50
 * @author biaoping.yin
 * @version 1.0
 */
public abstract class ValueCastUtil {
	public static int toInt(Object value,int defaultValue) throws Exception {
		if(value == null)
			return defaultValue;
		try {
			if(value instanceof Integer){
				return (Integer)value;
			}
			else if(value instanceof Number){
				return ((Number)value).intValue();
			}
			else{
				return Integer.parseInt(String.valueOf(value));
			}

		}
		catch (Exception e){
			throw e;
		}
	}

	public static long toLong(Object value,long defaultValue) throws Exception {
		if(value == null)
			return defaultValue;
		try {
			if(value instanceof Long){
				return (Long)value;
			}
			else if(value instanceof Number){
				return ((Number)value).longValue();
			}
			else {
				return Long.parseLong(String.valueOf(value));
			}
		}
		catch (Exception e){
			throw e;
		}
	}

	public static boolean toBoolean(Object value,boolean defaultValue)  {
		if(value == null)
			return defaultValue;

		if(value instanceof Boolean){
			return (Boolean)value;
		}
		if(value.equals("true")){
			return true;
		}
		return false;
	}

	public static String toString(Object value,String defaultValue)  {
		if(value == null)
			return defaultValue;

		if(value instanceof String){
			return (String)value;
		}
		return String.valueOf(value);
	}
}
