package com.boatfly.codehub.netty.examples.netty.dubborpc.provider;

import com.boatfly.codehub.netty.examples.netty.dubborpc.netty.NettyServer;

public class ServerBootstrap {
    public static void main(String[] args) {
        NettyServer.startserver("127.0.0.1", 9990);
    }
}
