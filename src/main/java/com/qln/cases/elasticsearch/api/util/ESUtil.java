package com.qln.cases.elasticsearch.api.util;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

/**
 * 工具类获取 TransportClient 实例
 * 
 * @author zxn
 */
public class ESUtil {
    public static TransportClient client = null;

    public static TransportClient init() {
        Settings settings = null;
        try {
            settings = Settings.builder().put("cluster.name", "qln-es").put("client.transport.sniff", true).build();
            client = new PreBuiltTransportClient(settings);
            client.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("192.168.220.130"), 9300));
        } catch (UnknownHostException e) {
            e.printStackTrace();
            client = null;
        }

        return client;
    }

    public static TransportClient getTransportClient() {
        if (client == null) {
            client = init();
        }
        return client;
    }

    public static void destory() {
        if (client != null) {
            client.close();
        }
    }
}
