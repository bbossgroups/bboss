package org.frameworkset.web.socket.config;

import java.util.List;

import org.frameworkset.web.socket.handler.HandshakeInterceptor;
import org.frameworkset.web.socket.inf.WebSocketHandler;

public class WebSocketRegistryMeta {
	private List<HandshakeInterceptor> interceptors = null;
	private String allowedOrigins = null;
	private WebSocketHandler websocketHandler;
	private String websocketUrl;
	private String sockJSUrl;
	public WebSocketRegistryMeta() {
		// TODO Auto-generated constructor stub
	}
	public List<HandshakeInterceptor> getInterceptors() {
		return interceptors;
	}
	public void setInterceptors(List<HandshakeInterceptor> interceptors) {
		this.interceptors = interceptors;
	}
	public String getAllowedOrigins() {
		return allowedOrigins;
	}
	public void setAllowedOrigins(String allowedOrigins) {
		this.allowedOrigins = allowedOrigins;
	}
	 
	public String getWebsocketUrl() {
		return websocketUrl;
	}
	public void setWebsocketUrl(String websocketUrl) {
		this.websocketUrl = websocketUrl;
	}
	public String getSockJSUrl() {
		return sockJSUrl;
	}
	public void setSockJSUrl(String sockJSUrl) {
		this.sockJSUrl = sockJSUrl;
	}
	public WebSocketHandler getWebsocketHandler() {
		return websocketHandler;
	}
	public void setWebsocketHandler(WebSocketHandler websocketHandler) {
		this.websocketHandler = websocketHandler;
	}

}
