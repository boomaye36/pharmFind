package com.pharmfind.pharmacy.cache;

import com.pharmfind.AbstractIntegrationContainerBaseTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.ValueOperations;

@SpringBootTest
public class RedisTemplateTest extends AbstractIntegrationContainerBaseTest {
    @Autowired
    private RedisTemplate redisTemplate;
    @Test
    void RedisTemplateOperations(){
        ValueOperations valueOperations = redisTemplate.opsForValue();
        String key = "stringKey";
        String value = "hello";

        valueOperations.set(key, value);
        String result = (String) valueOperations.get(key);
        Assertions.assertEquals(result, value);
    }

    @Test
    void RedisTemplateSetOperations(){
        SetOperations setOperations = redisTemplate.opsForSet();
        String key = "setKey";

        setOperations.add(key, "h", "e", "l", "l");
        Assertions.assertEquals(setOperations.size(key), 4);
    }
}
