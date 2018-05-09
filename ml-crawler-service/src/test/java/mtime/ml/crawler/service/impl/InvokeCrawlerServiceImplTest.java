package mtime.ml.crawler.service.impl;

import mtime.lark.util.ioc.ServiceLocator;
import mtime.ml.crawler.service.AbstractTest;
import mtime.ml.crawler.service.iface.InvokeCrawlerService;
import org.junit.Test;

import static org.junit.Assert.*;

public class InvokeCrawlerServiceImplTest extends AbstractTest {


    InvokeCrawlerService testService = ServiceLocator.current().getInstance(InvokeCrawlerService.class);
    @Test
    public void invokeCrawler() {
        testService.invokeCrawler();
    }
}