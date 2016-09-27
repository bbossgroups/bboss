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

import java.util.HashMap;
import java.util.Map;

import org.frameworkset.spi.BaseApplicationContext;
import org.frameworkset.spi.assemble.LinkConfigFile;
import org.frameworkset.spi.assemble.Pro;
import org.frameworkset.spi.assemble.PropertiesContainer;
import org.frameworkset.spi.assemble.ProviderParser;
import org.frameworkset.spi.assemble.ServiceProviderManager;
import org.xml.sax.Attributes;

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
    
    protected void mergeParentConfigProperties()
    {
    	 
    }
    
    protected void setFAttr(Pro property,Attributes attributes)
    {
    	if(attributes == null || attributes.getLength() == 0)
    		return;
    	int length = attributes.getLength();
    	
    	Map<String,Object> extendsAttributes = new HashMap<String,Object>();
    	Map<String,String> SOAAttributes = new HashMap<String,String>();
    	
    	for(int i = 0; i < length; i ++)
    	{
    		String name = attributes.getQName(i);
    		if(name.equals("n") || name.equals("name"))
    		{
    			continue;
    		}
    		
    		else if(name.startsWith("s:"))//soa:
    		{
    			
    			SOAAttributes.put(name, attributes.getValue(i));
    		}
    		else if( name.equals("cs") || name.equals("v") || name.equals("class") || name.equals("value"))
    		{
    			continue;
    		}
    		
    		else if(name.startsWith("f:"))//通过property的属性来制定对象中field的值
    		{
    			Pro f = this._buildPro();// new Pro(applicationContext);
    			
    			f.setName(name.substring(2));
    			
    			String value = attributes.getValue(i);
    			if(value.startsWith(ServiceProviderManager.SERVICE_PREFIX) 
    					|| value.startsWith(ServiceProviderManager.ATTRIBUTE_PREFIX))
    			{
    				f.setRefid(value);
    			}
    			else
    			{
    				f.setValue(value,configPropertiesFile);
    			}
    			//增加xpath信息
    			f.setXpath(property.getXpath() + Pro.REF_TOKEN + f.getName());
    			property.addReferenceParam(f);
    		}
//    		else if(name.startsWith("path:"))
//    		{
//    			pathAttributes.put(name,attributes.getValue(i));
//    		}
//    		else if(name.startsWith("ws:"))
//    		{
//    			WSAttributes.put(name, attributes.getValue(i));
//    		}
//    		
//    		else if(name.startsWith("rmi:"))
//    		{
//    			
//    			RMIAttributes.put(name, attributes.getValue(i));
//    		}
    			
    		else if(!Pro.isFixAttribute(name))
    		{
    		    extendsAttributes.put(name, attributes.getValue(i));
    		}
    		    
    	}
    	if(SOAAttributes.size() > 0)
    	{
    		property.setSOAAttributes(SOAAttributes);
    	}
    	property.setExtendsAttributes(extendsAttributes);
//    	return null;
    }
      
}
