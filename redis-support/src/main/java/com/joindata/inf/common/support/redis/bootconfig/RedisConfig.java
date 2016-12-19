package com.joindata.inf.common.support.redis.bootconfig;

import java.util.Set;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.joindata.inf.common.support.redis.component.RedisClient;
import com.joindata.inf.common.support.redis.support.RedisProperties;
import com.joindata.inf.common.util.basic.CollectionUtil;
import com.joindata.inf.common.util.basic.StringUtil;
import com.joindata.inf.common.util.log.Logger;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;

@Configuration
public class RedisConfig
{
    private static final Logger log = Logger.get();

    @Autowired
    private RedisProperties properties;

    @Bean
    public RedisClient redisClient()
    {
        GenericObjectPoolConfig config = new GenericObjectPoolConfig();
        config.setMaxTotal(properties.getMaxActive());
        config.setMaxIdle(properties.getMaxIdle());
        config.setMaxWaitMillis(properties.getMaxWait());
        config.setTestOnBorrow(properties.isTestOnBorrow());
        config.setTestOnReturn(properties.isTestOnReturn());

        String[] hosts = StringUtil.splitToArray(properties.getHost(), ",");

        Set<HostAndPort> hostSet = CollectionUtil.newHashSet();
        for(String host: hosts)
        {
            String[] pair = host.split(":");
            HostAndPort hostAndPort = new HostAndPort(pair[0], Integer.parseInt(pair[1]));
            hostSet.add(hostAndPort);
        }

        JedisCluster jedisCluster = new JedisCluster(hostSet, config);

        log.info("Redis 连接地址: {}, 最大活动连接数: {}, 最大空闲数: {}", properties.getHost(), properties.getMaxActive(), properties.getMaxIdle());

        return new RedisClient(jedisCluster);
    }
}