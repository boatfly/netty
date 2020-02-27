package com.boatfly.codehub.netty.io.nio;

import java.nio.IntBuffer;

/**
 * IntBuffer
 * ...
 * ByteBuffer
 *
 * - mark
 * - position
 * - limit
 * - capacity
 *
 * 0<=mark<=position<=limit<=capacity
 */
public class BasicBuffer {
    public static void main(String[] args) {
        IntBuffer buf = IntBuffer.allocate(5);
        for (int i = 0; i <buf.capacity() ; i++) {
            buf.put(i*2);
        }
        buf.flip();//读写模式切换
        while (buf.hasRemaining()){
            System.out.println(buf.get());
        }
    }
}
