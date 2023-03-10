# springCloudAliSample
基于alibaba的springcloud实例

# 一、框架搭建

0.端口分配

- 服务 8000开始

- nacas 8848

- sentinel 8858

### 1.在父项目中添加所依赖的版本

```
 <dependencyManagement>
      <dependencies>
          <dependency>
              <groupId>com.alibaba.cloud</groupId>
              <artifactId>spring-cloud-alibaba-dependencies</artifactId>
              <version>2.2.9.RELEASE</version>
              <type>pom</type>
              <scope>import</scope>
          </dependency>
         <dependency>
             <groupId>com.springframework.cloud</groupId>
             <artifactId>spring-cloud-dependencies</artifactId>
             <version>Hoxton.SR12</version>
             <type>pom</type>
             <scope>import</scope>
         </dependency>
     </dependencies>
 </dependencyManagement>
```

### 2.部署nacos

nacos 【端口8848】是一个集服务发现、服务配置、服务管理的平台，默认配置的存储是内存，可通过application.properties文件修改存储方式。


- 下载 ：https://github.com/alibaba/nacos/releases

- 启动 ：linux|sh startup.sh -m standalone、windows|startup.cmd -m standalone

> 如果不想在启动时加参数，可以直接修改startup里面的set MODE="cluster"为"standalone"


### 3.新增order、stock模块

- 添加依赖
```
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>

<dependency>
    <groupId>com.alibaba.cloud</groupId>
    <artifactId>spring-cloud-starter-alibaba-nacos-discovery</artifactId>
</dependency>
```

### 4.增加负载均衡 ribbon

> Ribbon是一套客户端的负载均衡工具，提供了完善的功能，如超时、重试等，通过Load Balancer获取服务提供的所有机器实例，并基于某种规则(轮询、随机、加权轮询、地址hash、最小链接数)去调用。

### 5.增加openFeign

- 添加依赖

```
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-openfeign</artifactId>
</dependency>
```

- 编写接口

order项目中
```java
package com.soopen.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * name rest接口所在的服务名
 * path 所在的controller
 */
@FeignClient(name = "stock-service",path = "/stock")
public interface StockFeignService {

    //与rest接口对应的方法
    @RequestMapping("get")
    String getStock();


    /*@RequestMapping("get")
    public String getStock() {
        System.out.println("查询库存");
        return "查询库存"+port;
    }*/
}

```

stock项目中
```java
package com.soopen.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient(name = "order-service",path = "/order")
public interface OrderFeignService {

    @RequestMapping("get")
    String getOrder();
}

```

- 注入

在orderController中注入

```java
 @Autowired
 StockFeignService stockFeignService;
```

在stockController中注入

```java
@Autowired
OrderFeignService orderFeignService;
```

并在项目启动类中加入注解

```java
@EnableFeignClients
```

### 6.feign日志配置

- 配置类的方式

1.全局配置

```java
/**
 * 全局配置添加@Configuration
 */
@Configuration
public class FeignConfig {

    @Bean
    public Logger.Level feignLoggerLevel(){
        return Logger.Level.FULL;
    }
}
```

2.局部配置

在具体的类上添加configuration= FeignConfig.class

3.注意

要在application.yml中配置项目的日志级别

```yaml
logging:
  level:
    com.soopen.feign: debug
```

4.注解方式

```yaml
#日志局部配置
feign:
  client:
    config:
      product-service:
        loggerLevel: BASIC
```

- 超时配置

```
@Configuration
public class FeignConfig {

    @Bean
    public Request.Options options(){
        return new Request.Options(5L, TimeUnit.SECONDS, 10L,TimeUnit.SECONDS,true);
    }
}
```

```yaml
feign:
  client:
    config:
      order-service: #服务名
        connectTimeout: 5000   #连接超时时间，默认2S
        readTimeout: 10000     #请求处理超时间，默认5s
```

- openfeign中自定义拦截器

1.添加FeignAuthReuestInterceptor类

2.在FeignConfig中添加

```java
@Bean
public FeignAuthReuestInterceptor feignAuthReuestInterceptor(){
    return new FeignAuthReuestInterceptor();
}
```

3.也可以在yml文件中配置

```yaml
feign:
  client:
    config:
      order-service: #服务名
        requestInterceptors[0]: #配置拦截器
          com.soopen.interceptor.FeignAuthReuestInterceptor
```

### 7.组件sentinel

sentinel是面向分布式服务架构的流量控制组件，主要以流量为切入点，从限流、流量整形、熔断降级、系统负载保护、热点防护等多个维度来帮助开发者保障微服务的稳定性。

- 下载安装

https://sentinelguard.io/zh-cn/docs/introduction.html

- 整合springcloud

1.添加依赖

```xml
<dependency>
    <groupId>com.alibaba.cloud</groupId>
    <artifactId>spring-cloud-starter-alibaba-sentinel</artifactId>
</dependency>
```

2.yml配置，设置sentinel控制台地址

```yaml
  sentinel:
      transport:
        dashboard: 127.0.0.1:8858
```

- 自定义流控信息

要被监控就要加上@SentinelResource

```java
 @RequestMapping("/flow")
@SentinelResource(value = "flow",blockHandler = "flowBlockHandler")
public String flow(){
    return "正常访问";
}

public String flowBlockHandler(BlockException e){
    return "自定义流控信息";
}
```

- 统一异常处理

```java
public class MyBlockExceptionHandler implements BlockExceptionHandler
```

- sentinel整合openfeign做降级处理

1.yml中配置

```yml
feign:
  sentinel:
    enabled: true  #添加对feign的支持
```

2.创建类StockFeignServiceFallback

```java
@Component
public class StockFeignServiceFallback implements StockFeignService{
    @Override
    public String getStock() {
        return "降级了！！！";
    }

    @Override
    public String reduceStock() {
        return "降级了！！！";
    }
}
```

3.在openfeign的类中加入fallback = StockFeignServiceFallback.class

```java
@FeignClient(name = "stock-service",path = "/stock",configuration= FeignConfig.class,fallback = StockFeignServiceFallback.class)
public interface StockFeignService {

    //与rest接口对应的方法
    @RequestMapping("get")
    String getStock();

    @RequestMapping("reduce")
    String reduceStock();
}
```

### 8.sentinel持久化

1.引入依赖

```yaml
<dependency>
    <groupId>com.alibaba.csp</groupId>
    <artifactId>sentinel-datasource-nacos</artifactId>
</dependency>
```

2.nacos中新建配置

3.

### 9.Seate集成

Seata 是一款开源的分布式事务解决方案，致力于提供高性能和简单易用的分布式事务服务。Seata 将为用户提供了 AT、TCC、SAGA 和 XA 事务模式，为用户打造一站式的分布式解决方案。 

- 下载安装

https://github.com/seata/seata/releases/download/v1.5.2/seata-server-1.5.2.zip

```xml
<dependency>
    <groupId>com.alibaba.cloud</groupId>
    <artifactId>spring-cloud-starter-alibaba-seata</artifactId>
</dependency>
```

- 配置存储为数据库

conf文件夹下application.yml中修改，具体可参考application.example.yml

- 注册使用nacos

http://seata.io/zh-cn/docs/ user/registry/nacos.html

- nacos参数配置

http://seata.io/zh-cn/docs/user/configuration/nacos.html

- 建表语句

https://github.com/seata/seata/blob/master/script/server/db/mysql.sql




