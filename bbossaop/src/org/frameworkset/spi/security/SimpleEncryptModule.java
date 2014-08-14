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

package org.frameworkset.spi.security;

import java.security.Security;

import org.apache.log4j.Logger;
import org.jasypt.encryption.pbe.PBEByteEncryptor;
import org.jasypt.encryption.pbe.StandardPBEByteEncryptor;
import org.jasypt.encryption.pbe.config.SimplePBEConfig;


/**
 * <p>Title: SimpleEncryptModule.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2007</p>
 * @Date 2010-2-20 下午05:21:03
 * @author biaoping.yin
 * @version 1.0
 */
public class SimpleEncryptModule implements EncryptModule
{
	private static Logger log = Logger.getLogger(SimpleEncryptModule.class);

	PBEByteEncryptor  encryptor = null;
    
    public SimpleEncryptModule(String algorithm,String password,String provider)
    {
    		encryptor = new StandardPBEByteEncryptor();
    	  SimplePBEConfig config  = new SimplePBEConfig();
	      config.setPassword(password);
	      config.setAlgorithm(algorithm);      
	      config.setProvider(Security.getProvider(provider));
	      ((StandardPBEByteEncryptor)encryptor).setConfig(config);
	      ((StandardPBEByteEncryptor)encryptor).initialize();
    }
    
    public SimpleEncryptModule()
    {
    	encryptor = new StandardPBEByteEncryptor();
    	String algorithm="PBEWithMD5AndDES";
    	String password="123456";
    	String provider="SunJCE";
	  SimplePBEConfig config  = new SimplePBEConfig();
      config.setPassword(password);
      config.setAlgorithm(algorithm);      
      config.setProvider(Security.getProvider(provider));
      ((StandardPBEByteEncryptor)encryptor).setConfig(config);
      ((StandardPBEByteEncryptor)encryptor).initialize();
    }
    
    public SimpleEncryptModule(PBEByteEncryptor encryptor)
    {
    	if(encryptor == null)
    		throw new java.lang.IllegalArgumentException("encryptor == null");
    	
    	this.encryptor = encryptor;
    	
    }
    
    public byte[] decode(byte[] value)
    {
//        StandardPBEByteEncryptor  encryptor = new StandardPBEByteEncryptor();
//        encryptor.setPassword(password);
        System.out.println("decode byte code.");
        return encryptor.decrypt(value);
    }

    public byte[] encode(byte[] value)
    {
        System.out.println("encode byte code");
//        StandardPBEByteEncryptor  encryptor = new StandardPBEByteEncryptor();
//        encryptor.setPassword(password);
        return encryptor.encrypt(value);
    }

}
