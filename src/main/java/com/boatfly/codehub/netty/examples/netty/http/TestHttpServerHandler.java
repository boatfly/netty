package com.boatfly.codehub.netty.examples.netty.http;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

import java.net.URI;

/**
 * 说明：
 * 1.SimpleChannelInboundHandler 继承 ChannelInboundHandlerAdapter
 * 2.HttpObject 客户端和服务器端相互通信的数据被封装成的类型
 */
public class TestHttpServerHandler extends SimpleChannelInboundHandler<HttpObject> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HttpObject msg) throws Exception {
        //判断msg是否是一个httprequest请求
        if (msg instanceof HttpRequest) {
            System.out.println("msg 类型：" + msg.getClass());
            System.out.println("客户端地址：" + ctx.channel().remoteAddress());

            //过滤uri

            HttpRequest request = (HttpRequest) msg;
            URI uri = new URI(request.uri());
            if ("/someuri".equals(uri.getPath())) {
                System.out.println("someuri is request,ignore it...");
                return;
            }

            //回复信息给浏览器[http协议]
            ByteBuf buf = Unpooled.copiedBuffer("hello,i am server!武汉加油！", CharsetUtil.UTF_8);

            //构造一个http的响应，即httpresponse
            FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, buf);

            response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain;charset=utf-8");
            response.headers().set(HttpHeaderNames.CONTENT_LENGTH, buf.readableBytes());

            //将构建好的response返回
            ctx.channel().writeAndFlush(response);
        }
    }
}
