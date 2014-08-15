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

import org.jasypt.encryption.pbe.StandardPBEByteEncryptor;
import org.jasypt.encryption.pbe.config.SimplePBEConfig;


/**
 * <p>Title: EncryptModuleTest.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2007</p>
 * @Date 2010-2-4 下午04:56:14
 * @author biaoping.yin
 * @version 1.0
 */
public class EncryptModuleTest implements EncryptModule
{
    private static  StandardPBEByteEncryptor  encryptor = new StandardPBEByteEncryptor();
    static{
        
        SimplePBEConfig config  = new SimplePBEConfig();
        config.setPassword("1");
        config.setAlgorithm("PBEWithMD5AndDES");      
        config.setProvider(Security.getProvider("SunJCE"));
        encryptor.setConfig(config);
        encryptor.initialize();
    }
    
    public byte[] decode(byte[] value)
    {
//        StandardPBEByteEncryptor  encryptor = new StandardPBEByteEncryptor();
//        encryptor.setPassword(password);
        System.out.println("decode:" + value);
        return encryptor.decrypt(value);
    }

    public byte[] encode(byte[] value)
    {
        System.out.println("encode:" + value);
//        StandardPBEByteEncryptor  encryptor = new StandardPBEByteEncryptor();
//        encryptor.setPassword(password);
        return encryptor.encrypt(value);
    }

}
