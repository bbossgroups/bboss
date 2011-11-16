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

import java.util.Collection;
import java.util.Map;


/**
 * <p>EmptyTag.java</p>
 * <p> Description: 判断指定的字段的值是否是null或者是空串，如果条件成立，则执行标签体得逻辑</p>
 * <p> bboss workgroup </p>
 * <p> Copyright (c) 2009 </p>
 * 
 * @Date 2011-5-26
 * @author biaoping.yin
 * @version 1.0
 */
public class EmptyTag extends MatchTag
{

	@Override
	protected boolean match()
	{
		if(this.actualValue == null || this.actualValue.equals(""))
			return true;
		if(actualValue instanceof Collection && ((Collection)actualValue).size() == 0)
		{
			return true;
		}
		if(actualValue instanceof Map && ((Map)actualValue).size() == 0)
		{
			return true;
		}
		return false;
	}

}
