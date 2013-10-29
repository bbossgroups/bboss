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

package org.frameworkset.spi.beans;

import org.frameworkset.spi.BaseSPIManager;
import org.junit.Test;

/**
 * <p>Title: ExtendAttribute.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2007</p>
 * @Date 2010-2-23 下午02:15:52
 * @author biaoping.yin
 * @version 1.0
 */
public class ExtendAttribute
{
    @Test
    public void testInt()
    {
        System.out.println("int:" + BaseSPIManager.getIntExtendAttribute("test.beans", "int"));
    }
    
    @Test
    public void testString()
    {
        System.out.println("string:" + BaseSPIManager.getStringExtendAttribute("test.beans", "string"));
    }
    
    @Test
    public void testBoolean()
    {
        System.out.println("Boolean:" + BaseSPIManager.getBooleanExtendAttribute("test.beans", "boolean"));
    }
    
    
    @Test
    public void testLong()
    {
        System.out.println("Long:" + BaseSPIManager.getLongExtendAttribute("test.beans", "long"));
    }
    
    @Test
    public void testObject()
    {
        System.out.println("Object:" + BaseSPIManager.getExtendAttribute("test.beans", "object"));
    }
}

