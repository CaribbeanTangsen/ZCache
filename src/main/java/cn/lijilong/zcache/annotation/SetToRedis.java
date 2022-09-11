package cn.lijilong.zcache.annotation;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

@Documented
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
//将代理函数的返回值储存到缓存池默认key为入参的变量
public @interface SetToRedis {

    //入参变量名称
    String keyParamName();

    //key的前缀
    String preKey() default "";

    //超时时间
    long timeOut() default 1;

    //超时时间单位
    TimeUnit timeUnit() default TimeUnit.HOURS;

}
