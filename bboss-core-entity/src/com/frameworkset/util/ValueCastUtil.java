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

import java.math.BigDecimal;

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

    public static double toDouble(Object value,double defaultValue) throws Exception {
        if(value == null)
            return defaultValue;
        try {
            if(value instanceof Double){
                return (Double)value;
            }
            else if(value instanceof Number){
                    return ((Number)value).doubleValue();
            }
            else {
                return Double.parseDouble(String.valueOf(value));
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


	public static  Integer integerValue(Object num,Integer defaultValue){
		return intValue(num,defaultValue);
	}
	public static  Long longValue(Object num,Long defaultValue){
		if(num == null)
			return defaultValue;
		if(num instanceof Long)
		{
			return ((Long)num);
		}
		else  if(num instanceof BigDecimal)
		{
			return ((BigDecimal)num).longValue();
		}
		else if(num instanceof Double)
		{
			return ((Double)num).longValue();
		}else if(num instanceof Integer){
			return ((Integer)num).longValue();
		}
		else if(num instanceof Float)
		{
			return ((Float)num).longValue();
		}
		else  if(num instanceof Short)
		{
			return ((Short)num).longValue();
		}
		else
		{
			return Long.parseLong(num.toString());
		}
	}

	public static  Integer intValue(Object num,Integer defaultValue){
		if(num == null)
			return defaultValue;
		if(num instanceof Integer)
		{
			return ((Integer)num);
		}
		else if(num instanceof Long)
		{
			return ((Long)num).intValue();
		}
		else  if(num instanceof BigDecimal)
		{
			return ((BigDecimal)num).intValue();
		}
		else if(num instanceof Double)
		{
			return ((Double)num).intValue();
		}
		else if(num instanceof Float)
		{
			return ((Float)num).intValue();
		}
		else  if(num instanceof Short)
		{
			return ((Short)num).intValue();
		}
		else
		{
			return Integer.parseInt(num.toString());
		}
	}

	public static  Float floatValue(Object num,Float defaultValue){
		if(num == null)
			return defaultValue;
		if(num instanceof Float)
		{
			return (Float)num;
		}
		else  if(num instanceof BigDecimal)
		{
			return ((BigDecimal)num).floatValue();
		}
		else if(num instanceof Double)
		{
			return ((Double)num).floatValue();
		}else if(num instanceof Integer){
			return ((Integer)num).floatValue();
		}
		else  if(num instanceof Long)
		{
			return ((Long)num).floatValue();
		}
		else  if(num instanceof Short)
		{
			return ((Short)num).floatValue();
		}
		else
		{
			return Float.parseFloat(num.toString());
		}
	}

	public static  String stringValue(Object num,String defaultValue){
		if(num == null)
			return defaultValue;
		if(num instanceof String)
		{
			return (String)num;
		}
		else
		{
			return String.valueOf(num);
		}

	}
	public static  Double doubleValue(Object num,Double defaultValue){
		if(num == null)
			return defaultValue;
		if(num instanceof Double)
		{
			return (Double)num;
		}
		else  if(num instanceof BigDecimal)
		{
			return ((BigDecimal)num).doubleValue();
		}
		else if(num instanceof Float)
		{
			return ((Float)num).doubleValue();
		}else if(num instanceof Integer){
			return ((Integer)num).doubleValue();
		}
		else  if(num instanceof Long)
		{
			return ((Long)num).doubleValue();
		}
		else  if(num instanceof Short)
		{
			return ((Short)num).doubleValue();
		}
		else
		{

			return Double.parseDouble(num.toString());
		}
	}
}
