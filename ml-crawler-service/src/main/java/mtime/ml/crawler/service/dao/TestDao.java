package mtime.ml.crawler.service.dao;

import mtime.ml.crawler.service.entity.TestObject;
import org.springframework.stereotype.Repository;

@Repository
public class TestDao {
    // todo: remove this method
    public TestObject getObject(int id) {
        TestObject object = new TestObject();
        object.setId(id);
        object.setName("noname");
        return object;
    }
}
