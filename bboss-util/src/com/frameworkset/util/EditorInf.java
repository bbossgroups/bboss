/*
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

package com.frameworkset.util;


/**
 * <p>Title: EditorInf.java</p> 
 * <p>Description: 用户自定义属性编辑接口</p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2008</p>
 * @Date 2009-12-7 上午10:44:15
 * @author biaoping.yin
 * @version 1.0
 */
public interface EditorInf<T>
{
    
    /**
     * Gets the property value.
     * @param fromValue
     * @return The value of the property.  Primitive types such as "int" will
     * be wrapped as the corresponding object type such as "java.lang.Integer".
     */
    T getValueFromObject(Object fromValue) ;    
    T getValueFromString(String fromValue);
   
  
}
