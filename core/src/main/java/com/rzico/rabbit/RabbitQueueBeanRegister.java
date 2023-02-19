package com.rzico.rabbit;

import com.alibaba.fastjson.JSON;
import com.rzico.util.SpringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.*;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.beans.factory.support.AbstractBeanDefinition.AUTOWIRE_BY_NAME;
import static org.springframework.beans.factory.support.AbstractBeanDefinition.AUTOWIRE_CONSTRUCTOR;

/**
 * 消息队列创建
 *
 * @author Rzico Boot
 * @version 1.0
 * @date 2020-02-26
 */
@Component
public class RabbitQueueBeanRegister implements InitializingBean,DisposableBean {

    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    QueuePropertiesConfig queuePropertiesConfig;

    @Override
    public void destroy() throws Exception {

    }

    @Override
    public void afterPropertiesSet() throws Exception {
        ApplicationContext applicationContext = SpringUtils.getApplicationContext();
        DefaultListableBeanFactory fty = (DefaultListableBeanFactory) applicationContext.getAutowireCapableBeanFactory();
        //创建消息交换机
        List<Map<String, String>> exchangeMapList = queuePropertiesConfig.getExchangeMapList();
        builderMessageExchange(exchangeMapList, fty);
        //  创建消息队列以及绑定交换机
        List<Map<String, String>> queueMapList = queuePropertiesConfig.getQueueMapList();
        builderMessageQueuesAndBind(queueMapList, fty);

    }


    /**
     * 创建消息交换机
     * @param exchangeMapList
     * @param fty
     */
    public void builderMessageExchange(List<Map<String, String>> exchangeMapList, DefaultListableBeanFactory fty){
        for (int i=0; i<exchangeMapList.size(); i++) {
            Map<String, String> exchangeMap = exchangeMapList.get(i);
            String exchangeName = exchangeMap.get("name");
            if (exchangeName.equals("directExchange")){
                fty.registerBeanDefinition(exchangeName, BeanDefinitionBuilder.genericBeanDefinition(DirectExchange.class, () -> (DirectExchange) ExchangeBuilder
                        .directExchange(exchangeName)
                        .durable(true)
                        .build()).getBeanDefinition());
            }else if (exchangeName.equals("topicExchange")){
                fty.registerBeanDefinition(exchangeName, BeanDefinitionBuilder.genericBeanDefinition(TopicExchange.class, () -> (TopicExchange) ExchangeBuilder
                        .topicExchange(exchangeName)
                        .durable(true)
                        .build()).getBeanDefinition());
            }else {
                fty.registerBeanDefinition(exchangeName, BeanDefinitionBuilder.genericBeanDefinition(FanoutExchange.class, () -> (FanoutExchange) ExchangeBuilder
                        .fanoutExchange(exchangeName)
                        .durable(true)
                        .build()).getBeanDefinition());
            }
            logger.info("[---正在创建第"+(i+1)+"/"+exchangeMapList.size()+"个交换机---]:" + JSON.toJSONString(exchangeName));
        }
    }

    /**
     * 创建消息队列以及绑定交换机
     * @param queueMapList
     * @param fty
     * @return
     */
    public void builderMessageQueuesAndBind(List<Map<String, String>> queueMapList, DefaultListableBeanFactory fty){
        for (int i=0; i<queueMapList.size(); i++) {
            Map<String, String> map = queueMapList.get(i);
            String queueName = map.get("queue");
            String exchangeName = map.get("exchange");
            Queue queue = new Queue(queueName, true);

            BeanDefinitionBuilder queueBuilder = BeanDefinitionBuilder.genericBeanDefinition(Queue.class);
            queueBuilder.addConstructorArgValue(queue.getName());
            queueBuilder.addConstructorArgValue(true);
            queueBuilder.setAutowireMode(AUTOWIRE_BY_NAME);
            fty.registerBeanDefinition(queue.getName(), queueBuilder.getBeanDefinition());
            logger.info("[---正在创建第"+(i+1)+"/"+queueMapList.size()+"个队列---]:" + queueName);

            String routingKey = map.get("routingkey");
            BeanDefinitionBuilder bindBuilder = BeanDefinitionBuilder.genericBeanDefinition(Binding.class);
            bindBuilder.addConstructorArgValue(queueName);
            bindBuilder.addConstructorArgValue(Binding.DestinationType.QUEUE);
            bindBuilder.addConstructorArgValue(exchangeName);
            bindBuilder.addConstructorArgValue(routingKey);
            Map<String, Object> arguments = new HashMap<>(1 << 4);
            arguments.put("x-dead-letter-exchange", exchangeName);
            arguments.put("x-dead-letter-routing-key", routingKey);
            bindBuilder.addConstructorArgValue(arguments);
            bindBuilder.setAutowireMode(AUTOWIRE_CONSTRUCTOR);
            fty.registerBeanDefinition(routingKey, bindBuilder.getBeanDefinition());
            logger.info("[---正在绑定第"+(i+1)+"/"+queueMapList.size()+"个消息队列到交换机---]:" + exchangeName+"-"+queueName+"-"+routingKey);
        }
    }
}
