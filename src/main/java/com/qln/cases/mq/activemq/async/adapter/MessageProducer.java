package com.qln.cases.mq.activemq.async.adapter;

import java.util.Date;

import javax.jms.BytesMessage;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.qln.cases.common.util.DateUtil;
import com.qln.cases.common.util.PropertiesUtil;
import com.qln.cases.common.util.DateUtil.DATE_PATTERN;

@Component
@EnableScheduling
public class MessageProducer {

    private static final Logger LOG = LoggerFactory.getLogger(MessageProducer.class);

    @Autowired
    protected JmsTemplate jmsTemplate;

    protected int numberOfMessages = 100;

    public void setNumberOfMessages(int numberOfMessages) {
        this.numberOfMessages = numberOfMessages;
    }

    public void sendMessages(String destinationName, String messageType) throws JMSException {
        if ("text".equalsIgnoreCase(messageType)) {
            sendTextMessages(destinationName);
        } else if ("bytes".equalsIgnoreCase(messageType)) {
            sendBytesMessages(destinationName);
        } else if ("map".equalsIgnoreCase(messageType)) {
            sendMapMessages(destinationName);
        } else if ("object".equalsIgnoreCase(messageType)) {
            sendObjectMessages(destinationName);
        }
    }

    public void sendTextMessages(String destinationName) throws JMSException {

        for (int i = 0; i < numberOfMessages; ++i) {
            final StringBuilder buffer = new StringBuilder();
            buffer.append("Message '").append(i).append("' sent at: ").append(DateUtil.format(new Date(), DATE_PATTERN.YYYY_MM_DD_HH_MM_SS));

            final int count = i;
            final String payload = buffer.toString();

            jmsTemplate.send(destinationName, new MessageCreator() {
                public Message createMessage(Session session) throws JMSException {
                    TextMessage message = session.createTextMessage(payload);
                    message.setIntProperty("messageCount", count);
                    LOG.info("Sending text message number '{}'", count);
                    return message;
                }
            });
        }
    }

    public void sendBytesMessages(String destinationName) throws JMSException {

        for (int i = 0; i < numberOfMessages; ++i) {
            final StringBuilder buffer = new StringBuilder();
            buffer.append("Message '").append(i).append("' sent at: ").append(DateUtil.format(new Date(), DATE_PATTERN.YYYY_MM_DD_HH_MM_SS));

            final int count = i;
            final String payload = buffer.toString();

            jmsTemplate.send(destinationName, new MessageCreator() {
                public Message createMessage(Session session) throws JMSException {
                    BytesMessage message = session.createBytesMessage();
                    message.writeUTF(payload);
                    message.setIntProperty("messageCount", count);
                    LOG.info("Sending bytes message number '{}'", count);
                    return message;
                }
            });
        }
    }

    public void sendMapMessages(String destinationName) throws JMSException {

        for (int i = 0; i < numberOfMessages; ++i) {
            final StringBuilder buffer = new StringBuilder();
            buffer.append("Message '").append(i).append("' sent at: ").append(DateUtil.format(new Date(), DATE_PATTERN.YYYY_MM_DD_HH_MM_SS));

            final int count = i;
            final String payload = buffer.toString();

            jmsTemplate.send(destinationName, new MessageCreator() {
                public Message createMessage(Session session) throws JMSException {
                    MapMessage message = session.createMapMessage();
                    message.setString("payload", payload);
                    message.setIntProperty("messageCount", count);
                    LOG.info("Sending map message number '{}'", count);
                    return message;
                }
            });
        }
    }

    public void sendObjectMessages(String destinationName) throws JMSException {
        final StringBuilder buffer = new StringBuilder();

        for (int i = 0; i < numberOfMessages; ++i) {
            buffer.append("Message '").append(i).append("' sent at: ").append(DateUtil.format(new Date(), DATE_PATTERN.YYYY_MM_DD_HH_MM_SS));

            final int count = i;
            final String payloadStr = buffer.toString();
            final Payload payload = new Payload(payloadStr);

            jmsTemplate.send(destinationName, new MessageCreator() {
                public Message createMessage(Session session) throws JMSException {
                    ObjectMessage message = session.createObjectMessage(payload);
                    message.setIntProperty("messageCount", count);
                    LOG.info("Sending object message number '{}'", count);
                    return message;
                }
            });
        }
    }

    @Scheduled(fixedRate = 50000)
    public void exceute() throws JMSException {
        String destinationName = PropertiesUtil.getByConfType("application").getProperty("activemq.foo.test2");
        sendMessages(destinationName, "text");
    }

}
