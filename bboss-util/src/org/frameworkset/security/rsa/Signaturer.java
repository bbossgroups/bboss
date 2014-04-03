package org.frameworkset.security.rsa;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;

import org.frameworkset.util.Base64;

public class Signaturer {

	public static String sign(String priKeyText, String plainText) {
		try {
			PKCS8EncodedKeySpec priPKCS8 = new PKCS8EncodedKeySpec(
					Base64.decode(priKeyText));
			KeyFactory keyf = KeyFactory.getInstance("RSA");
			PrivateKey prikey = keyf.generatePrivate(priPKCS8);

			// 用私钥对信息生成数字签名
			java.security.Signature signet = java.security.Signature
					.getInstance("MD5withRSA");
			signet.initSign(prikey);
			signet.update(plainText.getBytes());
			String signed = Base64.encode(signet.sign());
			return signed;
		} catch (java.lang.Exception e) {
			System.out.println("签名失败");
			e.printStackTrace();
		}
		return null;
	}
}
