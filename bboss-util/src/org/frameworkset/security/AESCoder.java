/**
 * 
 */
package org.frameworkset.security;

import java.nio.charset.Charset;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * @author yinbp
 *
 * @Date:2016-11-13 21:53:19
 */
public class AESCoder {
	public static Charset CHARSET = Charset.forName("utf-8");
	private byte[] aesKey;

	/**
	 * 
	 */
	public AESCoder(String aesKey) {
//		if (aesKey.length() > 8)
//			aesKey = aesKey.substring(0, 7);
		this.aesKey = aesKey.getBytes(CHARSET);
	}

	/**
	 * 对明文进行加密.
	 * 
	 * @param text
	 *            需要加密的明文
	 * @return 加密后base64编码的字符串
	 * @throws Exception
	 * @throws AesException
	 *             aes加密失败
	 */
	public String encrypt(String text) throws Exception {

		try {
			byte[] textBytes = text.getBytes(CHARSET);

			KeyGenerator kgen = KeyGenerator.getInstance("AES"); // KeyGenerator提供（对称）密钥生成器的功能。使用getInstance
																	// 类方法构造密钥生成器。
			kgen.init(128, new SecureRandom(aesKey));// 使用用户提供的随机源初始化此密钥生成器，使其具有确定的密钥大小。
			SecretKey secretKey = kgen.generateKey();
			byte[] enCodeFormat = secretKey.getEncoded();
			SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");// 使用SecretKeySpec类来根据一个字节数组构造一个
																		// SecretKey,，而无须通过一个（基于
																		// provider
																		// 的）SecretKeyFactory.
			Cipher cipher = Cipher.getInstance("AES");// 创建密码器 //为创建 Cipher
														// 对象，应用程序调用 Cipher 的
														// getInstance 方法并将所请求转换
														// 的名称传递给它。还可以指定提供者的名称（可选）。
			cipher.init(Cipher.ENCRYPT_MODE, key);// 初始化
			byte[] result = cipher.doFinal(textBytes); // 按单部分操作加密或解密数据，或者结束一个多部分操作。数据将被加密或解密（具体取决于此
														// Cipher 的初始化方式）。
			// 使用BASE64对加密后的字符串进行编码
			String base64Encrypted = new String(CoderUtil.byteGrpToHexStr(result));

			return base64Encrypted;
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * 对密文进行解密.
	 * 
	 * @param text
	 *            需要解密的密文
	 * @return 解密得到的明文
	 * @throws AesException
	 *             aes解密失败
	 */
	public String decrypt(String text) throws Exception {

		try {
			KeyGenerator kgen = KeyGenerator.getInstance("AES");
			kgen.init(128, new SecureRandom(aesKey));
			SecretKey secretKey = kgen.generateKey();
			byte[] enCodeFormat = secretKey.getEncoded();
			SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
			Cipher cipher = Cipher.getInstance("AES");// 创建密码器
			cipher.init(Cipher.DECRYPT_MODE, key);// 初始化
			byte[] result = cipher.doFinal(CoderUtil.hexStrToByteGrp(text));

			return new String(result, CHARSET);
		} catch (Exception e) {

			throw e;
		}

	}
	
	 public static void main(String[] args) throws Exception{
		 AESCoder dd = new AESCoder("1234567812345678dddddddddd");
	    	String r = dd.encrypt("bbbbbbbbbbbbbbbbbbbbb");
	    	System.out.println(r);
	    	System.out.println(dd.decrypt(r));
	    }

}
