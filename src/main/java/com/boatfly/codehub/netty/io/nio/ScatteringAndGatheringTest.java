package com.boatfly.codehub.netty.io.nio;

import org.junit.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Arrays;

/**
 * Scattering: 将数据写入buffer时，可以采用buffer数组，依次写入
 * Gathering：从buffer读数据时，可以采用buffer数组，依次读
 */
public class ScatteringAndGatheringTest {
    public static void main(String[] args) throws IOException {
        ServerSocketChannel ssChannel = ServerSocketChannel.open();

        //绑定端口
        ssChannel.bind(new InetSocketAddress(9898));

        //创建buffer数组
        ByteBuffer[] byteBuffers = new ByteBuffer[2];

        byteBuffers[0] = ByteBuffer.allocate(5);
        byteBuffers[1] = ByteBuffer.allocate(3);

        SocketChannel client = ssChannel.accept();
        int messageLen = 8; //假定从客户端接收8个字节
        while (true) {

            int byteRead = 0;

            while (byteRead < messageLen) {
                long read = client.read(byteBuffers);
                byteRead += read;//累计读取
                System.out.println("byteRead=" + byteRead);
                Arrays.asList(byteBuffers).stream().map(b -> "position:" + b.position() + ",limit:" + b.limit()).forEach(System.out::println);
            }

            // 将所有的bufferi进行flip反战
            Arrays.asList(byteBuffers).forEach(byteBuffer -> byteBuffer.flip());

            //将数据读出，显示到客户端
            int byteWrite = 0;
            while (byteWrite < messageLen) {
                long write = client.write(byteBuffers);
                byteWrite += write;
            }

            // 将所有的buffer进行clear
            Arrays.asList(byteBuffers).forEach(byteBuffer -> byteBuffer.clear());

            System.out.println("byteRead:" + byteRead + ",byteWrite:" + byteWrite);
        }


    }
}
