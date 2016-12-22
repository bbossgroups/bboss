/**
 *
 * @copyright Copyright (c) 1998-2014 Tencent Inc.
 */

// ------------------------------------------------------------------------

package org.frameworkset.security;

import java.security.MessageDigest;
import java.util.Arrays;

import org.frameworkset.security.AesException;

/**
 * SHA1 class
 *
 * 计算消息签名接口.
 */
public class SHA1 {

	 
	public static String getSHA1(String token,String data, String timestamp, String nonce) throws AesException {
		try {
			String[] array = new String[] { token, timestamp, nonce ,data};
			StringBuilder sb = new StringBuilder();
			// 字符串排序
			Arrays.sort(array);
			for (int i = 0; i < array.length; i++) {
				sb.append(array[i]);
			}
			String str = sb.toString();
			// SHA1签名生成
			MessageDigest md = MessageDigest.getInstance("SHA-1");
			md.update(str.getBytes());
			byte[] digest = md.digest();

			StringBuilder hexstr = new StringBuilder();
			String shaHex = "";
			for (int i = 0; i < digest.length; i++) {
				shaHex = Integer.toHexString(digest[i] & 0xFF);
				if (shaHex.length() < 2) {
					hexstr.append(0);
				}
				hexstr.append(shaHex);
			}
			return hexstr.toString();
		} catch (Exception e) {
			e.printStackTrace();
			throw new AesException(AesException.ComputeSignatureError);
		}
	}
}
