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
package com.frameworkset.common;

import com.frameworkset.common.poolman.DBUtil;
import org.frameworkset.security.DESCipher;

public class TestEncrypt {
	public static void main(String[] args) throws Exception
	{
		DESCipher aa = new DESCipher();
		String bb = aa.encrypt("123456");
		System.out.println(bb);
		System.out.println(aa.decrypt(bb));
		
		bb = aa.encrypt("root");
		System.out.println("user:"+bb);
		System.out.println(aa.decrypt(bb));
		
		bb = aa.encrypt("jdbc:mysql://localhost:3306/cim");
		System.out.println("url:"+bb);
		System.out.println(aa.decrypt(bb));
		DBUtil.getConection();
	}

}
