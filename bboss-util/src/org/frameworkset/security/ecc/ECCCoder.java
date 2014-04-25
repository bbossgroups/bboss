package org.frameworkset.security.ecc;

import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
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

import javax.crypto.Cipher;
import javax.crypto.NullCipher;




import org.frameworkset.util.encoder.Hex;

import sun.security.ec.ECKeyFactory;
import sun.security.ec.ECPrivateKeyImpl;
import sun.security.ec.ECPublicKeyImpl;


public class ECCCoder extends BaseECCCoder  {

//	public static final String ALGORITHM = "EC";
//	public static final String PUBLIC_KEY = "ECCPublicKey";
//	public static final String PRIVATE_KEY = "ECCPrivateKey";
	
	
	
	/* (non-Javadoc)
	 * @see org.frameworkset.security.ecc.ECCCoderInf#evalECPrivateKey(java.lang.String)
	 */
	@Override
	public PrivateKey _evalECPrivateKey(byte[] privateKey)
	{
		
			try {
				
				// 对密钥解密
				PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(privateKey);
				KeyFactory keyFactory = ECKeyFactory.INSTANCE;
	
				ECPrivateKey priKey = (ECPrivateKey) keyFactory
						.generatePrivate(pkcs8KeySpec);
				return priKey;
			} catch (Exception e) {
				throw new java.lang.RuntimeException(e);
			}
		
	}
	
	/* (non-Javadoc)
	 * @see org.frameworkset.security.ecc.ECCCoderInf#evalECPublicKey(java.lang.String)
	 */
	@Override
	public  PublicKey _evalECPublicKey(byte[] publicKey)
	{
		
		
			try {
				// 对公钥解密

				// 取得公钥
				X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(publicKey);
				KeyFactory keyFactory = ECKeyFactory.INSTANCE;

				ECPublicKey pubKey = (ECPublicKey) keyFactory
						.generatePublic(x509KeySpec);
				return pubKey;
			} catch (Exception e) {
				throw new java.lang.RuntimeException(e);
			}
		
		
	}



	
	/* (non-Javadoc)
	 * @see org.frameworkset.security.ecc.ECCCoderInf#decrypt(byte[], java.security.PrivateKey)
	 */
	@Override
	public  byte[] decrypt(byte[] data, PrivateKey priKey_) throws Exception {
		
		ECPrivateKey priKey = (ECPrivateKey)priKey_;
		ECPrivateKeySpec ecPrivateKeySpec = new ECPrivateKeySpec(priKey.getS(),
				priKey.getParams());

		// 对数据解密
		// TODO Chipher不支持EC算法 未能实现
		Cipher cipher = new NullCipher();
		// Cipher.getInstance(ALGORITHM, keyFactory.getProvider());
		cipher.init(Cipher.DECRYPT_MODE, priKey, ecPrivateKeySpec.getParams());

		return cipher.doFinal(data);
	}

	/* (non-Javadoc)
	 * @see org.frameworkset.security.ecc.ECCCoderInf#encrypt(byte[], java.lang.String)
	 */
	@Override
	public  byte[] encrypt(byte[] data, String publicKey)
			throws Exception {
		

		ECPublicKey pubKey = (ECPublicKey) evalECPublicKey(publicKey);

		return encrypt( data,  pubKey);
	}
	
	
	
	/* (non-Javadoc)
	 * @see org.frameworkset.security.ecc.ECCCoderInf#encrypt(byte[], java.security.PublicKey)
	 */
	@Override
	public  byte[] encrypt(byte[] data, PublicKey pubKey_)
			throws Exception {
		
		ECPublicKey pubKey = (ECPublicKey)pubKey_;
		ECPublicKeySpec ecPublicKeySpec = new ECPublicKeySpec(pubKey.getW(),
				pubKey.getParams());

		// 对数据加密
		// TODO Chipher不支持EC算法 未能实现
		Cipher cipher = new NullCipher();
		// Cipher.getInstance(ALGORITHM, keyFactory.getProvider());
		cipher.init(Cipher.ENCRYPT_MODE, pubKey, ecPublicKeySpec.getParams());

		return cipher.doFinal(data);
	}
	
	

	
	/* (non-Javadoc)
	 * @see org.frameworkset.security.ecc.ECCCoderInf#genECKeyPair()
	 */
	@Override
	public  SimpleKeyPair genECKeyPair() throws Exception {
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
		String spublicKey = Hex.toHexString(publicKey.getEncoded());

		BigInteger s = new BigInteger(
				"1234006549323611672814741753598448348329118574063", 10);
		// 私钥
		ECPrivateKey privateKey = new ECPrivateKeyImpl(s, ecParameterSpec);
		String sprivateKey = Hex.toHexString(privateKey.getEncoded());
		SimpleKeyPair ECKeyPair = new SimpleKeyPair(sprivateKey, spublicKey,
				publicKey, privateKey);
		PrivateKeyPairIndex.put(sprivateKey, ECKeyPair);
		ECPublicKeyPairIndex.put(spublicKey, ECKeyPair);
		return ECKeyPair;
	}

	@Override
	public KeyPair _genECKeyPair() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
