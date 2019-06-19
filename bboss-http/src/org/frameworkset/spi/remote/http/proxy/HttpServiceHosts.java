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
	private String authAccount;
	private String authPassword;
	private String health;
	private long healthCheckInterval = -1l;
	private String discoverService;
	private String exceptionWare;
	private ExceptionWare exceptionWareBean;
	private Map<String, String> authHeaders;
	protected RoundRobinList serversList;
	protected List<HttpAddress> addressList;
	private HealthCheck healthCheck;
	private Map<String,HttpAddress> addressMap ;
	private String hosts;
	private ClientConfiguration clientConfiguration;
	public HttpServiceHosts(){

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
		{
			addressList = new ArrayList<HttpAddress>();
			addressMap = new HashMap<String,HttpAddress>();
			if(hosts != null && !hosts.trim().equals("")) {
				String[] hostNames = hosts.split(",");
				for (String host : hostNames) {
					HttpAddress esAddress = new HttpAddress(host.trim(), health);
					addressList.add(esAddress);
					addressMap.put(esAddress.getAddress(), esAddress);
				}
			}
			serversList = new RoundRobinList(addressList);
			if (authAccount != null && !authAccount.equals("")) {
				authHeaders = new HashMap<String, String>();
				authHeaders.put("Authorization", getHeader(getAuthAccount(), getAuthPassword()));
			}
			if(exceptionWare != null){
				try {
					Class<ExceptionWare> exceptionWareClass = (Class<ExceptionWare>) Class.forName(this.exceptionWare.trim());
					ExceptionWare exceptionWare_ = exceptionWareClass.newInstance();
					exceptionWare_.setHttpServiceHosts(this);
					this.exceptionWareBean = exceptionWare_;
				}
				catch (Exception e){
					if(logger.isErrorEnabled()) {
						logger.error(" ExceptionWare init failed:", e);
					}
				}
			}

			if(healthCheckInterval > 0 && this.health != null && !this.health.equals("")) {
				if(logger.isInfoEnabled()) {
					logger.info("Start HttpProxy server healthCheck thread,you can set http.healthCheckInterval=-1 in config file to disable healthCheck thread.");
				}
				healthCheck = new HealthCheck(addressList, healthCheckInterval,authHeaders);
				healthCheck.run();
			}
			else {
				if(logger.isInfoEnabled()) {
					logger.info("HttpProxy server healthCheck disable,you can set HttpProxy http.healthCheckInterval (>0) and http.health in configfile to enabled healthCheck thread.");
				}

			}

			if(this.discoverService != null && !this.discoverService.equals("")) {
				logger.info("Start http server discoverHost thread,to distabled set http.discoverService to null in configfile.");

				try {
					Class<HttpHostDiscover> httpHostDiscoverClass = (Class<HttpHostDiscover>) Class.forName(this.discoverService);
					HttpHostDiscover hostDiscover = httpHostDiscoverClass.newInstance();
					hostDiscover.setHttpServiceHosts(this);
					hostDiscover.start();
				}
				catch (Exception e){
					if(logger.isErrorEnabled()) {
						logger.error("Start discovery service failed:", e);
					}
				}
			}
			else {
				logger.info("Discover  http server is disabled,to enabled set http.discoverService in configfile.");
			}
		}
	}

	public String getAuthAccount() {
		return authAccount;
	}

	public void setAuthAccount(String authAccount) {
		this.authAccount = authAccount;
	}

	public String getAuthPassword() {
		return authPassword;
	}

	public void setAuthPassword(String authPassword) {
		this.authPassword = authPassword;
	}

	public String getHealth() {
		return health;
	}

	public void setHealth(String health) {
		this.health = health;
	}

	public String getDiscoverService() {
		return discoverService;
	}

	public void setDiscoverService(String discoverService) {
		this.discoverService = discoverService;
	}

	public Map<String, String> getAuthHeaders() {
		return authHeaders;
	}
	public void toString(StringBuilder log){
		log.append(",http.authAccount=").append(authAccount);
		log.append(",http.authPassword=").append(authPassword);
		log.append(",http.hosts=").append(hosts);
		log.append(",http.health=").append(health);
		log.append(",http.healthCheckInterval=").append(healthCheckInterval);
		log.append(",http.discoverService=").append(discoverService);
	}

	public String getHosts() {
		return hosts;
	}

	public void setHosts(String hosts) {
		this.hosts = hosts;
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
			info.append("All Live Http Servers:");
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
						logger.info("Http Node["+address.toString()+"] is down and removed.");
					}
				}
			}
			else {
				address.setStatus(2);
				if(logger.isInfoEnabled()){
					logger.info("Http Node["+address.toString()+"] is down and removed.");
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
				}
			}
		}
	}
	public boolean containAddress(HttpAddress address){
		return addressMap.containsKey(address.getAddress());
	}

	public long getHealthCheckInterval() {
		return healthCheckInterval;
	}

	public void setHealthCheckInterval(long healthCheckInterval) {
		this.healthCheckInterval = healthCheckInterval;
	}

	public String getExceptionWare() {
		return exceptionWare;
	}

	public void setExceptionWare(String exceptionWare) {
		this.exceptionWare = exceptionWare;
	}

	public ExceptionWare getExceptionWareBean() {
		return exceptionWareBean;
	}

	public ClientConfiguration getClientConfiguration() {
		return clientConfiguration;
	}

	public void setClientConfiguration(ClientConfiguration clientConfiguration) {
		this.clientConfiguration = clientConfiguration;
	}
}
