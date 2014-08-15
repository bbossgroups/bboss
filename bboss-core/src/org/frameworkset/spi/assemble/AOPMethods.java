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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.frameworkset.spi.UNmodify;

/**
 * <p>Title: AOPMethods.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2007</p>
 * @Date 2011-4-20 下午03:21:57
 * @author biaoping.yin
 * @version 1.0
 */
public class AOPMethods  implements java.io.Serializable,UNmodify
{
    private static Logger log = Logger.getLogger(Transactions.class);

    /**
     * 事务方法信息 List<SynchronizedMethod>
     */
    private List<SynchronizedMethod> aopMethods;

    /**
     * 标识方法是否是声明式事务方法 Map<methoduuid,SynchronizedMethod>
     */
    private Map aopMethodInfoIDX;

    public static final SynchronizedMethod NULL = new SynchronizedMethod();

    public AOPMethods()
    {
    	aopMethods = new ArrayList();

    	aopMethodInfoIDX = new HashMap();
    }

    /**
     * 判断方法是否是需要进行事务控制,如果是则返回包含事务控制策略信息的方法对象， 不是就返回null
     * 
     * @param method
     * @return SynchronizedMethod 包含事务控制策略信息
     */
    public SynchronizedMethod isTransactionMethod(Method method,String uuid)
    {
        return containMethod(method,uuid);
    }
    
    /**
     * 判断给定的方法是否是异步调用方法，如果是则将异步方法信息返回给调用程序
     * @param method
     * @return
     */
    public SynchronizedMethod isAsyncMethod(Method method,String uuid)
    {
        return containMethod(method,uuid);
    }
    
    private SynchronizedMethod containMethod(Method method, String uuid)
    {
    	if(aopMethods == null || aopMethods.size() == 0)
    		return null;
        try
        {
        	if(uuid == null)
        		uuid = SynchronizedMethod.buildMethodUUID(method);
            SynchronizedMethod match = (SynchronizedMethod) aopMethodInfoIDX.get(uuid);
            if (match == null)
            {
                synchronized (this)
                {
                    match = (SynchronizedMethod) aopMethodInfoIDX.get(uuid);
                    if (match != null)
                    {
                        if (match == NULL)
                        {
                            return null;
                        }
                        else
                        {
                            return match;
                        }
                    }
                    for (int i = 0; i < aopMethods.size(); i++)
                    {
                        SynchronizedMethod m = (SynchronizedMethod) aopMethods.get(i);
                        if (m.match(method, uuid))
                        {
                            this.aopMethodInfoIDX.put(uuid, m);
                            return m;
                        }
                    }
                    aopMethodInfoIDX.put(uuid, NULL);
                    return null;
                }
            }
            else
            {
                if (match == NULL)
                {
                    return null;
                }
                else
                {
                    return match;
                }
            }
        }
        catch (Exception e)
        {
            log.info("",e);
            return null;
        }
    }

    public int size()
    {
        return this.aopMethods.size();
    }

    public void addTransactionMethod(SynchronizedMethod method)
    {
        if(this.containTransactionMethod(method)) 
            return;
        this.aopMethods.add(method);
    }
    
    

    private boolean containTransactionMethod(SynchronizedMethod method)
    {
       if(aopMethods == null || aopMethods.size() == 0)
           return false;
       for(SynchronizedMethod method_: this.aopMethods)
       {
           if(method_.getUUID().equals(method.getUUID()))
               return true;
       }
       return false;
        
    }

    public List<SynchronizedMethod> getTransactionMethods()
    {
        return aopMethods;
    }

    public void unmodify()
    {
        aopMethods = java.util.Collections.unmodifiableList(aopMethods);
//        this.aopMethodInfoIDX = java.util.Collections.unmodifiableMap(this.aopMethodInfoIDX);
        
    }

}
