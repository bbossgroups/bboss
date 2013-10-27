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

package org.frameworkset.spi;

import java.lang.reflect.Method;

import org.frameworkset.spi.assemble.SynchronizedMethod;
import org.frameworkset.spi.assemble.SynchronizedMethod.MethodInfo;
import org.junit.Test;

/**
 * <p>Title: MethodInfoTest.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2007</p>
 * @Date 2010-3-30 下午04:06:01
 * @author biaoping.yin
 * @version 1.0
 */
public class MethodInfoTest
{
    
    public void testInfo(String type,int i) throws ClassNotFoundException
    {
        
    }
    @Test
    public void testInfo() throws ClassNotFoundException
    {
        System.out.println(int.class);
        
        
        String methodUUid = "testInfo_java.lang.String_int";
        MethodInfo method = SynchronizedMethod.refactorMehtodfromUUID(methodUUid);
        System.out.println(method.getMethodName());
        System.out.println(method.getParamTypes()[0]);
        System.out.println(method.getParamTypes()[1]);
    }
    
    @Test
    public void testUUID() throws ClassNotFoundException, SecurityException, NoSuchMethodException
    {
        
        
        Method method_ = MethodInfoTest.class.getMethod("testInfo", new Class[]{String.class,int.class});
        String methodUUid = SynchronizedMethod.buildMethodUUID(method_);
        System.out.println(methodUUid);
        MethodInfo method = SynchronizedMethod.refactorMehtodfromUUID(methodUUid);
        System.out.println(method.getMethodName());
        System.out.println(method.getParamTypes()[0]);
        System.out.println(method.getParamTypes()[1]);
    }
    
    

}
