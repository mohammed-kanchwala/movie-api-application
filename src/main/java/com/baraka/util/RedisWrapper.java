package com.baraka.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class RedisWrapper {

    private RedisTemplate<String, Object> redisTemplate;

    private long expireTime;

    @Autowired
    public RedisWrapper(RedisTemplate<String, Object> redisTemplate,
                        @Value("${spring.redis.expiry}") long expireTime) {
        this.redisTemplate = redisTemplate;
        this.expireTime = expireTime;
    }

    public <T> void saveToRedis(String key,
                                T data) {
        log.debug("Adding Value to Redis for Key :: {}", key);
        redisTemplate.opsForValue().set(key, data, expireTime, TimeUnit.MINUTES);
    }

    public <T> T getValueFromRedis(String key) {
        log.debug("Retrieving Value from Redis for Key :: {}", key);
        return (T) redisTemplate.opsForValue().get(key);
    }

    public Boolean hasKey(String key) {
        log.debug("Checking Redis for Key :: {}", key);
        return redisTemplate.hasKey(key);
    }

}
