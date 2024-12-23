/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.frameworkset.commons.jocl;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * Miscellaneous {@link Constructor} related utility functions.
 *
 * @author Rodney Waldhoff
 * @version $Revision: 479137 $ $Date: 2006-11-25 10:51:48 -0500 (Sat, 25 Nov 2006) $
 */
public class ConstructorUtil {
    /**
     * Returns a {@link Constructor} for the given method signature, or <tt>null</tt>
     * if no such <tt>Constructor</tt> can be found.
     *
     * @param type     the (non-<tt>null</tt>) type of {@link Object} the returned {@link Constructor} should create
     * @param argTypes a non-<tt>null</tt> array of types describing the parameters to the {@link Constructor}.
     * @return a {@link Constructor} for the given method signature, or <tt>null</tt>
     *         if no such <tt>Constructor</tt> can be found.
     * @see #invokeConstructor
     */
    public static Constructor getConstructor(Class type, Class[] argTypes) {
        if(null == type || null == argTypes) {
            throw new NullPointerException();
        }
        Constructor ctor = null;
        try {
            ctor = type.getConstructor(argTypes);
        } catch(Exception e) {
            ctor = null;
        }
        if(null == ctor) {
            // no directly declared matching constructor,
            // look for something that will work
            // XXX this should really be more careful to
            //     adhere to the jls mechanism for late binding
            Constructor[] ctors = type.getConstructors();
            for(int i=0;i<ctors.length;i++) {
                Class[] paramtypes = ctors[i].getParameterTypes();
                if(paramtypes.length == argTypes.length) {
                    boolean canuse = true;
                    for(int j=0;j<paramtypes.length;j++) {
                        if(paramtypes[j].isAssignableFrom(argTypes[j])) {
                            continue;
                        } else {
                            canuse = false;
                            break;
                        }
                    }
                    if(canuse == true) {
                        ctor = ctors[i];
                        break;
                    }
                }
            }
        }
        return ctor;
    }

    /**
     * Creates a new instance of the specified <tt><i>type</i></tt>
     * using a {@link Constructor} described by the given parameter types
     * and values.
     *
     * @param type      the type of {@link Object} to be created
     * @param argTypes  a non-<tt>null</tt> array of types describing the parameters to the {@link Constructor}.
     * @param argValues a non-<tt>null</tt> array containing the values of the parameters to the {@link Constructor}.
     * @return a new instance of the specified <tt><i>type</i></tt>
     *         using a {@link Constructor} described by the given parameter types
     *         and values.
     * @exception InstantiationException
     * @exception IllegalAccessException
     * @exception InvocationTargetException
     */
    public static Object invokeConstructor(Class type, Class[] argTypes, Object[] argValues) throws InstantiationException, IllegalAccessException, InvocationTargetException {
        return ConstructorUtil.getConstructor(type,argTypes).newInstance(argValues);
    }
}


