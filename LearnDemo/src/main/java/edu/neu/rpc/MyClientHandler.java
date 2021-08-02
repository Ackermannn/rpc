package edu.neu.rpc;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;


/*
作者：阿里云云栖号
        链接：https://zhuanlan.zhihu.com/p/181239748

*/
@Data
@Slf4j
public class MyClientHandler extends ChannelInboundHandlerAdapter {
    private RpcResponse<Object> response;
    @Override
    public void channelActive(ChannelHandlerContext ctx)  {
        //发送消息到服务端
//        RpcRequest rr = new RpcRequest();
//        rr.setInterfaceName("131221");
//        ctx.writeAndFlush(rr);
    }

    /**
     * 这个必须用啊，当收到对方发来的数据后，就会触发，参数msg就是发来的信息，可以是基础类型，也可以是序列化的复杂对象。
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //接收服务端发送过来的消息
//        RpcRequest byteBuf = () msg;
        response = (RpcResponse<Object>) msg;
        // 在这里执行下一步运行逻辑不就行了
        System.out.println("收到服务端" + ctx.channel().remoteAddress() + "的消息：" + msg.toString());
    }
}