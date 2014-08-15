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

/**
 * <p>Title: PreSerial.java</p> 
 * <p>Description: 对序列化对象进行预处理</p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2007</p>
 * @Date 2014年6月20日 下午11:46:46
 * @author biaoping.yin
 * @version 1.0
 */
public interface PreSerial<T> {
	/**
	 * 获取预处理前的类型路径
	 * @return
	 */
	public String getClazz();
	/**
	 * 在序列化之前调用prehandle方法预处理对象
	 * @param object
	 * @return
	 */
	public T prehandle(T object);
	/**
	 * 在反序列化后调用posthandle方法处理生成的对象
	 * @param object
	 * @return
	 */
	public T posthandle(T object);
}
