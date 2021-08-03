package edu.neu.rpc;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;

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
        try {
            log.info("收到客户端" + ctx.channel().remoteAddress() + "发送的消息：" + msg.toString());
            Class<?> aClass = serviceProviderImpl.getServiceProvider(Class.forName(msg.getInterfaceName()));
            Object o = aClass.newInstance();
            Method method = aClass.getDeclaredMethod(msg.getMethodName(), msg.getParamTypes());
            Object result = method.invoke(o, msg.getParameters());

            RpcResponse<Object> rpcResponse = new RpcResponse<>();
            rpcResponse.setData(result);
            rpcResponse.setMessage("SUCCESS");
            log.info("服务：{} 成功执行方法 {}", msg.getInterfaceName(), msg.getMethodName());
            ctx.writeAndFlush(rpcResponse);
            log.info("服务器开始返回一个消息: " + rpcResponse);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        //发生异常，关闭通道
        cause.printStackTrace();
        ctx.close();
    }
}
