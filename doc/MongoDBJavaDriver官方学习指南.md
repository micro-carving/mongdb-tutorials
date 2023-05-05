# MongoDB Java Driver 文档（V4.9.0）

## 各版本新特性

### 4.9

- 添加了一个新的异常，用于标识由 `BsonCreator` 注释的构造函数除了包含具有 `BsonProperty` 或 `BsonId` 以外注释的参数。
- 修改了 `org.mongodb.driver.protocol.command` 包上报的命令监控消息的日志格式。
- 向 `Aggregate` 帮助类添加了对 `$documents` 聚合管道阶段的支持。
- 添加了 `MongoClientSettings.Builder.applyToLoggerSettings()` 和 `LoggerSettings.Builder.maxDocumentLength()` 方法，用于在日志消息中为 `BSON` 文档的扩展 `JSON` 表示指定最大长度

其他特性参考链接：[whats-new](https://mongodb.github.io/mongo-java-driver/4.9/whats-new/)

## Java 驱动

### 简介

欢迎访问 Java 驱动程序的文档网站，Java 驱动程序是用于同步 Java 应用程序的官方 MongoDB 驱动程序。使用 Maven 或 Gradle 下载，或者按照我们的快速入门指南设置一个可运行的项目。

如果 Java 应用程序需要异步流处理，请使用 Reactive Streams Driver，该驱动程序使用 ReactiveStreams 对 MongoDB 进行非阻塞调用。

### 快速开始

#### 介绍

本指南展示了如何创建一个使用 Java 驱动程序连接到 MongoDB Atlas 集群的应用程序。

Java 驱动程序允许从 Java 应用程序连接到 MongoDB 集群并与之通信。

MongoDB Atlas 是一个完全托管的云数据库服务，在 MongoDB 集群上托管你的数据。在本指南中，我们将向你展示如何开始使用你自己的免费（无需支付）集群。

请参阅以下步骤，将 Java 应用程序与 MongoDB Atlas 集群连接起来。

#### 设置项目

##### 安装 Java 开发工具（JDK）

请确保你的系统安装了 JDK 8 或更高版本。有关如何检查 Java 版本并安装 JDK 的更多信息，请参阅 [Oracle JDK 安装文档概述](https://www.oracle.com/java/technologies/javase-downloads.html)。

##### 创建项目

本指南向你展示如何使用 Maven 或 Gradle 添加 MongoDB Java 驱动程序依赖项。我们建议你使用集成开发环境（IDE），如 Intellij IDEA 或 Eclipse IDE，这样可以更方便地配置 Maven 或 Gradle 来构建和运行你的项目。

如果你没有使用 IDE，请参阅 [Building Maven](https://maven.apache.org/guides/development/guide-building-maven.html) 或 [Creating New Gradle Builds](https://guides.gradle.org/creating-new-gradle-builds/)，了解有关如何设置项目的更多信息。

##### 将 MongoDB 添加为依赖项

如果你正在使用 Maven，请将以下内容添加到 `pom.xml` 依赖项列表中：

```xml
<dependencies>
    <dependency>
        <groupId>org.mongodb</groupId>
        <artifactId>mongodb-driver-sync</artifactId>
        <version>4.9.1</version>
    </dependency>
</dependencies>
```

如果你正在使用 Gradle，请将以下内容添加到你的 `build.gradle` 依赖项列表中：

```text
dependencies {
  implementation 'org.mongodb:mongodb-driver-sync:4.9.1'
}
```

一旦配置了依赖项，请确保它们可用于你的项目，这可能需要运行依赖项管理器并在 IDE 中刷新项目。

#### 创建 MongoDB 集群

##### 在 Atlas 中建立一个免费分层集群

在设置 Java 项目依赖项之后，创建一个 MongoDB 集群，你可以在其中存储和管理数据。完成 [Atlas 入门](https://www.mongodb.com/docs/atlas/getting-started/?jmp=docs_driver_java)指南，设置一个新的 [Atlas 帐户](https://account.mongodb.com/account/register?tck=docs_atlas&_ga=2.85896410.238876901.1683168999-176447065.1683168999)，创建并启动一个免费层 MongoDB 集群，加载数据集，并与数据交互。

完成 Atlas 指南中的步骤后，你应该在 Atlas 中部署了一个新的 MongoDB 集群、一个新的数据库用户以及将示例数据集加载到集群中。

##### 连接到集群

在这一步中，我们创建并运行一个应用程序，该应用程序使用 MongoDB Java 驱动程序连接到 MongoDB 集群，并对样本数据运行查询。

我们在一个名为连接字符串的字符串中向驱动程序传递如何连接到 MongoDB 集群的说明。此字符串包括有关群集的主机名或 IP 地址和端口、身份验证机制、用户凭据（如果适用）以及其他连接选项的信息。

如果你正在连接到非 Atlas 托管的实例或集群，请参阅[连接到 MongoDB 的其他方法](https://www.mongodb.com/docs/drivers/java/sync/current/fundamentals/connection/connect/#std-label-java-other-ways-to-connect)，以获取有关如何格式化连接字符串的说明。

要检索你在上一步中创建的集群和用户的连接字符串，请登录你的 Atlas 帐户并导航到**数据库**部分，然后单击要连接到的集群的**连接**，如下所示：

![AtlasMongoDBCluster](./assets/AtlasMongoDBCluster.png)

继续 “Connect to your application” 步骤，然后选择 Java 驱动程序。选择 “4.3 or later” 作为版本。单击 “复制” 图标将连接字符串复制到剪贴板，如下所示：

![AtlasMongoDBClusterConnectDB](./assets/AtlasMongoDBClusterConnectDB.png)

将 Atlas 连接字符串保存在一个安全的位置，以便你可以访问该位置以进行下一步操作。

##### 从应用程序查询 MongoDB 集群

接下来，在项目的基本包目录中创建一个名为 `QuickStart.java` 的文件来包含你的应用程序。使用以下示例代码在 MongoDB Atlas 中对你的示例数据集运行查询，将 `uri` 变量的值替换为 MongoDB Atlas 连接字符串。

```java
import static com.mongodb.client.model.Filters.eq;

import org.bson.Document;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class QuickStart {
    public static void main( String[] args ) {

        // Replace the placeholder with your MongoDB deployment's connection string
        String uri = "<connection string uri>";

        try (MongoClient mongoClient = MongoClients.create(uri)) {
            MongoDatabase database = mongoClient.getDatabase("sample_mflix");
            MongoCollection<Document> collection = database.getCollection("movies");

            Document doc = collection.find(eq("title", "Back to the Future")).first();
            if (doc != null) {
                System.out.println(doc.toJson());
            } else {
                System.out.println("No matching documents found.");
            }
        }
    }
}
```

当你运行 `QuickStart` 类时，它应该从示例数据集中输出电影的详细信息，该数据集看起来如下：

```text
{
  _id: ...,
  plot: 'A young man is accidentally sent 30 years into the past...',
  genres: [ 'Adventure', 'Comedy', 'Sci-Fi' ],
  ...
  title: 'Back to the Future',
  ...
}
```

如果没有收到输出或错误，请检查 Java 类中是否包含了正确的连接字符串，以及是否将示例数据集加载到 MongoDB Atlas 集群中。

> **重要**
> 
> **使用 TLS v1.3 时的已知连接问题**
> 
> 如果在运行应用程序时，连接到 MongoDB 实例或集群时遇到类似以下情况的错误，你可能需要将 JDK 更新到最新的补丁版本：
> 
> `javax.net.ssl.SSLHandshakeException: extension (5) should not be presented in certificate_request`
> 
> 当将 TLS 1.3 协议与特定版本的 JDK 一起使用时，此异常是一个已知问题，但在以下版本中已修复：
> 
> - JDK 11.0.7 
> - JDK 13.0.3 
> - JDK 14.0.2
> 
> 要解决此错误，请将 JDK 更新到以前的修补程序版本或更新的版本。

完成这一步后，你应该有一个使用 Java 驱动程序连接到 MongoDB 集群的工作应用程序，对样本数据运行查询，并打印出结果。

##### 使用 POJO（可选）

在上一节中，你对示例集合运行了一个查询，以检索类似映射的类 `Document` 中的数据。在本节中，你可以学习使用自己的 Plain Old Java Object（POJO）来存储和检索 MongoDB 中的数据。

在项目的基本包目录中创建一个名为 `Movie.java` 的文件，并为包含以下字段、setter 和 getter 的类添加以下代码：

```java
public class Movie {
    String plot;
    List<String> genres;
    String title;

    public String getPlot() {
        return plot;
    }
    public void setPlot(String plot) {
        this.plot = plot;
    }
    public List<String> getGenres() {
        return genres;
    }
    public void setGenres(List<String> genres) {
        this.genres = genres;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    @Override
    public String toString() {
        return "Movie [\n  plot=" + plot + ",\n  genres=" + genres + ",\n  title=" + title + "\n]";
    }
}
```

在与项目中的 `Movie` 文件相同的包目录中创建一个新文件 `QuickStartPojoExample.java` 。使用以下示例代码在 MongoDB Atlas 中对你的示例数据集运行查询，将 `uri` 变量的值替换为 MongoDB Atlas 连接字符串。请确保将连接字符串的 “<password>” 部分替换为你为具有 **atlasAdmin** 权限的用户创建的密码：

```java
import static com.mongodb.MongoClientSettings.getDefaultCodecRegistry;
import static com.mongodb.client.model.Filters.eq;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class QuickStartPojoExample {

    public static void main(String[] args) {
        CodecProvider pojoCodecProvider = PojoCodecProvider.builder().automatic(true).build();
        CodecRegistry pojoCodecRegistry = fromRegistries(getDefaultCodecRegistry(), fromProviders(pojoCodecProvider));

        // Replace the uri string with your MongoDB deployment's connection string
        String uri = "<connection string uri>";

        try (MongoClient mongoClient = MongoClients.create(uri)) {
            MongoDatabase database = mongoClient.getDatabase("sample_mflix").withCodecRegistry(pojoCodecRegistry);
            MongoCollection<Movie> collection = database.getCollection("movies", Movie.class);

            Movie movie = collection.find(eq("title", "Back to the Future")).first();
            System.out.println(movie);
        }
    }
}
```

当你运行 `QuickStartPojoExample` 类时，它应该从示例数据集中输出电影的详细信息，该数据集应该如下所示：

```text
Movie [
  plot=A young man is accidentally sent 30 years into the past...,
  genres=[Adventure, Comedy, Sci-Fi],
  title=Back to the Future
]
```

如果没有收到输出或错误，请检查 Java 类中是否包含了正确的连接字符串，以及是否将示例数据集加载到 MongoDB Atlas 集群中。

有关使用 POJO 存储和检索数据的更多信息，请参阅以下链接：

- [使用 POJO 存储和检索数据指南](https://www.mongodb.com/docs/drivers/java/sync/current/fundamentals/data-formats/document-data-format-pojo/)
- [POJO 自定义序列化指南](https://www.mongodb.com/docs/drivers/java/sync/current/fundamentals/data-formats/pojo-customization/)

#### 下一步

在基础 CRUD 指南中学习如何使用 Java 驱动程序读取和修改数据，或者在用法示例中学习如何执行常见操作。

### 快速参考

本页显示了几个 MongoDB 命令的驱动程序语法，并链接到相关的参考和 API 文档。

- 查询单个文档

`coll.find(Filters.eq("title", "Hamlet")).first();`

```text
{ title: 'Hamlet', type: 'movie', ... }
```

- 查询多个文档

`coll.find(Filters.eq("year", 2005))`

```text
[
  { title: 'Christmas in Boston', year: 2005, ... },
  { title: 'Chicken Little', year: 2005, ... },
  ...
]
```

- 插入单个文档

`coll.insertOne(new Document("title", "Jackie Robinson"));`

- 插入多个文档

```shell
coll.insertMany(
    Arrays.asList(
            new Document("title", "Dangal").append("rating", "Not Rated"),
            new Document("title", "The Boss Baby").append("rating", "PG")));
```

- 更新单个文档

```shell
coll.updateOne(
        Filters.eq("title", "Amadeus"),
        Updates.set("imdb.rating", 9.5));
```

```text
{ title: 'Amadeus', imdb: { rating: 9.5, ... } }
```

- 更新多个文档

```shell
coll.updateMany(
        Filters.eq("year", 2001),
        Updates.inc("imdb.votes", 100));
```

```text
[
  { title: 'A Beautiful Mind', year: 2001, imdb: { votes: 826257, ... },
  { title: 'Shaolin Soccer', year: 2001, imdb: { votes: 65442, ... },
  ...
]
```

- 在文档中更新数组

```shell
coll.updateOne(
        Filters.eq("title", "Cosmos"),
        Updates.push("genres", "Educational"));
```

```text
{ title: 'Cosmos', genres: [ 'Documentary', 'Educational' ], ...}
```

- 文档替换

```shell
coll.replaceOne(
        Filters.and(Filters.eq("name", "Deli Llama"), Filters.eq("address", "2 Nassau St")),
        new Document("name", "Lord of the Wings").append("zipcode", 10001));
```

```text
{ name: 'Lord of the Wings', zipcode: 10001 }
```

- 删除单个文档

```text
coll.deleteOne(Filters.eq("title", "Congo"));
```

- 删除多个文档

```shell
coll.deleteMany(Filters.regex("title", "^Shark.*"));
```

- 批量写入

```shell
coll.bulkWrite(
        Arrays.asList(
               new InsertOneModel<Document>(
                       new Document().append("title", "A New Movie").append("year", 2022)),
               new DeleteManyModel<Document>(
                       Filters.lt("year", 1970))));
```

- 查看变更

```shell
coll.watch(Arrays.asList(
        Aggregates.match(Filters.gte("year", 2022))));
```

- 通过游标迭代器访问数据

```shell
MongoCursor<Document> cursor = coll.find().cursor();
while (cursor.hasNext()) {
    System.out.println(cursor.next().toJson());
}
```

```text
[
  { title: '2001: A Space Odyssey', ... },
  { title: 'The Sound of Music', ... },
  ...
]
```

- 以数组形式访问查询的结果

```shell
List<Document> resultList = new ArrayList<Document>();
coll.find().into(resultList);
```

```text
[
  { title: '2001: A Space Odyssey', ... },
  { title: 'The Sound of Music', ... },
  ...
]
```

- 文档计数

```shell
coll.countDocuments(Filters.eq("year", 2000));
```

```text
618
```

- 列出不同的文档或字段值

```shell
coll.distinct("year", Integer.class);
```

```text
[ 1891, 1893, 1894, 1896, 1903, ... ]
```

- 限制检索文档的数量

```shell
coll.find().limit(2);
```

```text
[
  { title: 'My Neighbor Totoro', ... },
  { title: 'Amélie', ... }
]
```

- 跳过检索文档

```shell
coll.find(Filters.regex("title", "^Rocky")).skip(2);
```

```text
[
  { title: 'Rocky III', ... },
  { title: 'Rocky IV', ... },
  { title: 'Rocky V', ... }
]
```

- 检索文档时对文档进行排序

```shell
coll.find().sort(Sorts.ascending("year"));
```

```text
[
  { title: 'Newark Athlete', year: 1891, ... },
  { title: 'Blacksmith Scene', year: 1893, ...},
  { title: 'Dickson Experimental Sound Film', year: 1894},
  ...
]
```

- 检索项目文档字段

```shell
coll.find().projection(Projections.fields(
       Projections.excludeId(),
       Projections.include("year", "imdb")));
```

```text
[
  { year: 2012, imdb: { rating: 5.8, votes: 230, id: 8256 }},
  { year: 1985, imdb: { rating: 7.0, votes: 447, id: 1654 }},
  ...
]
```

- 创建索引

```shell
coll.createIndex(
        Indexes.compoundIndex(
                Indexes.ascending("title"),
                Indexes.descending("year")));
```

- 文本搜索

```shell
// only searches fields with text indexes
coll.find(Filters.text("zissou"));
```

```text
[
  { title: 'The Life Aquatic with Steve Zissou', ... }
]
```

- 用 Maven 安装驱动依赖

```xml
<dependencies>
  <dependency>
    <groupId>org.mongodb</groupId>
    <artifactId>mongodb-driver-sync</artifactId>
    <version>4.9.1</version>
  </dependency>
</dependencies>
```

- 用 Gradle 安装驱动依赖

```gradle
dependencies {
  implementation 'org.mongodb:mongodb-driver-sync:4.9.1'
}
```

## 响应式流

## BSON

## 构建器

## 参考或引用

- [https://mongodb.github.io/mongo-java-driver/](https://mongodb.github.io/mongo-java-driver/)


