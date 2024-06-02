## 一 WebFlux概述

### 1.1 简介

- WebFlux是Spring5新添加的模块以用于web开发，功能和SpringMVC类似。
- Webflux使用**响应式编程**的框架。
- Webflux 是一种**异步非阻塞**的框架，异步非阻塞的框架在 Servlet3.1 以后才支持，核心是基于Reactor的相关 API 实现的。

### 1.2 异步非阻塞

**异步和同步**针对调用者，调用者发送请求，如果等着对方回应之后才去做其他事情就是同步，如果发送请求之后不等着对方回应就去做其他事情就是异步。

**阻塞和非阻塞**针对被调用者阻塞和非阻塞针对被调用者，被调用者受到请求之后，做完请求任务之后才给出反馈就是阻塞，收到请求之后马上给出反馈然后再去做事情就是非阻塞。

## 二 SpringWebflux执行流程和核心API

SpringWebflux基于 Reactor，默认使用容器是 Netty，Netty是高性能的 NIO(同步非阻
塞) 

### 2.1 执行流程

SpringWebflux核心控制器DispatchHandler，实现接口WebHandler，接口中有个handler方法:

```java
public interface WebHandler {
    Mono<Void> handle(ServerWebExchange var1);
}
```



`实现`

```java
public Mono<Void> handle(ServerWebExchange exchange) {// exchange放http请求信息
    return this.handlerMappings == null ? this.createNotFoundError() : Flux.fromIterable(this.handlerMappings).concatMap((mapping) -> {
        return mapping.getHandler(exchange);//根据请求地址获取对应mapping
    }).next().switchIfEmpty(this.createNotFoundError()).flatMap((handler) -> {
        return this.invokeHandler(exchange, handler);//调用具体的业务方法
    }).flatMap((result) -> {
        return this.handleResult(exchange, result);//处理结果返回
    });
}
```



### 2.2  处理流程

SpringWebflux里面DispatcherHandler类负责请求的处理，具体如下：

|          类          |         功能         |
| :------------------: | :------------------: |
|    HandlerMapping    | 请求查询到处理的方法 |
|    HandlerAdapter    |   真正负责请求处理   |
| HandlerResultHandler |     响应结果处理     |



Spring Webflux 实现函数式编程，两个接口：



|       类        |                  功能                  |
| :-------------: | :------------------------------------: |
| RouterFunction  | 路由处理 ,主要是设置请求地址映射等相关 |
| HandlerFunction |   处理函数,设置什么类上面方法来处理    |



## 三  SpringWebflux使用

### 3.1  基于注解模式使用



#### 3.1.1 介绍

> SpringWebflux 使用注解编程模型方式，和之前 SpringMVC使用相似的，只需要把相关依赖配置到项目中，SpringBoot自动配置相关运行容器，默认情况下使用 Netty服务器。SpringMVC和SpringWebflux对比如下

- SpringMVC 方式实现，同步阻塞的方式，基于SpringMVC+Servlet+Tomcat
- Spring Webflux 方式实现，异步非阻塞 方式，基于SpringWebflux+Reactor+Netty



#### 3.1.2 导入依赖

> 需要springboot项目



```xml
<dependency>
   <groupId>org.springframework.boot</groupId>
   <artifactId>spring-boot-starter-webflux</artifactId>
</dependency>
```



#### 3.1.3 数据类

> 用于操作的数据类

```java
public class User {
    private String name;
    private String gender;
    private Integer age;

    public User(String name, String gender, Integer age) {
        this.name = name;
        this.gender = gender;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }
}
```



#### 3.1.4 service接口



```java
public interface UserService {
    //根据 id 查询用户
    Mono<User> getUserById(int id);
    //查询所有用户
    Flux<User> getAllUser();
    //event流式数据
     Flux<User> getUsersStream();
    //添加用户
    Mono<Void> saveUserInfo(Mono<User> user);
}
```



#### 3.1.5 service实现



```java
@Service
public class UserServiceImpl implements UserService {

    //创建 map 集合存储数据
    private final Map<Integer,User> users = new HashMap<>();
    public UserServiceImpl() {
        this.users.put(1,new User("lucy","female",20));
        this.users.put(2,new User("mary","female",25));
        this.users.put(3,new User("jack","male",30));
    }

    //根据 id 查询
    @Override
    public Mono<User> getUserById(int id) {
        return Mono.justOrEmpty(this.users.get(id));
    }

    //查询多个用户
    @Override
    public Flux<User> getAllUser() {
        return Flux.fromIterable(this.users.values());
    }

     @Override
    public Flux<User> getUsersStream() {
        return Flux.fromIterable(this.users.values()).doOnNext(user -> {
            try {
                //模拟数据的获取需要消耗时间,一条一条的获取过来的
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
    }
    //添加用户
    @Override
    public Mono<Void> saveUserInfo(Mono<User> userMono) {
        return userMono.doOnNext(person -> {
            //向 map 集合里面放值
            int id = users.size()+1;
            users.put(id,person);
        }).thenEmpty(Mono.empty());
    }
}
```



#### 3.1.6 controller



```java
@RestController
public class UserController {

    private  UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    //id 查询
    @GetMapping("/user/{id}")
    public Mono<User> geetUserId(@PathVariable int id) {
        return userService.getUserById(id);
    }

    //查询所有
    @GetMapping("/getusers")
    public Flux<User> getUsers() {
        return userService.getAllUser();
    }

      //查询所有,通过flux流的方式一条一条返回
    @GetMapping(value ="/getusers/stream",produces=MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<User> getUsersStream() {
        return userService.getUsersStream();
    }
    
    //添加
    @PostMapping("/saveuser")
    public Mono<Void> saveUser(@RequestBody User user) {
        Mono<User> userMono = Mono.just(user);
        return userService.saveUserInfo(userMono);
    }
}
```



#### 3.1.7 启动类



```java
@SpringBootApplication
public class WebfluxdemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebfluxdemoApplication.class,args);
    }
}
```



### 3.2 基于函数式编程

`此方式中User和Service和上面一致,主要是handler不再添加requestmapping`

> 在使用函数式编程模型操作时候，需要自己初始化服务器。基于函数式编程模型时候，有两个核心接口：RouterFunction（实现路由功能，请求转发给对应的 handler）和 HandlerFunction（处理请求生成响应的函数）。核心任务定义两个函数式接口的实现并且启动需要的服务器。SpringWebflux 请 求 和 响 应 不 再 是 ServletRequest和ServletResponse ，而是ServerRequest 和 ServerResponse。



#### 3.2.1 具体步骤

- 创建Handler(在这里写具体实现方法)
- 创建Router路由，并初始化服务器做适配
  - 创建Router路由
  - 创建服务器做适配
  - 启动主程序



#### 3.2.2 Handler

> 类似于前面的controller,只不过没有指定地址映射
>
> 此处使用的是springboot模式

```java
@Component
public class UserHandler {
    private final UserService userService;

    @Autowired
    public setUserService(UserService userService) {
        this.userService = userService;
    }

    //根据 id 查询
    public Mono<ServerResponse> getUserById(ServerRequest request) {
        //获取 id 值
        int userId = Integer.valueOf(request.pathVariable("id"));
        //空值处理
        Mono<ServerResponse> notFound = ServerResponse.notFound().build();
        //调用 service 方法得到数据
        Mono<User> userMono = this.userService.getUserById(userId);
        //把 userMono 进行转换返回
        //使用 Reactor 操作符 flatMap
        return
                userMono.flatMap(person -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(person))
                        .switchIfEmpty(notFound);
    }

    //查询所有
    public Mono<ServerResponse> getAllUsers(ServerRequest request) {
        //调用 service 得到结果
        Flux<User> users = this.userService.getAllUser();
        return
                ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(users);
    }

    //添加
    public Mono<ServerResponse> saveUser(ServerRequest request) {
        //得到 user 对象
        Mono<User> userMono = request.bodyToMono(User.class);
        return
                ServerResponse.ok().build(this.userService.saveUserInfo(userMono));
    }
}
```



#### 3.2.3  普通方式启动

> 可以通过java原生代码的方式来编写服务端启动程序

##### 3.2.3.1 服务端

> 这种方式是手动启动的方式,没有依赖于springboot

```java
public class Server {

    public static void main(String[] args) throws Exception{
        Server server = new Server();
        server.createReactorServer();
        System.out.println("enter to exit");
        System.in.read();
    }

    //1 创建Router路由
    public RouterFunction<ServerResponse> routingFunction() {
        //创建service和hanler对象
        UserService userService = new UserServiceImpl();
        UserHandler handler = new UserHandler();
        handler.setUserService(userService);
        //设置路由
        return RouterFunctions.route(
                 RequestPredicates.GET("/users/{id}").and(accept(APPLICATION_JSON)),handler::getUserById)
                .andRoute( RequestPredicates.GET("/users").and(accept(APPLICATION_JSON)),handler::getAllUsers);
    }

    //2 创建服务器完成适配
    public void createReactorServer() {
        //路由和handler适配
        RouterFunction<ServerResponse> route = routingFunction();
        HttpHandler httpHandler = toHttpHandler(route);
        ReactorHttpHandlerAdapter adapter = new ReactorHttpHandlerAdapter(httpHandler);
        //创建服务器
        HttpServer httpServer = HttpServer.create();
        httpServer.handle(adapter).bindNow();
    }
}
```



##### 3.2.3.2 客户端

> 通过手动创建web客户端的方式来访问我们的服务端, 这里是客户端的代码, 与服务端的启动方式无关

```java
public class Client {

    public static void main(String[] args) {
        //调用服务器地址
        WebClient webClient = WebClient.create("http://127.0.0.1:5794");

        //根据id查询
        String id = "1";
        User userresult = webClient.get().uri("/users/{id}", id)
                .accept(MediaType.APPLICATION_JSON).retrieve().bodyToMono(User.class)
                .block();
        System.out.println(userresult.getName());

        //查询所有
        Flux<User> results = webClient.get().uri("/users")
                .accept(MediaType.APPLICATION_JSON).retrieve().bodyToFlux(User.class);

        results.map(stu -> stu.getName())
                    .buffer().doOnNext(System.out::println).blockFirst();
    }
}
```





#### 3.2.4 Springboot模式

> 通过springboot的方式来指定地址映射

```java
@Configuration
public class FluxConfig {

    private UserHandler userHandler;

    @Autowired
    public void setUserHandler(UserHandler userHandler) {
        this.userHandler = userHandler;
    }

    @Bean
    public RouterFunction<ServerResponse> routingFunction() {
        //设置路由
        return RouterFunctions.route(
                        RequestPredicates.GET("/users/{id}").and(accept(APPLICATION_JSON)),UserHandler::getUserById)
                .andRoute( RequestPredicates.GET("/users").and(accept(APPLICATION_JSON)),UserHandler::getAllUsers);
    }
}

```



`主程序`

```java
@SpringBootApplication
public class WebfluxdemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebfluxdemoApplication.class,args);
    }
}
```



## 四 R2DBC



​			**Spring** 官方在 **Spring 5** 发布了响应式 **Web** 框架 **Spring WebFlux** 之后急需能够满足异步响应的数据库交互 **API** 。由于缺乏标准和驱动，**Pivotal（Spring 官方）** 团队开始研究响应式关系型数据库连接（**Reactive Relational Database Connectivity**），并提出了 **R2DBC** 规范 **API** 以评估可行性并讨论数据库厂商是否有兴趣支持响应式的异步非阻塞驱动程序。最开始只有 **PostgreSQL** 、**H2**、**MSSQL** 三家，现在 **MySQL** 也加入了进来。除了驱动实现外还提供了 ***\*R2DBC\** 连接池** [3] 和 **R2DBC 代理[4]**。除此之外还支持云原生应用。



### 4.1  创建项目

#### 4.1.1 导入依赖

> 项目为springboot项目

```xml
    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-webflux</artifactId>
        </dependency>
        <dependency>
            <groupId>io.asyncer</groupId>
            <artifactId>r2dbc-mysql</artifactId>
            <version>1.1.3</version>
        </dependency>
        <!--Spring r2dbc 抽象层-->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-r2dbc</artifactId>
        </dependency>
        <!--自动配置需要引入的一个嵌入式数据库类型对象-->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-jdbc</artifactId>
        </dependency>
    </dependencies>

```



#### 4.1.2  Pojo对象



```java
package com.qianefng.webflux.pojo;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDate;


@Table("user") //在Repository下使用
public class User {
    @Id //主键 在Repository下使用
    private Long uid;

    private String name;
    private int age;

    private String address;

    /**
     * 类型声明为LocalDate类型或者java.sql.Date,使用java.util.Date会报错,另外当我们的变量名存在大小写的时候,r2dbc默认会使用驼峰命名的方式去匹配表中的列名
     *
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthDay;


    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public LocalDate getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(LocalDate birthDay) {
        this.birthDay = birthDay;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}


```





#### 4.1.3 配置

> 我们需要配置数据库的连接信息等

##### 4.1.3.1 配置文件



```yaml
spring:
  r2dbc: #注意这里是r2dbc,下面没有驱动的类型,内部通过netty连接sql,Template模式有效
    url: r2dbcs:mysql://localhost:3306/webfluxtest?SSL=false&sslMode=DISABLED
    username: root
    password: qishimeiyoumima
```





##### 4.1.3.2 配置类模式

```java
@Configuration
public class MySQLConfig {

    @Bean
    ConnectionFactory connectionFactory() {
        return MySqlConnectionFactory.from(MySqlConnectionConfiguration.builder()
                .host("127.0.0.1")
                .port(3306)
                .username("root")
                .password("qishimeiyoumima")
                        .serverZoneId(ZoneId.of(ZoneId.SHORT_IDS.get("CTT")))
                .database("webfluxtest")//设置库
                // 额外的其它非必选参数省略
                .build());
    }
}

```





### 4.2 R2dbcEntityTemplate模式

> `此模式下官方要求spring版本是5.3.19,因为我们升级springboot为2.5.13版本`



#### 4.2.1 增删改查代码

> 代码中仅仅包含了操作sql的部分, controller等不包含

```java
@Service
@Transactional
public class UserServiceImpl implements UserService {

    private R2dbcEntityTemplate r2dbcEntityTemplate;

    @Autowired
    public void setR2dbcEntityTemplate(R2dbcEntityTemplate r2dbcEntityTemplate) {
        this.r2dbcEntityTemplate = r2dbcEntityTemplate;
    }

    /**
     * 添加数据
     * @param user
     * @return
     */
    @Override
    public void addUser(User user) {

         r2dbcEntityTemplate.insert(User.class)
                .into("user")
                .using(user);


    }




    /**
     * 查询所有数据
     * @return
     */
    @Override
    public Flux<User> findAllUser() {
        //默认是将类名从驼峰改成_来匹配表名
        return r2dbcEntityTemplate.select(User.class).from("user").all();
    }

    @Override
    public Mono<User> findById(Long id) {
        return r2dbcEntityTemplate.selectOne(Query.query(Criteria.where("uid").is(id)),User.class);
    }

    /**
     * 模糊查询
     * @param name
     * @return
     */
    @Override
    public Flux<User> findByNameLike(String  name) {
        return r2dbcEntityTemplate
                //返回值类型
                .select(User.class)
                //查询那张表
                .from("user")
                //查询条件
                .matching(Query.query(Criteria.where("name").like("%" + name + "%"))).all();
    }
    /**
     * 主键查询
     * @param name
     * @return
     */
    @Override
    public Mono<User> findByNameEquals(String name) {
        return r2dbcEntityTemplate.selectOne(Query.query(Criteria.where("name").is(name)), User.class);
    }
    /**
     * 根据条件删除
     * @param name
     * @return
     */
    @Override
    public Mono<Integer> deleteByNameEquals(String name) {
        return    r2dbcEntityTemplate.delete(User.class).
                from("user").
                matching(Query.query(Criteria.where("name").is(name))).all();
    }

    /**
     * 更新用户
     * @param user
     */
    @Override
    public void  updateUser(User user) {
        r2dbcEntityTemplate.update(User.class)
                .inTable("user")
                .matching(Query.query(Criteria.where("id").is(user.getUid())))
                .apply(
                        //要更新什么列,具体列自己可以根据情况做判断
                        Update.update("address", user.getAddress())
                                .set("age", user.getAge()).set("name",user.getName()))
                //要订阅下,不然数据不进入数据库
                .subscribe();
    }

    @Override
    public Mono<User> findFirstByNameLike(String name) {
        return r2dbcEntityTemplate
                //返回值类型
                .selectOne(Query.query(Criteria.where("name").like("%" + name + "%"))
                        //设置要返回的列
                        .columns("age","address"), User.class);
    }
}

```



### 4.3 Repository 模式

> 在jpa中,spring给我们提供了Repository可以帮我们简化sql的编写操作, 在r2dbc中也可以使用,不过r2dbc的功能暂时不完整,部分操作可能无法使用,注意与之对应的entity类中要添加Table和Id注解



```xml
   <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-jpa</artifactId>
        </dependency>
```





#### 4.3.1 定义Repository接口



```java
@Repository
public interface UserReactiveCrudRepository extends ReactiveCrudRepository<User,Long> {

    Mono<Integer> deleteAllByNameEquals(String name);

    /**
     * @Param("user") 给对象参数设置别名叫user,在sql中获取数据的时候要通过:#{#user.age}的方式来获取指定的属性, 其中user就是别名, age是属性名
     * 这里不能进行参数判断,所以在更新前我们需要先查询数据,然后将需要更新的内容设置到对象上面,再进行更新
     * @param user
     * @return
     */
    @Modifying//声明为当前方法是更新方法
    @Query("UPDATE `user` set name=:#{#user.name},  age=:#{#user.age},address=:#{#user.address},birth_day=:#{#user.birthDay} WHERE uid=:#{#user.uid}")
    Mono<Integer> updateByUid(@Param("user") User user);

    /**
     * 根据名字查询,这种简单的sql只需要按照规则编写方法名即可
     * @param name
     * @return
     */
    Mono<User> findByNameEquals(String name);
    /**
     * 注意name参数中需要自己拼接%%
     * @param name
     * @return
     */
    Flux<User> findByNameLike(String name);


    /**
     * 查询数据并返回第一个,可以通过注解来编写sql返回自定义列,此处用like 演示的,传递的参数中需要自己拼接%%
     *
     * @param name
     * @return
     */

    @Query("SELECT age,address,birth_day FROM `user` WHERE name  like :name ")
    //@Query("SELECT age,address,birth_day FROM `user` WHERE name  like :#{[0]}")
    //@Query("SELECT age,address,birth_day FROM `user` WHERE name  like $1")
    Mono<User> findFirstByNameLike(String name);

}
```





#### 4.3.2 Service实现类



```java
@Service
@Transactional
public class UserServiceImplWithRep implements UserService {

    private UserReactiveCrudRepository userReactiveCrudRepository;

    @Autowired
    public void setUserReactiveCrudRepository(UserReactiveCrudRepository userReactiveCrudRepository) {
        this.userReactiveCrudRepository = userReactiveCrudRepository;
    }

    /**
     * 查询所有
     * @return
     */
    @Override
    public Flux<User> findAllUser() {

        return userReactiveCrudRepository.findAll();
    }

    /**
     * 根据id查询
     * @param id
     * @return
     */
    @Override
    public Mono<User> findById(Long id) {
        return userReactiveCrudRepository.findById(id);
    }

    /**
     * 添加数据,如果是非自增的主键,传递了主键其实执行的是更新功能,因为主键不存在,所以更新失败
     * @param user
     */
    @Override
    public void addUser(User user) {
        userReactiveCrudRepository.save(user).subscribe();
    }

    /**
     * 根据名字模糊查新
     * @param name
     * @return
     */
    @Override
    public Flux<User> findByNameLike(String name) {
        //需要拼接%%
        return userReactiveCrudRepository.findByNameLike("%" + name + "%");
    }

    /**
     * 根据名字查询,名字是唯一的,所以此处返回mono
     * @param name
     * @return
     */
    @Override
    public Mono<User> findByNameEquals(String name) {
        return userReactiveCrudRepository.findByNameEquals(name);
    }

    /**
     * 根据名字删除
     * @param name
     * @return
     */
    @Override
    public Mono<Integer> deleteByNameEquals(String name) {
        return  userReactiveCrudRepository.deleteAllByNameEquals(name);
    }

    /**
     * 更新
     * @param user
     */
    @Override
    public void updateUser(User user) {
        //前置业务,比如先查询数据,因为这里不能写动态sql
        userReactiveCrudRepository.updateByUid(user).subscribe();
    }

    /**
     * 根据名字模糊查询并只返回部分列
     * @param name
     * @return
     */
    @Override
    public Mono<User> findFirstByNameLike(String name) {
        return userReactiveCrudRepository.findFirstByNameLike("%" + name + "%");
    }
}

```



#### 4.3.3 启用 R2dbcRepository

> 在主程序上添加注解@EnableR2dbcRepositories即可



```java
@SpringBootApplication
//启用R2dbcRepositories
@EnableR2dbcRepositories
public class StartApp {
    
    public static void main(String[] args) {
            SpringApplication.run(StartApp.class, args);
        }
}

```





### 4.4 显示日志

> 想要显示sql的日志,只需要在配置文件中添加 以下配置



```yaml
logging:
  level:
    ROOT: debug
```







### 4.5 	Mybatis Reactive

> Mybatis原生是不支持reactive的,因此Mybatis的响应式编程主要是第三方实现的
>
> https://github.com/chenggangpro/reactive-mybatis-support

#### 4.5.1 Reactive-Mybatis

- 在整合`r2dbc`和`mybatis`时，有一系列问题需要适配：
  - reactive项目中无法使用ThreadLocal，需要替换为`Context`，用于缓存`Connection`
  - reactive，属于数据流处理的形式，原有Mybatis中的处理逻辑是Blocking操作，需要替换为No-Blocking
  - 保留原有参数绑定和结果解析方案，并适配到reactive流处理过程中
  - `r2dbc-spi`驱动中，PreparedStatement中的占位符，不同驱动有不同的实现，而`JDBC`统一使用`?`为占位符，由驱动翻译占位符，这里需要转换为框架适配，否则适配不同的`r2dbc`驱动
  - 由于reactive本身就有cache性质，需要将`mybatis`中的cache剥离掉
  - 适配 `r2dbc` Data-Type，否则数据映射不成功
- 在整合`r2dbc`事物时，需要使用动态代理来实现缓存`Connection`到`Context`中，并保证，在一个事物中，`Connection`是同一个，这样才能实现`Transaction`
- 在解析返回结果集时，需要切换思路，由于数据是按照数据流的形式返回的，那么在合并和解析结果集时，需要合理缓存已解析的数据，从而可以保留原有`mybatis`的一对多映射功能
- 在适配`SelectKey`功能时，需要将原有的`before`和`after`操作，转移到executor中操作，原有的`SelectKey`功能是阻塞的，需要将流程贯穿到整个`executor`过程中，才能保留`before`和`after`操作
- 由于`r2dbc`的限制，要么只返回`自生成自增主键`，要么返回影响行数

#### 4.5.2 Reactive-Mybatis-Spring

- 整合到Spring的过程中，需要将Executor挂在到Spring的事物管理器上，并交由Spring管理
- Spring通过`ConnectionFactoryUtils.currentConnectionFactory(connectionFactory)`来判断，当前Context是否有事物管理
- Spring提供了`TransactionAwareConnectionFactoryProxy`来代理事物，需要将`ReactiveSqlSession`中使用的`ConnectionFactory`替换为`TransactionAwareConnectionFactoryProxy`才能和Spring的事物管理器一起使用



#### 4.5.3 mybatis-dynamic-sql

> mybatis-dynamic-sql是mybatis单独推出的一个框架,这是一种全新的模式，对java的版本要求最低是java 8。
>
> 该模式下不再生成 XML，不再生成 Example 类,XML中唯一我们需要写的就是定义ResultMap







##  五 Reactive Redis

> Spring Data Redis中同时支持了Jedis客户端和Lettuce客户端。但是仅Lettuce是支持Reactive方式的操作，所以如果你希望使用Reactive方式那你只能选择Lettuce客户端。
>
> 使用方式和之前的方式差不多

### 5.1 Spring Data Redis Reactive中的主要类

#### 5.1.1 ReactiveRedisConnection

​		ReactiveRedisConnection是Redis通信的核心，因为它处理与Redis后端的通信。 它还会自动将底层驱动程序异常转换为Spring一致的DAO异常层次结构，因此您可以在不更改任何代码的情况下切换连接器，因为操作语义保持不变。

#### 5.1.2 ReactiveRediscoverConnectionFactory

​		ReactiveRedisConnectionFactory创建活动的ReactiveRedisConnection实例。 此外，工厂还充当PersistenceExceptionTranslator实例，这意味着工厂一旦声明，就可以进行透明的异常转换-例如，通过使用@Repository批注和AOP进行异常转换。

#### 5.1.3 ReactiveRedisTemplate



### 5.2 创建项目

> 此项目为springboot项目,版本2.5.13



#### 5.2.1 pom

```xml
 		<dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-redis-reactive</artifactId>
        </dependency>
```



#### 5.2.2 配置文件

```yaml
  redis:
    host: 192.168.3.92
    port: 6379
    password: redis001
```



#### 5.2.3 基本使用

> 如果做基本的使用,则已经可以使用了,通过ReactiveRedisTemplate可以和之前的使用流程一样,只不过返回值变为了Mono或者Flux类型



##### 5.2.3.1 声明tempalte

```java
   private ReactiveRedisTemplate redisTemplate;

    @Autowired
    public void setRedisTemplate(ReactiveRedisTemplate reactiveRedisTemplate) {
        this.redisTemplate = reactiveRedisTemplate;
    }
```



### 5.3 其他配置

#### 5.3.1 配置类

> 配置类的方式来配置redis连接而不是通过配置文件



##### 5.3.1.1 基本连接



```java
@Bean
public ReactiveRedisConnectionFactory connectionFactory() {
  return new LettuceConnectionFactory("localhost", 6379);
}
```



##### 5.3.1.2 更多连接配置

>如果需要处理ssl、超时时间等问题可以使用`LettuceClientConfigurationBuilder`。

```java
@Bean
public ReactiveRedisConnectionFactory lettuceConnectionFactory() {
 LettuceClientConfiguration clientConfig = LettuceClientConfiguration.builder()
                .useSsl()//使用ssl
                .and()
                .commandTimeout(Duration.ofSeconds(2))//超时时间
                .shutdownTimeout(Duration.ZERO)
                .build();
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration("localhost", 6379);//服务器地址
        configuration.setPassword("redis001");//密码
       return new LettuceConnectionFactory(configuration, clientConfig);
}
```



#### 5.3.2 序列化方式

> ReactiveRedisTemplate默认使用的序列化是Jdk序列化，我们可以配置为json序列化



```java
@Bean
public RedisSerializationContext redisSerializationContext() {
    RedisSerializationContext.RedisSerializationContextBuilder builder = RedisSerializationContext.newSerializationContext();
    builder.key(StringRedisSerializer.UTF_8);////key的序列化方式为String,注意key我们一般不会序列化为json
    //设置具体的序列化类型,这样这个template就是专门序列化某种类型数据,在声明ReactiveRedisTemplate变量的时候就可以执行具体泛型
   // builder.value(new Jackson2JsonRedisSerializer<>(User.class));
    builder.value(RedisSerializer.json());//设置value的序列化方式
    builder.hashKey(StringRedisSerializer.UTF_8);//hashkey的序列化方式为String,注意key我们一般不会序列化为json
    builder.hashValue(StringRedisSerializer.UTF_8);//hashvalue的序列化方式为String

    return builder.build();
}
//重新创建template
@Bean
public ReactiveRedisTemplate reactiveRedisTemplate(ReactiveRedisConnectionFactory connectionFactory,RedisSerializationContext redisSerializationContext) {
    ReactiveRedisTemplate reactiveRedisTemplate = new ReactiveRedisTemplate(connectionFactory,redisSerializationContext);//根据上面的配置创建template
    return reactiveRedisTemplate;
}
```

