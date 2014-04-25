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

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.frameworkset.util.encoder.Hex;


/**
 * <p>Title: BaseECCCoder.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2008</p>
 * @Date 2014年4月22日
 * @author biaoping.yin
 * @version 3.8.0
 */
public abstract class BaseECCCoder implements ECCCoderInf {
	protected  Map<String,PrivateKey> PrivateKeyIndex = new HashMap<String,PrivateKey>();
	protected  Map<String,PublicKey> ECPublicKeyIndex = new HashMap<String,PublicKey>();
	protected  Map<String,SimpleKeyPair> PrivateKeyPairIndex = new HashMap<String,SimpleKeyPair>();
	protected  Map<String,SimpleKeyPair> ECPublicKeyPairIndex = new HashMap<String,SimpleKeyPair>();
	protected String randomToken()
	{
		String token = UUID.randomUUID().toString();
		return token;
	}
	public  abstract PrivateKey _evalECPrivateKey(byte[] privateKey);
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
				byte[] keyBytes = Hex.decode(privateKey);
				 priKey = _evalECPrivateKey( keyBytes);
				 PrivateKeyIndex.put(privateKey, priKey);
				return priKey;
			} catch (Exception e) {
				throw new java.lang.RuntimeException(e);
			}
		}
	}
	public  abstract PublicKey _evalECPublicKey(byte[] publicKey);
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
				byte[] keyBytes = Hex.decode(publicKey);

				pubKey = _evalECPublicKey(keyBytes);
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
		byte[] data = Hex.decode(database64);
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
		
		
		return decrypt(Hex.decode(database64),  priKey) ;
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
	public  byte[] encrypt(String data, PublicKey pubKey)
			throws Exception {
		

		return encrypt(data.getBytes(),  pubKey);
	}

	
	public abstract KeyPair _genECKeyPair() throws Exception ;
	public SimpleKeyPair genECKeyPair() throws Exception {
			KeyPair pair = _genECKeyPair();	      
	        PublicKey              pubKey = pair.getPublic();
	        PrivateKey              privKey = pair.getPrivate();
	        String sprivateKey = Hex.toHexString(privKey.getEncoded());
	  		String spublicKey = Hex.toHexString(pubKey.getEncoded());
	  		SimpleKeyPair ECKeyPair = new SimpleKeyPair(sprivateKey, spublicKey,
	  				pubKey, privKey);
	  		PrivateKeyPairIndex.put(sprivateKey, ECKeyPair);
	  		this.PrivateKeyIndex.put(sprivateKey, privKey);
	  		
	  		ECPublicKeyPairIndex.put(spublicKey, ECKeyPair);
	  		this.ECPublicKeyIndex.put(spublicKey, pubKey);
	  		return ECKeyPair;
	       
	}
	

}
