package com.bins.common.redis.test;

import com.bins.common.redis.lock.DistributedLock;
import com.bins.common.redis.service.RedisString;
import com.bins.common.redis.utils.JedisUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import redis.clients.jedis.JedisPool;

import java.security.Timestamp;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by songrongbin on 2017/3/8.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
        "/application-context.xml"})
public class RedisStringTest {
    @Autowired
    private RedisString redisString;

    @Autowired
    private JedisUtil jedisUtil;

    @Autowired
    private JedisPool jedisPool;

    @Test
    public void testGetKey() {

        redisString.set("trade", "order", "123", "5000");
    }

    @Test
    public void testDistributedLock() throws InterruptedException {

        ThreadPoolExecutor executor = new ThreadPoolExecutor(100, 100, 200, TimeUnit.MILLISECONDS,
                new ArrayBlockingQueue<Runnable>(100));

        for (int i = 0; i < 50; i++) {
            MyTask myTask = new MyTask(i);
            executor.execute(myTask);
            System.out.println("线程池中线程数目：" + executor.getPoolSize() + "，队列中等待执行的任务数目：" +
                    executor.getQueue().size() + "，已执行玩别的任务数目：" + executor.getCompletedTaskCount());
        }
        executor.shutdown();

        CountDownLatch countDownLatch = new CountDownLatch(1);
        countDownLatch.await();
    }

    class MyTask implements Runnable {
        private int taskNum;

        final String key = "lock:order:10000";

        public MyTask(int num) {
            this.taskNum = num;
        }

        public void run() {
            DistributedLock lock = new DistributedLock(key, 10000, 20000);
            System.out.println("正在执行task " + taskNum + ", currentTime " + System.currentTimeMillis() + "执行开始");
            lock.setJedis(jedisPool.getResource());
            try {
                if (lock.lock()) {
                    System.out.println("正在执行task " + taskNum + ", currentTime " + System.currentTimeMillis() + "get distributed lcok");
                    Thread.sleep(1000);
                }
                System.out.println("task " + taskNum + ", currentTime " + System.currentTimeMillis() + "执行完毕");
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                System.out.println("task " + taskNum + ", currentTime " + System.currentTimeMillis() + "finally执行完毕");
                //为了让分布式锁的算法更稳键些，持有锁的客户端在解锁之前应该再检查一次自己的锁是否已经超时，再去做DEL操作，因为可能客户端因为某个耗时的操作而挂起，
                //操作完的时候锁因为超时已经被别人获得，这时就不必解锁了。 ————这里没有做
                lock.unlock();
            }

        }
    }


}
