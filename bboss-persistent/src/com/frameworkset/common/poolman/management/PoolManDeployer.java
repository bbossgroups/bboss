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
 *  distributed under the License is distributed on an "AS IS" bboss persistent,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.  
 */
package com.frameworkset.common.poolman.management;

import java.util.Map;

public interface PoolManDeployer {

    public void deployConfiguration(PoolManConfiguration config) throws Exception;
    public void deployConfiguration(PoolManConfiguration config,Map<String,String> values) throws Exception;
    public void deployConfiguration(PoolManConfiguration config,String dbname) throws Exception;

}
