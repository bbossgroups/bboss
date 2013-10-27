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
package org.frameworkset.web.tag;

import org.frameworkset.spi.support.validate.FieldError;
import org.frameworkset.spi.support.validate.ObjectError;


/**
 * <p>Title: GlobalErrors.java</p> 
 * <p>Description: 全局错误信息标签</p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2008-2010</p>
 * @Date 2011-1-9
 * @author biaoping.yin
 * @version 1.0
 */
public class GlobalErrorsTag extends ErrorsTag {
	
	protected boolean suppport(ObjectError current_error)
	{
		return !(current_error instanceof FieldError);
	}

	

}
