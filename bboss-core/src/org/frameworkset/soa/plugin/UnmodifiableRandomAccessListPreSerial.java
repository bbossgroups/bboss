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

package org.frameworkset.soa.plugin;

import org.frameworkset.soa.PreSerial;
import org.frameworkset.util.ClassUtil;
import org.frameworkset.util.ClassUtil.ClassInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;

/**
 * <p>Title: UnmodifyListPreSerial.java</p> 
 * <p>Description: java.util.Collections$UnmodifiableList serializable</p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2007</p>
 * @Date 2014年8月8日 上午9:55:51
 * @author biaoping.yin
 * @version 1.0
 */
public class UnmodifiableRandomAccessListPreSerial implements PreSerial<List> {
	private static final String clazz = "java.util.Collections$UnmodifiableRandomAccessList";
	private static Logger logger = LoggerFactory.getLogger(UnmodifiableRandomAccessListPreSerial.class);
	private static  ClassInfo unmodify;
	static {
		
		try {
			unmodify = ClassUtil.getClassInfo(Class.forName(clazz));
		} catch (ClassNotFoundException e) {
			logger.warn("",e);
			unmodify = null;
		}
	}
	public UnmodifiableRandomAccessListPreSerial() {
		
	}

	public List prehandle(List object) {
		
		return (List)unmodify.getPropertyValue(object, "list");
	}

	public List posthandle(List object) {
		if(object == null)
			return null;
		return Collections.unmodifiableList(object);
	}
	public String getClazz() {
		// TODO Auto-generated method stub
		return this.clazz;
	}

	public String getVClazz() {
		// TODO Auto-generated method stub
		return null;
	}
}
