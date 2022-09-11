package cn.lijilong.zcache.exception;

import lombok.Data;

@Data
public class RedisException extends RuntimeException {
    private RedisExceptionEnum redisExceptionEnum;

    public RedisException(RedisExceptionEnum redisExceptionEnum) {
        this.redisExceptionEnum = redisExceptionEnum;
    }
}
