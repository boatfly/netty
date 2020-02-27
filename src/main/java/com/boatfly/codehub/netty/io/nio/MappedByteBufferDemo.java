package com.boatfly.codehub.netty.io.nio;

import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * MappedByteBuffer
 * - 可以直接让文件在物理内存中修改，避免copy一次（物理内存-jvm堆内存）
 */
public class MappedByteBufferDemo {
    @Test
    public void test() throws IOException {
        RandomAccessFile randomAccessFile = new RandomAccessFile("out/NIOFileChannel.txt","rw");

        FileChannel channel = randomAccessFile.getChannel();
        /**
         * 读写模式
         * 可以直接修改的起始位置
         * 映射到内存的大小，即将文件的多少个字节映射到内存
         */
        MappedByteBuffer map = channel.map(FileChannel.MapMode.READ_WRITE, 0, 5);

        map.put(0,(byte)'a');
        map.put(3,(byte)'a');

        randomAccessFile.close();
    }
}
