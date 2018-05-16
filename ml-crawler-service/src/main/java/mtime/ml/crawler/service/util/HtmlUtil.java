package mtime.ml.crawler.service.util;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import sun.misc.BASE64Encoder;
import us.codecraft.webmagic.pipeline.FilePipeline;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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

    public static String getMd5(String content) {

        return DigestUtils.md5Hex(ToStringBuilder.reflectionToString(content));

    }


}
