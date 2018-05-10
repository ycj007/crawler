package mtime.ml.crawler.service;

import mtime.lark.net.rpc.RpcApplication;
import mtime.lark.net.rpc.config.ServerOptions;
import mtime.lark.util.config.AppConfig;
import mtime.lark.util.redis.RedisClient;
import mtime.ml.crawler.common.scheduler.RedisClientPriorityScheduler;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.util.StringUtils;

@SpringBootApplication(exclude = {MongoAutoConfiguration.class, MongoDataAutoConfiguration.class})
public class Bootstrap {
    public static void main(String[] args) {
        // default options
        ServerOptions options = new ServerOptions(":9090", "爬虫服务");
        RpcApplication app = new RpcApplication(Bootstrap.class, args, options);
        app.run();
    }


    @Bean
    RedisClientPriorityScheduler redisClientPriorityScheduler() {

        RedisClientPriorityScheduler redisPriorityScheduler = new RedisClientPriorityScheduler(redisClient());
        return redisPriorityScheduler;
    }

    @Bean
    RedisClient redisClient() {
        String redisConfig =
                AppConfig.getDefault()
                         .getCustom()
                         .getString("mtime.ml.crawler.redis.name");
        RedisClient client = StringUtils.isEmpty(redisConfig) ?
                new RedisClient("SvsRedisBiz") : new RedisClient(redisConfig);

        return client;
    }


}
