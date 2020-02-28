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


