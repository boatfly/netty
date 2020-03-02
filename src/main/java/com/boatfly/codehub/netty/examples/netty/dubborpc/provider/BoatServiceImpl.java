package com.boatfly.codehub.netty.examples.netty.dubborpc.provider;

import com.boatfly.codehub.netty.examples.netty.dubborpc.service.IBoatService;

public class BoatServiceImpl implements IBoatService {

    private static int count = 0;

    @Override
    public String echo(String msg) {
        System.out.println("收到客户端消息：" + msg);
        if (msg != null) {
            return "echo to you a message:[" + msg + "] 第" + ++this.count + "次调用";
        } else
            return "echo to you a message";
    }
}
