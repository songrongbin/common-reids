package com.bins.common.redis.test;

import com.bins.common.redis.service.RedisString;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by songrongbin on 2017/3/8.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
        "/application-context.xml" })
public class RedisStringTest {
        @Autowired
        private RedisString redisString;

        @Test
        public void testGetKey() {
                redisString.set("trade", "order", "123", "5000");
        }


}
