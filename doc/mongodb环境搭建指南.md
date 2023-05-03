# DockerDesktop 版本（win10）

## 下载 MongoDB

```shell
docker pull mongo:6.0.5
```

![win10下的docker拉取mongodb镜像](./assets/win10下的docker拉取mongodb镜像.png)

## 启动 mongodb 容器

### 创建挂载目录

![创建docker挂载目录](./assets/创建docker挂载目录.png)

**config**：用于放置配置相关的目录

**data**：用于放置产生的数据的目录

### 新增 `mongod.conf` 配置文件

在 **config** 目录下面新建 `mongod.conf` 配置文件（这里的 `host: 0.0.0.0` 中间是有空格的。如果无，启动失败），文件内容如下：

这里放置的为基础配置内容，参考：[https://www.mongodb.com/docs/manual/administration/configuration/#std-label-base-config](https://www.mongodb.com/docs/manual/administration/configuration/#std-label-base-config)

```text
processManagement:
   fork: true
net:
   bindIp: 0.0.0.0
   port: 27017
storage:
   dbPath: /var/lib/mongo
systemLog:
   destination: file
   path: "/var/log/mongodb/mongod.log"
   logAppend: true
storage:
   journal:
      enabled: true
```

### 单节点启动

```shell
# 创建 docker 网络
docker network create mongodb

# 启动
docker run --name mongodb --network mongodb -p 27017:27017  -e MONGO_INITDB_ROOT_USERNAME=root -e MONGO_INITDB_ROOT_PASSWORD=root -v D:/DockerDesktop/docker-container-workspace/mongodb/config/mongod.conf:/etc/mongo/mongod.conf -v D:/DockerDesktop/docker-container-workspace/mongodb/data:/data/db -d mongo:6.0.5
```

> **说明**
>
> `--name mongodb`：容器名称为 mongodb </br>
> `--network mongodb`：连接容器网络 </br>
> `-p 27017:27017`：将容器的服务端口映射到宿主机端口（宿主机端口:容器端口）</br>
> `-e MONGO_INITDB_ROOT_USERNAME=root`：创建一个用户名为 **root** 的用户 </br>
> `-e MONGO_INITDB_ROOT_PASSWORD=root`：用户名为 **root** 的密码为 **root** </br>
> `-v xxx/config/mongod.conf:/etc/mongo/mongod.conf`：容器启动配置文件挂载，将本地的配置文件路径映射到容器的配置路径 </br>
> `-v xxx/data:/data/db`：容器启动数据目录挂载，容器数据自动映射到本地数据目录 </br>
> `-d mongo:6.0.5`：指定版本的镜像（这里是 mongo:6.0.5）以容器方式在后台启动 </br>


docker 启动方式参考：[https://hub.docker.com/_/mongo](https://hub.docker.com/_/mongo)

### 将 mongodb 容器设置为自启动

```shell
docker update --restart=always mongodb
```

### 进入容器

- 非认证用户进入容器

```shell
# 进入容器
docker exec -it mongodb mongosh
```

![进入mongodb容器](./assets/进入mongodb容器.png)

> **注意**
>
> 假如在执行 `show collections;` 命令，控制台提示 **MongoServerError: command listCollections requires authentication**
> 异常信息，一般解决方案是未进行身份认证。有如下 2 种解决方案：
>
> - 通过 `docker exec -it mongodb mongosh -u [用户名] -p [用户密码]` 方式进入容器并重新执行命令；
> - 为该对应的数据库 `admin` 中通过创建一个用户，并赋予用户 `root` 权限，命令如下：
> ```shell
> # 进入admin数据库 use admin 
> # 创建一个超级用户 db.createUser({user:"root",pwd:"123456",roles:[{role:"root",db:"admin"}]});
> # 授权登录 db.auth('root','123456')
> ```

- 认证用户进入容器

```shell
# 进入容器
docker exec -it mongodb mongosh -u root -p root
```

### 验证是否启动成功

通过可视化工具（我这里使用的 **Navicat**，当然也可以使用 **MongoDB Compass**，推荐使用 **MongoDB Compass** 后续方便 JSON 批量测试数据导入）连接 mongodb
进行测试，如出现截图信息表示启动成功！

- Navicat 下连接

![Navicat下验证mongodb启动是否成功](./assets/Navicat下验证mongodb启动是否成功.png)

- MongoDB Compass 下连接

![MongoDBCompass下验证mongodb启动是否成功](./assets/MongoDBCompass下验证mongodb启动是否成功.png)

### 停止或者删除容器

```shell
# 停止容器
docker stop mongodb

# 删除网络配置和容器
docker network rm mongodb
docker rm mongodb
```

# Docker 命令行版本（CentOS 7）

由于和 win10 下面的 docker 命令差不多，这里不再过多赘述，只贴出命令汇总，如下所示：

```shell
# 拉取 6.0.5 版本的官方镜像
docker pull mongo:6.0.5

# 创建文件目录
mkdir -p /home/你的用户名/docker/mongodb/config
mkdir -p /home/你的用户名/docker/mongodb/data

# 或者
mkdir -p /home/你的用户名/docker/mongodb/{config,data}

# 赋予文件权限，防止容器启动失败
chmod -R 777 /home/你的用户名/docker/mongodb/

# 新增如下内容到 /home/你的用户名/docker/mongodb/config/mongod.conf 配置文件
processManagement:
   fork: true
net:
   bindIp: 0.0.0.0
   port: 27017
storage:
   dbPath: /var/lib/mongo
systemLog:
   destination: file
   path: "/var/log/mongodb/mongod.log"
   logAppend: true
storage:
   journal:
      enabled: true

# 创建 docker 网络
docker network create mongodb

# 启动
docker run --name mongodb --network mongodb -p 27017:27017  \
-e MONGO_INITDB_ROOT_USERNAME=root  \
-e MONGO_INITDB_ROOT_PASSWORD=root  \
-v /home/你的用户名/docker/mongodb/config/mongod.conf:/etc/mongo/mongod.conf  \
-v /home/你的用户名/docker/mongodb/data:/data/db  \
-d mongo:6.0.5

# 将 mongodb 容器设置为自启动
docker update --restart=always mongodb

# 以认证用户方式进入容器
docker exec -it mongodb mongosh -u root -p root

# 停止容器
docker stop mongodb

# 删除网络配置和容器
docker network rm mongodb
docker rm mongodb
```

# 基于 Docker 搭建 MongoDB 集群

## 副本集模式（win10）

### 启动三个容器

```shell
docker run --name mongodb0 --network mongodb -p 27020:27017 -v D:/DockerDesktop/docker-container-workspace/mongodb0/data:/data/db -d mongo:6.0.5 --replSet rs0
docker run --name mongodb1 --network mongodb -p 27021:27017 -v D:/DockerDesktop/docker-container-workspace/mongodb1/data:/data/db -d mongo:6.0.5 --replSet rs0
docker run --name mongodb2 --network mongodb -p 27022:27017 -v D:/DockerDesktop/docker-container-workspace/mongodb2/data:/data/db -d mongo:6.0.5 --replSet rs0
```

### 进入主容器

```shell
docker exec -it mongodb0 bash
mongosh
```
或者
```shell
docker exec -it mongodb0 mongosh
```

输出结果如下：

```text
Current Mongosh Log ID: 645233708df8459a3e3979c5
Connecting to:          mongodb://127.0.0.1:27017/?directConnection=true&serverSelectionTimeoutMS=2000&appName=mongosh+1.8.0
Using MongoDB:          6.0.5
Using Mongosh:          1.8.0

For mongosh info see: https://docs.mongodb.com/mongodb-shell/


To help improve our products, anonymous usage data is collected and sent to MongoDB periodically (https://www.mongodb.com/legal/privacy-policy).
You can opt-out by running the disableTelemetry() command.

------
   The server generated these startup warnings when booting
   2023-05-03T10:11:21.052+00:00: Access control is not enabled for the database. Read and write access to data and configuration is unrestricted
   2023-05-03T10:11:21.052+00:00: /sys/kernel/mm/transparent_hugepage/enabled is 'always'. We suggest setting it to 'never'
   2023-05-03T10:11:21.052+00:00: vm.max_map_count is too low
------

------
   Enable MongoDB's free cloud-based monitoring service, which will then receive and display
   metrics about your deployment (disk utilization, CPU, operation statistics, etc).

   The monitoring data will be available on a MongoDB website with a unique URL accessible to you
   and anyone you share the URL with. MongoDB may use this information to make product
   improvements and to suggest MongoDB products and deployment options to you.

   To enable free monitoring, run the following command: db.enableFreeMonitoring()
   To permanently disable this reminder, run the following command: db.disableFreeMonitoring()
------
```

### 设置主节点

```shell
rs.initiate()
```

输出结果如下：

```text
{
  info2: 'no configuration specified. Using a default configuration for the set',
  me: '6e34e7bd27b2:27017',
  ok: 1
}
```

### 添加副节点

```shell
rs.add('mongodb1:27017')
rs.add({host:'mongodb2:27017', priority:0})
```

输出结果如下：

```text
rs0 [direct: other] test> rs.add('mongodb1:27017')
{
  ok: 1,
  '$clusterTime': {
    clusterTime: Timestamp({ t: 1683108741, i: 1 }),
    signature: {
      hash: Binary(Buffer.from("0000000000000000000000000000000000000000", "hex"), 0),
      keyId: Long("0")
    }
  },
  operationTime: Timestamp({ t: 1683108741, i: 1 })
}

rs0 [direct: primary] test> rs.add({host:'mongodb2:27017', priority:0})
{
  ok: 1,
  '$clusterTime': {
    clusterTime: Timestamp({ t: 1683108753, i: 1 }),
    signature: {
      hash: Binary(Buffer.from("0000000000000000000000000000000000000000", "hex"), 0),
      keyId: Long("0")
    }
  },
  operationTime: Timestamp({ t: 1683108753, i: 1 })
}
```

### 查看副本集状态

```shell
rs.status()
```

输出结果如下：

```text
rs0 [direct: primary] test> rs.status()
{
  set: 'rs0',
  date: ISODate("2023-05-03T10:13:00.743Z"),
  myState: 1,
  term: Long("1"),
  syncSourceHost: '',
  syncSourceId: -1,
  heartbeatIntervalMillis: Long("2000"),
  majorityVoteCount: 2,
  writeMajorityCount: 2,
  votingMembersCount: 3,
  writableVotingMembersCount: 3,
  optimes: {
    lastCommittedOpTime: { ts: Timestamp({ t: 1683108780, i: 1 }), t: Long("1") },
    lastCommittedWallTime: ISODate("2023-05-03T10:13:00.720Z"),
    readConcernMajorityOpTime: { ts: Timestamp({ t: 1683108780, i: 1 }), t: Long("1") },
    appliedOpTime: { ts: Timestamp({ t: 1683108780, i: 1 }), t: Long("1") },
    durableOpTime: { ts: Timestamp({ t: 1683108770, i: 1 }), t: Long("1") },
    lastAppliedWallTime: ISODate("2023-05-03T10:13:00.720Z"),
    lastDurableWallTime: ISODate("2023-05-03T10:12:50.719Z")
  },
  lastStableRecoveryTimestamp: Timestamp({ t: 1683108730, i: 1 }),
  electionCandidateMetrics: {
    lastElectionReason: 'electionTimeout',
    lastElectionDate: ISODate("2023-05-03T10:12:10.622Z"),
    electionTerm: Long("1"),
    lastCommittedOpTimeAtElection: { ts: Timestamp({ t: 1683108730, i: 1 }), t: Long("-1")
    lastSeenOpTimeAtElection: { ts: Timestamp({ t: 1683108730, i: 1 }), t: Long("-1") },
    numVotesNeeded: 1,
    priorityAtElection: 1,
    electionTimeoutMillis: Long("10000"),
    newTermStartDate: ISODate("2023-05-03T10:12:10.704Z"),
    wMajorityWriteAvailabilityDate: ISODate("2023-05-03T10:12:10.719Z")
  },
  members: [
    {
      _id: 0,
      name: '6e34e7bd27b2:27017',
      health: 1,
      state: 1,
      stateStr: 'PRIMARY',
      uptime: 102,
      optime: { ts: Timestamp({ t: 1683108780, i: 1 }), t: Long("1") },
      optimeDate: ISODate("2023-05-03T10:13:00.000Z"),
      lastAppliedWallTime: ISODate("2023-05-03T10:13:00.720Z"),
      lastDurableWallTime: ISODate("2023-05-03T10:12:50.719Z"),
      syncSourceHost: '',
      syncSourceId: -1,
      infoMessage: 'Could not find member to sync from',
      electionTime: Timestamp({ t: 1683108730, i: 2 }),
      electionDate: ISODate("2023-05-03T10:12:10.000Z"),
      configVersion: 5,
      configTerm: 1,
      self: true,
      lastHeartbeatMessage: ''
    },
    {
      _id: 1,
      name: 'mongodb1:27017',
      health: 1,
      state: 2,
      stateStr: 'SECONDARY',
      uptime: 39,
      optime: { ts: Timestamp({ t: 1683108770, i: 1 }), t: Long("1") },
      optimeDurable: { ts: Timestamp({ t: 1683108770, i: 1 }), t: Long("1") },
      optimeDate: ISODate("2023-05-03T10:12:50.000Z"),
      optimeDurableDate: ISODate("2023-05-03T10:12:50.000Z"),
      lastAppliedWallTime: ISODate("2023-05-03T10:13:00.720Z"),
      lastDurableWallTime: ISODate("2023-05-03T10:13:00.720Z"),
      lastHeartbeat: ISODate("2023-05-03T10:12:59.647Z"),
      lastHeartbeatRecv: ISODate("2023-05-03T10:12:59.670Z"),
      pingMs: Long("0"),
      lastHeartbeatMessage: '',
      syncSourceHost: '6e34e7bd27b2:27017',
      syncSourceId: 0,
      infoMessage: '',
      configVersion: 5,
      configTerm: 1
    },
    {
      _id: 2,
      name: 'mongodb2:27017',
      health: 1,
      state: 2,
      stateStr: 'SECONDARY',
      uptime: 27,
      optime: { ts: Timestamp({ t: 1683108770, i: 1 }), t: Long("1") },
      optimeDurable: { ts: Timestamp({ t: 1683108770, i: 1 }), t: Long("1") },
      optimeDate: ISODate("2023-05-03T10:12:50.000Z"),
      optimeDurableDate: ISODate("2023-05-03T10:12:50.000Z"),
      lastAppliedWallTime: ISODate("2023-05-03T10:13:00.720Z"),
      lastDurableWallTime: ISODate("2023-05-03T10:13:00.720Z"),
      lastHeartbeat: ISODate("2023-05-03T10:12:59.646Z"),
      lastHeartbeatRecv: ISODate("2023-05-03T10:13:00.150Z"),
      pingMs: Long("0"),
      lastHeartbeatMessage: '',
      syncSourceHost: 'mongodb1:27017',
      syncSourceId: 1,
      infoMessage: '',
      configVersion: 5,
      configTerm: 1
    }
  ],
  ok: 1,
  '$clusterTime': {
    clusterTime: Timestamp({ t: 1683108780, i: 1 }),
    signature: {
      hash: Binary(Buffer.from("0000000000000000000000000000000000000000", "hex"), 0),
      keyId: Long("0")
    }
  },
  operationTime: Timestamp({ t: 1683108780, i: 1 })
}
```

### 验证一下

向主节点中插入一条数据

```shell
db.name.insertOne({"name":"hello,world"})
```

输出结果如下：

```text
{
  acknowledged: true,
  insertedId: ObjectId("645233b7a788cd57c91ab768")
}
```

在副节点中查看数据

```shell
# 进入副节点
docker exec -it mongodb1 mongosh

# 允许从主节点中读取数据
db.getMongo().setReadPref("secondary")
# 查询 name
db.name.find()
```

输出如下

```text
rs0 [direct: secondary] test> db.getMongo().setReadPref("secondary")

rs0 [direct: secondary] test> db.name.find()
[ { _id: ObjectId("645233b7a788cd57c91ab768"), name: 'hello,world' } ]
```

# MongoDB Compass 可视化工具

## 安装

官网下载地址：[https://www.mongodb.com/try/download/compass](https://www.mongodb.com/try/download/compass)

这里选择 **MongoDB Compass Download (GUI)**，如果你想使用命令行方式，可以选择 **MongoDB Shell Download**

不想安装可以直接下载绿色版 **Windows 64-bit (7+) (Zip)**，直接解压就能使用。

![MongoDBCompassWebsite](./assets/MongoDBCompassWebsite.png)

## 启动

将上述下载好的压缩包进行解压，进入目录双击启动 **MongoDBCompass.exe** 进入 **GUI** 界面，如下图所示：

![MongoDBCompassGUI](./assets/MongoDBCompassGUI.png)

## 连接 MongoDB

输入 URI `mongodb://root:root@localhost:27017`，点击 **connect** 即可！

出现如下图所示表示成功！

![MongoDBCompassConnect](./assets/MongoDBCompassConnect.png)

初次连接，MongoDB 自带三个数据库，分别是 `admin`，`local`，`config`，下面分别来介绍一下三个数据库主要是干什么的？

- admin：存放 MongoDB 用户相关信息的数据库

![MongoDBDatabaseAdmin](./assets/MongoDBDatabaseAdmin.png)

- local：存放 MongoDB 服务启动日志相关的服务信息的数据库

![MongoDBDatabaseLocal](./assets/MongoDBDatabaseLocal.png)

- config：存放 MongoDB 配置相关的数据库

![MongoDBDatabaseConfig](./assets/MongoDBDatabaseConfig.png)

> **注意**
>
> 如果你通过 MongoDBCompass GUI 工具只能看到部分的数据库，那是因为用户对应的权限不足，我这里通过 Navicat 进行查看的。

![MongoDBAuth](./assets/MongoDBAuth.png)

## MongoDBCompass 工具使用

### 新建数据库

这里不进行文字描述，一图胜千言，如下图所示：

![MongoDBCompassCreateDB](./assets/MongoDBCompassCreateDB.png)

> **说明**
>
> 如果你想建一个集合不知道选什么类型，截图中出现的三个复选框可以不用勾选。

### 新建集合

同样地，这里不进行文字描述，一图胜千言，如下图所示：

![MongoDBCompassCreateCollection](./assets/MongoDBCompassCreateCollection.png)

### 导入数据

同样地，这里不进行文字描述，一图胜千言，如下图所示：

![MongoDBCompassImportData](./assets/MongoDBCompassImportData.png)

批量导入的 JSON 数据可以参考 [accounts_documents_template](./../scripts/accounts_documents_template.json) 案例数据。
