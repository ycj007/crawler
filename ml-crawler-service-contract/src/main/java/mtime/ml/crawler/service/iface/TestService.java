package mtime.ml.crawler.service.iface;

import mtime.ml.crawler.service.dto.TestDto.*;

/**
 * 测试服务
**/
public interface TestService {	
	/**
	 * 测试
	**/
	HelloResponse hello(HelloRequest request);
}