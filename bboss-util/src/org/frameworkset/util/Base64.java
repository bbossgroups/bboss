package org.frameworkset.util;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


import sun.misc.BASE64Decoder;

public class Base64 implements Serializable {
	/**
	 * base64 encode arithmetic
	 * @param s
	 * @return
	 */
	public static String getEncodeBase64(String s){
		if (s == null) return null;
		  return (new sun.misc.BASE64Encoder()).encode( s.getBytes() );

	}
	
	/**
	 */
	public static String encode(byte[] bytes){
		return (new sun.misc.BASE64Encoder()).encode(bytes);
	}
	/**
	 * base64 decode arithmetic
	 * @param s
	 * @return
	 */
	public static String getDecodeBase64(String s) {
		  if (s == null) return null;
		  BASE64Decoder decoder = new BASE64Decoder();
		  try {
		    byte[] b = decoder.decodeBuffer(s);
		    return new String(b);
		  } catch (Exception e) {
		    return null;
		  }
		} 
	
	public static  byte[] decodeBase64(String s) {
		  if (s == null) return null;
		  BASE64Decoder decoder = new BASE64Decoder();
		  try {
		    return decoder.decodeBuffer(s);
//		    return new String(b);
		  } catch (Exception e) {
		    return null;
		  }
		} 
	
	public static void main(String[] args)
	{
		Base64 base64 = new Base64();
		String encode = base64.getEncodeBase64("123456");
		MessageDigest digester = null;

		try{
			digester = MessageDigest.getInstance("SHA");

			digester.update("1234567".getBytes("UTF-8"));
		}
		catch (NoSuchAlgorithmException nsae) {
//			_log.error(nsae, nsae);
		}
		catch (UnsupportedEncodingException uee) {
//			_log.error(uee, uee);
		}

		byte[] bytes = digester.digest();

//		if (_BASE_64) {
		//IOq+XWSw4hZ5boNPUtYf0LcDMvw=
		System.out.println(base64.encode(bytes));
//		}
//		System.out.println(encode);
	}


}
