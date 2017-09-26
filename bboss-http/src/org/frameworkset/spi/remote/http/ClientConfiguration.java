/**
 * 
 */
package org.frameworkset.spi.remote.http;
import org.apache.http.*;
import org.apache.http.client.CookieStore;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.AuthSchemes;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.*;
import org.apache.http.conn.DnsResolver;
import org.apache.http.conn.HttpConnectionFactory;
import org.apache.http.conn.ManagedHttpClientConnection;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.DefaultHttpResponseFactory;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.*;
import org.apache.http.impl.io.DefaultHttpRequestWriterFactory;
import org.apache.http.io.HttpMessageParser;
import org.apache.http.io.HttpMessageParserFactory;
import org.apache.http.io.HttpMessageWriterFactory;
import org.apache.http.io.SessionInputBuffer;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicLineParser;
import org.apache.http.message.LineParser;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.CharArrayBuffer;
import org.frameworkset.spi.BaseApplicationContext;
import org.frameworkset.spi.BeanNameAware;
import org.frameworkset.spi.DefaultApplicationContext;
import org.frameworkset.spi.InitializingBean;

import javax.net.ssl.SSLContext;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.charset.CodingErrorAction;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * This example demonstrates how to customize and configure the most common aspects
 * of HTTP request execution and connection management.
 */
/**
 * @author yinbp
 *
 * @Date:2016-11-20 11:50:36
 */
public class ClientConfiguration implements InitializingBean,BeanNameAware{
	private final static int TIMEOUT_CONNECTION = 20000;
	private final static int TIMEOUT_SOCKET = 20000;
	private final static int RETRY_TIME = 3;
	private  CloseableHttpClient httpclient;
	private static RequestConfig defaultRequestConfig ;
	private RequestConfig requestConfig;
	private  static HttpClient defaultHttpclient;
	private int timeoutConnection = TIMEOUT_CONNECTION;
	private int timeoutSocket = TIMEOUT_SOCKET;
	private int retryTime = RETRY_TIME;
	private int maxLineLength = 2000;
	private int maxHeaderCount = 200;
	private int maxTotal = 200; 
	private int defaultMaxPerRoute = 10;
	private String beanName;
	private static Map<String,ClientConfiguration> clientConfigs = new HashMap<String,ClientConfiguration>();
	private static BaseApplicationContext context;
	private static void loadClientConfiguration(){
		if(context == null)
			context = DefaultApplicationContext.getApplicationContext("conf/httpclient.xml");
		
	}
	public static final ContentType TEXT_PLAIN_UTF_8 = ContentType.create(
            "text/plain", Consts.UTF_8);
	/**
	 * 
	 */
	public ClientConfiguration() {
		// TODO Auto-generated constructor stub
	}
	
	
	public  static RequestConfig getDefaultRequestConfig() {
		return defaultRequestConfig;
	}

	public int getTimeoutConnection() {
		return timeoutConnection;
	}

	public void setTimeoutConnection(int timeoutConnection) {
		this.timeoutConnection = timeoutConnection;
	}

	public int getTimeoutSocket() {
		return timeoutSocket;
	}

	public void setTimeoutSocket(int timeoutSocket) {
		this.timeoutSocket = timeoutSocket;
	}

	public int getRetryTime() {
		return retryTime;
	}

	public void setRetryTime(int retryTime) {
		this.retryTime = retryTime;
	}
	public final CloseableHttpClient getHttpClient()  throws Exception {
		if(httpclient != null)
			return httpclient;
//		synchronized(ClientConfiguration.class)
		{
//			if(httpclient != null)
//				return httpclient;
	        // Use custom message parser / writer to customize the way HTTP
	        // messages are parsed from and written out to the data stream.
	        HttpMessageParserFactory<HttpResponse> responseParserFactory = new DefaultHttpResponseParserFactory() {
	
	            @Override
	            public HttpMessageParser<HttpResponse> create(
	                SessionInputBuffer buffer, MessageConstraints constraints) {
	                LineParser lineParser = new BasicLineParser() {
	
	                    @Override
	                    public Header parseHeader(final CharArrayBuffer buffer) {
	                        try {
	                            return super.parseHeader(buffer);
	                        } catch (ParseException ex) {
	                            return new BasicHeader(buffer.toString(), null);
	                        }
	                    }
	
	                };
	                return new DefaultHttpResponseParser(
	                    buffer, lineParser, DefaultHttpResponseFactory.INSTANCE, constraints) {
	
	                    @Override
	                    protected boolean reject(final CharArrayBuffer line, int count) {
	                        // try to ignore all garbage preceding a status line infinitely
	                        return false;
	                    }
	
	                };
	            }
	
	        };
	        HttpMessageWriterFactory<HttpRequest> requestWriterFactory = new DefaultHttpRequestWriterFactory();
	
	        // Use a custom connection factory to customize the process of
	        // initialization of outgoing HTTP connections. Beside standard connection
	        // configuration parameters HTTP connection factory can define message
	        // parser / writer routines to be employed by individual connections.
	        HttpConnectionFactory<HttpRoute, ManagedHttpClientConnection> connFactory = new ManagedHttpClientConnectionFactory(
	                requestWriterFactory, responseParserFactory);
	
	        // Client HTTP connection objects when fully initialized can be bound to
	        // an arbitrary network socket. The process of network socket initialization,
	        // its connection to a remote address and binding to a local one is controlled
	        // by a connection socket factory.
	
	        // SSL context for secure connections can be created either based on
	        // system or application specific properties.
	        SSLContext sslcontext = SSLContexts.createSystemDefault();
	
	        // Create a registry of custom connection socket factories for supported
	        // protocol schemes.
	        Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
	            .register("http", PlainConnectionSocketFactory.INSTANCE)
	            .register("https", new SSLConnectionSocketFactory(sslcontext))
	            .build();
	
	        // Use custom DNS resolver to override the system DNS resolution.
	        DnsResolver dnsResolver = new SystemDefaultDnsResolver() {
	
	            @Override
	            public InetAddress[] resolve(final String host) throws UnknownHostException {
	                if (host.equalsIgnoreCase("localhost")) {
	                    return new InetAddress[] { InetAddress.getByAddress(new byte[] {127, 0, 0, 1}) };
	                } else {
	                    return super.resolve(host);
	                }
	            }
	
	        };
	
	        // Create a connection manager with custom configuration.
	        PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager(
	                socketFactoryRegistry, connFactory, dnsResolver);
	
	        // Create socket configuration
	        SocketConfig socketConfig = SocketConfig.custom()
	            .setTcpNoDelay(true)
	            .setSoTimeout(timeoutSocket)
	            .build();
	        // Configure the connection manager to use socket configuration either
	        // by default or for a specific host.
	        connManager.setDefaultSocketConfig(socketConfig);
//	        connManager.setSocketConfig(new HttpHost("localhost", 80), socketConfig);
	        // Validate connections after 1 sec of inactivity
	        connManager.setValidateAfterInactivity(1000);
	        
	        
	        // Create message constraints
	        MessageConstraints messageConstraints = MessageConstraints.custom()
	            .setMaxHeaderCount(this.maxHeaderCount)
	            .setMaxLineLength(this.maxLineLength)
	            .build();
	        // Create connection configuration
	        ConnectionConfig connectionConfig = ConnectionConfig.custom()
	            .setMalformedInputAction(CodingErrorAction.IGNORE)
	            .setUnmappableInputAction(CodingErrorAction.IGNORE)
	            .setCharset(Consts.UTF_8)
	            .setMessageConstraints(messageConstraints)
	            .build();
	        // Configure the connection manager to use connection configuration either
	        // by default or for a specific host.
	        connManager.setDefaultConnectionConfig(connectionConfig);
//	        connManager.setConnectionConfig(new HttpHost("localhost", 80), ConnectionConfig.DEFAULT);
	
	        // Configure total max or per route limits for persistent connections
	        // that can be kept in the pool or leased by the connection manager.
	        connManager.setMaxTotal(maxTotal);
	        connManager.setDefaultMaxPerRoute(defaultMaxPerRoute);
//	        connManager.setMaxPerRoute(new HttpRoute(new HttpHost("localhost", 80)), 20);
	
	        // Use custom cookie store if necessary.
	        CookieStore cookieStore = new BasicCookieStore();
	        
	        // Use custom credentials provider if necessary.
	        CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
	        // Create global request configuration
	        RequestConfig requestConfig = RequestConfig.custom()
	            .setCookieSpec(CookieSpecs.DEFAULT)
	            .setExpectContinueEnabled(true)
	            .setTargetPreferredAuthSchemes(Arrays.asList(AuthSchemes.NTLM, AuthSchemes.DIGEST))
	            .setProxyPreferredAuthSchemes(Arrays.asList(AuthSchemes.BASIC))
	            .setConnectTimeout(this.timeoutConnection).setConnectionRequestTimeout(TIMEOUT_CONNECTION)
	            .build();
	
	        // Create an HttpClient with the given custom dependencies and configuration.
	        httpclient = HttpClients.custom()
	            .setConnectionManager(connManager)
	            .setDefaultCookieStore(cookieStore)
	            .setDefaultCredentialsProvider(credentialsProvider)
	            //.setProxy(new HttpHost("myproxy", 8080))
	            .setDefaultRequestConfig(requestConfig)
	            .build();
	        if(this.beanName.equals("default")){
	        	defaultRequestConfig = requestConfig;
	        	defaultHttpclient = httpclient;
	        }
	        clientConfigs.put(beanName, this);
		}
        return httpclient;

//        try {
//            HttpGet httpget = new HttpGet("http://www.baidu.com");
//            // Request configuration can be overridden at the request level.
//            // They will take precedence over the one set at the client level.
//            RequestConfig requestConfig = RequestConfig.copy(defaultRequestConfig)
//                .setSocketTimeout(5000)
//                .setConnectTimeout(5000)
//                .setConnectionRequestTimeout(5000)
//               // .setProxy(new HttpHost("myotherproxy", 8080))
//                .build();
//            httpget.setConfig(requestConfig);
//
//            // Execution context can be customized locally.
//            HttpClientContext context = HttpClientContext.create();
//            // Contextual attributes set the local context level will take
//            // precedence over those set at the client level.
//            context.setCookieStore(cookieStore);
//            context.setCredentialsProvider(credentialsProvider);
//
//            System.out.println("executing request " + httpget.getURI());
//            CloseableHttpResponse response = httpclient.execute(httpget, context);
//            try {
//                System.out.println("----------------------------------------");
//                System.out.println(response.getStatusLine());
//                System.out.println(EntityUtils.toString(response.getEntity()));
//                System.out.println("----------------------------------------");
//
//                // Once the request has been executed the local context can
//                // be used to examine updated state and various objects affected
//                // by the request execution.
//
//                // Last executed request
//                context.getRequest();
//                // Execution route
//                context.getHttpRoute();
//                // Target auth state
//                context.getTargetAuthState();
//                // Proxy auth state
//                context.getTargetAuthState();
//                // Cookie origin
//                context.getCookieOrigin();
//                // Cookie spec used
//                context.getCookieSpec();
//                // User security token
//                context.getUserToken();
//
//            } finally {
//                response.close();
//            }
//        } finally {
//            httpclient.close();
//        }
    }

	public HttpClient getHttpclient() {
		return httpclient;
	}

	public int getMaxTotal() {
		return maxTotal;
	}

	public void setMaxTotal(int maxTotal) {
		this.maxTotal = maxTotal;
	}

	public int getDefaultMaxPerRoute() {
		return defaultMaxPerRoute;
	}

	public void setDefaultMaxPerRoute(int defaultMaxPerRoute) {
		this.defaultMaxPerRoute = defaultMaxPerRoute;
	}

	/** (non-Javadoc)
	 * @see org.frameworkset.spi.InitializingBean#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() throws Exception {
		this.getHttpClient();
		
	}

	/** (non-Javadoc)
	 * @see org.frameworkset.spi.BeanNameAware#setBeanName(java.lang.String)
	 */
	@Override
	public void setBeanName(String name) {
		this.beanName = name;
		
	}

	public static HttpClient getDefaultHttpclient() {
		loadClientConfiguration();
		return getDefaultClientConfiguration().getHttpclient();
	}
	private static ClientConfiguration defaultClientConfiguration;
	public static ClientConfiguration getDefaultClientConfiguration(){
		loadClientConfiguration();
		if(defaultClientConfiguration != null)
			return defaultClientConfiguration;
		defaultClientConfiguration = context.getTBeanObject("default", ClientConfiguration.class);
		return defaultClientConfiguration;
	}
	public static ClientConfiguration getClientConfiguration(String poolname){
		loadClientConfiguration();
		if(poolname == null)
			return getDefaultClientConfiguration();
		ClientConfiguration config = clientConfigs.get(poolname);
		if(config != null)
			return config;
		config = context.getTBeanObject(poolname, ClientConfiguration.class);
		return config;
	}

	public RequestConfig getRequestConfig() {
		return requestConfig;
	}
	
	
	

}
