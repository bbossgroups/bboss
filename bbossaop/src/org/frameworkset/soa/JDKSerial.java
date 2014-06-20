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

import java.io.Serializable;

import org.frameworkset.util.ObjectUtils;

import com.frameworkset.util.ValueObjectUtil;

/**
 * <p>Title: LocalSerial.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2007</p>
 * @Date 2014年5月23日 上午9:06:42
 * @author biaoping.yin
 * @version 1.0
 */
public class JDKSerial extends BaseSerial<java.io.Serializable> {

	public String serialize(Serializable object) {
		// TODO Auto-generated method stub
		
		return ValueObjectUtil
				.byteArrayEncoder(ObjectUtils.toBytes(object));
		
	}

	
	public Serializable deserialize(String object) {
		try
		{
			byte[] bytes = ValueObjectUtil.byteArrayDecoder(object);
			return (Serializable)ObjectUtils.toObject(bytes);
		}
		catch(RuntimeException e)
		{
			throw e;
		}
		catch(Exception d)
		{
			throw new SerialException(d);
		}
	}	
	

}
