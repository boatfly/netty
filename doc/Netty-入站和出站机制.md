# Netty-入站和出站机制

## Netty主要组件
- Channel
- EventLoop
- ChannelFuture
- ChannelHandler
- ChannelPipe

## ChannelHandler
ChannelHandler充当了处理入站和出站数据的应用程序逻辑的容器。
实现了ChannelInboundHandler(或者ChannelInboundHandlerAdapter)，就可以接受入站事件和操作。

## ChannelPipe
ChannelPipe提供了ChannelHandler链的容器。
- 以客户端的视角来看：如果事件的运动方向是从客户端到服务器端的，那么就称这些事件为出站；反之，称为入站；
- decoder(pipeline里的第一个handler) -> ChannelInboundHandler
- encoder  -> ChannelOutboundHandler

