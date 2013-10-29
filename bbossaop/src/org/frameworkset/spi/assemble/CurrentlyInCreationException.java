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
package org.frameworkset.spi.assemble;

/**
 * 
 * 
 * <p>
 * Title: BeanCurrentlyInCreationException.java
 * </p>
 * 
 * <p>
 * Description: 如果管理服务在注入引用变量的值时，出现循环引用的情况，比如：
 * 比如说，一个类A，需要需要注入类B的引用变量，而类B又需要通过注入类A的引用变量
 * 系统一旦检测到循环引用的情况，需要抛出CurrentlyInCreationException异常。
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2007
 * </p>
 * 
 * <p>
 * bboss workgroup
 * </p>
 * 
 * @Date Aug 14, 2008 3:28:10 PM
 * @author biaoping.yin,尹标平
 * @version 1.0
 */
public class CurrentlyInCreationException extends RuntimeException
{

    public CurrentlyInCreationException()
    {
        super();
        // TODO Auto-generated constructor stub
    }

    public CurrentlyInCreationException(String message, Throwable cause)
    {
        super(message, cause);
        // TODO Auto-generated constructor stub
    }

    public CurrentlyInCreationException(String msg)
    {
        super(msg);
    }

    public CurrentlyInCreationException(Throwable t)
    {
        super(t);
    }

}
