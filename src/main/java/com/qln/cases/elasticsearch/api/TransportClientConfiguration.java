package com.qln.cases.elasticsearch.api;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 注入TransportClient
 * 
 * @author zxn
 */
@Configuration
public class TransportClientConfiguration {

    @Bean(name = "transportClient")
    public TransportClient getTransportClient() {
        TransportClient client = null;
        Settings settings = null;
        try {
            settings = Settings.builder().put("cluster.name", "qln-es").put("client.transport.sniff", true).build();
            client = new PreBuiltTransportClient(settings).addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("192.168.220.130"), 9300));
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return client;
    }
}
