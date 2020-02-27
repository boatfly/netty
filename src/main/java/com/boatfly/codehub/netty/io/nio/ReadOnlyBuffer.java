package com.boatfly.codehub.netty.io.nio;

import org.junit.Test;

import java.nio.ByteBuffer;

public class ReadOnlyBuffer {
    @Test
    public void test() {
        ByteBuffer buf = ByteBuffer.allocate(64);
        for (int i = 0; i <64 ; i++) {
            buf.put((byte)i);
        }
        buf.flip();
        // 得到一个只读的buffer
        ByteBuffer readOnlyBuffer = buf.asReadOnlyBuffer();
        System.out.println(readOnlyBuffer.getClass()); //java.nio.HeapByteBufferR

        while (readOnlyBuffer.hasRemaining()){
            System.out.println(readOnlyBuffer.get());
        }

        readOnlyBuffer.put((byte)120); // java.nio.ReadOnlyBufferException
    }
}
