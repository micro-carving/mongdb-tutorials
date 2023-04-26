package com.olinonee.mongodb.quickstart.test;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.types.Binary;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

/**
 * 图片存储 MongoDB 存储
 *
 * @author olinH, olinone666@gmail.com
 * @version v1.0.0
 * @since 2023-04-26
 */
public class ImageStoreMongoTests {
    public static void main(String[] args) throws IOException {
        String uri = "mongodb://root:root@localhost:27017/?authSource=admin";
        try (MongoClient mongoClient = MongoClients.create(uri)) {
            MongoDatabase database = mongoClient.getDatabase("test");
            MongoCollection<Document> collection = database.getCollection("image");

            // 读取图片并将其转换为二进制格式
            File imageFile = new File("myImage.jpg");
            BufferedImage bufferedImage = ImageIO.read(imageFile);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, "jpg", baos);
            byte[] bytes = baos.toByteArray();

            // 将图片数据插入到数据库中
            Document document = new Document();
            document.append("name", "myImage");
            document.append("image", new Binary(bytes));
            collection.insertOne(document);

            // 从数据库中获取图片数据
            Document imageDocument = collection.find(new Document("name","myImage")).first();
            assert imageDocument != null;
            Binary binary = imageDocument.get("image", Binary.class);
            byte[] imageBytes = binary.getData();

            // 将二进制数据转换为图片对象并展示图片
            BufferedImage image = ImageIO.read(new ByteArrayInputStream(imageBytes));
            File outputFile = new File("myImageRetrieve.jpg");
            ImageIO.write(image, "jpg", outputFile);
        }
    }
}
