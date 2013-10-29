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
package org.frameworkset.soa;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>Title: SerialStack.java</p> 
 * <p>Description: 记录对象和对象引用标识，便于构建后续的引用节点</p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2008</p>
 * @Date 2012-1-6
 * @author biaoping.yin
 * @version 1.0
 */
public class SerialStack {
	private Map<Object,String> stack = new HashMap<Object,String>();
	public void addStack(Object address,String refid)
	{
		this.stack.put(address, refid);
	}
	public String getRefID(Object address)
	{
		return this.stack.get(address);
	}
	
	public void clear()
	{
		this.stack.clear();
		this.stack = null;
	}
	

}
