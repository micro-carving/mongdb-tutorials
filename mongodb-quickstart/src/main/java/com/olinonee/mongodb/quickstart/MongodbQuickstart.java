package com.olinonee.mongodb.quickstart;

import com.mongodb.client.*;
import org.bson.Document;

/**
 * MongoDB 快速开始
 *
 * @author olinH, olinone666@gmail.com
 * @version v1.0.0
 * @since 2023-04-28
 */
public class MongodbQuickstart {

    public static void main(String[] args) {
        // 格式：[jdbc:]mongodb[+srv]://[{user:identifier}[:{password:param}]@]<\,,{host::localhost}?[:{port::27017}]>[/{database}?[\?<&,{:identifier}={:param}>]]
        final String uri = "mongodb://root:root@localhost:27017/admin";
        try (MongoClient mongoClient = MongoClients.create(uri)) {
            MongoDatabase database = mongoClient.getDatabase("admin");
            MongoCollection<Document> collection = database.getCollection("system.version");
            final FindIterable<Document> findIterable = collection.find();
            findIterable.forEach(System.out::println);
        }
    }
}
