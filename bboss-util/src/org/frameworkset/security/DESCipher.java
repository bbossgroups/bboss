/*
 *  Copyright 2008 biaoping.yin
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
package org.frameworkset.security;

import java.security.Key;
import java.security.Security;

import javax.crypto.Cipher;

/**
 * 
 * <p>Title: DESCipher.java</p>
 *
 * <p>Description: 一个采用DES算法的加密解密类,可以通过设置密钥或使用默认密钥，对字符串和字节数组进行加密和解密运算。</p>
 *
 * <p>Copyright: Copyright (c) 2007</p>
 * @Date 2012-3-14 下午3:23:03
 * @author biaoping.yin
 * @version 1.0
 */
public class DESCipher {

    private static final String DEFAULTKEY = "BBOSSGROUPS";

    private Cipher encryptCipher = null;

    private Cipher decryptCipher = null;

    

    /**
     * 默认构造方法，使用默认密钥
     * 
     * @throws Exception
     */
    public DESCipher() throws Exception {
        this(DEFAULTKEY,type_all);
    }
    public DESCipher( int type) throws Exception {
        this(DEFAULTKEY,type);
    }
    public static int type_decode = 0;
    public static int type_encode = 1;
    public static int type_all = 2;
    public DESCipher(String strKey) throws Exception{
    	 this(strKey,type_all);
    }
    
    /**
     * 指定密钥构造方法
     * 
     * @param String
     *            指定的密钥
     * @throws Exception
     *             JAVA异常
     */
    public DESCipher(String strKey,int type) throws Exception {
        Security.addProvider(new com.sun.crypto.provider.SunJCE());
        Key key = getKey(strKey.getBytes());
        if(type == type_encode|| type == type_all){
	        encryptCipher = Cipher.getInstance("DES");
	        encryptCipher.init(Cipher.ENCRYPT_MODE, key);
        }
        if(type == type_decode|| type == type_all){
	        decryptCipher = Cipher.getInstance("DES");
	        decryptCipher.init(Cipher.DECRYPT_MODE, key);
        }
    }

    /**
     * 加密字节数组
     * 
     * @param byte[] 需加密的字节数组
     * @return byte[] 加密后的字节数组
     * @throws Exception
     *             JAVA异常
     */
    public byte[] encrypt(byte[] arrB) throws Exception {
        return encryptCipher.doFinal(arrB);
    }

    /**
     * 加密字符串
     * 将字符串转换为字节数组后重用encrypt(byte[])方法进行加密，并将得到字节数组转换为字符串返回
     * 
     * @param String
     *            需加密的字符串
     * @return String 加密后的字符串
     * @throws Exception
     *             JAVA异常
     */
    public String encrypt(String strIn) throws Exception {
        return CoderUtil.byteGrpToHexStr(encrypt(strIn.getBytes()));
    }

    /**
     * 解密字节数组
     * 
     * @param byte[] 需解密的字节数组
     * @return byte[] 解密后的字节数组
     * @throws Exception
     *             JAVA异常
     */
    public byte[] decrypt(byte[] arrB) throws Exception {
        return decryptCipher.doFinal(arrB);
    }

    /**
     * 解密字符串
     * 将参数字符串转换为字节数组后重用decrypt(String)方法进行解密，并将得到的解密字符数组转换为字符串后返回。
     * 
     * @param String
     *            需解密的字符串
     * @return String 解密后的字符串
     * @throws Exception
     *             JAVA异常
     */
    public String decrypt(String strIn) throws Exception {
        return new String(decrypt(CoderUtil.hexStrToByteGrp(strIn)));
    }


    /**
     * 从指定字符串生成密钥，密钥所需的字节数组长度为8位 不足8位时后面补0，超出8位只取前8位
     * 
     * @param byte[] 构成该字符串的字节数组
     * @return Key 生成的密钥
     * @throws Exception
     *             JAVA异常
     */
    private Key getKey(byte[] arrBTmp) throws Exception {
        byte[] arrB = new byte[8];
        for (int i = 0; i < arrBTmp.length && i < arrB.length; i++) {
            arrB[i] = arrBTmp[i];
        }
        Key key = new javax.crypto.spec.SecretKeySpec(arrB, "DES");
        return key;
    }
    public static void main(String[] args) throws Exception{
    	DESCipher dd = new DESCipher("123456789",2);
    	String r = dd.encrypt("bb");
    	System.out.println(r);
    	System.out.println(dd.decrypt(r));
    }
    
    
}
