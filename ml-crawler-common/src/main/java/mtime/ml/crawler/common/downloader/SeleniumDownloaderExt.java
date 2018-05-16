package mtime.ml.crawler.common.downloader;

import mtime.lark.pb.utils.StringUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.downloader.Downloader;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.selector.PlainText;

import java.io.Closeable;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 使用Selenium调用浏览器进行渲染。目前仅支持chrome。<br>
 * 需要下载Selenium driver支持。<br>
 *
 * @author code4crafter@gmail.com <br>
 * Date: 13-7-26 <br>
 * Time: 下午1:37 <br>
 */
public class SeleniumDownloaderExt implements Downloader, Closeable {

    private volatile WebDriverPool webDriverPool;

    private Logger logger = Logger.getLogger(getClass());

    private int sleepTime = 0;

    private int poolSize = 1;

    private static final String DRIVER_PHANTOMJS = "phantomjs";

    /**
     * 新建
     *
     * @param chromeDriverPath chromeDriverPath
     */
    public SeleniumDownloaderExt(String chromeDriverPath) {
        System.getProperties()
              .setProperty("webdriver.chrome.driver",
                           chromeDriverPath);
    }

    /**
     * Constructor without any filed. Construct PhantomJS browser
     *
     * @author bob.li.0718@gmail.com
     */
    public SeleniumDownloaderExt() {
        // System.setProperty("phantomjs.binary.path",
        // "/Users/Bingo/Downloads/phantomjs-1.9.7-macosx/bin/phantomjs");
    }

    /**
     * set sleep time to wait until load success
     *
     * @param sleepTime sleepTime
     * @return this
     */
    public SeleniumDownloaderExt setSleepTime(int sleepTime) {
        this.sleepTime = sleepTime;
        return this;
    }

    @Override
    public Page download(Request request, Task task) {
        checkInit();
        WebDriver webDriver;
        try {
            webDriver = webDriverPool.get();
        } catch (InterruptedException e) {
            logger.warn("interrupted", e);
            return null;
        }
        logger.info("downloading page " + request.getUrl());
        webDriver.get(request.getUrl());
        try {
            Thread.sleep(sleepTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        WebDriver.Options manage = webDriver.manage();
        Site site = task.getSite();
        if (site.getCookies() != null) {
            for (Map.Entry<String, String> cookieEntry : site.getCookies()
                                                             .entrySet()) {
                Cookie cookie = new Cookie(cookieEntry.getKey(),
                                           cookieEntry.getValue());
                manage.addCookie(cookie);
            }
        }

        /*
         * TODO You can add mouse event or other processes
         *
         * @author: bob.li.0718@gmail.com
         */
        actions(webDriver);

        WebElement webElement = webDriver.findElement(By.xpath("/html"));
        String content = webElement.getAttribute("outerHTML");
        Page page = new Page();
        page.setRawText(content);
        page.setHtml(new Html(content, request.getUrl()));
        page.setUrl(new PlainText(request.getUrl()));
        page.setRequest(request);
        webDriverPool.returnToPool(webDriver);
        return page;
    }

    private void checkInit() {
        if (webDriverPool == null) {
            synchronized (this) {
                webDriverPool = new WebDriverPool(poolSize);
            }
        }
    }

    @Override
    public void setThread(int thread) {
        this.poolSize = thread;
    }

    @Override
    public void close() throws IOException {
        webDriverPool.closeAll();
    }



    private void actions(WebDriver webDriver ){

/*        Actions action = new Actions(webDriver);//-------------------------------------------声明一个动作
        WebElement xia = webDriver.findElement(By.xpath("//*[@id='bqBotNav']"));//----------找到向下滑动到的元素位置
        action.moveToElement(xia).build().perform();*/

        //WebElement xia =  webDriver.findElement(By.xpath("//*[@id='bqBotNav']"));
        int lastCount=0;
        int currentCount=0;
        List<String> end=findEnd(webDriver);
        int retryTimes = 0;
        boolean endFlag = false;
        currentCount = end.size();
        while(!endFlag){
             boolean canExecut =false;
            if(currentCount > lastCount){
                canExecut =true;
                retryTimes=0;
            }else if(retryTimes<3){
                retryTimes++;
                canExecut =true;
            }
            if(canExecut) {
                lastCount = end.size();
                ((JavascriptExecutor) webDriver).executeScript("window.scrollTo(0, document.body.scrollHeight)");
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                end = findEnd(webDriver);
                currentCount = end.size();
            }else {
                endFlag =true;
            }
        }




    }

    private List<String> findEnd(WebDriver webDriver){
        WebElement webElement = webDriver.findElement(By.xpath("/html"));
        String content = webElement.getAttribute("outerHTML");
        Html html = new Html(content);
        return html.css(".grid-item").all();
    }



}
