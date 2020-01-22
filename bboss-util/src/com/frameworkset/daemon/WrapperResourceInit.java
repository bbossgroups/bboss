package com.frameworkset.daemon;
/**
 * Copyright 2008 biaoping.yin
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

import com.frameworkset.util.ResourceInitial;
import com.frameworkset.util.UUIDResource;

/**
 * <p>Description: </p>
 * <p></p>
 * <p>Copyright (c) 2018</p>
 * @Date 2020/1/22 10:39
 * @author biaoping.yin
 * @version 1.0
 */
public class WrapperResourceInit  implements ResourceInitial, UUIDResource {
	private ResourceInitial resourceInitial;
	private String fileName;
	public WrapperResourceInit(ResourceInitial resourceInitial,String fileName){
		this.resourceInitial = resourceInitial;
		this.fileName = fileName;
	}

	@Override
	public void reinit() {
		resourceInitial.reinit();
	}

	@Override
	public String getUUID() {
		if(resourceInitial instanceof UUIDResource)
			return ((UUIDResource)resourceInitial).getUUID();
		return this.fileName;
	}

}
