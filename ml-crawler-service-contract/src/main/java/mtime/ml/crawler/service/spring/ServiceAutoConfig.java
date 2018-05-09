package mtime.ml.crawler.service.spring;

import mtime.lark.net.rpc.RpcClient;
import mtime.ml.crawler.service.iface.InvokeCrawlerService;
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
    private final String SERVER = "mtime.ml.crawler.service";


    @Bean
    @ConditionalOnMissingBean
    public InvokeCrawlerService invokeCrawlerService() {
        return RpcClient.get(SERVER, InvokeCrawlerService.class);
    }
}
