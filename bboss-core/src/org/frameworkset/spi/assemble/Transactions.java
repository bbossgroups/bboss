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

import java.lang.reflect.Method;
import java.util.List;

import org.apache.log4j.Logger;
import org.frameworkset.spi.UNmodify;

/**
 * 
 * 
 * <p>
 * Title: Transactions.java
 * </p>
 * 
 * <p>
 * Description: 封装业务组件的事务属性,业务组件的事务属性 包含以下内容： 要进行事务控制的方法名 方法参数列表 触发事务回滚的异常清单
 * 方法所需事务类型
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
 * @Date Jul 30, 2008 9:19:00 AM
 * @author biaoping.yin,尹标平
 * @version 1.0
 */
public class Transactions implements java.io.Serializable,UNmodify
{
    private static Logger log = Logger.getLogger(Transactions.class);

    AOPMethods transationMethods;

    public Transactions()
    {
    	transationMethods = new AOPMethods();
    }

    /**
     * 判断方法是否是需要进行事务控制,如果是则返回包含事务控制策略信息的方法对象， 不是就返回null
     * 
     * @param method
     * @return SynchronizedMethod 包含事务控制策略信息
     */
    public SynchronizedMethod isTransactionMethod(Method method,String muuid)
    {
        return transationMethods.isTransactionMethod(method,muuid);
    }

    public int size()
    {
        return this.transationMethods.size();
    }

    public void addTransactionMethod(SynchronizedMethod method)
    {
        this.transationMethods.addTransactionMethod(method);
    }
    
    

//    private boolean containTransactionMethod(SynchronizedMethod method)
//    {
//       return this.transationMethods.containTransactionMethod(method);
//        
//    }

    public List<SynchronizedMethod> getTransactionMethods()
    {
        return this.transationMethods.getTransactionMethods();
    }

    public void unmodify()
    {
        this.transationMethods.unmodify();
//        this.fullTXMethodInfoIDX = java.util.Collections.unmodifiableMap(this.fullTXMethodInfoIDX);
        
    }

}
