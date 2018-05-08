package mtime.ml.crawler.service.processor;

import mtime.ml.crawler.service.entity.GexingsModel;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.ConsolePipeline;
import us.codecraft.webmagic.pipeline.FilePipeline;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.scheduler.RedisPriorityScheduler;

import java.util.List;

public class GexingsProcessor implements PageProcessor {


    public static final String URL_FIRST_LIST = "^http://www\\.gexings\\.com$";
    //第二个url 需要第一个内容
    public static final String URL_SECOND_LIST = "http://www\\.gexings\\.com/[a-z]+/{0,1}/[a-z]*/list\\_\\d+\\_\\d+\\.html";
    public static final String URL_SECOND_LIST_2 = "http://www\\.gexings\\.com/[a-z]+/{0,1}/[a-z]*$";
    public static final String URL_POST = "http://www\\.gexings\\.com/[a-z]+/{0,1}[a-z]*/[0-9]+\\.html";

    private static int sleepTime = 1000;
    private static int retTryTimes = 5;
    private static int retrySleepTime = 5;
    private static int timeOut = 10000;
    private static String charset = "gbk";
    private static String userAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/66.0.3359.139 Safari/537.36";


    private Site site = Site
            .me()
            .setDomain("www.gexings.com")
            .setSleepTime(sleepTime)
            .setRetryTimes(retTryTimes)
            .setRetrySleepTime(retrySleepTime)
            .setTimeOut(timeOut)
            .setCharset(charset)
            //.setCharset("utf-8")
            .setUserAgent(userAgent);

    public static void main(String[] args) {
        RedisPriorityScheduler redisPriorityScheduler = new RedisPriorityScheduler("127.0.0.1");
        Spider.create(new GexingsProcessor())
              .addUrl("http://www.gexings.com")
              .thread(Runtime.getRuntime()
                             .availableProcessors())
              .setScheduler(redisPriorityScheduler)
              .addPipeline(new FilePipeline("d://data"))
              .addPipeline(new ConsolePipeline())
              .run();

    }

    @Override
    public void process(Page page) {
        if (page.getUrl()
                .regex(URL_FIRST_LIST)
                .match()) {
            List<String> urls = page.getHtml()
                                    .xpath("//div[@class=\"new_header\"]")
                                    .links()
                                    .regex(URL_SECOND_LIST)
                                    .all();
            page.addTargetRequests(urls);

            List<String> urls_2 = page.getHtml()
                                      .links()
                                      .regex(URL_SECOND_LIST_2)
                                      .all();
            page.addTargetRequests(urls_2);


        }
        if (page.getUrl()
                .regex(URL_SECOND_LIST)
                .match() || page.getUrl()
                                .regex(URL_SECOND_LIST_2)
                                .match()) {
            List<String> urls = page.getHtml()
                                    .css("div.listbox")
                                    .links()
                                    .regex(URL_POST)
                                    .all();

            page.addTargetRequests(urls);


            page.addTargetRequests(page.getHtml()
                                       .links()
                                       .regex(URL_SECOND_LIST)
                                       .all());
            page.addTargetRequests(page.getHtml()
                                       .links()
                                       .regex(URL_SECOND_LIST_2)
                                       .all());

        }
        if (page.getUrl()
                .regex(URL_POST)
                .match()) {

            String place = page.getHtml()
                               .css("div.place")
                               .get();
            place = Utils.getHtmlText(place);
            String title = page.getHtml()
                               .css("div.title")
                               .css("h2")
                               .toString();
            title = Utils.getHtmlText(title);
            String info = page.getHtml()
                              .css("div.info")
                              .get();
            info = Utils.getHtmlText(info);
            String content = page.getHtml()
                                 .css("div.content")

                                 .get();
            content = Utils.getHtmlText(content);

            GexingsModel gexingsModel = new GexingsModel();
            gexingsModel.setContent(content);
            gexingsModel.setInfo(info);
            gexingsModel.setPlace(place);
            gexingsModel.setTitle(title);
            page.putField(GexingsModel.class.getSimpleName(), gexingsModel);


        }


    }

    @Override
    public Site getSite() {
        return site;
    }
}
