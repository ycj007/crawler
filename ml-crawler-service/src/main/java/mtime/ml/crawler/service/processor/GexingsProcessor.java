package mtime.ml.crawler.service.processor;

import com.google.common.collect.Lists;
import org.assertj.core.util.Collections;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.ConsolePipeline;
import us.codecraft.webmagic.processor.PageProcessor;

import java.util.List;

public class GexingsProcessor implements PageProcessor {


    public static final String URL_FIRST_LIST = "http://www\\.gexings\\.com";
    //第二个url 需要第一个内容
    public static final String URL_SECOND_LIST = "http://www\\.gexings\\.com/[a-z]+/([a-z]+)?/(listw+\\.html)?";

    public static final String URL_POST = "http://www\\.gexings\\.com/[a-z]+(/[a-z]+)?/[0-9]+\\.html";

    private Site site = Site
            .me()
            .setDomain("www.gexings.com")
            .setSleepTime(3000)
            .setUserAgent(
                    "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/66.0.3359.139 Safari/537.36");

    @Override
    public void process(Page page) {
         //获取首页所有标题

      /*  if(!Collections.isNullOrEmpty(secondPaths)){
            page.addTargetRequests(secondPaths);
        }

        if (page.getUrl().regex(URL_SECOND_LIST).match()) {
            page.addTargetRequests(page.getHtml().xpath("//div[@class=\"listbox\"]").links().regex(URL_POST).all());
            page.addTargetRequests(page.getHtml().links().regex(URL_SECOND_LIST).all());
        }
        if (page.getUrl().regex(URL_FIRST_LIST).match()) {

             page.getHtml().css("div.nav ").css("li").nodes().forEach(data->{
                 String  secondUrl = data.css("a","href").toString();
                 String  firstTitle = data.css("a","title").toString();
                 page.putField("firstTitle",firstTitle);
                 secondPaths.add(secondUrl);
             });

        }*/




        if (page.getUrl().regex(URL_FIRST_LIST).match()) {

            page.addTargetRequests(page.getHtml().xpath("//div[@class=\"new_header\"]").links().regex(URL_SECOND_LIST).all());

        }
        if (page.getUrl().regex(URL_SECOND_LIST).match()) {
            page.addTargetRequests(page.getHtml().xpath("//div[@class=\"listbox\"]").links().regex(URL_POST).all());
            page.addTargetRequests(page.getHtml().links().regex(URL_SECOND_LIST).all());
        }
        if (page.getUrl().regex(URL_POST).match()) {

             page.putField("place",page.getHtml().css("div.place"));
             page.putField("page",page.getHtml().css("div.title"));
             page.putField("info",page.getHtml().css("div.info"));
             page.putField("content",page.getHtml().css("div.content"));

        }



    }

    @Override
    public Site getSite() {
        return site;
    }

    public static void main(String[] args) {
        Spider.create(new GexingsProcessor())
              .addUrl("http://www.gexings.com")
              .addPipeline(new ConsolePipeline()).thread(5)
              .run();
    }
}
