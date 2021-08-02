package edu.neu.rpc;

/**
 * create time: 2021/7/31 下午 12:53
 *
 * @author DownUpZ
 *//*
作者：阿里云云栖号
        链接：https://zhuanlan.zhihu.com/p/181239748
        来源：知乎
        著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
*/

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

/**
 * 自定义的Handler需要继承Netty规定好的HandlerAdapter
 * 才能被Netty框架所关联，有点类似SpringMVC的适配器模式
 *
 * @author 201
 */
public class MyServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        //获取客户端发送过来的消息
        RpcRequest o = (RpcRequest) msg;
        System.out.println("收到客户端" + ctx.channel().remoteAddress() + "发送的消息：" + o.toString());
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        //发送消息给客户端
        RpcResponse<String> response = new RpcResponse<>();
        response.setData("返回");
        ctx.writeAndFlush(response);
        System.out.println("channelReadComplete 中 服务器成功返回信息");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        //发生异常，关闭通道
        ctx.close();
    }
}
