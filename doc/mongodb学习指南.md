# MongoDB 概述

## MongoDB 是什么？

MongoDB 是一个文档数据库，旨在简化应用程序的开发和扩展。

官网地址：https://www.mongodb.com/

## MongoDB 可以干什么？

- 对数据查询并存储
- 通过聚合函数转换数据
- 保障数据的访问安全
- 部署并扩展数据库

## MongoDB 应用场景

1. **日志存储**

在互联网业务中，日志是非常重要的数据，能够反映系统的运行情况和用户的行为。MongoDB 的文档模型非常适合存储日志数据，因为日志数据格式通常比较灵活，同时需要支持高效的读写操作。

2. **社交网络应用**

社交网络应用通常需要对用户产生的大量数据进行存储和分析，包括用户的关系、动态、评论等等。由于这些数据非常复杂，传统的关系型数据库可能无法轻松地存储和查询这些数据。

3. **物联网应用**

物联网应用通常需要存储和查询大量的传感器数据，这些数据通常有一定的结构，但是需要支持高效的读写操作。

4. **游戏**

使用 MongoDB 作为游戏服务器的数据库存储用户信息。用户的游戏装备、积分等直接以内嵌文档的形式存储，方便进行查询与更新。

## MongoDB 数据类型 BSON

### BSON 是什么？

BSON 是一种类似 JSON 的一种二进制形式的存储格式，“BSON” 是 “二进制” 和 “JSON” 的合成词，可以将 BSON 看作 JSON (JavaScript Object Notation) 文档的二进制表示。它和
JSON 一样，支持内嵌的文档对象和数组对象，但是 BSON 有 JSON 没有的一些数据类型，如 `Date` 和 `BinData` 类型。

BSON 可以做为网络数据交换的一种存储形式，这个有点类似于 Google 的 Protocol Buffer，但是 BSON 是一种 schema-less 的存储形式，它的优点是灵活性高，但它的缺点是空间利用率不是很理想， BSON
具有三个特点：轻量性、可遍历性、高效性

`{"hello":"world"}` 这是一个 BSON 的例子，其中 "hello" 是 key name，它一般是 `cstring` 类型，字节表示是 `cstring::= (byte*) "/x00"` ,其中 `*`
表示零个或多个 `byte` 字节，`/x00` 表示结束符`;` 后面的 `"world"` 是 `value` 值，它的类型一般是 `string,double,array,binarydata` 等类型。

在 MongoDB 中用于存储文档和进行远程过程调用的序列化格式。BSON 规范位于 [bsonspec.org](http://bsonspec.org/)。

## MongoDB 基本概念和操作

### 文档结构

在 MongoDB 中，一个文档可以包含多个字段，每个字段以名值对的形式存在，字段的值可以是字符串、数值、日期、布尔型、数组等，这些值都可以嵌套其他文档或数组。

例如，以下是一个 MongoDB 文档的示例：

```bson
{
  "_id": "5f262d6aa01dd6a49a6fa8d6",
  "title": "MongoDB 存储示例",
  "content": "这是一篇使用 MongoDB 存储数据的示例。",
  "tags": ["MongoDB", "数据库", "NoSQL"],
  "createdDate": ISODate("2023-03-31T09:34:02.765Z")
}
```

其中 "_id" 是文档的唯一标识符，"title"、"content"、"tags"、"createdDate" 都是字段名，对应这些字段的值分别是 "MongoDB 存储示例"、"这是一篇使用 MongoDB 存储数据的示例。"
、["MongoDB", "数据库", "NoSQL"]、ISODate("2023-03-31T09:34:02.765Z")。

### 集合

MongoDB 的所有文档都存储在集合中，一个集合可以包含多个文档，每个文档可以不同的结构。集合不需要预定义数据结构，插入的每个文档可以有不同的字段集合。

例如，以下是一个创建集合并插入文档的示例：

```shell
// 创建集合
db.createCollection("articles")

// 插入文档
db.articles.insertOne({
  "_id": "5f262d6aa01dd6a49a6fa8d6",
  "title": "MongoDB 存储示例",
  "content": "这是一篇使用 MongoDB 存储数据的示例。",
  "tags": ["MongoDB", "数据库", "NoSQL"],
  "createdDate": ISODate("2023-03-31T09:34:02.765Z")
})
```

这样就创建了一个名为 "articles" 的集合，并插入了一个文档。

### 查询

MongoDB 提供了丰富的查询语法，可以方便地对文档进行查询。以下是一些查询示例：

- 查询集合中的所有文档

```shell
db.articles.find()
```

- 查询 title 字段中包含 "MongoDB" 关键字的文档

```shell
db.articles.find({ "title": /MongoDB/ })
```

- 查询 createdDate 字段大于指定日期的文档

```shell
db.articles.find({ "createdDate": { "$gte": ISODate("2021-01-01T00:00:00.000Z") } })
```

以上仅是 MongoDB 数据存储的基本概念和操作方法。

# MongoDB CRUD Java 操作

## 在 Java 应用程序中插入文档

查看下面的代码，其中演示了如何将单个文档和多个文档插入到集合中。

### 插入单个文档

将单个文档插入集合中（如果数据库和集合不存在，会自动创建）：

- 使用 `getCollection()` 方法访问 `MongoCollection` 对象，该对象用于表示指定的集合
- 将 `insertOne()` 方法附加到 `collection` 对象
- 在 `insertOne()` 的括号内，包含一个包含文档数据的对象
- 打印出插入文档的 id

```java
public class CrudTests {

    // 格式：[jdbc:]mongodb[+srv]://[{user:identifier}[:{password:param}]@]<\,,{host::localhost}?[:{port::27017}]>[/{database}?[\?<&,{:identifier}={:param}>]]
    private static final String connectString = "mongodb://root:root@localhost:27017";

    // ...

    @Test
    void testInsertOne() {
        try (MongoClient mongoClient = MongoClients.create(connectString)) {
            final MongoDatabase database = mongoClient.getDatabase("sample_training");
            final MongoCollection<Document> collection = database.getCollection("inspections");

            final Document inspection = new Document("_id", new ObjectId())
                    .append("id", "10021-2015-ENFO")
                    .append("certificate_number", 9278806)
                    .append("business_name", "ATLIXCO DELI GROCERY INC.")
                    .append("date", Date.from(LocalDate.of(2015, 2, 20).atStartOfDay(ZoneId.systemDefault()).toInstant()))
                    .append("result", "No Violation Issued")
                    .append("sector", "Cigarette Retail Dealer - 127")
                    .append("address", new Document().append("city", "RIDGEWOOD").append("zip", 11385).append("street", "MENAHAN ST").append("number", 1712));
            final InsertOneResult insertOneResult = collection.insertOne(inspection);
            final BsonValue insertedId = insertOneResult.getInsertedId();
            System.out.println(insertedId);
        }
    }
}
```

输出结果如下：

```text
BsonObjectId{value=644b89cbbad0666fb329aa30}
```

### 插入多个文档

将多个文档插入到集合中：

- 将 `insertMany()` 方法附加到 `collection` 对象
- 在 `insertMany()` 的括号内，包含一个包含文档数据的对象
- 打印出插入文档的 id

```java
public class CrudTests {

    // 格式：[jdbc:]mongodb[+srv]://[{user:identifier}[:{password:param}]@]<\,,{host::localhost}?[:{port::27017}]>[/{database}?[\?<&,{:identifier}={:param}>]]
    private static final String connectString = "mongodb://root:root@localhost:27017";

    // ...

    @Test
    void testInsertMany() {
        try (MongoClient mongoClient = MongoClients.create(connectString)) {
            final MongoDatabase database = mongoClient.getDatabase("bank");
            final MongoCollection<Document> collection = database.getCollection("accounts");

            final Document doc1 = new Document().append("account_holder", "john doe").append("account_id", "MDB99115881").append("balance", 1785).append("account_type", "checking");
            final Document doc2 = new Document().append("account_holder", "jane doe").append("account_id", "MDB79101843").append("balance", 1468).append("account_type", "checking");

            final InsertManyResult insertManyResult = collection.insertMany(Arrays.asList(doc1, doc2));
            insertManyResult.getInsertedIds().forEach((k, v) -> System.out.println(k + " -> " + v.asObjectId()));
        }
    }
}
```

## 在 Java 应用程序中查询 MongoDB 的 Collection

查看下面的代码，它演示了如何使用 Java 在 MongoDB 中查询文档。

### 使用 `find()`

在下面的示例中，我们找到**余额大于或等于 1000 并且是支票帐户**的所有帐户。我们通过迭代 `MongoCursor` 来处理从 `find()`方法返回的每个文档。`find()` 方法接受查询筛选器并返回与集合中的筛选器匹配的文档。

```java
public class CrudTests {

    // 格式：[jdbc:]mongodb[+srv]://[{user:identifier}[:{password:param}]@]<\,,{host::localhost}?[:{port::27017}]>[/{database}?[\?<&,{:identifier}={:param}>]]
    private static final String connectString = "mongodb://root:root@localhost:27017";
    
    // ...
    
    @Test
    void testFind() {
        try (MongoClient mongoClient = MongoClients.create(connectString)) {
            MongoDatabase database = mongoClient.getDatabase("bank");
            MongoCollection<Document> collection = database.getCollection("accounts");
            try (MongoCursor<Document> cursor = collection.find(Filters.and(Filters.gte("balance", 1000), Filters.eq("account_type", "checking")))
                    .iterator()) {
                while (cursor.hasNext()) {
                    System.out.println(cursor.next().toJson());
                }
            }
        }
    }
}
```

输出结果如下：

```text
{"_id": {"$oid": "644b7a20d633f972e812254b"}, "account_holder": "john doe", "account_id": "MDB99115881", "balance": 1785, "account_type": "checking"}
{"_id": {"$oid": "644b7a20d633f972e812254c"}, "account_holder": "jane doe", "account_id": "MDB79101843", "balance": 1468, "account_type": "checking"}
```

### 使用 `find().first()`

将 `find()` 和 `first()` 方法链接在一起，为传递给 `find()` 方法的查询过滤器查找第一个匹配的文档。在下面的示例中，我们从相同的查询返回一个文档。

```java
public class CrudTests {

    // 格式：[jdbc:]mongodb[+srv]://[{user:identifier}[:{password:param}]@]<\,,{host::localhost}?[:{port::27017}]>[/{database}?[\?<&,{:identifier}={:param}>]]
    private static final String connectString = "mongodb://root:root@localhost:27017";

    // ...

    @Test
    void testFindFirst() {
        try (MongoClient mongoClient = MongoClients.create(connectString)) {
            MongoDatabase database = mongoClient.getDatabase("bank");
            MongoCollection<Document> collection = database.getCollection("accounts");
            final Document document = collection.find(Filters.and(Filters.gte("balance", 1000), Filters.eq("account_type", "checking"))).first();
            assert document != null;
            System.out.println(document.toJson());
        }
    }
}
```

输出结果如下：

```text
{"_id": {"$oid": "644b7a20d633f972e812254b"}, "account_holder": "john doe", "account_id": "MDB99115881", "balance": 1785, "account_type": "checking"}
```

## 在 Java 应用程序中更新文档

查看下面的代码，它演示了如何用 Java 更新 MongoDB 中的文档。

### 使用 `updateOne()`

为了更新单个文档，我们在一个 `MongoCollection` 对象上使用 `updateOne()` 方法。该方法接受与我们想要更新的文档匹配的过滤器，以及指示驱动程序如何更改匹配文档的更新语句。`updateOne()` 方法只更新与过滤器匹配的第一个文档。

在下面的示例中，我们通过将特定帐户的余额增加 100 并将帐户状态设置为活动来更新一个文档：

```java
public class CrudTests {

    // 格式：[jdbc:]mongodb[+srv]://[{user:identifier}[:{password:param}]@]<\,,{host::localhost}?[:{port::27017}]>[/{database}?[\?<&,{:identifier}={:param}>]]
    private static final String connectString = "mongodb://root:root@localhost:27017";

    // ...

    @Test
    void testUpdateOne() {
        try (MongoClient mongoClient = MongoClients.create(connectString)) {
            MongoDatabase database = mongoClient.getDatabase("bank");
            MongoCollection<Document> collection = database.getCollection("accounts");
            Bson query = Filters.eq("account_id", "MDB79101843");
            Bson updates = Updates.combine(Updates.set("account_status", "active"), Updates.inc("balance", 100));
            UpdateResult upResult = collection.updateOne(query, updates);
            System.out.println(upResult);
        }
    }
}
```

输出结果如下：

```text
AcknowledgedUpdateResult{matchedCount=1, modifiedCount=1, upsertedId=null}
```

### 使用 `updateMany()`

为了更新多个文档，我们在一个 `MongoCollection` 对象上使用 `updateMany()` 方法。该方法接受与我们想要更新的文档匹配的过滤器，以及指示驱动程序如何更改匹配文档的更新语句。`updateMany()` 方法更新集合中与过滤器匹配的所有文档。

在下面的示例中，我们通过向所有储蓄账户添加 100 的最低余额来更新许多文档：

```java
public class CrudTests {

    // 格式：[jdbc:]mongodb[+srv]://[{user:identifier}[:{password:param}]@]<\,,{host::localhost}?[:{port::27017}]>[/{database}?[\?<&,{:identifier}={:param}>]]
    private static final String connectString = "mongodb://root:root@localhost:27017";

    // ...

    @Test
    void testUpdateMany() {
        try (MongoClient mongoClient = MongoClients.create(connectString)) {
            MongoDatabase database = mongoClient.getDatabase("bank");
            MongoCollection<Document> collection = database.getCollection("accounts");
            Bson query = Filters.eq("account_type", "checking");
            Bson updates = Updates.combine(Updates.set("minimum_balance", 100));
            UpdateResult upResult = collection.updateMany(query, updates);
            System.out.println(upResult);
        }
    }
}
```

输出结果如下：

```text
AcknowledgedUpdateResult{matchedCount=2, modifiedCount=1, upsertedId=null}
```

## 在 Java 应用程序中删除文档

查看下面的代码，它演示了如何用 Java 删除 MongoDB 中的文档。

### 使用 `deleteOne()`

为了从集合中删除单个文档，我们在一个 `MongoCollection` 对象上使用 `deleteOne()` 方法。该方法接受与我们要删除的文档匹配的查询筛选器。如果我们不指定过滤器，MongoDB 将匹配集合中的第一个文档。`deleteOne()` 方法只删除第一个匹配的文档。

在下面的例子中，我们删除了一个属于 John Doe 账户的文档：

```java
public class CrudTests {

    // 格式：[jdbc:]mongodb[+srv]://[{user:identifier}[:{password:param}]@]<\,,{host::localhost}?[:{port::27017}]>[/{database}?[\?<&,{:identifier}={:param}>]]
    private static final String connectString = "mongodb://root:root@localhost:27017";

    // ...

    @Test
    void testDeleteOne() {
        try (MongoClient mongoClient = MongoClients.create(connectString)) {
            MongoDatabase database = mongoClient.getDatabase("bank");
            MongoCollection<Document> collection = database.getCollection("accounts");
            Bson query = Filters.eq("account_holder", "john doe");
            DeleteResult delResult = collection.deleteOne(query);
            System.out.println("Deleted a document:\t" + delResult.getDeletedCount());
        }
    }
}
```

输出结果如下：

```text
Deleted a document:	1
```

### 对查询对象使用 `deleteMany()`

要在一次操作中从一个集合中删除多个文档，我们在一个 `MongoCollection` 对象上调用 `deleteMany()` 方法。为了指定要删除哪些文档，我们传递了一个查询过滤器，该过滤器与我们想要删除的文档相匹配。如果我们提供一个空文档，MongoDB 将匹配集合中的所有文档并删除它们。

为了演示批量删除的数据过程，我这里准备了一些测试的 JSON 数据，可以参考 [accounts_documents_template](./../scripts/accounts_documents_template.json) 案例数据。

在下面的示例中，我们使用查询对象删除 `state` 为 "TN" 帐户。然后，我们打印被删除文档的总数。

```java
public class CrudTests {

    // 格式：[jdbc:]mongodb[+srv]://[{user:identifier}[:{password:param}]@]<\,,{host::localhost}?[:{port::27017}]>[/{database}?[\?<&,{:identifier}={:param}>]]
    private static final String connectString = "mongodb://root:root@localhost:27017";

    // ...

    @Test
    void testDeleteManyWithQueryObject() {
        try (MongoClient mongoClient = MongoClients.create(connectString)) {
            MongoDatabase database = mongoClient.getDatabase("bank");
            MongoCollection<Document> collection = database.getCollection("accounts_doc_template");
            Bson query = Filters.eq("state", "TN");
            DeleteResult delResult = collection.deleteMany(query);
            System.out.println("Deleted document‘s counts are:\t" + delResult.getDeletedCount());
        }
    }
}
```

输出结果如下：

```text
Deleted document‘s counts are:	25
```

### 使用带有查询过滤器的 `deleteMany()`

要在一次操作中从一个集合中删除多个文档，我们在一个 `MongoCollection` 对象上调用 `deleteMany()` 方法。为了指定要删除哪些文档，我们传递了一个查询过滤器，该过滤器与我们想要删除的文档相匹配。如果我们提供一个空文档，MongoDB 将匹配集合中的所有文档并删除它们。

在下面的示例中，我们使用查询对象删除 `state` 为 "VA" 帐户。然后，我们打印被删除文档的总数。

```java
public class CrudTests {

    // 格式：[jdbc:]mongodb[+srv]://[{user:identifier}[:{password:param}]@]<\,,{host::localhost}?[:{port::27017}]>[/{database}?[\?<&,{:identifier}={:param}>]]
    private static final String connectString = "mongodb://root:root@localhost:27017";

    // ...

    @Test
    void testDeleteManyWithQueryFilter() {
        try (MongoClient mongoClient = MongoClients.create(connectString)) {
            MongoDatabase database = mongoClient.getDatabase("bank");
            MongoCollection<Document> collection = database.getCollection("accounts_doc_template");
            DeleteResult delResult = collection.deleteMany(Filters.eq("state", "VA"));
            System.out.println("Deleted document‘s counts are:\t" + delResult.getDeletedCount());
        }
    }
}
```

输出结果如下：

```text
Deleted document‘s counts are:	16
```

> **说明**
> 
> 关于文档批量删除，对查询对象使用 `deleteMany()` 和使用带有查询过滤器的 `deleteMany()` 本质上是一样的，只是叫法不一样而已。


## 在 Java 应用中创建 MongoDB 事务

查看下面的代码，它演示了如何用 Java 在 MongoDB 中创建多文档事务。

### 创建事务

为了启动事务，我们使用会话的 `WithTransaction()` 方法。然后，我们定义要在事务内部执行的操作序列。以下是完成多文档事务的步骤，然后是代码：

1. 开始一个新的会话；
2. 使用会话上的 `WithTransaction()` 方法开始事务；
3. 创建将在事务中使用的变量；
4. 获取将在事务中使用的用户帐户信息；
5. 创建 `transfers` 文档；
6. 更新用户帐号；
7. 插入 `transfer` 文档；
8. 提交事务。

为了模拟转账的事务流程，请添加对应的测试数据，可以参考：[accounts](./../scripts/accounts.json) 案例数据，下面的代码演示了这些步骤：

```java
public class CrudTests {

    // 格式：[jdbc:]mongodb[+srv]://[{user:identifier}[:{password:param}]@]<\,,{host::localhost}?[:{port::27017}]>[/{database}?[\?<&,{:identifier}={:param}>]]
    private static final String txConnectString = "mongodb://localhost:27020/retryWrites=false";

    // ...

    @Test
    void testTransaction() {
        try (MongoClient mongoClient = MongoClients.create(txConnectString)) {
            final ClientSession clientSession = mongoClient.startSession();

            final TransactionBody<String> transactionBody = () -> {
                MongoCollection<Document> bankingCollection = mongoClient.getDatabase("bank").getCollection("accounts_test");

                // 提取
                Bson fromAccount = Filters.eq("account_id", "MDB310054629");
                Bson withdrawal = Updates.inc("balance", -200);

                // 存入
                Bson toAccount = Filters.eq("account_id", "MDB643731035");
                Bson deposit = Updates.inc("balance", 200);

                System.out.println("This is from Account " + fromAccount.toBsonDocument().toJson() + " withdrawn " + withdrawal.toBsonDocument().toJson());
                System.out.println("This is to Account " + toAccount.toBsonDocument().toJson() + " deposited " + deposit.toBsonDocument().toJson());
                bankingCollection.updateOne(clientSession, fromAccount, withdrawal);
                bankingCollection.updateOne(clientSession, toAccount, deposit);

                return "Transferred funds from John Doe to Mary Doe";
            };

            try {
                // 开启事务
                clientSession.withTransaction(transactionBody);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                clientSession.close();
            }
        }
    }
}
```

输出结果如下：

```text
This is from Account {"account_id": "MDB310054629"} withdrawn {"$inc": {"balance": -200}}
This is to Account {"account_id": "MDB643731035"} deposited {"$inc": {"balance": 200}}
com.mongodb.MongoClientException: This MongoDB deployment does not support retryable writes. Please add retryWrites=false to your connection string.
...

Caused by: com.mongodb.MongoCommandException: Command failed with error 20 (IllegalOperation): 'Transaction numbers are only allowed on a replica set member or mongos' on server localhost:27017. The full response is {"ok": 0.0, "errmsg": "Transaction numbers are only allowed on a replica set member or mongos", "code": 20, "codeName": "IllegalOperation"}
```

从输出结果看出，事务发生了异常，经过搜索查询得出 “单节点 mongo 是不支持事务的，所以需要配置 mongo 副本集（Replica Set）”。有如下 2 种解决方案：

1. 配置集群分片（Sharding）模式，不要使用单节点；
2. 为单节点配置副本集（Replica Set）；

具体参考文档：[mongodb环境搭建指南](mongodb环境搭建指南.md)中的『基于 Docker 搭建 MongoDB 集群』内容。

如果完成集群搭建后输出结果如下：

```text
This is from Account {"account_id": "MDB310054629"} withdrawn {"$inc": {"balance": -200}}
This is to Account {"account_id": "MDB643731035"} deposited {"$inc": {"balance": 200}}
```

根据输出的结果可以看出，对应的数据有执行成功！

> **注意**
> 
> `Updates.inc` 原子性新增方法使用的字段类型不能为 `String` 类型，否则提示错误！
