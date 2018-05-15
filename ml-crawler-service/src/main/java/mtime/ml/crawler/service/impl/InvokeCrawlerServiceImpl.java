package mtime.ml.crawler.service.impl;

import mtime.lark.net.rpc.annotation.RpcService;
import mtime.ml.crawler.common.pipeline.dao.GexingsDaoPipeline;
import mtime.ml.crawler.common.scheduler.RedisClientPriorityScheduler;
import mtime.ml.crawler.service.dao.MlGexingsDao;
import mtime.ml.crawler.service.dto.InvokeCrawlerDto;
import mtime.ml.crawler.service.entity.MlGexings;
import mtime.ml.crawler.service.iface.InvokeCrawlerService;
import mtime.ml.crawler.service.processor.GexingsProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Spider;

@Component
@RpcService(name = "InvokeCrawlerService", description = "爬虫服务")
public class InvokeCrawlerServiceImpl implements InvokeCrawlerService {


    @Autowired
    private RedisClientPriorityScheduler redisClientPriorityScheduler;
    @Autowired
    private MlGexingsDao mlGexingsDao;


    @Override
    public InvokeCrawlerDto.InvokeCrawlerResponse invokeCrawler() {
        MlGexings mlGexings = new MlGexings();

        GexingsDaoPipeline gexingsDaoPipeline = new GexingsDaoPipeline(mlGexingsDao, mlGexings);
        Spider spider = Spider.create(new GexingsProcessor())
                              .addUrl("http://www.gexings.com")
                              .thread(Runtime.getRuntime()
                                             .availableProcessors())
                              //.setScheduler(redisClientPriorityScheduler)
                              .addPipeline(gexingsDaoPipeline);
        spider.run();
        InvokeCrawlerDto.InvokeCrawlerResponse invokeCrawlerResponse = new InvokeCrawlerDto.InvokeCrawlerResponse();
        invokeCrawlerResponse.setResult(true);
        return invokeCrawlerResponse;
    }
}
