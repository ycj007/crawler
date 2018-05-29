package mtime.ml.crawler.service.impl;

import com.google.common.collect.Lists;
import mtime.lark.net.rpc.annotation.RpcService;
import mtime.ml.crawler.common.downloader.SeleniumDownloaderExt;
import mtime.ml.crawler.common.pipeline.dao.DaoPipeline;
import mtime.ml.crawler.common.scheduler.RedisClientPriorityScheduler;
import mtime.ml.crawler.service.Bootstrap;
import mtime.ml.crawler.service.dao.MlGexingsDao;
import mtime.ml.crawler.service.dao.MlQuoteDao;
import mtime.ml.crawler.service.dto.InvokeCrawlerDto;
import mtime.ml.crawler.service.entity.MlGexings;
import mtime.ml.crawler.service.entity.MlQuote;
import mtime.ml.crawler.service.iface.InvokeCrawlerService;
import mtime.ml.crawler.service.processor.BrainyquoteProcessor;
import mtime.ml.crawler.service.processor.GexingsProcessor;
import mtime.ml.crawler.service.util.EventUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Spider;

import java.util.List;

@Component
@RpcService(name = "InvokeCrawlerService", description = "爬虫服务")
public class InvokeCrawlerServiceImpl implements InvokeCrawlerService {


    @Autowired
    private RedisClientPriorityScheduler redisClientPriorityScheduler;
    @Autowired
    private MlGexingsDao mlGexingsDao;
    @Autowired
    private MlQuoteDao mlQuoteDao;
    @Autowired
    private EventUtil eventUtil;


    @Override
    public InvokeCrawlerDto.InvokeCrawlerResponse invokeCrawler() {
        //invokeGexing();
        invokerQuote();
        //eventUtil.execute();
        InvokeCrawlerDto.InvokeCrawlerResponse invokeCrawlerResponse = new InvokeCrawlerDto.InvokeCrawlerResponse();
        invokeCrawlerResponse.setResult(true);
        return invokeCrawlerResponse;
    }


    private void invokerQuote() {
        String path = Bootstrap.class.getClassLoader()
                                     .getResource("config/config.ini")
                                     .getPath();

        String phantomjsPath = "config/phantomjs";
        String os = System.getProperty("os.name");
        if (os != null && os.startsWith("Windows")) {
            phantomjsPath = "config/phantomjs.exe";
        }
        String exePath = Bootstrap.class.getClassLoader()
                                        .getResource(phantomjsPath)
                                        .getPath();
        System.setProperty("selenuim_config", path);
        System.setProperty("phantomjs_exec_path", exePath);
        List<MlQuote> list = Lists.newArrayList();
        DaoPipeline quotePipeline = new DaoPipeline(mlQuoteDao, list);
        Spider.create(new BrainyquoteProcessor())
              .addPipeline(quotePipeline)
              .setDownloader(new SeleniumDownloaderExt())
              .setScheduler(redisClientPriorityScheduler)
              .thread(Runtime.getRuntime()
                             .availableProcessors())
              .addUrl("https://www.brainyquote.com/")
              .run();
    }

    private void invokeGexing() {
        MlGexings mlGexings = new MlGexings();

        DaoPipeline gexingsDaoPipeline = new DaoPipeline(mlGexingsDao, mlGexings);
        Spider spider = Spider.create(new GexingsProcessor())
                              .addUrl("http://www.gexings.com")
                              .thread(Runtime.getRuntime()
                                             .availableProcessors())
                              .setScheduler(redisClientPriorityScheduler)
                              .addPipeline(gexingsDaoPipeline);
        spider.run();
    }


}
