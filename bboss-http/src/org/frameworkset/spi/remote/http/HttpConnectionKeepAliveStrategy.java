package org.frameworkset.spi.remote.http;

import org.apache.http.HttpResponse;
import org.apache.http.impl.client.DefaultConnectionKeepAliveStrategy;
import org.apache.http.protocol.HttpContext;

public class HttpConnectionKeepAliveStrategy extends DefaultConnectionKeepAliveStrategy{
	private long keepAlive ;
	public HttpConnectionKeepAliveStrategy(long keepAlive) {
		this.keepAlive = keepAlive;
	}
	
	 @Override  
     public long getKeepAliveDuration(HttpResponse response, HttpContext context) {  
         long keepAlive = super.getKeepAliveDuration(response, context);  
         if (keepAlive == -1) {  
             keepAlive = this.keepAlive;  
         }  
         return keepAlive;  
     }  

}
