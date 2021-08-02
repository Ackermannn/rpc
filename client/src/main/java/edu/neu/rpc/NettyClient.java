package edu.neu.rpc;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.util.AttributeKey;
import lombok.extern.slf4j.Slf4j;


/**
 * create time: 2021/7/31 下午 3:54
 *
 * @author DownUpZ
 */
@Slf4j
public class NettyClient {
    private final String host;
    private final int port;
    private static final Bootstrap BOOTSTRAP;

    static {
        NioEventLoopGroup group = new NioEventLoopGroup();
        BOOTSTRAP = new Bootstrap();
        BOOTSTRAP.group(group)
                //设置客户端的通道实现类型
                .channel(NioSocketChannel.class)
                .option(ChannelOption.SO_KEEPALIVE, true)
                //使用匿名内部类初始化通道
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        //添加客户端通道的处理器
                        ChannelPipeline pipeline = ch.pipeline();
//                        pipeline.addLast(new ObjectDecoder(ClassResolvers.weakCachingConcurrentResolver(null)));
                        pipeline.addLast(new CommonDecoder());
//                        pipeline.addLast(new ObjectEncoder());
                        pipeline.addLast(new CommonEncoder(new JsonSerializer()));
                        pipeline.addLast(new MyClientHandler());
                    }
                });
        log.info("客户端 bootstrap 准备就绪，随时可以起飞~");
    }

    public NettyClient(String host, int port) {
        this.host = host;
        this.port = port;
    }


    public RpcResponse<Object> send(RpcRequest rpcRequest) {
        try {
            ChannelFuture future = BOOTSTRAP.connect(host, port).sync();
            log.info("客户端连接到服务器 {}:{}", host, port);
            Channel channel = future.channel();
            channel.writeAndFlush(rpcRequest).addListener(future1 -> {
                if (future1.isSuccess()) {
                    log.info(String.format("客户端发送消息: %s", rpcRequest.toString()));
                } else {
                    log.error("发送消息时有错误发生: ", future1.cause());
                }
            });
            channel.closeFuture().sync();
            AttributeKey<RpcResponse<Object>> key = AttributeKey.valueOf("rpcResponse");
            return channel.attr(key).get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

}
