# Netty模型-任务队列

TaskQueue的`物理`位置：
ChannelHandlerContext:ctx -> pipeline -> channel -> eventloop -> taskQueue

## 任务队列:TaskQueue
- 用户程序自定义的普通任务
- 用户自定义定时任务
- 非当前Reactor调用Channle的各种方法
  - 例如：推送系统中，根据用户标识，找到对应的user channel，然后调用其write类方法向该用户推送消息。最终的write会提交到任务队列中被异步消费。

### 用户程序自定义的普通任务

```java

ctx.channel().eventLoop().execute(()->{
    try {
        TimeUnit.SECONDS.sleep(7);
        ctx.writeAndFlush(Unpooled.copiedBuffer("hello,client,武汉加油！+2", CharsetUtil.UTF_8));
    } catch (InterruptedException e) {
        e.printStackTrace();
    }
});
可以同时批次加入多个普通任务到TaskQueue中
```
### 用户自定义定时任务
和普通任务加入taskQueue队列中不同的是，定时任务加入的队列是scheduledTaskQueue。
```java
ctx.channel().eventLoop().schedule(() -> {
            ctx.writeAndFlush(Unpooled.copiedBuffer("hello,client,武汉加油！+4", CharsetUtil.UTF_8));
        }, 20, TimeUnit.SECONDS);
```
### 非当前Reactor调用Channle的各种方法
```java
serverBootstrap.group(bossGroup, bossGroup)
    .channel(NioServerSocketChannel.class) //使用NioSocketChannel作为服务器端channel的实现
    .option(ChannelOption.SO_BACKLOG, 128)//设置线程队列等待个数
    .childOption(ChannelOption.SO_KEEPALIVE, true)//设置保持活动连接状态
    .childHandler(new ChannelInitializer<SocketChannel>() {
        //给pipeline设置处理器
        @Override
        protected void initChannel(SocketChannel socketChannel) throws Exception {
            //在此处可以将socketChannel加入一个集合中进行统一管理，之后可以根据特定表示进行召回
            socketChannel.pipeline().addLast(new NettyServerHandler());
        }
    });//为workgroup的eventloop对应的管道设置处理器
```


