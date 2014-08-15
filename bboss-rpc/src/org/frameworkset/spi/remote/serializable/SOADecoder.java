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

package org.frameworkset.spi.remote.serializable;

import java.io.InputStream;

import org.frameworkset.soa.ObjectSerializable;

/**
 * <p>Title: SOADecoder.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2007</p>
 * @Date 2011-12-7 下午4:26:40
 * @author biaoping.yin
 * @version 1.0
 */
public class SOADecoder  implements Decoder{

	public Object decoder(Object msg)  throws Exception{
		if(msg instanceof String)
		{
			return ObjectSerializable.toBean((String)msg, Object.class);
		}
		else if(msg instanceof InputStream)
		{
			return ObjectSerializable.toBean((InputStream)msg, Object.class);
		}
		else //直接返回
			return msg;
			
	}

}
