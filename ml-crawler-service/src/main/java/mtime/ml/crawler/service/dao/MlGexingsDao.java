package mtime.ml.crawler.service.dao;

import mtime.lark.db.jsd.UpdateValues;
import mtime.lark.pb.utils.StringUtils;
import mtime.ml.crawler.common.dao.BaseDao;
import mtime.ml.crawler.common.dao.MySQLShardingDao;
import mtime.ml.crawler.common.participles.Participles;
import mtime.ml.crawler.service.entity.MlGexings;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Objects;

import static mtime.lark.db.jsd.Shortcut.count;
import static mtime.lark.db.jsd.Shortcut.f;

/**
 */
@Repository
public class MlGexingsDao extends MySQLShardingDao<MlGexings> implements BaseDao<MlGexings>{

    /**
     * DBName
     */
    private static final String DB_NODE_NAME = "SvsUserCenterShard";
    private static final String TABLE_NAME = "ml_gexings";

    @Override
    protected String getDBNodeName() {
        return DB_NODE_NAME;
    }

    protected String getTableName() {
        return TABLE_NAME;
    }

    @Override
    public void insertOrUpdate(MlGexings mlGexings) {
        Objects.requireNonNull(mlGexings);
        if (!StringUtils.isEmpty(mlGexings.getUrl())) {
            if(!StringUtils.isEmpty(mlGexings.getContent())){
                mlGexings.setResolveContent(Participles.participlesWithmseg4j(mlGexings.getContent()));
            }
            long count = (long) openDefaultReadShard().select(count())
                                                      .from(getTableName())
                                                      .where(f("url", mlGexings.getUrl()))
                                                      .result()
                                                      .value();
            if (count > 0) {
                //更新
                UpdateValues updateValues = new UpdateValues();
                updateValues.add("place", mlGexings.getPlace());
                updateValues.add("title", mlGexings.getTitle());
                updateValues.add("info", mlGexings.getInfo());
                updateValues.add("content", mlGexings.getContent());
                updateValues.add("resolve_content", mlGexings.getResolveContent());
                openDefaultWriteShard().update(getTableName())
                                       .set(updateValues)
                                       .where(f("url", mlGexings.getUrl()));
            } else {
                //插入
                LocalDateTime localDateTime = LocalDateTime.now();
                mlGexings.setCreateTime(localDateTime);
                mlGexings.setUpdateTime(localDateTime);
                openDefaultWriteShard().insert(getTableName())
                                       .columns("url", "place", "title", "info", "content","resolve_content")
                                       .values(mlGexings.getUrl(), mlGexings.getPlace(), mlGexings.getTitle(),
                                               mlGexings.getInfo(), mlGexings.getContent(),mlGexings.getResolveContent())
                                       .result(true);
            }

        }

    }


}
