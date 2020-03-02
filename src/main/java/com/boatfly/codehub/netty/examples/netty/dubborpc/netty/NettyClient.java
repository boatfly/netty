package com.boatfly.codehub.netty.examples.netty.dubborpc.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.lang.reflect.Proxy;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NettyClient {

    public static ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    public static NettyClientHandler client = null;

    //创建代理对象
    public Object getBean(final Class<?> serverClass, final String providerName) {
        return Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), new Class<?>[]{serverClass}, (proxy, method, args) -> {
            if (client == null) {
                initClient();
            }

            client.setParam(providerName + args[0]);

            return executor.submit(client).get();
        });
    }

    public static void initClient() {
        client = new NettyClientHandler();
        NioEventLoopGroup eventExecutors = new NioEventLoopGroup();

        try {
            Bootstrap bootstrap = new Bootstrap();

            bootstrap.group(eventExecutors).channel(NioSocketChannel.class).option(ChannelOption.TCP_NODELAY, true).handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    ChannelPipeline pipeline = ch.pipeline();
                    pipeline.addLast(new StringDecoder());
                    pipeline.addLast(new StringEncoder());
                    pipeline.addLast(client);
                }
            });

            bootstrap.connect("127.0.0.1", 9990).sync();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
