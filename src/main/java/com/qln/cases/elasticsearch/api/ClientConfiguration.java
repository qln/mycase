package com.qln.cases.elasticsearch.api;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

public class ClientConfiguration {

    public Client getTransportClient() {
        TransportClient client = null;
        try {
            client = new PreBuiltTransportClient(Settings.EMPTY).addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("192.168.220.130"), 9200));
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return client;
    }

}
