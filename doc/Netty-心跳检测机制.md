# Netty 心跳检测机制

## IdleStateHandler
Netty提供的处理空闲状态的处理器
> Triggers an IdleStateEvent when a Channel has not performed read, write, or both operation for a while.

参数说明：
- readerIdleTime 
  - 表示多长时间没有读，就会发送一个心跳检测包检测是否连接
- writerIdleTime 
  - 表示多长时间没有写，就会发送一个心跳检测包检测是否连接
- allIdleTime 
  - 表示多长时间没有读或者写，就会发送一个心跳检测包检测是否连接
  
### 自定义处理空闲检测Handler
- 加入一个对空闲检测更进一步处理的自定义handler
- 当IdleStateEvent 触发后，就会传递给管道的下一个处理器
- 因此自定义检测空闲处理器要紧跟IdleStateHandler
- 通过调用下一个处理器的 userEventTriggered(读空闲，写空闲，读写空闲) 方法

```java
public class MyServerHeartBeatHandler extends ChannelInboundHandlerAdapter {
    /**
     * @param ctx
     * @param evt 读空闲，写空闲，读写空闲
     * @throws Exception
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            String eventType = null;
            switch (event.state()) {
                case READER_IDLE:
                    eventType = "readIdle";
                    break;
                case WRITER_IDLE:
                    eventType = "writeIdle";
                    break;
                case ALL_IDLE:
                    eventType = "readwriteIdle";
                    break;
            }
            System.out.println(ctx.channel().remoteAddress() + "----超时事件--" + eventType);
            //@TODO 服务器做相应处理
        }
    }
}
```