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

import java.util.Locale;

/**
 * <p>Title: LocaleSerial.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2007</p>
 * @Date 2014年6月23日 上午9:06:32
 * @author biaoping.yin
 * @version 1.0
 */
public class LocaleSerial extends BaseSerial<Locale>{

	public LocaleSerial() {
		// TODO Auto-generated constructor stub
	}

	public String serialize(Locale object) {
		// TODO Auto-generated method stub
		return object.toString();
	}

	public Locale deserialize(String object) {
		// TODO Auto-generated method stub
		return new Locale(object);
	}

}
