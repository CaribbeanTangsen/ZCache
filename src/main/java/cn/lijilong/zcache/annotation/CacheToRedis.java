package cn.lijilong.zcache.annotation;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

@Documented
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
//从缓存池获取数据，若缓存池中存在数据则跳过执行直接返回，反之正常执行。
public @interface CacheToRedis {
    //入参变量名称
    String keyParamName();

    //key的前缀
    String preKey() default "";

    //获取缓存的类型
    Class<?> clazz();

    //若正常执行返回则自动同步到缓存
    boolean syncToRedis() default true;
    //超时时间 syncToRedis == true 时有效
    long timeOut() default 1;
    //超时时间单位 syncToRedis == true 时有效
    TimeUnit timeUnit() default TimeUnit.HOURS;

}
