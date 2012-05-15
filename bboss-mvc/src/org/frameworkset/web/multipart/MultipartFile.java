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
package org.frameworkset.web.multipart;

import org.frameworkset.util.BigFile;
/**
 * 
 * <p>Title: MultipartFile.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2008-2010</p>
 * @Date 2010-10-2
 * @author biaoping.yin
 * @version 1.0
 */
public interface MultipartFile extends BigFile{
	/**
	 * Return the name of the parameter in the multipart form.
	 * @return the name of the parameter (never <code>null</code> or empty)
	 */
	String getName();
	void destroy();
	String getStorageDescription();
	
	

	

}
