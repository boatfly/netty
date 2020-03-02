package com.boatfly.codehub.netty.examples.netty.dubborpc.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.concurrent.Callable;

public class NettyClientHandler extends ChannelInboundHandlerAdapter implements Callable {
    private ChannelHandlerContext channelHandlerContext;
    private String result;//调用服务端返回的结果
    private String param;//客户端调用方法时，传递的参数

    /**
     * 被代理对象调用，发送数据给服务区，->wait 等待被唤醒（被channelRead） -> 返回结果
     *
     * @return
     * @throws Exception
     */
    @Override
    public synchronized Object call() throws Exception {
        channelHandlerContext.writeAndFlush(param);
        wait();
        return result;
    }

    /**
     * 与服务器连接创建成功后就会调用
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        this.channelHandlerContext = ctx;
    }

    /**
     * 收到服务器的数据后，就会调用
     *
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    public synchronized void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        this.result = msg.toString();
        notify();//唤醒等待的线程
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("异常发生!");
        ctx.close();
    }

    public void setParam(String _param) {
        param = _param;
    }
}
