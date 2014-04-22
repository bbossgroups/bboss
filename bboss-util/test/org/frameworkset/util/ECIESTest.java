/*
 *  Copyright 2008 bbossgroups
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
package org.frameworkset.util;

import org.frameworkset.security.ecc.ECCCoderInf;
import org.frameworkset.security.ecc.ECCHelper;
import org.frameworkset.security.ecc.SimpleKeyPair;
import org.junit.Before;
import org.junit.Test;

/**
 * <p>Title: ECIESTest.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2008</p>
 * @Date 2014年4月22日
 * @author biaoping.yin
 * @version 3.8.0
 */
public class ECIESTest {
	@Before
	public void init() throws Exception
	{
		test();
	}
	@Test
	public void test() throws Exception {
		String inputStr = "尹标平";
		byte[] data = inputStr.getBytes();
		ECCCoderInf ECCCoder = ECCHelper.getECCCoder(ECCHelper.ECC_ECIES);
		SimpleKeyPair keyMap = ECCCoder.genECKeyPair();

		String publicKey = keyMap.getPublicKey();
		String privateKey = keyMap.getPrivateKey();
		System.err.println("公钥: \n" + publicKey);
		System.err.println("私钥： \n" + privateKey);
		byte[] encodedData = ECCCoder.encrypt(data, publicKey);
		System.out.println("明文:"+Base64.encode(inputStr.getBytes()));
		System.out.println("密文:"+Base64.encode(encodedData));
		byte[] decodedData = ECCCoder.decrypt(encodedData, privateKey);
		inputStr = "解密局解密局";
		data = inputStr.getBytes();
		long start = System.currentTimeMillis();
		encodedData = ECCCoder.encrypt(data, ECCCoder.evalECPublicKey(publicKey));
		long end = System.currentTimeMillis();
		System.out.println("加密耗时:"+(end-start));
		
		start = System.currentTimeMillis();
		decodedData = ECCCoder.decrypt(encodedData, ECCCoder.evalECPrivateKey(privateKey));
		end = System.currentTimeMillis();
		System.out.println("解密耗时:"+(end-start));
		String outputStr = new String(decodedData);
		System.err.println("加密前: " + inputStr + "\n\r" + "解密后: " + outputStr);
		org.junit.Assert.assertEquals(inputStr, outputStr);
	}

}
