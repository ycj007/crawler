package mtime.ml.crawler.common.dao;

public interface BaseDao<E> {

    public void insertOrUpdate(E e);
}
