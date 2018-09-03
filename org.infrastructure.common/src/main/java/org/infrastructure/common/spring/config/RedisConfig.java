package org.infrastructure.common.spring.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.google.common.base.Strings;

import redis.clients.jedis.JedisPoolConfig;


@ComponentScan(basePackages = "org.infrastructure.common.service.spring.redis")
@EnableConfigurationProperties(RedisProperties.class)//开启属性注入,通过@autowired注入  
@Configuration
public class RedisConfig {
	@Autowired
	private RedisProperties properties;

	@Bean
	public RedisConnectionFactory jedisConnectionFactory() {
		JedisPoolConfig poolConfig = new JedisPoolConfig();
		poolConfig.setMaxTotal(properties.getPool().getMaxActive());
		poolConfig.setMaxIdle(properties.getPool().getMaxIdle());
		poolConfig.setMaxWaitMillis(properties.getPool().getMaxWait());
		poolConfig.setTestOnBorrow(true);
		poolConfig.setTestOnCreate(true);
		poolConfig.setTestWhileIdle(true);
		JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory(poolConfig);
		jedisConnectionFactory.setHostName(properties.getHost());
		if (!Strings.isNullOrEmpty(properties.getPassword())) {
			jedisConnectionFactory.setPassword(properties.getPassword());
		}
		jedisConnectionFactory.setDatabase(properties.getDatabase());
		jedisConnectionFactory.setPort(properties.getPort());
		jedisConnectionFactory.setTimeout(properties.getTimeout());

		// 其他配置，可再次扩展

		return jedisConnectionFactory;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Bean(name="redisTemplate")    
    public RedisTemplate redisTemplate(){    
        RedisTemplate redisTemplate = new RedisTemplate();    
          
        redisTemplate.setConnectionFactory(jedisConnectionFactory());    
          
        redisTemplate.setHashValueSerializer(new StringRedisSerializer());  
        redisTemplate.setKeySerializer(new StringRedisSerializer());  
        redisTemplate.setValueSerializer(new StringRedisSerializer());  
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());  
          
        redisTemplate.afterPropertiesSet();  
        redisTemplate.setEnableTransactionSupport(true);  
          
        return redisTemplate;    
    }
}