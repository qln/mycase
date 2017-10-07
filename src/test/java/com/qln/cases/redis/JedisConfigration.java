package com.qln.cases.redis;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedisPool;

@Configuration
public class JedisConfigration {

    /**
     * 连接池配置
     * @return
     */
    @Bean
    public JedisPoolConfig getJedisPoolConfig() {
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxIdle(50);
        poolConfig.setMinIdle(20);
        poolConfig.setMaxTotal(200);
        poolConfig.setTestOnBorrow(true);
        poolConfig.setMaxWaitMillis(60 * 1000);
        return poolConfig;
    }

    /**
     * 单独的redis实例
     * @return
     */
    @Bean(name = "jedisShardInfo1")
    public JedisShardInfo getJedisShardInfo1() {
        JedisShardInfo shardInfo = new JedisShardInfo("10.13.11.182", 6379);
        shardInfo.setPassword("zxn@0302");
        return shardInfo;
    }

    /**
     * 单独的redis实例
     * @return
     */
    @Bean(name = "jedisShardInfo1")
    public JedisShardInfo getJedisShardInfo2() {
        JedisShardInfo shardInfo = new JedisShardInfo("10.13.11.183", 6379);
        shardInfo.setPassword("zang123456");
        return shardInfo;
    }

    /**
     * redis 集群
     * @return
     */
    @Bean
    public ShardedJedisPool getShardedJedisPool() {
        List<JedisShardInfo> shards = new ArrayList<JedisShardInfo>();

        //各个节点之间没有任何关系
        shards.add(getJedisShardInfo1());
        shards.add(getJedisShardInfo2());

        ShardedJedisPool shardedJedisPool = new ShardedJedisPool(getJedisPoolConfig(), shards);
        System.out.println("*******spring boot********");
        return shardedJedisPool;
    }

    /**
     * redis 集群设置,以集群模式连接
     * @return
     */
    @Bean
    public JedisCluster getJedisCluster() {
        Set<HostAndPort> jedisClusterNodes = new HashSet<HostAndPort>();
        //Jedis Cluster will attempt to discover cluster nodes automatically
        jedisClusterNodes.add(new HostAndPort("10.13.11.182", 7000));
        JedisCluster jc = new JedisCluster(jedisClusterNodes, getJedisPoolConfig());
        return jc;
    }
}
