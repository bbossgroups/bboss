package org.frameworkset.util;

import org.frameworkset.security.ecc.ECCCoder;
import org.frameworkset.security.ecc.ECCCoder.ECKeyPair;
import org.junit.Test;

public class ECCCoderTest {
	@Test
	public void test() throws Exception {
		String inputStr = "尹标平";
		byte[] data = inputStr.getBytes();

		ECKeyPair keyMap = ECCCoder.genECKeyPair();

		String publicKey = keyMap.getPublicKey();
		String privateKey = keyMap.getPrivateKey();
		System.err.println("公钥: \n" + publicKey);
		System.err.println("私钥： \n" + privateKey);
		byte[] encodedData = ECCCoder.encrypt(data, publicKey);
		byte[] decodedData = ECCCoder.decrypt(encodedData, privateKey);
		inputStr = "解密局解密局";
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
}
