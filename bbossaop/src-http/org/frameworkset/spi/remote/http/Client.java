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
package org.frameworkset.spi.remote.http;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.security.KeyStore;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.frameworkset.mq.SSLHelper;
import org.frameworkset.soa.ObjectSerializable;
import org.frameworkset.spi.ApplicationContext;
import org.frameworkset.spi.assemble.ProMap;
import org.frameworkset.spi.remote.RPCAddress;
import org.frameworkset.spi.remote.RPCMessage;
import org.frameworkset.spi.remote.Target;
import org.frameworkset.spi.serviceidentity.TargetImpl;

import com.frameworkset.util.FileUtil;
import com.frameworkset.util.SimpleStringUtil;

//import com.thoughtworks.xstream.XStream;

//import com.thoughtworks.xstream.XStream;

/**
 * <p>
 * Title: Client.java
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * bboss workgroup
 * </p>
 * <p>
 * Copyright (c) 2008
 * </p>
 * 
 * @Date 2010-9-2
 * @author biaoping.yin
 * @version 1.0
 */
public class Client {
	static HttpParams params =  null;
	static ProMap conparams = HttpServer.getHttpServer().getParams();
	static boolean usepool = conparams.getBoolean("http.usepool", false);
	private static Logger log = Logger.getLogger(Client.class);
	static
	{
		params =  buildHttpParams();
		usepool = false;
		
	}
	public static HttpParams buildHttpParams()
	{
		
		 
		int so_timeout = conparams.getInt("http.socket.timeout", 30);//以秒为单位
		
		
		int CONNECTION_TIMEOUT = conparams.getInt("http.connection.timeout",30); 
		int CONNECTION_Manager_TIMEOUT = conparams.getInt("http.conn-manager.timeout",2);
		int httpsoLinger = conparams.getInt("http.soLinger",-1);
		HttpParams params = new BasicHttpParams();
		
		HttpConnectionParams.setSoTimeout(params, so_timeout * 1000);
		HttpConnectionParams.setConnectionTimeout(params, CONNECTION_TIMEOUT * 1000);
		HttpConnectionParams.setLinger(params, httpsoLinger );
		ConnManagerParams.setTimeout(params, CONNECTION_Manager_TIMEOUT * 1000);
		return params;
	}
	
	private static Map<String,HttpClient> cacheObjects = new HashMap<String,HttpClient>();
	private static HttpClient buildHttpClient_(RPCAddress address) throws Exception
	{
		HttpClient httpclient = new DefaultHttpClient();
		
		
		if (!address.getProtocol().equals(Target.BROADCAST_TYPE_HTTPS)) {

		} else {
			ProMap ssls = ApplicationContext.getApplicationContext()
					.getMapProperty("rpc.protocol.http.ssl.client");
			if (ssls == null) {
				throw new Exception(
						"启用了ssl模式， 但是没有指定rpc.protocol.http.ssl.server 参数，请检查文件org/frameworkset/spi/manager-rpc-http.xml是否正确设置了该参数。");
			}
			String trustStore_ = ssls.getString("trustStore");
			String trustStorePassword = ssls
					.getString("trustStorePassword");
			KeyStore trustStore = SSLHelper.getKeyStore(trustStore_,
					trustStorePassword);

			SSLSocketFactory socketFactory = new SSLSocketFactory(
					trustStore);
			Scheme sch = new Scheme("https", socketFactory, address
					.getPort());
			
			httpclient.getConnectionManager().getSchemeRegistry().register(
					sch);
		}
		
		return httpclient;
	}
	public static HttpClient buildHttpClient(RPCAddress address) throws Exception
	{
		
		HttpClient httpclient = null;
		if(usepool)
		{
			String uuid = address.getProtocol() + "::" + address.getIp() +":" + address.getPort();
			if(address.getContextpath() != null && !address.getContextpath().equals(""))
			{
				uuid = uuid + "/" + address.getContextpath();
			}
			httpclient = cacheObjects.get(uuid);
			if(httpclient != null)
				return httpclient;
			else
			{
				synchronized(cacheObjects)
				{
					httpclient = cacheObjects.get(uuid);
					if(httpclient != null)
						return httpclient;
					httpclient = buildHttpClient_(address);
					cacheObjects.put(uuid, httpclient);
					return httpclient;
				}
			}
		}
		else
		{
			httpclient = buildHttpClient_(address);
			return httpclient;
		}
		
		
	}
	
	public static RPCMessage sendMessage(RPCMessage srcmessag,
			RPCAddress address) throws ClientProtocolException, Exception {
		// InetSocketAddress serverAddress = new
		// InetSocketAddress("172.16.17.216",8080);
		// for(int i = 0; i < 10; i ++)
		// {
		//      
		// // Obtain HTTP connection
		// this.client.openConnection(
		// serverAddress,
		// "duoduo");
		// }
		HttpResponse response = null;
		HttpClient httpclient = null;
		HttpPost httppost = null;
		try {
			httpclient = buildHttpClient(address);
			
			String address_real = TargetImpl.buildWebserviceURL(address);
			httppost = new HttpPost(address_real);

//			XStream stream_ = new XStream();
			StringEntity reqEntity = new StringEntity(ObjectSerializable.toXML(srcmessag));
			reqEntity.setContentType("text/xml;charset=gbk");

			httppost.setEntity(reqEntity);
			httppost.setParams(params);

			log.debug("executing request " + httppost.getRequestLine());
			response = httpclient.execute(httppost);
			if(response.getStatusLine().getStatusCode() != 200)
			{
				StringBuffer message = new StringBuffer();
				message.append(response.getStatusLine() + "\r\n");
				message.append(EntityUtils.toString(response.getEntity()));
				
				throw new HttpRuntimeException(message.toString());
			}
			HttpEntity entity = response.getEntity();

//			System.out.println("----------------------------------------");
//			System.out.println(response.getStatusLine());
//			if (entity != null) {
//				System.out.println("Response content length: "
//						+ entity.getContentLength());
//				System.out.println("Chunked?: " + entity.isChunked());
//			}

			if (entity != null) {
				try {
					// ObjectInputStream in = new
					// ObjectInputStream(entity.getContent());
					InputStream instream = entity.getContent();
					ByteArrayOutputStream out = new ByteArrayOutputStream();
//					String xml = getFileContent( instream,"GBK");
//					System.out.println(xml);
					
//					XStream stream = new XStream();
					RPCMessage bean = (RPCMessage) ObjectSerializable.toBean(instream, RPCMessage.class);
					// System.out.println(bean.getId());
					// System.out.println(bean.getName());
					// System.out.println(in.readInt());
					// System.out.println(in.readObject());
					return bean;

				} catch (IOException e) {
					e.printStackTrace();
					throw e;
				}
			 catch (Exception e) {
					e.printStackTrace();
					throw e;
			}
				

				// catch (ClassNotFoundException e) {
				// // TODO Auto-generated catch block
				// e.printStackTrace();
				// }
			} else {
				throw new Exception("Result is null.");

			}
		} 
		catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		finally {
			if(usepool)
			{
				if(httppost != null) {
					try {
						httppost.abort();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			if(!usepool)
				shutdownclient(httpclient);
		}

		// When HttpClient instance is no longer needed,
		// shut down the connection manager to ensure
		// immediate deallocation of all system resources

	}
	
	public static String getFileContent(InputStream reader,String charSet) throws IOException
    {
    	ByteArrayOutputStream swriter = null;
        OutputStream temp = null;
        
        try
        {
//        	reader = new FileInputStream(file);
        	swriter = new ByteArrayOutputStream();
        	temp = new BufferedOutputStream(swriter);

            int len = 0;
            byte[] buffer = new byte[1024];
            while ((len = reader.read(buffer)) > 0)
            {
            	temp.write(buffer, 0, len);
            }
            temp.flush();
            if(charSet != null && !charSet.equals(""))
            	return swriter.toString(charSet);
            else
            	return swriter.toString();
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
            return "";
        }
        catch (IOException e)
        {
            e.printStackTrace();
            throw e;
        }
        finally
        {
            if (reader != null)
                try
                {
                    reader.close();
                }
                catch (IOException e)
                {
                }
            if (swriter != null)
                try
                {
                    swriter.close();
                }
                catch (IOException e)
                {
                }
            if (temp != null)
                try
                {
                	temp.close();
                }
                catch (IOException e)
                {
                }
        }
    }
	
	
	
	private static void shutdownclient(HttpClient httpclient)
	{
		if(httpclient != null)
			httpclient.getConnectionManager().shutdown();
	}

	// protected NHttpClientHandler createHttpClientHandler(
	// final HttpRequestExecutionHandler requestExecutionHandler,
	// final EventListener eventListener) {
	//        
	// BasicHttpProcessor httpproc = new BasicHttpProcessor();
	// httpproc.addInterceptor(new RequestContent());
	// httpproc.addInterceptor(new RequestTargetHost());
	// httpproc.addInterceptor(new RequestConnControl());
	// httpproc.addInterceptor(new RequestUserAgent());
	// httpproc.addInterceptor(new RequestExpectContinue());
	//
	// BufferingHttpClientHandler clientHandler = new
	// BufferingHttpClientHandler(
	// httpproc,
	// requestExecutionHandler,
	// new DefaultConnectionReuseStrategy(),
	// this.client.getParams());
	//
	// clientHandler.setEventListener(eventListener);
	//        
	// return clientHandler;
	// }

}
