package com.boatfly.codehub.netty.examples.netty.heartbeat;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleStateEvent;

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
