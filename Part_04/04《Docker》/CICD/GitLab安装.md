



#### GitLab

> 实现CICD，需要使用到代码仓库，我们使用的是GitLab来搭建



#### 一  准备工作

> - 创建一个全新的虚拟机，并且至少指定4G的运行内存，4G运行内存是Gitlab推荐的内存大小。
> - 并且安装Docker以及Docker-Compose



##### 1.1修改ssh的22端口

> 为了方便通过ssh方式操作我们的代码仓库,因此我们GitLab会占用22端口,所以我们需要将系统的ssh的默认22端口修改为其他端口,比如此处修改为60022



###### 1.1.1 修改

> 修改下面的文件将Port改成非22  此处改为了 60022,改成多少,后面通过ssh远程连接机器的时候就用什么端口

```shell
vi /etc/ssh/sshd_config
```

###### 1.1.2 重启sshd

```shell
systemctl restart sshd
```



##### 1.2 编写docker-compose.yml

> docker-compose.yml文件去安装gitlab（下载和运行的时间比较长的）

```yml
version: '3.1'
services:
 gitlab:
  image: 'baseservice.chenjunbo.xin:60001/gitlab-ce-zh:10.5'
  container_name: "gitlab"
  restart: always
  privileged: true
  hostname: 'gitlab'
  environment:
   TZ: 'Asia/Shanghai'
   GITLAB_OMNIBUS_CONFIG: |
    external_url 'http://192.168.3.105'
    gitlab_rails['time_zone'] = 'Asia/Shanghai'
    gitlab_rails['smtp_enable'] = true
    gitlab_rails['gitlab_shell_ssh_port'] = 22
  ports:
   - '80:80'
   - '443:443'
   - '22:22'
  volumes:
   - ./config:/etc/gitlab
   - ./data:/var/opt/gitlab
   - ./logs:/var/log/gitlab
```



#####  1.3 启动

```shell
docker-compose up -d
```

