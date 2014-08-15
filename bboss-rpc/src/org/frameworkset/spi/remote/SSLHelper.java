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

package org.frameworkset.spi.remote;



import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.Security;
import java.security.cert.CertificateException;

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;

import org.apache.log4j.Logger;



/**
 * <p>
 * Title: SSLHelper.java
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * bboss workgroup
 * </p>
 * <p>
 * Copyright (c) 2007
 * </p>
 * 
 * @Date 2009-11-22 下午03:49:17
 * @author biaoping.yin
 * @version 1.0
 */
public class SSLHelper
{
    private static final Logger log = Logger.getLogger(SSLHelper.class);

    /**
     * Protocol to use.
     */
    private static final String PROTOCOL = "TLS";
    
    public static final String[] enabledCipherSuites = { "SSL_DH_anon_WITH_RC4_128_MD5" };

    private static final String KEY_MANAGER_FACTORY_ALGORITHM;

    static
    {
        String algorithm = Security.getProperty("ssl.KeyManagerFactory.algorithm");
        if (algorithm == null)
        {
            algorithm = KeyManagerFactory.getDefaultAlgorithm();
        }

        KEY_MANAGER_FACTORY_ALGORITHM = algorithm;
    }

    public static TrustManager[] getTrustManagers(String trustStore, String trustStorePassword)
            throws java.security.NoSuchAlgorithmException, java.security.KeyStoreException, java.io.IOException,
            java.security.GeneralSecurityException
    {

        log.debug("Initiating TrustManagers use trustStore[" + trustStore + "],trustStorePassword[" + trustStorePassword + "]");

        KeyStore ks = KeyStore.getInstance("JKS");
        String path= getPathFromFile(trustStore);
//      System.setProperty("javax.net.ssl.trustStore",path); 
        InputStream in = null;
        try
        {
     	   in = new FileInputStream(path);
     	  ks.load(in, trustStorePassword.toCharArray());
        }
        finally
        {
     	   try {
 			if (in != null)
 				in.close();
 			} catch (Exception e) {
 				// TODO: handle exception
 			}
        }
       
//        ks.load(new FileInputStream(trustStore), trustStorePassword.toCharArray());
//        ks.load(SSLHelper.class.getResourceAsStream(trustStore), trustStorePassword.toCharArray());
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        tmf.init(ks);

        log.debug("TrustManagers init completed.");

        return tmf.getTrustManagers();
    }

    public static String getPathFromFile(String file)
    {
        URL url= null;
        url =   SSLHelper.class.getResource(file);
        if(url == null)
            url =   SSLHelper.class.getClassLoader().getResource(file);
        if(url != null)
            return url.getPath();
        return file;
    }
    public static KeyManager[] getKeyManagers(String keyStore, String keyStorePassword)
            throws java.security.NoSuchAlgorithmException, java.security.KeyStoreException,
            java.security.GeneralSecurityException, java.security.cert.CertificateException, java.io.IOException,
            java.security.UnrecoverableKeyException
    {

//        System.out.println("Initiating KeyManagers");
        log.debug("Initiating KeyManagers use keyStore[" + keyStore + "],keyStorePassword[" + keyStorePassword + "]");

        KeyStore ks = KeyStore.getInstance("JKS");
//        BogusSslContextFactory.class
//        .getResourceAsStream(BOGUS_KEYSTORE)
//        ks.load(new FileInputStream(keyStore), keyStorePassword.toCharArray());
        String path= getPathFromFile(keyStore);
//      System.setProperty("javax.net.ssl.trustStore",path); 

        InputStream in = null;
        try
        {
     	   in = new FileInputStream(path);
     	   ks.load(in, keyStorePassword.toCharArray());
        }
        finally
        {
     	   try {
 			if (in != null)
 				in.close();
 			} catch (Exception e) {
 				// TODO: handle exception
 			}
        }
//        ks.load(SSLHelper.class.getResourceAsStream(keyStore), keyStorePassword.toCharArray());
        KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        kmf.init(ks, keyStorePassword.toCharArray());

        log.debug("KeyManagers init completed.");

        return kmf.getKeyManagers();

    }

    public static SSLContext createSSLContext(String keyStore, String keyStorePassword, String trustStore,
            String trustStorePassword) throws GeneralSecurityException, IOException
    {

        // Initialize the SSLContext to work with our key managers.
        SSLContext sslContext = SSLContext.getInstance(PROTOCOL);
        sslContext.init(getKeyManagers(keyStore, keyStorePassword), getTrustManagers(trustStore, trustStorePassword), new java.security.SecureRandom());
//        sslContext.
        return sslContext;
    }

	public static KeyStore getKeyStore(String trustStore,
			String trustStorePassword) throws KeyStoreException, NoSuchAlgorithmException, CertificateException, FileNotFoundException, IOException {
		 KeyStore ks = KeyStore.getInstance("JKS");
//       BogusSslContextFactory.class
//       .getResourceAsStream(BOGUS_KEYSTORE)
//       ks.load(new FileInputStream(keyStore), keyStorePassword.toCharArray());
       String path= getPathFromFile(trustStore);
//     System.setProperty("javax.net.ssl.trustStore",path); 
       InputStream in = null;
       try
       {
    	   in = new FileInputStream(path);
	       ks.load(in, trustStorePassword.toCharArray());
	       return ks;
       }
       finally
       {
    	   if(in != null)
    		   in.close();
       }
	}
}
