package com.bins.common.redis.service.impl;

import com.bins.common.redis.service.RedisString;
import com.bins.common.redis.utils.JedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * Created by songrongbin on 2017/3/8.
 */
@Service("redisString")
public class RedisStringImpl implements RedisString {

    @Autowired
    private JedisUtil jedisUtil;

    public String get(String module, String business, String key) {
        if (StringUtils.isEmpty(module)) {
            return "";
        }
        if (StringUtils.isEmpty(business)) {
            return "";
        }
        if (StringUtils.isEmpty(key)) {
            return "";
        }
        return jedisUtil.getJedis().get(module + ":" + business + ":" + key);
    }

    public String set(String module, String business, String key, String value) {
        if (StringUtils.isEmpty(module)) {
            return "";
        }
        if (StringUtils.isEmpty(business)) {
            return "";
        }
        if (StringUtils.isEmpty(key)) {
            return "";
        }
        return jedisUtil.getJedis().set(module + ":" + business + ":" + key, value);
    }
}
