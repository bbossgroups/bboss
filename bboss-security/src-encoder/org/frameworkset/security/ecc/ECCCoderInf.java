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

/**
 * <p>Title: ECCCoderInf.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2008</p>
 * @Date 2014年4月22日
 * @author biaoping.yin
 * @version 3.8.0
 */
public interface ECCCoderInf {

	public abstract PrivateKey evalECPrivateKey(String privateKey);

	public abstract PublicKey evalECPublicKey(String publicKey);

	/**
	 * 解密<br>
	 * 用私钥解密
	 * 
	 * @param data
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public abstract byte[] decrypt(byte[] data, String privatekey)
			throws Exception;

	/**
	 * 解密<br>
	 * 用私钥解密
	 * 
	 * @param data
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public abstract byte[] decrypt(String database64, String privatekey)
			throws Exception;

	/**
	 * 解密<br>
	 * 用私钥解密
	 * 
	 * @param data
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public abstract byte[] decrypt(String database64, PrivateKey priKey)
			throws Exception;

	/**
	 * 解密<br>
	 * 用私钥解密
	 * 
	 * @param data
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public abstract byte[] decrypt(byte[] data, PrivateKey priKey_)
			throws Exception;

	/**
	 * 加密<br>
	 * 用公钥加密
	 * 
	 * @param data
	 * @param privateKey
	 * @return
	 * @throws Exception
	 */
	public abstract byte[] encrypt(byte[] data, String publicKey)
			throws Exception;

	public abstract byte[] encrypt(String data, String publicKey)
			throws Exception;

	/**
	 * 加密<br>
	 * 用公钥加密
	 * 
	 * @param data
	 * @param privateKey
	 * @return
	 * @throws Exception
	 */
	public abstract byte[] encrypt(byte[] data, PublicKey pubKey_)
			throws Exception;

	public abstract byte[] encrypt(String data, PublicKey pubKey_)
			throws Exception;

	/**
	 * 初始化密钥
	 * 
	 * @return
	 * @throws Exception
	 */
	public abstract SimpleKeyPair genECKeyPair() throws Exception;

}