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

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.List;

import javax.net.ssl.SSLContext;

import org.apache.http.HttpException;
import org.apache.http.impl.DefaultConnectionReuseStrategy;
import org.apache.http.impl.DefaultHttpResponseFactory;
import org.apache.http.impl.nio.DefaultServerIOEventDispatch;
import org.apache.http.impl.nio.SSLServerIOEventDispatch;
import org.apache.http.impl.nio.reactor.DefaultListeningIOReactor;
import org.apache.http.impl.nio.reactor.ExceptionEvent;
import org.apache.http.nio.NHttpConnection;
import org.apache.http.nio.NHttpServiceHandler;
import org.apache.http.nio.protocol.BufferingHttpServiceHandler;
import org.apache.http.nio.protocol.EventListener;
import org.apache.http.nio.reactor.IOEventDispatch;
import org.apache.http.nio.reactor.IOReactorException;
import org.apache.http.nio.reactor.IOReactorStatus;
import org.apache.http.nio.reactor.ListenerEndpoint;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.BasicHttpProcessor;
import org.apache.http.protocol.HttpExpectationVerifier;
import org.apache.http.protocol.HttpRequestHandler;
import org.apache.http.protocol.ResponseConnControl;
import org.apache.http.protocol.ResponseContent;
import org.apache.http.protocol.ResponseDate;
import org.apache.http.protocol.ResponseServer;
import org.frameworkset.spi.ApplicationContext;
import org.frameworkset.spi.assemble.ProMap;
import org.frameworkset.spi.remote.RPCAddress;
import org.frameworkset.spi.remote.SSLHelper;
import org.frameworkset.spi.remote.Target;
import org.frameworkset.spi.remote.mina.client.ClinentTransport;

/**
 * <p>
 * Title: HttpServer.java
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
public class HttpServer {

	private DefaultListeningIOReactor ioReactor;

	private ProMap params;
	HttpParams serverParams = null;
	private String ip ;
	private int port;
	private boolean enablessl;
	private int workerCount = 4;

	private volatile IOReactorThread thread;
	private ListenerEndpoint endpoint;

	// private volatile RequestCount requestCount;

	public HttpServer(ProMap params) throws IOException {
		super();
		this.params = params;
		this.ip = this.params.getString("connection.bind.ip","localhost");
		this.port = this.params.getInt("connection.bind.port", 8080);
		 enablessl = this.params.getBoolean("enablessl",false);
		 workerCount = this.params.getInt("http.workerCount", 4);
		this.localAddress = new RPCAddress(this.ip,port,null,enablessl ?Target.BROADCAST_TYPE_HTTPS:Target.BROADCAST_TYPE_HTTP);
	}

	

	

//	// public void setRequestCount(final RequestCount requestCount) {
//	// this.requestCount = requestCount;
//	// }
//
//	public void setExceptionHandler(
//			final IOReactorExceptionHandler exceptionHandler) {
//		this.ioReactor.setExceptionHandler(exceptionHandler);
//	}

	private void execute(IOEventDispatch ioEventDispatch )
			throws IOException {
		

		this.ioReactor.execute(ioEventDispatch);
	}

	public ListenerEndpoint getListenerEndpoint() {
		return this.endpoint;
	}

	public void setEndpoint(ListenerEndpoint endpoint) {
		this.endpoint = endpoint;
	}

	public IOReactorStatus getStatus() {
		return this.ioReactor.getStatus();
	}

	public List<ExceptionEvent> getAuditLog() {
		return this.ioReactor.getAuditLog();
	}

	public void join(long timeout) throws InterruptedException {
		if (this.thread != null) {
			this.thread.join(timeout);
		}
	}

	public Exception getException() {
		if (this.thread != null) {
			return this.thread.getException();
		} else {
			return null;
		}
	}
	private void startHttp() throws IOException 
	{
		
		try {
			this.ioReactor = new DefaultListeningIOReactor(workerCount, serverParams);

			 EventListener serverEventListener = new EventListener() {

				// @Override
				public void connectionClosed( NHttpConnection conn) {
					// closedServerConns.decrement();
					// super.connectionClosed(conn);
//					System.out.println("connectionClosed:" + conn);
				}

				public void connectionOpen( NHttpConnection conn) {
//					System.out.println("connectionOpen:" + conn);

				}

				public void connectionTimeout( NHttpConnection conn) {
					System.out.println("connectionTimeout:" + conn);

				}

				public void fatalIOException( IOException ex,
						 NHttpConnection conn) {
//					System.out.println("fatalIOException:" + conn);
					ex.printStackTrace();
				}

				public void fatalProtocolException( HttpException ex,
						 NHttpConnection conn) {
//					System.out.println("fatalProtocolException:" + conn);
					ex.printStackTrace();

				}

			};

			final NHttpServiceHandler serviceHandler = createHttpServiceHandler(
					HttpUtil.getHttpBaseRPCIOHandler(), null,
					serverEventListener);
			this.endpoint = this.ioReactor.listen(new InetSocketAddress(
					ip, port));
			IOEventDispatch ioEventDispatch = new DefaultServerIOEventDispatch(
					serviceHandler, serverParams);
//			this.execute(serviceHandler, ioEventDispatch);
			this.thread = new IOReactorThread(ioEventDispatch);
			this.thread.start();
			try {
				thread.join(1000);
				this.started = true;
			} catch ( InterruptedException e) {

				e.printStackTrace();
			}
			
		} catch ( IOReactorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private void startHttps() throws Exception 
	{
		
		try {
			this.ioReactor = new DefaultListeningIOReactor(workerCount, serverParams);

			EventListener serverEventListener = new EventListener() {

				// @Override
				public void connectionClosed( NHttpConnection conn) {
					// closedServerConns.decrement();
					// super.connectionClosed(conn);
//					System.out.println("connectionClosed:" + conn);
				}

				public void connectionOpen( NHttpConnection conn) {
//					System.out.println("connectionOpen:" + conn);

				}

				public void connectionTimeout( NHttpConnection conn) {
					System.out.println("connectionTimeout:" + conn);

				}

				public void fatalIOException( IOException ex,
						 NHttpConnection conn) {
//					System.out.println("fatalIOException:" + conn);
					ex.printStackTrace();
				}

				public void fatalProtocolException( HttpException ex,
						 NHttpConnection conn) {
//					System.out.println("fatalProtocolException:" + conn);
					ex.printStackTrace();

				}

			};

			final NHttpServiceHandler serviceHandler = createHttpServiceHandler(
					HttpUtil.getHttpBaseRPCIOHandler(), null,
					serverEventListener);
			this.endpoint = this.ioReactor.listen(new InetSocketAddress(
					ip, port));
			ProMap ssls =  ApplicationContext.getApplicationContext().getMapProperty("rpc.protocol.http.ssl.server");
            if(ssls == null)
            {
                throw new Exception("启用了ssl模式， 但是没有指定rpc.protocol.http.ssl.server 参数，请检查文件org/frameworkset/spi/manager-rpc-http.xml是否正确设置了该参数。");
            }
            String keyStore = ssls.getString("keyStore");
            String keyStorePassword = ssls.getString("keyStorePassword");
            String trustStore = ssls.getString("trustStore");
            String trustStorePassword = ssls.getString("trustStorePassword");
            SSLContext sslcontext = SSLHelper.createSSLContext(keyStore, keyStorePassword, trustStore, trustStorePassword);
            
			 IOEventDispatch ioEventDispatch = new SSLServerIOEventDispatch(
					 serviceHandler, 
		                sslcontext,
		                serverParams);
//			IOEventDispatch ioEventDispatch = new DefaultServerIOEventDispatch(
//					serviceHandler, serverParams);
//			this.execute(serviceHandler, ioEventDispatch);
			this.thread = new IOReactorThread(ioEventDispatch);
			this.thread.start();
			try {
				thread.join(1000);
				this.started = true;
			} catch ( InterruptedException e) {

				e.printStackTrace();
			}
			
//			ClassLoader cl = this.getClass().getClassLoader();
//	        URL url = cl.getResource("test.keystore");
//	        KeyStore keystore  = KeyStore.getInstance("jks");
//	        keystore.load(url.openStream(), "nopassword".toCharArray());
//	        KeyManagerFactory kmfactory = KeyManagerFactory.getInstance(
//	                KeyManagerFactory.getDefaultAlgorithm());
//	        kmfactory.init(keystore, "nopassword".toCharArray());
//	        KeyManager[] keymanagers = kmfactory.getKeyManagers(); 
//	        SSLContext sslcontext = SSLContext.getInstance("TLS");
//	        sslcontext.init(keymanagers, null, null);
//	        
	       
	        
	        // Set up request handlers
//	        HttpRequestHandlerRegistry reqistry = new HttpRequestHandlerRegistry();
//	        reqistry.register("*", new HttpFileHandler(args[0]));
//	        
//	        handler.setHandlerResolver(reqistry);
//	        
//	        // Provide an event logger
//	        handler.setEventListener(new EventLogger());
//	        
//	       
//	        
//	        ListeningIOReactor ioReactor = new DefaultListeningIOReactor(2, params);
//	        try {
//	            ioReactor.listen(new InetSocketAddress(8080));
//	            ioReactor.execute(ioEventDispatch);
//	        } catch (InterruptedIOException ex) {
//	            System.err.println("Interrupted");
//	        } catch (IOException e) {
//	            System.err.println("I/O error: " + e.getMessage());
//	        }
//	        System.out.println("Shutdown");
			
		} catch ( IOReactorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void start()
	{
		
		
		serverParams = new BasicHttpParams();
		int so_timeout = this.params.getInt("http.socket.timeout", 30);//以秒为单位
		int SOCKET_BUFFER_SIZE = this.params.getInt("http.socket.buffer-size",8 * 1024);
		boolean STALE_CONNECTION_CHECK = this.params.getBoolean("http.connection.stalecheck", false);
		boolean TCP_NODELAY = this.params.getBoolean("TCP_NODELAY", true);
		String ORIGIN_SERVER = this.params.getString("http.origin-server", "RPC-SERVER/1.1");
		int CONNECTION_TIMEOUT = this.params.getInt("http.connection.timeout",30); 
		int httpsoLinger = this.params.getInt("http.soLinger",-1);
		serverParams.setIntParameter(CoreConnectionPNames.SO_TIMEOUT, so_timeout * 1000)
				.setIntParameter(CoreConnectionPNames.SOCKET_BUFFER_SIZE,
						SOCKET_BUFFER_SIZE).setBooleanParameter(
						CoreConnectionPNames.STALE_CONNECTION_CHECK, STALE_CONNECTION_CHECK)
				.setBooleanParameter(CoreConnectionPNames.TCP_NODELAY, TCP_NODELAY)
				.setParameter(CoreProtocolPNames.ORIGIN_SERVER,
						ORIGIN_SERVER).setIntParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, CONNECTION_TIMEOUT * 1000).setIntParameter(CoreConnectionPNames.SO_LINGER, httpsoLinger);
		
		if(!enablessl)
		{
			try {
				this.startHttp();
				System.out.println("Http server is listenig at port " + port + ",ip is " + this.ip);
		        System.out.println("Http server started.");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else
		{
			try {
				this.startHttps();
				System.out.println("Https server is listenig at port " + port + ",ip is " + this.ip);
		        System.out.println("Https server started.");
		        
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if(this.started )
			ApplicationContext.addShutdownHook(new ShutDownHttpServer(this));
	}
	public void shutdown() throws IOException {
		this.ioReactor.shutdown();
		
		this.started = false;
		try {
			join(500);
		} catch (InterruptedException ignore) {
		}
	}

	private class IOReactorThread extends Thread {

		
		private IOEventDispatch ioEventDispatch;

		private volatile Exception ex;

		public IOReactorThread(final IOEventDispatch ioEventDispatch) {
			super();
			
			this.ioEventDispatch = ioEventDispatch;
		}

		@Override
		public void run() {
			try {
				execute(ioEventDispatch);
			} catch (Exception ex) {
				this.ex = ex;
				// if (requestCount != null) {
				// requestCount.failure(ex);
				// }
			}
		}

		public Exception getException() {
			return this.ex;
		}

	}

	private boolean started = false;
	private RPCAddress localAddress;
	public boolean validateAddress(RPCAddress address)
	{
	    //首先判断地址是否在地址范围中
	    
	    return ClinentTransport.validateAddress(address);
	}
	private static HttpServer server;
	
	public static HttpServer getHttpServer()
	{
		if(server != null)
			return server;
		synchronized(HttpServer.class)
		{
			if(server != null)
				return server;
			server = (HttpServer)ApplicationContext.getApplicationContext().getBeanObject("rpc.http.server");
			
		}
		
		return server;
	}
	
	private static class ShutDownHttpServer implements Runnable
	{
		HttpServer server;
		ShutDownHttpServer(HttpServer server)
	    {
	        this.server = server;
	    }
        public void run()
        {
            server.stop();
            
        }
	    
	    
	}
	public boolean started() {
		return this.started;
	}

	public void stop() {
		try {
			this.shutdown();
			started = false;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	

	public RPCAddress getLocalAddress() {
		// TODO Auto-generated method stub
		return this.localAddress;
	}

	// this.httpserver.start(serviceHandler);

	// ListenerEndpoint endpoint = this.server.getListenerEndpoint();
	// endpoint.waitFor();
	// InetSocketAddress serverAddress = (InetSocketAddress)
	// endpoint.getAddress();

	// this.server.shutdown();

	// closedServerConns.await(10000);

	protected NHttpServiceHandler createHttpServiceHandler(
			 HttpRequestHandler requestHandler,
			 HttpExpectationVerifier expectationVerifier,
			 EventListener eventListener) {

		BasicHttpProcessor httpproc = new BasicHttpProcessor();
		httpproc.addInterceptor(new ResponseDate());
		httpproc.addInterceptor(new ResponseServer());
		httpproc.addInterceptor(new ResponseContent());
		httpproc.addInterceptor(new ResponseConnControl());

		BufferingHttpServiceHandler serviceHandler = new BufferingHttpServiceHandler(
				httpproc, new DefaultHttpResponseFactory(),
				new DefaultConnectionReuseStrategy(), this.serverParams);

		serviceHandler.setHandlerResolver(new SimpleHttpRequestHandlerResolver(
				requestHandler));
		serviceHandler.setExpectationVerifier(expectationVerifier);
		serviceHandler.setEventListener(eventListener);

		return serviceHandler;
	}
	
	public ProMap getParams()
	{
		return this.params;
	}

}
