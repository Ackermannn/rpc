package edu.neu.rpc;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * create time: 2021/8/2 下午 4:02
 * <p>
 * CommonEncoder 继承了MessageToByteEncoder 类，见名知义，就是把 Message（实际要发送的对象）转化成 Byte 数组。
 * CommonEncoder 的工作很简单，就是把 RpcRequest 或者 RpcResponse 包装成协议包。
 * 根据上面提到的协议格式，将各个字段写到管道里就可以了，这里serializer.getCode() 获取序列化器的编号，
 * 之后使用传入的序列化器将请求或响应包序列化为字节数组写入管道即可。
 * <p>
 * 原文链接：https://blog.csdn.net/qq_40856284/article/details/107751877
 *
 * @author DownUpZ
 */
public class CommonEncoder extends MessageToByteEncoder {
    private static final int MAGIC_NUMBER = 0xCAFEBABE;

    private final CommonSerializer serializer;

    public CommonEncoder(CommonSerializer serializer) {
        this.serializer = serializer;
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {
        // 写入魔数
        out.writeInt(MAGIC_NUMBER);

        // 写入PackageType ( 区别请求还是接收)
        if (msg instanceof RpcRequest) {
            out.writeInt(PackageType.REQUEST_PACK.getCode());
        } else {
            out.writeInt(PackageType.RESPONSE_PACK.getCode());
        }

        // 序列化器类型 Serializer Type
        out.writeInt(serializer.getCode());

        // 数据长度
        byte[] bytes = serializer.serialize(msg);
        out.writeInt(bytes.length);

        out.writeBytes(bytes);
    }
}
