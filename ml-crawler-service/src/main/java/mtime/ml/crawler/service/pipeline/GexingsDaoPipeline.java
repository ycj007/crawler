package mtime.ml.crawler.service.pipeline;

import com.alibaba.fastjson.JSON;
import mtime.lark.util.log.Logger;
import mtime.lark.util.log.LoggerManager;
import mtime.ml.crawler.service.dao.MlGexingsDao;
import mtime.ml.crawler.service.entity.MlGexings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

@Component
public class GexingsDaoPipeline implements Pipeline {
    private static Logger logger = LoggerManager.getLogger(GexingsDaoPipeline.class);

    @Autowired
    private MlGexingsDao mlGexingsDao;
    @Override
    public void process(ResultItems resultItems, Task task) {

        MlGexings githubRepo = (MlGexings) resultItems.get(MlGexings.class.getSimpleName());
        //调用 dao 层
        if(githubRepo!=null) {
            mlGexingsDao.insertOrUpdate(githubRepo);
        }
        logger.info(JSON.toJSONString(githubRepo));
    }
}