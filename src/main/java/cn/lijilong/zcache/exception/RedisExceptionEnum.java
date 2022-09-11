package cn.lijilong.zcache.exception;

public enum RedisExceptionEnum {

    //实际返回类型与指定的返回类型不符
    RETURN_TYPE_NOT_EQUAL,
    //缓存中数据类型错误
    RETURN_TYPE_ERROR,
    //函数返回值为空
    METHOD_RETURN_NULL,
    //没有找到参数作为key去插入
    NO_KEY_TO_INSERT


}
