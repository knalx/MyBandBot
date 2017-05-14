package com.knalx.dao;

import com.knalx.model.Band;
import com.mongodb.DB;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.Set;

import static org.springframework.data.mongodb.core.query.Criteria.where;

/**
 * @since 02.05.17
 **/
@Repository
public class MongoDao {

    public static String COLLECTION = "myband";
    private final MongoTemplate mongoTemplate;

    @Autowired
    public MongoDao(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public void insert() {
        Band band = new Band();
        band.setName("name");
        band.setTest("1");
        band.setMembers(Collections.EMPTY_SET);
        mongoTemplate.insert(band, COLLECTION);

        Band one = mongoTemplate.findOne(new Query(where("name").exists(true)), Band.class);
        System.out.println(one);
    }


}
