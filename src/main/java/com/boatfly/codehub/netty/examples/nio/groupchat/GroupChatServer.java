package com.boatfly.codehub.netty.examples.nio.groupchat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;

public class GroupChatServer {
    //定义属性
    private Selector selector;
    private ServerSocketChannel listenChannel;
    private static final int PORT = 9998;

    private GroupChatServer() {
        try {
            //得到选择器
            selector = Selector.open();
            //初始化ServerSocketChannel
            listenChannel = ServerSocketChannel.open();
            listenChannel.configureBlocking(false);

            //绑定端口
            listenChannel.bind(new InetSocketAddress(PORT));

            listenChannel.register(selector, SelectionKey.OP_ACCEPT);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //监听
    public void lisen() {
        try {
            //循环处理
            while (true) {
                if (selector.select() > 0) { //有事件处理
                    //遍历得到的selectionkey集合
                    Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                    while (iterator.hasNext()) {
                        SelectionKey next = iterator.next();

                        if (next.isAcceptable()) {
                            SocketChannel socketChannel = listenChannel.accept();
                            socketChannel.configureBlocking(false);
                            socketChannel.register(selector, SelectionKey.OP_READ);
                            //提示
                            System.out.println(socketChannel.getRemoteAddress() + "上线！");
                        }

                        if (next.isReadable()) { //通道可读
                            SocketChannel clientChannel = (SocketChannel) next.channel();
                            //处理读事件
                            readData(next);
                        }

                        iterator.remove();
                    }
                } else {
                    System.out.println("等待...");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
    }

    //读取客户端消息
    private void readData(SelectionKey key) {
        SocketChannel socketChannel = null;
        try {
            socketChannel = (SocketChannel) key.channel();
            // 创建buffer
            ByteBuffer buffer = ByteBuffer.allocate(1024);

            int len = socketChannel.read(buffer);
            //根据len的值做处理
            if (len > 0) {
                //把缓冲区的数据转为字符串
                String msg = new String(buffer.array());
                System.out.println("from 客户端：" + msg);

                //向其他的客户端转发消息
                sendMsg2OhterClients(msg,socketChannel);
            }

        } catch (IOException e) {
            try {
                System.out.println(socketChannel.getRemoteAddress()+"离线了！");
                //取消注册
                key.cancel();
                //关闭通道
                socketChannel.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    //转发消息给其他客户端（通道）-去掉自己
    public void sendMsg2OhterClients(String msg, SocketChannel self) throws IOException {
        System.out.println("服务器转发消息中...");
        //遍历所有注册到selector上的socketchannel，并排除自己
        for (SelectionKey key : selector.keys()) {
            //获取channle
            Channel targetChannel = key.channel();
            //排除自己
            if (targetChannel instanceof SocketChannel && targetChannel != self) {
                SocketChannel socketChannel = (SocketChannel)targetChannel;
                //将msg存储到buffer
                ByteBuffer buffer = ByteBuffer.wrap(msg.getBytes());
                //将buffer数据写入通道
                socketChannel.write(buffer);
            }
        }
    }

    public static void main(String[] args) {
        //创建服务器对象
        GroupChatServer groupChatServer = new GroupChatServer();
        groupChatServer.lisen();
    }
}
