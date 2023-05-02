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
> 假如在执行 `show collections;` 命令，控制台提示 **MongoServerError: command listCollections requires authentication** 异常信息，一般解决方案是未进行身份认证。有如下 2 种解决方案：
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

通过可视化工具（我这里使用的 **Navicat**，当然也可以使用 **MongoDB Compass**，推荐使用 **MongoDB Compass** 后续方便 JSON 批量测试数据导入）连接 mongodb 进行测试，如出现截图信息表示启动成功！

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
