package mtime.ml.crawler.service.util;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.Objects;

public class HtmlUtil {


    public static String getHtmlText(String content) {

      /*  Document doc = Jsoup.parse(content); // 解析网页 得到文档对象
        Elements elements = doc.getAllElements();

        return elements.get(0)
                       .text();*/
        Objects.requireNonNull(content);
        Document doc = Jsoup.parse(content);

        HtmlToPlainText formatter = new HtmlToPlainText();
        return formatter.getPlainText(doc);


    }


}
