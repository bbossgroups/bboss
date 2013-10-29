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

import org.frameworkset.spi.BaseApplicationContext;

/**
 * 
 * 
 * <p>Title: Reference.java</p>
 *
 * <p>Description: 服务提供者引用其他引用服务者的信息</p>
 *
 * <p>Copyright: Copyright (c) 2007</p>
 *
 * <p>bboss workgroup</p>
 * @Date Aug 13, 2008 10:43:38 AM
 * @author biaoping.yin,尹标平
 * @version 1.0
 */
public class Reference extends Pro{
	
	public Reference(BaseApplicationContext applicationContext)
    {
        super(applicationContext);
        // TODO Auto-generated constructor stub
    }
    /**引用字段名称*/
	private String fieldname;
	
//	
	public String getFieldname() {
		return fieldname;
	}
	public void setFieldname(String fieldname) {
		this.fieldname = fieldname;
	}
}
