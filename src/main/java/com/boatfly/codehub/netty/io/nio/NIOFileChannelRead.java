package com.boatfly.codehub.netty.io.nio;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class NIOFileChannelRead {
    public static void main(String[] args) throws IOException {
        String str = "武汉加油！中国加油！";
        FileOutputStream outputStream = new FileOutputStream("out/NIOFileChannel.txt");

        // 获取通道
        FileChannel channel = outputStream.getChannel();

        // 创建缓冲区
        ByteBuffer buf = ByteBuffer.allocate(1024);

        buf.put(str.getBytes());

        // 对ByteBuffer进行反转
        buf.flip();

        channel.write(buf);

        //关闭
        //channel.close();
        outputStream.close();

    }
}
