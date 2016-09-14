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
package org.frameworkset.spi.assemble.soa;

import org.frameworkset.spi.BaseApplicationContext;
import org.frameworkset.spi.assemble.LinkConfigFile;
import org.frameworkset.spi.assemble.Pro;
import org.frameworkset.spi.assemble.ProviderParser;

/**
 * 
 * 
 * <p>
 * Title: ProviderParser.java
 * </p>
 * 
 * <p>
 * Description: 管理服务配置文件解析器,bean定义
 * 针对soa的优化节点和属性对照表
properties转换为ps
property转换为p
name转换为n
value转换为v
class转换为cs
list转换为l
array转换为a
map转换为m
set转换为s
soa:type_null_value转换为s:nvl
soa:type转换为s:t
componentType转换为cmt
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
 * @Date Sep 8, 2008 10:02:46 AM
 * @author biaoping.yin,尹标平
 * @version 1.0
 */
public class SOAProviderParser extends ProviderParser
{

	public SOAProviderParser(BaseApplicationContext applicationContext, String file, LinkConfigFile parent) {
		super(applicationContext, file, parent);
		// TODO Auto-generated constructor stub
	}

	public SOAProviderParser(BaseApplicationContext applicationContext) {
		super(applicationContext);
		// TODO Auto-generated constructor stub
	}
    protected Pro _buildPro()
    {
    	return new SOAPro(applicationContext);
    }
      
}
