package edu.neu.rpc;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import lombok.extern.slf4j.Slf4j;

/**
 * create time: 2021/7/31 下午 12:54
 *
 * @author DownUpZ
 *//*
作者：阿里云云栖号
        链接：https://zhuanlan.zhihu.com/p/181239748
        来源：知乎
        著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
*/
@Slf4j
public class MyClient {

    public static void main(String[] args) throws Exception {
        MyClientHandler mch = new MyClientHandler();
        NioEventLoopGroup eventExecutors = new NioEventLoopGroup();
        try {
            //创建bootstrap对象，配置参数
            Bootstrap bootstrap = new Bootstrap();
            //设置线程组
            bootstrap.group(eventExecutors)
                    //设置客户端的通道实现类型
                    .channel(NioSocketChannel.class)
                    //使用匿名内部类初始化通道
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            //添加客户端通道的处理器
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast(new ObjectDecoder(ClassResolvers.weakCachingConcurrentResolver(null)));
                            pipeline.addLast(new ObjectEncoder());
                            pipeline.addLast(mch);
                        }
                    });
            System.out.println("客户端准备就绪，随时可以起飞~");
            //连接服务端
            ChannelFuture channelFuture = bootstrap.connect("127.0.0.1", 6666).sync();
            RpcRequest rpcRequest =  new RpcRequest();
            rpcRequest.setInterfaceName("123");
            Channel channel = channelFuture.channel();
            channel.writeAndFlush(rpcRequest);
            System.out.println("客户端已发送信息");
//            System.out.println("调试断点");
            //对通道关闭进行监听

//            System.out.println(mch.getResponse());
            channelFuture.channel().closeFuture().sync(); // 客户端在这里阻塞了，当服务端关闭才退出
        } finally {
            //关闭线程组
            eventExecutors.shutdownGracefully();
        }
    }
}
