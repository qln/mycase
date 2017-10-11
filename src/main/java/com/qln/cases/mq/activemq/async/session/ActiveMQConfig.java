package com.qln.cases.mq.activemq.async.session;

import javax.jms.Queue;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQQueue;
import org.apache.activemq.pool.PooledConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.listener.DefaultMessageListenerContainer;

import com.qln.cases.common.util.PropertiesUtil;

//@EnableJms
//@Configuration
public class ActiveMQConfig {

    private final static PropertiesUtil properties = PropertiesUtil.getByConfType("application");

    private static String BROKER_URL = properties.getProperty("activemq.broker-url");
    private static String DESTINATION_QUEUE_NAME = properties.getProperty("activemq.destination.name");
    private static String REPLYTODESTINATION_QUEUE_NAME = properties.getProperty("activemq.replyToDestination.name");
    private static String ACTIVEMQ_CONCURRENCY = properties.getProperty("activemq.concurrency");

    @Bean(name = "destination")
    public Queue destination() {
        return new ActiveMQQueue(DESTINATION_QUEUE_NAME);
    }

    @Bean(name = "replyToDestination")
    public Queue replyToDestination() {
        return new ActiveMQQueue(REPLYTODESTINATION_QUEUE_NAME);
    }

    @Bean
    public ActiveMQConnectionFactory activeMQConnectionFactory() {
        return new ActiveMQConnectionFactory(BROKER_URL);
    }

    @Bean(name = "connectionFactory")
    public PooledConnectionFactory connectionFactory() {
        PooledConnectionFactory jmsFactory = new PooledConnectionFactory();
        jmsFactory.setConnectionFactory(activeMQConnectionFactory());
        return jmsFactory;
    }

    @Bean
    public JmsTemplate JmsTemplate() {
        JmsTemplate jmsTemplate = new JmsTemplate();
        jmsTemplate.setConnectionFactory(connectionFactory());
        jmsTemplate.setDefaultDestination(destination());
        return jmsTemplate;
    }

    @Bean(name = "clc")
    public DefaultMessageListenerContainer destinationMessageListenerContainer() {
        DefaultMessageListenerContainer container = new DefaultMessageListenerContainer();
        container.setConnectionFactory(connectionFactory());
        container.setMessageListener(new MySessionAwareMessageListener());
        container.setDestination(destination());
        container.setConcurrency(ACTIVEMQ_CONCURRENCY);
        return container;
    }

    @Bean(name = "plc")
    public DefaultMessageListenerContainer replyMessageListenerContainer() {
        DefaultMessageListenerContainer container = new DefaultMessageListenerContainer();
        container.setConnectionFactory(connectionFactory());
        container.setMessageListener(new SimpleMessageListener());
        container.setDestination(replyToDestination());
        container.setConcurrency(ACTIVEMQ_CONCURRENCY);
        return container;
    }

}
