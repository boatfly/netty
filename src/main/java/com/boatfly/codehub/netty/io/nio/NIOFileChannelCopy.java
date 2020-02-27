package com.boatfly.codehub.netty.io.nio;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class NIOFileChannelCopy {
    public static void main(String[] args) throws IOException {
        File file = new File("out/NIOFileChannel.txt");
        FileInputStream fileInputStream = new FileInputStream(file);

        FileChannel channel01 = fileInputStream.getChannel();

        FileOutputStream outputStream = new FileOutputStream("out/NIOFileChannel3.txt");
        FileChannel channel02 = outputStream.getChannel();

        //channel01.transferTo(0,channel01.size(),channel02);

        ByteBuffer buffer = ByteBuffer.allocate(1024);
        int len=0;
        while ((len=channel01.read(buffer))!=-1){
            buffer.flip(); //***
            channel02.write(buffer);
            buffer.clear(); //***
        }

        fileInputStream.close();
        outputStream.close();
    }
}
