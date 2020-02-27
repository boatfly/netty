package com.boatfly.codehub.netty.io.nio;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class NIOFileChannelWrite {
    public static void main(String[] args) throws IOException {
        File file = new File("out/NIOFileChannel.txt");
        FileInputStream fileInputStream = new FileInputStream(file);

        FileChannel channel = fileInputStream.getChannel();

        ByteBuffer buffer = ByteBuffer.allocate((int)file.length());

        channel.read(buffer);

        System.out.println(new String(buffer.array()));

        fileInputStream.close();
    }
}
