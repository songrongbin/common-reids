package com.bins.common.redis.service;

/**
 * Created by songrongbin on 2017/3/8.
 */
public interface RedisString {
    public String set(String module, String business, String key, String value);

    public String get(String module, String business, String key);
}
