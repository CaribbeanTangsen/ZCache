package cn.lijilong.zcache.config;

import cn.lijilong.zcache.RedisAspect;
import cn.lijilong.zcache.ZCache;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.nio.charset.StandardCharsets;

@Slf4j
@Configuration
public class RedisConfig {

    @Bean(name = "JsonRedisTemplate")
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        log.info("Init RedisTemplate");
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        RedisSerializer<String> keyRedisSerializer = new StringRedisSerializer();
        RedisSerializer<Object> redisSerializer = new RedisSerializer<>() {
            @Override
            public byte[] serialize(Object o) throws SerializationException {
                return JSONObject.toJSONString(o).getBytes(StandardCharsets.UTF_8);
            }
            @Override
            public Object deserialize(byte[] bytes) throws SerializationException {
                if (bytes == null) {
                    return null;
                }
                return new String(bytes, StandardCharsets.UTF_8);
            }
        };
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.setKeySerializer(keyRedisSerializer);
        redisTemplate.setValueSerializer(redisSerializer);
        return redisTemplate;
    }

    @Bean
    public ZCache zCache() {
        return new ZCache();
    }

    @Bean
    public RedisAspect redisAspect() {
        return new RedisAspect();
    }

}
