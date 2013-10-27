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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * 
 * <p>Title: LinkConfigFile.java</p>
 *
 * <p>Description: 配置文件配置信息
 * 包含配置文件路径，导入该配置文件的配置信息 
 * 以及在配置文件中的管理服务索引
 * 
 * 实现对所有管理服务的监控和管理
 * </p>
 *
 * <p>Copyright: Copyright (c) 2007</p>
 *
 * <p>bboss workgroup</p>
 * @Date Aug 20, 2008 10:36:22 AM
 * @author biaoping.yin,尹标平
 * @version 1.0
 */
public class LinkConfigFile {
	public LinkConfigFile(String fullPath,String configFile,LinkConfigFile parent)
	{
		this.configFile = configFile;
		this.parent = parent;
		linkConfigFiles = new ArrayList();
		this.fullPath = fullPath;
		this.mgrServices  = new HashMap();
	}
	
	/**
	 * 配置文件完整路径
	 */
	private String fullPath = null;
	/**
	 * 配置文件相对路径
	 */
	private String configFile ;
	private Map properties = new HashMap();
	
	/**
	 * 配置文件相对路径地址
	 */
	private String relativePath ;
	/**
	 * 导入配置文件configFile的配置文件配置信息
	 */
	private LinkConfigFile parent;
	
	/**
	 * 配置文件对应的所有管理服务
	 * Map<mgrid,ProviderManagerInfo>
	 */
	private Map mgrServices = null;
	/**
	 * 配置文件导入的管理配置文件
	 * List<LinkConfigFile>
	 */
	private List linkConfigFiles = null;
	 
	public String getConfigFile() {
		return configFile;
	}
	
	public LinkConfigFile getParent() {
		return parent;
	}
	
	public ProviderManagerInfo getProviderManagerInfo(String mgrid){
		
		return (ProviderManagerInfo)mgrServices.get(mgrid);
	}
	
	
	
	
	public String toString()
	{
		StringBuffer ret = new StringBuffer();
		if(parent != null)
		{
			ret.append(this.parent.toString())
			.append("@").append(this.configFile);
		}
		else
		{
			ret.append(this.configFile);
		}
		return ret.toString();
	}
	
	public String toString(String subfile)
    {
        StringBuffer ret = new StringBuffer();
        if(parent != null)
        {
            ret.append(this.parent.toString())
            .append("@").append(this.configFile).append("@").append(subfile);
        }
        else
        {
            ret.append(this.configFile).append("@").append(subfile);
        }
        return ret.toString();
    }

	public Map getMgrServices() {
		return mgrServices;
	}

	public void setMgrServices(Map mgrServices) {
		if(mgrServices != null && mgrServices.size() > 0)
			this.mgrServices.putAll( mgrServices);
	}
	
	public Map getProperties() {
            return properties;
        }
    
        public void setProperties(Map properties) {
                if(properties != null && properties.size() > 0)
                        this.properties.putAll( properties);
        }
	
	
	

	public void addLinkConfigFile(LinkConfigFile linkConfigFile)
	{
		this.linkConfigFiles.add(linkConfigFile);
	}

	public List getLinkConfigFiles() {
		return linkConfigFiles;
	}
	
	/**
	 * 配置文件的标识
	 * @return
	 */
	public String getIdentity()
	{
		return this.configFile;
	}
	
	public boolean hasSubLinks()
	{
		return this.linkConfigFiles != null && this.linkConfigFiles.size() > 0;
	}

	public String getFullPath() {
		return fullPath;
	}
	
	
	public boolean hasMGRService()
	{
		return this.mgrServices != null && this.mgrServices.size() > 0;
	}

	public boolean hasPros()
	{

		
		return this.properties != null && this.properties.size() > 0;
	}
	
	
}
