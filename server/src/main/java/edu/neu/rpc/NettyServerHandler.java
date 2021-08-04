package edu.neu.rpc;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * create time: 2021/7/31 下午 2:14
 *
 * @author DownUpZ
 */
@Slf4j
public class NettyServerHandler extends SimpleChannelInboundHandler<RpcRequest> {
    private ServiceProviderImpl serviceProviderImpl;

    NettyServerHandler(ServiceProviderImpl serviceProviderImpl) {
        this.serviceProviderImpl = serviceProviderImpl;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcRequest msg) {
        log.info("收到客户端" + ctx.channel().remoteAddress() + "发送的消息：" + msg.toString());
        RpcResponse<?> rpcResponse = serviceProviderImpl.runService(msg);
        ctx.writeAndFlush(rpcResponse);
        log.info("服务器开始返回一个消息: " + rpcResponse);
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        //发生异常，关闭通道
        cause.printStackTrace();
        ctx.close();
    }
}
