package com.knalx;

import com.knalx.dao.MongoDao;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @since 02.05.17
 **/
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@Ignore
public class MongoDaoTest {

    @Autowired
    MongoDao mongoDao;

    @Test
    public void testInsert() {
        mongoDao.insert();
    }

}
