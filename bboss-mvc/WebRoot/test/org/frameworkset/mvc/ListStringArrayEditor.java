/**
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
package org.frameworkset.mvc;

import java.util.ArrayList;
import java.util.List;

import com.frameworkset.util.ArrayEditorInf;

/**
 * <p> ListStringArrayEditor.java</p>
 * <p> Description: </p>
 * <p> bboss workgroup </p>
 * <p> Copyright (c) 2009 </p>
 * 
 * @Date 2012-11-21 下午5:27:57
 * @author biaoping.yin
 * @version 1.0
 */
public class ListStringArrayEditor implements ArrayEditorInf<List<String[]>> {

	@Override
	public List<String[]> getValueFromObject(Object fromValue) {
		if(fromValue == null)
			return null;
		if(fromValue instanceof String[])
		{
			String[] datas = (String[])fromValue; 
			if(datas.length<=0)
				return null;
			List<String[]> ret = new ArrayList<String[]>();
			
			for(String data :datas)
			{
				String[] tt = data.split(",");
				ret.add(tt);
			}
			return ret;
		}
		return null;
		
	}

	@Override
	public List<String[]> getValueFromString(String fromValue) {
		return null;
	}
}
