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
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Security;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

import org.frameworkset.security.ecc.BaseECCCoder;




/**
 * <p>Title: BCRSACoder.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2008</p>
 * @Date 2014年4月25日
 * @author biaoping.yin
 * @version 3.8.0
 */
public class BCRSACoder extends BaseECCCoder {
	static
	{
		Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
	}
	public  PrivateKey _evalECPrivateKey(byte[] privateKey)
	{
		
			try {
				
				// 对密钥解密
				PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(privateKey);
				KeyFactory keyFactory = KeyFactory.getInstance("RSA", "BC");
	
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
				KeyFactory keyFactory = KeyFactory.getInstance("RSA", "BC");

				PublicKey pubKey = (PublicKey) keyFactory
						.generatePublic(x509KeySpec);
				
				return pubKey;
			} catch (Exception e) {
				throw new java.lang.RuntimeException(e);
			}
		
		
	}
	@Override
	public byte[] decrypt(byte[] data, PrivateKey priKey_) throws Exception {
		 Cipher	         cipher = Cipher.getInstance("RSA/NONE/PKCS1Padding", "BC");
		  cipher.init(Cipher.DECRYPT_MODE, priKey_);

	     byte[] cipherText = cipher.doFinal(data);
		return cipherText;
	}

	@Override
	public byte[] encrypt(byte[] data, PublicKey pubKey_) throws Exception {
		Cipher	         cipher = Cipher.getInstance("RSA/NONE/PKCS1Padding", "BC");
		  cipher.init(Cipher.ENCRYPT_MODE, pubKey_);

	        byte[] cipherText = cipher.doFinal(data);
		return cipherText;
	}

	@Override
	public KeyPair _genECKeyPair() throws Exception {
	
//      SecureRandom     random = Utils.createFixedRandom();
      SecureRandom secrand = new SecureRandom();
		   secrand.setSeed(randomToken().getBytes()); // 初始化随机产生器
      
      // create the keys
      KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA", "BC");
      
      generator.initialize(1024, secrand);

      KeyPair          pair = generator.generateKeyPair();
     
		return  pair;
		
		
	}

}
