package com.redis.config;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//@Configuration
public class ElastiCacheConfig {

	@Value("${spring.redis.host}")
	private String host;
	
	@Value("${spring.redis.port}")
	private Integer port;
	
	@Value("${spring.redis.lettuce.pool.max-idle}")
	private String maxIdle;
	
	@Value("${spring.redis.lettuce.pool.min-idle}")
	private String minIdle;
	
	@Value("${spring.redis.lettuce.pool.max-wait}")
	private String maxWait;
	
	private static final String DEFAULT_CLUSTER = "false";
	
	@Bean("dynamicCacheService")
	ElastiCacheService testDynamicCacheService() {
		return ElastiCacheService.getInstance("hmscloud-dyn-cache.5evooa.clustercfg.euw1.cache.amazonaws.com", 6379, poolConfig());
	}

	@Bean
	GenericObjectPoolConfig poolConfig() {

		GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();

		poolConfig.setMaxIdle(
				Integer.parseInt(maxIdle));
		poolConfig.setMinIdle(
				Integer.parseInt(minIdle));
		poolConfig.setMaxWaitMillis(Long
				.parseLong(maxWait));

		return poolConfig;

	}

}
