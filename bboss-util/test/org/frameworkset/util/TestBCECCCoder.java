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
import org.frameworkset.util.encoder.Hex;
import org.junit.Before;
import org.junit.Test;

/**
 * <p>Title: TestBCECCCoder.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2008</p>
 * @Date 2014年4月25日
 * @author biaoping.yin
 * @version 3.8.0
 */
public class TestBCECCCoder {
	@Before
	public void init() throws Exception
	{
//		test();
	}
	@Test
	public void test() throws Exception {
		String inputStr = "尹标平尹标平尹标平尹标平尹标平尹标平尹标平";
		
		byte[] data = inputStr.getBytes();
		String ddd = Hex.toHexString(data);
		System.out.println("hex ddd:"+ddd);
		byte[] data0 = Hex.decode(ddd);
		System.out.println("hex data0:"+new String(data0));
		
		
		ECCCoderInf ECCCoder = ECCHelper.getECCCoder(ECCHelper.ECC_BC);
		SimpleKeyPair keyMap = ECCCoder.genECKeyPair();

		String publicKey = keyMap.getPublicKey();
		String privateKey = keyMap.getPrivateKey();
		System.err.println("公钥: \n" + publicKey);
		System.err.println("私钥： \n" + privateKey);
		byte[] encodedData = ECCCoder.encrypt(data, publicKey);
		System.out.println("HEX密文:"+Hex.toHexString(encodedData));
		System.out.println("Base64 密文:"+Base64.encode(encodedData));
//		String sss = Base64.toHex(encodedData);
//		encodedData = Base64.hexStringToBytes(sss);
		byte[] decodedData = ECCCoder.decrypt(encodedData, privateKey);
		
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
	@Test
	public void testGenKey() throws Exception {
		
		ECCCoderInf ECCCoder = ECCHelper.getECCCoder(ECCHelper.ECC_BC);
		SimpleKeyPair keyMap = ECCCoder.genECKeyPair();

		String publicKey = keyMap.getPublicKey();
		String privateKey = keyMap.getPrivateKey();
		System.err.println("公钥: \n" + publicKey);
		System.err.println("私钥： \n" + privateKey);
		
		keyMap = ECCCoder.genECKeyPair();

		String publicKey1 = keyMap.getPublicKey();
		String privateKey1 = keyMap.getPrivateKey();
		System.err.println("公钥: \n" + publicKey1);
		System.err.println("私钥： \n" + privateKey1);
		System.err.println("公钥: \n" + publicKey.equals(publicKey1));
		System.err.println("私钥： \n" + privateKey.equals(privateKey1));

	}
}
