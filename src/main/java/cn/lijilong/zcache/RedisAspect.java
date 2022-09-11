package cn.lijilong.zcache;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import cn.lijilong.zcache.annotation.CacheToRedis;
import cn.lijilong.zcache.annotation.SetToRedis;
import cn.lijilong.zcache.exception.RedisException;
import cn.lijilong.zcache.exception.RedisExceptionEnum;

import javax.annotation.Resource;
import java.util.Collection;

@Aspect
public class RedisAspect {

    @Resource
    ZCache zCache;

    @Pointcut("@annotation(cn.lijilong.zcache.annotation.CacheToRedis)")
    public void cacheToRedis() {
    }

    @Pointcut("@annotation(cn.lijilong.zcache.annotation.SetToRedis)")
    public void setToRedis() {
    }

    @Around("cacheToRedis()")
    public Object cache(ProceedingJoinPoint pjp) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) pjp.getSignature();
        CacheToRedis annotation = methodSignature.getMethod().getAnnotation(CacheToRedis.class);
        String[] parameterNames = methodSignature.getParameterNames();
        Object[] args = pjp.getArgs();
        String key = null;
        for (int i = 0; i < parameterNames.length; i++) {
            if (parameterNames[i].equals(annotation.keyParamName())) {
                key = args[i].toString();
                if (key == null) {
                    throw new RedisException(RedisExceptionEnum.NO_KEY_TO_INSERT);
                }
                try {
                    Object obj;
                    if (Collection.class.isAssignableFrom(methodSignature.getReturnType())) {
                        obj = zCache.getArray(annotation.preKey() + key, annotation.clazz());
                    } else {
                        obj = zCache.getObj(annotation.preKey() + key, annotation.clazz());
                    }
                    if (obj != null) {
                        return obj;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new RedisException(RedisExceptionEnum.RETURN_TYPE_ERROR);
                }
            }
        }
        if (key == null) {
            throw new RedisException(RedisExceptionEnum.NO_KEY_TO_INSERT);
        }
        Object proceed = pjp.proceed();
        if (proceed != null && annotation.syncToRedis()) {
            if (Collection.class.isAssignableFrom(methodSignature.getReturnType())) {
                if (JSONArray.parseArray(JSONObject.toJSONString(proceed)).size() > 0) {
                    zCache.setObj(annotation.preKey() + key, proceed, annotation.timeOut(), annotation.timeUnit());
                }
            } else {
                zCache.setObj(annotation.preKey() + key, proceed, annotation.timeOut(), annotation.timeUnit());
            }
        }

        return proceed;
    }

    @Around("setToRedis()")
    public Object set(ProceedingJoinPoint pjp) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) pjp.getSignature();
        if (methodSignature.getReturnType().toString().equals("void")) {
            throw new RedisException(RedisExceptionEnum.METHOD_RETURN_NULL);
        }
        SetToRedis annotation = methodSignature.getMethod().getAnnotation(SetToRedis.class);
        Object[] args = pjp.getArgs();
        String[] parameterNames = methodSignature.getParameterNames();
        String key = null;
        for (int i = 0; i < parameterNames.length; i++) {
            if (parameterNames[i].equals(annotation.keyParamName())) {
                key = String.valueOf(args[i]);
            }
        }
        if (key == null) {
            throw new RedisException(RedisExceptionEnum.NO_KEY_TO_INSERT);
        }
        Object proceed = pjp.proceed();
        if (proceed != null) {
            if (Collection.class.isAssignableFrom(methodSignature.getReturnType())) {
                if (JSONArray.parseArray(JSONObject.toJSONString(proceed)).size() > 0) {
                    zCache.setObj(annotation.preKey() + key, proceed, annotation.timeOut(), annotation.timeUnit());
                }
            } else {
                zCache.setObj(annotation.preKey() + key, proceed, annotation.timeOut(), annotation.timeUnit());
            }
        }
        return proceed;

    }

}
