package com.redis.config;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.validation.constraints.NotNull;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration.DefaultJedisClientConfigurationBuilder;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

public class ElastiCacheService {

	private static final Logger LOG = LoggerFactory.getLogger(ElastiCacheService.class);

	private static final String SET_ERR = "Error while setValue in cache {} ";

	private RedisTemplate<String, Object> redisTemplate;

	private ElastiCacheService() {

	}

	public static ElastiCacheService getInstance(String host, Integer port,
			@NotNull GenericObjectPoolConfig poolConfig) {
		ElastiCacheService service = new ElastiCacheService();

		JedisClientConfiguration.DefaultJedisClientConfigurationBuilder builder = (DefaultJedisClientConfigurationBuilder) JedisClientConfiguration
				.builder().usePooling().poolConfig(poolConfig);

		JedisClientConfiguration jedisConfiguration = builder.build();

		JedisConnectionFactory jedisConnectionFactory = null;

		RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration(host, port);

		jedisConnectionFactory = new JedisConnectionFactory(redisStandaloneConfiguration, jedisConfiguration);

		jedisConnectionFactory.afterPropertiesSet();

		service.redisTemplate = new RedisTemplate<>();

		service.redisTemplate.setConnectionFactory(jedisConnectionFactory);
		service.redisTemplate.setKeySerializer(new StringRedisSerializer());
		service.redisTemplate.setHashValueSerializer(new GenericToStringSerializer<Object>(Object.class));
		service.redisTemplate.setValueSerializer(new GenericToStringSerializer<Object>(Object.class));
		service.redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());

		service.redisTemplate.afterPropertiesSet();

		return service;
	}

	public void deleteKey(String key) {
		try {

			redisTemplate.delete(key);

		} catch (Exception e) {
			LOG.error(SET_ERR, e);
		}
	}

	public void deleteKey(Collection<String> keys) {
		try {

			redisTemplate.delete(keys);

		} catch (Exception e) {
			LOG.error(SET_ERR, e);
		}
	}

	public void setValue(String key, String value) {
		try {

			redisTemplate.opsForValue().set(key, value);

		} catch (Exception e) {
			LOG.error(SET_ERR, e);
		}

	}

	public void setObject(String key, Object value) {
		try {

			String jsonStr = MarshallingUtil.marshalToJson(value);
			redisTemplate.opsForValue().set(key, jsonStr);

		} catch (Exception e) {
			LOG.error(SET_ERR, e);
		}
	}

	public void setValue(String key, String value, long expiryTime, TimeUnit tu) {
		try {

			redisTemplate.opsForValue().set(key, value, expiryTime, tu);

		} catch (Exception e) {
			LOG.error(SET_ERR, e);
		}
	}

	public void setObject(String key, Object value, long expiryTime, TimeUnit tu) {
		try {

			String jsonStr = MarshallingUtil.marshalToJson(value);
			redisTemplate.opsForValue().set(key, jsonStr, expiryTime, tu);

		} catch (Exception e) {
			LOG.error(SET_ERR, e);
		}

	}

	/**
	 * Get the value of {@code key}.
	 *
	 * @param key must not be {@literal null}. if object is required, unmarshal it
	 *            from the json returned form this method
	 */
	public String getValue(String key) {
		String value = null;
		try {

			value = (String) redisTemplate.opsForValue().get(key);

		} catch (Exception e) {
			LOG.error(" Error while getValue from cache {} ", e);
		}
		return value;
	}

	/**
	 * 
	 * @param keyRegex must not be {@literal null}.
	 * @return set of keys that matches the keyRegex
	 */

	public Set<String> getKeys(@NotNull String keyRegex) {

		try {
			return redisTemplate.keys(keyRegex);
		} catch (Exception e) {
			LOG.error(" Error while getting keys (" + keyRegex + ") from cache ", e);
			return Collections.emptySet();
		}

	}

	/**
	 * 
	 * @param keys
	 * @return list of objects that matches the Keys
	 */
	public List<Object> multiGet(@NotNull Collection<String> keys) {
		try {
			return redisTemplate.opsForValue().multiGet(keys);
		} catch (Exception e) {
			LOG.error(" Error while getting  list of objects from cache ", e);
			return Collections.emptyList();
		}

	}

}
