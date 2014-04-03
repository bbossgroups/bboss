package org.frameworkset.security.rsa;

import org.frameworkset.util.Base64;

public class SignProvider {
	private SignProvider() {

	}

	/**
	 * 
	 * Description:校验数字签名,此方法不会抛出任务异常,成功返回true,失败返回false,要求全部参数不能为空
	 * 
	 * @param pubKeyText
	 *            公钥,base64编码
	 * @param plainText
	 *            明文
	 * @param signTest
	 *            数字签名的密文,base64编码
	 * @return 校验成功返回true 失败返回false
	 * @author 孙钰佳
	 * @since：2007-12-27 上午09:33:55
	 */
	public static boolean verify(String pubKeyText, String plainText,
			String signText) {
		try {
			// 解密由base64编码的公钥,并构造X509EncodedKeySpec对象
			java.security.spec.X509EncodedKeySpec bobPubKeySpec = new java.security.spec.X509EncodedKeySpec(
					Base64.decode(pubKeyText));
			// RSA对称加密算法
			java.security.KeyFactory keyFactory = java.security.KeyFactory
					.getInstance("RSA");
			// 取公钥匙对象
			java.security.PublicKey pubKey = keyFactory
					.generatePublic(bobPubKeySpec);
			// 解密由base64编码的数字签名
			byte[] signed = Base64.decode(signText);
			java.security.Signature signatureChecker = java.security.Signature
					.getInstance("MD5withRSA");
			signatureChecker.initVerify(pubKey);
			signatureChecker.update(plainText.getBytes());
			// 验证签名是否正常
			if (signatureChecker.verify(signed))
				return true;
			else
				return false;
		} catch (Throwable e) {
			System.out.println("校验签名失败");
			e.printStackTrace();
			return false;
		}
	}
}
