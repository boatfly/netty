package com.boatfly.codehub.netty.examples.netty.groupchat;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.util.Scanner;

public class GroupChatClient {
    private final String host;
    private final int prot;

    public GroupChatClient(String _host, int _port) {
        this.host = _host;
        this.prot = _port;
    }

    public void run() {
        NioEventLoopGroup eventExecutors = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap().group(eventExecutors)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast("decoder", new StringDecoder())//解码器
                                    .addLast("encoder", new StringEncoder())//编码器
                                    .addLast(new GroupChatClientHandler());//加入自用的业务处理handler
                        }
                    });
            ChannelFuture connectFuture = bootstrap.connect(host, prot).sync();

            Channel channel = connectFuture.channel();
            System.out.println("---------------" + channel.remoteAddress() + "---------------");

            //客户端需要输入信息
            Scanner scanner = new Scanner(System.in);
            while (scanner.hasNextLine()){
                String s = scanner.nextLine();
                //通过channel发送信息到服务器端
                channel.writeAndFlush(s+"\r\n");
            }

            ChannelFuture closeFuture = connectFuture.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            eventExecutors.shutdownGracefully();
        }


    }

    public static void main(String[] args) {
        new GroupChatClient("127.0.0.1",9990).run();
    }
}
