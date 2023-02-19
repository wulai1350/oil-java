package com.rzico.config;

import io.lettuce.core.dynamic.annotation.Value;
import org.apache.catalina.connector.Connector;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.ConfigurableWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TomcatConfig {

    @org.springframework.beans.factory.annotation.Value("${server.port}")
    private Integer port;

    @Bean
    public WebServerFactoryCustomizer<ConfigurableWebServerFactory> webServerFactoryCustomizer() {
        return new WebServerFactoryCustomizer<ConfigurableWebServerFactory>() {
            @Override
            public void customize(ConfigurableWebServerFactory factory) {
                if (port == 8080) {
                    return;
                }
                if (port == 9090) {
                    return;
                }
                if (factory instanceof TomcatServletWebServerFactory) {
                    TomcatServletWebServerFactory webServerFactory = (TomcatServletWebServerFactory)factory;
                    Connector connector = new Connector(TomcatServletWebServerFactory.DEFAULT_PROTOCOL);
                    // 设置http访问的端口号，不能与https端口重复，否则会报端口被占用的错误
                    connector.setPort(8080);
                    webServerFactory.addAdditionalTomcatConnectors(connector);

                    Connector connector1 = new Connector(TomcatServletWebServerFactory.DEFAULT_PROTOCOL);
                    // 设置http访问的端口号，不能与https端口重复，否则会报端口被占用的错误
                    connector1.setPort(1980);
                    webServerFactory.addAdditionalTomcatConnectors(connector1);
                }
            }
        };
    }

}