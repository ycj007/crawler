package mtime.ml.crawler.common.pipeline.dao;

import com.alibaba.fastjson.JSON;
import mtime.lark.util.log.Logger;
import mtime.lark.util.log.LoggerManager;
import mtime.ml.crawler.common.dao.BaseDao;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

public class GexingsDaoPipeline<T extends BaseDao, E> implements Pipeline {
    private static Logger logger = LoggerManager.getLogger(GexingsDaoPipeline.class);

    T t;
    E e;

    public GexingsDaoPipeline(T t, E e) {
        this.t = t;
        this.e = e;
    }

    @Override
    public void process(ResultItems resultItems, Task task) {

        E githubRepo = (E) resultItems.get(e.getClass().getSimpleName());
        //调用 dao 层
        if (githubRepo != null) {
            t.insertOrUpdate(githubRepo);
        }
        logger.info(JSON.toJSONString(githubRepo));
    }
}