package mtime.ml.crawler.service.impl;

import mtime.lark.net.rpc.annotation.RpcService;
import mtime.ml.crawler.service.biz.TestBiz;
import mtime.ml.crawler.service.entity.TestObject;
import mtime.ml.crawler.service.dto.TestDto;
import mtime.ml.crawler.service.iface.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@RpcService(name = "TestService", description = "测试服务")
public class TestServiceImpl implements TestService {
    @Autowired
    private TestBiz testBiz;

    @Override
    public TestDto.HelloResponse hello(TestDto.HelloRequest request) {
        TestObject object = testBiz.getObject(request.getId());
        TestDto.HelloResponse response = new TestDto.HelloResponse();
        response.setResult(object.getName());
        return response;
    }
}
