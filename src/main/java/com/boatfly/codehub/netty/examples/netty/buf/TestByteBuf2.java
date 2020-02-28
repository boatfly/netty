package com.boatfly.codehub.netty.examples.netty.buf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.util.CharsetUtil;

public class TestByteBuf2 {
    public static void main(String[] args) {
        //创建一个对象，包含一个数组arr，是一个byte[10]
        ByteBuf buf = Unpooled.copiedBuffer("武汉加油！", CharsetUtil.UTF_8);


    }
}
