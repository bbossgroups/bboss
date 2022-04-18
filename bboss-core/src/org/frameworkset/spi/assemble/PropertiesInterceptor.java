package org.frameworkset.spi.assemble;
/**
 * Copyright 2022 bboss
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

/**
 * <p>Description: 配置属性加载完毕后，对属性进行预处理</p>
 * <p></p>
 * <p>Copyright (c) 2020</p>
 * @Date 2022/4/17
 * @author biaoping.yin
 * @version 1.0
 */
public interface PropertiesInterceptor {
	/**
	 * 对加载的属性值进行拦截处理，用处理后的值替换原来的值，常用于对加密数据的解密处理
	 * @param propertyContext 包含property和value两个属性
	 *   property 属性名称 如果value是复杂对象，property可能为空
	 *   value  属性值
	 *	property为空时，只需要处理value即可,并且复杂对象处理后的值必须设置回复杂对象
	 *   会忽略返回值
	 * @return 返回处理加工后的新值
	 */
	public Object convert(PropertyContext propertyContext);
}
