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

通过可视化工具（我这里使用的 **Navicat**，当然也可以使用 **MongoDB Compass**）连接 mongodb 进行测试，如出现截图信息表示启动成功！

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
