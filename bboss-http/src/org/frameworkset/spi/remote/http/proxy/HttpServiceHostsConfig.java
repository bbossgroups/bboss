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

/**
 * <p>Description: </p>
 * <p></p>
 * <p>Copyright (c) 2018</p>
 * @Date 2019/6/20 20:47
 * @author biaoping.yin
 * @version 1.0
 */
public class HttpServiceHostsConfig {
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

	public long getHealthCheckInterval() {
		return healthCheckInterval;
	}

	public void setHealthCheckInterval(long healthCheckInterval) {
		this.healthCheckInterval = healthCheckInterval;
	}

	public String getDiscoverService() {
		return discoverService;
	}

	public void setDiscoverService(String discoverService) {
		this.discoverService = discoverService;
	}

	public String getExceptionWare() {
		return exceptionWare;
	}

	public void setExceptionWare(String exceptionWare) {
		this.exceptionWare = exceptionWare;
	}

	public String getHosts() {
		return hosts;
	}

	public void setHosts(String hosts) {
		this.hosts = hosts;
	}

	private String authAccount;
	private String authPassword;
	private String health;
	private long healthCheckInterval = -1l;
	private String discoverService;
	private String exceptionWare;
	private String hosts;
	public void toString(StringBuilder log, ExceptionWare exceptionWareBean, HttpHostDiscover httpHostDiscover){
		log.append(",http.authAccount=").append(authAccount);
		log.append(",http.authPassword=").append(authPassword);
		log.append(",http.hosts=").append(hosts);
		log.append(",http.health=").append(health);
		log.append(",http.healthCheckInterval=").append(healthCheckInterval);
		if(exceptionWare != null)
			log.append(",http.exceptionWare=").append(exceptionWare);
		else if(exceptionWareBean != null){
			log.append(",http.exceptionWare=").append(exceptionWareBean.getClass().getCanonicalName());
		}
		if(discoverService != null)
			log.append(",http.discoverService=").append(discoverService);
		else if(httpHostDiscover != null){
			log.append(",http.discoverService=").append(httpHostDiscover.getClass().getCanonicalName());
		}
	}
}
