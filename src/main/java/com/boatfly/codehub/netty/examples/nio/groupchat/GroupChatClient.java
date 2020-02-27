package com.boatfly.codehub.netty.examples.nio.groupchat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Scanner;

public class GroupChatClient {
    //定义相关属性
    private static final String HOST = "127.0.0.1";
    private static final int PORT = 9998;
    private Selector selector;
    private SocketChannel socketChannel;
    private String clientname;

    public GroupChatClient() throws IOException {
        selector = Selector.open();
        //连接服务器
        socketChannel = SocketChannel.open(new InetSocketAddress(HOST, PORT));
        socketChannel.configureBlocking(false);
        //注册selector
        socketChannel.register(selector, SelectionKey.OP_READ);

        clientname = socketChannel.getLocalAddress().toString().substring(1);

        System.out.println(clientname + " is ok...");
    }

    //向服务器发送消息
    public void send(String msg) {
        msg = clientname + " 说：" + msg;

        try {
            socketChannel.write(ByteBuffer.wrap(msg.getBytes()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //读取从服务器端回复的消息
    public void receive() {
        try {
            int readChannels = selector.select();
            if (readChannels > 0) {
                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                while (iterator.hasNext()) {
                    SelectionKey next = iterator.next();
                    if (next.isReadable()) {
                        SocketChannel channel = (SocketChannel) next.channel();
                        //得到一个buffer
                        ByteBuffer buffer = ByteBuffer.allocate(1024);
                        //读数据
                        channel.read(buffer);
                        //把从缓冲区读到的数据转成字符串
                        String msg = new String(buffer.array());
                        System.out.println(msg.trim());
                    }
                    iterator.remove();
                }
            } else {
                System.out.println("没有可以用的通道...");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        GroupChatClient client = new GroupChatClient();

        //启动一个线程，每个3s从服务器端读取数据
        new Thread(() -> {
            client.receive();
            try {
                Thread.currentThread().sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

        //发送数据给服务器端
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String msg = scanner.nextLine();
            client.send(msg);
        }

    }
}
