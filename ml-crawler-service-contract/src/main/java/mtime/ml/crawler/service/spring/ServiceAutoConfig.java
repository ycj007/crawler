package mtime.ml.crawler.service.spring;

import mtime.lark.net.rpc.RpcClient;
import mtime.ml.crawler.service.iface.TestService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

@Configuration
@Lazy
@Order(Ordered.LOWEST_PRECEDENCE)
public class ServiceAutoConfig {
    private final String SERVER = "mtime.test.ml.crawler.service";

    @Bean
    @ConditionalOnMissingBean
    public TestService testService() {
        return RpcClient.get(SERVER, TestService.class);
    }
}
