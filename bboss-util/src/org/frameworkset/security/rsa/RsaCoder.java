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
package org.frameworkset.security.rsa;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

import org.frameworkset.security.ecc.BaseECCCoder;
import org.frameworkset.security.ecc.SimpleKeyPair;
import org.frameworkset.util.Base64;



/**
 * <p>Title: RsaCoder.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2008</p>
 * @Date 2014年4月22日
 * @author biaoping.yin
 * @version 3.8.0
 */
public class RsaCoder extends BaseECCCoder {
	/** 指定加密算法为RSA */
	private static String ALGORITHM = "RSA";
	/** 指定key的大小 */
	private static int KEYSIZE = 1024;

	public  PrivateKey evalECPrivateKey(String privateKey)
	{
		PrivateKey priKey = PrivateKeyIndex.get(privateKey);
		if(priKey != null)
			return priKey;
		synchronized(PrivateKeyIndex)
		{
			priKey = PrivateKeyIndex.get(privateKey);
			if(priKey != null)
				return priKey;
			try {
				
				// 对密钥解密
				byte[] keyBytes = Base64.decode(privateKey);
				PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
				KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
	
				 priKey = (PrivateKey) keyFactory
						.generatePrivate(pkcs8KeySpec);
				 PrivateKeyIndex.put(privateKey, priKey);
				return priKey;
			} catch (Exception e) {
				throw new java.lang.RuntimeException(e);
			}
		}
	}
	
	public  PublicKey evalECPublicKey(String publicKey)
	{
		
		PublicKey pubKey = ECPublicKeyIndex.get(publicKey);
		if(pubKey != null)
			return pubKey;
		synchronized(ECPublicKeyIndex)
		{
			pubKey = ECPublicKeyIndex.get(publicKey);
			if(pubKey != null)
				return pubKey;
			try {
				// 对公钥解密
				byte[] keyBytes = Base64.decode(publicKey);

				// 取得公钥
				X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
				KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);

				pubKey = (PublicKey) keyFactory
						.generatePublic(x509KeySpec);
				ECPublicKeyIndex.put(publicKey, pubKey);
				return pubKey;
			} catch (Exception e) {
				throw new java.lang.RuntimeException(e);
			}
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
		Cipher cipher = Cipher.getInstance(ALGORITHM);
		cipher.init(Cipher.DECRYPT_MODE, priKey);
		
		/** 执行解密操作 */
		byte[] b = cipher.doFinal(data);
		return b;
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
		

		Cipher cipher = Cipher.getInstance(ALGORITHM);
		cipher.init(Cipher.ENCRYPT_MODE, pubKey);
		/** 执行加密操作 */
		byte[] b1 = cipher.doFinal(data);
		return b1;
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

	@Override
	public SimpleKeyPair genECKeyPair() throws Exception {
				java.security.KeyPairGenerator keygen = java.security.KeyPairGenerator
			     .getInstance(ALGORITHM);
			   SecureRandom secrand = new SecureRandom();
			   
			   secrand.setSeed(randomToken().getBytes()); // 初始化随机产生器
			   keygen.initialize(KEYSIZE, secrand);
			   KeyPair keys = keygen.genKeyPair();
			 
			   PublicKey pubkey = keys.getPublic();
			   PrivateKey prikey = keys.getPrivate();
			 
			   String sprivateKey = Base64.encode(prikey.getEncoded());
				String spublicKey = Base64.encode(pubkey.getEncoded());
				SimpleKeyPair ECKeyPair = new SimpleKeyPair(sprivateKey, spublicKey,
						pubkey, prikey);
				PrivateKeyPairIndex.put(sprivateKey, ECKeyPair);
				ECPublicKeyPairIndex.put(spublicKey, ECKeyPair);
				return ECKeyPair;
	}

}
