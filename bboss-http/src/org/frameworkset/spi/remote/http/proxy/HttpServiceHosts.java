package org.frameworkset.spi.remote.http.proxy;
/**
 * Copyright 2008 biaoping.yin
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.apache.commons.codec.binary.Base64;
import org.frameworkset.spi.assemble.GetProperties;
import org.frameworkset.spi.remote.http.ClientConfiguration;
import org.frameworkset.spi.remote.http.HttpHost;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.Charset;
import java.util.*;

/**
 * <p>Description: </p>
 * <p></p>
 * <p>Copyright (c) 2018</p>
 * @Date 2019/6/17 19:08
 * @author biaoping.yin
 * @version 1.0
 */
public class HttpServiceHosts {
	private static Logger logger = LoggerFactory.getLogger(HttpServiceHosts.class);


	public HttpServiceHostsConfig getHttpServiceHostsConfig() {
		return httpServiceHostsConfig;
	}

	private HttpServiceHostsConfig httpServiceHostsConfig;
	private ExceptionWare exceptionWare;
	private HttpHostDiscover hostDiscover;
	private Map<String, String> authHeaders;
	protected RoundRobinList serversList;
	protected List<HttpAddress> addressList;
	private HealthCheck healthCheck;
	private Map<String,HttpAddress> addressMap ;
	private String routing;

	private ClientConfiguration clientConfiguration;
	public HttpServiceHosts(){
		httpServiceHostsConfig = new HttpServiceHostsConfig();
	}
	public HttpAddress getHttpAddress(){
		return serversList.get();
	}

	public boolean reachEnd(int tryCount){
		return tryCount >= serversList.size();
	}
	public static String getHeader(String user, String password) {
		String auth = user + ":" + password;
		byte[] encodedAuth = Base64.encodeBase64(auth.getBytes(Charset.forName("US-ASCII")));
		return "Basic " + new String(encodedAuth);
	}
	public void after(String httpPoolName, GetProperties context){
//		if(hosts != null && !hosts.trim().equals(""))

		addressList = new ArrayList<HttpAddress>();
		addressMap = new HashMap<String,HttpAddress>();
		if(httpServiceHostsConfig.getHosts() != null && !httpServiceHostsConfig.getHosts().trim().equals("")) {
			String[] hostNames = httpServiceHostsConfig.getHosts().split(",");
			for (String host : hostNames) {
				HttpAddress esAddress = new HttpAddress(host.trim(), httpServiceHostsConfig.getHealth());
				addressList.add(esAddress);
				addressMap.put(esAddress.getAddress(), esAddress);
			}
		}
		serversList = new RoundRobinList(addressList);
		if (httpServiceHostsConfig.getAuthAccount() != null && !httpServiceHostsConfig.getAuthAccount().equals("")) {
			authHeaders = new HashMap<String, String>();
			authHeaders.put("Authorization", getHeader(httpServiceHostsConfig.getAuthAccount(), httpServiceHostsConfig.getAuthPassword()));
		}
		if(httpServiceHostsConfig.getExceptionWare() != null){
			try {
				Class<ExceptionWare> exceptionWareClass = (Class<ExceptionWare>) Class.forName(httpServiceHostsConfig.getExceptionWare().trim());
				ExceptionWare exceptionWare_ = exceptionWareClass.newInstance();
				exceptionWare_.setHttpServiceHosts(this);
				this.exceptionWare = exceptionWare_;
			}
			catch (Exception e){
				if(logger.isErrorEnabled()) {
					logger.error(new StringBuilder().append(" Http pool[")
							.append(getClientConfiguration().getBeanName()).append("]")
					.append("  ExceptionWare init failed:").toString(), e);
				}
			}
		}
		else if(this.exceptionWare != null){
			exceptionWare.setHttpServiceHosts(this);
		}

		if(httpServiceHostsConfig.getHealthCheckInterval() > 0 && httpServiceHostsConfig.getHealth() != null && !this.httpServiceHostsConfig.getHealth().equals("")) {
			if(logger.isInfoEnabled()) {
				logger.info(new StringBuilder().append("Start Http pool[")
							.append(getClientConfiguration().getBeanName()).append("]")
					.append(" HttpProxy server healthCheck thread,you can set http.healthCheckInterval=-1 in config file to disable healthCheck thread.").toString());
			}
			healthCheck = new HealthCheck(httpPoolName,addressList, httpServiceHostsConfig.getHealthCheckInterval(),authHeaders);
			healthCheck.run();
		}
		else {
			if(logger.isInfoEnabled()) {
				logger.info(new StringBuilder().append("HttpProxy server Http pool[")
							.append(getClientConfiguration().getBeanName()).append("]")
					.append(" healthCheck disable,you can set HttpProxy http.healthCheckInterval (>0) and http.health in configfile to enabled healthCheck thread.").toString());
			}
		}

		if(httpServiceHostsConfig.getDiscoverService() != null && !this.httpServiceHostsConfig.getDiscoverService().equals("")) {
			logger.info(new StringBuilder().append("Start Http pool[")
							.append(getClientConfiguration().getBeanName()).append("]")
					.append(" discoverHost thread,to distabled set http.discoverService to null in configfile.").toString());

			try {
				Class<HttpHostDiscover> httpHostDiscoverClass = (Class<HttpHostDiscover>) Class.forName(this.httpServiceHostsConfig.getDiscoverService());
				HttpHostDiscover hostDiscover = httpHostDiscoverClass.newInstance();
				hostDiscover.setHttpServiceHosts(this);
				hostDiscover.start();
				this.hostDiscover = hostDiscover;
			}
			catch (Exception e){
				if(logger.isErrorEnabled()) {
					logger.error(new StringBuilder().append("Start Http pool[")
							.append(getClientConfiguration().getBeanName()).append("]").append(" discovery service failed:").toString(), e);
				}
			}
		}
		else if(hostDiscover == null){
			logger.info(new StringBuilder().append("Discover Http pool[")
							.append(getClientConfiguration().getBeanName()).append("]").append(" is disabled,to enabled set http.discoverService in configfile.").toString());
		}
		else{
			hostDiscover.setHttpServiceHosts(this);
			hostDiscover.start();
		}

	}



	public void setAuthAccount(String authAccount) {
		httpServiceHostsConfig.setAuthAccount(authAccount);
	}



	public void setAuthPassword(String authPassword) {
		httpServiceHostsConfig.setAuthPassword(authPassword);
	}


	public void setHealth(String health) {
		httpServiceHostsConfig.setHealth(health);
	}


	public void setDiscoverService(String discoverService) {
		httpServiceHostsConfig.setDiscoverService(discoverService);
	}

	public Map<String, String> getAuthHeaders() {
		return authHeaders;
	}
	public void toString(StringBuilder log){
		httpServiceHostsConfig.toString(log,this.exceptionWare,this.hostDiscover);
	}



	public void setHosts(String hosts) {
		httpServiceHostsConfig.setHosts(hosts);
	}

	public void addAddresses(List<HttpAddress> address){
		this.serversList.addAddresses(address);
		if(this.healthCheck != null){
			this.healthCheck.checkNewAddresses(address);
		}
		for(HttpAddress host:address){
			addressMap.put(host.getAddress(),host);
		}
		if(logger.isInfoEnabled()){
			StringBuilder info = new StringBuilder();
			info.append("Http pool[")
								.append(getClientConfiguration().getBeanName()).append("]").append(" All live Http Servers:");
			Iterator<Map.Entry<String, HttpAddress>> iterator = this.addressMap.entrySet().iterator();
			boolean firsted = true;
			while(iterator.hasNext()){
				Map.Entry<String, HttpAddress> esAddressEntry = iterator.next();
				String host = esAddressEntry.getKey();

				if(firsted){
					info.append(host);
					firsted = false;
				}
				else{
					info.append(",").append(host);
				}
			}
			logger.info(info.toString());
		}
	}

	public void handleRemoved(List<HttpHost> hosts){
		boolean hasHosts = true;
		if(hosts == null || hosts.size() == 0){//没有可用节点
			hasHosts = false;
		}
		Iterator<Map.Entry<String, HttpAddress>> iterator = this.addressMap.entrySet().iterator();
		while(iterator.hasNext()){
			Map.Entry<String, HttpAddress> esAddressEntry = iterator.next();
			String host = esAddressEntry.getKey();
			HttpAddress address = esAddressEntry.getValue();
			if(hasHosts) {
				boolean exist = false;
				for (HttpHost httpHost : hosts) {
					if (httpHost.toString().equals(host)) {
						exist = true;
						break;
					}
				}
				if (!exist) {
					address.setStatus(2);
					if(logger.isInfoEnabled()){
						logger.info(new StringBuilder().append("Http pool[")
								.append(getClientConfiguration().getBeanName()).append("]").append(" Http Node[").append(address.toString()).append("] is down and removed.").toString());
					}
				}
			}
			else {
				address.setStatus(2);
				if(logger.isInfoEnabled()){
					logger.info(new StringBuilder().append("Http pool[")
							.append(getClientConfiguration().getBeanName()).append("]").append(" Http Node[").append(address.toString()).append("] is down and removed.").toString());
				}
			}

		}

	}


	public void recoverRemovedNodes(List<HttpHost> hosts) {
		if(hosts == null || hosts.size() == 0){
			return;
		}
		for(HttpHost httpHost: hosts) {
			HttpAddress address = this.addressMap.get(httpHost.toString());
			if(address != null  ){
				if(address.getStatus() == 2){//节点还原
					address.onlySetStatus(0);
					if(logger.isInfoEnabled()){
						logger.info(new StringBuilder().append("Recover Removed Node [").append(address.toString()).append("] to Http pool[")
						.append(getClientConfiguration().getBeanName()).append("] clusters addresses list.").toString());
					}
				}
			}
		}
	}
	public boolean containAddress(HttpAddress address){
		return addressMap.containsKey(address.getAddress());
	}



	public void setHealthCheckInterval(long healthCheckInterval) {
		httpServiceHostsConfig.setHealthCheckInterval(  healthCheckInterval);
	}


	public void setExceptionWare(String exceptionWare) {
		httpServiceHostsConfig.setExceptionWare(  exceptionWare);
	}

	public ExceptionWare getExceptionWare() {
		return exceptionWare;
	}
	public void setExceptionWareBean(ExceptionWare exceptionWareBean) {
		this.exceptionWare = exceptionWareBean;
	}
	public ClientConfiguration getClientConfiguration() {
		return clientConfiguration;
	}

	public void setClientConfiguration(ClientConfiguration clientConfiguration) {
		this.clientConfiguration = clientConfiguration;
	}

	public HttpHostDiscover getHostDiscover() {
		return hostDiscover;
	}

	public void setHostDiscover(HttpHostDiscover hostDiscover) {
		this.hostDiscover = hostDiscover;
	}

	public String getRouting() {
		return routing;
	}

	public void setRouting(String routing) {
		this.routing = routing;
	}
}
