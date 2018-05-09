package mtime.ml.crawler.service.impl;

import mtime.lark.net.rpc.annotation.RpcService;
import mtime.ml.crawler.service.dto.InvokeCrawlerDto;
import mtime.ml.crawler.service.iface.InvokeCrawlerService;
import mtime.ml.crawler.service.pipeline.GexingsDaoPipeline;
import mtime.ml.crawler.service.processor.GexingsProcessor;
import mtime.ml.crawler.service.scheduler.RedisClientPriorityScheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Spider;

@Component
@RpcService(name = "InvokeCrawlerService", description = "爬虫服务")
public class InvokeCrawlerServiceImpl implements InvokeCrawlerService {


    @Autowired
    private RedisClientPriorityScheduler redisClientPriorityScheduler;
    @Autowired
    private GexingsDaoPipeline gexingsDaoPipeline;

    @Override
    public InvokeCrawlerDto.InvokeCrawlerResponse invokeCrawler() {
        Spider spider = Spider.create(new GexingsProcessor())
                              .addUrl("http://www.gexings.com")
                              .thread(Runtime.getRuntime()
                                             .availableProcessors())
                              .setScheduler(redisClientPriorityScheduler)
                              .addPipeline(gexingsDaoPipeline);
        spider.run();
        InvokeCrawlerDto.InvokeCrawlerResponse invokeCrawlerResponse = new InvokeCrawlerDto.InvokeCrawlerResponse();
        invokeCrawlerResponse.setResult(true);
        return invokeCrawlerResponse;
    }
}
