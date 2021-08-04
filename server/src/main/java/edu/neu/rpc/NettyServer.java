package edu.neu.rpc;

import edu.neu.rpc.serializer.KryoSerializer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;

/**
 * create time: 2021/7/31 下午 2:09
 *
 * @author DownUpZ
 */
@Slf4j
public class NettyServer extends AbstractRpcServer{

    public NettyServer(String host, int port) {
        super(host, port);
    }

    @Override
    public void start() {

        //创建两个线程组 boosGroup、workerGroup
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            //创建服务端的启动对象，设置参数
            ServerBootstrap bootstrap = new ServerBootstrap();
            //设置两个线程组boosGroup和workerGroup
            bootstrap.group(bossGroup, workerGroup)
                    //设置服务端通道实现类型
                    .channel(NioServerSocketChannel.class)
                    //设置线程队列得到连接个数
                    .option(ChannelOption.SO_BACKLOG, 128)
                    //设置保持活动连接状态
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    //使用匿名内部类的形式初始化通道对象
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            //给pipeline管道设置处理器
                            ChannelPipeline pipeline = ch.pipeline();
//                            pipeline.addLast(new ObjectEncoder());
                            pipeline.addLast(new CommonEncoder(new KryoSerializer()));
//                            pipeline.addLast(new ObjectDecoder(ClassResolvers.weakCachingConcurrentResolver(null)));
                            pipeline.addLast(new CommonDecoder());
                            pipeline.addLast(new NettyServerHandler(serviceProviderImpl));
                        }
                    });//给workerGroup的EventLoop对应的管道设置处理器
            log.info("Netty服务端已经准备就绪..., 端口号:" + port);
            //绑定端口号，启动服务端
            ChannelFuture channelFuture = bootstrap.bind(this.host, this.port).sync();

            // 创建钩子进程当服务器关闭时注销注册
            serviceRegistry.clearRegistry();

            //对关闭通道进行监听
            channelFuture.channel().closeFuture().sync();
        }catch (Exception e){
            e.printStackTrace();
        }
        finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

}
