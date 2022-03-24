package org.frameworkset.nosql.redis;
/**
 * Copyright 2022 bboss
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

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import redis.clients.jedis.Connection;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisClientConfig;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.providers.ClusterConnectionProvider;

import java.time.Duration;
import java.util.Set;

/**
 * <p>Description: </p>
 * <p></p>
 * <p>Copyright (c) 2020</p>
 * @Date 2022/3/24
 * @author biaoping.yin
 * @version 1.0
 */
public class ProviderJedisCluster extends JedisCluster {
	public ProviderJedisCluster(HostAndPort node) {
		super(node);
	}

	public ProviderJedisCluster(HostAndPort node, int timeout) {
		super(node, timeout);
	}

	public ClusterConnectionProvider getClusterConnectionProvider(){
		return (ClusterConnectionProvider)provider;
	}

	public ProviderJedisCluster(HostAndPort node, int timeout, int maxAttempts) {
		super(node, timeout, maxAttempts);
	}

	public ProviderJedisCluster(HostAndPort node, GenericObjectPoolConfig<Connection> poolConfig) {
		super(node, poolConfig);
	}

	public ProviderJedisCluster(HostAndPort node, int timeout, GenericObjectPoolConfig<Connection> poolConfig) {
		super(node, timeout, poolConfig);
	}

	public ProviderJedisCluster(HostAndPort node, int timeout, int maxAttempts, GenericObjectPoolConfig<Connection> poolConfig) {
		super(node, timeout, maxAttempts, poolConfig);
	}

	public ProviderJedisCluster(HostAndPort node, int connectionTimeout, int soTimeout, int maxAttempts, GenericObjectPoolConfig<Connection> poolConfig) {
		super(node, connectionTimeout, soTimeout, maxAttempts, poolConfig);
	}

	public ProviderJedisCluster(HostAndPort node, int connectionTimeout, int soTimeout, int maxAttempts, String password, GenericObjectPoolConfig<Connection> poolConfig) {
		super(node, connectionTimeout, soTimeout, maxAttempts, password, poolConfig);
	}

	public ProviderJedisCluster(HostAndPort node, int connectionTimeout, int soTimeout, int maxAttempts, String password, String clientName, GenericObjectPoolConfig<Connection> poolConfig) {
		super(node, connectionTimeout, soTimeout, maxAttempts, password, clientName, poolConfig);
	}

	public ProviderJedisCluster(HostAndPort node, int connectionTimeout, int soTimeout, int maxAttempts, String user, String password, String clientName, GenericObjectPoolConfig<Connection> poolConfig) {
		super(node, connectionTimeout, soTimeout, maxAttempts, user, password, clientName, poolConfig);
	}

	public ProviderJedisCluster(HostAndPort node, int connectionTimeout, int soTimeout, int maxAttempts, String password, String clientName, GenericObjectPoolConfig<Connection> poolConfig, boolean ssl) {
		super(node, connectionTimeout, soTimeout, maxAttempts, password, clientName, poolConfig, ssl);
	}

	public ProviderJedisCluster(HostAndPort node, int connectionTimeout, int soTimeout, int maxAttempts, String user, String password, String clientName, GenericObjectPoolConfig<Connection> poolConfig, boolean ssl) {
		super(node, connectionTimeout, soTimeout, maxAttempts, user, password, clientName, poolConfig, ssl);
	}

	public ProviderJedisCluster(HostAndPort node, JedisClientConfig clientConfig, int maxAttempts, GenericObjectPoolConfig<Connection> poolConfig) {
		super(node, clientConfig, maxAttempts, poolConfig);
	}

	public ProviderJedisCluster(Set<HostAndPort> nodes) {
		super(nodes);
	}

	public ProviderJedisCluster(Set<HostAndPort> nodes, int timeout) {
		super(nodes, timeout);
	}

	public ProviderJedisCluster(Set<HostAndPort> nodes, int timeout, int maxAttempts) {
		super(nodes, timeout, maxAttempts);
	}

	public ProviderJedisCluster(Set<HostAndPort> nodes, String user, String password) {
		super(nodes, user, password);
	}

	public ProviderJedisCluster(Set<HostAndPort> nodes, GenericObjectPoolConfig<Connection> poolConfig) {
		super(nodes, poolConfig);
	}

	public ProviderJedisCluster(Set<HostAndPort> nodes, int timeout, GenericObjectPoolConfig<Connection> poolConfig) {
		super(nodes, timeout, poolConfig);
	}

	public ProviderJedisCluster(Set<HostAndPort> clusterNodes, int timeout, int maxAttempts, GenericObjectPoolConfig<Connection> poolConfig) {
		super(clusterNodes, timeout, maxAttempts, poolConfig);
	}

	public ProviderJedisCluster(Set<HostAndPort> clusterNodes, int connectionTimeout, int soTimeout, int maxAttempts, GenericObjectPoolConfig<Connection> poolConfig) {
		super(clusterNodes, connectionTimeout, soTimeout, maxAttempts, poolConfig);
	}

	public ProviderJedisCluster(Set<HostAndPort> clusterNodes, int connectionTimeout, int soTimeout, int maxAttempts, String password, GenericObjectPoolConfig<Connection> poolConfig) {
		super(clusterNodes, connectionTimeout, soTimeout, maxAttempts, password, poolConfig);
	}

	public ProviderJedisCluster(Set<HostAndPort> clusterNodes, int connectionTimeout, int soTimeout, int maxAttempts, String password, String clientName, GenericObjectPoolConfig<Connection> poolConfig) {
		super(clusterNodes, connectionTimeout, soTimeout, maxAttempts, password, clientName, poolConfig);
	}

	public ProviderJedisCluster(Set<HostAndPort> clusterNodes, int connectionTimeout, int soTimeout, int maxAttempts, String user, String password, String clientName, GenericObjectPoolConfig<Connection> poolConfig) {
		super(clusterNodes, connectionTimeout, soTimeout, maxAttempts, user, password, clientName, poolConfig);
	}

	public ProviderJedisCluster(Set<HostAndPort> clusterNodes, int connectionTimeout, int soTimeout, int infiniteSoTimeout, int maxAttempts, String user, String password, String clientName, GenericObjectPoolConfig<Connection> poolConfig) {
		super(clusterNodes, connectionTimeout, soTimeout, infiniteSoTimeout, maxAttempts, user, password, clientName, poolConfig);
	}

	public ProviderJedisCluster(Set<HostAndPort> clusterNodes, int connectionTimeout, int soTimeout, int maxAttempts, String password, String clientName, GenericObjectPoolConfig<Connection> poolConfig, boolean ssl) {
		super(clusterNodes, connectionTimeout, soTimeout, maxAttempts, password, clientName, poolConfig, ssl);
	}

	public ProviderJedisCluster(Set<HostAndPort> clusterNodes, int connectionTimeout, int soTimeout, int maxAttempts, String user, String password, String clientName, GenericObjectPoolConfig<Connection> poolConfig, boolean ssl) {
		super(clusterNodes, connectionTimeout, soTimeout, maxAttempts, user, password, clientName, poolConfig, ssl);
	}

	public ProviderJedisCluster(Set<HostAndPort> clusterNodes, JedisClientConfig clientConfig, int maxAttempts, GenericObjectPoolConfig<Connection> poolConfig) {
		super(clusterNodes, clientConfig, maxAttempts, poolConfig);
	}

	public ProviderJedisCluster(Set<HostAndPort> clusterNodes, JedisClientConfig clientConfig, int maxAttempts, Duration maxTotalRetriesDuration, GenericObjectPoolConfig<Connection> poolConfig) {
		super(clusterNodes, clientConfig, maxAttempts, maxTotalRetriesDuration, poolConfig);
	}

	public ProviderJedisCluster(Set<HostAndPort> clusterNodes, JedisClientConfig clientConfig) {
		super(clusterNodes, clientConfig);
	}

	public ProviderJedisCluster(Set<HostAndPort> clusterNodes, JedisClientConfig clientConfig, int maxAttempts) {
		super(clusterNodes, clientConfig, maxAttempts);
	}

	public ProviderJedisCluster(Set<HostAndPort> clusterNodes, JedisClientConfig clientConfig, int maxAttempts, Duration maxTotalRetriesDuration) {
		super(clusterNodes, clientConfig, maxAttempts, maxTotalRetriesDuration);
	}
}
