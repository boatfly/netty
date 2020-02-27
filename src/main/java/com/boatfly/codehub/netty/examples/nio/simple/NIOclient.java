package com.boatfly.codehub.netty.examples.nio.simple;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class NIOclient {
    public static void main(String[] args) throws IOException {
        //得到一个网络通道
        SocketChannel socketChannel = SocketChannel.open();
        //设置非阻塞
        socketChannel.configureBlocking(false);

        //服务端的ip和端口
        InetSocketAddress inetSocketAddress = new InetSocketAddress("127.0.0.1", 9898);

        //连接服务器
        if(!socketChannel.connect(inetSocketAddress)){
            while (!socketChannel.finishConnect()){
                System.out.println("......");
            }
        }
        //连接成功，发送数据
        String str = "武汉加油！";
//        ByteBuffer buffer = ByteBuffer.allocate(str.length());
//        buffer.put(str.getBytes());
        ByteBuffer buffer = ByteBuffer.wrap(str.getBytes());//包裹，同时写入buffer

        //发送数据
        socketChannel.write(buffer);

        //client is staying
        System.in.read();
    }
}
