package cn.lijilong.zcache.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Data
@ConfigurationProperties(prefix = ZCacheConfigurationProperties.CONFIG_PREFIX)
public class ZCacheConfigurationProperties {

    public static final String CONFIG_PREFIX = "zcache.config";

}


