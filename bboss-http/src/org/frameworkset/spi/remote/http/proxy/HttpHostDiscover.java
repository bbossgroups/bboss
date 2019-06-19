package org.frameworkset.spi.remote.http.proxy;


import org.frameworkset.spi.BaseApplicationContext;
import org.frameworkset.spi.remote.http.HttpHost;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * 自动发现es 主机节点
 */
public abstract class HttpHostDiscover extends Thread{
	private static Logger logger = LoggerFactory.getLogger(HttpHostDiscover.class);
	private long discoverInterval  = 10000l;
	private HttpServiceHosts httpServiceHosts;
	public HttpHostDiscover( ){
		super("Http HostDiscover Thread");

		BaseApplicationContext.addShutdownHook(new Runnable() {
			@Override
			public void run() {
				stopCheck();
			}
		});
	}
	boolean stop = false;
	public void stopCheck(){
		this.stop = true;
		this.interrupt();
	}

	private void handleDiscoverHosts(List<HttpHost> hosts){
		List<HttpAddress> newAddress = new ArrayList<HttpAddress>();
		//恢复移除节点
		httpServiceHosts.recoverRemovedNodes(hosts);
		//识别新增节点
		for(int i = 0; i < hosts.size();i ++){
			HttpAddress address = new HttpAddress(hosts.get(i).toString(),httpServiceHosts.getHealth());
			if(!httpServiceHosts.containAddress(address)){
				newAddress.add(address);
			}
		}
		//处理新增节点
		if(newAddress.size() > 0) {
			if (logger.isInfoEnabled()) {
				logger.info(new StringBuilder().append("Discovery new http servers[").append(newAddress).append("].").toString());
			}
			httpServiceHosts.addAddresses(newAddress);
		}
		//处理删除节点
		httpServiceHosts.handleRemoved( hosts);
	}
	protected abstract List<HttpHost> discover();
	@Override
	public void run() {
		do {
			if(this.stop)
				break;
			doDiscover();
			try {
				sleep(discoverInterval);
			} catch (InterruptedException e) {
				break;
			}
		}while(true);

	}

	private void doDiscover(){
		try {
//				clientInterface.discover("_nodes/http",ClientInterface.HTTP_GET, new ResponseHandler<Void>() {
//
//					@Override
//					public Void handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
//						int status = response.getStatusLine().getStatusCode();
//						if (status >= 200 && status < 300) {
//							List<HttpHost> hosts = readHosts(response.getEntity());
//							handleDiscoverHosts(hosts);
//
//						} else {
//
//						}
//						return null;
//					}
//				});
			if (logger.isDebugEnabled())
				logger.debug("Discovery http servers.");
			List<HttpHost> httpHosts = discover();
			handleDiscoverHosts( httpHosts);
		} catch (Exception e) {
			if (logger.isInfoEnabled())
				logger.info("Discovery http servers failed:",e);
		}
	}

	public void setHttpServiceHosts(HttpServiceHosts httpServiceHosts) {
		this.httpServiceHosts = httpServiceHosts;
	}
	public void start(){
		//do server discover first，then start the thread.
		if (logger.isInfoEnabled())
			logger.info("First doDiscovery http servers at start time.");
		this.doDiscover();
		super.start();

	}
}
