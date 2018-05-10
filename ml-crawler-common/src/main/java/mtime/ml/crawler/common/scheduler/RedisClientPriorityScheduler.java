package mtime.ml.crawler.common.scheduler;

import com.alibaba.fastjson.JSON;
import mtime.lark.util.redis.RedisClient;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import redis.clients.jedis.ShardedJedis;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Task;

import java.util.Set;


public class RedisClientPriorityScheduler extends RedisClientScheduler {

    private static final String ZSET_PREFIX = "zset_";

    private static final String QUEUE_PREFIX = "queue_";

    private static final String NO_PRIORITY_SUFFIX = "_zore";

    private static final String PLUS_PRIORITY_SUFFIX = "_plus";

    private static final String MINUS_PRIORITY_SUFFIX = "_minus";


    public RedisClientPriorityScheduler(RedisClient redisClient) {
        super(redisClient);
    }

    @Override
    protected void pushWhenNoDuplicate(Request request, Task task) {


        redisClient.invoke(jedis -> {
            if (request.getPriority() > 0) {
                jedis.zadd(getZsetPlusPriorityKey(task), request.getPriority(), request.getUrl());
            } else if (request.getPriority() < 0) {
                jedis.zadd(getZsetMinusPriorityKey(task), request.getPriority(), request.getUrl());
            } else {
                jedis.lpush(getQueueNoPriorityKey(task), request.getUrl());
            }

            setExtrasInItem(jedis, request, task);
            return null;
        });
    }

    @Override
    public synchronized Request poll(Task task) {


        return redisClient.invoke(jedis -> {
            String url = getRequest(jedis, task);
            if (StringUtils.isBlank(url)) {
                return null;
            }
            return getExtrasInItem(jedis, url, task);
        });
    }

    private String getRequest(ShardedJedis jedis, Task task) {
        String url;
        Set<String> urls = jedis.zrevrange(getZsetPlusPriorityKey(task), 0, 0);
        if (urls.isEmpty()) {
            url = jedis.lpop(getQueueNoPriorityKey(task));
            if (StringUtils.isBlank(url)) {
                urls = jedis.zrevrange(getZsetMinusPriorityKey(task), 0, 0);
                if (!urls.isEmpty()) {
                    url = urls.toArray(new String[0])[0];
                    jedis.zrem(getZsetMinusPriorityKey(task), url);
                }
            }
        } else {
            url = urls.toArray(new String[0])[0];
            jedis.zrem(getZsetPlusPriorityKey(task), url);
        }
        return url;
    }

    @Override
    public void resetDuplicateCheck(Task task) {

        redisClient.invoke(jedis -> jedis.del(getSetKey(task)));
    }

    private String getZsetPlusPriorityKey(Task task) {
        return ZSET_PREFIX + task.getUUID() + PLUS_PRIORITY_SUFFIX;
    }

    private String getQueueNoPriorityKey(Task task) {
        return QUEUE_PREFIX + task.getUUID() + NO_PRIORITY_SUFFIX;
    }

    private String getZsetMinusPriorityKey(Task task) {
        return ZSET_PREFIX + task.getUUID() + MINUS_PRIORITY_SUFFIX;
    }

    private void setExtrasInItem(ShardedJedis jedis, Request request, Task task) {
        if (request.getExtras() != null) {
            String field = DigestUtils.shaHex(request.getUrl());
            String value = JSON.toJSONString(request);
            jedis.hset(getItemKey(task), field, value);
        }
    }

    private Request getExtrasInItem(ShardedJedis jedis, String url, Task task) {
        String key = getItemKey(task);
        String field = DigestUtils.shaHex(url);
        byte[] bytes = jedis.hget(key.getBytes(), field.getBytes());
        if (bytes != null) {
            return JSON.parseObject(new String(bytes), Request.class);
        }
        return new Request(url);
    }
}
