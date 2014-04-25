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
package org.frameworkset.security.ecc;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Security;

import javax.crypto.Cipher;



import org.frameworkset.util.encoder.Hex;

import de.flexiprovider.api.keys.KeyFactory;
import de.flexiprovider.common.ies.IESParameterSpec;
import de.flexiprovider.core.FlexiCoreProvider;
import de.flexiprovider.ec.FlexiECProvider;
import de.flexiprovider.ec.keys.ECKeyFactory;
import de.flexiprovider.ec.parameters.CurveParams;
import de.flexiprovider.ec.parameters.CurveRegistry.BrainpoolP160r1;
import de.flexiprovider.pki.PKCS8EncodedKeySpec;
import de.flexiprovider.pki.X509EncodedKeySpec;

/**
 * <p>Title: FlexiECCCoder.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2008</p>
 * @Date 2014年4月22日
 * @author biaoping.yin
 * @version 3.8.0
 */
public class FlexiECCCoder extends BaseECCCoder{
	static
	{
		Security.addProvider(new FlexiCoreProvider());
		Security.addProvider(new FlexiECProvider());
	}
	
	
	public  PrivateKey _evalECPrivateKey(byte[] privateKey)
	{
			try {
				
				// 对密钥解密
				PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(privateKey);
				KeyFactory keyFactory = new ECKeyFactory();
	
				PrivateKey priKey = (PrivateKey) keyFactory
						.generatePrivate(pkcs8KeySpec);
				return priKey;
			} catch (Exception e) {
				throw new java.lang.RuntimeException(e);
			}
	}
	
	public  PublicKey _evalECPublicKey(byte[] publicKey)
	{
		
			try {
				// 对公钥解密

				// 取得公钥
				X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(publicKey);
				KeyFactory keyFactory = new ECKeyFactory();

				PublicKey pubKey = (PublicKey) keyFactory
						.generatePublic(x509KeySpec);
				return pubKey;
			} catch (Exception e) {
				throw new java.lang.RuntimeException(e);
			}
		
	}


	
	
	/**
	 * 解密<br>
	 * 用私钥解密
	 * 
	 * @param data
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public  byte[] decrypt(byte[] data, PrivateKey priKey) throws Exception {
		// 对数据解密
		// TODO Chipher不支持EC算法 未能实现
		Cipher cipher = Cipher.getInstance("ECIES", "FlexiEC");
		IESParameterSpec iesParams = new IESParameterSpec("AES128_CBC",
				"HmacSHA1", null, null);
		cipher.init(Cipher.DECRYPT_MODE, priKey, iesParams);
		return cipher.doFinal(data);
	}

	
	
	/**
	 * 加密<br>
	 * 用公钥加密
	 * 
	 * @param data
	 * @param privateKey
	 * @return
	 * @throws Exception
	 */
	public  byte[] encrypt(byte[] data, PublicKey pubKey)
			throws Exception {
		

		Cipher cipher = Cipher.getInstance("ECIES", "FlexiEC");

		IESParameterSpec iesParams = new IESParameterSpec("AES128_CBC",
				"HmacSHA1", null, null);
		cipher.init(Cipher.ENCRYPT_MODE, pubKey, iesParams);
		return cipher.doFinal(data);
	}
	
	/**
	 * 加密<br>
	 * 用公钥加密
	 * 
	 * @param data
	 * @param privateKey
	 * @return
	 * @throws Exception
	 */
	public  byte[] encrypt(String data, PublicKey pubKey)
			throws Exception {
		

		return encrypt(data.getBytes(),  pubKey);
	}


	
	/**
	 * 初始化密钥
	 * 
	 * @return
	 * @throws Exception
	 */
	public  KeyPair _genECKeyPair() throws Exception {
		KeyPairGenerator kpg = KeyPairGenerator.getInstance("ECIES", "FlexiEC");
		
		CurveParams ecParams = new BrainpoolP160r1();
		  SecureRandom secrand = new SecureRandom();
		   secrand.setSeed(randomToken().getBytes()); // 初始化随机产生器
		kpg.initialize(ecParams, secrand);
		KeyPair keyPair = kpg.generateKeyPair();
		
		return keyPair;
		
	}

}
