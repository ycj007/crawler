package mtime.ml.crawler.common.pipeline.es;

import org.apache.http.HttpHost;
import org.elasticsearch.action.ActionResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.function.Function;


public class ESClient {

    public static ActionResponse execute(Function<RestHighLevelClient,ActionResponse> consumer) throws UnknownHostException {
        HttpHost httpHost = new HttpHost("192.167.13.144", 9200, null);
        RestClientBuilder restClientBuilder = RestClient.builder(httpHost);
        //RestClient restClient  = restClientBuilder.build();
        try (RestHighLevelClient restHighLevelClient = new RestHighLevelClient(restClientBuilder);) {
            return consumer.apply(restHighLevelClient);
        }catch (Exception e){

        }

        return null;
    }



    public static ActionResponse index(IndexRequest indexRequest) throws UnknownHostException {

        return execute(restHighLevelClient -> {
            try {
                return restHighLevelClient.index(indexRequest);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        });


    }

    public static ActionResponse get(GetRequest getRequest) throws UnknownHostException {

        return execute(restHighLevelClient -> {
            try {
                return restHighLevelClient.get(getRequest);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        });
    }


}
