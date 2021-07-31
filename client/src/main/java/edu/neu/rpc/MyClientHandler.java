package edu.neu.rpc;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.AttributeKey;
import lombok.extern.slf4j.Slf4j;

/**
 * @author 201
 */
@Slf4j
public class MyClientHandler extends SimpleChannelInboundHandler<RpcResponse> {
    /**
     * 客户端接受到的信息存放到 ctx.channel().attr(key).set(msg);
     * @param ctx
     * @param msg
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcResponse msg)  {
        AttributeKey<RpcResponse> key = AttributeKey.valueOf("rpcResponse");
        ctx.channel().attr(key).set(msg);
        ctx.channel().close();
    }
}