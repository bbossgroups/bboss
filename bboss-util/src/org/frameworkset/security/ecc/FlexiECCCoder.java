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
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Cipher;

import org.frameworkset.util.Base64;

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
public class FlexiECCCoder implements ECCCoderInf{
	static
	{
		Security.addProvider(new FlexiCoreProvider());
		Security.addProvider(new FlexiECProvider());
	}
	private  Map<String,PrivateKey> PrivateKeyIndex = new HashMap<String,PrivateKey>();
	private  Map<String,PublicKey> ECPublicKeyIndex = new HashMap<String,PublicKey>();
	private  Map<String,SimpleKeyPair> PrivateKeyPairIndex = new HashMap<String,SimpleKeyPair>();
	private  Map<String,SimpleKeyPair> ECPublicKeyPairIndex = new HashMap<String,SimpleKeyPair>();
	
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
				KeyFactory keyFactory = new ECKeyFactory();
	
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
				KeyFactory keyFactory = new ECKeyFactory();

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
	public  byte[] decrypt(byte[] data, String privatekey) throws Exception {
		PrivateKey priKey = evalECPrivateKey(privatekey);
		return decrypt( data,  priKey) ;
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
	public  byte[] decrypt(String database64, String privatekey) throws Exception {
		byte[] data = Base64.decode(database64);
		return decrypt(data,  privatekey);
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
	public  byte[] decrypt(String database64, PrivateKey priKey) throws Exception {
		
		
		return decrypt(Base64.decode(database64),  priKey) ;
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
	public  byte[] encrypt(byte[] data, String publicKey)
			throws Exception {
		
		
		
		PublicKey pubKey = evalECPublicKey(publicKey);
		return encrypt( data,  pubKey);
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
	public  byte[] encrypt(String data, String publicKey)
			throws Exception {
		
		
		
		PublicKey pubKey = evalECPublicKey(publicKey);
		return encrypt( data,  pubKey);
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
	public  SimpleKeyPair genECKeyPair() throws Exception {
		KeyPairGenerator kpg = KeyPairGenerator.getInstance("ECIES", "FlexiEC");
		
		CurveParams ecParams = new BrainpoolP160r1();

		kpg.initialize(ecParams, new SecureRandom());
		KeyPair keyPair = kpg.generateKeyPair();
		PublicKey pubKey = keyPair.getPublic();
		PrivateKey privKey = keyPair.getPrivate();
		String sprivateKey = Base64.encode(privKey.getEncoded());
		String spublicKey = Base64.encode(pubKey.getEncoded());
		SimpleKeyPair ECKeyPair = new SimpleKeyPair(sprivateKey, spublicKey,
				pubKey, privKey);
		PrivateKeyPairIndex.put(sprivateKey, ECKeyPair);
		ECPublicKeyPairIndex.put(spublicKey, ECKeyPair);
		return ECKeyPair;
		
	}

}
