# spring-boot

**待更新中。。。**

## Docker 安装 (mac)
- 安装brew:https://brew.sh/index_zh-cn.html

```
/usr/bin/ruby -e "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/master/install)"
```
- 安装docker

```
brew cask install docker
— 点击图标、启动登录docker 然后终端运行
docker --version
docker-compose --version
docker-machine --version
```

## docker 下载并创建mysql
- docker/mysql:https://hub.docker.com/_/mysql
- 下载，创建

```
docker pull mysql
//创建密码，同时创建一个名为abcd的数据库,同时mysql端口和本机的端口
docker run --name some-mysql -e MYSQL_ROOT_PASSWORD=password -e MYSQL_DATABASE=abcd -p 3306:3306  -d mysql
```

## Flyway
- 官网：https://flywaydb.org/documentation/maven/
- 配置 maven 依赖
- 配置项目的目录

```
   src
     main
       resources
         db
             migration                 
                 R__My_view.sql
                 U1.1__Fix_indexes.sql
                 U2__Add a new table.sql
                 V1__Initial_version.sql
                 V1.1__Fix_indexes.sql
                 V2__Add a new table.sql
```

- 在.sql文件里写创建表的sql命令,例如

```
create table user
(
    id                 bigint primary key auto_increment,
    username           varchar(100),
    encrypted_password varchar(100),
    avatar             varchar(100),
    created_at         datetime,
    updated_at         datetime
)
```

- docker启动数据库后（注意是启动数据库后），输入以下命令创建表结构

```
mvn flyway:migrate
```

## Java DAO 模式 
- Controller 对请求的参数验证和清洗
- Service 处理业务逻辑的方法
- DAO  提供访问数据库系统所需操作的接口。DAO 模式的优势就在于它实现了两次隔离：隔离了数据访问代码和业务逻辑代码，隔离了不同数据库实现。
