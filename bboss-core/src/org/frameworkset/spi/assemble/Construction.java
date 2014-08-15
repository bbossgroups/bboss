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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * <p>Title: Construction.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2007</p>
 * @Date Feb 8, 2009 10:47:22 PM
 * @author biaoping.yin  
 * [
 * http://blog.csdn.net/yin_bp,
 * www.openbboss.com
 * www.openbboss.com.cn
 * www.openbboss.cn
 * ]
 * @version 1.0
 */
public class Construction implements Serializable {
	
	private List<Pro> params;
	
	public Construction()
	{
		params = new ArrayList<Pro>();
	}
	
	/**
	 * add param for construction
	 * @param param
	 */
	public void addParam(Pro param)
	{
		this.params.add(param);
	}
	
	public List<Pro> getParams()
	{
		return this.params;
	}
	
	public String toString()
	{
	    StringBuffer ret = new StringBuffer();
	    if(params == null || params.size() == 0)
	        return "";
	    for(Pro pro:params)
	    {
	        ret.append(pro.getClazz()).append(",");
	    }
	    return ret.toString();
	}

}
