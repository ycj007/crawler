package mtime.ml.crawler.common.scheduler;

import com.alibaba.fastjson.JSON;
import mtime.lark.util.redis.RedisClient;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.scheduler.DuplicateRemovedScheduler;
import us.codecraft.webmagic.scheduler.MonitorableScheduler;
import us.codecraft.webmagic.scheduler.component.DuplicateRemover;


public class RedisClientScheduler extends DuplicateRemovedScheduler implements MonitorableScheduler, DuplicateRemover {

    protected RedisClient redisClient;

    private static final String QUEUE_PREFIX = "queue_";

    private static final String SET_PREFIX = "set_";

    private static final String ITEM_PREFIX = "item_";

    public RedisClientScheduler(RedisClient redisClient) {
        this.redisClient = redisClient;
        setDuplicateRemover(this);
    }

    @Override
    public void resetDuplicateCheck(Task task) {
        redisClient.invoke(jedis -> jedis.del(getSetKey(task)));

    }

    @Override
    public boolean isDuplicate(Request request, Task task) {

        return redisClient.invoke(jedis -> jedis.sadd(getSetKey(task), request.getUrl()) == 0);

    }

    @Override
    protected void pushWhenNoDuplicate(Request request, Task task) {
        redisClient.invoke(jedis -> {
            jedis.rpush(getQueueKey(task), request.getUrl());
            if (checkForAdditionalInfo(request)) {
                String field = DigestUtils.shaHex(request.getUrl());
                String value = JSON.toJSONString(request);
                jedis.hset((ITEM_PREFIX + task.getUUID()), field, value);
            }
            return null;
        });
    }

    private boolean checkForAdditionalInfo(Request request) {
        if (request == null) {
            return false;
        }

        if (!request.getHeaders()
                    .isEmpty() || !request.getCookies()
                                          .isEmpty()) {
            return true;
        }

        if (StringUtils.isNotBlank(request.getCharset()) || StringUtils.isNotBlank(request.getMethod())) {
            return true;
        }

        if (request.isBinaryContent() || request.getRequestBody() != null) {
            return true;
        }

        if (request.getExtras() != null && !request.getExtras()
                                                   .isEmpty()) {
            return true;
        }
        if (request.getPriority() != 0L) {
            return true;
        }

        return false;
    }

    @Override
    public synchronized Request poll(Task task) {


        return redisClient.invoke(jedis -> {
            String url = jedis.lpop(getQueueKey(task));
            if (url == null) {
                return null;
            }
            String key = ITEM_PREFIX + task.getUUID();
            String field = DigestUtils.shaHex(url);
            byte[] bytes = jedis.hget(key.getBytes(), field.getBytes());
            if (bytes != null) {
                Request o = JSON.parseObject(new String(bytes), Request.class);
                return o;
            }
            Request request = new Request(url);
            return request;
        });
    }

    protected String getSetKey(Task task) {
        return SET_PREFIX + task.getUUID();
    }

    protected String getQueueKey(Task task) {
        return QUEUE_PREFIX + task.getUUID();
    }

    protected String getItemKey(Task task) {
        return ITEM_PREFIX + task.getUUID();
    }

    @Override
    public int getLeftRequestsCount(Task task) {

        return redisClient.invoke(jedis -> {
            Long size = jedis.llen(getQueueKey(task));
            return size.intValue();
        });
    }

    @Override
    public int getTotalRequestsCount(Task task) {
        return redisClient.invoke(jedis -> {
            Long size = jedis.scard(getSetKey(task));
            return size.intValue();
        });
    }
}
