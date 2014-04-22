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

/**
 * <p>Title: ExampleECIES.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2008</p>
 * @Date 2014年4月22日
 * @author biaoping.yin
 * @version 3.8.0
 */
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Security;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;

import org.junit.Before;
import org.junit.Test;

import de.flexiprovider.common.ies.IESParameterSpec;
import de.flexiprovider.core.FlexiCoreProvider;
import de.flexiprovider.ec.FlexiECProvider;
import de.flexiprovider.ec.parameters.CurveParams;
import de.flexiprovider.ec.parameters.CurveRegistry.BrainpoolP160r1;

public class ExampleECIES {
	@Before
	public void init() throws Exception {

		Security.addProvider(new FlexiCoreProvider());
		Security.addProvider(new FlexiECProvider());

		KeyPairGenerator kpg = KeyPairGenerator.getInstance("ECIES", "FlexiEC");

		CurveParams ecParams = new BrainpoolP160r1();

		kpg.initialize(ecParams, new SecureRandom());
		KeyPair keyPair = kpg.generateKeyPair();
		PublicKey pubKey = keyPair.getPublic();
		PrivateKey privKey = keyPair.getPrivate();

		// Encrypt

		Cipher cipher = Cipher.getInstance("ECIES", "FlexiEC");

		IESParameterSpec iesParams = new IESParameterSpec("AES128_CBC",
				"HmacSHA1", null, null);

		cipher.init(Cipher.ENCRYPT_MODE, pubKey, iesParams);

		String cleartextFile = "readme.txt";
		String ciphertextFile = "ciphertextFile.txt";

		byte[] block = new byte[64];
		FileInputStream fis = new FileInputStream(cleartextFile);
		FileOutputStream fos = new FileOutputStream(ciphertextFile);
		CipherOutputStream cos = new CipherOutputStream(fos, cipher);

		int i;
		while ((i = fis.read(block)) != -1) {
			cos.write(block, 0, i);
		}
		cos.close();

		// Decrypt

		String cleartextAgainFile = "cleartextAgainECIES.txt";

		cipher.init(Cipher.DECRYPT_MODE, privKey, iesParams);

		fis = new FileInputStream(ciphertextFile);
		CipherInputStream cis = new CipherInputStream(fis, cipher);
		fos = new FileOutputStream(cleartextAgainFile);

		while ((i = cis.read(block)) != -1) {
			fos.write(block, 0, i);
		}
		fos.close();
	}

	@Test
	public void test() throws Exception {

		long start = System.currentTimeMillis();

		KeyPairGenerator kpg = KeyPairGenerator.getInstance("ECIES", "FlexiEC");

		CurveParams ecParams = new BrainpoolP160r1();

		kpg.initialize(ecParams, new SecureRandom());
		KeyPair keyPair = kpg.generateKeyPair();
		PublicKey pubKey = keyPair.getPublic();
		PrivateKey privKey = keyPair.getPrivate();

		// Encrypt

		Cipher cipher = Cipher.getInstance("ECIES", "FlexiEC");

		IESParameterSpec iesParams = new IESParameterSpec("AES128_CBC",
				"HmacSHA1", null, null);

		cipher.init(Cipher.ENCRYPT_MODE, pubKey, iesParams);

		String cleartextFile = "readme.txt";
		String ciphertextFile = "ciphertextFile1.txt";

		byte[] block = new byte[64];
		FileInputStream fis = new FileInputStream(cleartextFile);
		FileOutputStream fos = new FileOutputStream(ciphertextFile);
		CipherOutputStream cos = new CipherOutputStream(fos, cipher);

		int i;
		while ((i = fis.read(block)) != -1) {
			cos.write(block, 0, i);
		}
		cos.close();
		long end = System.currentTimeMillis();
		
		// Decrypt
		System.out.println("t:"+ (end -start));
		start = System.currentTimeMillis();
		String cleartextAgainFile = "cleartextAgainECIES1.txt";

		cipher.init(Cipher.DECRYPT_MODE, privKey, iesParams);

		fis = new FileInputStream(ciphertextFile);
		CipherInputStream cis = new CipherInputStream(fis, cipher);
		fos = new FileOutputStream(cleartextAgainFile);

		while ((i = cis.read(block)) != -1) {
			fos.write(block, 0, i);
		}
		fos.close();
		end = System.currentTimeMillis();
		System.out.println("d:"+ (end -start));
	}
	
	public void testEncrypt()
	{
		
	}

	

}
