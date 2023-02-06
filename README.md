# springCloudAliSample
基于alibaba的springcloud实例

# 一、框架搭建

0.端口分配

- 服务 8000开始

- nacas

- 

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





