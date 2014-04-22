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

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.frameworkset.util.Base64;

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
	@Override
	public PrivateKey evalECPrivateKey(String privateKey) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PublicKey evalECPublicKey(String publicKey) {
		// TODO Auto-generated method stub
		return null;
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

	@Override
	public SimpleKeyPair genECKeyPair() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
