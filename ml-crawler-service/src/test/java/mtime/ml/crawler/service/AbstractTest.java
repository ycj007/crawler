package mtime.ml.crawler.service;

import mtime.lark.util.test.TestBase;
import mtime.lark.util.test.spring.SpringJUnit;
import org.junit.BeforeClass;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;

public abstract class AbstractTest extends TestBase {
    @BeforeClass
    public static void init() {
        SpringJUnit.boot(Dummy.class, Bootstrap.class);
    }

    @SpringBootApplication(exclude = {MongoAutoConfiguration.class, MongoDataAutoConfiguration.class})
    public static class Dummy {
    }
}
