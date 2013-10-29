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

import org.frameworkset.spi.assemble.BaseTXManager;

import com.frameworkset.proxy.Interceptor;
/**
 * <p>Title: </p>
 *
 * <p>Description: 与服务提供者相关的拦截器</p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: 长沙科创计算机系统集成有限公司</p>
 *
 * @author biaoping.yin
 * @version 1.0
 */
public abstract class ProviderInterceptor implements Interceptor {
    protected BaseTXManager providerManagerInfo;
    public BaseTXManager getProviderManagerInfo() {
        return providerManagerInfo;
    }

    public void setProviderManagerInfo(BaseTXManager providerManagerInfo) {
        this.providerManagerInfo = providerManagerInfo;
    }

}
