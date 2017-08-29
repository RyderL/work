package com.zterc.uos.base.cache.redis;

import java.util.Set;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisClusterCommand;
import redis.clients.jedis.JedisClusterConnectionHandler;
import redis.clients.jedis.JedisSlotBasedConnectionHandler;
import redis.clients.util.SafeEncoder;

public class BinaryJedisCluster extends JedisCluster {

	private int maxRedirections;

	private JedisClusterConnectionHandler connectionHandler;

	public BinaryJedisCluster(Set<HostAndPort> nodes,
			GenericObjectPoolConfig poolConfig) {
		super(nodes, poolConfig);
	}

	public BinaryJedisCluster(Set<HostAndPort> nodes, int timeout,
			GenericObjectPoolConfig poolConfig) {
		super(nodes, timeout, poolConfig);
	}

	public BinaryJedisCluster(Set<HostAndPort> jedisClusterNode, int timeout,
			int maxRedirections, GenericObjectPoolConfig poolConfig) {
		super(jedisClusterNode, timeout, maxRedirections, poolConfig);
	}

	public BinaryJedisCluster(Set<HostAndPort> nodes, int timeout,
			int maxRedirections) {
		super(nodes, timeout, maxRedirections);
	}

	public BinaryJedisCluster(Set<HostAndPort> nodes, int timeout) {
		super(nodes, timeout);
	}

	public BinaryJedisCluster(Set<HostAndPort> nodes) {
		super(nodes);
	}

	public BinaryJedisCluster(Set<HostAndPort> jedisClusterNode,
			int connectionTimeout, int soTimeout, int maxRedirections,
			final GenericObjectPoolConfig poolConfig) {
		super(jedisClusterNode, connectionTimeout, soTimeout, maxRedirections,
				poolConfig);
		this.connectionHandler = new JedisSlotBasedConnectionHandler(
				jedisClusterNode, poolConfig, connectionTimeout, soTimeout);
		this.maxRedirections = maxRedirections;
	}

	public String setBinary(final String key, final byte[] bytes) {
		return new JedisClusterCommand<String>(connectionHandler,
				maxRedirections) {
			@Override
			public String execute(Jedis connection) {
				return connection.set(SafeEncoder.encode(key), bytes);
			}
		}.run(key);
	}

	public byte[] getBinary(final String key) {
		return new JedisClusterCommand<byte[]>(connectionHandler,
				maxRedirections) {
			@Override
			public byte[] execute(Jedis connection) {
				return connection.get(SafeEncoder.encode(key));
			}
		}.run(key);
	}
}
