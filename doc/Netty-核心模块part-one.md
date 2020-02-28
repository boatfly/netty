# Netty 核心模块 PART-ONE

## Bootstrap ServerBootStrap
配置整个Netty程序。
client：Bootstrap；server：ServerBootStrap

## Future 、ChannelFuture
- channel()
  - 返回当前正在进行I/O操作的通道
- sync()
  - 等待异步操作执行完毕

## Channel
- NioSocketChannel
- NioServerSocketChannel
- NioDatagramChannel

## Selectdor
选择器、多路复用器
- 循环轮询事件

## ChannelHandler
- 是一个接口，处理I/O事件或者拦截I/O操作，并将其转发到其ChannelPipeline（业务处理链）中的下一个处理程序。
  - ChannelInboundHandler
    - 用于处理入站I/O事件
  - ChannelOutboundHandler
    - 用于处理出站I/O事件
  - 适配器
    - ChannelInboundHandlerAdapter
    - ChannelOutboundHandlerAdapter
    - ChannelDuplexHandlerAdapter

## Pipeline ChannelPipeline
- ChannelPipeline 是一个Handler的集合，它负责处理和拦截inbound或者outbound的事件和操作，相当于一个贯穿的netty的链。
- 由ChannelHandlerContext维护一个双向链表
- 入站事件和出站事件在一个双向链表中
  - addFirst()
  - addLast()

## ChannelHandlerContext
- 保存Channel相关的所有上下文信息，同时关联一个ChannelHandler对象

## ChannelOption
创建Channel实例后，需要设置ChannelOption参数。
- option(ChannelOption.SO_BACKLOG, 128)//设置线程队列等待个数
- childOption(ChannelOption.SO_KEEPALIVE, true)//设置保持活动连接状态

## EventLoopGroup NioEventLoopGroup
- 默认轮询机制 由next()方法具体提供算法支持。


