package org.frameworkset.security.rsa;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;

import org.frameworkset.util.Base64;

public class KeyGenerater {
	 private String priKey;
	 private String pubKey;
	 
	 public void generater() {
	  try {
	   java.security.KeyPairGenerator keygen = java.security.KeyPairGenerator
	     .getInstance("RSA");
	   SecureRandom secrand = new SecureRandom();
	   secrand.setSeed("syj".getBytes()); // 初始化随机产生器
	   keygen.initialize(1024, secrand);
	   KeyPair keys = keygen.genKeyPair();
	 
	   PublicKey pubkey = keys.getPublic();
	   PrivateKey prikey = keys.getPrivate();
	 
	   pubKey = Base64.encode(pubkey.getEncoded());
	   priKey = Base64.encode(prikey.getEncoded());
	 
	   System.out.println("pubKey = " + new String(pubKey));
	   System.out.println("priKey = " + new String(priKey));
	  } catch (java.lang.Exception e) {
	   System.out.println("生成密钥对失败");
	   e.printStackTrace();
	  }
	 }
	 
	 public String getPriKey() {
	  return priKey;
	 }
	 
	 public String getPubKey() {
	  return pubKey;
	 }

}
