package com.boatfly.codehub.netty.io.bio;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * BIO
 * 其编程模型：
 * 1.服务器端启动一个ServerSocket
 * 2.客户端启动Socket对服务器进行通信，默认情况下服务器端会对每个客户建立一个线程与之通讯。
 * 3.客户端发出请求后，先咨询服务器是否有线程响应，如果没有，则会等待或被决绝；如果有响应，客户端线程会等待请求结束后，再继续执行。
 */
public class TestBlockingIO {
    public static void main(String[] args) throws IOException {
        ExecutorService pool = Executors.newCachedThreadPool();
        ServerSocket serverSocket = new ServerSocket(9898);
        System.out.println("server is running!");

        while (true) {
            //监听，等待客户端连接
            final Socket client = serverSocket.accept();//blocking...
            System.out.println("a client is up!");
            //创建一个线程，与之通讯
            pool.submit(() -> {
                handler(client);
            });
        }
    }

    //编写handler方法，和client通讯
    public static void handler(Socket socket) {
        try {
            System.out.println("thread-"+Thread.currentThread().getId());
            //通过socket获取输入流
            InputStream inputStream = socket.getInputStream();
            byte[] bytes = new byte[1024];
            //循环读取client发送过来的数据
//            while (true){
//                int len = inputStream.read(bytes);
//                if(len!=-1){
//                    System.out.println(new String(bytes,0,len));
//                }else{
//                    break;
//                }
//            }
            int len = 0;
            while ((len = inputStream.read(bytes)) != -1) {//blocking...
                System.out.println("thread-"+Thread.currentThread().getId());
                System.out.println(new String(bytes, 0, len));
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            System.out.println("close a client!");
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
