package com.qln.cases.mq.activemq.async.adapter;

import javax.jms.Queue;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQQueue;
import org.apache.activemq.pool.PooledConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.listener.DefaultMessageListenerContainer;
import org.springframework.jms.listener.adapter.MessageListenerAdapter;
import org.springframework.stereotype.Component;

import com.qln.cases.common.util.PropertiesUtil;

@Component
@EnableJms
public class ActiveMQConfig {
    private final static PropertiesUtil properties = PropertiesUtil.getByConfType("application");

    private static String BROKER_URL = properties.getProperty("activemq.broker-url");
    private static String FOO_TEST = properties.getProperty("activemq.foo.test");
    private static String FOO_TEST2 = properties.getProperty("activemq.foo.test2");
    private static String ACTIVEMQ_CONCURRENCY = properties.getProperty("activemq.concurrency");

    @Bean("footest")
    public Queue footest() {
        return new ActiveMQQueue(FOO_TEST);
    }

    @Bean("footest2")
    public Queue footest2() {
        return new ActiveMQQueue(FOO_TEST2);
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
        jmsTemplate.setDefaultDestination(footest());
        return jmsTemplate;
    }

    @Bean("messageDelegate")
    public MessageListenerAdapter adapter1() {
        MessageListenerAdapter adapter = new MessageListenerAdapter(new MessageDelegateImpl());
        adapter.setDefaultListenerMethod("processMessage");
        adapter.setMessageConverter(null);
        return adapter;
    }

    @Bean("messageDelegate2")
    public MessageListenerAdapter adapter2() {
        MessageListenerAdapter adapter = new MessageListenerAdapter(new MessageDelegate2Impl());
        adapter.setDefaultListenerMethod("processMessage");
        return adapter;
    }

    @Bean("containr1")
    public DefaultMessageListenerContainer containr1() {
        DefaultMessageListenerContainer container = new DefaultMessageListenerContainer();
        container.setConnectionFactory(connectionFactory());
        container.setMessageListener(adapter1());
        container.setDestination(footest());
        container.setConcurrency(ACTIVEMQ_CONCURRENCY);
        return container;
    }

    @Bean("containr2")
    public DefaultMessageListenerContainer container2() {
        DefaultMessageListenerContainer container = new DefaultMessageListenerContainer();
        container.setConnectionFactory(connectionFactory());
        container.setMessageListener(adapter2());
        container.setDestination(footest2());
        container.setConcurrency(ACTIVEMQ_CONCURRENCY);
        return container;
    }

}
