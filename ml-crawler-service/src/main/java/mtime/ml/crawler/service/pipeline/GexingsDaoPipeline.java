package mtime.ml.crawler.service.pipeline;

import com.alibaba.fastjson.JSON;
import mtime.lark.util.log.Logger;
import mtime.lark.util.log.LoggerManager;
import mtime.ml.crawler.service.entity.GexingsModel;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

public class GexingsDaoPipeline implements Pipeline {
    private static Logger logger = LoggerManager.getLogger(GexingsDaoPipeline.class);

    @Override
    public void process(ResultItems resultItems, Task task) {

        GexingsModel githubRepo = (GexingsModel) resultItems.get(GexingsModel.class.getSimpleName());
        //调用 dao 层

        logger.info(JSON.toJSONString(githubRepo));
    }
}