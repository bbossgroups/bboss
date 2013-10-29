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


/**
 * <p>Title: ProParentInf.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2007</p>
 * @Date 2010-1-17 下午02:29:17
 * @author biaoping.yin
 * @version 1.0
 */
public abstract class ProParentInf
{
	/**
	 * 获取父容器中的引用对象，必须有效防止循环依赖注入
	 * 
	 * 获取引用变量容器包括：
	 * contruct
	 * pro
	 * promap
	 * proset
	 * prolist
	 * providermanagerinfo
	 * 
	 * @param refid
	 * @return
	 */
	public abstract Object getContainerRefValue(String refid);
}
