# Netty 异步模型

## 异步模型
- 当一个异步过程调用发出后，调用者不能立即得到结果。实际处理这个调用的组件在完成后，通过状态、通知或者回调来通知调用者。
- Netty中的I/O操作是异步的，包括Bind、Write、Connect等操作会简单的返回一个ChannelFuture。
- Netty的异步模型是建立在future和callback基础之上的。
  - callback就是回调。
  - Future
    - 核心思想
      - 假设一个Fun，计算过程非常耗时，等Fun返回结果不现实。在调用Fun时，马上返回一个Future，后续可以通过Future去监控方法Fun的处理过程。
      - Future-Listener机制
      - `ChannelFuture future = serverBootstrap.bind(9997).sync();`
      - `ChannelFuture future = bootstrap.connect(new InetSocketAddress("127.0.0.1", 9997)).sync();`
      
## Future
- 表示异步的执行结果。可以通过它提供的方法检测执行是否完成。
- ChannelFuture
  - 是一个接口：public interface ChannelFuture extends Future<Void>
  - 我们可以添加监听器，当监听的事件发生时，就会通知监听器。
  
## 工作原理
- 在使用Netty进行编程时，拦截操作和转换出入站数据只需提供callback或者利用future即可。这使得链式操作简单、高效，并有利于编写可重用、通用代码；
- Netty框架的目标：让业务逻辑从网络基础应用编码中分离出来。

### Future-Listener机制
当Future对象刚刚创建时，处于非完成状态，调用者可以通过返回的ChannelFuture来获取操作执行的状态，注册监听器来执行完成后的操作。

常见操作：
- isDone
- isSuccess
- getCause 获取当前操作失败的原因
- isCancelled
- addListener 注册监听器


      

      
