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

import java.util.ArrayList;
import java.util.List;



/**
 * 
 * 
 * <p>Title: ProviderInfoQueue.java</p>
 *
 * <p>Description: 管理服务提供者队列，按优先级顺序
 * 存放管理服务的多个提供者
 * </p>
 *
 * <p>Copyright: Copyright (c) 2007</p>
 *
 * <p>bboss workgroup</p>
 * @Date Sep 8, 2008 10:05:06 AM
 * @author biaoping.yin,尹标平
 * @version 1.0
 */
public class ProviderInfoQueue implements java.io.Serializable {
    private List list = new ArrayList();
    public static void main(String[] args) {
        ProviderInfoQueue providerqueue = new ProviderInfoQueue();
    }

    public SecurityProviderInfo getSecurityProviderInfo(int i)
    {
        return (SecurityProviderInfo)list.get(i);
    }

    public void addSecurityProviderInfo(SecurityProviderInfo securityProvider)
    {
        this.list.add(securityProvider);
        java.util.Collections.sort(list, new java.util.Comparator()
        {
        	/**
        	 * provider排序规则，prior值越小，优先级越高
        	 */
			public int compare(Object o1, Object o2) {
				SecurityProviderInfo o1_ = (SecurityProviderInfo)o1;
				SecurityProviderInfo o2_ = (SecurityProviderInfo)o2;
				if(o1_.getPrior() < o2_.getPrior())
					return 1;
				else if(o1_.getPrior() > o2_.getPrior())
					return -1;
				
				return 0;
			}        	
        });
    }

    public int size()
    {
        return list.size();
    }
}
