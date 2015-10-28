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
package com.frameworkset.common.poolman;

import java.io.Serializable;

/**
 * <p>Title: PoolmanMBean</p>
 *
 * <p>Description: 初始化poolman中的所有数据库链接池，并且将连接池绑定到特定服务器jndi服务</p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: </p>
 *
 * @author biaoping.yin
 * @version 1.0
 */
public interface PoolmanMBean extends Serializable{
    /**初始化poolman中的所有数据库链接池，并且将连接池绑定到特定服务器jndi服务*/
    public void bootStartup();
}
