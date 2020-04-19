# MySql创建和数据卷管理

## 使用docker-compose创建

- 使用Named数据卷来备份和管理数据
- 创建自定义网络来使网络中的容器能根据容器名(`hostname`)互相访问
  - `aliases` 作用可以让其他同一个网络内(`dev_net`)的容器用 `mysql` `mysql-db` `database` `db` 等别名来访问 `dev_db`(也可以直接用 `dev_db`)
- 指定默认的字符串编码格式
- 宿主机直接访问本地"3306"端口即可登陆数据库(`ports`设置将容器端口映射到宿主机)
- `docker-compose [-f <compose file>] up/stop/start/rm/exec` 管理容器
- docker-compose创建的volumes, networks名称都会增加yaml文件所在目录的名称作为前缀

```ymal
# resources/mysql-local.yaml
version: "3"
services:
  dev_db:
    container_name: dev_db
    hostname: dev_db
    image: mysql
    restart: always
    command: ['mysqld', '--character-set-server=utf8mb4', '--collation-server=utf8mb4_unicode_ci']
    environment:
      # 创建数据库
      MYSQL_DATABASE: dev_db
      MYSQL_USER: admin
      MYSQL_PASSWORD: admin
      MYSQL_ROOT_PASSWORD: root
    ports:
      - "3306:3306"
    volumes:
      - dev_dbdata:/var/lib/mysql
    networks:
      dev_net:
        aliases:
          - mysql
          - mysql-db
          - database
          - db

volumes:
  dev_dbdata:
networks:
  dev_net:
```
