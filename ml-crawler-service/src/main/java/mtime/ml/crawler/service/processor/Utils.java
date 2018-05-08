package mtime.ml.crawler.service.processor;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class Utils {


    public static String getHtmlText(String content) {

        Document doc = Jsoup.parse(content); // 解析网页 得到文档对象
        Elements elements = doc.getAllElements();

        return elements.get(0)
                       .text();


    }
}
