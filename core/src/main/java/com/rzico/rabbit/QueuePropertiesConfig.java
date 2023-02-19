package com.rzico.rabbit;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;

/**
 * Created by icssh on 2020/2/23.
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "message")
public class QueuePropertiesConfig {

    private List<Map<String, String>> exchangeMapList;

    private List<Map<String, String>> queueMapList;

}
