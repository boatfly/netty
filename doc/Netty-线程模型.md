# Netty 线程模型

## 线程模型
 - 传统阻塞I/O服务模型
 - Reactor模式
   - 单Reactor单线程
   - 单Reactor多线程
   - 主从Reactor多线程
     - Netty基于此模式，并做了改进。

### Reactor模式
- NIO 多路复用
  - 反应器模式、分发者模式、通知者模式
- 基于线程池复用线程资源

### Reactor核心组成
- Reactor
  - 负责监听和分发事件
- Handlers
  - 处理程序执行I/O事件要完成的实际事件，执行非阻塞操作。
  
#### 单Reactor单线程
```
client - > Reactor（single） -> Acceptor -> accept()
                            -> Handlers -> read()
                                        -> 业务处理()
                                        -> send()

```
#### 单Reactor多线程
```
client - > Reactor（single） -> Acceptor -> accept()
                            -> Handlers -> read()
                                        <-> worker线程池 <-> worker线程 <-> 业务处理()
                                        -> send()

```
#### 主从Reactor多线程
```
client - > Reactor（主） -> Acceptor -> accept()
              |
           SubReactor(01)   -> Handlers -> read()
                                    <-> worker线程池 <-> worker线程 <-> 业务处理()
                                    -> send()
           SubReactor(0n)   -> Handlers -> read()
                                    <-> worker线程池 <-> worker线程 <-> 业务处理()
                                    -> send()

```
![Using multiple Reactors](doc/reactor/multi-reactor-mode.png)
form 《scalable IO in Java》 author：Doug Lea

### Netty 模型
Netty主要基于主从Reactor多线程，做了一定改进，其中主从Reactor多线程模型有多个Reactor。

#### Simple
- BossGroup线程维护Selector，只关注Accept。
- 当接收到Accept事件，获取到对应的SocketChannel，封装成NIOSocketChannel，并注册到workerGroup Selecor（事件循环，并由其进行维护）。
- 当worker线程监听到selector中通道发生自己感兴趣的事件后，就进行处理（分派handler） 

#### Complex
- Netty会抽象出两组线程池
  - BossGroup
    - 接收客户端的连接
  - WorkerGroup
    - 负责网络的Read/Write
- BossGropu & WorkerGroup 类型都是 NioEventLoopGroup
- NioEventLoopGroup相当于一个事件循环组，这个组中含有多个事件循环 NioEventLoop
- NioEventLoop 表示一个不断循环的执行处理任务的线程。每个NioEventLoop都有一个Selector，用于监听绑定在其上的socket网络通讯。
- 每个BossGroup的NioEventLoop执行的步骤：
  - 轮询accept事件
  - 处理accept事件，与client建立连接，生成NioSocketChannel，并将其注册到某个wworker NioEventLoop的selector上
  - 处理任务队列中的其他任务，runAllTasks
- 每个Worker NioEventLopp执行的步骤：
  - 轮询read/write事件
  - 处理i/o事件，即read/write事件，在对应的NioSocketChannel上进行处理。
  - 处理任务队列中的其他任务，runAllTasks  
- 每个Worker NioEventLopp处理业务时，会使用pipeline（管道），pipeline中包含了channel，即通过pipeline可以获取对应的通道。管道中维护了很多的处理器（Handler）。

