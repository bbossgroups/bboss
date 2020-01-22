package com.frameworkset.daemon;
/**
 * Copyright 2008 biaoping.yin
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import com.frameworkset.util.ResourceInitial;
import com.frameworkset.util.UUIDResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>Description: </p>
 * <p></p>
 * <p>Copyright (c) 2018</p>
 * @Date 2020/1/19 17:17
 * @author biaoping.yin
 * @version 1.0
 */
public abstract class ResourceNameSpace implements Serializable {
	private Logger logger = LoggerFactory.getLogger(ResourceNameSpace.class);
	private List<WrapperResourceInit> resourceInitList;
	public String getNameSpace() {
		return nameSpace;
	}

	public void setNameSpace(String nameSpace) {
		this.nameSpace = nameSpace;
	}

	public long getModifyTimestamp() {
		return modifyTimestamp;
	}

	public void setModifyTimestamp(long modifyTimestamp) {
		this.modifyTimestamp = modifyTimestamp;
	}

	private String nameSpace;
	private long modifyTimestamp;
	public abstract long getLastModifyTimestamp();

	public void addResourceInit(WrapperResourceInit wrapperResourceInit) {
		if(resourceInitList == null){
			resourceInitList = new ArrayList<WrapperResourceInit>();
		}
		if(!this.contain(wrapperResourceInit)) {
			resourceInitList.add(wrapperResourceInit);
		}
	}
	
	private boolean contain(ResourceInitial init)
	{

		if(init instanceof UUIDResource)
		{
			String uuid = ((UUIDResource)init).getUUID();
			for(int i = 0; i < this.resourceInitList.size(); i ++)
			{
				ResourceInitial initOld = this.resourceInitList.get(i);
				if(initOld instanceof UUIDResource)
				{
					if(((UUIDResource)initOld).getUUID().equals(uuid))
						return true;
				}
			}
			return false;
		}
		else {

			return true;//兼容旧版本,没有实现UUIDResource接口的初始化资源初始化接口只允许存在一个ResourceInitial

		}
	}
	public synchronized void reinit(){
		//System.out.println("Reload changed file：" + file.getAbsolutePath());
		if(logger.isInfoEnabled()) {
			logger.info("Reload data in changed resource[" + this.nameSpace + "]");
		}
		long lastModifiedTime = this.getLastModifyTimestamp();
		if(lastModifiedTime == this.modifyTimestamp)
			return;

		this.modifyTimestamp = lastModifiedTime;

		for(int i = 0; resourceInitList != null && i < this.resourceInitList.size(); i++)
		{
			WrapperResourceInit init = resourceInitList.get(i);
			try {
				init.reinit();
				if(logger.isInfoEnabled()) {
					logger.info("Reload data in changed resource[" + this.nameSpace + "] sucessed.");
				}
			} catch (Exception e) {
				if(logger.isErrorEnabled())
					logger.error("Reload data in changed resource[" + this.nameSpace + "] failed:" ,e);
			}
		}
	}

	public boolean checkChanged() {
		long lastModifiedTime = this.getLastModifyTimestamp();
		if(lastModifiedTime == this.modifyTimestamp){
			return false;
		}
		return true;
	}
}
