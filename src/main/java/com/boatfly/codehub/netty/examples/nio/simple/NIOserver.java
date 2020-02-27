package com.boatfly.codehub.netty.examples.nio.simple;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;

public class NIOserver {
    public static void main(String[] args) throws IOException {
        ServerSocketChannel ssChannel = ServerSocketChannel.open();

        //得到一个selector
        Selector selector = Selector.open();

        //绑定端口
        ssChannel.bind(new InetSocketAddress(9898));

        //设置为非阻塞
        ssChannel.configureBlocking(false);

        // 注册到选择器 或者 多路复用器
        ssChannel.register(selector, SelectionKey.OP_ACCEPT);

        //循环等待客户端连接
        while (true) {
            if (selector.select(1000) == 0) { // 没有事件发生
                System.out.println("服务器等待了1s，无连接！");
                continue;
            }

            Iterator<SelectionKey> it = selector.selectedKeys().iterator();
            while (it.hasNext()) {
                SelectionKey next = it.next();
                if (next.isAcceptable()) { // 有新连接接入 OP_ACCEPT
                    //为客户端生成一个socketchannel
                    SocketChannel socketChannel = ssChannel.accept();
                    socketChannel.configureBlocking(false);
                    System.out.println("客户端连接成功！");
                    //将当前socketchannel也注册到selector上，关注事件为OP_READ，同时可以给channel关联一个Buffer
                    socketChannel.register(selector, SelectionKey.OP_READ, ByteBuffer.allocate(1024));
                }

                if(next.isReadable()){ //发生OP_READ 事件
                    SocketChannel client = (SocketChannel)next.channel();
                    //获取该channel关联的buffer
                    ByteBuffer buffer = (ByteBuffer) next.attachment();
                    client.read(buffer);
                    System.out.println("从客户端接收到数据："+new String(buffer.array()));
                    //buffer.clear();
                }

                //从当前集合中移除selectionkey，防止重复操作
                it.remove();
            }
        }

    }
}
