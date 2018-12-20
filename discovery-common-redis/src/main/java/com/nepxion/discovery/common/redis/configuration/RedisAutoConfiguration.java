package com.nepxion.discovery.common.redis.configuration;

/**
 * <p>Title: Nepxion Discovery</p>
 * <p>Description: Nepxion Discovery</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @author JiKai Sun
 * @version 1.0
 */

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;

import com.nepxion.discovery.common.redis.operation.RedisOperation;

@Configuration
public class RedisAutoConfiguration {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Bean
    public HashOperations<String, String, String> hashOperations() {
        return stringRedisTemplate.opsForHash();
    }

    @Bean
    public RedisOperation redisOperation() {
        return new RedisOperation();
    }
}