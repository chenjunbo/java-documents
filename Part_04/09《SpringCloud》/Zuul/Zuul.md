

# Zuul

## 一 问题简要概述

让我们想象一下，你要为一个购物应用程序开发一个原生移动客户端。你很可能需要实现一个产品详情页面，展示任何指定商品的信息。

下图展示了 Amazon Android 应用在商品详情页显示的内容。

| ![Richardson-microservices-part2-1_amazon-apps](mdpic/Richardson-microservices-part2-1_amazon-apps.png) |
| :----------------------------------------------------------: |

 

即使只是个智能手机应用，产品详情页面也显示了大量的信息。该页面不仅包含基本的产品信息（如名称、描述、价格），而且还显示了如下内容：

> - 购物车中的商品数量
> - 历史订单
> - 客户评论
> - 低库存预警
> - 送货选项
> - 各种推荐，包括经常与该商品一起购买的其它商品、购买该商品的客户购买的其它商品、购买该商品的客户看过的其它商品
> - 其它的购物选择

使用单体应用程序架构时，移动客户端通过向应用程序发起一次 REST 调用（GET api.company.com/productdetails/）来获取这些数据。负载均衡器将请求路由给 N 个相同的应用程序实例中的其中之一。然后，应用程序会查询各种数据库表，并将响应返回给客户端。

相反，若是采用微服务架构，显示在产品页上的数据会分布在不同的微服务上。下面列举了可能与产品详情页数据有关的一些微服务：

> - 购物车服务——购物车中的件数
> - 订单服务——历史订单
> - 目录服务——商品基本信息，如名称、图片和价格
> - 评论服务——客户的评论
> - 库存服务——低库存预警
> - 送货服务——送货选项、期限和费用，这些信息单独从送货方 API 获取
> - 推荐服务——推荐商品

| ![Richardson-microservices-part2-2_microservices-client](mdpic/Richardson-microservices-part2-2_microservices-client.png) |
| :----------------------------------------------------------: |

我们需要决定移动客户端如何访问这些服务。让我们看看有哪些方法。

## 二 客户端与微服务直接通信

从理论上讲，客户端可以直接向每个微服务发送请求。每个微服务都有一个公开的端点(https ://.api.company.name）。该 URL 映射到微服务的负载均衡器，由后者负责在可用实例之间分发请求。为了获取产品详情，移动客户端将逐一向上文列出的 N 个服务发送请求。

遗憾的是，这种方法存在挑战和局限。**问题之一是客户端需求和每个微服务暴露的细粒度 API 不匹配。**在这个例子中，客户端需要发送 7 个独立请求。在更复杂的应用程序中，可能要发送更多的请求；按照 Amazon 的说法，他们在显示他们的产品页面时就调用了数百个服务。然而，客户端通过 LAN 发送许多请求，这在公网上可能会很低效，在移动网络上就根本不可行。这种方法还使得客户端代码非常复杂。

**客户端直接调用微服务的另一个问题是，部分服务使用的协议对 web 并不友好。**一个服务可能使用 Thrift 二进制 RPC，而另一个服务可能使用 AMQP 消息传递协议。不管哪种协议对于浏览器或防火墙都不够友好，最好是内部使用。在防火墙之外，应用程序应该使用诸如 HTTP 和 WebSocket 之类的协议。

**这种方法的另一个缺点是，它会使得微服务难以重构。**随着时间推移，我们可能想要更改系统拆分成服务的方式。例如，我们可能合并两个服务，或者将一个服务拆分成两个或更多服务。然而，如果客户端与微服务直接通信，那么执行这类重构就非常困难了。

由于上述三种问题的原因，客户端直接与服务器端通信的方式很少在实际中使用。

## 三 使用 API 网关构建微服务



通常来说，使用 API 网关是更好的解决方式。API 网关是一个服务器，也可以说是进入系统的唯一节点。这与面向对象设计模式中的 Facade 模式很像。API 网关封装内部系统的架构，并且提供 API 给各个客户端。它还可能还具备授权、监控、负载均衡、缓存、请求分片和管理、静态响应处理等功能。下图展示了一个适应当前架构的 API 网关。

| ![](mdpic/Richardson-microservices-part2-3_api-gateway.png) |
| :----------------------------------------------------------: |

API 网关负责服务请求路由、组合及协议转换。客户端的所有请求都首先经过 API 网关，然后由它将请求路由到合适的微服务。API 网关经常会通过调用多个微服务并合并结果来处理一个请求。它可以在 web 协议（如 HTTP 与 WebSocket）与内部使用的非 web 友好协议之间转换。

API 网关还能为每个客户端提供一个定制的 API。通常，它会向移动客户端暴露一个粗粒度的 API。以产品详情的场景为例，API 网关可以提供一个端点（/productdetails?productid=xxx），使移动客户端可以通过一个请求获取所有的产品详情。API 网关通过调用各个服务（产品信息、推荐、评论等等）并合并结果来处理请求。

**Netflix API 网关是一个很好的 API 网关实例。**Netflix 流媒体服务提供给成百上千种类型的设备使用，包括电视、机顶盒、智能手机、游戏系统、平板电脑等等。

最初，Netflix 试图为他们的流媒体服务提供一个通用的 API。然而他们发现，由于各种各样的设备都有自己独特的需求，这种方式并不能很好地工作。如今，他们使用一个 API 网关，通过运行与针对特定设备的适配器代码，来为每种设备提供定制的 API。通常，一个适配器通过调用平均 6 到 7 个后端服务来处理每个请求。Netflix API 网关每天处理数十亿请求。

## 四 API 网关的优点和缺点

如你所料，使用 API 网关有优点也有不足。**使用 API 网关的最大优点是，它封装了应用程序的内部结构。**客户端只需要同网关交互，而不必调用特定的服务。API 网关为每一类客户端提供了特定的 API，这减少了客户端与应用程序间的交互次数，还简化了客户端代码。

**API 网关也有一些不足。它增加了一个我们必须开发、部署和维护的高可用组件。还有一个风险是，API 网关变成了开发瓶颈。**为了暴露每个微服务的端点，开发人员必须更新 API 网关。API网关的更新过程要尽可能地简单，这很重要；否则，为了更新网关，开发人员将不得不排队等待。不过，虽然有这些不足，但对于大多数现实世界的应用程序而言，使用 API 网关是合理的。

## 五 实现 API 网关

到目前为止，我们已经探讨了使用 API 网关的动力及其优缺点。下面让我们看一下需要考虑的各种设计问题。

### **5.1 性能和可扩展性**

只有少数公司拥有 Netflix 这样的规模，需要每天处理每天需要处理数十亿请求。不管怎样，对于大多数应用程序而言，API 网关的性能和可扩展性都非常重要。**因此，将 API 网关构建在一个支持异步、I/O 非阻塞的平台上是合理的。**有多种不同的技术可以实现一个可扩展的 API 网关。在 JVM 上，可以使用一种基于 NIO 的框架，比如 Netty、Vertx、Spring Reactor 或 JBoss Undertow 中的一种。一个非常流行的非 JVM 选项是 Node.js，它是一个基于 Chrome JavaScript 引擎构建的平台。

另一个方法是使用 NGINX Plus。NGINX Plus 提供了一个成熟的、可扩展的、高性能 web 服务器和一个易于部署的、可配置可编程的反向代理。NGINX Plus 可以管理身份验证、访问控制、负载均衡请求、缓存响应，并提供应用程序可感知的健康检查和监控。

### **5.2 使用响应式编程模型**

API 网关通过简单地将请求路由给合适的后端服务来处理部分请求，而通过调用多个后端服务并合并结果来处理其它请求。对于部分请求，比如产品详情相关的多个请求，它们对后端服务的请求是独立于其它请求的。为了最小化响应时间，API 网关应该并发执行独立请求。

然而，有时候，请求之间存在依赖。在将请求路由到后端服务之前，API 网关可能首先需要调用身份验证服务验证请求的合法性。类似地，为了获取客户心愿单中的产品信息，API 网关必须首先获取包含这些信息的客户资料，然后再获取每个产品的信息。关于 API 组合，另一个有趣的例子是 Netflix Video Grid。

使用传统的异步回调方法编写 API 组合代码会让你迅速坠入回调地狱。代码会变得混乱、难以理解且容易出错。一个更好的方法是使用响应式方法，以一种声明式样式编写 API 网关代码。响应式抽象概念的例子有 Scala 中的 Future、Java 8 中的 CompletableFuture 和 JavaScript 中的P romise，还有最初微软为 .NET 平台开发的 Reactive Extensions（RX）。Netflix 创建了 RxJava for JVM，专门用于他们的 API 网关。此外，还有 RxJS for JavaScript，它既可以在浏览器中运行，也可以在 Node.js 中运行。使用响应式方法能让你编写简单但高效的 API 网关代码。

### **5.3 服务调用**

基于微服务的应用程序是一个分布式系统，必须使用一种进程间通信机制。有两种类型的进程间通信机制可供选择。**一种是使用异步的、基于消息传递的机制。**有些实现使用诸如 JMS 或 AMQP 那样的消息代理，而其它的实现（如 Zeromq）则没有代理，服务间直接通信。

**另一种进程间通信类型是诸如 HTTP 或 Thrift 那样的同步机制。**通常，一个系统会同时使用异步和同步两种类型。它甚至还可能使用同一类型的多种实现。总之，API 网关需要支持多种通信机制。

### **5.4 服务发现**

API 网关需要知道它与之通信的每个微服务的位置（IP 地址和端口）。在传统的应用程序中，或许可以硬连线这个位置，但在现代的、基于云的微服务应用程序中，这并不是一个容易解决的问题。基础设施服务（如消息代理）通常会有一个静态位置，可以通过 OS 环境变量指定。但是，确定一个应用程序服务的位置没有这么简单。应用程序服务的位置是动态分配的，而且，单个服务的一组实例也会随着自动扩展或升级而动态变化。

**总之，像系统中的其它服务客户端一样，API 网关需要使用系统的服务发现机制，可以是服务器端发现，也可以是客户端发现。**下一篇文章将更详细地描述服务发现。现在，需要注意的是，如果系统使用客户端发现，那么 API 网关必须能够查询服务注册中心，这是一个包含所有微服务实例及其位置的数据库。

### **5.5 处理局部失败**

在实现 API 网关时，还需要处理局部失败的问题。该问题出现在所有的分布式系统中。当一个服务调用另一个服务，而后者响应慢或不可用的时候，就会出现这个问题。API 网关不能因为无限期地等待下游服务而阻塞。不过，如何处理失败取决于特定的场景以及哪个服务失败。例如，在产品详情场景下，如果推荐服务无响应，那么 API 网关应该向客户端返回产品详情的其它内容，因为它们对用户依然有用。推荐内容可以为空，也可以用一个固定的 TOP 10 列表取代。不过，如果产品信息服务无响应，那么 API 网关应该向客户端返回一个错误信息。

如果缓存数据可用，那么 API 网关还可以返回缓存数据。例如，鉴于产品价格不会频繁变动，如果价格服务不可用，API 网关可以返回缓存的价格数据。数据可以由 API 网关自己缓存，也可以存储在像 Redis 或 Memcached 之类的外部缓存中。通过返回默认数据或者缓存数据，API 网关可以确保系统故障不影响用户体验。

在编写代码调用远程服务方面，Netflix Hystrix 是一个格外有用的库。Hystrix 会暂停超出特定阈限的调用。它实现了一个“断路器（circuit breaker）”模式，可以防止客户端对无响应的服务进行不必要的等待。如果服务的错误率超出了设定的阈值，那么 Hystrix 会启动断路器，所有请求会立即失败并持续一定时间。Hystrix 允许用户定义一个请求失败后的后援操作，比如从缓存读取数据，或者返回一个默认值。如果你正在使用 JVM，那么你应该考虑使用 Hystrix；如果你正在使用一个非 JVM 环境，那么可以使用一个功能相同的库。

### 5.6 zuul 能做什么？

Zuul可以通过加载动态过滤机制，从而实现以下各项功能：

- 验证与安全保障: 识别面向各类资源的验证要求并拒绝那些与要求不符的请求。
- 审查与监控: 在边缘位置追踪有意义数据及统计结果，从而为我们带来准确的生产状态结论。
- 动态路由: 以动态方式根据需要将请求路由至不同后端集群处。
- 压力测试: 逐渐增加指向集群的负载流量，从而计算性能水平。
- 负载分配: 为每一种负载类型分配对应容量，并弃用超出限定值的请求。
- 静态响应处理: 在边缘位置直接建立部分响应，从而避免其流入内部集群。
- 多区域弹性: 跨越AWS区域进行请求路由，旨在实现ELB使用多样化并保证边缘位置与使用者尽可能接近。

## 六  zuul 基本使用

> 每次使用不同的配置文件
>
> http://cloud.spring.io/spring-cloud-static/Edgware.SR2/single/spring-cloud.html#_router_and_filter_zuul
>
>  hystrix 的默认隔离级别是 Thread, zuul 是SEMAPHORE 
> Default Hystrix isolation pattern (ExecutionIsolationStrategy) for all routes is SEMAPHORE. `zuul.ribbonIsolationStrategy` can be changed to THREAD if this isolation pattern is preferred. 

​	`启用网关`

​	To enable it, annotate a Spring Boot main class with `@EnableZuulProxy`, and this forwards local calls to the appropriate service 使用EnableZuulProxy注解开启

​		the Zuul starter does not include a discovery client, so for routes based on service IDs you need to provide one of those on the classpath as well (e.g. Eureka is one choice). zuul 不包括客户端发现,所以我们需要配置一个服务发现,比如 eureka,所以我们需要添加服务发现依赖包和配置注册中心

### 6.1 pom.xml

```xml

            <dependency>
                <groupId>org.springframework.cloud</groupId>
                <artifactId>spring-cloud-starter-netflix-zuul</artifactId>

            </dependency>
<!--通过 eureka 来拉取服务列表-->
            <dependency>
                <groupId>org.springframework.cloud</groupId>
                <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
  		<dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-actuator</artifactId>
        </dependency>
            </dependency>

```



### 6.2 application.yml

```yaml
server:
  port: 16500
eureka: #注册中心的地址,用于获取服务列表
  client:
    service-url:
      defaultZone: http://localhost:10000/eureka #curl风格

spring:
  application:
    name: 22apigateway-zuul
management:
  endpoints:
    web:
      exposure:
        include: '*'  #允许访问所有管理地址,不然无法通过网页查看路由的列表页/actuator/routes,但是不影响路由功能更

```



### 6.3 主程序

```java
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

/**
 * Created by jackiechan on 19-1-10/上午9:34
 *
 * @Author jackiechan
 */
@SpringBootApplication
@EnableZuulProxy//开启反向代理 也就是开启路由功能,内部会通过 eureka 上面获取服务列表
public class ZuulStarApp {
    public static void main (String[] args){
        SpringApplication.run(ZuulStarApp.class,args);
    }
}

```



### 6.4 测试

> 启动我们的服务提供者,消费者,和 zuul,然后访问项目下的/actuator/routes地址查看zuul 反向代理的服务
>
> 注意这个地址默认是无法访问的,需要在配置中开启,但是不配置不影响服务的路由功能
>
> 默认情况下映射的地址和服务的名字一致

| ![image-20190808225947903](mdpic/image-20190808225947903.png) |
| :----------------------------------------------------------: |

**通过反向代理访问服务中的接口**

> 请求格式:    ZUUL IP:端口/服务的映射地址/服务中的接口地址/参数

| ![image-20190824094428238](mdpic/image-20190824094428238.png) |
| :----------------------------------------------------------: |

## 七 其他映射方式

### 7.1. 自定义映射路径

> 默认情况下映射的地址和服务的名字一致,但是我们可以自己定义某个服务的映射地址,只需要在 yml 中配置即可
>
> 例如下面的例子中给05consumer-eureka这个服务设置映射地址/abc/**.注意后面的\*\*不能少
>
> 那么可以通过ZUUL IP:端口/abc/服务中的接口地址/参数的方式来访问05consumer-eureka这个服务

```yaml
#方式一
zuul:
  routes:
    14consumer-eureka-feign-hystrix-fallbackfactory: /gouwuche/** #给14consumer-eureka-feign-hystrix-fallbackfactory服务配置一个地址映射的地址/gouwuche/**
    05consumer-eureka: /abc/** #给05consumer-eureka服务配置一个地址映射的地址为 /abc/**
```



```yaml
#方式二
zuul:
  routes:
    aaaaa: #随便写,但是在配置文件里面不能重复
      path: /abcdefg/** #映射后的地址
      serviceId: 05consumer-eureka #要给哪个服务设置路由地址
    bbbb:
      path: /def/**
      serviceId: 04provider-eureka
```



### 7.2. 忽略某个服务

> 在某些情况下,我们的一些服务不会对外暴露出来,不能通过路由进行访问,那么我们可以对某个服务进行忽略



```yaml
zuul:
  routes:
    #05consumer-eureka: /abc/**
  ignored-services: 04provider-eureka,05consumer-eureka #忽略指定的服务,写服务的名字,如果多个以,分割

```



### 7.3 映射其他服务

> 有时候有些服务可能是其他语言编写的,没有在 eureka 上进行注册,所以无法通过注册中心进行动态拉取,我们可以单独设置



```yaml
zuul:
  routes:
    aaaaa: #随便写,但是在这个配置文件里面不能重复
      path: /abcdefg/** #路由后的地址
      url: http://10.9.32.191:8001 #给指定的url设置映射地址, 注意这样子没有负载均衡,这样我们可以通过/abcdefg/**来访问这个地址
	bbbb: #随便写,但是在这个配置文件里面不能重复
      path: /fsdfsdf/** #路由后的地址
      url: http://10.9.32.191:8002 #给指定的url设置映射地址, 注意这样子没有负载均衡,这样我们可以通过/abcdefg/**来访问这个地址


```



### 7.4 映射其他服务的集群

> 在实际开发中,我们的节点都是集群,当一个服务集群没有在 eureka 上面注册的时候,我们需要单独配置

```yaml
zuul:
  routes:
    aaaaa: #随便写,但是在这个配置文件里面不能重复,此处需要先配置这个
      path: /abcdefg/**
      serviceId: 05consumer-eureka #给服务定义一个 id,注意此处实际上不是从 eureka 中获取的,而是从下面获取的
05consumer-eureka: #这个值和上面的serviceId保持一致,目的是给这个服务设置负载均衡的服务器地址
  ribbon:
    listOfServers: http://10.9.32.111:1234,http://10.9.32.111:2345 #指定ribbon的负载均衡地址
#关闭ribbon的负载均衡,使用我们手动指定的服务器地址 ,停用后将不会从注册中心查找服务,会导致注册中心中的服务不可用访问     
ribbon:
  eureka:
    enabled: false
```



### 7.5. 配置访问前缀

> 像我们的 controller 一样,我们可以给路由下的所有服务设置统一的前缀



```yaml
zuul:
  prefix: /suibian #给所有的请求地址之前添加前缀suibian
```



###  7.6 跳过前缀

> zuul在代理访问的时候默认会忽略掉前缀地址,也就是默认情况下我们的服务的名字,当我们自定义了访问地址的时候会默认忽略掉这个地址,如下图中,网关实际请求目标服务中 **那部分的地址,虽然我们访问网关的时候加上了dingdan或者address,但是访问的时候会忽略掉



| ![image-20220628155332984](mdpic/image-20220628155332984.png) |
| :----------------------------------------------------------- |



`如果我们要访问的目标服务刚好也是以address开头的,比如/address/info/1,这种情况下我们要想通过网关来访问的话,那么我们请求网关的地址就是/address/address/info/1,这样的话会出现重复的地址,这个时候我们期望直接将这个前缀地址也一起带到下游服务,这样我们访问网关的时候就不需要写两次address了,我们以配置当前服务不跳过前缀,可以通过设置stripPrefix为false`



```yaml
zuul:
  ignored-services: '*' # 要忽略的列表,被忽略的服务不会自动添加到映射中,想要映射需要通过手动别名才可以,多个以逗号分割,'*'代表所有
  routes:
    orders: #这个是一个key,随便写,但是必须唯一
      path: /dingdan/**
      serviceId: 12orderconsumeropenfeignhystrix
    dizhi: #另外一个映射
      path: /address/**
      serviceId: 10addressproviderhystrix
      stripPrefix: false 
      #不携带前缀,比如当前我们要想访问m11consumer-feign-hystrix比如是/users/来访问
      #,但是我们的目标服务内部刚好也是/users开头的,会导致我们实际在浏览器中填写的地址会出现两次/users
      # 那既然重复了,我们可不可以这样,在通过/users确定访问的服务的时候顺便把/users带到请求地址中,这样我们就不需要在后面再添加一个/users了
```





## 八 表达式方式映射

### 8.1. yml

> yml和之前的配置没有什么区别

```yaml

server:
  port: 16500
eureka: #注册中心的地址
  client:
    service-url:
      defaultZone: http://zhangsan:abc@localhost:10000/eureka #curl风格

spring:
  application:
    name: 23apigateway-zuul-reg
  security:
    user:
      password: xiaomei
      name: laowang #默认是user
management:
  endpoints:
    web:
      exposure:
        include: '*'  #允许访问所有管理地址,不然无法通过网页查看路由的列表,但是影响访问
```



### 8.2. 启动类或者配置类

> 我们需要创建一个PatternServiceRouteMapper对象,在内部设置表达式

```java
@SpringBootApplication
@EnableZuulProxy
public class ZuulStarApp {
    public static void main(String[] args) {
        SpringApplication.run(ZuulStarApp.class, args);
    }

    /**
     * 设置服务的映射方式,当我们的服务的名字是  name-vx 的方式进行命名的时候,访问地址会被映射为 vx/name
     * 一般我们会通过这个方式来进行版本区分
     * 比如一个服务叫 gouwuche-v1,那么它的路径就是 v1/gouwuche
     * 当我们的这个服务升级后我们只需要将名字改成 gouwuche-v2 那么客户端就可以通过不同的版本前缀来访问不同版本的服务
     * @return
     */
    @Bean
    public PatternServiceRouteMapper serviceRouteMapper() {
      //其中<name> <version>是一个别名,是对应表达式内容的别名 ,实际上 name 代表的是^. 也就是任意内容 <version>代表是 v.代表以 v 开始的内容,所以下面的表达式代表是 name-vx
        return new PatternServiceRouteMapper(
                "(?<name>^.+)-(?<version>v.+$)",
                "${version}/${name}");
    }
}
```



## 九 Zuul fallback

> 在我们之前的时候,可以通过 hystrix 来进行服务的降级回退,当我们使用 zuul 的时候,也可以对有问题的服务进行降级
>
> 使用特别简单,只需要额外创建一个回退类实现FallbackProvider接口就可以,其他内容同上

### 9.1. 降级类

```java
@Component //必须添加这个注解,不添加的话无效果,spring 会创建这个类的对象,当出现问题的时候会调用这个里面对应的方法
public class MyZuulFallback implements FallbackProvider {
    /**
     * 返回值是服务的名字,代表这个falback是给哪个服务开启的,如果是给所有的,使用*,否则返回具体服务的名字
     * @return
     */
    @Override
    public String getRoute() {
        return "*";
    }
	/**
	 * 当出现问题的时候我们返回什么,此方法在 D 版本的时候没有参数,在 E 版本的时候出现了cause参数,F 版本出现的两个参数
	 * route代表出现问题的服务
	 * cause代表出的异常是什么
	 */
    @Override
    public ClientHttpResponse fallbackResponse(String route, Throwable cause) {
        return new ClientHttpResponse() {
          //返回的 http状态码 例如200 404等,但是此处是一个HttpStatus对象
            @Override
            public HttpStatus getStatusCode() throws IOException {
                return HttpStatus.OK;//理论上应该根据异常信息来决定返回什么
            }
				 //返回的 http状态码 例如200 404等,此处是数字,其实就是上面的内容的数字值
            @Override
            public int getRawStatusCode() throws IOException {
                return HttpStatus.BAD_GATEWAY.value();//和上面的HttpStatu一致即可
            }
				 // 状态文本，比如200是 OK 等,都可以自定义
            @Override
            public String getStatusText() throws IOException {
                return HttpStatus.BAD_GATEWAY.getReasonPhrase();
            }

            @Override
            public void close() {

            }
					//响应正文
            @Override
            public InputStream getBody() throws IOException {
                return new ByteArrayInputStream("提溜tama的喇嘛想用手里的tama换别着喇叭的哑巴的喇叭".getBytes());
            }
				//相应头
            @Override
            public HttpHeaders getHeaders() {
                HttpHeaders httpHeaders = new HttpHeaders();
                httpHeaders.add("ContentType","text/html;charset=utf-8");
                return httpHeaders;
            }
        };
    }


}
```



### 9.2. 测试

> 启动我们的注册中心,对应的服务和 zuul 后,正常情况下访问可以获取到数据,当我们将对应的服务停止后,再访问就会出现下面内容,乱码请忽略,那个可以通过设置流单独解决,主要是演示走了我们的降级



| ![image-20190909093330125](mdpic/image-20190909093330125.png) |
| :----------------------------------------------------------: |



### 9.3. 问题

#### 9.3.1 404

> 当我们访问的服务本身不存在时候我们发现上面的 fallback并不会生效,而是出现404错误,或者当我们的服务不可用出现了上面的降级后,在一段时间后再访问又会出现404
>
> 原因是因为 zuul 只会对其从 eureka 中拉取到的服务列表中的服务进行 fallback,如果服务本身不存在,就不会在 eureka 中存在,就会出现404找不的错误,同理,当我们的被代理的服务出现问题后一开始我们是可以 fallback的,因为我们的 zuul 内部由缓存,当缓存和 eureka 同步后服务不见了,就会出现404,当然实际开发中我们的服务都是集群,所以即便一个机器出问题了 zuul 也可以访问另外一个机器来完成功能



| ![image-20190909093602260](mdpic/image-20190909093602260.png) |
| :----------------------------------------------------------: |

#### 9.3.2 解决

> 对于上面的问题,我们自习看错误提示,zuul 代理出现问题的时候会将请求转发到 /error 这个地址,而我们的项目中并没有对这个地址进行映射,所以导致出现404,所以解决方式就是写一个 controller 来映射这个地址即可
>
> 但是注意这个 controller 不是随便写的,有特殊要求



```java

/**
 * Created by jackiechan on 19-7-3/下午2:50
 *
 * @Author jackiechan
 */
@RestController
//这个类必须实现ErrorController接口
public class MyErrorController implements ErrorController {
  //下面的方法返回什么地址,这里就映射什么地址
    @RequestMapping("/error")
    public String getError() { //方法名字随便写,返回值随便定
        return "老王带婷婷去兜风";
    }

	// 重写此方法,返回我们要映射的地址,不然会出现异常
    @Override
    public String getErrorPath() {
        return "/error";
    }
}

```



> 以下错误就是如果不实现ErrorController的时候会出现的错误
>
> 大体内容就是 spring 启动的时候会映射一个/error 地址(内部返回的其实就是我们的错误信息),但是我们自己又映射了一个,所以会提示已经映射.所以我们需要重写这个映射,所以需要实现接口并重写getErrorPath返回/error

```java
Caused by: java.lang.IllegalStateException: Ambiguous mapping. Cannot map 'basicErrorController' method 
public org.springframework.http.ResponseEntity<java.util.Map<java.lang.String, java.lang.Object>> org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController.error(javax.servlet.http.HttpServletRequest)
to {[/error]}: There is already 'myErrorController' bean method
```



## 十 Zuul Filter

### 10.1 zuul的工作原理

#### 10.1.1、过滤器机制

zuul的核心是一系列的**filters**, 其作用可以类比Servlet框架的Filter，或者AOP。

zuul把Request route到 用户处理逻辑 的过程中，这些filter参与一些过滤处理，比如Authentication，Load Shedding等。  

| ![img](mdpic/486074-20170220185335288-1703224333.png) |
| :----------------------------------------------------------: |



**Zuul提供了一个框架，可以对过滤器进行动态的加载，编译，运行。**

Zuul的过滤器之间没有直接的相互通信，他们之间通过一个RequestContext的静态类来进行数据传递的。RequestContext类中有ThreadLocal变量来记录每个Request所需要传递的数据。

Zuul的过滤器是由Groovy写成，这些过滤器文件被放在Zuul Server上的特定目录下面，Zuul会定期轮询这些目录，修改过的过滤器会动态的加载到Zuul Server中以便过滤请求使用。

下面有几种标准的过滤器类型：

Zuul大部分功能都是通过过滤器来实现的。Zuul中定义了四种标准过滤器类型，这些过滤器类型对应于请求的典型生命周期。

(1) `PRE`：这种过滤器在请求被路由之前调用。我们可利用这种过滤器实现身份验证、在集群中选择请求的微服务、记录调试信息等。

(2) `ROUTING`：这种过滤器将请求路由到微服务。这种过滤器用于构建发送给微服务的请求，并使用Apache HttpClient或Netfilx Ribbon请求微服务。

(3) `POST`：这种过滤器在路由到微服务以后执行。这种过滤器可用来为响应添加标准的HTTP Header、收集统计信息和指标、将响应从微服务发送给客户端等。

(4) `ERROR`：在其他阶段发生错误时执行该过滤器。

**内置的特殊过滤器**

zuul还提供了一类特殊的过滤器，分别为：StaticResponseFilter和SurgicalDebugFilter

StaticResponseFilter：StaticResponseFilter允许从Zuul本身生成响应，而不是将请求转发到源。

SurgicalDebugFilter：SurgicalDebugFilter允许将特定请求路由到分隔的调试集群或主机。

**自定义的过滤器**

除了默认的过滤器类型，Zuul还允许我们创建自定义的过滤器类型。

例如，我们可以定制一种STATIC类型的过滤器，直接在Zuul中生成响应，而不将请求转发到后端的微服务。

 

#### 10.1.2、过滤器的生命周期

Zuul请求的生命周期如图，该图详细描述了各种类型的过滤器的执行顺序。

| ![img](mdpic/1099841-20170630111344414-1260445909.png) |
| :----------------------------------------------------------: |

 

#### 10.1.3、过滤器调度过程

| ![image-20190909102426659](mdpic/image-20190909102426659.png) |
| :----------------------------------------------------------: |

### 10.2 自定义 filter

> 我们只需要写一个类继承自 ZuulFilter 即可

```java
@Component
public class MyZuulFilter extends ZuulFilter {
  	//返回过滤器的类型
    @Override
    public String filterType() {
        return "pre";//前置
    }

    /**
     * 如果相同类型的过滤器有多个,可用通过这个值指定顺序,越小优先级越高
     * @return
     */
    @Override
    public int filterOrder() {
        return 1;
    }

    /**
     * fasle禁用过滤器,true开启,此处应该根据实际工作来决定
     * @return
     */
    @Override
    public boolean shouldFilter() {
        return true;
    }

    /**
     * 过滤器要做的事情
     * @return 返回值没有什么意义
     * @throws ZuulException
     */
    @Override
    public Object run() throws ZuulException {
        System.err.println(">>>>>>>>>>>>>>>>执行了");
        return "aaaaaaa";
    }
}

```

### 10.3 RequestContext

#### 10.3.1 RequestContext介绍

> 我们知道,在 servlet 中,我们的每个 filter 可以通过方法中的 request和 response 来进行通信和传递数据,但是在 zuul 中并没有这两个参数,那如何在 filter 中进行通信呢,zuul 给我们提供了一个RequestContext来进行通信,RequestContext中封装了我们的请求和相应对象以及一系列的 zuul 的操作,RequestContext被放在了 Threadlocal中,这样子我们每个请求都会有一个RequestContext对象,不至于出现线程安全问题,RequestContext本质上是一个 HashMap



**`下图为RequestContext中的内容`**

| ![image-20190909104756267](mdpic/image-20190909104756267.png) |
| :----------------------------------------------------------: |

| ![image-20190909104805948](mdpic/image-20190909104805948.png) |
| :----------------------------------------------------------: |



#### 10.3.2 RequestContext中的常见 API

```java
RequestContext context=RequestContext.getCurrentContext();//获取RequestContext对象
HttpServletRequest request = context.getRequest();//获取请求对象
HttpServletResponse response = context.getResponse();//获取响应对象
context.setSendZuulResponse(false);//拦截请求,默认是 true 放行
context.setResponseBody("在有生的瞬间能遇到你,竟花光所有运气,到今日才发现,曾呼吸过空气");//设置响应内容,等同于获取到response后通过流写出的内容
context.set("name", "zhangsan");//向下游服务传递参数
context.put(FilterConstants.REQUEST_URI_KEY,path+requestURI2);//跳转到服务中的哪个地址,key 是固定的,可以写具体的值或者用常量
context.put("serviceId", servicename);//设置要转发的服务的id,用于实现自定义的路由功能,key对应常量FilterConstants.SERVICE_ID_KEY
```



**示例过滤器**

```java
@Component
public class MyZuulFilter2 extends ZuulFilter {
    @Override
    public String filterType() {
        return "pre";//前置
    }

    /**
     * 如果有多个过滤器,可用通过这个值指定顺序,越小优先级越高
     * @return
     */
    @Override
    public int filterOrder() {
        return 2;
    }

    /**
     * fasle禁用过滤器,true开启,此处应该根据实际工作来决定
     * @return
     */
    @Override
    public boolean shouldFilter() {
        return true;
    }

    /**
     * 过滤器要做的事情
     * @return
     * @throws ZuulException
     */
    @Override
    public Object run() throws ZuulException {
        System.err.println(">>>>>>>>>>>>>>>>2222执行了");
        RequestContext context = RequestContext.getCurrentContext();//获取上下文
        HttpServletRequest request = context.getRequest();//获取请求
        HttpServletResponse response = context.getResponse();
        context.setSendZuulResponse(false);//拦截请求
        context.setResponseBody("在有生的瞬间能遇到你,竟花光所有运气,到今日才发现,曾呼吸过空气");//设置响应正文
        
        return "aaaaaaa";
    }
}

```



**示例代码2**



```java
@Component
public class MyZuulFilter3 extends ZuulFilter {
	
	private static HashMap<String, String> routeMap =new HashMap<>();//模拟存放服务列表的集合
	private static HashMap<String, String> urlMap =new HashMap<>();//模拟存放请求地址的集合
	static {
		routeMap.put("gouwuche", "14consumer-eureka-feign-hystrix-fallbackfactory");
		urlMap.put("getuser", "/userconsumer/info");
	}
    @Override
    public String filterType() {
        return "pre";//前置
    }

    /**
     * 如果有多个过滤器,可用通过这个值指定顺序,越小优先级越高
     * @return
     */
    @Override
    public int filterOrder() {
        return 999;
    }

    /**
     * fasle禁用过滤器,true开启,此处应该根据实际工作来决定
     * @return
     */
    @Override
    public boolean shouldFilter() {
        return true;
    }

    /**
     * 过滤器要做的事情,此处的例子是我们实现自定义路由的功能,根据用户传递的参数来决定跳转到什么服务的什么地址,实际开发中此功能使用较多
     * @return
     * @throws ZuulException
     */
    @Override
    public Object run() throws ZuulException {
        System.err.println(">>>>>>>>>>>>>>>>2222执行了");
        RequestContext context = RequestContext.getCurrentContext();//获取上下文
        HttpServletRequest request = context.getRequest();//获取请求
        String method = request.getParameter("method");//获取method,method 代表要请求哪个服务
        String servicename = routeMap.get(method);//根据 method 获取到对应的服务
        context.put("serviceId", servicename);//设置要转发的服务的id
        //context.put(FilterConstants.REQUEST_URI_KEY,insideApiUrl+requesturI.toString());//跳转到服务中的哪个地址
        //String requestURI = request.getRequestURI();
        String api = request.getParameter("api"); //api 代表我想请求什么地址
        String path = urlMap.get(api);//根据 api 获取到地址
        //Object requesturI = context.get(FilterConstants.REQUEST_URI_KEY);//等同于上面的两行代码,获取requesturi,因为我们的是rest风格.所以可以将一些参数通过请求地址的方式传递过来
       // System.out.println(requesturI);
        String requestURI2 = request.getRequestURI();
        //System.out.println("22222=====>"+requestURI2);
        context.put(FilterConstants.REQUEST_URI_KEY,path+requestURI2);//跳转到服务中的哪个地址
        context.set("name", "zhangsan");//向下游服务传递参数
        return "aaaaaaa";
    }
}

```





##  十一 灰度发布

> 在我们的实际开发中,我们会遇到要是上线一个新功能,但是又不能一下子给所有用户测试,所以会先开放给小范围测试,然后逐渐开放至所有用户,所以我们需要一种方案能让我们自定义什么用户可以访问什么版本



### 11.1 导入依赖



```xml
 		<dependency>
            <groupId>io.jmnarloch</groupId>
            <artifactId>ribbon-discovery-filter-spring-cloud-starter</artifactId>
            <version>2.1.0</version>
        </dependency>
```





### 11.2 给服务添加标识

> 我们要针对不同的情况分发到不同的服务,所以每个版本的服务必须有独立的标识,而这些服务本身名字是一样的,所以我们需要一个其他的标识来帮助我们,给服务通过添加元数据集合来区分



```yaml
eureka:
  client:
    serviceUrl:
      defaultZone: http://localhost:10000/eureka/
  instance:
    metadata-map: #服务的元数据集合
      forward: 1 #随便写key和value，但是不同版本不要出现重复,其中key就是我们用于区分的标识, value就是区分的值
```





### 11.3 网关中进行判断

> 我们在网关中通过前置过滤器对我们需要进行灰度的情况进行区分,然后转发到对应的服务上面



```java
/**
 * Created by jackiechan on 2021/12/31 16:41
 *
 * @author jackiechan
 * @version 1.0
 * @since 1.0
 */
@Component
public class GrayReleaseFIlter extends ZuulFilter {

    private List<String> forws = new ArrayList<>();

    public GrayReleaseFIlter() {
//        for (int i = 0; i < 5; i++) {
//            forws.add("1");
//        }
//        for (int i = 0; i < 10; i++) {
//            forws.add("2");
//        }
//        for (int i = 0; i < 30; i++) {
//            forws.add("3");
//        }
//        Collections.shuffle(forws);
    }

    /**
     * 返回的就是当期那过滤器的类型
     *
     * @return
     */
    @Override
    public String filterType() {
        return FilterConstants.PRE_TYPE;
    }

    /**
     * 返回过滤器的顺序, 在相同的 type 的情况下,数字越小,优先级越高
     *
     * @return
     */
    @Override
    public int filterOrder() {
        return FilterConstants.PRE_DECORATION_FILTER_ORDER + 200;//只要比这PRE_DECORATION_FILTER_ORDER过滤器大就行
    }

    /**
     * 当前过滤器是否开启,如果返回 false,则不开启,就不会执行对应的操作
     * 此处应该根据实际的业务需求来决定是否开启
     *
     * @return
     */
    @Override
    public boolean shouldFilter() {
        //必须是有灰度的服务和地址以及前面没有拦截才会开启这个过滤器,并且把规则计算出来
        //获取到要请求的服务的id
        //获取到真正要请求的地址
        //判断当前服务和当前地址在不在我们的灰度数据中（我们保存的可能是restful的地址，但是获取到的地址是真实地址）

        //如果在就把规则获取出来，按照我们的要求生成集合数据，保存起来

        return true;
    }

    /**
     * 如果过滤器开启了,则会执行当前方法
     *
     * @return 无意义, 不是返回给用户的
     * @throws ZuulException
     */
    @Override
    public Object run() throws ZuulException {
//        //我们的目标是根据用户的标识将用户分配到不同的版本的服务上
//        //用户的标识是什么，如何确定不同的服务，我们的服务都是一个名字，但是我们现在找不到它，因为我们没有办法区分出来用户真正要去的是哪个
//        // 所以我们的服务也得有唯一标识，相同版本的服务使用一个标识，我们确定好哪个标识即可
//        //假设我们的用户在请求的时候会传递一个参数叫 forw，这个的值就代表它期望去哪个服务
//        HttpServletRequest request = RequestContext.getCurrentContext().getRequest();
//        String forw = request.getParameter("forw");//获取的就是用户想去的，我们这里是1 2 3
//        if (forws != null && forws.size() > 0) {
//            String forw = forws.remove(0);
//            System.err.println(forw+"---->"+forws.size());
//            RibbonFilterContextHolder.getCurrentContext().add("forward", forw);//其中forward就是我们在配置文件中给每个服务定义的元数据的key，后面的值就是想去的服务的值
//        }
        //当这个方法一旦执行的时候，就意味着当前访问的服务和地址是确定有灰度参数
        //获取到灰度的参数，得到每个标识和权重比例
        //计算出当前的值，问题是怎么计算,我们可以用洗牌的方式，比如一个标识为a的版本是10一个标识为b的版本是50，我就向一个集合中存放10个a和50个b然后打乱顺序，从里面开始获取数据，得到的就是当前要去的
        return null;
    }
}
```





