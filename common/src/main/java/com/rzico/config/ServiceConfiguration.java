/**
 * 版权声明：厦门睿商网络科技有限公司 版权所有 违者必究
 * 日    期：2020-01-16
 */
package com.rzico.config;

import com.github.pagehelper.PageHelper;
import com.rzico.util.RedisHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

/**
 * ServiceConfiguration
 *
 * @author Rzico Boot
 * @version 1.0
 * @date 2020-01-16
 */
@Configuration
public class ServiceConfiguration {

    @Bean
    public RedisHandler redisHandler() {
        return new RedisHandler();
    }

    /**
     * 配置mybatis的分页插件pageHelper
     * @return
     */
    @Bean
    public PageHelper pageHelper(){
        PageHelper pageHelper = new PageHelper();
        Properties properties = new Properties();
        //RowBounds参数offset作为PageNum使用 - 默认不使用
        properties.setProperty("offsetAsPageNum","false");
        //RowBounds是否进行count查询 - 默认不查询
        properties.setProperty("rowBoundsWithCount","false");
        //分页合理化
        properties.setProperty("reasonable","true");
        //当设置为true的时候，如果pagesize设置为0，就不执行分页，返回全部结果
        properties.setProperty("pageSizeZero","true");
        properties.setProperty("dialect","mysql");
        pageHelper.setProperties(properties);
        return pageHelper;
    }

}
