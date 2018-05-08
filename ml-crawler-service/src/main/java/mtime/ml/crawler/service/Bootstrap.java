package mtime.ml.crawler.service;

import mtime.lark.net.rpc.RpcApplication;
import mtime.lark.net.rpc.config.ServerOptions;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;

@SpringBootApplication(exclude = {MongoAutoConfiguration.class, MongoDataAutoConfiguration.class})
public class Bootstrap {
    public static void main(String[] args) {
        // default options
        ServerOptions options = new ServerOptions(":9090", "爬虫服务");
        RpcApplication app = new RpcApplication(Bootstrap.class, args, options);
        app.run();
    }
}
