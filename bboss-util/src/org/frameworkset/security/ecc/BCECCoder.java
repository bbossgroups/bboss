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
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Security;
import java.security.spec.ECGenParameterSpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

import org.bouncycastle.jce.spec.IESParameterSpec;
import org.frameworkset.util.encoder.Hex;


/**
 * <p>Title: BCECCoder.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2008</p>
 * @Date 2014年4月25日
 * @author biaoping.yin
 * @version 3.8.0
 */
public class BCECCoder extends BaseECCCoder {
	static
	{
		Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
	}
	@Override
	public byte[] decrypt(byte[] data, PrivateKey priKey_) throws Exception {
//		byte[] derivation = Hex.decode("202122232425262728292a2b2c2d2e2f");
//        byte[] encoding   = Hex.decode("303132333435363738393a3b3c3d3e3f");
//        IESParameterSpec params = new IESParameterSpec(derivation,encoding,128);
		Cipher cipher = Cipher.getInstance("ECIES");
		
		
//		 cipher.init(Cipher.DECRYPT_MODE, priKey_, params, new SecureRandom());
		 cipher.init(Cipher.DECRYPT_MODE, priKey_, new SecureRandom());
		 
//		
		return cipher.doFinal(data);
		
	}

	@Override
	public byte[] encrypt(byte[] data, PublicKey pubKey_) throws Exception {
//		byte[] derivation = Hex.decode("202122232425262728292a2b2c2d2e2f");
//        byte[] encoding   = Hex.decode("303132333435363738393a3b3c3d3e3f");
//        IESParameterSpec params = new IESParameterSpec(derivation,encoding,128);
		Cipher cipher = Cipher.getInstance("ECIES");
		
		
//		 cipher.init(Cipher.ENCRYPT_MODE, pubKey_, params, new SecureRandom());
		 cipher.init(Cipher.ENCRYPT_MODE, pubKey_, new SecureRandom());
		 
		return cipher.doFinal(data);
	}

	@Override
	public KeyPair _genECKeyPair() throws Exception {
		 KeyPairGenerator   keyGen = KeyPairGenerator.getInstance("EC", "BC");
//	        ECGenParameterSpec ecSpec = new ECGenParameterSpec("prime192v1");
//	        SecureRandom secrand = new SecureRandom();
//			   secrand.setSeed(randomToken().getBytes()); // 初始化随机产生器
//	        keyGen.initialize(ecSpec, secrand);	       
	        KeyPair pair = keyGen.generateKeyPair();   
	        return pair;
	}

	@Override
	public PrivateKey _evalECPrivateKey(byte[] privateKey) {
		try {
			KeyFactory fact = KeyFactory.getInstance("EC", "BC");
			PrivateKey priKey = fact.generatePrivate(new PKCS8EncodedKeySpec(privateKey));
			return priKey;
		} catch (Exception e) {
			throw new java.lang.RuntimeException(e);
		} 
	}

	@Override
	public PublicKey _evalECPublicKey(byte[] publicKey) {
		try {
			KeyFactory fact = KeyFactory.getInstance("EC", "BC");
			PublicKey pubKey = fact.generatePublic(new X509EncodedKeySpec(publicKey));
			return pubKey;
		} catch (Exception e) {
			throw new java.lang.RuntimeException(e);
		} 
	}

}
