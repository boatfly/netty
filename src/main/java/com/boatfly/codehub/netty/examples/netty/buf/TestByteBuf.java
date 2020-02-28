package com.boatfly.codehub.netty.examples.netty.buf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class TestByteBuf {
    public static void main(String[] args) {
        //创建一个对象，包含一个数组arr，是一个byte[10]
        ByteBuf buf = Unpooled.buffer(10);

        for (int i = 0; i < 10; i++) {
            buf.writeByte(i);
        }

        //相对于NIO ByteBuffer，不需要用flip进行反转
        //底层维护了 readindex writeindex
        // writeindex == limit
        for (int i = 0; i < 10; i++) {
            System.out.println(buf.getByte(i));//用index方式get，不会引起readindex变化
        }

        for (int i = 0; i < 10; i++) {
            System.out.println(buf.readByte());//会造成readindex的变化
        }
    }
}
