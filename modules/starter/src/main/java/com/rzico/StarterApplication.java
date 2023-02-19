package com.rzico;

import com.rzico.config.TomcatConfig;
import org.apache.catalina.connector.Connector;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import javax.annotation.PostConstruct;
import java.util.TimeZone;

@EnableWebMvc
@SpringBootApplication
@EnableScheduling
@EnableCaching
@EnableTransactionManagement
@EnableAutoConfiguration(exclude = {
		org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class
})
@ImportAutoConfiguration(value = {
		TomcatConfig.class,
		com.rzico.config.ServiceConfiguration.class,
		com.rzico.config.RedisCacheConfig.class,})
public class StarterApplication {

	@PostConstruct
	void started() {
		//时区设置：中国上海
		//time.zone: "Asia/Shanghai"
		TimeZone.setDefault(TimeZone.getTimeZone("Asia/Shanghai"));
	}

	public static void main(String[] args) {
		SpringApplication.run(StarterApplication.class, args);
	}

}
