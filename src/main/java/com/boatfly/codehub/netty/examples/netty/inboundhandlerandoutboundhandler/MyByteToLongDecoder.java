package com.boatfly.codehub.netty.examples.netty.inboundhandlerandoutboundhandler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

public class MyByteToLongDecoder extends ByteToMessageDecoder {
    /**
     * decode 会根据接收到的数据，被调用多次，直到确定没有新的元素被添加到list
     * 或者ByteBuf 没有更多的可读字节w为止
     *
     * 如果list不为空，就会将list内容发送到下一个handler处理，因此该处理器也会被调用多次。
     * @param ctx
     * @param in  入站的ByteBuf
     * @param out 出站的List集合，传入到下一个Handler进行接着处理
     * @throws Exception
     */
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        System.out.println("MyByteToLongDecoder decoder being called!");
        //应为一个long是8个字节
        if (in.readableBytes() >= 8) {
            out.add(in.readLong());
        }
    }
}
