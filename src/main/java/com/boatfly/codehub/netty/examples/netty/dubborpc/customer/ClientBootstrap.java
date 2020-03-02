package com.boatfly.codehub.netty.examples.netty.dubborpc.customer;

import com.boatfly.codehub.netty.examples.netty.dubborpc.netty.NettyClient;
import com.boatfly.codehub.netty.examples.netty.dubborpc.service.IBoatService;

public class ClientBootstrap {
    //定义协议头
    private static final String provicerName = "BoatService#echo#";

    public static void main(String[] args) {
        //创建一个消费者
        NettyClient customer = new NettyClient();
        //创建代理对象
        IBoatService service = (IBoatService) customer.getBean(IBoatService.class, provicerName);

        String result = service.echo("sahoo!");

        System.out.println("from server result:" + result);
    }
}
