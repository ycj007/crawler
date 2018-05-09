package mtime.ml.crawler.service.dao;

import mtime.lark.db.jsd.*;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static mtime.lark.db.jsd.Shortcut.f;

/**
 * MySQLShardingDao
 */
public abstract class MySQLShardingDao<T> {

    protected Class<T> clazz;

    public MySQLShardingDao() {
        Type genType = this.getClass()
                           .getGenericSuperclass();
        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
        this.clazz = (Class<T>) params[0];
    }

    /**
     * Gets db node name.
     *
     * @return the db node name
     */
    protected abstract String getDBNodeName();

    public Query getTransactionContext() {
        Transaction tx = Transaction.get();
        if (tx != null) {
            return tx;
        } else {
            return openDefaultWriteShard();
        }
    }

    public Query getTransactionContext(Object... keys) {
        Transaction tx = Transaction.get();
        if (tx != null) {
            return tx;
        } else {
            return openWriteShard(keys);
        }
    }

    /**
     * Open read shard database.
     *
     * @param keys the keys
     * @return the database
     */
    public Database openReadShard(Object... keys) {
        return DatabaseFactory.openReadShard(getDBNodeName(), keys);
    }

    /**
     * Open write shard database.
     *
     * @param keys the keys
     * @return the database
     */
    public Database openWriteShard(Object... keys) {
        return DatabaseFactory.openWriteShard(getDBNodeName(), keys);
    }

    /**
     * Open read shard database.
     *
     * @return the database
     */
    public Database openDefaultReadShard() {
        return DatabaseFactory.openReadShard(getDBNodeName(), 1);
    }

    /**
     * Open write shard database.
     *
     * @return the database
     */
    public Database openDefaultWriteShard() {
        return DatabaseFactory.openWriteShard(getDBNodeName(), 1);
    }


    public T findById(Object id) {
        return openDefaultReadShard().select(clazz)
                                     .where(f("id", id))
                                     .result()
                                     .one(clazz);
    }

    public Object save(T entity) {
        return getTransactionContext().insert(entity)
                                      .result(true)
                                      .getKeys()
                                      .get(0);
    }

    public int save(List<T> entities) {
        return getTransactionContext().insert(entities)
                                      .result()
                                      .getAffectedRows();
    }

    public int update(T entity) {
        return getTransactionContext().update(entity)
                                      .result()
                                      .getAffectedRows();
    }

    public int deleteById(T entity) {
        return getTransactionContext().delete(entity)
                                      .result()
                                      .getAffectedRows();
    }

    public List<T> findByIds(Object[] ids) {
        Objects.requireNonNull(ids);
        if (ids.length == 0) {
            return Collections.emptyList();
        }
        return openDefaultReadShard().select(clazz)
                                     .where(f("id", FilterType.IN, ids))
                                     .result()
                                     .all(clazz);
    }
}
