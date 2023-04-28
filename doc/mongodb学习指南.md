# MongoDB 概述

## MongoDB 是什么？

MongoDB 是一个文档数据库，旨在简化应用程序的开发和扩展。

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
