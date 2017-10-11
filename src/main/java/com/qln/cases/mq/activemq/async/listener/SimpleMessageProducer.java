package com.qln.cases.mq.activemq.async.listener;

import java.util.Date;

import javax.jms.JMSException;
import javax.jms.Message;
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
import com.qln.cases.common.util.DateUtil.DATE_PATTERN;

//@Component
//@EnableScheduling
public class SimpleMessageProducer {

    private static final Logger LOG = LoggerFactory.getLogger(SimpleMessageProducer.class);

    @Autowired
    protected JmsTemplate jmsTemplate;

    protected int numberOfMessages = 100;

    public void setNumberOfMessages(int numberOfMessages) {
        this.numberOfMessages = numberOfMessages;
    }

    @Scheduled(fixedRate = 5000)
    public void sendMessages() throws JMSException {

        for (int i = 0; i < numberOfMessages; ++i) {
            final StringBuilder buffer = new StringBuilder();
            buffer.append("Message '").append(i).append("' sent at: ").append(DateUtil.format(new Date(), DATE_PATTERN.YYYY_MM_DD_HH_MM_SS));

            final int count = i;
            final String payload = buffer.toString();

            jmsTemplate.send(new MessageCreator() {
                public Message createMessage(Session session) throws JMSException {
                    TextMessage message = session.createTextMessage(payload);
                    message.setIntProperty("messageCount", count);
                    LOG.info("Sending message number '{}'", count);
                    return message;
                }
            });
        }
    }
}
