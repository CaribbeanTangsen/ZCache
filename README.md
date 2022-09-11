# ZCache 缓存框架

> 项目详细说明文档即将上线
>
> 开源不易，bug或建议请发送邮件到 CaribbeanTangsen@163.com

## 起步

> 克隆项目
> ```shell
> git clone https://github.com/CaribbeanTangsen/ZCache.git
> ```
> 安装到本地maven仓库
> ```shell
> mvn install
> ```
> 引入到项目
> ```xml
> <dependency>
>   <groupId>cn.lijilong.zcache</groupId>
>   <artifactId>ZCache</artifactId>
>   <version>0.1.1-release</version>
> </dependency>
> ```

## 注解

> 函数注解 @CacheToRedis
>
> 函数执行前根据配置去redis查询相应的数据，如果数据存在则返回缓存内的数据，如果不存在则执行函数。函数返回值可根据配置进行缓存。
> ```java
>
>    //入参变量名称
>    String keyParamName();
>
>    //key的前缀
>    String preKey() default "";
>
>    //获取缓存的类型
>    Class<?> clazz();
>
>    //若正常执行返回则自动同步到缓存
>    boolean syncToRedis() default true;
>    //超时时间 syncToRedis == true 时有效
>    long timeOut() default 1;
>    //超时时间单位 syncToRedis == true 时有效
>    TimeUnit timeUnit() default TimeUnit.HOURS;
>
>```

> 函数注解 @SetToRedis
> 
> 函数执行结果根据配置缓存到redis。
> 
> ```java
>    //入参变量名称
>    String keyParamName();
>
>    //key的前缀
>    String preKey() default "";
>
>    //超时时间
>    long timeOut() default 1;
>
>    //超时时间单位
>    TimeUnit timeUnit() default TimeUnit.HOURS;
> ```
> 

## 服务类

> ZCache
> 
> 此类已自动注册到IOC
> 
> ```java
>
> /**
>   * 获取缓存对象
>   * @param key 键
>   * @param tClass 返回类型
>   * @return 返回值
>   */
> public <T> T getObj(String key, Class<T> tClass)
> 
> /**
>   * 获取缓存集合
>   * @param key 键
>   * @param tClass 集合类型
>   * @return 返回值
>   */
> public <T> Collection<T> getArray(String key, Class<T> tClass)
> 
> /**
>   * 插入数据到redis
>   * @param key 键
>   * @param value 值
>   * @param timeOut 超时时间
>   * @param timeUnit 时间单位
>   */
> public void setObj(String key, Object value, long timeOut, TimeUnit timeUnit)
> 
> /**
>   * 判断键是否存在
>   * @param key 键
>   * @return 返回值
>   */
> public boolean exist(String key)
> 
> /**
>   * 删除缓存
>   * @param key 键
>   */
> public void delete(String key)
> 
> /**
>   * 使用通配符删除
>   * @param key 键起始字段
>   */
> public void deleteAll(String key)
>```

> JsonRedisTemplate已注册到IOC