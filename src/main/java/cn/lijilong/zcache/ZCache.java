package cn.lijilong.zcache;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class ZCache {

    @Resource(name = "JsonRedisTemplate")
    RedisTemplate<String, Object> redisTemplate;

    /**
     * 获取缓存对象
     * @param key 键
     * @param tClass 返回类型
     * @return 返回值
     */
    public <T> T getObj(String key, Class<T> tClass) {
        if (key == null) {
            return null;
        }
        Object o = redisTemplate.opsForValue().get(key);
        if (o == null) {
            return null;
        }
        return JSONObject.parseObject(o.toString(), tClass);
    }

    /**
     * 获取缓存集合
     * @param key 键
     * @param tClass 集合类型
     * @return 返回值
     */
    public <T> Collection<T> getArray(String key, Class<T> tClass) {
        if (key == null) {
            return null;
        }
        Object o = redisTemplate.opsForValue().get(key);
        if (o == null) {
            return null;
        }
        return JSONArray.parseArray(o.toString(), tClass);
    }

    /**
     * 插入数据到redis
     * @param key 键
     * @param value 值
     * @param timeOut 超时时间
     * @param timeUnit 时间单位
     */
    public void setObj(String key, Object value, long timeOut, TimeUnit timeUnit) {
        redisTemplate.opsForValue().set(key, value, timeOut, timeUnit);
    }

    /**
     * 判断键是否存在
     * @param key 键
     * @return 返回值
     */
    public boolean exist(String key){
        Boolean flag = true;
        try {
            flag = redisTemplate.hasKey(key);
        }catch (Exception e ){
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * 删除缓存
     * @param key 键
     */
    public void delete(String key) {
        redisTemplate.delete(key);
    }

    /**
     * 使用通配符删除
     * @param key 键起始字段
     */
    public void deleteAll(String key) {
        Set<String> keys = redisTemplate.keys(key + "**");
        if (keys == null) {
            return;
        }
        keys.forEach(k->{
            redisTemplate.delete(k);
        });
    }

    /**
     * 根据表达式获取key
     * @param pattern 表达式
     * @return keys
     */
    public Collection<String> getKeys(String pattern) {
        return redisTemplate.keys(pattern);
    }
}
