package com.olinonee.mongodb.quickstart.test;

import com.mongodb.client.*;
import org.bson.Document;

/**
 * 快速开始测试类
 *
 * @author olinH, olinone666@gmail.com
 * @version v1.0.0
 * @since 2023-04-26
 */
public class QuickstartTests {

    public static void main(String[] args) {
        String uri = "mongodb://root:root@localhost:27017/admin";
        try (MongoClient mongoClient = MongoClients.create(uri)) {
            MongoDatabase database = mongoClient.getDatabase("admin");
            MongoCollection<Document> collection = database.getCollection("system.version");
            final FindIterable<Document> findIterable = collection.find();
            findIterable.forEach(System.out::println);
        }
    }
}
