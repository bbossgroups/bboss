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
 * <p>Description: 对加载的属性值进行拦截处理，用处理后的值替换原来的值，常用于对加密数据的解密处理</p>
 * <p></p>
 * <p>Copyright (c) 2020</p>
 * @Date 2022/4/18
 * @author biaoping.yin
 * @version 1.0
 */
public class PropertyContext {
	/**
	 * 属性名称 如果value是复杂对象，那么property可能为空，那么只需要处理value即可
	 */
	private Object property;
	/**
	 * 属性值
	 */
	private Object value;

	public Object getProperty() {
		return property;
	}

	public void setProperty(Object property) {
		this.property = property;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}
	@Override
	public String toString(){

		if(property != null) {
			StringBuilder ret = new StringBuilder();
			ret.append(property).append("=").append(value);
			return ret.toString();
		}
		else
			return String.valueOf(value);

	}
}
