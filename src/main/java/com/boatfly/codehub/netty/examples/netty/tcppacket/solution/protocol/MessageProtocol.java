package com.boatfly.codehub.netty.examples.netty.tcppacket.solution.protocol;

import lombok.Data;

/**
 * 协议包
 *
 */
@Data
public class MessageProtocol {
    private int len; //关键
    private byte[] content;
}
