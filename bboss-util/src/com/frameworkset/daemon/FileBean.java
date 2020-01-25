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

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>Description: </p>
 * <p></p>
 * <p>Copyright (c) 2018</p>
 * @Date 2020/1/22 10:41
 * @author biaoping.yin
 * @version 1.0
 */
public class FileBean {
	private Logger log = LoggerFactory.getLogger(FileBean.class);
	public FileBean(File file,
					WrapperResourceInit init) {
		super();
		this.file = file;
		this.inits = new ArrayList<WrapperResourceInit>();
		inits.add(init);
		this.oldModifiedTime =  file.lastModified();

	}
	private boolean removeflag = false;
	private File file;
	//		 private long lastModifiedTime;
	private long oldModifiedTime;
	private List<WrapperResourceInit> inits;
	/**
	 * @return the file
	 */
	public File getFile() {
		return file;
	}
	/**
	 * @param file the file to set
	 */
	public void setFile(File file) {
		this.file = file;
	}

	/**
	 * @return the oldModifiedTime
	 */
	public long getOldModifiedTime() {
		return oldModifiedTime;
	}
	/**
	 * @param oldModifiedTime the oldModifiedTime to set
	 */
	public void setOldModifiedTime(long oldModifiedTime) {
		this.oldModifiedTime = oldModifiedTime;
	}


	public  boolean checkChanged()
	{
		if(file == null)
			return false;
		boolean exist = file.exists();
		if(!exist)
		{
			//init.destroy();
			return false;
		}
		long lastModifiedTime = file.lastModified();
		if(this.oldModifiedTime != lastModifiedTime)
		{

			return true;
		}
		else
		{
			return false;
		}
	}

	/**
	 * 1 resolved java.util.ConcurrentModificationException by biaoping.yin on 2015.03.09
	 */
	public synchronized void reinit()
	{
		//System.out.println("Reload changed file：" + file.getAbsolutePath());
		if(log.isInfoEnabled()) {
			log.info("Reload resources in changed file：" + file.getAbsolutePath());
		}
		long lastModifiedTime = file.lastModified();
		if(lastModifiedTime == this.oldModifiedTime)
			return;
		else {
			//检查文件是否已经读写完毕，通过文件间隔1秒检测lastModified时间搓来判断（不一定准确）
//			long length = file.length();
			long last = lastModifiedTime;
			do {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					break;
				}
				lastModifiedTime = file.lastModified();
				if (last == lastModifiedTime) {
//					lastModifiedTime = file.lastModified();
					break;

				} else {
					last = lastModifiedTime;
				}
			}
			while(true);
		}
		this.oldModifiedTime = lastModifiedTime;

		for(int i = 0; inits != null && i < this.inits.size(); i++)
		{
			WrapperResourceInit init = inits.get(i);
			try {
				init.reinit();
				if(log.isInfoEnabled()) {
					log.info("Reload changed resource file " + init.getUUID()+"@"+ file.getAbsolutePath() + " sucessed.");
				}
			} catch (Exception e) {
				if(log.isErrorEnabled())
					log.error("Reload changed resource  file " + init.getUUID()+"@"+ file.getAbsolutePath() + " failed:" ,e);
			}
		}


	}
	//end 1 resolved java.util.ConcurrentModificationException by biaoping.yin on 2015.03.09
	public boolean isRemoveflag() {
		return removeflag;
	}
	public void setRemoveflag(boolean removeflag) {
		this.removeflag = removeflag;
	}
	public void addResourceInit(WrapperResourceInit init) {
		if(!this.contain(init))
			this.inits.add(init);

	}

	private boolean contain(ResourceInitial init)
	{

		if(init instanceof UUIDResource)
		{
			String uuid = ((UUIDResource)init).getUUID();
			for(int i = 0; i < this.inits.size(); i ++)
			{
				ResourceInitial initOld = this.inits.get(i);
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


}
