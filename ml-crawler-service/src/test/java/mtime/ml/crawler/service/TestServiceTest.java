package mtime.ml.crawler.service;

import mtime.lark.util.ioc.ServiceLocator;
import mtime.ml.crawler.service.constant.TestType;
import mtime.ml.crawler.service.dto.TestDto;
import mtime.ml.crawler.service.iface.TestService;
import org.junit.Test;

import java.time.LocalDateTime;

public class TestServiceTest extends AbstractTest {
    TestService testService = ServiceLocator.current().getInstance(TestService.class);

    @Test
    public void testHello() throws Exception {
        TestDto.HelloRequest request = new TestDto.HelloRequest();
        request.setId(123);
        request.setType(TestType.GOOD);
        request.setTime(LocalDateTime.now());

        TestDto.HelloResponse response = testService.hello(request);
        printJsonln(response);
    }
}
