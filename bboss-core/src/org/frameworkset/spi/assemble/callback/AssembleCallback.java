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

package org.frameworkset.spi.assemble.callback;

/**
 * <p>Title: AssembleCallback.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2007</p>
 * @Date 2010-10-3 下午10:46:21
 * @author biaoping.yin
 * @version 1.0
 */
public interface AssembleCallback {
	final String webprex = "web::";
	final String classpathprex = "classpath::";
	/**
	 * 由具体的实现将传入的路径path转换为具体环境的绝对路径
	 * @param path
	 * @return
	 */
	String getDocbasePath(String path);
	
	String getDocbaseType();
	
	String getRootPath();
	

}
