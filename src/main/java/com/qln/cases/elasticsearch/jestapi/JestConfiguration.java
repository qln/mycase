package com.qln.cases.elasticsearch.jestapi;

import io.searchbox.client.JestClient;
import io.searchbox.client.JestClientFactory;
import io.searchbox.client.config.HttpClientConfig;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JestConfiguration {

    @Bean
    public JestClientFactory getJestClientFactory() {
        JestClientFactory factory = new JestClientFactory();
        factory.setHttpClientConfig(getHttpClientConfig());
        return factory;
    }

    @Bean
    public HttpClientConfig getHttpClientConfig() {
        HttpClientConfig config = new HttpClientConfig.Builder("http://192.168.11.219:9200").multiThreaded(true).defaultMaxTotalConnectionPerRoute(2).maxTotalConnection(10).build();
        return config;
    }

    @Bean
    public JestClient getJestClient() {
        return getJestClientFactory().getObject();
    }

}
