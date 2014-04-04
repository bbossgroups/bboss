package org.frameworkset.security.ecc;

import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import java.security.spec.ECFieldF2m;
import java.security.spec.ECParameterSpec;
import java.security.spec.ECPoint;
import java.security.spec.ECPrivateKeySpec;
import java.security.spec.ECPublicKeySpec;
import java.security.spec.EllipticCurve;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Cipher;
import javax.crypto.NullCipher;

import org.frameworkset.util.Base64;

import sun.security.ec.ECKeyFactory;
import sun.security.ec.ECPrivateKeyImpl;
import sun.security.ec.ECPublicKeyImpl;

/**
 * ECC安全编码组件
 * 
 * @author 梁栋
 * @version 1.0
 * @since 1.0
 */
public abstract class ECCCoder  {

//	public static final String ALGORITHM = "EC";
//	public static final String PUBLIC_KEY = "ECCPublicKey";
//	public static final String PRIVATE_KEY = "ECCPrivateKey";
	
	private static Map<String,ECPrivateKey> ECPrivateKeyIndex = new HashMap<String,ECPrivateKey>();
	private static Map<String,ECPublicKey> ECPublicKeyIndex = new HashMap<String,ECPublicKey>();
	private static Map<String,ECKeyPair> ECPrivateKeyPairIndex = new HashMap<String,ECKeyPair>();
	private static Map<String,ECKeyPair> ECPublicKeyPairIndex = new HashMap<String,ECKeyPair>();
	
	public static ECPrivateKey evalECPrivateKey(String privateKey)
	{
		ECPrivateKey priKey = ECPrivateKeyIndex.get(privateKey);
		if(priKey != null)
			return priKey;
		synchronized(ECPrivateKeyIndex)
		{
			priKey = ECPrivateKeyIndex.get(privateKey);
			if(priKey != null)
				return priKey;
			try {
				
				// 对密钥解密
				byte[] keyBytes = Base64.decode(privateKey);
				PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
				KeyFactory keyFactory = ECKeyFactory.INSTANCE;
	
				 priKey = (ECPrivateKey) keyFactory
						.generatePrivate(pkcs8KeySpec);
				 ECPrivateKeyIndex.put(privateKey, priKey);
				return priKey;
			} catch (Exception e) {
				throw new java.lang.RuntimeException(e);
			}
		}
	}
	
	public static ECPublicKey evalECPublicKey(String publicKey)
	{
		
		ECPublicKey pubKey = ECPublicKeyIndex.get(publicKey);
		if(pubKey != null)
			return pubKey;
		synchronized(ECPublicKeyIndex)
		{
			pubKey = ECPublicKeyIndex.get(publicKey);
			if(pubKey != null)
				return pubKey;
			try {
				// 对公钥解密
				byte[] keyBytes = Base64.decode(publicKey);

				// 取得公钥
				X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
				KeyFactory keyFactory = ECKeyFactory.INSTANCE;

				pubKey = (ECPublicKey) keyFactory
						.generatePublic(x509KeySpec);
				ECPublicKeyIndex.put(publicKey, pubKey);
				return pubKey;
			} catch (Exception e) {
				throw new java.lang.RuntimeException(e);
			}
		}
		
	}


	/**
	 * 解密<br>
	 * 用私钥解密
	 * 
	 * @param data
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static byte[] decrypt(byte[] data, String privatekey) throws Exception {
		// 对密钥解密
		byte[] keyBytes = Base64.decode(privatekey);

		// 取得私钥
		PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = ECKeyFactory.INSTANCE;

		ECPrivateKey priKey = (ECPrivateKey) keyFactory
				.generatePrivate(pkcs8KeySpec);

		ECPrivateKeySpec ecPrivateKeySpec = new ECPrivateKeySpec(priKey.getS(),
				priKey.getParams());

		// 对数据解密
		// TODO Chipher不支持EC算法 未能实现
		Cipher cipher = new NullCipher();
		// Cipher.getInstance(ALGORITHM, keyFactory.getProvider());
		cipher.init(Cipher.DECRYPT_MODE, priKey, ecPrivateKeySpec.getParams());

		return cipher.doFinal(data);
	}
	
	/**
	 * 解密<br>
	 * 用私钥解密
	 * 
	 * @param data
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static byte[] decrypt(String database64, String privatekey) throws Exception {
		byte[] data = Base64.decode(database64);
		return decrypt(data,  privatekey);
	}
	
	/**
	 * 解密<br>
	 * 用私钥解密
	 * 
	 * @param data
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static byte[] decrypt(String database64, ECPrivateKey priKey) throws Exception {
		
		
		return decrypt(Base64.decode(database64),  priKey) ;
	}
	
	/**
	 * 解密<br>
	 * 用私钥解密
	 * 
	 * @param data
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static byte[] decrypt(byte[] data, ECPrivateKey priKey) throws Exception {
		

		ECPrivateKeySpec ecPrivateKeySpec = new ECPrivateKeySpec(priKey.getS(),
				priKey.getParams());

		// 对数据解密
		// TODO Chipher不支持EC算法 未能实现
		Cipher cipher = new NullCipher();
		// Cipher.getInstance(ALGORITHM, keyFactory.getProvider());
		cipher.init(Cipher.DECRYPT_MODE, priKey, ecPrivateKeySpec.getParams());

		return cipher.doFinal(data);
	}

	/**
	 * 加密<br>
	 * 用公钥加密
	 * 
	 * @param data
	 * @param privateKey
	 * @return
	 * @throws Exception
	 */
	public static byte[] encrypt(byte[] data, String publicKey)
			throws Exception {
		// 对公钥解密
		byte[] keyBytes = Base64.decode(publicKey);

		// 取得公钥
		X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = ECKeyFactory.INSTANCE;

		ECPublicKey pubKey = (ECPublicKey) keyFactory
				.generatePublic(x509KeySpec);

		ECPublicKeySpec ecPublicKeySpec = new ECPublicKeySpec(pubKey.getW(),
				pubKey.getParams());

		// 对数据加密
		// TODO Chipher不支持EC算法 未能实现
		Cipher cipher = new NullCipher();
		// Cipher.getInstance(ALGORITHM, keyFactory.getProvider());
		cipher.init(Cipher.ENCRYPT_MODE, pubKey, ecPublicKeySpec.getParams());

		return cipher.doFinal(data);
	}
	
	/**
	 * 加密<br>
	 * 用公钥加密
	 * 
	 * @param data
	 * @param privateKey
	 * @return
	 * @throws Exception
	 */
	public static byte[] encrypt(byte[] data, ECPublicKey pubKey)
			throws Exception {
		

		ECPublicKeySpec ecPublicKeySpec = new ECPublicKeySpec(pubKey.getW(),
				pubKey.getParams());

		// 对数据加密
		// TODO Chipher不支持EC算法 未能实现
		Cipher cipher = new NullCipher();
		// Cipher.getInstance(ALGORITHM, keyFactory.getProvider());
		cipher.init(Cipher.ENCRYPT_MODE, pubKey, ecPublicKeySpec.getParams());

		return cipher.doFinal(data);
	}

//	/**
//	 * 取得私钥
//	 * 
//	 * @param keyMap
//	 * @return
//	 * @throws Exception
//	 */
//	public static String getPrivateKey(Map<String, Key> keyMap)
//			throws Exception {
//		Key key = (Key) keyMap.get(PRIVATE_KEY);
//
//		return Base64.encode(key.getEncoded());
//	}
//
//	/**
//	 * 取得公钥
//	 * 
//	 * @param keyMap
//	 * @return
//	 * @throws Exception
//	 */
//	public static String getPublicKey(Map<String, Key> keyMap)
//			throws Exception {
//		Key key = (Key) keyMap.get(PUBLIC_KEY);
//
//		return Base64.encode(key.getEncoded());
//	}
	
	public static class ECKeyPair
	{
		
		private String privateKey;
		private String publicKey;
		private ECPublicKey pubKey;
		private ECPrivateKey priKey;
		public ECKeyPair(String privateKey, String publicKey,
				ECPublicKey pubKey, ECPrivateKey priKey) {
			super();
			this.privateKey = privateKey;
			this.publicKey = publicKey;
			this.pubKey = pubKey;
			this.priKey = priKey;
		}
		public String getPrivateKey() {
			return privateKey;
		}
		public String getPublicKey() {
			return publicKey;
		}
		public ECPublicKey getPubKey() {
			return pubKey;
		}
		public ECPrivateKey getPriKey() {
			return priKey;
		}
	}

//	/**
//	 * 初始化密钥
//	 * 
//	 * @return
//	 * @throws Exception
//	 */
//	public static Map<String, Key> initKey() throws Exception {
//		BigInteger x1 = new BigInteger(
//				"2fe13c0537bbc11acaa07d793de4e6d5e5c94eee8", 16);
//		BigInteger x2 = new BigInteger(
//				"289070fb05d38ff58321f2e800536d538ccdaa3d9", 16);
//
//		ECPoint g = new ECPoint(x1, x2);
//
//		// the order of generator
//		BigInteger n = new BigInteger(
//				"5846006549323611672814741753598448348329118574063", 10);
//		// the cofactor
//		int h = 2;
//		int m = 163;
//		int[] ks = { 7, 6, 3 };
//		ECFieldF2m ecField = new ECFieldF2m(m, ks);
//		// y^2+xy=x^3+x^2+1
//		BigInteger a = new BigInteger("1", 2);
//		BigInteger b = new BigInteger("1", 2);
//
//		EllipticCurve ellipticCurve = new EllipticCurve(ecField, a, b);
//
//		ECParameterSpec ecParameterSpec = new ECParameterSpec(ellipticCurve, g,
//				n, h);
//		// 公钥
//		ECPublicKey publicKey = new ECPublicKeyImpl(g, ecParameterSpec);
//
//		BigInteger s = new BigInteger(
//				"1234006549323611672814741753598448348329118574063", 10);
//		// 私钥
//		ECPrivateKey privateKey = new ECPrivateKeyImpl(s, ecParameterSpec);
//
//		Map<String, Key> keyMap = new HashMap<String, Key>(2);
//
//		keyMap.put(PUBLIC_KEY, publicKey);
//		keyMap.put(PRIVATE_KEY, privateKey);
//
//		return keyMap;
//	}
	
	/**
	 * 初始化密钥
	 * 
	 * @return
	 * @throws Exception
	 */
	public static ECKeyPair genECKeyPair() throws Exception {
		BigInteger x1 = new BigInteger(
				"2fe13c0537bbc11acaa07d793de4e6d5e5c94eee8", 16);
		BigInteger x2 = new BigInteger(
				"289070fb05d38ff58321f2e800536d538ccdaa3d9", 16);

		ECPoint g = new ECPoint(x1, x2);

		// the order of generator
		BigInteger n = new BigInteger(
				"5846006549323611672814741753598448348329118574063", 10);
		// the cofactor
		int h = 2;
		int m = 163;
		int[] ks = { 7, 6, 3 };
		ECFieldF2m ecField = new ECFieldF2m(m, ks);
		// y^2+xy=x^3+x^2+1
		BigInteger a = new BigInteger("1", 2);
		BigInteger b = new BigInteger("1", 2);

		EllipticCurve ellipticCurve = new EllipticCurve(ecField, a, b);

		ECParameterSpec ecParameterSpec = new ECParameterSpec(ellipticCurve, g,
				n, h);
		// 公钥
		ECPublicKey publicKey = new ECPublicKeyImpl(g, ecParameterSpec);
		String spublicKey = Base64.encode(publicKey.getEncoded());

		BigInteger s = new BigInteger(
				"1234006549323611672814741753598448348329118574063", 10);
		// 私钥
		ECPrivateKey privateKey = new ECPrivateKeyImpl(s, ecParameterSpec);
		String sprivateKey = Base64.encode(privateKey.getEncoded());
		ECKeyPair ECKeyPair = new ECKeyPair(sprivateKey, spublicKey,
				publicKey, privateKey);
		ECPrivateKeyPairIndex.put(sprivateKey, ECKeyPair);
		ECPublicKeyPairIndex.put(spublicKey, ECKeyPair);
		return ECKeyPair;
	}

}
