package com.qln.cases.redis;

import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = JedisConfigration.class)
public class RedisTest {

    private final static Logger logger = LoggerFactory.getLogger(RedisTest.class);

    @Autowired
    ShardedJedisPool shardedJedisPool;

    @Autowired
    JedisCluster jedisCluster;

    @Test
    public void redisTest() {
        String key = "foo";
        String foo = jedisCluster.get(key);
        logger.info("foo:{}", foo);

        Map<String, JedisPool> map = jedisCluster.getClusterNodes();
        for (String k : map.keySet()) {
            System.out.println("----->" + k);
        }

        JedisPool jedisPool = map.get("10.13.11.182:7002");

        Jedis jedis = jedisPool.getResource();
        String info = jedis.info();

        logger.info("info:{}", info);

        info = jedis.clusterInfo();

        logger.info("-----------------------------------------------------------------------");

        logger.info("info:{}", info);
    }

    @Test
    public void shardedJedisTest() {
        ShardedJedis shardedJedis = shardedJedisPool.getResource();
        String key = "foo";
        String value = "";
        key = "redis_test_001";
        value = "test";
        shardedJedis.del(key);
        shardedJedis.set(key, value);
        value = shardedJedis.get(key);
        shardedJedis.close();
        logger.info("key:{},value:{}", key, value);
        logger.info("-----------------------------------------------------------------------");

        shardedJedis = shardedJedisPool.getResource();
        key = "apple_test_001";
        value = "mac_pro";
        shardedJedis.del(key);
        shardedJedis.set(key, value);
        value = shardedJedis.get(key);
        shardedJedis.close();
        logger.info("key:{},value:{}", key, value);
        logger.info("-----------------------------------------------------------------------");
        
    }
}
