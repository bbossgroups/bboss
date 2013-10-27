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
package com.frameworkset.common.tag.pager.tags;



/**
 * <p>MapKeyTag.java</p>
 * <p> Description: map标签结合使用，返回当前map元素对应的key值</p>
 * <p> bboss workgroup </p>
 * <p> Copyright (c) 2009 </p>
 * 
 * @Date 2011-7-31
 * @author biaoping.yin
 * @version 1.0
 */
public class MapKeyTag extends CellTag
{

	protected Object getObjectValue() {
		if(this.dataSet != null)
		{
			return this.dataSet.getMapKey();
		}
		return null;
	}

}
