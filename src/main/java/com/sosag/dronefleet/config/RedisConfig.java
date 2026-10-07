package com.sosag.dronefleet.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * Redis configuration class.
 * Configures a RedisTemplate with String serializers to ensure
 * human-readable keys and values in Redis, making it easy to
 * inspect data from external tools (CLI, Python scripts, etc.).
 */
@Configuration
public class RedisConfig {

    /**
     * Creates and configures a RedisTemplate bean for String-based
     * key-value operations.
     *
     * @param connectionFactory the Redis connection factory provided by Spring Boot
     * @return a configured RedisTemplate instance
     */
    @Bean
    public RedisTemplate<String, String> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, String> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        // Use String serializers for human-readable keys and values
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(new StringRedisSerializer());
        return template;
    }
}