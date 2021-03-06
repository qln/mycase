package com.qln.cases.mq.activemq.async.listener;

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

//@Configuration
//@EnableJms
public class ActiveMQConfig {

    private final static PropertiesUtil properties = PropertiesUtil.getByConfType("application");

    private static String BROKER_URL = properties.getProperty("activemq.broker-url");
    private static String DESTINATION_QUEUE_NAME = properties.getProperty("activemq.destination.name");
    private static String ACTIVEMQ_CONCURRENCY = properties.getProperty("activemq.concurrency");

    @Bean
    public Queue queue() {
        return new ActiveMQQueue(DESTINATION_QUEUE_NAME);
    }

    @Bean
    public ActiveMQConnectionFactory activeMQConnectionFactory() {
        return new ActiveMQConnectionFactory(BROKER_URL);
    }

    @Bean(name = "connectionFactory")
    public PooledConnectionFactory connectionFactory() {
        PooledConnectionFactory pool = new PooledConnectionFactory();
        pool.setConnectionFactory(activeMQConnectionFactory());
        return pool;
    }

    @Bean
    public JmsTemplate jmsTemplate() {
        JmsTemplate jmsTemplate = new JmsTemplate();
        jmsTemplate.setConnectionFactory(connectionFactory());
        jmsTemplate.setDefaultDestination(queue());
        return jmsTemplate;
    }

    @Bean
    public DefaultMessageListenerContainer container() {
        DefaultMessageListenerContainer container = new DefaultMessageListenerContainer();
        container.setConnectionFactory(connectionFactory());
        container.setMessageListener(new SimpleMessageListener());
        container.setConcurrency(ACTIVEMQ_CONCURRENCY);
        container.setDestination(queue());
        return container;
    }
}
