package mtime.ml.crawler.service;

import mtime.lark.net.rpc.RpcApplication;
import mtime.lark.net.rpc.config.ServerOptions;
import mtime.ml.crawler.service.pipeline.GexingsDaoPipeline;
import mtime.ml.crawler.service.processor.GexingsProcessor;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.context.annotation.Bean;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.ConsolePipeline;
import us.codecraft.webmagic.scheduler.RedisPriorityScheduler;

import java.io.IOException;

@SpringBootApplication(exclude = {MongoAutoConfiguration.class, MongoDataAutoConfiguration.class})
public class Bootstrap {
    public static void main(String[] args) {
        // default options
        ServerOptions options = new ServerOptions(":9090", "爬虫服务");
        RpcApplication app = new RpcApplication(Bootstrap.class, args, options);
        app.run();
    }


    @Bean(destroyMethod = "close")
    Spider spider()  {
        Spider spider = Spider.create(new GexingsProcessor())
                              .addUrl("http://www.gexings.com")
                              .thread(Runtime.getRuntime()
                                             .availableProcessors())
                              .setScheduler(redisPriorityScheduler())

                              .addPipeline(gexingsDaoPipeline());
        spider.run();
        return spider;
    }


    @Bean
    RedisPriorityScheduler redisPriorityScheduler() {

        RedisPriorityScheduler redisPriorityScheduler = new RedisPriorityScheduler("127.0.0.1");
        return redisPriorityScheduler;
    }

    @Bean
    GexingsDaoPipeline gexingsDaoPipeline() {


        return new GexingsDaoPipeline();
    }


}
