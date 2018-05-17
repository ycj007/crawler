package mtime.ml.crawler.service.processor;

import com.google.common.collect.Lists;
import mtime.lark.pb.utils.StringUtils;
import mtime.ml.crawler.service.Bootstrap;
import mtime.ml.crawler.service.constant.DefaultConstant;
import mtime.ml.crawler.service.entity.MlQuote;
import mtime.ml.crawler.service.util.HtmlUtil;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.ConsolePipeline;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Selectable;

import java.util.List;

import static mtime.ml.crawler.service.constant.CategoryConstant.*;

/**
 * 花瓣网抽取器。<br>
 * 使用Selenium做页面动态渲染。<br>
 *
 * @author code4crafter@gmail.com <br>
 * Date: 13-7-26 <br>
 * Time: 下午4:08 <br>
 */
public class BrainyquoteProcessor implements PageProcessor {


    private Site site;

    @Override
    public void process(Page page) {


        if (page.getUrl()
                .regex("^https://www\\.brainyquote\\.com/$")
                .match()) {
            page.addTargetRequests(page.getHtml()
                                       .links()
                                       .regex("https://www\\.brainyquote\\.com/\\w+")
                                       .all());
            List<MlQuote> quoteList = Lists.newLinkedList();
            page.getHtml()
                .css(".grid-item")
                .nodes()
                .forEach(
                        node -> {
                            if (node != null) {
                                MlQuote mlQuote = packageData(node, page.getUrl()
                                                                        .get());
                                if (mlQuote != null) {
                                    mlQuote.setCategory(HOME);
                                    quoteList.add(mlQuote);
                                }
                            }


                        }
                );

            page.putField("data", quoteList);


        }
        if (page.getUrl()
                .regex("https://www\\.brainyquote\\.com/authors|topics/\\w+")
                .match()) {
            page.addTargetRequests(page.getHtml()
                                       .links()
                                       .regex("https://www\\.brainyquote\\.com/topics/\\w+")
                                       .all());
            page.addTargetRequests(page.getHtml()
                                       .links()
                                       .regex("https://www\\.brainyquote\\.com/authors/\\w+")
                                       .all());
            List<MlQuote> quoteList = Lists.newLinkedList();
            String originalAuthorInfo = page.getHtml()
                                            .css(".bio-under")
                                            .get();
            if (!StringUtils.isEmpty(originalAuthorInfo)) {
                String info = HtmlUtil.getHtmlText(originalAuthorInfo);
                page.getHtml()
                    .css(".grid-item")
                    .nodes()
                    .forEach(
                            node -> {
                                MlQuote quote = packageData(node, page.getUrl()
                                                                      .get());
                                if (quote != null) {
                                    quote.setInfo(info.replace("Quotes"," - "));

                                    if (page.getUrl()
                                            .regex("https://www\\.brainyquote\\.com/authors/\\w+")
                                            .match()) {
                                        quote.setCategory(AUTHORS);

                                    } else {
                                        quote.setCategory(TOPIS);
                                    }
                                    quoteList.add(quote);
                                }
                            }
                    );

                page.putField("data", quoteList);
            }


        }

    }

    @Override
    public Site getSite() {
        if (null == site) {
            site = Site.me()
                       .setDomain("www.brainyquote.com")
                       .setSleepTime(0);
        }
        return site;
    }

    public static void main(String[] args) {
        String path = Bootstrap.class.getClassLoader()
                                     .getResource("config/config.ini")
                                     .getPath();

        String exePath = Bootstrap.class.getClassLoader()
                                        .getResource("config/phantomjs.exe")
                                        .getPath();
        System.setProperty("selenuim_config", path);
        System.setProperty("phantomjs_exec_path", exePath);
        Spider.create(new BrainyquoteProcessor())
              .addPipeline(new ConsolePipeline())
              //.addPipeline(new FilePipeline("d://data"))
              //.setDownloader(new SeleniumDownloaderExt("D:\\chromedriver\\chromedriver_win32\\chromedriver.exe"))
              // .setDownloader(new SeleniumDownloaderExt())
              .addUrl("https://www.brainyquote.com/")
              .run();
    }


    private MlQuote packageData(Selectable node, String url) {
        if (node != null) {
            MlQuote data = new MlQuote();

            /**
             * 初始化数据
             */
            data.setTopic(DefaultConstant.NONE);
            data.setInfo(DefaultConstant.NONE);
            data.setUrl(url);
            String originalContet = node.css(".b-qt")
                                        .get();
            String originalAuthor = node.css(".bq-aut")
                                        .get();
            String originalKeyWords = node.css(".kw-box")
                                          .get();

            if (!StringUtils.isEmpty(originalContet) && !StringUtils.isEmpty(originalAuthor) && !StringUtils.isEmpty(originalKeyWords)) {
                String content = HtmlUtil.getHtmlText(originalContet);

                String author = HtmlUtil.getHtmlText(originalAuthor);

                String keyWords = HtmlUtil.getHtmlText(originalKeyWords);
                data.setContent(content);
                data.setAuthor(author);
                data.setKeywords(keyWords);
                data.setHash(HtmlUtil.getMd5(url + data.getContent()));

                return data;
            }

        }
        return null;
    }
}
