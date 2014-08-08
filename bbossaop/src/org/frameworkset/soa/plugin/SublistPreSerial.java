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

import java.util.ArrayList;
import java.util.List;

import org.frameworkset.soa.PreSerial;
import org.frameworkset.util.ClassUtil;
import org.frameworkset.util.ClassUtil.ClassInfo;

/**
 * <p>Title: UnmodifyListPreSerial.java</p> 
 * <p>Description: java.util.Collections$UnmodifiableList serializable</p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2007</p>
 * @Date 2014年8月8日 上午9:55:51
 * @author biaoping.yin
 * @version 1.0
 */
public class SublistPreSerial implements PreSerial<List> {
	private static final String clazz = "java.util.ArrayList$SubList";
	private static  ClassInfo unmodify;
	static {
		
		try {
			ArrayList list = new ArrayList();
			list.add(new Object());
			list.add(new Object());list.add(new Object());
			list.subList(0, 2).getClass();
			unmodify = ClassUtil.getClassInfo(list.subList(0, 2).getClass());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			unmodify = null;
		}
	}
	public SublistPreSerial() {
		
	}

	public List prehandle(List object) {
		ArrayList list = new ArrayList();
		list.addAll(object);
		return list;
	}

	public List posthandle(List object) {
		// TODO Auto-generated method stub
		if(object == null)
			return object;
		return object.subList(0, object.size() - 1);
	}

	public String getClazz() {
		// TODO Auto-generated method stub
		return this.clazz;
	}

}
