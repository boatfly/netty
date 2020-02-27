# Netty核心技术和源码分析

## Netty
- 本质上是一个NIO框架。
- `异步的`、基于`事件驱动`的网络应用框架，用于快速开发高性能、高可靠的网络ioc程序。
- 主要针对在TCPx协议下，面向clientd端的高并发应用，或者peer-to-peer场景下的大量数据持久传输的y应用。

`TCP/IP -> JDK原生IO -> NIO -> Netty`
### I/O模型
Java共支持三种网络编程I/O模式
- BIO
  - 同步并阻塞
- NIO
  - 同步非阻塞
    - 多路复用选择器 Selector
    - [java-NIO](images/nio.png)
- AIO
  - 异步非阻塞
  
## code
更多笔记，包含在com.boatfly.codehub.netty.doc目录下。
