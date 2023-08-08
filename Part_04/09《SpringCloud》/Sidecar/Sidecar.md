# Sidecar

## 一 Sidecar 介绍

​				在我们的分布式项目中,经常会出现一部分服务是 Java 语言写的,一部分服务是非 Java 语言写的,Java 语言写的服务可以通过我们的 SpringCloud 组件来进行服务发现,网关路由等操作,但是非 Java语言的程序则无法实现这个功能,为了解决这个问题,Netfilix 提供了 Sidecar 来解决,其基本思想就是 sidecar 是一个 Java 语言的程序,然后内容通过配置访问非 Java语言的程序,然后注册到我们的 SpringCloud组件中,然后实现我们的功能,本质上其就是一个代理

#### 官方说明

>Do you have non-JVM languages with which you want to take advantage of Eureka, Ribbon, and Config Server? The Spring Cloud Netflix Sidecar was inspired by Netflix Prana. It includes an HTTP API to get all of the instances (by host and port) for a given service. You can also proxy service calls through an embedded Zuul proxy that gets its route entries from Eureka. The Spring Cloud Config Server can be accessed directly through host lookup or through the Zuul Proxy. The non-JVM application should implement a health check so the Sidecar can report to Eureka whether the app is up or down.
>
>To include Sidecar in your project, use the dependency with a group ID of `org.springframework.cloud` and artifact ID or `spring-cloud-netflix-sidecar`.
>
>`sidecar内置了断路器,服务发现和 zuul 功能,sidecar 需要和被代理的服务运行在同一个机器上面`
>
>To enable the Sidecar, create a Spring Boot application with` @EnableSidecar`. This annotation includes `@EnableCircuitBreaker, @EnableDiscoveryClient, and @EnableZuulProxy.Run the resulting application on the same host as the non-JVM application.`
>
>`sidecar.port 是被代理服务的端口`
>
>`sidecar.health-uri 是服务的健康检查地址,用于显示在 eukrea 中的服务状态,如 up 或者 down`
>
>To configure the side car, add `sidecar.port` and `sidecar.health-uri` to `application.yml`. The `sidecar.port `property is the port on which the non-JVM application listens. This is so the Sidecar can properly register the application with Eureka. The`sidecar.health-uri` is a URI accessible on the non-JVM application that mimics a Spring Boot health indicator. `It should return a JSON document that resembles the following:`
>
>**health-uri-document**
>
>```json
>{
>"status":"UP"
>}
>
>```
>
>The following application.yml example shows sample configuration for a Sidecar application:
>
>
>
>
>
>`示例配置`
>
>application.yml.
>
>```yaml
>server:
>  port: 5678
>spring:
>  application:
>    name: sidecar
>
>sidecar:
>  port: 8000 #非java 程序的端口
>  health-uri: http://localhost:8000/health.json
>
>```
>
>



## 二 示例

### 2.1 非 Java 项目

> 此处我们使用了 nodejs 作为另外一个服务端进行测试,所以机器上面需要安装 nodejs 环境,然后通过 node xxx.js 的方式启动程序

### 2.1.1 node-service.js

```javascript
var http = require('http');
var url = require("url");
var path = require('path');

// 创建server
var server = http.createServer(function(req, res) {
  // 获得请求的路径
  var pathname = url.parse(req.url).pathname;  
  res.writeHead(200, { 'Content-Type' : 'application/json; charset=utf-8' });
  // 访问http://localhost:8060/，将会返回{"index":"欢迎来到首页"}
  if (pathname === '/') {
    res.end(JSON.stringify({ "index" : "欢迎来到首页" }));
  }
  // 访问http://localhost:8060/health，将会返回{"status":"UP"}
  else if (pathname === '/health.json') {
    res.end(JSON.stringify({ "status" : "UP" }));//此处的 up down 会影响到 eureka 的状态,UP 代表是 ok 的,此处营房根据实际业务判断,当然也可以直接返回 UP,当程序崩溃后无结果也被认为是DOWN
  }
  else if (pathname==='/userinfo.json'){ //如果访问的是/userinfo.json 返回的是下面的内容
    res.end(JSON.stringify({"name":"zhangsan","age":18,"address":"china"}))
  }
  // 其他情况返回404,内容是404
  else {
    res.end("404");
  }
});
// 创建监听，并打印日志,服务运行的端口是8060
server.listen(8060, function() {
  console.log('listening on localhost:8060');
});

```

## 2.2 sidecar 项目

本项目最终必须和非 java 程序运行在一个服务器中,即保证域名是一样的

### 2.2.1 pom.xml

> 此处只列出来了依赖部分

```xml
   <dependencies>


            <dependency>
                <groupId>org.springframework.cloud</groupId>
                <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
            </dependency>

            <dependency>
                <groupId>org.springframework.cloud</groupId>
                <artifactId>spring-cloud-netflix-sidecar</artifactId>
            </dependency>

        </dependencies>

```

### 2.2.2 application.yml

```yaml
server:
  port: 14785
spring:
  application:
    name: 26sidecar
eureka: #注册中心的地址
  client:
    service-url:
      defaultZone: http://zhangsan:abc@localhost:10000/eureka #curl风格
sidecar: #配置sidecar
  port: 8060 #非jvm程序运行的端口,因为本程序要和非 java 程序在一个机器中,所以只需要端口即可
  health-uri: http://localhost:8060/health.json #非jvm程序用于检查服务状态的接口,返回up 服务是可用的,返回 UP 代表正常,DOWN 代表下线,用于将结果注册到 eureka 中

```

### 2.2.3 主程序

```java
@SpringBootApplication
@EnableSidecar
public class SideCarStarApp {

    public static void main (String[] args){
        SpringApplication.run(SideCarStarApp.class,args);
    }
}

```

## 2.3 启动测试

1. 启动我们的 nodejs 服务端
2. 启动我们的 eureka
3. 启动我们的 sidecar
4. 启动我们的 zuul(zuul 最后启动,如果有其他服务,先启动其他服务)

**`访问 eureka 可以看到我们的 sidecar程序注册到了eureka 中`**

| ![image-20190909112852134](mdpic/image-20190909112852134.png) |
| :----------------------------------------------------------: |



**`访问我们的 zuul,可以看到能代理我们的 sidecar`**

| ![image-20190909112942873](mdpic/image-20190909112942873.png) |
| :----------------------------------------------------------: |



**`通过zuul 访问我们的 sidecar,可以看到最终的 node 返回的内容`**



| ![image-20190909113041015](mdpic/image-20190909113041015.png) |
| :----------------------------------------------------------: |
| ![image-20190909113059335](mdpic/image-20190909113059335.png) |



