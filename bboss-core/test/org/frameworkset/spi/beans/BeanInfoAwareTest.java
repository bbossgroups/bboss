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

package org.frameworkset.spi.beans;

import org.frameworkset.spi.InitializingBean;

/**
 * <p>Title: BeanInfoAware.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2007</p>
 * @Date 2012-3-2 上午09:52:40
 * @author biaoping.yin
 * @version 1.0
 */
public class BeanInfoAwareTest extends org.frameworkset.spi.BeanInfoAware implements InitializingBean {
	public void afterPropertiesSet() throws Exception {
		String extrattr = super.beaninfo.getStringExtendAttribute("extrattr");//获取扩展属性值
		String extrattr_default = super.beaninfo.getStringExtendAttribute("extrattr","defaultvalue");//获取扩展属性值，如果没有指定则返回后面的默认值
	}
}
