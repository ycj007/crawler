package mtime.ml.crawler.service.dao;

import mtime.ml.crawler.common.dao.BaseDao;
import mtime.ml.crawler.common.dao.MySQLShardingDao;
import mtime.ml.crawler.service.entity.MlQuote;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import static mtime.lark.db.jsd.Shortcut.count;
import static mtime.lark.db.jsd.Shortcut.f;

/**
 */
@Repository
public class MlQuoteDao extends MySQLShardingDao<MlQuote> implements BaseDao<List<MlQuote>> {

    /**
     * DBName
     */
    private static final String DB_NODE_NAME = "SvsUserCenterShard";
    private static final String TABLE_NAME = "ml_quote";

    @Override
    protected String getDBNodeName() {
        return DB_NODE_NAME;
    }

    protected String getTableName() {
        return TABLE_NAME;
    }

    @Override
    public void insertOrUpdate(List<MlQuote> mlQuoteList) {
        Objects.requireNonNull(mlQuoteList);

        mlQuoteList.forEach(data -> {

            if (data != null) {

                long count = (long) openDefaultReadShard().select(count())
                                                          .from(getTableName())
                                                          .where(f("hash", data.getHash()))
                                                          .result()
                                                          .value();
                if (count == 0) {

                    //插入
                    LocalDateTime localDateTime = LocalDateTime.now();
                    data.setCreateTime(localDateTime);
                    data.setUpdateTime(localDateTime);
                    openDefaultWriteShard().insert(getTableName())
                                           .columns("hash", "url", "content", "category", "keywords", "author", "topic", "info")
                                           .values(data.getHash(), data.getUrl(), data.getContent(),
                                                   data.getCategory(), data.getKeywords(), data.getAuthor(), data.getTopic(), data.getInfo())
                                           .result(true);

                }

            }


        });


    }


}
