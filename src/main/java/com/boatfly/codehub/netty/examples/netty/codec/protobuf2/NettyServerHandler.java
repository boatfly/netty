package com.boatfly.codehub.netty.examples.netty.codec.protobuf2;

import com.boatfly.codehub.netty.examples.netty.codec.protobuf.StudentPOJO;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

/**
 * 自定义一个Handler,需要继承netty设定好的某个HandlerAdapter（规范）
 */
public class NettyServerHandler extends SimpleChannelInboundHandler<MyDataInfo.MyMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MyDataInfo.MyMessage msg) throws Exception {
        //读取从客户端发送的MyDataInfo.MyMessage对象
        //根据data_type来确定发送过来的数据时Student 还是 Worker
        MyDataInfo.MyMessage.DataType dataType = msg.getDataType();
        if (dataType == MyDataInfo.MyMessage.DataType.StudentType) {
            MyDataInfo.Student student = msg.getStudent();
            System.out.println("student's name=" + student.getName());
        } else if (dataType == MyDataInfo.MyMessage.DataType.WorkerType) {
            MyDataInfo.Worker worker = msg.getWorker();
            System.out.println("worker's name=" + worker.getName());
        } else {
            System.out.println("类型不正确");
        }
    }

    /**
     * 读取数据事件，可以获取客户端发送的消息
     *
     * @param ctx 上下文
     * @param msg 消息
     * @throws Exception
     */
//    @Override
//    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//        //读取从客户端发送的StudentPOJO.Student对象
//        StudentPOJO.Student student = (StudentPOJO.Student) msg;
//        System.out.println("客户端发送的数据,id=" + student.getId());
//        System.out.println("客户端发送的数据,name=" + student.getName());
//    }

    /**
     * 读完毕
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        //对发送数据进行编码
        ctx.writeAndFlush(Unpooled.copiedBuffer("hello,client,武汉加油！", CharsetUtil.UTF_8));
    }

    /**
     * 异常处理，一般需要关闭通道
     *
     * @param ctx
     * @param cause
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.channel().close();
    }
}
